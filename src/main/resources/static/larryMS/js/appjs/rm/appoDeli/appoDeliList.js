var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initAppoTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  //预约相关
	  laydate.render({
		    elem: '#appoStartDate', //指定元素
	  });
	  laydate.render({
		    elem: '#appoEndDate', //指定元素
	  });
	  laydate.render({
		  elem: '#appoCreStartDate', //指定元素
	  });
	  laydate.render({
		  elem: '#appoCreEndDate', //指定元素
	  });
	  //送货相关
	  laydate.render({
		  elem: '#deliStartDate', //指定元素
	  });
	  laydate.render({
		  elem: '#deliEndDate', //指定元素
	  });
	  laydate.render({
		  elem: '#deliCreStartDate', //指定元素
	  });
	  laydate.render({
		  elem: '#deliCreEndDate', //指定元素
	  });
	  //收货相关
	  laydate.render({
		  elem: '#receStartDate', //指定元素
	  });
	  laydate.render({
		  elem: '#receEndDate', //指定元素
	  });
	  laydate.render({
		  elem: '#receCreStartDate', //指定元素
	  });
	  laydate.render({
		  elem: '#receCreEndDate', //指定元素
	  });
	  //监听工具条
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
	    	var url  ="/getAppointAddHtml?appoId="+data.appoId+"&type=3";
    		//location=url;
	    	tuoGo(url,'appointAdd',"appointId");
	    } else if(obj.event === 'del'){//删除
	       if(data.appoStatus=='已保存'){
		      layer.confirm('真的删除这个预约申请吗？', function(index){
		    	  var appoIds =[];
		    	  appoIds.push(data.appoId);
		    	  $.ajax({
		    		 type:"post",
		    		 url:"/deleteAppointByAppoId",
		    		 data:"appoIds="+appoIds,
		    		 dataType:"JSON",
		    		 success:function(data2){
		    			 if(data2){
		    				 layer.msg('删除成功', {time:2000 });
		    				 initAppoTable();
		    			 }else{
		    				 layer.alert('<span style="color:red;">删除失败</sapn>');
		    			 }
		    		 }
		    	  });
		    	  layer.close(index);
		      });
	       }else{
	    	   layer.alert('<span style="color:red;">只有"已保存"状态的预约申请才能被删除</sapn>');
	       }
	    } else if(obj.event === 'edit'){//编辑
	    	if(data.appoStatus=="已保存" || data.appoStatus=="已拒绝"){
	    		var url  ="/getAppointAddHtml?appoId="+data.appoId+"&type=2";
	    		//location=url;
	    		tuoGo(url,'appointAdd',"appointId");
	    	}else{
	    		layer.alert('<span style="color:red;">只有"已保存"状态的预约申请可以编辑</sapn>');
	    	}
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      
			      var suppName = $('#suppName');
			      var suppRange = $('#suppRange');
			      var appoType = $("#appoType");
			      var requCode = $("#requCode");
			      var appoCode = $("#appoCode");
			      var appoStartDate = $("#appoStartDate");
			      var appoEndDate = $("#appoEndDate");
			      //var createDate = $("#createDate");
			      var appoCreStartDate = $("#appoCreStartDate");
			      var appoCreEndDate = $("#appoCreEndDate");
			      var deliCode = $("#deliCode");
			      var deliStartDate = $("#deliStartDate");
			      var deliEndDate = $("#deliEndDate");
			      var deliCreStartDate = $("#deliCreStartDate");
			      var deliCreEndDate = $("#deliCreEndDate");
			      //var deliCreateDate = $("#deliCreateDate");
			      var receCode = $("#receCode");
			      var receStartDate = $("#receStartDate");
			      var receEndDate = $("#receEndDate");
			      var receCreStartDate = $("#receCreStartDate");
			      var receCreEndDate = $("#receCreEndDate");
			      //var receCreateDate = $("#receCreateDate");
			      var mateName = $("#mateName");
			      var orderId = $("#orderId");
			      var inboDeliCode = $("#inboDeliCode");
			      var receUnit = $("#receUnit");
			      var isOccupy = $("#isOccupy").val();
			      if(isOccupy != '' && isOccupy != undefined){
			    	  if(isOccupy =='是'){
			    		  isOccupy ='yes';
			    	  }else if(isOccupy == '否'){
			    		  isOccupy = 'no';
			    	  }
			      }
			       var checkedStatusCode=[];
				    $(".checked").each(function(){
				    	var status=$(this).attr("name");
				    	checkedStatusCode.push(status);
				    });
				    var statusJson=JSON.stringify(checkedStatusCode);
				    var checkedStatus=[];
				    $(".checked2").each(function(){
				    	var status=$(this).attr("name");
				    	checkedStatus.push(status);
				    });
				    var checkedStatusJson=JSON.stringify(checkedStatus);
				    var checkedAppoStatus=[];
				    $(".checked3").each(function(){
				    	var status=$(this).attr("name");
				    	checkedAppoStatus.push(status);
				    });
				    var checkedAppoStatusJson=JSON.stringify(checkedAppoStatus);
			      //执行重载
			      table.reload('appoDeliId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  suppName: suppName.val(),
			        	  suppRange: suppRange.val(),
			        	  appoType:appoType.val(),
			        	  requCode:requCode.val(),
			        	  appoCode:appoCode.val(),
			        	  appoStartDate:appoStartDate.val(),
			        	  appoEndDate:appoEndDate.val(),
			        	  appoCreStartDate:appoCreStartDate.val(),
			        	  appoCreEndDate:appoCreEndDate.val(),
			        	  deliCode:deliCode.val(),
			        	  deliStartDate:deliStartDate.val(),
			        	  deliEndDate:deliEndDate.val(),
			        	  deliCreStartDate:deliCreStartDate.val(),
			        	  deliCreEndDate:deliCreEndDate.val(),
			        	  receCode:receCode.val(),
			        	  receStartDate:receStartDate.val(),
			        	  receEndDate:receEndDate.val(),
			        	  receCreStartDate:receCreStartDate.val(),
			        	  receCreEndDate:receCreEndDate.val(),
			        	  mateName:mateName.val(),
			        	  orderId:orderId.val(),
			        	  inboDeliCode:inboDeliCode.val(),
			        	  isOccupy:isOccupy,
			        	  receUnit:receUnit.val(),
			        	  statusJson:statusJson,
			        	  checkedStatusJson:checkedStatusJson,
			        	  checkedAppoStatusJson:checkedAppoStatusJson
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  

	  //重置
	  $("#reset").click(function(){
		  $(".checked").each(function(index,row){
				$(this).removeClass("checked");
				$(this).addClass("uncheck");
				$(this).css('color','gray');
		  });
		  $(".checked2").each(function(index,row){
			  $(this).removeClass("checked2");
			  $(this).addClass("uncheck2");
			  $(this).css('color','gray');
		  });
		  $(".checked3").each(function(index,row){
			  $(this).removeClass("checked3");
			  $(this).addClass("uncheck3");
			  $(this).css('color','gray');
		  });
	  });
	 //导出
	  $('#exportAppo').click(function(){
		  var suppName = $('#suppName').val();
		  var suppRange = $('#suppRange').val();
	      var appoType = $("#appoType").val();
	      var requCode = $("#requCode").val();
	      var appoCode = $("#appoCode").val();
	      var appoStartDate = $("#appoStartDate").val();
	      var appoEndDate = $("#appoEndDate").val();
	      var appoCreStartDate = $("#appoCreStartDate").val();
	      var appoCreEndDate = $("#appoCreEndDate").val();
	      var deliCode = $("#deliCode").val();
	      var deliStartDate = $("#deliStartDate").val();
	      var deliEndDate = $("#deliEndDate").val();
	      var deliCreStartDate = $("#deliCreStartDate").val();
	      var deliCreEndDate = $("#deliCreEndDate").val();
	      var receCode = $("#receCode").val();
	      var receStartDate = $("#receStartDate").val();
	      var receEndDate = $("#receEndDate").val();
	      var receCreStartDate = $("#receCreStartDate").val();
	      var receCreEndDate = $("#receCreEndDate").val();
	      var mateName = $("#mateName").val();
	      var orderId = $("#orderId").val();
	      var inboDeliCode = $("#inboDeliCode").val();
	      var isOccupy = $("#isOccupy").val();
	      var receUnit = $("#receUnit").val();
	      if(isOccupy != '' && isOccupy != undefined){
	    	  if(isOccupy =='是'){
	    		  isOccupy ='yes';
	    	  }else if(isOccupy == '否'){
	    		  isOccupy = 'no';
	    	  }
	      }
	      
	       var checkedStatusCode=[];
		    $(".checked").each(function(){
		    	var status=$(this).attr("name");
		    	checkedStatusCode.push(status);
		    });
		  //送货单状态
		    var statusJson=JSON.stringify(checkedStatusCode);
		    var checkedStatus=[];
		    $(".checked2").each(function(){
		    	var status=$(this).attr("name");
		    	checkedStatus.push(status);
		    });
		  //收货单状态
		    var checkedStatusJson=JSON.stringify(checkedStatus);
		    var checkedAppoStatus=[];
		    $(".checked3").each(function(){
		    	var status=$(this).attr("name");
		    	checkedAppoStatus.push(status);
		    });
		  //预约单状态
		    var checkedAppoStatusJson=JSON.stringify(checkedAppoStatus);
		     if(suppName=='' && suppRange=='' && appoType=='' && requCode=='' && appoCode=='' && 
		    		 appoStartDate== '' && appoEndDate == '' && appoCreStartDate == '' &&
		    		 appoCreEndDate == '' && deliCode=='' && deliStartDate== '' && deliEndDate=='' &&
		    		 deliCreStartDate == '' && deliCreEndDate == '' && receCode == '' && receStartDate=='' &&
		    		 receEndDate == '' && receCreStartDate== '' &&  receCreEndDate == '' && mateName == '' &&
		    		 orderId == '' && inboDeliCode == '' && isOccupy == '' && receUnit == '' && checkedStatusCode.length == 0 &&
		    		 checkedStatus.length == 0 && checkedAppoStatus.length == 0){
		    	 layer.confirm('是否导出全部数据？', {icon: 3, title:'提示'}, function(index){
			    		var obj = new Object();
			    		obj.suppName = suppName;
			    		obj.suppRange = suppRange;
			    		obj.appoType = appoType;
			    		obj.requCode = requCode;
			    		obj.appoCode = appoCode;
			    		obj.appoStartDate = appoStartDate;
			    		obj.appoEndDate = appoEndDate;
			    		obj.appoCreStartDate = appoCreStartDate;
			    		obj.appoCreEndDate = appoCreEndDate;
			    		obj.deliCode = deliCode;
			    		obj.deliStartDate = deliStartDate;
			    		obj.deliEndDate = deliEndDate;
			    		obj.deliCreStartDate = deliCreStartDate;
			    		obj.deliCreEndDate = deliCreEndDate;
			    		obj.receStartDate = receStartDate;
			    		obj.receEndDate = receEndDate;
			    		obj.receCreStartDate = receCreStartDate;
			    		obj.receCreEndDate = receCreEndDate;
			    		obj.mateName = mateName;
			    		obj.orderId = orderId;
			    		obj.inboDeliCode = inboDeliCode;
			    		obj.isOccupy = isOccupy;
			    		obj.receUnit = receUnit;
			    		var objjson =  JSON.stringify(obj);
			    		selectMateCodes =encodeURIComponent(objjson);
			    		statusJsonCodes =encodeURIComponent(statusJson);
			    		checkedStatusJson =encodeURIComponent(checkedStatusJson);
			    		checkedAppoStatusJson =encodeURIComponent(checkedAppoStatusJson);
				    	var url ="/exportAppoDeliList?objjson="+selectMateCodes+"&statusJsonCodes="+statusJsonCodes+
				    	"&checkedStatusJson="+checkedStatusJson+"&checkedAppoStatusJson="+checkedAppoStatusJson;
						location=url;
		    		  layer.close(index);
		    		});
		     }else{
		    	 layer.confirm('是否导出过滤后的全部数据？', {icon: 3, title:'提示'}, function(index){
			    	    var obj = new Object();
		    		    obj.suppName = suppName;
		    		    obj.suppRange = suppRange;
			    		obj.appoType = appoType;
			    		obj.requCode = requCode;
			    		obj.appoCode = appoCode;
			    		obj.appoStartDate = appoStartDate;
			    		obj.appoEndDate = appoEndDate;
			    		obj.appoCreStartDate = appoCreStartDate;
			    		obj.appoCreEndDate = appoCreEndDate;
			    		obj.deliCode = deliCode;
			    		obj.deliStartDate = deliStartDate;
			    		obj.deliEndDate = deliEndDate;
			    		obj.deliCreStartDate = deliCreStartDate;
			    		obj.deliCreEndDate = deliCreEndDate;
			    		obj.receStartDate = receStartDate;
			    		obj.receEndDate = receEndDate;
			    		obj.receCreStartDate = receCreStartDate;
			    		obj.receCreEndDate = receCreEndDate;
			    		obj.mateName = mateName;
			    		obj.orderId = orderId;
			    		obj.inboDeliCode = inboDeliCode;
			    		obj.isOccupy = isOccupy;
			    		obj.receUnit = receUnit;
			    		var objjson =  JSON.stringify(obj);
		    		    selectMateCodes =encodeURIComponent(objjson);
		    		    statusJsonCodes =encodeURIComponent(statusJson);
		    		    checkedStatusJson =encodeURIComponent(checkedStatusJson);
		    		    checkedAppoStatusJson =encodeURIComponent(checkedAppoStatusJson);
			    	    var url ="/exportAppoDeliList?objjson="+selectMateCodes+"&statusJsonCodes="+statusJsonCodes+
			    	    "&checkedStatusJson="+checkedStatusJson+"&checkedAppoStatusJson="+checkedAppoStatusJson;
					    location=url;
			    	  layer.close(index);
		    	  });
		     }
	  })
	  

});

