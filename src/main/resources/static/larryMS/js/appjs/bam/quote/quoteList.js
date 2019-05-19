//地址前缀
var prefix = "/quote";
//选中节点
var direId="";
//定义树形控件
layui.use([ 'tree', 'table','laytpl','laydate' ], function() {
	var $ = layui.$;
	var table = layui.table;
	var laydate = layui.laydate;
	var userType = $("#userType").val();
	var quoteStatus = '';
	var quoteCode ='';
	laydate.render({
		elem:'#createTime',
	});
	initQuoteTable(prefix+"/getQuoteByPage");
	//初始化表格
	function initQuoteTable(url){
		var str=$('#createDiv').html().trim();
		var firstAuditstr=$('#firstAuditDiv').html().trim();
		var toolBar="#barDemo";
		if( str=='' && firstAuditstr==''){
			toolBar="#barDemo1";
		}else if(firstAuditstr!=''){
			toolBar="#barDemo2";
		}
	   table.render({
		     elem: '#quote-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:url
		    ,cols: [[
		       {checkbox:true}
		      ,{field:'status', title: '报价状态',width:'7%'}
		      ,{field:'quoteCode', title: '报价单号',width:'7%'}
		      ,{field:'quoteTypeDesc', title: '报价类型',width:'7%'}
		      ,{field:'suppName', title: '供应商名称',width:'17%',
		    	  /*templet: function(d){
		    		  return d.suppName.substr(d.suppNo.length);
		    	  }*/
		      }
		      ,{field:'suppNo', title: '供应商编码',width:'8%'}
		      ,{field:'creater', title: '创建人',width:'17%'}
		      ,{field:'createTime', title: '创建日期',width:'10%',
		    	  templet: function(d){
		    		  return formatTime(d.createTime,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'tools', title: '操作',width:'14%',toolbar:toolBar}
		    ]]
		  ,page:true
	  });
	}
	//表格监听事件
	 table.on('tool(docTool)', function(obj) {
		var data = obj.data;
		var quoteCode=data.quoteCode;
		var quoteCodes=[];
		var status=data.status;
		quoteCodes.push(quoteCode);
		if (obj.event === 'edit') {
			if(status!="已保存" && status!="已退回"){
				layer.msg('请选择已保存或已退回的数据进行编辑！',{time:1000});
				return ;
			}
			//编辑
			var type="2";
			var url=prefix+"/getAddQuotePage?quoteCode="+quoteCode+"&type="+type;
			tuoGo(url,"addQuote","quote-table");
//			window.location.href=url;
		}else if(obj.event==='view'){
			var type="3";
			var url=prefix+"/getAddQuotePage?quoteCode="+quoteCode+"&type="+type;
//			window.location.href=url;
			tuoGo(url,"addQuote","quote-table");
		}else if(obj.event === 'del'){
			if(status!="已保存"){
				layer.msg('只能删除已保存状态的数据！',{time:1000});
				return ;
			}
			layer.confirm('确认删除吗?', function(index){
				//删除事件
				 $.ajax({
						type:'POST',
						url:prefix+"/delQuotes",
						data:{codeJson:JSON.stringify(quoteCodes)},
						success:function(msg){
							reloadTable();
							layer.msg("操作成功！",{time:1000});
						},
						error:function(){
							layer.msg("操作失败！",{time:1000});
						}
				 });
				  layer.close(index);
				});   
		 }else if(obj.event === 'validDate'){
			 /*layer.open({
				 type:2,
				 shade:false,
				 title:'维护有效期',
				 maxmin:true,
				 area:['400px','360px'],
				 content:prefix+"/getValidDateDg?quoteCode="+quoteCode,
				 success:function(index,layero){
					 
				 }
			 });*/
			 if(status == "OA审核"){
				 layer.msg("OA审核状态的报价单无法进行附件信息和有效期的编辑",{time:2000})
				 return;
			 }
			var type="4";
			var url=prefix+"/getAddQuotePage?quoteCode="+quoteCode+"&type=4";
			//window.location.href=url;
			tuoGo(url,"addQuote","quote-table");
		 }
	});
	//新建按钮点击事件
	$("#createBtn").click(function(){
		var type='1';
		var url=prefix+"/getAddQuotePage?type="+type;
//		window.location.href=url;
		tuoGo(url,"addQuote","quote-table");

	});
	 $(document).on("click",".xuanzhong5 table tbody tr",function() {
		  $('#quote-table + div tr').removeClass('pitch5');
		  $(event.target).parents('tr').addClass('pitch5');
		});
	
	
	//提交事件
	$("#submitBtn").click(function(){
		/*var rows = table.checkStatus('quote-table').data;
		if(rows==null || rows.length==0){
			layer.msg('请选择需要操作的数据！',{time:1000});
			return;
		}
		var quoteCodes=[];
		var flag=true;
		for(var i=0;i<rows.length;i++){
			quoteCodes.push(rows[i].quoteCode);
			var status=rows[i].status;
			if(status!='已保存' && status!='已退回'){
				flag=false;
			}
		}
		if(!flag){
			layer.msg('请提交已保存或者已退回的单据',{time:1000});
			return;
		}
		//提交
		for(var i=0;i<rows.length;i++){
			quoteCodes=[];
			var id = rows[i].id;
			var processCode='';
			var judge = true;
			$.ajax({
				type:'POST',
				url:prefix+"/getProcessCodeOfTask?id="+id,
				dataType:"JSON",
				async:false,
				success:function(task){
					if(task.isOwn){
						processCode = task.processCode;
					}else{
						judge = false;
					}
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
			});
			if(judge == false){
				layer.msg("未找到相应的任务,无法提交");
				return;
			}
			var suppName = rows[i].suppName;
			var createTime = rows[i].createTime;
			//var format = new Date(createTime).Format("yyyy-MM-dd");
			var remark = "报价单审核: "+suppName;
			quoteStatus = '未审核';
			var flag = taskProcess(id, processCode, remark, "process");*/
			/*if(flag=="process_success"){
				quoteCodes.push(rows[i].quoteCode);
				updateQuoteStatus("未审核",quoteCodes);
			}*/
	    var rows = table.checkStatus('quote-table').data;
		if(rows==null || rows.length==0){
			layer.msg('请选择需要操作的数据！',{time:1000});
			return;
		}
		
		var quoteCodes=[];
		var flag=true;
		for(var i=0;i<rows.length;i++){
			quoteCodes.push(rows[i].quoteCode);
		}		
		//判断所有的是否所有的报价单都维护了有效期,
		var msg="";
		$.ajax({
			type:"POST",
			async:false,
			data:{quoteJson:JSON.stringify(quoteCodes)},
			url:prefix+"/validDate",
			success:function(res){
				var code=res.code;
				if(code==0){
					//layer.msg('操作成功',{time:1000});
				}else{
					flag=false;
					msg=res.msg;
				}
			}
		});
		if(!flag){
			layer.alert(msg);
			return;
		}
		if(rows.length!=1){
			layer.msg('请选择一条数据进行提交！',{time:1000});
			return;
		}
		var status = rows[0].status;
		if(status!="已保存" && status!="已退回"){
			layer.msg('请提交已保存或者已退回的单据！',{time:1000});
			return;
		}
		var quoteCodes=[];
		quoteCodes.push(rows[0].quoteCode);
		var id = rows[0].id;
		var processCode='';
		var judge = true;
		$.ajax({
			type:'POST',
			url:prefix+"/getProcessCodeOfTask?id="+id,
			dataType:"JSON",
			async:false,
			success:function(task){
				if(task.isOwn){
					processCode = task.processCode;
				}else{
					judge = false;
				}
			},
			error:function(){
				layer.msg("操作失败！",{time:1000});
			}
		});
		if(judge == false){
			layer.msg("未找到相应的任务,无法提交");
			return;
		}
		var suppName = rows[0].suppName;
		//var createTime = rows[0].createTime;
		//var format = new Date(createTime).Format("yyyy-MM-dd");
		var remark = "报价单审核: "+suppName;
		if(userType =='supplier'){
			quoteStatus = '未审核';
		}else{
			quoteStatus = '初审核';
		}
		quoteCode = rows[0].quoteCode;
		var flag = taskProcess(id, processCode, remark, "process");
		/*if(userType =='supplier'){
			var flag = taskProcess(id, processCode, remark, "process");
		}else{
			var flag = taskProcess(id, processCode, remark, "save");
			if(flag=="init_success"){
				updateQuoteStatus("未审核",quoteCodes);
			}else{
				layer.msg("任务生成失败");
			}
		}*/
		/*if(flag=="process_success"){
			updateQuoteStatus("未审核",quoteCodes);
		}*/
	   //updateQuoteStatus("未审核",quoteCodes);
   });
	window.returnFunction = function() {
		debugger 
		var quoteCodes=[];
		quoteCodes.push(quoteCode);
		updateQuoteStatus(quoteStatus,quoteCodes);
		
	}
	//拒绝事件
	$("#refuseBtn").click(function(){
		var rows = table.checkStatus('quote-table').data;
		if(rows==null || rows.length==0){
			layer.msg('请选择需要操作的数据！',{time:1000});
			return;
		}
		var quoteCodes=[];
		var flag=true;
		for(var i=0;i<rows.length;i++){
			quoteCodes.push(rows[i].quoteCode);
			var status=rows[i].status;
			if(status!='未审核'){
				flag=false;
			}
		}
		if(!flag){
			layer.msg('请操作未审核的数据！',{time:1000});
			return;
		}
		for(var i=0;i<rows.length;i++){
			quoteCodes=[];
			var id = rows[i].id;
			var result = backProcess(id);
			if(result == "back_success"){
				quoteCodes.push(rows[i].quoteCode);
				updateQuoteStatus("已退回",quoteCodes);
			}
		}
		//updateQuoteStatus("已退回",quoteCodes);
	});
	//初审核事件
	$("#firstAuditBtn").click(function(){
		var rows = table.checkStatus('quote-table').data;
		/*if(rows==null || rows.length==0){
			layer.msg('请选择需要操作的数据！',{time:1000});
			return;
		}*/
		/*var quoteCodes=[];
		var flag=true;
		for(var i=0;i<rows.length;i++){
			quoteCodes.push(rows[i].quoteCode);
			var status=rows[i].status;
			if(status!='未审核'){
				flag=false;
			}
		}
		if(!flag){
			layer.msg('请操作未审核的数据！',{time:1000});
			return;
		}*/
		
		if(rows==null || rows.length==0){
			layer.msg('请选择需要操作的数据！',{time:1000});
			return;
		}
		var quoteCodes=[];
		var flag=true;
		for(var i=0;i<rows.length;i++){
			quoteCodes.push(rows[i].quoteCode);
		}		
		//判断所有的是否所有的报价单都维护了有效期,
		var msg="";
		$.ajax({
			type:"POST",
			async:false,
			data:{quoteJson:JSON.stringify(quoteCodes)},
			url:prefix+"/validDate",
			success:function(res){
				var code=res.code;
				if(code==0){
					//layer.msg('操作成功',{time:1000});
				}else{
					flag=false;
					msg=res.msg;
				}
			}
		});
		if(!flag){
			layer.alert(msg);
			return;
		}
		if(rows.length!=1){
			layer.msg('请选择一条数据进行初审！',{time:1000});
			return;
		}
		var status = rows[0].status;
		if(status!="未审核"){
			layer.msg('请操作未审核的数据！',{time:1000});
			return;
		}
		var quoteCodes=[];
		quoteCodes.push(rows[0].quoteCode);
		var id = rows[0].id;
		var processCode='';
		var judge = true;
		$.ajax({
			type:'POST',
			url:prefix+"/getProcessCodeOfTask?id="+id,
			dataType:"JSON",
			async:false,
			success:function(task){
				if(task.isOwn){
					processCode = task.processCode;
				}else{
					judge = false;
				}
			},
			error:function(){
				layer.msg("操作失败！",{time:1000});
			}
		});
		if(judge == false){
			layer.msg("未找到相应的任务,无法初审");
			return;
		}
		var suppName = rows[0].suppName;
		//var createTime = rows[0].createTime;
		//var format = new Date(createTime).Format("yyyy-MM-dd");
		var remark = "报价单审核: "+suppName;
		quoteStatus = '初审核';
		quoteCode = rows[0].quoteCode;
		var flag = taskProcess(id, processCode, remark, "process");
		/*if(flag=="process_success"){
			updateQuoteStatus("初审核",quoteCodes);
		}*/
	});
	
	//OA拒绝事件
	$("#OARefuseBtn").click(function(){
		var rows = table.checkStatus('quote-table').data;
		if(rows==null || rows.length==0){
			layer.msg('请选择需要操作的数据！',{time:1000});
			return;
		}
		var quoteCodes=[];
		var flag=true;
		for(var i=0;i<rows.length;i++){
			quoteCodes.push(rows[i].quoteCode);
			var status=rows[i].status;
			if(status!='初审核'){
				flag=false;
			}
		}
		if(!flag){
			layer.msg('请操作初审核的数据！',{time:1000});
			return;
		}
		for(var i=0;i<rows.length;i++){
			quoteCodes=[];
			var id = rows[i].id;
			var result = backProcess(id);
			if(result == "back_success"){
				quoteCodes.push(rows[i].quoteCode);
				updateQuoteStatus("已退回",quoteCodes);
			}
		}
		//updateQuoteStatus("已退回",quoteCodes);
	});
	//OA审核事件
	$("#OAAuditBtn").click(function(){
		var rows = table.checkStatus('quote-table').data;
		if(rows==null || rows.length==0){
			layer.msg('请选择需要操作的数据！',{time:1000});
			return;
		}
		var quoteCodes=[];
		var flag=true;
		for(var i=0;i<rows.length;i++){
			quoteCodes.push(rows[i].quoteCode);
			var status=rows[i].status;
			if(status!='初审核' && status!='OA待审核'){
				flag=false;
			}
		}
		if(!flag){
			layer.msg('请操作初审核,OA待审核的数据！',{time:1000});
			return;
		}
		debugger;
		for(var i=0;i<rows.length;i++){
			quoteCodes=[];
			var id = rows[i].id;
			var status = rows[i].status;
			var flag = "";
			if(status == "初审核"){
				var processCode='';
				var judge = true;
				$.ajax({
					type:'POST',
					url:prefix+"/getProcessCodeOfTask?id="+id,
					dataType:"JSON",
					async:false,
					success:function(task){
						if(task.isOwn){
							processCode = task.processCode;
						}else{
							judge = false;
						}
					},
					error:function(){
						layer.msg("操作失败！",{time:1000});
					}
				});
				if(judge == false){
					layer.msg("未找到相应的任务,无法进行OA审核");
					return;
				}
				var suppName = rows[i].suppName;
				var createTime = rows[i].createTime;
				//var format = new Date(createTime).Format("yyyy-MM-dd");
				var remark = "报价单审核: "+suppName;
				flag = taskProcess(id, processCode, remark, "process");
			}else if(status == 'OA待审核'){
				flag = "over_success";
			}
//			var flag="over_success"
			if(flag=="over_success"){
				quoteCodes.push(rows[i].quoteCode);
				$.ajax({
					type:'POST',
					url:prefix+"/submitQuoteToOA",
					data:{jsonCodes:JSON.stringify(quoteCodes)},
					async:false,//注意
					success:function(res){
						reloadTable();
						var code =res.code;
						if(code==0){
							layer.msg("提交OA成功！",{time:1000});
						}else{
							layer.msg(res.msg,{time:1000});
						}
					},
					error:function(){
						layer.msg("程序异常！",{time:1000});
					}
				});
			}
		}
	});
	//删除按钮点击事件
	$("#deleteBtn").click(function(){
		var rows = table.checkStatus('quote-table').data;
		if(rows==null || rows.length==0){
			layer.msg('请选择需要操作的数据！',{time:1000});
			return;
		}
		var quoteCodes=[];
		var flag=true;
		for(var i=0;i<rows.length;i++){
			quoteCodes.push(rows[i].quoteCode);
			var status=rows[i].status;
			if(status!='已保存'){
				flag=false;
			}
		}
		if(!flag){
			layer.msg('请选择已保存的数据进行删除！',{time:1000});
			return;
		}
		layer.confirm("确认删除吗？",function(index){
			$.ajax({
				type:'POST',
				url:prefix+"/delQuotes",
				data:{codeJson:JSON.stringify(quoteCodes)},
				success:function(msg){
					reloadTable();
					layer.msg("操作成功！",{time:1000});
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
			});
			layer.close(index);
		});
	});
	//导出事件
	$("#exportBtn").click(function(){
		
	});
	
	window.reloadTable= function(){
		var suppName=$("#suppName").val();
		var quoteCode=$("#quoteCode").val();
		debugger;
		var createTime=$("#createTime").val();
		var checkedStatusCode=[];
	    $(".checked").each(function(){
	    	var status=$(this).attr("name");
	    	checkedStatusCode.push(status);
	    });
	    var statusJson=JSON.stringify(checkedStatusCode);
		var url=prefix+"/getQuoteByPage?suppName="+suppName+"&quoteCode="+quoteCode+"&createTime="+createTime+"&statusJson="+statusJson;
		initQuoteTable(url);
	}
	 //查询条件重置
	 $("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
		 $(".checked").each(function(index,row){
			$(this).removeClass("checked");
			$(this).addClass("uncheck");
			$(this).css('color','gray');
		 });
	 });
	 $("#searchBtn").click(function(){
		 reloadTable();
	 });
	 /**
	  * 更新报价单状态
	  * @param status
	  * @param quoteCodes
	  * @returns
	  */
	 function updateQuoteStatus(status,quoteCodes){
		 $.ajax({
				type:'POST',
				url:prefix+"/changeQuoteStatus",
				data:{codeJson:JSON.stringify(quoteCodes),status:status},
				success:function(msg){
					reloadTable();
					layer.msg("操作成功！",{time:1000});
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
		});
	 }
	 
});
//文档查询点击事件
function changeClass(dom){
	if($(dom).hasClass("uncheck")){
		$(dom).removeClass("uncheck");
		$(dom).addClass("checked");
		$(dom).css('color','blue');
	}else{
		$(dom).removeClass("checked");
		$(dom).addClass("uncheck");
		$(dom).css('color','gray');
	}
}

