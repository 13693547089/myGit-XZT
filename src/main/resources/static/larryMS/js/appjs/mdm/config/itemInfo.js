var changeRow;
// 初始化字段校验
$(document).ready(function() {
	inint({});
});
// 刷新
$("#refreshBtn").click(function() {
	location.reload();
});

$("#searchBtn").click(function() {
	searchFn();
});
/**
 * 查询方法
 * @returns
 */
function searchFn(){
	var mateCode = $("#mateCode").val();
	var mateType = $("#mateType").val();
	var arr = {
		mateCode : mateCode,
		mateType : mateType
	}
	inint(arr);
}

/**
 * 批量修改
 */
$("#batchBtn").click(function() {
	// 获取选中的行
	var selectRows = $("#mateTable").bootstrapTable('getSelections');
	if(selectRows.length == 0){
		alert("请选择需要批量修改的行！");
		return;
	}
	
	// 重新设置下拉框值
	$('#boardID').val('');
	$('#itemTypeID').val('');
	
	// 显示修改弹窗
	$('#batchUpdate').modal('show');
	
});

/**
 * 批量修改数据方法
 */
$("#comfirmBtn").click(function() {
	
	var selectRows = $("#mateTable").bootstrapTable('getSelections');
	
	var itemTypeCode = $('#itemTypeID').val();
	var itemTypeName = "";
	if(itemTypeCode != ''){
		itemTypeName = $('#itemTypeID').find("option:selected").text();
	}
	var board = $('#boardID').val();
	
	var matData = [];
	for(var i=0;i<selectRows.length;i++){
		var item = selectRows[i];
		var obj = {
				"mateId":item.mateId,
				"itemCode":itemTypeCode,
				"itemName":itemTypeName,
				"board":board
				};
		
		matData.push(obj);
	}
	// 对json字符串数据进行编码  
	var matInfoData =JSON.stringify(matData);
	$.ajax({
		url : "/item/updateMatPartByBatch",
		data : {
			matData : matInfoData
		},
		success : function(res) {
			// 隐藏
			$('#batchUpdate').modal('hide');
			alert(res.msg);
			searchFn();
		}
	});
});

// 初始化表格
function inint(searchArgs) {
	$("#mateTable").bootstrapTable('destroy');
	$("#mateTable").bootstrapTable({
		onClickRow : function(row, $element, field) {
			changeRow = row;
		},
		url : '/itemInfo/itemPage',
		method : 'get',
		editable : true,
		contentType : "application/x-www-form-urlencoded",
		queryParamsType : searchArgs,
		queryParams : function queryParams(params) {
			var param = {
				pageNumber : params.pageNumber,
				pageSize : params.pageSize
			};
			for ( var key in searchArgs) {
				param[key] = searchArgs[key]
			}
			return param;
		},
		undefinedText : "",
		locale : 'zh-CN',
		pagination : true,
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20 ],
		sidePagination : "server",
		showRefresh : false,
		columns : [ {
			checkbox:true
		},{
			title : '序号',
			formatter : function(value, row, index) {
				return index + 1;
			},
			align : 'center',
			width : '10px',
			edit : false
		},{
			field : 'mateName',
			title : '物料名称',
			edit : false
		}, {
			field : 'mateCode',
			title : '物料编码',
			edit : false
		}, {
			field : 'mateGroupExpl',
			title : '物料组',
			edit : false
		}, {
			field : 'mateType',
			title : '物料类型',
			edit : false
		}, {
			field : 'basicUnit',
			title : '基本单位',
			edit : false
		}, {
			field : 'procUnit',
			title : '采购单位',
			edit : false
		}, {
			field : 'finMateId',
			title : '成品物料编码',
			edit : false
		}, {
			field : 'board',
			title : '机台',
			edit : {
				type : 'select',// 下拉框
				url : '/item/getBoardData',
				valueField : 'dicCode',
				textField : 'dicName',
				onSelect : function(val, rec) {
					// 更新项次信息
					$.ajax({
						url : "/item/saveItemInfo",
						data : {
							mateId : changeRow.mateId,
							board : rec.dicCode,
							itemCode : ''
						},
						success : function(res) {
							//alert(res.msg);
						}
					});
				}
			}
		}, {
			field : 'itemCode',
			title : '类型',
			width : '100px',
			edit : {
				type : 'select',// 下拉框
				url : '/item/getItemData',
				valueField : 'dicCode',
				textField : 'dicName',
				onSelect : function(val, rec) {
					// 更新项次信息
					$.ajax({
						url : "/item/saveItemInfo",
						data : {
							mateId : changeRow.mateId,
							itemCode : rec.dicCode,
							itemName : rec.dicName,
							board : ''
						},
						success : function(res) {
							//alert(res.msg);
						}
					});
				}
			}
		}/*,{
            field: 'operate',
            title: '操作',
            align: 'center',
            width : 80,
          //  events: operateEvents,
            formatter: operateFormatter,
			edit : false
            } */
		]
	})
}

/*window.operateEvents = {
    'click .RoleOfsave': function (e, value, row, index) {
    	alert(row.board);
    }
};*/

/**
 * bootstrapTable 操作
 * @param value
 * @param row
 * @param index
 * @returns
 */
/*function operateFormatter(value, row, index) {
   return [
   '<button type="button" class="RoleOfsave btn btn-primary  btn-sm">保存</button>'
   ].join('');
   
   return '<button type="button" class="RoleOfsave btn btn-primary  btn-sm" onclick="onlll('+row.board+')">保存</button>';

}*/

Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}
function timeFormatter(value) {
	if (value == null) {
		return "";
	} else if (value == "") {
		return "";
	} else {
		return new Date(value).Format("yyyy-MM-dd");
	}
}
