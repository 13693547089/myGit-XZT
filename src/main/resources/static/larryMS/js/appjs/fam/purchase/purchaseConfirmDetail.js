layui.use([ 'table', 'layer', 'upload' ], function() {
	var $ = layui.jquery;
	var prefix = "/purchRecon";
	var mateArr = [];
	var debitArr = [];
	var invoiceArr = [];
	var table = layui.table;
	var layer = layui.layer;
	var upload = layui.upload;
	var direCode = 'CGDZ';
	var docCate = 'bam_purch_recon'
	var type = $("#type").val();
	var linkNo = $("#reconCode").val();
	var invoTableLns;
	var currentId = "";
	// 根据类型判断是确认还是查看
	if (type == '2') {
		$('.disableBtn').show();
	}

	// 加载页面初始化表格
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
		invoiceArr = res;
		initInvoiveTable();
	});
	// 提交点击事件
	$("#confirmBtn").click(function() {
		var status=$("#reconStatusDesc").val();
		if(status=='待确认'){
			layer.msg('待确认的对账单不能重复提交！',{time:1000});
			return;
		}
		if(invoiceArr==null || invoiceArr.length==0){
			layer.msg('请填写发票信息后提交！',{time:1000});
			return;
		}
		var reconCode = $("#reconCode").val();
		var id = $("#id").val();
		//var format = $("#createDate").val();
		var suppName = $("#suppName").val();
		var remark = "采购对账审核: " + suppName;
		var json = JSON.stringify(invoiceArr);
		$.post(prefix + "/confirmReconInfo", {
			reconCode : reconCode,
			invoice : json
		}, function(res) {
			layer.msg("操作成功！", {
				time : 1000
			});
			//$("#reconStatusDesc").val('待确认');
			//tuoBack("purchaseConfirmDetail","#searchBtn");
			var flag = taskProcess(id,"purcReconAudit", remark,"process");
		});
	});
	
	//在弹窗中选择执行人后，点击确认按钮回调
	   window.returnFunction = function() {
			debugger
			var reconCodes = [];
			var reconCode = $("#reconCode").val();
			reconCodes.push(reconCode);
			var json = JSON.stringify(reconCodes);
			$.post(prefix+"/changeReconStatus",{status:"DQR",reconCodeJson:json},function(res){
				layer.msg("操作成功！",{time:1000});
				tuoBack("purchaseConfirmDetail","#searchBtn");
			});
	  }
	// 返回事件
	$("#backBtn").click(function() {
//		history.go(-1);
		tuoBack("purchaseConfirmDetail","#searchBtn");
	});
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
			page :true,
			cols : [ [ {
				type : 'numbers',
				title : '序号',
				width : 40,
				fixed : 'left'
			}, {
				field : 'incomingDate',
				title : '入库日期',
				width : 100,
				fixed : 'left',
				templet : function(d) {
					return formatTime(d.incomingDate, 'yyyy-MM-dd');
				}
			}, {
				field : 'purchCode',
				title : '采购订单号',
				width : 100,
				fixed : 'left'
			}, {
				field : 'suppRange',
				title : '供应商子范围编码',
				width : 136,
				fixed : 'left'
			}, {
				field : 'suppRangeDesc',
				title : '供应商子范围描述',
				width : 136,
				fixed : 'left'
			},{
				field : 'mateDesc',
				title : '物料描述',
				width : 100,
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
				width : 100,
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
				width : 100,
				title : '物料编码 '
			}, {
				field : 'discountAmount',
				width : 100,
				title : '以折金额 '
			}, {
				field : 'taxCost',
				width : 100,
				title : '折前含税价 '
			}, {
				field : 'loan',
				width : 120,
				title : '借款项 '
			}] ]
		});
	}
	/**
	 * 初始化扣款信息表格
	 * 
	 * @param data
	 * @returns
	 */
	function initDebitTable(data) {

		var debitMoney = 0;
		$.each(data, function(index, row) {
			debitMoney += parseFloat(row.amount);
		});
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
				title : '物料名称'
			}, {
				field : 'mateCode',
				title : '物料编码'
			}, {
				field : 'amount',
				title : '未税金额',
			}, {
				field : 'debitReason',
				title : '扣款原因',
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
				field : 'remark',
				title : '备注'
			} ] ]
		});

		$(".debitDiv").children('div.layui-form').children(
				'div.layui-table-box').children('div.layui-table-main').css(
				'overflow', 'hidden');
	}
	// 扣款信息下载
	table.on('tool(debit-table)', function(obj) {
		var data = obj.data;
		if (obj.event === "download") {
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
	// 添加发票信息
	$("#addInvoiceBtn").on('click', function() {
		var arr = {
			id : guid()
		};
		invoiceArr.push(arr);
		initInvoiveTable();
	});
	// 监听发票的操作事件
	table.on('tool(invoice-table)', function(obj) {
		var data = obj.data;
		if (obj.event === "upload") {
			currentId = data.id;
			$("#uploadBtn").click();
		} else if (obj.event === "download") {
			// 下载数据
			var attFile = data.attFile;
			if (attFile == undefined) {
				layer.msg("无附件可以下载!");
			} else {
				var att = JSON.parse(attFile);
				var docId = att.docId;
				window.location.href = "/doc/downLoadDoc?docId=" + docId
			}
		} else if (obj.event === "del") {
			var id = data.id;
			var tempData = []
			for ( var k = 0; k < invoiceArr.length; k++) {
				var elem = invoiceArr[k];
				if (elem.id !== id) {
					tempData.push(elem);
				}
			}
			invoiceArr = tempData;
			initInvoiveTable();
		}if(obj.event=== 'myEdit'){ 
			obj.update({
				noTaxMoney: data.noTaxMoney
			});
		 }
	});

	// 渲染发票表格
	function initInvoiveTable() {
		var bar = ""
		if (type == '2') {
			// 确认
			bar = {title : '操作',toolbar : '#operateBar2' };
		}
		invoTableLns=table.render({
			elem : '#invoice-table',
			id : 'invoice-table',
			data : invoiceArr,
			page :true,
			limit : 50, // 显示的数量
			cols : [ [ {
				type : 'numbers',
				title : '序号',
			}, {
				field : 'invoceNo',
				edit : 'text',
				title : '发票号'
			}, {
				field : 'taxMoney',
				edit : 'text',
				event :'myEdit',
				title : '税金'
			}, {
				field : 'totalMoney',
				edit : 'text',
				event :'myEdit',
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
			}, bar] ]
		});
		
		if(type!=2){
			$('td').removeAttr('data-edit');
		}
		calInvoMoney();
	}
	//计算发票的合计值
	table.on('edit(invoice-table)', function(obj){ 
		calInvoMoney();
		var data=obj.data;
		var totalMoney=data.totalMoney;
		if(totalMoney==null || isNaN(totalMoney)){
			totalMoney=0
		}
		var taxMoney=data.taxMoney;
		if(taxMoney==null|| isNaN(taxMoney)){
			taxMoney=0
		}
		data.noTaxMoney=(totalMoney-taxMoney).toFixed(2);
		$(this).click();
	});
	
	
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
	
	
	
	
	// 文件上传
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
				for ( var int = 0; int < invoiceArr.length; int++) {
					var elem = invoiceArr[int];
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
				initInvoiveTable(debitArr);
			}
		},
		error : function() {
			// 请求异常回调
		}
	});
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
})

function downLoadFile(docId){
	window.location.href = "/doc/downLoadDoc?docId=" + docId
}
