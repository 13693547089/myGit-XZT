var currentMouldId = "";
var mateData = [];
var mouldData = [];
var tt;
var status;
var auditStatus;
layui.use([ 'form', 'table', 'upload' ], function() {
	var form = layui.form;
	var table = layui.table;
	tt = table;
	var upload = layui.upload;
	var $ = layui.jquery;
	var approve = $("#approve").val();
	if (approve != undefined && approve != "") {
		var data = JSON.parse(approve);
		loadApproveTable(table, data);
	}

	status = $("#status").val();
	auditStatus = $("#auditStatus").val();
	var pagePattern = $("#pagePattern").val();
	if(pagePattern == 'read'){
		auditStatus='已提交';
	}
	var auditId = '';
	getMateData(table);
	loadMouldTable(table);
	if (auditStatus == '已提交') {
		disableFormItem();
		$('table td').removeAttr('data-edit');
	}
	// 执行实例
	var auditId = $("#id").val();
	if (auditId == undefined || auditId == "") {
		auditId = guid();
		$("#id").val(auditId);
	}
	var auditNo = $("#auditCode").val();
	var uploadInst = upload.render({
		elem : '#uploadBtn',
		url : '/doc/docUpload?direCode=' + 'CWJHGL' + '&linkNo=' + auditNo
				+ "&linkId=" + auditId + "&docCate=" + 'doc_audit',
		multiple : true,
		done : function(res) {
			// 上传完毕回调
			var result = res.data;
			var docJson = [];
			var realName = "";
			for ( var k = 0; k < result.length; k++) {
				var r = result[k];
				var arr = {
					'fileName' : r.fileName,
					'fileUrl' : r.fileUrl,
					'docId' : r.id,
					'realName' : r.realName
				};
				if (k == 0) {
					realName += r.realName;
				} else {
					realName += "," + r.realName;
				}
				docJson.push(arr);
			}
			if (docJson.length > 0) {
				for ( var int = 0; int < mouldData.length; int++) {
					var elem = mouldData[int];
					if (currentMouldId == elem.id) {
						var old = elem.imgUrl;
						if (old != undefined) {
							debugger;
							var oldJson = JSON.parse(old);
							if (oldJson.length > docJson) {
								for ( var k = 0; k < docJson.length; k++) {
									var elem1 = docJson[k];
									oldJson.push(elem1);
								}
								elem.imgUrl = JSON.stringify(oldJson);
							} else {
								for ( var j = 0; j < oldJson.length; j++) {
									var elem2 = oldJson[j];
									docJson.push(elem2);
								}
								elem.imgUrl = JSON.stringify(docJson);
							}
							var oldName = elem.imgName;
							elem.imgName = realName += "," + oldName;
						} else {
							elem.imgUrl = JSON.stringify(docJson);
							elem.imgName = realName;
						}

					}
				}
				loadMouldData(table, mouldData);
			}
		},
		error : function() {
			// 请求异常回调
		}
	});

	// 监听单元格编辑时间，计算月末库存
	table.on('edit(mateTable)', function(obj) {
		var rows = obj.data;
		// 上月结存+本月入库-本月出库-脱普损耗-供应商损耗+本月补损
		var r = Number(rows.lastMonthBala) + Number(rows.outWarehouse)
				- Number(rows.inWarehouse) - Number(rows.topLoss)
				- Number(rows.suppLoss) + Number(rows.addLoss);
		if (isNaN(r)) {
			layer.msg("请输入有效数字!");
			rows[obj.field] = 0;
		} else {
			rows.monthStock = r;
		}
		loadMateTable(table);
	});

	// 监听下载图片事件
	table.on('tool(mouldTable)', function(obj) {
		debugger;
		var url = currentData.imgUrl;
		if (url != undefined && url != "") {
			var json = JSON.parse(url);
			if (json.length > 0) {
				var docId = json[0].docId;
				window.location.href = "/doc/downLoadDoc?docId=" + docId
			}
		} else {
			layer.msg("没有附件可下载！");
		}

	});

	// 监听模具信息表
	table.on('edit(mouldTable)', function(obj) {
		var rows = obj.data;
		var field = obj.field;
		var val = obj.value;
		if (field == "qualRate") {
			// 校验生产合格率是否为0-100的数字
			if (isNaN(val)) {
				layer.msg("请输入有效数字！");
				rows[field] = 0;
				loadMouldData(table, mouldData);
			} else {
				var n = Number(val);
				if (n < 0 || n > 100) {
					layer.msg("请输入0~100的数字");
					rows[field] = 0;
					loadMouldData(table, mouldData);
				}
			}
		}
	});

	$("#addMateBtn").on(
			'click',
			function() {
				var suppId = $("#suppId").val();
				var selectedIds = "";
				if (mateData.length > 0) {
					for ( var k = 0; k < mateData.length; k++) {
						var elem = mateData[k];
						selectedIds += elem.mateId + ",";
					}
				}
				if (suppId != undefined && suppId != '') {
					layer.open({
						type : 2,
						title : '物料选择',
						shadeClose : false,
						shade : 0.1,
						maxmin : true, // 开启最大化最小化按钮
						area : [ '900px', '500px' ],
						content : '/finance/chooseMate?suppId=' + suppId
								+ "&selected=" + selectedIds,
						btn : [ '确定', '取消' ],
						yes : function(index, layero) {
							// 确定按钮
							var iframe = $(layero).find("iframe")[0];
							var data = iframe.contentWindow.getMateData();
							mateData = [];
							if (data.length > 0) {
								for ( var j = 0; j < data.length; j++) {
									var elem = data[j];
									//debugger;
									var lastMonthBala =0;
									$.ajax({
										type:"post",
										url:"/finance/queryLastMonthBala?suppId="+suppId+"&mateId="+elem.mateId,
										dataType:"JSON",
										async:false,//注意
										error : function(request) {
											 layer.alert("Connection error");
										},
										success:function(map){
											if(map.judge){
												lastMonthBala = map.auditMate.monthStock;
											}
										}
									});
									var arr = {
										mateId : elem.mateId,
										mateCode : elem.mateCode,
										mateName : elem.mateName,
										lastMonthBala : lastMonthBala,
										outWarehouse : '',
										inWarehouse : '',
										topLoss : '',
										suppLoss : '',
										noAdd : '',
										addLoss : '',
										monthStock : lastMonthBala
									}
									mateData.push(arr);
								}
							}
							loadMateTable(table);
							layer.close(index);
						},
						btn2 : function(index, layero) {
							// 取消按钮
						}
					});
				} else {
					layer.msg("请选择供应商！");
				}

			});

	// 监听table的操作
	table.on('tool(mouldTable)', function(obj) {
		var currentData = obj.data;
		if (obj.event === 'upload') {
			// 上传
			currentMouldId = currentData.id;
			$("#uploadBtn").click();
		} else if (obj.event === 'del') {
			// 删除
			layer.confirm('确定要删除选择的数据？', {
				btn : [ '确认', '取消' ]
			}, function(index) {
				var tableData = [];
				for ( var k = 0; k < mouldData.length; k++) {
					var elem = mouldData[k];
					if (elem.id != currentData.id)
						tableData.push(elem);
				}
				mouldData = tableData;
				loadMouldData(table, mouldData);
				layer.close(index);
			});
		} else if (obj.event == 'download') {
			debugger;
			$(this).context.localName
			if($(this).context.localName=='td'){
				 var ImgUrllist = JSON.parse(currentData.imgUrl);
					var Img=''
						for(var i =0;i<ImgUrllist.length;i++){
							var ImgUrl='ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121'+ImgUrllist[i].fileUrl+'/'+ImgUrllist[i].fileName;
							Img+="<div style='display:inline-block;'class='Div'><img src="+ImgUrl+" style='width:100px;height:100px;'></img><span style='display:none;'class='DocId'>"+ImgUrllist[i].docId+"</span><button type='button' class='Download layui-btn layui-btn-xs blueHollow' style='display:block;margin:0px auto;margin-top:5px;'>下载</button></div>"
						}
					$("#ExhiImg").children('.Div').detach();
					$('#ExhiImg').append(Img);
					$('#ExhiImg').show();
			}
//			var url = currentData.imgUrl;
//			if (url != undefined && url != "") {
//				var json = JSON.parse(url);
//				if (json.length > 0) {
//					var docId = json[0].docId;
//					window.location.href = "/doc/downLoadDoc?docId=" + docId
//				}
//			} else {
//				layer.msg("没有附件可下载！");
//			}
			//下载照片
			$('.Div').on('click','.Download',function(){
				
				var docId = $(this).prev('.DocId').text();
				window.location.href = "/doc/downLoadDoc?docId=" + docId
			})
		}
	});

	// 添加模型
	$("#addMouldBtn").on('click', function() {
		var arr = {
			id : guid(),
			mouldName : '',
			mouldNum : 0,
			mouldStatus : '',
			storePlace : '',
			qualRate : 0,
			belongRight : '',
			imgName : '',
			remark : ''
		}
		mouldData.push(arr);
		loadMouldData(table, mouldData);
	});

	// 删除物料数据
	$("#delMateBtn").on('click', function() {
		var checkStatus = table.checkStatus('mateTable');
		var data = checkStatus.data;
		if (data.length > 0) {
			for ( var i = 0; i < mateData.length; i++) {
				var elem = mateData[i];
				if (elem.LAY_CHECKED) {
					mateData.splice(i, 1);
					i--;
				}
			}
			loadMateTable(table);
		} else {
			layer.msg("请选择要删除的数据！");
		}
	});
	//保存
	$("#saveBtn").on('click', function() {
		$("#mateList").val(JSON.stringify(mateData));
		$("#mouldList").val(JSON.stringify(mouldData));
		$("#auditStatus").val('已保存');
		var data = $("#signForm").serialize();
		$.ajax({
			url : '/finance/saveAuditData',
			data : data,
			type : 'post',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					window.history.back(-1);
				}
			}
		});
	});
	// 提交数据，校验数据完整性
	$("#submitBtn").on('click', function() {
		var msg = "";
		// 校验模具数据完整性
		for ( var i = 0; i < mouldData.length; i++) {
			var elem = mouldData[i];
			if (elem.mouldName == undefined || elem.mouldName == '') {
				msg = "请补充完善模具信息！";
				break;
			}
			if (elem.mouldNum == 0) {
				msg = "请补充完善模具信息！";
				break;
			}
			if (elem.mouldStatus == undefined || elem.mouldStatus == '') {
				msg = "请补充完善模具信息！";
				break;
			}
			if (elem.storePlace == undefined || elem.storePlace == "") {
				msg = "请补充完善模具信息！";
				break;
			}
			if (elem.qualRate == 0) {
				msg = "请补充完善模具信息！";
				break;
			}
			if (elem.belongRight == '') {
				msg = "请补充完善模具信息！";
				break;
			}
			if (elem.imgName == "" || elem.imgName == null) {
				msg = "请补充完善模具信息！";
				break;
			}
		}
		if (msg == "") {
			var taskName = $("#taskName").val();
			var processCode = $("#processCode").val();
			//var flag = taskProcess(auditId, processCode, taskName, "process");
			//if(flag == "process_success"){
				$("#mateList").val(JSON.stringify(mateData));
				$("#mouldList").val(JSON.stringify(mouldData));
				$("#auditStatus").val('已保存');
				var data = $("#signForm").serialize();
				$.ajax({
					url : '/finance/saveAuditData',
					data : data,
					type : 'post',
					success : function(r) {
						if (r.code == 0) {
							var flag = taskProcess(auditId, processCode, taskName, "process");
							//layer.msg(r.msg);
							//window.history.back(-1);
						}else{
							layer.msg(r.msg);
						}
					}
				});
			//}
		} else {
			layer.msg(msg);
		}
	});
	//在弹窗中选择执行人后，点击确认按钮回调
	   window.returnFunction = function() {
			debugger
			var auditStatus = '已提交';
			$.ajax({
				type:"post",
				url:"/finance/updateAuditSatusByAuditId?auditStatus="+encodeURIComponent(auditStatus)+"&auditId="+auditId,
				dataType:"JSON",
				error:function(request){
					layer.alert("Connection error");
				},
				success:function(data){
					if(data){
						layer.msg("提交成功");
						window.history.back(-1);
					}else{
						layer.msg("提交失败！");
					}
				}
			});
		}
	//确认
	$("#confirmBtn").on('click', function() {
		var auditId = $("#id").val();
		var taskName = $("#taskName").val();
		var processCode = $("#processCode").val();
		var flag = taskProcess(auditId, processCode, taskName, "process");
		if(flag == "over_success"){
			$.ajax({
				url : '/finance/auditConfirm?auditIds=' + auditId + '&type=single',
				method : 'post',
				async : false,
				success : function(res) {
					layer.msg(res.msg);
					if (res.code == "0") {
						window.history.back(-1);
					}
				}
			});
		}
	});
	// 退回
	$("#rejectBtn").on('click', function() {
		layer.open({
			type : 2,
			title : '退回信息',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '500px', '220px' ],
			content : '/finance/rejectInfo',
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				var data = $(layero).find("iframe")[0].contentWindow.getData();
				if (data != "") {
					// 退回操作
					var auditId = $("#id").val();
					var result = backProcess(auditId);
					if(result == "back_success"){
						var params = "?auditId=" + auditId + "&apprIdea=" + data
						var url = "/finance/auditReject" + params;
						$.ajax({
							url : url,
							method : 'post',
							async : false,
							success : function(res) {
								layer.msg(res.msg);
								if (res.code == "0") {
									layer.close(index);
									window.history.back(-1);
								}
							}
						
						});
					}
				} else {
					layer.msg("请输入退回意见！");
				}
			},
			btn2 : function(index, layero) {
				layer.close(index);
			}
		});
	});
	
	$("#backBtn").on('click', function() {
		window.history.back(-1);
	});

	// 所属权
	form.on('switch(belongRightDemo)', function(obj) {
		for ( var i = 0; i < mouldData.length; i++) {
			var elem = mouldData[i];
			if (elem.id == this.value) {
				if (obj.elem.checked) {
					elem[this.name] = 'T';
				} else {
					elem[this.name] = 'S';
				}
				return;
			}
		}
	});
	form.on('radio(testDemo)', function(obj) {
		for ( var i = 0; i < mouldData.length; i++) {
			var elem = mouldData[i];
			if (elem.id == obj.elem.name) {
				elem.mouldStatus = this.value;
				return;
			}
		}
	});
	$('.Close').click(function(){
		$('#ExhiImg').hide();
		
	})
	
});
function loadMouldTable(table) {
	var auditId = $("#id").val();
	if (status == 2) {
		// 根据稽核单过滤数据
		table.render({
			elem : '#mouldTable',
			url : '/finance/getMouldData?auditId=' + auditId,
			page : true,
			cols : [ [ {
				type : 'numbers'
			}, {
				field : 'mouldName',
				title : '模具名称'
			}, {
				field : 'mouldNum',
				title : '数量'
			}, {
				field : 'mouldStatus',
				title : '状态',
				width : 200,
				templet : '#mouldStatus'
			}, {
				field : 'storePlace',
				title : '存放地方'
			}, {
				field : 'qualRate',
				title : '生产合格率%'
			}, {
				field : 'belongRight',
				title : '归属权',
				templet : '#belongRight'
			}, {
				field : 'imgName',
				title : '模具图片',
				event : 'download',
				templet:function(d){
					debugger;
					if(d.imgUrl!=null && d.imgUrl!=undefined && d.imgUrl!=''){
						var ImgUrllist = JSON.parse(d.imgUrl)
						var Img=''
						for(var i =0;i<ImgUrllist.length;i++){
							var ImgUrl='ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121'+ImgUrllist[i].fileUrl+'/'+ImgUrllist[i].fileName;
							Img+="<img src="+ImgUrl+" style='width:50px;'></img>"
						}
						return Img;
					}
				}
			}, {
				field : 'remark',
				title : '备注'
			} ] ],
			done : function() {
				$('.mstatus').attr('disabled', true);
			}
		});
	} else {
		if (status == '1') {
			$.ajax({
				url : '/finance/getMouldData',
				data : {
					auditId : auditId
				},
				type : 'get',
				async : false,
				success : function(res) {
					mouldData = res.data;
				}
			});
		}
		if(auditStatus == '已提交'){
			loadMould(table,mouldData);
		}else{
			loadMouldData(table, mouldData);
		}
	}
};

