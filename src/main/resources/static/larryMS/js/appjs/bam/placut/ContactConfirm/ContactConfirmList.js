var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initCutLiaisonTable();
	  //监听工具条
	  table.on('tool(cutLiaisonTable)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
	    	var url  ="/getContactConfirmDetailHtml?liaiId="+data.liaiId+"&type=2";
    		//location=url;
	    	tuoGo(url,'ContactConfirmDetail',"cutLiaisonTableId");
	    } else if(obj.event === 'del'){//删除
	       if(data.status!='已确认'){
		      layer.confirm('真的删除这个打切联络单吗？', function(index){
		    	  var liaiIds =[];
		    	  liaiIds.push(data.liaiId);
		    	  $.ajax({
		    		 type:"post",
		    		 url:"/deleteCutLiaisonByliaiIds",
		    		 data:"liaiIds="+liaiIds,
		    		 dataType:"JSON",
		    		 success:function(data2){
		    			 if(data2){
		    				 layer.msg('删除成功', {time:2000 });
		    				 initCutLiaisonTable();
		    			 }else{
		    				 layer.alert('<span style="color:red;">删除失败</sapn>');
		    			 }
		    		 }
		    	  });
		    	  layer.close(index);
		      });
	       }else{
	    	   layer.alert('<span style="color:red;">只有"已确认"状态的打切联络单不能被删除</sapn>');
	       }
	    } else if(obj.event === 'affirm'){//确认
	    	if(data.status=="已提交"){
	    		var url  ="/getContactConfirmDetailHtml?liaiId="+data.liaiId+"&type=1";
	    		//location=url;
	    		tuoGo(url,'ContactConfirmDetail',"cutLiaisonTableId");
	    	}else{
	    		layer.alert('<span style="color:red;">只有"已提交"状态的打切联络单才可以确认</sapn>');
	    	}
	    }
	  });
	    // 条件搜索
		var $ = layui.$, active = {
			reload : function() {
				var cutMonth = $('#cutMonth');
				var suppInfo = $("#suppInfo");
				var status = $("#status");
				var liaiCode = $("#liaiCode");
				// 执行重载
				table.reload('cutLiaisonTableId', {
					page : {
						curr : 1
					// 重新从第 1 页开始
					},
					where : {
						cutMonth : cutMonth.val(),
						suppInfo : suppInfo.val(),
						status : status.val(),
						liaiCode : liaiCode.val()
					}
				});
			}
		};
	  //年月选择器
	  laydate.render({
	    elem: '#cutMonth'
	    ,type: 'month'
	  });
	  //确认
	  $("#ConfirmBut").click(function(){
		    var table = layui.table;
	  		var checkStatus = table.checkStatus('cutLiaisonTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 0){
	  			  var liaiIds = [];
	  			  var a=0;
	  			  for(var i=0;i<length;i++){
	  				  liaiIds[i]=data[i].liaiId;
	  				  if(data[i].status !='已提交'){
	  					  a++;
	  				  }
	  			  }
	  			  if(a == 0){
	  				  layer.confirm('对选中的打切联络单进行确认吗？', function(index){
	  					  for(var i=0;i<length;i++){
	  						  liaiIds=[];
	  						  var liaiId = data[i].liaiId;
	  						  liaiIds.push(liaiId);
	  						  var liaiIdJson = JSON.stringify(liaiIds);
	  						  var suppName = data[i].suppName;
	  						  var createDate = data[i].createDate;
	  						  //var format = new Date(createDate).Format("yyyy-MM-dd");
	  						  var remark = "打切联络单审核: "+suppName;
		  						var flag = taskProcess(liaiId, "cutLiaiAudit", remark, "process");
		  						if(flag=="over_success"){
		  							$.ajax({
		  								type:"post",
		  								url:"/updateStatusOfCutLiaisonByLiaiId?liaiIdJson="+liaiIdJson,
		  								dataType:"JSON",
		  								success:function(data2){
		  									if(data2){
		  										layer.msg('确认成功', {time:2000 });
		  										initCutLiaisonTable();
		  									}else{
		  										layer.alert('<span style="color:red;">确认失败</sapn>');
		  									}
		  								},
		  								error:function(){
		  									layer.msg('程序出错', {time:2000 });
		  								}
		  							});
		  						}
	  					  }
	  				      layer.close(index);
	  				  });
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"已提交"状态的打切联络单才能确认</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条或多条数据进行确认</sapn>');	
	  		  }
	  });
	  //退回
	  $("#ReturnBut").click(function(){
		  var table = layui.table;
		  var checkStatus = table.checkStatus('cutLiaisonTableId');
		  var data = checkStatus.data;
		  var length = data.length;
		  if(length != 0){
			  var liaiIds = [];
			  var a=0;
			  for(var i=0;i<length;i++){
				  liaiIds[i]=data[i].liaiId;
				  if(data[i].status !='已提交'){
					  a++;
				  }
			  }
			  if(a == 0){
				  layer.confirm('对选中的打切联络单进行退回吗？', function(index){
					  for(var i=0;i<length;i++){
  						  liaiIds=[];
  						  var liaiId = data[i].liaiId;
  						  liaiIds.push(liaiId);
  						  var liaiIdJson = JSON.stringify(liaiIds);
  						  var result = backProcess(liaiId);
	  						if(result=="back_success"){
	  							$.ajax({
	  							  type:"post",
	  							  url:"/updateStatusOfCutLiaisonByLiaiId2?liaiIdJson="+liaiIdJson,
	  							  dataType:"JSON",
	  							  success:function(data2){
	  								  if(data2){
	  									  layer.msg('退回成功', {time:2000 });
	  									  initCutLiaisonTable();
	  								  }else{
	  									  layer.alert('<span style="color:red;">退回失败</sapn>');
	  								  }
	  							  },
	  							  error:function(){
	  								  layer.msg('程序出错', {time:2000 });
	  							  }
	  						  });
	  						}
  					  }
					  layer.close(index);
				  });
			  }else{
				  layer.alert('<span style="color:red;">只有"已提交"状态的打切联络单才能退回</sapn>');	 
			  }
		  }else{
			  layer.alert('<span style="color:red;">请选择一条或多条数据进行退回</sapn>');	
		  }
	  });
	  //导出
	  /*$("#ExportBut").click(function(){
		  var table = layui.table;
		  var checkStatus = table.checkStatus('cutLiaisonTableId');
		  var data = checkStatus.data;
		  var length = data.length;
		  if(length == 1){
			  if(data[0].status == '已确认'){
				  layer.confirm('确定要导出选中的打切联络单吗？', function(index){
					  location="/exportCutLiaison?liaiId="+data[0].liaiId;
					  layer.close(index);
				  });
			  }else{
				  layer.alert('<span style="color:red;">只有"已确认"状态的打切联络单才能导出</sapn>');	 
			  }
		  }else{
			  layer.alert('<span style="color:red;">请选择一条数据进行导出</sapn>');	
		  }
	  });*/
	  $('.demoTable .layui-btn').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
	  });
	  
	 $("#cancellBut").click(function(){
		    var checkStatus = table.checkStatus('cutLiaisonTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 0){
	  			  var liaiIds = [];
	  			  var a=0;
	  			  for(var i=0;i<length;i++){
	  				  liaiIds[i]=data[i].liaiId;
	  				  if(data[i].status !='已确认'){
	  					  a++;
	  				  }
	  			  }
	  			  if(a == 0){
	  				  layer.confirm('对选中的打切联络单进行作废吗？', function(index){
  						    var liaiIdJson = JSON.stringify(liaiIds);
							$.ajax({
								type:"post",
								url:"/cancellCutLiaisonByLiaiIds?liaiIdJson="+liaiIdJson,
								dataType:"JSON",
								success:function(data2){
									if(data2){
										layer.msg('作废成功', {time:2000 });
										table.reload("cutLiaisonTableId");
									}else{
										layer.alert('<span style="color:red;">作废失败</sapn>');
									}
								},
								error:function(){
									layer.msg('程序出错', {time:2000 });
								}
							});
	  				       layer.close(index);
	  				  });
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"已确认"状态的打切联络单才能作废</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条或多条数据进行作废</sapn>');	
	  		  }
	 });
	  
});

