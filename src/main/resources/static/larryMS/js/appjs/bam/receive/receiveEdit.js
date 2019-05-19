var tableData = [];
var table;
var receData;
var deliCode2;
var receId;
var funType2;
var oldDeliCode;

layui.use([ 'form', 'table','laydate' ], function() {
	var form = layui.form;
	table = layui.table;
	layer = layui.layer;
	var $ = layui.$;
	var laydate = layui.laydate;
	funType2 = $("#funType").val();
	deliCode2 = $("#deliCode").val();
	oldDeliCode = $("#deliCode").val();
	receId = $("#receId").val();
	 var suppr = $("#suppRange").val();
	  var supprd = $("#suppRangeDesc").val();
	  if(suppr != null && suppr != '' ){
		  $("#suppRange2").val(suppr +' - '+supprd);
	  }else{
		  $("#suppRange2").val('');
	  }
	laydate.render({
	    elem: '#receDate', //指定元素
 	});
	// 查看时不能编辑
	if (funType2 == '2') {
		$(".disa").attr("readonly", "readonly");
	}
	$.ajax({
		type : "post",
		url : "/queryReceMatesByReceId?receId=" + receId,
		dataType : "JSON",
		success : function(data) {
			if (data != null) {
				tableData = data;
				initTable(table, tableData);
				//渲染库位下拉框
				dealWithSelect(tableData);
			}
		},
		error : function() {
			layer.alert("Connection error");
		}
	});
	function dealWithSelect(tableData){
		debugger;
		var factoryAddrs=[];
		$.each(tableData,function(index,item) {
			var factoryAddr = item.factoryAddr;
			if(factoryAddr != null && factoryAddr != ''){
				factoryAddrs.push(factoryAddr);
			}
		});
		if(factoryAddrs.length == 0){
			layer.msg("物料没有工厂信息,导致无法选择库位");
			return;
		}
		$.ajax({
			type : "post",
			url : "/queryStorLocationList",
			data:{
				factoryAddrJson:JSON.stringify(factoryAddrs)
			},
			dataType : "JSON",
			async : true,
			success : function(data) {
				if(data.length>0){
					var str = '<option value ="" selected="selected">请选择</option>';
                    $.each(data,function(index,row){
                        str+='<option value = "' + row.dicCode + '">' + row.dicName + '</option>'
                    });
                    $("#storLocation").html(str);
                    //重新初始化下拉框
                    form.render('select');
				}else{
					layer.msg("未查询到库位信息");
				}
			}
		});
	}
	//库位下拉框触发
    form.on("select(storLocation)",function(obj){
        var storLocation = obj.value;
        if(storLocation == '' || storLocation == null || storLocation == undefined){
        	layer.msg("请选择库位！");
        	return;
        }
        $.each(tableData,function(index,item){
        	item.storLocation = storLocation;
        });
        initTable(table, tableData);
    });
	// 打印
	$("#printBut").click(function() {
		var url = "/getRecePrintHtml?receId=" + receId;
		location = url;
	});
	// 返回
	$("#goBack").click(function() {
		// window.history.back(-1);
		tuoBack('.receiveEdit', '#serachRece');
	});
	// 送货单号触发回车事件
	$("#deliCode").keydown(function(e) {
		if (e.keyCode == 13) {
			var deliCode = $("#deliCode").val();
			if (deliCode != null && deliCode != "") {
				if(oldDeliCode != deliCode) {
					var index = layer.load();
					$.ajax({
						type : "post",
						url : "/queryDeliveryByDeliCode?deliCode=" + deliCode,
						dataType : "JSON",
						async : true,
						success : function(data) {
							layer.close(index);
							oldDeliCode = deliCode;
							if (data.judge) {
								$("#receType").val(data.deli.deliType);
								$("#suppId").val(data.deli.suppId);
								$("#suppName").val(data.deli.suppName);
								$("#receNumber").val(data.deli.deliNumber);
								$("#receAmount").val(data.deli.deliAmount);
								$("#receUnit").val(data.deli.receUnit);
								$("#truckNum").val(data.deli.truckNum);
								$("#post").val(data.deli.post);
								$("#suppRange").val(data.deli.suppRange);
								 $("#suppRangeDesc").val(data.deli.suppRangeDesc);
								 if(data.deli.suppRange != null && data.deli.suppRange !=''){
									 $("#suppRange2").val(data.deli.suppRange+' - '+data.deli.suppRangeDesc);
								 }else{
									 $("#suppRange2").val("");
								 }
								var now = new Date(data.deli.deliDate);
								// 格式化日，如果小于9，前面补0
								var day = ("0" + now.getDate()).slice(-2);
								// 格式化月，如果小于9，前面补0
								var month = ("0" + (now.getMonth() + 1))
										.slice(-2);
								// 拼装完整日期格式
								var receday = now.getFullYear() + "-" + (month)
										+ "-" + (day);
								// 完成赋值
								$("#receDate").val(receday);
								judgeCode = true;
								tableData = data.list;
								initTable(table, tableData);
							} else {
								judgeCode = false;
								resetHtml();
								layer.alert("请填写正确的送货单号");
							}
						},
						error : function() {
							layer.close(index);
							layer.alert("Connection error");
						}
					});
				}
			} else {
				resetHtml();
				judgeCode = false;
				layer.alert("请填写送货单号");
			}

		}
	});
	function resetHtml(){
		$("#receType").val("");
		$("#suppId").val("");
		$("#suppName").val("");
		$("#receNumber").val("");
		$("#receAmount").val("");
		$("#receUnit").val("");
		$("#receDate").val("");
		$("#truckNum").val("");
		$("#post").val("");
		$("#suppRange").val("");
		$("#suppRangeDesc").val("");
		$("#suppRange2").val("");
		tableData = [];
		initTable(table, tableData);
	}
	
	table.on('edit(receMateTableEvent)', function(obj) { 
		// 注：edit是固定事件名，test是table原始容器的属性
		// lay-filter="对应的值"
		/*
		 * console.log(parseInt(obj.value)); //得到修改后的值 console.log(obj.field);
		 * //当前编辑的字段名 console.log(obj.data); //所在行的所有相关数据
		 * console.info(obj.data.mateNumber) console.info(tableData);
		 */
		if (obj.field == "receNumber") {
			var rowdata = obj.data;
			var deliNumber = rowdata.deliNumber;
			if (isNaN(obj.value)) {
				layer.msg("请输入有效数字!");
				rowdata[obj.field] = 0;
				initTable(table, tableData);
				return;
			}
			if (obj.value < 0) {
				layer.msg("请填写正确的实收数量");
				rowdata[obj.field] = 0;
				initTable(table, tableData);
			}
		}
	});
	// 保存
	$("#saveRece").click(function() {
		// 获取layui 编辑后的table表格中的所有数据
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		if (tableData.length == 0) {
			layer.msg("物资信息表为空,不能保存,请填写正确的送货单号");
			return;
		}
		var deliCode = $("#deliCode").val();
		 if(oldDeliCode != deliCode){
			 layer.alert("送货单号不正确,请检查填写的送货单号");
			 return;
		 }
		$("#receMateData").val(JSON.stringify(tableData));
		var formData = $("#receiveForm").serialize();
		$.ajax({
			type : "POST",
			url : "/updateReceive?type=add&deliCode2=" + deliCode2,
			data : formData,
			async : false,
			error : function(request) {
				layer.alert("Connection error");
			},
			success : function(data) {
				debugger;
				var code = data.code;
				if(code == 0) {
					layer.msg("收货单保存成功");
					// window.history.back(-1);
					tuoBack('.receiveEdit', '#serachRece');
				}else if(code == 1) {
					layer.alert("收货单保存失败");
				}else if(code == 2) {
					layer.alert(data.msg);
				}
			}
		});

	});
	
	// 库位信息
	table.on("tool(receMateTableEvent)", function(obj) {
		if (obj.event = "setStorLocation") {
			debugger;
			var factoryAddr = obj.data.factoryAddr;
			if(factoryAddr != "" && factoryAddr != null && factoryAddr!= undefined){
				layer.open({
					type : 2,
					title : "库位信息",
					shadeClose : false,
					shade : 0.1,
					content : '/storageLocationInfo?factoryAddr=' + factoryAddr,
					area : [ '200px', '150' ],
					maxmin : false, // 开启最大化最小化按钮
					btn : [ '确认', '取消' ],
					yes : function(index, layero) {
						// 获取选中的物料数据
						var contentWindow = $(layero).find("iframe")[0].contentWindow;
						var checkedData = contentWindow.getCheckedData();
						if(checkedData.length == 1) {
							obj.update({
								storLocation : checkedData[0].dicCode
							});
							layer.close(index);
						}else {
							layer.msg("请选择一个库位!");
						}
					},
					btn2 : function(index, layero) {
						layer.close(index);
					}
				});
			} else {
				layer.msg("该订单无工厂信息，请先联系MIS确认工厂信息！");
			}
		}
	});
	
	// 收货
	$("#rece").click(function() {
		// 获取layui 编辑后的table表格中的所有数据
		var status = $("#status").val();
		if(status == "已收货"){
			layer.msg("收货单已收货，无法再次收货");
			return;
		}
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		if (tableData.length == 0) {
			layer.msg("物资信息表为空,不能保存,请填写正确的送货单号");
			return;
		}
		var deliCode = $("#deliCode").val();
		 if(oldDeliCode != deliCode){
			 layer.alert("送货单号不正确,请检查填写的送货单号");
			 return;
		 }
		var msg = checkTableData();
		if(msg == "") {
			$("#receMateData").val(JSON.stringify(tableData));
			var formData = $("#receiveForm").serialize();
			$.ajax({
				type : "POST",
				url : "/updateReceive?type=rece&deliCode2=" + deliCode2,
				data : formData,
				dataType : "JSON",
				async : false,
				error : function(request) {
					layer.alert("Connection error");
				},
				success : function(data) {
					var code = data.code;
					if(code == 0) {
						layer.msg("收货单保存成功");
						// window.history.back(-1);
						tuoBack('.receiveEdit', '#serachRece');
					}else if(code == 1) {
						layer.alert("收货单保存失败");
					}else if(code == 2) {
						$("#status").val("已收货");
						$("#asyncStatus").val("同步失败");
						$("#errorMsg").val(data.msg);
						layer.alert(data.msg);
					}
				}
			});
		} else {
			layer.alert(msg);
		}

	});
	
	// 验退
	$("#regressBtn").click(function() {
		// 获取layui 编辑后的table表格中的所有数据
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		if (tableData.length == 0) {
			layer.msg("物资信息表为空,不能保存,请填写正确的送货单号");
			return;
		}
		var deliCode = $("#deliCode").val();
		 if(oldDeliCode != deliCode){
			 layer.alert("送货单号不正确,请检查填写的送货单号");
			 return;
		 }
		var msg = checkTableData();
		if(msg == "") {
			$("#receMateData").val(JSON.stringify(tableData));
			var formData = $("#receiveForm").serialize();
			$.ajax({
				type : "POST",
				url : "/updateReceive?type=regress&deliCode2=" + deliCode2,
				data : formData,
				dataType : "JSON",
				async : false,
				error : function(request) {
					layer.alert("Connection error");
				},
				success : function(data) {
					var code = data.code;
					if(code == 0) {
						layer.msg("收货单验退成功");
						// window.history.back(-1);
						tuoBack('.receiveEdit', '#serachRece');
					}else {
						layer.alert("收货单验退保存失败");
					}
				}
			});
		} else {
			layer.alert(msg);
		}

	});
	// 删除
	$("#remRece").click(function() {
		layer.confirm('真的删除这个收货单吗？', function(index) {
			var receIds = [];
			receIds.push(receId);
			var deliCodes = [];
			deliCodes.push(deliCode2);
			var deliCodes2 = JSON.stringify(deliCodes);
			$.ajax({
				type : "post",
				url : "/deleteReceiveByReceId?deliCodes2=" + deliCodes2,
				data : "receIds=" + receIds,
				dataType : "JSON",
				success : function(data2) {
					if (data2) {
						layer.msg('删除成功', {
							time : 2000
						});
						// window.history.back(-1);
						tuoBack('.receiveEdit', '#serachRece');
					} else {
						layer.alert('<span style="color:red;">删除失败</sapn>');
					}
				}
			});
			layer.close(index);
		});
	});
	// 同步 tableData
	$("#synchReceBut").click(function() {
		debugger;
		var asyncStatus = $("#asyncStatus").val();
		if(asyncStatus != "同步成功") {
			$("#receMateData").val(JSON.stringify(tableData));
			var formData = $("#receiveForm").serialize();
			$.ajax({
				type : "POST",
				url : "/synchReceiveSap",
				data : formData,
				dataType : "JSON",
				async : false,
				error : function(request) {
					layer.alert("Connection error");
				},
				success : function(data) {
					var code = data.code;
					if(code == 0) {
						layer.msg("同步成功");
						// window.history.back(-1);
						tuoBack('.receiveEdit', '#serachRece');
					}else if(code == 2) {
						$("#asyncStatus").val("同步失败");
						$("#errorMsg").val(data.msg);
						layer.alert(data.msg);
					}
				}
			});
		} else {
			layer.alert("内向交货单已经完成,不需要同步该收货单");
		}
	});

});