function loadMouldData(table, data) {
	debugger;
	table.render({
		elem : '#mouldTable',
		data : data,
		page : true,
		cols : [ [ {
			type : 'numbers'
		}, {
			field : 'mouldName',
			title : '模具名称',
			edit : 'text'
		}, {
			field : 'mouldNum',
			title : '数量',
			edit : 'text'
		}, {
			field : 'mouldStatus',
			title : '状态',
			width : 200,
			templet : '#mouldStatus'
		}, {
			field : 'storePlace',
			title : '存放地方',
			edit : 'text'
		}, {
			field : 'qualRate',
			title : '生产合格率%',
			edit : 'text'
		}, {
			field : 'belongRight',
			title : '归属权',
			templet : '#belongRight'
		}, {
			field : 'imgName',
			title : '模具图片',
			event : 'download',
			templet:function(d){
				debugger;
				if(d.imgUrl!=null && d.imgUrl!=undefined && d.imgUrl!=''){
					var ImgUrllist = JSON.parse(d.imgUrl)
					var Img=''
					for(var i =0;i<ImgUrllist.length;i++){
						var ImgUrl='ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121'+ImgUrllist[0].fileUrl+'/'+ImgUrllist[0].fileName;
						Img+="<img src="+ImgUrl+" style='width:50px;'></img>"
					}
					return Img;
				}
			}
		}, {
			field : 'remark',
			title : '备注',
			edit : 'text'
		}, {
			fixed : 'right',
			title : '操作',
			width : 150,
			align : 'center',
			toolbar : '#operateBar'
		} ] ]
	});
};

