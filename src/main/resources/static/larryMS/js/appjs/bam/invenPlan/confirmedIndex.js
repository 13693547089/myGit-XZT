var prefix = "/invenPlan";
layui.use([ 'table', 'laydate', 'layer' ], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate = layui.laydate;
	var condition = {};
	var status = '已审核';
	condition.status = status;

	initInvenPlanTable(condition);
	// 日期控件格式化
	laydate.render({
		elem : '#startDate'
	});
	laydate.render({
		elem : '#endDate'
	});
	laydate.render({
		elem : '#planMonth',
		type:"month"
	});
	// 搜索的点击事件
	$("#searchBtn").click(function() {
		var planDesc = $("#planDesc").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var createUser=$('#createUser').val();
		var planMonth=$('#planMonth').val();
		condition.planDesc = planDesc;
		condition.startDate = startDate;
		condition.endDate = endDate;
		condition.createUser = createUser;
		condition.planMonth = planMonth;
		initInvenPlanTable(condition);
	});
	// 重置点击事件
	$("#resetBtn").click(function() {
		$("#searchForm")[0].reset();
	});
	// 表格监听事件
	table.on('tool(invenPlanTable)', function(obj) {
		var data = obj.data;
		var id = data.id;
		var ids = [];
		var planMonth= formatTime(data.planMonth, 'yyyy-MM');
		ids.push(id);
		if (obj.event === 'view') {
			var url = prefix + "/getEditPage?id=" + id + "&type=3"+"&planMonth="+planMonth;
			tuoGo(url,"invenPlanEdit","invenPlanTable");
		} 
	});
	
	/**
	 * 初始化采购对账单表格
	 * 
	 * @param condition
	 * @returns
	 */
	function initInvenPlanTable(condition) {
		var operateBar="#operateBar1";
		table.render({
			elem : '#invenPlanTable',
			id : 'invenPlanTable',
			where : condition,
			url : prefix + '/getConfirmedByPage',
			page : true,
			cols : [ [ {
				type : 'checkbox',
			}, {
				type : 'numbers',
				title : '序号'
			}, {
				field : 'status',
				title : '状态',
				width:'5%',
			}, {
				field : 'planMonth',
				title : '计划月份',
				width:'10%',
				templet : function(d) {
					return formatTime(d.planMonth, 'yyyy-MM');
				}
			}, {
				field : 'planCode',
				width:'13%',
				title : '计划编号'
			}, {
				field : 'planDesc',
				width:'30%',
				title : '计划名称'
			}, {
				field : 'createUser',
				width:'10%',
				title : '创建人'
			}, {
				field : 'createTime',
				width:'10%',
				title : '创建时间',
				templet : function(d) {
					return formatTime(d.createTime, 'yyyy-MM-dd');
				}
			}, {
				fixed : 'right',
				title : '操作',
				width:'15%',
				align : 'center',
				toolbar : operateBar
			} ] ]
		});
	}
});