var currentId = "";
layui.use([ 'form', 'table', 'laydate', 'layer', 'upload' ], function() {
	var $ = layui.jquery;
	var prefix = "/purchRecon";
	var mateArr = [];
	var mateTableIns;
	var debitArr = [];
	var direCode = 'CGDZ';
	var docCate = 'bam_purch_recon'
	var debitTableIns;
	var table = layui.table;
	var laydate = layui.laydate;
	var layer = layui.layer;
	var upload = layui.upload;
	var form = layui.form;
	var type = $("#type").val();
	var linkNo = $("#reconCode").val();
	var status = $("#reconStatusDesc").val();
	//采购员确认供应商提交的单据时，按钮控制
	if(status == '待确认'){
		$('#saveBtn').hide();
		$('#submitBtn').hide();
		$('#userConfirmBtn').show();
		$('#userBackBtn').show();
		type=3;
	}
	var invoTableLns;
	if(type!=1){
		$('#invoceDiv').show();
	}
	if(type=='3'){
		$('.suma').css('margin-left','108px');
	}
	if (type != 3) {
		laydate.render({
			elem : '#startDate'
		});
		laydate.render({
			elem : '#endDate'
		});
	} else {
		disableFormItem();

	}
	
	// 加载页面初始化表格
	initDate();
	if (type == '1') {
		initMateTable(mateArr);
		initDebitTable(debitArr);
	} else {
		var reconCode = $("#reconCode").val();
		$.post(prefix + "/getMatesByCode", {
			reconCode : reconCode
		}, function(res) {
			mateArr = res;
			initMateTable(res);
		});
		$.post(prefix + "/getDebitsByCode", {
			reconCode : reconCode
		}, function(res) {
			debitArr = res;
			initDebitTable(res);
		});
		$.post(prefix + "/getInvocesByReconCode", {
			reconCode : reconCode
		}, function(res) {
			initInvoceTable(res);
		});
	}
	// 保存事件
	$("#saveBtn").click(function() {
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		var flag ="";
		if (type == "1") {// 新建保存
			var id = guid();
			$("#id").val(id);
			var suppName = $("#suppName").val();
			//var createDate = $("#createDate").val();
			var remark = "采购对账审核: " + suppName ;
			flag = taskProcess(id, "purcReconAudit", remark, "save");
		} else {// 编辑保存
			flag = "init_success";
		}
		if (flag == "init_success") {
			savePurchRecon();
		}
	});
	// 提交事件
	$("#submitBtn").click(function() {
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		$("#reconStatusDesc").val('已保存');
		$("#reconStatus").val('YBC');
		var id = $("#id").val();
		var flag="";
		if (type == "1") {//新建提交
			id = guid();
			$("#id").val(id);
			flag="process_success";
		} 
		var format = $("#createDate").val();
		var suppName = $("#suppName").val();
		var remark = "采购对账审核: " + suppName ;
		/*if(type != "1"){
			flag = taskProcess(id,"purcReconAudit", remark,"process");
		}
		if (flag == "process_success") {
			savePurchRecon();
			if(type == "1"){
				flag = taskProcess(id,"purcReconAudit", remark,"process");
			}
		}*/
		savePurchRecon();
		var flag = taskProcess(id,"purcReconAudit", remark,"process");
	});
   //在弹窗中选择执行人后，点击确认按钮回调
   window.returnFunction = function() {
		var reconCodes = [];
		var reconCode = $("#reconCode").val();
		reconCodes.push(reconCode);
		var json = JSON.stringify(reconCodes);
		$.post(prefix+"/changeReconStatus",{status:'YTJ',reconCodeJson:json},function(res){
			layer.msg("操作成功！",{time:1000});
//			history.go(-1);
			tuoBack("purchaseEdit","#searchBtn");

		});
	}
   
 //采购员确认供应商提交的采购对账信息
	$("#userConfirmBtn").click(function(){
		if(status!='待确认'){
			layer.msg('只有待确认的对账单才可以确认！',{time:1000});
			return;
		}
		var id = $("#id").val();
		var suppName = $("#suppName").val();
		var remark = "采购对账审核: " + suppName ;
		var flag = taskProcess(id,"purcReconAudit", remark,"process");
		if(flag == "over_success"){
			var reconCodes = [];
			var reconCode = $("#reconCode").val();
			reconCodes.push(reconCode);
			var json = JSON.stringify(reconCodes);
			$.post(prefix+"/changeReconStatus",{status:'YQR',reconCodeJson:json},function(res){
				layer.msg("操作成功！",{time:1000});
				tuoBack("purchaseEdit","#searchBtn");
			});
		}
	});
	//采购员回退
   $("#userBackBtn").click(function(){
   	if(status!='待确认'){
			layer.msg('只有待确认的对账单才可以退回！',{time:1000});
			return;
		}
   	var id = $("#id").val();
   	var result = backProcess(id);
		if(result = "back_success"){
			var reconCodes = [];
			var reconCode = $("#reconCode").val();
			reconCodes.push(reconCode);
			var json = JSON.stringify(reconCodes);
			$.post(prefix+"/changeReconStatus",{status:'YTH',reconCodeJson:json},function(res){
				layer.msg("操作成功！",{time:1000});
				tuoBack("purchaseEdit","#searchBtn");
			});
		}
	});
	// 返回事件
	$("#backBtn").click(function() {
//		history.go(-1);
		tuoBack("purchaseEdit","#searchBtn");
	});
	// 同步按钮点击事件
	$("#synchBtn").click(function() {
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		var suppNo = $("#suppNo").val();
		var plantCode = $("#plantCode").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if (suppNo == null || suppNo == '') {
			layer.msg('请选择供应商', {
				time : 1000
			});
			return;
		}
		if (plantCode == null || plantCode == '') {
			layer.msg('请选择工厂', {
				time : 1000
			});
			return;
		}
		if (startDate == null || startDate == '') {
			layer.msg('请选择开始时间', {
				time : 1000
			});
			return;
		}
		if (endDate == null || endDate == '') {
			layer.msg('请选择结束时间', {
				time : 1000
			});
			return;
		}
		$.ajax({
			url : prefix + "/synchReconMate",
			data : {
				suppNo : suppNo,
				startDate : startDate,
				endDate : endDate,
				plantCode : plantCode
			},
			type : "POST",
			success : function(res) {
				mateArr = res;
				if (res == null || res.length == 0) {
					layer.msg('无同步数据', {
						time : 1000
					});
					initMateTable([]);
					calSumAmount();
					return;
				}
				initMateTable(res);
				calSumAmount();
			}
		});
	});
	// 点击添加扣款信息按钮
	$("#addDebitBtn").click(function() {
		debitArr = debitTableIns.config.data;
		var newRow = {
			id : guid()
		};
		debitArr.push(newRow);
		initDebitTable(debitArr)
	});
	// 点击删除扣款信息按钮
	$("#delDebitBtn").click(function() {
		var checkStatus = table.checkStatus('debit-table');
		var delRows = checkStatus.data;
		deleteRows(debitArr, delRows);
	});
	// 监听表格事件 删除
	table.on('tool(debit-table)', function(obj) {
		var data = obj.data;
		var delRows = [];
		delRows.push(data);
		if (obj.event === 'del') {
			// 删除行
			// obj.del();
			deleteRows(debitArr, delRows);
		} else if (obj.event === 'upload') {
			currentId = data.id;
			$("#uploadBtn").click();
		} else if (obj.event === 'download') {
			// 下载数据
			var attFile = data.attFile;
			if (attFile == undefined) {
				layer.msg("无附件可以下载!");
			} else {
				var atts = JSON.parse(attFile);
				for ( var i = 0; i < atts.length; i++) {
					var elem = atts[i];
					var docId = elem.docId;
					window.location.href = "/doc/downLoadDoc?docId=" + docId
				}
			}
		}
	});
	table.on('edit(debit-table)', function(obj) {
		calSumAmount();
	});

	/**
	 * 保存采购对账单信息
	 * 
	 * @returns
	 */
	function savePurchRecon() {
		var reconStatus = $("#reconStatus").val();
		if (reconStatus == null || reconStatus == '') {
			$("#reconStatus").val('YBC');
		}
		var matesJson = JSON.stringify(mateArr);
		$("#matesJson").val(matesJson);
		var debitsJson = JSON.stringify(debitArr);
		$("#debitsJson").val(debitsJson);
		var options = {
			url : prefix + "/saveRecon",
			type : "POST",
			success : function(msg) {
				if (msg.code == "0") {
					id = msg.data.id;
					$('#id').val(id);
					if (reconStatus == "YTJ") {
						$("#reconStatusDesc").val('已提交');
					} else {
						$("#reconStatusDesc").val('已保存');
					}
					$('#reconCode').val(msg.data.reconCode);
					layer.msg("操作成功！", {
						time : 1000
					});
					type = $('#type').val('2');
				} else {
					layer.msg("操作失败！", {
						time : 1000
					});
				}
			},
			error : function(request) {
				layer.msg("程序出错了！", {
					time : 1000
				});
			}
		};
		$("#recon-form").ajaxSubmit(options);
	}
	// 供应商选择
	form.on('select(suppNo)', function(data) {
		suppId = $("#suppNo").val();
		var suppName = $(data.elem).find("option:selected").text();
		$("#suppName").val(suppName.substr(suppId.length));
	});
	// 工厂选择
	form.on('select(plantCode)', function(data) {
		var plantCode = $("#plantCode").val();
		var plantDesc = $(data.elem).find("option:selected").text();
		$("#plantDesc").val(plantDesc.substr(plantCode.length));
	});
	/**
	 * 删除选中行
	 * 
	 * @returns
	 */
	function deleteRows(allRows, deleteRows) {
		$.each(allRows, function(index, row) {
			$.each(deleteRows, function(index1, deleteRow) {
				if (row.id == deleteRow.id) {
					allRows.splice(index, 1);
				}
			});
		});
		initDebitTable(allRows);
	}
	/**
	 * 初始化创建日期
	 * 
	 * @returns
	 */
	function initDate() {
		var id = $('#id').val();
		if (id == null || id == '') {
			$('#createDate').val(formatTime(new Date(), 'yyyy-MM-dd'));
		}
	}
	/**
	 * 初始化采购对账单物资信息表格
	 * 
	 * @param condition
	 * @returns
	 */
	function initMateTable(data) {
		var totalNum = 0;
		var totalMoney = 0;
		$.each(data, function(index, row) {
			totalNum += parseFloat(row.incomingNum);
			totalMoney += parseFloat(row.incomingTotalPrice);
		});
		$("#totalNum").val(totalNum.toFixed(2));
		$("#totalMoney").val(totalMoney.toFixed(2));
		mateTableIns = table.render({
			elem : '#mate-table',
			data : data,
			page : true,
			cols : [ [ {
				type : 'numbers',
				title : '序号',
				width : 40,
				fixed : 'left'
			}, {
				field : 'incomingDate',
				title : '入库日期',
				width : 120,
				fixed : 'left',
				templet : function(d) {
					return formatTime(d.incomingDate, 'yyyy-MM-dd');
				}
			}, {
				field : 'purchCode',
				title : '采购订单号',
				width : 120,
				fixed : 'left'
			},  {
				field : 'suppRange',
				title : '供应商子范围编码',
				width : 136,
				fixed : 'left'
			}, {
				field : 'suppRangeDesc',
				title : '供应商子范围描述',
				width : 136,
				fixed : 'left'
			}, {
				field : 'mateDesc',
				title : '物料描述',
				width : 180,
				fixed : 'left'
			}, {
				field : 'unitDesc',
				title : '单位',
				width : 100,
				fixed : 'left'
			}, {
				field : 'wareHouse',
				width : 100,
				title : '仓库'
			}, {
				field : 'incomingNum',
				width : 100,
				title : '入库数量'
			}, {
				field : 'taxPrice',
				width : 100,
				title : '含税单价'
			}, {
				field : 'incomingPrice',
				width : 100,
				title : '入库单价',
			}, {
				field : 'incomingTotalPrice',
				width : 100,
				title : '入库总价'
			}, {
				field : 'incomingCode',
				width : 120,
				title : '入库单号'
			}, {
				field : 'factoryPrice',
				width : 120,
				title : '出厂价'
			}, {
				field : 'incomingFreight',
				width : 120,
				title : '入库运费'
			}, {
				field : 'theirCdc',
				width : 120,
				title : '所属CDC'
			}, {
				field : 'innerDeliveryCode',
				width : 120,
				title : '内向交货单号'
			}, {
				field : 'orderReason',
				width : 100,
				title : '订单原因'
			}, {
				field : 'mateCode',
				width : 200,
				title : '物料编码 '
			}, {
				field : 'discountAmount',
				width : 100,
				title : '已折金额 '
			}, {
				field : 'taxCost',
				width : 100,
				title : '折前含税价 '
			}, {
				field : 'loan',
				width : 120,
				title : '借款项 '
			} ] ]
		});
	}
	/**
	 * 计算
	 * 
	 * @returns
	 */
	function calSumAmount() {
		var rows = debitTableIns.config.data;
		var debitMoney = 0;
		$.each(rows, function(index, row) {
			var amount = row.amount;
			if (amount == null || amount == '') {
				amount = 0;
			}
			debitMoney += parseFloat(amount);
		})
		$("#debitMoney").val(debitMoney.toFixed(2));
		var totalMoney = parseFloat($("#totalMoney").val());
		$("#sumAmount").val((totalMoney - debitMoney).toFixed(2));
	}
	/**
	 * 初始化扣款信息表格
	 * 
	 * @param data
	 * @returns
	 */
	function initDebitTable(data) {
		var bar = 'debitOperateBar1';
		if (type != 3) {
			bar = 'debitOperateBar';
		}
		var debitMoney = 0;
		$.each(data, function(index, row) {
			var amount = row.amount;
			if (amount == null || amount == '') {
				amount = 0;
			}
			debitMoney += parseFloat(amount);
		})
		$("#debitMoney").val(debitMoney.toFixed(2));

		debitTableIns = table.render({
			elem : '#debit-table',
			id : 'debit-table',
			data : data,
			page :true,
			limit : 50, // 显示的数量
			cols : [ [ {
				type : 'numbers',
				title : '序号',
			}, {
				field : 'mateDesc',
				edit : 'text',
				title : '物料名称'
			}, {
				field : 'mateCode',
				edit : 'text',
				title : '物料编码'
			}, {
				field : 'amount',
				edit : 'text',
				title : '未税金额',
			}, {
				field : 'debitReason',
				edit : 'text',
				title : '扣款原因',
			}, {
				field : 'remark',
				edit : 'text',
				title : '备注'

			}, {
				field : 'attFile',
				title : '附件',
				templet : function(d) {
					var attFile1 = d.attFile;
					if (attFile1 != undefined) {
						var att = JSON.parse(attFile1);
						var realName = "";
						for ( var i = 0; i < att.length; i++) {
							var elem = att[i];
							if (realName == "") {
								realName =realName+ '<span onclick="downLoadFile(\''+elem.docId+'\')">' + elem.realName + '</span>';
							} else {
								realName =realName+ '<span onclick="downLoadFile(\''+elem.docId+'\')">' + elem.realName + '</span>';
							}
						}
						return realName;
					} else {
						return "<span></span>";
					}
				}
			}, {
				title : '操作',
				align : 'center',
				toolbar : '#' + bar
			} ] ]
		});
	}
	/*
	 * 对账单物料导出
	 */
	$('#exportBtn').click(function(){
		var reconCode=$('#reconCode').val();
		if(reconCode==null || reconCode==''){
			layer.msg('请保存单据后进行导出操作！',{time:1000});
			return;
		}
		window.location.href = prefix+"/exportReconMate?reconCode=" + reconCode
	});

	// 文件上传
	var uploadInst = upload.render({
		elem : '#uploadBtn',
		url : '/doc/docUpload?direCode=' + direCode + "&docCate=" + docCate
				+ "&linkNo=" + linkNo,
		multiple : true,
		accept: 'file',
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
				for ( var int = 0; int < debitArr.length; int++) {
					var elem = debitArr[int];
					if (currentId == elem.id) {
						var old = elem.attFile;
						if (old != undefined) {
							var oldJson = JSON.parse(old);
							if (oldJson.length > docJson) {
								for ( var k = 0; k < docJson.length; k++) {
									var elem1 = docJson[k];
									oldJson.push(elem1);
								}
								elem.attFile = JSON.stringify(oldJson);
							} else {
								for ( var j = 0; j < oldJson.length; j++) {
									var elem2 = oldJson[j];
									docJson.push(elem2);
								}
								elem.attFile = JSON.stringify(docJson);
							}
						} else {
							elem.attFile = JSON.stringify(docJson);
						}
						break;
					}
				}
				initDebitTable(debitArr);
			}
		},
		error : function() {
			// 请求异常回调
		}
	});

	/**
	 * 发票信息
	 */
	function initInvoceTable(data) {
		invoTableLns=table.render({
			data : data,
			page :true,
			elem : "#invoce-table",
			cols : [ [ {
				field : 'invoceNo',
				title : '发票号'
			}, {
				field : 'taxMoney',
				title : '税金'
			}, {
				field : 'totalMoney',
				title : '总额'
			}, {
				field : 'noTaxMoney',
				title : '不含税金额',
			},{
				field : 'attFile',
				title : '附件',
				templet : function(d) {
					var attFile1 = d.attFile;
					if (attFile1 != undefined) {
						var att = JSON.parse(attFile1);
						var realName = "";
						for ( var i = 0; i < att.length; i++) {
							var elem = att[i];
							if (realName == "") {
								realName =realName+ '<span onclick="downLoadFile(\''+elem.docId+'\')">' + elem.realName + '</span>';
							} else {
								realName =realName+ '<span onclick="downLoadFile(\''+elem.docId+'\')">' + elem.realName + '</span>';
							}
						}
						return realName;
					} else {
						return "<span></span>";
					}
				}
			}] ]
		});
		calInvoMoney();
	}
	
	function calInvoMoney(){
		var data =invoTableLns.config.data;
		var sumTaxMoney=0;
		var sumTotalMoney=0;
		for (var i = 0; i < data.length; i++) {
			var row=data[i];
			var taxMoney=row.taxMoney;
			var totalMoney=row.totalMoney;
			if(!isNaN(taxMoney)){
				sumTaxMoney+=parseFloat(taxMoney);
			}
			if(!isNaN(totalMoney)){
				sumTotalMoney+=parseFloat(totalMoney);
			}
		}
		$('#sumTaxMoney').val(sumTaxMoney.toFixed(2));
		$('#sumNoTaxMoney').val((sumTotalMoney-sumTaxMoney).toFixed(2));
		$('#sumTotalMoney').val(sumTotalMoney.toFixed(2));
	}
	
	
})





function downLoadFile(docId){
	window.location.href = "/doc/downLoadDoc?docId=" + docId
}