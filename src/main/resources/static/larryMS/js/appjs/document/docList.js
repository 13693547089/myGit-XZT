//地址前缀
var prefix = "/doc";
//选中节点
var direId="";
var where={};
//定义树形控件
layui.use([ 'tree', 'table','laytpl' ,'form','laydate'], function() {
	var $ = layui.$;
	var table = layui.table;
	var laytpl = layui.laytpl;
	var form=layer.form;
	var laydate = layui.laydate;
	// 日期控件格式化
	laydate.render({
		elem : '#startDate'
	});
	laydate.render({
		elem : '#endDate'
	});
	$("#resetBtn").click(function() {
		$("#searchForm")[0].reset();
	});
	initDocTree();
	initDocTable(where);
	function setCondition(){
		var docName=$("#docName").val();
		where.docName=docName;
		var createUser=$("#createUser").val();
		where.createUser=createUser;
		var startDate=$("#startDate").val();
		where.startDate=startDate;
		var endDate=$("#endDate").val();
		where.endDate=endDate;
		var linkNo=$("#linkNo").val();
		where.linkNo=linkNo;
	}
	//初始化树形控件
	function initDocTree(table){
		$.ajax({
			cache : true,
			type : "POST",
			url : prefix + "/getDireTree",
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				layui.tree({
					elem : '#doc-tree', // 传入元素选择器
					skin : '',
					click : function(item) {
					    direId=item.id;
						where.direId=direId;
						setCondition();
//						var url=prefix+"/getDocByPage?direId="+direId+"&docName="+docName;
						initDocTable(where);	
					},
					nodes : data
				});
			}
		});
	}
	//初始化表格
	function initDocTable(where ){
	   table.render({
		     elem: '#doc-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:prefix+"/getDocByPage"
		    ,where:where
		    ,cols: [[
		       {checkbox:true}
		      ,{field:'linkNo', title: '关联单据',width:'12%'}
		      ,{field:'realName', title: '文件名称',width:'24.5%'}
		      ,{field:'creater', title: '上传人',width:'20%'}
		      ,{field:'createTime', title: '上传日期',width:'10%',
		    	  templet: function(d){
		    		  return formatTime(d.createTime,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'createUser', title: '操作',width:'10%',toolbar:"#barDemo"}
		    ]]
		  ,page:true
	  });
	}
	//表格监听事件
	 table.on('tool(docTool)', function(obj) {
		var data = obj.data;
		if (obj.event === 'downLoad') {
			var url=prefix+"/downLoadDoc?docId="+data.id;
			window.location.href=url;
		}
	});
	// 文档查询点击事件
	$("#searchBtn").click(function(){
		setCondition();
//		var url=prefix+"/getDocByPage?direId="+direId+"&docName="+docName;
		initDocTable(where);
	});
});

