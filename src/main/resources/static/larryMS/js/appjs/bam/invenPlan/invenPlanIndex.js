var prefix = "/invenPlan";
layui.use([ 'table', 'laydate', 'layer' ], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate = layui.laydate;
	var condition = {};
	initInvenPlanTable(condition);
	// 日期控件格式化
	laydate.render({
		elem : '#startDate'
	});
	laydate.render({
		elem : '#planMonth',
		type:"month"
	});
	laydate.render({
		elem : '#endDate'
	});
	// 新建按钮点击事件
	$("#addBtn").on('click', function() {
		var url = prefix + "/getEditPage?type=1";
//		window.location.href = url;
		tuoGo(url,"invenPlanEdit","invenPlanTable");
	});
	// 提交点击事件
	$("#submitBtn").click(function() {
		var checkData = table.checkStatus('invenPlanTable').data;
		var ids = [];
		var msg = "操作成功！";
		var flag = true;
		if (checkData.length <= 0) {
			layer.msg("请选择需要提交的数据！", {
				time : 1000
			});
			return;
		}
		for ( var i = 0; i < checkData.length; i++) {
			ids.push(checkData[i].id);
			if (checkData[i].status == '已提交') {
				msg = "已提交的数据请勿重复提交！";
				flag = false;
				break;
			}
		}
		if (!flag) {
			layer.msg(msg, {
				time : 1000
			});
			return;
		}
		var json = JSON.stringify(ids);
		$.post(prefix + "/getStatusNumsByMainIds", {
			status : '未分解',
			jsonIds : json
		}, function(res) {
			if (res > 0) {
				layer.msg('存在未分解的备货计划，请分解后再提交', {
					time : 1000
				});
			} else {
				$.ajax({
					url : prefix + "/changeInvenPlanStatus",
					data : {
						status : '已提交',
						jsonIds : json
					},
					success : function(res) {
						$("#searchBtn").click();
						layer.msg("操作成功！", {
							time : 1000
						});
					},
					error : function() {
						layer.msg("操作失败！", {
							time : 1000
						});
					}
				});
			}
		});
	});
	// 点击删除事件
	$("#delBtn").click(function() {
		var checkData = table.checkStatus('invenPlanTable').data;
		var ids = [];
		var flag = true;
		if (checkData.length <= 0) {
			layer.msg("请选择需要删除的数据！", {
				time : 1000
			});
			return;
		}
		for ( var i = 0; i < checkData.length; i++) {
			ids.push(checkData[i].id);
			if (checkData[i].status == '已提交') {
				msg = "已提交数据不能删除！";
				flag = false;
				break;
			}
		}
		deleteInvenPlan(ids);
	});
	$("#exportBtn").click(function() {

	});
	// 搜索的点击事件
	$("#searchBtn").click(function() {
		var planDesc = $("#planDesc").val();
		var status = $("#status").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var createUser=$('#createUser').val();
		var planMonth=$('#planMonth').val();
		condition.planDesc = planDesc;
		condition.status = status;
		condition.startDate = startDate;
		condition.endDate = endDate;
		condition.createUser = createUser;
		condition.planMonth = planMonth;
		initInvenPlanTable(condition);
	});
	// 重置点击事件
	$("#resetBtn").click(function() {
		$("#searchForm")[0].reset();
	});
	// 表格监听事件
	table.on('tool(invenPlanTable)', function(obj) {
		var data = obj.data;
		var id = data.id;
		var ids = [];
		var planMonth= formatTime(data.planMonth, 'yyyy-MM');
		ids.push(id);
		if (obj.event === 'edit') {
//			if(data.status != '已保存'){
//				layer.msg("只有已保存的数据才能编辑！", {
//					time : 1000
//				});
//				return;
//			}
			var url = prefix + "/getEditPage?id=" + id + "&type=2"+"&planMonth="+planMonth;
			tuoGo(url,"invenPlanEdit","invenPlanTable");
			//window.location.href = url;
		} else if (obj.event === 'view') {
			var url = prefix + "/getEditPage?id=" + id + "&type=3"+"&planMonth="+planMonth;
			tuoGo(url,"invenPlanEdit","invenPlanTable");
//			window.location.href = url;
		} else if (obj.event === 'del') {
			if (data.status == '已提交') {
				layer.msg("已提交的计划不能删除！", {
					time : 1000
				});
				return;
			}else if(data.status == '已审核'){
				layer.msg("已审核的计划不能删除！", {
					time : 1000
				});
				return;
			}
			deleteInvenPlan(ids);
		}
	});
	/**
	 * 删除制定的行
	 * 
	 * @returns
	 */
	function deleteInvenPlan(data) {
		layer.confirm('确认删除?', function(index) {
			var json = JSON.stringify(data);
			$.ajax({
				type : 'POST',
				url : prefix + "/delInvenPlan",
				data : {
					jsonIds : json
				},
				success : function(msg) {
					$("#searchBtn").click();
					layer.msg("操作成功！", {
						time : 1000
					});
				},
				error : function() {
					layer.msg("操作失败！", {
						time : 1000
					});
				}
			});
			layer.close(index);
		});
	}
	/**
	 * 初始化采购对账单表格
	 * 
	 * @param condition
	 * @returns
	 */
	function initInvenPlanTable(condition) {
		debugger;
		var str=$('#createSpan').html().trim();
		var operateBar="#operateBar";
		if( str==''){
			operateBar="#operateBar1";
		}
		table.render({
			elem : '#invenPlanTable',
			id : 'invenPlanTable',
			where : condition,
			url : prefix + '/getInvenPlanByPage',
			page : true,
			cols : [ [ {
				type : 'checkbox',
			}, {
				type : 'numbers',
				title : '序号'
			}, {
				field : 'status',
				title : '状态',
				width:'5%',
			}, {
				field : 'planMonth',
				title : '计划月份',
				width:'10%',
				templet : function(d) {
					return formatTime(d.planMonth, 'yyyy-MM');
				}
			}, {
				field : 'planCode',
				width:'13%',
				title : '计划编号'
			}, {
				field : 'planDesc',
				width:'30%',
				title : '计划名称'
			}, {
				field : 'createUser',
				width:'10%',
				title : '创建人'
			}, {
				field : 'createTime',
				width:'10%',
				title : '创建时间',
				templet : function(d) {
					return formatTime(d.createTime, 'yyyy-MM-dd');
				}
			}, {
				fixed : 'right',
				title : '操作',
				width:'15%',
				align : 'center',
				toolbar : operateBar
			} ] ]
		});
	}
});