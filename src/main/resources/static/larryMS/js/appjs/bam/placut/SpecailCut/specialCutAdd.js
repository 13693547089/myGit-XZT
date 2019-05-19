var table;
var str;
var tableData = [];
var cutLiaiMate;
var fieldsData = [];
var nowRowData
var nowField;
var nextField;
var totalField;
var differenceField;
var origNum;
layui.use([ 'table', 'laydate','form' ], function() {
	table = layui.table;
	var $ = layui.$;
	var form = layui.form;
	var laydate = layui.laydate;
	var type = $("#type").val();
	var liaiId = $("#liaiId").val();
	// 返回
	$("#goBack").click(function() {
		//window.history.back(-1);
		tuoBack('.specialCutAdd','#search');
	}); 
	// 年月选择器
	laydate.render({
		elem : '#cutMonth',
		type : 'month',
		done : function(value, date, endDate) {
			var oldPlanMonth = $("#cutMonth").val();
			if (oldPlanMonth != value) {
				getCutLiaiMate(value);
			}
		}
	});
	//导出
	  $("#ExportBut").click(function(){
		  layer.confirm('确定要导出当前打切联络单吗？', function(index){
			  location="/exportCutLiaison?liaiId="+liaiId;
			  layer.close(index);
		  });
	  });
	if("1" == type){//新建
		// 获取字段
		$.ajax({
			type : "post",
			url : "/queryFields",
			dataType : "JSON",
			async : false,// 注意
			success : function(data) {
				debugger;
				str = data;
				initCutLiaisonTable();
			},
			error : function() {
				layer.alert("Connection error");
			}
		})
		
	}else{ //编辑和查看
		$("#cutMonth").attr("disabled",true);
		// 获取字段
		$.ajax({
			type : "post",
			url : "/queryLiaiMateFields?liaiId="+liaiId,
			dataType : "JSON",
			async : false,// 注意
			success : function(data) {
				str = data;
			},
			error : function() {
				layer.alert("Connection error");
			}
		});
		//获取物料数据
		$.ajax({
			type : "post",
			url : "/getData?id="+liaiId,
			dataType : "JSON",
			async : false,// 注意
			success : function(data) {
				tableData = data.data;
			},
			error : function() {
				layer.alert("Connection error");
			}
		});
	}
	initCutLiaisonTable();
	
	function getCutLiaiMate(cutMonth){
		tableData=[];
		// 获取物料数据
		$.ajax({
			type : "post",
			url : "/querySpecialCutLiaiMate?cutMonth="+cutMonth,
			dataType : "JSON",
			async : false,// 注意
			success : function(data) {
				if(data.judge){
					$.each(data.list, function(index, item) {
						var arr = {
								sumOutNum : item.sumOutNum ==undefined ?0: item.sumOutNum,
								sumInveNum : item.sumInveNum ==undefined ?0: item.sumInveNum,
								sumProdNum : item.sumProdNum ==undefined ?0: item.sumProdNum,
								mainStru : item.mainStru,
								cutAim : item.cutAim,
								version : item.version,
								boxNumber :item.boxNumber,
								seriesExpl :item.seriesExpl,
								lastProdNum : item.lastProdNum,
								mateCode : item.mateCode,
								mateName : item.mateName
						}
						tableData.push(arr);
					});
				}else{
					layer.alert(data.msg);
				}
			},
			error : function() {
				layer.alert("Connection error");
			}
		});
		initCutLiaisonTable();
	}
	// table 数据刷新处理
	table.on('tool(cutLiaiMateTable)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'setSign'){
	    	//debugger;
	    	var upd={};
	    	//prodNum 作为upd对象的属性
	    	upd.prodNum=data.prodNum;
	    	//var nextField = B302 ，nextField的所代表的值B302 作为upd对象的属性，
	    	//data[nextField] ：表示获取data对象B302属性的值
	    	upd[nextField]=data[nextField];
	    	upd[totalField]=data[totalField];
	    	upd[differenceField]= data[differenceField];
	    	$.each(str,function(index,item){
	    		// debugger;
	    		for(var e in item){
	    			if(e == 'field'){
	    				var v = item[e];//获取合计字段名
	    				if(v.indexOf("C") == 0){
	    					var diffField = "D"+v.substring(1);
	    					upd[diffField] = data[diffField];
	    				}
	    			}
	    		}
	    	});
	    	obj.update(upd);
	    	nextField="";
	    	totalField="";
	    	differenceField="";
	    }
	});
	
	//填写table数据计算结果
	table.on('edit(cutLiaiMateTable)', function(obj){ //注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"
		debugger;   
		   nowRowData = obj.data;
		   nowField =obj.field;//A302
		   if('remark'==nowField){
			   return;
		   }
		   var content = nowField.indexOf("M") != -1 ? nowField.substring(1,nowField.indexOf("M")) : nowField.substring(1);///////////////
		   var k = parseInt(parseInt(content)/100);
		   totalField= nowField.indexOf("M") != -1 ? "C"+(k*100+1)+"MASTER" : "C"+(k*100+1);//C301
		   differenceField = "D"+totalField.substring(1);
		   var total = nowRowData[totalField];
		   if(isNaN(total)){total=0;}
		   if(total !=0){
			   var oldData = parseInt(total)-parseInt(nowRowData[nowField]);
		   }
		   nextField = "B"+nowField.substring(1);//B302
		   var nowValue =obj.value;
		   if(nowValue == ""){
			   nowValue = 0;
		   }
		   //debugger;
		   if(isNaN(nowValue)){
			   layer.alert("<span style='color:red;font-size: 17px;'>请输入有效数字!</span>");
			  /* nowRowData[obj.field] = 0;
			   initCutLiaisonTable(table, tableData, str);*/
		   }
		   if(parseFloat(nowValue)<0){
			   layer.alert("<span style='color:red;font-size: 17px;'>请输入大于等于有效数字!</span>");
			   /* nowRowData[obj.field] = 0;
			   initCutLiaisonTable(table, tableData, str);*/
		   }
		   if('outNum' == obj.field){
			   debugger;
			   var outNum=nowRowData.outNum;
			   if(outNum == undefined || outNum==""){
				   outNum =0
			   }
			   if(isNaN(outNum)){
				   outNum= 0;
				   nowRowData.outNum = 0;
				   initCutLiaisonTable();
			   }
			   var ivn = nowRowData.inveNum;
			   if(ivn == undefined || ivn==""){
				   ivn =0
			   }
			   if(isNaN(ivn)){
				   ivn=0;
				   nowRowData.inveNum = 0;
				   initCutLiaisonTable();
			   }
			   var sumOutNum = parseInt(nowRowData.sumOutNum);
			   if(parseInt(outNum) > sumOutNum){
				   /* outNum= 0;
				   nowRowData.outNum = 0;
				   initCutLiaisonTable();*/
				   layer.msg("<span style='font-size: 17px;'>打切订单在外量大于总订单在外量</span>");
			   }
			   /*var sumInveNum = parseInt(nowRowData.sumInveNum);
			   if(parseInt(ivn) > sumInveNum){
				   ivn=0;
				   nowRowData.inveNum = 0;
				   initCutLiaisonTable();
				   layer.msg("<span style='font-size: 17px;'>打切成品库存大于总成品库存</span>");
			   }*/
			   var prodNum =parseInt(outNum)-parseInt(ivn);
			   if(prodNum<0){
				   prodNum=0;
			   }
			   //获取xxx合计的值，
			   $.each(str,function(index,item){
				  // debugger;
				   for(var e in item){
					   if(e == 'field'){
						   var v = item[e];//获取合计字段名
						   if(v.indexOf("C") == 0){
							   var diffField = "D"+v.substring(1);
							   //获取合计的值
							   var totalValue = parseFloat(nowRowData[v] == undefined ? 0:nowRowData[v]);
							   //xxx差异的值 = 本月打切可生产订单（prodNum） - xxx合计的值；
							   var diffValue = (parseFloat(prodNum) - totalValue).toFixed(2);
							   if(diffValue < 0){
								   diffValue = "<span style='color:red;'>"+diffValue+"</span>"
							   }
							   nowRowData[diffField] = diffValue;
						   }
					   }
				   }
			   });
			   
			   var lastProdNum = parseInt(nowRowData.lastProdNum);
			   if(prodNum > lastProdNum){
				   prodNum="<span style='color:red;'>"+prodNum+"</span>";
			   }
			   nowRowData.prodNum = prodNum;
			   //initCutLiaisonTable();
			   
		   }else if(obj.field == 'inveNum'){
			   var outNum=nowRowData.outNum;
			   if(outNum == undefined || outNum==""){
				   outNum =0
			   }
			   if(isNaN(outNum)){
				   outNum= 0;
				   nowRowData.outNum = 0;
				   initCutLiaisonTable();
			   }
			   var ivn = nowRowData.inveNum;
			   if(ivn == undefined ||ivn == ""){
				   ivn =0
			   }
			   if(isNaN(ivn)){
				   ivn=0;
				   nowRowData.inveNum = 0;
				   initCutLiaisonTable();
			   }
			  /* var sumOutNum = parseInt(nowRowData.sumOutNum);
			   if(parseInt(outNum) > sumOutNum){
				    outNum= 0;
				   nowRowData.outNum = 0;
				   initCutLiaisonTable();
				   layer.msg("<span style='font-size: 17px;'>打切订单在外量大于总订单在外量</span>");
			   }*/
			   var sumInveNum = parseInt(nowRowData.sumInveNum);
			   if(parseInt(ivn) > sumInveNum){
				   /*ivn=0;
				   nowRowData.inveNum = 0;
				   initCutLiaisonTable();*/
				   layer.msg("<span style='font-size: 17px;'>打切成品库存大于总成品库存</span>");
			   }
			   var prodNum =parseInt(outNum)-parseInt(ivn);
			   if(prodNum<0){
				   prodNum=0;
			   }
			 //获取xxx合计的值，
			   $.each(str,function(index,item){
				  // debugger;
				   for(var e in item){
					   if(e == 'field'){
						   var v = item[e];//获取合计字段名
						   if(v.indexOf("C") == 0){
							   var diffField = "D"+v.substring(1);
							   //获取合计的值
							   var totalValue = parseFloat(nowRowData[v] == undefined ? 0:nowRowData[v]);
							   //xxx差异的值 = 本月打切可生产订单（prodNum） - xxx合计的值；
							   var diffValue = (parseFloat(prodNum) - totalValue).toFixed(2);
							   if(diffValue < 0){
								   diffValue = "<span style='color:red;'>"+diffValue+"</span>"
							   }
							   nowRowData[diffField] = diffValue;
						   }
					   }
				   }
			   });
			   var lastProdNum = parseInt(nowRowData.lastProdNum);
			   if(prodNum > lastProdNum){
				   prodNum="<span style='color:red;'>"+prodNum+"</span>";
			   }
			   nowRowData.prodNum=prodNum;
		   }else{
			   var boxNumber = nowRowData.boxNumber;
			   if(boxNumber==0 || boxNumber==null){
				   layer.msg("箱入数为0")
			   }else{
				   //获取本月打切可生产订单
				   var prodNum = nowRowData.prodNum+"";
				   if(prodNum == "undefined"){
					   prodNum = 0;
					}else{
						if(prodNum.indexOf("<") != -1){
							var index1 = find(prodNum,'>',0);
							var index2 = find(prodNum,'<',1);
							var prodNumValue = prodNum.substring(index1+1,index2);
							prodNum = prodNumValue;
						}
					}
				   prodNum = parseFloat(prodNum);
				   var value =nowField.indexOf("M") != -1 ? parseFloat(nowValue)/parseFloat(1) : parseFloat(nowValue)/parseFloat(boxNumber);
				   var value2 = value.toFixed(2);
				   nowRowData[nextField]=value2;
				   if(total==0){
					   nowRowData[totalField] =nowRowData[nextField]+total;
				   }
				   var e=0;
				   for ( var r in nowRowData) {
					   if(r.substring(0,1)=='B'){
						   var a = nowField.indexOf("M") != -1 ? parseFloat(r.substring(1,nowField.indexOf("M"))) : parseFloat(r.substring(1));///////////////
						   var b = ((k+1)*100);
						   var c = ((k*100));
						   if(a<b && a>c){
							   var d = nowRowData[r];
							   if(d==null || d == ""){
								   d=0;
							   }
							   e=parseFloat(e)+parseFloat(d);
						   }
					   } 
				   }
				   //合计
				   nowRowData[totalField]=e.toFixed(2);
				   //差异 = 本月可生产订单 - 合计
				   var diffValue = (prodNum - e.toFixed(2)).toFixed(2);
				   if(diffValue < 0){
					   diffValue = "<span style='color:red;'>"+diffValue+"</span>"
				   }
				   nowRowData[differenceField] = diffValue;
			   }
		   }
		   $(this).click();
		 });
	
	//保存        -----注意
	$("#SaveBut").click(function() {
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		}
		 var length =tableData.length;
		 if(length == 0){
			 layer.msg("物料信息不能为空");
			 return;
		 }
		 var result = getResultData();
		var type = $("#type").val();
		if(type == "1"){
			var liaiId = guid();
			$("#liaiId").val(liaiId);
		}
		$("#cutLiaiMateData").val(JSON.stringify(result));
		$("#fields").val(JSON.stringify(str));
		 var formData = $("#cutLiaisonForm").serialize();
		 debugger;
		 $.ajax({
			 type : "POST",
			 url : "/addSpeCutLiaison",
			 data : formData,
			 dataType:"JSON",
			 async: false,
			 error : function(request) {
				 layer.alert("Connection error");
			 },
			 success : function(data) {
				 if(data){
					 $("#Submit").attr("disabled","disabled");
					 $("#SaveBut").attr("disabled","disabled");
					 layer.msg("打切联络单保存成功");
					 //window.history.back(-1);
					 tuoBack('.specialCutAdd','#search');
				 }else{
					 layer.alert("打切联络单保存失败");
				 }
			 }
		 });

	});
	function getResultData(){
		var result = [];
		for (var i = 0; i < tableData.length; i++) {
			debugger;
			var elem = tableData[i];
			var r = {};
			var ss = [];
			for ( var e in elem) {
				if (e == 'mateCode' || e == 'mateName'|| e == 'sumOutNum'
					|| e == 'sumInveNum'|| e == 'sumProdNum' 
						|| e == 'outNum' || e == 'inveNum' 
							 || e == 'boxNumber' || e == 'lastProdNum'
								|| e == 'version' || e == 'cutAim'
									|| e == 'mainStru' || e=='seriesExpl'
										|| e== 'remark') {
					r[e] = elem[e];
				}else if(e == 'LAY_TABLE_INDEX'){
					
				}else if(e == 'prodNum'){
					debugger;
					var strv = elem[e]+"";
					if(strv == "undefined" || strv == undefined){
						r[e] = elem[e];
					}else{
						if(strv.indexOf("<") != -1){
							var index1 = find(strv,'>',0);
							var index2 = find(strv,'<',1);
							var prodNum = strv.substring(index1+1,index2);
							r[e] = prodNum;
						}else{
							r[e] = elem[e];
						}
					}
				}else{
					var q = {};
					q[e] = elem[e];
					ss.push(q);
				}
			}
			r.fields = JSON.stringify(ss);

			result.push(r);
		}
		return result;
	}
	function find(str,cha,num){
	    var x=str.indexOf(cha);
	    for(var i=0;i<num;i++){
	        x=str.indexOf(cha,x+1);
	    }
	    return x;
	}
	//提交       -----注意
	$("#Submit").click(function() {
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		 var length =tableData.length;
		 if(length == 0){
			 layer.msg("物料信息不能为空");
			 return;
		 }
		 var result = getResultData();
		var type = $("#type").val();
		if(type == "1"){
			var liaiId = guid();
			$("#liaiId").val(liaiId);
		}
		$("#cutLiaiMateData").val(JSON.stringify(result));
		$("#fields").val(JSON.stringify(str));
		var formData = $("#cutLiaisonForm").serialize();
		 $.ajax({
			 type : "POST",
			 url : "/commitSpeCutLiaison",
			 data : formData,
			 dataType:"JSON",
			 async: false,
			 error : function(request) {
				 layer.alert("Connection error");
			 },
			 success : function(data) {
				 if(data){
					 $("#Submit").attr("disabled","disabled");
					 $("#SaveBut").attr("disabled","disabled");
					 layer.msg("打切联络单提交成功");
					 //window.history.back(-1);
					 tuoBack('.specialCutAdd','#search');
				 }else{
					 layer.alert("打切联络单提交失败");
				 }
			 }
		 });
		
	});
	
	//更新
	$("#updateBut").click(function(){
		var cutMonth = $("#cutMonth").val();
		var liaiId = $("#liaiId").val();
		debugger;
		$.ajax({
			type:"post",
			url:"/updateSpeCutLiaiMateByMonth",
			data:{
				cutMonth : cutMonth,
				liaiId : liaiId
			},
			dataType:"JSON",
			error:function(result){
				layer.msg('程序出错', {time:2000 });
			},
			success:function(data){
				debugger;
				if(data.judge){
					tableData = data.data;
					initCutLiaisonTable();
					layer.msg("更新成功");
				}else{
					layer.alert(data.msg);
				}
			}
		});
	});
	
});

// 初始化table
function initCutLiaisonTable() {
	table.render({
		elem : "#cutLiaiMateTable",
		data : tableData,
		page : true,
		width : '100%',
		minHeight : '20px',
		id : "cutLiaiMateTableId",
		cols : [ str ]
	})
	var type = $("#type").val();
	if(type == '3'){
		$('table td').removeAttr('data-edit');
	}
}