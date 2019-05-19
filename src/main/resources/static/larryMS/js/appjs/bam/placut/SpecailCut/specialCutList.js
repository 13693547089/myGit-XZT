var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  var id ='';
	  initCutLiaisonTable();
	  //特殊打切录入 specialBut
	  $('#specialBut').click(function(){
		  var url ="/getSpecialCutAddHtml?type=1"
		   //location=url;
		   tuoGo(url,'specialCutAdd',"cutLiaisonTableId");  
	  })
	  //监听工具条
	  table.on('tool(cutLiaisonTable)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
	    	var url  ="/getSpecialCutAddHtml?liaiId="+data.liaiId+"&type=3";
    		//location=url;
	    	tuoGo(url,'specialCutAdd','cutLiaisonTableId'); 
	    } else if(obj.event === 'del'){//删除
	       if(data.status=='已保存'){
		      layer.confirm('真的删除这个打切联络单吗？', function(index){
		    	  var liaiIds =[];
		    	  liaiIds.push(data.liaiId);
		    	  $.ajax({
		    		 type:"post",
		    		 url:"/deleteSpeCutLiaisonByliaiIds",
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
	    	   layer.alert('<span style="color:red;">只有"已保存"状态的打切联络单才能被删除</sapn>');
	       }
	    } else if(obj.event === 'edit'){//编辑
	    	if(data.status=="已保存"){
	    		var url  ="/getSpecialCutAddHtml?liaiId="+data.liaiId+"&type=2";
	    		//location=url;
	    		tuoGo(url,'specialCutAdd','cutLiaisonTableId'); 
	    	}else{
	    		layer.alert('<span style="color:red;">只有"已保存"状态的预约申请可以编辑</sapn>');
	    	}
	    }
	  });
	    // 条件搜索
		var $ = layui.$, active = {
			reload : function() {
				var cutMonth = $('#cutMonth');
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
	  //删除
	  $("#removeBut").click(function(){
		  var table = layui.table;
	  		var checkStatus = table.checkStatus('cutLiaisonTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 0){
	  			  var liaiIds = [];
	  			  var a=0;
	  			  for(var i=0;i<length;i++){
	  				  liaiIds[i]=data[i].liaiId;
	  				  if(data[i].status !='已保存'){
	  					  a++;
	  				  }
	  			  }
	  			  if(a == 0){
	  				  layer.confirm('真的删除选中的打切联络单吗？', function(index){
	  				     $.ajax({
	  				    	 type:"post",
	  				    	 url:"/deleteSpeCutLiaisonByliaiIds",
	  				    	 data:"liaiIds="+liaiIds,
	  				    	 dataType:"JSON",
	  				    	 success:function(data2){
	  				    		 if(data2){
	  			    				 layer.msg('删除成功', {time:2000 });
	  			    				 initCutLiaisonTable();
	  			    			 }else{
	  			    				 layer.alert('<span style="color:red;">删除失败</sapn>');
	  			    			 }
	  				    	 },
	  				      	 error:function(){
	  				      		layer.msg('程序出错', {time:2000 });
	  				      	 }
	  				      });
	  				      layer.close(index);
	  				  });
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"已保存"状态的打切联络单才能被删除</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条或多条数据进行删除</sapn>');	
	  		  }
	  });
	  
	  //提交
	  $("#submitBut").click(function(){
		  var table = layui.table;
	  		var checkStatus = table.checkStatus('cutLiaisonTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 0){
	  			  var liaiIds = [];
	  			  var a=0;
	  			  for(var i=0;i<length;i++){
	  				  liaiIds[i]=data[i].liaiId;
	  				  if(data[i].status !='已保存'){
	  					  a++;
	  				  }
	  			  }
	  			  if(a == 0){
	  				  layer.confirm('真的提交选中的打切联络单吗？', function(index){
	  				     var liaiIdsJson =JSON.stringify(liaiIds);
	  					  $.ajax({
	  				    	 type:"post",
	  				    	 url:"/updateSpeCutLiaiStatusByliaiIds?liaiIds="+liaiIdsJson,
	  				    	 dataType:"JSON",
	  				    	 success:function(data2){
	  				    		 if(data2){
	  			    				 layer.msg('提交成功', {time:2000 });
	  			    				 initCutLiaisonTable();
	  			    			 }else{
	  			    				 layer.alert('<span style="color:red;">提交失败</sapn>');
	  			    			 }
	  				    	 },
	  				      	 error:function(){
	  				      		layer.msg('程序出错', {time:2000 });
	  				      	 }
	  				      });
	  				      layer.close(index);
	  				  });
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"已保存"状态的打切联络单才能提交</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条或多条数据进行提交</sapn>');	
	  		  }
	  });
	 $("#cancelConfirmBut").click(function(){
		    var table = layui.table;
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
	  				  layer.confirm('确认要取消选中的打切联络单吗？', function(index){
	  				     var liaiIdsJson =JSON.stringify(liaiIds);
	  					  $.ajax({
	  				    	 type:"post",
	  				    	 url:"/cancleSpeCutLiaiByliaiIds?liaiIds="+liaiIdsJson,
	  				    	 dataType:"JSON",
	  				    	 success:function(data2){
	  				    		 if(data2){
	  			    				 layer.msg('取消确认成功', {time:2000 });
	  			    				 initCutLiaisonTable();
	  			    			 }else{
	  			    				 layer.alert('<span style="color:red;">取消确认失败</sapn>');
	  			    			 }
	  				    	 },
	  				      	 error:function(){
	  				      		layer.msg('程序出错', {time:2000 });
	  				      	 }
	  				      });
	  				      layer.close(index);
	  				  });
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"已确认"状态的打切联络单才能取消确认</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条或多条数据进行取消确认</sapn>');	
	  		  }
	 });
	  
	  $('.demoTable .layui-btn').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
	  });
});

//初始化table
function initCutLiaisonTable() {
	table.render({
		elem : "#cutLiaisonTable",
		url : "/querySpeCutLiaisonByPage",
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
					width : 117
				},
				{
					field : 'isSpecial',
					align : 'center',
					title : '打切类型',
					width : 110,
					templet:function(d){
						var isSpecial = d.isSpecial;
						if("YES"==isSpecial){
							return "特殊打切";
						}else{
							return "";
						}
					}
				},
				{
					field : 'creator',
					align : 'center',
					title : '创建人',
					width : 179
				},
				{
					field : 'createDate',
					align : 'center',
					title : '创建时间',
					width : 113,
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