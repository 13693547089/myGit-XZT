var prefix = "/sys/assign";
var orgIds = "";
$(function() {
	getOrgTreeData();
});
function load(data) {
	$('#orgTreeTable').jstree({
		"plugins" : [ "wholerow", "checkbox" ],
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

$('#subBtn').click(function() {
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
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			roleName : {
				required : true
			}
		},
		messages : {
			roleName : {
				required : icon + "请输入角色名"
			}
		}
	});
}

function update() {
	$('#orgIds').val(orgIds);
	var role = $('#signupForm').serialize();
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
			var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
			if (r.code == 0) {
				parent.layer.msg(r.msg);
				parent.reLoad();
				parent.layer.close(index);
			} else {
				parent.layer.close(index);
				parent.layer.msg(r.msg);
			}

		}
	});
}
