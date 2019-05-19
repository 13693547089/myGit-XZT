// 地址前缀
var prefix = "/bam/pdr";
// 选中节点
var direId="";
// table数据
var tableData;

//定义日期格式
function laydateInit(laydate){
	  //常规用法
	  laydate.render({
	    elem: '#search_prodDateStart'
	  });
	  laydate.render({
		elem: '#search_prodDateEnd'
	  });
}

//定义树形控件
layui.use(['table','laydate'], function() {
	var $ = layui.$;
	var table = layui.table;
	var laydate = layui.laydate;
	// 定义日期控件
	laydateInit(laydate);
	tableData = [];
	initListTable(tableData);
	
	//初始化表格
	function initListTable(tableData){
	   table.render({
		     elem: '#list-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,data:tableData
		    ,limit:30
		    ,limits:[30,50,100,200,500,1000]
		    ,cols: [[
		       //{checkbox:true} ,
			  {field:'sn', title: '序号',width:'5%',type:'numbers'}
		      ,{field:'suppName', title: '供应商名称',width:'15%'}
		      ,{field:'produceDate', title: '生产日期',width:'7%'}
			  ,{field:'series', title: '系列',width:'7%'}
		      ,{field:'matName', title: '物料名称',width:'15%'}
		      ,{field:'productType', title: '产品类别',width:'7%'}
		      ,{field:'batchNo', title: '批次号',width:'10%'}
		      ,{field:'qcQty', title: '已检数量',width:'10%'}
		      ,{field:'unQcQty', title: '未检数量',width:'10%'}
		      ,{field:'qty', title: '库存数量',width:'10%'}
		      ,{field:'matCode', title: '物料编号',width:'10%'}
		      ,{field:'suppCode', title: '供应商编码',width:'10%'}
		    ]]
		  ,page:true
	  });
	}
	
	function reloadTable(){

		var suppName = $("#search_suppName").val();
		var statusName=$("#search_status").find("option:selected").text();
		if(statusName == '请选择'){
			statusName = "";
		}
		var prodDateStart=$("#search_prodDateStart").val();
		var prodDateEnd=$("#search_prodDateEnd").val();

		var series = $("#search_series").val();
		var matName = $("#search_matName").val();
		var matType = $("#search_matType").val();
		
    	// 启动加载层
    	var index = layer.load(0, {shade: false});
		$.ajax({
			 type:"post",
			 url:prefix+"/getPdrStockReportInfo",
			 data:{"suppName":suppName,"status":statusName,"prodDateStart":prodDateStart,"prodDateEnd":prodDateEnd
				 ,"series":series,"matName":matName,"matType":matType},
			 dataType:"JSON",
			 success:function(data){
				tableData = data;
				initListTable(tableData);
				
		    	// 关闭加载层
		    	layer.close(index);
			 },
			 error: function (XMLHttpRequest, textStatus, errorThrown) {				 
	 	    	// 关闭加载层
	 	    	layer.close(index);
	         }
		 });
		
	}
	
	//导出库存事件
	$("#exportStockBtn").click(function(){
		exportStock();
	});
	// 查询点击事件
	$("#searchBtn").click(function(){
		reloadTable();
	});

	//查询条件重置
	$("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	});
	
	/**
	 * 导出供应商库存数据
	 * @returns
	 */
	function exportStock(){

		var suppName = $("#search_suppName").val();
		var statusName=$("#search_status").find("option:selected").text();
		if(statusName == '请选择'){
			statusName = "";
		}
		var prodDateStart=$("#search_prodDateStart").val();
		var prodDateEnd=$("#search_prodDateEnd").val();

		var series = $("#search_series").val();
		var matName = $("#search_matName").val();
		var matType = $("#search_matType").val();
		 
		var url = prefix+"/exportPdrStock?suppName="+suppName+"&status="+statusName+"&prodDateStart="+prodDateStart
		+"&prodDateEnd="+prodDateEnd+"&series="+series+"&matName="+matName+"&matType="+matType;
		location.href=url;
	}
});
