layui.use([ 'table' ], function() {
	var $ = layui.$;
	var stompClient = null;
	establishSocket();
	/**
	 * 任务处理 save, process
	 */
	window.taskProcess = function(sdata1, processCode, taskName, status) {
		debugger;
		var processR = "";
		// 保存业务数据的时候生成一条任务数据
		if (status == "save") {
			// 校验是否有配置信息
			var flag = checkConfig(processCode);
			if (flag) {
				var r = initialProcess(sdata1, processCode, taskName);
				if (r == "0") {
					processR = "init_success";
				} else {
					processR = "init_error";
				}
			} else {
				layer.msg("编码为：" + processCode + "的流程无配置信息,请完善任务配置信息！");
				processR = "noConfig";
			}
		} else if (status == "process") {
			// 校验是否有配置信息
			var flag = checkConfig(processCode);
			if (flag) {
				// 判断是否打开选择执行者
				debugger;
				var result = isSelectExecutor(sdata1, processCode);
				var isProcess = result.isProcess;
				if (isProcess === "yes") {
					// 打开选择执行者(需要做回调函数)
					var isOpen = result.isOpen;
					if (isOpen == "Y") {
						// 打开选择执行者，流转
						openExecutor(sdata1, processCode, taskName);
						processR = "process_success";
					} else if (isOpen == "N") {
						$.ajax({
							url : '/task/isProcess',
							method : 'get',
							data : {
								sdata1 : sdata1,
								processCode : processCode,
								taskName : taskName,
								type : "backstage"
							},
							async : false,
							error : function() {
								processR = "process_error";
							},
							success : function(res) {
								if (res.code == 0) {
									returnFunction();
									sendToMessage(sdata1);
									processR = "process_success";
								} else {
									processR = "process_error";
								}
							}
						});
					} else if (isOpen == "over") {
						var doRes = doOverTask(sdata1);
						if (doRes == "0") {
							layer.msg("任务流转完成！");
							processR = "over_success";
						} else {
							layer.msg("任务流转失败！");
							processR = "over_error"
						}
					} else {
						processR = "process_error";
					}
				} else if (isProcess === "no") {
					var doRes = doCurrentTask(sdata1);
					if (doRes == "0") {
						layer.msg("请等待其他任务执行者执行完！");
						processR = "wait_success";
					} else {
						layer.msg("任务流转失败！");
						processR = "wait_error"
					}
				} else if (isProcess == "isLeader") {
					var r = doTaskToLeader(sdata1, processCode, taskName);
					if (r == "0") {
						processR = "process_success";
					} else {
						processR = "process_error"
					}
				} else {
					layer.msg("程序出错！");
					processR = "error";
				}
			} else {
				layer.msg("编码为：" + processCode + "的流程无配置信息,请完善任务配置信息！");
				processR = "noConfig";
			}
		}
		return processR;
	}

	// 校验是否有配置信息
	function checkConfig(processCode) {
		var flag = false;
		// 校验是否有该任务的配置信息
		$.ajax({
			url : '/task/checkConfig',
			method : 'get',
			data : {
				processCode : processCode
			},
			async : false,
			success : function(res) {
				flag = res;
			}
		});
		return flag;
	}

	// 结束任务
	function doOverTask(sdata1) {
		var result = "false";
		$.ajax({
			url : "/task/doOverTask",
			method : "get",
			data : {
				sdata1 : sdata1
			},
			async : false,
			error : function(res) {
				layer.msg("connection error!");
			},
			success : function(res) {
				result = res.code;
			}
		});
		return result;
	}
	// 处理当前节点的任务
	function doCurrentTask(sdata1) {
		var result = "false";
		$.ajax({
			url : "/task/doCurrentTask",
			method : "get",
			data : {
				sdata1 : sdata1
			},
			async : false,
			error : function(res) {
				layer.msg("connection error!");
			},
			success : function(res) {
				result = res.code;
			}
		});
		return result;
	}

	// 处理当前节点任务，并指定下一节点的执行者为直接上级
	function doTaskToLeader(sdata1, processCode, taskName) {
		var result = "false";
		$.ajax({
			url : "/task/doTaskToLeader",
			method : "get",
			data : {
				sdata1 : sdata1,
				processCode : processCode,
				taskName : taskName
			},
			async : false,
			error : function(res) {
				layer.msg("connection error!");
			},
			success : function(res) {
				result = res.code;
			}
		});
		return result;
	}

	// 打开选择审批人
	function openExecutor(sdata1, processCode, taskName) {
		// 选择下一节点的审批人
		layer.open({
			type : 2,
			title : '选择下一节点审批人',
			maxmin : false,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : '/task/getSelectExecutor' + '?processCode=' + processCode
					+ '&sdata1=' + sdata1 + "&type=start&taskName=" + taskName,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				// 调用content层的js方法
				var data = $(layero).find("iframe")[0].contentWindow.getData();
				if (data.length > 0) {
					// 流转审批
					$.ajax({
						cache : true,
						data : {
							sdata1 : sdata1,
							processCode : processCode,
							taskName : taskName,
							type : "process",
							executorStr : JSON.stringify(data)
						},
						url : '/task/isProcess',
						async : false,
						error : function(request) {
							layer.alert("Connection error");
						},
						success : function(res) {
							layer.msg(res.msg);
							if (res.code == 0) {
								returnFunction();
								layer.close(index);
								sendToMessage(sdata1);
							}
						}
					});
				} else {
					layer.msg("请选择审批人！");
				}
			},
			btn2 : function(index, layero) {
				layer.close(index);
			}
		});
	}

	// 初始化一条本人的代办任务
	function initialProcess(sdata1, processCode, taskName) {
		var result = "";
		$.ajax({
			url : '/task/initialProcess',
			method : 'get',
			data : {
				sdata1 : sdata1,
				processCode : processCode,
				taskName : taskName
			},
			async : false,
			success : function(res) {
				result = res.code;
			}
		});
		return result;
	}

	// 判断是否打开待选执行者列表
	function isSelectExecutor(sdata1, processCode) {
		var flag = false;
		$.ajax({
			url : '/task/isSelectExecutor',
			method : 'get',
			data : {
				sdata1 : sdata1,
				processCode : processCode
			},
			async : false,
			success : function(res) {
				flag = res;
			}
		});
		return flag;
	}

	/**
	 * 任务回退
	 */
	window.backProcess = function(sdata1) {
		var result = "";
		$.ajax({
			url : '/task/backProcess',
			method : 'get',
			data : {
				sdata1 : sdata1
			},
			async : false,
			error : function() {
				layer.msg("Connection error!");
			},
			success : function(res) {
				if (res.code == "0") {
					layer.msg("任务回退成功！")
					result = "back_success";
				} else {
					layer.msg("任务回退失败！")
					result = "back_error";
				}
			}
		});
		return result;
	}

	/**
	 * 删除业务数据的时候级联删除任务，并将任务存到历史记录中
	 */
	window.removeTaskBySdata1 = function(sdata1) {
		var result = "";
		$.ajax({
			url : '/task/removeTaskBySdata1',
			method : 'get',
			data : {
				sdata1 : sdata1
			},
			async : false,
			error : function() {
				layer.msg("Connection error!");
			},
			success : function(res) {
				if (res.code == "0") {
					result = "back_success";
				} else {
					result = "back_error";
				}
			}
		});
		return result;
	}

	// 及时通讯
	function establishSocket() {
		var socket = new SockJS('/endpointAric'); // 连接SockJS的endpoint名称为"endpointWisely"
		stompClient = Stomp.over(socket);// 使用STMOP子协议的WebSocket客户端
		stompClient.connect({}, function(frame) {// 连接WebSocket服务端
			// 通过stompClient.subscribe订阅/topic/getResponse
			// 目标(destination)发送的消息,这个是在控制器的@SentTo中定义的
			stompClient.subscribe('/topic/getResponse', function(response) {
				// showResponse(JSON.parse(response.body).responseMessage);
			});
		});
	}

	function sendToMessage(sdata1) {
		// 通过stompClient.send向/welcome
		// 目标(destination)发送消息,这个是在控制器的@MessageMapping中定义的
		stompClient.send("/welcome", {}, JSON.stringify({
			'sdata1' : sdata1
		}));
	}
});
