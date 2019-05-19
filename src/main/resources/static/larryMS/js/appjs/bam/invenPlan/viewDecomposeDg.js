layui.use([ 'form', 'table','layer'], function() {
	var $=layui.jquery;
	var prefix="/invenPlan";
	var table=layui.table;
	var layer=layui.layer;
	var form=layui.form;
	var tableIns=[];
	//加载页面初始化表格
	loadTable();
	//确认
	$("#confirmBtn").click(function(){
		closeDg();
	});
	//取消按钮点击事件
	$("#cancleBtn").click(function(){
		closeDg();
	});
	/**
	 * layer关闭子表窗口
	 * @returns
	 */
	function closeDg(){
		var index = parent.layer.getFrameIndex(window.name);//获取子窗口索引
		 parent.layer.close(index);
	}
	
	/**
	 * 交货计划表格数据的加载
	 * @returns
	 */
	function loadTable(){
		var planDetailId=$("#planDetailId").val();
		$.ajax({
			url:"/suppProd/getSuppProdByPlanDetailId",
			type:"post",
			data:{planDetailId:planDetailId},
			success:function(res){
				var planDetail={};
				planDetail.suppNo='小计';
				planDetail.beginOrder=0;
				planDetail.beginStock=0;
				planDetail.beginEnableOrder=0;
				planDetail.prodPlan=0;
				planDetail.deliveryPlan=0;
				planDetail.endStock=0;
				planDetail.safeScale='';
				res.push(planDetail);
				initInvenPlanDtetailTable(res);
				loadSubTotal();
			}
		});
	}

	/**
	 * 更新小计等信息
	 * @returns
	 */
	function loadSubTotal(){
		var data= tableIns.config.data;
		var length=data.length;
		var subTotal=data[length-1];
		var subProdPlan=0;
		var subDeliveryPlan=0;
		var subBeginOrder=0;
		var subBeginStock=0;
		for(var i=0;i<length-1;i++){
			var currRow=data[i];
			var prodPlan=currRow.prodPlan;
			var deliveryPlan=currRow.deliveryPlan;
			var beginOrder=currRow.beginOrder;
			var beginStock=currRow.beginStock;
			if(prodPlan==null){
				prodPlan=0;
			}
			if(deliveryPlan==null){
				deliveryPlan=0;
			}
			if(beginOrder==null){
				beginOrder=0;
			}
			if(beginStock==null){
				beginStock=0;
			}
			currRow.endStock=parseFloat(beginStock)+parseFloat(prodPlan)-parseFloat(deliveryPlan);
			currRow.beginEnableOrder=parseFloat(beginOrder)-parseFloat(beginStock);
			subProdPlan+=parseFloat(prodPlan);
			subDeliveryPlan+=parseFloat(deliveryPlan);
			subBeginOrder+=parseFloat(beginOrder);
			subBeginStock+=parseFloat(beginStock);
		}
		subTotal.prodPlan=subProdPlan;
		subTotal.deliveryPlan=subDeliveryPlan;
		subTotal.beginOrder=subBeginOrder;
		subTotal.beginStock=subBeginStock;
		subTotal.beginEnableOrder=subBeginOrder-subBeginStock;
		var totalEndStock=subBeginStock+subProdPlan-subDeliveryPlan;
		subTotal.endStock=totalEndStock;
		var safeScale='';
		var nextMonthDeliveryNum=$('#nextMonthDeliveryNum').val();
		if(nextMonthDeliveryNum!=null && nextMonthDeliveryNum!='' && nextMonthDeliveryNum!=0){
			safeScale=decimal(totalEndStock/nextMonthDeliveryNum*100,2)+"%";
		}
		subTotal.safeScale=safeScale;
		initInvenPlanDtetailTable(data);
	}
	/**
	 * 初始化排产计划详情表
	 * @param condition
	 * @returns
	 */
	function initInvenPlanDtetailTable(data){
		tableIns=table.render({
			elem : '#suppTable',
			id:'suppTable',
			data:data,
			limit:5000,
			cols : [ [{
				field : 'suppNo',
				width: '10%',
				title : '供应商编码',
			},{
				field : 'suppName',
				width: '20%',
				title : '厂商',
			},{
				field : 'beginOrder',
				width: '9.8%',
				title : '期初订单',
			},{
				field : 'beginStock',
				width: '10%',
				title : '期初库存',
			}, {
				field : 'beginEnableOrder',
				width: '10%',
				title : '期初可生产订单'
			}, {
				field : 'prodPlan',
				width: '10%',
				title : '生产计划',
			},{
				field : 'deliveryPlan',
				width: '10%',
				title : '交货计划',
			},{
				field : 'endStock',
				width: '10%',
				title : '期末预计库存'
			},{
				field : 'safeScale',
				width: '10%',
				title : '安全库存率',
			} ] ]
		});
	}
})
