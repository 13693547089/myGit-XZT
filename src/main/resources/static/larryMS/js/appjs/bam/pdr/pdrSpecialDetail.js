//地址前缀
var prefix = "/bam/pdr";
var id='';
var type="1";

var tableData;
var table;

var itemTb1;
var itemTb2;
var itemTb3;

// 最大的序号
var maxNo1 = 0;
var maxNo2 = 0;
var maxNo3 = 0;
$(function(){
	var status = $('#status').val();
	if(status == '已提交'){
		// 已提交状态下不允许保存与提交，隐藏
		/*$('#submitBtn').css("display", "none");
		$('#saveBtn').css("display", "none");*/
		$(".btnShow").css("display", "none");
	}else{
		// 显示
		$('#submitBtn').css("display", "inline");
		$('#saveBtn').css("display", "inline");
	}
	
	// 编辑状态  1：编辑  2：查看
	var editType=$('#type').val();
	if(editType == '1'){
		$("#returnBtn").css("display", "none");
	}else{
		// 查看状态下不允许编辑
		$("input[type=text]").attr("disabled", true);
		//$("input[type=radio]").attr("disabled", true);
		$("input[type=select]").attr("disabled", true);
		//$("input[type=checkbox]").attr("disabled", true);
		$("input[type=button]").attr("disabled", true);
		//$("textarea").attr("disabled", true);
		
		$(".btnShow").css("display", "none");
		
		$("#suppSelBtn").attr("disabled", true);
	}
	
	// 返回
	$("#backBtn").click(function(){
		//window.history.go(-1);
		tuoBack('.pdrDetail','#searchBtn');
	});
	
	// 保存
	$("#saveBtn").click(function(){
		saveFn();
	});
	// 提交
	$("#submitBtn").click(function(){
		submitFn();
	});
	
	// 选择公益公司
	$("#suppSelBtn").click(function(){
		selSuppFn();
	});
	
	// 处理项次数据
	$("#addItem1Btn").click(function(){
		// 实际生产
		var type = 'pi';
		addItemData(type);
	});
	$("#addItem2Btn").click(function(){
		// 实际库存
		var type = 'po';
		addItemData(type);
	});
	$("#addItem3Btn").click(function(){
		// 实际交货
		var type = 'pd';
		addItemData(type);
	});

	$("#delItem1Btn").click(function(){
		var type = 'pi';
		delItemData(type);
	});
	$("#delItem2Btn").click(function(){
		var type = 'po';
		delItemData(type);
	});
	$("#delItem3Btn").click(function(){
		var type = 'pd';
		delItemData(type);
	});
	
	// *****筛选*****
	// 明细
	$('#searchBtn').click(function(){

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
		  		
		  		var matInfo = data.matInfo;
		  		var series = data.series;
		  		var batch = data.batch;
		  		
		  		var searchTbData = new Array();
		  		for(var i=0;i<tableData.length;i++){
	  				var item = tableData[i];
	  				
	  				var matFlag = false;
	  				var seriesFlag = false;
	  				var batchFlag = false;
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
	  				if(batch != ''){
	  					if(item.batch != undefined
		  						&& item.batch.toUpperCase().indexOf(batch.toUpperCase())>=0){
	  						batchFlag = true;
	  					}
	  				}else{
	  					batchFlag=true;
	  				}
	  				
	  				if(matFlag && seriesFlag && batchFlag){
	  					searchTbData.push(item);
	  				}
	  			}
		  		// 重新加载数据
		   		loadDetailTable(table, searchTbData);
			  }
			  ,btn2: function(index, layero){
				  //按钮【按钮二】的回调
				  // 默认会关闭弹框
				  //return false 开启该代码可禁止点击该按钮关闭
			  }
		  });
	});
	$('#resetBtn').click(function(){
		// 重新加载数据
		loadDetailTable(table, tableData);
	});
	// 实际生产
	$('#search1Btn').click(function(){
		var type = 'pi';
		itemFilter(type);
	});
	$('#reset1Btn').click(function(){
		loadItemTable1(table,itemTb1);
	});
	// 实际库存
	$('#search2Btn').click(function(){
		var type = 'po';
		itemFilter(type);
	});
	$('#reset2Btn').click(function(){
		loadItemTable2(table,itemTb2);
	});
	// 实际交货
	$('#search3Btn').click(function(){
		var type = 'pd';
		itemFilter(type);
	});
	$('#reset3Btn').click(function(){
		loadItemTable3(table,itemTb3);
	});
});

