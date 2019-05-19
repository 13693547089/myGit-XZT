var table;
var tableData=[];
var CutStruTable;
layui.use('table',function(){
	table = layui.table;
	var $ = layui.$;
	$.ajax({
		type:"post",
		url:"/queryAllCutStru",
		dataType:"JSON",
		success:function(data){
			$.each(data,function(index,item){
				tableData.push(item);
			})
			initCutStruTable(tableData);
		},
		error:function(){
			layer.alert("Connection error");
		}
	});
	//保存
	$("#Save").click(function(){
		   //获取layui 编辑后的table表格中的所有数据
		   var data = CutStruTable.config.data;
		   if(data.length > 0){
		   var tableData3=[];
		   var count=0;
		   $.each(data,function(index,item){
			   var converRule = item.converRule;
			   if(converRule == "" || converRule == undefined || converRule == " "){
				   count++;
			   }
			   tableData3.push(item);
		   });
		   if(count != 0 ){
			   layer.alert("请将每个包材的折算规则填写完整");
			   return;
		   }
		   $("#cutStruData").val(JSON.stringify(tableData3));
		   var formData = $("#cutStruForm").serialize();
			   $.ajax({
				   type : "POST",
				   url : "/addCutStru",
				   data : formData,
				   dataType:"JSON",
				   async: false,
				   error : function(request) {
					   layer.alert("Connection error");
				   },
				   success : function(data) {
					   if(data){
						   layer.msg("打切结构保存成功");
					   }else{
						   layer.alert("打切结构保存失败");
					   }
				   }
			   });
		   }else{
			   layer.alert("请填写打切结构");
		   }
	});
	
	//返回
	 $("#goBack").click(function(){
	 	 //window.history.back(-1);
		 tuoBack('.MaintainDetail','#search');
	  });
	 //添加段
	 $("#addDuan").click(function(){
		 var classCodeMax=0;
		 var contentCodeMax=0;
		 $.each(tableData,function(index,item){
			 var ccode = parseInt(item.classCode);
			 var ccode2 = parseInt(item.contentCode);
			 classCodeMax = classCodeMax>ccode ? classCodeMax : ccode;
			 contentCodeMax = contentCodeMax>ccode2 ? contentCodeMax : ccode2;
			 
		 });
		 var i = parseInt(contentCodeMax/100);
		 var a =(i+1)*100+1;
		 var arr = {
		     inveType:'',
		     converRule:'',
			 classCode: classCodeMax+10,
			 className:'',
			 contentCode:a,
			 contentName:'',
			 unit:'',
			 remark:''
		 }
		 tableData.push(arr);
		 initCutStruTable(tableData);
	 });
	 
	//删除段
	$("#Detail").click(function(){
			var checkStatus = table.checkStatus('cutStruTableId');
		   	var checkedData = checkStatus.data;
		   	if (checkedData.length > 0) {
		   		layer.confirm('确定要删除选中的打切结构吗？', function(index){
			   		for ( var int = 0; int < checkedData.length; int++) {
			   			tableData.remove(checkedData[int]);
			   			initCutStruTable(tableData);
			   		}
			   		layer.close(index);
		   		});
		   	} else {
		   		layer.msg("请选择要删除的节点");
		   	}
	});
	 //监听工具条
	  table.on('tool(cutStruTable)', function(obj){
	    var data = obj.data;
	    var checkedData2=[];
	    checkedData2.push(data);
	    if(obj.event === 'check'){//查看

	    } else if(obj.event === 'del'){//删除
	       layer.confirm('确定要删除吗？', function(index){
	    	  for(var i=0;i<checkedData2.length;i++){
	    		  tableData.remove(checkedData2[i]);
	    		  initCutStruTable(tableData);
	    	  }
	    	  layer.close(index);
	      });
	    } else if(obj.event === 'add'){//添加
	    	 var inveType = data.inveType;
	    	 var converRule = data.converRule;
	    	 var classCode = data.classCode;
	    	 var className = data.className;
	    	 var contentCode =data.contentCode;
	    	 var tableData2 = [];
	    	 var contentCodeMax=0;
	    	 $.each(tableData,function(index,item){
	    		 if(item.classCode == classCode){
	    			 var ccode2 = parseInt(item.contentCode);
	    			 contentCodeMax = contentCodeMax>ccode2 ? contentCodeMax : ccode2;
	    		 }
	    	 });
	    	 $.each(tableData,function(index,item){
	    		 tableData2.push(item);
	    		 if(item.contentCode == contentCodeMax){
	    			 var arr = {
	    					 inveType:inveType,
	    					 converRule:converRule,
	    					 classCode: classCode,
	    					 className:className,
	    					 contentCode:parseInt(contentCodeMax)+1,
	    					 contentName:'',
	    					 unit:'',
	    					 remark:''	 
	    			 }
	    			 tableData2.push(arr);
	    		 }
	    	 });
	    	 tableData = tableData2;
	    	 initCutStruTable(tableData);
	    }
	  });
	  //编辑折算规则
	  table.on('edit(cutStruTable)', function(obj){
		  var data = obj.data;
		  var value = obj.value;
		  var field = obj.field;
		  if(field == "converRule"){
			  if(value != "A" && value != "B"){
				  layer.alert("请根据列表上的折算规则填写");
				  data[field] = "";
				  initCutStruTable(tableData);
				  return;
			  }
		  }
	  });
	
});

//初始化table
function initCutStruTable(data) {
	CutStruTable = table.render({
		elem : "#cutStruTable",
		data : data,
		width : '100%',
		minHeight : '20px',
		limit : 50,
		id : "cutStruTableId",
		cols : [ [ {
			checkbox : true,
		}, {
			field : 'inveType',
			title : '库存类型',
			align : 'center',
			edit : 'text',
			width : 115
		}, {
			field : 'converRule',
			title : '折算规则',
			align : 'center',
			edit : 'text',
			width : 92
		},{
			field : 'classCode',
			title : '类号',
			align : 'center',
			width : 92
		}, {
			field : 'className',
			align : 'center',
			title : '类名',
			edit : 'text',
			width : 116
		}, {
			field : 'contentCode',
			align : 'center',
			title : '内容号',
			width : 110
		}, {
			field : 'contentName',
			align : 'center',
			title : '内容名称',
			edit : 'text',
			width : 134
		}, {
			field : 'unit',
			align : 'center',
			title : '单位',
			edit : 'text',
			width : 76
		}, {
			field : 'remark',
			align : 'center',
			title : '备注',
			edit : 'text',
			width : 147
		}, {
			fixed : 'right',
			title : '操作',
			width : 120,
			align : 'center',
			toolbar : '#barDemo'
		} ] ]
	})
}

Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].contentCode == val.contentCode) {
			this.splice(k, 1);
			return;
		}
	}
};