var tableData;
var tableCarData=[];
var table;
var suppId;
layui.use(['form','table','laydate'], function(){
	  var form = layui.form;
	  table = layui.table;
	  layer = layui.layer;
	  var laydate = layui.laydate;
	  var $ = layui.$;
	  var type = $("#type").val();
	  var appoId = $("#appoId").val();
	  //refreshTable();
	  if(type==1){
		  $(function(){
			   $("#mateNumber").val(0);
			   $("#mateAmount").val(0);
		   });
		  tableData=[];
		  initTable(table,tableData);
		  controllCell(tableData);
		  initCarTable(tableCarData);
	  }else{
		 $.ajax({
			 type:"post",
			 url:"/queryAppoMateOfAppoint?appoId="+appoId,
			 dataType:"JSON",
			 async:false,//注意
			 success:function(data){
				 tableData=[];
				 $.each(data,function(index,item){
					 tableData.push(item);
					 initTable(table,tableData);
					 controllCell(tableData);
				 });
			 }
		 });
		 $.ajax({
			 type:"post",
			 url:"/queryAppoCarOfAppoint?appoId="+appoId,
			 dataType:"JSON",
			 async:false,//注意
			 success:function(data){
				 $.each(data,function(index,item){
					 tableCarData.push(item);
					 initCarTable(tableCarData);
				 });
			 }
		 });
		 var suppRange = $("#suppRange").val();
		 var suppRangeDesc = $("#suppRangeDesc").val();
		 var html = '<option selected  value="'+suppRange+' , '+suppRangeDesc+'">'+suppRange+' - '+suppRangeDesc+'</option>';
		 $("#suppRange2").html(html);
		 //重新初始化下拉框
		 form.render('select');
	  }
	  //执行一个laydate实例,限制预约日期在30天之内
	  laydate.render({
	    elem: '#appoDate', //指定元素
	    min: 0,
	    max: 30	
	  });
	  //编辑确认时
	  if(type=='2'){
		  $('.layui-form-select').eq(0).css('display','none');
		  $('#suppId').css('display','block');
		  $("#suppId").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#suppId").attr("disabled",'disabled');
		  $("#appoDate").attr("readonly","readonly");
		  $('.layui-form-select').eq(1).css('display','none');
		  $('#suppRange2').css('display','block');
		  $("#suppRange2").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#suppRange2").attr("disabled",'disabled');
		  $('.layui-form-select').eq(2).css('display','none');
		  $('#expectDate').css('display','block');
		  $("#expectDate").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#expectDate").attr("disabled",'disabled');
		  $('.layui-form-select').eq(5).css('display','none');
		  $('#receUnit').css('display','block');
		  $("#receUnit").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#receUnit").attr("disabled",'disabled');
		  $("#appoDate").attr("disabled",true);
	  }
	  //查看时只读
	  if(type=='3'){
		  $('.layui-form-select').eq(0).css('display','none');
		  $('#suppId').css('display','block');
		  $("#suppId").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#suppId").attr("disabled",'disabled');
		  $('.layui-form-select').eq(1).css('display','none');
		  $('#suppRange2').css('display','block');
		  $("#suppRange2").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#suppRange2").attr("disabled",'disabled');
		  $('.layui-form-select').eq(2).css('display','none');
		  $('#expectDate').css('display','block');
		  $("#expectDate").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#expectDate").attr("disabled",'disabled');
		  $('.layui-form-select').eq(4).css('display','none');
		  $('#priority').css('display','block');
		  $("#priority").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#priority").attr("disabled",'disabled');
		  $('.layui-form-select').eq(5).css('display','none');
		  $('#receUnit').css('display','block');
		  $("#receUnit").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#receUnit").attr("disabled",'disabled');
          $(".disa").attr("disabled",true);		 
 	  }
	  
	  //select下拉框改变值触发
	  //根据sap编码查询供应商
	  form.on("select(suppId)",function(obj){
			suppId =obj.value;
			if(suppId!=""){
				  $.ajax({
					 type:"post",
					 url:"/queryQualSuppBySuppId?suppId="+suppId,
				  	 dataType:"JSON",
				  	 success:function(data){
				  		 if(data.judge){
				  			suppName=data.supp.suppName;
				  			$("#suppName").val(suppName);
				  			var proList = data.proList;
				  			var html = '<option value=""></option>';
				  			for(var i=0;i<proList.length;i++){
				  				var pro = proList[i];
				  				html+='<option value="'+pro.suppRange+' , '+pro.suppRangeDesc+'">'+pro.suppRange+' - '+pro.suppRangeDesc+'</option>'
				  			}
				  			$("#suppRange2").html(html);
			    			//重新初始化下拉框
			    			form.render('select');
				  		 }else{
				  			layer.alert("请输入正确的供应商编码");
				  		 }
				  	 },
				  	 error:function(){
				  		layer.alert("程序出错了"); 
				  	 }
				  });
			  }else{
				  layer.alert("请选择供应商编码");
			  }
      });
	  
	  //select下拉框改变值触发
	  //供应商选择供应商子范围
	  form.on("select(suppRange2)",function(obj){
		    debugger;
			var suppRange =obj.value;
			var index = suppRange.split(' , ');
			$("#suppRange").val(index[0]);
			$("#suppRangeDesc").val(index[1]);
      });
	  //select下拉框改变值触发
	  //根据接收单位查询收货信息
	  form.on("select(receUnit)",function(obj){
		  var receUnit =obj.value;
		  if(receUnit!=""){
			  var  receUnitValue = encodeURIComponent(receUnit);
			  $.ajax({
				  type:"post",
				  url:"/queryReceiveMessByReceUnit?receUnit="+receUnitValue,
				  dataType:"JSON",
				  success:function(data){
					  if(data.judge){
						  var receAddr = data.rece.receAddr;
						  var contact = data.rece.contact;
						  var phone = data.rece.phone;
						  var post = data.rece.post;
						  if(receAddr == null || receAddr == "" ){
							  layer.alert("请配置收货地址");
							  return;
						  }
						  if(contact == null || contact == "" ){
							  layer.alert("请配置联系人");
							  return;
						  }
						  if( phone == null || phone == ""){
							  layer.alert("请配置联系电话");
							  return;
						  }
						  if(  post == null || post == ""){
							  layer.alert("请配置收货单位的岗位");
							  return;
						  }
						  $("#receAddr").val(receAddr);
						  $("#contact").val(contact);
						  $("#phone").val(phone);
						  $("#post").val(post);
					  }else{
						  layer.alert("请输入正确的收货单位");
					  }
				  },
				  error:function(){
					  layer.alert("程序出错了"); 
				  }
			  });
		  }else{
			  $("#receAddr").val('');
			  $("#contact").val('');
			  $("#phone").val('');
			  $("#post").val('');
			  layer.alert("请选择收货单位");
		  }
	  });
	  
	
	   //添加物资
	   $("#addMate").click(function(){
		   	  data = {
		   			suppId:suppId,
		   			tableData:tableData,
					a:this.itemActive
			  };
			  layer.open({
				  type:2,
				  title:"物料列表",
				  shadeClose : false,
				  shade : 0.1,
				  content : '/getSuppMateForManListHtml',
				  area : [ '800px', '90%' ],
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
			      appoMates(checkedData);
			     },
			     btn2: function(index, layero){
			      //按钮【按钮二】的回调
			      // 默认会关闭弹框
			      //return false 开启该代码可禁止点击该按钮关闭
			     }
			  });
			  
	   });
	 //批量删除物资
	   $('#removeMate').click(function() {
		   	var checkStatus = table.checkStatus('appoMateTableId');
		   	var checkedData = checkStatus.data;
		   	if (checkedData.length > 0) {
		   		for ( var int = 0; int < checkedData.length; int++) {
		   			tableData.remove(checkedData[int]);
		   			initTable(table, tableData);
		   			statis(tableData);
		   		}
		   	} else {
		   		layer.msg("请选择要删除的物资");
		   	}
	   })
	    //返回
		 $("#goBack").click(function(){
		 	//window.history.back(-1);
			 tuoBack('.appointUrgeAdd','#serachAppo');
		  });
	 //删除物资后统计数量和方量
	   function statis(tableData){
	   	var amount=parseFloat(0);
	   	  var number=parseFloat(0);
	   	 $.each(tableData,function(index,item){
	   		 var num = item.mateNumber;
	   		   var amo = item.mateAmount;
	   		   if(num==''){
	   			   num=0;
	   		   }
	   		   if(amo == ''){
	   			   amo=0;
	   		   }
	   		   number+=parseFloat(num);
	   		   amount+=parseFloat(amo); 
	   	 });
	   	 $("#mateNumber").val(number.toFixed(3));
	   	  $("#mateAmount").val(amount.toFixed(3));
	   }
		
	 //监听单元格编辑
	   function controllCell(tableData){
	   		   table.on('edit(appoMateTableEvent)', function(obj){ //注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"
	   			   /*console.log(parseInt(obj.value)); //得到修改后的值
	   			   console.log(obj.field); //当前编辑的字段名
	   			   console.log(obj.data); //所在行的所有相关数据  
	   			   console.info(obj.data.mateNumber)
	   			   console.info(tableData);*/
	   			  var rowdata = obj.data;
	   			  var value = obj.value;
	   			  var field = obj.field;
	   			  if("remark"!=field){
	   				  if(isNaN(obj.value)){
	   					  layer.msg("请输入有效数字!");
	   					  rowdata[obj.field] = 0;
	   					  initTable(table,tableData);
	   					  statis(tableData);
	   					  return ;
	   				  }
	   				  debugger;
	   				  var result  = isInteger(parseFloat(value));
	   				  if(!result ){
	   					  layer.msg("请输入大于等于零的数!");
	   					  rowdata[obj.field] = 0;
	   					  initTable(table,tableData);
	   				  }
	   				  if(field == "mateAmount"){
	   					   statis(tableData);
	   				   }
	   				  if(field =="mateNumber"){
	   					  var mateBluk = parseFloat(rowdata.mateBluk)/1000;
	   					  var mateNumber = parseFloat(value);
	   					  var predAmount = mateNumber*mateBluk;
	   					  rowdata.predAmount = predAmount.toFixed(3);
	   					 rowdata.mateAmount = predAmount.toFixed(3);
	   				  }
	   				  $(this).click();
	   			  }
	   			   /*var amount=0;
	   			   var number=0;
	   			   $.each(tableData,function(index,item){
	   				   var num = item.mateNumber;
	   				   var amo = item.mateAmount;
	   				   if(num==''){
	   					   num=0;
	   				   }
	   				   if(amo == ''){
	   					   amo=0;
	   				   }
	   				   number+=parseInt(num);
	   				   amount+=parseInt(amo);
	   			   });
	   			   $("#mateNumber").val(number);
	   			   $("#mateAmount").val(amount);*/
	   			 });
	   }
	   
	   //确认
	   $("#affirmAppoint").click(function(){
		   var isOwn = $("#isOwn").val();
			if(isOwn == "false"){
				layer.msg("未找到相应的任务,无法确认")
				return ;
			}
		   var priority = $("#priority").val();
		   //var expectDate = $("#expectDate").val();
		   if(priority != "" && priority != null){
			   //if(expectDate != ""){
				   /*var suppName = $("#suppName").val();
				   var format = $("#createDate").val();
				   var remark = "预约送货审核——"+suppName+"——"+format;*/
				   var taskName = $("#taskName").val();
				   var processCode = $("#processCode").val();
				  // var flag = taskProcess(appoId, processCode, taskName, "process");
				   //if(flag=="process_success"){
					   var formData = $("#appointForm").serialize();
					   $.ajax({
						   type : "POST",
						   url : "/affirmAppoint",
						   data : formData,
						   dataType:"JSON",
						   async: false,
						   error : function(request) {
							   layer.alert("Connection error");
						   },
						   success : function(data) {
							   if(data){
								   var flag = taskProcess(appoId, processCode, taskName, "process");
								   //layer.msg("预约确认成功");
								   //window.history.back(-1);
							   }else{
								   layer.alert("预约确认失败");
							   }
						   }
					   });
				   //}
			   /*}else{
				   layer.alert('<span style="color:red;">请选择期望送货时间</span>');
			   }*/
		   }else{
			   layer.alert('<span style="color:red;">请选择优先级</span>');
		   }
	   });
	   
	   //提交
	   $("#submitAppoint").click(function(){
		   var paperTableData = $('#appoCarTable').bootstrapTable("getData");
		   var result = checkMust();
			 if(!result.flag){
				 layer.msg(result.msg); 
				 return ;
			 }
			var post = $("#post").val();
			 if(post == null || post ==""){
				  layer.msg("收货单位的岗位信息为空，请在收货地址信息中配置岗位信息");
				  return;
			 }
		   if(tableData.length>0){
			   if(paperTableData.length>0){ 
				   var count=0; 
				   for(var i =0;i<paperTableData.length;i++){
					   if(paperTableData[i].carType == ''){
						   layer.msg("请选择车辆的类型");
						   return;
					   }
					   count+=parseInt(paperTableData[i].carNumber);
				    }
				   if(count == 0 || isNaN(count)){
					   layer.msg("请填写车量的数量");
					   return;
				   }
				   for(var j =0;j<tableData.length;j++){
					   var result2= isInteger(parseFloat(tableData[j].mateNumber))
					   var result3 = isInteger(parseFloat(tableData[j].mateAmount))
					   if(!result2 || !result3){
						   layer.msg("物料的预约数量和方量为大于等于零的数");
						   return ;
					   }
				   }
				   $("#truckNum").val(count);
				   $("#appoCarData").val(JSON.stringify(paperTableData)); 
				   $("#appoMateData").val(JSON.stringify(tableData));
				   appoId = guid();
				   $("#appoId").val(appoId);
				   var suppName = $("#suppName").val();
				   //var format = new Date().Format("yyyy-MM-dd");
				   var remark = "预约送货审核: "+suppName;
				   //var flag = taskProcess(appoId, "appoAudit", remark, "process");
				   //if(flag == "process_success"){
					   var formData = $("#appointForm").serialize();
					   $.ajax({
						   type : "POST",
						   url : "/submitUrgeAppoint",
						   data : formData,
						   dataType:"JSON",
						   async: false,
						   error : function(request) {
							   layer.alert("Connection error");
						   },
						   success : function(data) {
							   if(data){
								   var flag = taskProcess(appoId, "appoAudit", remark, "process");
								   //layer.msg("预约提交成功");
								   //window.history.back(-1);
							   }else{
								   layer.alert("预约提交失败");
							   }
						   }
					   });
				   //}
			   }else{
					 layer.msg("请选择车辆"); 
			   }
			 }else{
				 layer.msg("请选择物资");
			 }
	   });
	   //在弹窗中选择执行人后，点击确认按钮回调
	   window.returnFunction = function() {
			debugger
			var appoIds = [];
			appoIds.push(appoId);
			appoStatus='已确认';
			var appoIdJson = JSON.stringify(appoIds);
				$.ajax({
					type:"post",
					url:"/confirmAppointByAppoIds?appoStatus="+appoStatus +
							"&appoIds="+appoIdJson,
					dataType:"JSON",
					success:function(data2){
						if(data2){
							layer.msg('提交成功', {time:2000 });
							//window.history.back(-1);
							tuoBack('.appointUrgeAdd','#serachAppo');
						}else{
							layer.alert('<span style="color:red;">提交失败</sapn>');
						}
					}
			});
		}
	   //添加车辆
	   $("#addCar").click(function(){
		   var id = getMyId();
			var object = new Object();
			object.carType = '';
			object.carNumber = 1;
			object.remark = '';
			object.id = id;
			tableCarData.push(object);
			$('#appoCarTable').bootstrapTable("load", tableCarData);
	   });
	   //删除车辆
	   $("#removeCar").click(function(){
		   var checkStatus = table.checkStatus('appoCarTableId');
		   	var checkedData = checkStatus.data;
		   	if (checkedData.length > 0) {
		   		for ( var int = 0; int < checkedData.length; int++) {
		   			tableCarData.remove(checkedData[int]);
		   			initCarTable(tableCarData);
		   			statisCarNum(tableCarData);
		   		}
		   	} else {
		   		layer.msg("请选择要删除的节点");
		   	}
	   });
	   
	     //监听工具条
		  table.on('tool(appoMateTableEvent)', function(obj){
		    var data = obj.data;
		    if(obj.event === 'check'){//查看
		    	
		    } else if(obj.event === 'del'){//删除
		    	if(type=='1'){
		    		tableData.remove(data);
		    		statis(tableData);
		    		initTable(table,tableData);
		    	}else{
		    		layer.msg("不能删除");
		    	}
		    } else if(obj.event === 'edit'){//编辑
		    	
		    }else if(obj.event === 'setSign'){
		    	var upd={};
		    	debugger;
		    	//var nextField = B302 ，nextField的所代表的值B302 作为upd对象的属性，
		    	//data[nextField] ：表示获取data对象B302属性的值
		    	upd.predAmount = data.predAmount;
		    	upd.mateAmount = data.mateAmount;
		    	obj.update(upd);
		    	statis(tableData);
		    }
		  });
		
});

