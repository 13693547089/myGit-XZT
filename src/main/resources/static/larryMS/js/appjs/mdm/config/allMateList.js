var table;
var suppTable;

var checkedRow = [];
layui.use('table', function(){
	  table = layui.table;
	  var $ = layui.$;
	  initSuppTable();
	  /*$.ajax({
			type:"post",
			url:"/queryAllMaterial",
			dataType:"JSON",
			success:function(data){
				addCheckbox(table,data)
			}
		});*/
	  
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
		  /* var data = obj.data;
		  if(obj.type=='all'){
			  var checkStatus = table.checkStatus('mateTableId');
			  var checkedData = checkStatus.data;
			  
		  }else if(obj.type=='one'){
			  if(obj.checked){
				  checkedTableData.push(data);
			  }else{
				  checkedTableData.remove(data);
			  }
		  }*/
		  
		  	var type = obj.type;
			var checked = obj.checked;
			var data = obj.data;
			if (type == 'all') {
				var cacheData = table.cache.mateTableId;
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
			      var mateInfo = $('#mateInfo');
			      var mateGroupInfo = $("#mateGroupInfo");
			      var mateTypeInfo = $("#mateTypeInfo");
			      //执行重载
			      table.reload('mateTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {
			        	  mateInfo: mateInfo.val(),
			        	  mateGroupInfo:mateGroupInfo.val(),
			        	  mateTypeInfo:mateTypeInfo.val()
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
	  
	  
	  function addCheckbox(table,data){
			var userId = $("#userId").val();
			 $.ajax({
					type:"post",
					url:"/queryMaterialOfUser?userId="+userId,
					dataType:"JSON",
					success:function(mateData){
						$.each(data,function(mateindex,mateitem){
							$.each(mateData.data,function(mateDataIndex,mateDataItem){
								if(mateDataItem.mateId == mateitem.mateId){
									data[mateindex]["LAY_CHECKED"]='true';
									var index= data[mateindex]['LAY_TABLE_INDEX'];
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

		//取消
		$("#cancel").click(function(){
			var index=parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		});
		//确定
		$("#confirm").click(function(){
			var checkStatus = table.checkStatus('mateTableId');
			var data = checkStatus.data;
			var userId = $("#userId").val();
			var mateIds =[];
			 $.ajax({
					type:"post",
					url:"/queryMaterialOfUser?userId="+userId,
					dataType:"JSON",
					async: false,
					success:function(mateData){
						$.each(data,function(mateindex,mateitem){
							var a = 0;
							$.each(mateData.data,function(mateDataIndex,mateDataItem){
								if(mateDataItem.mateId == mateitem.mateId){
									a++;
								}
							});
							if(a == 0){
								mateIds.push(mateitem.mateId);
							}
						});
					}
			});
			 if(mateIds.length!=0){
				 $.ajax({
						type:"post",
						url:"/addMaterialForUser?userId="+userId,
						data:"mateIds="+mateIds,
						dataType:"JSON",
						success:function(data){
							if(data){
								layer.msg('物料添加成功',{time:2000});
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
				 layer.msg('无法添加重复物料');
			 }
		});
	  
	  
});

function initSuppTable(){
	suppTable = table.render({
		 elem:"#mateTable",
		 url:"/queryAllMaterial",
		 page:true,
		 width: '100%', 
		 height:'auto',
		 limit:10,
		 cols:[[{
			 checkbox:true
		 },{
			 field:"finMateId",
			 title:"成品物料编码",
			 align:'center',
		     width:118
		 },{
			 field:"mateCode",
			 title:"物料编码",
			 align:'center',
		     width:181
		 },{
			 field:"mateName",
			 title:"物料名称",
			 align:'center',
		     width:270
		 },{
			 field:"mateGroupExpl",
			 title:"物料组说明",
			 align:'center',
		     width:155
		 },{
			 field:"mateType",
			 title:"物料类型编码",
			 align:'center',
		     width:116
		 },{
			 field:"basicUnit",
			 title:"基本单位",
			 align:'center',
		     width:91
		 },{
			 field:"procUnit",
			 title:"采购单位",
			 align:'center',
		     width:91
		 }
		 ]],
		 id:'mateTableId'
	 });
}

function getCheckedData(){
	/*var checkStatus = table.checkStatus('mateTableId');
	var checkedData = checkStatus.data;
	return checkedData;*/
	
	return checkedRow;
}


Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].mateId == val.mateId) {
			this.splice(k, 1);
			return;
		}
	}
};	


