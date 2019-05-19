layui.use([ 'form', 'table','layer'], function() {
	var $=layui.jquery;
	var prefix="/invenPlan";
	var table=layui.table;
	var layer=layui.layer;
	var tablelns;
	var codes=$("#selectSeriesCodes").val();
	var selectSeriesCodes=JSON.parse(codes);
	//加载页面初始化表格
	loadTable();
	//查询按钮点击事件
	$("#searchBtn").click(function(){
		loadTable();
	});
	//确认
	$("#confirmBtn").click(function(){
		closeDg();
		parent.updateSearchCond('selectSeriesCodes',JSON.stringify(selectSeriesCodes));
	});
	//取消按钮点击事件
	$("#cancleBtn").click(function(){
		closeDg();
	});
	/**
	 * layer关闭子表窗口
	 * @returns
	 */
	function closeDg(){
		var index = parent.layer.getFrameIndex(window.name);//获取子窗口索引
		 parent.layer.close(index);
	}
	/**
	 * 交货计划表格数据的加载
	 * @returns
	 */
	function loadTable(){
		var planMonth=$("#planMonth").val();
		var seriesDesc=$("#seriesDesc").val();
		var planCode=$("#planCode").val();

		$.ajax({
			url:prefix+"/getSelectProdSeriers",
			type:"post",
			data:{planMonth:planMonth,seriesDesc:seriesDesc,planCode:planCode},
			success:function(res){
				initMateTable(res);
			}
		});
	}
	/**
	 * 初始化排产计划详情表
	 * @param condition
	 * @returns
	 */
	function initMateTable(data){
		$.each(data,function(index,row){
			$.each(selectSeriesCodes,function(index1,selectSeriesCode){
				if(row.code==selectSeriesCode){
					row.LAY_CHECKED=true;
				}
			});
		});
		tablelns=table.render({
			elem : '#seriesTable',
			id:'seriesTable',
			data:data,
			page:true,
			limit:20,
			cols : [ [ {
				type : 'checkbox',
			},{
				field : 'code',
				title : '系列编码',
			},{
				field : 'name',
				title : '产品系列',
			} ] ]
		});
	}
	/**
	 * 表格的选择监听事件
	 */
	table.on('checkbox(seriesTable)', function(obj){
		debugger;
		var row=obj.data;
		var type=obj.type;
		//单选
		if(type=='one'){
			if(obj.checked){
				selectSeriesCodes.push(row.code);
			}else{
				selectSeriesCodes.splice($.inArray(row.code,selectSeriesCodes),1);
			}
		}else{
			var data=table.cache.seriesTable;
			if(obj.checked){
				for(var i=0;i<data.length;i++){
					var code=data[i].code;
					if(selectSeriesCodes.indexOf(code)==-1){
						selectSeriesCodes.push(code);
					}
				}
			}else{
				for(var i=0;i<data.length;i++){
					var code=data[i].code;
					if(selectSeriesCodes.indexOf(code)!=-1){
						selectSeriesCodes.splice($.inArray(code,selectSeriesCodes),1);
					}
				}
			}
		}
	});
})