function getMateData(table) {
	var auditId = $("#id").val();
	if (auditId != undefined && auditId != '')
		$.ajax({
			url : '/finance/getMateData?auditId=' + auditId,
			async : false,
			method : 'get',
			success : function(res) {
				mateData = res.data;
			}
		});
	loadMateTable(table);
}
function loadMateTable(table) {
	table.render({
		elem : '#mateTable',
		data : mateData,
		page : true,
		cols : [ [ {
			type : 'numbers',
			title : '序号',
			fixed : 'center'
		}, {
			checkbox : true,
			fixed : 'center'
		}, {
			field : 'mateCode',
			title : '物料编码'
		}, {
			field : 'mateName',
			title : '物料名称'
		}, {
			field : 'lastMonthBala',
			title : '上月结存',
			edit : 'text'
		}, {
			field : 'outWarehouse',
			title : '本月入库',
			edit : 'text'
		}, {
			field : 'inWarehouse',
			title : '本月出库',
			edit : 'text'
		}, {
			field : 'topLoss',
			title : '脱普损耗',
			edit : 'text'
		}, {
			field : 'suppLoss',
			title : '供应商损耗',
			edit : 'text'
		}, {
			field : 'noAdd',
			title : '累计未补',
			edit : 'text'
		}, {
			field : 'addLoss',
			title : '本月补损',
			edit : 'text'
		}, {
			field : 'monthStock',
			title : '月末库存',
			edit : 'text'
		} ] ],
		id : 'mateTable'
	});
}

