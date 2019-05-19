var tableData;

layui.use([ 'table' ], function() {
	var table = layui.table;
	var $ = layui.$;
	var userJson = $("#userJson").val();
	tableData = JSON.parse(userJson);
	table.render({
		elem : '#executorTable',
		data : tableData,
		cols : [ [ {
			checkbox : true
		}, {
			field : 'username',
			title : '登录名'
		}, {
			field : 'name',
			title : '用户名'
		} ] ]
	});
});

function getData() {
	var result = [];
	for ( var i = 0; i < tableData.length; i++) {
		var elem = tableData[i];
		if (elem.LAY_CHECKED) {
			var arr = {
				name : elem.name,
				id : elem.userId,
				userName : elem.username
			};
			result.push(arr);

		}
	}
	return result;
}
