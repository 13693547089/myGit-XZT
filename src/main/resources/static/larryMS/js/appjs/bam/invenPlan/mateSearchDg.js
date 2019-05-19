layui.use([ 'form', 'table','layer'], function() {
	var $=layui.jquery;
	var prefix="/invenPlan";
	var table=layui.table;
	var layer=layui.layer;
	var tablelns;
	var codes=$("#selectMateCodes").val();
	var selectMateCodes=JSON.parse(codes);
	//加载页面初始化表格
	loadTable();
	//查询按钮点击事件
	$("#searchBtn").click(function(){
		loadTable();
	});
	//确认
	$("#confirmBtn").click(function(){
		//调用父窗口的方法
		var data = table.checkStatus('invenPlanTable').data;
		closeDg();
		parent.updateSearchCond('mateDesc',JSON.stringify(selectMateCodes));
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
		var mateDesc=$("#mateDesc").val();
		var planCode=$("#planCode").val();

		$.ajax({
			url:prefix+"/getMateSelectInfo",
			type:"post",
			data:{planMonth:planMonth,mateDesc:mateDesc,planCode:planCode},
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
			$.each(selectMateCodes,function(index1,selectMateCode){
				if(row.mateCode==selectMateCode){
					row.LAY_CHECKED=true;
				}
			});
		});
		tablelns=table.render({
			elem : '#mateTable',
			id:'mateTable',
			data:data,
			page:true,
			limit:20,
			cols : [ [ {
				type : 'checkbox',
			},{
				field : 'mateCode',
				title : '物料编码',
			},{
				field : 'mateName',
				title : '物料名称',
			} ] ]
		});
	}
	/**
	 * 表格的选择监听事件
	 */
	table.on('checkbox(mateTable)', function(obj){
		var row=obj.data;
		var type=obj.type;
		//单选
		if(type=='one'){
			if(obj.checked){
				selectMateCodes.push(row.mateCode);
			}else{
				selectMateCodes.splice($.inArray(row.mateCode,selectMateCodes),1);
			}
		}else{
			var data=table.cache.mateTable;
			if(obj.checked){
				for(var i=0;i<data.length;i++){
					var mateCode=data[i].mateCode;
					if(selectMateCodes.indexOf(mateCode)==-1){
						selectMateCodes.push(mateCode);
					}
				}
			}else{
				for(var i=0;i<data.length;i++){
					var mateCode=data[i].mateCode;
					if(selectMateCodes.indexOf(mateCode)!=-1){
						selectMateCodes.splice($.inArray(mateCode,selectMateCodes),1);
					}
				}
			}
		}
	});
})
