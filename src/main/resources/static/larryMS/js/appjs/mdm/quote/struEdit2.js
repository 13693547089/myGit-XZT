layui.use([ 'form', 'table' ], function() {
	var form = layui.form;
	var $ = layui.$;
	var table = layui.table;
	debugger;
	var status = $("#status").val();
	if (status == '0') {
		loadTable(table, []);
	} else {
		var struId = $("#id").val();
		loadTable(table, getDataByStruId(struId));
	}
	// 模板下拉选中监听事件
	form.on('select(tempSelected)', function(data) {
		if (data.value != "") {
			// 获取传入的tempList
			var tempList = $("#tempList").val();
			var temp = JSON.parse(tempList);
			for ( var i = 0; i < temp.length; i++) {
				if (temp[i].tempCode == data.value) {
					$("#tempName").val(temp[i].tempName);
					var tempId = temp[i].id;
					$("#quoteTempId").val(tempId);
					loadTable(table, getDataByTempId(tempId));
					return;
				}
			}
		} else {
			$("#tempName").val("");
			$("#quoteTempId").val("");
		}

		// console.log(data.elem); // 得到select原始DOM对象
		// console.log(data.value); // 得到被选中的值
		// console.log(data.othis); // 得到美化后的DOM对象
	});
	// 物料下拉选中监听事件
	form.on('select(mateSelected)', function(data) {
		debugger;
		if (data.value == "") {
			$("#mateType").val("");
			$("#basicUnit").val("");
			$("#chName").val("");
			$("#enName").val("");
			$("#mateId").val("");
		} else {
			// 获取传入的tempList
			var mateList = $("#mateList").val();
			var mate = JSON.parse(mateList);
			for ( var k = 0; k < mate.length; k++) {
				if (mate[k].mateCode == data.value) {
					$("#mateType").val(mate[k].mateType);
					$("#basicUnit").val(mate[k].basicUnit);
					$("#chName").val(mate[k].chinName);
					$("#enName").val(mate[k].skuEnglish);
					$("#mateId").val(mate[k].mateId);
					return;
				}
			}
		}
	});

	table.on('tool(struDetails)', function(obj) {
		debugger;
		var currentRow = obj.data;
		var tableData = $("#struTable").serialize();
		if (obj.event === 'add') {
			// 添加组件信息
			// 校验是否有段号
			if (currentRow.segmCode == '') {
				layer.msg("请输入段号！");
			} else {
				addAsse(currentRow);
			}
		} else if (obj.event === 'del') {
			// 删除组件信息
			layer.confirm('是否确定删除？', {
				btn : [ '确定', '取消' ]
			}, function(index) {
				removeAsse(currentRow.id);
				layer.close(index);
			})
		}
	})

	// 通过struId获取数据
	function getDataByStruId(struId) {
		var restultData;
		$.ajax({
			url : '/quote/getDataByStruId',
			data : {
				struId : struId
			},
			type : 'get',
			async : false,
			success : function(data) {
				restultData = data;
			}
		});
		return restultData;
	}
	// 通过tempId获取数据
	function getDataByTempId(tempId) {
		debugger;
		var restultData;
		$.ajax({
			url : '/quote/getDataByTempId',
			data : {
				tempId : tempId
			},
			type : 'get',
			async : false,
			success : function(data) {
				restultData = data;
			}
		});

		return restultData;
	}

	// 添加段
	$("#addSegBtn").on("click", function() {
		debugger;
		var data = table.cache.struTable;
		var arr = {
			id : guid(),
			segmCode : '',
			segmName : '',
			asseCode : '',
			asseName : '',
			mateCode : '',
			detailsNum : '',
			unit : '',
			standard : '',
			material : '',
			remark : '',
			parentId : '0'
		}
		data.push(arr);
		loadTable(table, data);
	})

	function addAsse(currentRow) {
		debugger;
		var data = table.cache.struTable;
		var tableData = [];
		var parentId = currentRow.parentId == '0' ? currentRow.id
				: currentRow.parentId;
		var arr = {
			id : guid(),
			segmCode : currentRow.segmCode,
			segmName : currentRow.segmName,
			asseCode : '',
			asseName : '',
			mateCode : '',
			detailsNum : '',
			unit : '',
			standard : '',
			material : '',
			remark : '',
			parentId : parentId
		}
		for ( var i = 0; i < data.length; i++) {
			var elem = data[i];
			tableData.push(elem);
			if (elem.segmName == currentRow.segmName
					&& elem.segmCode == currentRow.segmCode
					&& elem.asseCode == currentRow.asseCode)
				tableData.push(arr)
		}
		loadTable(table, tableData);
	}

	function removeAsse(id) {
		var data = table.cache.struTable;
		var tableData = [];
		for ( var i = 0; i < data.length; i++) {
			var elem = data[i];
			if (elem.id != id)
				tableData.push(elem)
		}
		loadTable(table, tableData);
	}

	// 保存数据
	$("#saveBtn").on("click", function() {
		var details = table.cache.struTable;
		$("#struDetailsList").val(JSON.stringify(details));
		var data = $("#signForm").serialize();
		$.ajax({
			url : '/quote/saveStruData',
			data : data,
			type : 'post',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					window.history.back(-1);
				}
			}
		})
	})
	// 返回上一页
	$("#backBtn").on("click", function() {
		window.history.back(-1);
	})
})

function loadTable(table, data) {
	table.render({
		elem : '#struTable',
		data : data,
		page : false,
		cols : [ [ {
			field : 'segmCode',
			title : '段号',
			width : 90,
			fixed : 'left',
			edit : 'text'
		}, {
			field : 'segmName',
			title : '段名',
			width : 100,
			edit : 'text'
		}, {
			field : 'asseCode',
			title : '组件编号',
			edit : 'text'
		}, {
			field : 'asseName',
			title : '组件名称',
			edit : 'text'
		}, {
			field : 'mateCode',
			title : '物料编号',
			edit : 'text'
		}, {
			field : 'detailsNum',
			title : '用量',
			edit : 'text'
		}, {
			field : 'unit',
			title : '单位',
			edit : 'text'
		}, {
			field : 'standard',
			title : '规格',
			edit : 'text'
		}, {
			field : 'material',
			title : '材料',
			edit : 'text'
		}, {
			field : 'remark',
			title : '备注',
			edit : 'text'
		}, {
			fixed : 'right',
			title : '操作',
			width : 120,
			align : 'center',
			toolbar : '#operateBar'
		} ] ]
	});
}

// 生成GUID
function guid() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}
