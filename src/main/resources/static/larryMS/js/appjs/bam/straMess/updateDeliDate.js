var table;
layui.use(['form','table','laydate'], function(){
	var form = layui.form;
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  laydate.render({
		 elem:"#arriDate",
	  });
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
});

function getData(){
	 var messId = $("#messId").val();
	 var arriDate = $("#arriDate").val();
	 var result = checkMust();
	 var map={};
	 if(!result.flag){
		 map.judge=false;
		 map.msg=result.msg;
		 return map;
	 }else{
		 map.judge=true;
		 map.messId = messId;
		 map.arriDate = arriDate;
		 return map;
	 }
}






