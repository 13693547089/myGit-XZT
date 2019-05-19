var planDetailArr = [];
//查看详情信息是否修改了 默认没有修改  修改的数据为0条
var modifyCount=0;
var type;
var prefix = "/invenPlan";
layui.use([ 'form', 'table', 'laydate', 'layer', 'element' ], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate = layui.laydate;
	var element = layui.element;
	var layer = layui.layer;
	var form = layui.form;
	 type = $("#type").val();
	var mainId = $("#id").val();
	var isChange = false;
	if (type == 3) {
		$("input").attr("readOnly","readOnly");
		$(".disableBtn").css('display','none');
		//disableFormItem();
	} else {
		laydate.render({
			elem : '#planMonth',
			type : 'month',
			done : function(value, date, endDate) {
				var oldPlanMonth = $("#planMonth").val();
				if (oldPlanMonth !== value) {
					planDetailArr = [];
					initInvenPlanDtetailTable(table);
					initalMateTable(value);
					initalSuppTable(value);
					//初始化物料下拉框
					inintMateSelect(value);
				}
			}
		});
	}
	if (type !== "1") {
		$.ajax({
			url : prefix + "/getPlanDetailByMainId",
			method : "get",
			data : {
				mainId : mainId
			},
			async : false,
			success : function(res) {
				planDetailArr = res;
			}
		});
	}
	/**
	 * 从新初始化物料多选下拉框
	 * @returns
	 */
	function inintMateSelect(planMonth){
		if(planMonth!=null || planMonth!=''){
			$.post(prefix+"/getMateSelectList",{planMonth:planMonth},function(res){
				var str='<option value="">请选择</option>';
				$.each(res,function(index,row){
					str+='<option value="'+row.mateCode+'">'+row.mateName+'</option>';
				});
				$("#mateDesc").html(str);
				//重新初始化下拉菜单
				form.render('select');
				form.render("checkbox");
			});
		}
	}
	//-----------------表格检索开始----------------------
	//检索确定后修改主页面的检索条件
	window.updateSearchCond=function(elemId,val){
		$("#"+elemId).val(val);
	}
	
	$('#itemSearchBtn').click(function(){
		var planCode=$('#planCode').val();
		if(planCode==null || planCode==''){
			layer.msg('请先生成并保存备货计划信息！',{time:1000});
			return ;
		}
		var planMonth=$('#planMonth').val();
		//获取已经选中的物料
		var selectItemCodes=$('#selectItemCodes').val();
		if(selectItemCodes==null || selectItemCodes==""){
			selectItemCodes=[];
			selectItemCodes=JSON.stringify(selectItemCodes);
		}
		selectItemCodes =escape(selectItemCodes);
		layer.open({
			type:2,
			shade:false,
			area:['800px','600px'],
			title:'请选择物料',
			maxmin:true,
			content:prefix+"/geItemSearchDg?selectItemCodes="+selectItemCodes+"&planCode="+planCode+"&planMonth="+planMonth,
		});
	});
	
	$('#mateSearchBtn').click(function(){
		var planCode=$('#planCode').val();
		if(planCode==null || planCode==''){
			layer.msg('请先生成并保存备货计划信息！',{time:1000});
			return ;
		}
		var planMonth=$('#planMonth').val();
		//获取已经选中的物料
		var selectMateCodes=$('#mateDesc').val();
		if(selectMateCodes==null || selectMateCodes==""){
			selectMateCodes=[];
			selectMateCodes=JSON.stringify(selectMateCodes);
		}
		selectMateCodes =escape(selectMateCodes);
		layer.open({
			type:2,
			shade:false,
			area:['800px','600px'],
			title:'请选择物料',
			maxmin:true,
			content:prefix+"/getMateSearchDg?selectMateCodes="+selectMateCodes+"&planCode="+planCode+"&planMonth="+planMonth,
		});
	});
	
	$('#seriesSearchBtn').click(function(){
		var planCode=$('#planCode').val();
		if(planCode==null || planCode==''){
			layer.msg('请先生成并保存备货计划信息！',{time:1000});
			return ;
		}
		var planMonth=$('#planMonth').val();
		//获取已经选中的系列
		var selectSeriesCodes=$('#selectSeriesCodes').val();
		if(selectSeriesCodes==null || selectSeriesCodes==""){
			selectSeriesCodes=[];
			selectSeriesCodes=JSON.stringify(selectSeriesCodes);
		}
		selectSeriesCodes =escape(selectSeriesCodes);
		layer.open({
			type:2,
			shade:false,
			area:['800px','600px'],
			title:'请选择系列',
			maxmin:true,
			content:prefix+"/getSeriesSearchDg?selectSeriesCodes="+selectSeriesCodes+"&planCode="+planCode+"&planMonth="+planMonth,
		});
	});
	
	$('#suppSearchBtn').click(function(){
		var planCode=$('#planCode').val();
		if(planCode==null || planCode==''){
			layer.msg('请先生成并保存备货计划信息！',{time:1000});
			return ;
		}
		var planMonth=$('#planMonth').val();
		//获取已经选中的系列
		var selectSuppCodes=$('#selectSuppCodes').val();
		if(selectSuppCodes==null || selectSuppCodes==""){
			selectSuppCodes=[];
			selectSuppCodes=JSON.stringify(selectSuppCodes);
		}
		selectSuppCodes =escape(selectSuppCodes);
		layer.open({
			type:2,
			shade:false,
			area:['800px','600px'],
			title:'请选择供应商',
			maxmin:true,
			content:prefix+"/getSuppSearchDg?selectSuppCodes="+selectSuppCodes+"&planCode="+planCode+"&planMonth="+planMonth,
		});
	});
	
	//-----------------表格检索结束----------------------
	//按照品相  物料  供应商等条件进行搜索过滤
	$('#searchBtn').click(function(){
		var planCode =$('#planCode').val();
		var itemInfo=$('#selectItemCodes').val();
		var suppName=$('#selectSuppCodes').val();
		var mateDesc=$('#mateDesc').val();
		var selectSeriesCodes=$('#selectSeriesCodes').val();
		//备货计划未保存
		if(planCode==null || planCode==''){
			layer.msg('请先生成并保存备货计划信息！',{time:1000});
			return ;
		}
		//备货计划保存后但是有数据修改 提醒采购员先保存修改的数据 否则会造成数据的
		if(modifyCount>0){
			layer.confirm('确认进行搜索吗？搜索会造成您一下该的数据的丢失，请先保存！',function(index){
				$.ajax({
					url : prefix + "/getPlanDetailByMainId",
					method : "get",
					data : {
						mainId : mainId,
						itemInfo:itemInfo,
						suppNosJson:suppName,
						mateCodeJson:mateDesc,
						seriesCodeJson:selectSeriesCodes
					},
					async : false,
					success : function(res) {
						planDetailArr = res;
						initInvenPlanDtetailTable(table);
					}
				});
				modifyCount=0;
				layer.close(index);
			});
		}else{
			$.ajax({
				url : prefix + "/getPlanDetailByMainId",
				method : "get",
				data : {
					mainId : mainId,
					itemInfo:itemInfo,
					suppNosJson:suppName,
					mateCodeJson:mateDesc,
					seriesCodeJson:selectSeriesCodes
				},
				async : false,
				success : function(res) {
					planDetailArr = res;
					initInvenPlanDtetailTable(table);
				}
			});
		}
	});
	
	// 初始化备货计划详情表
	initInvenPlanDtetailTable(table);
	// 初始化按物料的报表
	var planMonth = $("#planMonth").val();
	initalMateTable(planMonth);

	$("#generateBtn").on('click', function() {
		// 首先校验条件是否已经完备
		var planMonth = $("#planMonth").val();
		if (planMonth == "" || planMonth == undefined) {
			layer.msg("请选择月份！");
			return;
		} else {
			var flag = checkRepeat(planMonth, mainId);
			if (flag) {
				if (planDetailArr.length > 0) {
					layer.confirm("已经生成过，是否确认重新生成？", function(index) {
						initalPlanDetailsData(planMonth);
						layer.close(index);
					});
				} else {
					initalPlanDetailsData(planMonth);
				}
			} else {
				layer.msg("该月份已经存在备货计划，不可再次生成！");
				return;
			}
		}
	});

	// 初始化详情数据
	window.initalPlanDetailsData = function(planMonth) {
		// 根据月份初始化数据
		$.ajax({
			url : prefix + '/initalPlanDetailsData',
			data : {
				planMonth : planMonth,
				mainId : mainId
			},
			method : 'get',
			async : false,
			error : function() {
				layer.msg("Connection error");
			},
			success : function(res) {
				planDetailArr = res;
				initInvenPlanDtetailTable(table);
			}
		});
	}

	// 保存事件
	$("#saveBtn").click(function() {
		if (planDetailArr.length > 0) {
			var status = $("#status").val();
			if (status == '已提交') {
				layer.msg('已提交的计划不能更改', {
					time : 1000
				});
				return;
			} else {
				if (status == null || status == '') {
					$("#status").val("已保存");
				}
			}
			saveInvenPlan("save");
		} else {
			layer.msg("请先生成备货计划详情，否则不可保存!");
			return;
		}
	});

	// 提交
	$("#submitBtn").click(function() {
		if (planDetailArr.length > 0) {
			$("#status").val("已提交");
			saveInvenPlan("submit");
		} else {
			layer.msg("请先生成备货计划详情，否则不可提交!");
			return;
		}
	});

	// 保存ajax
	function saveInvenPlan(type) {
		var detailJson = JSON.stringify(planDetailArr);
		$("#detailJson").val(detailJson);
		var options = {
			url : prefix + "/saveInvenPlanInfo",
			type : "POST",
			success : function(msg) {
				if (msg.code == "0") {
					modifyCount=0;
					if (type == "submit")
						window.history.back(-1);
					$('#planCode').val(msg.data.planCode);
					layer.msg("操作成功！", {
						time : 1000
					});
				} else {
					layer.msg(msg.msg, {
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
		$("#invenPlan-form").ajaxSubmit(options);
	}

	// 回退
	$("#backBtn").click(function() {
		window.history.back(-1);
	});

	// 监听操作
	table.on('edit(planDetail-table)', function(obj) {
		//增加修改的数量
		modifyCount++;
		var data = obj.data;
		var beginStock = data.beginStock;
		var prodPlan = data.prodPlan;
		var deliveryPlan = data.deliveryPlan;
		var endStock = Number(beginStock) + Number(prodPlan)
				- Number(deliveryPlan);
		data.endStock = endStock;
		$(this).click();
	});

	table.on('tool(planDetail-table)', function(obj) {
		var data = obj.data;
		modifyCount++;
		obj.update({
			endStock : data.endStock,
			safeScale : data.safeScale
		});
	});

	function initalMateTable(planMonth) {
		var mateData = [];
		if (planMonth != null && planMonth != '') {
			$.ajax({
				url : prefix + "/getReportByMate",
				method : "get",
				data : {
					planMonth : planMonth
				},
				async : false,
				success : function(res) {
					mateData=calSubTotal(res)
				}
			});
		}
		
		function calSubTotal(mateData){
			var newData=[];
			//按照品相进行汇总
			if(mateData!=null && mateData.length>0){
				var tempItemCode=mateData[0].itemCode;
				var tempItemName=mateData[0].itemName;
				var tempMateName=mateData[0].MateName;
				var subBeginOrder=0;
				var subBeginStock=0;
				var subBeginEnableOrder=0;
				var subProdPlan=0;
				var subDeliveryPlan=0;
				var subEndStock=0;
				var subNextDeliveryPlan=0;
				var safeScale='';
				
				for(var i=0;i<mateData.length;i++){
					var mate=mateData[i];
					var currItemCode=mate.itemCode;
					var currItemName=mate.itemName;
					
					
					var currBeginOrder=mate.beginOrder;
					if(currBeginOrder==null ){
						currBeginOrder=0;
					}
					var currBeginStock=mate.beginStock;
					if(currBeginStock==null ){
						currBeginStock=0;
					}
					var currBeginEnableOrder=mate.beginEnableOrder;
					if(currBeginEnableOrder==null ){
						currBeginEnableOrder=0;
					}
					subBeginOrder+=parseFloat(currBeginOrder);
					var currProdPlan=mate.prodPlan;
					if(currProdPlan==null ){
						currProdPlan=0;
					}
					var currDeliveryPlan=mate.deliveryPlan;
					if(currDeliveryPlan==null ){
						currDeliveryPlan=0;
					}
					var currEndStock=mate.endStock;
					if(currEndStock==null ){
						currEndStock=0;
					}
					currNextDeliveryPlan=mate.nextDeliveryNum;
					if(currNextDeliveryPlan==null ){
						currNextDeliveryPlan=0;
					}
					if(tempItemCode!=currItemCode){
						var subData={};
						subData.itemCode=tempItemCode;
						subData.itemName=tempItemName;
						subData.mateCode='小计';
						subData.beginOrder=subBeginOrder;
						subData.beginStock=subBeginStock;
						subData.beginEnableOrder=subBeginEnableOrder;
						subData.prodPlan=subProdPlan;
						subData.deliveryPlan=subDeliveryPlan;
						subData.endStock=subEndStock;
						subData.nextDeliveryNum=subNextDeliveryPlan;
						if(subNextDeliveryPlan!=0){
							safeScale=decimal(subEndStock/subNextDeliveryPlan*100,2)+"%";
						}
						subData.safeScale=safeScale;
						newData.push(subData);
						//数据置空
						subBeginOrder=0;
						subBeginStock=0;
						subBeginEnableOrder=0;
						subProdPlan=0;
						subDeliveryPlan=0;
						subEndStock=0;
						subNextDeliveryPlan=0;
						subSafeScale='';
					}
					subBeginOrder+=parseFloat(currBeginOrder);
					subBeginStock+=parseFloat(currBeginStock);
					subBeginEnableOrder+=parseFloat(currBeginEnableOrder);
					subProdPlan+=parseFloat(currProdPlan);
					subDeliveryPlan+=parseFloat(currDeliveryPlan);
					subEndStock+=parseFloat(currEndStock);
					subNextDeliveryPlan+=parseFloat(currNextDeliveryPlan);
					tempItemName=currItemName;
					tempItemCode=currItemCode;
					newData.push(mate);
					if(i==mateData.length-1){
						var subData={};
						subData.itemCode=tempItemCode;
						subData.itemName=tempItemName;
						subData.mateCode='小计';
						subData.beginOrder=subBeginOrder;
						subData.beginStock=subBeginStock;
						subData.beginEnableOrder=subBeginEnableOrder;
						subData.prodPlan=subProdPlan;
						subData.deliveryPlan=subDeliveryPlan;
						subData.endStock=subEndStock;
						subData.nextDeliveryNum=subNextDeliveryPlan;
						if(subNextDeliveryPlan!=0){
							safeScale=decimal(subEndStock/subNextDeliveryPlan*100,2)+"%";
						}
						subData.safeScale=safeScale;
						newData.push(subData);
					}
				}
			}
			return newData;
		}
		// 按物料汇总
		table.render({
			elem : '#mateTable',
			id : 'mateTable',
			data : mateData,
			page : true,
			cols : [ [
			          {
				field : 'itemName',
				width : '10%',
				title : '品项',
			}, {
				field : 'mateCode',
				width : '13%',
				title : '物料编码',
			}, {
				field : 'mateDesc',
				width : '15%',
				title : '物料名称',
			}, {
				field : 'beginOrder',
				width : '8%',
				title : '期初订单',
			}, {
				field : 'beginStock',
				width : '8%',
				title : '期初库存',
			}, {
				field : 'beginEnableOrder',
				width : '10%',
				title : '期初可生产订单'
			}, {
				field : 'prodPlan',
				width : '8%',
				title : '生产计划',
			}, {
				field : 'deliveryPlan',
				width : '8%',
				title : '交货计划',
			}, {
				field : 'endStock',
				width : '10%',
				title : '期末预计库存'
			}, {
				field : 'safeScale',
				width : '10%',
				title : '安全库存率'
			} ] ]
		});
	}

	// 初始化供应商的
	function initalSuppTable(planMonth) {
		// 按供应商汇总
		var suppData = [];
		if (planMonth != null && planMonth != '') {
			$.ajax({
				url : prefix + "/getReportBySupp",
				method : "get",
				data : {
					planMonth : planMonth
				},
				async : false,
				success : function(res) {
					suppData = res;
				}
			});
		}
		// 按物料汇总
		table.render({
			elem : '#suppTable',
			id : 'suppTable',
			data : suppData,
			page : true,
			cols : [ [ {
				field : 'suppNo',
				width : '15%',
				title : '供应商编码',
			}, {
				field : 'suppName',
				width : '15%',
				title : '供应商名称',
			}, {
				field : 'beginOrder',
				width : '8%',
				title : '期初订单',
			}, {
				field : 'beginStock',
				width : '8%',
				title : '期初库存',
			}, {
				field : 'beginEnableOrder',
				width : '12%',
				title : '期初可生产订单'
			}, {
				field : 'prodPlan',
				width : '8%',
				title : '生产计划',
			}, {
				field : 'deliveryPlan',
				width : '8%',
				title : '交货计划',
			}, {
				field : 'endStock',
				width : '12%',
				title : '期末预计库存'
			}, {
				field : 'safeScale',
				width : '14%',
				title : '安全库存率'
			} ] ]
		});
	}
	// 监听tab切换
	element.on('tab(planTab)', function(data) {
		var planMonth = $("#planMonth").val();
		if (data.index == 0) {
			initalMateTable(planMonth);
		} else {
			initalSuppTable(planMonth);
		}
	});

	// 校验不重复
	function checkRepeat(planMonth, mainId) {
		var flag = true;
		// 校验月份不重复
		$.ajax({
			url : prefix + "/checkRepeat",
			method : "get",
			data : {
				planMonth : planMonth,
				id : mainId
			},
			async : false,
			error : function() {
				layer.msg("Connection error!");
				flag = false;
			},
			success : function(res) {
				if (res.code !== 0)
					flag = false;
			}
		})
		return flag;
	}
});

// 渲染备货计划详情数据
function initInvenPlanDtetailTable(table) {
	table.render({
		elem : '#planDetail-table',
		id : 'planDetail-table',
		data : planDetailArr,
		page : true,
		cols : [ [ {
			type : 'numbers',
		},{
			field : 'prodSeriesCode',
			width : '13%',
			title : '系列',
		}, {
			field : 'mateCode',
			width : '10%',
			title : '物料编码',
		}, {
			field : 'mateDesc',
			width : '10%',
			title : '物料名称',
		}, {
			field : 'suppNo',
			width : '10%',
			title : '供应商编码',
		}, {
			field : 'suppName',
			width : '10%',
			title : '厂商',
		}, {
			field : 'beginOrder',
			width : '8%',
			title : '期初订单',
		}, {
			field : 'beginStock',
			width : '8%',
			title : '期初库存',
		}, {
			field : 'beginEnableOrder',
			width : '8%',
			title : '期初可生产订单'
		}, {
			field : 'prodPlan',
			width : '8%',
			title : '生产计划',
			edit : 'text',
			event : 'updateStock'
		}, {
			field : 'deliveryPlan',
			width : '8%',
			title : '交货计划',
			edit : 'text',
			event : 'updateStock'
		}, {
			field : 'endStock',
			width : '8%',
			title : '期末预计库存'
		}, {
			field : 'safeScale',
			width : '8%',
			title : '安全库存率'
		} ] ]
	});
	if(type==3){
		$('td').removeAttr('data-edit');
	}
}
