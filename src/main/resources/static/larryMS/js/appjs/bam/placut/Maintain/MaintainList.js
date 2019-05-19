var table;
var mate;
layui.use('table', function(){
	  table = layui.table;
	  var $ = layui.$;
	  initCutProductTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	 
	// 条件搜索
		var $ = layui.$, active = {
			reload : function() {
				var mateInfo = $('#mateInfo');
				var mateGroupInfo = $("#mateGroupInfo");
				var mateTypeInfo = $("#mateTypeInfo");
				var isSpecial = $("#isSpecial");
				var mainStru = $("#mainStru");
				var cutAim = $("#cutAim");
				// 执行重载
				table.reload('cutProductTableId', {
					page : {
						curr : 1
					// 重新从第 1 页开始
					},
					where : {
						mateInfo : mateInfo.val(),
						mateGroupInfo : mateGroupInfo.val(),
						mateTypeInfo : mateTypeInfo.val(),
						isSpecial : isSpecial.val(),
						mainStru : mainStru.val(),
						cutAim : cutAim.val()
					}
				});
			}
		};
		
		// 添加
		$('#add').on('click', function() {
			layer.open({
				type : 2,
				title : '物料列表',
				shadeClose : false,
				shade : 0.1,
				content : '/getAllMaterialsHtml',
				area : [ '1100px', '95%' ],
				maxmin : true, // 开启最大化最小化按钮
				btn: ['确认', '取消']
			       ,yes: function(index, layero){
			      //按钮【按钮一】的回调
			      // 获取选中的物料数据
			      var checkedData = $(layero).find("iframe")[0].contentWindow
			          .getCheckedData();
			      if(checkedData.length == 0){
			    	  layer.msg("请选择物料");
			    	  return;
			      }else{
			    	  // 关闭弹框
			    	  layer.close(index);
			      }
			      // 处理数据
			      dealMates2(checkedData);
			     },
			     btn2: function(index, layero){
			      //按钮【按钮二】的回调
			      // 默认会关闭弹框
			      //return false 开启该代码可禁止点击该按钮关闭
			     }
			});
		});
		
	  //移除
	  $("#remove").click(function(){
		   var table = layui.table;
	  		var checkStatus = table.checkStatus('cutProductTableId');
	  		var data = checkStatus.data;
	  		var length = data.length;
	  		if(length != 0){
	  			  var prodIds = [];
	  			  for(var i=0;i<length;i++){
	  				 prodIds[i]=data[i].prodId;
	  			  }
  				  layer.confirm('确定要删除选中的物料吗？', function(index){
  				     $.ajax({
  				    	 type:"post",
  				    	 url:"/deleteCutProductByprodId",
  				    	 data:"prodIds="+prodIds,
  				    	 dataType:"JSON",
  				    	 success:function(data2){
  				    		 if(data2){
  			    				 layer.msg('删除成功', {time:2000 });
  			    				 initCutProductTable();
  			    			 }else{
  			    				 layer.alert('<span style="color:red;">删除失败</sapn>');
  			    			 }
  				    	 },
  				      	 error:function(){
  				      		layer.msg('程序出错', {time:2000 });
  				      	 }
  				      });
  				      layer.close(index);
  				  });
	  		  }else{
	  			  layer.alert('<span style="color:red;">请选择一条或多条数据进行删除</sapn>');	
	  		  }
	  });
	  //打切结构
	  $("#cutStructure").click(function(){
		  var url="/getMaintainDetailHtml";
		  //location=url;
		  tuoGo(url,'MaintainDetail','cutProductTableId');
	  });
	  
	  //导出打切品物料
	  $("#exportBut").click(function(){
		 debugger;
		 var mateInfo =  $("#mateInfo").val();
		 var mateGroupInfo =  $("#mateGroupInfo").val();
		 var mateTypeInfo =  $("#mateTypeInfo").val();
		 var isSpecial =  $("#isSpecial").val();
		 var mainStru =  $("#mainStru").val();
		 var cutAim =  $("#cutAim").val();
		 if(mateInfo=="" && mateGroupInfo== "" && mateTypeInfo == ""&& isSpecial == ""&& mainStru == ""&& cutAim == ""){
			 //导出全部打切品
			 layer.confirm('是否导出全部数据？', {icon: 3, title:'提示'}, function(index){
				 var obj = {};
				 obj.mateInfo = mateInfo;
				 obj.mateGroupInfo = mateGroupInfo;
				 obj.mateTypeInfo = mateTypeInfo;
				 obj.isSpecial = isSpecial;
				 obj.mainStru = mainStru;
				 obj.cutAim = cutAim;
				 var objjson =  JSON.stringify(obj);
				 var selectMateCodes =encodeURIComponent(objjson);
				 var url ="/exportCutProduct?objjson="+selectMateCodes;
				 location = url;
				 layer.close(index);
			 });
		 }else{
			 //导出查询后的打切品
			 layer.confirm('是否导出查询后全部数据？', {icon: 3, title:'提示'}, function(index){
				 debugger;
				 var obj = {};
				 obj.mateInfo = mateInfo;
				 obj.mateGroupInfo = mateGroupInfo;
				 obj.mateTypeInfo = mateTypeInfo;
				 obj.isSpecial = isSpecial;
				 obj.mainStru = mainStru;
				 obj.cutAim = cutAim;
				 var objjson =  JSON.stringify(obj);
				 var selectMateCodes =encodeURIComponent(objjson);
				 var url ="/exportCutProduct?objjson="+selectMateCodes;
				 location = url;
				 layer.close(index);
			 });
		 }
		 
	  });
	  
	  $('.demoTable .layui-btn').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
	  });
	//监听工具条
	  table.on('tool(cutProductTable)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'add'){//添加版本
	    	layer.open({
				type : 2,
				title : '物料信息',
				shadeClose : false,
				shade : 0.1,
				content : '/getAddProdMateVerHtml?prodId='+data.prodId+"&type=add",
				area : [ '1100px', '90%' ],
				maxmin : true, // 开启最大化最小化按钮
				btn: ['确认', '取消']
			       ,yes: function(index, layero){
			      //按钮【按钮一】的回调
			      // 获取选中的物料数据
			      var map = $(layero).find("iframe")[0].contentWindow
			          .getVersion();
			      if(map.version == undefined || map.version == ""){
			    	  layer.msg("请填写版本信息！",{time:2000});
			    	  return;
			      }
			      if(map.judge){
			    	  // 关闭弹框
			    	  layer.close(index);
			    	  // 处理数据
			    	  debugger;
			    	  map.mateId=data.mateId;
			    	  map.isSpecial = data.isSpecial;
			    	  map.isHaveSeim = data.isHaveSeim;
			    	  addProdMate(map);
			      }else{
			    	  layer.msg(map.msg);
			      }
			     },
			     btn2: function(index, layero){
			      //按钮【按钮二】的回调
			      // 默认会关闭弹框
			      //return false 开启该代码可禁止点击该按钮关闭
			     }
			});
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
	    } else if(obj.event === 'update'){//编辑版本
	    	layer.open({
				type : 2,
				title : '物料信息',
				shadeClose : false,
				shade : 0.1,
				content : '/getAddProdMateVerHtml?prodId='+data.prodId+"&type=update",
				area : [ '1100px', '90%' ],
				maxmin : true, // 开启最大化最小化按钮
				btn: ['确认', '取消']
			       ,yes: function(index, layero){
			      //按钮【按钮一】的回调
			      // 获取选中的物料数据
			    	 var map = $(layero).find("iframe")[0].contentWindow
				          .getVersion();
				      if(map.judge){
				    	  // 关闭弹框
				    	  layer.close(index);
				    	  // 处理数据
				    	  map.mateId=data.mateId;
				    	  updateProdMateVer(map);
				      }else{
				    	  layer.msg(map.msg);
				      }
			     },
			     btn2: function(index, layero){
			      //按钮【按钮二】的回调
			      // 默认会关闭弹框
			      //return false 开启该代码可禁止点击该按钮关闭
			     }
			});
	    }
	  });

});
//重新加载
function reload(){
	initCutProductTable();
}
//初始化table
function initCutProductTable() {
	table.render({
		elem : "#cutProductTable",
		url : "/queryCutProductByPage",
		page : true,
		width : '100%',
		minHeight : '20px',
		limit : 10,
		id : "cutProductTableId",
		cols : [ [ {
			checkbox : true,
		}, {
			title : '序号',
			type : 'numbers'
		}, {
			field : 'mateCode',
			title : '成品物料编码',
			align : 'center',
			width : 149
		}, {
			field : 'mateName',
			align : 'center',
			title : '物料名称',
			width : 179
		}, {
			field : 'version',
			align : 'center',
			title : '版本',
			width : 100
		}, {
			field : 'mateGroupExpl',
			align : 'center',
			title : '物料组说明',
			width : 145
		}, {
			field : 'mateType',
			align : 'center',
			title : '物料类型',
			width : 88
		}, {
			field : 'basicUnit',
			align : 'center',
			title : '基本单位',
			width : 88
		}, {
			field : 'procUnit',
			align : 'center',
			title : '采购单位',
			width : 88
		},{
			field : 'isSpecial',
			align : 'center',
			title : '打切品类型',
			width : 99,
			templet:function(d){
				if(d.isSpecial=="YES"){
					return "自制打切";
				}else{
					return "OEM打切";
				}
			}
		},{
			field : 'mainStru',
			title : '主包材',
			align : 'center',
			width : 86
		}, {
			field : 'cutAim',
			title : '打切目的',
			align : 'center',
			width : 99
		},{
			fixed : 'right',
			title : '操作',
			width : 160,
			align : 'center',
			toolbar : '#barDemo'
		} ] ]
	})
}


