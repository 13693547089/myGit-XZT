layui.use([ "table", "form",'laydate' ], function() {
	var $ = layui.$, form = layui.form, table = layui.table;
	var laydate = layui.laydate;
	laydate.render({
		    elem: '#startDate', //指定元素
	});
	 laydate.render({
		    elem: '#endDate', //指定元素
	 });
	table.render({
		elem : '#signTable',
		url : '/sign/getSignData',
		page : true,
		cols : [ [
				{
					checkbox : true,
					fixed : 'left'
				},
				{
					field : 'status',
					title : '单据状态',
					align : 'center',
					width : 93
				},
				{
					field : 'deliCode',
					title : '送货单号',
					align : 'center',
					width : 113
				},
				{
					field : 'deliDate',
					title : '送货日期',
					align : 'center',
					width : 106,
					templet : function(d) {
						var date = new Date(d.deliDate);
						var year = date.getFullYear();
						var month = date.getMonth() + 1;
						var day = date.getDate();
						return year + "-" + (month < 10 ? "0" + month : month)
								+ "-" + (day < 10 ? "0" + day : day);
					}
				},
				{
					field : 'suppName',
					title : '供应商',
					align : 'center',
					width : 145
				},
				{
					field : 'deliAmount',
					title : '送货方量',
					align : 'center',
					width : 83
				},
				{
					field : 'truckNum',
					title : '送货车次',
					align : 'center',
					width : 63
				},
				{
					field : 'creator',
					title : '创建人',
					align : 'center',
					width : 109
				},
				{
					field : 'createDate',
					title : '创建时间',
					align : 'center',
					width : 106,
					templet : function(d) {
						var date = new Date(d.createDate);
						var year = date.getFullYear();
						var month = date.getMonth() + 1;
						var day = date.getDate();
						return year + "-" + (month < 10 ? "0" + month : month)
								+ "-" + (day < 10 ? "0" + day : day);
					}
				}, {
					fixed : 'right',
					title : '操作',
					width : 50,
					align : 'center',
					toolbar : '#barDemo'
				} ] ]

	});

	// 签到，触发扫描枪
	$("#signBtn").on(
			'click',
			function() {
				layer.open({
					type : 2,
					title : '扫描信息',
					maxmin : true,
					shadeClose : false, // 点击遮罩关闭层
					area : [ '800px', '520px' ],
					content : '/sign/sign/',
					btn : [ '确认', '取消' ],
					yes : function(index, layero) {
						// 按钮【按钮一】的回调
						var data = $(layero).find("iframe")[0].contentWindow
								.getSignInfo();
						if (data != false) {
							$.ajax({
								url : '/sign/saveSign',
								method : 'post',
								async : false,
								success : function(res) {
									layer.msg(res.msg);
									if (res.code == 0)
										layer.close(index);
								}
							});
						}
					},
					btn2 : function(index, layero) {
						// 按钮【按钮二】的回调
						layer.close(index);
						// return false 开启该代码可禁止点击该按钮关闭
					}
				});
			});
});