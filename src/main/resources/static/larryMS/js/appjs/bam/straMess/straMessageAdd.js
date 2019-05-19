var tableData;
var table;
var suppId;
var sapId;
var oldSuppId = "";
var oldZzoem = "";
var oldSuppRange = "";

var operate = "";
var operateNo = "";
layui.use([ 'form', 'table', 'laydate' ], function() {
	var form = layui.form;
	table = layui.table;
	layer = layui.layer;
	var $ = layui.$;
	var laydate = layui.laydate;
	laydate.render({
		elem : "#deliveryDate"
	});
	laydate.render({
		elem : "#arriDate"
	});
	var type = $("#type").val();
	var messId = $("#messId").val();
	oldSuppId = $("#suppId").val();
	oldZzoem = $("#zzoem").val();
	oldSuppRange = $("#suppRange").val();
	if (type == 1) {
		$(function() {
			$("#mateNumber").val(0);
			$("#mateAmount").val(0);
		});
		tableData = [];
		initTable(table, tableData);
		// controllCell(tableData);
	} else {
		sapId = $("#sapId").val();
		$.ajax({
			type : "post",
			url : "/queryMessMateForStraMess",
			data:{
				messId:messId,
				sapId:oldZzoem
			},
			dataType : "JSON",
			success : function(data) {
				debugger;
				tableData = [];
				if (data.list.length != 0)
					tableData = data.list;
				initTable(table, tableData);
				if(data.proList.length !=0){
					var html = '<option value=""></option>';
					var suppRange = $("#suppRange").val();
	  				for(var i=0;i<data.proList.length;i++){
	  					var pro = data.proList[i];
	  					var range = pro.suppRange;
	  					if(suppRange == range){
	  						html+='<option selected value="'+pro.suppRange+' , '+pro.suppRangeDesc+'">'+pro.suppRange+' - '+pro.suppRangeDesc+'</option>'
	  					}else{
	  						html+='<option value="'+pro.suppRange+' , '+pro.suppRangeDesc+'">'+pro.suppRange+' - '+pro.suppRangeDesc+'</option>'
	  					}
	  				}
	  				$("#suppRange2").html(html);
	  				if (type != '3') {
	  					//重新初始化下拉框
	  					form.render('select');
	  				}
				}
			}
		});
	}
	if (type == '3') {
		debugger;
		
		$('.layui-form-select').eq(0).css('display', 'none');
		$('#suppId').css('display', 'block');
		$("#suppId").css({
			"display" : "block",
			"width" : "100%",
			"min-height" : "36px",
			"border" : "1px solid #e6e6e6",
			"webkit-appearance" : "none",
			"padding-left" : "10px"
		});
		$("#suppId").attr("disabled", 'disabled');
		$('.layui-form-select').eq(1).css('display', 'none');
		$('#zzoem').css('display', 'block');
		$("#zzoem").css({
			"display" : "block",
			"width" : "100%",
			"min-height" : "36px",
			"border" : "1px solid #e6e6e6",
			"webkit-appearance" : "none",
			"padding-left" : "10px"
		});
		$("#zzoem").attr("disabled", 'disabled');
		$('.layui-form-select').eq(2).css('display', 'none');
		$('#receUnit').css('display', 'block');
		$("#receUnit").css({
			"display" : "block",
			"width" : "100%",
			"min-height" : "36px",
			"border" : "1px solid #e6e6e6",
			"webkit-appearance" : "none",
			"padding-left" : "10px"
		});
		$("#receUnit").attr("disabled", 'disabled');
		$('.layui-form-select').eq(3).css('display', 'none');
		$('#suppRange2').css('display', 'block');
		$("#suppRange2").css({
			"display" : "block",
			"width" : "100%",
			"min-height" : "36px",
			"border" : "1px solid #e6e6e6",
			"webkit-appearance" : "none",
			"padding-left" : "10px"
		});
		$("#suppRange2").attr("disabled", 'disabled');
		$(".disa").prop("disabled", true);
	}

	// 监听工具条
	table.on('tool(MessmateTableEvent)', function(obj) {
		var data = obj.data;
		if (obj.event === 'check') {// 查看

		} else if (obj.event === 'del') {// 删除

		}
	});
	// 添加物资
	$("#addMate").click(function() {
		var suppId = $("#suppId").val();
		var zzoem = $("#zzoem").val();
		var suppRange = $("#suppRange").val();
		var msg = "";
		if (suppId == "" || suppId == undefined)
			msg += "请选择供应商！";
		if (zzoem == "" || zzoem == undefined)
			msg += "请选择OEM供应商！";
		if (msg == "") {
			operate = "";
			operateNo = "";
			suppId = suppId.replace("s", "");
			data = {
				sapId : suppId,
				zzoem : zzoem,
				tableData : tableData,
				a : this.itemActive
			};
			layer.open({
						type : 2,
						title : "调拨单列表",
						shadeClose : false,
						shade : 0.1,
						content : '/getAllAllotOrderListHtml',
						area : [ '1200px',
								'95%' ],
						maxmin : true, // 开启最大化最小化按钮
						btn : [ '确认', '取消' ],
						yes : function(index,
								layero) {
							// 获取选中的物料数据
							var contentWindow = $(layero).find("iframe")[0].contentWindow;
							var checkedData = contentWindow.getCheckedData();
							if (checkedData.length == 1) {
								var alloNo = $("#alloNo").val();
								var messId = $("#messId").val();
								var checkedNo = checkedData[0].contOrdeNumb;
								if (alloNo != checkedNo) {
									// 配置一个透明的询问框
									var loadIndex = layer.load();
									// 校验并处理获取到对应的半成品数据
									$.ajax({
										type : "get",
										url : "/getStraMates",
										data : {
											orderId : checkedNo,
											messId : messId,
											suppRange : suppRange
										},
										dataType : "JSON",
										async : true,
										error : function(request) {
											layer.close(loadIndex);
											layer.alert("Connection error");
										},
										success : function(data) {
											layer.closeAll();
											var judge = data.judge;
											var msg = data.result;
											//校验是否可以创建新的直发通知单
											if (judge) {
												var mateMsg = data.mateMsg;
												//校验是否查询到符合条件的采购订单
												if(mateMsg == "" || mateMsg == undefined){
													debugger;
													var strError = data.strError;
													//校验物料的订单可用量大于零
													if(strError == "" || strError == undefined){
														$("#alloNo").val(checkedNo);
														var total = data.total;
														if (total != undefined) {
															$("#mateNumber").val(total);
														} else {
															$("#mateNumber").val(0);
														}
														var totalAmount = data.totalAmount;
														if (totalAmount != undefined) {
															$("#mateAmount").val(totalAmount);
														} else {
															$("#mateAmount").val(0);
														}
														data.msg;
														data.mateMsg;
														tableData = data.messMateList;
														initTable(table, tableData);
													}else{
														reportError("半成品"+strError+" 对应的订单可用量小于等于零。");
													}
												}else{
													reportError(mateMsg);
												}
											} else {
												reportError(msg);
											}
										}
									});
								}
							} else {
								layer.msg("请选择一个调拨单！");
							}
						},
						btn2 : function(index,layero) {
							layer.close(index);
						}
					});
		} else {
			layer.msg(msg);
		}

	});

	function reportError(msg){
		$("#alloNo").val("");
		$("#mateNumber").val(0);
		$("#mateAmount").val(0);
		tableData = [];
		initTable(table, tableData);
		layer.confirm(msg, {btn : [ '确定' ]}, function(index, layero) {
			layer.close(index);
		});
	}
	
	// 批量删除物资
	$('#removeMate').click(function() {
		layer.confirm("是否确定删除调拨单？", {
			btn : [ '确定', '取消' ]
		}, function(index, layero) {
			$("#alloNo").val("");
			$("#mateNumber").val(0);
			tableData = [];
			initTable(table, tableData);
			layer.close(index);
		}, function(index) {
			layer.close(index);
		});
	})
	// 返回
	$("#goBack").click(function() {
		// window.history.back(-1);
		tuoBack('.straMessageAdd', '#serachStraMess');
	});

	// 监听单元格编辑
	table.on('edit(MessmateTableEvent)', function(obj) { // 注：edit是固定事件名，test是table原始容器的属性
		var rowdata = obj.data;
		var value = obj.value;
		if (isNaN(obj.value)) {
			layer.msg("请输入有效数字!");
			rowdata[obj.field] = 0;
			initTable(table, tableData);
			statis(tableData);
			return;
		}
		var pfv = parseFloat(value);
		if (pfv > 0) {
			statis(tableData);
		} else {
			layer.msg("请输入大于零的数!");
			rowdata[obj.field] = 0;
			statis(tableData);
			initTable(table, tableData);
		}
	});

	// 提交
	$("#submitStraMess").click(
			function() {
				var result = checkMust();
				if (!result.flag) {
					layer.msg(result.msg);
					return;
				}
				var post = $("#post").val();
				if (post == null || post == "") {
					layer.msg("收货单位的岗位信息为空，请在收货地址信息中配置岗位信息");
					return;
				}
				var type = $("#type").val();
				if (tableData.length > 0) {
					$("#messMateData").val(
							JSON.stringify(tableData));
					var formData = $("#straMessForm")
							.serialize();
					$.ajax({
						type : "POST",
						url : "/submitStraMess",
						data : "type=" + type,
						data : formData,
						dataType : "JSON",
						async : false,
						error : function(request) {
							layer.alert("Connection error");
						},
						success : function(data) {
							if (data) {
								layer.msg("直发通知提交成功");
								// location =
								// "/getStraMessageListHtml";
								tuoBack('.straMessageAdd',
										'#serachStraMess');
							} else {
								layer.alert("直发通知提交失败");
							}
						}
					});
				} else {
					layer.msg("请选择物资");
				}

			});
	// select下拉框改变值触发
	// 根据sap编码查询供应商
	form.on("select(suppId)", function(obj) {
		if (oldSuppId !== obj.value) {
			oldSuppId = obj.value;
			tableData = [];
			initTable(table, tableData);
			$("#mateNumber").val(0);
			$("#mateAmount").val(0);
		}
	});

	// 监听OEM供应商下拉选择
	form.on("select(zzoem)", function(obj) {
		if (oldZzoem !== obj.value) {
			oldZzoem = obj.value;
			tableData = [];
			initTable(table, tableData);
			$("#mateNumber").val(0);
			$("#mateAmount").val(0);
		}
		$.ajax({
			 type:"post",
			 url:"/querySuppRangeOfQualSuppBySapId",
			 data:{
				 sapId:obj.value
			 },
		  	 dataType:"JSON",
		  	 success:function(data){
		  			var proList = data.proList;
		  			if(proList.length>0){
		  				var html = '<option value=""></option>';
		  				for(var i=0;i<proList.length;i++){
		  					var pro = proList[i];
		  					html+='<option value="'+pro.suppRange+' , '+pro.suppRangeDesc+'">'+pro.suppRange+' - '+pro.suppRangeDesc+'</option>'
		  				}
		  				$("#suppRange2").html(html);
		  				//重新初始化下拉框
		  				form.render('select');
			  		 }else{
			  			layer.msg("未查询到供应商子范围信息");
			  		 }
		  	 },
		  	 error:function(){
		  		layer.alert("程序出错了"); 
		  	 }
		  });
	});
	
	//select下拉框改变值触发
	  //供应商选择供应商子范围
	  form.on("select(suppRange2)",function(obj){
		    debugger;
			var suppRange =obj.value;
			if(suppRange != null && suppRange !==''){
				var index = suppRange.split(' , ');
				$("#suppRange").val(index[0]);
				$("#suppRangeDesc").val(index[1]);
			}else{
				$("#suppRange").val("");
				$("#suppRangeDesc").val("");
			}
			if (oldSuppRange !== suppRange) {
				oldSuppRange = suppRange;
				tableData = [];
				initTable(table, tableData);
				$("#mateNumber").val(0);
				$("#mateAmount").val(0);
			}
    });
	// select下拉框改变值触发
	// 根据接收单位查询收货信息
	form
			.on(
					"select(receUnit)",
					function(obj) {
						var receUnit = obj.value;
						if (receUnit != "") {
							$
									.ajax({
										type : "post",
										url : "/queryReceiveMessByReceUnit?receUnit="
												+ encodeURIComponent(receUnit),
										dataType : "JSON",
										success : function(data) {
											if (data.judge) {
												var receAddr = data.rece.receAddr;
												var contact = data.rece.contact;
												var phone = data.rece.phone;
												var post = data.rece.post;
												if (receAddr == null
														|| receAddr == "") {
													layer
															.alert("请配置收货地址");
													return;
												}
												if (contact == null
														|| contact == "") {
													layer
															.alert("请配置联系人");
													return;
												}
												if (phone == null
														|| phone == "") {
													layer
															.alert("请配置联系电话");
													return;
												}
												if (post == null
														|| post == "") {
													layer
															.alert("请配置收货单位的岗位");
													return;
												}
												$("#receAddr")
														.val(
																data.rece.receAddr);
												$("#contact")
														.val(
																data.rece.contact);
												$("#phone")
														.val(
																data.rece.phone);
												$("#post")
														.val(
																data.rece.post);
											} else {
												layer
														.alert("请输入正确的收货单位");
											}
										},
										error : function() {
											layer
													.alert("程序出错了");
										}
									});
						} else {
							$("#receAddr").val('');
							$("#contact").val('');
							$("#phone").val('');
							$("#post").val('');
							layer.alert("请选择收货单位");
						}
					});

	$("#printBut").click(function() {
		var url = "/getStraMessPrintHtml?messId=" + messId;
		location = url;
	});

});

