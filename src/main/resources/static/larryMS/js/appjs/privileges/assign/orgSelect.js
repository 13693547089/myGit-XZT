var prefix = "/sys/assign";
var orgIds = "";
var firstLoaded;
$(function() {
	getOrgTreeData();
});
function load(data) {
	$('#orgTreeTable').jstree({
		"plugins" : [ "wholerow", "checkbox" ],
		"checkbox" : {
			"keep_selected_style" : false,// 是否默认选中
			"three_state" : true,// 父子级别级联选择
			"tie_selection" : true
		},
		'core' : {
			'data' : data
		}
	});
	$('#orgTreeTable').jstree('open_all');
}
$.validator.setDefaults({
	submitHandler : function() {
		getAllSelectNodes();
		update();
	}
});

var dataAdd = {};
var dataMinus = {};
var length = 0;
var treeId = '';
$('#orgTreeTable').on(
		"changed.jstree",
		function(e, data) {
			var $id = '#' + data.selected[length] + '_anchor';
			treeId = data.selected[length];
			if (data.selected[length] == undefined
					|| data.selected[length] == null
					|| data.selected[length] == '') {
				length = data.selected.length;
				if (data.node != undefined) {
					var $a = '#' + data.node.id + '_anchor'
					var returnId1 = data.node.id + '_' + digui($($a));
					dataMinus[returnId1] = '';
				}
			} else {
				length = data.selected.length;
				var returnId = treeId + '_' + digui($($id));
				dataAdd[returnId] = '';
			}
		});

function digui(data) {
	if (data.parent().parent().prev().text() == '') {
		return '';
	} else {
		var aid = data.parent().parent().prev().eq(0).attr('id');
		var bid = aid.split('_');
		return bid[0] + '_' + digui(data.parent().parent());
	}
}
function outRepeat(a) {
	var hash = [], arr = [];
	for ( var i = 0; i < a.length; i++) {
		hash[a[i]] != null;
		if (!hash[a[i]]) {
			arr.push(a[i]);
			hash[a[i]] = true;
		}
	}
	return arr;
}

function getData() {
	debugger;

	var index;
	for ( var i in dataMinus) {
		index = i;
		delete dataAdd[index];
	}
	var addString = '';
	for ( var k in dataAdd) {
		addString = addString + k;
	}
	var addTable = addString.split('_');
	console.log(addTable);
	var ab = outRepeat(addTable);
	console.log(ab)

	getAllSelectNodes();
	update();

}

$('#subBtn').click(function() {
	var index;
	for ( var i in dataMinus) {
		index = i;
		delete dataAdd[index];
	}
	var addString = '';
	for ( var k in dataAdd) {
		addString = addString + k;
	}
	var addTable = addString.split('_');
	console.log(addTable);
	var ab = outRepeat(addTable);
	console.log(ab)

	getAllSelectNodes();
	update();
});
function getAllSelectNodes() {
	var ref = $('#orgTreeTable').jstree(true);// 获得整个树
	orgIds = ref.get_selected(); // 获得所有选中节点，返回值为数组
}
function getOrgTreeData() {
	var roleId = $('#roleId').val();
	$.ajax({
		type : "GET",
		url : prefix + "/select?roleId=" + roleId,
		success : function(data) {
			load(data);
		}
	});
}
function update() {
	debugger;
	var index;
	for ( var i in dataMinus) {
		index = i;
		delete dataAdd[index];
	}
	var addString = '';
	for ( var k in dataAdd) {
		addString = addString + k;
	}
	var addTable = addString.split('_');
	console.log(addTable);
	var ab = outRepeat(addTable);
	console.log(ab)

	getAllSelectNodes();
	$('#orgIds').val(orgIds);
	var role = $('#signupForm').serialize();
	var flag = "";
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + "/updateByRoleId",
		data : role,// 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(r) {
			flag = "success";
		}
	});
	return flag;
}