function initAppoTable(){
	table.render({
		  elem:"#appoDeliTableId",
		  url:"/queryAppoDeliByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  limits:[10,20,50,100,200,300,500,1000],
		  id:"appoDeliId",
		  cols:[[
		     {type : 'numbers', title : '序号'},
		     {field:'suppName',title:'供应商', align:'center',width:164},
		     {field:'suppRange',title:'供应商子范围编码', align:'center',width:123},
		     {field:'suppRangeDesc',title:'供应商子范围描述', align:'center',width:123},
		     {field:'appoType',title:'预约类型', align:'center',width:67,templet:
		    	 function(d){
		    	 	var appoType = d.appoType;
		    	 	if("appo"==appoType){
		    	 		return "预约单";
		    	 	}else{
		    	 		return "直发单";
		    	 	}
 		     	 }
		     },
		     {field:'appoCode',title:'预约单号', align:'center',width:76 },
		     {field:'appoStatus',title:'预约状态', align:'center',width:67},
		     {field:'receUnit',title:'送货地址', align:'center',width:67},
		     {field:'requCode',title:'调拨单号', align:'center',width:76},
		     {field:'mateName',title:'物料名称', align:'center',width:166},
		     {field:'mateNumber', title:'预约数量',align:'center',width:85},
		     {field:'deliCode', title:'送货单号',align:'center',width:76},
		     {field:'status', title:'送货状态',align:'center',width:67},
		     {field:'frequency', title:'项次',align:'center',width:60},
		     {field:'orderId', title:'采购订单',align:'center',width:76},
		     {field:'unpaNumber', title:'订单未交量',align:'center',width:111},
		     {field:'calculNumber', title:'订单可用量',align:'center',width:111},
		     {field:'deliNumber', title:'实际送货数量',align:'center',width:111},
		     {field:'receCode', title:'收货单号',align:'center',width:76},
		     {field:'receStatus', title:'收货状态',align:'center',width:67},
		     {field:'receNumber', title:'实收数量',align:'center',width:85},
		     {field:'inboDeliCode', title:'内向交货单号',align:'center',width:88},
		     {field:'isOccupy', title:'是否占用',align:'center',width:67,templet:
		    	 function(d){
		    	 var isOccupy = d.isOccupy;
		    	 if(isOccupy != '' && isOccupy != undefined){
		    		 if(isOccupy == 'yes'){
		    			 return "是";
		    		 }else if(isOccupy == 'no'){
		    			 return "否";
		    		 }else{
		    			 return "";
		    		 }
		    	 }else{
		    		 return "";
		    	 }
		     }
		     },
		     {field:'mateCode', title:'物料编码',align:'center',width:143},
		     {field:'appoDate', title:'预约日期',align:'center',width:76,templet:
		    	 function(d){
	    		      return formatTime(d.appoDate,'yyyy-MM-dd');
	    	     }
		     },
		     {field:'createDate', title:'预约创建日期',align:'center',width:91,templet:
		    	 function(d){
		    	      return formatTime(d.createDate,'yyyy-MM-dd');
		         }
		     },
		     {field:'deliDate', title:'送货日期',align:'center',width:76,templet:
		    	 function(d){
		    	 	return formatTime(d.deliDate,'yyyy-MM-dd');
		     	}
		     },
		     {field:'deliCreateDate', title:'送货创建日期',align:'center',width:91,templet:
		    	 function(d){
		    	 	return formatTime(d.deliCreateDate,'yyyy-MM-dd');
		     	}
		     },
		     {field:'receDate', title:'收货日期',align:'center',width:76,templet:
		    	 function(d){
		    	 	return formatTime(d.receDate,'yyyy-MM-dd');
		     	}
		     },
		     {field:'receCreateDate', title:'收货创建日期',align:'center',width:91,templet:
		    	 function(d){
		    	      return formatTime(d.receCreateDate,'yyyy-MM-dd');
		         }
		     }
		  ]],
		  done : function (res, curr, count){
				var data = res.data;
				var appoNum = 0, deliNum = 0, receNum = 0;
				if(data.length > 0) {
					for ( var k = 0; k < data.length; k++) {
						var elem = data[k];
						appoNum += (elem.mateNumber == undefined || elem.mateNumber == "" || isNaN(elem.mateNumber) || elem.mateNumber<0 )? 0 : parseFloat(elem.mateNumber) ;
						deliNum += (elem.deliNumber == undefined || elem.deliNumber == "" || isNaN(elem.deliNumber) || elem.deliNumber<0 )? 0 : parseFloat(elem.deliNumber) ;
						receNum += (elem.receNumber == undefined || elem.receNumber == "" || isNaN(elem.receNumber) || elem.receNumber<0 )? 0 : parseFloat(elem.receNumber) ;
					}
				}
				$("#appoNum").html(appoNum.toFixed(3));
				$("#deliNum").html(deliNum.toFixed(3));
				$("#receNum").html(receNum.toFixed(3));
		 }
	  })
}


//送货单状态点击事件
function changeClass(dom){
	if($(dom).hasClass("uncheck")){
		$(dom).removeClass("uncheck");
		$(dom).addClass("checked");
		$(dom).css('color','blue');
	}else{
		$(dom).removeClass("checked");
		$(dom).addClass("uncheck");
		$(dom).css('color','gray');
	}
}
//收货单状态点击事件
function changeClass2(dom){
	if($(dom).hasClass("uncheck2")){
		$(dom).removeClass("uncheck2");
		$(dom).addClass("checked2");
		$(dom).css('color','blue');
	}else{
		$(dom).removeClass("checked2");
		$(dom).addClass("uncheck2");
		$(dom).css('color','gray');
	}
}
//预约单状态点击事件
function changeClass3(dom){
	if($(dom).hasClass("uncheck3")){
		$(dom).removeClass("uncheck3");
		$(dom).addClass("checked3");
		$(dom).css('color','blue');
	}else{
		$(dom).removeClass("checked3");
		$(dom).addClass("uncheck3");
		$(dom).css('color','gray');
	}
}