var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  var id='';
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
	  //监听工具条
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    var appoStatus = data.appoStatus;
	    if(obj.event === 'edit'){//编辑
	    	if(appoStatus=='未确认'){
	    		var url="/getAppointUrgeAddHtml?appoId="+data.appoId+"&type=2";
	  		  	//location=url;
	    		tuoGo(url,'appointUrgeAdd',"appointTableId");
	    	}else if(appoStatus =="已发布" || appoStatus =="待发货" ||appoStatus =="已发货"){
	    		layer.open({
					type : 2,
					title : '编辑预约单',
					shadeClose : false,
					shade : 0.1,
					content : '/getUpdateAppoHtml?appoCode='+data.appoCode,
					area : [ '500px', '70%' ],
					maxmin : true, // 开启最大化最小化按钮
					btn: ['确认', '取消']
				       ,yes: function(index, layero){
				      //按钮【按钮一】的回调
				      // 获取选中的物料数据
				      var map = $(layero).find("iframe")[0].contentWindow
				          .getData();
				      if(map.judge){
				    	  // 关闭弹框
				    	  layer.close(index);
				    	  // 处理数据
				    	  if(appoStatus =="待发货" ||appoStatus =="已发货" ){
				    		  layer.confirm('预约单:'+data.appoCode+'已经和送货单关联，确认要修改预约单的预约送货日期和确认送货时间吗？', {
				    			  btn: ['确认','取消'] //按钮
				    			}, function(index, layero){
				    				map.type = "1";
				    				updateAppoDate(map);
				    				layer.close(index)
				    			}, function(index, layero){
				    				layer.close(index)
				    			});
				    	  }else{
				    		  layer.confirm('确认要修改'+data.appoCode+'预约单的预约送货日期和确认送货时间吗？', {
				    			  btn: ['确认','取消'] //按钮
				    			}, function(index, layero){
				    				map.type ="0";
				    				updateAppoDate(map);
				    				layer.close(index)
				    			}, function(index, layero){layer.close(index)});
				    	  }
				    	  
				      }else{
				    	  layer.msg(map.msg);
				      }
				     },
				     btn2: function(index, layero){
				      //按钮【按钮二】的回调
				      // 默认会关闭弹框
				      //return false 开启该代码可禁止点击该按钮关闭
				     }
				});
	    	}else{
	    		layer.alert('<span style="color:red;">不能编辑已确认的预约单</sapn>');
	    	}
	    } else if(obj.event === 'affirm'){//确认
	       if(data.appoStatus=='未确认'){
		      //layer.confirm('是否确认预约？', function(index){
		    	  var appoIds =[];
		    	  var appoId = data.appoId;
		    	  appoIds.push(appoId);
		    	  id=appoId;
		    	  var suppName =data.suppName;
				  var createDate = data.createDate;
				  //var format = new Date(createDate).Format("yyyy-MM-dd");
				  var remark = "预约送货审核: "+suppName;
				  var flag = taskProcess(appoId, "appointAudit", remark, "process");
				  /*if(flag=="process_success"){
					  var appoIds = JSON.stringify(appoIds);
					  $.ajax({
						  type:"post",
						  url:"/updateAppoStatusByAppoId?status=1&appoIds="+appoIds,
						  dataType:"JSON",
						  success:function(data2){
							  if(data2){
								  layer.msg('确认成功', {time:2000 });
								  //initAppoTable();
							  }else{
								  layer.alert('<span style="color:red;">确认失败</sapn>');
							  }
						  }
					  });
				  }*/
		    	  //layer.close(index);
		      //});
	       }else{
	    	   layer.alert('<span style="color:red;">只有"未确认"状态的预约才能确认</sapn>');
	       }
	    } else if(obj.event === 'refuse'){//拒绝
	    	if(data.appoStatus=="未确认"){
	    		layer.confirm('是否拒绝预约？', function(index){
	    			 layer.prompt({title: '拒绝原因', formType: 2}, function(text, index){
	    				 var appoIds =[];
	    				 var appoId = data.appoId;
	    				 appoIds.push(appoId);
	    				 var result = backProcess(appoId);
	    				 if(result = "back_success"){
	    					 var appoIds = JSON.stringify(appoIds);
	    					 $.ajax({
	    						 type:"post",
	    						 url:"/updateAppoStatusByAppoId?status=2&appoIds="+appoIds+"&text="+text,
	    						 dataType:"JSON",
	    						 success:function(data2){
	    							 if(data2){
	    								 layer.msg('拒绝成功', {time:2000 });
	    								 initAppoTable();
	    							 }else{
	    								 layer.alert('<span style="color:red;">拒绝失败</sapn>');
	    							 }
	    						 }
	    					 });
	    				 }
	    				layer.close(index);
	    			  });
		    		layer.close(index);
			    });
	    	}else{
	    		layer.alert('<span style="color:red;">只有"未确认"状态的预约可以拒绝</sapn>');
	    	}
	    }else if(obj.event === 'check'){//查看
	    	var url="/getAppointUrgeAddHtml?appoId="+data.appoId+"&type=3";
  		  	//location=url;
	    	tuoGo(url,'appointUrgeAdd',"appointTableId");
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      var endDate = $('#endDate');
			      var startDate = $("#startDate");
			      var priority = $("#priority");
			      //var appoStatus = $("#appoStatus");
			      var appoCode = $("#appoCode");
			      var suppName = $("#suppName");
			      var expectDate = $("#expectDate");
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
			        	suppName:suppName.val(),
			        	expectDate:expectDate.val(),
			        	statusJson:statusJson
			        }
			      });
			    }
			  
		};
	  //在弹窗中选择执行人后，点击确认按钮回调
	   window.returnFunction = function() {
			debugger
			var appoIds = [];
			appoIds.push(id);
			var appoIdJson = JSON.stringify(appoIds);
			$.ajax({
				  type:"post",
				  url:"/updateAppoStatusByAppoId?status=1" +
				  		"&appoIds="+appoIdJson,
				  dataType:"JSON",
				  success:function(data2){
					  if(data2){
						  layer.msg('确认成功', {time:2000 });
						  initAppoTable();
					  }else{
						  layer.alert('<span style="color:red;">确认失败</sapn>');
					  }
				  }
			});
		}
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  
	  
	//紧急预约
	  $("#addAppo").click(function(){
	  	var url="/getAppointUrgeAddHtml?type=1";
	  	//location=url;
	  	tuoGo(url,'appointUrgeAdd',"appointTableId");
	  });
	  
	//重置
	  $("#reset").click(function(){
		  $(".checked").each(function(index,row){
				$(this).removeClass("checked");
				$(this).addClass("uncheck");
				$(this).css('color','gray');
		  });
	  });
	  //确认
	  $("#affirmAppo").click(function(){
	  		var table = layui.table;
	  		var checkStatus = table.checkStatus('appointTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length == 1){
	  			  var appoIds = [];
	  			  var a=0;
	  			  for(var i=0;i<length;i++){
	  				  appoIds.push(data[i].appoId);
	  				  if(data[i].appoStatus !='未确认'){
	  					  a++;
	  				  }
	  			  }
	  			  if(a == 0){
	  				  layer.confirm('确认选中的 '+length+' 条预约申请吗？',{title:'预约确认'}, function(index){
	  					  for(var i =0;i<length;i++){
	  						appoIds = [];
	  						var appoId = data[i].appoId;
	  						appoIds.push(appoId);
	  						id = appoId;
	  						var suppName =data[i].suppName;
	  						var createDate = data[i].createDate;
	  						//var format = new Date(createDate).Format("yyyy-MM-dd");
	  						var remark = "预约送货审核: "+suppName;
	  						var flag = taskProcess(appoId, "appointAudit", remark, "process");
	  						//if(flag=="process_success"){
	  							/*var appoIdJson = JSON.stringify(appoIds);
	  							$.ajax({
	  								type:"post",
	  								url:"/updateAppoStatusByAppoId?status=1&appoIds="+appoIdJson,
	  								dataType:"JSON",
	  								success:function(data2){
	  									if(data2){
	  										layer.msg('确认成功', {time:2000 });
	  										//initAppoTable();
	  									}else{
	  										layer.alert('<span style="color:red;">确认失败</sapn>');
	  									}
	  								}
	  							});*/
	  						//}
	  					  }
	  				      layer.close(index);
	  				  });
	  			  }else{
	  				  layer.alert('<span style="color:red;">只有"未确认"状态的预约才能确认</sapn>');	 
	  			  }
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条数据进行确认</sapn>');	
	  		  }
	  	
	  });
	  
	  //拒绝
	  $("#refuseAppo").click(function(){
		  var table = layui.table;
		  var checkStatus = table.checkStatus('appointTableId');
		  var data = checkStatus.data;
		  var length = data.length;
		  if(length != 0){
			  var appoIds = [];
			  var a=0;
			  for(var i=0;i<length;i++){
				  appoIds.push(data[i].appoId);
				  if(data[i].appoStatus !='未确认'){
					  a++;
				  }
			  }
			  if(a == 0){
				  layer.confirm('拒绝选中的 '+length+' 条预约吗？',{title:'预约拒绝'},function(index){
					  layer.prompt({title: '拒绝原因', formType: 2}, function(text, index){
						  for(var i =0;i<length;i++){
							  appoIds=[];
							  var appoId = data[i].appoId;
							  appoIds.push(appoId);
							  var result = backProcess(appoId);
							  if(result = "back_success"){
								  var appoIdJson = JSON.stringify(appoIds);
								  $.ajax({
									  type:"post",
									  url:"/updateAppoStatusByAppoId?status=2&appoIds="+appoIdJson+"&text="+text,
									  dataType:"JSON",
									  success:function(data2){
										  if(data2){
											  layer.msg('拒绝成功', {time:2000 });
											  initAppoTable();
										  }else{
											  layer.alert('<span style="color:red;">拒绝失败</sapn>');
										  }
									  }
								  });
							  }
						  }
					  layer.close(index);
	    			  });
					  layer.close(index);
				  });
			  }else{
				  layer.alert('<span style="color:red;">只有"未确认"状态的预约才能拒绝</sapn>');	 
			  }
		  }else{
			  layer.alert('<span style="color:red;">请选择一条或多条数据进行拒绝</sapn>');	
		  }
		  
	  });
	//作废
	  $("#cancellBut").click(function(){
		  var table = layui.table;
		  var checkStatus = table.checkStatus('appointTableId');
		  var data = checkStatus.data;
		  var length = data.length;
		  if(length != 0){
			  var appoIds = [];
			  var appos = [];
			  var a=0;
			  for(var i=0;i<length;i++){
				  appoIds.push(data[i].appoId);
				  var obj = new Object();
				  obj.appoCode = data[i].appoCode;
				  obj.suppId = data[i].suppId;
				  appos.push(obj);
				  if(data[i].appoStatus =='待发货' || data[i].appoStatus =='已发货' ||data[i].appoStatus =='已作废'){
					  a++;
				  }
			  }
			  if(a==0){
				  layer.confirm('作废选中的 '+length+' 条预约吗？',{title:'预约作废'},function(index){
					  var appoIdJson = JSON.stringify(appoIds);
					  var apposJson = JSON.stringify(appos);
					  $.ajax({
						  type:"post",
						  url:"/cancellAppointForManByAppoId",
						  data:{
							  appoIds:appoIdJson,
							  appos:apposJson
						  },
						  dataType:"JSON",
						  success:function(data2){
							  if(data2.judge){
								  layer.msg('预约已作废', {time:2000 });
								  initAppoTable();
							  }else{
								  layer.alert(data2.msg);
							  }
						  }
					  });
					  layer.close(index);
				  });
			  }else{
				  layer.alert('<span style="color:red;">"待发货"、"已发货"、"已作废"状态的预约不能作废</sapn>');
			  }
		  }else{
			  layer.alert('<span style="color:red;">请选择一条或多条数据进行作废</sapn>');	
		  }
	  });
	//预约查询
	  $("#queryAppo").click(function(){
		  layer.open({
			  type:2,
			  title:"预约查询",
			  shadeClose : false,
			  shade : 0.1,
			  content : '/getAppoQueryPopHtml',
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

function initAppoTable(){
	table.render({
		  elem:"#appointTable",
		  url:"/queryAppointForManagerByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"appointTableId",
		  cols:[[
		     {checkbox: true, fixed:'left'},
		     {field:'priority',title:'优先级', align:'center',width:60},
		     {field:'appoStatus',title:'预约状态', align:'center',width:70},
		     {field:'appoCode',title:'预约单号', align:'center',sort: true,width:113 },
		     {field:'appoDate', title:'预约送货日期',align:'center',sort: true,width:106,templet:
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
		     {field:'expectDate',title:'期望送货时间', align:'center',sort: true,width:108},
		     {field:'suppName', title:'供应商',align:'center',width:145,sort: true},
		     {field:'mateNumber', title:'数量/箱',align:'center',width:60},
		     {field:'mateAmount',title:'方量', align:'center',width:60},
		     {field:'truckNum', title:'车次',align:'center',width:60},
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
		     {field:'emailStatus', title:'邮件发送状态',align:'center',width:108},
		     {field:'prodVeriId', title:'确认人',align:'center',width:69},
		     {fixed: 'right', title:'操作',width:200, align:'center', toolbar: '#barDemo'}
		  ]]
	  })
}
//修改预约单的送货时间
function updateAppoDate(map){
	$.ajax({
		type:"post",
		url:"/updateAppoDate?appoCode="+map.appoCode+
		"&appoDate="+map.appoDate+"&affirmDate="+map.affirmDate+"&type="+map.type,
		dataType:"JSON",
		error:function(result){
			layer.alert("Connection error");
		},
		success:function(data){
			if(data){
				initAppoTable();
				layer.msg("修改成功");
			}else{
				layer.msg("修改失败");
			}
		}
	});
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