var prefix="/suppProd";
layui.use(['table','laydate','layer'], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate=layui.laydate;
	var condition = {};
	//返回点击事件
	$("#backBtn").click(function(){
		history.go(-1);
	});
	//搜索的点击事件
	$("#searchBtn").click(function(){
		loadTable();
	});
	window.loadTable=function(){
		var planMonth=$("#planMonth").val();
		var mateDesc=$("#mateDesc").val();
		var suppName=$("#suppName").val();
		var invenPlanId=$("#id").val();
		condition.planMonth=planMonth;
		condition.mateDesc=mateDesc;
		condition.invenPlanId=invenPlanId;
		condition.suppName=suppName;
		initSuppProdTable(condition);
	}
	loadTable();
	//重置点击事件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	//表格监听事件
	 table.on('tool(suppProdTable)', function(obj) {
		var data = obj.data;
		var id = data.id;
		var planMonth=data.planMonth;
		var status=data.status;
		if(status=="未排产"){
			layer.msg("该数据还未排产，无法查看！",{time:1000});
			return;
		}
		if (obj.event === 'plan') {
			layer.open({
				  type: 2,
				  shadeClose : false,
				  shade: 0.1,
				  title:'排产计划',
				  area: ['800px', '500px'],
				  maxmin: true,
				  content: prefix + "/getViewProdPlanDg?id=" + id+"&planMonth="+planMonth+"&status="+status,
				  yes: function(index, layero){

				  }
			});
		} 
	});
	/**
	 * 初始化采购对账单表格
	 * 
	 * @param condition
	 * @returns
	 */
	function initSuppProdTable(condition){
		table.render({
			elem : '#suppProdTable',
			id:'suppProdTable',
			where: condition,
			url  : prefix+'/getSuppProdByPage',
			page : true,
			cols : [ [ {
				type : 'checkbox',
			},{
				type : 'numbers',
				title : '序号'
			},{
				field : 'status',
				title : '状态'
			},{
				field : 'mateCode',
				title : '物料编码',
			},  {
				field : 'mateDesc',
				title : '物料名称'
			},  {
				field : 'suppName',
				title : '供应商'
			}, {
				field : 'ranking',
				title : '排名'
			}, {
				field : 'beginOrder',
				title : '期初订单'
			}, {
				field : 'beginStock',
				title : '期初库存'
			}, {
				field : 'beginEnableOrder',
				title : '期初可生产订单'
			}, {
				field : 'beginStock',
				title : '生产计划'
			}, {
				field : 'beginStock',
				title : '交货计划'
			}, {
				field : 'beginStock',
				title : '期末预计库存'
			},{
				fixed : 'right',
				title : '操作',
				width : 150,
				align : 'center',
				toolbar : '#operateBar'
			} ] ]
		});
	}
});