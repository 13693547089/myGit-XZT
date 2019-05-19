layui.use([ "form", "table" ], function() {
	var $ = layui.$, table = layui.table, form = layui.form;
	var tableData = [];

	getData("", "", "");
	// 监听下拉选择
	form.on("select(oemSupp)", function(data) {
		var oemVal = data.value;
		var packSuppCode = $("#packSuppCode").val();
		var packSuppName = $("#packSuppName").val();
		if (packSuppCode != "") {
			var packVal = packSuppCode + "-" + packSuppName;
			if (packVal == oemVal) {
				$("#oemSuppId").empty();
				$('#oemSuppId').append('<option value="">请选择...</option>');
				var list = $("#suppListStr").val();
				var ret = JSON.parse(list);
				$.each(ret, function(i, item) {
					$('#oemSuppId').append(
							'<option value="' + item.suppId + "-"
									+ item.suppName + '">' + item.suppName
									+ '</option>');
				});
				form.render("select");
				$("#oemSuppCode").val("");
				$("#oemSuppName").val("");
				layer.msg("OEM供应商和包材供应商不可一样，请重新选择！")
				return;
			}
		}
		if (oemVal != "") {
			var spl = oemVal.split("-");
			$("#oemSuppCode").val(spl[0]);
			$("#oemSuppName").val(spl[1]);
		} else {
			$("#oemSuppCode").val("");
			$("#oemSuppName").val("");
		}
	});
	

	form.on("select(packSupp)", function(data) {
		var packVal = data.value;
		var oemSuppCode = $("#oemSuppCode").val();
		var oemSuppName = $("#oemSuppName").val();
		if (oemSuppCode != "") {
			var oemVal = oemSuppCode + "-" + oemSuppName;
			if (packVal == oemVal) {
				$("#packSuppId").empty();
				$('#packSuppId').append('<option value="">请选择...</option>');
				var list = $("#suppListStr").val();
				var ret = JSON.parse(list);
				$.each(ret, function(i, item) {
					$('#packSuppId').append(
							'<option value="' + item.suppId + "-"
									+ item.suppName + '">' + item.suppName
									+ '</option>');
				});
				form.render("select");
				$("#packSuppCode").val("");
				$("#packSuppName").val("");
				layer.msg("OEM供应商和包材供应商不可一样，请重新选择！")
				return;
			}
		}
		if (packVal != "") {
			var spl = packVal.split("-");
			$("#packSuppCode").val(spl[0]);
			$("#packSuppName").val(spl[1]);
		} else {
			$("#packSuppCode").val("");
			$("#packSuppName").val("");
		}
	})

	// 搜索
	$("#btn_query").on("click", function() {
		var oemCode = $("#oemSuppCode").val();
		var packCode = $("#packSuppCode").val();
		var mateCode = $("#mateCode").val();
		if (oemCode != "" && packCode != "") {
			layer.confirm("搜索会导致已选择未保存的数据清空，是否确认搜索？", {
				icon : 3,
				title : '提示'
			}, function(index) {
				getData(oemCode, packCode, mateCode);
				layer.close(index);
			})
		} else {
			layer.msg("OEM供应商和包材供应商必须都选择！");
		}
	});
	
	// 回车搜索
	$("#mateCode").on("keydown", function(event) {
		if(event.keyCode == "13") {
			// 回车搜索
			var mateCode = $("#mateCode").val();
			if(mateCode != "") {
				var oemCode = $("#oemSuppCode").val();
				var packCode = $("#packSuppCode").val();
				layer.confirm("搜索会导致已选择未保存的数据清空，是否确认搜索？", {
					icon : 3,
					title : '提示'
				}, function(index) {
					getData(oemCode, packCode, mateCode);
					layer.close(index);
				})
			}
		}
	});

	function getData(oemCode, packCode, mateCode) {
		debugger;
		$.ajax({
			url : "/supp_pack_mate/mate_data",
			async : false,
			data : {
				oemSuppCode : oemCode,
				packSuppCode : packCode,
				mateCode : mateCode
			},
			error : function() {

			},
			success : function(res) {
				if (res.length > 0) {
					for (var i = 0; i < res.length; i++) {
						var elem = res[i];
						var flag = elem.lay_CHECKED;
						if (flag == "yes")
							elem.LAY_CHECKED = true;
					}
				}
				tableData = res;
				tableRender();
			}
		});

	}
	function tableRender() {
		table.render({
			elem : '#toChooseTable',
			data : tableData,
			page : true,
			cols : [ [ {
				type : "checkbox"
			}, {
				field : 'mateCode',
				title : '物料编码'
			}, {
				field : 'mateName',
				title : '物料名称'
			}, {
				field : 'mateGroupExpl',
				title : '物料组'
			}, {
				field : 'mateType',
				title : '物料类型'
			} ] ]
		});
	}
	
	
	function guid() {
		function S4() {
			return (((1 + Math.random()) * 0x10000) | 0).toString(16)
					.substring(1);
		}
		return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
	}
	window.getCheckedData = function() {
		debugger;
		var result = [];
		var oemCode = $("#oemSuppCode").val();
		var oemName = $("#oemSuppName").val();
		var packCode = $("#packSuppCode").val();
		var packName = $("#packSuppName").val();
		if (oemCode != "" && packCode != "") {
			for (var k = 0; k < tableData.length; k++) {
				var elem = tableData[k];
				if (elem.LAY_CHECKED) {
					var arr = {
						id : guid(),
						oemSuppCode : oemCode,
						oemSuppName : oemName,
						packSuppCode : packCode,
						packSuppName : packName,
						mateCode : elem.mateCode,
						mateName : elem.mateName
					};
					result.push(arr);
				}
			}
		}
		return result;
	}
});