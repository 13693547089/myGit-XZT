layui.use([ 'form', 'table' ], function() {
	var table = layui.table;
	var $ = layui.$;

	table.render({
		elem : '#toChooseTable',
		url : "/mate/to_choose_single_data",
		page : true,
		cols : [ [ {
			type : "radio"
		}, {
			field : 'mateCode',
			title : '物料编码'
		}, {
			field : 'mateName',
			title : '物料名称'
		}, {
			field : 'mateGroupExpl',
			title : '物料组'
		}, {
			field : 'mateType',
			title : '物料类型'
		} ] ]
	});

	// 搜索物料
	$("#mateCode").keydown(function(e) {
		if (e.keyCode == 13) {
			var mateCode = $('#mateCode').val();
			// 执行重载
			table.reload('toChooseTable', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					mateCode : mateCode
				}
			});
		}
	});
	
	// 搜索供应商
	$("#mateName").keydown(function(e) {
		if (e.keyCode == 13) {
			var mateName = $('#mateName').val();
			// 执行重载
			table.reload('toChooseTable', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					mateName : mateName
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
