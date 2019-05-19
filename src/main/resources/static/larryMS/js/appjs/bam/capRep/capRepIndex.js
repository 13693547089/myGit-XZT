var prefix = "/capRep";
layui.use([ 'table', 'laydate', 'layer' ], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate = layui.laydate;
	var userType=$("#userType").val();
	var condition = {};
	initCapRepTable(condition);
	// 日期控件格式化
	laydate.render({
		elem : '#startDate'
	});
	laydate.render({
		elem : '#repMonth',
		type:"month"
	});
	laydate.render({
		elem : '#endDate'
	});
	// 新建按钮点击事件
	$("#addBtn").on('click', function() {
		var url = prefix + "/page/edit?type=1";
		tuoGo(url,"capRepEdit","capRepTable");
	});
	// 点击删除事件
	$("#delBtn").click(function() {
		var checkData = table.checkStatus('capRepTable').data;
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
		if (flag==false) {
			layer.msg(msg, {
				time : 1000
			});
			return;
		}
		deleteCapRep(ids);
	});
	// 搜索的点击事件
	$("#searchBtn").click(function() {
		var searchForm=$('#searchForm').serializeJSON();			   
		initCapRepTable(searchForm);
	});
	// 重置点击事件
	$("#resetBtn").click(function() {
		$("#searchForm")[0].reset();
	});
	// 表格监听事件
	table.on('tool(capRepTable)', function(obj) {
		var data = obj.data;
		var id = data.id;
		var ids = [];
		var status=data.status;
		ids.push(id);
		if (obj.event === 'edit') {
			if(status=='已提交'){
				layer.msg("已提交的计划不能编辑！", {
					time : 1000
				});
				return false;
			}
			var url = prefix + "/page/edit?id=" + id + "&type=2";
			tuoGo(url,"capRepEdit","capRepTable");
		} else if (obj.event === 'view') {
			var url = prefix + "/page/edit?id=" + id + "&type=3";
			tuoGo(url,"capRepEdit","capRepTable");
		} else if (obj.event === 'del') {
			if (data.status == '已提交') {
				layer.msg("已提交的计划不能删除！", {
					time : 1000
				});
				return;
			}
			deleteCapRep(ids);
		}
	});
	/**
	 * 删除制定的行
	 * 
	 * @returns
	 */
	function deleteCapRep(data) {
		layer.confirm('确认删除?', function(index) {
			var json = JSON.stringify(data);
			$.ajax({
				type : 'POST',
				url : prefix + "/del",
				data : {
					idStr : json
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
	function initCapRepTable(condition) {
		var str=$('#createSpan').html().trim();
		var operateBar="#operateBar";
		if( str==''){
			operateBar="#operateBar1";
		}
		table.render({
			elem : '#capRepTable',
			id : 'capRepTable',
			where : condition,
			url : prefix + '/list',
			page : true,
			cols : [ [ {
				type : 'checkbox',
			}, {
				type : 'numbers',
				title : '序号'
			}, {
				field : 'status',
				title : '上报状态',
				width:'8%',
			}, {
				field : 'capCode',
				width:'10%',
				title : '月报编号'
			}, {
				field : 'repMonth',
				title : '计划月份',
				width:'10%',
				templet : function(d) {
					return formatTime(d.repMonth, 'yyyy-MM');
				}
			}, {
				field : 'suppName',
				width:'20%',
				title : '包材供应商'
			}, {
				field : 'createrName',
				width:'20%',
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