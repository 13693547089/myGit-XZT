var table;
var tableData=[];
layui.use('table', function(){
	  table = layui.table;
	  var $ = layui.$;
	  var index=parent.layer.getFrameIndex(window.name);
	  var data2 =parent.data;
	  var checkedData = data2.checkedData;
	  $.each(checkedData,function(index,item){
		  tableData.push(item);
	  })
	  initAppoTable(table,tableData);
		
	   //取消
		$("#cancel").click(function(){
			var index=parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		});
		
		//确定
		$("#confirm").click(function(){
			var tdata; 
			var length = tableData.length;
			for(var i=0;i<length;i++){
				tdata = [];
				tdata.push(tableData[i]);
				 var appoId = tableData[i].appoId;
				 var suppName = tableData[i].suppName;
				 var processCode ='';
				 var taskName = '';
				 var judge = true;
					$.ajax({
						type:'POST',
						url:"/quote/getProcessCodeOfTask?id="+appoId,
						dataType:"JSON",
						async:false,
						success:function(task){
							if(task.isOwn){
								processCode = task.processCode;
								taskName = task.taskName;
							}else{
								judge = false;
							}
						},
						error:function(){
							layer.msg("操作失败！",{time:1000});
						}
					});
					if(judge == false){
						layer.msg("未找到相应的任务,无法发布");
						return;
					}
				 //var createDate = tableData[i].createDate;
				 //var format = new Date(createDate).Format("yyyy-MM-dd");
				 //var remark = "预约送货审核: "+suppName;
				 var flag = taskProcess(appoId, processCode, taskName, "process");
				 if(flag== "over_success"){
					 var tableDataJson = JSON.stringify(tdata);
					 $.ajax({
						 type : "POST",
						 url : "/updateAffirmDateByAppoId?type=2",
						 data : {dateJson:tableDataJson},
						 dataType:"JSON",
						 async: false,
						 error : function(request) {
							 layer.alert("Connection error");
						 },
						 success : function(data) {
							 if(data){
								 layer.msg("预约发布成功");
							 }else{
								 layer.alert("预约发布失败");
							 }
						 }
					 });
				 }
			}
		    var index=parent.layer.getFrameIndex(window.name);
			parent.reloadTable();
			parent.layer.close(index);
		});
		
	  
		$('.demoTable .layui-btn').on('click', function(){
			var type = $(this).data('type');
			active[type] ? active[type].call(this) : '';
		});
});



function initAppoTable(table,data){
	var appoTable = table.render({
		 elem:"#appoTable",
		 data:data,
		 page:false,
		 width: '100%', 
		 minHeight:30,
		 //limit:10,
		 cols:[[
		 {
			 field:"appoCode",
			 title:"预约单号",
			 align:'center',
		     width:147
		 },{
			 field:"appoDate",
			 title:"预约日期",
			 align:'center',
		     width:104,
		     templet:
		    	 function(d){
		    	    var date = new Date(d.appoDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		 },{
			 field:"expectDate",
			 title:"期望送货时间",
			 align:'center',
		     width:115
		 },{
			 field:"suppName",
			 title:"供应商",
			 align:'center',
		     width:91
		 }
		 ]],
		 id:'appoTableId'
	 });
}






