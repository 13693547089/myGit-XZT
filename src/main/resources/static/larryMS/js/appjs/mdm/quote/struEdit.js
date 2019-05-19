var tableData = [];
var table;
layui.use([ 'form', 'table' ], function() {
	var form = layui.form;
	var $ = layui.$;
	table = layui.table;
	var status = $("#status").val();
	if (status != '0') {
		var struId = $("#id").val();
		getDataByStruId(struId);
	}

	loadTable(table);
	// 模板下拉选中监听事件
	form.on('select(tempSelected)', function(data) {
		if (data.value != "") {
			// 获取传入的tempList
			var tempList = $("#tempList").val();
			var temp = JSON.parse(tempList);
			for (var i = 0; i < temp.length; i++) {
				if (temp[i].tempCode == data.value) {
					$("#tempName").val(temp[i].tempName);
					var tempId = temp[i].id;
					$("#quoteTempId").val(tempId);
					getDataByTempId(tempId);
					break;
				}
			}
		} else {
			$("#tempName").val("");
			$("#quoteTempId").val("");
			tableData = [];
		}
		loadTable(table);
	});
	// 物料下拉选中监听事件
	form.on('select(mateSelected)', function(data) {
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
			for (var k = 0; k < mate.length; k++) {
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
		var currentRow = obj.data;
		var segmCode=currentRow.segmCode;
		var tableData = $("#struTable").serialize();
		if (obj.event === 'add') {
			var pid = currentRow.parentId;
			if (currentRow.parentId == "0")
				pid = currentRow.id;
			var segmCode = currentRow.segmCode;
			var segmName = currentRow.segmName;
			addSeg(pid, segmCode, segmName);
		} else if (obj.event === 'del') {
			// 删除组件信息
			layer.confirm('是否确定删除？', {
				btn : [ '确定', '取消' ]
			}, function(index) {
				removeAsse(currentRow.id);
				layer.close(index);
			})
		} else if (obj.event === "updateMate") {
			// 物料编码
			var content="/mate/choose_single";
			if(segmCode==60){
				content="/quote/chooseReceiveMsg"
			}
			layer.open({
				type : 2,
				title : '物料调整',
				maxmin : false,
				shadeClose : false, // 点击遮罩关闭层
				area : [ '800px', '520px' ],
				content : content,
				btn : [ '确定', '取消' ],
				yes : function(index, layero) {
					// 获取数据
					var rows = $(layero).find('iframe')[0].contentWindow.getData();
					var res = JSON.parse(rows);
					if (res.length == 0) {
						layer.alert("请选择物料！");
					} else {
						if(segmCode==60){
							obj.update({
								asseName : res[0].receUnit,
								mateCode : res[0].freightRange,
							});
						}else{
							obj.update({
								asseName : res[0].skuEnglish,
								mateCode : res[0].mateCode,
								unit : res[0].basicUnit
							});
						}
	
						layer.close(index);
					}
				}
			});
		}
	});

	// 监听单元格编辑事件
	table.on("edit(struDetails)", function(obj) {
		debugger;
		var field = obj.field;
		if (field == "asseName") {
			var row = obj.data;
			var mateCode = row.mateCode;
			var rowid = row.id;
			if (mateCode != null) {
				// 只有有物料号的情况下才不可更改，所以如果有物料号的话就把值恢复为物料名称
				var mateName = "";
				$.ajax({
					url : "/quote/find_mate_name_by_code",
					data : {
						mateCode : mateCode
					},
					type : "get",
					async : false,
					success : function(res) {
						mateName=res;
					}
				});
				for (var i = 0; i < tableData.length; i++) {
					var elem = tableData[i];
					if(elem.id == rowid){
						elem.asseName = mateName;
					}
				}
				loadTable(table);
				layer.msg("存在物料编码，不可编辑更改组件名称");
			}
		}
	});

	// 通过struId获取数据
	function getDataByStruId(struId) {
		$.ajax({
			url : '/quote/getDataByStruId',
			data : {
				struId : struId
			},
			type : 'get',
			async : false,
			success : function(data) {
				tableData = data;
			}
		});
	}
	// 通过tempId获取数据
	function getDataByTempId(tempId) {
		$.ajax({
			url : '/quote/getDataByTempId',
			data : {
				tempId : tempId
			},
			type : 'get',
			async : false,
			success : function(data) {
				tableData = data;
			}
		});
	}

	function removeAsse(id) {
		var temp = [];
		for (var i = 0; i < tableData.length; i++) {
			var elem = tableData[i];
			if (elem.id != id)
				temp.push(elem)
		}
		tableData = temp;
		loadTable(table);
	}

	// 保存数据
	$("#saveBtn").on("click", function() {
		debugger;
		var details = table.cache.struTable;
		// 校验
		var mateId = $("#mateId").val();
		var quoteTempId = $("#quoteTempId").val();
		var quoteUnit = $("#quoteUnit").val();
		var msg = "";
		if (mateId == "" || mateId == undefined)
			msg += "请选择物料！"
		if (quoteTempId == "" || quoteTempId == undefined)
			msg += "请选择模板编码！";
		if (quoteUnit == "" || quoteUnit == undefined)
			msg += "请输入报价单位";
		if (msg == "") {
			$("#struDetailsList").val(JSON.stringify(details));
			var data = $("#signForm").serialize();
			$.ajax({
				url : '/quote/saveStruData',
				data : data,
				type : 'post',
				success : function(r) {
					if (r.code == 0) {
						layer.msg(r.msg);
						tuoBack('.struEdit', '#serachInfo');
					}
				}
			})
		} else {
			layer.msg(msg);
		}
	})
	// 返回上一页
	$("#backBtn").on("click", function() {
		tuoBack('.struEdit', '#serachInfo');
	})
})

function loadTable(table) {
	table.render({
		elem : '#struTable',
		data : tableData,
		limit : 30,
		page : false,
		cols : [ [ {
			field : 'segmCode',
			title : '段号',
			width : 60,
			fixed : 'left'
		}, {
			field : 'segmName',
			title : '段名',
			width : 60
		}, {
			field : 'asseCode',
			title : '组件编号',
			width : 85
		}, {
			field : 'asseName',
			title : '组件名称',
			edit : 'text',
			width : 120
		}, {
			field : 'mateCode',
			title : '物料编号',
			width : 180,
			event : 'updateMate'
		}, {
			field : 'detailsNum',
			title : '用量',
			edit : 'text',
			width : 60
		}, {
			field : 'unit',
			title : '单位',
			edit : 'text',
			width : 60
		}, {
			field : 'standard',
			title : '规格',
			edit : 'text',
			width : 60
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

function addSeg(pid, segmCode, segmName) {
	var params = "";
	if (pid !== '0') {
		var temp;
		for (var i = 0; i < tableData.length; i++) {
			var elem = tableData[i];
			var code = Number(elem.asseCode);
			if (elem.id == pid) {
				// 父节点
				temp = code + 1;
			} else if (elem.parentId == pid) {
				if (temp == code)
					temp = code + 1;
			}
		}
		params = "?segmCode=" + segmCode + "&segmName="
				+ encodeURIComponent(segmName) + "&asseCode=" + temp;
	}
	openDialog(params, pid);
}

function reload(val) {
	// 处理返回来的数据
	debugger;
	val.id = guid();
	var temp = [];
	for (var i = 0; i < tableData.length; i++) {
		var elem = tableData[i];
		temp.push(elem);
		if (elem.id == val.parentId || elem.parentId == val.parentId) {
			var code = Number(elem.asseCode);
			if (val.asseCode - code == 1)
				temp.push(val);
		}
	}
	tableData = temp;
	loadTable(table);
}

// 打开模板新增弹出框
function openDialog(params, pid) {
	layer.open({
		type : 2,
		title : '增加段',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : '/quote/addSeg/' + pid + params
	});
}
