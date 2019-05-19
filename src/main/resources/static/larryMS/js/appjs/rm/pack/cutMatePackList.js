var table;
var headerFields;
var tableData;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  //年月选择器--打切月份
	  laydate.render({
	    elem: '#cutMonth',
	    type: 'month',
	    value: new Date(),
	    btns: ['now', 'confirm'],
	    done: function(value, date){
	    	debugger;
	    	//获取数据
	  	  	getData(value);
	    }
	  });
	  var date = new Date();
	  var year = date.getFullYear();
	  var month = (date.getMonth()+1)<10?"0"+(date.getMonth()+1):(date.getMonth()+1);
	  var cutMonth = year+"-"+month;
	  $("#cutMonth").val(cutMonth);
	  var cutMonth = $("#cutMonth").val();
	 //获取数据
	  getData(cutMonth);
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
	  //前台搜索
	  $("#serachMate").click(function(){
		  var mateName = $("#mateName").val();
		  var oemSuppName = $("#oemSuppName").val();
		  var baoSuppName = $("#baoSuppName").val();
		  var searchTbData = new Array();
		  for(var i=0;i<tableData.length;i++){
			    var item = tableData[i];
			    var mateFlag = false;
				var oemFlag = false;
				var baoFlag = false;
				if(mateName != ''){
					if(item.mateCode != undefined
	  						&& item.mateName != undefined
	  						&& (item.mateCode.toUpperCase().indexOf(mateName.toUpperCase())!= -1 || item.mateName.toUpperCase().indexOf(mateName.toUpperCase())!= -1)){
						mateFlag = true;
					}
				}else{
					mateFlag = true;
				}
				if(oemSuppName != ''){
					if(item.oemSuppName != undefined
							&& item.oemSapId != undefined
							&& (item.oemSuppName.toUpperCase().indexOf(oemSuppName.toUpperCase())!= -1 || item.oemSapId.toUpperCase().indexOf(oemSuppName.toUpperCase())!= -1)){
						oemFlag = true;
					}
				}else{
					oemFlag = true;
				}
				if(baoSuppName != ''){
					if(item.baoSuppName != undefined
							&& item.baoSapId != undefined
							&& (item.baoSuppName.toUpperCase().indexOf(baoSuppName.toUpperCase())!= -1 || item.baoSapId.toUpperCase().indexOf(baoSuppName.toUpperCase())!= -1)){
						baoFlag = true;
					}
				}else{
					baoFlag = true;
				}
				if(mateFlag && oemFlag && baoFlag){
					searchTbData.push(item);
				}
		  }
		  initcutMatePackTable(headerFields,searchTbData);
		  
	  });
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  

	  //重置
	  $("#reset").click(function(){
		  initcutMatePackTable(headerFields,tableData);
		  $("#mateName").val("");
		  $("#oemSuppName").val("");
		  $("#baoSuppName").val("");
	  });
	 //导出
	  $('#exportBut').click(function(){
		  debugger;
		  	 var mateName = $("#mateName").val();
		  	 var oemSuppName = $("#oemSuppName").val();
		  	 var baoSuppName = $("#baoSuppName").val();
		     var month = $("#cutMonth").val();
		     if(month == '' || month == undefined){
		    	 layer.msg("打切月份不能为空！");
		    	 return;
		     }
		     if(mateName=='' && oemSuppName=='' && baoSuppName==''){
		    	 layer.confirm('是否导出'+month+'的全部数据？', {icon: 3, title:'提示'}, function(index){
			    	debugger;	
		    		 var obj = new Object();
			    		obj.mateName = mateName;
			    		obj.oemSuppName = oemSuppName;
			    		obj.baoSuppName = baoSuppName;
			    		obj.cutMonth = month;
			    		var objjson =  JSON.stringify(obj);
			    		var selectMateCodes =encodeURIComponent(objjson);
				    	var url ="/exportCutMatePackList?objjson="+selectMateCodes;
						location=url;
		    		  layer.close(index);
		    		});
		     }else{
		    	 layer.confirm('是否导出'+month+'过滤后的全部数据？', {icon: 3, title:'提示'}, function(index){
		    		 var obj = new Object();
		    		 	obj.mateName = mateName;
			    		obj.oemSuppName = oemSuppName;
			    		obj.baoSuppName = baoSuppName;
			    		obj.cutMonth = month;
			    		var objjson =  JSON.stringify(obj);
			    		var selectMateCodes =encodeURIComponent(objjson);
				    	var url ="/exportCutMatePackList?objjson="+selectMateCodes;
					    location=url;
			    	  layer.close(index);
		    	  });
		     }
	  })
	  

});
//初始化列表数据
function initcutMatePackTable(headerFields,tableData){
	table.render({
		  elem:"#cutMatePackTable",
		  data:tableData,
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  limits:[10,20,50,100,200,300,500,1000],
		  id:"cutMatePackTableId",
		  cols:[headerFields]
		 
	  })
}
//获取数据
function getData(cutMonth){
	 $.ajax({
		 type:"post",
		 url:"/getCutMatePackList",
		 data:{
			 cutMonth:cutMonth
		 },
		 dataType:"JSON",
		 success:function(data){
			 console.info(data);
			 headerFields = data.jsonArray;
			 tableData = data.data;
			 initcutMatePackTable(headerFields,tableData);
		 }
	  });
}


