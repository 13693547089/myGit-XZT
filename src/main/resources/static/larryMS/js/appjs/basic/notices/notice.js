layui.use([ "form", "table" ], function() {
	var $ = layui.$;
	var form = layui.form;
	var table = layui.table;
	var stompClient = null;
	establishSocket();
	// 搜索
	$("#serachInfo").on("click", function() {
		var asyncName = $("#asyncName").val();
		var asyncStatus = $("#asyncStatus").val();
		var asyncUserName = $("#asyncUserName").val();
		var dateStr = $("#dateStr").val();
		table.reload('asyncLogTable', {
			where : {
				asyncName : asyncName,
				asyncStatus : asyncStatus,
				asyncUserName : asyncUserName,
				dateStr : dateStr
			}
		});
	});

	// 新建通知
	$("#createBtn").on("click", function() {
		layer.open({
			type : 2,
			title : '新建通知',
			maxmin : true,
			area : [ '600px', '450px' ],
			shade : false,
			shadeClose : false, // 点击遮罩关闭
			btn : [ '确定', '取消' ],
			content : '/notices/noticeInfo?type=create',
			yes : function(index, layero) {
				debugger;
				var data = $(layero).find("iframe")[0].contentWindow.submitData();
				// 发消息给所有在线的用户
				if(data == "" || data == "error") {
					layer.msg("通知出错，请重新确认通知！");
				}else {
					sendToMessage(data);
					layer.close(index);
				}
			}
		});
	});

	// 重置
	$("#reset").on("click", function() {
		$("#asyncName").val("");
		$("#asyncStatus").val("");
		$("#asyncUserName").val("");
		$("#dateStr").val("");
		table.reload('asyncLogTable', {
			where : {
				asyncName : "",
				asyncStatus : "",
				asyncUserName : "",
				dateStr : ""
			}
		});
	});

	table.render({
		elem : '#noticesTable',
		url : "/notices/findNoticesList",
		page : true,
		cols : [ [ {
			field : 'userName',
			title : '通知者',
			width : 150,
			fixed : 'left'
		}, {
			field : 'noticeTime',
			title : '通知时间',
			width : 150,
			templet : function(d) {
				return formatTime(d.noticeTime, 'yyyy-MM-dd hh:mm:ss');
			}
		}, {
			field : 'noticeContent',
			title : '通知内容'
		} ] ]
	});
	
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
			"sdata1" : sdata1,
			"type" : "notice"
		}));
	}
})