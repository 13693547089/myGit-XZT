layui.use([ 'form', 'table','layer'], function() {
	var $=layui.jquery;
	var prefix="/invenPlan";
	var table=layui.table;
	var layer=layui.layer;
	var form=layui.form;
	//加载页面初始化表格
	loadTable();
	//查询按钮点击事件
	$("#searchBtn").click(function(){
		loadTable();
	});
	//确认
	$("#confirmBtn").click(function(){
		//调用父窗口的方法
		var data = table.checkStatus('invenPlanTable').data;
		closeDg();
		parent.addRows(data);
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
		var planMonth=$("#planMonth").val();
		initInvenPlanDtetailTable(-1)
		var prodSeriesCode=$("#prodSeriesCode").val();
		var mateDesc=$("#mateDesc").val();
		$.ajax({
			url:prefix+"/getDeliveryPlans",
			type:"post",
			data:{planMonth:planMonth,prodSeriesCode:prodSeriesCode,mateDesc:mateDesc},
			success:function(res){
				initInvenPlanDtetailTable(res);
			}
		});
	}
	/**
	 * 初始化排产计划详情表
	 * @param condition
	 * @returns
	 */
	function initInvenPlanDtetailTable(data){
		var mateCode=$("#mateCode").val();
		var noContent='';
		if(data==-1){
			data=[];
			table.render({
				elem : '#invenPlanTable',
				id:'invenPlanTable',
				data:data,
				text:{none:'数据加载中'},
				limit:90,
				cols : [ [ {
					type : 'checkbox',
				},{
					type : 'numbers',
					title : '序号'
				},{
					field : 'planMonth',
					title : '计划月份',
					templet : function(d) {
						return formatTime(d.planMonth, 'yyyy-MM');
					}
				},{
					field : 'mateCode',
					title : '物料编码',
				},{
					field : 'prodSeriesDesc',
					title : '产品系列',
				},{
					field : 'mateDesc',
					title : '物料名称',
				},{
					field : 'ranking',
					title : '排名',
				},{
					field : 'deliveryPlan',
					title : '交货计划'
				} ] ]
			});
		}else{
			table.render({
				elem : '#invenPlanTable',
				id:'invenPlanTable',
				data:data,
				limit:5000,
				cols : [ [ {
					type : 'checkbox',
				},{
					type : 'numbers',
					title : '序号'
				},{
					field : 'planMonth',
					title : '计划月份',
					templet : function(d) {
						return formatTime(d.planMonth, 'yyyy-MM');
					}
				},{
					field : 'mateCode',
					title : '物料编码',
				},{
					field : 'prodSeriesDesc',
					title : '产品系列',
				},{
					field : 'mateDesc',
					title : '物料名称',
				},{
					field : 'ranking',
					title : '排名',
				},{
					field : 'deliveryPlan',
					title : '交货计划'
				} ] ]
			});
		}
		
		
	}
})
