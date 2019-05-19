layui.use([ 'table', 'form' ], function() {
	var table = layui.table;
	var form = layui.form;
	var $ = layui.$;

	$("#chooseBtn").on('click', function() {
		layer.open({
			type : 2,
			title : '执行者',
			shadeClose : true,
			shade : false,
			maxmin : true, // 开启最大化最小化按钮
			area : [ '400px', '500px' ],
			content : '/processConfig/executor?executor=' + executor,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				// 调用content层的js方法
				var data = $(layero).find("iframe")[0].contentWindow.getData();
				var ids = "";
				var name = "";
				for ( var i = 0; i < data.length; i++) {
					var elem = data[i];
					ids += elem.menuId + ',';
					name += elem.sname + ',';
				}
			},
			btn2 : function(index, layero) {
				layer.close(index);
			}
		});
	});
});
