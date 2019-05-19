layui.use([ 'form', 'table' ], function() {
	var table = layui.table;
	var $ = layui.$;
	var selected = $("#selected").val();
	var type = $("#type").val();
	table.render({
		elem : '#toChooseTable',
		url : '/quote/toChooseData?selected=' + selected + "&type=" + type,
		page : false,
		cols : [ [ {
			checkbox : true,
			fiexd : 'center'
		}, {
			field : 'mateCode',
			title : '物料编码'
		}, {
			field : 'mateName',
			title : '物料名称'
		}, {
			field : 'tempCode',
			title : '模板编码'
		}, {
			field : 'tempName',
			title : '模板名称'
		} ] ]
	});

	loadSelectedTable(table, []);

	$("#chooseBtn").on("click", function() {
		var checked = table.checkStatus('toChooseTable');
		var checkedRows = checked.data;
		if (checkedRows.length > 0) {
			loadSelectedTable(table, checkedRows);
			var tt = JSON.stringify(checkedRows);
			$("#selected").val(tt);
		}
	});

	$("#removeBtn").on("click", function() {
		var checked = table.checkStatus("selectedTable");
		var checkedRows = checked.data;
		var old = table.cache.selectedTable;
		for ( var i = 0; i < checkedRows.length; i++) {
			for ( var k = 0; k < old.length; k++) {
				if (checkedRows[i].id == old[k].id) {
					old.splice(i, 1);
					break;
				}
			}
		}
		loadSelectedTable(table, old);
		var tt = JSON.stringify(old);
		$("#selected").val(tt);
	});
});
function loadToSelectedTable(table, data) {
	table.render({
		elem : '#toChooseTable',
		data : data,
		page : false,
		cols : [ [ {
			checkbox : true,
			fiexd : 'center'
		}, {
			field : 'mateCode',
			title : '物料编码'
		}, {
			field : 'mateName',
			title : '物料名称'
		}, {
			field : 'tempCode',
			title : '模板编码'
		}, {
			field : 'tempName',
			title : '模板名称'
		} ] ]
	});
}
function loadSelectedTable(table, data) {
	table.render({
		elem : '#selectedTable',
		data : data,
		page : true,
		cols : [ [ {
			checkbox : true,
			fiexd : 'center'
		}, {
			field : 'mateCode',
			title : '物料编码'
		}, {
			field : 'mateName',
			title : '物料名称'
		}, {
			field : 'tempCode',
			title : '模板编码'
		}, {
			field : 'tempName',
			title : '模板名称'
		} ] ]
	});
}