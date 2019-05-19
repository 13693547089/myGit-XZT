var table;
var checkedData;
var sapId;
var zzoem;
layui.use(['table','laydate'], function() {
	table = layui.table;
	var $ = layui.$;
	var laydate = layui.laydate;
	laydate.render({
		elem:"#startDate"
	})
	laydate.render({
		elem:"#endDate"
	})
	var index = parent.layer.getFrameIndex(window.name);
	var parentData = parent.data;
	sapId = parentData.sapId;
	zzoem = parentData.zzoem;
	initOrderTable();
	// 监听表格复选框选择
	table.on('checkbox(demo)', function(obj) {
	});
	// 条件搜索
	var $ = layui.$, active = {
		reload : function() {
			debugger;
			var contOrdeNumb = $('#contOrdeNumb');
			var zzoem = $('#zzoem');
			var endDate = $("#endDate");
			var startDate = $("#startDate");
			// 执行重载
			table.reload('orderTableId', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					contOrdeNumb : contOrdeNumb.val(),
					endDate : endDate.val(),
					startDate : startDate.val()
				}
			});
		}
	};
	// 监听单元格事件
	table.on('tool(demo)', function(obj) {
		var data = obj.data;
		if (obj.event === 'setSign') {
			var url = "/getCheckQualSuppHtml?suppId=" + data.suppId;
			location = url;
		}
	});

	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});

	// 取消
	$("#cancel").click(function() {
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	});

	// 确定
	$("#confirm").click(function() {
		var checkStatus = table.checkStatus('orderTableId');
		checkedData = checkStatus.data;
		var index = parent.layer.getFrameIndex(window.name);
		parent.orders(checkedData);
		parent.layer.close(index);
	});
	// 选中复选框
	function addCheckbox(table, data) {
		var index = parent.layer.getFrameIndex(window.name);
		var data2 = parent.data;
		var tableData = data2.tableData;
		$.each(data, function(mateindex, mateitem) {
			$.each(tableData, function(tableDataIndex, tableDataItem) {
				if (tableDataItem.orderId == mateitem.contOrdeNumb) {
					data[mateindex]["LAY_CHECKED"] = 'true';
				}
			});
		});
		initOrderTable();
	}

});

function initOrderTable() {
	var mateTable = table.render({
		elem : "#orderTable",
		url : "/queryAllAllotOrder?sapId=" + sapId + "&zzoem=" + zzoem,
		page : true,
		width : '100%',
		height : 'auto',
		limit : 10,
		cols : [ [
				{
					checkbox : true
				},
				{
					field : "subeDate",
					title : "订单日期",
					align : 'center',
					width : 117,
					templet : function(d) {
						var date = new Date(d.subeDate);
						var year = date.getFullYear();
						var month = date.getMonth() + 1;
						var day = date.getDate();
						return year + "-" + (month < 10 ? "0" + month : month)
								+ "-" + (day < 10 ? "0" + day : day);
					}
				}, {
					field : "contOrdeNumb",
					title : "调拨单号",
					align : 'center',
					width : 84
				}, {
					field : "zzoem",
					title : "OEM供应商",
					align : 'center',
					width : 122
				}, {
					field : "suppNumb",
					title : "供应商",
					align : 'center',
					width : 238
				}, {
					field : "suppName",
					title : "供应商编码",
					align : 'center',
					width : 115
				} ] ],
		id : 'orderTableId'
	});
}

function getCheckedData() {
	var checkStatus = table.checkStatus('orderTableId');
	checkedData = checkStatus.data;
	return checkedData;
}
