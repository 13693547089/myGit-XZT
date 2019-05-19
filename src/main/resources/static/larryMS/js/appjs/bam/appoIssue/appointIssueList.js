var table;
layui.use(['table','laydate','layer'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  var layer=layui.layer;
	  initAppoTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  table.on('checkbox(demo)', function(obj){
	  });
	  laydate.render({
		  elem: '#startDate', //指定元素
	  });
	  laydate.render({
		    elem: '#endDate', //指定元素
	  });
	  //监听工具条
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'edit'){//编辑
	    	 if(data.appoStatus=='已确认'){
	    		var url ="/getAppointIssueHtml?appoId="+data.appoId+"&type=2";
	    		//location=url;
	    		tuoGo(url,'appointIssue',"appointTableId");
	    	 }else{
	    		 layer.alert('<span style="color:red;">只有"已确认"状态的预约才能编辑</sapn>'); 
	    	 }
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
	    } else if(obj.event === 'check'){//查看
	    	var url ="/getAppointIssueHtml?appoId="+data.appoId+"&type=3";
    		//location=url;
	    	tuoGo(url,'appointIssue',"appointTableId");
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      var startDate = $('#startDate');
			      var endDate = $("#endDate");
			      var priority = $("#priority");
			      var appoCode = $("#appoCode");
			      var affirmDate = $("#affirmDate");
			      var checkedStatusCode=[];
				    $(".checked").each(function(){
				    	var status=$(this).attr("name");
				    	checkedStatusCode.push(status);
				    });
				    var statusJson=JSON.stringify(checkedStatusCode);
			      //执行重载
			      table.reload('appointTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  startDate: startDate.val(),
			        	  endDate:endDate.val(),
			        	  priority:priority.val(),
			        	  appoCode:appoCode.val(),
			        	  affirmDate:affirmDate.val(),
			        	  statusJson:statusJson
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  $('#exportAppo').click(function(){
		  var checkStatus = table.checkStatus('appointTableId');
		     var data = checkStatus.data;
//		     var fids;
//		     for(var i=0;i<data.length;i++){
//		    	 if(i==0){
//		    		 fids=data[i].appoId;
//		    	 }else{
//		    		 fids+='","'+data[i].appoId;
//		    	 }
//		     }
		     
//		     var fids= [];
//		     for(var i=0;i<data.length;i++){
//		    		 fids.push(data[i].appoId);		 
//		    
//		     }
		     //预约单号
		     var appoCode = $('#appoCode').val();
		     //预约日期
		     var startDate = $('#startDate').val();
		     var endDate = $('#endDate').val();
		     //优先级
		     var priority = $('#priority').val();
		     //实际送货时间
		     var affirmDate = $('#affirmDate').val();
		     
		     if(appoCode==''&startDate==''&endDate==''&priority==''){
		    	 layer.confirm('是否导出全部数据？', {icon: 3, title:'提示'}, function(index){
		    		  //do something
		    		 var obj = new Object();
		    		 obj.appoCode= appoCode;
		    		 obj.startDate=startDate;
		    		 obj.endDate=endDate;
		    		 obj.priority=priority;
		    		 obj.affirmDate=affirmDate;
		    		var objjson =  JSON.stringify(obj);
		    		selectMateCodes =escape(objjson);
			    		 var url ="/exportAppointList?objjson="+selectMateCodes;
					     location=url;
		    		  layer.close(index);
		    		  
		    		  
		    		});
		     }else{
		    	 debugger;
		    	 if(priority=='紧急'){
		    		 priority='jinji'
		    	 }else if(priority=='一般'){
		    		 priority='yiban'
		    	 }
		    	 var obj = new Object();
	    		 obj.appoCode= appoCode;
	    		 obj.startDate=startDate;
	    		 obj.endDate=endDate;
	    		 obj.priority=priority;
	    		 obj.affirmDate=affirmDate;
	    		var objjson =  JSON.stringify(obj);
	    		selectMateCodes =escape(objjson);
		    		 var url ="/exportAppointList?objjson="+selectMateCodes;
				     location=url;
		     }
//		     var FidsJson =JSON.stringify(fids);
//		     var url ="/exportAppointList?Fids="+FidsJson;
//		     var url ="/exportAppointList?appoCode="+appoCode+"&startDate="+startDate+"&endDate="+endDate+"&priority="+priority;
//		     location=url;
	  })
	  
	  
	//新建
	  $("#addAppo").click(function(){
	  	var url="/getAppointAddHtml?type=1";
	  	location=url;
	  });
	  //发布
	  $("#issueAppo").click(function(){
		  var checkStatus = table.checkStatus('appointTableId');
		  var checkedData = checkStatus.data;
		  var length = checkedData.length;
		  if(length == 1){
  			  var a=0;
  			  var b=0;
  			  for(var i=0;i<length;i++){
  				  if(checkedData[i].appoStatus !='已确认'){
  					  a++;
  				  }
  				  if(checkedData[i].affirmDate == "" || checkedData[i].affirmDate == null){
  					  b++;
  				  }
  			  }
  			  if(a == 0){
  				  if(b ==0){
  					  data = {
  							  checkedData:checkedData,
  							  a:this.itemActive
  					  };
  					  layer.open({
  						  type:2,
  						  title:"预约发布",
  						  shadeClose : true,
  						  shade : 0.6,
  						  content : '/getAppoIssuePopHtml',
  						  area : [ '503px', '85%' ],
  						  maxmin : true // 开启最大化最小化按钮
  					  });
  				  }else{
  					layer.alert('<span style="color:red;">请选择实际送货时间后发布</sapn>');
  				  }
  			  }else{
  				  layer.alert('<span style="color:red;">只有"已确认"状态的预约才能发布</sapn>');
  			  }
		  }else{
			  layer.alert('<span style="color:red;">请选择一条数据进行发布</sapn>');	 
		  }
		  
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
	  		var checkStatus = table.checkStatus('appointTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 0){
	  			  var a=0;
	  			  for(var i=0;i<length;i++){
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
	  //预约查询
	  $("#queryAppo").click(function(){
		  layer.open({
				  type:2,
				  title:"预约查询",
				  shadeClose : false,
				  shade : 0.1,
				  content : '/getAppoQueryPopHtml?isCdc=yes',
				  area : [ '600px', '80%' ],
				  maxmin : false // 开启最大化最小化按钮
		  });
	  });
	  
	//邮件重发
	  $("#sendEmail").click(function(){
		  debugger;
		  var checkStatus = table.checkStatus('appointTableId');
		  var data = checkStatus.data;
		  var length = data.length;
		  if(length == 0){
			  layer.alert('<span style="color:red;">请选择一条或多条邮件发送失败的数据进行邮件重发</sapn>');
			  return;
		  }
		  var a = 0;
		  var b = 0;
		  var appos = [];
		  var appoCodes = '';
		  for(var i=0;i<length;i++){
			  var obj = new Object();
			  obj.appoStatus = data[i].appoStatus;
			  obj.appoCode = data[i].appoCode;
			  obj.suppId = data[i].suppId;
			  obj.appoId = data[i].appoId;
			  obj.emailStatus = data[i].emailStatus;
			  appos.push(obj);
			  if(data[i].emailStatus == '' || data[i].emailStatus == undefined || data[i].emailStatus == '成功'){
				  b++;
				  appoCodes+=data[i].appoCode+',';
			  }
			  
		  }
		  if(b>0){
			  layer.alert('<span style="color:red;">只有邮件未发送成功的预约可以邮件重发,以下单号不符合条件:'+appoCodes+'</sapn>'); 
			  return;
		  }
		  layer.confirm('邮件重发选中的 '+length+' 条预约吗？',{title:'预约邮件重发'},function(index){
			  var apposJson = JSON.stringify(appos);
			  $.ajax({
				  type:"post",
				  url:"/sendEmailOfAppoint",
				  data:{
					  appos:apposJson
				  },
				  dataType:"JSON",
				  success:function(data2){
					  if(data2.judge){
						  layer.msg('邮件发送成功', {time:2000 });
						  initAppoTable();
					  }else{
						  layer.alert(data2.msg);
					  }
				  }
			  });
			  layer.close(index);
		  });
	  });
	  

});
function reloadTable(){
	initAppoTable();
}
function initAppoTable(){
	table.render({
		  elem:"#appointTable",
		  url:"/queryAppointForIssueByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"appointTableId",
		  cols:[[
		     {checkbox: true, fixed:'left'},
		     {field:'priority',title:'优先级', align:'center',width:60},
		     {field:'appoStatus',title:'预约状态', align:'center',width:70},
		     {field:'appoCode',title:'预约单号', align:'center',width:113 },
		     {field:'appoDate', title:'预约日期',align:'center',width:106,templet:
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
		     {field:'expectDate',title:'期望送货时间', align:'center',width:120},
		     {field:'affirmDate',title:'实际送货时间',align:'center', width:108 },
		     {field:'suppName', title:'供应商',align:'center',width:145},
		     {field:'mateNumber', title:'数量/箱',align:'center',width:60},
		     {field:'mateAmount',title:'方量', align:'center',width:60},
		     {field:'truckNum', title:'车次',align:'center',width:60},
		     {field:'creator', title:'创建人',align:'center',width:109},
		     {field:'createDate',title:'创建时间', align:'center',width:85,templet:
		    	 function (d){
		    	    var date = new Date(d.createDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		     },
		     {field:'emailStatus', title:'邮件发送状态',align:'center',width:108},
		     {field:'prodVeriId', title:'确认人',align:'center',width:69},
		     {fixed: 'right', title:'操作',width:100, align:'center', toolbar: '#barDemo'}
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