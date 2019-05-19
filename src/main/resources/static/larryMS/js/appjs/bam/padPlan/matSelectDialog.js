var table;
var checkedRow=[];
//地址前缀
var prefix = "/bam/ps";
layui.use('table', function(){
	table = layui.table;
	var $ = layui.$;
	// 加载数据
	loadData($);

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

	$("#searchBtn").click(function(){
		loadData($);
	});

	//查询条件重置
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
});

/**
 * 获取选中的数据
 * @returns
 */
function getCheckedData(){
	/*// 获取选中的物料数据
	var checkStatus = table.checkStatus('mateTableId');

	return checkStatus.data;*/
	
	return checkedRow;
}

/**
 * 加载数据
 * @param jq
 * @returns
 */
function loadData(jq){
	// 编码或名称
	var suppCodeName = jq('#suppInfo').val();
	// 系列
	var seriesExpl = jq('#seriesExpl').val();
	// 物料类型编码
	var matType = "0005";

	jq.ajax({
		type:"post",
		url:prefix+"/queryAllMaterialOfSupp",
		dataType:"JSON",
		data:{"matType":matType,"suppCodeName":suppCodeName,"seriesExpl":seriesExpl},
		success:function(data){
			initMateTable(table,data);
		}
	});
}

function initMateTable(table,data){
	var mateTable = table.render({
		elem:"#mateTable",
		data:data,
		page:true,
		width: '100%',
		minHeight:30,
		limit:200,
		limits:[200,300,500,1000],
		cols:[[{
			checkbox:true
		},{
			field:"sn",
			title:"序号",
			align:'center',
			type:'numbers',
			width:60
		},{
			field:"seriesExpl",
			title:"系列",
			align:'center',
			width:110
		},{
			field:"mateName",
			title:"物料名称",
			align:'center',
			width:230
		},{
			field:"mateCode",
			title:"物料编码",
			align:'center',
			width:200
		},{
			field:"procUnit",
			title:"单位",
			align:'center',
			width:91
		}
		]],
		id:'mateTableId'
	});
}






