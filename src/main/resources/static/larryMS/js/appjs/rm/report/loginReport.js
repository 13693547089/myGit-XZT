$(function() {
	var tt1_sort = tt2_sort = tt3_sort = tt4_sort = "desc";

	layui.use([ "form", "table", "laydate" ], function() {
		var $ = layui.$;
		var laydate = layui.laydate;
		var dateStr = $("#dateStr").val();
		var currentDate = $("#currentDate").val();

		loadData(dateStr);
		laydate.render({
			elem : '#month_date',
			type : 'month',
			range : false,
			value : dateStr,
			isInitValue : true,
			max : currentDate,
			done : function(value, date) {
				loadData1(value);
				login_operate(value);
			}
		});

		// 前十名
		$("#btn_before1").on("click", function() {
			remove_class("btn_after1", "btn_before1");
			tt1_sort = "desc";
			getTopTen(topTen1, dateStr, "user", "login", tt1_sort);
		});
		$("#btn_before2").on("click", function() {
			remove_class("btn_after2", "btn_before2");
			tt2_sort = "desc";
			getTopTen(topTen2, dateStr, "supplier", "login", tt2_sort);
		});
		$("#btn_before3").on("click", function() {
			remove_class("btn_after3", "btn_before3");
			tt3_sort = "desc";
			getTopTen(topTen3, dateStr, "user", "operate", tt3_sort);
		});
		$("#btn_before4").on("click", function() {
			remove_class("btn_after4", "btn_before4");
			tt4_sort = "desc";
			getTopTen(topTen4, dateStr, "supplier", "login", tt4_sort);
		});

		// 后十名
		$("#btn_after1").on("click", function() {
			remove_class("btn_before1", "btn_after1");
			tt1_sort = "asc";
			getTopTen(topTen1, dateStr, "user", "login", tt1_sort);
		});
		$("#btn_after2").on("click", function() {
			remove_class("btn_before2", "btn_after2");
			tt2_sort = "asc";
			getTopTen(topTen2, dateStr, "supplier", "login", tt2_sort);
		});
		$("#btn_after3").on("click", function() {
			remove_class("btn_before3", "btn_after3");
			tt3_sort = "asc";
			getTopTen(topTen3, dateStr, "user", "operate", tt3_sort);
		});
		$("#btn_after4").on("click", function() {
			remove_class("btn_before4", "btn_after4");
			tt4_sort = "asc";
			getTopTen(topTen4, dateStr, "supplier", "login", tt4_sort);
		});

		function loadData1(value) {
			var index = layer.load();
			var url = "/report/login_report_ratio";
			$("#ratio_div").load(url, {
				dateStr : value
			}, function(r, status) {
				layer.close(index);
			});

			var url = "/report/login_operate_top_ten";
			$("#top_div").load(url, {
				dateStr : value,
				user_type : 'user', // user, supplier
				operate_type : 'login',// login, operate
				sort_type : 'asc' // asc(low 10),desc(top 10)
			}, function(r, status) {

			})
		}
		// 移除和添加样式
		function remove_class(add_btn, remove_btn) {
			$("#" + remove_btn).addClass("btn_check");
			$("#" + remove_btn).removeClass("btn_uncheck");
			$("#" + add_btn).addClass("btn_uncheck");
			$("#" + add_btn).removeClass("btn_check");
		}

		function loadData(value) {
			var index = layer.load();
			var url = "/report/login_report_ratio";
			$("#ratio_div").load(url, {
				dateStr : value
			}, function(r, status) {
				layer.close(index);
			});

		}
	});
	var myChart = echarts.init(document.getElementById('login_div'));
	var topTen1 = echarts.init(document.getElementById('top_div_1'));
	var topTen2 = echarts.init(document.getElementById('top_div_2'));
	var topTen3 = echarts.init(document.getElementById('top_div_3'));
	var topTen4 = echarts.init(document.getElementById('top_div_4'));
	
	var dateStr = $("#dateStr").val();
	login_operate(dateStr);
	function login_operate(value) {
		$.ajax({
			type : "get",
			url : "/report/login_operate_frequence",
			data : {
				dateStr : value
			},
			async : false,
			dataType : "JSON",
			success : function(data) {
				debugger;
				option = {
					legend : {},
					tooltip : {},
					dataset : {
						// 这里指定了维度名的顺序，从而可以利用默认的维度到坐标轴的映射。
						// 如果不指定 dimensions，也可以通过指定 series.encode 完成映射，参见后文。
						// dimension.add("内部登录");
						// dimension.add("内部操作");
						// dimension.add("外部登录");
						// dimension.add("外部操作");
						source : data
					},
					grid : {
						containLabel : true
					},
					xAxis : {
						type : 'category'
					},
					yAxis : {
						type : 'value',
						name : '登录次数/操作次数',
						position : 'left',
						offset : 0
					},
					series : [ {
						type : 'bar',
						stack : 'xx',
						itemStyle : {
							color : new echarts.graphic.LinearGradient(
									0, 0, 0, 1, 
									[ 
										{offset : 0, color : '#FBDF89'},
										{offset: 0.5, color: '#F9CD6B'},
										{offset : 1, color : '#F6BC51'} 
									])
						}
					}, {
						type : 'bar',
						stack : 'yy',
						itemStyle : {
							color : new echarts.graphic.LinearGradient(
									0, 0, 0, 1, 
									[ 
										{offset : 0, color : '#A5C5FB'},
										{offset : 0.5, color : '#4183F1'},
										{offset : 1, color : '#2F77EF'} 
									])
						}
					}, {
						type : 'bar',
						stack : 'xx',
						itemStyle : {
							color : new echarts.graphic.LinearGradient(
									0, 0, 0, 1, 
									[ 
										{offset : 0, color : '#9AE3D2'},
										{offset : 1, color : '#6BC9B4'} 
									])
						}
					}, {
						type : 'bar',
						stack : 'yy',
						itemStyle : {
							color : new echarts.graphic.LinearGradient(
									0, 0, 0, 1, 
									[ 
										{offset : 0, color : '#FBC0A4'},
										{offset : 1, color : '#ED743B'} 
									])
						}

					} ]
				};
				myChart.setOption(option);
			}
		});
	}
	// user_type : 'user', // user, supplier
	// operate_type : 'login',// login, operate
	// sort_type : 'asc' // asc(low 10),desc(top 10)
	getTopTen(topTen1, dateStr, "user", "login", tt1_sort);
	getTopTen(topTen2, dateStr, "supplier", "login", tt2_sort);
	getTopTen(topTen3, dateStr, "user", "operate", tt3_sort);
	getTopTen(topTen4, dateStr, "supplier", "login", tt4_sort);

	function getTopTen(myecharts, value, user_type, operate_type, sort_type) {
		var color1 = '#377AF1';
		var color2 = '#66F3D4';
		var grid_left = "10%";
		if (user_type == "supplier") {
			color1 = '#66F3D4';
			color2 = '#377AF1';
			grid_left = "26%";
		}
		$.ajax({
			type : "get",
			url : "/report/login_operate_top_ten",
			data : {
				date_str : dateStr,
				user_type : user_type,
				operate_type : operate_type,
				sort_type : sort_type
			},
			async : false,
			dataType : "JSON",
			error : function(data) {
				debugger;
			},
			success : function(data) {
				option = {
					legend : {
						height : 0
					},
					tooltip : {},
					dataset : {
						dimensions : [],
						source : data
					},
					xAxis : {},
					yAxis : {
						axisLabel : {
							show : true,
						},
						position : 'left',
						type : 'category',
						inverse : true
					},
					grid: {
						left: grid_left
					},
					series : [ {
						type : 'bar',
						itemStyle : {
							barBorderRadius : [ 0, 14, 14, 0 ],
							color : new echarts.graphic.LinearGradient(1, 0, 0,
									1, [ {
										offset : 1,
										color : color1
									}, {
										offset : 0,
										color : color2
									} ])
						}
					} ]
				};
				myecharts.setOption(option);
			}
		});
	}
});