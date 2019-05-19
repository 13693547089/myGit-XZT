layui.use('table', function() {
	var $ = layui.$;
	$.ajax({
		cache : true,
		type : "POST",
		url : "/reportData",
		async : false,
		error : function(request) {
			layer.alert("Connection error");
		},
		success : function(data) {

			initialTable(table, data, cols);
		}
	});
})

function initialTable(table, data, cols) {
	table.render({
		elem : '#reportTable',
		height : 315,
		data : data,
		page : true,
		cols : cols
	});
}
