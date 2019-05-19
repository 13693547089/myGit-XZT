var selectedMateData = [];
var selected = [];
layui.use([ 'form', 'table' ], function() {
	var table = layui.table;
	var $ = layui.$;
	var oldSelected = $("#selected").val();
	var type = $("#type").val();

	$.ajax({
		url : '/quote/toChooseData',
		data : {
			type : type
		},
		async : false,
		method : 'get',
		error : function() {
			layer.msg("Connection error!");
		},
		success : function(res) {
			selectedMateData = res.data;
			// 处理获取到的数据，将已经选择的标识选中
			if (oldSelected != "") {
				for ( var k = 0; k < selectedMateData.length; k++) {
					var elem = selectedMateData[k];
					if (oldSelected.indexOf(elem.id) > -1)
						elem.LAY_CHECKED = true;
				}
			}
		}
	});
	loadToSelectedTable(table, selectedMateData);

	// 处理获取到的数据
	window.getData = function() {
		var result = [];
		for ( var i = 0; i < selectedMateData.length; i++) {
			var elem = selectedMateData[i];
			var checked = elem.LAY_CHECKED;
			if (checked) {
				var arr = {
					basicUnit : elem.basicUnit,
					chName : elem.chName,
					enName : elem.enName,
					id : elem.id,
					mateCode : elem.mateCode,
					mateId : elem.mateId,
					mateName : elem.mateName,
					mateType : elem.mateType,
					quoteTempId : elem.quoteTempId,
					quoteUnit : elem.quoteUnit,
					tempCode : elem.tempCode,
					tempName : elem.tempName
				}
				result.push(arr);
			}
		}
		return result;
	};
});

function loadToSelectedTable(table, data) {
	table.render({
		elem : '#toChooseTable',
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
