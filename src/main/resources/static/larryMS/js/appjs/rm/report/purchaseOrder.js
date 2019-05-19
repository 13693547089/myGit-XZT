$(function() {
	var mychart = echarts.init(document.getElementById('po_div'));

	layui.use([ "form", "laydate" ], function() {
		var $ = layui.$;
		var laydate = layui.laydate;
		var dateStr = $("#dateStr").val();
		var currentDate = $("#currentDate").val();

		renderEchart(dateStr);

		laydate.render({
			elem : '#month_date',
			type : 'month',
			range : false,
			value : dateStr,
			isInitValue : true,
			max : currentDate,
			done : function(value, date) {
				renderEchart(value);
			}
		});
	});

	function renderEchart(value) {
		var index = layer.load();
		$.ajax({
			type : "get",
			url : "/report/purchase_order_data",
			data : {
				dateStr : value
			},
			dataType : "JSON",
			success : function(data) {
				debugger;
				option = {
					legend : {},
					tooltip : {},
					dataset : {
						source : data
					},
					grid : {
						containLabel : true
					},
					xAxis : {
						type : 'category',
						inverse : true
					},
					yAxis : [ {
						type : 'value',
						name : '采购单据量/明细合计',
						position : 'left',
						offset : 0
					}, {
						type : 'value',
						name : '环比/同比',
						axisLabel : {
							show : true,
							interval : 0,
							showMinLabel : true,
							formatter : '{value} %'
						},
						position : 'right',
						offset : 0
					} ],
					series : [ {
						type : 'line',
						yAxisIndex : 0,
						itemStyle : {
							color : "#70CBB7FF"
						}
					}, {
						type : 'line',
						yAxisIndex : 0,
						itemStyle : {
							color : "#528EF3FF"
						}
					}, {
						type : 'bar',
						yAxisIndex : 1,
						itemStyle : {
							color : new echarts.graphic.LinearGradient(
									0, 0, 0, 1, 
									[ 
										{offset : 0, color : '#A5C5FBFF'},
										{offset: 0.5, color: '#4183F1FF'},
										{offset : 1, color : '#2F77EFFF'} 
									])
						}
					}, {
						type : 'bar',
						yAxisIndex : 1,
						itemStyle : {
							color : new echarts.graphic.LinearGradient(
									0, 0, 0, 1, 
									[ 
										{offset : 0, color : '#9AE3D2FF'},
										{offset : 1, color : '#6BC9B4FF'} 
									])
						}
					}, {
						type : 'bar',
						yAxisIndex : 1,
						itemStyle : {
							color : new echarts.graphic.LinearGradient(
									0, 0, 0, 1, 
									[ 
										{offset : 0, color : '#FBDF89FF'},
										{offset: 0.5, color: '#F9CD6BFF'},
										{offset : 1, color : '#F6BC51FF'} 
									])
						}
					}, {
						type : 'bar',
						yAxisIndex : 1,
						itemStyle : {
							color : new echarts.graphic.LinearGradient(
									0, 0, 0, 1, 
									[ 
										{offset : 0, color : '#FBC0A4FF'},
										{offset : 1, color : '#ED743BFF'} 
									])
						}

					} ]
				};
				mychart.setOption(option);
				layer.close(index);
			}
		});

	}

});