var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initPackOrderTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  
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
			      
			      var startDate = $('#startDate');
			      var endDate = $('#endDate');
			      var packOrderCode = $("#packOrderCode");
			      var mateName = $("#mateName");
			      var packName = $("#packName");
			      var oemSuppName = $("#oemSuppName");
			      var baoSuppName = $("#baoSuppName");
			      //执行重载
			      table.reload('packOrderTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	startDate: startDate.val(),
			        	endDate: endDate.val(),
			        	packOrderCode:packOrderCode.val(),
			        	mateName:mateName.val(),
			        	packName:packName.val(),
			        	oemSuppName:oemSuppName.val(),
			        	baoSuppName:baoSuppName.val()
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
		  initPackOrderTable();
	  });
	 //导出
	  $('#exportAppo').click(function(){
		  var startDate = $('#startDate').val();
	      var endDate = $('#endDate').val();
	      var packOrderCode = $("#packOrderCode").val();
	      var mateName = $("#mateName").val();
	      var packName = $("#packName").val();
	      var oemSuppName = $("#oemSuppName").val();
	      var baoSuppName = $("#baoSuppName").val();
		     if(startDate=='' && endDate=='' && packOrderCode=='' && mateName=='' && packName=='' && 
		    		 oemSuppName== '' && baoSuppName == '' ){
		    	 layer.confirm('是否导出全部数据？', {icon: 3, title:'提示'}, function(index){
			    		var obj = new Object();
			    		obj.startDate = startDate;
			    		obj.endDate = endDate;
			    		obj.packOrderCode = packOrderCode;
			    		obj.mateName = mateName;
			    		obj.packName = packName;
			    		obj.oemSuppName = oemSuppName;
			    		obj.baoSuppName = baoSuppName;
			    		var objjson =  JSON.stringify(obj);
			    		var selectMateCodes =encodeURIComponent(objjson);
				    	var url ="/exportPackOrderList?objjson="+selectMateCodes;
						location=url;
		    		  layer.close(index);
		    		});
		     }else{
		    	 layer.confirm('是否导出过滤后的全部数据？', {icon: 3, title:'提示'}, function(index){
		    		 var obj = new Object();
			    		obj.startDate = startDate;
			    		obj.endDate = endDate;
			    		obj.packOrderCode = packOrderCode;
			    		obj.mateName = mateName;
			    		obj.packName = packName;
			    		obj.oemSuppName = oemSuppName;
			    		obj.baoSuppName = baoSuppName;
			    		var objjson =  JSON.stringify(obj);
		    		    var selectMateCodes =encodeURIComponent(objjson);
			    	    var url ="/exportPackOrderList?objjson="+selectMateCodes;
					    location=url;
			    	  layer.close(index);
		    	  });
		     }
	  })
	  

});

function initPackOrderTable(){
	table.render({
		  elem:"#packOrderTable",
		  url:"/getPackOrderListByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  limits:[10,20,50,100,200,300,500,1000],
		  id:"packOrderTableId",
		  cols:[[
		     {type : 'numbers', title : '序号'},
		     {field:'orderDate',title:'订单日期', align:'center',width:164,templet:
		    	 function(d){
   		      		return formatTime(d.orderDate,'yyyy-MM-dd');
		     	 }
		     },
		     {field:'packOrderCode',title:'包材订单编号', align:'center',width:123},
		     {field:'oemSuppName',title:'OEM供应商', align:'center',width:174},
		     {field:'mateCode',title:'半成品料号', align:'center',width:153 },
		     {field:'mateName',title:'半成品品名', align:'center',width:182},
		     {field:'packCode',title:'包材编码', align:'center',width:158},
		     {field:'packName',title:'包材名称', align:'center',width:189},
		     {field:'mateNumber', title:'采购数量',align:'center',width:85},
		     {field:'baoSuppName', title:'包材供应商',align:'center',width:164},
		     {field:'packOrderNum', title:'包材订单数量',align:'center',width:97},
		     {field:'completeNum', title:'当前已完成',align:'center',width:80},
		     {field:'residueNum', title:'剩余未完成',align:'center',width:86},
		     {field:'invenNum', title:'库存数量',align:'center',width:111},
		     {field:'oemSapId', title:'OEM供应商编码',align:'center',width:111},
		     {field:'baoSapId', title:'包材供应商编码',align:'center',width:111}
		  ]]
	  })
}
