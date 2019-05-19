var table;
var tableData;
var tablelns;
var dicData = [];
layui.use('table', function() {
	table = layui.table;
	var $ = layui.$;

	debugger;

	id = $("#id").val();
	if (id == null || id == '') {
		initTable(dicData);
	} else {
		$.ajax({
			type : "GET",
			url : "/dicInfo/dicDetails_1",
			data : {
				cateId : id
			},
			success : function(res) {
				dicData = res;
				initTable(dicData);
			}
		});
	}

	// 监听table的操作
	table.on('tool(detailTableEvent)', function(obj) {
		var currentData = obj.data;
		if (obj.event === 'del') {
			layer.confirm('确定删除吗？', function(index) {
				// dicData == tablelns.config.data;
				var currId = currentData.id;
				for (var i = dicData.length - 1; i >= 0; i--) {
					if (dicData[i].id == currId) {
						// 删除当前行
						dicData.splice(i, 1);
					}
				}
				initTable(dicData);
				layer.close(index);
			});
		}
	});

	// 添加数据字典
	$("#addDetail").on('click', function() {
		// dicData == tablelns.config.data;
		var arr = {
			id : guid(),
			dicName : '',
			dicCode : '',
			remark : ''
		}
		dicData.push(arr);
		initTable(dicData);
	});

	// 保存
	$("#saveDic").click(function() {
		debugger;
		if (dicData.length > 0) {
			$("#jsonStr").val(JSON.stringify(dicData));
			var formData = $("#dicForm").serialize();
			$.ajax({
				type : "POST",
				url : "/saveDicInfo",
				async : true,
				data : formData,
				error : function(request) {
					parent.layer.msg("程序出错了！", {
						time : 1000
					});
				},
				success : function(r1) {
					if (r1) {
						parent.layer.msg("保存成功", {
							time : 1000
						});
						initTable(dicData);
					}
				}
			});
		} else {
			layer.msg("请将信息填写完整");
		}
	});

	// 返回
	$('#backBtn').on('click', function() {
		history.go(-1);
	});

});

// 初始化数据字典详情表格
function initTable(data) {
	tablelns = table.render({
		elem : '#detailTable',
		data : data,
		id : "dicTableId",
		page : true,
		limit : 10,
		width : '100%',
		minHeight : '20px',
		cols : [ [ {
			checkbox : true,
			width : 35,
			fixed : 'center'
		}, {
			field : 'dicName',
			title : '名称',
			width : 260,
			edit : 'text'
		}, {
			field : 'dicCode',
			title : '分类编码',
			width : 240,
			edit : 'text'
		}, {
			field : 'remark',
			title : '备注',
			width : 240,
			edit : 'text'
		}, {
			fixed : 'right',
			title : '操作',
			width : 180,
			align : 'center',
			toolbar : '#barDemo'
		} ] ]
	});
}

// 生成GUID
function guid() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}
