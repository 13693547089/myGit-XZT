layui.use(['table','laydate'], function() {
	var table = layui.table;
	var laydate = layui.laydate;
	var searchForm = $("#searchForm");
	searchData = searchForm.serialize();
	loadTable(table, searchData);
	// 搜索
	$("#serachInfo").on('click', function() {
		var searchForm = $("#searchForm");
		searchData = searchForm.serialize();
		loadTable(table, searchData);
	});
	laydate.render({
	    elem: '#startDate', //指定元素
	});
	laydate.render({
		    elem: '#endDate', //指定元素
	});
	// 重置
	$("#reset").on('click', function() {
		$("#suppName").val("");
		$("#auditCode").val("");
		$("#startDate").val("");
		$("#endDate").val("");
		var searchForm = $("#searchForm");
		searchData = searchForm.serialize();
		loadTable(table, searchData);
	});
	// 新建
	$("#addBtn").on('click', function() {
		window.location.href = "/finance/auditEdit?status=0"
	});
	// 批量删除
	$("#batchDelBtn").on('click', function() {
		var checkRow = table.checkStatus("auditTable");
		var data = checkRow.data;
		if (data.length > 0) {
			var msg = "";
			for ( var k = 0; k < data.length; k++) {
				var elem = data[k];
				if (elem.auditStatus != '已保存') {
					if (msg == "") {
						msg = elem.auditCode;
					} else {
						msg += "," + elem.auditCode;
					}
				}
			}
			if (msg != "") {
				layer.msg("稽核单号为：" + msg + "的稽核单不可删除！");
			} else {
				layer.confirm('确定要删除选择的数据？', {
					btn : [ '确认', '取消' ]
				}, function() {
					$.ajax({
						url : '/finance/batchRemoveAudit',
						type : 'post',
						data : {
							rows : JSON.stringify(data)
						},
						success : function(res) {
							if (res.code == 0) {
								layer.msg("删除成功");
								var searchForm = $("#searchForm");
								searchData = searchForm.serialize();
								loadTable(table, searchData);
							} else {
								layer.msg(res.msg);
							}
						}
					});
				});
			}
		} else {
			layer.msg("请选择要删除的数据！");
		}
	});

	table.on('tool(audit)', function(obj) {
		var currentRow = obj.data;
		if (obj.event == 'edit') {
			if (currentRow.auditStatus == '已保存'
					|| currentRow.auditStatus == '已退回') {
				window.location.href = "/finance/auditEdit?status=1&auditId="
						+ currentRow.id;
			} else {
				layer.alert("不可编辑！");
			}
		} else if (obj.event == 'detail') {
			window.location.href = "/finance/auditEdit?status=2&auditId="
					+ currentRow.id;

		} else if (obj.event == 'del') {
			if (currentRow.auditStatus != "已保存") {
				layer.msg("非保存状态的数据不可删除！")
			} else {
				layer.confirm('确定删除行吗？', {
					btn : [ '确定', '取消' ]
				}, function() {
					$.ajax({
						url : '/finance/removeAudit',
						type : 'post',
						data : {
							id : currentRow.id
						},
						success : function(res) {
							if (res.code == 0) {
								layer.msg("删除成功");
							} else {
								layer.msg(res.msg);
							}
						}
					});
				});
			}
		}
	})
});

function loadTable(table, searchData) {
	table.render({
		elem : '#auditTable',
		url : '/finance/auditList?searchData=' + searchData,
		page : true,
		cols : [ [ {
			checkbox : true,
			fixed : 'center'
		}, {
			field : 'auditStatus',
			width:83,
			title : '状态'
		}, {
			field : 'auditCode',
			width:108,
			title : '稽核单号'
		}, {
			field : 'suppName',
			width:347,
			title : '供应商'
		}, {
			field : 'suppCode',
			width:121,
			title : '供应商编码'
		}, {
			field : 'creatorName',
			width:283,
			title : '创建人'
		}, {
			field : 'createTime',
			width:124,
			title : '创建时间',
			templet : function(d) {
				return formatTime(d.createTime, 'yyyy-MM-dd');
			}
		}, {
			fixed : 'right',
			title : '操作',
			width : 150,
			align : 'center',
			toolbar : '#operateBar'
		} ] ]
	});
};