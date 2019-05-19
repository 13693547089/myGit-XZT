//地址前缀
var prefix = "/bam/ps";
//选中节点
var direId="";

//定义树形控件
layui.use(['table','laydate' ], function() {
	var $ = layui.$;
	var table = layui.table;
	//定义日期格式
	var laydate = layui.laydate;
	//常规用法
    laydate.render({
    	elem: '#search_crtDateStart'
    });
	laydate.render({
		elem: '#search_crtDateEnd'
	});
	
	initListTable(prefix+"/getPadPlanPageList?flag=2");
	//初始化表格
	function initListTable(url){
		//var str=$('#createSpan').html().trim();
		var operateBar="#barDemo";
		/*if( str==''){
			operateBar="#barDemo1";
		}*/
	   table.render({
		     elem: '#list-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:url
		    //,id:'olist'
		    //,even: true
		    ,cols: [[
		       {checkbox:true}
			  ,{field:'sn', title: '序号',width:'5%',type:'numbers'}
			  ,{field:'status', title: '状态',width:'7%'}
		      ,{field:'planMonth', title: '计划月份',width:'10%'}
		      ,{field:'planCode', title: '计划编号',width:'15%'}
		      ,{field:'planName', title: '计划名称'}
		      ,{field:'crtUser', title: '创建人',width:'10%'}
		      ,{field:'crtDate', title: '创建日期',width:'10%',
		    	  templet: function(d){
		    		  return formatTime(d.crtDate,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'tools', title: '操作',width:'12%',align:'center',toolbar:operateBar,fixed: 'right'}
		    ]]
		  ,page:true
		  ,initSort: {
		    field: 'planCode' //排序字段，对应 cols 设定的各字段名
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
			var url=prefix+"/padPlanDetailPage?mainId="+id+"&type="+type;
			//window.location.href=url;
			tuoGo(url,'padPlanDetail','list-table');
		}
	});
	function reloadTable(){
		var planName=$("#search_planName").val();
		var status=$("#search_status").val();
		var statusName=$("#search_status").find("option:selected").text();
		if(statusName == '请选择'){
			statusName = "";
		}
		
		var crtDateStart=$("#search_crtDateStart").val();
		var crtDateEnd=$("#search_crtDateEnd").val();
		var url=prefix+"/getPadPlanPageList?search_planName="+planName+"&search_status="+statusName
		+"&search_crtDateStart="+crtDateStart+"&search_crtDateEnd="+crtDateEnd+"&flag=2";
		initListTable(url);
	}
	
	// 文档查询点击事件
	$("#searchBtn").click(function(){
		reloadTable();
	});
	
	//查询条件重置
	$("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	});
	
	//导出事件
	$("#exportBtn").click(function(){
		exportExcel();
	});
	
	// 同步事件
	$("#syncBtn").click(function(){
		syncMonthData();
	});
});

/**
 * 导出excel
 * @returns
 */
function exportExcel(){
	
	// ajax 提交不能触发页面的保存框
	/*$.ajax({
		type:'POST',
		url:prefix+"/exportPadPlanInfo",
		//data:{ids:JSON.stringify(ids)},
		success:function(msg){
			layer.msg("操作成功！",{time:1000});
		},
		error:function(){
			layer.msg("操作失败！",{time:1000});
		}
	});*/
	
	var url = prefix+"/exportPadPlanInfo";
	location.href=url;
}

/**
 * 同步本月的物料数据
 * @returns
 */
function syncMonthData(){
	// 获取当前年月
	var padMonth = new Date();	
	var month = (padMonth.getMonth()+1);
	month = month<10?"0"+month:month;
	padMonth = padMonth.getFullYear()+"-"+month;
	
	var confirmDialog = layer.confirm('是否同步'+padMonth+'月物料数据？', {
	  btn: ['确定','取消'] //按钮
	}, function(){
		layer.close(confirmDialog);
		
		// 启动加载层
		var index = layer.load(0, {shade: false});
		// 获取明细数据
		$.ajax({
			 type:"post",
			 url:"/sync/syncPadMaterial",
			 data:{"padMonth":padMonth},
			 dataType:"JSON",
			 success:function(data){
				// 关闭加载层
				layer.close(index);
				if(data.code==0){
					layer.msg("同步成功！",{time: 2000});
				}else{
					layer.msg(data.msg,{time: 2000});
				}
			 },error: function (XMLHttpRequest, textStatus, errorThrown) {
		           /*  // 状态码
	             console.log(XMLHttpRequest.status);
	             // 状态
	             console.log(XMLHttpRequest.readyState);
	             // 错误信息   
	             console.log(textStatus);*/
				 
	 	    	// 关闭加载层
	 	    	layer.close(index);
	         }
		});
	}, function(){
	});
}