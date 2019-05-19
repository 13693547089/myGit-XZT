layui.use([ "table", "form", "laydate" ], function() {
	var $ = layui.$, form = layui.form, table = layui.table, laydate = layui.laydate;
	var currentDay = $("#currentDay").val();
	var defaultDay = $("#defaultDay").val();
	laydate.render({
		elem : '#day_date',
		range : true,
		value : defaultDay,
		isInitValue : true,
		max : currentDay,
	});

	$("#btn_search").on("click", function() {
		debugger;
		var day_date = $("#day_date").val();
		var user_type = $("#user_type").val();
		var user_dept = $("#user_dept").val();
		var username = $("#username").val();
		// 执行重载
		table.reload('log_table', {
			page : false,
			where : {
				day_date : day_date,
				user_type : user_type,
				user_dept : user_dept,
				username : username
			}
		});
	});
	
	/**
	 * 明细
	 */
	table.on("tool(logTable)", function(obj) {
		var data = obj.data;
		if (obj.event === 'details') {
			var username = data.username;
			var userId = data.userId;
			var day_date = $("#day_date").val();
			var url  = "/report/login_details_index?day_date=" + day_date + "&username=" + username+"&userId="+userId;
	    	tuoGo(url,'loginDetails',"logTable");
		}
	});

	table.render({
		id : "log_table",
		elem : '#logTable',
		url : '/report/login_list',
		page : false,
		cols : [ [ {
			field : 'userId',
			title : '编号',
			width : 60
		}, {
			field : 'realName',
			title : '姓名',
			width : 240
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
			field : 'operateNum',
			title : '操作次数',
			width : 100
		}, {
			field : 'loginNum',
			title : '登录次数',
			width : 100
		}, {
			fixed : 'right',
			title : '操作',
			width : 80,
			align : 'center',
			toolbar : '#operateBar'
		} ] ]
	})
});
