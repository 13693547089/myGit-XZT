//地址前缀
var prefix = "/bam/tran";
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
}

//定义树形控件
layui.use(['table','laydate'], function() {
	var $ = layui.$;
	var table = layui.table;
	var laydate = layui.laydate;
	// 定义日期控件
	laydateInit(laydate);
	
	initListTable(prefix+"/getTranPlanPageList");
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
		      ,{field:'tranDate', title: '调拨日期',width:'15%'}
		      ,{field:'tranCode', title: '计划编码',width:'13%'}
		      /*,{field:'produceDate', title: '生产日期',width:'10%',
		    	  templet: function(d){
		    		  return formatTime(d.produceDate,'yyyy-MM-dd');
		    	  }
		      }*/
		      ,{field:'tranName', title: '计划名称',width:'25%'}
		      ,{field:'crtUser', title: '创建人',width:'10%'}
		      ,{field:'crtDate', title: '创建日期',width:'10%'}
		      ,{field:'tools', title: '操作',width:'12%',toolbar:operateBar,fixed: 'right'}
		    ]]
		  ,page:true
		  ,initSort: {
		    field: 'tranCode' //排序字段，对应 cols 设定的各字段名
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
			var url=prefix+"/tranPlanDetailPage?mainId="+id+"&type="+type;
			//window.location.href=url;
			tuoGo(url,'tranPlanDetail','list-table');
		}else if(obj.event === 'edit') {
			var status = data.status;
			if(status == '已提交'){
				layer.msg("已提交状态下不允许编辑！",{time:1000});
				return;
			}
			//编辑
			var type="1";
			var url=prefix+"/tranPlanDetailPage?mainId="+id+"&type="+type;
			//window.location.href=url;
			tuoGo(url,'tranPlanDetail','list-table');
		}else if(obj.event === 'del'){
			
			var status = data.status;
			if(status == '已提交'){
				layer.msg("已提交状态下不允许删除！",{time:1000});
				return;
			}
			
			layer.confirm('是否删除该调拨计划？', {
			  btn: ['确定','取消'] //按钮
			}, function(){
				//删除
				$.ajax({
					type:'POST',
					url:prefix+"/deleteTranPlan",
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
		var name=$("#search_name").val();
		var status=$("#search_status").val();
		var statusName=$("#search_status").find("option:selected").text();
		if(statusName == '请选择'){
			statusName = "";
		}
		
		var crtDateStart=$("#search_crtDateStart").val();
		var crtDateEnd=$("#search_crtDateEnd").val();
		var url=prefix+"/getTranPlanPageList?search_name="+name+"&search_status="+statusName
		+"&search_crtDateStart="+crtDateStart+"&search_crtDateEnd="+crtDateEnd;
		initListTable(url);
	}
	
	//新建按钮点击事件
	$("#createBtn").click(function(){
		var type='1';
		var url=prefix+"/tranPlanDetailPage?mainId="+"&type="+type;
		//window.location.href=url;
		tuoGo(url,'tranPlanDetail','list-table');
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
			layer.msg("请选择需要删除的已保存的调拨计划！",{time:1000});
			return;
		}
		
		layer.confirm('是否删除选中的已保存的调拨计划？', {
		  btn: ['确定','取消'] //按钮
		}, function(){
			$.ajax({
				type:'POST',
				url:prefix+"/deleteBatchTranPlan",
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
	
	//导出事件
	$("#exportBtn").click(function(){

		var rows = table.checkStatus('list-table').data;
		if(rows.length != 1){
			layer.msg("请选择一条需要导出的调拨记录！",{time:2000});
			return;
		}

		var mainId = rows[0].id;
		
		var url = prefix+"/exportTranPlanInfo?mainId="+mainId;
		location.href=url;
	});
	
	// 引用创建
	$("#refCreateBtn").click(function(){
		var rows = table.checkStatus('list-table').data;
		if(rows.length != 1){
			layer.msg("请选择一条需要引用的调拨记录！",{time:2000});
			return;
		}
		// 引用的ID
		var refId = rows[0].id;
		// 引用创建类型
		var type='3';
		var url=prefix+"/tranPlanDetailPage?mainId="+"&type="+type+"&refId="+refId;
		tuoGo(url,'tranPlanDetail','list-table');
	});
	
	// 文档查询点击事件
	$("#searchBtn").click(function(){
		reloadTable();
	});

	//查询条件重置
	$("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	});
});