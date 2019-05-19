var prefix="/actReach";
layui.use(['table','laydate','layer'], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate=layui.laydate;
	var condition = {};
	//日期控件格式化
	 laydate.render({
		elem : '#endDate'
	 });
	 $("#exportBtn").click(function(){
		 var endDate=$('#endDate').val();
		 window.location.href=prefix+"/exportBusyStock?endDate="+endDate
	 });
	 $("#actReachBtn").click(function(){
		 var endDate=$('#endDate').val();
		 var mateDesc=$("#mateDesc").val();
		 window.location.href=prefix+"/exportActReach?endDate="+endDate+"&mateDesc="+mateDesc;
	 });
	//搜索的点击事件
	$("#searchBtn").click(function(){
		loadTable();
	});
	$('#innerCtrBtn').click(function(){
		var endDate=$('#endDate').val();
		window.location.href=prefix+"/exportInnerControl?endDate="+endDate
	});
	$('#innerCtrWBtn').click(function(){
		var endDate=$('#endDate').val();
		window.location.href=prefix+"/exportInnerControl?endDate="+endDate+"&wFlag=w"
	});
	
	/**
	 * 初始化日期
	 * @returns
	 */
	function initDate(){
		var endDate=$("#endDate").val();
		if(endDate==null|| endDate==''){
			$('#endDate').val(formatTime(new Date(),'yyyy-MM-dd'));
		}
	}
	//表格从新加载
	window.loadTable=function(){
		var mateDesc=$("#mateDesc").val();
		var endDate=$("#endDate").val();
		if(endDate==null || endDate==''){
			endDate=formatTime(new Date(),'yyyy-MM-dd');
			$('#endDate').val(endDate);
		}
		condition.endDate=endDate;
		condition.mateDesc=mateDesc;
		initActReachTable(condition);
	}
	//页面初始化日历和表格
	 initDate();
	 loadTable();
	//重置点击事件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	//表格监听事件
	 table.on('tool(act_reach_table)', function(obj) {
		var data = obj.data;
		var id = data.id;
		var planMonth=formatTime(data.planMonth,'yyyy-MM');
		var mateCode=data.mateCode;
		var mateDesc=data.mateDesc;
		var nextDevNum=data.nextDevNum;
		var endDate=$('#endDate').val();
		if (obj.event === 'detail') {
			var perContent=layer.open({
				  type: 2,
				  shadeClose : false,
				  shade: 0.1,
				  title:'详情',
				  area: ['800px', '500px'],
				  maxmin: true,
				  content: prefix + "/getSuppReachDg?mateCode=" + mateCode+"&planMonth="+planMonth+"&mateDesc="+mateDesc+"&endDate="+endDate+"&nextDevNum="+nextDevNum,
				  yes: function(index, layero){

				  }
			});
			layer.full(perContent);   
		}  else if (obj.event === 'view') {
			var perContent=layer.open({
				type: 2,
				shadeClose : false,
				shade: 0.1,
				title:'详情',
				area: ['800px', '500px'],
				maxmin: true,
				content: prefix + "/getSuppDlvDg?mateCode=" + mateCode+"&planMonth="+planMonth+"&mateDesc="+mateDesc+"&endDate="+endDate+"&nextDevNum="+nextDevNum,
				yes: function(index, layero){
					
				}
			});
			layer.full(perContent);   
		} 
	});
	/**
	 * 初始化采购对账单表格
	 * 
	 * @param condition
	 * @returns
	 */
	function initActReachTable(condition){
		table.render({
			elem : '#act_reach_table',
			id:'act_reach_table',
			where: condition,
			url  : prefix+'/getActReachByPage',
			page : true,
			cols : [ [ {
				type : 'numbers',
				title : '序号'
			},{
				field : 'mateCode',
				width : '12%',
				title : '物料编码',
			},  {
				field : 'mateDesc',
				width : '15%',
				title : '物料名称'
			}, {
				field : 'rank',
				title : '排名'
			},  {
				field : 'planMonth',
				title : '计划月份',
				width : '7%',
				templet:function(d){
					return formatTime(d.planMonth, 'yyyy-MM');
				}
			}, {
				field : 'planPrdNum',
				width : '7%',
				title : '生产计划'
			}, {
				field : 'actPrdNum',
				width : '7%',
				title : '实际生产'
			}, {
				field : 'pudReachScale',
				width : '10%',
				title : '实际生产达成'
			}, {
				field : 'planDlvNum',
				width : '7%',
				title : '交货计划'
			}, {
				field : 'actDlvNum',
				width : '7%',
				title : '实际交货'
			}, {
				field : 'dlvReachScale',
				width : '7%',
				title : '交货达成'
			}, {
				field : 'suppActNum',
				width : '10%',
				title : '供应商实际库存'
			},{
				field : 'safeScale',
				width : '8%',
				title : '安全库存率'
			},{
				fixed : 'right',
				title : '操作',
				width : '10%',
				align : 'center',
				toolbar : '#operateBar'
			} ] ]
		});
	}
});