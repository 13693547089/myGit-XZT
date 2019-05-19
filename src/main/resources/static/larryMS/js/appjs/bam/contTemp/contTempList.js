//地址前缀
var prefix = "/contTemp";
//选中节点
var direId="";
//定义树形控件
layui.use([ 'tree', 'table','laytpl' ], function() {
	var $ = layui.$;
	var table = layui.table;
	initTempTable(prefix+"/getContTempByPage");
	//初始化表格
	function initTempTable(url){
	   table.render({
		     elem: '#temp-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:url
		    ,cols: [[
		       {checkbox:true}
		      ,{field:'statusName', title: '模板状态',width:'7%'}
		      ,{field:'contTypeName', title: '合同类型',width:'7%'}
		      ,{field:'contVersion', title: '版本号',width:'12%'}
		      ,{field:'tempNo', title: '模板编码',width:'7%'}
		      ,{field:'tempName', title: '模板名称',width:'15%'}
		      ,{field:'creater', title: '创建人',width:'6%'}
		      ,{field:'createTime', title: '创建日期',width:'7%',
		    	  templet: function(d){
		    		  return formatTime(d.createTime,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'modifier', title: '修改人',width:'6%'}
		      ,{field:'modifyTime', title: '修改日期',width:'7%',
		    	  templet: function(d){
		    		  return formatTime(d.modifyTime,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'tools', title: '操作',width:'15%',toolbar:"#barDemo"}
		    ]]
		  ,page:true
	  });
	}
	//表格监听事件
	 table.on('tool(docTool)', function(obj) {
		var data = obj.data;
		var tempNo=data.tempNo;
		var tempNos=[];
		var status=data.statusName;
		tempNos.push(tempNo);
		if (obj.event === 'edit') {
			//编辑
			if(status=='已发布'){
				layer.msg("已发布状态下不能修改!",{time:1000});
				return;
			}
			var type="2";
			var url=prefix+"/getAddTempPage?tempNo="+tempNo+"&type="+type;
//			window.location.href=url;
			tuoGo(url,"addContTemp","temp-table");
		}else if(obj.event==='view'){
			var type="3";
			var url=prefix+"/getAddTempPage?tempNo="+tempNo+"&type="+type;
//			window.location.href=url;
			tuoGo(url,"addContTemp","temp-table");
		}else if(obj.event==='publish'){
			if(status=='已发布'){
				layer.msg("请勿重复发布",{time:1000});
				return;
			}
			//发布changeTempStatus
			$.ajax({
				type:'POST',
				url:prefix+"/changeTempStatus",
				data:{tempNos:JSON.stringify(tempNos),status:'YFB'},
				success:function(msg){
					if(msg.code=='0'){
						reloadTable();
						layer.msg("操作成功！",{time:1000});
					}
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
			});
		}else if(obj.event=='revoke'){
			//撤回
			if(status!='已发布'){
				layer.msg("请选择已发布的模板撤回！",{time:1000});
				return;
			}
			$.ajax({
				type:'POST',
				url:prefix+"/changeTempStatus",
				data:{tempNos:JSON.stringify(tempNos),staus:'WFB'},
				success:function(msg){
					reloadTable();
					layer.msg("操作成功！",{time:1000});
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
			});
		}else if(obj.event === 'del'){
			if(status=='已发布'){
				layer.msg("已发布状态下不能删除!",{time:1000});
				return;
			}
			layer.confirm('确认删除吗?', function(index){
				//删除事件
				$.ajax({
					type:'POST',
					url:prefix+"/deleteContTemp",
					data:{tempNos:JSON.stringify(tempNos)},
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
			
		}
	});
	//新建按钮点击事件
	$("#createBtn").click(function(){
		var type='1';
		var url=prefix+"/getAddTempPage?type="+type;
//		window.location.href=url;
		tuoGo(url,"addContTemp","temp-table");
	});
	//发布事件
	$("#publishBtn").click(function(){
		var checkStatus = table.checkStatus('temp-table').data;
		if(checkStatus==null || checkStatus.length==0){
			layer.msg('请选择要操作的数据',{time:1000});
			return;
		}
		var tempNos=[];
		var flag =true;
		var msg='';
		for(var i=0;i<checkStatus.length;i++){
			tempNos.push(checkStatus[i].tempNo);
			if(checkStatus[i].statusName!='未发布'){
				msg="请选择未发布的订单进行发布！";
				flag=false;
			}
		}
		if(!flag){
			layer.msg(msg,{time:1000});
			return;
		}
		//撤回
		$.ajax({
			type:'POST',
			url:prefix+"/changeTempStatus",
			data:{tempNos:JSON.stringify(tempNos),status:'YFB'},
			success:function(msg){
				reloadTable();
				layer.msg("操作成功！",{time:1000});
			},
			error:function(){
				layer.msg("操作失败！",{time:1000});
			}
		});
	});
	//撤回事件
	$("#revokeBtn").click(function(){
		var checkStatus = table.checkStatus('temp-table').data;
		if(checkStatus==null || checkStatus.length==0){
			layer.msg('请选择要操作的数据',{time:1000});
			return;
		}
		var tempNos=[];
		var flag =true;
		var msg='';
		for(var i=0;i<checkStatus.length;i++){
			tempNos.push(checkStatus[i].tempNo);
			if(checkStatus[i].statusName!='已发布'){
				msg="请选择已发布的订单进行撤回！";
				flag=false;
			}
		}
		if(!flag){
			layer.msg(msg,{time:1000});
			return;
		}
		//撤回
		$.ajax({
			type:'POST',
			url:prefix+"/changeTempStatus",
			data:{tempNos:JSON.stringify(tempNos),status:'WFB'},
			success:function(msg){
				reloadTable();
				layer.msg("操作成功！",{time:1000});
			},
			error:function(){
				layer.msg("操作失败！",{time:1000});
			}
		});
	});
	//删除按钮点击事件
	$("#deleteBtn").click(function(){
		var checkStatus = table.checkStatus('temp-table').data;
		if(checkStatus==null || checkStatus.length==0){
			layer.msg('请选择要操作的数据',{time:1000});
			return;
		}
		var tempNos=[];
		var flag =true;
		var msg='';
		for(var i=0;i<checkStatus.length;i++){
			tempNos.push(checkStatus[i].tempNo);
			if(checkStatus[i].statusName=='已发布'){
				msg="已发布的合同模板不能删除！";
				flag=false;
			}
		}
		if(!flag){
			layer.msg(msg,{time:1000});
			return;
		}
		layer.confirm('确认删除吗?', function(index){
			$.ajax({
				type:'POST',
				url:prefix+"/deleteContTemp",
				data:{tempNos:JSON.stringify(tempNos)},
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
	// 文档查询点击事件
	$("#searchBtn").click(function(){
		reloadTable();
	});
	function reloadTable(){
		var tempName=$("#tempName").val();
		var tempStatus=$("#tempStatus").val();
		var contType=$("#contType").val();
		var url=prefix+"/getContTempByPage?tempName="+tempName+"&tempStatus="+tempStatus+"&contType="+contType;
		initTempTable(url);
	}
	 //查询条件重置
	 $("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	 });
});

