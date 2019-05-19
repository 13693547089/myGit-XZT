//地址前缀
var prefix = "/template";
//选中节点
var direId="";
//定义树形控件
layui.use([ 'tree', 'table','laytpl' ], function() {
	var $ = layui.$;
	var table = layui.table;
	initTempTable(prefix+"/getTempByPage");
	//初始化表格
	function initTempTable(url){
	   table.render({
		     elem: '#temp-table'
		    ,id:'temp-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:url
		    ,cols: [[
		       {checkbox:true}
		      ,{field:'tempName', title: '模板名称',width:'15%'}
		      ,{field:'tempNo', title: '模板编号',width:'7%'}
		      ,{field:'attachNum', title: '附件数',width:'6%'}
		      ,{field:'creater', title: '创建人',width:'7%'}
		      ,{field:'createTime', title: '创建日期',width:'7%',
		    	  templet: function(d){
		    		  return formatTime(d.createTime,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'modifier', title: '修改人',width:'7%'}
		      ,{field:'modifyTime', title: '修改日期',width:'7%',
		    	  templet: function(d){
		    		  return formatTime(d.modifyTime,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'tools', title: '操作',width:'12%',toolbar:"#barDemo"}
		    ]]
		  ,page:true
	  });
	}
	//表格监听事件
	 table.on('tool(docTool)', function(obj) {
		var data = obj.data;
		var tempNo=data.tempNo;
		var tempNos=[];
		tempNos.push(tempNo);
		if (obj.event === 'edit') {
			//编辑
			var type="2";
			var url=prefix+"/getAddTempPage?tempNo="+tempNo+"&type="+type;
//			window.location.href=url;
			tuoGo(url,"addTemp","temp-table","temp-table");
		}else if(obj.event === 'del'){
			//删除
			delTemps(tempNos);
		}else if(obj.event==='view'){
			//查看
			var type="3";
			var url=prefix+"/getAddTempPage?tempNo="+tempNo+"&type="+type;
//			window.location.href=url;
			tuoGo(url,"addTemp","temp-table");
		}
	});
	// 文档查询点击事件
	$("#tempSeachBtn").click(function(){
		var tempName=$("#tempName").val();
		var url=prefix+"/getTempByPage?tempName="+tempName;
		initTempTable(url);
	});
	//新建按钮点击事件
	$("#createBtn").click(function(){
		var type='1';
		var url=prefix+"/getAddTempPage?type="+type;
//		window.location.href=url;
		tuoGo(url,"addTemp","temp-table");
	});
	//删除按钮点击事件
	$("#deleteBtn").click(function(){
		var tempNos=[];
		var checkData = table.checkStatus('temp-table').data;
		$.each(checkData,function(index,row){
			tempNos.push(row.tempNo);
		});
		delTemps(tempNos);
	});
	function delTemps(tempNos){
		$.ajax({
			url:prefix+"/deleteTemp",
			data:{tempNos:JSON.stringify(tempNos)},
			success:function(res){
				if(res.code=='0'){
					$("#tempSeachBtn").click();
					layer.msg("操作成功",{time:1000});
				}else{
					layer.msg("操作失败",{time:1000});
				}
			},
			error:function(){
				layer.msg("操作失败",{time:1000});
			}
		});
	}
});

