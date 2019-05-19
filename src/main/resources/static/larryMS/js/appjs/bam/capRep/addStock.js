layui.use([ 'form','layer'], function() {
	var $=layui.jquery;
	var prefix="/capRep";
	var layer=layui.layer;
	var form=layui.form;
	//加载页面初始化表格
	initMates();
	//确认
	$("#confirmBtn").click(function(){
		//保存库存信息数据
		var stock=$('#stockForm').serializeJSON();
		stock.id=guid();
		parent.stockData.push(stock);
		parent.reloadStockTable();
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
