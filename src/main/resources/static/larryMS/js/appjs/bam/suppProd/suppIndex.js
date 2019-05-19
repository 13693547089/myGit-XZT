var prefix="/suppProd";
layui.use(['table','laydate','layer'], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate=layui.laydate;
	var condition = {};
	initSuppProdTable(condition);
	//日期控件格式化
	 laydate.render({
		elem : '#startDate'
	 });
	 laydate.render({
		 elem : '#planMonth',
		 type: 'month'
	 });
	//提交点击事件
	$("#submitBtn").click(function(){
		var checkData = table.checkStatus('suppProdTable').data;
		var ids=[];
		var msg="操作成功！";
		var flag=true;
		if(checkData.length<=0){
			 layer.msg("请选择需要确认的数据！",{time:1000});
			 return;
		}
		for(var i=0;i<checkData.length;i++){
			ids.push(checkData[i].id);
			if(checkData[i].status!='已排产'){
				msg="请选择已排产的数据进行提交！";
				flag=false;
			}
		}
		if(!flag){
			layer.msg(msg,{time:1000});
			return ;
		}
		var json=JSON.stringify(ids);
		$.post(prefix+"/changeSuppProdStatus",{status:'已提交',json:json},function(res){
			layer.msg("操作成功！",{time:1000});
			loadTable();
		});
	});

	$("#exportBtn").click(function(){
		
	});
	
	//批量排产
	$("#planAllBtn").click(function(){
		var checkData = table.checkStatus('suppProdTable').data;
		var ids=[];
		var msg="操作成功！";
		var flag=true;
		if(checkData.length<=0){
			 layer.msg("请选择需要排产的的数据！",{time:1000});
			 return;
		}
		for(var i=0;i<checkData.length;i++){
			ids.push(checkData[i].id);
			if(checkData[i].status!='未排产' && checkData[i].status!='已驳回' ){
				msg="请选择未排产或已驳回的数据进行排产！";
				flag=false;
			}
		}
		if(!flag){
			layer.msg(msg,{time:1000});
			return ;
		}
		$.ajax({
			url:prefix+"/avgAllSuppProdPlan",
			data:{jsonIds:JSON.stringify(ids)},
			success:function(res){
				loadTable();
				layer.msg('操作成功！',{time:1000});
			},
			error:function(){
				layer.msg('操作失败！',{time:1000});
			}
		});
		
		
		
	});
	//搜索的点击事件
	$("#searchBtn").click(function(){
		loadTable();
	});
	window.loadTable=function(){
		var planDesc=$("#planDesc").val();
		var status=$("#status").val();
		var startDate=$("#startDate").val();
		var endDate=$("#endDate").val();
		var planMonth=$("#planMonth").val();
		var series=$("#series").val();
		var mateDesc=$("#mateDesc").val();
		var suppName=$("#suppName").val();
		condition.planDesc=planDesc;
		condition.status=status;
		condition.startDate=startDate;
		condition.endDate=endDate;
		condition.planMonth=planMonth;
		condition.series=series;
		condition.mateDesc=mateDesc;
		condition.suppName=suppName;
		table.reload('suppProdTable',{url  : prefix+'/getSuppProdByPage',where: condition,});
//		debugger;
//		initSuppProdTable(condition);
	}
	//重置点击事件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	//表格监听事件
	 table.on('tool(suppProdTable)', function(obj) {
		var data = obj.data;
		var id = data.id;
		var planMonth=data.planMonth;
		var status=data.status;
		var url='getSuppProdPlanDg';
//		if(status=='已确认'){
//			url='getViewProdPlanDg';
//		}
		if (obj.event === 'plan') {
			layer.open({
				  type: 2,
				  shadeClose : false,
				  shade: 0.1,
				  title:'排产计划',
				  area: ['900px', '630px'],
				  maxmin: true,
				  content: prefix + "/"+url+"?id=" + id+"&planMonth="+planMonth+"&status="+status,
				  yes: function(index, layero){

				  }
			});
		} 
	});
	/**
	 * 初始化采购对账单表格
	 * 
	 * @param condition
	 * @returns
	 */
	function initSuppProdTable(condition){
		table.render({
			elem : '#suppProdTable',
			id:'suppProdTable',
			where: condition,
			url  : prefix+'/getSuppProdByPage',
			page : true,
			cols : [ [ {
				type : 'checkbox',
			},{
				type : 'numbers',
				title : '序号'
			},{
				field : 'status',
				width : '5%',
				title : '状态',
				templet:function(d){
					var status=d.status;
					if(status=='未排产'||status=='已驳回'){
						return '<span class="red">'+status+'</span>';
					}else{
						return '<span class="green">'+status+'</span>';
					}
				}
			}, {
				field : 'planMonth',
				title : '计划月份',
				width : '7%',
				templet:function(d){
					return formatTime(d.planMonth, 'yyyy-MM');
				}
			},  {
				field : 'ranking',
				title : '排名'
			},{
				field : 'mateCode',
				width : '12%',
				title : '物料编码',
			},  {
				field : 'mateDesc',
				width : '15%',
				title : '物料名称'
			},  {
				field : 'suppName',
				width : '15%',
				title : '供应商'
			}, {
				field : 'beginOrder',
				width : '7%',
				title : '期初订单'
			}, {
				field : 'beginStock',
				width : '7%',
				title : '期初库存'
			}, {
				field : 'beginEnableOrder',
				width : '10%',
				title : '期初可生产订单'
			}, {
				field : 'prodPlan',
				width : '7%',
				title : '生产计划'
			},{
				fixed : 'right',
				title : '操作',
				width : '7%',
				align : 'center',
				toolbar : '#operateBar'
			} ] ]
		});
	}
});