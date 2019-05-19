socket = new WebSocket("ws://localhost:9094/starManager/websocket/张三");
var socket;
if (typeof (WebSocket) == "undefined") {
	debugger;
	console.log("您的浏览器不支持WebSocket");
} else {
	debugger;
	console.log("您的浏览器支持WebSocket");

	// 实现化WebSocket对象，指定要连接的服务器地址与端口
	// socket = new WebSocket("ws://localhost:9094/starManager/websocket/张三");
	socket = new WebSocket("ws://localhost:9094/starManager/websocket");
	// 打开事件
	socket.onopen = function() {
		console.log("Socket 已打开");
		// socket.send("这是来自客户端的消息" + location.href + new Date());
	};
	// 获得消息事件
	socket.onmessage = function(msg) {
		console.log(msg.data);
		// 发现消息进入 调后台获取
		getCallingList();
	};
	// 关闭事件
	socket.onclose = function() {
		console.log("Socket已关闭");
	};
	// 发生了错误事件
	socket.onerror = function() {
		alert("Socket发生了错误");
	}
	$(window).unload(function() {
		socket.close();
	});

	// $("#btnSend").click(function() {
	// socket.send("这是来自客户端的消息" + location.href + new Date());
	// });
	//  
	// $("#btnClose").click(function() {
	// socket.close();
	// });
}