$(window).on('click',function(evnet){
	var num = 0;
	for(var i = 0; i<$('.boottable tbody td:nth-child(3)').length;i++){
		if($($('.boottable tbody td:nth-child(3)')[i]).html()!=''||$($('.boottable tbody td:nth-child(3)')[i]).html()!=undefined){
			if($($('.boottable tbody td:nth-child(3)')[i]).html().length<15){
				num = num + Number($($('.boottable tbody td:nth-child(3)')[i]).html());
			}
		}
	}
	$('#truckNum').val(num);	
})
//初始化预约申请车辆表表格
function initCarTable(data) {
	$('#appoCarTable').bootstrapTable('destroy');
	$('#appoCarTable')
			.bootstrapTable(
					{
						method : 'get',
						editable : true,// 开启编辑模式
						undefinedText : '',
						pageList : [ 10, 20 ],
						pageSize : 10,
						pageNumber : 1,
						striped : true,
						locale : 'zh-CN',
						columns : [
								{checkbox: true},
								{
									field : "carType",
									title : "车型",
									align : "center",
									width : "300px",
									edit : {
										type : 'select',// 下拉框
										required : true,
										url : '/queryCarType',
										valueField : 'dicName',
										textField : 'dicName',
										onSelect : function(val, rec) {
										}
									}
								},
								{
									field : "carNumber",
									title : "数量(辆)",
									width:"200px",
									sortable:true,
									align : "center",
									footerFormatter: function (value) { 
										var count = 0;
									for(var i in value) { count += value[i].carNumber; } 
									   return  count;
									 }
								},
								{
									field : "remark",
									title : "备注",
									width:"800px",
									align : "center",
								},
								{
									field : 'id',
									title : "id",
									align : 'center',
									visible : false,
									edit : false,
								},
								{
									field : 'status',
									title : '操作',
									align : "center",
									width:"100px",
									formatter : function(value, row, index) {
										var editStr = '<a class="layui-btn layui-btn-danger layui-btn-xs redInline" style="text-decoration:none;" onclick="deleteRowCar(\''
												+ row.id + '\') ">删除</a>';
										return editStr;
									}
								} ],
						data : data
					});
}

