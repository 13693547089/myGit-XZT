//地址前缀
var prefix = "/bam/pdr";
//选中节点
var direId="";

//定义日期格式
function laydateInit(laydate){
	  //常规用法
	  laydate.render({
	    elem: '#search_crtDateStart'
	  });
	  laydate.render({
		elem: '#search_crtDateEnd'
	  });
	  laydate.render({
	    elem: '#search_prodDateStart'
	  });
	  laydate.render({
		elem: '#search_prodDateEnd'
	  });
}

//定义树形控件
layui.use(['table','laydate'], function() {
	var $ = layui.$;
	var table = layui.table;
	var laydate = layui.laydate;
	// 定义日期控件
	laydateInit(laydate);
	
	initListTable(prefix+"/getPdrSpecialPageList");
	//初始化表格
	function initListTable(url){
		var str=$('#createSpan').html().trim();
		var operateBar="#barDemo";
		if( str==''){
			operateBar="#barDemo1";
		}
	   table.render({
		     elem: '#list-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:url
		    ,cols: [[
		       {checkbox:true}
			  ,{field:'sn', title: '序号',width:'5%',type:'numbers'}
			  ,{field:'status', title: '状态',width:'7%'}
		      ,{field:'pdrCode', title: '日报编号',width:'13%'}
		      ,{field:'produceDate', title: '生产日期',width:'10%',
		    	  templet: function(d){
		    		  return formatTime(d.produceDate,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'suppName', title: '供应商名称'}
		      ,{field:'crtUser', title: '创建人',width:'10%'}
		      ,{field:'crtDate', title: '创建日期',width:'10%'}
		      ,{field:'tools', title: '操作',width:'12%',toolbar:operateBar,fixed: 'right'}
		    ]]
		  ,page:true
		  ,initSort: {
		    field: 'pdrCode' //排序字段，对应 cols 设定的各字段名
		    ,type: 'desc' //排序方式  asc: 升序、desc: 降序、null: 默认排序
		  }
	  });
	}
	//表格监听事件
	 table.on('tool(docTool)', function(obj) {
		var data = obj.data;
		var id=data.id;
		if(obj.event == 'show'){
			//查看
			var type="2";
			var url=prefix+"/pdrSpecialDetailPage?mainId="+id+"&type="+type;
			//window.location.href=url;
			tuoGo(url,'pdrDetail','list-table');
		}else if (obj.event === 'edit') {
			var status = data.status;
			if(status == '已提交'){
				layer.msg("已提交状态下不允许编辑！",{time:1000});
				return;
			}
			//编辑
			var type="1";
			var url=prefix+"/pdrSpecialDetailPage?mainId="+id+"&type="+type;
			//window.location.href=url;
			tuoGo(url,'pdrDetail','list-table');
		}else if(obj.event === 'del'){
			
			var status = data.status;
			if(status == '已提交'){
				layer.msg("已提交状态下不允许删除！",{time:1000});
				return;
			}
			
			layer.confirm('是否删除该生产日报？', {
			  btn: ['确定','取消'] //按钮
			}, function(){
				//删除
				$.ajax({
					type:'POST',
					url:prefix+"/deletePdr",
					data:{id:id},
					success:function(msg){
						reloadTable();
						layer.msg("操作成功！",{time:1000});
					},
					error:function(){
						layer.msg("操作失败！",{time:1000});
					}
				});
			}, function(){
			});
		}
	});
	function reloadTable(){
		var pdrCode=$("#search_pdrCode").val();
		var status=$("#search_status").val();
		var statusName=$("#search_status").find("option:selected").text();
		if(statusName == '请选择'){
			statusName = "";
		}
		
		var crtDateStart=$("#search_crtDateStart").val();
		var crtDateEnd=$("#search_crtDateEnd").val();
		var prodDateStart=$("#search_prodDateStart").val();
		var prodDateEnd=$("#search_prodDateEnd").val();
		var suppName = $("#search_suppName").val();
		var url=prefix+"/getPdrSpecialPageList?search_pdrCode="+pdrCode+"&search_status="+statusName
		+"&search_crtDateStart="+crtDateStart+"&search_crtDateEnd="+crtDateEnd
		+"&prodDateStart="+prodDateStart+"&prodDateEnd="+prodDateEnd+"&suppName="+suppName;
		initListTable(url);
	}
	
	//新建按钮点击事件
	$("#createBtn").click(function(){
		var type='1';
		var url=prefix+"/pdrSpecialDetailPage?mainId="+"&type="+type;
		//window.location.href=url;
		tuoGo(url,'pdrDetail','list-table');
	});
	
	//删除按钮点击事件
	$("#deleteBtn").click(function(){
		var rows = table.checkStatus('list-table').data;
		var ids=[];
		for(var i=0;i<rows.length;i++){
			if(rows[i].status == '已保存'){
				ids.push(rows[i].id);
			}
		}
		
		if(ids.length == 0){
			layer.msg("请选择需要删除的已保存的生产日报！",{time:1000});
			return;
		}
		
		layer.confirm('是否删除选中的已保存的生产日报？', {
		  btn: ['确定','取消'] //按钮
		}, function(){
			$.ajax({
				type:'POST',
				url:prefix+"/deleteBatchPdr",
				data:{ids:JSON.stringify(ids)},
				success:function(msg){
					reloadTable();
					layer.msg("操作成功！",{time:1000});
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
			});
		}, function(){
			
		});
	});
	//导出库存事件
	$("#exportBtn").click(function(){
		exportStock();
	});
	// 文档查询点击事件
	$("#searchBtn").click(function(){
		reloadTable();
	});

	//查询条件重置
	$("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	});
	
	// 同步产能上报
	$("#syncBtn").click(function(){
		var rows = table.checkStatus('list-table').data;
		var ids=[];
		for(var i=0;i<rows.length;i++){
			if(rows[i].status == '已提交'){
				ids.push(rows[i].id);
			}
		}
		
		if(ids.length == 0){
			layer.msg("请选择需要同步的已提交的生产日报！",{time:2000});
			return;
		}
		
		layer.confirm('是否同步选中的已提交的生产日报？', {
		  btn: ['确定','取消'] //按钮
		}, function(){
			// 启动加载层
			var index = layer.load(0, {shade: false});
			$.ajax({
				type:'POST',
				url:prefix+"/syncPdrInfo",
				data:{pdrIds:JSON.stringify(ids)},
				success:function(msg){
					if(msg.code == 0){
						layer.msg("同步成功！",{time:2000});
					}else{
						layer.msg("同步失败："+msg.msg,{time:2000});
					}
			    	// 关闭加载层
			    	layer.close(index);
				},
				error:function(){
					layer.msg("同步失败！",{time:2000});
			    	// 关闭加载层
			    	layer.close(index);
				}
			});
		}, function(){
			
		});
	});
	
	/**
	 * 导出供应商库存数据
	 * @returns
	 */
	function exportStock(){
		/*var rows = table.checkStatus('list-table').data;
		if(rows.length == 0){
			layer.msg("请选择需要导出的供应商！",{time:1000});
			return;
		}
		
		var ids=[];
		for(var i=0;i<rows.length;i++){
			if(rows[i].status == '已保存'){
				ids.push(rows[i].id);
			}
		}
		

		var url = prefix+"/exportSaleFcstInfo?id="+checkedData[0].id+"&year="+checkedData[0].fsctYear;
		location.href=url;*/
	}
});
