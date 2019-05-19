var prefix = "/sys/assign";
var orgId = 0;
var table;
layui.use([ 'tree', 'table' ], function() {
	var $ = layui.$;
	table = layui.table;
	inintTable();
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + "/orgList",
		async : false,
		error : function(request) {
			layer.alert("Connection error");
		},
		success : function(data) {
			layui.tree({
				elem : '#orgTree', // 传入元素选择器
				skin : '',
				click : function(node) {
					// 单击事件
					orgId = node.id;
					inintTable();
				},
				nodes : data,
			});
		}
	});

	// 搜索组织机构
	$("#orgName").keydown(function(e) {
		if (e.keyCode == 13) {
			$(".layui-tree li").remove();
			var orgName = $('#orgName').val();
			$.ajax({
				cache : true,
				type : "POST",
				url : prefix + "/orgList",
				data : {
					orgName : orgName
				},
				async : false,
				error : function(request) {
					layer.alert("Connection error");
				},
				success : function(data) {
					layui.tree({
						elem : '#orgTree', // 传入元素选择器
						skin : '',
						click : function(node) {
							// 单击事件
							orgId = node.id;
							inintTable();
						},
						nodes : data,
					});
					orgId = "";
					reload();
				}

			});
		}
	});

	// 点击弹出添加框
	$('#addBtn').on('click', function() {
		if (orgId === '') {
			layer.alert("请先选中为哪个组织机构分配角色！");
		} else {
			layer.open({
				type : 2,
				title : '角色',
				shadeClose : true,
				shade : false,
				maxmin : true, // 开启最大化最小化按钮
				area : [ '400px', '500px' ],
				content : '/sys/assign/roleSelect?orgId=' + orgId
			});
		}
	});
	// 点击删除
	$("#delBtn").on('click', function() {
		var checkStatus = table.checkStatus('assignTable');
		var data = checkStatus.data;
		if (data.length > 0) {
			var roleId = new Array();
			for ( var int = 0; int < data.length; int++) {
				roleId.push(data[int].roleId);
			}
			var roleIds = JSON.stringify(roleId);
			var url1 = '/sys/assign/removeByRoleIds';
			var url = url1 + '?roleIds=' + roleIds + '&orgId=' + orgId;
			$.ajax({
				type : 'POST',
				url : url,
				dataType : 'json',
				success : function(data) {
					layer.msg(data.msg);
					inintTable();
				},
				error : function(xhr) {
					layer.msg('error');
				}
			});
		} else {
			layer.msg('请选择要删除的角色！');
		}
	});
});

function inintTable() {
	var url = prefix + "/assignListByOrgId?orgId=" + orgId;
	table.render({
		elem : '#assignTable',
		height : 315,
		url : url,
		page : false,
		cols : [ [ {
			checkbox : true,
			fixed : 'center'
		}, {
			field : 'roleId',
			title : '角色ID',
			width : 90,
			fixed : 'left'
		}, {
			field : 'roleName',
			title : '角色名称',
			width : 300
		} ] ]
	});
}

function reload() {
	inintTable();
}
