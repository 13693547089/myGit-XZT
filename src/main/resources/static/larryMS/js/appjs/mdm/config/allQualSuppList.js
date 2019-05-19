var table;

var checkedRow = [];
layui.use('table', function(){
	  table = layui.table;
	  var $ = layui.$;
	  initSuppTable()
	  /*$.ajax({
			type:"post",
			url:"/getAllQualSuppList",
			dataType:"JSON",
			success:function(data){
				//console.info(data);
				//initSuppTable(table,data);
				addCheckbox(table,data)
			}
		});*/
		
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
		  	var type = obj.type;
			var checked = obj.checked;
			var data = obj.data;
			if (type == 'all') {
				var cacheData = table.cache.qualSuppTableId;
				if (checked) {
					var tempData = checkedRow;
					for ( var k = 0; k < cacheData.length; k++) {
						var elem = cacheData[k];
						if (tempData.indexOf(elem) == -1)
							checkedRow.push(elem);
					}
				} else {
					var tempData = [];
					for ( var k = 0; k < checkedRow.length; k++) {
						var elem = checkedRow[k];
						if (cacheData.indexOf(elem) == -1)
							tempData.push(elem);
					}
					checkedRow = tempData;
				}
			} else if (type = 'one') {
				if (checked) {
					checkedRow.push(data);
				} else {
					var tempData = [];
					for ( var i = 0; i < checkedRow.length; i++) {
						var elem = checkedRow[i];
						if (elem.mateId != data.mateId)
							tempData.push(elem);
					}
					checkedRow = tempData;
				}
			}
	  });
	  
	  //条件搜索
	  var $ = layui.$, active = {
			    reload: function(){
			      var suppInfo = $('#suppInfo');
			      var category = $("#category");
			      //执行重载
			      table.reload('qualSuppTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {
			        	  suppInfo: suppInfo.val(),
			        	  category:category.val(),
			        }
			      });
			    }
		};
	  //监听单元格事件
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'setSign'){
	     var url ="/getCheckQualSuppHtml?suppId="+data.suppId;
	     location=url;
	    }
	  });
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  
	  
	//取消
	  $("#cancel").click(function(){
	  	var index=parent.layer.getFrameIndex(window.name);
	  	parent.layer.close(index);
	  });
	  //确定
	  $("#confirm").click(function(){
	  	var checkStatus = table.checkStatus('qualSuppTableId');
	  	var data = checkStatus.data;
	  	var userId = $("#userId").val();
	  	var suppIds =[];
	  	 $.ajax({
	  			type:"post",
	  			url:"/queryQualSuppOfUser?userId="+userId,
	  			dataType:"JSON",
	  			async: false,
	  			success:function(userData){
	  				$.each(data,function(suppindex,suppitem){
	  					var a = 0;
	  					$.each(userData.data,function(userindex,useritem){
	  						if(useritem.suppId == suppitem.suppId){
	  							a++;
	  						}
	  					});
	  					if(a == 0){
	  						suppIds.push(suppitem.suppId);
	  					}
	  				});
	  			}
	  	});
	  	 if(suppIds.length!=0){
	  		 $.ajax({
	  				type:"post",
	  				url:"/addQualSuppForUser?userId="+userId,
	  				data:"suppIds="+suppIds,
	  				dataType:"JSON",
	  				success:function(data){
	  					if(data){
	  						layer.msg('供应商添加成功',{time:2000});
	  						var index=parent.layer.getFrameIndex(window.name);
	  					  	parent.layer.close(index);
	  					  	parent.reload();
	  					}
	  				},
	  				error : function(xhr) {
	  					layer.msg('程序错误');
	  				}
	  		});
	  	 }else{
	  		layer.msg('无法添加重复供应商');
	  	 }
	  	
	  	
	  });

	  
	  function addCheckbox(table,data){
			var userId = $("#userId").val();
			 $.ajax({
					type:"post",
					url:"/queryQualSuppOfUser?userId="+userId,
					dataType:"JSON",
					success:function(userData){
						$.each(data,function(suppindex,suppitem){
							$.each(userData.data,function(userindex,useritem){
								if(useritem.suppId == suppitem.suppId){
									data[suppindex]["LAY_CHECKED"]='true';
									var index= data[suppindex]['LAY_TABLE_INDEX'];
		                            $('.layui-table-fixed-l tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
		                            $('.layui-table-fixed-l tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');
								}
							});
						});
						initSuppTable(table,data);
						 var checkStatus = table.checkStatus('qualSuppTableId');
		                 if(checkStatus.isAll){
		                     $(' .layui-table-header th[data-field="0"] input[type="checkbox"]').prop('checked', true);
		                     $('.layui-table-header th[data-field="0"] input[type="checkbox"]').next().addClass('layui-form-checked');
		                 }
					}
			});
			 
			 
		}
	  
});

function initSuppTable(){
	var suppTable = table.render({
		 elem:"#suppTable",
		 url:"/getAllQualSuppList",
		 page:true,
		 width: '100%', 
		 height:'auto',
		 limit:10,
		 cols:[[{
			 checkbox:true
		 },{
			 field:"sapId",
			 title:"SAP编码",
			 align:'center',
		     width:186
		 },{
			 field:"category",
			 title:"供应商分类",
			 align:'center',
		     width:143
		 },{
			 field:"suppName",
			 title:"供应商名称",
			 align:'center',
		     width:308
		 },{
			 field:"suppAbbre",
			 title:"供应商简称",
			 align:'center',
		     width:104
		 }
		 ]],
		 id:'qualSuppTableId',
		 done : function(res, curr, count) {
			
			 var data2 = res.data;
			 var suppId = data2[0].suppId;
		 }
	 });
}

function getCheckedData(){
	/*var checkStatus = table.checkStatus('qualSuppTableId');
	checkedData = checkStatus.data;
	return checkedData;*/
	return checkedRow;
}






