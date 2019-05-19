var table;
layui.use('table', function() {
	
	  table = layui.table;
	  var $ = layui.$;
	  initDicTable();
	  //监听表格复选框选择
	  table.on('checkbox(dicDetailTable)', function(obj){
	    //console.log(obj)
	  });

	//监听工具条
	  table.on('tool(dicDetailTable)', function(obj){
		 
	    var data = obj.data;
	    var cateId = data.id;
	    if(obj.event === 'del'){//删除
		      layer.confirm('确定删除吗？', function(index){
		    	  debugger;
		    	  var selecteds =[];
		    	  selecteds.push(data);
		    	  $.ajax({
		    		 type:"post",
		    		 url:"/deleteCategoryInfo",
		    		 dataType:"JSON",
		    		 data:{selected:JSON.stringify(selecteds)},
		    		 success:function(data2){
		    			 if(data2){
		    				 layer.msg('删除成功', {time:2000 });
		    				 initDicTable();
		    			 }else{
		    				 layer.alert('<span style="color:red;">删除失败</sapn>');
		    			 }
		    		 }
		    	  });
		    	  layer.close(index);
		      });
	    
	    } else if(obj.event === 'edit'){//编辑
	    	debugger;
    		var url  ="/dicAddHtml?id="+cateId+"&type=1";
    		/*location=url;*/
    		window.location.href=url;
    		 /* $.ajax({
		    		 type:"post",
		    		 url:url,
		    		 dataType:"JSON",
		    		 data:{selected:JSON.stringify(selecteds)},
		    		 success:function(data2){
		    			 if(data2){
		    				 layer.msg('修改成功', {time:2000 });
		    				 initDicTable();
		    			 }else{
		    				 layer.alert('<span style="color:red;">修改失败</sapn>');
		    			 }
		    		 }
		    	  });*/
	    }
	  });
	  
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      var categoryName = $('#categoryName');
			      var categoryCode = $("#categoryCode");
			      var isUsed = $("#isUsed");
			      //执行重载
			      table.reload('dicId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	cateName: categoryName.val(),
			        	cateCode:categoryCode.val(),
			        	isUsed:isUsed.val()
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
		  });
		
	  
	  //新建字典信息
	  $("#addBtn").on('click', function() {
		 var url="/dicAddHtml?type=0";
		 location=url;
		});

	/*  // 新建字典信息
		$('#addBtn').click(function() {
			$('#code').val("");
			$('#name').val("");
			$('#cateId').val("");
			inintDicInfoTable({});
		});*/

	  //重置
	  $("#reset").click(function(){
	  	$("#categoryName").val('');
	  	$("#categoryCode").val('');
	  	$("#status").val('');
	  });
	  
	  
	  
});

function initDicTable() {
	table.render({
		elem : "#dicTable",
		url : '/dicInfo/dic_1',
		page : true,
		width : '100%',
		minHeight : '20px',
		limit : 10,
		id : "dicId",
		cols : [[
		   {checkbox : true,width:50,fixed : 'center'}, 
		   {field : 'cateName',width:260,title : '分类名称'}, 
		   {field : 'cateCode',width:200,title : '分类编码'}, 
		   {field : 'isUsed',width:100,title : '状态'}, 
		   {field : 'creatorName',width:160,title : '创建人'}, 
		   {field : 'createTime',width:200,title : '创建时间',
			   templet : function(d) {return formatTime(d.createTime, 'yyyy-MM-dd');}},  
		   {field : 'modifier',width:100,title : '修改人'}, 
		   {field : 'updatetime',width:200,title : '修改时间',
			   templet : function(d) {return formatTime(d.createTime, 'yyyy-MM-dd');}},
		   {fixed: 'right', title:'操作',width:220, align:'center', toolbar: '#barDemo'}
		   ]]
	});
};