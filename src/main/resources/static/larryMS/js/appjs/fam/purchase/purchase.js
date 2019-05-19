var prefix="/purchRecon";
layui.use(['table','laydate','layer'], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate=layui.laydate;
	var condition = {};
	var reconCode2='';
	initPurchReconTable(condition);
	//日期控件格式化
	 laydate.render({
		elem : '#startDate'
	 });
	 laydate.render({
		 elem : '#endDate'
	 });
	// 新建按钮点击事件
	$("#addBtn").on('click', function() {
//		window.location.href = "/purchRecon/editPurchaseRecn?type=1"
		var url="/purchRecon/editPurchaseRecn?type=1";
		tuoGo(url,"purchaseEdit","reconTable");
	});
	//提交点击事件
	$("#submitBtn").click(function(){
		var checkData = table.checkStatus('reconTable').data;
		var length = checkData.length;
		var reconCodeJson=[];
		var msg="操作成功！";
		var flag=true;
		/*if(checkData.length<=0){
			 layer.msg("请选择需要提交的数据！",{time:1000});
			 return;
		}*/
		if(checkData.length!=1){
			layer.msg("请选择一条数据进行提交！",{time:1000});
			return;
		}
		for(var i=0;i<checkData.length;i++){
			reconCodeJson.push(checkData[i].reconCode);
			if(checkData[i].reconStatus!='YBC'){
				msg="请提交“已保存”状态的数据！";
				flag=false;
			}
		}
		if(!flag){
			layer.msg(msg,{time:1000});
			return ;
		}
		for(var i =0;i<length;i++){
			reconCodeJson=[];
			var reconCode = checkData[i].reconCode;
			var id = checkData[i].id;
			reconCode2 =reconCode;
			reconCodeJson.push(reconCode);
			var json=JSON.stringify(reconCodeJson);
			var suppName = checkData[i].suppName;
			var createDate = checkData[i].createDate;
			//var format  = new Date(createDate).Format("yyyy-MM-dd");
			var remark = "采购对账审核: " + suppName ;
			var flag = taskProcess(id,"purcReconAudit", remark,"process");
			/*if (flag == "process_success") {
				$.post(prefix+"/changeReconStatus",{status:'YTJ',reconCodeJson:json},function(res){
					layer.msg("操作成功！",{time:1000});
					//$("#searchBtn").click();
				});
			}*/
		}
	});
	//在弹窗中选择执行人后，点击确认按钮回调
	   window.returnFunction = function() {
			debugger
			var reconCodes = [];
			reconCodes.push(reconCode2);
			var json = JSON.stringify(reconCodes);
			$.post(prefix+"/changeReconStatus",{status:'YTJ',reconCodeJson:json},function(res){
				layer.msg("操作成功！",{time:1000});
				$("#searchBtn").click();
			});
		}
	//点击删除事件
	$("#delBtn").click(function(){
		var checkData = table.checkStatus('reconTable').data;
		var reconCodeJson=[];
		var flag=true;
		for(var i=0;i<checkData.length;i++){
			reconCodeJson.push(checkData[i].reconCode);
			if(checkData[i].reconStatus=='YQR'){
				flag=false;
			}
		}
		if(!flag){
			layer.msg('已确认的单据不能删除！',{time:1000});
			return false;
		}
		deleteRecon(reconCodeJson);
	});
	$("#exportBtn").click(function(){
		
	});
	//搜索的点击事件
	$("#searchBtn").click(function(){
		var suppName=$("#suppName").val();
		var reconCode=$("#reconCode").val();
		var startDate=$("#startDate").val();
		var endDate=$("#endDate").val();
		var status=$("#status").val();
		condition.suppName=suppName;
		condition.reconCode=reconCode;
		condition.startDate=startDate;
		condition.endDate=endDate;
		condition.status=status;
		initPurchReconTable(condition);
	});
	//重置点击事件
	$("#resetBtn").click(function(){
		$("#searchForm")[0].reset();
	});
	//表格监听事件
	 table.on('tool(reconTable)', function(obj) {
		var data = obj.data;
		var reconCode = data.reconCode;
		var reconCodes = [];
		reconCodes.push(reconCode);
		if (obj.event === 'edit') {
			if(data.reconStatus!='YBC' && data.reconStatus!='DQR'){
				layer.msg('只能编辑已保存,待确认的单据！',{time:1000});
				return false;
			}
			var url = prefix + "/editPurchaseRecn?reconCode=" + reconCode + "&type=2";
//			window.location.href = url;
			tuoGo(url,"purchaseEdit","reconTable");

		}else if(obj.event==='view'){
			var url = prefix + "/editPurchaseRecn?reconCode=" + reconCode + "&type=3";
//			window.location.href = url;
			tuoGo(url,"purchaseEdit","reconTable");

		} else if (obj.event === 'del') {
			if(data.reconStatus=='YQR'){
				layer.msg('已确认的单据不能删除！',{time:1000});
				return false;
			}
			deleteRecon(reconCodes);
		}
	});
	 /**
	  * 删除制定的行
	  * @returns
	  */
	 function deleteRecon(data) {
		if(data.length<=0){
			 layer.msg("请选择需要删除的数据！",{time:1000});
			 return;
		}
		layer.confirm('确认删除?', function(index) {
			var json = JSON.stringify(data);
			$.ajax({
				type : 'POST',
				url : prefix + "/delRecon",
				data : { codeJson : json },
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
	function initPurchReconTable(condition){
		table.render({
			elem : '#reconTable',
			id:'reconTable',
			where: condition,
			url  : prefix+'/getFinancePurchReconByPage',
			page : true,
			cols : [ [ {
				checkbox : true,
				fixed : 'center'
			}, {
				field : 'reconStatusDesc',
				width:'5%',
				title : '状态'
			}, {
				field : 'reconCode',
				width:'10%',
				title : '对账单号'
			}, {
				field : 'startDate',
				title : '对账日期从',
				width:'10%',
				templet : function(d) {
					return formatTime(d.startDate, 'yyyy-MM-dd');
				}
			}, {
				field : 'endDate',
				title : '对账日期至',
				width:'8%',
				templet : function(d) {
					return formatTime(d.endDate, 'yyyy-MM-dd');
				}
			}, {
				field : 'suppName',
				width:'20%',
				title : '供应商名称'
			}, {
				field : 'suppNo',
				width:'10%',
				title : '供应商编码'
			}, {
				field : 'creater',
				width:'9%',
				title : '创建人'
			}, {
				field : 'createTime',
				title : '创建时间',
				width:'9%',
				templet : function(d) {
					return formatTime(d.createTime, 'yyyy-MM-dd');
				}
			}, {
				fixed : 'right',
				title : '操作',
				width:'15%',
				align : 'center',
				toolbar : '#operateBar'
			} ] ]
		});
	}
});