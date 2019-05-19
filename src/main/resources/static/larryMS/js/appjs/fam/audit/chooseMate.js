var checkedRow = [];
var mateData = [];
layui.use([ 'table' ], function() {
	var $ = layui.$;
	var table = layui.table;
	var suppId = $("#suppId").val();
	var mateIds = $("#mateIds").val();
	initTable(table);
	$.ajax({
		url : '/finance/chooseMateData?suppId=' + suppId,
		method : 'get',
		async : false,
		success : function(res) {
			mateData = res.data;
			if (mateIds != undefined)
				for ( var i = 0; i < mateData.length; i++) {
					var elem = mateData[i];
					if (mateIds.indexOf(elem.mateId) > -1) {
						elem.LAY_CHECKED = true;
						checkedRow.push(elem);
					}
				}
			initTable(table);
		}
	});
	// 监听table前面的选中事件
	table.on('checkbox(mateFilter)', function(obj) {
		debugger;
		var type = obj.type;
		var checked = obj.checked;
		var data = obj.data;
		if (type == 'all') {
			var cacheData = table.cache.mateTable;
			if (checked) {
				var tempData = checkedRow;
				for ( var k = 0; k < cacheData.length; k++) {
					var elem = cacheData[k];
					if (tempData.indexOf(elem) == -1)
						checkedRow.push(elem);
				}
			} else {
				var tempData = [];
				for ( var k = 0; k < checkedRow.length; k++) {
					var elem = checkedRow[k];
					if (cacheData.indexOf(elem) == -1)
						tempData.push(elem);
				}
				checkedRow = tempData;
			}
		} else if (type = 'one') {
			if (checked) {
				checkedRow.push(data);
			} else {
				var tempData = [];
				for ( var i = 0; i < checkedRow.length; i++) {
					var elem = checkedRow[i];
					if (elem.mateId != data.mateId)
						tempData.push(elem);
				}
				checkedRow = tempData;
			}
		}
	});

	// 搜索
	$("#searchBtn").on('click', function() {
		var form = $("#searchForm");
		var data = form.serialize();
		$.ajax({
			url : '/finance/chooseMateData?suppId=' + suppId,
			method : 'get',
			data : data,
			async : false,
			success : function(res) {
				mateData = res.data;
				if (mateIds != undefined)
					for ( var i = 0; i < mateData.length; i++) {
						var elem = mateData[i];
						if (mateIds.indexOf(elem.mateId) > -1) {
							elem.LAY_CHECKED = true;
						}
					}
				initTable(table);
			}
		});
	});
});

function initTable(table) {
	table.render({
		elem : '#mateTable',
		data : mateData,
		page : true,
		cols : [ [ {
			checkbox : true,
			fixed : 'center'
		}, {
			field : "mateCode",
			title : "物料编码",
			width : 175
		}, {
			field : "mateName",
			title : "物料名称",
			width : 244
		}, {
			field : "mateGroupExpl",
			title : "物料组",
			width : 135
		}, {
			field : "mateType",
			title : "物料类型",
			width : 90
		}, {
			field : "basicUnit",
			title : "基本单位",
			width : 83
		}, {
			field : "procUnit",
			title : "采购单位",
			width : 100
		} ] ]
	});
}
// 父页面获取子页面的数据的方法
function getMateData() {
	return checkedRow;
}