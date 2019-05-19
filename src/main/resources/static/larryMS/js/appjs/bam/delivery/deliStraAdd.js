var tableData=[];
var table;
var deliData;
var judgeCode =false;
layui.use(['form','table'], function(){
	  var form = layui.form;
	  table = layui.table;
	  layer = layui.layer;
	  var $ = layui.$;
	  initTable(table,tableData);
	    //返回
		 $("#goBack").click(function(){
		 	//window.history.back(-1);
			 tuoBack('.deliStraAdd','#serachSupp');
		  });
	   
	   
	   //保存
	   $("#saveDeli").click(function(){
		   //获取layui 编辑后的table表格中的所有数据
		   debugger;
		   var data = deliData.config.data;
		   var length = data.length;
		   if(length == 0){
			   layer.msg("物资信息表为空,无法保存,请选择提货单号");
			   return;
		   }
		   debugger;
		   $("#deliMateData").val(JSON.stringify(data));
		   var formData = $("#deliveryForm").serialize();
		   if(judgeCode){
			   //配置一个加载组件，注意必须是异步ajax
			   var index = layer.load();
			   $.ajax({
				   type : "POST",
				   url : "/addDeliveryTwo?type=add",
				   data : formData,
				   dataType:"JSON",
				   async: true,
				   error : function(request) {
					   layer.close(index);
					   layer.alert("Connection error");
				   },
				   success : function(data) {
					    //停止加载
						layer.closeAll();
					   if(data){
						   $("#saveDeli").attr("disabled","disabled");
						   $("#submitDeli").attr("disabled","disabled");
						   layer.msg("直发送货保存成功");
						   //window.history.back(-1);
						   tuoBack('.deliStraAdd','#serachSupp');
					   }else{
						   layer.alert("直发送货保存失败");
					   }
				   }
			   });
		   }else{
			   layer.alert("请填写正确的提货单号");
		   }
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
		   if(judgeCode){
			   var index = layer.load();
			   $.ajax({
				   type : "POST",
				   url : "/addDeliveryTwo?type=sub",
				   data : formData,
				   dataType:"JSON",
				   async: true,
				   error : function(request) {
					   layer.close(index);
					   layer.alert("Connection error");
				   },
				   success : function(data) {
					   layer.closeAll();
					   if(data){
						   $("#saveDeli").attr("disabled","disabled");
						   $("#submitDeli").attr("disabled","disabled");
						   layer.msg("直发送货提交成功");
						   //window.history.back(-1);
						   tuoBack('.deliStraAdd','#serachSupp');
					   }else{
						   layer.alert("直发送货提交失败");
					   }
				   }
			   });
		   }else{
			   layer.alert("请填写正确的提货单号"); 
		   }
	   });
	   
	  //预约单号select下拉框改变值触发
	  //根据根据提货单号查询这个直发通知的信息
	  form.on("select(mapgCode)",function(obj){
		  var mapgCode =obj.value;
		  if(mapgCode != null && mapgCode != ""){
			   $.ajax({
				  type:"post",
				  url:"/queryStraMessageBymessCode?mapgCode="+mapgCode,
				  dataType:"JSON",
				  success:function(data){
					  var messMateData=[];
					  if(data.judge){
						  var straMess = data.straMess;
						  debugger;
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
						 var deliday = now.getFullYear()+"-"+(month)+"-"+(day) ;  
						 //完成赋值  
						 $("#deliDate").val(deliday);
						 messMateData = data.deliMates;
						 judgeCode=true;
						 initTable(table,messMateData);
					  }else{
						  judgeCode=false;
						  resetHtml(table);
						  layer.alert("请选择正确的提货单号");
					  }
				  },
				  error:function(){
					  layer.alert("Connection error");
				  }
			   });
		   }else{
			   judgeCode=false;
			   resetHtml(table);
			   layer.alert("请填写提货单号");
		   }
	  });
		
});

//初始化预约申请物资表格
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
		},{
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

