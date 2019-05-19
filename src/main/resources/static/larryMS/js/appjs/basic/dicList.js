layui.use([ "form", "table" ], function() {
	var $ = layui.$;
	var form = layui.form;
	var table = layui.table;
	// 搜索
	$("#serachInfo").on("click", function() {
		var cateName = $("#cateName").val();
		var cateCode = $("#cateCode").val();
		var isUsed = $("#isUsed").val();
		table.reload('dicListTable', {
			where : {
				cateName : cateName,
				cateCode : cateCode,
				isUsed : isUsed
			}
		});
	});

	// 重置
	$("#reset").on("click", function() {
		$("#cateName").val("");
		$("#cateCode").val("");
		$("#isUsed").val("");
		table.reload('dicListTable', {
			where : {
				cateName : "",
				cateCode : "",
				isUsed : "",
			}
		});
	});

	// 创建字典
	$("#createBtn").on("click", function() {
		openEditDialog("add", "");
	});
	table.render({
		elem : '#dicListTable',
		url : "/basic/dicInfo",
		page : true,
		cols : [ [ {
			checkbox : true
		}, {
			field : 'cateName',
			title : '分类名称'
		}, {
			field : 'cateCode',
			title : '分类编码'
		}, {
			field : 'isUsed',
			title : '状态',
			templet : function(d) {
				if (d.isUsed == '1') {
					return '<span>启用</span>';
				} else {
					return '<span>禁用</span>';
				}
			}
		}, {
			field : 'creatorName',
			title : '创建人'
		}, {
			field : 'createTime',
			title : '创建时间',
			templet : function(d) {
				return formatTime(d.createTime, 'yyyy-MM-dd hh:mm:ss');
			}
		}, {
			field : 'modifierName',
			title : '修改人'
		}, {
			field : 'updatetime',
			title : '修改时间',
			templet : function(d) {
				return formatTime(d.updatetime, 'yyyy-MM-dd hh:mm:ss');
			}
		} ] ]
	});

	// 监听table操作列
	table.on("tool(dicListTable)", function(obj) {
		var currentRow = obj.data;
		var id = currentRow.id;
		if (obj.event === 'edit') {
			openEditDialog("edit", id);
		} else if (obj.event === 'read') {
			layer.open({
				type : 2,
				title : "字典信息",
				maxmin : true,
				shadeClose : false, // 点击遮罩关闭层
				area : [ '800px', '520px' ],
				content : '/basic/dicDetails?type=read&id=' + id
			});
		}
	});

	function openEditDialog(type, id) {
		layer.open({
			type : 2,
			title : "字典信息",
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : '/basic/dicDetails?type=new&id=' + id,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {

			},
			btn2 : function(index, layero) {
				layer.close(index);
			}
		});
	}
})