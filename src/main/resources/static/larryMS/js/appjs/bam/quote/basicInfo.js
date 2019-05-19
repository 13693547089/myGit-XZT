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
		}, {
			field : 'standard',
			title : '规格',
		}, {
			field : 'unit',
			title : '单位',
		} ] ]
	});
}
