$(function() {
	function inint(searchArgs) {
		$("#toChooseTable").bootstrapTable('destroy');
		$("#toChooseTable").bootstrapTable({
			onDblClickRow : function(row, $element, field) {
				inintSelectedTable([]);
			},
			url : '/quote/toChooseData',
			method : 'get',
			contentType : "application/x-www-form-urlencoded",
			queryParamsType : searchArgs,
			queryParams : function queryParams(params) {
				var param = {
					pageNumber : params.pageNumber,
					pageSize : params.pageSize
				};
				for ( var key in searchArgs) {
					param[key] = searchArgs[key]
				}
				return param;
			},
			undefinedText : "",
			locale : 'zh-CN',
			pageNumber : 1,
			pageSize : 10,
			pageList : [ 10, 20 ],
			sidePagination : "server",
			showRefresh : false,
			columns : [ {
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
			} ]
		})
	}

	function inintSelectedTable(data) {
		$("#selectedTable").bootstrapTable('destroy');
		$("#selectedTable").bootstrapTable({
			data : data,
			undefinedText : "",
			locale : 'zh-CN',
			pagination : false,
			columns : [ {
				field : 'mateCode',
				title : '物料编码'
			}, {
				field : 'mateName',
				title : '物料名称'
			}, {
				field : '模板编码',
				title : '备注'
			}, {
				field : 'tempName',
				title : '模板名称'
			} ]
		})
	}

	inint([]);
	inintSelectedTable([]);
});