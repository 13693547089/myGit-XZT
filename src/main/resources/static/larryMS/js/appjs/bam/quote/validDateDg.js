layui.use([ 'form', 'laydate','layer'], function() {
	var $=layui.jquery;
	var prefix="/quote";
	var layer=layui.layer;
	var laydate=layui.laydate;
	var form=layui.form;
	//初始化日历控件
	laydate.render({
		  elem: '#validStart' //指定元素
		});
	laydate.render({
		elem: '#validEnd' //指定元素
	});
	//确认
	$("#confirmBtn").click(function(){
		var quoteCode=$("#quoteCode").val();
		var validStart=$("#validStart").val();
		var validEnd=$("#validEnd").val();
		$.ajax({
			type:"POST",
			data:{quoteCode:quoteCode,validStart:validStart,validEnd:validEnd},
			url:prefix+"/updateValidDate",
			success:function(res){
				if(res.code==0){
					parent.reloadTable();
				}
			}
		});
		//调用父窗口的方法
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
})
