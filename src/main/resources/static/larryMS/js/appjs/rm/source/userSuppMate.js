var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initAppoTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
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
			      
			      var name = $('#name');
			      var suppName = $("#suppName");
			      var mateName = $("#mateName");
			      var mateGroupExpl = $("#mateGroupExpl");
			      var mateType = $("#mateType");
			      var basicUnit = $("#basicUnit");
			      var procUnit = $("#procUnit");
			      
			      //执行重载
			      table.reload('userSuppMateId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	name: name.val(),
			        	suppName:suppName.val(),
			        	mateName:mateName.val(),
			        	mateGroupExpl:mateGroupExpl.val(),
			        	mateType:mateType.val(),
			        	basicUnit:basicUnit.val(),
			        	procUnit:procUnit.val()
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
		 
	  });
	 //导出
	  $('#exportAppo').click(function(){
		  var suppName = $('#suppName').val();
	      var name = $("#name").val();
	      var mateName = $("#mateName").val();
	      var mateGroupExpl = $("#mateGroupExpl").val();
	      var mateType = $("#mateType").val();
	      var basicUnit = $("#basicUnit").val();
	      var procUnit = $("#procUnit").val();
		     if(suppName=='' && name=='' && mateName=='' && mateGroupExpl=='' && 
		    		 mateType== '' && basicUnit == '' && procUnit == ''){
		    	 layer.confirm('是否导出全部数据？', {icon: 3, title:'提示'}, function(index){
			    		var obj = new Object();
			    		obj.suppName = suppName;
			    		obj.name = name;
			    		obj.mateName = mateName;
			    		obj.mateGroupExpl = mateGroupExpl;
			    		obj.mateType = mateType;
			    		obj.basicUnit = basicUnit;
			    		obj.procUnit = procUnit;
			    		var objjson =  JSON.stringify(obj);
			    		var selectMateCodes =encodeURIComponent(objjson);
				    	var url ="/exportUserSuppMateList?objjson="+selectMateCodes;
						location=url;
		    		  layer.close(index);
		    		});
		     }else{
		    	 layer.confirm('是否导出过滤后的全部数据？', {icon: 3, title:'提示'}, function(index){
		    		 var obj = new Object();
			    		obj.suppName = suppName;
			    		obj.name = name;
			    		obj.mateName = mateName;
			    		obj.mateGroupExpl = mateGroupExpl;
			    		obj.mateType = mateType;
			    		obj.basicUnit = basicUnit;
			    		obj.procUnit = procUnit;
			    		var objjson =  JSON.stringify(obj);
			    		var selectMateCodes =encodeURIComponent(objjson);
				    	var url ="/exportUserSuppMateList?objjson="+selectMateCodes;
					    location=url;
			    	  layer.close(index);
		    	  });
		     }
	  })
	  

});

function initAppoTable(){
	table.render({
		  elem:"#userSuppMateTableId",
		  url:"/getUserSuppMateListByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  limits:[10,20,50,100,200,300,500,1000],
		  id:"userSuppMateId",
		  cols:[[
		     {type : 'numbers', title : '序号'},
		     {field:'userId',title:'采购员编码', align:'center',width:70},
		     {field:'name',title:'采购员名称', align:'center',width:67},
		     {field:'suppName',title:'供应商名称', align:'center',width:190 },
		     {field:'sapId',title:'供应商编码', align:'center',width:67},
		     {field:'mateCode',title:'物料编码', align:'center',width:167},
		     {field:'mateName',title:'物料名称', align:'center',width:170},
		     {field:'mateGroupExpl',title:'物料组说明', align:'center',width:131},
		     {field:'mateType', title:'物料类型',align:'center',width:85},
		     {field:'basicUnit', title:'基本单位',align:'center',width:76},
		     {field:'procUnit', title:'采购单位',align:'center',width:67}
		  ]]
		  
	  })
}


