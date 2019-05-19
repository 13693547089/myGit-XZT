//地址前缀
var prefix = "/bam/tran";
var id='';
var type="1";

var tableData;
var table;

$(function(){
	var status = $('#status').val();
	if(status == '已提交'){
		// 已提交状态下不允许保存与提交，隐藏
		//$('#submitBtn').css("display", "none");
		//$('#saveBtn').css("display", "none");
		$(".btnShow").css("display", "none");
		$('#returnBtn').css("display", "inline");
	}else{
		// 显示
		$('#submitBtn').css("display", "inline");
		$('#saveBtn').css("display", "inline");
		$('#returnBtn').css("display", "none");
	}
	
	// 编辑状态  1：编辑  2：查看 3：引用创建
	var editType=$('#type').val();
	if(editType == '1'||editType == '3'){
		
	}else{
		// 查看状态下不允许编辑
		$("input[type=text]").attr("disabled", true);
		//$("input[type=radio]").attr("disabled", true);
		$("input[type=select]").attr("disabled", true);
		//$("input[type=checkbox]").attr("disabled", true);
		$("input[type=button]").attr("disabled", true);
		//$("textarea").attr("disabled", true);
		
		$(".btnShow").css("display", "none");
	}
	
	// 返回
	$("#backBtn").click(function(){
		 //window.history.go(-1);
		 tuoBack('.tranPlanDetail','#searchBtn');
	});
	
	// 保存
	$("#saveBtn").click(function(){
		saveFn();
	});
	// 提交
	$("#submitBtn").click(function(){
		submitFn();
	});
	// 退回
	$("#returnBtn").click(function(){
		returnDeal();
	});
	
	// 更新物料信息
	$('#updateMatBtn').click(function(){
		var code = $('#tranCode').val();
		if(code == undefined || code == ''){
			layer.msg("请在编辑状态下更新！");
			return;
		}
		
		// mainId
		var mainId = $('#id').val();
		// 获取计划年月
		var ym = $('#tranDate').val();
		if(ym == undefined || ym == null || ym == ''){
			layer.msg("请选择调拨日期！");
			return;
		}
		ym = ym.split('~');
    	var date1 = ym[0].replace(/\s+/g,"");// 去除所有空格
    	var arrDate = date1.split("-");
    	var year = arrDate[0];
    	var month = arrDate[1];
    	var planYm = year+"-"+month;

    	var index = layer.load(0, {shade: false});
    	$.ajax({
			 type:"post",
			 url:prefix+"/updateTranPlanDetailFromPadPlan",
			 dataType:"JSON",
			 data:{"mainId":mainId,"planYm":planYm},
			 success:function(data){
				if(data.code == 0){
					layer.msg("物料更新成功！");
					// 重新加载物料数据
					loadMatData();
				}else{
					layer.msg("物料更新失败："+data.msg);
				}
				// 关闭加载层
				layer.close(index);
			 },
			 error: function (XMLHttpRequest, textStatus, errorThrown) {
	 	    	// 关闭加载层
	 	    	layer.close(index);
	         }
		 });
	});
	
	// 添加物料信息
	$('#addMatBtn').click(function(){
		// 获取年月
		var ym = $('#tranDate').val();
		if(ym == undefined || ym == null || ym == ''){
			layer.msg("请选择调拨日期！");
			return;
		}

		ym = ym.split('~');
    	
    	var date1 = ym[0].replace(/\s+/g,"");// 去除所有空格
    	//var date2 = ym[1].replace(/\s+/g,"");// 去除所有空格
    	var arrDate = date1.split("-");
    	var year = arrDate[0];
    	var month = arrDate[1];
    	// 计划年月
    	var planYm = year+"-"+month;
		
    	// 主表ID
    	var mainId = $('#id').val();
    	
    	/*// 启动加载层
    	var index = layer.load(0, {shade: false});
		// 获取明细数据
		$.ajax({
			 type:"post",
			 url:prefix+"/getTranPlanDetailFromPadPlan",
			 dataType:"JSON",
			 data:{"planYm":planYm,"year":year,"month":month,"mainId":mainId},
			 success:function(data){
				 tableData=data;
				 loadDetailTable(table,tableData);
				 
				 // 关闭加载层
				 layer.close(index);
			 }
		 });*/
		
		// 原来的方式，添加物料信息
		layer.open({
			  type:2,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
			  title:"物料列表",
			  shadeClose : false,
			  shade : 0.1,
			  content : prefix+'/matSelectDialog',
			  area : [ '800px', '90%' ],
			  maxmin : true, // 开启最大化最小化按钮
			  btn: ['确认', '取消']
			  ,success: function(layero, index){
				  // 传递参数至弹窗
				  var doc = layero.find("iframe")[0].contentWindow.document;
				  var mainIdDOC = $("#mainId",doc);
				  mainIdDOC.attr("value",mainId);
				  
				  var planYmDOC = $("#planYm",doc);
				  planYmDOC.attr("value",planYm);
			  }
		  	  ,yes: function(index, layero){
		  		//按钮【按钮一】的回调
		  		  
		  		// 获取选中的物料数据
		  		var data = $(layero).find("iframe")[0].contentWindow
		        .getCheckedData();
		  		// 关闭弹框
		  		layer.close(index);
		  		// 处理数据
		  		dialogSelDataDeal(data);
			  }
			  ,btn2: function(index, layero){
				  //按钮【按钮二】的回调
				  // 默认会关闭弹框
				  //return false 开启该代码可禁止点击该按钮关闭
			  }
		  });
	});
	
	// 删除物料信息
	$('#delMatBtn').click(function(){
	   	var checkStatus = table.checkStatus('mat-table');
	   	var checkedData = checkStatus.data;
	   	if (checkedData.length > 0) {
	   		for (var i = checkedData.length-1; i >=0 ; i--) {
	   			// 删除数组元素
	   			tableData.splice(checkedData[i].sn-1,1);
	   		}
	   		
	   		// 数据集重新设置sn
	   		for(var i = 0; i < tableData.length; i++){
	   			tableData[i].sn = i+1;
	   		}
	   		
   			// 重新加载数据
   			loadDetailTable(table, tableData);
	   	} else {
	   		layer.msg("请选择要删除的物料信息！");
	   	}
	});
});

