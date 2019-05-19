layui.use('table', function(){
	  var table = layui.table;
	  var $ = layui.$;
	  //条件搜索
	  var $ = layui.$, active = {
			    reload: function(){
			      var mateInfo = $('#mateInfo');
			      var mateGroupInfo = $("#mateGroupInfo");
			      var mateTypeInfo = $("#mateTypeInfo");
			      //执行重载
			      table.reload('mateTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {
			        	 mateInfo: mateInfo.val(),
			        	 mateGroupInfo:mateGroupInfo.val(),
			        	 mateTypeInfo:mateTypeInfo.val()
			        }
			      });
			    }
		};
	  //监听单元格事件
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    console.info(data.mateId)
	    if(obj.event === 'setSign'){
	     var url ="getMaterialLookHtml?mateId="+data.mateId;
	     location=url;
	    }
	  });
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  
	//重置
	  $("#reset").click(function(){
	  	$("#mateInfo").val('');
	  	$("#mateGroupInfo").val('');
	  	$("#mateTypeInfo").val('');
	  });
});


