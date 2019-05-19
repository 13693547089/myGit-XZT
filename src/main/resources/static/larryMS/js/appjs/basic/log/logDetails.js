layui.use([ "form", "table" ], function() {
	var $ = layui.$;
	var form = layui.form;
	var table = layui.table;

	var userName = $("#userName").val();
	table.render({
		elem : "#logDetailsTable",
		url : "/basic/getLogDetailsByUserName?userName=" + userName,
		page : true,
		cols : [ [ {
			field : 'operation',
			title : '操作'
		}, {
			field : 'createTime',
			title : '登录时间',
			templet : function(d) {
				return formatTime(d.createTime, 'yyyy-MM-dd hh:mm:ss');
			}
		}, {
			field : 'ip',
			title : '登录IP'
		}, {
			field : 'macAddr',
			title : 'MAC地址'
		} ] ]
	});

	$("#backBtn").on("click", function() {
		window.history.back(-1);
	});
});