// 初始化预约申请物资表格
function initTable(table, data) {
	var type = $("#type").val();
	table.render({
		elem : "#MessmateTable",
		data : data,
		id : "MessmateTableId",
		limit : 100,
		cols : [ [ {
			field : "orderId",
			title : "调拨单号",
			align : 'center',
			width : 100
		}, {
			field : "mateName",
			title : "成品",
			align : 'center'
		}, {
			field : "mateCode",
			title : "成品编码",
			align : 'center'
		}, {
			field : "mateNumber",
			title : "调拨数量(箱)",
			align : 'center'
		}, {
			field : "poId",
			title : "采购订单",
			align : 'center',
			width : 100
		}, {
			field : "semiMateName",
			title : "半成品",
			align : 'center'
		}, {
			field : "semiMateCode",
			title : "半成品编码",
			align : 'center'
		}, {
			field : "unpaNumber",
			title : "订单未交量",
			align : 'center',
			width : 120
		}, {
			field : "calculNumber",
			title : "订单可用量",
			align : 'center',
			width : 120
		}, {
			field : "semiMateNumber",
			title : "数量(箱)",
			align : 'center',
			width : 120
		}, {
			field : "semiMateAmount",
			title : "方量",
			align : 'center',
			width : 80,
			edit : 'text'
		} ] ]

	});
	if (type == '3') {
		$('table td').removeAttr('data-edit');
	}
}

