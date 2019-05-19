var dicData = [];

layui.use('table', function() {
	var table = layui.table;
	var $ = layui.$;
	var cateId = $("#cateId").val();
	$.get("/basic/getDicDetailsData?cateId=" + cateId, function(result) {
		debugger;
		dicData = result;
		initTable(table);
	});

	// 新建字典
	$("#addDetailsBtn").on("click", function() {
		var arr = {
			id : "",
			dicName : "",
			dicCode : ""
		}
		dicData.push(arr);
		initTable(table);
	});
});

// 初始化表格
function initTable(table) {
	table.render({
		elem : '#dicDetailsTable',
		data : dicData,
		page : true,
		limits : [ 5, 10, 20 ],
		limit : 10,
		cols : [ [ {
			field : 'dicName',
			width : 120,
			edit : 'text',
			title : '字典名称'
		}, {
			field : 'dicCode',
			width : 120,
			edit : 'text',
			title : '字典编码'
		}, {
			field : 'remark',
			width : 360,
			edit : 'text',
			title : '备注'
		} ] ]
	});
}