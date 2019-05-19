var searchData;
layui.use('table', function() {
	var $ = layui.$;
	var table = layui.table;
	var searchForm = $("#searchForm");
	searchData = searchForm.serialize();
	load(table, searchData);

	$("#addBtn").on("click", function() {
		var url = "/quote/struEdit?status=0";
		tuoGo(url,'struEdit',"structureTableId");
	});

	$("#batchModifyBtn").on("click", function() {
		var url = "/quote/batchModify";
		tuoGo(url,'batchModify',"structureTableId");
	});

	$("#batchCreateBtn").on("click", function() {
		var url = "/quote/batchCreate";
		tuoGo(url,'batchCreate',"structureTableId");
	});

	$("#serachInfo").on("click", function() {
		var form = $("#searchForm");
		searchData = form.serialize();
		load(table, searchData);
	});

	$("#reset").on("click", function() {
		$("#tempCode").val("");
		$("#mateCode").val("");
		$("#status").val("");
		var form = $("#searchForm");
		searchData = form.serialize();
		load(table, searchData);
	});

	$("#batchDelBtn").on("click", function() {
		var checked = table.checkStatus('structureTableId');
		var rows = checked.data;
		if (rows.length > 0) {
			layer.confirm("是否确定删除选中的数据?", {
				btn : [ '确定', '取消' ]
			}, function(index) {
				layer.close(index);
				$.ajax({
					url : '/quote/batchRemoveStruData',
					type : 'post',
					data : {
						rows : JSON.stringify(rows)
					},
					success : function(r) {
						if (r.code == 0)
							load(table, searchData);
						layer.msg(r.msg);
					}
				});
			});
		} else {
			layer.msg("请选择要删除的数据!");
		}
	})
	table.on('tool(structure)', function(obj) {
		var data = obj.data;
		var id = data.id;
		if (obj.event == "edit") {
			// 编辑
			var url = "/quote/struEdit?status=1&id=" + id;
			tuoGo(url,'struEdit',"structureTableId");
		} else if (obj.event == "detail") {
			// 查看
			var url = "/quote/struEdit?status=2&id=" + id;
			tuoGo(url,'struEdit',"structureTableId");
		} else if (obj.event == "del") {
			// 删除
			layer.confirm('是否确定删除？', {
				btn : [ '确定', '取消' ]
			}, function(index) {
				layer.close(index);
				$.ajax({
					url : '/quote/removeStru',
					type : 'post',
					data : {
						id : id
					},
					success : function(r) {
						if (r.code == 0)
							load(table, searchData);
						layer.msg(r.msg);
					}
				});
			})
		}
	})
})

function load(table, searchData) {
	table.render({
		elem : '#structureTable',
		url : "/quote/struData?sarchData=" + searchData,
		page : true,
		id : "structureTableId",
		cols : [ [ {
			checkbox : true,
			fixed : 'center'
		}, {
			field : 'mateCode',
			title : '物料编码',
			width : 90,
			fixed : 'left'
		}, {
			field : 'mateName',
			title : '物料名称',
			width : 100
		}, {
			field : 'quoteUnit',
			title : '报价单位'
		}, {
			field : 'tempCode',
			title : '模板编码'
		}, {
			field : 'tempName',
			title : '模板名称'
		}, {
			field : 'creatorName',
			title : '创建人'
		}, {
			field : 'createTime',
			title : '创建时间',
			templet : function(d) {
				return formatTime(d.createTime, 'yyyy-MM-dd');
			}
		}, {
			field : 'modifierName',
			title : '修改人'
		}, {
			field : 'modifyTime',
			title : '修改时间',
			templet : function(d) {
				return formatTime(d.modifyTime, 'yyyy-MM-dd');
			}
		}, {
			fixed : 'right',
			title : '操作',
			width : 150,
			align : 'center',
			toolbar : '#operateBar'
		} ] ]
	});
}