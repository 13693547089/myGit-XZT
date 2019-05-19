var prefix="/cutPlan";
layui.use(['table','laydate','layer'], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate=layui.laydate;
	var condition = {};
	initCutPlanTable(condition);
	//日期控件格式化
	 laydate.render({
		elem : '#cutMonth',
		type: 'month'
	 });
	// 新建按钮点击事件
	$("#addBtn").on('click', function() {
		var url = prefix + "/getCutPlanEditPage?type=1";
		//window.location.href = url;
		tuoGo(url,'cutPlanEdit',"cutPlanTable");
	});
	//提交点击事件
	$("#submitBtn").click(function(){
		var checkData = table.checkStatus('cutPlanTable').data;
		var ids=[];
		var msg="操作成功！";
		var flag=true;
		if(checkData.length<=0){
			 layer.msg("请选择需要提交的数据！",{time:1000});
			 return;
		}
		for(var i=0;i<checkData.length;i++){
			ids.push(checkData[i].planId);
			if(checkData[i].status =='已提交'){
				msg="已提交的数据请勿重复提交！";
				flag=false;
				break;
			}
		}
		if(!flag){
			layer.msg(msg,{time:1000});
			return ;
		}
		var json=JSON.stringify(ids);
		$.ajax({
			url:prefix+"/changeCutPlanStatus",
			data:{status:'已提交',jsonIds:json},
			success:function(res){
				$("#searchBtn").click();
				layer.msg("操作成功！",{time:1000});
			},
			error:function(){
				layer.msg("操作失败！",{time:1000});
			}
		});
	});
	//点击删除事件
	$("#delBtn").click(function(){
		var checkData = table.checkStatus('cutPlanTable').data;
		var ids=[];
		var flag=true;
		if(checkData.length<=0){
			 layer.msg("请选择需要删除的数据！",{time:1000});
			 return;
		}
		for(var i=0;i<checkData.length;i++){
			ids.push(checkData[i].planId);
			if(checkData[i].status =='已提交'){
				msg="已提交数据不能删除！";
				flag=false;
				break;
			}
		}
		if(!flag){
			layer.msg("已提交的计划不能删除！",{time:1000});
			return;
		}
		deleteCutPlan(ids);
	});
	//导出
	$("#exportBtn").click(function(){
		var checkData = table.checkStatus('cutPlanTable').data;
		if(checkData.length!=1){
			 layer.msg("请选择一条数据导出！",{time:1000});
			 return;
		}
		layer.confirm('确定要导出选中的打切计划吗？', function(index){
			var planId =checkData[0].planId;
			location=prefix+"/exportCutPlan?planId="+planId;
			layer.close(index);
	     });
	});
	//搜索的点击事件
	$("#searchBtn").click(function(){
		var cutMonth=$("#cutMonth").val();
		var cutPlanCode=$("#cutPlanCode").val();
		var status=$("#status").val();
		condition.cutMonth=cutMonth;
		condition.cutPlanCode=cutPlanCode;
		condition.status=status;
		initCutPlanTable(condition);
	});
	//重置点击事件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	
	//作废
	$("#cancellBtn").click(function(){
		var checkData = table.checkStatus('cutPlanTable').data;
		if(checkData.length<=0){
			 layer.msg("请选择需要作废的数据！",{time:1000});
			 return;
		}
		layer.confirm('确定要作废选中的打切计划吗？', function(index){
			var ids =[];
			for(var i=0;i<checkData.length;i++){
				ids.push(checkData[i].planId);
			}
			var json=JSON.stringify(ids);
			$.ajax({
				url:prefix+"/changeCutPlanStatus",
				data:{status:'已作废',jsonIds:json},
				success:function(res){
					$("#searchBtn").click();
					layer.msg("操作成功！",{time:1000});
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
			});
			
			 layer.close(index);
	     });
		
	});
	//表格监听事件
	 table.on('tool(cutPlanTable)', function(obj) {
		var data = obj.data;
		var planId = data.planId;
		var ids = [];
		ids.push(planId);
		if (obj.event === 'edit') {
			if(data.status=='已提交'){
				layer.msg("已提交的计划不能编辑！",{time:1000});
				return;
			}
			var url = prefix + "/getCutPlanEditPage?planId=" + planId + "&type=2";
			//window.location.href = url;
			tuoGo(url,'cutPlanEdit',"cutPlanTable");
		}else if(obj.event==='view'){
			var url = prefix + "/getCutPlanEditPage?planId=" + planId + "&type=3";
			//window.location.href = url;
			tuoGo(url,'cutPlanEdit',"cutPlanTable");
		} else if (obj.event === 'del') {
			if(data.status=='已提交'){
				layer.msg("已提交的计划不能删除！",{time:1000});
				return;
			}
			deleteCutPlan(ids);
		}
	});
	 /**
	  * 删除制定的行
	  * @returns
	  */
	 function deleteCutPlan(data) {
		layer.confirm('确认删除?', function(index) {
			var json = JSON.stringify(data);
			$.ajax({
				type : 'POST',
				url : prefix + "/delCutPlans",
				data : { jsonIds : json },
				success : function(msg) {
					$("#searchBtn").click();
					layer.msg("操作成功！", { time : 1000 });
				},
				error : function() {
					layer.msg("操作失败！", { time : 1000 });
				}
			});
			layer.close(index);
		});
	}
	/**
	 * 初始化采购对账单表格
	 * 
	 * @param condition
	 * @returns
	 */
	function initCutPlanTable(condition){
		table.render({
			elem : '#cutPlanTable',
			id:'cutPlanTable',
			where: condition,
			url  : prefix+'/getCutPlanByPage',
			page : true,
			cols : [ [ {
				type : 'checkbox',
			},{
				type : 'numbers',
				title : '序号'
			},{
				field : 'status',
				width : 143,
				title : '状态'
			},{
				field : 'cutMonth',
				width : 151,
				title : '打切月份',
			},  {
				field : 'cutPlanCode',
				width : 143,
				title : '打切计划编号'
			},  {
				field : 'planName',
				width : 311,
				title : '名称'
			}, {
				field : 'creater',
				width : 156,
				title : '创建人'
			}, {
				field : 'createDate',
				title : '创建时间',
				width : 145,
				templet : function(d) {
					return formatTime(d.createDate, 'yyyy-MM-dd');
				}
			}, {
				fixed : 'right',
				title : '操作',
				width : 159,
				align : 'center',
				toolbar : '#operateBar'
			} ] ]
		});
	}
	
});