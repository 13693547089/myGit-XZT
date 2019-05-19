var treeData;
var tree;
$(function() {
	var executor = $("#executor").val();
	getOrgTreeData(executor);
});

function getOrgTreeData(executor) {
	var roleId = $('#roleId').val();
	$.ajax({
		type : "GET",
		url : "/processConfig/executorSelect?executor=" + executor,
		async : false,
		success : function(data) {
			treeData = data;
			load(data);
		}
	});
	$.ajax({
		url : '/sys/org/list',
		method : 'get',
		async : false,
		success : function(res) {
			tree = res;
		}
	});
}

function load(data) {
	debugger;
	$('#orgTreeTable1').jstree({
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
	$('#orgTreeTable1').jstree('open_all');
}

function getData() {
	debugger;
	var ref = $('#orgTreeTable1').jstree(true);// 获得整个树
	var selected = ref.get_selected();
	var result = [];
	if (selected.length > 0 && tree.length > 0) {
		for ( var i = 0; i < tree.length; i++) {
			var elem = tree[i];
			var id = "" + elem.menuId;
			if (selected.indexOf(id) > -1 && elem.stype == 'psn')
				result.push(elem);
		}
	}
	return result;
}
