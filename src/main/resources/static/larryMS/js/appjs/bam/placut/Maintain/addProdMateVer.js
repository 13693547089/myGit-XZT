var table;
layui.use(['form','table'], function(){
	var form = layui.form;
	  table = layui.table;
	  var $ = layui.$;
	  var isSpecial = $("#isSpecial").val();
	  if(isSpecial == "YES"){
		  $("#cutProdType").val("自制打切");
	  }else{
		  $("#cutProdType").val("OEM打切");
	  }
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
});

function getVersion(){
	 var version = $("#version").val();
	 var prodId = $("#prodId").val();
	 var cutAim = $("#cutAim").val();
	 var mainStru = $("#mainStru").val();
	 var result = checkMust();
	 var map={};
	 if(!result.flag){
		 map.judge=false;
		 map.msg=result.msg;
		 return map;
	 }else{
		 map.judge=true;
		 map.version = version;
		 map.prodId = prodId;
		 map.cutAim = cutAim;
		 map.mainStru = mainStru;
		 return map;
	 }
}






