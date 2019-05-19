var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initAppoTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  laydate.render({
		    elem: '#startDate', //指定元素
	  });
	  laydate.render({
		    elem: '#endDate', //指定元素
	  });
	  laydate.render({
		  elem: '#createDate', //指定元素
	  });
	  //监听工具条
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
	    	var url  ="/getAppointAddHtml?appoId="+data.appoId+"&type=3";
    		//location=url;
	    	tuoGo(url,'appointAdd',"appointId");
	    } else if(obj.event === 'del'){//删除
	       if(data.appoStatus=='已保存'){
		      layer.confirm('真的删除这个预约申请吗？', function(index){
		    	  var appoIds =[];
		    	  appoIds.push(data.appoId);
		    	  $.ajax({
		    		 type:"post",
		    		 url:"/deleteAppointByAppoId",
		    		 data:"appoIds="+appoIds,
		    		 dataType:"JSON",
		    		 success:function(data2){
		    			 if(data2){
		    				 layer.msg('删除成功', {time:2000 });
		    				 initAppoTable();
		    			 }else{
		    				 layer.alert('<span style="color:red;">删除失败</sapn>');
		    			 }
		    		 }
		    	  });
		    	  layer.close(index);
		      });
	       }else{
	    	   layer.alert('<span style="color:red;">只有"已保存"状态的预约申请才能被删除</sapn>');
	       }
	    } else if(obj.event === 'edit'){//编辑
	    	if(data.appoStatus=="已保存" || data.appoStatus=="已拒绝"){
	    		var url  ="/getAppointAddHtml?appoId="+data.appoId+"&type=2";
	    		//location=url;
	    		tuoGo(url,'appointAdd',"appointId");
	    	}else{
	    		layer.alert('<span style="color:red;">只有"已保存"状态的预约申请可以编辑</sapn>');
	    	}
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      debugger;
			    	var startDate = $('#startDate');
			      var endDate = $("#endDate");
			      var appoCode = $("#appoCode");
			      var createDate = $("#createDate");
			      var suppName = $("#suppName");
			       var checkedStatusCode=[];
				    $(".checked").each(function(){
				    	var status=$(this).attr("name");
				    	checkedStatusCode.push(status);
				    });
				    var statusJson=JSON.stringify(checkedStatusCode);
			      //执行重载
			      table.reload('appointId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  startDate: startDate.val(),
			        	  endDate:endDate.val(),
			        	  appoCode:appoCode.val(),
			        	  suppName:suppName.val(),
			        	  createDate:createDate.val(),
			        	  statusJson:statusJson
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  
	  
	//新建
	  $("#addAppo").click(function(){
	  	var url="/getAppointAddHtml?type=1";
	  	//location=url;
	  	tuoGo(url,'appointAdd',"appointId");
	  });
	  //重置
	  $("#reset").click(function(){
		  $(".checked").each(function(index,row){
				$(this).removeClass("checked");
				$(this).addClass("uncheck");
				$(this).css('color','gray');
		  });
	  });
	  //删除
	  $("#removeAppo").click(function(){
	  		var table = layui.table;
	  		var checkStatus = table.checkStatus('appointId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 0){
	  			  var appoIds = [];
	  			  var a=0;
	  			  for(var i=0;i<length;i++){
	  				  appoIds[i]=data[i].appoId;
	  				  if(data[i].appoStatus !='已保存'){
	  					  a++;
	  				  }
	  			  }
	  			  if(a == 0){
	  				  layer.confirm('真的删除选中的预约申请吗？', function(index){
	  				     $.ajax({
	  				    	 type:"post",
	  				    	 url:"/deleteAppointByAppoId",
	  				    	 data:"appoIds="+appoIds,
	  				    	 dataType:"JSON",
	  				    	 success:function(data2){
	  				    		 if(data2){
	  			    				 layer.msg('删除成功', {time:2000 });
	  			    				 initAppoTable();
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
	  				  layer.alert('<span style="color:red;">只有"已保存"状态的预约才被删除</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条或多条数据进行删除</sapn>');	
	  		  }
	  	
	  });
	  //引用预约单
	  $("#citeAppoBut").click(function(){
		    debugger;
		    var checkStatus = table.checkStatus('appointId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 1){
	  			layer.msg("请选择一条预约单数据进行引用");
	  			return;
	  		}
	  		layer.confirm('确定要引用选中的预约申请吗？', function(index){
				var appoId = data[0].appoId;
	  			var url ="/getAppointAddHtml?type=4"+"&appoId="+appoId;    
	  			 //location = url;
	  			tuoGo(url,'appointAdd',"appointId");
				layer.close(index);
			});
	  		
	  });

});

function initAppoTable(){
	table.render({
		  elem:"#appointTableId",
		  url:"/queryAppointOfSuppByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"appointId",
		  cols:[[
		     {checkbox: true, fixed:'left'},
		     {field:'appoStatus',title:'预约状态', align:'center',width:70},
		     {field:'appoCode',title:'预约单号', align:'center',width:96 },
		     {field:'appoDate', title:'预约日期',align:'center',width:89,templet:
		    	 function(d){
		    	    var date = new Date(d.appoDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					/*var h = date.getHours();
					var m = date.getMinutes();
					var s =date.getSeconds();*/
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		     },
		     {field:'affirmDate',title:'确定送货时间',align:'center', width:96},
		     {field:'expectDate',title:'期望送货时间', align:'center',width:96},
		     {field:'suppName', title:'供应商',align:'center',width:145},
		     {field:'mateNumber', title:'数量/箱',align:'center',width:60},
		     {field:'mateAmount',title:'方量', align:'center',width:60},
		     {field:'truckNum', title:'车次',align:'center',width:63},
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
		     {field:'prodVeriId', title:'确认人',align:'center',width:69},
		     {fixed: 'right', title:'操作',width:160, align:'center', toolbar: '#barDemo'}
		  ]]
	  })
}


//文档查询点击事件
function changeClass(dom){
	if($(dom).hasClass("uncheck")){
		$(dom).removeClass("uncheck");
		$(dom).addClass("checked");
		$(dom).css('color','blue');
	}else{
		$(dom).removeClass("checked");
		$(dom).addClass("uncheck");
		$(dom).css('color','gray');
	}
}