//初始化table
function initCutLiaisonTable() {
	table.render({
		elem : "#cutLiaisonTable",
		url : "/queryCutLiaisonForManageByPage",
		page : true,
		width : '100%',
		minHeight : '20px',
		limit : 10,
		id : "cutLiaisonTableId",
		cols : [ [
				{
					checkbox : true,
				},
				{
					title : '序号',
					type : 'numbers'
				},
				{
					field : 'status',
					title : '状态',
					align : 'center',
					width : 92
				},
				{
					field : 'cutMonth',
					title : '打切月份',
					align : 'center',
					width : 99
				},
				{
					field : 'liaiCode',
					align : 'center',
					title : '打切联络单号',
					width : 127
				},
				{
					field : 'suppId',
					align : 'center',
					title : '供应商编码',
					width : 199
				},
				{
					field : 'suppName',
					align : 'center',
					title : '供应商',
					width : 271
				},
				{
					field : 'creator',
					align : 'center',
					title : '创建人',
					width : 120
				},
				{
					field : 'createDate',
					align : 'center',
					title : '创建时间',
					width : 147,
					templet : function(d) {
						var date = new Date(d.createDate);
						var year = date.getFullYear();
						var month = date.getMonth() + 1;
						var day = date.getDate();
						return year + "-" + (month < 10 ? "0" + month : month)
								+ "-" + (day < 10 ? "0" + day : day);
					}
				}, {
					fixed : 'right',
					title : '操作',
					width : 160,
					align : 'center',
					toolbar : '#barDemo'
				} ] ]
	})
}