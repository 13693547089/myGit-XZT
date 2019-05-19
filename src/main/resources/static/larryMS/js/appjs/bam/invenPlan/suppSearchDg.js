layui.use([ 'form', 'table','layer'], function() {
	var $=layui.jquery;
	var prefix="/invenPlan";
	var table=layui.table;
	var layer=layui.layer;
	var tablelns;
	var codes=$("#selectSuppCodes").val();
	var selectSuppCodes=JSON.parse(codes);
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
		parent.updateSearchCond('selectSuppCodes',JSON.stringify(selectSuppCodes));
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
		var suppName=$("#suppName").val();
		var planCode=$("#planCode").val();

		$.ajax({
			url:prefix+"/getSuppList",
			type:"post",
			data:{planMonth:planMonth,suppName:suppName,planCode:planCode},
			success:function(res){
				initSuppTable(res);
			}
		});
	}
	/**
	 * 初始化排产计划详情表
	 * @param condition
	 * @returns
	 */
	function initSuppTable(data){
		$.each(data,function(index,row){
			$.each(selectSuppCodes,function(index1,selectSuppCode){
				if(row.sapId==selectSuppCode){
					row.LAY_CHECKED=true;
				}
			});
		});
		tablelns=table.render({
			elem : '#suppTable',
			id:'suppTable',
			data:data,
			page:true,
			limit:20,
			cols : [ [ {
				type : 'checkbox',
			},{
				field : 'sapId',
				title : '供应商编码',
			},{
				field : 'suppName',
				title : '供应商名称',
			} ] ]
		});
	}
	/**
	 * 表格的选择监听事件
	 */
	table.on('checkbox(suppTable)', function(obj){
		var row=obj.data;
		var type=obj.type;
		//单选
		if(type=='one'){
			if(obj.checked){
				selectSuppCodes.push(row.sapId);
			}else{
				selectSuppCodes.splice($.inArray(row.sapId,selectSuppCodes),1);
			}
		}else{
			var data=table.cache.mateTable;
			if(obj.checked){
				for(var i=0;i<data.length;i++){
					var sapId=data[i].sapId;
					if(selectSuppCodes.indexOf(sapId)==-1){
						selectSuppCodes.push(sapId);
					}
				}
			}else{
				for(var i=0;i<data.length;i++){
					var sapId=data[i].sapId;
					if(selectSuppCodes.indexOf(sapId)!=-1){
						selectSuppCodes.splice($.inArray(sapId,selectSuppCodes),1);
					}
				}
			}
		}
	});
})
