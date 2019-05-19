//地址前缀
var prefix = "/cont";
//选中节点
var direId="";
//定义树形控件
layui.use([ 'tree', 'table','laytpl','laydate' ], function() {
	var $ = layui.$;
	var table = layui.table;
	initContTable(prefix+"/getContByPage");
	var laydate=layui.laydate;
	//日期控件格式化
	 laydate.render({
		elem : '#startDate'
	 });
	 laydate.render({
		 elem : '#endDate'
	 });
	 laydate.render({
		 elem : '#vaildStart'
	 });
	 laydate.render({
		 elem : '#vaildEnd'
	 });
	//初始化表格
	function initContTable(url){
	   table.render({
		     elem: '#cont-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:url
		    ,cols: [[
		       {checkbox:true}
		      ,{field:'contTypeName', title: '合同类型',width:'7%'}
		      ,{field:'contNo', title: '合同编号',width:'7%'}
		      ,{field:'firstPartName', title: '甲方',width:'15%'}
		      ,{field:'secondPartName', title: '乙方',width:'15%'}
		      ,{field:'contName', title: '合同名称',width:'15%'}
		      ,{field:'contVersion', title: '合同版本',width:'7%'}
		      ,{field:'startDate', title: '有效期始',width:'8%',
		    	  templet: function(d){
		    		  return formatTime(d.startDate,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'endDate', title: '有效期止',width:'7%',
		    	  templet: function(d){
		    		  return formatTime(d.endDate,'yyyy-MM-dd');
		    	  }
		      }
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
		      ,{field:'tools', title: '操作',width:'12%',toolbar:"#barDemo",fixed:'right'}
		    ]]
		  ,page:true
	  });
	}
	//表格监听事件
	 table.on('tool(docTool)', function(obj) {
		var data = obj.data;
		var contId=data.id;
		var contIds=[];
		contIds.push(contId);
		if (obj.event === 'edit') {
			//编辑
			var type="2";
			var url=prefix+"/getAddContPage?contId="+contId+"&type="+type;
//			window.location.href=url;
			tuoGo(url,"addCont","cont-table");
		}else if(obj.event==='publish'){
			//发布changeTempStatus
			$.ajax({
				type:'POST',
				url:prefix+"/changeContStatus",
				data:{contIds:JSON.stringify(contIds),status:'YFB'},
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
			$.ajax({
				type:'POST',
				url:prefix+"/changeContStatus",
				data:{contIds:JSON.stringify(contIds),staus:'WFB'},
				success:function(msg){
					reloadTable();
					layer.msg("操作成功！",{time:1000});
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
			});
		}else if(obj.event === 'del'){
			//删除事件
			
			layer.confirm('确认删除吗？',function(index){
				$.ajax({
					type:'POST',
					url:prefix+"/deleteCont",
					data:{contIds:JSON.stringify(contIds)},
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
		}else if(obj.event === 'view'){
			var type="3";
			var url=prefix+"/getAddContPage?contId="+contId+"&type="+type;
//			window.location.href=url;
			tuoGo(url,"addCont","cont-table");
		}
	});
	//新建按钮点击事件
	$("#createBtn").click(function(){
		var type='1';
		var url=prefix+"/getAddContPage?type="+type;
//		window.location.href=url;
		tuoGo(url,"addCont","cont-table");
	});
	//发布事件
	$("#publishBtn").click(function(){
		var rows = table.checkStatus('cont-table').data;
		var contIds=[];
		for(var i=0;i<rows.length;i++){
			contIds.push(rows[i].id);
		}
		//撤回
		$.ajax({
			type:'POST',
			url:prefix+"/changeContStatus",
			data:{contIds:JSON.stringify(contIds),status:'YFB'},
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
		var rows = table.checkStatus('cont-table').data;
		var contIds=[];
		for(var i=0;i<rows.length;i++){
			contIds.push(rows[i].id);
		}
		//撤回
		$.ajax({
			type:'POST',
			url:prefix+"/changeContStatus",
			data:{contIds:JSON.stringify(contIds),status:'WFB'},
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
		var rows = table.checkStatus('cont-table').data;
		if(rows==null || rows.length==0){
			layer.msg('请选择需要操作的数据！',{time :1000});
			return;
		}
		var contIds=[];
		for(var i=0;i<rows.length;i++){
			contIds.push(rows[i].id);
		}
		
		layer.confirm('确认删除吗？',function(index){
			$.ajax({
				type:'POST',
				url:prefix+"/deleteCont",
				data:{contIds:JSON.stringify(contIds)},
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
		var contName=$("#contName").val();
		var contType=$("#contType").val();
		var startDate=$("#startDate").val();
		var endDate=$("#endDate").val()
		var suppName=$("#suppName").val();
		var vaildStart=$("#vaildStart").val();
		var vaildEnd=$("#vaildEnd").val();
		var url=prefix+"/getContByPage?contName="+contName+"&contType="+contType+"&startDate="+startDate+"&endDate="+endDate+"&suppName="+suppName+"&vaildStart="+vaildStart+"&vaildEnd="+vaildEnd;
		initContTable(url);
	}
	 //查询条件重置
	 $("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	 });
});

