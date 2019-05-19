var mateData = [];
var selectedId = "";
layui.use([ 'form', 'table' ], function() {
	var form = layui.form;
	var table = layui.table;
	var $ = layui.$;
	loadMatetable(table);
	$("#addMateBtn").on("click", function() {
		layer.open({
			type : 2,
			title : "选择物料",
			area : [ '800px', '650px' ],
			shade : 0,
			maxmin : false,
			content : "/quote/choose?selected=" + selectedId + "&type=modify",
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				var rows = $(layero).find('iframe')[0].contentWindow.getData(); // 将子窗口中的
				if (rows != "") {
					mateData = [];
					selectedId = "";
					for ( var i = 0; i < rows.length; i++) {
						var elem = rows[i];
						mateData.push(elem);
						if (selectedId == "") {
							selectedId = elem.id;
						} else {
							selectedId += "," + elem.id;
						}
					}
					$("#struList").val(JSON.stringify(mateData));
					loadMatetable(table);
				}
				layer.close(index); // 关闭弹窗
			},
			btn2 : function(index) {
				layer.close(index);
			}
		});
	});

	$("#delMateBtn").on('click', function() {
		var checked = table.checkStatus("mateTable");
		var tempData = []
		if (checked.data.length > 0) {
			selectedId = "";
			for ( var i = 0; i < mateData.length; i++) {
				var elem = mateData[i];
				var flag = true;
				for ( var k = 0; k < checked.data.length; k++) {
					var elem1 = checked.data[k];
					if (elem.id = elem1.id) {
						flag = false;
						break;
					}
				}
				if (flag) {
					tempData.push(elem);
					if (selectedId == "") {
						selectedId = elem.id;
					} else {
						selectedId += "," + elem.id;
					}
				}
			}
			mateData = tempData;
			loadMatetable(table);
		} else {
			layer.msg("请选择要删除的数据！");
		}
	});

	// 模板下拉选中监听事件
	form.on('select(tempSelected)', function(data) {
		if (data.value != "") {
			// 获取传入的tempList
			var tempList = $("#tempList").val();
			var temp = JSON.parse(tempList);
			for ( var i = 0; i < temp.length; i++) {
				if (temp[i].tempCode == data.value) {
					var tempId = temp[i].id;
					$("#quoteTempId").val(tempId);
					return;
				}
			}
		} else {
			$("#tempName").val("");
			$("#quoteTempId").val("");
		}
	});

	$("#backBtn").on("click", function() {
		tuoBack('.batchModify','#serachInfo');
	});
	$("#saveBtn").on("click", function() {
		var data = $("#signForm").serialize();
		$.ajax({
			url : '/quote/saveBatchModify',
			type : 'post',
			data : data,
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					tuoBack('.batchModify','#serachInfo');
				}
			}
		});
	});
});

function loadMatetable(table) {
	table.render({
		elem : '#mateTable',
		data : mateData,
		page : true,
		cols : [ [ {
			checkbox : true,
			fixed : 'center'
		}, {
			field : 'mateCode',
			title : '物料编码',
			width : 90,
			fixed : 'left'
		}, {
			field : 'mateName',
			title : '物料名称',
			width : 100
		}, {
			field : 'unit',
			title : '单位'
		}, {
			field : 'tempCode',
			title : '模板编码'
		}, {
			field : 'tempName',
			title : '模板名称'
		}, {
			field : 'basicUnit',
			title : '报价单位'
		} ] ]
	})
}