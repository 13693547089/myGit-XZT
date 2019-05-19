var table;
layui.use(['table','laydate','element'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  var element = layui.element;
	  
	  $("#serachSupp").click(function(){
		  loadData();
	  });
	  function loadData(){
		  var appoCode = $("#appoCode").val(); 
			 var messCode = $("#messCode").val(); 
			 var deliCode = $("#deliCode").val(); 
			 var receCode = $("#receCode").val();
			 if(!((appoCode != '' && appoCode != undefined 
					 && (messCode == '' || messCode == undefined)
					 && (deliCode == '' || deliCode == undefined) 
					 && (receCode == '' || receCode == undefined))
					 || (messCode != '' && messCode != undefined 
							 && (appoCode == '' || appoCode == undefined) 
							 && (deliCode == '' || deliCode == undefined) 
							 && (receCode == '' || receCode == undefined))
							 || (deliCode != '' && deliCode != undefined 
									 && (messCode == '' || messCode == undefined) 
									 && (appoCode == '' || appoCode == undefined) 
									 && (receCode == '' || receCode == undefined))
									 || (receCode != '' && receCode != undefined 
											 && (messCode == '' || messCode == undefined) 
											 && (deliCode == '' || deliCode == undefined) 
											 && (appoCode == '' || appoCode == undefined)))){
				 layer.msg("请确认只有一个单号进行搜索");
				 return;
			 }
			 var code='';
			 var codeDesc = '';
			 if(appoCode != '' && appoCode != undefined){
				 //预约单号
				 code = appoCode;
				 codeDesc = 'appoint'
			 }
			 if(messCode != '' && messCode != undefined){
				 //直发通知单号
				 code = messCode;
				 codeDesc = 'straMess'
			 }
			 if(deliCode != '' && deliCode != undefined){
				 //送货单号
				 code = deliCode;
				 codeDesc = 'delivery'
			 }
			 if(receCode != '' && receCode != undefined){
				 //收货单号
				 code = receCode;
				 codeDesc = 'receive'
			 }
			 //切换tap选项卡
			 element.tabChange('demo', codeDesc);
			 $.ajax({
	    		 type:"post",
	    		 url:"/getAppoDeliData",
	    		 data:{
	    			 code:code,
	    			 codeDesc:codeDesc
	    		 },
	    		 dataType:"JSON",
	    		 success:function(data2){
	    			 console.info(data2);
	    			//处理预约单信息
	    			 var appo = data2.appoint;
	    			 var appoCarList = data2.appoCarList;
	    			 dealAppoint(appo,appoCarList);
	    			 //处理直发单信息
	    			var stra =  data2.straMessage;
	    			 dealStraMess(stra);
	    			 //处理送货单信息
	    			 var deliveryList = data2.deliveryList;
	    			 dealDelivery(deliveryList);
	    			 //处理收货单信息
	    			 var receiveList = data2.receiveList;
	    			 dealReceive(receiveList);
	    		 }
	    	  });
	  }
	//处理预约单信息
	  function dealAppoint(appo,appoCarList){
		  if(appo != null && appo != undefined){
			  debugger;
				 var appoMatesList = appo.appoMates;
				 $("#appoCode2").val(appo.appoCode);
				 $("#appoStatus").val(appo.appoStatus);
				 var createDate = dealDate(appo.createDate);
				 $("#createDate").val(createDate);
				 $("#suppName").val(appo.suppName);
				 var appoDate = dealDate(appo.appoDate);
				 $("#appoDate").val(appoDate);
				 $("#citeAppo").val(appo.citeAppo);
				 $("#expectDate").val(appo.expectDate);
				 $("#affirmDate").val(appo.affirmDate);
				 $("#receUnit").val(appo.receUnit);
				 $("#receAddr").val(appo.receAddr);
				 $("#contact").val(appo.contact);
				 $("#phone").val(appo.phone);
				 $("#truckNum").val(appo.truckNum);
				 $("#mateNumber").val(appo.mateNumber);
				 $("#mateAmount").val(appo.mateAmount);
				 if(appo.suppRange != null && appo.suppRange !=''){
					 $("#suppRange").val(appo.suppRange +" - "+appo.suppRangeDesc);
				 }else{
					 $("#suppRange").val('');
				 }
				 initCarTable(table,appoCarList);
				 initTable(table,appoMatesList);
		   }else{
			     $("#appoCode2").val("");
				 $("#appoStatus").val("");
				 $("#createDate").val("");
				 $("#suppName").val("");
				 $("#appoDate").val("");
				 $("#citeAppo").val("");
				 $("#expectDate").val("");
				 $("#affirmDate").val("");
				 $("#receUnit").val("");
				 $("#receAddr").val("");
				 $("#contact").val("");
				 $("#phone").val("");
				 $("#truckNum").val("");
				 $("#mateNumber").val("");
				 $("#mateAmount").val("");
				 $("#suppRange").val('');
			   initCarTable(table,[]);
			   initTable(table,[]);
		   }
	  }
	  initCarTable(table,[]);
	  initTable(table,[]);
	  //处理直发单信息
	  function dealStraMess(stra){
		  debugger;
		  if(stra != null && stra != undefined){
				var straMateList = stra.messMates;
				$("#messCode2").val(stra.messCode);
				$("#messStatus").val(stra.messStatus);
				 var createDate = dealDate(stra.createDate);
				$("#createDate2").val(createDate);
				$("#suppName2").val(stra.suppId);
				$("#zzoem").val(stra.zzoem);
				var deliveryDate = dealDate(stra.deliveryDate);
				$("#deliveryDate").val(deliveryDate);
				var arriDate = dealDate(stra.arriDate);
				$("#arriDate").val(arriDate);
				$("#receUnit2").val(stra.receUnit);
				$("#receAddr2").val(stra.receAddr);
				$("#contact2").val(stra.contact);
				$("#phone2").val(stra.phone);
				$("#mateNumber2").val(stra.mateNumber);
				$("#mateAmount2").val(stra.mateAmount);
				if(stra.suppRange != null && stra.suppRange !=''){
					 $("#suppRange2").val(stra.suppRange +" - "+stra.suppRangeDesc);
				 }else{
					 $("#suppRange2").val('');
				 }
				initStraMessTable(table, straMateList);
		  }else{
			    $("#messCode2").val("");
			    $("#messStatus").val("");
				$("#createDate2").val("");
				$("#suppName2").val("");
				$("#zzoem").val("");
				$("#deliveryDate").val("");
				$("#arriDate").val("");
				$("#receUnit2").val("");
				$("#receAddr2").val("");
				$("#contact2").val("");
				$("#phone2").val("");
				$("#mateNumber2").val("");
				$("#mateAmount2").val("");
				$("#suppRange2").val('');
			  initStraMessTable(table, []);
		  }
	  }	
	  initStraMessTable(table, []);
	 //处理送货单信息
	  function dealDelivery(deliveryList){
		  initDeliTable(table,deliveryList);
	  }
	  initDeliTable(table,[]);
	 //处理收货单信息
	  function dealReceive(receiveList){
		  initReceTable(table,receiveList);
	  }
	  initReceTable(table,[]);
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
			      
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  

	  //重置
	  $("#reset").click(function(){
		  $(".checked").each(function(index,row){
				$(this).removeClass("checked");
				$(this).addClass("uncheck");
				$(this).css('color','gray');
		  });
		  $(".checked2").each(function(index,row){
			  $(this).removeClass("checked2");
			  $(this).addClass("uncheck2");
			  $(this).css('color','gray');
		  });
		  $(".checked3").each(function(index,row){
			  $(this).removeClass("checked3");
			  $(this).addClass("uncheck3");
			  $(this).css('color','gray');
		  });
	  });
	 
	  //监听送货列表的工具条
	  table.on('tool(deliveryTableDemo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
	    	if(data.deliType=="1"){//预约
    			var url  ="/getAppoDeliveryCheckHtml?deliId="+data.deliId+"&type=2";
    			//location=url;
    			tuoGo(url,'deliveryEdit',"deliveryTableId");
    		}else if(data.deliType=="0"){//直发
    			var url  ="/getstraMessDeliveryCheckHtml?deliId="+data.deliId+"&type=2";
    			//location=url;
    			tuoGo(url,'deliStraEdit',"deliveryTableId");
    		}else if(data.deliType=='2'){//特殊
    			var url  ="/getSpecialDeliveryCheckHtml?deliId="+data.deliId+"&type=2";
    			//location=url;
    			tuoGo(url,'specialDeliEdit',"deliveryTableId");
    		}
	    }  else if(obj.event === 'edit'){//编辑状态
	    	 var deliCode = data.deliCode;
			 var status = data.status;  
	    	 appoData = {
					  appoCodeDesc:'送货单号',
					  appoStatusDesc : '送货单状态',
					  a : this.itemActive
			  };
			  layer.open({
				  type : 2,
				  title : '编辑送货单状态',
				  shadeClose : false,
				  shade : 0.1,
				  content : '/getEditStatusHtml?code='+deliCode+'&status='+status+'&type=delivery',
				  area : [ '600px', '70%' ],
				  maxmin : true, // 开启最大化最小化按钮
				  btn: ['确认', '取消']
			  ,yes: function(index, layero){
				  //按钮【按钮一】的回调
				  // 获取选中的物料数据
				  var map = $(layero).find("iframe")[0].contentWindow
				  .getVersion();
				  if(map.code == undefined || map.code == ""){
					  layer.msg("请填写状态！",{time:2000});
					  return;
				  }
				  if(map.judge){
					  // 关闭弹框
					  layer.close(index);
					  // 处理数据
					  debugger;
					  map.type = 'delivery';
					  updateStatus(map);
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
	    }
	  });
	  //监听收货列表的工具条
	  table.on('tool(receiveTableDemo)', function(obj){
		  var data = obj.data;
		  if(obj.event === 'check'){//查看
			  var url  ="/getReceiveCheckHtml?receId="+data.receId+"&funType=2";
			  tuoGo(url,'receiveEdit',"receiveTableId");
		  }  else if(obj.event === 'edit'){//编辑状态
			  var receCode = data.receCode;
				 var status = data.status;  
		    	 appoData = {
						  appoCodeDesc:'收货单号',
						  appoStatusDesc : '收货单状态',
						  a : this.itemActive
				  };
				  layer.open({
					  type : 2,
					  title : '编辑收货单状态',
					  shadeClose : false,
					  shade : 0.1,
					  content : '/getEditStatusHtml?code='+receCode+'&status='+status+'&type=receive',
					  area : [ '600px', '70%' ],
					  maxmin : true, // 开启最大化最小化按钮
					  btn: ['确认', '取消']
				  ,yes: function(index, layero){
					  //按钮【按钮一】的回调
					  // 获取选中的物料数据
					  var map = $(layero).find("iframe")[0].contentWindow
					  .getVersion();
					  if(map.code == undefined || map.code == ""){
						  layer.msg("请填写状态！",{time:2000});
						  return;
					  }
					  if(map.judge){
						  // 关闭弹框
						  layer.close(index);
						  // 处理数据
						  debugger;
						  map.type = 'receive';
						  updateStatus(map);
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
		  }
	  });

	  //预约单修改状态
	  $("#editAppoStatus").click(function(){
		  debugger;
		  var appoCode = $("#appoCode2").val();
		  if(appoCode === '' || appoCode === undefined){
			  layer.msg('无预约单信息,不可编辑');
			  return;
		  }
		  var appoStatus = $("#appoStatus").val();
		  appoData = {
				    appoCodeDesc:'预约单号',
					appoStatusDesc : '预约单状态',
					a : this.itemActive
				};
		  layer.open({
				type : 2,
				title : '编辑预约单状态',
				shadeClose : false,
				shade : 0.1,
				content : '/getEditStatusHtml?code='+appoCode+'&status='+appoStatus+'&type=appoint',
				area : [ '600px', '70%' ],
				maxmin : true, // 开启最大化最小化按钮
				btn: ['确认', '取消']
			       ,yes: function(index, layero){
			      //按钮【按钮一】的回调
			      // 获取选中的物料数据
			      var map = $(layero).find("iframe")[0].contentWindow
			          .getVersion();
			      if(map.code == undefined || map.code == ""){
			    	  layer.msg("请填写状态！",{time:2000});
			    	  return;
			      }
			      if(map.judge){
			    	  // 关闭弹框
			    	  layer.close(index);
			    	  // 处理数据
			    	  debugger;
			    	  map.type = 'appoint';
			    	  updateStatus(map);
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
	  });
	  //直发通知单修改状态
	  $("#editStraMessStatus").click(function(){
		  debugger;
		  var messCode2 = $("#messCode2").val();
		  if(messCode2 === '' || messCode2 === undefined){
			  layer.msg('无直发单信息,不可编辑');
			  return;
		  }
		  var messStatus = $("#messStatus").val();
		  appoData = {
				  appoCodeDesc:'直发单号',
				  appoStatusDesc : '直发单状态',
				  a : this.itemActive
		  };
		  layer.open({
			  type : 2,
			  title : '编辑直发单状态',
			  shadeClose : false,
			  shade : 0.1,
			  content : '/getEditStatusHtml?code='+messCode2+'&status='+messStatus+'&type=straMess',
			  area : [ '600px', '70%' ],
			  maxmin : true, // 开启最大化最小化按钮
			  btn: ['确认', '取消']
		  ,yes: function(index, layero){
			  //按钮【按钮一】的回调
			  // 获取选中的物料数据
			  var map = $(layero).find("iframe")[0].contentWindow
			  .getVersion();
			  if(map.code == undefined || map.code == ""){
				  layer.msg("请填写状态！",{time:2000});
				  return;
			  }
			  if(map.judge){
				  // 关闭弹框
				  layer.close(index);
				  // 处理数据
				  debugger;
				  map.type = 'straMess';
				  updateStatus(map);
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
	  });
	  
	//修改状态
	  function updateStatus(map){
	  	 $.ajax({
	  		 type:"post",
	  		 url:"/updateStatus",
	  		 data:{
	  			 status:map.status,
	  			 code:map.code,
	  			 type:map.type
	  		 },
	  		 dataType:"JSON",
	  		 success:function(data2){
	  			 if(data2){
	  				 layer.msg('修改成功', {time:2000 });
	  				 var type = map.type;
	  				if("appoint"==type) {
	  					 $("#appoStatus").val(map.status);
	  				}else if("straMess"==type){
	  					 $("#messStatus").val(map.status);
	  				}else if("delivery"==type){
	  					 loadData();
	  				}else if("receive"==type) {
	  					 loadData();
	  				}
	  			 }else{
	  				 layer.alert('<span style="color:red;">修改失败</sapn>');
	  			 }
	  		 }
	  	  });
	  }
});

//初始化预约申请物资表格
function initCarTable(table,data){
	table.render({
		elem:"#appoCarTable",
		data:data,
		id:"appoCarTableId",
		width:1329,
		limit:100,
		cols:[[
		   {checkbox:true,align:'center'},
		   {field:"carType",title:"车型",align:'center'},
		   {field:"carNumber",title:"数量(辆)",align:'center'},
		   {field:"remark",title:"备注",align:'center'}
		]]
		
	});
}
//初始化预约申请物资表格
function initTable(table,data){
	table.render({
		elem:"#appoMateTable",
		data:data,
		id:"appoMateTableId",
		width:1329,
		limit:100,
		cols:[[
		   {checkbox:true,align:'center'},
		   {field:"mateName",title:"物料名称",align:'center'},
		   {field:"mateCode",title:"物料编码",align:'center'},
		   {field:"mateNumber",title:"数量(箱)",align:'center'},
		   {field:"mateAmount",title:"实际方量",align:'center'},
		   {field:"predAmount",title:"预计方量",align:'center'},
		   {field:"remark",title:"备注",align:'center'},
		]]
		
	});
}


//初始化直发通知单物资表格
function initStraMessTable(table, data) {
	table.render({
		elem : "#MessmateTable",
		data : data,
		id : "MessmateTableId",
		limit : 100,
		cols : [ [ {
			field : "orderId",
			title : "调拨单号",
			align : 'center',
			width : 100
		}, {
			field : "mateName",
			title : "成品",
			align : 'center'
		}, {
			field : "mateCode",
			title : "成品编码",
			align : 'center'
		}, {
			field : "mateNumber",
			title : "调拨数量(箱)",
			align : 'center'
		}, {
			field : "poId",
			title : "采购订单",
			align : 'center',
			width : 100
		}, {
			field : "semiMateName",
			title : "半成品",
			align : 'center'
		}, {
			field : "semiMateCode",
			title : "半成品编码",
			align : 'center'
		}, {
			field : "unpaNumber",
			title : "订单未交量",
			align : 'center',
			width : 120
		}, {
			field : "calculNumber",
			title : "订单可用量",
			align : 'center',
			width : 120
		}, {
			field : "semiMateNumber",
			title : "数量(箱)",
			align : 'center',
			width : 120
		}, {
			field : "semiMateAmount",
			title : "方量",
			align : 'center',
			width : 80,
		} ] ]

	});
}

//送货单列表
function initDeliTable(table,data){
	table.render({
		  elem:"#deliveryTable",
		  data:data,
		  width: '100%',
		  minHeight:'20px',
		  limit:100,
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
		     {fixed: 'right', title:'操作',width:150, align:'center', toolbar: '#delibarDemo'}
		  ]]
	  })
}
//收货单列表
function initReceTable(table,data){
	table.render({
		  elem:"#receiveTable",
		  data:data,
		  width: '100%',
		  minHeight:'20px',
		  limit:100,
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
		     {field:'suppName', title:'供应商',align:'center',width:190},
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
		     {fixed: 'right', title:'操作',width:150, align:'center', toolbar: '#recebarDemo'}
		  ]]
	  })
}

function dealDate(date){
	if(date != '' && date != undefined){
		var now = new Date(date);  
		//格式化日，如果小于9，前面补0  
		var day = ("0" + now.getDate()).slice(-2);  
		//格式化月，如果小于9，前面补0  
		var month = ("0" + (now.getMonth() + 1)).slice(-2);  
		//拼装完整日期格式  
		var d = now.getFullYear()+"-"+(month)+"-"+(day);
		return d;
	}else{
		return "";
	}
}