// 初始化预约申请物资表格
function initTable(table, data) {
	receData = table.render({
		elem : "#receMateTable",
		data : data,
		id : "receMateTableId",
		limit : 100,
		cols : [ [ {
			title : "项次",
			field : "frequency",
			align : 'center',
			width : 60
		}, {
			title : "内向交货单号",
			field : "inboDeliCode",
			align : 'center',
			width : 100
		},{
			field : "orderId",
			title : "采购订单号",
			align : 'center',
			width : 100
		}, {
			field : "mateCode",
			title : "物料编码",
			align : 'center'
		}, {
			field : "mateName",
			title : "物料名称",
			align : 'center'
		}, {
			field : "deliNumber",
			title : "送货数量",
			align : 'center',
			width : 120
		}, {
			field : "receNumber",
			title : "实收数量",
			align : 'center',
			edit : 'text',
			width : 120
		}, {
			field : "unit",
			title : "单位",
			align : 'center',
			width : 60
		}, {
			field : "prodPatchNum",
			title : "产品批号",
			align : 'center',
			width : 120
		}, {
			field : "factoryAddr",
			title : "工厂",
			align : 'center',
			width : 80
		}, {
			field : "storLocation",
			title : "库位",
			align : 'center',
			event : "setStorLocation",
			width : 80
		}, {
			field : "remark",
			title : "备注",
			align : 'center',
			edit : 'text'
		}, ] ]

	});
	if (funType2 == '2') {
		$('table td').removeAttr('data-edit');
		$('table td').removeAttr('lay-event');
	}
}
//校验是否填写库位信息
function checkTableData() {
	debugger;
	for ( var k = 0; k < tableData.length; k++) {
		var elem = tableData[k];
		var location = elem.storLocation;
		var receNumber = elem.receNumber;
		if(location == null || location == " " || location == undefined)
			return "请添加库位信息！";
		if(receNumber === null || receNumber === "" || receNumber === undefined)
			return "请填写实收数量！";
	}
	return "";
}

Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].id == val.id) {
			this.splice(k, 1);
			return;
		}
	}
};
