var tableData;
var tableCarData = [];
var table;
layui.use([ 'form', 'table' ], function() {
	var form = layui.form;
	table = layui.table;
	layer = layui.layer;
	var $ = layui.$;
	var type = $("#type").val();
	// 返回
	$("#goBack").click(function() {
		window.history.back(-1);
	});
	// 保存
	$("#saveReceMess").click(function() {
		var a = verifyInput();
		if (a) {
			var formData = $("#receMessForm").serialize();
			$.ajax({
				type : "POST",
				url : "/addReceiveMessage",
				data : formData,
				dataType : "JSON",
				async : false,
				error : function(request) {
					layer.alert("Connection error");
				},
				success : function(data) {
					if (data) {
						layer.msg("收货信息保存成功");
						window.history.back(-1);
					} else {
						layer.alert("收货信息保存失败");
					}
				}
			});
		} else {
			layer.msg("请把收货信息填写完整");
		}
	});
	// 验证Input
	function verifyInput() {
		var judge = false;
		var receUnit = $("#receUnit").val();
		var receAddr = $("#receAddr").val();
		var contact = $("#contact").val();
		var phone = $("#phone").val();
		var post = $("#post").val();
		var freightRange = $("#freightRange").val();
		if (receUnit == '' || receAddr == '' || contact == '' || phone == '' || post == '' ||freightRange=='') {
			judge = false;
			//layer.alert("请填写收货信息");
		} else {
			judge = true;
		}
		return judge;
	}

});