// 定义日期格式
function laydateInit(laydate){
	  var laydate = layui.laydate;
	  // 调拨日期 区间
	  laydate.render({
	    elem: '#tranDate'
	    ,range: '~'
	    ,done: function(value, date, endDate){
	        //value 得到日期生成的值，如：2017-08-18
	        //date 得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
	        //endDate 得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员同上。
	    	
	    	var ym = value.split('~');
	    	
	    	var date1 = ym[0].replace(/\s+/g,"");// 去除所有空格
	    	var date2 = ym[1].replace(/\s+/g,"");// 去除所有空格
	    	
	    	var dt1 = new Date(date1);
	    	var dt2 = new Date(date2);
	    	
	    	// 回的是0-6的数字，0 表示星期天
	    	//var xq = dt1.getDay();
	    	
	    	var time = dt2.getTime() - dt1.getTime();
	    	var days = parseInt(time / (1000 * 60 * 60 * 24));
	    	
	    	if(days == 6){ // && xq == 1
	    		// 设置日期列头名称
	    		setColumnName(date1);
	    		// 重新加载数据
	    		//loadDetailTable(table,tableData);
	    		var editType=$('#type').val();
	    		if(editType == '3'){
	    			// 引用创建情况下，物料的处理
	    			refDeal(date1);
	    		}
	    	}else{
	    		//$('#tranDate').val('');
	    		layer.msg("请选择七天的范围！",{time: 2000});
	    		return;
	    	}
	     }
	  });
}

