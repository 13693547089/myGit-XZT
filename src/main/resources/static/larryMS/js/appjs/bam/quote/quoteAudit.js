	//地址前缀
var prefix = "/quote";
var id='';
var quoteCode="";
var type="1";
var selectedRow={};
var suppId="";
var mateList=[];
var mateId="";
var viewMateId='';
//定义树形控件
layui.use([ 'table','upload','layer','form','laydate'], function() {
	var $ = layui.jquery;
	var upload = layui.upload;
	var layer=layui.layer;
	var form=layui.form;
	var table = layui.table;
	var laydate=layui.laydate;
	var tablelns=[];
	type=$('#type').val();
	var userType = $("#userType").val();
	var quoteStatus='';
	var pagePattern = $("#pagePattern").val();
	if(pagePattern == 'read'){//查看已办任务
		  disableFormItem();
		  type =3;
		  $("#type").val(3);
	}
	if(type==3 || type==4){
		disableFormItem();
	}
	if(type==4){
		//日期控件格式化
		laydate.render({
			elem : '#validStart'
		});
		laydate.render({
			elem : '#validEnd'
		});
	}
	var status = $("#status").val();
	if(status == '未审核' || status == '初审核'){
		disableFormItem();
	}
	/*if(status == "已保存"){//保存，发布，返回
		$("#publishBtn2").hide();
		$("#publishBtn3").hide();
		$("#backBtn").hide();
	}else if(status == "未审核"){//保存，初审，回退，返回
		//$("#saveBtn").hide();
		$("#publishBtn").hide();
		$("#publishBtn3").hide();
		//$(".disableBtn").hide();
		disableFormItem();
	}else if(status == "初审核"){//OA审核，回退，返回
		$("#saveBtn").hide();
		$("#publishBtn").hide();
		$("#publishBtn2").hide();
		disableFormItem();
	}*/
	
	var menus=[];
	quoteCode=$("#quoteCode").val();
	
	id=$("#id").val();
	suppId=$("#suppNo").val();
	if(id==null || id==''){
		initQuotePriceTable([]);
		initMateTable([]);
	}else{
		$.ajax({
			url:prefix+"/getQuoteMateByQuoteCode",
			data:{quoteCode:quoteCode},
			async:false,//注意
			success:function(msg){
				mateList=msg;
				initMateTable(msg);
				if(msg!=null && msg.length>0){
					initQuotePriceTable(msg[0].priceList);
					$("#mateRemark").val(msg[0].remark);
				}else{
					initQuotePriceTable([]);
				}
			}
		});
		$.ajax({
			url:prefix+"/getQuoteAttrByQuoteCode",
			data:{quoteCode:quoteCode},
			async:false,//注意
			success:function(res){
				initQuoteAttrTable(res);
			}
		});
	}
	//修改有效期的保存点击事件
	$('#validDateBtn').click(function(){
		if(mateList==null || mateList.length==0){
			layer.msg('供应商没有维护物料数据！',{time:1000});
			return false;
		}
		var flag=true
		$.each(mateList,function(index,row){
			var startDate=row.startDate;
			var endDate=row.endDate;
			if(startDate==null || startDate=='' ||endDate==null || endDate==''){
				flag=false;
			}
		});
		if(!flag){
			layer.msg('请维护有效期后保存！',{time:1000});
			return false;
		}
		$.ajax({
			type:"POST",
			data:{mateJson:JSON.stringify(mateList)},
			url:prefix+"/updateValidDate",
			success:function(res){
				var code=res.code;
				if(code==0){
					layer.msg('操作成功',{time:1000});
				}
			}
		});
		return;
	});
	//初始化报价表格的方法
	function initQuotePriceTable(data){
		   data=calculateToatal(data);
		 tablelns=table.render({
			     elem: '#price-table'
			    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
			    ,data:data
			    ,limit:90
			    ,cols: [[
				      {field:'segmCode', title: '段号',width:'5%',fixed:'left'}
				      ,{field:'segmName', title: '段名',width:'5%',fixed:'left'}
				      ,{field:'asseCode', title: '组件编号',width:'7%',fixed:'left'}
				      ,{field:'asseName', title: '组件名称',width:'7%',fixed:'left'}
				      ,{field:'mateCode', title: '物料编号',width:'11%'}
				      ,{field:'unitPrice', title: '单价',width:'7%',edit: 'text'}
				      ,{field:'detailsNum', title: '用量',width:'5%'}
				      ,{field:'detailUnit', title: '单位',width:'5%'}
				      ,{field:'detailPrice', title: '价格',width:'8%'}
				      ,{field:'matePrice', title: '原料报价',width:'8%',edit: 'text'}
				      ,{field:'mateUnit', title: '原料报价单位',width:'10%',edit: 'text'}
				      ,{field:'standard', title: '规格',width:'10%'}
				      ,{field:'material', title: '材料',width:'10%'}
				      ,{field:'remark', title: '备注',width:'18%',edit: 'text'}
			    ]]
			  ,page:false
		  });
		 if(status!="已保存" && status!="已退回"){
			 $('td').removeAttr('data-edit');
		 }
	}
	initDate();
	 //指定允许上传的文件类型
	upload.render({
	   elem: '#addQuoteBase'
	   ,url: '/doc/docUpload?direCode='+'BJD'+'&linkNo='+quoteCode+"&docCate="+'bam_quote'
	   ,accept: 'file' //普通文件
	   ,done: function(res){
	      if(res.code=='0'){
	    	  var file=res.data[0];
	    	  $("#quoteBase").val(file.id);
	    	  $("#filename").html(file.realName);
	      }
	    }
	 });
	//指定允许上传的文件类型
	upload.render({
		elem: '#addQuoteBaseTwo'
			,url: '/doc/docUpload?direCode='+'BJD'+'&linkNo='+quoteCode+"&docCate="+'bam_quote'
			,accept: 'file' //普通文件
				,done: function(res){
					if(res.code=='0'){
						var file=res.data[0];
						$("#quoteBaseTwo").val(file.id);
						$("#filenameTwo").html(file.realName);
					}
				}
	});
	function initDate(){
		 //创建时这是创建时间合有效开始时间与结束时间
		 var id=$('#id').val();
		 if(id==null || id==''){
			 $('#quoteDate').val(formatTime(new Date(),'yyyy-MM-dd'));
		 }
	}
	//返回
	$("#goBackBtn").click(function(){
		 window.history.go(-1);
	});
	//保存
	$("#saveBtn").click(function(){
		var status = $("#status").val();
		if(status =='未审核'){
			var judge = saveAttr();
		}else{
			if(userType == 'user'){
				$("#quoteBase").attr("class","noMust");
				$("#quoteBaseTwo").attr("class","noMust");
			}
			var result=checkMust();
			if(result.flag==false){
				layer.msg(result.msg,{time:1000});
				return;
			}
			var judge = saveQuote();
		}
	});
	//发布
	$("#publishBtn").click(function(){
		if(userType == 'user'){
			$("#quoteBase").attr("class","noMust");
			$("#quoteBaseTwo").attr("class","noMust");
		}
		var result=checkMust();
		if(result.flag==false){
			layer.msg(result.msg,{time:1000});
			return;
		}
		var id = $("#id").val();
		var taskName = $("#taskName").val();
		var processCode = $("#processCode").val();
		//$("#status").val("未审核");
		$("#status").val("已保存");
		if(userType == "supplier"){
			quoteStatus = '未审核'
		}else{
			quoteStatus = '初审核'
			var verdict = validDate();
			if(!verdict){
				layer.msg("请维护物料的有效期",{time:2000});
				return;
			}
			/*var attrData=attrTableLns.config.data;
	  	  	if(attrData!=null && attrData.length==0){
	  	  		layer.msg("请上传附件信息",{time:1000});
	  	  		return;
	  	  	}*/
		}
		var judge = saveQuote();
		if(judge){
			var flag = taskProcess(id, processCode, taskName, "process");
		}
		/*if(userType == "supplier"){
			var flag = taskProcess(id, processCode, taskName, "process");
		}else{
			var flag = taskProcess(id, processCode, taskName, "save");
			if(flag=="init_success"){
				var quoteCodes=[];
				quoteCodes.push(quoteCode);
				updateQuoteStatus("未审核",quoteCodes);
			}else{
				layer.msg("任务生成失败");
			}
		}*/
		/*if(flag == "process_success"){
		  var judge = saveQuote();
		}*/
	});
	//检验物料有效期
	function validDate(){
		var count=0;
		$.each(mateList,function(index,item){
			var startDate = item.startDate;
			var endDate = item.endDate;
			if(startDate == undefined || startDate =='' || endDate == undefined || endDate == ''){
				count++;
			}
		});
		if(count != 0){
			return false;
		}else{
			return true;
		}
	}
	window.returnFunction = function() {
		var quoteCodes=[];
		quoteCodes.push(quoteCode);
		updateQuoteStatus(quoteStatus,quoteCodes);
	}
	//初审核
	$("#publishBtn2").click(function(){
		/*var quoteCodes = [];
		var quoteCode = $("#quoteCode").val();
		quoteCodes.push(quoteCode);
		//判断所有的是否所有的报价单都维护了有效期，采购是否上传附件信息
		var msg="";
		var flag=true;
		$.ajax({
			type:"POST",
			async:false,
			data:{quoteJson:JSON.stringify(quoteCodes)},
			url:prefix+"/validDate",
			success:function(res){
				var code=res.code;
				if(code==0){
					layer.msg('操作成功',{time:1000});
				}else{
					flag=false;
					msg=res.msg;
				}
			}
		});
		if(!flag){
			layer.msg(msg,{time:1000});
			return;
		}*/
		var id = $("#id").val();
		var taskName = $("#taskName").val();
		var processCode = $("#processCode").val();
		quoteStatus = '初审核';
		var judge = saveAttr();
		if(judge){
			var flag = taskProcess(id, processCode, taskName, "process");
		}
		/*if(flag == "process_success"){
			updateQuoteStatus("初审核",quoteCodes);
		}*/
	});
	//初审核报价单，修改有效期,上传附件信息的保存点击事件
	function saveAttr(){
			if(mateList==null || mateList.length==0){
				layer.msg('供应商没有维护物料数据！',{time:1000});
				return false;
			}
			var flag=true
			$.each(mateList,function(index,row){
				var startDate=row.startDate;
				var endDate=row.endDate;
				if(startDate==null || startDate=='' ||endDate==null || endDate==''){
					flag=false;
				}
			});
			if(!flag){
				layer.msg('请维护有效期后保存！',{time:1000});
				return false;
			}
	  	  	var attrData=attrTableLns.config.data;
	  	    $.each(attrData,function(index,item){
		  		var index = item.LAY_TABLE_INDEX;
		  		item.attrIndex = index;
		  	})
	  	  	/*if(attrData!=null && attrData.length>5){
	  	  		layer.msg("附件数量不能超过5个！",{time:1000});
	  	  		return false;
	  	  	}
	  	    if(attrData!=null && attrData.length==0){
	  	  		layer.msg("请上传附件信息",{time:1000});
	  	  		return;
	  	  	}*/
	  	  	var judge = false;
			$.ajax({
				type:"POST",
				data:{mateJson:JSON.stringify(mateList),attrJson:JSON.stringify(attrData),quoteCode:quoteCode},
				url:prefix+"/updateValidDate",
				async:false,//注意
				success:function(res){
					var code=res.code;
					if(code==0){
						layer.msg('操作成功',{time:1000});
						judge = true;
					}else{
						judge = false;
					}
				}
			});
			return judge;
	}
	//OA审核
	$("#publishBtn3").click(function(){
		var id = $("#id").val();
		var taskName = $("#taskName").val();
		var processCode = $("#processCode").val();
		var quoteCodes = [];
		var quoteCode = $("#quoteCode").val();
		var status = $("#status").val();
		var	flag ="";
		if(status == '初审核'){
			flag = taskProcess(id, processCode, taskName, "process");
		}else if(status == 'OA待审核'){
			flag =="over_success";
		}else{
			layer.msg("只有初审核状态的报价单才能提交OA");
			return;
		}
		if(flag == "over_success"){
			quoteCodes.push(quoteCode);
			$.ajax({
				type:'POST',
				url:prefix+"/submitQuoteToOA",
				data:{jsonCodes:JSON.stringify(quoteCodes)},
				async:false,//注意
				success:function(res){
					//reloadTable();
					var code =res.code;
					if(code==0){
						window.history.go(-1);
						layer.msg("提交OA成功！",{time:1000});
						$("#status").val("OA审核");
					}else{
						layer.msg(res.msg,{time:2000});
						$("#status").val("OA待审核");
					}
				},
				error:function(){
					layer.msg("程序异常！",{time:1000});
					$("#status").val("OA待审核");
				}
			});
		}
	});
	//退回
	$("#backBtn").click(function(){
		var id = $("#id").val();
		var quoteCodes = [];
		var quoteCode = $("#quoteCode").val();
		var result = backProcess(id);
		if(result == "back_success"){
			quoteCodes.push(quoteCode);
			updateQuoteStatus("已退回",quoteCodes);
		}
	});
	/**
	  * 更新报价单状态
	  * @param status
	  * @param quoteCodes
	  * @returns
	  */
	 function updateQuoteStatus(status,quoteCodes){
		 $.ajax({
				type:'POST',
				url:prefix+"/changeQuoteStatus",
				data:{codeJson:JSON.stringify(quoteCodes),status:status},
				success:function(msg){
					window.history.go(-1);
					layer.msg("操作成功！",{time:1000});
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
		});
	 }
	function saveQuote(){
		var attrData=attrTableLns.config.data;
		 $.each(attrData,function(index,item){
		  		var index = item.LAY_TABLE_INDEX;
		  		item.attrIndex = index;
		 })
  	  	/*if(attrData!=null && attrData.length>5){
  	  		layer.msg("附件数量不能超过5个！",{time:1000});
  	  		return false;
  	  	}*/
		var suppNo=$("#suppNo").val();
		/*if(suppNo==null || suppNo==''){
			layer.msg('请选择供应商！',{time:1000});
			return;
		}*/
		var mateJson=JSON.stringify(mateList);
		var attrJson=JSON.stringify(attrData);
		$("#mateJson").val(mateJson);
		$("#attrJson").val(attrJson);
//		alert(mateJson);
//		return ;
		var judge =false;
		var options = {
				url: prefix+"/saveQuote",
				type:"POST",
				async:false,//注意
				success: function (msg) {
					if(msg.code=="0"){
						id=msg.quote.id;
						$('#id').val(id);	
						$('#quoteCode').val(msg.quote.quoteCode);	
						layer.msg("操作成功！",{time: 1000});
						judge=true;
						type=$('#type').val('2');
					}else{
						layer.msg("操作失败！",{time: 1000});
						judge=false;
					}
				},
				error: function(request) {
					layer.msg("程序出错了！",{time: 1000});
				}
		};
		$("#quoteForm").ajaxSubmit(options);
		return judge;
	}
	
	form.on('select(suppNo)', function(data){
		 suppId=$("#suppNo").val();
		 var suppName=$(data.elem).find("option:selected").text();
		 $("#suppName").val(suppName.substr(suppId.length));
		 $.ajax({
				url:prefix+"/getQuoteMateByQuoteCode",
				data:{quoteCode:quoteCode},
				success:function(msg){
					mateList=msg;
					initMateTable(msg);
					if(msg!=null && msg.length>0){
						initQuotePriceTable(msg[0].priceList);
						$("#mateRemark").val(msg[0].remark);
					}
				}
			});
	});
	form.on('select(quoteType)', function(data){
		var quoteTypeDesc=$(data.elem).find("option:selected").text();
		$("#quoteTypeDesc").val(quoteTypeDesc);
	});
	//模板表格初始化
	function initMateTable(data) {
		$('#mate-table').bootstrapTable('destroy');
		var userType=$('#userType').val();
		if(userType=='supplier'){
			$('#mate-table').bootstrapTable({
				method: 'get',
				editable:true,//开启编辑模式
				undefinedText:'',
				pageList: [10,20],  
				pageSize:10,  
				pageNumber:1, 
				striped: true,  
				columns: [
			      {checkbox: true},
			      {
			    	  field:"mateNo",
			    	  edit:{
			    		  type:'select',//下拉框
			    		  required:true,
			    		  url:prefix+'/getMateBySuppNo?suppId='+suppId,
			    		  valueField:'mateId',
			    		  textField:'skuEnglish',
			    		  onSelect:function(val,rec){
			    			  
			    			  //下拉是重新加载价格信息列表
		    				  $.ajax({
		    					  url:prefix+"/getQuotePriceByMateId",
		    					  data:{mateId:val},
		    					  success:function(data){
		    						  initQuotePriceTable(data);
		    						  selectedRow.mateNo=val;
		    						  selectedRow.priceList=data;
		    						  updateMateList(selectedRow);
		    						  reloadPriceAndRemark(selectedRow);
		    					  }
		    				  });
			    		  }
			    	  },
			    	  title:"物料",
			    	  align:"center"
			      },
			      {
			    	  field:"suppScope",
			    	  width:"20%",
			    	  edit:{
			    		  type:'select',//下拉框
			    		  url:prefix+'/queryQualProcBySuppId?suppId='+suppId,
			    		  valueField:'suppRange',
			    		  textField:'suppRangeDesc',
			    		  onSelect:function(val,rec){
			    			  selectedRow.suppScope=val;
			    			  updateMateList(selectedRow);
			    		  }
			    	  },
			    	  title:"供应商范围",
			    	  align:"center"
			      },
			      {field: 'status',title: '操作',align:"center",edit:false,
	                  formatter : function(value, row,  index) {
							var editStr='<a class="layui-btn layui-btn-xs blueInline" style="text-decoration:none;" onclick="showMateDetail(\''+row.mateNo+'\','+index+') ">查看</a>';
							return editStr;
						}
	              }
			      ],
			      data:data,
			      onClickRow:function(	row, $element){
			    	  selectedRow=row
			    	  reloadPriceAndRemark(selectedRow);
			      }
			});
		}else{
			$('#mate-table').bootstrapTable({
				method: 'get',
				editable:true,//开启编辑模式
				undefinedText:'',
				pageList: [10,20],  
				pageSize:10,  
				pageNumber:1, 
				striped: true,  
				columns: [
			      {checkbox: true},
			      {
			    	  field:"mateNo",
			    	  edit:{
			    		  type:'select',//下拉框
			    		  required:true,
			    		  url:prefix+'/getMateBySuppNo?suppId='+suppId,
			    		  valueField:'mateId',
			    		  textField:'skuEnglish',
			    		  onSelect:function(val,rec){
			    			  
			    			  //下拉是重新加载价格信息列表
		    				  $.ajax({
		    					  url:prefix+"/getQuotePriceByMateId",
		    					  data:{mateId:val},
		    					  success:function(data){
		    						  initQuotePriceTable(data);
		    						  selectedRow.mateNo=val;
		    						  selectedRow.priceList=data;
		    						  updateMateList(selectedRow);
		    						  reloadPriceAndRemark(selectedRow);
		    					  }
		    				  });
			    		  }
			    	  },
			    	  title:"物料",
			    	  align:"center"
			      },
			      {
			    	  field:"suppScope",
			    	  width:"20%",
			    	  edit:{
			    		  type:'select',//下拉框
			    		  url:prefix+'/queryQualProcBySuppId?suppId='+suppId,
			    		  valueField:'suppRange',
			    		  textField:'suppRangeDesc',
			    		  onSelect:function(val,rec){
			    			  selectedRow.suppScope=val;
			    			  updateMateList(selectedRow);
			    		  }
			    	  },
			    	  title:"供应商范围",
			    	  align:"center"
			      },
			      {field:"startDate",title:"有效期始",align:"center",  edit:{ type:"date", required:true, },visible:true },
			      {field:"endDate",title:"有效期止",align:"center", edit:{ type:"date", required:true, },visible:true },
			      {field:"mateReason",title:"原因",align:"center",visible:true ,width:"20%",},
			      {field: 'status',title: '操作',align:"center",edit:false,
	                  formatter : function(value, row,  index) {
							var editStr='<a class="layui-btn layui-btn-xs blueInline" style="text-decoration:none;" onclick="showMateDetail(\''+row.mateNo+'\','+index+') ">查看</a>';
							return editStr;
						}
	              }
			      ],
			      data:data,
			      onClickRow:function(	row, $element){
			    	  selectedRow=row
			    	  reloadPriceAndRemark(selectedRow);
			      }
			});
		}
		if(status=='初审核'){
			$('td').bind('click',function(){
				$('tr').removeClass('editable-select');
				return false;
			});
		}else if(status=='未审核'){
			$('tbody').children('tr').children('td').eq(1).bind('click',function(){
				$('tr').removeClass('editable-select');
				return false;
			});
			$('tbody').children('tr').children('td').eq(2).bind('click',function(){
				$('tr').removeClass('editable-select');
				return false;
			});
		}
	} 
	
	$("#filename").click(function(){
		var docId=$('#quoteBase').val();
		window.location.href="/doc/downLoadDoc?docId="+docId
	});
	$("#filenameTwo").click(function(){
		var docId=$('#quoteBaseTwo').val();
		window.location.href="/doc/downLoadDoc?docId="+docId
	});
	
	/**
	 * 打开物料信息弹框
	 * @param mateId
	 * @returns
	 */

	table.on('edit(price-table)', function(obj){ //注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"
		var field=obj.field;
		if(field!='unitPrice'){
			return;
		}
		var data=tablelns.config.data;
		var newData=[];
		$.each(data,function(index,row){
			var segmName=row.segmName;
			if(segmName.indexOf('小计')==-1 && segmName.indexOf('出厂价')==-1 && segmName.indexOf('合计')==-1){
				newData.push(row);
			}
		});
		initQuotePriceTable(newData);
	});
	
	/**
	 * 数据变化是更新数据源信息
	 * @param row
	 * @returns
	 */
	function updateMateList(row){
		$.each(mateList,function(index,mate){
			if(mate.id==row.id){
				mateList.splice(index,1);
				mateList[index]=row;
				//mateList.push(row);
			}
		})
		
	}
	
	/**
	 * 重新加载价格信息和备注信息
	 * @param row
	 * @returns
	 */
	function reloadPriceAndRemark(row){
		initQuotePriceTable(selectedRow.priceList);
		$("#mateRemark").val(row.remark)
	}
	/**
	 * 物料备注信息失去焦点时更新数据源
	 */
	$("#mateRemark").blur(function(){
		selectedRow.remark=$("#mateRemark").val();
		updateMateList(selectedRow);
	});
	/**
	 * 添加物料
	 */
	$("#addMateBtn").click(function(){
		var row={id:new Date().getTime(),priceList:[]};
		$('#mate-table').bootstrapTable('append',row)
		mateList.push(row);
	});
	/**
	 * 删除物料
	 */
	$("#delMateBtn").click(function(){
		var ids=[];
		var rows=$('#mate-table').bootstrapTable('getAllSelections');
		$.each(rows,function (index,row){
			ids.push(row.id);
		});
		$('#mate-table').bootstrapTable('remove', {field: 'id', values: ids});
		$.each(mateList,function(index,mate){
			var num=$.inArray(mate.id, ids); 
			if(num>-1){
				mateList.splice(index,1);
			}
		})
	});
	//重新计算小计和总价
	function calculateToatal(data){
		var newData=[];
		var subTotal=0;
		var exFactoryPrice=0;
		var exFlag=false;
		if(data==null || data.length==0){
			return data;
		}
		var segmCode=data[0].segmCode;
		var total=0;
		for(var i=0;i<data.length;i++){
			var currRow=data[i];
			var dNum= currRow.detailsNum;
			if(dNum==null || dNum==''){
				dNum=1;
			}
			var detailsNum=parseFloat(dNum);
			var duPrice=currRow.unitPrice;
			if(duPrice==null || duPrice==''){
				duPrice=0;
			}
			var unitPrice=parseFloat(duPrice);
			var currPrice=detailsNum*unitPrice;
			currRow.detailPrice=currPrice.toFixed(4);
			if(currPrice==null || currPrice==''){
				currPrice=0;
			}
			total+=parseFloat(currPrice);
			var currCode=currRow.segmCode;
			
			if(currCode!=segmCode){
				var subData={};
				subData.segmName='小计';
				subData.detailPrice=subTotal.toFixed(4);
				subTotal=0;
				newData.push(subData);
				segmCode=currCode;
				if(currCode==60 && !exFlag){
					var exFactoryPriceData={};
					exFactoryPriceData.segmName='出厂价';
					exFactoryPriceData.detailPrice=exFactoryPrice.toFixed(4);
					newData.push(exFactoryPriceData);
					exFlag=true;
				}
			}
			subTotal+=parseFloat(currPrice);
			newData.push(currRow);
			if(i==data.length-1){
				if( currCode!='60'){
					var subData={};
					subData.segmName='小计';
					subData.detailPrice=subTotal.toFixed(4);
					newData.push(subData);
				}
				if(!exFlag){
					var exFactoryPriceData={};
					exFactoryPriceData.segmName='出厂价';
					exFactoryPriceData.detailPrice=exFactoryPrice.toFixed(4);
					newData.push(exFactoryPriceData);
					exFlag=true;
				}
			}
			if(!exFlag){
				exFactoryPrice+=parseFloat(currPrice);
			}
			if(currCode==60){
				var subTotal60Data={};
				subTotal60Data.segmName=currRow.asseCode+'小计';
				subTotal60Data.detailPrice=(parseFloat(exFactoryPrice)+parseFloat(currRow.detailPrice)).toFixed(4);
				newData.push(subTotal60Data);
			}

		}
//		var totalData={};
//		totalData.segmName='合计';
//		totalData.detailPrice=total.toFixed(4);
//		newData.push(totalData);
		return newData;
	}
	
	//-------------------报价单附件列表--------------------------
	/**
	 * 初始化报价单附件表格
	 * @param data
	 * @returns
	 */
	function initQuoteAttrTable(data){
		var operateBar="#operateBar1";
		var status=$("#status").val();
		if( status == '未审核' || status=='已保存' && userType=='user' || status=='已退回' && userType=='user'){
			operateBar="#operateBar";
		}
		attrTableLns=table.render({
			     elem: '#attr-table'
			    ,cellMinWidth: 100
			    ,data:data
			    ,limit:90
			    ,cols: [[
			      {type: 'numbers', fixed:'left',title: '序号',field:'attrIndex'},
			      {field:'attrFile', title: '附件名称',width:'40%',
			    	  templet: function(d){
			    		  var fileStr=d.attrFile;
			    		  var file=JSON.parse(fileStr);
			    		  return file.realName;
			    	  }  
			      },
			      {field:'remark', title: '附件备注',width:'40%',edit:'text'},
			      {field:'tools', title: '操作',width:'18%',toolbar:operateBar}
			    ]]
			  ,page:false
		  });
		/* if(type!=4 &&  status!='未审核'){
			 $('td').removeAttr('data-edit');
		 }*/
		if( status == '未审核' || status=='已保存' && userType=='user' || status=='已退回' && userType=='user'){
			
		}else{
			 $('td').removeAttr('data-edit');
		}
	}
	/**
	 * 附件上传添加按钮
	 */
	upload.render({
		   elem: '#addAttrBtn'
		   ,url: '/doc/docUpload?direCode='+'BJD'+'&linkNo='+quoteCode+"&docCate="+'bam_quote'
		   ,accept: 'file' //普通文件
		   ,done: function(res){
		      if(res.code=='0'){
		    	  var file=res.data[0];
		    	  var data=attrTableLns.config.data;
		    	  var attr={};
		    	  attr.id=file.id;
		    	  attr.remark='';
		    	  attr.attrFile=JSON.stringify(file);
		    	  data.push(attr);
		    	  if(data.length >= 5){
		    		 $("#addAttrBtn").hide(); 
		    		 layer.alert("添加附件最多只能添加5个。");
		    	  }
		    	  initQuoteAttrTable(data);
		      }
		    }
		});
	
	/**
	 * 附件表格的工具条点击事件
	 */
	table.on('tool(attr-table)', function(obj){ 
		  var mydata = obj.data; 
		  var layEvent = obj.event; 
		  if(layEvent === 'downLoad'){ //下载
				window.location.href="/doc/downLoadDoc?docId="+mydata.id
		  } else if(layEvent === 'del'){ //删除
	    	  var data=attrTableLns.config.data;
	    	  for (var i = data.length-1; i >=0 ; i--) {
				var row =data[i];
				if(row.id==mydata.id){
					var docIds=[];
					docIds.push(row.id);
					$.post(prefix+"/delFile",{docIds:JSON.stringify(docIds)},function(res){
					});
					data.splice(i,1);
				}
	    	  }
	    	  if(data.length <5){
	    		  $("#addAttrBtn").show();
	    	  }
	    	  initQuoteAttrTable(data);
		   } 
		});
	//---------------------------------------------
	
	
	
	Date.prototype.Format = function (fmt) { //author: meizz   
	    var o = {  
	        "M+": this.getMonth() + 1, //月份   
	        "d+": this.getDate(), //日   
	        "h+": this.getHours(), //小时   
	        "m+": this.getMinutes(), //分   
	        "s+": this.getSeconds(), //秒   
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度   
	        "S": this.getMilliseconds() //毫秒   
	    };  
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
	    for (var k in o)  
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
	    return fmt;  
	} 
 	function timeFormatter(value, row, index){
 		if(value==null){
 			return "";
 		}else if(value==""){
 			return "";
 		}else{
			return new Date(value).Format("yyyy-MM-dd hh:mm:ss");  
 		}
	}
 	function dateFormatter(value, row, index){
 		if(value==null){
 			return "";
 		}else if(value==""){
 			return "";
 		}else{
 			return new Date(value).Format("yyyy-MM-dd");  
 		}
 	}
});
function showMateDetail(mateId,index){
	if(mateId==null || mateId==''){
		mateId=mateList[index].mateNo;
	}
	layer.open({
		  type: 2,
		  shade: false,
		  title: '物料信息',
		  area: ['800px','500px'],
		  maxmin: true,
		  content: prefix+'/getMateInfoPage?mateId='+mateId,
		  success: function(layero){
			  
		  }
		}); 
}


