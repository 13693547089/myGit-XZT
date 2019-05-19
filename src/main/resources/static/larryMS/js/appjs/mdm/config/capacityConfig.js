
//定义树形控件
layui.use([ 'table'], function() {
	var $ = layui.$;
	var table = layui.table;
	initListTable("/itemInfo/capacityPage");
	//初始化表格
	function initListTable(url){
	   table.render({
		     elem: '#list-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:url
		    //,even: true
		    ,limit:20
		    ,cols: [[
		       {checkbox:true}
			  ,{field:'sn', title: '序号',width:'5%',type:'numbers'}
			  ,{field:'mateName', title: '物料名称'}
		      ,{field:'mateCode', title: '物料编码',width:'10%'}
		      ,{field:'mateGroupExpl', title: '物料组',width:'15%'}
		      ,{field:'mateType', title: '物料类别',width:'10%'}
		      ,{field:'basicUnit', title: '基本单位',width:'7%'}
		      ,{field:'procUnit', title: '采购单位',width:'7%'}
		      ,{field:'finMateId', title: '成品物料编码',width:'10%'}
		      ,{field:'boardName', title: '机台',width:'7%'}
		      ,{field:'itemName', title: '类型',width:'7%'}
		    ]]
		  ,page:true
	  });
	}
	
	function reloadTable(){
		var matInfo=$("#search_matInfo").val();
		var matType=$("#search_matType").val();
		
		var url="/itemInfo/capacityPage?matInfo="+matInfo+"&matType="+matType;
		initListTable(url);
	}
	
	// 批量修改
	$("#batchBtn").click(function(){
		batchUpdate();
	});
	
	// 查询事件
	$("#searchBtn").click(function(){
		reloadTable();
	});

	//查询条件重置
	$("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	});
	
	/**
	 * 批量更新物料的信息
	 * @returns
	 */
	function batchUpdate(){
		var rows = table.checkStatus('list-table').data;
		if(rows.length == 0){
			layer.msg("请选择需要修改的物料！",{time:1000});
			return;
		}
		
		layer.open({
			  type:2,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
			  title:"批量修改",
			  shadeClose : true,
			  shade : false,
			  content : '/itemInfo/updateDialog',
			  area : [ '500px', '50%' ],
			  maxmin : true, // 开启最大化最小化按钮
			  btn: ['确认', '取消']
		  	  ,yes: function(index, layero){
		  		//按钮【按钮一】的回调
		  		
		  		// 获取选中的物料数据
		  		var data = $(layero).find("iframe")[0].contentWindow.getData();
		  		// 关闭弹框
		  		layer.close(index);
		  		// 处理数据
		  		dialogDataDeal(data,rows);
			  }
			  ,btn2: function(index, layero){
				  //按钮【按钮二】的回调
				  // 默认会关闭弹框
				  //return false 开启该代码可禁止点击该按钮关闭
			  }
		});
	}
	
	/**
	 * 处理
	 * @param data
	 * @param selectRows
	 * @returns
	 */
	function dialogDataDeal(data,selectRows){
		
		var matData = [];
		for(var i=0;i<selectRows.length;i++){
			var item = selectRows[i];
			var obj = {
					"mateId":item.mateId,
					"itemCode":data.itemCode,
					"itemName":data.itemName,
					"board":data.board,
					"boardName":data.boardName
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
				if(res.code == 0){
					layer.msg("修改成功！");
					reloadTable();
				}else{
					layer.msg(res.msg);
				}
			}
		});
	}
});
