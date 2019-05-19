var table;
layui.use([ 'table', 'laydate' ], function() {
	table = layui.table;
	var $ = layui.$;
	var laydate = layui.laydate;
	initOrderTable();
	laydate.render({
		elem : '#startDate', // 指定元素
	});
	laydate.render({
		elem : '#endDate', // 指定元素
	});
	table.on('tool(demo)', function(event) {
		var Purchase = '';
		var data = event.data;
		if (this.innerHTML == '查看') {
			console.info(event.data.fid)
			var contNumber = data.contOrdeNumb;
			var url = "/getReleaseDetail?fid=" + contNumber + "&type=" + Purchase;
			console.info(contNumber)
			tuoGo(url, 'ReleaseDetail', 'orderTableId')
		}
	});
	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	// 条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	var $ = layui.$, active = {
		reload : function() {
			var supply = $('.supply').val(); // 供应商
			var Buyer = $('.Buyer').val();// 采购员
			var contOrdeNumb = $('#contOrdeNumb').val();// 合同订单编号
			var dateSta = $('.dateSta').val();
			var dateEnd = $('.dateEnd').val();
			var type = $('.type').val();
			var deliState = $('.deliState').val();
			var purchOrg = $("#purchOrg").val();
			var publishName = $("#publishName").val();
			
			// 执行重载
			table.reload('orderTableId', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {// 后台定义对象接收
					supply : supply,
					Buyer : Buyer,
					ordeNumb : contOrdeNumb,
					dateSta : dateSta,
					dateEnd : dateEnd,
					ordeType : type,
					deliState : deliState,
					purchOrg : purchOrg,
					publishName : publishName
				}
			});
		}
	};

});

function initOrderTable() {
	table.render({
		elem : "#orderTable1",
		page : true,
		width : '100%',
		minHeight : '20px',
		limit : 10,
		id : "orderTableId",
		cols : [ [
				{type : 'numbers', title : '序号' },
				{field : 'contOrdeNumb', title : '合同订单/编号', align : 'center', width : 120, sort : true},
				{field : 'status', title : '状态', align : 'center', width : 60, sort : true},
				{field : 'subeDate', title : '订购日期', align : 'center', width : 90, sort : true,
					templet : function(d) {
						var date = new Date(d.subeDate);
						var year = date.getFullYear();
						var month = date.getMonth() + 1;
						var day = date.getDate();
						return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
					}
				}, 
				{field : 'suppNumb', title : '供应商名称', align : 'center', width : 360, sort : true}, 
				{field : 'suppName', title : '供应商编码', align : 'center', width : 155, sort : true},
				{field : 'ortype', title : '订单类型', align : 'center', width : 104},
				{field : 'releNumb', title : '已交量/订单量', align : 'center', width : 104}, 
				{field : 'deliType', title : '交货状态', align : 'center', width : 90, sort : true}, 
				{field : 'purchOrg', title : '采购组织', align : 'center', width : 70},
				{field : 'publishName', title : '发布人', align : 'center', width : 70}, 
				{fixed : 'right', title : '操作', width : 80, align : 'center', toolbar : '#barDemo'} 
			] ],
		url : "/queryReleaseList" + "?Supp=Supp",
		done : function (res, curr, count){
			var data = res.data;
			var doneNum = 0, totalNum = 0;
			if(data.length > 0) {
				for ( var k = 0; k < data.length; k++) {
					var elem = data[k];
					var releNumb = elem.releNumb;
					var result = releNumb.split("/");
					var done = result[0];
					var total = result[1];
					doneNum += parseFloat(done);
					totalNum += parseFloat(total);
				}
			}
			$("#doneNum").html(doneNum);
			$("#totalNum").html(totalNum);
		}
	})

};
//导出top数据
$('#exportBtn').click(function() {
	// 供应商
//	var suppName = $('#suppName').val();
	// 订购日期
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	// 采购订单号
	var contOrdeNumb = $('#contOrdeNumb').val();
	// 采购订单状态
	var status = $('#status').val();
	// 交货状态
	var deliType = $('#deliType').val();
	// 采购组织
	var purchOrg = $('#purchOrg').val();
	// 发布人
	var publishName = $("#publishName").val();
	var msg = "是否导出条件过滤后的全部订单数据？";
	if (publishName == "" && startDate == '' && endDate == '' && contOrdeNumb == '' && status == '' && deliType == '' && purchOrg == '') {
		msg = "是否导出全部订单数据？";
	}
	layer.confirm(msg,  
			{icon : 3, title : '提示'},
			function(index) {
				var obj = new Object();
//				obj.suppName = suppName;
				obj.startDate = startDate;
				obj.endDate = endDate;
				obj.contOrdeNumb = contOrdeNumb;
				obj.status = status;
				obj.deliType = deliType;
				obj.purchOrg = purchOrg;
				obj.publishName = publishName;
				var objjson = JSON.stringify(obj);
				var selectMateCodes = encodeURIComponent(objjson);
				var url = "/exportOrderInfo?objjson=" + selectMateCodes+"&zzoem=res";
				location = url;
				layer.close(index);
			});
});
