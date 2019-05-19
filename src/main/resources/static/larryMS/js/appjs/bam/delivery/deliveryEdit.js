var tableData=[];
var startTableData=[];
var table;
var deliData;
var mapgCode2;
var suppId;
var paraData;
var mateName2;
var resiNumber;
var str;
var oldDeliNumber=0;
var type;
var fieldstr;
layui.use(['form','table'], function(){
	  var form = layui.form;
	  table = layui.table;
	  layer = layui.layer;
	  var $ = layui.$;
	  var oldCodeJudge = false;//预约单的下拉框中的值发生变化，则为true；
	  type = $("#type").val();
	  suppId = $("#suppId").val();
	  var oldCode = $("#oldCode").val();//转发到页面的预约单号，
	  //作用：判断下拉框中预约单号是否变化，根据有没有变化执行相应的添加规则
	  var deliId = $("#deliId").val();
	  mapgCode2=$("#mapgCode").val();
	  var suppr = $("#suppRange").val();
	  var supprd = $("#suppRangeDesc").val();
	  if(suppr != null && suppr != '' ){
		  $("#suppRange2").val(suppr +' - '+supprd);
	  }else{
		  $("#suppRange2").val('');
	  }
	   var str1=[
	  		   {title:"项次",field:"frequency",align:'center',width:77},
			   {field:"orderId",title:"采购订单号",align:'center',width:112},
			   {field:"mateCode",title:"物料编码",align:'center',width:150},
			   {field:"mateName",title:"物料名称",align:'center',width:200},
			   {field:"appoNumber",title:"预约数量",align:'center'},
			   {field:"unpaNumber",title:"订单未交量",align:'center'},
			   {field:"calculNumber",title:"订单可用量",align:'center'},
			   {field:"deliNumber",title:"实际送货量",align:'center',edit:'text',event:"setSign"},
			   {field:"unit",title:"单位",align:'center'},
			   {field:"prodPatchNum",title:"产品批号",align:'center',edit:'text'},
			   {field:"remark",title:"备注",align:'center',edit:'text'},
			   {fixed: 'right', title:'操作',width:120, align:'center', toolbar: '#barDemo'}
			];
	   var str2=[
	             {title:"项次",field:"frequency",align:'center',width:77},
	             {field:"orderId",title:"采购订单号",align:'center',width:112},
	             {field:"mateCode",title:"物料编码",align:'center',width:150},
	             {field:"mateName",title:"物料名称",align:'center',width:200},
	             {field:"appoNumber",title:"预约数量",align:'center'},
	             {field:"unpaNumber",title:"订单未交量",align:'center'},
	             {field:"calculNumber",title:"订单可用量",align:'center'},
	             {field:"deliNumber",title:"实际送货量",align:'center',edit:'text',event:"setSign"},
	             {field:"unit",title:"单位",align:'center'},
	             {field:"prodPatchNum",title:"产品批号",align:'center',edit:'text'},
	             {field:"remark",title:"备注",align:'center',edit:'text'},
	             ];
	   fieldstr = str1;
	   if(type=='2'){
		   fieldstr = str2;
	   }
	  $.ajax({
		 type:"post",
		 url:"/queryDeliMateByDeliId?deliId="+deliId,
	  	 dataType:"JSON",
	  	 success:function(data){
	  		 $.each(data,function(index,item){
	  			tableData.push(item); 
	  		 });
	  		 initTable(table,tableData);
	  	 },
	  	 error:function(){
	  		layer.alert("Connection error");
	  	 }
	   });
	    //返回
		 $("#goBack").click(function(){
		 	//window.history.back(-1);
			 tuoBack('.deliveryEdit','#serachSupp');
		  });
		 //打印
		 $("#printBut").click(function(){
			 var url  ="/getDeliPrintHtml?deliId="+deliId;
 			 location=url; 
		 });
		 //查看时不能编辑
		 if(type=='2'){
			 //$(".disa").attr("disabled",true);
			  $('.layui-form-select').eq(0).css('display','none');
			  $('#mapgCode').css('display','block');
			  $("#mapgCode").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
			  $("#mapgCode").attr("disabled",'disabled');
	          $(".disa").prop("readonly","readonly");	
		 }
		/*//监听工具条
		  table.on('tool(deliMateTableEvent)', function(obj){
			  var data = obj.data;
		    if(obj.event === 'add'){//添加
		    	//传到子页面的数据
		    	//debugger;
		    	var totalNum=0;
		    	var origNum=0;
		    	$.each(tableData,function(index,item){
		    		if(data.mateCode == item.mateCode){
		    			totalNum+=parseInt(item.deliNumber);
		    		}
		    	});
		    	var mapgCode = $("#mapgCode").val();
		    	$.ajax({
					  type:"post",
					  url:"/queryAppointByAppoCode?mapgCode="+mapgCode,
					  dataType:"JSON",
					  async: false,
					  success:function(map){
						  $.each(map.list,function(index2,item2){
							  if(item2.mateCode==data.mateCode){
								  origNum=parseInt(item2.mateNumber);
							  }
						  });
					  },
					  error:function(){
						  layer.alert("Connection error"); 
					  }
		    	});
		    	resiNumber=origNum-totalNum;
		    	console.info(resiNumber)
		    	mateName2 =data.mateName;
		    	paraData = {
		    			resiNumber:resiNumber,
			    		mateCode:data.mateCode,
			    		suppId:suppId,
						a:this.itemActive
			    }; 
		    	paraData.tableData=tableData;
		    	if(resiNumber>0){
		    		layer.open({
		    			type:2,
		    			title:"采购订单列表",
		    			shadeClose : true,
		    			shade : false,
		    			content : '/getOrderListHtml',
		    			area : [ '900px', '90%' ],
		    			maxmin : true, // 开启最大化最小化按钮
		    			btn: ['确认', '取消']
					       ,yes: function(index, layero){
					      //按钮【按钮一】的回调
					        debugger;
					      // 获取选中的物料数据
					      var checkedData = $(layero).find("iframe")[0].contentWindow
					          .getCheckedData();
					      // 关闭弹框
					      layer.close(index);
					      // 处理数据
					      deliveryMates(checkedData);
					     },
					     btn2: function(index, layero){
					      //按钮【按钮二】的回调
					      // 默认会关闭弹框
					      //return false 开启该代码可禁止点击该按钮关闭
					     }
		    		});
		    	}else{
		    		layer.msg("该物料不需要额外添加订单了");
		    	}
		    	
		    } else if(obj.event === 'rem'){//删除
		    	debugger;
		    	var count=0;
		    	var id = data.id;
		    	if(id!= null && id != ''){
   			    tableData.remove(data);
   			    initTable(table,tableData);
   			    statis();
		    	}else{
		    		layer.msg("不能删除");
		    	}
		    }
		  });*/
		 //监听工具条
		  table.on('tool(deliMateTableEvent)', function(obj){
			  var data = obj.data;
				if (obj.event === 'add') {// 添加
					var suppRange = $("#suppRange").val();
					var mateCode = data.mateCode;
					var orderId = data.orderId;
					var MaxSort = data.sort;// 相同物料中最新采购订单日期
					var remark = data.remark;
					$.each(tableData,function(index,item) {
							if (data.mateCode == item.mateCode) {
								var itemSort = item.sort
								MaxSort = MaxSort > itemSort ? MaxSort : itemSort;
							}
					});
					if(MaxSort != data.sort){
						layer.msg("不能添加,请操作最新添加的操作列");
						return;
					}
					// 传到子页面的数据
					//var totalNum = 0;
					var mateNum = 0;//计算无聊有几个采购订单与它关联
					var Numcount = 0
					var str ="";
					$.each(tableData,function(index,item) {
							if (mateCode == item.mateCode) {
								//totalNum += parseFloat(item.deliNumber);
								var calculNumber =  item.calculNumber;
								var deliNumber = item.deliNumber;
								if(deliNumber != calculNumber){
									str +="物料："+mateCode+"所在的采购订单："+item.orderId+"实际送货数量不等于未交数量,"
									Numcount++;
								}
								mateNum++;
							}
					});
					str+="不能添加。";
					if(Numcount != 0){//实际送货数量不等于未交数量,不能添加
						layer.alert(str);
						return ;
					}
					var count = mateNum + 1;
					layer.load();
					$.ajax({
								type : "post",
								url : "/queryOrderBySuppIdAndMateCode",
								data : {
									suppId:suppId,
									mateCode:mateCode,
									num:count,
									oldCodeJudge:oldCodeJudge,
									orderId:orderId,
									suppRange:suppRange
								},
								dataType : "JSON",
								async : true,
								success : function(data2) {
									layer.closeAll();
									if (data2.judge) {
										var deliMate = data2.deliMate;
										var id = getMyId();
										var tableData2 = [];
										$.each(tableData,function(index,item) {
												tableData2.push(item);
												if (data.mateCode == item.mateCode) {
													if (item.sort == MaxSort) {
														var arr = {
															id : id,
															orderId : deliMate.orderId,
															unit : deliMate.unit,
															mateCode : data.mateCode,
															mateName : data.mateName,
															frequency : deliMate.frequency,
															subeDate : deliMate.subeDate,
															appoNumber : deliMate.appoNumber,
															unpaNumber : deliMate.unpaNumber,
															calculNumber : deliMate.calculNumber,
															deliNumber : deliMate.deliNumber,
															remark : remark,
															sort : parseInt(MaxSort) + 1,
															addCate :"own",
														};
														tableData2.push(arr);
													}
												}
										});
										tableData = tableData2;
										initTable(table,tableData);
										statis();
									} else {
										layer.alert(data2.msg);
									}
								},
								error : function() {
									layer.alert("Connection error");
								}
						});
			  } else if(obj.event === 'rem'){//删除
				  debugger;
				  var addCate = data.addCate;
				  if(addCate=="own"){
					  //当前物料的日期
					  /*var MaxorderDate = data.subeDate;//相同物料中最新采购订单日期
					  $.each(tableData,function(index,item){
						  if(data.mateCode == item.mateCode){
							  var itemOrderDate = item.subeDate
							  MaxorderDate = MaxorderDate > itemOrderDate ?  MaxorderDate:itemOrderDate;
						  }
					  });*/
					  var MaxSort = data.sort;// 相同物料中最新采购订单日期
						$.each(tableData,function(index,item) {
								if (data.mateCode == item.mateCode) {
									var itemSort = item.sort
									MaxSort = MaxSort > itemSort ? MaxSort : itemSort;
								}
						});
						debugger;
					  if(data.sort == MaxSort){
						  tableData.remove(data);
						  initTable(table,tableData);
						  statis();
					  }else{
						  layer.msg("不能删除,请操作最新添加的操作列");
					  }
					  
				  }else{
					  layer.msg("不能删除,请操作最新添加的操作列");
				  }
				  
			  }else if(obj.event === "setSign"){
				  oldDeliNumber = data.deliNumber;
			  }
		  });
		  table.on('edit(deliMateTableEvent)', function(obj){ //注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"
			  if(obj.field == "deliNumber"){
				  //debugger;
				  var data = obj.data;
				  var MaxorderDate = data.subeDate;//相同物料中最新采购订单日期
				  $.each(tableData,function(index,item){
					  if(data.mateCode == item.mateCode){
						  var itemOrderDate = item.subeDate
						  MaxorderDate = MaxorderDate > itemOrderDate ?  MaxorderDate:itemOrderDate;
					  }
				  });
				  if(data.subeDate == MaxorderDate){
					  if(isNaN(obj.value)){
						  layer.msg("请输入有效数字!");
						  data[obj.field] = oldDeliNumber;
						  initTable(table,tableData);
					  }else{
							var result  = isInteger(parseFloat(obj.value));
							if(!result ){
								layer.msg("请输入大于等于零的整数!");
								data[obj.field] = oldDeliNumber;
								initTable(table,tableData);
							}
						}
					  var calculNumber = parseFloat(data.calculNumber);
					  var deliNumber = parseFloat(data.deliNumber);
					  if(deliNumber>calculNumber){
						  layer.alert("实际送货量不能大于订单的未交数量!");
						  data.deliNumber = oldDeliNumber;
						  initTable(table,tableData);
					  }else{
						  var number=parseFloat(0);
						  $.each(tableData,function(index,item){
							  var num = item.deliNumber;
							  if(num==''){
								  num=0;
							  }
							  number+=parseFloat(num);
						  });
						  $("#deliNumber").val(number.toFixed(3));
					  }
				  }else{
					  data.deliNumber = oldDeliNumber;
					  initTable(table,tableData);
					  layer.msg("该订单不能修改,只能修改最新添加的订单物料数量");
				  }
			  }
 			 });
	   //保存
	   $("#saveDeli").click(function(){
		   //获取layui 编辑后的table表格中的所有数据
		   var mateData=[];
		   var data = deliData.config.data;
		   var length = data.length;
		   if(length == 0){
			   layer.msg("物资信息表为空，无法保存");
			   return;
		   }
		   for(var i =0;i<data.length;i++){
			   if(parseFloat(data[i].deliNumber)!=0){
				   mateData.push(data[i])
			   }
		   }
		   $("#deliMateData").val(JSON.stringify(mateData));
		   var formData = $("#deliveryForm").serialize();
		   $.ajax({
			   type : "POST",
			   url : '/updateDelivery?subtype=add&mapgCode2='+mapgCode2,
			   data : formData,
			   dataType:"JSON",
			   async: false,
			   error : function(request) {
				   layer.alert("Connection error");
			   },
			   success : function(data) {
				   if(data){
					   layer.msg("预约送货保存成功");
					   //window.history.back(-1);
					   tuoBack('.deliveryEdit','#serachSupp');
				   }else{
					   layer.alert("预约送货保存失败");
				   }
			   }
		   });
		   
	   });
	   //提交
	   $("#submitDeli").click(function(){
		   //获取layui 编辑后的table表格中的所有数据
		   var mateData=[];
		   var data = deliData.config.data;
		   var length = data.length;
		   if(length == 0){
			   layer.msg("物资信息表为空，无法提交");
			   return;
		   }
		   for(var i =0;i<data.length;i++){
			   if(parseFloat(data[i].deliNumber)!=0){
				   mateData.push(data[i])
			   }
		   }
		   $("#deliMateData").val(JSON.stringify(mateData));
		   var formData = $("#deliveryForm").serialize();
		   $.ajax({
			   type : "POST",
			   url : "/updateDelivery?subtype=sub&mapgCode2="+mapgCode2,
			   data : formData,
			   dataType:"JSON",
			   async: false,
			   error : function(request) {
				   layer.alert("Connection error");
			   },
			   success : function(data) {
				   if(data){
					   layer.msg("预约送货提交成功");
					   //window.history.back(-1);
					   tuoBack('.deliveryEdit','#serachSupp');
				   }else{
					   layer.alert("预约送货提交失败");
				   }
			   }
		   });
	   });
	   
	   function statis(){
		   	 var number=parseFloat(0);
		   	 $.each(tableData,function(index,item){
		   		 var num = item.deliNumber;
		   		   if(num==''){
		   			   num=0;
		   		   }
		   		   number+=parseFloat(num);
		   	 });
		   	 $("#deliNumber").val(number.toFixed(3));
		}
	   
	 //预约单号select下拉框改变值触发
     //根据根据预约单号查询这个预约单号的信息
     form.on("select(mapgCode)",function(obj){
    	 oldCodeJudge = true;
    	 var mapgCode = obj.value;
    	 var deliCode = $("#deliCode").val();
			if (mapgCode != null && mapgCode != "") {
				layer.load();
				$.ajax({
					type : "get",
					url : "/recommendPurchaseOrder?mapgCode="
							+ mapgCode+"&deliCode="+deliCode,
					dataType : "JSON",
					async : true,
					success : function(res) {
						debugger;
						layer.closeAll();
						var msg = res.msg;
						if (msg == '' || msg == undefined) {
							var appoint = res.appoint;
							 suppId=appoint.suppId;
							 $("#suppId").val(appoint.suppId);
							 $("#suppName").val(appoint.suppName);
							 $("#affirmDate").val(appoint.affirmDate);
							 $("#appoNumber").val(appoint.mateNumber);
							 $("#deliAmount").val(appoint.mateAmount);
							 $("#truckNum").val(appoint.truckNum);
							 $("#receUnit").val(appoint.receUnit);
							 $("#receAddr").val(appoint.receAddr);
							 $("#contact").val(appoint.contact);
							 $("#phone").val(appoint.phone);
							 $("#post").val(appoint.post);
							 $("#suppRange").val(appoint.suppRange);
							 $("#suppRangeDesc").val(appoint.suppRangeDesc);
							 if(appoint.suppRange != null && appoint.suppRange !=''){
								 $("#suppRange2").val(appoint.suppRange+' - '+appoint.suppRangeDesc);
							 }else{
								 $("#suppRange2").val("");
							 }
							 var now = new Date(appoint.appoDate);  
							 //格式化日，如果小于9，前面补0  
							 var day = ("0" + now.getDate()).slice(-2);  
							 //格式化月，如果小于9，前面补0  
							 var month = ("0" + (now.getMonth() + 1)).slice(-2);  
							 //拼装完整日期格式  
							 var appoday = now.getFullYear()+"-"+(month)+"-"+(day) ;  
							 //完成赋值  
							 $("#deliDate").val(appoday);
							tableData = res.deliMates;
							statis();
							initTable(table, tableData);
							var str = res.str;
							if(str != undefined && str != ""){
								layer.alert(str+"订单可用量为零");
							}
						} else {
							$("#suppId").val("");
							$("#suppName").val("");
							$("#affirmDate").val("");
							$("#appoNumber").val("");
							$("#deliAmount").val("");
							$("#truckNum").val("");
							$("#deliDate").val("");
						    $("#receUnit").val("");
							$("#receAddr").val("");
							$("#contact").val("");
							$("#phone").val("");
							$("#post").val("");
							$("#suppRange").val("");
							$("#suppRangeDesc").val("");
							$("#suppRange2").val("");
							tableData=[];
							statis();
							initTable(table, tableData);
							layer.alert(msg);
						}
					}
				});
			} else {
				layer.alert("请选择预约单号");
			}
     });
     
   //修改送货单状态
	 $("#editStatus").click(function(){
		 var deliCode = $("#deliCode").val();
		 var status = $("#status").val();
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
	  			 type:'delivery'
	  		 },
	  		 dataType:"JSON",
	  		 success:function(data2){
	  			 if(data2){
	  				 layer.msg('修改成功', {time:2000 });
	  				 $("#status").val(map.status);
	  			 }else{
	  				 layer.alert('<span style="color:red;">修改失败</sapn>');
	  			 }
	  		 }
	  	  });
	  }
		
});

