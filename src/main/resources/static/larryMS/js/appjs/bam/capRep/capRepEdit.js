var prefix = "/capRep";
var mateData=[];
var orderData=[];
var stockData=[];
var type;
layui.use([ 'form', 'table', 'laydate', 'layer','upload'], function() {
	var $ = layui.jquery;
	var table = layui.table;
	var laydate = layui.laydate;
	var layer = layui.layer;
	var form = layui.form;
	var upload = layui.upload;
	type = $("#type").val();
	var mainId = $("#id").val();
	var suppNo=$("#suppNo").val();
	if (type == 3) {
		$("input").attr("readOnly","readOnly");
		$(".disableBtn").css('display','none');
	}
	if(type == 1){
		laydate.render({
			elem : '#repMonth',
			type : 'month'
		});
		getSuppMate();
		initOrderTable();
		initStockTable();
	}
	
	if(type!=1&&type!=2){
		disableFormItem();
	}	
	if(type!="1"){
		$.ajax({
			url : prefix + "/mate/list",
			method : "get",
			data : { mainId : mainId },
			success : function(res) {
				mateData=res;
				initMateTable();
			}
		});
		$.ajax({
			url : prefix + "/order/list",
			method : "get",
			data : { mainId : mainId },
			success : function(res) {
				orderData=res;
				initOrderTable();
			}
		});
		$.ajax({
			url : prefix + "/stock/list",
			method : "get",
			data : { mainId : mainId },
			success : function(res) {
				stockData=res;
				initStockTable();
			}
		});
	}
	// 保存事件
	$("#saveBtn").click(function() {
		//校验必填项
		var result =checkMust();
		if(result.flag==false){
			layer.msg(result.msg, {
				time : 1000
			});
			return false;
		}
		var flag=checkRepeat();
		if(!flag){
			layer.msg("该月已经创建了产能月报，请勿重复创建！",{time:1000});
			return false;
		}	
		var status=$('#status').val();
		if(status==null || status==''){
			$('#status').val('已保存');
		}
		saveCapRep("save");
	});
	// 提交
	$("#submitBtn").click(function() {
		var status=$("#status").val();
		if(status=="已提交"){
			layer.msg("该数据已为提交状态！",{time:1000});
			return;
		}
		//校验必填项
		var result =checkMust();
		if(result.flag==false){
			layer.msg(result.msg, {
				time : 1000
			});
			return false;
		}
		var flag=checkRepeat();
		if(!flag){
			layer.msg("改月已经创建了产能月报，请勿重复创建！",{time:1000});
			return false;
		}
		$('#status').val('已提交');
		saveCapRep("submit");
	});
	// 保存ajax
	function saveCapRep(type) {
		var orderForm=$('#capRep-form').serializeJSON();	
		orderForm.mateStr=JSON.stringify(mateData);
		orderForm.orderStr=JSON.stringify(orderData);
		orderForm.stockStr=JSON.stringify(stockData);
		$.post(prefix + "/save",orderForm,function(res){
			if(res.code==0){
				$("#type").val("2");
				$('#capCode').val(res.data.capCode);
				layer.msg("操作成功！", {
					time : 1000
				});
			}else{
				layer.msg(res.msg, {
					time : 1000
				});
			}
		});
	}
	$("#refuseBtn").click(function() {
		var status=$("#status").val();
		if(status!="已提交"){
			layer.msg("只有已提交状态的数据才能退回！",{time:1000});
			return;
		}
		var ids=[];
		ids.push(mainId);
		$.ajax({
			url : prefix + "/update/status",
			method : "get",
			data:{idStr:JSON.stringify(ids),status:'已退回'},
			success : function(res) {
				if(res.code==0){
					$("#status").val("已退回");
					layer.msg("操作成功！", {
						time : 1000
					});
				}else{
					layer.msg(res.msg, {
						time : 1000
					});
				}
			}
		});
	});
	// 回退
	$("#backBtn").click(function() {
		tuoBack("capRepEdit","#searchBtn");
	});						
	table.on('edit(mateTable)', function(obj){ //注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"

	});	 
	form.on('select(suppNo)', function(data){
		suppNo=$("#suppNo").val();
		 var suppName=$(data.elem).find("option:selected").text();
		 $("#suppName").val(suppName.substr(suppNo.length));
		 getSuppMate();
		 orderData=[];
		 stockData=[];
		 initOrderTable();
		 initStockTable();
	});
	function getSuppMate(){
		$.ajax({
			url : prefix + "/mate/supp/list",
			method : "get",
			data:{mainId:mainId,suppNo:suppNo},
			success : function(res) {
				mateData=res;
				initMateTable();
			}
		});
	}
	//添加库存信息
	$('#addOrderBtn').click(function(){
		layer.open({
			  type: 2,
			  shadeClose : false,
			  shade: 0.1,
			  title:'添加订单信息',
			  area: ['500px', '550px'],
			  maxmin: true,
			  content: prefix+'/page/order/add?mainId='+mainId,
			  yes: function(index, layero){

			  }
		});
	}); 
	//删除订单信息
	$('#delOrderBtn').click(function(){
		var checkOrderData = table.checkStatus('order-table').data;
		reomveArrById(orderData,checkOrderData);
		//删除附件信息
		var ids=[];
		$.each(checkOrderData,function(row,index){
			var orderAttach=row.orderAttach;
			if(orderAttach!=null && orderAttach!=null){
				var doc=JSON.parse(orderAttach);
				ids.push(doc.id);
			}
		});
		if(ids.length>0){
			$.post("doc/delFile",{docIds:JSON.stringify(ids)},function(res){			
			});
		}
		reloadOrderMateTable();
	}); 
	//添加库存信息
	$('#addStockBtn').click(function(){
		layer.open({
			  type: 2,
			  shadeClose : false,
			  shade: 0.1,
			  title:'添加库存信息',
			  area: ['500px', '350px'],
			  maxmin: true,
			  content: prefix+'/page/stock/add?mainId='+mainId,
			  yes: function(index, layero){

			  }
		});
	}); 
	//删除库存信息
	$('#delStockBtn').click(function(){
		var checkStockData = table.checkStatus('stock-table').data;
		reomveArrById(stockData,checkStockData);
		reloadStockTable();
	}); 
	// 物料信息
	window.initMateTable=function(){
		 table.render({
			elem : '#mate-table',
			id : 'mate-table',
			data : mateData,
			page : true,
			cols : [ [{
				 type : 'checkbox',
			  }, {
				 type : 'numbers',
				 title : '序号'
			  }, {
	        	  field : 'mateCode',
	        	  title : '物料编码',
	          }, {
	        	  field : 'mateDesc',
	        	  title : '物料名称',
	          }, {
	        	  field : 'unitName',
	        	  title : '单位',
	          }, {
	        	  field : 'orderNum',
	        	  title : '订单总量',
	          }, {
	        	  field : 'finishedNum',
	        	  title : '当前已完成'
	          }, {
	        	  field : 'unfinishedNum',
	        	  title : '未完成',
	          }, {
	        	  field : 'expectCompleteTime',
	        	  title : '预计完成时间',
	        	  templet : function(d) {
					return formatTime(d.expectCompleteTime, 'yyyy-MM-dd');
	        	  }
	          }, {
	        	  field : 'stockNum',
	        	  title : '库存数量'
	          } ] ]
		});
		if(type=='3'){
			$('td').removeAttr('data-edit');
		}
	}
	window.reloadOrderMateTable=function(){
		//更新订单信息
		table.reload('order-table', {
			data : orderData,
		});
		//更新物料信息 重新计算订单数量未完成数量
		calMate();
		table.reload('mate-table', {
			data : mateData,
		});
	}
	function calMate(){
		$.each(mateData,function(index,mate){
			var mateCode=mate.mateCode;
			var orderNum=0;
			var finishedNum=0;
			var expectCompleteTime=mate.expectCompleteTime;
			$.each(orderData,function(index1,order){
				var mateCode1=order.mateCode;
				var orderNum1=order.orderNum;
				var finishedNum1=order.finishedNum;
				var expectCompleteTime1=order.expectCompleteTime;
				if(mateCode==mateCode1){
					orderNum+=parseFloat(orderNum1);
					finishedNum+=parseFloat(finishedNum1);
					if(expectCompleteTime1>expectCompleteTime || expectCompleteTime==null || expectCompleteTime==''){
						expectCompleteTime=expectCompleteTime1;
						mate.expectCompleteTime=expectCompleteTime1;
					}
				}
			});
			mate.orderNum=orderNum;
			mate.finishedNum=finishedNum;
			mate.unfinishedNum=orderNum-finishedNum;			
		});
	}
	function calStock(){
		$.each(mateData,function(index,mate){
			var mateCode=mate.mateCode;
			var stockNum=0;
			$.each(stockData,function(index1,stock){
				var mateCode1=stock.mateCode;
				var stockNum1=stock.stockNum;
				if(mateCode==mateCode1){
					stockNum+=parseFloat(stockNum1);
				}
			});
			mate.stockNum=stockNum;		
		});
	}
	// 订单信息
	function initOrderTable(){
		table.render({
			elem : '#order-table',
			id : 'order-table',
			data : orderData,
			page : true,
			cols : [ [{
				 type : 'checkbox',
			  }, {
				 type : 'numbers',
				 title : '序号'
			  }, {
	        	  field : 'mateCode',
	        	  title : '物料编码',
	          }, {
	        	  field : 'mateDesc',
	        	  title : '物料名称',
	          }, {
	        	  field : 'version',
	        	  title : '版本',
	          }, {
	        	  field : 'batchNo',
	        	  title : '批次号',
	          }, {
	        	  field : 'orderNum',
	        	  title : '订单总量',
	          }, {
	        	  field : 'finishedNum',
	        	  title : '当前已完成'
	          },  {
	        	  field : 'orderNo',
	        	  title : '订单编号',
	          },{
	        	  field : 'orderSide',
	        	  title : '下单方',
	          }, {
	        	  field : 'expectCompleteTime',
	        	  title : '预计完成时间',
	        	  templet : function(d) {
					return formatTime(d.expectCompleteTime, 'yyyy-MM-dd');
		          }
	          }, {
	        	  field : 'orderAttach',
	        	  title : '订单附件',
	        	  templet : function(d) {
	        		  var orderAttach=d.orderAttach;	        	 
	        		  if(orderAttach==null || ""==orderAttach){
	        			  return "";
	        		  }
	        		  var attach=JSON.parse(orderAttach);
	        		  var  str='<span class="downLoadSpan" onclick="downLoadFile(\''+attach.id+'\')" >'+attach.realName+'</span>';;
					 return str;
			       }
	          } ] ]
		});
	}
	window.reloadStockTable=function(){
		table.reload('stock-table', {
			data : stockData,
		});
		//更新物料信息 重新计算订单数量未完成数量
		calStock();
		table.reload('mate-table', {
			data : mateData,
		});
	}

	// 初始化库存信息
	function initStockTable() {
		// 按物料汇总
		table.render({
			elem : '#stock-table',
			id : 'stock-table',
			data : stockData,
			page : true,
			cols : [ [{
				 type : 'checkbox',
			  }, {
				 type : 'numbers',
				 title : '序号'
			  }, {
	        	  field : 'mateCode',
	        	  title : '物料编码',
	          }, {
	        	  field : 'mateDesc',
	        	  title : '物料名称',
	          }, {
	        	  field : 'version',
	        	  title : '版本',
	          }, {
	        	  field : 'batchNo',
	        	  title : '批次号',
	          }, {
	        	  field : 'orderSide',
	        	  title : '下单方'
	          }, {
	        	  field : 'stockNum',
	        	  title : '数量'
	          } ] ]
		});
	}
	function checkRepeat(){
		var repMonth=$("#repMonth").val();
		var flag=true;
		$.ajax({
			url : prefix + "/check/repeat",
			async:false,
			method : "get",
			data:{id:mainId,suppNo:suppNo,repMonth:repMonth},
			success : function(res) {
				flag=!res;
			}
		});	
		return flag;
	}
});
function downLoadFile(docId){
	window.location.href = "/doc/downLoadDoc?docId=" + docId
}