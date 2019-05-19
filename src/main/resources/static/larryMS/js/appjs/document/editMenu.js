var menuIds;
var tempNo;
$(function() {
	tempNo=$('#tempNo').val();
	getMenuTreeData();
});
function loadMenuTree(menuTree) {
	$('#menuTree').jstree({
		"plugins" : [ "wholerow", "checkbox" ],
		'core' : {
			'data' : menuTree
		}
	});
	$('#menuTree').jstree('open_all');
}
function getMenuTreeData() {
	var roleId = $('#roleId').val();
	$.ajax({
		type : "GET",
		url : "/template/getMenuTree",
		data:{tempNo:tempNo},
		success : function(data) {
			loadMenuTree(data);
		}
	});
}
$('#menuSubmitBtn').click(function(){
	var ref = $('#menuTree').jstree(true);// 获得整个树
	menuIds = ref.get_selected(); // 获得所有选中节点，返回值为数组
	$.ajax({
		type:'POST',
		url:'/template/getCheckedMenus',
		data:{jsonStr:JSON.stringify(menuIds)},
		success:function(msg){
			window.parent.initMenuTable(msg);
			var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
			parent.layer.close(index);
		},
		error:function(){
			parent.layer.msg('操作失败！');
		}
	});
	
});
function update() {
	getAllSelectNodes();
	$('#menuIds').val(menuIds);
	var role = $('#signupForm').serialize();
	$.ajax({
		cache : true,
		type : "POST",
		url : "/sys/role/update",
		data : role,// 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(r) {
			if (r.code == 0) {
				parent.layer.msg(r.msg);
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.msg(r.msg);
			}

		}
	});
}