//初始化预约申请物资表格
function initTable(table,data){
	 deliData = table.render({
		elem:"#deliMateTable",
		data:data,
		id:"deliMateTableId",
		limit:100,
		cols:[fieldstr]
		
	});
    if(type == '2'){
		 $('table td').removeAttr('data-edit');
    }
}

//弹出框子页面调用父页面方法传递数据
function deliveryMates(data){
	for(var i=0;i<data.length;i++){
		var unpa=data[i].unpaQuan;
		var min =resiNumber>=unpa?unpa:resiNumber;
		//创建table的行，赋值，添加到table中
		var id = getMyId();
		var arr = {
				id:id+i,
				orderId : data[i].contOrdeNumb,
				unit : data[i].company,
				mateName : mateName2,
				mateCode : data[i].mateNumb,
				frequency : data[i].frequency
		};
		if(min>=resiNumber){
			arr.deliNumber=resiNumber;
			resiNumber=0;
		}else{
			arr.deliNumber=min;
			resiNumber=resiNumber-min;
		}
		tableData.push(arr);
	
    }
	var number=parseFloat(0);  
	for(var j=0;j<tableData.length;j++){
		var num = tableData[j].deliNumber;
		   if(num==''){
			   num=0;
		   }
		   number+=parseFloat(num);
	}
	$("#deliNumber").val(number.toFixed(3));
	initTable(table, tableData);
		
}

//生成唯一编码
function getMyId(){
	var mydate = new Date();
	var uuid = "srm"+mydate.getDay()+ mydate.getHours()+ mydate.getMinutes()+mydate.getSeconds()+mydate.getMilliseconds();
	return uuid;
}


Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].id == val.id) {
			this.splice(k, 1);
			return;
		}
	}
};
//判断是不是整数
function isInteger(obj){  
    return  obj>=0;  //是大于等于零的整数，则返回true，否则返回false  

} 