// 加载物料数据
function loadMatData(){
	// 获取明细数据
	$.ajax({
		 type:"post",
		 url:prefix+"/getTranPlanDetailList?mainId="+id,
		 dataType:"JSON",
		 success:function(data){
			 tableData=data;
			 loadDetailTable(table,tableData);
		 }
	 });
}

//加载引用调拨的物料数据
function loadMatDataRef(){
	var refId = $('#refId').val();
	// 获取明细数据
	$.ajax({
		 type:"post",
		 url:prefix+"/getTranPlanDetailListRef?mainId="+id+"&refId="+refId,
		 dataType:"JSON",
		 success:function(data){
			 tableData=data;
			 loadDetailTable(table,tableData);
		 }
	 });
}

// 日期
var rq1;
var rq2;
var rq3;
var rq4;
var rq5;
var rq6;
var rq7;
// 物料明细table
layui.use(['table','laydate'], function() {
	var $ = layui.$;
	table = layui.table;
	var laydate = layui.laydate;
	// 定义日期控件
	laydateInit(laydate);
	
	type=$('#type').val();
	id=$("#id").val();
	
	//调拨日期
	var ym = $("#tranDate").val();
	if(ym == null || ym == ''){
		// 设置日期列头名称
		var date1 = new Date().Format("yyyy-MM-dd");
		setColumnName(date1);
	}else{
		var ymArr = ym.split('~');
		var date1 = ymArr[0].replace(/\s+/g,"");// 去除所有空格
		// 设置日期列头名称
		setColumnName(date1);
	}
	
	var editType=$('#type').val();
	if(editType == '3'){
		// 引用调用情况下
		loadMatDataRef();
	}else{
		// 获取明细数据
		loadMatData();
	}
	
	// table 数据刷新处理
	table.on('tool(dealChangeEvent)', function(obj){
	    var data = obj.data;
	    if(obj.event == 'setSign'){
			obj.update({unDevQty:data.unDevQty,devScale:data.devScale,sumQty:data.sumQty});
	    }
	});
	
	// table 单元格编辑监听
	table.on('edit(dealChangeEvent)', function(obj){
		if(obj.field == 'estDevQty'){
				
			var item = obj.data;
			var cell4 = (item.planDevQty==null||isNaN(item.planDevQty)||item.planDevQty=='')?0:parseFloat(item.planDevQty);
			var cell5 = (item.actDevQty==null||isNaN(item.actDevQty)||item.actDevQty=='')?0:parseFloat(item.actDevQty);
			var cell6 = (item.estDevQty==null||isNaN(item.estDevQty)||item.estDevQty=='')?0:parseFloat(item.estDevQty);
			
			item.unDevQty = parseFloat((cell4-cell5-cell6).toFixed(2));
			if(cell4 == 0){
				item.devScale = '0%';
			}else{
				item.devScale = (100-(item.unDevQty/cell4)*100).toFixed(2)+"%";
			}
			
			$(this).click();
		}
		
		if(obj.field == 'monQty'||obj.field == 'tueQty'||obj.field == 'wedQty'||obj.field == 'thuQty'
			||obj.field == 'friQty'||obj.field == 'satQty'||obj.field == 'sunQty'){
			
			var item = obj.data;
			var cell9 = (item.monQty==null||isNaN(item.monQty)||item.monQty=='')?0:parseFloat(item.monQty);
			var cell10 = (item.tueQty==null||isNaN(item.tueQty)||item.tueQty=='')?0:parseFloat(item.tueQty);
			var cell11 = (item.wedQty==null||isNaN(item.wedQty)||item.wedQty=='')?0:parseFloat(item.wedQty);
			var cell12 = (item.thuQty==null||isNaN(item.thuQty)||item.thuQty=='')?0:parseFloat(item.thuQty);
			var cell13 = (item.friQty==null||isNaN(item.friQty)||item.friQty=='')?0:parseFloat(item.friQty);
			var cell14 = (item.satQty==null||isNaN(item.satQty)||item.satQty=='')?0:parseFloat(item.satQty);
			var cell15 = (item.sunQty==null||isNaN(item.sunQty)||item.sunQty=='')?0:parseFloat(item.sunQty);
			
			item.sumQty = parseFloat((cell9+cell10+cell11+cell12+cell13+cell14+cell15).toFixed(2));
			
			$(this).click();
		}
	});
});

