layui.use([ 'form', 'table','layer','laydate'], function() {
	var $=layui.jquery;
	var prefix="/actReach";
	var table=layui.table;
	var layer=layui.layer;
	var form=layui.form;
	var laydate=layui.laydate;
	var tableIns=[];
	
	//日期控件格式化
	 laydate.render({
		elem : '#startDate'
	 });
		//日期控件格式化
	 laydate.render({
		elem : '#endDate1'
	 });
	//加载页面初始化表格
	loadTable();
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
	
	$('#searchBtn').click(function(){
		loadTable();
	});
	
	/**
	 * 交货计划表格数据的加载
	 * @returns
	 */
	function loadTable(){
		var mateCode=$("#mateCode").val();
		var suppName=$("#suppName").val();
		var startDate=$('#startDate').val();
		var endDate=$('#endDate1').val();
		$.ajax({
			url:prefix+"/getPdrData",
			type:"post",
			data:{mateCode:mateCode,startDate:startDate,suppName:suppName,endDate:endDate},
			success:function(res){
				inintPrdTable(res);
			}
		});
	}

	/**
	 * 初始化排产计划详情表
	 * @param condition
	 * @returns
	 */
	function inintPrdTable(data){
		tableIns=table.render({
			elem : '#pdrTable',
			id:'pdrTable',
			data:data,
			page:true,
			cols : [ [ {
				type : 'numbers',
				title : '序号'
			}, {
				field : 'suppName',
				title : '供应商'
			},  {
				field : 'produceDate',
				title : '生产日期',
				templet:function(d){
					return formatTime(d.produceDate,"yyyy-MM-dd");
				}
			}, {
				field : 'actPdcQty',
				title : '实际生产'
			}, {
				field : 'actDevQty',
				title : '实际交货'
			},{
				field : 'stockQty',
				title : '供应商库存'
			} ] ]
		});
	}
})