// 弹出框子页面调用父页面方法传递数据
function orders(data) {
	layui.use([ 'form', 'table' ], function() {
		var form = layui.form;
		layer = layui.layer;
		var $ = layui.$;
		for ( var i = 0; i < data.length; i++) {
			var count = 0;
			for ( var j = 0; j < tableData.length; j++) {
				if (data[i].contOrdeNumb == tableData[j].orderId) {
					count++;
				}
			}
			if (count == 0) {
				$.ajax({
					type : "post",
					url : "/queryOrderMateByContOrdeNumb?contOrdeNumb="
							+ data[i].contOrdeNumb,
					dateType : "JSON",
					async : false,
					success : function(orderMateData) {
						$.each(orderMateData, function(index, item) {
							var arr = {
								orderId : item.mainId,
								unit : item.company,
								mateCode : item.mateNumb,
								mateName : item.prodName,
								frequency : item.frequency,
								mateNumber : item.purcQuan,
								mateAmount : '',
							};
							tableData.push(arr);
						})
					},
					error : function() {
						layer.alert("程序出错了");
					}
				})
				initTable(table, tableData);
				statis(tableData);
			} else {
				layer.msg("无法添加相同的调拨单");
			}
		}

	});
}
// 保存
function saveStraMess() {
	layui.use([ 'form', 'table' ], function() {
		var $ = layui.$;
		var type = $("#type").val();
		var result = checkMust();
		if (!result.flag) {
			layer.msg(result.msg);
			return;
		}
		var post = $("#post").val();
		if (post == null || post == "") {
			layer.msg("收货单位的岗位信息为空，请在收货地址信息中配置岗位信息");
			return;
		}
		if (tableData.length > 0) {
			$("#messMateData").val(JSON.stringify(tableData));
			var formData = $("#straMessForm").serialize();
			$.ajax({
				type : "POST",
				url : "/addStraMess",
				data : "type=" + type,
				data : formData,
				dataType : "JSON",
				async : false,
				error : function(request) {
					layer.alert("Connection error");
				},
				success : function(data) {
					if (data) {
						layer.msg("直发通知保存成功");
						// window.history.back(-1);
						tuoBack('.straMessageAdd', '#serachStraMess');
					} else {
						layer.alert("直发通知保存失败");
					}
				}
			});
		} else {
			layer.msg("请选择物资");
		}
	});
}

// 删除物资后统计数量和方量
function statis(tableData) {
	debugger;
	var amount = 0;
	$.each(tableData, function(index, item) {
		var amo = item.semiMateAmount;
		if (amo == '' || amo == null || amo == undefined) {
			amo = 0;
		}
		amount += parseFloat(amo);
	});
	$("#mateAmount").val(amount);
}

Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].orderId == val.orderId) {
			this.splice(k, 1);
			return;
		}
	}
};