// 数据加载
function loadDetailTable(table,tableData) {
	table.render({
	     elem: '#mat-table'
	    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
	    ,data:tableData
	    ,cols: [[
	           {checkbox:true}
			  //,{field:'sn', title: '序号',width:'70'}
			  ,{field:'matCode', title: '物料编号',width:'150'}
		      ,{field:'matName', title: '物料名称',width:'200'}
		      ,{field:'prodSeries', title: '系列',width:'100'}
		      ,{field:'rank', title: '排名',width:'70'}
		      ,{field:'saleForeQty', title: '本月销售预测',width:'150'}
		      ,{field:'saleQty', title: '本月已销售',width:'120'}
		      ,{field:'saleScale', title: '销售达成率',width:'100'}
		      ,{field:'planDevQty', title: '交货计划',width:'120'}
		      ,{field:'actDevQty', title: '已交量',width:'120'}
		      ,{field:'estDevQty', title: '预计交货',width:'120',edit: 'text',event: 'setSign'}
		      ,{field:'devScale', title: '交货达成率',width:'120'}
		      ,{field:'unDevQty', title: '未交量',width:'100'}
		      ,{field:'monQty', title: rq1,width:'110',edit: 'text',event: 'setSign'}
		      ,{field:'tueQty', title: rq2,width:'110',edit: 'text',event: 'setSign'}
		      ,{field:'wedQty', title: rq3,width:'110',edit: 'text',event: 'setSign'}
		      ,{field:'thuQty', title: rq4,width:'110',edit: 'text',event: 'setSign'}
		      ,{field:'friQty', title: rq5,width:'110',edit: 'text',event: 'setSign'}
		      ,{field:'satQty', title: rq6,width:'110',edit: 'text',event: 'setSign'}
		      ,{field:'sunQty', title: rq7,width:'110',edit: 'text',event: 'setSign'}
		      ,{field:'sumQty', title: '小计',width:'110'}
	    ]]
	  ,page:true
	  ,done: function(res, curr, count){
		    //如果是异步请求数据方式，res即为你接口返回的信息。
		    //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
		    //console.log(res);
		    //得到当前页码
		    //console.log(curr);
		    //得到数据总量
		    //console.log(count);
		  var data = res.data;
		  var cell1 = 0;
		  var cell2 = 0;
		  var cell3 = ''; // 销售达成率
		  var cell4 = 0;
		  var cell5 = 0;
		  var cell6 = 0;
		  var cell7 = ''; // 交货达成率
		  var cell8 = 0;
		  var cell9 = 0;
		  var cell10 = 0;
		  var cell11 = 0;
		  var cell12 = 0;
		  var cell13 = 0;
		  var cell14 = 0;
		  var cell15 = 0;
		  var cell16 = 0;
		  if(data.length>0){
			  for(var i=0;i<data.length;i++){
				  var item = data[i];
				  
				  cell1 += (item.saleForeQty==null||isNaN(item.saleForeQty)||item.saleForeQty=='')?0:parseFloat(item.saleForeQty);
				  cell2 += (item.saleQty==null||isNaN(item.saleQty)||item.saleQty=='')?0:parseFloat(item.saleQty);
				 // cell3 += (item.padPlanQty==null||isNaN(item.padPlanQty)||item.padPlanQty=='')?0:parseFloat(item.padPlanQty);
				  cell4 += (item.planDevQty==null||isNaN(item.planDevQty)||item.planDevQty=='')?0:parseFloat(item.planDevQty);
				  cell5 += (item.actDevQty==null||isNaN(item.actDevQty)||item.actDevQty=='')?0:parseFloat(item.actDevQty);
				  cell6 += (item.estDevQty==null||isNaN(item.estDevQty)||item.estDevQty=='')?0:parseFloat(item.estDevQty);
				  //cell7 += (item.estSaleQty==null||isNaN(item.estSaleQty)||item.estSaleQty=='')?0:parseFloat(item.estSaleQty);
				  cell8 += (item.unDevQty==null||isNaN(item.unDevQty)||item.unDevQty=='')?0:parseFloat(item.unDevQty);
				  cell9 += (item.monQty==null||isNaN(item.monQty)||item.monQty=='')?0:parseFloat(item.monQty);
				  cell10 += (item.tueQty==null||item.tueQty==null||isNaN(item.tueQty)||item.tueQty=='')?0:parseFloat(item.tueQty);
				  cell11 += (item.wedQty==null||isNaN(item.wedQty)||item.wedQty=='')?0:parseFloat(item.wedQty);
				  cell12 += (item.thuQty==null||isNaN(item.thuQty)||item.thuQty=='')?0:parseFloat(item.thuQty);
				  cell13 += (item.friQty==null||isNaN(item.friQty)||item.friQty=='')?0:parseFloat(item.friQty);
				  cell14 += (item.satQty==null||isNaN(item.satQty)||item.satQty=='')?0:parseFloat(item.satQty);
				  cell15 += (item.sunQty==null||isNaN(item.sunQty)||item.sunQty=='')?0:parseFloat(item.sunQty);
				  
				  // 小计计算
				  //item.sumQty = parseFloat((cell9+cell10+cell11+cell12+cell13+cell14+cell15).toFixed(2));
				  
				  cell16 += item.sumQty;
			  }
			  
			  // 计算销售达成率
			  if(cell1==0){
				  cell3 = '0%';
			  }else{
				  cell3 = ((cell2/cell1)*100).toFixed(2)+"%";
			  }
			  
			  // 计算交货达成率
			  if(cell4==0){
				  cell7 = '0%';
			  }else{
				  cell7 = (100-(cell8/cell4)*100).toFixed(2)+"%";
			  }
			  
			  var sumRow = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
				  '<td></td>'+
				  '<td></td>'+
				  '<td></td>'+
				  '<td>总计</td></tr>';
			  
			  var sumDetailRow = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
			  '<td></td>'+
			  '<td></td>'+
			  '<td></td>'+
			  '<td>总计</td>'+
			  '<td>'+cell1.toFixed(2)+'</td>'+
			  '<td>'+cell2.toFixed(2)+'</td>'+
			  '<td>'+cell3+'</td>'+
			  '<td>'+cell4.toFixed(2)+'</td>'+
			  '<td>'+cell5.toFixed(2)+'</td>'+
			  '<td>'+cell6.toFixed(2)+'</td>'+
			  '<td>'+cell7+'</td>'+
			  '<td>'+cell8.toFixed(2)+'</td>'+
			  '<td>'+cell9.toFixed(2)+'</td>'+
			  '<td>'+cell10+'</td>'+
			  '<td>'+cell11.toFixed(2)+'</td>'+
			  '<td>'+cell12.toFixed(2)+'</td>'+
			  '<td>'+cell13.toFixed(2)+'</td>'+
			  '<td>'+cell14.toFixed(2)+'</td>'+
			  '<td>'+cell15.toFixed(2)+'</td>'+
			  '<td>'+cell16.toFixed(2)+'</td>'+
			  '</tr>';
			  
			  $('.layui-table-fixed .layui-table tbody').append(sumRow);
			  
			  $('.layui-table-main .layui-table tbody').append(sumDetailRow);
		  }
	  }
	});

	if(type == '2'){
		 $('table td').removeAttr('data-edit');
	}
}

