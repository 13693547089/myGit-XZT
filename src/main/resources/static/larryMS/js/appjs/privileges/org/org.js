var prefix = "/sys/org"
$(document).ready(function() {
	load();
});
var load = function() {
	$('#orgTreeTable')
			.bootstrapTreeTable(
					{
						id : 'menuId',
						parentColumn : 'parentId',
						type : "GET", // 请求数据的ajax类型
						url : prefix + '/list', // 请求数据的ajax的url
						ajaxParams : {}, // 请求数据的ajax的data属性
						expandColumn : '1',// 在哪一列上面显示展开按钮
						striped : true, // 是否各行渐变色
						bordered : true, // 是否显示边框
						expandAll : false, // 是否全部展开
						// toolbar : '#exampleToolbar',
						columns : [
								{
									title : '编号',
									field : 'scode',
									align : 'center',
									width : '80px'
								},
								{
									title : '名称',
									field : 'sname'
								},
								{
									title : '类型',
									field : 'stype',
									formatter : function(item, index) {
										if (item.stype === 'ogn') {
											return '<span>机构</span>';
										}
										if (item.stype === 'dept') {
											return '<span>部门</span>';
										}
										if (item.stype === 'pos') {
											return '<span>岗位</span>';
										}
										if (item.stype === 'psn') {
											return '<span>人员</span>';
										}
									}
								},
								{
									title : '操作',
									field : 'menuId',
									formatter : function(item, index) {
										var e = '<a class="btn btn-primary btn-xs" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ item.menuId + '\')">编辑</a> ';
										var p = '<a class="btn btn-primary btn-xs" href="#" mce_href="#" title="添加下级" onclick="add(\''
												+ item.menuId
												+ '\')">添加下级</a> ';
										if (item.stype == 'psn')
											p = '<a class="btn btn-primary disabled btn-xs" href="#" mce_href="#" title="添加下级">添加下级</a> ';
										var d = '<a class="btn btn-warning btn-xs" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ item.menuId + '\')">删除</a> ';
										return e + p + d;
									}
								} ]
					});
}
function reLoad() {
	load();
}
function add(pId) {
	debugger;
	layer.open({
		type : 2,
		title : '添加组织机构',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add/' + pId // iframe的url
	});
}
function remove(id) {
	if (id == "8") {
		// 系统管理员不允许删除
		layer.msg("系统管理员不允许删除");
	} else {
		layer.confirm('确定要删除选中的记录？', {
			btn : [ '确定', '取消' ]
		}, function() {
			$.ajax({
				url : prefix + "/remove",
				type : "post",
				data : {
					'id' : id
				},
				success : function(data) {
					if (data.code == 0) {
						layer.msg("删除成功");
						reLoad();
					} else {
						layer.msg(data.msg);
					}
				}
			});
		})
	}
}
function edit(id) {
	layer.open({
		type : 2,
		title : '组织机构修改',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}
function batchRemove() {
	// var rows = $('#exampleTable').bootstrapTable('getSelections');

}