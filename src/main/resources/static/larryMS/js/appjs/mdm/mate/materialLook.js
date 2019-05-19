layui.use('table', function() {
	var table = layui.table;
	var $ = layui.$;
	//返回
	 $("#goBack").click(function(){
	 	//window.history.back(-1);
		 tuoBack('.materialLook','#serachSupp');
	  });
	$(".disa").attr("disabled",true);
	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});

	
});