/**
 * 设置列头名称
 * @returns
 */
function setColumnName(startDate){
	// commn.js c_addDate
	rq1= startDate;
	rq2= c_addDate(startDate,1);
	rq3= c_addDate(startDate,2);
	rq4= c_addDate(startDate,3);
	rq5= c_addDate(startDate,4);
	rq6= c_addDate(startDate,5);
	rq7= c_addDate(startDate,6);
}

function saveFn(){
	// 判断是否选择日期区间
	var ym = $('#tranDate').val();
	if(ym == undefined || ym == null || ym == ''){
		layer.msg("请选择调拨日期！");
		return;
	}

	// 判断明细与主表id是否一致
	var editType=$('#type').val();
	if(editType == '3'){
		// 引用创建情况下
		if(tableData.length>0){
			var mainId = tableData[0].mainId;
			if(id != mainId){
				layer.msg("物料数据未获取成功，请重新选择调拨日期！");
				return;
			}
		}
	}
	
	// 状态处理
	var status = $('#status').val();
	if(status == '' || status == undefined){
		// 创建情况下
		$('#status').val('已保存');
	}

	// 启动加载层
	var loadLayer = layer.load(0, {shade: false});
	// 数组转字符串存储
	$('#tranPlanDetailData').val(JSON.stringify(tableData));
	var options = {
			url: prefix+"/saveTranPlanInfo?sType=sv",
			type:'POST',
			success: function (msg) {
				// 关闭加载层
				layer.close(loadLayer);
				if(msg.code=="0"){
					layer.msg("保存成功！",{time: 1000});
					// 重新加载物料数据
					loadMatData();
				}else{
					$('#status').val('');
					layer.msg("保存失败！",{time: 1000});
				}
			},
			error: function(request) {
				// 关闭加载层
				layer.close(loadLayer);
				$('#status').val('');
				layer.msg("保存异常！",{time: 1000});
			}
	};
	$("#submitForm").ajaxSubmit(options);
}

