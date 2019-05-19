var table;
var str;
var tableData = [];
var liaiId;
layui.use([ 'table', 'laydate' ], function() {
	table = layui.table;
	var $ = layui.$;
	var layer = layui.layer;
	var laydate = layui.laydate;
	liaiId = $("#liaiId").val();
	// 返回
	$("#goBack").click(function() {
		//window.history.back(-1);
		tuoBack('.ContactConfirmDetail','#search');
	});
	// 年月选择器
	laydate.render({
		elem : '#cutMonth',
		type : 'month'
	});
	//确认
	$("#affirmBut").click(function(){
		var liaiIds=[];
		liaiIds.push(liaiId);
		var liaiIdJson = JSON.stringify(liaiIds);
		var suppName = $("#suppName").val();
		//var createDate = $("#createDate").val();
		var remark = "打切联络单审核: "+suppName;
		var flag = taskProcess(liaiId, "cutLiaiAudit", remark, "process");
		if(flag=="over_success"){
			$.ajax({
				type : "post",
				url : "/updateStatusOfCutLiaisonByLiaiId?liaiIdJson="+liaiIdJson,
				dataType : "JSON",
				async : false,// 注意
				success : function(data) {
					if(data){
						layer.msg("打切联络单确认成功");
						//window.history.back(-1);
						tuoBack('.ContactConfirmDetail','#search');
					}else{
						layer.msg("打切联络单确认失败");
					}
					
				},
				error : function() {
					layer.alert("Connection error");
				}
			})
		}
	});
	//退回
	$("#backBut").click(function(){
		var liaiIds=[];
		liaiIds.push(liaiId);
		var liaiIdJson = JSON.stringify(liaiIds);
		var result = backProcess(liaiId);
		if(result == "back_success"){
			$.ajax({
				type : "post",
				url : "/updateStatusOfCutLiaisonByLiaiId2?liaiIdJson="+liaiIdJson,
				dataType : "JSON",
				async : false,// 注意
				success : function(data) {
					if(data){
						layer.msg("打切联络单退回成功");
						//window.history.back(-1);
						tuoBack('.ContactConfirmDetail','#search');
					}else{
						layer.msg("打切联络单退回失败");
					}
				},
				error : function() {
					layer.alert("Connection error");
				}
			})
		}
	});
	 //导出
	  $("#ExportBut").click(function(){
		var  status = $("#status").val();
		  if(status == '已确认' || status == '已提交'){
			  layer.confirm('确定要导出当前打切联络单吗？', function(index){
				  location="/exportCutLiaison?liaiId="+liaiId;
				  layer.close(index);
			  });
		  }else{
			  layer.alert('<span style="color:red;">只有"已确认","已提交"状态的打切联络单才能导出</sapn>');	 
		  }
		  
	  });
	
	// debugger;
	// 获取字段
	$.ajax({
		type : "post",
		url : "/queryLiaiMateFields?liaiId="+liaiId,
		dataType : "JSON",
		async : false,// 注意
		success : function(data) {
			str = data;
		},
		error : function() {
			layer.alert("Connection error");
		}
	})
	//获取物料数据
	$.ajax({
		type : "post",
		url : "/getData?id="+liaiId,
		dataType : "JSON",
		async : false,// 注意
		success : function(data) {
			tableData = data.data;
		},
		error : function() {
			layer.alert("Connection error");
		}
	});
	initMateTable();
});

function initMateTable(){
	table.render({
		elem : "#cutLiaiMateTable",
		data : tableData,
		page : true,
		width : '100%',
		minHeight : '20px',
		id : "cutLiaiMateTableId",
		cols : [ str ]
	})
	$('table td').removeAttr('data-edit');
}
