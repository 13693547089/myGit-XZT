var tableData;
var table;
var tableCarData=[];
layui.use(['form','table','laydate'], function(){
	  var form = layui.form;
	  table = layui.table;
	  layer = layui.layer;
	  var laydate = layui.laydate;
	  /*laydate.render({
		 elem:"#appoDate", 
		 min: 0,
		 max: 30
	  });*/
	  var $ = layui.$;
	  var appoId = $("#appoId").val();
	  var type = $("#type").val();
		 $.ajax({
			 type:"post",
			 url:"queryAppoMateOfAppoint?appoId="+appoId,
			 dataType:"JSON",
			 async:false,//注意
			 success:function(data){
				 tableData=[];
				 $.each(data,function(index,item){
					 tableData.push(item);
					 initTable(table,tableData);
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
					 initCarTable(table,tableCarData);
				 });
			 }
		 });
		 //查看时只读
		 if(type=='3'){
			 $('.layui-form-select').eq(0).css('display','none');
			 $('#expectDate').css('display','block');
			 $("#expectDate").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
			 $("#expectDate").attr("disabled",'disabled');
			 $('.layui-form-select').eq(1).css('display','none');
			 $('#affirmDate').css('display','block');
			 $("#affirmDate").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
			 $("#affirmDate").attr("disabled",'disabled');
			  $('.layui-form-select').eq(2).css('display','none');
			  $('#priority').css('display','block');
			  $("#priority").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
			  $("#priority").attr("disabled",'disabled');
			  $(".disa").prop("readonly","readonly");
		  } 
		 
	  //返回
	  $("#goBack").click(function(){
	 	  //window.history.back(-1);
		  tuoBack('.appointIssue','#serachAppo');
	  });
	  //监听工具条
	  table.on('tool(appoMateTableEvent)', function(obj){
		    var data = obj.data;
	    	if(obj.event === 'check'){//查看

	    	} else if(obj.event === 'del'){//删除
	       
	    	} 
	  });
	  var suppRange = $("#suppRange").val();
	  var suppRangeDesc = $("#suppRangeDesc").val();
	  if(suppRange != null || suppRange !=''){
		  $("#suppRange2").val(suppRange+" - "+suppRangeDesc);
	  }
		
});
//初始化预约申请车辆表表格
function initCarTable(table,data){
	table.render({
		elem:"#appoCarTable",
		data:data,
		id:"appoCarTableId",
		width:1329,
		cols:[[
		       {type:'numbers',align:'center',title:"序号"},
		       {field:"carType",title:"车型",align:'center'},
		       {field:"carNumber",title:"数量(辆)",align:'center'},
		       {field:"remark",title:"备注",align:'center'},
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
		   {type:'numbers',align:'center',title:"序号"},
		   {field:"mateName",title:"物料名称",align:'center'},
		   {field:"mateCode",title:"物料编码",align:'center'},
		   {field:"mateNumber",title:"数量(箱)",align:'center'},
		   {field:"mateAmount",title:"实际方量",align:'center'},
		   {field:"predAmount",title:"预计方量",align:'center'},
		   {field:"remark",title:"备注",align:'center'},
		]]
		
	});
}

//保存/发布
function saveAppoint(funtype){
	layui.use(['form','table'], function(){
		 var $ = layui.$;
		 var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		 var flag="";
		 if(funtype == 2){//发布
			 var isOwn = $("#isOwn").val();
			 if(isOwn == "false"){
				layer.msg("未找到相应的任务,无法确认")
				return ;
			 }
			 var appoId = $("#appoId").val();
			 /*var suppName = $("#suppName").val();
			 var format = $("#createDate").val();
			 var remark = "预约送货审核——"+suppName+"——"+format;*/
			 var taskName = $("#taskName").val();
			 var processCode = $("#processCode").val();
			 flag = taskProcess(appoId, processCode, taskName, "process");
		 }else{
			 flag = "over_success" ;
		 }
		 if(flag == "over_success"){
			 var formData = $("#appointForm").serialize();
			 $.ajax({
				 type : "POST",
				 url : "/updateAffirmDate?funtype="+funtype,
				 data : formData,
				 dataType:"JSON",
				 async: false,
				 error : function(request) {
					 layer.alert("Connection error");
				 },
				 success : function(data) {
					 if(data.judge){
						 if(funtype==1){
							 layer.msg("预约保存成功");
						 }else{
							 layer.msg("预约发布成功");
						 }
						 //window.history.back(-1);
						 tuoBack('.appointIssue','#serachAppo');
					 }else{
						 layer.alert(data.msg); 
					 }
				 }
			 });
		 }
		
			 
	});
}




Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].id == val.id) {
			this.splice(k, 1);
			return;
		}
	}
};

