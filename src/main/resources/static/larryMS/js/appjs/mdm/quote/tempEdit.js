var treeData;

$(function() {
	getTreeData();
	load();
});

function getTreeData() {
	var tempId = $("#tempId").val();
	$.ajax({
		url : '/quote/test',
		type : 'GET',
		async : false,
		data : {
			tempId : tempId
		},
		success : function(data) {
			treeData = data;
		}
	});
}

// 子页面返回值
function reload(val) {
	debugger;
	var flag = true;
	for ( var i = 0; i < treeData.length; i++) {
		var elem = treeData[i];
		if (elem.segmCode == val.segmCode && elem.asseCode == val.asseCode) {
			elem.material = val.material;
			elem.mateCode = val.mateCode;
			elem.asseName = val.asseName;
			elem.detailsNum = val.detailsNum;
			elem.remark = val.remark;
			elem.standard = val.standard;
			elem.unit = val.unit;
			flag = false;
			load();
			return;
		}
	}
	if (flag){
		treeData.push(val);
		load();
	}
}
function load() {
	$('#tempDetailsTable').bootstrapTreeTable({
		id : 'menuId',
		parentColumn : 'parentId',
		type : "GET", // 请求数据的ajax类型
		// url : '/quote/tempDetails?tempId=' + tempId, //
		// 请求数据的ajax的url
		// ajaxParams : {}, // 请求数据的ajax的data属性
		data : treeData,
		expandColumn : '1',// 在哪一列上面显示展开按钮
		striped : true, // 是否各行渐变色
		bordered : true, // 是否显示边框
		expandAll : true, // 是否全部展开
		// toolbar : '#exampleToolbar',
		columns : [
			{title : '段号', field : 'segmCode'},
			{title : '段名', field : 'segmName'},
			{title : '组件编号', field : 'asseCode'},
			{title : '组件名称', field : 'asseName'},
			{title : '物料编码', field : 'mateCode'},
			{title : '用量', field : 'detailsNum'},
			{title : '单位', field : 'unit'},
			{title : '规格', field : 'standard'},
			{title : '操作', field : 'id', width : '180px', align : 'center',
				formatter : function(item, index) {
					var p;
					if (item.parentId == '0') {
						p = '<a class="layui-btn layui-btn-xs blueInline" onclick="addSeg(\''
								+ item.menuId
								+ '\',\''
								+ item.segmCode
								+ '\',\''
								+ item.segmName
								+ '\')">添加</a>';
					} else {
						p = '<a class="layui-btn layui-btn-xs blueInline layui-disabled">添加</a>';
					}
					var e = '<a class="layui-btn layui-btn-xs blueInline" onclick="editSeg(\''
							+ item.menuId + '\')">编辑</a>';

					var d = '<a class="layui-btn layui-btn-xs blueInline" onclick="removeSeg(\''
							+ item.menuId
							+ '\',\''
							+ item.parentId + '\')">删除</a>';
					return p + e + d;
				}
			}]
	});
}

// 保存
function saveTemp() {
	var tempName = $("#tempName").val();
	var msg = "";
	if (tempName == undefined || tempName == '')
		msg += "请输入模板名称！";
	if (treeData.length == 0)
		msg += "请完善段信息！";
	if (msg == "") {
		$("#status").val("0");
		var saveResult = save();
		if (saveResult !== undefined) {
			layer.msg(saveResult.msg);
			if (saveResult.code == 0)
				tuoBack('.tempEdit','#serachInfo');
		}
	} else {
		layer.msg(msg);
	}
}
// 返回
function back() {
	tuoBack('.tempEdit','#serachInfo');
}

function save() {
	var tempId = $("#tempId").val();
	if (tempId == undefined || tempId == '') {
		tempId = guid();
		$('#tempId').val(tempId);
	}
	var saveRestult;
	$("#detailsList").val(JSON.stringify(treeData));
	var form = $("#signForm");
	var data = form.serialize();
	$.ajax({
		url : "/quote/save",
		type : "post",
		async : false,
		data : data,
		success : function(data) {
			saveRestult = data;
		}
	});
	return saveRestult;
}

// 添加新段
function addSeg(pid, segmCode, segmName) {
	var params = "";
	if (pid !== '0') {
		var temp;
		for ( var i = 0; i < treeData.length; i++) {
			var elem = treeData[i];
			var code = Number(elem.asseCode);
			if (elem.menuId == pid) {
				// 父节点
				temp = code + 1;
			} else if (elem.parentId == pid) {
				if (temp == code) {
					temp = code + 1;
				} else {
					return;
				}
			}
		}
		params = "?segmCode=" + segmCode + "&segmName=" + segmName
				+ "&asseCode=" + temp;
	}
	openDialog(params, pid);
}

// 打开模板新增弹出框
function openDialog(params, pid) {
	layer.open({
		type : 2,
		title : '增加段',
		maxmin : false,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '400px' ],
		content : '/quote/addSeg/' + pid + params
	});
}
// 编辑段
function editSeg(id) {
	for ( var i = 0; i < treeData.length; i++) {
		var elem = treeData[i];
		if (elem.menuId == id) {
			var segmCode = elem.segmCode == null ? '' : elem.segmCode;
			var segmName = elem.segmName == null ? '' : elem.segmName;
			var asseCode = elem.asseCode == null ? '' : elem.asseCode;
			var asseName = elem.asseName == null ? '' : elem.asseName;
			var mateCode = elem.mateCode == null ? '' : elem.mateCode;
			var detailsNum = elem.detailsNum == null ? '' : elem.detailsNum;
			var unit = elem.unit == null ? '' : elem.unit;
			var standard = elem.standard == null ? '' : elem.standard;
			var material = elem.material == null ? '' : elem.material;
			var remark = elem.remark == null ? '' : elem.remark;
			var url = '/quote/editSeg?menuId=' + id + "&segmCode=" + segmCode
					+ "&segmName=" + encodeURIComponent(segmName) + "&asseCode=" + asseCode
					+ "&asseName=" + encodeURIComponent(asseName) + "&mateCode=" + mateCode
					+ "&detailsNum=" + detailsNum + "&unit=" + unit
					+ "&standard=" + standard + "&material=" + material
					+ "&remark=" + remark;
			layer.open({
				type : 2,
				title : '编辑段',
				maxmin : true,
				shadeClose : false, // 点击遮罩关闭层
				area : [ '800px', '520px' ],
				content : url
			});
			return;
		}
	}
}
// 删除段
function removeSeg(id, parentId) {
	debugger;
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function(index) {
		remove(id);
		layer.close(index);
		// $.ajax({
		// url : "/quote/removeSeg",
		// type : "post",
		// data : {
		// 'id' : id,
		// 'parentId' : parentId
		// },
		// success : function(data) {
		// if (data.code == 0) {
		// layer.msg("删除成功");
		// remove(id);
		// } else {
		// layer.msg(data.msg);
		// }
		// }
		// });
	})
}

function remove(id) {
	// 判断是不是父节点，如果是则子节点全部删除
	var arr = [];
	for (var k = 0; k < treeData.length; k++) {
		var elem = treeData[k];
		if (elem.menuId !== id && elem.parentId != id)
			arr.push(elem);
	}
	treeData = arr;
	load();
}

// 生成GUID
function guid() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}
