var table;
var str;
var tableData = [];
var cutLiaiMate;
var fieldsData = [];
layui.use([ 'table', 'laydate' ], function() {
	table = layui.table;
	var $ = layui.$;
	var laydate = layui.laydate;
	var liaiId = $("#liaiId").val();
	
	
	// 返回
	$("#goBack").click(function() {
		//window.history.back(-1);
		tuoBack('.ContactSheetCheck','#search');
	});
	// 年月选择器
	laydate.render({
		elem : '#cutMonth',
		type : 'month'
	});
	
	$('td').removeAttr('data-edit')
	
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
	debugger;
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
