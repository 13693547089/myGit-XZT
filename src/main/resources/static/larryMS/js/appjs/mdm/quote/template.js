var searchData;
layui.use('table', function() {
	var $ = layui.$;
	var table = layui.table;
	var searchForm = $("#searchForm");
	searchData = searchForm.serialize();
	load(table, searchData);
	$("#addBtn").on("click", function() {
		var url = "/quote/tempEdit";
		tuoGo(url,'tempEdit',"templateTableId");
	});
	$("#delBtn").on("click", function() {
		var checkStatus = table.checkStatus('templateTable');
		var checked = checkStatus.data;
		if (checked.length > 0) {
			layer.confirm('是否确定删除数据？', function(index) {
				var ids = [];
				for ( var i = 0; i < checked.length; i++) {
					ids.push(checked[i].id);
				}
				$.ajax({
					url : "/quote/batchRemoveTemp",
					type : "post",
					data : {
						ids : JSON.stringify(ids)
					},
					success : function(data) {
						if (data.code == 0) {
							layer.msg("删除成功");
							load(table, searchData);
						} else {
							layer.msg(data.msg);
						}
					}
				});
			})
		} else {
			layer.msg("请选择要删除的数据！");
		}
	})

	$("#serachInfo").on("click", function() {
		var form = $("#searchForm");
		searchData = form.serialize();
		load(table, searchData);
	})

	$("#reset").on("click", function() {
		$("#tempCode").val("");
		$("#tempName").val("");
		$("#status").val("");
		var form = $("#searchForm");
		searchData = form.serialize();
		load(table, searchData);
	})

	// 监听工具条
	table.on('tool(tempTable)', function(obj) {
		var data = obj.data;
		if (obj.event === 'detail') {
			var url = "/quote/tempView?tempId=" + data.id;
			tuoGo(url,'tempView',"templateTableId");
		} else if (obj.event === 'del') {
			layer.confirm('是否确认删除数据？', function(index) {
				deleteObj(obj.data.id);
			});
		} else if (obj.event === 'edit') {
			if (data.status != '1')
				var url = "/quote/tempEdit?tempId=" + data.id;
			    tuoGo(url,'tempEdit',"templateTableId");
		} else if (obj.event === 'copy') {
			// 复制
			var url = "/quote/tempCopy?tempId=" + data.id;
			tuoGo(url,'tempCopy',"templateTableId");
		}
	});

	// 逐条删除
	function deleteObj(id) {
		$.ajax({
			url : "/quote/removeTemp",
			type : "post",
			data : {
				'id' : id
			},
			success : function(data) {
				if (data.code == 0) {
					layer.msg("删除成功");
					load(table);
				} else {
					layer.msg(data.msg);
				}
			}
		});
	}
})

function load(table, searchData) {
	table.render({
		elem : '#templateTable',
		url : "/quote/tempData?sarchData=" + searchData,
		page : true,
		id :"templateTableId",
		cols : [ [ {
			checkbox : true,
			fixed : 'center'
		}, {
			field : 'tempCode',
			title : '模版编号',
			width : 90,
			fixed : 'left'
		}, {
			field : 'tempName',
			title : '模板名称',
			width : 300
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
			width : 200,
			align : 'center',
			toolbar : '#operateBar'
		} ] ]
	});
}