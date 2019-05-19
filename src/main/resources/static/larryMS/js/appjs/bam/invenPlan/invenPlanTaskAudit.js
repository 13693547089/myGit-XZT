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
	var mateTableLns=[];
	type = $("#type").val();
	var mainId = $("#id").val();
	var status = $("#status").val();
	var pagePattern = $("#pagePattern").val();
	if(pagePattern == 'read'){
		type =3;
		$("#type").val(3);
	}
	var isChange = false;
	if (type == 3) {
		$("input").attr("readOnly","readOnly");
		$(".disableBtn").css('display','none');
	} else {
		laydate.render({
			elem : '#planMonth',
			type : 'month',
			done : function(value, date, endDate) {
				var oldPlanMonth = $("#planMonth").val();
				if (oldPlanMonth != value) {
					var planDesc=$("#planDesc").val();
					if(planDesc==null || planDesc==''){
						$("#planDesc").val("备货计划"+value);
					}
					initPlanMateByMonth(value);
				}
			}
		});
	}
	if(type!="1"){
		$.ajax({
			url : prefix + "/getMatePlanDetailByMainId",
			method : "get",
			data : { mainId : mainId },
			success : function(res) {
				planDetailArr=calSubTotal(res)
				initMateTable(planDetailArr);
			}
		});
	}
	if(status == "已提交"){
		$("#auditBtn").show();
		$("#saveBtn2").show();
		$("#saveBtn").hide();
		$("#submitBtn").hide();
	}else if(status == "已保存"){
		
	}
	
	/**
	 * 根据月份初始化时获取生产交货计划物料信息
	 * @returns
	 */
	function initPlanMateByMonth(value){
		$.ajax({
			url : prefix + "/initPlanDetailsData",
			method : "get",
			data : { planMonth : value },
			async : false,
			success : function(res) {
				planDetailArr=calSubTotal(res);
				initMateTable(planDetailArr);
			}
		});
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
			shadeClose : false,
			shade:0.1,
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
			shadeClose : false,
			shade:0.1,
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
			shadeClose : false,
			shade:0.1,
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
			shadeClose : false,
			shade:0.1,
			area:['800px','600px'],
			title:'请选择供应商',
			maxmin:true,
			content:prefix+"/getSuppSearchDg?selectSuppCodes="+selectSuppCodes+"&planCode="+planCode+"&planMonth="+planMonth,
		});
	});
	
	//-----------------表格检索结束----------------------
	//按照品相  物料  供应商等条件进行搜索过滤
	$('#searchBtn1').click(function() {
		var planCode = $('#planCode').val();
		var itemInfo = $('#selectItemCodes').val();
		var mateDesc = $('#mateDesc').val();
		var selectSeriesCodes = $('#selectSeriesCodes').val();
		// 备货计划未保存
		if (planCode == null || planCode == '') {
			layer.msg('请先生成并保存备货计划信息！', {
				time : 1000
			});
			return;
		}
		// 备货计划保存后但是有数据修改
		$.ajax({
			url : prefix + "/getMatePlanDetailByMainId",
			method : "get",
			data : {
				mainId : mainId,
				itemInfo : itemInfo,
				mateCodeJson : mateDesc,
				seriesCodeJson : selectSeriesCodes
			},
			async : false,
			success : function(res) {
				planDetailArr = calSubTotal(res);
				initMateTable(planDetailArr);
			}
		});
	});
	$('#searchBtn2').click(function(){
		var planMonth=$("#planMonth").val();
		initalSuppTable(planMonth);
	});