function test(val) {
	for ( var k = 0; k < val.length; k++) {
		var elem = val[k];
		var flag = true;
		for ( var i = 0; i < mateData.length; i++) {
			var elem1 = mateData[i];
			if (elem.mateCode == elem1.mateCode) {
				flag = false;
				return;
			}
		}
		if (flag) {
			var auditId = "";
			var arr = {
				addLoss : 0,
				auditId : auditId,
				id : guid(),
				inWarehouse : 0,
				lastMonthBala : 0,
				mateCode : elem.mateCode,
				mateId : elem.mateId,
				mateName : elem.mateName,
				monthStock : 0,
				noAdd : 0,
				outWarehouse : 0,
				suppLoss : 0,
				topLoss : 0
			}
			mateData.push(arr);
		}
	}
	loadMateTable(tt);
}
function loadApproveTable(table, data) {
	table.render({
		elem : '#approveTable',
		data : data,
		page : true,
		cols : [ [ {
			field : 'apprName',
			title : '审批人姓名'
		}, {
			field : 'apprStatus',
			title : '审批状态'
		}, {
			field : 'apprIdea',
			title : '审批意见'
		}, {
			field : 'apprTime',
			title : '审批时间',
			templet : function(d) {
				return formatTime(d.apprTime, 'yyyy-MM-dd');
			}
		} ] ]
	});
};
// 生成GUID
function guid() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}

