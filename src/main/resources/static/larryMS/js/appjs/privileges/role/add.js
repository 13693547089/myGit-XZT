//var menuTree;
var menuIds;
$(function() {
	getMenuTreeData();
	validateRule();
});
$.validator.setDefaults({
	submitHandler : function() {
		getAllSelectNodes();
		save();
	}
});

function getAllSelectNodes() {
	/*debugger
	var ref = $('#menuTree').jstree(true);// 获得整个树
	menuIds = ref.get_selected(); // 获得所有选中节点的，返回值为数组*/	
	$('#menuTree').jstree(true).get_all_checked = function(full) {
		//debugger;
	    var tmp=new Array;
	    for(var i in this._model.data){
	        if(this.is_undetermined(i)||this.is_checked(i)){tmp.push(full?this._model.data[i]:i);}
	    }
	    return tmp;
	};
	menuIds=[];
	var checkedNodes = $('#menuTree').jstree(true).get_all_checked();
	var length = checkedNodes.length;
	debugger;
	var j=0;
	for(var i =0;i<length;i++){
		if(checkedNodes[i] !="#"){
     		 menuIds[j] = checkedNodes[i];
     		 j++;
		}
   
	}
	console.info(checkedNodes);
	console.info(menuIds);
}
function getMenuTreeData() {
	$.ajax({
		type : "GET",
		url : "/sys/menu/tree",
		success : function(menuTree) {
			// menuTree = data;
			loadMenuTree(menuTree);
		}
	});
	console.info(12121)
}
function loadMenuTree(menuTree) {
	$('#menuTree').jstree({
		'core' : {
			'data' : menuTree
		},
		"checkbox" : {
			"three_state" : true
		// 不起作用，修改了源代码的默认true--false
		},
		"plugins" : [ "wholerow", "checkbox" ]
	});
	$('#menuTree').jstree().open_all();
}

function save() {
	$('#menuIds').val(menuIds);
	var role = $('#signupForm').serialize();
	$.ajax({
		cache : true,
		type : "POST",
		url : "/sys/role/save",
		data : role,// 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.msg(data.msg);
			}
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

$('#menuTree').jstree(true).get_all_checked = function(full) {
	debugger;
    var tmp=new Array;
    for(var i in this._model.data){
        if(this.is_undetermined(i)||this.is_checked(i)){tmp.push(full?this._model.data[i]:i);}
    }
    return tmp;
};