/*	//供应商等条件进行搜索过滤
	$('#suppSearchBtn').click(function(){
		var planCode =$('#planCode').val();
		var suppName=$('#selectSuppCodes').val();
		//备货计划未保存
		if(planCode==null || planCode==''){
			layer.msg('请先生成并保存备货计划信息！',{time:1000});
			return ;
		}
		//备货计划保存后但是有数据修改 
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
				planDetailArr=calSubTotal(res);
				initMateTable(planDetailArr);
			}
		});
	});*/
	
	// 初始化按物料的报表
	initMateTable(planDetailArr);
	
	// 编辑保存事件
	$("#saveBtn").click(function() {
		var planMonth=$('#planMonth').val();
		var flag=checkRepeat(planMonth, mainId);
		if(!flag){
			layer.msg("改月已经创建了备货计划，请勿重复创建！",{time:1000});
			return false;
		}
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

	// 编辑提交
	$("#submitBtn").click(function() {
		var planMonth=$('#planMonth').val();
		var flag=checkRepeat(planMonth, mainId);
		if(!flag){
			layer.msg("改月已经创建了备货计划，请勿重复创建！",{time:1000});
			return false;
		}
		var rows=planDetailArr;
		var flag=true;
		for(var i=rows.length-1;i>=0;i--){
			var row=rows[i];
			if(row.status=='未分解'){
				flag=false;
			}
		}
		if(!flag){
			layer.msg("存在未分解的物料，请分解后提交！");
			return;
		}
		if (rows.length > 0) {
			var taskName = $("#taskName").val();
			var processCode = $("#processCode").val();
			$("#status").val("已保存");
			saveInvenPlan("submit");
			var flag2 = taskProcess(mainId, processCode, taskName, "process");
			/*if (flag2 == "process_success") {
				$("#status").val("已提交");
				saveInvenPlan("submit");
			}*/
		} else {
			layer.msg("请先生成备货计划详情，否则不可提交!");
			return;
		}
	});
	window.returnFunction = function() {
		debugger
		var id = $("#id").val();
		var ids =[];
		ids.push(id);
		var json = JSON.stringify(ids);
		$.ajax({
			url : prefix + "/changeInvenPlanStatus",
			data : {
				status : '已提交',
				jsonIds : json
			},
			success : function(res) {
				layer.msg("操作成功！", {
					time : 1000
				});
  				window.history.go(-1);
			},
			error : function() {
				layer.msg("操作失败！", {
					time : 1000
				});
			}
		});
	}
	// 编辑保存ajax
	function saveInvenPlan(type) {
		var rows=planDetailArr;
		// 去除合计数据
		for(var i=rows.length-1;i>=0;i--){
			var row=rows[i];
			if(row.mateDesc=='小计' || row.mateDesc=='合计'){
				rows.splice(i,1);
			}
		}
		var detailJson = JSON.stringify(rows);
		$("#detailJson").val(detailJson);
		/*var planMonth=$('#planMonth').val();
		var flag=checkRepeat(planMonth, mainId);
		if(!flag){
			layer.msg("改月已经创建了备货计划，请勿重复创建！",{time:1000});
			return false;
		}*/
		var options = {
			url : prefix + "/saveInvenPlan",
			type : "POST",
			success : function(msg) {
				if (msg.code == "0") {
					modifyCount=0;
					if (type == "submit")
						//window.history.back(-1);
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

	
	// 审核保存事件
	$("#saveBtn2").click(function() {
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
			saveInvenPlan2("save");
		} else {
			layer.msg("请先生成备货计划详情，否则不可保存!");
			return;
		}
	});

	// 审核提交提交
	$("#auditBtn").click(function() {
		debugger;
		var taskName = $("#taskName").val();
		var processCode = $("#processCode").val();
		var flag = taskProcess(mainId, processCode, taskName, "process");
		debugger;
		if (flag == "over_success") {
			debugger;
			$("#status").val("已审核");
			saveInvenPlan2("submit");
		}
	});

	// 审核保存ajax
	function saveInvenPlan2(type) {
		var rows=planDetailArr;
		// 去除合计数据
		for(var i=rows.length-1;i>=0;i--){
			var row=rows[i];
			if(row.mateDesc=='小计' || row.mateDesc=='合计'){
				rows.splice(i,1);
			}
		}
		var detailJson = JSON.stringify(rows);
		$("#detailJson").val(detailJson);
		var options = {
			url : prefix + "/saveInvenPlan",
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
		
	function calSubTotal(mateData){
		debugger;
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
				var subNextMonthDeliveryPlan=0;
				var safeScale='';
				
				
				var totalBeginOrder=0;
				var totalBeginStock=0;
				var totalBeginEnableOrder=0;
				var totalProdPlan=0;
				var totalDeliveryPlan=0;
				var totalEndStock=0;
				var totalNextMonthDeliveryPlan=0;
				
				
				
				
				
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
					//subBeginOrder+=parseFloat(currBeginOrder);
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
					currNextMonthDeliveryPlan=mate.nextMonthDeliveryNum;
					if(currNextMonthDeliveryPlan==null ){
						currNextMonthDeliveryPlan=0;
					}
					if(tempItemCode!=currItemCode){
						var subData={};
						subData.itemCode=tempItemCode;
						subData.itemName=tempItemName;
						subData.mateDesc='小计';
						subData.beginOrder=decimal(subBeginOrder,2);
						subData.beginStock=decimal(subBeginStock,2);
						subData.beginEnableOrder=decimal(subBeginEnableOrder,2);
						subData.prodPlan=decimal(subProdPlan,2);
						subData.deliveryPlan=decimal(subDeliveryPlan,2);
						subData.endStock=decimal(subEndStock,2);
						subData.nextMonthDeliveryNum=decimal(subNextMonthDeliveryPlan,2);
						if(subNextMonthDeliveryPlan!=0){
							safeScale=decimal(subEndStock/subNextMonthDeliveryPlan*100,2)+"%";
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
						subNextMonthDeliveryPlan=0;
						subSafeScale='';
					}
					subBeginOrder+=parseFloat(currBeginOrder);
					subBeginStock+=parseFloat(currBeginStock);
					subBeginEnableOrder+=parseFloat(currBeginEnableOrder);
					subProdPlan+=parseFloat(currProdPlan);
					subDeliveryPlan+=parseFloat(currDeliveryPlan);
					subEndStock+=parseFloat(currEndStock);
					subNextMonthDeliveryPlan+=parseFloat(currNextMonthDeliveryPlan);
					
					totalBeginOrder+=parseFloat(currBeginOrder);
					totalBeginStock+=parseFloat(currBeginStock);
					totalBeginEnableOrder+=parseFloat(currBeginEnableOrder);
					totalProdPlan+=parseFloat(currProdPlan);
					totalDeliveryPlan+=parseFloat(currDeliveryPlan);
					totalEndStock+=parseFloat(currEndStock);
					totalNextMonthDeliveryPlan+=parseFloat(currNextMonthDeliveryPlan);
					
					tempItemName=currItemName;
					tempItemCode=currItemCode;
					newData.push(mate);
					if(i==mateData.length-1){
						var subData={};
						subData.itemCode=tempItemCode;
						subData.itemName=tempItemName;
						subData.mateDesc='小计';
						subData.beginOrder=decimal(subBeginOrder,2);
						subData.beginStock=decimal(subBeginStock,2);
						subData.beginEnableOrder=decimal(subBeginEnableOrder,2);
						subData.prodPlan=decimal(subProdPlan,2);
						subData.deliveryPlan=decimal(subDeliveryPlan,2);
						subData.endStock=decimal(subEndStock,2);
						subData.nextMonthDeliveryNum=decimal(subNextMonthDeliveryPlan,2);
						if(subNextMonthDeliveryPlan!=0){
							safeScale=decimal(subEndStock/subNextMonthDeliveryPlan*100,2)+"%";
						}
						subData.safeScale=safeScale;
						newData.push(subData);
					}
				}
				var totalData={};
				totalData.mateDesc='合计';
				totalData.beginOrder=decimal(totalBeginOrder,2);
				totalData.beginStock=decimal(totalBeginStock,2);
				totalData.beginEnableOrder=decimal(totalBeginEnableOrder,2);
				totalData.prodPlan=decimal(totalProdPlan,2);
				totalData.deliveryPlan=decimal(totalDeliveryPlan,2);
				totalData.endStock=decimal(totalEndStock,2);
				totalData.nextMonthDeliveryNum=decimal(totalNextMonthDeliveryPlan,2);
				if(totalNextMonthDeliveryPlan!=0){
					totalData.safeScale=decimal(totalEndStock/totalNextMonthDeliveryPlan*100,2)+"%";
				}
				newData.push(totalData);
			}
			return newData;
		}
		/**
		 * 重新加载物料汇总表格数据
		 */
	window.loadMateTable= function(){
		var planCode = $('#planCode').val();
		var itemInfo = $('#selectItemCodes').val();
		var mateDesc = $('#mateDesc').val();
		var selectSeriesCodes = $('#selectSeriesCodes').val();
		// 备货计划保存后但是有数据修改
		$.ajax({
			url : prefix + "/getMatePlanDetailByMainId",
			method : "get",
			data : {
				mainId : mainId,
				itemInfo : itemInfo,
				mateCodeJson : mateDesc,
				seriesCodeJson : selectSeriesCodes
			},
			async : false,
			success : function(res) {
				planDetailArr = calSubTotal(res);
				table.reload('mateTable',{data:planDetailArr});
			}
		});
	}
		// 按物料汇总
		function initMateTable(mateData){
			var operateBar='operateBar';
			if(type=='3'){
				operateBar="operateBar1";
			}
			 mateTableLns=table.render({
				elem : '#mateTable',
				id : 'mateTable',
				data : mateData,
				page : true,
				cols : [ [
		          {
		        	  field : 'itemName',
		        	  width : '5%',
		        	  title : '类别',
		          }, {
		        	  field : 'ranking',
		        	  width : '5%',
		        	  title : '排名',
		          },{
		        	  field : 'mateDesc',
		        	  width : '15%',
		        	  title : '物料名称',
		        	  templet: function(d){
		        		 var mateDesc= d.mateDesc;
		        		 if(mateDesc=='小计' ||mateDesc=='合计'){
		        			 return '<span class="trBold">'+ mateDesc +'</span>';
		        		 }else{
		        			return '<span>'+ mateDesc +'</span>' ;
		        		 }
			        }
		          },{
		        	  field : 'status',
		        	  width : '5%',
		        	  title : '状态',
		          },  {
		        	  field : 'beginOrder',
		        	  width : '7%',
		        	  title : '期初订单',
		          }, {
		        	  field : 'beginStock',
		        	  width : '7%',
		        	  title : '期初库存',
		          }, {
		        	  field : 'beginEnableOrder',
		        	  width : '10%',
		        	  title : '期初可生产订单'
		          }, {
		        	  field : 'prodPlan',
		        	  width : '7%',
		        	  title : '生产计划',
		          }, {
		        	  field : 'deliveryPlan',
		        	  width : '7%',
		        	  title : '交货计划',
		        	  edit  : 'text',
		        	  event : 'editTable'
		          }, {
		        	  field : 'endStock',
		        	  width : '10%',
		        	  title : '期末预计库存'
		          }, {
		        	  field : 'safeScale',
		        	  width : '10%',
		        	  title : '安全库存率'
		          }, {
		        	  field : 'mateCode',
		        	  width : '12%',
		        	  title : '物料编码',
		          }, {
		        	  title : '操作',
		        	  width : 150,
		        	  fixed : 'right',
		        	  field : 'operateBar',
		        	  align : 'center',
		        	  toolbar : '#'+operateBar
		          } ] ],
		          done: function(res, curr, count){
			        	 $('.trBold').parent('div').parent('td').parent('tr').css("font-weight","bold");
			      }
			});
		}
	//监听表格事件  分解或者撤销	
	 table.on('tool(mateTable)', function(obj) {
			if(type==3 && obj.event!="view"){
				return;
			}
			var data = obj.data;
			var planCode=$('#planCode').val();
			var mainStatus=$("#status").val();
			var mateCode=data.mateCode;
			var mateDesc=data.mateDesc;
			var status=data.status;
			var delRows=[];
			delRows.push(data);
			if(planCode==null || planCode==''){
				layer.msg('请先保存备货计划',{time:1000});
				return ;
			}else if(mainStatus =='已提交' && obj.event!='view'){
				layer.msg('备货计划已提交不能进行该操作',{time:1000});
				return ;
			}else if(mateDesc=='小计' || mateDesc=='合计'){
				layer.msg(mateDesc+"物料无法分解!",{time:1000});
				return;
			}
			if (obj.event === 'decompose') {
				if(status!='未分解'){
					layer.msg('请分解未分解状态下的数据',{time:1000});
				}else{
					//进行分解的业务逻辑
					decomposeDetail(data);
				}
			}else if(obj.event === 'cancle'){
				if(status!='已分解'){
					layer.msg('请分解状态为已分解的数据！',{time:1000});
				}else{
					//撤销分解的业务逻辑
					$.ajax({
						url:prefix+"/cancleDecompose",
						data:{id:data.id,status:"未分解"},
						success:function(res){
							loadMateTable();
							layer.msg("操作成功！",{time:1000});
						},
						error:function(){
							layer.msg("操作失败！",{time:1000});
						}
					});
				}
			}else if(obj.event==='view'){
				if(status!='已分解'){
					layer.msg('请查看已分解的数据',{time:1000});
				}else{
					viewDecomposeDetail(data);
				}
			}
		});

	   /**
		 * 分解备货计划详情
		 * @returns
		 */
		function decomposeDetail(data){
			var nextMonthDeliveryNum=data.nextMonthDeliveryNum;
			if(nextMonthDeliveryNum==null || nextMonthDeliveryNum==''){
				nextMonthDeliveryNum=0;
			}
			var planMonth=$('#planMonth').val();
			//弹出添加物料的页面
			var perContent=layer.open({
				  type: 2,
				  shadeClose : false,
				  shade: 0.1,
				  title:'分解计划',
				  area: ['800px', '500px'],
				  maxmin: true,
				  content: prefix+'/getDecomposePage?mateCode='+data.mateCode+"&mateDesc="+data.mateDesc+"&deliveryPlan="+data.deliveryPlan+"&mainId="+mainId+"&planMonth="+planMonth+"&nextMonthDeliveryNum="+nextMonthDeliveryNum+"&planDetailId="+data.id,
				  yes: function(index, layero){

				  }
			});
			layer.full(perContent);
		}
		
		/**
		 * 查看分解的排查计划
		 * @param data
		 * @returns
		 */
		function viewDecomposeDetail(data){
			var nextMonthDeliveryNum=data.nextMonthDeliveryNum;
			if(nextMonthDeliveryNum==null || nextMonthDeliveryNum==''){
				nextMonthDeliveryNum=0;
			}
			var planMonth=$('#planMonth').val();
			//弹出添加物料的页面
			var perContent=layer.open({
				  type: 2,
				  shadeClose : false,
				  shade: 0.1,
				  title:'分解计划',
				  area: ['800px', '500px'],
				  maxmin: true,
				  content: prefix+'/getDecomposeViewPage?mateCode='+data.mateCode+"&mateDesc="+data.mateDesc+"&deliveryPlan="+data.deliveryPlan+"&mainId="+mainId+"&planMonth="+planMonth+"&nextMonthDeliveryNum="+nextMonthDeliveryNum+"&planDetailId="+data.id,
				  yes: function(index, layero){

				  }
			});
			layer.full(perContent);
		}
		
		 //监听表格事件  分解或者撤销	
		 table.on('tool(suppTable)', function(obj) {
			var data = obj.data;
			var perContent = layer.open({
				type : 2,
				shadeClose : false,
				shade : 0.1,
				title : '供应商备货计划详情',
				area : [ '800px', '500px' ],
				maxmin : true,
				content : prefix + '/getSuppMateViewPage?suppNo='
						+ data.suppNo + "&suppName="
						+ data.suppName + "&mainId=" + mainId,

			});
			layer.full(perContent);
		});
	// 初始化供应商的
	window.initalSuppTable=function(planMonth) {
		// 按供应商汇总
		var suppData = [];
		var selectSuppCodes=$('#selectSuppCodes').val();
		if(selectSuppCodes==null || selectSuppCodes==""){
			selectSuppCodes=[];
			selectSuppCodes=JSON.stringify(selectSuppCodes);
		}
		if (planMonth != null && planMonth != '') {
			$.ajax({
				url : prefix + "/getReportBySupp",
				method : "get",
				data : {
					mainId : mainId,
					selectSuppCodes:selectSuppCodes,
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
			},{
				title : '操作',
				width : 150,
				fixed : 'right',
				field : 'operateBar',
				align : 'center',
				toolbar : '#operateBar1'
			} ] ]
		});
	}
	// 监听tab切换
	element.on('tab(planTab)', function(data) {
		var planMonth = $("#planMonth").val();
		if (data.index == 0) {
			loadMateTable();
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