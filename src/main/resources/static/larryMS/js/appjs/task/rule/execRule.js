layui.use([ "form" ], function() {
	var $ = layui.$, form = layui.form;

	// 校验任务名称
	$("#taskNameBtn").on("click", function() {
		var taskName = $("#taskName").val();
		if (taskName !== "") {
			var result = checkTaskName(taskName);
			if (result.code == 0) {
				var msg = result.msg;
				var r = JSON.parse(msg);
				layer.confirm("校验通过：" + r.result);
			} else {
				layer.confirm("校验不通过，请检查表达式");
			}
		}
	});
	function checkTaskName(taskName) {
		var result = "";
		$.ajax({
			url : "/rule/checkTaskName",
			data : {
				taskName : taskName
			},
			async : false,
			error : function() {
				layer.msg("Connection error!");
			},
			success : function(res) {
				result = res;
			}
		});
		return result;
	}

	$("#execStrBtn").on("click", function() {
		var execStr = $("#execStr").val();
		if (execStr !== "") {
			var result = checkExecStr(execStr);
			if (result.code == 0) {
				var msg = result.msg;
				var r = JSON.parse(msg);
				layer.confirm("校验通过：" + r.name);
			} else {
				layer.confirm("校验不通过，请检查表达式");
			}
		}

	});
	function checkExecStr(execStr) {
		var result = "";
		$.ajax({
			url : "/rule/checkExecStr",
			data : {
				execStr : execStr
			},
			async : false,
			error : function() {
				layer.msg("Connection error!");
			},
			success : function(res) {
				result = res;
			}
		});
		return result;
	}

	$("#execBtn").on("click", function() {
		var ids = $("#executor").val();
		var names = $("#execName").val();
		// 打开选择执行者
		layer.open({
			type : 2,
			title : '执行者',
			shadeClose : true,
			shade : false,
			maxmin : true, // 开启最大化最小化按钮
			area : [ '400px', '500px' ],
			content : '/processConfig/executor?executor=' + ids,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				// 调用content层的js方法
				var data = $(layero).find("iframe")[0].contentWindow.getData();
				for ( var i = 0; i < data.length; i++) {
					var elem = data[i];
					var id = elem.menuId;
					var name = elem.sname;
					if (ids == null || ids == "" || ids == undefined) {
						ids = id + "";
					} else if (ids.indexOf(id) == -1) {
						ids += "," + id;
					}
					if (names == null || names == "" || names == undefined) {
						names = name;
					} else if (names.indexOf(name) == -1) {
						names += "," + name;
					}
				}
				$("#executor").val(ids);
				$("#execName").val(names);
				layer.close(index);
			},
			btn2 : function(index, layero) {
				layer.close(index);
			}
		});
	});

	// 保存执行规则
	window.saveExecRule = function() {
		debugger;
		var result = "";
		var taskName = $("#taskName").val();
		var defaultAudti = $("#defaultAudti").val();
		var execType = $("#execType").val();
		var execStr = $("#execStr").val();
		var execRange = $("#execRange").val();
		var openChooseDialog = $("#openChooseDialog").val();
		var msg = "";
		var flag = false;
		if (taskName != "") {
			flag = true;
			var result = checkTaskName(taskName);
			if (result.code !== 0) {
				msg += "任务名称校验不通过";
			}
		}
		if (defaultAudti != "" && defaultAudti != undefined)
			flag = true;
		if (execType != "" && defaultAudti != undefined)
			flag = true;
		if (execStr != "") {
			flag = true;
		}
		if (execRange != "")
			flag = true;
		if (openChooseDialog != "")
			flag = true;
		if (flag) {
			if (msg == "") {
				var form = $("#signForm").serialize();
				$.ajax({
					url : "/rule/saveExecRule",
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
			} else {
				layer.msg(msg);
			}
		} else {
			// 删除当前的执行规则
			var form = $("#signForm").serialize();
			$.ajax({
				url : "/rule/removeExecRule",
				data : form,
				async : false,
				error : function() {
					layer.msg("Connection error!");
				},
				success : function(res) {
					var code = res.code;
					if (code == 0) {
						result = "n_success";
					} else {
						layer.msg(res.msg);
					}
				}
			});
		}
		return result;
	};
});