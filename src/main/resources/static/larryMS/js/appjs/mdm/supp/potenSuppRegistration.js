
$("#down1").click(function(){
	var docId = $("#yyzz").val();
	if(docId == "" || docId == undefined || docId == "down01"){
		alert("未找到相关附件");
		return ;
	}
	downFile(docId);
});
$("#down2").click(function(){
	var docId = $("#gys").val();
	if(docId == "" || docId == undefined || docId == "down02"){
		alert("未找到相关附件");
		return ;
	}
	downFile(docId);
});

<!--立即注册 -->
$('.register').click(function(){
	$('.DIV_mind_regi_notice').hide();
	$('.DIV_mind_regi_data').show();
	$(".Img").attr ("src", "/img/type2.png");
	
})
<!--基本资料上一步-->
$('.dataPush').click(function(){
	$('.DIV_mind_regi_notice').show();
	$('.DIV_mind_regi_data').hide();
	$(".Img").attr ("src", "/img/type1.png");
	
})
<!--基本资料下一步-->
$('.dataNext').click(function(){
	 for(var i=0;i<8;i++){
		 if($('input').eq(i).val()==''){
			 alert('请填写必输项')
			 return;
		 }
	 }
	$('.DIV_mind_regi_data').hide();
	$('.DIV_mind_regi_bank').show();
	$(".Img").attr ("src", "/img/type3.png");
	establishSocket();
})
<!--银行上一步-->
$('.bankPush').click(function(){
	$('.DIV_mind_regi_data').show();
	$('.DIV_mind_regi_bank').hide();
	$(".Img").attr ("src", "/img/type2.png");
	
})

<!--银行下一步-->
$('.bankNext').click(function(){
	for(var i=9;i<16;i++){
		if($('input').eq(i).val()==''){
			alert('请填写必输项')
			return;
		}
	}
	var suppId = guid();
	$("#suppId").val(suppId);
	var suppName = $("#suppName").val();
	if(suppName==null || suppName ==""){
		suppName="供应商审批任务";
	}
	var ajax_option={
		    type: "POST",
			url:'/suppReg/docUploadPaperAndSaveSupp?direCode='+'MBGL',
			dataType:"JSON",
			success:function(res){
				if(res != null){
			    		if(res.code=='0'){
			    			$('.DIV_mind_regi_comp').show();
			    			$('.DIV_mind_regi_bank').hide();
			    			$(".Img").attr ("src", "/img/type4.png");
			    			// 发送通讯信息
			    			sendToMessage(suppId);
			    		}
			    }
		    },
		    error:function(data){
		    	console.log('程序异常');
		    }
		}
	$('#latentSuppForm').ajaxSubmit(ajax_option);
	$('#bankNext').attr("disabled","disabled");
	$('#bankPush').attr("disabled","disabled");
	/*var suppFormData = new FormData($('#latentSuppForm')[0]);// 序列化表单，
	 // 文件上传
	 $.ajax({
	 		type: "POST",
	 		url:'/suppReg/docUploadPaperAndSaveSupp?direCode='+'MBGL',
	 		data:suppFormData,
	 		processData: false,// tell jQuery not to process the data
	 		contentType: false,// tell jQuery not to set contentType，必须false
	 		dataType: "json",
	 		async: false,
	 	    error: function(request) {
	 	    	// console.log('失败')
	 	    },
	 	    success: function(res) {
	 	    	// console.log('成功')
	 	    	if(res != null){
	 	    		if(res.code=='0'){
	 	    			$('.DIV_mind_regi_comp').show();
	 	    			$('.DIV_mind_regi_bank').hide();
	 	    			$(".Img").attr ("src", "/img/type4.png");
	 	    			// 发送通讯信息
	 	    			sendToMessage(suppId);
	 	    		}
	 	    	}
	 	    }
	 }); */ 
})
/* input移入事件 */
$('input').change(function(){
	$(this).css('border','none');
	
})
$('.Slect').change(function(){
	$(this).css('border','none');
	
})
$(".Slect").mouseout(function(){
	if($(this).val()==''){
		$(this).css('border','1px solid #e6e6e6')
	}
  });
$("input").blur(function(){
	// console.log($(this).val())
	if($(this).val()==''){
		$(this).css('border','1px solid #e6e6e6')
	}
  });
<!--跳转登录-->
/*
 * $('.Close').click(function(){ window.location.href='/login' })
 */
$('.Close').click(function(){
	 if (navigator.userAgent.indexOf("Firefox") != -1 || navigator.userAgent.indexOf("Chrome") !=-1) {  
	        window.location.href="about:blank";  
	        window.close();  
	    } else {  
	        window.opener = null;  
	        window.open("", "_self");  
	        window.close();  
	    }  
})

function establishSocket() {
	var socket = new SockJS('/endpointAric'); // 连接SockJS的endpoint名称为"endpointWisely"
	stompClient = Stomp.over(socket);// 使用STMOP子协议的WebSocket客户端
	stompClient.connect({}, function(frame) {// 连接WebSocket服务端
		stompClient.subscribe('/topic/getResponse', function(response) {
		});
	});
}

function sendToMessage(sdata1) {
	// 通过stompClient.send向/welcome
	// 目标(destination)发送消息,这个是在控制器的@MessageMapping中定义的
	stompClient.send("/welcome", {}, JSON.stringify({
		'sdata1' : sdata1
	}));
}
// 生成GUID
function guid() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}
//下载
function downFile(docId){
	window.location.href = "/suppReg/downLoadDoc?docId=" + docId
}



