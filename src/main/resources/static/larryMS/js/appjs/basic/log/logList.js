layui.use([ "table" ], function() {
	var $ = layui.$;
	var table = layui.table;

	table.render({
		elem : "#logTable",
		url : "/basic/getLogList",
		page : true,
		cols : [ [ {
			checkbox : true
		}, {
			field : 'realName',
			title : '姓名',
			templet : function(d) {
				return "<span style='color: blue;'>" + d.realName + "</span>";
			},
			event : "detailsInfo"
		}, {
			field : 'userName',
			title : '登录名',
			templet : function(d) {
				return "<span style='color: blue;'>" + d.userName + "</span>";
			},
			event : "detailsInfo"
		}, {
			field : 'isOnline',
			title : '是否在线'
		}, {
			field : 'firstTime',
			title : '首次访问时间',
			templet : function(d) {
				return formatTime(d.firstTime, 'yyyy-MM-dd hh:mm:ss');
			}
		}, {
			field : 'lastTime',
			title : '最后访问时间',
			templet : function(d) {
				return formatTime(d.lastTime, 'yyyy-MM-dd hh:mm:ss');
			}
		}, {
			field : 'logTimes',
			title : '登录次数'
		} ] ]
	});

	// 监听单元格点击事件
	table.on("tool(logTable)", function(obj) {
		debugger;
		var layEvent = obj.event;
		var data = obj.data;
		if (layEvent == "detailsInfo") {
			var userName = data.userName;
			// 进入该用户的操作详情信息
			url = "/basic/getLogDetails?userName=" + userName
			window.location.href = url;
		}
	});

});