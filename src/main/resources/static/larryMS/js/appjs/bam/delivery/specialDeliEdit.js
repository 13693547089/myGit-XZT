var tableData=[];
var startTableData=[];
var table;
var deliData;
var judgeCode =false;
var  suppId;
var paraData;
var mateName2;
var appoNumber2;
var mapgCode2;
//其中一个物料的剩余数量
var resiNumber;
var oldDeliNumber=0;
var mate={};
var type;
var fieldstr;
var  suppId;
layui.use(['form','table'], function(){
	  var form = layui.form;
	  table = layui.table;
	  layer = layui.layer;
	  var $ = layui.$;
	  suppId = $("#suppId").val();
	  type = $("#type").val();
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
				   {field:"mateName",title:"物料名称",align:'center',width:223},
				   {field:"appoNumber",title:"预约数量",width :97,align:'center'},
				   {field:"unpaNumber",title:"订单未交量",width :97,align:'center'},
				   {field:"calculNumber",title:"订单可用量",width :97,align:'center'},
				   {field:"deliNumber",title:"实际送货量",width :97,align:'center',edit:'text',event:"setSign"},
				   {field:"unit",title:"单位",align:'center'},
				   {field:"prodPatchNum",title:"产品批号",align:'center',edit:'text'},
				   {field:"remark",title:"备注",align:'center',edit:'text'},
				   {fixed: 'right', title:'操作',width:120, align:'center', toolbar: '#barDemo'}
				];
		   var str2=[
		             {title:"项次",field:"frequency",align:'center',width:77},
		             {field:"orderId",title:"采购订单号",align:'center',width:112},
		             {field:"mateCode",title:"物料编码",align:'center',width:150},
		             {field:"mateName",title:"物料名称",align:'center',width:223},
		             {field:"appoNumber",title:"预约数量",width :97,align:'center'},
		             {field:"unpaNumber",title:"订单未交量",width :97,align:'center'},
		             {field:"calculNumber",title:"订单可用量",width :97,align:'center'},
		             {field:"deliNumber",title:"实际送货量",width :97,align:'center',edit:'text',event:"setSign"},
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
			 tuoBack('.specialDeliEdit','#serachSupp');
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
		 //监听工具条
		  table.on('tool(deliMateTableEvent)', function(obj){
			  var data = obj.data;
		    if(obj.event === 'add'){//添加
		    	//传到子页面的数据
		    	var totalNum=parseFloat(0);
		    	var origNum=parseFloat(0);
		    	$.each(tableData,function(index,item){
		    		if(data.mateCode == item.mateCode){
		    			var deliNumber = item.deliNumber;
						  if(deliNumber ==''){
							  deliNumber = 0;
						  }
		    			totalNum+=parseFloat(deliNumber);
		    		}
		    	});
		    	var mapgCode = $("#mapgCode").val();
		    	origNum = data.appoNumber;
		    	resiNumber=origNum-totalNum;
		    	if(resiNumber<0){
		    		resiNumber=0;
		    	}
		    	mateName2 =data.mateName;
		    	appoNumber2 = data.appoNumber;
		    	var suppRange = $("#suppRange").val();
		    	paraData = {
		    			resiNumber:resiNumber,
			    		mateCode:data.mateCode,
			    		suppId:suppId,
			    		suppRange:suppRange,
						a:this.itemActive
			    }; 
		    	paraData.tableData=tableData;
		    	
		    	if(data.orderId==''){
		    		mate=data;
		    	}
		    	   layer.open({
		    			type:2,
		    			title:"采购订单列表",
		    			shadeClose : false,
		    			shade : 0.1,
		    			content : '/getOrderListHtml',
		    			area : [ '900px', '90%' ],
		    			maxmin : true, // 开启最大化最小化按钮
		    			btn: ['确认', '取消']
					       ,yes: function(index, layero){
					      //按钮【按钮一】的回调
					      // 获取选中的物料数据
					      var checkedData = $(layero).find("iframe")[0].contentWindow
					          .getCheckedData();
					      // 关闭弹框
					      layer.close(index);
					      // 处理数据
					      if(checkedData.length != 0){
					    	  deliveryMates(checkedData);
					      }
					     },
					     btn2: function(index, layero){
					      //按钮【按钮二】的回调
					      // 默认会关闭弹框
					      //return false 开启该代码可禁止点击该按钮关闭
					     }
		    		});
		    	   
		    	
		    } else if(obj.event === 'rem'){//删除
		    	var MinSort = data.sort;//相同物料中最小的排序sort
				$.each(tableData,function(index,item){
					if(data.mateCode == item.mateCode){
						var itemSort = item.sort
						MinSort = MinSort < itemSort ?  MinSort:itemSort;
					}
				});
				if(data.sort == MinSort){
					if(data.orderId !=''){
						layer.confirm('该物料不能删除,是否需要修改该物料的采购订单？', function(index){
							$.each(tableData,function(index,item){
								if(data.id == item.id){
									item.orderId='';
									item.unit='';
									item.frequency='';
									item.subeDate='';
									item.unpaNumber='';
									item.calculNumber='';
									item.deliNumber='';
								}
							});
							initTable(table,tableData);
							statis();
							layer.msg("订单已清空,请添加采购订单");
							layer.close(index);
						});
					}else{
						layer.msg("不能删除,请添加采购订单");
					}
				}else{
					tableData.remove(data);
	    			initTable(table,tableData);
	    			statis();
				}
		    }else if(obj.event === "setSign"){
				  oldDeliNumber = data.deliNumber;
			 }
		  });
		  table.on('edit(deliMateTableEvent)', function(obj){ //注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"
			  if(obj.field == "deliNumber"){
				  var data = obj.data;
				  if(isNaN(obj.value)){
					  layer.msg("请输入有效数字!");
					  data[obj.field] = oldDeliNumber;
					  initTable(table,tableData);
				  }
				  var calculNumber = parseFloat(data.calculNumber);
				  var deliNumber = parseFloat(data.deliNumber);
				  if(deliNumber>calculNumber || deliNumber<0){
					  layer.alert("实际送货量不能大于订单可用量,且要大于等于零");
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
			  }
			  
  			 });
	   //保存
	   $("#saveDeli").click(function(){
		   var data = deliData.config.data;
		   var length = data.length;
		   if(length == 0){
			   layer.msg("物资信息表为空,无法保存,请选择正确的预约单号");
			   return;
		   }
		   var mateData=[];
		   var count=0;
		   for(var i =0;i<tableData.length;i++){
			   if(tableData[i].orderId==''){
				   count++;
			   }
			   if(parseInt(tableData[i].deliNumber)!=0){
				   mateData.push(tableData[i])
			   }
		   }
		   if(count !=0){
			   layer.msg("请为物料添加采购订单");
			   return;
		   }
		   //获取layui 编辑后的table表格中的所有数据
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
					   tuoBack('.specialDeliEdit','#serachSupp');
				   }else{
					   layer.alert("预约送货保存失败");
				   }
			   }
		   });
	   });
	   //提交
	   $("#submitDeli").click(function(){
		   var data = deliData.config.data;
		   var length = data.length;
		   if(length == 0){
			   layer.msg("物资信息表为空,无法提交,请选择正确的预约单号");
			   return;
		   }
		   var mateData=[];
		   var count=0;
		   for(var i =0;i<tableData.length;i++){
			   if(tableData[i].orderId==''){
				   count++;
			   }
			   if(parseInt(tableData[i].deliNumber)!=0){
				   mateData.push(tableData[i])
			   }
		   }
		   if(count !=0){
			   layer.msg("请为物料添加采购订单");
			   return;
		   }
		   //获取layui 编辑后的table表格中的所有数据
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
					   tuoBack('.specialDeliEdit','#serachSupp');
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
			  var mapgCode =obj.value;
			  var deliCode = $("#deliCode").val();
			  if(mapgCode != null && mapgCode != ""){
				   $.ajax({
					  type:"post",
					  url:"/queryAppointByAppoCode?mapgCode="+mapgCode+"&deliCode="+deliCode,
					  dataType:"JSON",
					  async: false,
					  success:function(data){
						  var appoMateData=[];
						  if(data.judge){
							  debugger;
							  suppId=data.appo.suppId;
							 $("#suppId").val(data.appo.suppId);
							 $("#suppName").val(data.appo.suppName);
							 $("#affirmDate").val(data.appo.affirmDate);
							 $("#appoNumber").val(data.appo.mateNumber);
							 $("#deliAmount").val(data.appo.mateAmount);
							 $("#truckNum").val(data.appo.truckNum);
							 $("#receUnit").val(data.appo.receUnit);
							 $("#receAddr").val(data.appo.receAddr);
							 $("#contact").val(data.appo.contact);
							 $("#phone").val(data.appo.phone);
							 $("#post").val(data.appo.post);
							 $("#suppRange").val(data.appo.suppRange);
							 $("#suppRangeDesc").val(data.appo.suppRangeDesc);
							 if(data.appo.suppRange != null && data.appo.suppRange !=''){
								 $("#suppRange2").val(data.appo.suppRange+' - '+data.appo.suppRangeDesc);
							 }else{
								 $("#suppRange2").val("");
							 }
							 var now = new Date(data.appo.appoDate);  
							 //格式化日，如果小于9，前面补0  
							 var day = ("0" + now.getDate()).slice(-2);  
							 //格式化月，如果小于9，前面补0  
							 var month = ("0" + (now.getMonth() + 1)).slice(-2);  
							 //拼装完整日期格式  
							 var appoday = now.getFullYear()+"-"+(month)+"-"+(day) ;  
							 //完成赋值  
							 $("#deliDate").val(appoday);
							 $.each(data.list,function(index,item){
								var sort = index+"01";
								var id = getMyId();
								var arr = {
										id :id+index,
										orderId : '',
										unit : '',
										mateCode : item.mateCode,
										mateName : item.mateName,
										frequency : '',
										subeDate : '',
										appoNumber : item.mateNumber,
										unpaNumber : '',
										deliNumber : '',
										sort : sort
								};
								appoMateData.push(arr);
									
									
							  });
							
							 judgeCode=true;
							 initTable(table,appoMateData);
							 tableData = appoMateData;
							 statis();
							 startTableData = appoMateData;
						  }else{
							  judgeCode=false;
							  resetHtml(table);
							  layer.alert(data.msg);
						  }
					  },
					  error:function(){
						  layer.alert("Connection error");
					  }
				   });
			   }else{
				   judgeCode=false;
				   resetHtml(table);
				   layer.alert("请填写预约单号");
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

function resetHtml(table){
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
	  var a =[];
	  initTable(table,a);
}
//初始化预约申请物资表格
function initTable(table,data){
	 
	 deliData = table.render({
		elem:"#deliMateTable",
		data:data,
		id:"deliMateTableId",
		width:1329,
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
		var unpa=data[i].calculNumber;
		var min =resiNumber>=unpa?unpa:resiNumber;
		var MaxSort ='0';//相同物料中最新采购订单日期
		$.each(tableData,function(index,item){
			if(data[i].mateNumb == item.mateCode){
				var itemSort = item.sort
				MaxSort = MaxSort > itemSort ?  MaxSort:itemSort;
			}
		});
		//创建table的行，赋值，添加到table中
		if(mate == null){
			mate.orderId='1';
		}
		var id = getMyId();
		if(mate.orderId==''){
			var appoMate=[];
			mate.orderId=data[i].contOrdeNumb;
			mate.unit = data[i].company;
			mate.frequency =  data[i].frequency;
			mate.unpaNumber =  data[i].unpaQuan;
			mate.subeDate =  data[i].subeDate;
			mate.calculNumber = data[i].calculNumber;
			if(min>=resiNumber){
				mate.deliNumber=resiNumber;
				resiNumber=0;
			}else{
				mate.deliNumber=min;
				resiNumber=resiNumber-min;
			}
			for(var j =0;j<tableData.length;j++){
				if(mate.id == tableData[j].id){
					tableData[j]=mate;
				}
			}
		}else{
			var tableData2=[];
			for(var k =0;k<tableData.length;k++){
				tableData2.push(tableData[k]);
				if(tableData[k].mateCode == data[i].mateNumb){
					if(tableData[k].sort == MaxSort ){
						var arr = {
								id:id+i,
								orderId : data[i].contOrdeNumb,
								unit : data[i].company,
								mateName : mateName2,
								mateCode : data[i].mateNumb,
								frequency : data[i].frequency,
								unpaNumber : data[i].unpaQuan,
								subeDate : data[i].subeDate,
								calculNumber : data[i].calculNumber,
								appoNumber : appoNumber2,
								sort : parseInt(MaxSort)+1,
						};
						if(min>=resiNumber){
							arr.deliNumber=resiNumber;
							resiNumber=0;
							layer.msg("该物料的实际送货量已满足预约数量");
						}else{
							arr.deliNumber=min;
							resiNumber=resiNumber-min;
						}
						tableData2.push(arr);
					}
				}
			}
			tableData = tableData2;
		}
	
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



Array.prototype.rem = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].mateCode == val.mateCode) {
			this.splice(k, 1);
			return;
		}
	}
};
Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].id == val.id) {
			this.splice(k, 1);
			return;
		}
	}
};

