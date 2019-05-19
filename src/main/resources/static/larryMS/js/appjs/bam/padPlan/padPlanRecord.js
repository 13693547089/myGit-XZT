//地址前缀
var prefix = "/bam/ps";
//选中节点
var direId="";
var table;
var oneName;
var twoName;
var threeName;
var fourName;
var fiveName;
var sixName;
var sevenName;
var eightName;
var nineName;
var tenName;
var elevenName;
var twelveName;

var planMonth ;
var startVersion ;
var endVersion;
//定义树形控件
layui.use(['table','laydate' ], function() {
	var $ = layui.$;
	table = layui.table;
	//定义日期格式
	var laydate = layui.laydate;
	//常规用法
    laydate.render({
    	elem: '#planMonth',
    	type: 'month',
    	btns: ['now', 'confirm'],
    	done: function (value,date){
    		planMonth = value;
    	}
    });
	laydate.render({
		elem: '#startVersion',
		type: 'month'
	});
	laydate.render({
		elem: '#endVersion',
		type: 'month',
		btns: ['now', 'confirm'],
		done:function(value,date){
			
			var year = date.year;
			var month = date.month;
			$("#endVersion").val(value);
			getVersionFiels(year,month);
			var matName = $("#matName").val();
			var produExpl = $("#produExpl").val();
			var prodSeries = $("#prodSeries").val();
			var bigItemExpl = $("#bigItemExpl").val();
			var url = prefix+"/getPadPlanRecordListByPage?planMonth="+planMonth+
			"&startVersion="+startVersion+"&endVersion="+endVersion+"&matName="+matName+
			"&produExpl="+produExpl+"&prodSeries="+prodSeries+"&bigItemExpl="+bigItemExpl;
			initListTable(url);
    	}
	});
	debugger;
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var nextMonth = month-1;
	if(nextMonth==0){
		nextMonth = 12;
		year = year-1;
	}
	var strMonth = nextMonth<10?"0"+nextMonth:nextMonth;
	$("#planMonth").val(year+"-"+strMonth);
	planMonth= $("#planMonth").val();
	$("#endVersion").val(year+"-"+strMonth);
	 getVersionFiels(year,nextMonth);
	 function getVersionFiels(year,nextMonth){
		 debugger;
		 var lastMonth = nextMonth-11;
		 if(lastMonth <=0){
			 year = year-1;
			 var a = 11-nextMonth;
			 if(a==0){
				 lastMonth = 12;
			 }else if(a==1){
				 lastMonth = 11;
			 }else if(a==2){
				 lastMonth = 10;
			 }else if(a==3){
				 lastMonth = 9;
			 }else if(a==4){
				 lastMonth = 8;
			 }else if(a==5){
				 lastMonth = 7;
			 }else if(a==6){
				 lastMonth = 6;
			 }else if(a==7){
				 lastMonth = 5;
			 }else if(a==8){
				 lastMonth = 4;
			 }else if(a==9){
				 lastMonth = 3;
			 }else if(a==10){
				 lastMonth = 2;
			 }
		 }
		 var strlastMonth = lastMonth<10?"0"+lastMonth:lastMonth;
		 $("#startVersion").val(year+"-"+strlastMonth);
		 var startV =  $("#startVersion").val();
		 startVersion = startV;
		 var endV =  $("#endVersion").val();
		 endVersion = endV;
		 $.ajax({
			 type:"post",
			 url:prefix+"/getPadPlanRecordFields",
			 async:false,
			 data:{
				 startVersion:startV,
				 endVersion:endV
			 },
			 dataType:"JSON",
			 success:function(data){
				 console.info(data);
				 debugger;
				 var array = new Array("one","two","three","four","five","six","seven","eight","nine","ten","eleven","twelve");
				 for(var i =0;i<array.length;i++){
					 var f = array[i];
					 var month = data[f];
					 if("one" == f){
						 oneName = month+"版本";
					 }else if("two" == f){
						 twoName = month+"版本";
					 }else if("three" == f){
						 threeName = month+"版本";
					 }else if("four" == f){
						 fourName = month+"版本";
					 }else if("five" == f){
						 fiveName = month+"版本";
					 }else if("six" == f){
						 sixName = month+"版本";
					 }else if("seven" == f){
						 sevenName = month+"版本";
					 }else if("eight" == f){
						 eightName = month+"版本";
					 }else if("nine" == f){
						 nineName = month+"版本";
					 }else if("ten" == f){
						 tenName = month+"版本";
					 }else if("eleven" == f){
						 elevenName = month+"版本";
					 }else if("twelve" == f){
						 twelveName = month+"版本";
					 }
				 }
			 }
		 });
	 }
	 
	 
	/*oneName = "2019-01版本";
	twoName = "2019-02版本";
	threeName = "2019-03版本";
	fourName = "2019-04版本";
	fiveName = "2019-05版本";
	sixName = "2019-06版本";
	sevenName = "2019-07版本";
	eightName = "2019-08版本";
	nineName = "2019-09版本";
	tenName = "2019-10版本";
	elevenName = "2019-11版本";
	twelveName = "2019-12版本";*/
	var url = prefix+"/getPadPlanRecordListByPage?planMonth="+planMonth+"&startVersion="+startVersion+"&endVersion="+endVersion;
	initListTable(url);
	
	
	//
	var $ = layui.$, active = {
		    reload: function(){
		      debugger;
		      var matName = $('#matName');
		      var prodSeries = $("#prodSeries");
		      var produExpl = $("#produExpl");
		      var bigItemExpl = $("#bigItemExpl");
		      //执行重载
		      table.reload('listTableId', {
		        page: {
		          curr: 1 //重新从第 1 页开始
		        }
		        ,where: {//后台定义对象接收
		        	  matName: matName.val(),
		        	  prodSeries:prodSeries.val(),
		        	  produExpl:produExpl.val(),
		        	  bigItemExpl:bigItemExpl.val(),
		        }
		      });
		    }
		  
	};
	$('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	
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
		var matName = $("#matName").val();
	  	 var produExpl = $("#produExpl").val();
	  	 var prodSeries = $("#prodSeries").val();
	     var bigItemExpl = $("#bigItemExpl").val();
	     var judge = judgeMonth();
	     if(!judge){
	    	 layer.msg("计划月份和版本范围不能为空！");
	    	 return;
	     }
	     if(matName=='' && produExpl=='' && prodSeries=='' && bigItemExpl == ''){
	    	 layer.confirm('是否导出计划月份为:'+planMonth+',版本范围为:'+startVersion+'~'+endVersion+'的全部物料数据？', {icon: 3, title:'提示'}, function(index){
		    	debugger;	
	    		 	var obj = new Object();
		    		obj.matName = matName;
		    		obj.produExpl = produExpl;
		    		obj.prodSeries = prodSeries;
		    		obj.bigItemExpl = bigItemExpl;
		    		obj.planMonth = planMonth;
		    		obj.startVersion = startVersion;
		    		obj.endVersion = endVersion;
		    		var objjson =  JSON.stringify(obj);
		    		var selectMateCodes =encodeURIComponent(objjson);
			    	var url =prefix+"/exportPadPlanMateList?objjson="+selectMateCodes;
					location=url;
	    		  layer.close(index);
	    		});
	     }else{
	    	 layer.confirm('是否导出计划月份为:'+planMonth+',版本范围为:'+startVersion+'~'+endVersion+'过滤后的全部数据？', {icon: 3, title:'提示'}, function(index){
	    		 var obj = new Object();
	    		 obj.matName = matName;
		    		obj.produExpl = produExpl;
		    		obj.prodSeries = prodSeries;
		    		obj.bigItemExpl = bigItemExpl;
		    		obj.planMonth = planMonth;
		    		obj.startVersion = startVersion;
		    		obj.endVersion = endVersion;
		    		var objjson =  JSON.stringify(obj);
		    		var selectMateCodes =encodeURIComponent(objjson);
			    	var url =prefix+"/exportPadPlanMateList?objjson="+selectMateCodes;
				    location=url;
		    	  layer.close(index);
	    	  });
	     }
	});
	// 查询点击事件
	$("#searchBtn").click(function(){
		var matName = $("#matName").val();
		var produExpl = $("#produExpl").val();
		var prodSeries = $("#prodSeries").val();
		var bigItemExpl = $("#bigItemExpl").val();
		var url = prefix+"/getPadPlanRecordListByPage?planMonth="+planMonth+
		"&startVersion="+startVersion+"&endVersion="+endVersion+"&matName="+matName+
		"&produExpl="+produExpl+"&prodSeries="+prodSeries+"&bigItemExpl="+bigItemExpl;
		initListTable(url);
	});
	// 同步事件
	$("#syncBtn").click(function(){
		syncMonthData();
	});
	//查询条件重置
	$("#resetBtn").click(function(){
		$("#matName").val("");
		$("#produExpl").val("");
		$("#prodSeries").val("");
		$("#bigItemExpl").val("");
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var nextMonth = month-1;
		if(nextMonth==0){
			nextMonth = 12;
			year = year-1;
		}
		var strMonth = nextMonth<10?"0"+nextMonth:nextMonth;
		$("#planMonth").val(year+"-"+strMonth);
		planMonth= $("#planMonth").val();
		$("#endVersion").val(year+"-"+strMonth);
		 getVersionFiels(year,nextMonth);
		 var url = prefix+"/getPadPlanRecordListByPage?planMonth="+planMonth+"&startVersion="+startVersion+"&endVersion="+endVersion;
		 initListTable(url);
	});
});


