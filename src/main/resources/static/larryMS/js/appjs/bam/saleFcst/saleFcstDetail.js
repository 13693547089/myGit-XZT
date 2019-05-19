	//地址前缀
var prefix = "/bam/sf";
var id='';
var type="1";

var tableData;
var table;

var yearSel=true;

// 列头
var Tq=new Array(24);

var Tsum1='';
var Tsum2='';
var Tsum3='';
var matInfoCondition='' ;
var seriesCondition='' ;
var bigItemExplCondition='';
/*
 * 列头处理
 */
function dealColumnName(year){
	if(year == null || year == ''){
		for(var i=0;i<24;i++){
			Tq[i] = (i+1)+'月销售预测';
		}
		
		Tsum1 = '预测总计';
		Tsum2 = '预测总计';
		Tsum3 = '预测总计';
	}else{
		var ymArr = year.split('~');
		var sYm = ymArr[0].trim();
		var eYm = ymArr[1].trim();
		
		var sYmArr = sYm.split('-');
		var sYear = parseInt(sYmArr[0]);
		var sMonth = parseInt(sYmArr[1]);
		var eYmArr = eYm.split('-');
		var eYear = parseInt(eYmArr[0]);
		var eMonth = parseInt(eYmArr[1]);
		
		var sY = sYmArr[0];
		var eY = eYmArr[0];
		var year1 = sY.substring(sY.length-2,sY.length);
		var year2 = eY.substring(eY.length-2,eY.length);
		if(sMonth == 1){
			
			// 两年，初始月为1月
			for(var i=0;i<12;i++){
				Tq[i] = year1+'年'+(i+1)+'月销售预测';
			}
			Tsum1 = year1+'年总计';
			
			for(var i=12;i<24;i++){
				Tq[i] = year2+'年'+(i-11)+'月销售预测';
			}
			Tsum2 = year2+'年总计';
			
			Tsum3 = 'XX年总计';
		}else{
			// 三年
			var year3 = parseInt(year1)+1;
			
			// 起始年份
			var ssi = 12-sMonth;
			for(var i=0;i<=ssi;i++){
				Tq[i] = year1+'年'+(sMonth+i)+'月销售预测';
			}
			// 中间年份
			for(var i=1;i<=12;i++){
				Tq[ssi+i] = year3+'年'+i+'月销售预测';
			}
			// 结束年份
			for(var i=1;i<=eMonth;i++){
				Tq[ssi+12+i] = year2+'年'+i+'月销售预测';
			}

			Tsum1 = year1+'年总计';
			Tsum2 = year3+'年总计';
			Tsum3 = year2+'年总计';
		}
	}
}

