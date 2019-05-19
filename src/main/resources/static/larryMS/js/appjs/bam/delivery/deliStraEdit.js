var tableData=[];
var table;
var deliData;
var mapgCode2;
var type;
layui.use(['form','table'], function(){
	  var form = layui.form;
	  table = layui.table;
	  layer = layui.layer;
	  var $ = layui.$;
	  var deliId = $("#deliId").val();
	  mapgCode2=$("#mapgCode").val();
	  var suppr = $("#suppRange").val();
	  var supprd = $("#suppRangeDesc").val();
	  if(suppr != null && suppr != '' ){
		  $("#suppRange2").val(suppr +' - '+supprd);
	  }else{
		  $("#suppRange2").val('');
	  }
	  $.ajax({
		 type:"post",
		 url:"/queryDeliMateByDeliId?deliId="+deliId,
	  	 dataType:"JSON",
	  	 success:function(data){
	  		 tableData= data;
	  		 initTable(table,tableData);
	  	 },
	  	 error:function(){
	  		layer.alert("Connection error");
	  	 }
	  });
	    //返回
		 $("#goBack").click(function(){
		 	//window.history.back(-1);
			 tuoBack('.deliStraEdit','#serachSupp');
		  });
		//打印
		 $("#printBut").click(function(){
			 var url  ="/getDeliPrintHtml?deliId="+deliId;
 			 location=url; 
		 });
		 //查看时不能编辑
		 type = $("#type").val();
		 if(type=='2'){
			  $('.layui-form-select').eq(0).css('display','none');
			  $('#mapgCode').css('display','block');
			  $("#mapgCode").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
			  $("#mapgCode").attr("disabled",'disabled');
	          $(".disa").prop("readonly","readonly");	
		 }
	   
	   //保存
	   $("#saveDeli").click(function(){
		   //获取layui 编辑后的table表格中的所有数据
		   var data = deliData.config.data;
		   var length = data.length;
		   if(length == 0){
			   layer.msg("物资信息表为空,无法保存,请选择提货单号");
			   return;
		   }
		   $("#deliMateData").val(JSON.stringify(data));
		   var formData = $("#deliveryForm").serialize();
		   $.ajax({
			   type : "POST",
			   url : '/updateDeliveryTwo?subtype=add&mapgCode2='+mapgCode2,
			   data : formData,
			   dataType:"JSON",
			   async: false,
			   error : function(request) {
				   layer.alert("Connection error");
			   },
			   success : function(data) {
				   if(data){
					   layer.msg("直发送货保存成功");
					  // window.history.back(-1);
					   tuoBack('.deliStraEdit','#serachSupp');
				   }else{
					   layer.alert("直发送货保存失败");
				   }
			   }
		   });
	   });
	   //提交
	   $("#submitDeli").click(function(){
		   //获取layui 编辑后的table表格中的所有数据
		   var data = deliData.config.data;
		   var length = data.length;
		   if(length == 0){
			   layer.msg("物资信息表为空,无法提交,请选择提货单号");
			   return;
		   }
		   $("#deliMateData").val(JSON.stringify(data));
		   var formData = $("#deliveryForm").serialize();
		   $.ajax({
			   type : "POST",
			   url : "/updateDeliveryTwo?subtype=sub&mapgCode2="+mapgCode2,
			   data : formData,
			   dataType:"JSON",
			   async: false,
			   error : function(request) {
				   layer.alert("Connection error");
			   },
			   success : function(data) {
				   if(data){
					   layer.msg("直发送货提交成功");
					   //window.history.back(-1);
					   tuoBack('.deliStraEdit','#serachSupp');
				   }else{
					   layer.alert("直发送货提交失败");
				   }
			   }
		   });
	   });
	   
	 //预约单号select下拉框改变值触发
	 //根据根据提货单号查询这个直发通知的信息
	 form.on("select(mapgCode)",function(obj){
	   var mapgCode =obj.value;
	   
	   if(mapgCode != null && mapgCode != ""){
		   if(mapgCode == mapgCode2){
			   initTable(table,tableData);
			   return;
		   }
		   $.ajax({
			  type:"post",
			  url:"/queryStraMessageBymessCode?mapgCode="+mapgCode,
			  dataType:"JSON",
			  success:function(data){
				  var messMateData=[];
				  if(data.judge){
					  var straMess  = data.straMess;
					  $("#suppId").val(straMess.zzoem);
					 $("#suppName").val(straMess.suppName);
					 $("#deliNumber").val(straMess.mateNumber);
					 $("#deliAmount").val(straMess.mateAmount);
					 $("#receUnit").val(straMess.receUnit);
					 $("#receAddr").val(straMess.receAddr);
					 $("#contact").val(straMess.contact);
					 $("#phone").val(straMess.phone);
					 $("#post").val(straMess.post);
					 $("#suppRange").val(straMess.suppRange);
					 $("#suppRangeDesc").val(straMess.suppRangeDesc);
					 if(straMess.suppRange != null && straMess.suppRange !=''){
						 $("#suppRange2").val(straMess.suppRange+' - '+straMess.suppRangeDesc);
					 }else{
						 $("#suppRange2").val("");
					 }
					 var now = new Date(straMess.deliveryDate);  
					 //格式化日，如果小于9，前面补0  
					 var day = ("0" + now.getDate()).slice(-2);  
					 //格式化月，如果小于9，前面补0  
					 var month = ("0" + (now.getMonth() + 1)).slice(-2);  
					 //拼装完整日期格式  
					 var appoday = now.getFullYear()+"-"+(month)+"-"+(day) ;  
					 //完成赋值  
					 $("#deliDate").val(appoday);
					 messMateData = data.deliMates;
					 initTable(table,messMateData);
				  }else{
					  resetHtml(table)
					  layer.alert("请填写正确的预约单号");
				  }
			  },
			  error:function(){
				  layer.alert("Connection error");
			  }
		   });
	   }else{
		   debugger;
		   resetHtml(table)
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

// 初始化预约申请物资表格
function initTable(table, data) {
	deliData = table.render({
		elem : "#deliMateTable",
		data : data,
		id : "deliMateTableId",
		width : 1329,
		limit : 100,
		cols : [ [ {
			title : "项次",
			field : "frequency",
			align : 'center'
		}, {
			field : "orderId",
			title : "采购订单号",
			align : 'center'
		}, {
			field : "mateCode",
			title : "物料编码",
			align : 'center'
		}, {
			field : "mateName",
			title : "物料名称",
			align : 'center'
		},{
			field : "appoNumber",
			title : "调拨数量",
			align : 'center',
			width :100
		},{
			field : "unpaNumber",
			title : "订单未交量",
			align : 'center',
			width :100
		}, {
			field : "calculNumber",
			title : "订单可用量",
			align : 'center',
			width :100
		}, {
			field : "deliNumber",
			title : "实际送货数量",
			align : 'center'
		}, {
			field : "unit",
			title : "单位",
			align : 'center'
		}, {
			field : "prodPatchNum",
			title : "产品批号",
			align : 'center',
			edit : 'text'
		}, {
			field : "remark",
			title : "备注",
			align : 'center',
			edit : 'text'
		}, ] ]
	});
	if (type == '2') {
		$('table td').removeAttr('data-edit');
	}
}

function resetHtml(table){
	 $("#suppId").val("");
	  $("#suppName").val("");
	  $("#deliNumber").val("");
	  $("#deliAmount").val("");
	  $("#receUnit").val("");
	  $("#receAddr").val("");
	  $("#deliDate").val("");
	  $("#contact").val("");
	  $("#phone").val("");
	  $("#post").val("");
	  $("#suppRange").val("");
	  $("#suppRangeDesc").val("");
	  $("#suppRange2").val("");
	  var messMateData=[];
	  initTable(table,messMateData);
}


Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].id == val.id) {
			this.splice(k, 1);
			return;
		}
	}
};

