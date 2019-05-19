	//地址前缀
var prefix = "/bam/ps";
var id='';
var type="1";

var tableData;
var table;
// 保存时显示数据
var saveShowData;

//标题
var TnationStock2 = '';
var TnationStock1 = '';
var TpadPlanQty = '';
var TsaleForeQty ='';
var TnextSaleForeQty = '';
var TestDeliQty = "";
var TestSaleQty = "";
var TactSaleQty = "";
var TactDeliQty = "";

// 物料筛选条件
var matInfo = '';
var series = '';
var bigItemExpl = '';
var produExpl='';
// 当前页码
var currPageNum = 1;

$(function(){
	var status = $('#status').val();
	if(status == '已提交'){
		// 已提交状态下不允许保存与提交，隐藏
		//$('#submitBtn').css("display", "none");
		//$('#saveBtn').css("display", "none");
		//$(".btnShow").css("display", "none");
	}else{
		// 显示
		$('#submitBtn').css("display", "inline");
		$('#saveBtn').css("display", "inline");
	}

	// 非创建的情况下，不允许修改计划月份
	/*if(status!='' && status!=undefined){
		$('#planMonth').attr("readonly","readonly");
	}*/
	
	// 编辑状态  1：编辑  2：查看
	var editType=$('#type').val();
	if(editType == '1'){
		// （提交状态下）只读
		$("input[type=text]").attr("readonly", true);
		//$("input[type=radio]").attr("disabled", true);
		$("input[type=select]").attr("readonly", true);
		//$("input[type=checkbox]").attr("disabled", true);
		$("input[type=button]").attr("readonly", true);
		//$("textarea").attr("disabled", true);
		
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
		// 编辑的状态
		if(editType == '1'){
			// 删除编码key
			var code = $('#planCode').val();
			code = "PAD_"+code;
			$.ajax({
				type:'POST',
				url:prefix+"/delPlanCode",
				data:{orderCode:code},
				success:function(msg){
					/*if(msg.code == 20){
						layer.msg("订单编码缓存清理失败！",{time:2000});
						return;
					}*/
					
					//window.history.go(-1);
					tuoBack('.padPlanDetail','#searchBtn');
				},
				error:function(){
					layer.msg("判断是否编辑中失败！",{time:1000});
				}
			});
		}else{
			tuoBack('.padPlanDetail','#searchBtn');
		}
	});
	
	// 保存
	$("#saveBtn").click(function(){
		saveFn();
	});
	// 提交
	$("#submitBtn").click(function(){
		submitFn();
	});
	
	// 筛选查询
	$("#searchBtn").click(function(){
		
		layer.open({
			  type:2,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
			  title:"物料筛选",
			  shadeClose : false,
			  shade : 0.1,
			  content : prefix+'/matRepeat',
			  area : [ '500px', '200px' ],
			  maxmin : false, // 开启最大化最小化按钮
			  btn: ['确认','取消']
		  	  ,yes: function(index, layero){
		  		//按钮【按钮一】的回调
		  		var data = $(layero).find("iframe")[0].contentWindow.getData();
		  		// 关闭弹框
		  		layer.close(index);
		  		
		  		matInfo = data.matInfo;
		  		series = data.series;
		  		bigItemExpl = data.bigItemExpl;
		  		produExpl = data.produExpl;

		  		// 筛选
		  		matSearch(tableData);
			  }
			  ,btn2: function(index, layero){
				  //按钮【按钮二】的回调
				  // 默认会关闭弹框
				  //return false 开启该代码可禁止点击该按钮关闭
			  }
		  });
	});
	
	$('#resetBtn').click(function(){

		matInfo = '';
		series = '';
		bigItemExpl = '';
		produExpl='';
		currPageNum = 1;
		// 重新加载数据
		loadDetailTable(table, tableData);
	});
});