$(function(){
	var status = $('#status').val();
	if(status == '已提交'){
		// 已提交状态下不允许保存与提交，隐藏
		//$('#submitBtn').css("display", "none");
		//$('#saveBtn').css("display", "none");
		$(".btnShow").css("display", "none");
	}else{
		// 显示
		/*$('#submitBtn').css("display", "inline");*/
		$('#saveBtn').css("display", "inline");
	}
	
	// 编辑状态  1：编辑  2：查看
	var editType=$('#type').val();
	if(editType == '1'){
		
	}else{
		// 查看状态下不允许编辑
		$("input[type=text]").attr("disabled", true);
		//$("input[type=radio]").attr("disabled", true);
		$("input[type=select]").attr("disabled", true);
		$("select").attr("disabled", true);
		$("input[type=button]").attr("disabled", true);
		//$("textarea").attr("disabled", true);
		
		$(".btnShow").css("display", "none");
	}
	
	// 添加物料信息
	$('#addMatBtn').click(function(){
		// 获取年月
		var ym = $('#fsctYear').val();
		if(ym == undefined || ym == null || ym == ''){
			layer.msg("请选择预测期间！");
			return;
		}
		
		layer.open({
			  type:2,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
			  title:"物料列表",
			  shadeClose : false,
			  shade : 0.1,
			  content : '/bam/ps/matSelectDialog',
			  area : [ '800px', '90%' ],
			  maxmin : true, // 开启最大化最小化按钮
			  btn: ['确认', '取消']
		  	  ,yes: function(index, layero){
		  		//按钮【按钮一】的回调
		  		  
		  		// 获取选中的物料数据
		  		var data = $(layero).find("iframe")[0].contentWindow
		        .getCheckedData();
		  		// 关闭弹框
		  		layer.close(index);
		  		// 处理数据
		  		if(data.length>0){
			  		dialogDataDeal(data);
		  		}
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
	   	var checkStatus = table.checkStatus('matTableID');//mat-table
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
	
	// 返回
	$("#backBtn").click(function(){
		 //window.history.go(-1);
		tuoBack('.saleFcstDetail','#searchBtn')
	});
	
	// 保存
	$("#saveBtn").click(function(){
		saveFn();
	});
	
	/*// 提交
	$("#submitBtn").click(function(){
		submitFn();
	});*/
	// 筛选查询
	$("#searchBtn").click(function(){
		
		layer.open({
			  type:2,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
			  title:"物料筛选",
			  shadeClose : false,
			  shade : 0.1,
			  content : '/bam/ps/matRepeat',
			  area : [ '500px', '200px' ],
			  maxmin : false, // 开启最大化最小化按钮
			  btn: ['确认','取消']
		  	  ,yes: function(index, layero){
		  		//按钮【按钮一】的回调
		  		var data = $(layero).find("iframe")[0].contentWindow.getData();
		  		// 关闭弹框
		  		layer.close(index);
		  		
		  		var matInfo = data.matInfo;
		  		matInfoCondition = matInfo;
		  		var series = data.series;
		  		seriesCondition = series;
		  		var bigItemExpl = data.bigItemExpl;
		  		bigItemExplCondition = bigItemExpl;
		  		var searchTbData = new Array();
		  		for(var i=0;i<tableData.length;i++){
	  				var item = tableData[i];
	  				
	  				var matFlag = false;
	  				var seriesFlag = false;
	  				var bigItemFlag = false;
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
	  				
	  				if(matFlag && seriesFlag && bigItemFlag){
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
		matInfoCondition='' ;
		seriesCondition='' ;
		bigItemExplCondition='';
	});
});

//定义日期格式
function laydateInit(laydate){
	  var laydate = layui.laydate;
	  // 调拨日期 区间
	  laydate.render({
	    elem: '#fsctYear'
	    ,type: 'month'
	    ,range: '~'
	    ,done: function(value, date, endDate){
	        //value 得到日期生成的值，如：2017-08-18
	        //date 得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
	        //endDate 得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员同上。
	    	if(value != '' && value != null && value != undefined){
	    		// 判断是否满足24个月
	    		var sYear = date.year;
	    		var sMonth = date.month;
	    		var eYear = endDate.year;
	    		var eMonth = endDate.month;
	    		if(sMonth == 1){
	    			if(eYear-sYear == 1 && eMonth == 12){
	    			}else{
	    				layer.msg("预测区间需要24个月！",{time: 2000});
	    				yearSel = false;
	    				return;
	    			}
	    		}else{
	    			if(eYear-sYear == 2 && sMonth-eMonth == 1){
	    			}else{
	    				layer.msg("预测区间需要24个月！",{time: 2000});
			    		yearSel = false;
			    		return;
	    			}
	    		}
	    		
		    	/*var ym = value.split('~');
		    	var date1 = ym[0].replace(/\s+/g,"");// 去除所有空格
		    	var date2 = ym[1].replace(/\s+/g,"");// 去除所有空格
		    	if(parseInt(date2,10)-parseInt(date1,10)!=1){
		    		layer.msg("请选择两年的年度区间！",{time: 2000});
		    		yearSel = false;
		    		return;
		    	}*/
		    	
		    	yearSel = true;
		    	$('#fsctName').val(value+"月份销售预测");
		    	// 设置表头
		    	dealColumnName(value);

				loadDetailTable(table,tableData);
	    	}
	     }
	  });
}

// 物料明细table
layui.use(['table','laydate'], function() {
	var $ = layui.$;
	table = layui.table;
	var laydate = layui.laydate;
	// 定义日期控件
	laydateInit(laydate);
	
	type=$('#type').val();
	id=$("#id").val();
	
	// 获取明细数据
	$.ajax({
		 type:"post",
		 url:prefix+"/getSaleFcstDetailList?mainId="+id,
		 dataType:"JSON",
		 success:function(data){
			 tableData=data;
			 // 设置列头
			 var year = $('#fsctYear').val();
			 dealColumnName(year);
			 loadDetailTable(table,tableData);
		 }
	});
	
	// table 数据刷新处理
	table.on('tool(dealChangeEvent)', function(obj){
	    var data = obj.data;
	    if(obj.event == 'setSign'){
	    	obj.update({sumSaleFore1:data.sumSaleFore1,
	    		sumSaleFore2:data.sumSaleFore2,
	    		sumSaleFore3:data.sumSaleFore3});
	    }
	});
	
	// table 单元格编辑监听
	table.on('edit(dealChangeEvent)', function(obj){
		//console.log(obj.value); //得到修改后的值
		//console.log(obj.field); //当前编辑的字段名
		//console.log(obj.data); //所在行的所有相关数据
		if(obj.field == 'saleForeQty1' || obj.field == 'saleForeQty2' || obj.field == 'saleForeQty3'|| obj.field == 'saleForeQty4'
			|| obj.field == 'saleForeQty5' || obj.field == 'saleForeQty6' || obj.field == 'saleForeQty7'|| obj.field == 'saleForeQty8'
				|| obj.field == 'saleForeQty9' || obj.field == 'saleForeQty10' || obj.field == 'saleForeQty11'|| obj.field == 'saleForeQty12'
			||obj.field == 'saleFore1' || obj.field == 'saleFore2' || obj.field == 'saleFore3'|| obj.field == 'saleFore4'
				|| obj.field == 'saleFore5' || obj.field == 'saleFore6' || obj.field == 'saleFore7'|| obj.field == 'saleFore8'
					|| obj.field == 'saleFore9' || obj.field == 'saleFore10' || obj.field == 'saleFore11'|| obj.field == 'saleFore12'){
			// 预测区间
			var year = $('#fsctYear').val();
			
			var ymArr = year.split('~');
			var sYm = ymArr[0].trim();
			var eYm = ymArr[1].trim();
			
			var sYmArr = sYm.split('-');
			var sYear = parseInt(sYmArr[0]);
			var sMonth = parseInt(sYmArr[1]);
			var eYmArr = eYm.split('-');
			var eYear = parseInt(eYmArr[0]);
			var eMonth = parseInt(eYmArr[1]);
			
			var saleFore1 = isNaN(obj.data.saleForeQty1)||obj.data.saleForeQty1==""?0:obj.data.saleForeQty1;
			var saleFore2 = isNaN(obj.data.saleForeQty2)||obj.data.saleForeQty2==""?0:obj.data.saleForeQty2;
			var saleFore3 = isNaN(obj.data.saleForeQty3)||obj.data.saleForeQty3==""?0:obj.data.saleForeQty3;
			var saleFore4 = isNaN(obj.data.saleForeQty4)||obj.data.saleForeQty4==""?0:obj.data.saleForeQty4;
			var saleFore5 = isNaN(obj.data.saleForeQty5)||obj.data.saleForeQty5==""?0:obj.data.saleForeQty5;
			var saleFore6 = isNaN(obj.data.saleForeQty6)||obj.data.saleForeQty6==""?0:obj.data.saleForeQty6;
			var saleFore7 = isNaN(obj.data.saleForeQty7)||obj.data.saleForeQty7==""?0:obj.data.saleForeQty7;
			var saleFore8 = isNaN(obj.data.saleForeQty8)||obj.data.saleForeQty8==""?0:obj.data.saleForeQty8;
			var saleFore9 = isNaN(obj.data.saleForeQty9)||obj.data.saleForeQty9==""?0:obj.data.saleForeQty9;
			var saleFore10 = isNaN(obj.data.saleForeQty10)||obj.data.saleForeQty10==""?0:obj.data.saleForeQty10;
			var saleFore11 = isNaN(obj.data.saleForeQty11)||obj.data.saleForeQty11==""?0:obj.data.saleForeQty11;
			var saleFore12 = isNaN(obj.data.saleForeQty12)||obj.data.saleForeQty12==""?0:obj.data.saleForeQty12;
			var saleFore13 = isNaN(obj.data.saleFore1)||obj.data.saleFore1==""?0:obj.data.saleFore1;
			var saleFore14 = isNaN(obj.data.saleFore2)||obj.data.saleFore2==""?0:obj.data.saleFore2;
			var saleFore15 = isNaN(obj.data.saleFore3)||obj.data.saleFore3==""?0:obj.data.saleFore3;
			var saleFore16 = isNaN(obj.data.saleFore4)||obj.data.saleFore4==""?0:obj.data.saleFore4;
			var saleFore17 = isNaN(obj.data.saleFore5)||obj.data.saleFore5==""?0:obj.data.saleFore5;
			var saleFore18 = isNaN(obj.data.saleFore6)||obj.data.saleFore6==""?0:obj.data.saleFore6;
			var saleFore19 = isNaN(obj.data.saleFore7)||obj.data.saleFore7==""?0:obj.data.saleFore7;
			var saleFore20 = isNaN(obj.data.saleFore8)||obj.data.saleFore8==""?0:obj.data.saleFore8;
			var saleFore21 = isNaN(obj.data.saleFore9)||obj.data.saleFore9==""?0:obj.data.saleFore9;
			var saleFore22 = isNaN(obj.data.saleFore10)||obj.data.saleFore10==""?0:obj.data.saleFore10;
			var saleFore23 = isNaN(obj.data.saleFore11)||obj.data.saleFore11==""?0:obj.data.saleFore11;
			var saleFore24 = isNaN(obj.data.saleFore12)||obj.data.saleFore12==""?0:obj.data.saleFore12;
			
			if(sMonth == 1){
				
				obj.data.sumSaleFore1 = (parseFloat(saleFore1)+parseFloat(saleFore2)+parseFloat(saleFore3)
				+parseFloat(saleFore4)+parseFloat(saleFore5)+parseFloat(saleFore6)+parseFloat(saleFore7)
				+parseFloat(saleFore8)+parseFloat(saleFore9)+parseFloat(saleFore10)+parseFloat(saleFore11)
				+parseFloat(saleFore12)).toFixed(2);
				
				obj.data.sumSaleFore2 = (parseFloat(saleFore13)+parseFloat(saleFore14)+parseFloat(saleFore15)
						+parseFloat(saleFore16)+parseFloat(saleFore17)+parseFloat(saleFore18)+parseFloat(saleFore19)
						+parseFloat(saleFore20)+parseFloat(saleFore21)+parseFloat(saleFore22)+parseFloat(saleFore23)
						+parseFloat(saleFore24)).toFixed(2);
				
				obj.data.sumSaleFore3 = 0;
			}else{
				var sum1=0;
				var sum2=0;
				var sum3=0;
				
				var saleForeArr = new Array(24);
				saleForeArr[0] = saleFore1;
				saleForeArr[1] = saleFore2;
				saleForeArr[2] = saleFore3;
				saleForeArr[3] = saleFore4;
				saleForeArr[4] = saleFore5;
				saleForeArr[5] = saleFore6;
				saleForeArr[6] = saleFore7;
				saleForeArr[7] = saleFore8;
				saleForeArr[8] = saleFore9;
				saleForeArr[9] = saleFore10;
				saleForeArr[10] = saleFore11;
				saleForeArr[11] = saleFore12;
				saleForeArr[12] = saleFore13;
				saleForeArr[13] = saleFore14;
				saleForeArr[14] = saleFore15;
				saleForeArr[15] = saleFore16;
				saleForeArr[16] = saleFore17;
				saleForeArr[17] = saleFore18;
				saleForeArr[18] = saleFore19;
				saleForeArr[19] = saleFore20;
				saleForeArr[20] = saleFore21;
				saleForeArr[21] = saleFore22;
				saleForeArr[22] = saleFore23;
				saleForeArr[23] = saleFore24;
				
				var ssi = 12-sMonth;
				for(var i=0;i<=ssi;i++){
					sum1 += parseFloat(saleForeArr[i]);
				}
				sum1 = sum1.toFixed(2);
				
				for(var i=1;i<=12;i++){
					sum2 += parseFloat(saleForeArr[ssi+i]);
				}
				sum2 = sum2.toFixed(2);
				
				for(var i=1;i<=eMonth;i++){
					sum3 += parseFloat(saleForeArr[ssi+12+i]);
				}
				sum3 = sum3.toFixed(2);
				
				obj.data.sumSaleFore1 = sum1;
				obj.data.sumSaleFore2 = sum2;
				obj.data.sumSaleFore3 = sum3;
			}
			
			//
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
	       {checkbox:true, fixed: 'left'}
		  //,{field:'sn', title: '序号',width:'70',event: 'setSign'}
		  ,{field:'rank', title: '排名',width:'70',edit: 'text', fixed: 'left'}
		  ,{field:'prodSeries', title: '产品系列',width:'82', fixed: 'left'}
		  ,{field:'bigItemExpl', title: '大品项',width:'100', fixed: 'left'}
		  ,{field:'matName', title: '物料名称',width:'220', fixed: 'left'}
	      ,{field:'saleForeQty1', title: Tq[0],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty2', title: Tq[1],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty3', title: Tq[2],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty4', title: Tq[3],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty5', title: Tq[4],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty6', title: Tq[5],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty7', title: Tq[6],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty8', title: Tq[7],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty9', title: Tq[8],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty10', title: Tq[9],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty11', title: Tq[10],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleForeQty12', title: Tq[11],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore1', title: Tq[12],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore2', title: Tq[13],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore3', title: Tq[14],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore4', title: Tq[15],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore5', title: Tq[16],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore6', title: Tq[17],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore7', title: Tq[18],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore8', title: Tq[19],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore9', title: Tq[20],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore10', title: Tq[21],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore11', title: Tq[22],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'saleFore12', title: Tq[23],width:'150',edit: 'text',event: 'setSign'}
	      ,{field:'sumSaleFore1', title: Tsum1,width:'120'}
	      ,{field:'sumSaleFore2', title: Tsum2,width:'120'}
	      ,{field:'sumSaleFore3', title: Tsum3,width:'120'}
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
		  var data = res.data;
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
		  var cell13 = 0;
		  var cell14 = 0;
		  var cell15 = 0;
		  var cell16 = 0;
		  var cell17 = 0;
		  var cell18 = 0;
		  var cell19 = 0;
		  var cell20 = 0;
		  var cell21 = 0;
		  var cell22 = 0;
		  var cell23 = 0;
		  var cell24 = 0;
		  var cell25 = 0;
		  var cell26 = 0;
		  var cell27 = 0;
		  if(data.length>0){
			  for(var i=0;i<data.length;i++){
				  var item = data[i];
				  
				  cell1 += (item.saleForeQty1==null||isNaN(item.saleForeQty1)||item.saleForeQty1=='')?0:parseFloat(item.saleForeQty1);
				  cell2 += (item.saleForeQty2==null||isNaN(item.saleForeQty2)||item.saleForeQty2=='')?0:parseFloat(item.saleForeQty2);
				  cell3 += (item.saleForeQty3==null||isNaN(item.saleForeQty3)||item.saleForeQty3=='')?0:parseFloat(item.saleForeQty3);
				  cell4 += (item.saleForeQty4==null||isNaN(item.saleForeQty4)||item.saleForeQty4=='')?0:parseFloat(item.saleForeQty4);
				  cell5 += (item.saleForeQty5==null||isNaN(item.saleForeQty5)||item.saleForeQty5=='')?0:parseFloat(item.saleForeQty5);
				  cell6 += (item.saleForeQty6==null||isNaN(item.saleForeQty6)||item.saleForeQty6=='')?0:parseFloat(item.saleForeQty6);
				  cell7 += (item.saleForeQty7==null||isNaN(item.saleForeQty7)||item.saleForeQty7=='')?0:parseFloat(item.saleForeQty7);
				  cell8 += (item.saleForeQty8==null||isNaN(item.saleForeQty8)||item.saleForeQty8=='')?0:parseFloat(item.saleForeQty8);
				  cell9 += (item.saleForeQty9==null||isNaN(item.saleForeQty9)||item.saleForeQty9=='')?0:parseFloat(item.saleForeQty9);
				  cell10 += (item.saleForeQty10==null||isNaN(item.saleForeQty10)||item.saleForeQty10=='')?0:parseFloat(item.saleForeQty10);
				  cell11 += (item.saleForeQty11==null||isNaN(item.saleForeQty11)||item.saleForeQty11=='')?0:parseFloat(item.saleForeQty11);
				  cell12 += (item.saleForeQty12==null||isNaN(item.saleForeQty12)||item.saleForeQty12=='')?0:parseFloat(item.saleForeQty12);
				  
				  cell13 += (item.saleFore1==null||isNaN(item.saleFore1)||item.saleFore1=='')?0:parseFloat(item.saleFore1);
				  cell14 += (item.saleFore2==null||isNaN(item.saleFore2)||item.saleFore2=='')?0:parseFloat(item.saleFore2);
				  cell15 += (item.saleFore3==null||isNaN(item.saleFore3)||item.saleFore3=='')?0:parseFloat(item.saleFore3);
				  cell16 += (item.saleFore4==null||isNaN(item.saleFore4)||item.saleFore4=='')?0:parseFloat(item.saleFore4);
				  cell17 += (item.saleFore5==null||isNaN(item.saleFore5)||item.saleFore5=='')?0:parseFloat(item.saleFore5);
				  cell18 += (item.saleFore6==null||isNaN(item.saleFore6)||item.saleFore6=='')?0:parseFloat(item.saleFore6);
				  cell19 += (item.saleFore7==null||isNaN(item.saleFore7)||item.saleFore7=='')?0:parseFloat(item.saleFore7);
				  cell20 += (item.saleFore8==null||isNaN(item.saleFore8)||item.saleFore8=='')?0:parseFloat(item.saleFore8);
				  cell21 += (item.saleFore9==null||isNaN(item.saleFore9)||item.saleFore9=='')?0:parseFloat(item.saleFore9);
				  cell22 += (item.saleFore10==null||isNaN(item.saleFore10)||item.saleFore10=='')?0:parseFloat(item.saleFore10);
				  cell23 += (item.saleFore11==null||isNaN(item.saleFore11)||item.saleFore11=='')?0:parseFloat(item.saleFore11);
				  cell24 += (item.saleFore12==null||isNaN(item.saleFore12)||item.saleFore12=='')?0:parseFloat(item.saleFore12);
				  
				  cell25 += (item.sumSaleFore1==null||isNaN(item.sumSaleFore1)||item.sumSaleFore1=='')?0:parseFloat(item.sumSaleFore1);
				  cell26 += (item.sumSaleFore2==null||isNaN(item.sumSaleFore2)||item.sumSaleFore2=='')?0:parseFloat(item.sumSaleFore2);
				  cell27 += (item.sumSaleFore3==null||isNaN(item.sumSaleFore3)||item.sumSaleFore3=='')?0:parseFloat(item.sumSaleFore3);
			  }
			  
			  var sumRow = '<tr  height="30" align="center" style="font-weight:bold"><td></td><td></td><td></td><td></td>'+
				  '<td>总计</td></tr>';
			  
			  var sumDetailRow = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
			  '<td></td>'+'<td></td>'+'<td></td>'+'<td></td>'+
			  '<td>'+cell1.toFixed(2)+'</td>'+
			  '<td>'+cell2.toFixed(2)+'</td>'+
			  '<td>'+cell3.toFixed(2)+'</td>'+
			  '<td>'+cell4.toFixed(2)+'</td>'+
			  '<td>'+cell5.toFixed(2)+'</td>'+
			  '<td>'+cell6.toFixed(2)+'</td>'+
			  '<td>'+cell7.toFixed(2)+'</td>'+
			  '<td>'+cell8.toFixed(2)+'</td>'+
			  '<td>'+cell9.toFixed(2)+'</td>'+
			  '<td>'+cell10.toFixed(2)+'</td>'+
			  '<td>'+cell11.toFixed(2)+'</td>'+
			  '<td>'+cell12.toFixed(2)+'</td>'+
			  '<td>'+cell13.toFixed(2)+'</td>'+
			  '<td>'+cell14.toFixed(2)+'</td>'+
			  '<td>'+cell15.toFixed(2)+'</td>'+
			  '<td>'+cell16.toFixed(2)+'</td>'+
			  '<td>'+cell17.toFixed(2)+'</td>'+
			  '<td>'+cell18.toFixed(2)+'</td>'+
			  '<td>'+cell19.toFixed(2)+'</td>'+
			  '<td>'+cell20.toFixed(2)+'</td>'+
			  '<td>'+cell21.toFixed(2)+'</td>'+
			  '<td>'+cell22.toFixed(2)+'</td>'+
			  '<td>'+cell23.toFixed(2)+'</td>'+
			  '<td>'+cell24.toFixed(2)+'</td>'+
			  '<td>'+cell25.toFixed(2)+'</td>'+
			  '<td>'+cell26.toFixed(2)+'</td>'+
			  '<td>'+cell27.toFixed(2)+'</td>'+
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

/**保存*/
function saveFn(){
	/*// 状态处理
	var status = $('#status').val();
	if(status == '' || status == undefined){
		// 创建情况下
		$('#status').val('已保存');
	}*/
	var year = $('#fsctYear').val();
	if(year == null || year ==undefined || year == ""){
		layer.msg("请选择预测年度！",{time: 2000});
		return;
	}
	
	if(!yearSel){
		layer.msg("请选择两年的年度区间！",{time: 2000});
		return;
	}
	
	// 数组转字符串存储
	$('#saleFcstDetailData').val(JSON.stringify(tableData));
	// 启动加载层
	var loadLayer = layer.load(0, {shade: false});
	var options = {
			url: prefix+"/saveSaleFcstInfo",
			type:'POST',
			success: function (msg) {
				if(msg.code=="0"){
					//$('#planCode').val(msg.msg);
					layer.msg("保存成功！",{time: 1000});
					// 重新加载明细数据
					$.ajax({
						 type:"post",
						 url:prefix+"/getSaleFcstDetailList?mainId="+id,
						 dataType:"JSON",
						 success:function(data){
							 tableData=data;
							 // 设置列头
							 var year = $('#fsctYear').val();
							 dealColumnName(year);
							 loadDetailTable(table,tableData);
							 //重新加载筛选数据
							 searchMates();
						 }
					});
				}else if(msg.code=="90" || msg.code==90){
					//$('#status').val('');
					layer.msg(msg.msg,{time: 2000});
				}else{
					//$('#status').val('');
					layer.msg("保存失败！请检查物料编码是否重复，数量的格式是否正确！"+msg.msg,{time: 1000});
				}
				// 关闭加载层
				layer.close(loadLayer);
			},
			error: function(request) {
				//$('#status').val('');
				layer.msg("保存异常！",{time: 1000});
				// 关闭加载层
				layer.close(loadLayer);
			}
	};
	$("#submitForm").ajaxSubmit(options);
}

// 提交，【没有提交】
function submitFn(){
	
}

/**
 * 物料弹出框选择数据处理
 * @param data
 * @returns
 */
function dialogDataDeal(data){
	
	// 启动加载层
	var loadLayer = layer.load(0, {shade: false});
	
	// 主表ID
	var mainId = $('#id').val();
	// 获取最大序号
	var dataLenth = tableData.length;
	var lastNum = 0;
	if(dataLenth > 0){
		lastNum = tableData[dataLenth-1].sn;
	}
	
	var matData = [];
	for(var i=0;i<data.length;i++){
		var item  = data[i];
		var obj = {
				"matCode":item.mateCode,
				"matName":item.mateName,
				"prodSeriesCode":item.seriesCode,
				"prodSeries":item.seriesExpl,
				"bigItemCode":item.bigItemCode,
				"bigItemExpl":item.bigItemExpl
				};
		
		matData.push(obj);
	}
	// 对json字符串数据进行编码  // unescape 解码方法 ，escape 编码方法
	var matInfoData =JSON.stringify(matData);// escape();
	
	var ym = $('#fsctYear').val();
	var ymArr = ym.split('~');
	// 年份
	var sYm = ymArr[0];
	// 
	var eYm = ymArr[1];
	
	// 获取物料的其他信息
	$.ajax({
		 type:"post",
		 url:prefix+"/getSaleFcstDetailByCxjh",
		 dataType:"JSON",
		 data:{"matInfoData":matInfoData,"sYm":sYm,"eYm":eYm},
		 success:function(data){
			 if(data.length>0){
				 var repeatItem = "";
					// 循环
					for(var i=0;i<data.length;i++){
						var count=0;
						
						for(var j=0;j<tableData.length;j++){
							if(data[i].matCode == tableData[j].matCode){
								count++;
								
								repeatItem += data[i].matCode+","+data[i].matName+";<br>";
								break;
							}
						}
						
						if(count == 0){
							lastNum = lastNum+1;
							//创建table的行，赋值，添加到table中
							var arr = {
								mainId:mainId,
								sn : lastNum,
								rank:"",
								matCode : data[i].matCode,
								matName : data[i].matName,
								prodSeriesCode:data[i].prodSeriesCode,
								prodSeries:data[i].prodSeries,
								bigItemCode:data[i].bigItemCode,
								bigItemExpl:data[i].bigItemExpl,
								saleForeQty1:data[i].saleForeQty1,
								saleForeQty2:data[i].saleForeQty2,
								saleForeQty3:data[i].saleForeQty3,
								saleForeQty4:data[i].saleForeQty4,
								saleForeQty5:data[i].saleForeQty5,
								saleForeQty6:data[i].saleForeQty6,
								saleForeQty7:data[i].saleForeQty7,
								saleForeQty8:data[i].saleForeQty8,
								saleForeQty9:data[i].saleForeQty9,
								saleForeQty10:data[i].saleForeQty10,
								saleForeQty11:data[i].saleForeQty11,
								saleForeQty12:data[i].saleForeQty12,
								saleFore1:data[i].saleFore1,
								saleFore2:data[i].saleFore2,
								saleFore3:data[i].saleFore3,
								saleFore4:data[i].saleFore4,
								saleFore5:data[i].saleFore5,
								saleFore6:data[i].saleFore6,
								saleFore7:data[i].saleFore7,
								saleFore8:data[i].saleFore8,
								saleFore9:data[i].saleFore9,
								saleFore10:data[i].saleFore10,
								saleFore11:data[i].saleFore11,
								saleFore12:data[i].saleFore12,
								sumSaleFore1:data[i].sumSaleFore1,
								sumSaleFore2:data[i].sumSaleFore2,
								sumSaleFore3:data[i].sumSaleFore3
							};
							tableData.push(arr);
						}
					}
					// 加载数据
					loadDetailTable(table, tableData);
					
					// 提示重复项
					if(repeatItem != ''){
						layer.msg(repeatItem+"这些物料已存在，无法重复添加！", {
					        time: 20000, //20s后自动关闭
					        btn: ['关闭']
					    });
					}
			 }
			// 关闭加载层
			layer.close(loadLayer);
		 },
		 error: function (XMLHttpRequest, textStatus, errorThrown) {
			 // 关闭加载层
			 layer.close(loadLayer);
	     }
	});
}
//重新加载筛选数据
function searchMates(){
	var searchTbData = new Array();
	for(var i=0;i<tableData.length;i++){
		var item = tableData[i];
		
		var matFlag = false;
		var seriesFlag = false;
		var bigItemFlag = false;
		if(matInfoCondition != ''){
			if(item.matCode != undefined
					&& item.matName != undefined
					&& (item.matCode.toUpperCase().indexOf(matInfoCondition.toUpperCase())>=0 || item.matName.toUpperCase().indexOf(matInfoCondition.toUpperCase())>=0)){
				matFlag = true;
			}
		}else{
			matFlag = true;
		}
		
		if(seriesCondition != ''){
			if(item.prodSeries != undefined
					&& item.prodSeries.toUpperCase().indexOf(seriesCondition.toUpperCase())>=0){
				seriesFlag = true;
			}
		}else{
			seriesFlag = true;
		}
		if(bigItemExplCondition != ''){
			if(item.bigItemExpl != undefined
					&& item.bigItemExpl.toUpperCase().indexOf(bigItemExplCondition.toUpperCase())>=0){
				bigItemFlag = true;
			}
		}else{
			bigItemFlag=true;
		}
		
		if(matFlag && seriesFlag && bigItemFlag){
			searchTbData.push(item);
		}
	}
	// 重新加载数据
	loadDetailTable(table, searchTbData);
}
