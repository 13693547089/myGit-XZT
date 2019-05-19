var table;
//地址前缀
var prefix = "/bam/pdr";
layui.use('table', function(){
	table = layui.table;
	var $ = layui.$;
	// 加载数据
	loadData($);
	
	//监听表格复选框选择
	table.on('checkbox(demo)', function(obj){
		/*var type = obj.type;
		var checked = obj.checked;
		var data = obj.data;
		if (data.length == 1) {
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
		}else{
			layer.msg("只能选择一个供应商！",{time:2000});
			return;
		}*/

		var checkStatus = table.checkStatus('suppTable');
		if(checkStatus.data.length>1){
			layer.msg("只能选择一个供应商！",{time:2000});
			return;
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
	// 获取选中的数据
	var checkStatus = table.checkStatus('suppTable');

	return checkStatus.data;
}

/**
 * 加载数据
 * @param jq
 * @returns
 */
function loadData(jq){
	// 编码或名称
	var suppInfo = jq('#suppInfo').val();
	if(suppInfo==null || suppInfo == undefined){
		suppInfo = "";
	}

	table.render({
		elem : '#suppTable',
		page : true,
		url : prefix+'/queryAllQualSuppOfUser?suppInfo='+suppInfo,
		width: '100%',
		limit: 100, //显示的数量
		limits: [100,200,1000], //显示的数量
		even: true,//隔行变色
		cols : [ [ 
		{checkbox:true},
		{
			field : 'suppName',
			title : '供应商名称',
			align : 'center',
			width : 300
		}, {
			field : 'sapId',
			title : '供应商编码',
			align : 'center',
			width : 150
		} , {
			field : 'username',
			title : '采购员',
			align : 'center',
			width : 120
		} ] ],
		id : 'suppTable'
	});
}