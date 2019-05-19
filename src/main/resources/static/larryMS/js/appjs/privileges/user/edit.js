layui.use([ 'form' ], function() {
	var form = layui.form;
	var $ = layui.$;

	form.verify({
		email1 : function(value) {
			if (value == '' || value == undefined)
				return '请输入您的E-mail';
		},
		title : function(value) {
			if (value.length < 5) {
				return '标题至少得5个字符啊';
			}
		},
		pass : [ /(.+){6,12}$/, '密码必须6到12位' ],
		content : function(value) {
			layedit.sync(editIndex);
		}
	});

	// 监听提交事件
	form.on('submit(signForm)', function(data) {
		$.ajax({
			cache : true,
			type : "POST",
			url : "/sys/user/update",
			data : data.field,// 你的formid
			async : false,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				if (data.code == 0) {
					parent.layer.msg(data.msg);
					parent.reLoad();
					var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
					parent.layer.close(index);

				} else {
					parent.layer.msg(data.msg);
				}

			}
		});
		return false;
	});
});