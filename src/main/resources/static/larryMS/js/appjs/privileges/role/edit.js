var menuIds;
$(function() {
	getMenuTreeData();
	validateRule();
	
	
});
$.validator.setDefaults({
	submitHandler : function() {
		getAllSelectNodes();
		update();
	}
});
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
	$('#menuTree').jstree('open_all');
}
function getAllSelectNodes() {
	//debugger;
	//var ref = $('#menuTree').jstree(true);// 获得整个树
	//menuIds = ref.get_selected(); // 获得所有选中节点，返回值为数组
	/*var list = $('#menuTree').jstree().get_selected(true);
	$.each(list,function(index,item){
		console.info(item);
	})*/
	//debugger;
	$('#menuTree').jstree(true).get_all_checked = function(full) {
		//debugger;
	    var tmp=new Array;
	    for(var i in this._model.data){
	        if(this.is_undetermined(i)||this.is_checked(i)){tmp.push(full?this._model.data[i]:i);}
	    }
	    return tmp;
	};
	//var checkedNodes = $('#container').jstree(true).get_all_checked(true);--获取选中的子节点和关联的父节点的对象组成的数组
	//以下两个调用方法是获取选中的子节点和关联的父节点的id组成的数组
	//var checkedNodes = $('#container').jstree("get_all_checked"); 
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
	var roleId = $('#roleId').val();
	$.ajax({
		type : "GET",
		url : "/sys/menu/tree/" + roleId,
		success : function(data) {
			loadMenuTree(data);
		}
	});
}
function update() {
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