// 物料明细table
layui.use(['table','laydate'], function() {
	var $ = layui.$;
	table = layui.table;
	
	type=$('#type').val();
	id=$("#id").val();
	
	// 获取明细数据
	$.ajax({
		 type:"post",
		 url:prefix+"/getPadPlanDetailList?mainId="+id,
		 dataType:"JSON",
		 success:function(data){
			 tableData=data;
			 // 设置列头
			 var ym = $('#planMonth').val();
			 dealColumnName(ym);
			 loadDetailTable(table,tableData);
		 }
	});
	
	// table 数据刷新处理
	table.on('tool(dealChangeEvent)', function(obj){
	    var data = obj.data;
	    if(obj.event == 'setSign'){
			obj.update({nationStock1:data.nationStock1,
				turnOverDays:data.turnOverDays});
	    }
	});
	
	// table 单元格编辑监听
	table.on('edit(dealChangeEvent)', function(obj){
		//console.log(obj.value); //得到修改后的值
		//console.log(obj.field); //当前编辑的字段名
		//console.log(obj.data); //所在行的所有相关数据
		
		//当前全国库存，当月：上月底预计全国库存+预计交货-预计销售/ 未来月份：上月底预计全国库存+生产交货计划-销售预测
		//周转天数（当月）=6月底库存/6月预计销售*30，如果预计销售为0或者空，则跳过不计算
		//周转天数（未发生月份）=本月预计全国库存/本月销售预测*30
		if(obj.field == 'padPlanQty' || obj.field == 'estDeliQty' || obj.field == 'estSaleQty'){
			
			var planMonth = $('#planMonth').val()+"";
			if(pCurrDate<planMonth){
				// 计算未来月份的数据
				// 生产交货计划
				var padPlanQty = parseFloat((obj.data.padPlanQty==""||obj.data.padPlanQty==undefined)?0:obj.data.padPlanQty);
				// 销售预测
				var saleForeQty = parseFloat((obj.data.saleForeQty==""||obj.data.saleForeQty==undefined)?0:obj.data.saleForeQty);
				// xx月全国预测库存
				var nationStock1 = 
					parseFloat(obj.data.nationStock2==undefined?0:obj.data.nationStock2)
					+ padPlanQty-saleForeQty;
				
				nationStock1 = parseFloat(nationStock1.toFixed());
				obj.data.nationStock1 = nationStock1;
				
				// 周转天数
				if(saleForeQty == 0){
					obj.data.turnOverDays = 0;
				}else{
					obj.data.turnOverDays = parseFloat((nationStock1/saleForeQty)*30).toFixed();
				}
				
				$(this).click();
			}else{
				// 计算当前及以前月份
				// 预计交货
				var estDeliQty = parseFloat((obj.data.estDeliQty==""||obj.data.estDeliQty==undefined)?0:obj.data.estDeliQty);
				// 预计销售
				var estSaleQty = parseFloat((obj.data.estSaleQty==""||obj.data.estSaleQty==undefined)?0:obj.data.estSaleQty);
				// xx月全国预测库存
				var nationStock1 = 
					parseFloat(obj.data.nationStock2==undefined?0:obj.data.nationStock2)
					+ estDeliQty-estSaleQty;
				
				nationStock1 = parseFloat(nationStock1.toFixed());
				obj.data.nationStock1 = nationStock1;
				
				// 周转天数
				if(estSaleQty == 0){
					obj.data.turnOverDays = 0;
				}else{
					obj.data.turnOverDays = parseFloat((nationStock1/estSaleQty)*30).toFixed();
				}
				
				$(this).click();
			}
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
	       {checkbox:true, fixed: 'left'}
		  //,{field:'sn', title: '序号',width:'70'}
	      ,{field:'rank', title: '排名',width:'56',
	    	  templet: function(d){
	    		  if(d.rank == undefined){
	    			  return "";
	    		  }
	    		  
	    		  if(d.rank.indexOf('A')>=0){
		    		  return "<span style=\"color:#FF0000;\">"+d.rank+"</span>";
	    		  }else if(d.rank.indexOf('B')>=0){
		    		  return "<span style=\"color:#FF00FF;\">"+d.rank+"</span>";
	    		  }else{
		    		  return d.rank;
	    		  }
	    	}, fixed: 'left'
	      }
	      ,{field:'produExpl', title: '产能划分',width:'82', fixed: 'left'}
	      ,{field:'prodSeries', title: '产品系列',width:'82', fixed: 'left'}
	      ,{field:'bigItemExpl', title: '大品项',width:'100', fixed: 'left'}
	      ,{field:'matName', title: '物料名称',width:'220', fixed: 'left'}
	      ,{field:'nationStock2', title: TnationStock2,width:'130'}
	      ,{field:'padPlanQty', title: TpadPlanQty,width:'150'}
	      ,{field:'actDeliQty', title: TactDeliQty,width:'130'}
	      ,{field:'estDeliQty', title: TestDeliQty,width:'130',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty', title: TsaleForeQty,width:'130'}
	      ,{field:'estSaleQty', title: TestSaleQty,width:'130',edit: 'text',event: 'setSign'}
	      ,{field:'actSaleQty', title: TactSaleQty,width:'130'}
	      ,{field:'nationStock1', title: TnationStock1,width:'150'}
	      ,{field:'turnOverDays', title: '周转天数',width:'90'}
	      //,{field:'actTurnOverDays', title: '实际周转天数',width:'110'}
	      ,{field:'nextSaleForeQty', title: TnextSaleForeQty,width:'130'}
	      ,{field:'threeAvgSales', title: '前三个月平均销量',width:'150'}
		  ,{field:'matCode', title: '物料编号',width:'150'}
	    ]]
	  ,page:true
	  ,id:'matTableID'
	  ,limit:20
	  ,limits:[20,50,80,100,150,200]
	  ,done: function(res, curr, count){
		    //如果是异步请求数据方式，res即为你接口返回的信息。
		    //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
		    //console.log(res);
		    
		    //得到当前页码
		    //console.log(curr); 
		    
		    //得到数据总量
		    //console.log(count);
//		  var data = res.data;
		  var data = tableData;
		  var cell1 = 0;
		  var cell2 = 0;
		  var cell3 = 0;
		  var cell4 = 0;
		  var cell5 = 0;
		  var cell6 = 0;
		  var cell7 = 0;
		  var cell8 = 0;
		  var cell9 = 0;
		  var cell10 = 0;
		  var cell11 = 0;
		  var cell12 = 0;
		  if(data.length>0){
			  // 赋值
			  saveShowData = data;
			  for(var i=0;i<data.length;i++){
				  var item = data[i];
				  
				  cell1 += (item.threeAvgSales==null||isNaN(item.threeAvgSales)||item.threeAvgSales=='')?0:parseFloat(item.threeAvgSales);
				  cell2 += (item.nationStock2==null||isNaN(item.nationStock2)||item.nationStock2=='')?0:parseFloat(item.nationStock2);
				  cell3 += (item.padPlanQty==null||isNaN(item.padPlanQty)||item.padPlanQty=='')?0:parseFloat(item.padPlanQty);
				  cell4 += (item.actDeliQty==null||isNaN(item.actDeliQty)||item.actDeliQty=='')?0:parseFloat(item.actDeliQty);
				  cell5 += (item.estDeliQty==null||isNaN(item.estDeliQty)||item.estDeliQty=='')?0:parseFloat(item.estDeliQty);
				  cell6 += (item.saleForeQty==null||isNaN(item.saleForeQty)||item.saleForeQty=='')?0:parseFloat(item.saleForeQty);
				  cell7 += (item.estSaleQty==null||isNaN(item.estSaleQty)||item.estSaleQty=='')?0:parseFloat(item.estSaleQty);
				  cell8 += (item.actSaleQty==null||isNaN(item.actSaleQty)||item.actSaleQty=='')?0:parseFloat(item.actSaleQty);
				  cell9 += (item.nationStock1==null||isNaN(item.nationStock1)||item.nationStock1=='')?0:parseFloat(item.nationStock1);
				  cell10 += (item.turnOverDays==null||item.turnOverDays==null||isNaN(item.turnOverDays)||item.turnOverDays=='')?0:parseFloat(item.turnOverDays);
				  //cell11 += (isNaN(item.actTurnOverDays)||item.actTurnOverDays=='')?0:parseFloat(item.actTurnOverDays);
				  cell12 += (item.nextSaleForeQty==null||isNaN(item.nextSaleForeQty)||item.nextSaleForeQty=='')?0:parseFloat(item.nextSaleForeQty);
			  }
			  
			  // 计算周转天数
			  var planMonth = $('#planMonth').val()+"";
			  if(pCurrDate<planMonth){
				  // 未来月份计算
				  if(cell6==0){
					  cell10 = 0;
				  }else{
					  cell10 = parseFloat((cell9/cell6)*30).toFixed();
				  }
			  }else{
				  // 当前月及以前
				  if(cell7==0){
					  cell10 = 0;
				  }else{
					  cell10 = parseFloat((cell9/cell7)*30).toFixed();
				  }
			  }
			  
			  var sumRow = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
				  '<td></td>'+
				  '<td></td>'+
				  '<td></td>'+
				  '<td></td>'+
				  '<td>总计</td></tr>';
			  
			  var sumDetailRow = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
			  '<td></td>'+
			  '<td></td>'+
			  '<td></td>'+
			  '<td></td>'+
			  '<td></td>'+
			  '<td>'+cell2.toFixed()+'</td>'+
			  '<td>'+cell3.toFixed()+'</td>'+
			  '<td>'+cell4.toFixed()+'</td>'+
			  '<td>'+cell5.toFixed()+'</td>'+
			  '<td>'+cell6.toFixed()+'</td>'+
			  '<td>'+cell7.toFixed()+'</td>'+
			  '<td>'+cell8.toFixed()+'</td>'+
			  '<td>'+cell9.toFixed()+'</td>'+
			  '<td>'+cell10+'</td>'+
			  //'<td>'+cell11.toFixed()+'</td>'+
			  '<td>'+cell12.toFixed()+'</td>'+
			  '<td>'+cell1.toFixed()+'</td>'+
			  '<td></td></tr>';
			  
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
 * 处理列头的名称
 * @param ym
 * @returns
 */
function dealColumnName(ym){
	if(ym == null || ym == ''){
		TnationStock2 = "上月底全国库存";
		TnationStock1 = "本月底预计全国库存";
		TpadPlanQty = "本月生产/交货计划";
		TsaleForeQty = "本月销售预测";
		TnextSaleForeQty = "下月销售预测";
		TestDeliQty = "本月预计交货";
		TestSaleQty = "本月预计销售";
		TactSaleQty = "本月实际销售";
		TactDeliQty = "本月实际生产";
	}else{
		var ymArr = ym.split('-');
		// 计划月份
		var monthNum = parseInt(ymArr[1]);
		var preMonthNUM = 0;
		var nextMonthNum = 0;
		if(monthNum == 1){
			preMonthNUM = 12;
		}else{
			preMonthNUM = monthNum-1;
		}
		
		if(monthNum == 12){
			nextMonthNum = 1;
		}else{
			nextMonthNum = monthNum+1;
		}
		
		TnationStock2 = preMonthNUM+"月底全国库存";
		TnationStock1 = monthNum+"月底预计全国库存";
		TpadPlanQty = monthNum+"月生产/交货计划";
		TsaleForeQty = monthNum+"月销售预测";
		TnextSaleForeQty = nextMonthNum+"月销售预测";
		TestDeliQty = monthNum+"月预计交货";
		TestSaleQty = monthNum+"月预计销售";
		TactSaleQty = monthNum+"月实际销售";
		TactDeliQty = monthNum+"月实际生产";
	}
	
	//loadDetailTable(table,tableData);
}

/**保存*/
function saveFn(){

	// 获取当前页
	currPageNum = $(".layui-laypage-em").next().html();
	
	// 状态处理
	var status = $('#status').val();
	if(status == '' || status == undefined){
		// 创建情况下
		$('#status').val('已保存');
	}
	
	// 启动加载层
	var loadLayer = layer.load(0, {shade: false});
	// 数组转字符串存储
	$('#padPlanDetailData').val(JSON.stringify(tableData));
	var options = {
			url: prefix+"/savePadPlanInfo",
			type:'POST',
			success: function (msg) {
				// 关闭加载层
				layer.close(loadLayer);
				if(msg.code=="0"){
					//$('#planCode').val(msg.msg);
					//loadDetailTable(table,saveShowData);
			  		// 筛选
			  		matSearch(saveShowData);
			  		// 加载当前页
			  		loadMatDataByPageNum(currPageNum);
					layer.msg("保存成功！",{time: 1000});
				}else if(msg.code=="90" || msg.code==90){
					$('#status').val('');
					layer.msg(msg.msg,{time: 2000});
				}else{
					$('#status').val('');
					layer.msg("保存失败！",{time: 1000});
				}
			},
			error: function(request) {
				$('#status').val('');
				// 关闭加载层
				layer.close(loadLayer);
				layer.msg("保存异常！",{time: 1000});
			}
	};
	$("#submitForm").ajaxSubmit(options);
}

/**
 * 根据页码加载物料数据
 * @returns
 */
function loadMatDataByPageNum(pageNum){
	if(pageNum == null || pageNum == undefined || pageNum === ''){
	}else{
		pageNum = parseInt(pageNum);

  		$(".layui-laypage-skip").find("input").val(pageNum);
  		$(".layui-laypage-btn").click();
	}
}

// 提交
function submitFn(){
	/*// 主表ID
	var id = $('#id').val();
	// 获取明细数据
	$.ajax({
		 type:"post",
		 url:prefix+"/submitPadPlan?mainId="+id,
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
	 });*/
	
	// 状态处理
	var status = $('#status').val();
	if(status == '' || status == undefined){
		status = '';
		// 创建情况下
		$('#status').val(status);
	}
	
	// 启动加载层
	var loadLayer = layer.load(0, {shade: false});
	// 数组转字符串存储
	$('#padPlanDetailData').val(JSON.stringify(tableData));
	var options = {
			url: prefix+"/submitPadPlan",
			type:'POST',
			success: function (msg) {
				// 关闭加载层
				layer.close(loadLayer);
				if(msg.code=="0"){
					layer.msg("提交成功！",{time: 1000});
					// 返回列表页
					//window.history.go(-1);
					tuoBack('.padPlanDetail','#searchBtn');
				}else if(msg.code=="90" || msg.code==90){
					$('#status').val(status);
					layer.msg(msg.msg,{time: 2000});
				}else{
					$('#status').val(status);
					layer.msg("提交失败！",{time: 1000});
				}
			},
			error: function(request) {
				$('#status').val(status);
				// 关闭加载层
				layer.close(loadLayer);
				layer.msg("提交异常！",{time: 1000});
			}
	};
	$("#submitForm").ajaxSubmit(options);
}

/**
 * 物料筛选
 * @returns
 */
function matSearch(data){
	var searchTbData = new Array();
	var tbData = data;
	for(var i=0;i<tbData.length;i++){
		var item = tbData[i];
		
		var matFlag = false;
		var seriesFlag = false;
		var bigItemFlag = false;
		var produExplFlag = false;

		if(matInfo != ''){
			if(item.matCode != undefined
					&& item.matName != undefined
					&& (item.matCode.toUpperCase().indexOf(matInfo.toUpperCase())>=0 || item.matName.toUpperCase().indexOf(matInfo.toUpperCase())>=0)){
				matFlag = true;
			}
		}else{
			matFlag = true;
		}
		
		if(series != ''){
			if(item.prodSeries != undefined
					&& item.prodSeries.toUpperCase().indexOf(series.toUpperCase())>=0){
				seriesFlag = true;
			}
		}else{
			seriesFlag = true;
		}
		if(bigItemExpl != ''){
			if(item.bigItemExpl != undefined
					&& item.bigItemExpl.toUpperCase().indexOf(bigItemExpl.toUpperCase())>=0){
				bigItemFlag = true;
			}
		}else{
			bigItemFlag=true;
		}
		
		if(produExpl != ''){
			if(item.produExpl != undefined
					&& item.produExpl.toUpperCase().indexOf(produExpl.toUpperCase())>=0){
				produExplFlag = true;
			}
		}else{
			produExplFlag=true;
		}				
		if(matFlag && seriesFlag && bigItemFlag && produExplFlag){
			searchTbData.push(item);
		}
	}
	// 重新加载数据
	loadDetailTable(table, searchTbData);
}

