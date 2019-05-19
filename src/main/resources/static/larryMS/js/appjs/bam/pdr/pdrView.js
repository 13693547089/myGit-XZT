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
	
	initListTable(prefix+"/getPdrViewList");
	//初始化表格
	function initListTable(url){
	  var operateBar="#barDemo";
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
		      ,{field:'tools', title: '操作',width:'12%',align:'center',toolbar:operateBar,fixed: 'right'}
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
			var url=prefix+"/pdrViewDetailPage?mainId="+id+"&type="+type;
			//window.location.href=url;
			tuoGo(url,'pdrViewDetail','list-table');
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
		var url=prefix+"/getPdrViewList?search_pdrCode="+pdrCode+"&search_status="+statusName
		+"&search_crtDateStart="+crtDateStart+"&search_crtDateEnd="+crtDateEnd
		+"&prodDateStart="+prodDateStart+"&prodDateEnd="+prodDateEnd+"&suppName="+suppName;
		initListTable(url);
	}
	
	//导出事件
	$("#exportBtn").click(function(){
		
	});
	
	// 文档查询点击事件
	$("#searchBtn").click(function(){
		reloadTable();
	});
	
	//查询条件重置
	$("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	});
	
	var active = {
	    reload: function(){
	      var demoReload = $('#demoReload');
	      
	      //执行重载
	      table.reload('testReload', {
	        page: {
	          curr: 1 //重新从第 1 页开始
	        }
	        ,where: {
	          key: {
	            id: demoReload.val()
	          }
	        }
	      });
	    }
	  };
	
	  $('#searchBtn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
});