layui.use([ 'form','layer','upload','laydate'], function() {
	var $=layui.jquery;
	var prefix="/capRep";
	var layer=layui.layer;
	var form=layui.form;
	var upload = layui.upload;
	var mainId=$("#mainId").val();
	var laydate=layui.laydate;
	var id=guid();
	$('#id').val(id);;
	laydate.render({
		  elem: '#expectCompleteTime' //指定元素
	});
	initMates();
	//加载页面初始化表格
	//确认
	$("#confirmBtn").click(function(){
		var result =checkMust();
		if(result.flag==false){
			layer.msg(result.msg, {
				time : 1000
			});
			return false;
		}
		//保存库存信息数据
		var orderForm=$('#orderForm').serializeJSON();			   
		parent.orderData.push(orderForm);
		console.info(JSON.stringify(orderForm));
		parent.reloadOrderMateTable();
		closeDg();
	});
	//取消按钮点击事件
	$("#cancleBtn").click(function(){
		closeDg();
	});
	/**
	 * layer关闭子表窗口
	 * @returns
	 */
	function closeDg(){
		var index = parent.layer.getFrameIndex(window.name);//获取子窗口索引
		 parent.layer.close(index);
	}
	upload.render({
	    elem: '#addFile' //绑定元素
	    ,url: '/doc/docUpload?linkId='+id+'&direCode=CNSB' //上传接口
	    ,accept:'file'
	    ,done: function(res){
	      //上传完毕回调
	    	if(res.code==0){
	    		var file=res.data[0];
	    		$('#fileName').html(file.realName);
	    		$('#orderAttach').val(JSON.stringify(file));	    		    
	    	}
	    }
	 });
	function initMates(){
		var mates=parent.mateData;
		var str='<option value="">请选择</option>';
		$.each(mates,function(index,row){
			str+='<option value="'+row.mateCode+'">'+row.mateCode+row.mateDesc+'</option>';
		});
		$("#mateCode").html(str);
		//重新初始化下拉菜单
		form.render('select');
	}
	form.on('select(mateCode)', function(data){
		 var mateCode=$("#mateCode").val();
		 var mateDesc=$(data.elem).find("option:selected").text();
		 $("#mateDesc").val(mateDesc.substr(mateCode.length));
	});
})
