var table;
layui.use(['form','table'], function(){
	var form = layui.form;
	  table = layui.table;
	  var $ = layui.$;
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
});

function getVersion(){
	 var id = $("#id").val();
	 var inboDeliCode = $("#inboDeliCode").val();
	 var isOccupy = $("#isOccupy").val();
	 var result = checkMust();
	 var map={};
	 if(!result.flag){
		 map.judge=false;
		 map.msg=result.msg;
		 return map;
	 }else{
		 map.judge=true;
		 map.id = id;
		 map.inboDeliCode = inboDeliCode;
		 if(isOccupy == '是'){
			 isOccupy ='yes';
		 }else if(isOccupy =='否'){
			 isOccupy ='no';
		 }
		 map.isOccupy = isOccupy;
		 return map;
	 }
}






