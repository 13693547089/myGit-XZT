var table;
layui.use(['form','table','laydate'], function(){
	var form = layui.form;
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  laydate.render({
		 elem:"#appoDate",
		 min: 0,
		 max: 30	
	  });
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
});

function getData(){
	 var appoDate = $("#appoDate").val();
	 var affirmDate = $("#affirmDate").val();
	 var appoCode = $("#appoCode").val();
	 var appoStatus = $("#appoStatus").val();
	 var result = checkMust();
	 var map={};
	 if(!result.flag){
		 map.judge=false;
		 map.msg=result.msg;
		 return map;
	 }else{
		 map.judge=true;
		 map.appoDate = appoDate;
		 map.affirmDate = affirmDate;
		 map.appoCode = appoCode;
		 map.appoStatus = appoStatus;
		 return map;
	 }
}






