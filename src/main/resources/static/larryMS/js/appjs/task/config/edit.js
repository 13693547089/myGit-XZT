var tableData;
var handleList;
layui.use([ "form", "layedit", "laydate", "table" ], function() {
	var form = layui.form, layer = layui.layer;
	var table = layui.table, $ = layui.$;
	var processId = $("#processId").val();
	handleList = $("#handleList").val();

	if (processId != "") {
		$.ajax({
			cache : true,
			type : "GET",
			url : "/processConfig/processDetails?processId=" + processId,
			async : false,
			error : function(request) {
				layer.alert("Connection error");
			},
			success : function(data) {
				tableData = data
				inintTable(table, tableData);
			}
		});
	} else {
		tableData = [];
		inintTable(table, tableData);
	}
	$("#backBtn").on("click", function() {
		history.back(1);
	});
	$("#saveBtn").on("click", function() {
		// 检查是否是启用状态，如果是启用则要校验节点信息不能为空，并且待执行者不能为空
		var isUsed = $("#isUsed").val();
		var msg = "";
		var backNode = $("#backNode").val();
		if (isUsed !== 0) {// 启用
			msg += checkInfo(backNode);
		}
		if (msg == "") {
			$("#detailsData").val(JSON.stringify(tableData));
			var formData = $("#signForm").serialize();
			$.ajax({
				cache : true,
				data : formData,
				type : "POST",
				url : "/processConfig/save",
				async : false,
				error : function(request) {
					layer.alert("Connection error");
				},
				success : function(res) {
					layer.msg(res.msg);
					if (res.code == 0) {
						window.history.back(-1);
					}
				}
			});
		} else {
			layer.msg(msg);
		}
	});
	// 添加配置节点
	$("#addBtn").on("click", function() {
		var id = Date.parse(new Date());
		var node = tableData.length + 1;
		var arr = {
			id : id,
			node : node,
			nodeType : "",
			nodeTypeDesc : "",
			nodeName : "",
			nodeCondition : "",
			execRule : "",
			transmitRule : "",
			noticeRule : "",
			processRule : "",
			backRule : "",
			executor : "",
			execName : "",
			execType : ""
		};
		tableData.push(arr);
		inintTable(table, tableData);
	});
	// 批量删除配置节点
	$("#delBtn").on("click", function() {
		var checkStatus = table.checkStatus("detailsTable");
		var checkedData = checkStatus.data;
		if (checkedData.length > 0) {
			var newData = [];
			for ( var int = 0; int < checkedData.length; int++) {
				tableData.remove(checkedData[int]);
				inintTable(table, tableData);
			}
		} else {
			layer.msg("请选择要删除的节点");
		}
	});

	// 监听单元格编辑
	table.on("edit(demoEvent)", function(obj) {
		var value = obj.value // 得到修改后的值
		, data = obj.data // 得到所在行所有键值
		, field = obj.field; // 得到字段
		layer.msg('[ID: ' + data.id + '] ' + field + ' 字段更改为：' + value);
	});

	// 监听单元格事件
	table.on('tool(demoEvent)', function(obj) {
		var currRow = obj.data;
		if (obj.event === 'setExecutor') {
			var executor = currRow.executor;
			var ids = currRow.executor;
			var names = currRow.execName;
			openExectors(executor, obj, ids, names);
		} else if (obj.event === 'setExecRule') {
			var node = currRow.node;
			var mainId = currRow.id;
			var nodeType = currRow.nodeType;
			if (nodeType != "" && nodeType != undefined) {
				openExecRule(node, mainId, obj, nodeType);
			} else {
				layer.msg("请先配置节点类型！");
			}
		} else if (obj.event == 'setBackRule') {
			var node = currRow.node;
			var mainId = currRow.id;
			openBackRule(node, mainId, obj);
		} else if (obj.event == "setNodeType") {
			var nodeType = currRow.nodeType;
			openNodeType(nodeType);
		}
	});
	// 打开节点类型
	function openNodeType(nodeType) {
		layer.open({
			type : 2,
			title : '节点类型',
			shadeClose : false,
			shade : false,
			maxmin : false,
			area : [ '600px', '560px' ],
			content : '/processConfig/nodeType?nodeType=' + nodeType
		});
	}
	// 打开回退规则
	function openBackRule(node, mainId, obj) {
		layer.open({
			type : 2,
			title : '回退规则',
			shadeClose : true,
			shade : false,
			maxmin : false, // 开启最大化最小化按钮
			area : [ '600px', '560px' ],
			content : '/rule/setBackRule?node=' + node + "&mainId=" + mainId,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				// 调用content层的js方法
				var data = $(layero).find("iframe")[0].contentWindow
						.saveBackRule();
				if (data == "y_success") {
					obj.update({
						backRule : 'Y'
					});
					layer.close(index);
				} else if (data == "n_success") {
					obj.update({
						backRule : ''
					});
					layer.close(index);
				}
			},
			btn2 : function(index, layero) {
				layer.close(index);
			}
		});
	}

	// 打开执行规则
	function openExecRule(node, mainId, obj, nodeType) {
		layer.open({
			type : 2,
			title : '执行规则',
			shadeClose : true,
			shade : false,
			maxmin : false, // 开启最大化最小化按钮
			area : [ '600px', '660px' ],
			content : '/rule/setExecRule?node=' + node + "&mainId=" + mainId
					+ "&nodeType=" + nodeType,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				// 调用content层的js方法
				var data = $(layero).find("iframe")[0].contentWindow
						.saveExecRule();
				if (data == "y_success") {
					obj.update({
						execRule : 'Y'
					});
					layer.close(index);
				} else if (data == "n_success") {
					obj.update({
						execRule : ''
					});
					layer.close(index);
				}
			},
			btn2 : function(index, layero) {
				layer.close(index);
			}
		});
	}
	// 打开选择执行者
	function openExectors(executor, obj, ids, names) {
		layer.open({
			type : 2,
			title : '执行者',
			shadeClose : true,
			shade : false,
			maxmin : true, // 开启最大化最小化按钮
			area : [ '400px', '500px' ],
			content : '/processConfig/executor?executor=' + executor,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				// 调用content层的js方法
				var data = $(layero).find("iframe")[0].contentWindow.getData();
				for ( var i = 0; i < data.length; i++) {
					var elem = data[i];
					var id = elem.menuId;
					var name = elem.sname;
					if (ids == null || ids == "" || ids == undefined) {
						ids = id + "";
					} else if (ids.indexOf(id) == -1) {
						ids += "," + id;
					}
					if (names == null || names == "" || names == undefined) {
						names = name;
					} else if (names.indexOf(name) == -1) {
						names += "," + name;
					}
				}
				obj.update({
					executor : ids,
					execName : names,
					execType : 'psn'
				});
				layer.close(index);
			},
			btn2 : function(index, layero) {
				layer.close(index);
			}
		});
	}

	// 监听性别操作
	form.on('switch(handWayDemo)', function(obj) {
		var node = this.value;
		// 改变的这一行
		if (obj.elem.checked) {
			tableData[node - 1].handWay = 'AND';
			tableData[node - 1].handWayDesc = '并处理';
		} else {
			tableData[node - 1].handWay = 'OR';
			tableData[node - 1].handWayDesc = '或处理';
		}
	});

	// 监听锁定操作
	form.on('switch(isBackDemo)', function(obj) {
		var node = this.value;
		// 改变的这一行
		if (obj.elem.checked) {
			tableData[node - 1].isBack = '1';
		} else {
			tableData[node - 1].isBack = '0';
		}
	});
});

