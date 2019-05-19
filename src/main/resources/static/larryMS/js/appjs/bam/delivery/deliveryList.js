var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initDeliTable();
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
	  laydate.render({
		  elem: '#createDate', //指定元素
	  });
	  //监听工具条
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
	    	if(data.deliType=="1"){//预约
    			var url  ="/getDeliEditHtml?deliId="+data.deliId+"&type=2";
    			//location=url;
    			tuoGo(url,'deliveryEdit',"deliveryTableId");
    		}else if(data.deliType=="0"){//直发
    			var url  ="/getDeliStraEditHtml?deliId="+data.deliId+"&type=2";
    			//location=url;
    			tuoGo(url,'deliStraEdit',"deliveryTableId");
    		}else if(data.deliType=='2'){//特殊
    			var url  ="/getSpecialDeliEditHtml?deliId="+data.deliId+"&type=2";
    			//location=url;
    			tuoGo(url,'specialDeliEdit',"deliveryTableId");
    		}
	    } else if(obj.event === 'del'){//删除
	       if(data.status=='已保存'){
		      layer.confirm('真的删除这个送货单吗？', function(index){
		    	  var deliIds =[];
		    	  deliIds.push(data.deliId);
		    	  $.ajax({
		    		 type:"post",
		    		 url:"/deleteDeliveryBydeliId?mapgCode="+data.mapgCode+"&deliType="+data.deliType,
		    		 data:"deliIds="+deliIds,
		    		 dataType:"JSON",
		    		 success:function(data2){
		    			 if(data2){
		    				 layer.msg('删除成功', {time:2000 });
		    				 initDeliTable();
		    			 }else{
		    				 layer.alert('<span style="color:red;">删除失败</sapn>');
		    			 }
		    		 }
		    	  });
		    	  layer.close(index);
		      });
	       }else{
	    	   layer.alert('<span style="color:red;">只有"已保存"状态的送货单才能被删除</sapn>');
	       }
	    } else if(obj.event === 'edit'){//编辑
	    	if(data.status=="已保存"){
	    		if(data.deliType=="1"){//预约
	    			var url  ="/getDeliEditHtml?deliId="+data.deliId+"&type=1";
	    			//location=url;
	    			tuoGo(url,'deliveryEdit',"deliveryTableId");
	    		}else if(data.deliType=="0"){//直发
	    			var url  ="/getDeliStraEditHtml?deliId="+data.deliId+"&type=1";
	    			//location=url;
	    			tuoGo(url,'deliStraEdit',"deliveryTableId");
	    		}else if(data.deliType=="2"){//特殊
	    			var url  ="/getSpecialDeliEditHtml?deliId="+data.deliId+"&type=1";
	    			//location=url;
	    			tuoGo(url,'specialDeliEdit',"deliveryTableId");
	    		}
	    	}else{
	    		layer.alert('<span style="color:red;">只有"已保存"状态的送货单可以编辑</sapn>');
	    	}
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      var startDate = $('#startDate');
			      var endDate = $("#endDate");
			      var status = $("#status");
			      var deliCode = $("#deliCode");
			      var createDate = $("#createDate");
			      var suppName = $("#suppName");
			      var mapgCode = $("#mapgCode");
			      var receUnit = $("#receUnit");
			      var deliType = $("#deliType");
			      //执行重载
			      table.reload('deliveryTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  startDate: startDate.val(),
			        	  endDate:endDate.val(),
			        	  status:status.val(),
			        	  deliCode:deliCode.val(),
			        	  createDate:createDate.val(),
			        	  suppName:suppName.val(),
			        	  mapgCode:mapgCode.val(),
			        	  deliType:deliType.val(),
			        	  receUnit:receUnit.val()
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  
	  
	//预约送货
	  $("#appoDeli").click(function(){
	  	var url="/getDeliveryAddHtml?deliType=1";
	  	//location=url;
	  	tuoGo(url,'deliveryAdd',"deliveryTableId");
	  });
	  //直发送货
	  $("#straDeli").click(function(){
		  var url="/getStraDeliAddHtml?deliType=0";
		  //location=url;
		  tuoGo(url,'deliStraAdd',"deliveryTableId");
	  });
	  //特殊送货
	  $("#specialDeli").click(function(){
		  var url="/getSpecialDeliAddHtml?deliType=2";
		  //location=url;
		  tuoGo(url,'specialDeliAdd',"deliveryTableId");
	  });
	  //重置
	  $("#reset").click(function(){
		  	$("#suppInfo").val('');
		  	$("#category").val('');
	  });
	  //作废
	  $("#cancellBut").click(function(){
		  var table = layui.table;
		  var checkStatus = table.checkStatus('deliveryTableId');
		  var data = checkStatus.data;
		  var length = data.length;
		  if(length != 0){
			  var deliIds = [];
			  var a=0;
			  for(var i=0;i<length;i++){
				  deliIds.push(data[i].deliId);
				  if(data[i].status !='已保存' && data[i].status !='已发货' && data[i].status !='已签到'){
					  a++;
				  }
			  }
			  if(a==0){
				  layer.confirm('作废选中的 '+length+' 条送货单吗？',{title:'送货单作废'},function(index){
					  var deliIdJson = JSON.stringify(deliIds);
					  $.ajax({
						  type:"post",
						  url:"/cancellDeliveryByDeliIds?deliIds="+deliIdJson,
						  dataType:"JSON",
						  success:function(data2){
							  debugger;
							  if(data2){
								  layer.msg('送货单作废成功', {time:2000 });
								  initDeliTable();
							  }else{
								  layer.alert('<span style="color:red;">作废失败</sapn>');
							  }
						  }
					  });
					  layer.close(index);
				  });
			  }else{
				  layer.alert('<span style="color:red;">只有"已保存"、"已发货"、"已签到"状态的送货单可以作废</sapn>');
			  }
		  }else{
			  layer.alert('<span style="color:red;">请选择一条或多条数据进行作废</sapn>');	
		  }
	  });
});

function initDeliTable(){
	table.render({
		  elem:"#deliveryTable",
		  url:"/queryDeliveryByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"deliveryTableId",
		  cols:[[
		     {checkbox: true, fixed:'left'},
		     {field:'deliType',title:'送货单类型', align:'center',width:81,templet:
		    	 function(d){
		    	 	var deliType = d.deliType;
		    	 	var str="";
		    	 	if(deliType == "0"){
		    	 		str = "直发送货";
		    	 	}else if(deliType == "1"){
		    	 		str = "预约送货";
		    	 	}else{
		    	 		str = "特殊送货";
		    	 	}
		    	 	return str;
		     	 }
		     },
		     {field:'status',title:'单据状态', align:'center',width:64},
		     {field:'deliCode',title:'送货单号', align:'center',width:113 },
		     {field:'deliDate', title:'送货日期',align:'center',width:97,templet:
		    	 function(d){
		    	    var date = new Date(d.deliDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					/*var h = date.getHours();
					var m = date.getMinutes();
					var s =date.getSeconds();*/
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		     },
		     {field:'suppName', title:'供应商',align:'center',width:237},
		     {field:'deliNumber',title:'实际送货数量', align:'center',width:83},
		     {field:'receUnit',title:'送货地点', align:'center',width:83},
		     {field:'mapgCode',title:'预约/直发单号', align:'center',width:113},
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