/**
 * item数据筛选
 * @param type
 * @returns
 */
function itemFilter(type){
	
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
	  		
	  		var matInfo = data.matInfo;
	  		var series = data.series;
	  		var batch = data.batch;
	  		
	  		var searchTbData = new Array();
	  		
	  		var itemData;
	  		if(type == 'pi'){
	  			//实际生产
	  			itemData= itemTb1;
	  		}else if(type == 'po'){
	  			//实际库存
	  			itemData= itemTb2;
	  		}else if(type == 'pd'){
	  			//实际交货
	  			itemData= itemTb3;
	  		}
	  		
	  		for(var i=0;i<itemData.length;i++){
				var item = itemData[i];
				
				var matFlag = false;
				var seriesFlag = false;
				var batchFlag = false;
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
				if(batch != ''){
					if(item.boardName != undefined
	  						&& item.boardName.toUpperCase().indexOf(batch.toUpperCase())>=0){
						batchFlag = true;
					}
				}else{
					batchFlag=true;
				}
				
				if(matFlag && seriesFlag && batchFlag){
					searchTbData.push(item);
				}
			}
	  		// 重新加载数据
	   		if(type == 'pi'){
	  			//实际生产
	   			loadItemTable1(table,searchTbData);
	  		}else if(type == 'po'){
	  			//实际库存
	   			loadItemTable2(table,searchTbData);
	  		}else if(type == 'pd'){
	  			//实际交货
	   			loadItemTable3(table,searchTbData);
	  		}
		  }
		  ,btn2: function(index, layero){
			  //按钮【按钮二】的回调
			  // 默认会关闭弹框
			  //return false 开启该代码可禁止点击该按钮关闭
		  }
	  });
}

// 定义日期格式
function laydateInit(laydate){
	  // 生产日期
	  laydate.render({
	    elem: '#produceDate',
	    done: function(value, date, endDate){
	        //value 得到日期生成的值，如：2017-08-18
	        //date 得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
	        //endDate 得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员同上。
	    	
	    	// 根据生产日期获取物料明细的数据
	    	loadDetailData(value);
	     }
	  });
};

