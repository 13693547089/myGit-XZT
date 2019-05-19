layui.use([ 'form' ], function() {
	var form = layui.form;
	var $ = layui.$;

	form.verify({
		sname : function(value) {
			if (value == '' || value == undefined)
				return '请输入组织名称！';
		},
		scode : function(value) {
			if (value == '' || value == undefined)
				return '请输入组织编码！';
		}
	});

	form.on("submit(signForm)", function(data) {
		debugger;
		var formData = data.field;
		// 校验编码是否重复
		var stype = formData.stype;
		var flag1 = true;
		if (stype == 'psn') {
			var leader = formData.leader;
			if (leader == "") {
				layer.msg("请选择直接领导人！");
				flag1 = false;
			}
		}
		if (flag1) {
			var scode = formData.scode;
			var flag = false;
			$.ajax({
				url : '/sys/org/checkCode',
				data : {
					scode : scode,
					sid : formData.menuId
				},
				async : false,
				error : function(request) {
					layer.alert("连接错误！");
				},
				success : function(res) {
					if (res.code == '1') {
						layer.msg("编码重复！");
					} else {
						flag = true;
					}
				}
			});
			if (flag) {
				$
						.ajax({
							cache : true,
							type : "POST",
							url : "/sys/org/orgUpdate",
							data : data.field,// formid
							async : false,
							error : function(request) {
								layer.alert("Connection error");
							},
							success : function(data) {
								if (data.code == 0) {
									parent.layer.msg("保存成功");
									parent.reLoad();
									var index = parent.layer
											.getFrameIndex(window.name); // 获取窗口索引
									parent.layer.close(index);

								} else {
									layer.alert(data.msg)
								}

							}
						});
			} else {
				return false;
			}
		} else {
			return false;
		}
	});
});