function deleteRowCar(id){
	var type = $("#type").val();
	if(type=='1'){
		var ids =[];
		ids.push(id);
		$('#appoCarTable').bootstrapTable('remove', {
			field : 'id',
			values : ids
		});
	}else{
		layer.msg("不能删除");
	}
}
//初始化预约申请物资表格
function initTable(table,data){
	var type = $("#type").val();
	var appoStatus = $("#appoStatus").val();
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
		   {field:"mateNumber",title:"数量(箱)",align:'center',edit:'text',event:'setSign'},
		   {field:"mateAmount",title:"实际方量",align:'center',edit:'text'},
		   {field:"predAmount",title:"预计方量",align:'center'},
		   {field:"remark",title:"备注",align:'center',edit:'text'},
		   {fixed: 'right', title:'操作',width:100, align:'center', toolbar: '#barDemo'}
		]]
		
	});
	if(type == '3' || type =='2'){
		 $('table td').removeAttr('data-edit');
    }
}


//弹出框子页面调用父页面方法传递数据
function appoMates(data){
	debugger;
	for(var i=0;i<data.length;i++){
		var count=0;
		for(var j=0;j<tableData.length;j++){
			if(data[i].mateCode == tableData[j].mateCode){
				count++;
			}
		}
		if(count == 0){
			//创建table的行，赋值，添加到table中
			var id = getMyId();
			//data[i].mateBulk
			var arr = {
					id:id+i,
					mateName : data[i].mateName,
					mateCode : data[i].mateCode,
					mateNumber:0,
					mateAmount:0,
					predAmount:0,
					mateBluk:data[i].mateBulk
			};
			tableData.push(arr);
		}
	}
	initTable(table, tableData);
	
}
//紧急预约保存/预约确认保存
function saveAppoint(){
	 var paperTableData = $('#appoCarTable').bootstrapTable("getData");
	layui.use(['form','table'], function(){
		var $ = layui.$;
		 var type = $("#type").val();
		 var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		 if(tableData.length>0){
			 if(paperTableData.length>0){
				 var count=0; 
				   for(var i =0;i<paperTableData.length;i++){
					   if(paperTableData[i].carType == ''){
						   layer.msg("请选择车辆的类型");
						   return;
					   }
					   count+=parseInt(paperTableData[i].carNumber);
				    }
				   if(count == 0 || isNaN(count)){
					   layer.msg("请填写车量的数量");
					   return;
				   }
				   for(var j =0;j<tableData.length;j++){
					   var result2= isInteger(parseFloat(tableData[j].mateNumber))
					   var result3 = isInteger(parseFloat(tableData[j].mateAmount))
					   if(!result2 || !result3){
						   layer.msg("物料的预约数量和方量为大于等于零的数");
						   return ;
					   }
				   }
				 $("#truckNum").val(count);
				 $("#appoCarData").val(JSON.stringify(paperTableData));
				 $("#appoMateData").val(JSON.stringify(tableData));
				 var flag ="";
				 if(type=="1"){//新建紧急预约的保存
					 var post = $("#post").val();
				     if(post == null || post ==""){
					     layer.msg("收货单位的岗位信息为空，请在收货地址信息中配置岗位信息");
					     return;
				     }
					 var appoId = guid();
					 $("#appoId").val(appoId);
					 var suppName = $("#suppName").val();
					 //var format = new Date().Format("yyyy-MM-dd");
					 var remark = "预约送货审核: "+suppName;
					 flag = taskProcess(appoId, "appoAudit", remark, "save");
				 }else{//预约确认的保存
					 flag = "init_success";
				 }
				 if(flag == "init_success"){
					 var formData = $("#appointForm").serialize();
					 $.ajax({
						 type : "POST",
						 url : "/addUrgeAppoint",
						 data:"type="+type,
						 data : formData,
						 dataType:"JSON",
						 async: false,
						 error : function(request) {
							 layer.alert("Connection error");
						 },
						 success : function(data) {
							 if(data){
								 layer.msg("预约保存成功");
								 //window.history.back(-1);
								 tuoBack('.appointUrgeAdd','#serachAppo');
							 }else{
								 layer.alert("预约保存失败");
							 }
						 }
					 });
				 }
			 }else{
				 layer.msg("请选择车辆"); 
			 }
		 }else{
			 layer.msg("请选择物资");
		 }
	});
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
    return  obj>=0;  //是大于等于零的数，则返回true，否则返回false  

}