// 物料明细table
layui.use(['table','laydate'], function() {
	var $ = layui.$;
	table = layui.table;
	var laydate = layui.laydate;
	// 定义日期控件
	laydateInit(laydate);
	
	type=$('#type').val();
	id=$("#id").val();
	
	// 加载明细、项次数据
	loadData(id);
	
	// table 数据刷新处理
	table.on('tool(dealChangeEvent)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'setSign'){
	    	obj.update({currCompQty:data.currCompQty,
	    		compScale:data.compScale});
	    }
	});
	
	// table 单元格编辑监听
	table.on('edit(dealChangeEvent)', function(obj){
		//console.log(obj.value); //得到修改后的值
		//console.log(obj.field); //当前编辑的字段名
		//console.log(obj.data); //所在行的所有相关数据  
		
		if(obj.field == 'actPdcQty'){
			// 目前已完成
			var currCompQty = 
				parseFloat(obj.data.preCompQty)
				+parseFloat(obj.data.actPdcQty==""?0:obj.data.actPdcQty);
			
			currCompQty = parseFloat(currCompQty.toFixed(2));
			obj.data.currCompQty = currCompQty;
			
			// 达成比=实际生产/生产计划
			var actPdcQty = parseFloat(obj.data.actPdcQty==""?0:obj.data.actPdcQty);
			var pdcPlanQty = parseFloat(obj.data.pdcPlanQty==""?0:obj.data.pdcPlanQty);
			if(pdcPlanQty == 0){
				obj.data.compScale = "";
			}else{
				obj.data.compScale = parseFloat((actPdcQty/pdcPlanQty)*100).toFixed(2)+"%";
			}

			//
			$(this).click();
		}
	});
	
	// 实际库库 grid 事件
	table.on('edit(item2Event)', function(obj){
		// 已检与未检触发
		if(obj.field == 'qcQty' || obj.field == 'unQcQty'){
			var qcQty = obj.data.qcQty;
			var unQcQty = obj.data.unQcQty;
			
			var qty = 
				parseFloat((qcQty==undefined||qcQty=='')?0:qcQty)
				+parseFloat((unQcQty==undefined||unQcQty=='')?0:unQcQty);
			
			obj.data.qty = parseFloat(qty.toFixed(2));
			
			$(this).click();
		}
	});
	
	table.on('tool(item2Event)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'setSign'){
	    	obj.update({qty:data.qty});
	    }
	})
	
});

/**
 * 加载明细、项次数据
 * @param id
 * @returns
 */
function loadData(id){

	// 获取明细数据
	$.ajax({
		 type:"post",
		 url:prefix+"/getPdrDetailList?mainId="+id,
		 dataType:"JSON",
		 success:function(data){
			tableData=data;
			loadDetailTable(table,tableData);
		 }
	});
	
	// 获取项次数据
	$.ajax({
		 type:"post",
		 url:prefix+"/getPdrItemList?mainId="+id,
		 data:{"type":"pi"},
		 dataType:"JSON",
		 success:function(data){
			itemTb1=data;
			loadItemTable1(table,itemTb1);
		 }
	});
	$.ajax({
		 type:"post",
		 url:prefix+"/getPdrItemList?mainId="+id,
		 data:{"type":"po"},
		 dataType:"JSON",
		 success:function(data){
			itemTb2=data;
			loadItemTable2(table,itemTb2);
		 }
	});
	$.ajax({
		 type:"post",
		 url:prefix+"/getPdrItemList?mainId="+id,
		 data:{"type":"pd"},
		 dataType:"JSON",
		 success:function(data){
			itemTb3=data;
			loadItemTable3(table,itemTb3);
		 }
	});
}


// 加载物料明细的数据
function loadDetailData(productDate){
	/*if(productDate == null || productDate == undefined){
		layer.msg("请选择生产日期！",{time: 1000});
		return;
	}*/

	// 启动加载层
	var index = layer.load(0, {shade: false});
	// 供应商编码
	var suppCode = $('#suppCode').val();
	// 主表ID
	var mainId = $('#id').val();
	$.ajax({
		 type:"post",
		 url:prefix+"/getPdrDetailListFromSuppProd",
		 dataType:"JSON",
		 data:{"suppCode":suppCode,"productDate":productDate,"mainId":mainId},
		 success:function(data){
			tableData = data;
			loadDetailTable(table,tableData);
			
			// 获取实际库存数据
			itemTb2 = [];
			for(var i=0;i<tableData.length;i++){
				var arr = {
					mainId:mainId,
					sn : i+1,
					matCode : tableData[i].matCode,
					matName: tableData[i].matName,
					itemType:'po',
					batchNo:'',
					qty:tableData[i].stockQty,
					qcQty:tableData[i].qcStock,
					unQcQty:tableData[i].unQcStock,
					remark:''
				};
				
				itemTb2.push(arr);
			}
			if(itemTb2.length>0){
				
				var itemTb2Json = JSON.stringify(itemTb2);
				
				$.ajax({
					 type:"post",
					 url:prefix+"/getPdrItemStockList",
					 dataType:"JSON",
					 data:{"suppCode":suppCode,"productDate":productDate,"itemTb2Json":itemTb2Json},
					 success:function(data){
						 itemTb2 = data;
						// 加载
						loadItemTable2(table,itemTb2);
					 }
				});
			}
			
	    	// 关闭加载层
	    	layer.close(index);
		 },error: function (XMLHttpRequest, textStatus, errorThrown) {
           /*  // 状态码
             console.log(XMLHttpRequest.status);
             // 状态
             console.log(XMLHttpRequest.readyState);
             // 错误信息   
             console.log(textStatus);*/
			 
 	    	// 关闭加载层
 	    	layer.close(index);
         }
	});
}

