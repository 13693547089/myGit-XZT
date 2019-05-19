var table;
layui
		.use(
				[ 'table', 'laydate' ],
				function() {
					table = layui.table;
					var $ = layui.$;
					var laydate = layui.laydate;
					initStraMessTable();
					// 监听表格复选框选择
					table.on('checkbox(straMessdemo)', function(obj) {
					});
					laydate.render({
						elem : '#startDate', // 指定元素
					});
					laydate.render({
						elem : '#endDate', // 指定元素
					});
					laydate.render({
						elem : '#createDate', // 指定元素
					});
					// 监听工具条
					table.on('tool(straMessdemo)',function(obj) {
										var data = obj.data;
										if (obj.event === 'check') {// 查看
											var url = "/getStraMessageAddHtml?messId="
													+ data.messId + "&type=3";
											//location = url;
											tuoGo(url,'straMessageAdd','straMessTableId');
										} else if (obj.event === 'del') {// 删除
											if (data.messStatus == '已保存') {
												layer.confirm('真的删除这个直发通知吗？',
																function(index) {
																	var messIds = [];
																	messIds.push(data.messId);
																	$.ajax({
																				type : "post",
																				url : "/deleteStraMessByMessId",
																				data : "messIds="
																						+ messIds,
																				dataType : "JSON",
																				success : function(
																						data2) {
																					if (data2) {
																						layer.msg(
																										'删除成功',
																										{
																											time : 2000
																										});
																						initStraMessTable();
																					} else {
																						layer.alert('<span style="color:red;">删除失败</sapn>');
																					}
																				}
																			});
																	layer.close(index);
																});
											} else {
												layer.alert('<span style="color:red;">只有"已保存"状态的直发通知才能被删除</sapn>');
											}
										} else if (obj.event === 'edit') {// 编辑
											debugger;
											var messStatus = data.messStatus;
											var createId = data.createId;
											var userId = $("#userId").val();
											var messId = data.messId;
											var messCode = data.messCode;
											if (messStatus == "已保存") {
												var url = "/getStraMessageAddHtml?messId="
														+ messId + "&type=2";
												//location = url;
												tuoGo(url,'straMessageAdd','straMessTableId');
											} else if (messStatus == "已通知"
													|| messStatus == "待发货"
													|| messStatus == "已发货") {
												debugger;
												if (userId == createId) {
													layer.open({
																type : 2,
																title : '编辑提货日期',
																shadeClose : false,
																shade : 0.1,
																content : '/getUpdateDeliDateHtml?messId='
																		+ messId,
																area : [
																		'500px',
																		'70%' ],
																maxmin : true, // 开启最大化最小化按钮
																btn : [ '确认',
																		'取消' ],
																yes : function(
																		index,
																		layero) {
																	// 按钮【按钮一】的回调
																	// 获取选中的物料数据
																	var map = $(
																			layero)
																			.find(
																					"iframe")[0].contentWindow
																			.getData();
																	if (map.judge) {
																		// 关闭弹框
																		layer
																				.close(index);
																		// 处理数据
																		map.messCode = messCode;
																		if (messStatus == "待发货"
																				|| messStatus == "已发货") {
																			layer
																					.confirm(
																							'直发通知单:'
																									+ data.messCode
																									+ '已经和送货单关联，确认要修改直发通知单的提货日期吗？',
																							{
																								btn : [
																										'确认',
																										'取消' ]
																							// 按钮
																							},
																							function(
																									index,
																									layero) {
																								map.type = "1";
																								updateDeliDate(map);
																								layer
																										.close(index)
																							},
																							function(
																									index,
																									layero) {
																								layer
																										.close(index)
																							});
																		} else {
																			layer
																					.confirm(
																							'确认要修改'
																									+ data.messCode
																									+ '直发通知单的提货日期吗？',
																							{
																								btn : [
																										'确认',
																										'取消' ]
																							// 按钮
																							},
																							function(
																									index,
																									layero) {
																								map.type = "0";
																								updateDeliDate(map);
																								layer
																										.close(index)
																							},
																							function(
																									index,
																									layero) {
																								layer
																										.close(index)
																							});
																		}

																	} else {
																		layer
																				.msg(map.msg);
																	}
																},
																btn2 : function(
																		index,
																		layero) {
																	// 按钮【按钮二】的回调
																	// 默认会关闭弹框
																	// return
																	// false
																	// 开启该代码可禁止点击该按钮关闭
																}
															});
												} else {
													layer
															.alert('<span style="color:red;">只能编辑自己创建的直发通知单</sapn>')
												}
											} else {
												layer
														.alert('<span style="color:red;">只有"已保存"状态的直发通知进入编辑页面编辑</sapn>');
											}
										}
									});
					// 条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
					var $ = layui.$, active = {
						reload : function() {
							var startDate = $('#startDate');
							var endDate = $("#endDate");
							var messStatus = $("#messStatus");
							var messCode = $("#messCode");
							var suppName = $("#suppName");
							var createDate = $("#createDate");
							var receUnit = $("#receUnit");
							var alloNo = $("#alloNo");
							// 执行重载
							table.reload('straMessTableId', {
								page : {
									curr : 1
								// 重新从第 1 页开始
								},
								where : {// 后台定义对象接收
									startDate : startDate.val(),
									endDate : endDate.val(),
									messStatus : messStatus.val(),
									messCode : messCode.val(),
									suppName : suppName.val(),
									createDate : createDate.val(),
									alloNo : alloNo.val(),
									receUnit : receUnit.val()
								}
							});
						}

					};

					$('.demoTable .layui-btn').on('click', function() {
						var type = $(this).data('type');
						active[type] ? active[type].call(this) : '';
					});

					// 新建
					$("#addStraMess").click(function() {
						var url = "/getStraMessageAddHtml?type=1";
						//location = url;
						tuoGo(url,'straMessageAdd','straMessTableId');
					});
					// 重置
					$("#reset").click(function() {
						$("#suppInfo").val('');
						$("#category").val('');
					});
					// 删除
					$("#removeStraMess")
							.click(
									function() {
										var table = layui.table;
										var checkStatus = table
												.checkStatus('straMessTableId');
										var data = checkStatus.data;
										var length = data.length;
										if (length != 0) {
											var messIds = [];
											var a = 0;
											for ( var i = 0; i < length; i++) {
												messIds[i] = data[i].messId;
												if (data[i].messStatus != '已保存') {
													a++;
												}
											}
											if (a == 0) {
												layer
														.confirm(
																'真的删除选中的直发通知吗？',
																function(index) {
																	$
																			.ajax({
																				type : "post",
																				url : "/deleteStraMessByMessId",
																				data : "messIds="
																						+ messIds,
																				dataType : "JSON",
																				success : function(
																						data2) {
																					if (data2) {
																						layer
																								.msg(
																										'删除成功',
																										{
																											time : 2000
																										});
																						initStraMessTable();
																					} else {
																						layer
																								.alert('<span style="color:red;">删除失败</sapn>');
																					}
																				},
																				error : function() {
																					layer
																							.msg(
																									'程序出错',
																									{
																										time : 2000
																									});
																				}
																			});
																	layer
																			.close(index);
																});
											} else {
												layer
														.alert('<span style="color:red;">只有"已保存"状态的直发通知才被删除</sapn>');
											}
										} else {
											layer
													.alert('<span style="color:red;">请选择一条或多条数据进行删除</sapn>');
										}

									});

					// 提交
					$("#subStraMess")
							.click(
									function() {
										var checkStatus = table
												.checkStatus('straMessTableId');
										var data = checkStatus.data;
										var length = data.length;
										if (length != 0) {
											var messIds = [];
											var a = 0;
											for ( var i = 0; i < length; i++) {
												messIds[i] = data[i].messId;
												if (data[i].messStatus != '已保存') {
													a++;
												}
											}
											if (a == 0) {
												layer
														.confirm(
																'确定要提交选中的直发通知吗？',
																function(index) {
																	var messIdJson = JSON
																			.stringify(messIds);
																	$
																			.ajax({
																				type : "post",
																				url : "/updateStraMessStatusByMessId?messIdJson="
																						+ messIdJson,
																				dataType : "JSON",
																				success : function(
																						data2) {
																					if (data2) {
																						layer
																								.msg(
																										'提交成功',
																										{
																											time : 2000
																										});
																						initStraMessTable();
																					} else {
																						layer
																								.alert('<span style="color:red;">提交失败</sapn>');
																					}
																				},
																				error : function() {
																					layer
																							.msg(
																									'程序出错',
																									{
																										time : 2000
																									});
																				}
																			});
																	layer
																			.close(index);
																});
											} else {
												layer
														.alert('<span style="color:red;">只有"已保存"状态的直发通知才能提交</sapn>');
											}
										} else {
											layer
													.alert('<span style="color:red;">请选择一条或多条数据进行提交</sapn>');
										}
									});

					// 作废
					$("#cancellBut")
							.click(
									function() {
										var checkStatus = table
												.checkStatus('straMessTableId');
										var data = checkStatus.data;
										var length = data.length;
										if (length != 0) {
											var messIds = [];
											var a = 0;
											for ( var i = 0; i < length; i++) {
												messIds[i] = data[i].messId;
												if (data[i].messStatus != '已保存'
														&& data[i].messStatus != '已通知') {
													a++;
												}
											}
											if (a == 0) {
												layer
														.confirm(
																'确定要作废选中的直发通知吗？',
																function(index) {
																	var messIdJson = JSON
																			.stringify(messIds);
																	$
																			.ajax({
																				type : "post",
																				url : "/cancellStraMessByMessId?messIdJson="
																						+ messIdJson,
																				dataType : "JSON",
																				success : function(
																						data2) {
																					if (data2) {
																						layer
																								.msg(
																										'作废成功',
																										{
																											time : 2000
																										});
																						initStraMessTable();
																					} else {
																						layer
																								.alert('<span style="color:red;">作废失败</sapn>');
																					}
																				},
																				error : function() {
																					layer
																							.msg(
																									'程序出错',
																									{
																										time : 2000
																									});
																				}
																			});
																	layer
																			.close(index);
																});
											} else {
												layer
														.alert('<span style="color:red;">只有"已保存","已通知"状态的直发通知才能作废</sapn>');
											}
										} else {
											layer
													.alert('<span style="color:red;">请选择一条或多条数据进行提交</sapn>');
										}
									});

				});

