layui.use([ 'form', 'table' ], function() {
	var form = layui.form;
	var $ = layui.$;
	var table = layui.table;

	$("#saveBtn").on('click', function() {
		debugger;
		var sdata1 = $("#id").val();
		if (sdata1 == undefined || sdata1 == "") {
			sdata1 = guid();
			$("#id").val(sdata1);
		}
		var remark = $("#remark").val();
		var form = $("#signForm").serialize();
		$.ajax({
			url : "/process/saveTestInfo",
			type : "post",
			// timeout:"", // 请求超时时间
			async : false,
			data : form,
			error : function() {
				layer.msg("Connection error!");
			},
			success : function(res) {
				if (res.code == 0) {
					debugger;
					taskProcess(sdata1, "testAudit", remark, "save");
				}
			}
		});
	});
	// 回退
	$("#backBtn").on('click', function() {
		var sdata1 = $("#id").val();
		backProcess(sdata1);
	});

	$("#submitBtn").on('click', function() {
		debugger;
		var sdata1 = $("#id").val();
		if (sdata1 == undefined || sdata1 == "") {
			sdata1 = guid();
			$("#id").val(sdata1);
		}
		submitInfo();
		// taskProcess(sdata1, processCode, taskName, status);
		var remark = $("#remark").val();
		var flag = taskProcess(sdata1, "testAudit", remark, "process");
	});
	// 提交
	function submitInfo() {
		var flag = "";
		var form = $("#signForm").serialize();
		$.ajax({
			url : "/process/saveTestInfo",
			type : "post",
			// timeout:"", // 请求超时时间
			async : false,
			data : form,
			error : function() {
				flag = "error";
			},
			success : function(res) {
				flag = res.code;
			}
		});
		return flag;
	}

	table.render({
		elem : '#undoTable',
		url : '/task/undoList',
		page : true,
		cols : [ [ {
			field : 'taskName',
			title : '任务名称',
			event : 'doTask'
		}, {
			field : 'executorName',
			title : '执行者'
		} ] ]
	});
	table.render({
		elem : '#doneTable',
		url : '/task/doneList',
		page : true,
		cols : [ [ {
			field : 'taskName',
			title : '任务名称'
		} ] ]
	});

	window.returnFunction = function() {
		window.history.go(-1);
	}
	// 监听代办列表的单元格事件
	table.on('tool(undoFilter)', function(obj) {
		debugger;
		var url = obj.data.actionUrl;
		if (url != null) {
			window.location.href = url + "?sdata1=" + obj.data.sdata1
					+ "&taskName=" + obj.data.taskName + "&processCode="
					+ obj.data.processCode;
		} else {
			layer.msg("数据错误！");
		}
	});
});

// 生成GUID
function guid() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}
// 生成GUID
function guid1() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}