// 数据加载，table渲染
function loadDetailTable(table,tableData) {
	table.render({
	     elem: '#mat-table'
	    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
	    ,data:tableData
	    ,cols: [[
	           {checkbox:true, fixed: 'left'}
			  //,{field:'sn', title: '序号',width:'70',type:'numbers'}
			  ,{field:'matCode', title: '物料编号',width:'130', fixed: 'left'}
		      ,{field:'matName', title: '物料名称',width:'220', fixed: 'left'}
		      ,{field:'matUnit', title: '单位',width:'70', fixed: 'left'}
		      ,{field:'prodSeries', title: '系列',width:'100', fixed: 'left'}
		      ,{field:'batch', title: '类别',width:'70', fixed: 'left'}
		      ,{field:'pdcPlanQty', title: '本月生产计划',width:'120'}
		      ,{field:'currNeedQty', title: '目前应完成',width:'100'}
		      //,{field:'preCompQty', title: '之前已完成',width:'100'}
		      ,{field:'currCompQty', title: '目前已完成',width:'100'}
		      ,{field:'compScale', title: '生产达成比',width:'100'}
		      ,{field:'actPdcQty', title: '实际生产',width:'100',event: 'setSign'}
		      ,{field:'devPlanQty', title: '本月交货计划',width:'120'}
		      //,{field:'preSumDev', title: '之前已交货',width:'120'}
		      ,{field:'currSumDev', title: '本月已交货',width:'120'}
		      ,{field:'actDevQty', title: '本日实际交货',width:'130'}
		      ,{field:'devCompScale', title: '交货达成比',width:'100'}
		      ,{field:'qcStock', title: '已检库存',width:'100'}
		      ,{field:'unQcStock', title: '未检库存',width:'100'}
		      ,{field:'stockQty', title: '库存数量',width:'100'}
		      ,{field:'beginStock', title: '期初库存',width:'100'}
		      ,{field:'theoryStock', title: '理论库存',width:'100'}
		      ,{field:'diffStock', title: '差异库存',width:'100'}
		      ,{field:'remark', title: '备注',width:'90',edit: 'text'}
	    ]]
	  ,page:true
	  ,done: function(res, curr, count){
		  var data = res.data;
		  var cell1 = 0;
		  var cell2 = 0;
		  var cell3 = 0;
		  var cell4 = '';
		  var cell5 = 0;
		  var cell6 = 0;
		  var cell7 = 0;
		  var cell8 = 0;
		  var cell9 = 0;
		  var cell10 = 0;
		  var cell11 = 0;
		  var cell12 = 0;
		  var cell13 = 0; // 本月交货计划
		  var cell14 = ''; // 交货达成比
		  
		  var currSumDev = 0;
		  if(data.length>0){
			  // 赋值
			  saveShowData = data;
			  for(var i=0;i<data.length;i++){
				  var item = data[i];
				  
				  cell1 += (item.pdcPlanQty==null||isNaN(item.pdcPlanQty)||item.pdcPlanQty=='')?0:parseFloat(item.pdcPlanQty);
				  cell2 += (item.currNeedQty==null||isNaN(item.currNeedQty)||item.currNeedQty=='')?0:parseFloat(item.currNeedQty);
				  cell3 += (item.currCompQty==null||isNaN(item.currCompQty)||item.currCompQty=='')?0:parseFloat(item.currCompQty);
				  //cell4 += (item.actDeliQty==null||isNaN(item.actDeliQty)||item.actDeliQty=='')?0:parseFloat(item.actDeliQty);
				  cell5 += (item.actPdcQty==null||isNaN(item.actPdcQty)||item.actPdcQty=='')?0:parseFloat(item.actPdcQty);
				  cell6 += (item.actDevQty==null||isNaN(item.actDevQty)||item.actDevQty=='')?0:parseFloat(item.actDevQty);
				  cell7 += (item.qcStock==null||isNaN(item.qcStock)||item.qcStock=='')?0:parseFloat(item.qcStock);
				  cell8 += (item.unQcStock==null||isNaN(item.unQcStock)||item.unQcStock=='')?0:parseFloat(item.unQcStock);
				  cell9 += (item.stockQty==null||isNaN(item.stockQty)||item.stockQty=='')?0:parseFloat(item.stockQty);
				  cell10 += (item.beginStock==null||isNaN(item.beginStock)||item.beginStock=='')?0:parseFloat(item.beginStock);
				  cell11 += (item.theoryStock==null||isNaN(item.theoryStock)||item.theoryStock=='')?0:parseFloat(item.theoryStock);
				  cell12 += (item.diffStock==null||isNaN(item.diffStock)||item.diffStock=='')?0:parseFloat(item.diffStock);
				  cell13 += (item.devPlanQty==null||isNaN(item.devPlanQty)||item.devPlanQty=='')?0:parseFloat(item.devPlanQty);
				  
				  currSumDev += (item.currSumDev==null||isNaN(item.currSumDev)||item.currSumDev=='')?0:parseFloat(item.currSumDev);
			  }
			  
			  if(cell1 == 0){
				  // 生产达成比
				  cell4 = "0%";
				  // 交货达成比
				  cell14 = "0%";
			  }else{
				  cell4 = parseFloat((cell3/cell1)*100).toFixed(2)+"%";
				  
				  if(cell13 != 0){
					  cell14 = parseFloat((currSumDev/cell13)*100).toFixed(2)+"%";
				  }else{
					  cell14 = "0%";
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
			  '<td>'+cell1.toFixed(2)+'</td>'+
			  '<td>'+cell2.toFixed(2)+'</td>'+
			  '<td>'+cell3.toFixed(2)+'</td>'+
			  '<td>'+cell4+'</td>'+
			  '<td>'+cell5.toFixed(2)+'</td>'+
			  '<td>'+cell13.toFixed(2)+'</td>'+
			  '<td>'+currSumDev.toFixed(2)+'</td>'+
			  '<td>'+cell6.toFixed(2)+'</td>'+
			  '<td>'+cell14+'</td>'+
			  '<td>'+cell7.toFixed(2)+'</td>'+
			  '<td>'+cell8.toFixed(2)+'</td>'+
			  '<td>'+cell9.toFixed(2)+'</td>'+
			  '<td>'+cell10.toFixed(2)+'</td>'+
			  '<td>'+cell11.toFixed(2)+'</td>'+
			  '<td>'+cell12.toFixed(2)+'</td>'+
			  '<td></td></tr>';
			  
			  $('.sumMat .layui-table-fixed .layui-table tbody').append(sumRow);
			  
			  $('.sumMat .layui-table-main .layui-table tbody').append(sumDetailRow);
		  }
	  }
	});

	if(type == '2'){
		 $('table td').removeAttr('data-edit');
	}
}

//数据加载，table渲染, 实际生产
function loadItemTable1(table,itemTb1) {
	maxNo1 = 0;
	if(itemTb1.length > 0){
		maxNo1 = itemTb1[itemTb1.length-1].sn;
	}
	table.render({
	     elem: '#item-table1'
	    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
	    ,data:itemTb1
	    ,cols: [[
	           {checkbox:true}
			  //,{field:'sn', title: '序号',width:'70',type:'numbers'}
			  ,{field:'matCode', title: '物料编号',width:'150'}
		      ,{field:'matName', title: '物料名称',width:'250'}
		      ,{field:'prodSeries', title: '系列',width:'100'}
		      ,{field:'boardName', title: '类别',width:'70'}
		      ,{field:'batchNo', title: '批次号',width:'230',edit: 'text'}
		      ,{field:'qty', title: '数量',width:'150',edit: 'text'}
		      ,{field:'remark', title: '备注',width:'170',edit: 'text'}
	    ]]
	  ,page:true
	});

	if(type == '2'){
		 $('table td').removeAttr('data-edit');
	}
}


//数据加载，table渲染, 实际库存
function loadItemTable2(table,itemTb2) {
	maxNo2 = 0;
	if(itemTb2.length > 0){
		maxNo2 = itemTb2[itemTb2.length-1].sn;
	}
	table.render({
	     elem: '#item-table2'
	    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
	    ,data:itemTb2
	    ,cols: [[
	           {checkbox:true}
			  //,{field:'sn', title: '序号',width:'70',type:'numbers'}
			  ,{field:'matCode', title: '物料编号',width:'150'}
		      ,{field:'matName', title: '物料名称',width:'250'}
		      ,{field:'prodSeries', title: '系列',width:'100'}
		      ,{field:'boardName', title: '类别',width:'70'}
		      ,{field:'batchNo', title: '批次号',width:'230',edit: 'text'}
		      ,{field:'qcQty', title: '已检数量',width:'130',edit: 'text',event: 'setSign'}
		      ,{field:'unQcQty', title: '未检数量',width:'130',edit: 'text',event: 'setSign'}
		      ,{field:'qty', title: '数量',width:'150'}
		      ,{field:'remark', title: '备注',width:'170',edit: 'text'}
	    ]]
	  ,page:true
	});

	if(type == '2'){
		 $('table td').removeAttr('data-edit');
	}
}

//数据加载，table渲染, 实际交货
function loadItemTable3(table,itemTb3) {
	maxNo3 = 0;
	if(itemTb3.length > 0){
		maxNo3 = itemTb3[itemTb3.length-1].sn;
	}
	table.render({
	     elem: '#item-table3'
	    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
	    ,data:itemTb3
	    ,cols: [[
	           {checkbox:true}
			  //,{field:'sn', title: '序号',width:'70',type:'numbers'}
			  ,{field:'matCode', title: '物料编号',width:'150'}
		      ,{field:'matName', title: '物料名称',width:'250'}
		      ,{field:'prodSeries', title: '系列',width:'100'}
		      ,{field:'boardName', title: '类别',width:'70'}
		      ,{field:'batchNo', title: '批次号',width:'230',edit: 'text'}
		      ,{field:'qty', title: '数量',width:'150',edit: 'text'}
		      ,{field:'remark', title: '备注',width:'170',edit: 'text'}
	    ]]
	  ,page:true
	});

	if(type == '2'){
		 $('table td').removeAttr('data-edit');
	}
}
function saveFn(){
	
	// 状态处理
	var status = $('#status').val();
	if(status == '' || status == undefined){
		// 创建情况下
		$('#status').val('已保存');
	}

	// 启动加载层
	var loadLayer = layer.load(0, {shade: false});
	// 数组转字符串存储
	$('#pdrDetailData').val(JSON.stringify(tableData));
	$('#pdrItem1Data').val(JSON.stringify(itemTb1));
	$('#pdrItem2Data').val(JSON.stringify(itemTb2));
	$('#pdrItem3Data').val(JSON.stringify(itemTb3));
	var options = {
			url: prefix+"/savePdrInfo?sType=sv",
			type:'POST',
			success: function (msg) {
				// 关闭加载层
				layer.close(loadLayer);
				if(msg.code=="0"){
					layer.msg("保存成功！",{time: 1000});
					
					// 重新加载明细、项次数据
					loadData(id);
				}else if(msg.code=="90" || msg.code==90){
					$('#status').val('');
					layer.msg(msg.msg,{time: 2000});
				}else if(msg.code=="80" || msg.code==80){
					$('#status').val('');
					layer.msg('存在重复物料：<br>'+msg.msg, {
				        time: 20000, //20s后自动关闭
				        btn: ['关闭']
				    });
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
		 url:prefix+"/submitPdr?mainId="+id,
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
	
	// 状态处理
	var status = $('#status').val();
	if(status == '' || status == undefined){
		status = '';
		// 创建情况下
		$('#status').val('已保存');
	}

	// 启动加载层
	var loadLayer = layer.load(0, {shade: false});
	// 数组转字符串存储
	$('#pdrDetailData').val(JSON.stringify(tableData));
	$('#pdrItem1Data').val(JSON.stringify(itemTb1));
	$('#pdrItem2Data').val(JSON.stringify(itemTb2));
	$('#pdrItem3Data').val(JSON.stringify(itemTb3));
	var options = {
			url: prefix+"/savePdrInfo?sType=sb",
			type:'POST',
			success: function (msg) {
				// 关闭加载层
				layer.close(loadLayer);
				if(msg.code=="0"){
					layer.msg("提交成功！",{time: 1000});
					// 返回列表页
					//window.history.go(-1);
					tuoBack('.pdrDetail','#searchBtn');
				}else if(msg.code=="90" || msg.code==90){
					$('#status').val(status);
					layer.msg(msg.msg,{time: 2000});
				}else if(msg.code=="80" || msg.code==80){
					$('#status').val(status);
					layer.msg('存在重复物料：<br>'+msg.msg, {
				        time: 20000, //20s后自动关闭
				        btn: ['关闭']
				    });
				}else{
					$('#status').val(status);
					layer.msg("提交失败！",{time: 1000});
				}
			},
			error: function(request) {
				// 关闭加载层
				layer.close(loadLayer);
				$('#status').val(status);
				layer.msg("提交异常！",{time: 1000});
			}
	};
	$("#submitForm").ajaxSubmit(options);
}

/**
 * 添加项次数据
 * @param type
 * @returns
 */
function addItemData(type){
	var checkStatus = table.checkStatus('mat-table');
   	var checkedData = checkStatus.data;
   	if (checkedData.length > 0) {
   		var mainId = $('#id').val();
   		var sno = 0;
		if(type=='pi'){
			sno = maxNo1;
		}else if(type=='po'){
			sno = maxNo2;
		}else if(type == 'pd'){
			sno = maxNo3;
		}
   		for(var i=0;i<checkedData.length;i++){
   			//创建table的行，赋值，添加到table中
   			sno++;
			var arr = {
				mainId:mainId,
				sn : sno,
				matCode : checkedData[i].matCode,
				matName: checkedData[i].matName,
				prodSeries:checkedData[i].prodSeries,
				boardName:checkedData[i].batch,
				itemType:type,
				batchNo:'',
				qty:0,
				qcQty:0,
				unQcQty:0,
				remark:''
			};
			
			if(type=='pi'){
				itemTb1.push(arr); //添加至数组末
				//itemTb1.unshift(arr); // 添加至数组首
			}else if(type=='po'){
				itemTb2.push(arr); //添加至数组末
				//itemTb2.unshift(arr);
			}else if(type == 'pd'){
				itemTb3.push(arr); //添加至数组末
				//itemTb3.unshift(arr);
			}
   		}
   		
   		// 重新设置最大值
   		if(type=='pi'){
			maxNo1 = sno;
		}else if(type=='po'){
			maxNo2 = sno;
		}else if(type == 'pd'){
			maxNo3 = sno;
		}
   		
   		if(type=='pi'){
   	   		// 重新加载数据
   			loadItemTable1(table, itemTb1);
		}else if(type=='po'){
	   		// 重新加载数据
			loadItemTable2(table, itemTb2);
		}else if(type == 'pd'){
	   		// 重新加载数据
			loadItemTable3(table, itemTb3);
		}
   	} else {
   		layer.msg("请选择要添加的物料信息！");
   	}
}

/**
 * 删除项次数据
 * @param type
 * @returns
 */
function delItemData(type){
	var checkStatus;
	if(type=='pi'){
		checkStatus = table.checkStatus('item-table1');
	}else if(type=='po'){
		checkStatus = table.checkStatus('item-table2');
	}else if(type == 'pd'){
		checkStatus = table.checkStatus('item-table3');
	}
	
   	var checkedData = checkStatus.data;
   	if (checkedData.length > 0) {
   		
   		if(type=='pi'){
   	   		for (var i = checkedData.length-1; i >=0 ; i--) {
   	   			// 删除数组元素
   	   			itemTb1.splice(checkedData[i].sn-1,1);
   	   		}
   	   		
   	   		// 数据集重新设置sn
   	   		for(var i = 0; i < itemTb1.length; i++){
   	   			itemTb1[i].sn = i+1;
   	   		}
			maxNo1 = itemTb1.length;
   	   		loadItemTable1(table, itemTb1);
   		}else if(type=='po'){
   			for (var i = checkedData.length-1; i >=0 ; i--) {
	   			// 删除数组元素
	   			itemTb2.splice(checkedData[i].sn-1,1);
	   		}
	   		
	   		// 数据集重新设置sn
	   		for(var i = 0; i < itemTb2.length; i++){
	   			itemTb2[i].sn = i+1;
	   		}
			maxNo2 = itemTb2.length;
	   		loadItemTable2(table, itemTb2);
   		}else if(type == 'pd'){
   			for (var i = checkedData.length-1; i >=0 ; i--) {
   	   			// 删除数组元素
   	   			itemTb3.splice(checkedData[i].sn-1,1);
   	   		}
   	   		
   	   		// 数据集重新设置sn
   	   		for(var i = 0; i < itemTb3.length; i++){
   	   			itemTb3[i].sn = i+1;
   	   		}
			maxNo3 = itemTb3.length;
	   		loadItemTable3(table, itemTb3);
   		}
   	}else{
   		layer.msg("请选择需要删除的信息！");
   	}
}

/**
 * 选择供应商
 * @returns
 */
function selSuppFn(){
	
	layer.open({
		  type:2,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
		  title:"供应商选择",
		  shadeClose : false,
		  shade : 0.1,
		  content : prefix+'/suppSelDialog',
		  area : [ '700px', '600px' ],
		  maxmin : false, // 开启最大化最小化按钮
		  btn: ['确认','取消']
	  	  ,yes: function(index, layero){
	  		//按钮【按钮一】的回调
	  		var data = $(layero).find("iframe")[0].contentWindow.getCheckedData();
	  		
	  		if(data.length !=1){
				layer.msg("只能选择一个供应商！",{time:2000});
				return;
	  		}
	  		
	  		var suppName = data[0].suppName;
	  		var suppCode = data[0].sapId;
	  		$("#suppCode").val(suppCode);
	  		$("#suppName").val(suppName);

	  		// 加载物料数据
	  		var productDate = $("#produceDate").val();
	  		if(productDate !== undefined && productDate !== ""){
		  		loadDetailData(productDate);
	  		}
	  		
	  		layer.close(index);
		  }
		  ,btn2: function(index, layero){
			  //按钮【按钮二】的回调
			  // 默认会关闭弹框
			  //return false 开启该代码可禁止点击该按钮关闭
		  }
	  });
}
