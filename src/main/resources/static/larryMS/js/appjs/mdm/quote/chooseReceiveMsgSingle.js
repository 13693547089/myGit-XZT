layui.use([ 'form', 'table' ], function() {
	var table = layui.table;
	var $ = layui.$;

	table.render({
		elem : '#toChooseTable',
		url : "/queryReceiveMessByPage",
		page : true,
		cols : [ [ {
			type : "radio"
		}, {
			field : 'freightRange',
			title : '收货编码'
		}, {
			field : 'receUnit',
			title : '收货单位'
		}, {
			field : 'receAddr',
			title : '收货地址'
		}, {
			field : 'contact',
			title : '联系人'
		} ] ]
	});

	// 搜索物料
	$("#receUnit").keydown(function(e) {
		if (e.keyCode == 13) {
			var receUnit = $('#receUnit').val();
			// 执行重载
			table.reload('toChooseTable', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					receUnit : receUnit
				}
			});
		}
	});
	
	// 搜索供应商
	$("#freightRange").keydown(function(e) {
		if (e.keyCode == 13) {
			var freightRange = $('#freightRange').val();
			// 执行重载
			table.reload('toChooseTable', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					freightRange : freightRange
				}
			});
		}
	});

	// 处理获取到的数据
	window.getData = function() {
		var checkStatus = table.checkStatus("toChooseTable"); // 获取选中行状态
		var data = checkStatus.data; // 获取选中行数据
		return JSON.stringify(data);
	};
});
