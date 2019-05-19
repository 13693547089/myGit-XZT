layui.use([ 'table' ], function() {
	var $ = layui.$;
	var table = layui.table;
	receiveSocket();
	table.render({
		elem : '#undoTable',
		url : '/task/undoList',
		cols : [ [ {
			field : 'taskName',
			title : '任务名称',
			event : 'doTask'
		}, {
			width : 120,
			field : 'executorName',
			title : '执行者',
			event : 'doTask'
		}, {
			width : 180,
			field : 'createTime',
			title : '创建时间',
			templet : function(d) {
				return formatTime(d.createTime, 'yyyy-MM-dd hh:mm:ss');
			},
			event : 'doTask'
		} ] ]
	});
	table.render({
		elem : '#doneTable',
		url : '/task/doneList',
		cols : [ [ {
			field : 'taskName',
			title : '任务名称',
			event : 'doTask'
		}, {
			width : 120,
			field : 'executorName',
			title : '执行者',
			event : 'doTask'
		}, {
			width : 180,
			field : 'createTime',
			title : '创建时间',
			templet : function(d) {
				return formatTime(d.createTime, 'yyyy-MM-dd hh:mm:ss');
			},
			event : 'doTask'
		} ] ]
	});
	// 代办已办
	$('.daimore').on('click', function(event) {
		var url = "/task/taskCenter?centerType=undo";
		window.location.href = url;
	})
	$('.yimore').on('click', function(event) {
		var url = "/task/taskCenter?centerType=done";
		window.location.href = url;
	})

	// 监听代办列表的单元格事件
	table.on('tool(undoFilter)', function(obj) {
		var url = obj.data.actionUrl;
		// debugger;
		// var a = encodeURIComponent(obj.data.taskName);
		if (url != null) {
			window.location.href = url + "?sdata1=" + obj.data.sdata1
					+ "&taskName=" + encodeURIComponent(obj.data.taskName)
					+ "&processCode=" + obj.data.processCode
					+ "&pagePattern=write";
		} else {
			layer.msg("数据错误！");
		}
	});
	// 监听一版列表的单元格事件
	table.on('tool(doneFilter)', function(obj) {
		debugger;
		var url = obj.data.actionUrl;
		if (url != null) {
			window.location.href = url + "?sdata1=" + obj.data.sdata1
					+ "&taskName=" + encodeURIComponent(obj.data.taskName)
					+ "&processCode=" + obj.data.processCode
					+ "&pagePattern=read";
		} else {
			layer.msg("数据错误！");
		}
	});

	function receiveSocket() {
		var socket = new SockJS('/endpointAric'); // 连接SockJS的endpoint名称为"endpointWisely"
		var stompClient = Stomp.over(socket);// 使用STMOP子协议的WebSocket客户端
		stompClient.connect({}, function(frame) {// 连接WebSocket服务端
			// 通过stompClient.subscribe订阅/topic/getResponse
			// 目标(destination)发送的消息,这个是在控制器的@SentTo中定义的
			stompClient.subscribe('/topic/getResponse', function(response) {
				debugger;
				var json = $.parseJSON(response.body);
				var sdata1 = json.sdata1;
				var type = json.type;
				var msg = "您有新的任务，请及时处理！";
				if ("notice" == type) {
					layer.open({
						type : 2,
						title : '新消息通知',
						maxmin : true,
						area : [ '600px', '450px' ],
						shade : false,
						shadeClose : false, // 点击遮罩关闭
						btn : [ '关闭' ],
						content : '/notices/noticeInfo?type=read&id=' + sdata1,
						yes : function(index, layero) {
							// 更新已读的数据
							$.get("/notices/readNotice", {id : sdata1}, function(data,status){
//						        alert("Data: " + data + "nStatus: " + status);
						    });
							layer.close(index);
						}
					});
				} else {
					// 处理任务
					$.ajax({
						url : '/task/getTask',
						method : 'get',
						data : {
							sdata1 : sdata1
						},
						async : false,
						error : function() {
							layer.msg("Connection error!");
						},
						success : function(res) {
							var isOwn = res.isOwn;
							if (isOwn) {
								layer.confirm(msg, {btn : [ '处理', '关闭' ]}, function() {
									var url = res.actionUrl;
									if (url != null)
										window.location.href = url + "?sdata1=" + res.sdata1;
									layer.close();
								}, function() {});
							}
						}
					});
				}
				// showResponse(JSON.parse(response.body).responseMessage);
			});
		});
	}
});
