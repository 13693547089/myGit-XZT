layui.use([ 'form', 'table' ,'laydate','layer'], function() {
	var $=layui.jquery;
	var prefix="/cutPlan";
	var mateArr=[];
	var table=layui.table;
	var laydate=layui.laydate;
	var layer=layui.layer;
	var form=layui.form;
	var tableIns=[];
	var type=$("#type").val();
	if(type==3){
		disableFormItem();
	}else{
		laydate.render({
		    elem: '#cutMonth',
		    type:'month',
		    btns: ['now', 'confirm'],
		    done: function(value, date){
		    	initLoadTable();
		    	//initCutPlanMateTable(mateArr);
		    	$.post(prefix+"/getCutPlanMateByPlanMonth",{cutMonth:value},function(res){
		    		if(res.judge){
						mateArr=res.data;
						debugger;
						initCutPlanMateTable(mateArr);
						//getCutPlanCodes(value);
					}else{
						layer.alert(res.msg);
						mateArr=[];
						initCutPlanMateTable(mateArr);
						/*var str = '<option value ="" selected="selected">请选择</option>';
						$("#citeCode").html(str);
		    			//重新初始化下拉框
		    			form.render('select');*/
					}
				});
		    	
		    	
		    }
		});
	}
	
	//查询已作废和已提交的打切计划单号集合
	/*function getCutPlanCodes(value){
		$.ajax({
    		type:"post",
    		url:prefix+"/queryCutPlanCodesByCutMonth?cutMonth="+value;
    		dataType:"JSON",
    		error:function(result){
    			layer.msg("程序出错了！",{time: 1000});
    		},
    		success:function(data){
    			if(data != null){
    				var str = '<option value ="" selected="selected">请选择</option>';
    				$.each(data,function(index,row){
    					str+='<option value ="'+row+'">'+row+'</option>'
    				});
    			}
    			$("#citeCode").html(str);
    			//重新初始化下拉框
    			form.render('select');
    		}
    	});
	}*/
	//加载页面初始化表格
	if(type=='1'){
		//initDate();
		var value=$("#cutMonth").val();
		initLoadTable();
		//initCutPlanMateTable(mateArr);
		$.post(prefix+"/getCutPlanMateByPlanMonth",{cutMonth:value},function(res){
			if(res.judge){
				mateArr=res.data;
				debugger;
				initCutPlanMateTable(mateArr);
				//getCutPlanCodes(value);
			}else{
				layer.alert(res.msg);
				mateArr=[];
				initCutPlanMateTable(mateArr);
			}
		});
		$("#id").val(guid());
	}else{
		laodPlanMateTable();
	}
	
	$("#backBtn").click(function(){
		//history.go(-1);
		tuoBack('.cutPlanEdit','#searchBtn');
	});
	
	/**
	 * 初始化创建日期
	 * @returns
	 */
	function initDate(){
		 var id=$('#id').val();
		 if(id==null || id==''){
			 $('#createDate').val(formatTime(new Date(),'yyyy-MM-dd'));
			 $("#cutMonth").val(formatTime(new Date(),'yyyy-MM'));
		 }

	}
	/**
	 * 加载打切计划物料数据
	 * @returns
	 */
	function laodPlanMateTable(){
		var planId=$("#planId").val();
		$.post(prefix+"/getCutPlanMateByPlanId",{planId:planId},function(res){
			mateArr=res;
			initCutPlanMateTable(res)
		});
	}
	//保存事件
	$("#saveBtn").click(function(){
		var status=$("#status").val();
		if(status=='已提交'){
			layer.msg('已提交的计划不能更改',{time:1000});
			return ;
		}
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		//var details=tableIns.config.data;
		 var length = mateArr.length;
		 if(length == 0){
			 layer.msg("物料数据为空，不能保存");
			 return;
		 }
		saveCutPlan();
		//history.go(-1);
		//tuoBack('.cutPlanEdit','#searchBtn');
	});
	//提交事件
	$("#submitBtn").click(function(){
		var status=$("#status").val();
		if(status=='已提交'){
			layer.msg('已提交的计划请勿重复提交',{time:1000});
		}
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		//var details=tableIns.config.data;
		var length = mateArr.length;
		 if(length == 0){
			 layer.msg("物料数据为空，不能提交");
			 return;
		 }
		 var cutMonth =$("#cutMonth").val();
		 $.ajax({
			type:"post",
			url:prefix+"/queryCutPlansByCutMonth?cutMonth="+cutMonth,
			 async:false,//注意
			dataType:"JSON",
			error:function(result){
				layer.msg("程序出错了！",{time: 1000});
			},
			success:function(data){
				if(data.judge){
					$("#status").val('已提交');
					saveCutPlan();
				}else{
					layer.alert(data.msg);
				}
			}
		 });
		//history.go(-1);
		//tuoBack('.cutPlanEdit','#searchBtn');
	});

	/**
	 * 保存备货计划信息
	 * @returns
	 */
	function saveCutPlan(){
		debugger;
		var status=$("#status").val();
		if(status==null || status==''){
			$("#status").val("已保存");
		}
		//var details=tableIns.config.data;

		var detailJson=JSON.stringify(mateArr);
		
		$("#detailJson").val(detailJson);
		var options = {
				url: prefix+"/saveCutPlan",
				type:"POST",
				success: function (msg) {
					if(msg.code=="0"){
						id=msg.data.id;
						$('#id').val(id);	
						$('#cutPlanCode').val(msg.data.cutPlanCode);	
						layer.msg("操作成功！",{time: 1000});
						type=$('#type').val('2');
						tuoBack('.cutPlanEdit','#searchBtn');
					}else{
						layer.msg("操作失败！",{time: 1000});
					}
				},
				error: function(request) {
					layer.msg("程序出错了！",{time: 1000});
				}
		};
		$("#cutPlan-form").ajaxSubmit(options);
	}
	/**
	 * 打切计划表头时间计算
	 * @param cutMonth
	 * @param addMonth
	 * @returns
	 */
	function getMonth(addMonth){
		var report={};
		var cutMonth=$("#cutMonth").val();
		if(cutMonth==null || cutMonth==''){
			return '未知';
		}else{
			var dateStr=cutMonth.replace(/-/g,"/");
			var date = new Date(dateStr+"/1"); 
			var mm = date.getMonth()+addMonth;
			if(mm>12){
				mm=mm-12;
			}if(mm<1){
				mm=mm+12;
			}
			return mm;
		}
		
	}
	
	//引用打切进度
	form.on("select(citeCode)",function(obj){
		//var oldCiteCode = $("#citeCode").val();
		var cutMonth = $("#cutMonth").val();
		var citeCode =obj.value;
		debugger;
		if(citeCode == null || citeCode == ""){
			layer.msg("请选择正确的打切计划单号");
			return;
		}else{
			$.ajax({
				type:"post",
				url:prefix+"/getCutScheOfCutPlanMate",
				data:{
					citeCode:citeCode,
					cutMonth:cutMonth
				},
				dataType:"JSON",
				error:function(result){
					layer.msg("程序出错了！",{time: 1000});
				},
				success:function(data){
					debugger;
					if(data.judge){
						mateArr = data.data;
						initCutPlanMateTable(mateArr);
					}else{
						layer.alert(data.msg);
						mateArr=[];
						initCutPlanMateTable(mateArr);
					}
				}
			});
		}
		
	});
	// table 数据刷新处理
	table.on('tool(planMate-table)', function(obj){
	    var data = obj.data;
	    obj.update({residue:data.residue,replaceDate:data.replaceDate});
	});
	
	table.on('edit(planMate-table)', function(obj){ 
		var field=obj.field;
		var val=obj.value;
		var data=obj.data;
		if(field!='addOne' && field!='addTwo' && field!='addThree' && field!='addFour' && field!='addFive' && field!='addSix'){
			return ;
		}
		if(val==null || val==''){
			data[field]=0;
		}
		if(isNaN(val)){
			layer.msg("请输入有效数字!");
			data[field]=0;
			initCutPlanMateTable(mateArr);
			//$(this).click();
		}
		obj.data=updateDate(data);
		$(this).click();
	});
	
	function updateDate(data){
		var nowInve = data.nowInve;
		var outInve=data.outInve;
		var inveNum=data.inveNum;
		var mainStruNum=data.mainStruNum;
		var addOne=data.addOne;
		var addTwo=data.addTwo;
		var addThree=data.addThree;
		var addFour=data.addFour;
		var addFive=data.addFive;
		var addSix=data.addSix;
		var residue=nowInve+inveNum+mainStruNum-addOne-addTwo-addThree-addFour-addFive-addSix;
		data.residue=residue;
		var remianNum=nowInve+inveNum+mainStruNum;
		
		if(remianNum<=0){
			data.replaceDate=getMonth(0)+"月";
			return data;
		}
		remianNum=remianNum-addOne;
		if(remianNum<=0){
			data.replaceDate=getMonth(1)+"月";
			return data;
		}
		remianNum=remianNum-addTwo;
		if(remianNum<=0){
			data.replaceDate=getMonth(2)+"月";
			return data;
		}
		remianNum=remianNum-addThree;
		if(remianNum<=0){
			data.replaceDate=getMonth(3)+"月";
			return data;
		}
		remianNum=remianNum-addFour;
		if(remianNum<=0){
			data.replaceDate=getMonth(4)+"月";
			return data;
		}
		remianNum=remianNum-addFive;
		if(remianNum<=0){
			data.replaceDate=getMonth(5)+"月";
			return data;
		}
		remianNum=remianNum-addSix;
		if(remianNum<=0){
			data.replaceDate=getMonth(6)+"月";
			return data;
		}
		data.replaceDate='待定';
		return data;

	}
	
	//重置
	$("#resetBtn").click(function(){
		initCutPlanMateTable(mateArr);
	});
	//筛选
	$("#screenBtn").click(function(){
		layer.open({
			  type:2,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
			  title:"物料筛选",
			  shadeClose : false,
			  shade : 0.1,
			  content : '/cutPlan/matRepeat',
			  area : [ '500px', '200px' ],
			  maxmin : false, // 开启最大化最小化按钮
			  btn: ['确认','取消']
		  	  ,yes: function(index, layero){
		  		//按钮【按钮一】的回调
		  		var data = $(layero).find("iframe")[0].contentWindow.getData();
		  		// 关闭弹框
		  		layer.close(index);
		  		debugger;
		  		var matInfo = data.matInfo;
		  		var bigItemExpl = data.bigItemExpl;
		  		
		  		var searchTbData = new Array();
		  		for(var i=0;i<mateArr.length;i++){
	  				var item = mateArr[i];
	  				var matFlag = false;
	  				var itemFlag = false;
	  				if(matInfo != ''){
	  					if(item.mateId != undefined
		  						&& item.mateName != undefined
		  						&& (item.mateId.toUpperCase().indexOf(matInfo.toUpperCase())!= -1 || item.mateName.toUpperCase().indexOf(matInfo.toUpperCase())!= -1)){
	  						matFlag = true;
	  					}
	  				}else{
	  					matFlag = true;
	  				}
	  				if(bigItemExpl != ''){
	  					if(item.bigItemCode != undefined
	  							&& item.bigItemExpl != undefined
	  							&& (item.bigItemCode.toUpperCase().indexOf(bigItemExpl.toUpperCase())!= -1 || item.bigItemExpl.toUpperCase().indexOf(bigItemExpl.toUpperCase())!= -1)){
	  						itemFlag = true;
	  					}
	  				}else{
	  					itemFlag = true;
	  				}
	  				if(matFlag && itemFlag){
	  					searchTbData.push(item);
	  				}
	  			}
		  		// 重新加载数据
		  		initCutPlanMateTable(searchTbData);
			  }
			  ,btn2: function(index, layero){
				  //按钮【按钮二】的回调
				  // 默认会关闭弹框
				  //return false 开启该代码可禁止点击该按钮关闭
			  }
		  });
	});
	/**
	 * 初始化排产计划详情表
	 * @param data
	 * @returns
	 */
	function initCutPlanMateTable(data){
		tableIns=table.render({
			elem : '#planMate-table',
			id:'planMate-table',
			page :true,
			data:data,
			cols : [ [ {
				type : 'numbers',
				fixed : 'left',
				title : '序号'
			},{
				field : 'bigItemExpl',
				width : 99,
				fixed : 'left',
				title : '大品项',
			},{
				field : 'mateName',
				width : 156,
				fixed : 'left',
				title : '物料名称',
			},{
				field : 'isSpecial',
				width : 99,
				fixed : 'left',
				title : '打切品类型',
				templet:function(d){
					if(d.isSpecial=="YES"){
						return "自制打切";
					}else{
						return "OEM打切";
					}
				}
			},{
				field : 'mateVersion',
				width : 92,
				fixed : 'left',
				title : '物料版本',
			},{
				field : 'nowInve',
				width : 180,
				title : getMonth(0)+'月底成品库存+在途',
			},{
				field : 'outInve',
				width : 180,
				title : getMonth(0)+'月底在外量订单',
			},{
				field : 'inveNum',
				width : 132,
				title : '供应商成品库存',
			},{
				field : 'mainStruNum',
				width : 105,
				title : '主包材箱数',
			},{
				field : 'addOne',
				width : 120,
				event : 'calEvent',
				title : getMonth(1)+'月预测',
				edit  : 'text',
			}, {
				field : 'addTwo',
				width : 120,
				event : 'calEvent',
				title : getMonth(2)+'月预测',
				edit  : 'text',
			}, {
				field : 'addThree',
				width : 120,
				event : 'calEvent',
				title : getMonth(3)+'月预测',
				edit  : 'text',
			},{
				field : 'addFour',
				width : 120,
				event : 'calEvent',
				title : getMonth(4)+'月预测',
				edit  : 'text',
			},{
				field : 'addFive',
				width : 120,
				event : 'calEvent',
				title : getMonth(5)+'月预测',
				edit  : 'text',
			},{
				field : 'addSix',
				width : 120,
				event : 'calEvent',
				title : getMonth(6)+'月预测',
				edit  : 'text',
			},{
				field : 'residue',
				width : 120,
				title : '剩余量',
			},{
				field : 'replaceDate',
				width : 120,
				title : '预计替换时间',
			},{
				field : 'cutGoal',
				width : 120,
				title : '打切目的',
				edit  :'text'
			},{
				field : 'cutSche',
				width : 120,
				title : '打切进度',
				edit  :'text'
			},{
				field : 'remark',
				width : 120,
				title : '备注',
				edit  :'text'
			},{
				field : 'mateId',
				width : 143,
				title : '物料编码',
			}] ],
			done: function(res, curr, count){
				//如果是异步请求数据方式，res即为你接口返回的信息。
			    //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
			    //console.log(res);
			    //得到当前页码
			    //console.log(curr); 
			    //得到数据总量
			    //console.log(count);
				var data = res.data;
				if(data.length>0){
					var nowInve = 0;
					var outInve = 0;
					var inveNum = 0;
					var mainStruNum = 0;
					var addOne = 0;
					var addTwo = 0;
					var addThree = 0;
					var addFour = 0;
					var addFive = 0;
					var addSix = 0;
					var residue = 0;
					for(var i=0;i<data.length;i++){
						  var item = data[i];
						  nowInve += (item.nowInve==null||isNaN(item.nowInve)||item.nowInve=='')?0:parseFloat(item.nowInve);
						  outInve += (item.outInve==null||isNaN(item.outInve)||item.outInve=='')?0:parseFloat(item.outInve);
						  inveNum += (item.inveNum==null||isNaN(item.inveNum)||item.inveNum=='')?0:parseFloat(item.inveNum);
						  mainStruNum += (item.mainStruNum==null||isNaN(item.mainStruNum)||item.mainStruNum=='')?0:parseFloat(item.mainStruNum);
						  addOne += (item.addOne==null||isNaN(item.addOne)||item.addOne=='')?0:parseFloat(item.addOne);
						  addTwo += (item.addTwo==null||isNaN(item.addTwo)||item.addTwo=='')?0:parseFloat(item.addTwo);
						  addThree += (item.addThree==null||isNaN(item.addThree)||item.addThree=='')?0:parseFloat(item.addThree);
						  addFour += (item.addFour==null||isNaN(item.addFour)||item.addFour=='')?0:parseFloat(item.addFour);
						  addFive += (item.addFive==null||isNaN(item.addFive)||item.addFive=='')?0:parseFloat(item.addFive);
						  addSix += (item.addSix==null||isNaN(item.addSix)||item.addSix=='')?0:parseFloat(item.addSix);
						  residue += (item.residue==null||isNaN(item.residue)||item.residue==''||parseFloat(item.residue)<0)?0:parseFloat(item.residue);
					}
					
					var sumRow2 = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
				      '<td></td>'+
				      '<td></td>'+
				      '<td></td>'+
				      '<td>总计</td></tr>';

					var sumRow = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
					  '<td></td>'+
					  '<td></td>'+
					  '<td></td>'+
					  '<td>总计</td>'+
					  '<td>'+nowInve.toFixed(2)+'</td>'+
					  '<td>'+outInve.toFixed(2)+'</td>'+
					  '<td>'+inveNum.toFixed(2)+'</td>'+
					  '<td>'+mainStruNum.toFixed(2)+'</td>'+
					  '<td>'+addOne.toFixed(2)+'</td>'+
					  '<td>'+addTwo.toFixed(2)+'</td>'+
					  '<td>'+addThree.toFixed(2)+'</td>'+
					  '<td>'+addFour.toFixed(2)+'</td>'+
					  '<td>'+addFive.toFixed(2)+'</td>'+
					  '<td>'+addSix.toFixed(2)+'</td>'+
					  '<td>'+residue.toFixed(2)+'</td>'+
					  '<td></td>'+
					  '<td></td>'+
					  '<td></td>'+
					  '<td></td>'+
					  '<td></td></tr>';
					  $('.layui-table-fixed .layui-table tbody').append(sumRow2);
					  $('.layui-table-main .layui-table tbody').append(sumRow);
				}
				
			}
		});
		if(type == '3'){
			 $('table td').removeAttr('data-edit');
		}
	}
	
	
	
	function initLoadTable(){
		data=[];
		tableIns=table.render({
			elem : '#planMate-table',
			id:'planMate-table',
			data:data,
			page :true,
			text:{none:'数据加载中'},
			cols : [ [ {
				type : 'numbers',
				fixed : 'left',
				title : '序号'
			},{
				field : 'bigItemExpl',
				width : 99,
				fixed : 'left',
				title : '大品项',
			},{
				field : 'mateName',
				width : 156,
				fixed : 'left',
				title : '物料名称',
			},{
				field : 'mateVersion',
				width : 92,
				fixed : 'left',
				title : '物料版本',
			},{
				field : 'nowInve',
				width : 180,
				title : getMonth(0)+'月底成品库存+在途',
			},{
				field : 'outInve',
				width : 180,
				title : getMonth(0)+'月底在外量订单',
			},{
				field : 'inveNum',
				width : 132,
				title : '供应商成品库存',
			},{
				field : 'mainStruNum',
				width : 105,
				title : '主包材箱数',
			},{
				field : 'addOne',
				width : 120,
				title : getMonth(1)+'月预测',
				edit  : 'text',
			}, {
				field : 'addTwo',
				width : 120,
				title : getMonth(2)+'月预测',
				edit  : 'text',
			}, {
				field : 'addThree',
				width : 120,
				title : getMonth(3)+'月预测',
				edit  : 'text',
			},{
				field : 'addFour',
				width : 120,
				title : getMonth(4)+'月预测',
				edit  : 'text',
			},{
				field : 'addFive',
				width : 120,
				title : getMonth(5)+'月预测',
				edit  : 'text',
			},{
				field : 'addSix',
				width : 120,
				title : getMonth(6)+'月预测',
				edit  : 'text',
			},{
				field : 'residue',
				width : 120,
				title : '剩余量',
			},{
				field : 'replaceDate',
				width : 120,
				title : '预计替换时间',
			},{
				field : 'cutGoal',
				width : 120,
				title : '打切目的',
				edit  :'text'
			},{
				field : 'cutSche',
				width : 120,
				title : '打切进度',
				edit  :'text'
			},{
				field : 'remark',
				width : 120,
				title : '备注',
				edit  :'text'
			},{
				field : 'mateId',
				width : 143,
				title : '物料编码',
			}] ]
		});
	}
	
})

