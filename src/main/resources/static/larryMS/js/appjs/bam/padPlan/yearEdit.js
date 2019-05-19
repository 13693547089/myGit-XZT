layui.use([ 'form','layer','table'], function() {
	var $=layui.jquery;
	var prefix = "/bam/ps";
	var layer=layui.layer;
	var form=layui.form;
	var table=layui.table;
	var planMonth=parent.$('#planMonth').val()+"";
	var detailData=[];
	initMates();
	initTable(detailData);
	//加载页面初始化表格
	//确认
	$("#confirmBtn").click(function(){
		console.info(JSON.stringify(detailData));
		closeDg();
	});
	//取消按钮点击事件
	$("#cancleBtn").click(function(detailData){
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
		var mates=parent.tableData;
		var str='<option value="">请选择</option>';
		$.each(mates,function(index,row){
			str+='<option value="'+row.matCode+'">'+row.matCode+row.matName+'</option>';
		});
		$("#mateCode").html(str);
		//重新初始化下拉菜单
		form.render('select');
	}
	form.on('select(mateCode)', function(data){
		 var mateCode=$("#mateCode").val();
		 $.ajax({
			 url:prefix+"/detail/year",
			 data:{mateCode:mateCode,planMonth:planMonth},
			 method:'get',
			 success:function(res){
				 detailData=res;
				 initTable();
			 },
			 error:function(){
				 layer.msg("保存成功！",{time: 1000});
			 }
		 });
	});
	function initTable(){
		table.render({
		     elem: '#yearEditTable'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,data:detailData
		    ,cols: [[
			   {field:'matCode', title: '物料编号'}
		      ,{field:'matName', title: '物料名称'}
		      ,{field:'planMonth', title: '计划月份',}
		      ,{field:'padPlanQty', title: "交货计划",edit: 'text',event: 'setSign'}
		    ]]
		  ,page:true
		  ,id:'yearEditTable'
		});
	}
})
