//地址前缀
var prefix = "/bam/ps";
//选中节点
var direId="";

//定义树形控件
layui.use(['table','laydate' ], function() {
	var $ = layui.$;
	var table = layui.table;
	//定义日期格式
	var laydate = layui.laydate;
	//常规用法
    laydate.render({
    	elem: '#search_crtDateStart'
    });
	laydate.render({
		elem: '#search_crtDateEnd'
	});
	
	initListTable(prefix+"/getPadPlanPageList?flag=2");
	//初始化表格
	function initListTable(url){
		var str=$('#createSpan').html().trim();
		var operateBar="#barDemo";
		if( str==''){
			operateBar="#barDemo1";
		}
	   table.render({
		     elem: '#list-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:url
		    //,id:'olist'
		    //,even: true
		    ,cols: [[
		       {checkbox:true}
			  ,{field:'sn', title: '序号',width:'5%',type:'numbers'}
			  ,{field:'status', title: '状态',width:'7%'}
		      ,{field:'planMonth', title: '计划月份',width:'10%'}
		      ,{field:'planCode', title: '计划编号',width:'15%'}
		      ,{field:'planName', title: '计划名称'}
		      ,{field:'crtUser', title: '创建人',width:'10%'}
		      ,{field:'crtDate', title: '创建日期',width:'10%',
		    	  templet: function(d){
		    		  return formatTime(d.crtDate,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'tools', title: '操作',width:'12%',align:'center',toolbar:operateBar,fixed: 'right'}
		    ]]
		  ,page:true
		  ,initSort: {
		    field: 'planCode' //排序字段，对应 cols 设定的各字段名
		    ,type: 'desc' //排序方式  asc: 升序、desc: 降序、null: 默认排序
		  }
	  });
	}
	//表格监听事件
	 table.on('tool(docTool)', function(obj) {
		var data = obj.data;
		var id=data.id;
		if(obj.event == 'show'){
			//查看
			var type="2";
			var url=prefix+"/padPlanDetailPage?mainId="+id+"&type="+type;
			//window.location.href=url;
			tuoGo(url,'padPlanDetail','list-table');
		}else if (obj.event === 'edit') {
			var status = data.status;
			/*var crtUserCode= data.crtUserCode;
			if(status == '已提交' && userCode != crtUserCode){
				layer.msg("已提交状态下不允许编辑！",{time:2000});
				return;
			}*/

			//编辑
			var type="1";
			// 保存状态下
			var url=prefix+"/padPlanDetailPage?mainId="+id+"&type="+type;
			
			// 计划编码
			var code = data.planCode;
			code = "PAD_"+code;
			// 判断是否编辑中
			$.ajax({
				type:'POST',
				url:prefix+"/getEditingSeries",
				data:{orderCode:code},
				success:function(msg){
					
					if(msg.code == 20){
						// 存在系列
						var seriesData = msg.msg;
						
						layer.confirm(seriesData+'<br><br>是否继续编辑？', {
						  btn: ['是','否'] //按钮
						}, function(index, layero){
							layer.close(index);
							
							if(status == '已提交'){
								// 非产销部门人员不能编辑
								if(isPad != "1"){
									layer.msg("已提交状态下不允许编辑！",{time:2000});
									return;
								}
								
								url = prefix+"/padPlanDetailSubmitPage?mainId="+id+"&type="+type;
							}
							tuoGo(url,'padPlanDetail','list-table');
						}, function(){
						});
					}else{

						if(status == '已提交'){
							// 非产销部门人员不能编辑
							if(isPad != "1"){
								layer.msg("已提交状态下不允许编辑！",{time:2000});
								return;
							}
							
							url = prefix+"/padPlanDetailSubmitPage?mainId="+id+"&type="+type;
						}
						tuoGo(url,'padPlanDetail','list-table');
					}					
				},
				error:function(){
					layer.msg("编辑中系列获取失败！",{time:1000});
				}
			});
			
			// 判断是否过了当前月
			/*var planMonth = data.planMonth;
			var currDate = new Date();
			var month=currDate.getMonth()+1;
			month =(month<10 ? "0"+month:month);
			var currYm = currDate.getFullYear().toString()+"-"+month.toString();
			if(currYm > planMonth.toString()){
				layer.msg("月份已超过时限，无法编辑！",{time:2000});
				return;
			}*/
			
			//window.location.href=url;
		}else if(obj.event === 'del'){
			
			var status = data.status;
			if(status == '已提交'){
				layer.msg("已提交状态下不允许删除！",{time:2000});
				return;
			}
			
			// 计划编码
			var code = "PAD_"+data.planCode;
			// 判断是否编辑中
			$.ajax({
				type:'POST',
				url:prefix+"/isExistEditor",
				data:{orderCode:code},
				success:function(msg){
					if(msg.code == 20){
						layer.msg("该计划已处于编辑中，无法删除！",{time:2000});
						return;
					}
					
					layer.confirm('是否删除该计划？', {
					  btn: ['确定','取消'] //按钮
					}, function(){
						//删除
						$.ajax({
							type:'POST',
							url:prefix+"/deletePadPlan",
							data:{id:id},
							success:function(msg){
								reloadTable();
								layer.msg("操作成功！",{time:1000});
							},
							error:function(){
								layer.msg("操作失败！",{time:1000});
							}
						});
					}, function(){
					});
				},
				error:function(){
					layer.msg("判断是否编辑中失败！",{time:1000});
				}
			});
		}
	});
	function reloadTable(){
		var planName=$("#search_planName").val();
		var status=$("#search_status").val();
		var statusName=$("#search_status").find("option:selected").text();
		if(statusName == '请选择'){
			statusName = "";
		}
		
		var crtDateStart=$("#search_crtDateStart").val();
		var crtDateEnd=$("#search_crtDateEnd").val();
		var url=prefix+"/getPadPlanPageList?search_planName="+planName+"&search_status="+statusName
		+"&search_crtDateStart="+crtDateStart+"&search_crtDateEnd="+crtDateEnd+"&flag=2";
		initListTable(url);
	}
	
	//新建按钮点击事件
	$("#createBtn").click(function(){
		var type='1';
		var url=prefix+"/padPlanDetailPage?mainId="+"&type="+type;
		//window.location.href=url;
		tuoGo(url,'padPlanDetail','list-table');
	});
	
	//删除按钮点击事件
	$("#deleteBtn").click(function(){
		var rows = table.checkStatus('list-table').data;
		var ids=[];
		for(var i=0;i<rows.length;i++){
			if(rows[i].status == '已保存'){
				ids.push(rows[i].id);
			}
		}
		
		if(ids.length == 0){
			layer.msg("请选择需要删除的已保存的计划！",{time:1000});
			return;
		}
		
		layer.confirm('是否删除选中的已保存的计划？', {
		  btn: ['确定','取消'] //按钮
		}, function(){
			$.ajax({
				type:'POST',
				url:prefix+"/deleteBatchPadPlan",
				data:{ids:JSON.stringify(ids)},
				success:function(msg){
					reloadTable();
					layer.msg("操作成功！",{time:1000});
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
			});
		}, function(){
			
		});
	});
	//导出事件
	$("#exportBtn").click(function(){
		exportExcel();
	});
	// 文档查询点击事件
	$("#searchBtn").click(function(){
		reloadTable();
	});
	// 同步事件
	$("#syncBtn").click(function(){
		syncMonthData();
	});
	//查询条件重置
	$("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	});
});

/**
 * 导出excel
 * @returns
 */
function exportExcel(){
	
	// ajax 提交不能触发页面的保存框
	/*$.ajax({
		type:'POST',
		url:prefix+"/exportPadPlanInfo",
		//data:{ids:JSON.stringify(ids)},
		success:function(msg){
			layer.msg("操作成功！",{time:1000});
		},
		error:function(){
			layer.msg("操作失败！",{time:1000});
		}
	});*/
	
	var url = prefix+"/exportPadPlanInfo";
	location.href=url;
}

/**
 * 同步本月的物料数据
 * @returns
 */
function syncMonthData(){
	// 获取当前年月
	var padMonth = new Date();	
	var month = (padMonth.getMonth()+1);
	month = month<10?"0"+month:month;
	padMonth = padMonth.getFullYear()+"-"+month;
	
	var confirmDialog = layer.confirm('是否同步'+padMonth+'月物料数据？', {
	  btn: ['确定','取消'] //按钮
	}, function(){
		layer.close(confirmDialog);
		
		// 启动加载层
		var index = layer.load(0, {shade: false});
		// 获取明细数据
		$.ajax({
			 type:"post",
			 url:"/sync/syncPadMaterial",
			 data:{"padMonth":padMonth,"isChecked":"0"},
			 dataType:"JSON",
			 success:function(data){
				// 关闭加载层
				layer.close(index);
				if(data.code==0){
					layer.msg("同步成功！",{time: 2000});
				}else{
					layer.msg(data.msg,{time: 2000});
				}
			 },error: function (XMLHttpRequest, textStatus, errorThrown) {
		           /*  // 状态码
	             console.log(XMLHttpRequest.status);
	             // 状态
	             console.log(XMLHttpRequest.readyState);
	             // 错误信息   
	             console.log(textStatus);*/
				 
	 	    	// 关闭加载层
	 	    	layer.close(index);
	         }
		});
	}, function(){
	});
}