function dealMates(data){
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
						reload();
					}
				},
				error : function(xhr) {
					layer.msg('程序错误');
				}
		});
	 }else{
		 layer.msg('无法添加重复的物料');
	 }
}

function dealMates2(data){
	debugger;
	var dataJson = JSON.stringify(data);
	$.ajax({
			type:"post",
			url:"/dealMates",
			data:{dataJson : dataJson},
			dataType:"JSON",
			success:function(data2){
				if(data2.judge){
					layer.msg('打切品添加成功',{time:2000});
					reload();
				}else{
					layer.msg(data2.msg);
					reload();
				}
			},
			error : function(xhr) {
				layer.msg('程序错误');
			}
	});
}
//添加不同版本的打切品物料
function addProdMate(map){
	$.ajax({
		type:"post",
		url:"/addCutProduct2?version="+map.version+"&mateId="+map.mateId+"&cutAim="+map.cutAim+"&mainStru="+map.mainStru+"&isSpecial="+map.isSpecial+"&isHaveSeim="+map.isHaveSeim,
		dataType:"JSON",
		success:function(data){
			if(data.judge){
				layer.msg('打切品添加成功',{time:2000});
				table.reload("cutProductTableId");
			}else{
				layer.msg(data.msg,{time:2000});
			}
		},
		error : function(xhr) {
			layer.msg('程序错误');
		}
  });
}
//修改打切品物料的版本
function updateProdMateVer(map){
	$.ajax({
		type:"post",
		url:"/updateCutProductVer?version="+encodeURIComponent(map.version)+"&prodId="+map.prodId+"&cutAim="+encodeURIComponent(map.cutAim)+"&mainStru="+map.mainStru+"&mateId="+map.mateId,
		dataType:"JSON",
		success:function(data){
			if(data.judge){
				layer.msg('打切品版本修改成功',{time:2000});
				table.reload("cutProductTableId");
			}else{
				layer.msg(data.msg,{time:2000});
			}
		},
		error : function(xhr) {
			layer.msg('程序错误');
		}
   });
}