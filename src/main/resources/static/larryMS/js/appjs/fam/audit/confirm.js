layui.use('table', function() {
	var table = layui.table;
	var $ = layui.$;
	var searchForm = $("#searchForm");
	searchData = searchForm.serialize();
	loadTable(table, searchData);

	// 搜索
	$("#serachInfo").on('click', function() {
		var searchForm = $("#searchForm");
		searchData = searchForm.serialize();
		loadTable(table, searchData);
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

	table.on('tool(audit)', function(obj) {
		var currentRow = obj.data;
		if (obj.event == 'auditConfirm') {
			// 确认操作
			if (currentRow.auditStatus == "已提交") {
				window.location.href = "/finance/auditConfirmDetails?&auditId="
						+ currentRow.id + "&type=0";
			} else {
				layer.alert("该数据不可进行确认操作！");
			}
		} else if (obj.event = 'details') {
			// 查看操作
			window.location.href = "/finance/auditConfirmDetails?&auditId="
					+ currentRow.id + "&type=1";
		}
	})
	$("#batchConfirmBtn").on(
			'click',
			function() {
				var data = table.checkStatus("auditTable");
				if (data.length > 0) {
					var auditId;
					for ( var i = 0; i < data.length; i++) {
						var elem = data[i];
						if (auditId == undefined) {
							auditId = elem.id;
						} else {
							auditId += "," + elem.id;
						}
					}
					$.ajax({
						url : '/finance/auditConfirm?auditIds=' + auditId
								+ '&type=batch',
						method : 'post',
						async : false,
						success : function(res) {
							layer.msg(res.msg);
							if (res.code == "0") {
								window.history.back(-1);
							}
						}
					});
				} else {
					layer.msg("请选择要批量确认的数据！");
				}
			});
});

function loadTable(table, searchData) {
	table.render({
		elem : '#auditTable',
		url : '/finance/auditConfirmData?searchData=' + searchData,
		page : true,
		cols : [ [ {
			checkbox : true,
			fixed : 'center'
		}, {
			field : 'auditStatus',
			title : '状态'
		}, {
			field : 'auditCode',
			title : '稽核单号'
		}, {
			field : 'suppName',
			title : '供应商'
		}, {
			field : 'suppCode',
			title : '供应商编码'
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
			fixed : 'right',
			title : '操作',
			width : 120,
			align : 'center',
			toolbar : '#operateBar'
		} ] ]
	});
};