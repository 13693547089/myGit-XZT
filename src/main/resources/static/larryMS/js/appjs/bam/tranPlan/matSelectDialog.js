var table;
var checkedRow=[];
//地址前缀
var prefix = "/bam/tran";
layui.use('table', function(){
	table = layui.table;
	var $ = layui.$;
	// 加载数据
	loadData($);
	
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
	return checkedRow;
}

/**
 * 加载数据
 * @param jq
 * @returns
 */
function loadData(jq){
	// 编码或名称
	var matInfo = jq('#matInfo').val();
	// 系列
	var seriesExpl = jq('#seriesExpl').val();
	// 计划的年月
	var planYm = jq('#planYm').val();
	// 主表ID
	var mainId = jq('#mainId').val();
	
	var index = layer.load(0, {shade: false});
	jq.ajax({
		type:"post",
		url:prefix+"/getTranPlanDetailFromPadPlan",
		dataType:"JSON",
		data:{"matInfo":matInfo,"seriesExpl":seriesExpl,"planYm":planYm,"mainId":mainId},
		success:function(data){
			initMateTable(table,data);
			// 关闭加载层
			layer.close(index);
		},
		error:function (XMLHttpRequest, textStatus, errorThrown) {
			// 关闭加载层
			layer.close(index);
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
			field:"prodSeries",
			title:"系列",
			align:'center',
			width:110
		},{
			field:"matName",
			title:"物料名称",
			align:'center',
			width:230
		},{
			field:"matCode",
			title:"物料编码",
			align:'center',
			width:200
		}/*,{
			field:"procUnit",
			title:"单位",
			align:'center',
			width:91
		}*/
		]],
		id:'mateTableId'
	});
}






