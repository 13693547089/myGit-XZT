var table;
var tableData;
var checkedRow=[];
//地址前缀
var prefix = "/bam/ps";

var index = parent.layer.getFrameIndex(window.name);
var parentData = parent.data;
debugger;
var matCode = parentData.matCode;
var matName = parentData.matName;
var prodSeries = parentData.prodSeries;
var bigItemExpl = parentData.bigItemExpl;
layui.use('table', function(){
	table = layui.table;
	var $ = layui.$;
	$("#mateInof").val(matCode + " " +matName);
	$("#prodSeries").val(prodSeries);
	$("#bigItemExpl").val(bigItemExpl);
	// 加载数据
	loadData();
	//监听单元格事件
	/*table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    console.info(data.suppId)
	    if(obj.event === 'setSign'){
	     var url ="/getCheckQualSuppHtml?suppId="+data.suppId;
	     location=url;
	    }
	  });*/

	/*//确定
		$("#confirm").click(function(){
			// 获取选中的物料数据
			var checkStatus = table.checkStatus('mateTableId');
			checkedData = checkStatus.data;

			var index=parent.layer.getFrameIndex(window.name);
			parent.dialogDataDeal(checkedData);
			parent.layer.close(index);
		});*/

	//监听表格复选框选择
	table.on('checkbox(demo)', function(obj){
		var type = obj.type;
		var checked = obj.checked;
		var data = obj.data;
		if (type == 'all') {
			var cacheData = table.cache.mateTableId;
			if (checked) {
				var tempData = checkedRow;
				for ( var k = 0; k < cacheData.length; k++) {
					var elem = cacheData[k];
					if (tempData.indexOf(elem) == -1)
						checkedRow.push(elem);
				}
			} else {
				var tempData = [];
				for ( var k = 0; k < checkedRow.length; k++) {
					var elem = checkedRow[k];
					if (cacheData.indexOf(elem) == -1)
						tempData.push(elem);
				}
				checkedRow = tempData;
			}
		} else if (type = 'one') {
			if (checked) {
				checkedRow.push(data);
			} else {
				var tempData = [];
				for ( var i = 0; i < checkedRow.length; i++) {
					var elem = checkedRow[i];
					if (elem.mateId != data.mateId)
						tempData.push(elem);
				}
				checkedRow = tempData;
			}
		}
	});

	
});

/**
 * 获取选中的数据
 * @returns
 */
function getCheckedData(){
	// 获取选中的物料数据
//	var checkStatus = table.checkStatus('planMessTableId');

//	return checkStatus.data;
	return tableData;
	
//	return checkedRow;
}

/**
 * 加载数据
 * @param jq
 * @returns
 */
function loadData(){
	$.ajax({
		type:"get",
		url:prefix+"/getPlanMessageListOfMateByMatCode",
		dataType:"JSON",
		data:{
			matCode:matCode
		},
		success:function(data){
			tableData = data;
			initMateTable(table,tableData);
		}
	});
}

function initMateTable(table,data){
	var mateTable = table.render({
		elem:"#planMessTable",
		data:data,
		width: '100%',
		Height:400,
		cols:[[{
			checkbox:true
		},{
			field:"planMonth",
			title:"计划月份",
			align:'center',
			width:110
		},{
			field:"padPlanQty",
			title:"交货计划",
			align:'center',
			edit:'number',
			width:230
		},{
			field:"turnOverDays",
			title:"周转天数",
			align:'center',
			width:200
		}
		]],
		id:'planMessTableId'
	});
}






