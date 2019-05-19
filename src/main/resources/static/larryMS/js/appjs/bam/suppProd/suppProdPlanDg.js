
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
	//平均分配的点击事件
	$("#averageBtn").click(function(){
		if(status=='已确认'){
			parent.layer.msg('已确定的订单请勿平均分配！');
			return;
		}
		averageProdPlan();
	});
	//确认
	$("#confirmBtn").click(function(){
		var rows=getData();
		var nextStatus='已排产';
		if(status=='已确认'){
			nextStatus='已确认';
		}
		var remainNum=$("#remainNum").val();
		//保存排产数据
		$.ajax({
			url:"/suppProd/saveSuppProdPlan",
			type:'POST',
			data:{planJson:JSON.stringify(rows),mainId:mainId,status:nextStatus,remainNum:remainNum},
			success:function(res){
				parent.layer.msg('操作成功',{time:1000});
				parent.loadTable();
			},
			error:function(){
				parent.layer.msg('操作失败',{time:1000});
			}
		});
		closeDg();
	});
	/**
	 * 获取日历中的数据
	 * @returns
	 */
	function getData(){
		//获取供应商该物料每天的排产计划
		var rows=[];
		$(".sent").each(function(index,row){
			var suppProdPlan={};
			var currDiv=$(this);
			var  calendarDate= currDiv.children("span.clickspan").html();
			var planNum=currDiv.children("input.project").val();
			suppProdPlan.planDate=report.yyyy+"-"+(report.mm+1)+'-'+calendarDate;
			suppProdPlan.planNum=planNum;
			suppProdPlan.mainId=mainId;
			rows.push(suppProdPlan);
		});
		return rows;
	}
	//取消按钮点击事件
	$("#cancleBtn").click(function(){
		closeDg();
	});
	
	$(".project").change(function(){
		var count=0;
		var prodPlan=$("#prodPlan").val();
		$(".project").each(function(index,row){
			var val=$(this).val();
			if(val ==null || val==''){
				val=0;
			}else if(isNaN(val)){
				val=0;
				$(this).val(0);
			}
			count+=parseFloat(val);
		});
		$("#remainNum").val(prodPlan-count);
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
	 * 平均分配的数据源
	 * @returns
	 */
	function averageProdPlan(){
		var planNum=$("#prodPlan").val();
		var remainder=planNum%dateNum;
		remainder=decimal(remainder,2);
		var datePlanNum=(planNum-remainder)/dateNum;
		var calenderDataSource=[];
		for(var i=1;i<=dateNum;i++){
			var suppProdPlan={};
			suppProdPlan.status='未确定';
			var dateStr=(report.yyyy+"-"+(report.mm+1)+'-'+i).replace(/-/g,"/");;
			var oDate1 = new Date(dateStr).getTime(); 
			suppProdPlan.planDate=oDate1;
			if(i==dateNum){
				suppProdPlan.planNum=datePlanNum+remainder;
			}else{
				suppProdPlan.planNum=datePlanNum;
			}
			calenderDataSource.push(suppProdPlan);
		}
		loadCalenderData(calenderDataSource);
		$("#remainNum").val(0);
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
					//var status=row.status;
//					if(status=='已确定'){
//						currDiv.children("input.project").attr('readonly','readonly');
//					}
					debugger;
					if(status=='已确认' && checkDate(date)){
						currDiv.children("input.project").attr('readonly','readonly');
					}
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
	/**
	 * 产看否个日期是否为当天之前的日期
	 * @param time
	 * @returns
	 */
	function checkDate(date){  
		var dateStr=(report.yyyy+"-"+(report.mm+1)+'-'+date).replace(/-/g,"/");;
	    var calDate=new Date(dateStr.replace(/-/g,"/"));
	    var nowStr=formatTime(new Date(),'yyyy-MM-dd');
	    var nowDate=new Date(nowStr.replace(/-/g,"/"));
	    if(calDate<nowDate){  
	        return true;  
	    }  
	    return false;  
	}  
})

