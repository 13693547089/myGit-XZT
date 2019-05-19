$(function() {
	var mychart = echarts.init(document.getElementById('pm_div'));

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
			url : "/report/product_market_data",
			data : {
				dateStr : value
			},
			dataType : "JSON",
			success : function(data) {
				option = {
					legend : {
						width : 1000,
						padding : [ 5, 50, 5, 50 ]
					},
					tooltip : {
						padding : 10
					},
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
						name : '产销管理明细合计',
						position : 'left',
						nameGap : 0,
						nameTextStyle : {
							padding : 20
						},
					}, {
						type : 'value',
						name : '环比/同比',
						position : 'right',
						nameGap : 0,
						nameTextStyle : {
							padding : 35
						},
						axisLabel : {
							show : true,
							interval : 0,
							showMinLabel : true,
							formatter : '{value} %'
						},
						position : 'right',
						offset : 0
					} ],
					grid : {
						top : "20%",
					},
					series : [
							{
								type : 'bar',
								yAxisIndex : 1,
								itemStyle : {
									color : new echarts.graphic.LinearGradient(
											0, 0, 0, 1, [ {
												offset : 0,
												color : '#A5C5FBFF'
											}, {
												offset : 0.5,
												color : '#4183F1FF'
											}, {
												offset : 1,
												color : '#2F77EFFF'
											} ])
								}
							},
							{
								type : 'bar',
								yAxisIndex : 1,
								itemStyle : {
									color : new echarts.graphic.LinearGradient(
											0, 0, 0, 1, [ {
												offset : 0,
												color : '#9AE3D2FF'
											}, {
												offset : 1,
												color : '#6BC9B4FF'
											} ])
								}
							},
							{
								type : 'bar',
								yAxisIndex : 1,
								itemStyle : {
									color : new echarts.graphic.LinearGradient(
											0, 0, 0, 1, [ {
												offset : 0,
												color : '#FBDF89FF'
											}, {
												offset : 0.5,
												color : '#F9CD6BFF'
											}, {
												offset : 1,
												color : '#F6BC51FF'
											} ])
								}
							},
							{
								type : 'bar',
								yAxisIndex : 1,
								itemStyle : {
									color : new echarts.graphic.LinearGradient(
											0, 0, 0, 1, [ {
												offset : 0,
												color : '#FBC0A4FF'
											}, {
												offset : 1,
												color : '#ED743BFF'
											} ])
								}

							}, {
								type : 'bar',
								yAxisIndex : 1,
								itemStyle : {
									color : '#B1DF6EFF'
								}
							}, {
								type : 'bar',
								yAxisIndex : 1,
								itemStyle : {
									color : "#A591F8FF"
								}
							}, {
								type : 'line',
								yAxisIndex : 0,
								itemStyle : {
									color : "#B418E3"
								}
							}, {
								type : 'line',
								yAxisIndex : 0,
								itemStyle : {
									color : "#528EF3FF"
								}
							}, {
								type : 'line',
								yAxisIndex : 0,
								itemStyle : {
									color : "#ED743BFF"
								}
							} ]
				};
				mychart.setOption(option);
				layer.close(index);
			}
		});

	}

});
