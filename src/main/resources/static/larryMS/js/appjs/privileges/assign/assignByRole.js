var currentRow = "";
var table;
layui.use('table', function() {
	table = layui.table;
	// 执行一个 table 实例
	var roleTable = table.render({
		elem : '#roleTable',
		url : '/sys/assign/roleList/',
		page : true,
		height : 465,
		cols : [ [ {
			field : 'roleName',
			title : '角色名称'
		}, {
			field : 'remark',
			title : '备注'
		} ] ],
		id : 'roleTable',
		done : function(res, curr, count) {
			var data = res.data;
			currentRow = "";
			if (data.length > 0)
				currentRow = data[0].roleId;
			inintAssignTable(table, currentRow, "");
			$('.layui-table-body tr').each(function() {
				var dataindex = $(this).attr('data-index');
				var idx = 0;
				for ( var item in data) {
					if (dataindex == idx) {
						$(this).click(function() {
							currentRow = data[item].roleId;
							inintAssignTable(table, currentRow, "");
						});
						break;
					}
					idx++;
				}
			});
		}
	});
});

function inintAssignTable(table, roleId, orgName) {
	table.render({
		elem : '#assignTable',
		url : '/sys/assign/assignListByRoleId?roleId=' + roleId + "&roleName="
				+ orgName,
		page : true,
		cols : [ [ {
			checkbox : true
		}, {
			field : 'sname',
			title : '组织机构'
		}, {
			field : 'scode',
			title : '组织机构编码'
		} ] ],
		id : 'assignTableId'
	});
}

function reLoad() {
	var orgName = $("#orgName").val();
	inintAssignTable(table, currentRow, orgName);
}

// 点击弹出添加框
$('#addBtn').on('click', function() {
	layer.open({
		type : 2,
		title : '组织机构',
		shadeClose : true,
		shade : false,
		maxmin : true, // 开启最大化最小化按钮
		area : [ '400px', '500px' ],
		content : '/sys/assign/orgSelect?roleId=' + currentRow,
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			debugger;
			// 调用content层的js方法
			var data = $(layero).find("iframe")[0].contentWindow.update();
			if (data != "") {
				layer.close(index);
				reLoad();
			}
		},
		btn2 : function(index, layero) {
			layer.close(index);
		}
	});
});

// 搜索角色
$("#roleName").keydown(function(e) {
	if (e.keyCode == 13) {
		var roleName = $('#roleName').val();
		table.reload('roleTable', {
			where : {
				roleName : roleName
			}
		});
	}
});

// 搜索组织机构
$("#orgName").keydown(function(e) {
	if (e.keyCode == 13) {
		var orgName = $('#orgName').val();
		var roleId = currentRow;
		inintAssignTable(table, roleId, orgName);
	}
});

// 删除选中的组织机构
$('#delBtn').on('click', function() {
	var checkRows = table.checkStatus('assignTableId');
	if (checkRows.data.length > 0) {
		var orgIds = new Array();
		for ( var int = 0; int < checkRows.data.length; int++) {
			orgIds.push(checkRows.data[int].menuId);
		}
		var orgIds = JSON.stringify(orgIds);
		var url1 = '/sys/assign/removeByOrgIds';
		var url = url1 + '?orgIds=' + orgIds + '&roleId=' + currentRow;
		$.ajax({
			type : 'POST',
			url : url,
			dataType : 'json',
			success : function(data) {
				var orgName = $("#orgName").val();
				inintAssignTable(table, currentRow, orgName);
			},
			error : function(xhr) {
				layer.msg('error');
			}
		});
	} else {
		layer.msg('请选择要删除的组织机构！');
	}

});
