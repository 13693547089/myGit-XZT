layui.use('table', function() {
	var table = layui.table;
	// 监听表格复选框选择
	table.on('checkbox(demo)', function(obj) {
		// console.log(obj)
	});
	// 条件搜索
	var $ = layui.$, active = {
		reload : function() {
			var suppInfo = $('#suppInfo');
			var category = $("#category");
			var provcity = $("#provcity");
			// 执行重载
			table.reload('qualSuppTableId', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					// key: {
					suppInfo : suppInfo.val(),
					category : category.val(),
					provcity : provcity.val()
				// }
				}
			});
		}
	};
	// 监听单元格事件
	table.on('tool(demo)',
			function(obj) {
				var data = obj.data;
				if (obj.event === 'check') {// 查看
					var url = "/getCheckQualSuppHtml?suppId=" + data.suppId
							+ "&type=1";
					//location = url;
					tuoGo(url,'qualSuppLook',"qualSuppTableId");
				} else if (obj.event === 'edit') {// 编辑
					var url = "/getCheckQualSuppHtml?suppId=" + data.suppId
							+ "&type=2";
					//location = url;
					tuoGo(url,'qualSuppLook',"qualSuppTableId");
				}
			});

	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});

	$("#synchron").on('click', function() {
		$.ajax({
			url : '/asyncSuppInfo',
			method : 'get',
			async : true,
			success : function(res) {
				layer.confirm(res.msg);
			},
			complete : function(XMLHttpRequest, status) {
				if (status == 'timeout') {
					layer.msg("超时");
				}
			}
		});
	});
});

// 重置
$("#reset").click(function() {
	$("#suppInfo").val('');
	$("#category").val('');
	$("#provcity").val('');
});
