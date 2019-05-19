var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initAppoTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  //订单日期
	  laydate.render({
		    elem: '#startDate', //指定元素
	  });
	  laydate.render({
		    elem: '#endDate', //指定元素
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
			      
			      var oemOrderCode = $('#oemOrderCode');
			      var oemSuppName = $("#oemSuppName");
			      var mateName = $("#mateName");
			      var packName = $("#packName");
			      var startDate = $("#startDate");
			      var endDate = $("#endDate");
			      //执行重载
			      table.reload('semiMatePackTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  oemOrderCode: oemOrderCode.val(),
			        	  oemSuppName:oemSuppName.val(),
			        	  mateName:mateName.val(),
			        	  packName:packName.val(),
			        	  startDate:startDate.val(),
			        	  endDate:endDate.val()
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
		  initAppoTable();
	  });
	 //导出
	  $('#exportBut').click(function(){
		  debugger;
			  var oemOrderCode = $('#oemOrderCode').val();
		      var oemSuppName = $("#oemSuppName").val();
		      var mateName = $("#mateName").val();
		      var packName = $("#packName").val();
		      var startDate = $("#startDate").val();
		      var endDate = $("#endDate").val();
		     if(oemOrderCode=='' && oemSuppName=='' && mateName=='' && packName=='' && 
		    		 startDate== '' && endDate == ''){
		    	 layer.confirm('是否导出全部数据？', {icon: 3, title:'提示'}, function(index){
			    	debugger;	
		    		 var obj = new Object();
			    		obj.oemOrderCode = oemOrderCode;
			    		obj.oemSuppName = oemSuppName;
			    		obj.mateName = mateName;
			    		obj.packName = packName;
			    		obj.startDate = startDate;
			    		obj.endDate = endDate;
			    		var objjson =  JSON.stringify(obj);
			    		var selectMateCodes =encodeURIComponent(objjson);
				    	var url ="/exportSemiMatePackList?objjson="+selectMateCodes;
						location=url;
		    		  layer.close(index);
		    		});
		     }else{
		    	 layer.confirm('是否导出过滤后的全部数据？', {icon: 3, title:'提示'}, function(index){
		    		 var obj = new Object();
			    		obj.oemOrderCode = oemOrderCode;
			    		obj.oemSuppName = oemSuppName;
			    		obj.mateName = mateName;
			    		obj.packName = packName;
			    		obj.startDate = startDate;
			    		obj.endDate = endDate;
			    		var objjson =  JSON.stringify(obj);
			    		var selectMateCodes =encodeURIComponent(objjson);
				    	var url ="/exportSemiMatePackList?objjson="+selectMateCodes;
					    location=url;
			    	  layer.close(index);
		    	  });
		     }
	  })
	  

});

function initAppoTable(){
	table.render({
		  elem:"#semiMatePackTable",
		  url:"/getSemiMatePackList",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  limits:[10,20,50,100,200,300,500,1000],
		  id:"semiMatePackTableId",
		  cols:[[
		     {type : 'numbers', title : '序号',width:40},
		     {field:'oemOrderCode',title:'OEM订单编号', align:'center',width:92},
		     {field:'orderDate', title:'订单日期',align:'center',width:75,templet:
		    	 function(d){
		    	      return formatTime(d.orderDate,'yyyy-MM-dd');
		         }
		     },
		     {field:'oemSuppName',title:'OEM供应商', align:'center',width:179},
		     {field:'mateName',title:'品名', align:'center',width:158 },
		     {field:'purcQuan',title:'采购数量', align:'center',width:67},
		     {field:'quanMate',title:'已交数量', align:'center',width:67},
		     {field:'unpaQuan',title:'未交数量', align:'center',width:76},
		     {field:'packName',title:'包材名称', align:'center',width:166},
		     {field:'packTotalNum', title:'包材总量',align:'center',width:85},
		     {field:'placedNum', title:'已下单数量',align:'center',width:76},
		     {field:'residueNum', title:'剩余可下单数量',align:'center',width:99},
		     {field:'oemSapId', title:'OEM供应商编码',align:'center',width:133},
		     {field:'mateCode', title:'料号',align:'center',width:146},
		     {field:'packCode', title:'包材编码',align:'center',width:143},
		  ]]
		 
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