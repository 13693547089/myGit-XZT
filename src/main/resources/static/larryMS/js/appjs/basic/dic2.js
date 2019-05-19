layui.use([ 'table', 'form' ], function() {
	var table = layui.table;
	var $ = layui.$;

	table.render({
		elem : '#dicTable',
		url : '/dicInfo/dic/',
		cellMinWidth : 120 // 全局定义常规单元格的最小宽度，layui 2.2.1 新增
		,
		cols : [ [ {
			field : 'id',
			width : 120,
			title : 'ID',
			sort : true
		}, {
			field : 'cateName',
			width : 120,
			title : '分类名称'
		}, {
			field : 'cateCode',
			width : 120,
			title : '分类编码',
			sort : true
		}, {
			field : 'isUsed',
			width : 120,
			title : '启用标识'
		} ] ],
		id : 'dicReload',
		page : true
	});

	var form = layui.form;
	form.on('submit(Search)', function(data) {
		debugger;
		table.reload('dicReload', {
			where : {
				cateName : data.field.cateName,
				cateCode : data.field.cateCode
			}
		});
		return false;
	});

	$('#add').on('click', function() {
		window.location.href="/dicDetails";
		
//		layer.open({
//			type : 2,
//			title : '添加字典',
//			maxmin : true,
//			area : [ '800px', '360px' ],
//			shade : false,
//			shadeClose : false, // 点击遮罩关闭
//			btn : [ '确定', '取消' ],
//			content : '/dicDetails',
//			yes: function(index, layero) {
//				debugger;
//				layer.close(index);
//			}
//			
//		});
	});
});
