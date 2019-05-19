layui.use([ 'form', 'laydate' ], function() {
	var $ = layui.$;
	var laydate = layui.laydate;
	var dateStr = $("#dateStr").val();
	var currentDate = $("#currentDate").val();

	loadData(dateStr);

	laydate.render({
		elem : '#month_date',
		type : 'month',
		range : true,
		value : dateStr,
		isInitValue : true,
		max : currentDate,
		done : function(value, date) {
			loadData(value);
		}
	});

	function loadData(value) {
		var index = layer.load();
		var spl = value.split(' - ');
		var url = "/report/refresh_data";
		$("#data_refresh").load(url, {
			startDate : spl[0],
			endDate : spl[1]
		}, function(r, status) {
			layer.close(index);
		});
	}
	
	// 查看详情
	$("#btn_details").on("click", function() {
//		window.location.href="/report/bill_report_details";
		var url  = "/report/bill_report_details";
    	tuoGo(url,'billReportDetails',"");
    	
	});
});