function loadMould(table,data){
	table.render({
		elem : '#mouldTable',
		data : data,
		page : true,
		cols : [ [ {
			type : 'numbers'
		}, {
			field : 'mouldName',
			title : '模具名称'
		}, {
			field : 'mouldNum',
			title : '数量'
		}, {
			field : 'mouldStatus',
			title : '状态',
			templet : function(d) {
				if (d.mouldStatus == "1") {
					return "<span>废弃</span>";
				} else if (d.mouldStatus == "2") {
					return "<span>启用</span>";
				} else if (d.mouldStatus == "3") {
					return "<span>闲置</span>";
				}
			}
		}, {
			field : 'storePlace',
			title : '存放地方'
		}, {
			field : 'qualRate',
			title : '生产合格率'
		}, {
			field : 'belongRight',
			title : '归属权',
			templet : function(d) {
				if (d.belongRight == "T") {
					return "<span>脱普</span>";
				} else if (d.belongRight = "S") {
					return "<span>供应商</span>";
				}
			}
		}, {
			field : 'imgName',
			title : '模具图片',
			event : 'download',
			templet:function(d){
				debugger;
				if(d.imgUrl!=null && d.imgUrl!=undefined && d.imgUrl!=''){
					var ImgUrllist = JSON.parse(d.imgUrl)
					var Img=''
					for(var i =0;i<ImgUrllist.length;i++){
						var ImgUrl='ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121'+ImgUrllist[i].fileUrl+'/'+ImgUrllist[i].fileName;
						Img+="<img src="+ImgUrl+" style='width:50px;'></img>"
					}
					return Img;
				}
			}
		}, {
			field : 'remark',
			title : '备注'
		} ] ]
	});
}
