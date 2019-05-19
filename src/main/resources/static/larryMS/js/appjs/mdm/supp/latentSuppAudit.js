var tableData = [];
var table;
layui.use(['form','table'], function(){
	  var form = layui.form;
	  var $ = layui.$;
	  table = layui.table;
	  //监听提交
	  /* form.on('submit(formDemo)', function(data){
	    layer.msg(JSON.stringify(data.field));
	    return false;
	  }); */
	   // debugger;
	    var paperList2 = $("#paperList2").val();
	    tableData = JSON.parse(paperList2);
	    initPaperTable(tableData);
		//返回
		 $("#goBack").click(function(){
		 	//window.history.back(-1);
			 tuoBack('.latentSuppAudit','#serachSupp');
		  });
		//批准潜在供应商
		$("#approveLatentSupp").click(function(){
		   //console.info(data)
			debugger;
			var isOwn = $("#isOwn").val();
			if(isOwn == "false"){
				layer.msg("未找到相关的任务,无法批准")
				return ;
			}
			var result = checkMust();
			 if(!result.flag){
				 layer.msg(result.msg); 
				 return ;
			 }
			 debugger;
		   var papername3 = $("#categoryId option:selected").text();
		   $("#category").val(papername3);
		  /* var suppName = $("#suppName").val();
		   var registerTime = $("#registerTime").val();
		   var remark = "潜在供应商审核——"+suppName+"——"+registerTime;*/ 
		   var taskName = $("#taskName").val();
		   var processCode = $("#processCode").val();
		   //var format = new Date(registerTime).Format("yyyy-MM-dd");
		   var suppId = $("#suppId").val();
			var saveFormData = new FormData($('#latentSuppForm')[0]);//序列化表单，
			 //$("form").serialize()只能序列化数据，不能序列化文件
			//var flag = taskProcess(suppId, processCode, taskName, "process");
			//if("process_success"==flag){
				$.ajax({
					type: "POST",
					url:"/approveLatentSupp",
					data:saveFormData,// 你的form的id属性值
					processData: false,
					contentType: false,
					async: false,
					error: function(request) {
						parent.layer.msg("程序出错了！",{time: 2000});
					},
					success: function(data) {
						if(data){
							//parent.layer.msg("批准并保存成功！",{time: 2000});
							//location="/getLatentSuppRegListHtml";
							//window.history.back(-1);
							var flag = taskProcess(suppId, processCode, taskName, "process");
						}else{
							parent.layer.msg("批准并保存失败！",{time: 2000});
						}
					}
				});   
			//}
			
		});
		window.returnFunction = function() {
			debugger
			var suppId = $("#suppId").val();
			$.ajax({
				type:"post",
				url:"/buyerAuditLatentSupp?suppId="+suppId,
				dataType:"JSON",
				error:function(request){
					parent.layer.msg("程序出错了！", {
						time : 2000
					});
				},
				success:function(data){
					if(data){
						//window.history.go(-1);
						tuoBack('.latentSuppAudit','#serachSupp');
					}else{
						layer.msg("初审失败！");
					}
				}
			});
		}
		/*//退回
		$("#backLatentSupp").click(function(){
			var suppId = $("#suppId").val(); 
			$.ajax({
		 		type: "POST",
		 		url:"/buyerSendBackLatentSupp?suppId="+suppId,
		 		dataType:"JSON",
		 		async: false,
		 	    error: function(request) {
		 	    	parent.layer.msg("程序出错了！",{time: 2000});
		 	    },
		 	    success: function(data) {
		 		   if(data){
		 				parent.layer.msg("退回成功！",{time: 2000});
		 				location="/getLatentSuppRegListHtml";
		 		   }else{
		 				parent.layer.msg("退回失败！",{time: 2000});
		 		   }
		 	    }
	        }); 
		});*/
		
		//监听工具条
		  table.on('tool(paperTableEvent)', function(obj){
		    var data = obj.data;
		    if(obj.event === 'down'){//查看
		    	downFile(data.fileId)
		    } else if(obj.event === 'del'){//删除
		    	if(type!='3'){
		    		tableData.remove(data);
		    		statis(tableData);
		    		initTable(table,tableData);
		    	}else{
		    		layer.msg("不能删除");
		    	}
		    } else if(obj.event === 'edit'){//编辑
		    	
		    }
		  });
	  
});
function initPaperTable(data){
	table.render({
		elem:"#paperTable",
		data:data,
		id:"paperTableId",
		width:1329,
		limit:100,
		cols:[[
		   {type:'numbers',align:'center',title:"序号"},
		   {field:"papersType",title:"附件类型",align:'center',width:149},
		   {field:"papersName",title:"名称",align:'center',width:257},
		   {field:"startDate",title:"有效期从",align:'center',width:167,templet:
			   function (d){
				    if(d.startDate == null || d.startDate ==""){
			   			return ""
			   		}
			   		return formatTime(d.startDate, 'yyyy-MM-dd');
		   	   }
		   },
		   {field:"endDate",title:"有效期至",align:'center',width:167,templet:
			   function (d){
			   		if(d.endDate == null || d.endDate ==""){
			   			return ""
			   		}
		   		    return formatTime(d.endDate, 'yyyy-MM-dd');
	   	   	   }
		   },
		   {field:"acceOldName",title:"附件",align:'center',width:221},
		   {fixed: 'right', title:'附件操作',width:101, align:'center', toolbar: '#barDemo'}
		]]
		
	});
}
function downFile(docId){
	debugger;
	location="/doc/downLoadDoc?docId="+docId;
}
	
 