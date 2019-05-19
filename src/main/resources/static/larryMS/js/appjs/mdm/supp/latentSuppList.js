layui.use('table', function(){
	  var table = layui.table;
	  var $ = layui.$;
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	    //console.log(obj)
	  });
	  //监听工具条
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
		      var url  ="/updateLatentSupp?suppId="+data.suppId+"&type=1&editType=0";
			  if(data.status=="未审核" || data.status=="不合格"){
				  url  ="/updateLatentSupp?suppId="+data.suppId+"&type=1&editType=1";
		      }
			  //location=url;
			  tuoGo(url,'latentSuppEdit',"latentSuppTableId");
	    } else if(obj.event === 'del'){//删除
	      // console.info(data.suppId);
	       if(data.status=='不合格' || data.status=='未审核'){
		      layer.confirm('真的删除这个供应商吗？', function(index){
		    	  var suppIds =[];
		    	  suppIds.push(data.suppId);
		    	  $.ajax({
		    		 type:"post",
		    		 url:"/deleteLatentSuppBySuppId",
		    		 data:"suppIds="+suppIds,
		    		 dataType:"JSON",
		    		 success:function(data2){
		    			 //console.info(data2);
		    			 if(data2){
		    				 layer.msg('删除成功', {time:2000 });
		    				 table.reload('latentSuppTableId');
		    			 }else{
		    				 layer.alert('<span style="color:red;">删除失败</sapn>');
		    			 }
		    		 }
		    	  });
		        layer.close(index);
		      });
	       }else{
	    	   layer.alert('<span style="color:red;">只有"不合格","未审核"的供应商才能删除</sapn>');
	       }
	    } else if(obj.event === 'edit'){//编辑
	    	if(data.status!="已提交OA" && data.status!="OA审核" && data.status!="合格" && data.status!="已初审" ){
	    		var url  ="/updateLatentSupp?suppId="+data.suppId+"&type=2&editType=0";
	    		if(data.status=='不合格'){
	    			url = "/updateLatentSupp?suppId="+data.suppId+"&type=3&editType=0";
	    		}
	    		//location=url;
	    		tuoGo(url,'latentSuppEdit',"latentSuppTableId");
	    	}else{
	    		layer.alert('<span style="color:red;">不能编辑'+data.status+'的供应商</sapn>');
	    	}
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      var suppInfo = $('#suppInfo');
			      var category = $("#category");
			      var status = $("#status");
			      //执行重载
			      table.reload('latentSuppTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  suppInfo: suppInfo.val(),
			        	  category:category.val(),
			        	  status:status.val()
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  
	  
	//新建
	  $("#addLatentSupp").click(function(){
	  	//console.info(112111)
	  	var url="/getLatentSuppRegHtml";
	  	//location=url;
	    //creatIframe(url,"潜在供应商登记"); 
	  	tuoGo(url,'latentSuppReg',"latentSuppTableId");
	  });
	  //重置
	  $("#reset").click(function(){
	  	$("#suppInfo").val('');
	  	$("#category").val('');
	  });
	  //审核
	  $("#apprSupp").click(function(){
	  	layui.use('table', function(){
	  		  var table = layui.table;
	  		  var checkStatus = table.checkStatus('latentSuppTableId')
	  	      ,data = checkStatus.data;
	  		  var length = data.length;
	  		 // console.info(length)
	  		  if(length==1){
	  			  //layer.alert(JSON.stringify(data[0].suppId));
	  			  if(data[0].status=='未审核'){
	  				  var url="/getLatentSuppAuditHtml?suppId="+data[0].suppId;
	  				  //location=url;
	  				  tuoGo(url,'latentSuppAudit',"latentSuppTableId");
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"未审核"供应商才能进行初审</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条数据进行审核</sapn>');	
	  		  }
	  	});
	  });
	  //提交OA
	  $("#submitOA").click(function(){
	  	layui.use('table', function(){
	  		var table = layui.table;
	  		var checkStatus = table.checkStatus('latentSuppTableId')
	  		,data = checkStatus.data;
	  		var length = data.length;
	  		// console.info(length)
	  		if(length==1){
	  			//layer.alert(JSON.stringify(data[0].suppId));
	  			if(data[0].status=='已初审'){
	  				var url="/getLatentSuppSubOAHtml?suppId="+data[0].suppId;
	  				//location=url;
	  				tuoGo(url,'latentSuppSubOA',"latentSuppTableId");
	  			}else{
	  				layer.alert('<span style="color:red;">只有"已初审"供应商才能进行提交OA</sapn>');	 
	  			}
	  		}else{
	  			layer.alert('<span style="color:red;">请选择一条数据进行审核后提交</sapn>');	
	  		}
	  	});
	  });
	  //删除
	  $("#removeSupp").click(function(){
	  	layui.use('table', function(){
	  		var table = layui.table;
	  		var checkStatus = table.checkStatus('latentSuppTableId');
	  		data = checkStatus.data;
	  		var length = data.length;
	  		// console.info(length)
	  		debugger;
	  		if(length != 0){
	  			  //layer.alert(JSON.stringify(data[0].suppId));
	  			  var suppIds = [];
	  			  var a=0;
	  			  for(var i=0;i<length;i++){
	  				  suppIds[i]=data[i].suppId;
	  				  if(data[i].status !='不合格' && data[i].status !='未审核' ){
	  					  a++;
	  				  }
	  			  }
	  			  if(a == 0){
	  				  layer.confirm('真的删除选中的供应商吗？', function(index){
	  				      $.ajax({
	  				    	 type:"post",
	  				    	 url:"/deleteLatentSuppBySuppId",
	  				    	 data:"suppIds="+suppIds,
	  				    	 dataType:"JSON",
	  				    	 success:function(data2){
	  				    		 if(data2){
	  			    				 layer.msg('删除成功', {time:2000 });
	  			    				table.reload('latentSuppTableId');
	  			    			 }else{
	  			    				 layer.alert('<span style="color:red;">删除失败</sapn>');
	  			    			 }
	  				    	 }
	  				      });
	  				      layer.close(index);
	  				  });
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"不合格","未审核"供应商才被删除</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条或多条数据进行删除</sapn>');	
	  		  }
	  	});
	  });
	  
	 
	  
	  

});

