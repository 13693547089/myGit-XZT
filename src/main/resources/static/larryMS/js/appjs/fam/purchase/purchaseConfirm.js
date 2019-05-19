var prefix = "/purchRecon";
layui.use([ 'table', 'laydate', 'layer' ], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate = layui.laydate;
	var condition = {};
	var json;
	initPurchReconTable(condition);
	//日期控件格式化
	 laydate.render({
		elem : '#startDate'
	 });
	 laydate.render({
		 elem : '#endDate'
	 });
	// 提交点击事件
	$("#confirmBtn").click(function() {
		var checkData = table.checkStatus('reconTable').data;
		var length = checkData.length;
		var reconCodeJson = [];
		var msg = "操作成功！";
		var flag = true;
		if (checkData.length <= 0) {
			layer.msg("请选择需要确认的数据！", {
				time : 1000
			});
			return;
		} 
		for(var i=0;i<checkData.length;i++){
			reconCodeJson.push(checkData[i].reconCode);
			if(checkData[i].reconStatus!='YTJ' && checkData[i].reconStatus!='YTH'){
				msg="请提交“已提交”,“已退回”状态的数据！";
				flag=false;
			}
		}
		if(!flag){
			layer.msg(msg,{time:1000});
			return ;
		}
		for(var i =0;i<length;i++){
			reconCodeJson=[];
			var reconCode = checkData[i].reconCode;
			reconCodeJson.push(reconCode);
			json = JSON.stringify(reconCodeJson);
			var id = checkData[i].id;
			var suppName = checkData[i].suppName;
			var createDate = checkData[i].createDate;
			//var format  = new Date(createDate).Format("yyyy-MM-dd");
			var remark = "采购对账审核: " + suppName ;
			var flag = taskProcess(id,"purcReconAudit", remark,"process");
			/*$.post(prefix + "/changeReconStatus", {
				status : 'YQR',
				reconCodeJson : json
			}, function(res) {
				layer.msg("操作成功！", {
					time : 1000
				});
				//$("#searchBtn").click();
			});*/
		}
		$("#searchBtn").click();
	});
	
	//在弹窗中选择执行人后，点击确认按钮回调
	   window.returnFunction = function() {
			$.post(prefix+"/changeReconStatus",{status:"DQR",reconCodeJson:json},function(res){
				layer.msg("操作成功！",{time:1000});
			});
	  }
	// 搜索的点击事件
	$("#searchBtn").click(function() {
		var reconCode = $("#reconCode").val();
		var status = $("#status").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		condition.reconCode = reconCode;
		condition.startDate = startDate;
		condition.endDate = endDate;
		condition.status = status;
		initPurchReconTable(condition);
	});
	// 重置点击事件
	$("#resetBtn").click(function() {
		$("#searchForm")[0].reset();
	});
	// 表格监听事件
	table.on('tool(reconTable)', function(obj) {
		var data = obj.data;
		var reconCode = data.reconCode;
		var reconCodes = [];
		reconCodes.push(reconCode);
		if (obj.event === 'edit') {
			var url = prefix + "/confirmPurchaseRecn?reconCode=" + reconCode
					+ "&type=1";
//			window.location.href = url;
			tuoGo(url,"purchaseConfirmDetail","reconTable");

		} else if (obj.event === 'del') {
			deleteRecon(reconCodes);
		} else if (obj.event == 'confirm') {
			var status = data.reconStatus
			if (status === "YQR" || status === "DQR") {
				layer.msg("待确认和已确认状态的对账单，不可再次确认！");
			} else {
				var url = prefix + "/confirmPurchaseRecn?reconCode="
						+ reconCode + "&type=2";
//				window.location.href = url;
				tuoGo(url,"purchaseConfirmDetail","reconTable");
			}
		}
	});
	/**
	 * 删除制定的行
	 * 
	 * @returns
	 */
	function deleteRecon(data) {
		if (data.length <= 0) {
			layer.msg("请选择需要删除的数据！", {
				time : 1000
			});
			return;
		}
		layer.confirm('确认删除?', function(index) {
			var json = JSON.stringify(data);
			$.ajax({
				type : 'POST',
				url : prefix + "/delQuotes",
				data : {
					codeJson : json
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
	function initPurchReconTable(condition) {
		table.render({
			elem : '#reconTable',
			id:'reconTable',
			where: condition,
			url  : prefix+'/getSuppPurchReconByPage',
			page : true,
			cols : [ [ {
				checkbox : true,
				fixed : 'center'
			}, {
				field : 'reconStatusDesc',
				width:'5%',
				title : '状态'
			}, {
				field : 'reconCode',
				width:'10%',
				title : '对账单号'
			}, {
				field : 'startDate',
				title : '对账日期从',
				width:'10%',
				templet : function(d) {
					return formatTime(d.startDate, 'yyyy-MM-dd');
				}
			}, {
				field : 'endDate',
				title : '对账日期至',
				width:'8%',
				templet : function(d) {
					return formatTime(d.endDate, 'yyyy-MM-dd');
				}
			}, {
				field : 'suppName',
				width:'20%',
				title : '供应商名称'
			}, {
				field : 'suppNo',
				width:'10%',
				title : '供应商编码'
			}, {
				field : 'creater',
				width:'9%',
				title : '创建人'
			}, {
				field : 'createTime',
				title : '创建时间',
				width:'9%',
				templet : function(d) {
					return formatTime(d.createTime, 'yyyy-MM-dd');
				}
			}, {
				fixed : 'right',
				title : '操作',
				width:'15%',
				align : 'center',
				toolbar : '#operateBar'
			} ] ]
		});
	}
});