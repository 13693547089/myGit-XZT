layui.use([ "form" ], function() {
	var form = layui.form;
	var $ = layui.$;

	// 保存回退规则
	window.saveBackRule = function() {
		var result = "";
		var form = $("#signForm").serialize();
		$.ajax({
			url : "/rule/saveBackRule",
			data : form,
			async : false,
			error : function() {
				layer.msg("Connection error!");
			},
			success : function(res) {
				var code = res.code;
				if (code == 0) {
					result = "y_success";
				} else {
					layer.msg(res.msg);
				}
			}
		});
		return result;
	}
});