//初始化表格
function initListTable(url){
	debugger;
   /*var planMonth = $("#planMonth").val();
   var startVersion = $("#startVersion").val();
   var endVersion = $("#endVersion").val();*/
   table.render({
	     elem: '#list-table'
	    ,url:url
	    ,id:"listTableId"
	    ,page:true
	    ,cols: [[
		   {field:'sn', title: '序号',width:40,type:'numbers',align:'center'}
		  ,{field:'rank', title: '排名',width:60,align:'center'}
	      ,{field:'prodSeries', title: '产品系列',width:90,align:'center'}
	      ,{field:'bigItemExpl', title: '大品项',width:100,align:'center'}
	      ,{field:'matName', title: '物料名称',width:140,align:'center'}
	      ,{field:'qtyOne', title: oneName,width:120,align:'center'}
	      ,{field:'qtyTwo', title: twoName,width:120,align:'center'}
	      ,{field:'qtyThree', title: threeName,width:120,align:'center'}
	      ,{field:'qtyFour', title: fourName,width:120,align:'center'}
	      ,{field:'qtyFive', title: fiveName,width:120,align:'center'}
	      ,{field:'qtySix', title: sixName,width:120,align:'center'}
	      ,{field:'qtySeven', title: sevenName,width:120,align:'center'}
	      ,{field:'qtyEight', title: eightName,width:120,align:'center'}
	      ,{field:'qtyNine', title: nineName,width:120,align:'center'}
	      ,{field:'qtyTen', title: tenName,width:120,align:'center'}
	      ,{field:'qtyEleven', title: elevenName,width:120,align:'center'}
	      ,{field:'qtyTwelve', title: twelveName,width:120,align:'center'}
	      ,{field:'matCode', title: '物料编码',width:120,align:'center'}
	      ,{field:'produExpl', title: '产能划分',width:80,align:'center'}
	      
	    ]]
  });
}
//校验日期是否为空
function judgeMonth(){
	   var planMonth = $("#planMonth").val();
	   var startVersion = $("#startVersion").val();
	   var endVersion = $("#endVersion").val();
	   if(planMonth == '' || planMonth == undefined || startVersion == '' || 
			   startVersion == undefined || endVersion == '' || endVersion == undefined){
		   return false;
	   }
	   return true;
}

