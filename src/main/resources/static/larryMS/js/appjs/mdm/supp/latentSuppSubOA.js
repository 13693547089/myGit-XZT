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
	  var paperList2 = $("#paperList2").val();
	    tableData = JSON.parse(paperList2);
	    initPaperTable(tableData);
	  var status = $("#status").val();
	  if(status == "已初审"){
		  debugger;
		  $('.layui-form-select').eq(0).css('display','none');
		  $('#categoryId').css('display','block');
		  $("#categoryId").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#categoryId").attr("disabled",'disabled');
		  $('.layui-form-select').eq(1).css('display','none');
		  $('#payClauseId').css('display','block');
		  $("#payClauseId").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#payClauseId").attr("disabled",'disabled');
		  $('.layui-form-select').eq(2).css('display','none');
		  $('#currencyId').css('display','block');
		  $("#currencyId").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#currencyId").attr("disabled",'disabled');
		  $('.layui-form-select').eq(3).css('display','none');
		  $('#taxeKindId').css('display','block');
		  $("#taxeKindId").css({"display":"block","width":"100%","min-height":"36px","border":"1px solid #e6e6e6","webkit-appearance":"none","padding-left":"10px"});
		  $("#taxeKindId").attr("disabled",'disabled');
	  }
	   //返回
		 $("#goBack").click(function(){
		 	//window.history.back(-1);
			 tuoBack('.latentSuppSubOA','#serachSupp');
		  });
		//提交OA
		$("#approveLatentSupp").click(function(){
			var isOwn = $("#isOwn").val();
			if(isOwn == "false"){
				layer.msg("未找到相关的任务,无法提交OA")
				return ;
			}
			var suppId = $("#suppId").val();
			/*var suppName = $("#suppName").val();
			var registerTime = $("#registerTime").val();
			//var format = new Date().Format("yyyy-MM-dd");
			var remark = "潜在供应商审核——"+suppName+"——"+registerTime;*/
			var taskName = $("#taskName").val();
			var processCode = $("#processCode").val();
			var flag = taskProcess(suppId, processCode, taskName, "process");
			if("over_success"==flag){
				$.ajax({
					type: "POST",
					url:"/subLatentSuppToOA?suppId="+suppId,
					dataType:"JSON",
					async: false,
					error: function(request) {
						parent.layer.msg("程序出错了！",{time: 2000});
					},
					success: function(data) {
						if(data){
							parent.layer.msg("提交成功！",{time: 2000});
							//location="/getLatentSuppMiniListHtml";
							//window.history.back(-1);
							tuoBack('.latentSuppSubOA','#serachSupp');
						}else{
							parent.layer.msg("提交失败！",{time: 2000});
						}
					}
				});
			}
		});
		//退回
		$("#backLatentSupp").click(function(){
			var suppId = $("#suppId").val();
			var result = backProcess(suppId);
			if(result == "back_success"){
				$.ajax({
					type: "POST",
					url:"/sendBackLatentSupp?suppId="+suppId,
					dataType:"JSON",
					async: false,
					error: function(request) {
						parent.layer.msg("程序出错了！",{time: 2000});
					},
					success: function(data) {
						if(data){
							parent.layer.msg("退回成功！",{time: 2000});
							//location="/getLatentSuppMiniListHtml";
							//window.history.back(-1);
							tuoBack('.latentSuppSubOA','#serachSupp');
						}else{
							parent.layer.msg("退回失败！",{time: 2000});
						}
					}
				});  
				
			}
	   });
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
		   {field:"acceOldName",title:"附件",align:'center',width:425},
		   {fixed: 'right', title:'附件操作',width:101, align:'center', toolbar: '#barDemo'}
		]]
		
	});
}
function downFile(docId){
	debugger;
	location="/doc/downLoadDoc?docId="+docId;
}
