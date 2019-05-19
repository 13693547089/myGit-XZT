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
		var mainId=$("#mainId").val();
		var suppNo=$("#suppNo").val();
		$.ajax({
			url:prefix+"/getSuppProdBySuppNo",
			type:"post",
			data:{mainId:mainId,suppNo:suppNo},
			success:function(res){
				initInvenPlanDtetailTable(res);
				loadSubTotal();
			}
		});
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
				width: '8%',
				title : '供应商编码',
			},{
				field : 'suppName',
				width: '15%',
				title : '厂商',
			},{
				field : 'mateCode',
				width: '11%',
				title : '物料编码',
			},{
				field : 'mateDesc',
				width: '15%',
				title : '物料描述',
			},{
				field : 'beginOrder',
				width: '7%',
				title : '期初订单',
			},{
				field : 'beginStock',
				width: '7%',
				title : '期初库存',
			}, {
				field : 'beginEnableOrder',
				width: '10%',
				title : '期初可生产订单'
			}, {
				field : 'prodPlan',
				width: '7%',
				title : '生产计划',
			},{
				field : 'deliveryPlan',
				width: '7%',
				title : '交货计划',
			},{
				field : 'endStock',
				width: '9%',
				title : '期末预计库存'
			},{
				field : 'safeScale',
				width: '8%',
				title : '安全库存率',
			} ] ]
		});
	}
})
