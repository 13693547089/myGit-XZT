
//地址前缀
var prefix = "/bam/ps";
/*layui.use('table', function(){
	var table = layui.table;
	var $ = layui.$;
	
});


$("#searchBtn").click(function(){
});

//查询条件重置
$("#resetBtn").click(function(){
});*/

/**
 * 获取数据
 * @returns
 */
function getData(){
	var matInfo = $('#matInfo').val();
	var bigItemExpl = $('#bigItemExpl').val();
	var data = {"matInfo":matInfo,"bigItemExpl":bigItemExpl};
	return data;
}
