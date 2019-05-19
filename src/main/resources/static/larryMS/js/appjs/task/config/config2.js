layui.use('table', function() {
	var table = layui.table;
	table.render({
		elem : '#configTable',
		url : '/processConfig/configList',
		page : true,
		cols : [ [ {
			checkbox : true
		}, {
			field : 'processName',
			title : '流程名称',
			width : 300
		}, {
			field : 'processCode',
			title : '流程编码',
			width : 120
		}, {
			field : 'isUsed',
			title : '是否启用',
			width : 100,
			templet : function(d) {
				if (d.isUsed == 0)
					return "<span>禁用</span>";
				return "<span>启用</span>";
			}
		}, {
			fixed : 'right',
			width : 300,
			align : 'center',
			toolbar : '#barDemo'
		} ] ],
		id : "idTest"
	})
	// 监听表格复选框选择
	table.on('checkbox(demo)', function(obj) {
		console.log(obj)
	});
	// 监听工具条
	table.on('tool(demo)', function(obj) {
		var data = obj.data;
		if (obj.event === 'detail') {
			layer.msg('ID：' + data.id + ' 的查看操作');
		} else if (obj.event === 'del') {
			layer.confirm('真的删除行么', function(index) {
				obj.del();
				layer.close(index);
			});
		} else if (obj.event === 'edit') {
			var processId = data.id;
			var url = "/processConfig/processInfo?type=1" + "&processId="
					+ processId;
			window.location.href = url;
		}
	});

	var $ = layui.$;
	var active = {
		newData : function() { // 新建流程
			showProcessInfo("", 0);
		},
		delData : function() { // 删除数据
			debugger;
			var checkStatus = table.checkStatus('idTest');
			var data = checkStatus.data;
			if (data.length > 0) {
				var ids = "";
				for ( var int = 0; int < data.length; int++) {
					ids += data[int].id + ","
				}
				$.ajax({
					cache : true,
					type : "POST",
					url : "/processConfig/remove?configIds=" + ids,
					async : false,
					error : function(request) {
						layer.alert("Connection error");
					},
					success : function(data) {
						debugger;
					}
				});
			} else {
				layer.msg('请选择要删除的数据！');
			}
		}
	};

	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
});
// 查看(2)编辑(1)新建(0)流程配置信息
function showProcessInfo(processId, type) {
	var url = "/processConfig/processInfo?type=" + type + "&processId="
			+ processId;
	window.location.href = url;
}