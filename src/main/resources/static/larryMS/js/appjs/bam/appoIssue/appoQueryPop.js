var table;
var tableData;
layui.use(['table','laydate'], function() {
	table = layui.table;
	var $ = layui.$;
	var laydate = layui.laydate;
	laydate.render({
		elem:"#appoDate",
		range:true
	});
	
	// 取消
	$("#cancel").click(function() {
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	});

	var appoDate = $("#appoDate").val();
	var cdc = $("#cdc").val();
	table.render({
		elem : "#reportTable",
		url : "/queryAppointStatByAppoDate",
		page : false,
		where : {
			appoDate : appoDate,
			cdc : cdc
		},
		limit : 10,
		id : "reportTable",
		cols : [ [ {
			field : 'reportTitle',
			title : '状态/时间段'
		}, {
			field : 'billStr',
			title : '单据量'
		}, {
			field : 'truckStr',
			title : '车辆'
		}, {
			field : 'amountStr',
			title : '方量'
		} ] ],
		id : "reportTable"
	});

	// 查询
	$("#queryAppo").click(function() {
		var appoDate = $("#appoDate").val();
		var cdc = $("#cdc").val();
		if(cdc == ""){
			cdc = $("#cdcAddr").val();
		}
		table.reload("reportTable", {
			where : {
				appoDate : appoDate,
				cdc : cdc
			}
		})

		// var appoDate = $("#appoDate").val();
		// if (appoDate != "") {
		// var formData = $("#appoForm").serialize();
		// $.ajax({
		// type : "POST",
		// url : "/queryAppointStatByAppoDate",
		// data : formData,
		// dataType : "JSON",
		// async : false,
		// error : function(request) {
		// layer.alert("Connection error");
		// },
		// success : function(data) {
		// $("#appoNumber").text(data.appoNumber)
		// $("#appoAmount").text(data.appoAmount)
		// $("#confAppo").text(data.confAppo)
		// $("#unconfAppo").text(data.unconfAppo)
		// $("#publAppo").text(data.publAppo)
		// $("#unpublAppo").text(data.unpublAppo)
		// $("#eighAppoNum").text(data.eighAppoNum)
		// $("#eighAppoAmo").text(data.eighAppoAmo)
		// $("#tenAppoNum").text(data.tenAppoNum)
		// $("#tenAppoAmo").text(data.tenAppoAmo)
		// $("#thirAppoNum").text(data.thirAppoNum)
		// $("#thirAppoAmo").text(data.thirAppoAmo)
		// $("#fiftAppoNum").text(data.fiftAppoNum)
		// $("#fiftAppoAmo").text(data.fiftAppoAmo)
		// $("#sixAppoNum").text(data.sixAppoNum)
		// $("#sixAppoAmo").text(data.sixAppoAmo)
		// }
		// });
		// } else {
		// layer.alert("请输入预约日期");
		// }
		//		
	});
});
