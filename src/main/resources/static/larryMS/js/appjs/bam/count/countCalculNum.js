var table;
layui.use([ 'table', 'laydate', 'form' ], function() {
	table = layui.table;
	var $ = layui.$;
	var form = layui.form;
	var layer = layui.layer;
	var laydate = layui.laydate;
	// 查询订单下物料的计算未交量（物料的可用量）
	$("#serachBut").click(function() {
		var result = checkMust();
		if (!result.flag) {
			layer.msg(result.msg);
			return;
		}
		var orderNo = $("#orderNo").val();
		var mateCode = $("#mateCode").val();
		var unpaNumber = $("#unpaNumber").val();
		var suppRange = $("#suppRange").val();//供应商子范围编码
		if (isNaN(unpaNumber)) {
			layer.alert("物料的未交数量为非数字，请重新输入！");
			return;
		}
		$.ajax({
			type : "post",
			url : "/count/getMateCalculNumber",
			data : {
				orderNo : orderNo,
				mateCode : mateCode,
				suppRange : suppRange,
				unpaNumber : unpaNumber
			},
			dataType : "JSON",
			error : function(result) {
				layer.alert("Connection error");
			},
			success : function(data) {
				$("#calculNumber").val(data.calculNumber);
			}
		});
		var searchData = "?orderNo=" + orderNo + "&mateCode=" + mateCode+"&suppRange=" +suppRange;
		load(table, searchData);
	});
});

function load(table, searchData) {
	table.render({
		elem : '#occupyTable',
		url : "/count/findOccupyNumberByParams" + searchData,
		page : true,
		id : "occupyTableId",
		cols : [ [ {
			type : 'numbers',
			title : '序号'
		}, {
			field : 'deliCode',
			title : '送货单编码',
			width : 120
		}, {
			field : 'messCode',
			title : '直发通知单的编码',
			width : 120
		}, {
			field : 'orderNo',
			title : '采购订单编码',
			width : 120
		}, {
			field : 'mateCode',
			title : '物料编码',
			width : 200
		}, {
			field : 'unpaNumber',
			title : '订单未交量',
			width : 120
		}, {
			field : 'calculNumber',
			title : '计算未交量',
			width : 120

		}, {
			field : 'occupyNumber',
			title : '直发送货量',
			width : 120
		}, {
			field : 'msg',
			title : '报错信息'
		} ] ]
	});
}