// 提交
function submitFn(){
	/*// 主表ID
	var id = $('#id').val();
	// 获取明细数据
	$.ajax({
		 type:"post",
		 url:prefix+"/submitTranPlan?mainId="+id,
		 dataType:"JSON",
		 success:function(data){
			 if(data.code=="0"){
				layer.msg("提交成功！",{time: 1000});
				// 返回列表页
				window.history.go(-1);
			 }else{
				layer.msg("提交失败！",{time: 1000});
			 }
		 }
	 }); */
	// 判断是否选择日期区间
	var ym = $('#tranDate').val();
	if(ym == undefined || ym == null || ym == ''){
		layer.msg("请选择调拨日期！");
		return;
	}
	
	// 判断明细与主表id是否一致
	var editType=$('#type').val();
	if(editType == '3'){
		// 引用创建情况下
		if(tableData.length>0){
			var mainId = tableData[0].mainId;
			if(id != mainId){
				layer.msg("物料数据未获取成功，请重新选择调拨日期！");
				return;
			}
		}
	}
	
	// 状态处理
	var status = $('#status').val();
	if(status == '' || status == undefined){
		status='';
		// 创建情况下
		$('#status').val('已保存');
	}
	
	// 数组转字符串存储
	$('#tranPlanDetailData').val(JSON.stringify(tableData));
	var options = {
			url: prefix+"/saveTranPlanInfo?sType=sb",
			type:'POST',
			success: function (msg) {
				if(msg.code=="0"){
					layer.msg("提交成功！",{time: 1000});
					// 返回列表页
					//window.history.go(-1);
					tuoBack('.tranPlanDetail','#searchBtn');
				}else{
					$('#status').val(status);
					layer.msg("提交失败！",{time: 1000});
				}
			},
			error: function(request) {
				$('#status').val(status);
				layer.msg("提交异常！",{time: 1000});
			}
	};
	$("#submitForm").ajaxSubmit(options);
}

/**
 * 物料弹出框选择数据处理
 * @param data
 * @returns
 */
