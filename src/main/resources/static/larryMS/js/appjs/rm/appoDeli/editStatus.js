var table;
layui.use(['form','table'], function(){
	var form = layui.form;
	  table = layui.table;
	  var $ = layui.$;
	  var index = parent.layer.getFrameIndex(window.name);
	  var parentData = parent.appoData;
	  //$("#code").val(parentData.appoCode);
	  $("#codeDesc").text(parentData.appoCodeDesc);
	  //$("#status").val(parentData.appoStatus);
	  $("#statusDesc").html('<i class="red xinghao">*</i>'+parentData.appoStatusDesc);
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
});

function getVersion(){
	 var code = $("#code").val();
	 var status = $("#status").val();
	 var result = checkMust();
	 var map={};
	 if(!result.flag){
		 map.judge=false;
		 map.msg=result.msg;
		 return map;
	 }else{
		 map.judge=true;
		 map.code = code;
		 map.status = status;
		 return map;
	 }
}






