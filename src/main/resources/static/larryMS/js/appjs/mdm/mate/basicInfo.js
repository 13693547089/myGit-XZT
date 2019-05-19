var basicData = [];
layui.use([ 'form', 'table' ], function() {
	var $ = layui.$, form = layui.form, table = layui.table;
	var mateId = $("#mateId").val();

	$.ajax({
		url : '/mate/basicData',
		method : 'get',
		data : {
			mateId : mateId
		},
		async : false,
		error : function(res) {

		},
		success : function(data) {
			basicData = data;
		}
	});

	initTable(table);

	$("#addBtn").on('click', function() {
		var arr = {
			id : '',
			mateId : '',
			basicName : '',
			standard : '',
			unit : ''
		}
		basicData.push(arr);
		initTable(table);
	});

	// 监听删除操作
	table.on('tool(basicInfoFilter)', function(obj) {
		obj.del();
	});
});

function initTable(table) {
	table.render({
		elem : '#basicInfo',
		data : basicData,
		cols : [ [ {
			type : 'numbers',
			fixed : 'center'
		}, {
			field : 'basicName',
			title : '名称',
			edit : 'text'
		}, {
			field : 'standard',
			title : '规格',
			edit : 'text'
		}, {
			field : 'unit',
			title : '单位',
			edit : 'text'
		}, {
			fixed : 'right',
			title : '操作',
			width : 80,
			align : 'center',
			toolbar : '#operateBar'
		} ] ]
	});
}

function getBasicData() {
	return basicData;
}