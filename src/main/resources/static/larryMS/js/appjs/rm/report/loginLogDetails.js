layui.use([ "table" ], function() {
	var $ = layui.$;
	var table = layui.table;
	var day_date = $("#day_date").val();
	var username = $("#username").val();
	var userId = $("#userId").val();
	
	$("#btn_back").on("click", function() {
		$(".layui-show", parent.document).find('.larryms-iframe').css('display','');
		$(".layui-show", parent.document).find(".loginDetails").remove();
	});

	table.render({
		id : "log_table",
		elem : '#logDetailsTable',
		url : '/report/login_details?day_date=' + day_date + "&username="
				+ username + "&userId=" + userId,
		page : false,
		cols : [ [ {
			field : 'userId',
			title : '编号',
			width : 60
		}, {
			field : 'realName',
			title : '姓名',
			width : 90
		}, {
			field : 'username',
			title : '用户名',
			width : 100
		}, {
			field : 'userType',
			title : '用户类型',
			width : 90,
			templet : function(d) {
				if (d.userType == 'user') {
					return '<span>内部用户</span>';
				} else {
					return '<span>外部供应商</span>';
				}
			}
		}, {
			field : 'userDept',
			title : '部门',
			width : 180
		}, {
			field : 'dateStr',
			title : '日期',
			width : 120
		}, {
			field : 'operateNum',
			title : '操作次数',
			width : 100
		}, {
			field : 'loginNum',
			title : '登录次数',
			width : 100
		} ] ]
	});
});