layui.use([ 'form', 'table','layer'], function() {
	var $=layui.jquery;
	var prefix="/invenPlan";
	var table=layui.table;
	var layer=layui.layer;
	var tablelns;
	var codes=$("#selectItemCodes").val();
	var selectItemCodes=JSON.parse(codes);
	//加载页面初始化表格
	loadTable();
	//查询按钮点击事件
	$("#searchBtn").click(function(){
		loadTable();
	});
	//确认
	$("#confirmBtn").click(function(){
		//调用父窗口的方法
		closeDg();
		parent.updateSearchCond('selectItemCodes',JSON.stringify(selectItemCodes));
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
		var itemDesc=$("#itemDesc").val();
		var planCode=$("#planCode").val();
		$.ajax({
			url:prefix+"/getItemInfo",
			type:"post",
			data:{planMonth:planMonth,itemDesc:itemDesc,planCode:planCode},
			success:function(res){
				initItemTable(res);
			}
		});
	}
	/**
	 * 初始化排产计划详情表
	 * @param condition
	 * @returns
	 */
	function initItemTable(data){
		$.each(data,function(index,row){
			$.each(selectItemCodes,function(index1,selectItemCode){
				if(row.dicCode==selectItemCode){
					row.LAY_CHECKED=true;
				}
			});
		});
		tablelns=table.render({
			elem : '#itemTable',
			id:'itemTable',
			data:data,
			page:true,
			limit:20,
			cols : [ [ {
				type : 'checkbox',
			},{
				field : 'dicCode',
				title : '品项编码',
			},{
				field : 'dicName',
				title : '品项',
			} ] ]
		});
	}
	/**
	 * 表格的选择监听事件
	 */
	table.on('checkbox(itemTable)', function(obj){
		var row=obj.data;
		var type=obj.type;
		//单选
		if(type=='one'){
			if(obj.checked){
				selectItemCodes.push(row.dicCode);
			}else{
				selectItemCodes.splice($.inArray(row.dicCode,selectItemCodes),1);
			}
		}else{
			var data=table.cache.itemTable;
			if(obj.checked){
				for(var i=0;i<data.length;i++){
					var dicCode=data[i].dicCode;
					if(selectItemCodes.indexOf(dicCode)==-1){
						selectItemCodes.push(dicCode);
					}
				}
			}else{
				for(var i=0;i<data.length;i++){
					var dicCode=data[i].dicCode;
					if(selectItemCodes.indexOf(dicCode)!=-1){
						selectItemCodes.splice($.inArray(dicCode,selectItemCodes),1);
					}
				}
			}
		}
	});
})
