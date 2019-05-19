var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initReceTable();
	  laydate.render({
		    elem: '#startDate', //指定元素
	  });
	  laydate.render({
		    elem: '#endDate', //指定元素
	  });
	  laydate.render({
		  elem: '#createDate', //指定元素
	  });
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  //监听工具条
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
			var url  ="/getReceiveEditHtml?receId="+data.receId+"&funType=2";
			tuoGo(url,'receiveEdit',"receiveTableId");
			//location=url;
	    } else if(obj.event === 'del'){//删除
	       if(data.status=='已保存'){
		      layer.confirm('真的删除这个收货单吗？', function(index){
		    	  var receIds =[];
		    	  receIds.push(data.receId);
		    	  var deliCodes=[];
		    	  deliCodes.push(data.deliCode);
		    	  var deliCodes2 = JSON.stringify(deliCodes);
		    	  $.ajax({
		    		 type:"post",
		    		 url:"/deleteReceiveByReceId?deliCodes2="+deliCodes2,
		    		 data:"receIds="+receIds,
		    		 dataType:"JSON",
		    		 success:function(data2){
		    			 if(data2){
		    				 layer.msg('删除成功', {time:2000 });
		    				 initReceTable();
		    			 }else{
		    				 layer.alert('<span style="color:red;">删除失败</sapn>');
		    			 }
		    		 }
		    	  });
		    	  layer.close(index);
		      });
	       }else{
	    	   layer.alert('<span style="color:red;">只有"已保存"状态的收货单才能被删除</sapn>');
	       }
	    } else if(obj.event === 'edit'){//编辑
	    	if(data.status=="已保存"){
	    		var url  ="/getReceiveEditHtml?receId="+data.receId+"&funType=1";
	    		//location=url;
	    		tuoGo(url,'receiveEdit',"receiveTableId");
	    	}else{
	    		layer.alert('<span style="color:red;">只有"已保存"状态的收货单可以编辑</sapn>');
	    	}
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      var startDate = $('#startDate');
			      var endDate = $("#endDate");
			      var status = $("#status");
			      var receCode = $("#receCode");
			      var suppName = $("#suppName");
			      var createDate = $("#createDate");
			      var deliCode = $("#deliCode");
			      //执行重载
			      table.reload('receiveTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  startDate: startDate.val(),
			        	  endDate:endDate.val(),
			        	  status:status.val(),
			        	  receCode:receCode.val(),
			        	  suppName:suppName.val(),
			        	  deliCode:deliCode.val(),
			        	  createDate:createDate.val()
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  //新建收货
	  $("#rece").click(function(){
		 var url="/getReceiveAddHtml";
		 //location=url;
		 tuoGo(url,'receiveAdd',"receiveTableId");
	  });
	  
	  //删除
	  $("#remRece").click(function(){
		   // var table = layui.table;
	  		var checkStatus = table.checkStatus('receiveTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 0){
	  			  var receIds = [];
	  			  var deliCodes=[];
	  			  var a=0;
	  			  for(var i=0;i<length;i++){
	  				  receIds.push(data[i].receId);
	  				  deliCodes.push(data[i].deliCode);
	  				  if(data[i].status !='已保存'){
	  					  a++;
	  				  }
	  			  }
	  			  if(a == 0){
	  				var deliCodes2 = JSON.stringify(deliCodes);
	  				layer.confirm('是否删除收货单？', function(index){
		  				  $.ajax({
	  			    		 type:"post",
	  			    		 url:"/deleteReceiveByReceId?deliCodes2="+deliCodes2,
	  			    		 data:"receIds="+receIds,
	  			    		 dataType:"JSON",
	  			    		 success:function(data2){
	  			    			 if(data2){
	  			    				 layer.msg('删除成功', {time:2000 });
	  			    				 initReceTable();
	  			    			 }else{
	  			    				 layer.alert('<span style="color:red;">删除失败</sapn>');
	  			    			 }
	  			    		 }
	  			    	  });
		  				 layer.close(index);
				    });
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"已保存"状态的收货单才能被删除</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择其中一条或多条数据</sapn>');	
	  		  }
	  });
	
	  //重置
	  $("#reset").click(function(){
	  	$("#suppInfo").val('');
	  	$("#category").val('');
	  });
	  
	  //冲销
	  $("#writeoff").click(function(){
		    //var table = layui.table;
	  		var checkStatus = table.checkStatus('receiveTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length == 1){
	  			  var inboDeliCodes=[];
	  			  if(data[0].status == "已收货"){
	  				 $.ajax({
	  					 type:"post",
	  					 url:"/queryReceMatesByReceId?receId="+data[0].receId,
	  					 dataType:"JSON",
	  					 async:false,//注意
	  					 success:function(data){
	  						var count=0;
	  						if(data!= null){
	  							 $.each(data,function(index,item){
	  								 if(item.inboDeliCode == null || item.inboDeliCode ==""){
	  									 count++;
	  								 }
	  							 });
	  							 if(count==0){
	  								 $.each(data,function(index,item){
	  									 inboDeliCodes.push(item.inboDeliCode); 
		  							 });
	  							 }
	  						}
	  					 }
	  				 });
	  				 if(inboDeliCodes.length!=0){
	  					 var inboDeliCodeJson = JSON.stringify(inboDeliCodes);
	  					 $.ajax({
	  						 type:"post",
	  						 url:"/writeoffReceive?receCode="+data[0].receCode+"&inboDeliCodes="+inboDeliCodeJson+"&deliCode="+data[0].deliCode,
	  						 dataType:"JSON",
	  						 async:false,
	  						 success:function(map){
	  							 if(map.judge){
	  								 layer.msg(map.msg);
	  								 table.reload("receiveTableId");
	  							 }else{
	  								layer.alert(map.result+","+map.msg);
	  							 }
	  						 }
	  					 });
	  				 }else{
	  					 layer.alert('<span style="color:red;">该收货单中有物料没有内向交货单号</sapn>');	 
	  				 }
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"已收货"状态的收货单才能冲销</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择其中一条数据进行冲销</sapn>');	
	  		  }
	  });
	  
});

function initReceTable(){
	table.render({
		  elem:"#receiveTable",
		  url:"/queryReceiveByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"receiveTableId",
		  cols:[[
		     {checkbox: true, fixed:'left'},
		     {field:'status',title:'单据状态', align:'center',width:93},
		     {field:'receCode',title:'收货单号', align:'center',width:113 },
		     {field:'receDate', title:'收货日期',align:'center',width:106,templet:
		    	 function(d){
		    	    var date = new Date(d.receDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					/*var h = date.getHours();
					var m = date.getMinutes();
					var s =date.getSeconds();*/
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		     },
		     {field:'suppName', title:'供应商',align:'center',width:145},
		     {field:'deliCode', title:'送货单号',align:'center',width:113},
		     {field:'creator', title:'创建人',align:'center',width:109},
		     {field:'createDate',title:'创建时间', align:'center',width:106,templet:
		    	 function (d){
		    	    var date = new Date(d.createDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		     },
		     {fixed: 'right', title:'操作',width:150, align:'center', toolbar: '#barDemo'}
		  ]]
	  })
}