function inintTable(table, data) {
	table.render({
		elem : '#detailsTable',
		data : data,
		cols : [ [ {
			checkbox : true
		}, {
			field : 'node',
			title : '序号',
			width : 60
		}, {
			field : 'nodeName',
			title : '节点名称',
			edit : 'text',
			width : 120
		}, {
			field : "nodeType",
			title : "节点类型",
			edit : "text"
		}, {
			field : "nodeCondition",
			title : "条件",
			event : "setNodeCondition"
		}, {
			field : "handWay",
			title : "处理方式",
			templet : "#handleWay"
		}, {
			field : "execRule",
			title : "执行规则",
			event : "setExecRule"
		}, {
			field : "transmitRule",
			title : "转发规则",
			event : "setTransmitRule"
		}, {
			field : "noticeRule",
			title : "通知规则",
			event : "setNoticeRule"
		}, {
			field : "processRule",
			title : "流转规则",
			event : "setProcessRule"
		}, {
			field : "backRule",
			title : "回退规则",
			event : "setBackRule"
		}, {
			field : "execName",
			title : "待选执行者",
			event : "setExecutor"
		}, {
			field : "execType",
			title : "执行者类型"
		} ] ],
		id : "detailsTable"
	});
}

Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].id == val.id) {
			this.splice(k, 1);
			return;
		}
	}
};

function checkInfo(backNode) {
	var msg = "";
	if (tableData.length == 0) {
		msg = "启用状态下必须有流程节点信息，请补全流程节点信息！"
	} else if (tableData.length >= 2) {
		var flag = false;
		for ( var i = 0; i < tableData.length; i++) {
			var elem = tableData[i];
			if (elem.node == backNode) {
				flag = true;
			}
			if (elem.node != 1) {
				if (elem.nodeName == "" || elem.nodeName == null)
					msg += "流程节点名称必填！";
				if (elem.nodeType == "" || elem.nodeType == null)
					msg += "流程节点类型必须指定！";
				// if (elem.executor == "" || elem.executor == null)
				// msg += "流程节点必须指定待选执行者！";

			}
		}

		if (backNode != "" && backNode != undefined && backNode != null
				&& !flag) {
			msg += "无此回退节点，请检查！"
		}
	} else {
		msg = "流程至少有两个节点！";
	}
	return msg;
};