var table;
layui.use(['table','laydate','layer'], function(){
	  table = layui.table;
	  var $ = layui.jquery;
	  var laydate=layui.laydate;
	  initReceMessTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  //监听工具条
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
	    	
	    } else if(obj.event === 'del'){//删除
	      layer.confirm('真的删除这个收货信息吗？', function(index){
	    	  var ids =[];
	    	  ids.push(data.id);
	    	  $.ajax({
	    		 type:"post",
	    		 url:"/deleteReceMessById",
	    		 data:"ids="+ids,
	    		 dataType:"JSON",
	    		 success:function(data2){
	    			 if(data2){
	    				 layer.msg('删除成功', {time:2000 });
	    				 initReceMessTable();
	    			 }else{
	    				 layer.alert('<span style="color:red;">删除失败</sapn>');
	    			 }
	    		 }
	    	  });
	    	  layer.close(index);
	      });
	    } else if(obj.event === 'edit'){//编辑
	    	var url="/getReceMessAddHtml?id="+data.id+"&type=2";
		  	location=url;
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      var startDate = $('#startDate');
			      var endDate = $("#endDate");
			      var appoStatus = $("#appoStatus");
			      //执行重载
			      table.reload('appointId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  startDate: startDate.val(),
			        	  endDate:endDate.val(),
			        	  appoStatus:appoStatus.val()
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  
	  
	//新建收货信息
	  $("#addReceMess").click(function(){
	  	var url="/getReceMessAddHtml?type=1";
	  	location=url;
	  });
	  //删除
	  $("#removeReceMess").click(function(){
	  		var table = layui.table;
	  		var checkStatus = table.checkStatus('receMessTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 0){
	  			  var ids = [];
	  			  for(var i=0;i<length;i++){
	  				ids[i]=data[i].id;
	  			  }
  				  layer.confirm('真的删除选中的收货信息吗？', function(index){
  				     $.ajax({
  				    	 type:"post",
  				    	 url:"/deleteReceMessById",
  				    	 data:"ids="+ids,
  				    	 dataType:"JSON",
  				    	 success:function(data2){
  				    		 if(data2){
  			    				 layer.msg('删除成功', {time:2000 });
  			    				 initReceMessTable();
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
	  			  layer.alert('<span style="color:red;">请选择一条或多条数据进行删除</sapn>');	
	  		  }
	  	
	  });

});

function initReceMessTable(){
	table.render({
		  elem:"#receMessTable",
		  url:"/queryReceiveMessByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"receMessTableId",
		  cols:[[
		     {checkbox: true, fixed:'left'},
		     {field:'freightRange',title:'运费子范围', align:'center',width:150},
		     {field:'receUnit',title:'收货单位', align:'center',width:150},
		     {field:'receAddr',title:'收货地址', align:'center',width:331 },
		     {field:'contact', title:'联系人',align:'center',width:102},
		     {field:'phone',title:'联系电话',align:'center', width:148},
		     {field:'post', title:'岗位',align:'center',width:109},
		     {field:'creator', title:'创建人',align:'center',width:109},
		     {field:'createDate',title:'创建时间', align:'center',width:106,templet : 
		    	 function(d) {
					return formatTime(d.createDate, 'yyyy-MM-dd');
				 }
		     },
		     {fixed: 'right', title:'操作',width:120, align:'center', toolbar: '#barDemo'}
		  ]]
	  })
}