function initStraMessTable() {
	table.render({
		elem : "#straMessTable",
		url : "/queryStraMessageByPage",
		page : true,
		width : '100%',
		minHeight : '20px',
		limit : 10,
		id : "straMessTableId",
		cols : [ [ {
			checkbox : true,
			fixed : 'left'
		}, {
			field : 'messStatus',
			title : '通知状态',
			align : 'center',
			width : 93
		}, {
			field : 'messCode',
			title : '提货单号',
			align : 'center',
			width : 113
		}, {
			field : 'arriDate',
			title : '到货日期',
			align : 'center',
			width : 106,
			templet : function(d) {
				return formatTime(d.arriDate, 'yyyy-MM-dd');
			}
		}, {
			field : 'oemSupp',
			title : 'OEM供应商',
			align : 'center',
			width : 145
		}, {
			field : 'mateNumber',
			title : '数量/箱',
			align : 'center',
			width : 92
		}, {
			field : 'mateAmount',
			title : '方量',
			align : 'center',
			width : 83
		}, {
			field : 'creator',
			title : '创建人',
			align : 'center',
			width : 109
		}, {
			field : 'createDate',
			title : '创建时间',
			align : 'center',
			width : 106,
			templet : function(d) {
				return formatTime(d.createDate, 'yyyy-MM-dd');
			}
		}, {
			fixed : 'right',
			title : '操作',
			width : 120,
			align : 'center',
			toolbar : '#barDemo'
		} ] ]
	})
}

// 修改预约单的送货时间
function updateDeliDate(map) {
	$.ajax({
		type : "post",
		url : "/updateDeliDate?messCode=" + map.messCode + "&arriDate="
				+ map.arriDate + "&type=" + map.type,
		dataType : "JSON",
		error : function(result) {
			layer.alert("Connection error");
		},
		success : function(data) {
			if (data) {
				initStraMessTable();
				layer.msg("修改成功");
			} else {
				layer.msg("修改失败");
			}
		}
	});
}
