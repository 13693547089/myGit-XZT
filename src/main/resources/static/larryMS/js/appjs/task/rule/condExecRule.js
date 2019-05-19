layui.use([ "form" ], function() {
	var $ = layui.$;
	var form = layui.form;

	// 校验比较值
	$("#preCondBtn").on("click", function() {
		debugger;
		var condition = $("#condition").val();
		var r = checkCondition(condition);
		$("#compR").val(r);
	});

	function checkCondition(condition) {
		var result = "";
		$.ajax({
			url : "/rule/checkCondition",
			data : {
				condition : condition
			},
			async : false,
			error : function() {
				layer.msg("程序出错！");
			},
			success : function(res) {
				result = res.msg;
			}
		});
		return result;
	}

	// 获取数据
	window.saveExecRule = function() {
		var result = "";
		var condition = $("#condition").val();
		var tBackNode = $("#tBackNode").val();
		var fBackNode = $("#fBackNode").val();
		var form = $("#signForm").serialize();
		if (condition != "") {
			var msg = "";
			var r = checkCondition(condition);
			if (r !== "error") {
				if (tBackNode == "")
					msg += "条件为真的输出节点不可为空！";
				if (fBackNode == "")
					msg += "条件为假的输出节点不可为空！";
			} else {
				msg = r;
			}
			if (msg == "") {
				$.ajax({
					url : "/rule/saveExecRule",
					data : form,
					async : false,
					error : function() {
						layer.msg("程序出错！")
					},
					success : function(res) {
						if (res.code == 0)
							result = "y_success";
					}
				})
			} else {
				layer.msg(msg);
				result = "error";
			}
		} else {
			$.ajax({
				url : "/rule/removeExecRule",
				data : form,
				async : false,
				error : function() {
					layer.msg("程序出错！")
				},
				success : function(res) {
					result = "n_success";
				}
			})
		}
		return result;
	}
});