function dialogSelDataDeal(data){
	var dataLenth = tableData.length;
	var lastNum = 0;
	if(dataLenth > 0){
		lastNum = tableData[dataLenth-1].sn;
	}
	
	if(data.length>0){
		// 循环
		for(var i=0;i<data.length;i++){
			var count=0;
			for(var j=0;j<tableData.length;j++){
				if(data[i].matCode == tableData[j].matCode){
					count++;
					break;
				}
			}
			
			if(count == 0){
				lastNum = lastNum+1;
				//创建table的行，赋值，添加到table中
				var arr = {
					mainId:data[i].mainId,
					sn : lastNum,
					matCode : data[i].matCode,
					matName:data[i].matName,
					prodSeries:data[i].prodSeries,
					rank:data[i].rank,
					saleForeQty:data[i].saleForeQty,
					saleQty:data[i].saleQty==null?0:data[i].saleQty,
					saleScale:data[i].saleScale,
					planDevQty:data[i].planDevQty,
					actDevQty:data[i].actDevQty,
					estDevQty:0,
					devScale:data[i].devScale,
					unDevQty:data[i].unDevQty,
					monQty:data[i].monQty,
					tueQty:data[i].tueQty,
					wedQty:data[i].wedQty,
					thuQty:data[i].thuQty,
					friQty:data[i].friQty,
					satQty:data[i].satQty,
					sunQty:data[i].sunQty,
					sumQty:0
				};
				tableData.push(arr);
			}
		}
		// 加载数据
		loadDetailTable(table, tableData);
	 }else{
		layer.msg("操作失败！",{time: 1000});
	 }
}

/**
 * 引用创建选择日期后的数据处理
 * @param data
 * @returns
 */
function refDeal(date1){
	
	if(tableData.length ==0){
		return;
	}
	
	var ymArr = date1.split('-');
	// 年份
	var year = ymArr[0];
	// 月份
	var month = ymArr[1];
	// 年月
	var ymValue = year+"-"+month;
	// 主表id
	var mainId = $('#id').val();
	var matData = [];
	for(var i=0;i<tableData.length;i++){
		var item  = tableData[i];
		var obj = {
				"mateCode":item.matCode,
				"mateName":item.matName,
				"seriesExpl":item.prodSeries
				};
		
		matData.push(obj);
	}
	// 对json字符串数据进行编码  // unescape 解码方法 ，escape 编码方法
	var matInfoData =JSON.stringify(matData);// escape();
	
	// 启动加载层
	var loadLayer = layer.load(0, {shade: false});
	// 获取物料的其他信息
	$.ajax({
		 type:"post",
		 url:prefix+"/getMatExtraInfo",
		 dataType:"JSON",
		 data:{"matInfoData":matInfoData,"year":year,"month":month,"mainId":mainId,"ym":ymValue},
		 success:function(data){
			 // 关闭加载层
			 layer.close(loadLayer);
			 if(data.length>0){
				 // 清空
				 tableData = [];
				 // 处理数据
				 dialogSelDataDeal(data);
			 }else{
				layer.msg("操作失败！",{time: 1000});
			 }
		 },
		 error: function (XMLHttpRequest, textStatus, errorThrown) {
			 // 关闭加载层
			 layer.close(loadLayer);
	     }
	 });
}

/**
 * 退回处理
 * @returns
 */
function returnDeal(){
	var mainId = $('#id').val();
	
	layer.confirm('是否退回该调拨计划？', {
	  btn: ['确定','取消'] //按钮
	}, function(){
		// 启动加载层
		var loadLayer = layer.load(0, {shade: false});
		$.ajax({
			 type:"post",
			 url:prefix+"/returnTranPlan",
			 dataType:"JSON",
			 data:{"mainId":mainId},
			 success:function(data){
				 // 关闭加载层
				 layer.close(loadLayer);
				 if(data.code==0){
					 tuoBack('.tranPlanDetail','#searchBtn');
				 }else{
					layer.msg("退回失败！",{time: 1000});
				 }
			 },
			 error: function (XMLHttpRequest, textStatus, errorThrown) {
				 // 关闭加载层
				 layer.close(loadLayer);
		     }
		 });
	}, function(){
	});
}