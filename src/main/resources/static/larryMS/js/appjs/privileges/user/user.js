layui.use([ 'table' ], function() {
	var $ = layui.$;
	var table = layui.table;

	table.render({
		elem : '#userTable',
		url : '/sys/user/list',
		page : true,
		limit : 10,
		cols : [ [ {
			field : 'userId',
			title : '编号',
			width : 60
		}, {
			field : 'name',
			title : '姓名',
			width : 90
		}, {
			field : 'username',
			title : '用户名',
			width : 100
		}, {
			field : 'email',
			title : '邮箱',
			width : 180
		}, {
			field : 'leaderName',
			title : '直接上级',
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
			field : 'sfnames',
			title : '全路径'
		}, {
			field : 'status',
			title : '状态',
			align : 'center',
			width : 60,
			templet : function(d) {
				if (d.status == '1') {
					return '<span>正常</span>';
				} else {
					return '<span>禁用</span>';
				}
			}
		}, {
			fixed : 'right',
			title : '操作',
			width : 80,
			align : 'center',
			toolbar : '#operateBar'
		} ] ],
		id : 'testReload'
	});

	table.on('tool(user)', function(obj) {
		var data = obj.data;
		if (obj.event === 'resetPWd') {
			layer.confirm("是否确定重置密码？", function(index) {
				$.ajax({
					url : "/sys/user/reset",
					method : "post",
					data : {
						userId : data.userId,
						password : "123456"
					},
					async : false,
					error : function() {
						layer.msg("Connection error!");
					},
					success : function(res) {
						layer.msg(res.msg);
					}
				});
			});

			// layer.open({
			// type : 2,
			// title : '重置密码',
			// maxmin : true,
			// shadeClose : false, // 点击遮罩关闭层
			// area : [ '400px', '260px' ],
			// content : '/sys/user/resetPwd/' + data.userId // iframe的url
			// });
		}
	});
	var active = {
		reload : function() {
			var query = $("#demoReload").val();
			// 执行重载
			table.reload('testReload', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					queryParams : query
				}
			});
		}
	};
	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});

	// 加密
	$("#encrypt").on("click", function() {
		$.ajax({
			url : "/sys/user/encrypt",
			method : "post",
			async : false,
			error : function() {
				layer.msg("Connection error!");
			},
			success : function(res) {
				layer.msg(res.msg);
			}
		});
	});
});