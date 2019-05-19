var table;
var checkedRow = [];
var cutProdType="AHAN";
layui.use(['table','form'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var form = layui.form;
	  initSuppTable(cutProdType);
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
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
			      var seriesInfo = $('#seriesInfo');
			      //执行重载
			      table.reload('mateTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {
			        	 mateInfo: mateInfo.val(),
			        	 seriesInfo: seriesInfo.val(),
			        }
			      });
			    }
		};
	  //重置
	  $("#resetBut").click(function(){
		  $('#mateInfo').val("");
		  $('#seriesInfo').val("");
	  });
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
			 $.ajax({
					type:"post",
					url:"/queryAllCutProduct",
					dataType:"JSON",
					success:function(mateData){
						$.each(data,function(mateindex,mateitem){
							$.each(mateData,function(mateDataIndex,mateDataItem){
								if(mateDataItem.mateId == mateitem.mateId){
									data[mateindex]["LAY_CHECKED"]='true';
									var index= data[mateindex]['LAY_TABLE_INDEX'];
		                            $('.layui-table-fixed-l tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
		                            $('.layui-table-fixed-l tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');
								}
							});
						});
						initSuppTable(cutProdType);
						 var checkStatus = table.checkStatus('mateTableId');
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
			var mateIds =[];
			 $.ajax({
					type:"post",
					url:"/queryAllCutProduct",
					dataType:"JSON",
					async: false,
					success:function(mateData){
							$.each(data,function(mateindex,mateitem){
								var a = 0;
								$.each(mateData,function(mateDataIndex,mateDataItem){
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
						url:"/addCutProduct",
						data:"mateIds="+mateIds,
						dataType:"JSON",
						success:function(data){
							if(data){
								layer.msg('打切品添加成功',{time:2000});
								var index=parent.layer.getFrameIndex(window.name);
		  					  	parent.layer.close(index);
		  					  	parent.reload();
							}
						},
						error : function(xhr) {
							layer.msg('程序错误');
						}
				});
			 }
		});
		//select下拉框改变值触发
		  //根据接收单位查询收货信息
		  form.on("select(cutProdType)",function(obj){
			  cutProdType =obj.value;
			  if(cutProdType == "" || cutProdType == undefined || cutProdType == " "){
				  layer.msg("请选择打切品类型");
				  return ;
			  }
			  initSuppTable(cutProdType);
			  
		  });
	  
	  
});

function initSuppTable(cutProdType){
	var suppTable = table.render({
		 elem:"#mateTable",
		 url:"/queryMateOfSuppMateConfig?cutProdType="+cutProdType,
		 page:true,
		 width: '100%', 
		 height:'auto',
		 limit:10,
		 cols:[[{
			 checkbox:true
		 },{
			 field:"mateName",
			 title:"物料名称",
			 align:'center',
			 width:230
		 },{
			 field:"mateCode",
			 title:"成品物料编码",
			 align:'center',
			 width:192
		 },
		 {
			 field:"seriesCode",
			 title:"系列编码",
			 align:'center',
			 width:86
		 },
		 {
			 field:"seriesExpl",
			 title:"系列说明",
			 align:'center',
			 width:148
		 },{
			 field:"mateType",
			 title:"物料类型",
			 align:'center',
			 width:88
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
	checkedData = checkStatus.data;
	return checkedData;*/
	return checkedRow;
}





