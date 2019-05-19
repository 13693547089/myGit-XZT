
layui.use([ 'form','layer'], function() {
	var $=layui.jquery;
	var prefix="/suppProd";
	var layer=layui.layer;
	var dateNum=30;
	var report = {};
	var mainId=$("#suppProdId").val();
	//加载页面初始化表格
	loadCalendar();
	//查看是否排产
	var status=$("#status").val();
	if(status!='未排产'){
		$.ajax({
			url:"/suppProd/getSuppProdPlanByMainId",
			type:'POST',
			data:{mainId:mainId},
			success:function(res){
				loadCalenderData(res);
			},
		});
	}
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
	/**
	 * 给日历加载数据
	 * @returns
	 */
	function loadCalenderData(dataSource){
		$(".sent").each(function(index,row){
			var currDiv=$(this);
			var  calendarDate= currDiv.children("span.clickspan").html();
			$.each(dataSource,function(index,row){
				var date=new Date(row.planDate).getDate();
				if(calendarDate==date){
					var num=row.planNum;
					var status=row.status;
					currDiv.children("input.project").attr('readonly','readonly');
					currDiv.children("input.project").val(num);
				}
			});
		});
	}
	/**
	 * 加载日历控件
	 * @returns
	 */
	function loadCalendar(){
		var planMonth=$("#planMonth").val();
		var longDate=Number(planMonth);
		var date = new Date(longDate);
		report.yyyy = date.getFullYear();
		report.mm = date.getMonth();
		report.day = date.getDate();
		createCalendar(report.yyyy,report.mm);
		dateNum=getNumber(report.yyyy,report.mm);
	}
})

