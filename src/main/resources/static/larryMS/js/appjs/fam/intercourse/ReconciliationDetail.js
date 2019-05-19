var table;
var tableData=[];
var upload;
var paperData = [];
var paperData2 = [];
var tableDatea;
var tableDatea2;
var paperAtta=[];
var  receMateData = [];
var  receMateData2 = [];
layui.use([ 'table', 'laytpl', 'upload', 'layer', 'laydate' ], function(){
	var form = layui.form;
	table=layui.table;
	var $=layui.jquery;
	var type= $('#type').val();
	var radio= $('#radio').val();
	var fid = $('#Fid').val();
	var id ='';
	if(radio=='数据无误'){
		$('#accuracy').attr('checked','checked');
	}else if(radio=='数据有误'){
		$('#Error').attr('checked','checked');
	}
	 var status=$('#status').val();
	 debugger;
	 $.ajax({
		  async: false, 
		  type:"post",
		  url:"/intercourse/queryAttr?fid="+fid,
		  dataType:"JSON",
		  success:function(data){
			  debugger;
			  $.each(data,function(index,item){
				  paperAtta.push(item);
			  })
			  console.info(paperAtta);
			  initPaperTable(paperAtta);
		  }
	  });
	if(type=='1'){
		reconciliationmoneTable(table,tableData);
		reconciliationmonesTable(table,tableData);
	}else if(type=='2'){
		debugger;
		var fid = $('#Fid').val();
		var moneJosn=$("#json1").val();
		console.log(moneJosn)
		var moneJosn2=$("#json2").val();
		receMateData=JSON.parse(moneJosn)
		receMateData2=JSON.parse(moneJosn2)
		reconciliationmoneTable(table,receMateData);
		reconciliationmonesTable(table,receMateData2);
		$('td').removeAttr('data-edit');
		$('.SuppSelect').show();
		 $('.layui-form-select').hide();
		 $('.SuppSelect').attr('disabled','disabled')
		 $('.SuppSelect').css({"width":"100%","height":"38px","border":"1px solid #e6e6e6"});
		 $('layui-input').attr('readonly','readonly');
		 $('input').attr('readonly','readonly');
	}else{
		debugger;
		var fid = $('#Fid').val();
		var moneJosn=$("#json1").val();
		console.log(moneJosn)
		var moneJosn2=$("#json2").val();
		receMateData=JSON.parse(moneJosn)
		receMateData2=JSON.parse(moneJosn2)
		reconciliationmoneTable(table,receMateData);
		reconciliationmonesTable(table,receMateData2);
		
	}
	 if(status=='已保存'||status==''||status=='已退回'){
		  if(type=='2'){
			  $('#reconPublOrder').hide();
			   $('#savedate').hide();
		   
		  }else{
			  $('#reconPublOrder').show();
			   $('#savedate').show();
			   $('.add').show();
		  }
			  
		  
	   }
	 if(status=='已确认'){
		 $('.confirm').show();
		 $('input').attr('readonly','readonly');
		 $('textarea').attr('readonly','readonly');
		 
	 }else if(status=='待确认'){
		 $('.confirm').hide();
		 $('input').attr('disabled',true);
		 $('textarea').attr('readonly','readonly');
	 }
	 
	//添加1
	$('#add').click(function(){
		 var arr = {
				 unseAcco : '',
				 amouPaya : '',
				 amouRece : '',
				 warrMone : '',
				 remarks : '',
		      };
		 receMateData.push(arr);
		 reconciliationmoneTable(table,receMateData);	
		});
	//添加2
	$('#add2').click(function(){
		 var arr = {
				 tempEsti : '',
				 mone : '',
				 remarks : '',
		      };
		 receMateData2.push(arr);
		 reconciliationmonesTable(table,receMateData2);	
		});
	$('#synchro').click(function(){
		var SuppSapId = $('.SuppSelect').val();
		var ClosDate = $('.ClosDate').val();
		if(ClosDate=='' || ClosDate == undefined){
			 layer.msg('请填写账款截止日期', {time:2000 });
			 return;
		}
		
		ClosDate = ClosDate.replace(/-/g,'');
		 $.ajax({
			 async: false, 
	         type:"post",
	         url:"/intercourse/SapInterAdd?SuppSapId="+SuppSapId+"&ClosDate="+ClosDate,
	         dataType:"JSON",
	         success:function(data){
		           if(data){
		        	   receMateData = [];
		        	   receMateData2 = [];	
			           for(var i=0;i<data.DATA.length;i++){
			        	   debugger;
			        	   var arr = {
			      				 unseAcco : data.DATA[i].YAEHS_MONTH,
			      				 amouPaya : data.DATA[i].ZGYSYIF,
			      				 amouRece : data.DATA[i].ZGYSYS,
			      				 prepAcco : data.DATA[i].ZGYSYUF,
			      				 warrMone : data.DATA[i].ZGYSZB,
			      				 remarks : '',
			      		      };
			      		 receMateData.push(arr);
			      		 if(data.DATA[i].YAEHS_MONTH!='0'){
			      			var arr1 = {
					   				 tempEsti : data.DATA[i].YAEHS_MONTH,
					   				 mone : data.DATA[i].ZGYSZG,
					   				 remarks : '',
					   		      };
					      		receMateData2.push(arr1);   
					           }
			      			 
			      		 }
			           if(receMateData.length == 0 && receMateData2.length == 0) {
			        	   layer.msg("无数据!");
			           } 
			           reconciliationmoneTable(table,receMateData);
			           reconciliationmonesTable(table,receMateData2);	
	               }else{
	            	   layer.msg("无数据!");
	            	   reconciliationmoneTable(table, []);
			           reconciliationmonesTable(table, []);	
	               }
	         }
         });
	})
	//提交
	$('#publOrder').click(function(){
		var status = $('#status').val();
		var fid = $('#Fid').val();
		if(status=='已保存'){
			 var fids =[];
			 fids.push(fid);
			 layer.confirm('是否提交订单？', function(index){
		        	console.info(fids)
			         $.ajax({
				         type:"post",
				         url:"/intercourse/resetReconciliationByFid",
				         data:"fids="+fids,
				         dataType:"JSON",
				         success:function(data2){
					           if(data2){
					        	   layer.msg('提交成功', {time:2000 });
					        	   initInterCourseTable();
				               }else{
				            	   layer.alert('<span style="color:red;">提交失败</sapn>');
				               }
				         }
			         });
		              layer.close(index);
		        });
		}else{
			layer.alert('<span style="color:red;">只有"已保存"状态的收货单才能被提交</sapn>');  
		}
		
	})
	  $('#Exprot').click(function(){
		   debugger;
		   var checkStatus = table.checkStatus('orderTableId');
		     var data = checkStatus.data;
		     alert(data.length)
		     var fids
		     for(var i=0;i<data.length;i++){
		    	 if(i==0){
		    		 fids=data[i].fid;
		    	 }else{
		    		 fids+='","'+data[i].fid;
		    	 }
		     }
		   var url ="/intercourse/exportCostInfo?Fids="+fids;
	   })
// 提交
$('#reconPublOrder')
		.click(
				function() {
					debugger;
					if (receMateData.length == 0) {
						layer.msg("请填写供应商相关信息");
						return;
					}
					// if($('#bankTota').val()==''||$('#advaInteAmou').val()==''){
					// layer.msg("请填写银行账款相关信息");
					// return;
					// }
					// if($('.status').val()=='待确认'){
					// layer.msg("请选择已保存或者已退回的数据");
					// return;
					// }
					var reg = /^[0-9a-zA-Z]+$/
					var str = document.getElementById("bankTota").value;
					if (!reg.test(str)) {
						layer.msg("银行付款金额和利息金额不能为中文");
						return;
					}

					$('#Error').val('');
					$('#accuracy').val('');
					$('.Textarea').val('');
					var fid = $('#Fid').val();
					//$('.status').val('待确认');
					$('.status').val('已保存');
					var SuppSapId = $('.SuppSelect').val();
					var data = tableDatea.config.data;
					var data2 = tableDatea2.config.data;
					$.each(data, function(index, item) {
						paperData.push(item);
					});
					$.each(data2, function(index, item) {
						paperData2.push(item);
					});
					$("#receMateData").val(
							JSON.stringify(paperData));
					$("#receMateData2").val(
							JSON.stringify(paperData2));
					//var format = "";
					var flag="";
					if (type == "1") {
						fid = guid();
						$("#Fid").val(fid);
						//format = new Date().Format("yyyy-MM-dd");
						flag = "process_success";
					} else {
						//format = $("#creatTime").val();
					}
					var text = $('.SuppSelect option:selected').text();
					var a = text.indexOf(" ");
					var suppName = text.substring(a+1);
					$("#suppName").val(suppName);
					id = fid;
					var remark = "财务往来对账确认: " + suppName ;
					/*if(type != "1"){
						flag = taskProcess(fid,"finaTranAudit", remark,"process");
					}
					if (flag == "process_success") {*/
						var formData = $("#oderform").serialize();
						var typ = 'submit';
						$.ajax({
									type : "POST",
									url : "/intercourse/savedateList?typ="
											+ typ
											+ "&Fid="
											+ fid
											+ "&sapId="
											+ SuppSapId,
									data : formData,
									dataType : "JSON",
									async : false,
									error : function(request) {
										layer.alert("Connection error");
									},
									success : function(data) {
										if (data) {
											//layer.msg("提交成功");
											var flag = taskProcess(fid,"finaTranAudit", remark,"process");
											/*if(type=="1"){
												flag = taskProcess(fid,"finaTranAudit", remark,"process");
											}*/
											//setTimeout("window.history.back(-1)",1000)
										} else {
											layer.alert("提交失败，无法查询到供应商编码");
											$('.status').val('');
										}
									}
						});
					//}
});
	//在弹窗中选择执行人后，点击确认按钮回调
	   window.returnFunction = function() {
			debugger
			var fids = [];
			fids.push(id);
			$.ajax({
					type:"post",
					url:"/intercourse/resetReconciliationByFid",
					data:"fids="+fids,
					dataType:"JSON",
					success:function(data2){
						if(data2){
							layer.msg('提交成功', {time:2000 });
							window.history.back(-1)
						}else{
							layer.alert('<span style="color:red;">提交失败</sapn>');
						}
					}
			});
		}
	$("#del").click(function(){
		//receMateData
		var checkStatus = table.checkStatus('reconciliationTable');
	    var checkedData = checkStatus.data;
	    console.info(checkedData)
		 if (checkedData.length > 0) {
			 for ( var int = 0; int < checkedData.length; int++) {
				 receMateData.remove(checkedData[int]);
				 reconciliationmoneTable(table, receMateData);
				 console.info(receMateData);
            }
         } else {
            layer.msg("请选择要删除的节点");
         }
	});
	$("#rem").click(function(){
		//receMateData
		var checkStatus = table.checkStatus('reconciliationTable2');
	    var checkedData = checkStatus.data;
	    console.info(checkedData)
		 if (checkedData.length > 0) {
			 for ( var int = 0; int < checkedData.length; int++) {
				 receMateData2.remove(checkedData[int]);
				 reconciliationmonesTable(table, receMateData2);
				 console.info(receMateData2);
            }
         } else {
            layer.msg("请选择要删除的节点");
         }
	});
	
	// 保存
	$('#savedate').click(
			function() {
				debugger;
				var reg = /^[0-9a-zA-Z]+$/
				var str = document.getElementById("bankTota").value;
				if (!reg.test(str)) {
					layer.msg("银行付款金额和利息金额不能为中文");
					return;
				}
				if (receMateData.length == 0) {
					layer.msg("请填写供应商相关信息");
					return;
				}
				if ($('#bankTota').val() == '' || $('#advaInteAmou').val() == '') {
					layer.msg("请填写银行账款相关信息");
					return;
				}
				var fid = $('#Fid').val();
				$('.status').val('已保存');
				// var date = new Date();
				// var year = date.getFullYear().toString().substr(2,2);
				// var month = date.getMonth()+1;
				// if(month.toString().length==1){
				// month='0'+month;
				// }
				// var yeamon=year+month;
				// alert("L"+yeamon)
				var data = tableDatea.config.data;
				var data2 = tableDatea2.config.data;
				var SuppSapId = $('.SuppSelect').val();
				$.each(data, function(index, item) {
					paperData.push(item);
				});
				$.each(data2, function(index, item) {
					paperData2.push(item);
				});
				$("#receMateData").val(JSON.stringify(paperData));
				$("#receMateData2").val(JSON.stringify(paperData2));
				var type = $("#type").val();
				var typ = 'recon';
				var flag = "";
				var text = $('.SuppSelect option:selected').text();
				var a = text.indexOf(" ");
				var suppName = text.substring(a+1);
				$("#suppName").val(suppName);
				if (type == "1") {// 新建保存
					fid = guid();
					$("#Fid").val(fid);
					//var format = new Date().Format("yyyy-MM-dd");
					var remark = "财务往来对账确认: " + suppName ;
					flag = taskProcess(fid, "finaTranAudit", remark, "save");
				} else {// 编辑保存
					flag = "init_success";
				}
				if (flag == "init_success") {
					var formData = $("#oderform").serialize();
					$.ajax({
						type : "POST",
						url : "/intercourse/savedateList?typ=" + typ + "&Fid="
								+ fid + "&sapId=" + SuppSapId,
						data : formData,
						dataType : "JSON",
						async : false,
						error : function(request) {
							layer.alert("Connection error");
						},
						success : function(data) {
							debugger;
							if (data) {
								layer.msg("保存成功");
								$('#savedate').hide();
								setTimeout("window.history.back(-1)", 1000)
							} else {
								layer.alert("保存失败");
								var status = $('.status').val(' ');
							}
						}
					});
				}

			});
	
})
	 //确认详情提交
	   $('#reset').click(function(){
		   debugger;
		   var fid = $('#Fid').val();
			$('.status').val('已确认');
			var SuppSapId = $('.SuppSelect').val();
			var data = tableDatea.config.data;
			var data2 = tableDatea2.config.data;
			console.info(data)
			console.info(data2)
			$.each(data,function( index,item) {
					paperData.push(item);
					console.info(item)
			});
			$.each(data2,function( index,item) {
				paperData2.push(item);
				console.info(item)
		    });
				$("#receMateData").val(JSON.stringify(paperData));
				$("#receMateData2").val(JSON.stringify(paperData2));
			     var formData = $("#oderform").serialize();
			    $.ajax({
			         type : "POST",
			         url : "/intercourse/savedateList?type="+type+"&Fid="+fid+"&sapId="+SuppSapId,
			         data : formData,
			         dataType:"JSON",
			         async: false,
			         error : function(request) {
			          layer.alert("Connection error");
			         },
			         success : function(data) {
			          if(data){
			           layer.msg("提交成功");
			          }else{
			           layer.alert("提交失败");
			          }
			         }
			        });
		   }); 


//未结清款项
function reconciliationmoneTable(table,data){
	  tableDatea = table.render({
		  elem:"#reconciliationTable",
		  page:true,
		  data:data,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"reconciliationTable",
		  cols:[[
		     {checkbox : true,width:60},
		     {field:'unseAcco',title:'账款未结清期间', align:'center',width:128},
		     {field:'amouPaya',title:'当期采购应付帐款金额', align:'center',width:170},
		     {field:'amouRece', title:'当期物料销售应收帐款金额',align:'center',width:200},
		     {field:'prepAcco', title:'预付账款金额',align:'center',width:200},
		     {field:'warrMone',title:'质保金（其他应付款）',align:'center', width:170},
		     {field:'remarks',title:'备注', align:'center',width:200,edit:'text'}
		  ]],
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
					var amouPaya = 0;
					var amouRece = 0;
					var prepAcco = 0;
					var warrMone = 0;
					for(var i=0;i<data.length;i++){
						  var item = data[i];
						  amouPaya += (item.amouPaya==null||isNaN(item.amouPaya)||item.amouPaya=='')?0:parseFloat(item.amouPaya);
						  amouRece += (item.amouRece==null||isNaN(item.amouRece)||item.amouRece=='')?0:parseFloat(item.amouRece);
						  prepAcco += (item.prepAcco==null||isNaN(item.prepAcco)||item.prepAcco=='')?0:parseFloat(item.prepAcco);
						  warrMone += (item.warrMone==null||isNaN(item.warrMone)||item.warrMone=='')?0:parseFloat(item.warrMone);
					}
					
					var sumRow2 = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
				      '<td></td>'+
				      '<td></td>'+
				      '<td></td>'+
				      '<td>总计</td></tr>';

					var sumRow = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
					  '<td>总计</td>'+
					  '<td>'+amouPaya.toFixed(2)+'</td>'+
					  '<td>'+amouRece.toFixed(2)+'</td>'+
					  '<td>'+prepAcco.toFixed(2)+'</td>'+
					  '<td>'+warrMone.toFixed(2)+'</td>'+
					  '<td></td></tr>';
					  $('.layui-table-fixed .layui-table tbody').append(sumRow2);
					  $('.unseAcco .layui-table-main .layui-table tbody').append(sumRow);
				}
				
			}
	  })
}
//暂估款
function reconciliationmonesTable(table,data){
	tableDatea2 = table.render({
		  elem:"#reconciliationmoneTable",
		  data:data,
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"reconciliationTable2",
		  cols:[[
		     { checkbox : true,width:60},
		     {field:'tempEsti',title:'应付暂估款期间', align:'center',width:128},
		     {field:'mone',title:'金额', align:'center',width:170},
		     {field:'remarks',title:'备注', align:'center',width:200,edit:'text'}
		  ]],
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
					var mone = 0;
					for(var i=0;i<data.length;i++){
						  var item = data[i];
						  mone += (item.mone==null||isNaN(item.mone)||item.mone=='')?0:parseFloat(item.mone);
					}
					
					var sumRow2 = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
				      '<td></td>'+
				      '<td></td>'+
				      '<td></td>'+
				      '<td>总计</td></tr>';

					var sumRow = '<tr  height="30" align="center" style="font-weight:bold"><td></td>'+
					  '<td>总计</td>'+
					  '<td>'+mone.toFixed(2)+'</td>'+
					  '<td></td></tr>';
					  $('.layui-table-fixed .layui-table tbody').append(sumRow2);
					  $('.tempEsti .layui-table-main .layui-table tbody').append(sumRow);
				}
				
			}
	  })
}
//返回
$("#goBack").click(function() {
//	window.history.back(-1);
	tuoBack('.ReconciliationDetail','#search')
});
//确认
$('#confirm').click(function(){
	var status = $('#status').val();
	var fid = $('#Fid').val();
	if(status=='已保存'){
		 var fids =[];
		 fids.push(fid);
		 layer.confirm('是否提交订单？', function(index){
	        	console.info(fids)
		         $.ajax({
			         type:"post",
			         url:"/intercourse/resetReconciliationByFid",
			         data:"fids="+fids,
			         dataType:"JSON",
			         success:function(data2){
				           if(data2){
				        	   layer.msg('提交成功', {time:2000 });
				        	   initInterCourseTable();
			               }else{
			            	   layer.alert('<span style="color:red;">提交失败</sapn>');
			               }
			         }
		         });
	              layer.close(index);
	        });
	}else{
		layer.alert('<span style="color:red;">只有"已保存"状态的收货单才能被提交</sapn>');  
	}
	
})


Array.prototype.remove = function(val) {
 for ( var k = 0; k < this.length; k++) {
  if (this[k].id == val.id) {
   this.splice(k, 1);
   return;
  }
 }
};
function initPaperTable(data) {
	$('#orderEncl')
			.bootstrapTable(
					{
						method : 'get',
						editable : true,// 关闭编辑模式
						undefinedText : '',
						pageList : [ 10, 20 ],
						pageSize : 10,
						pageNumber : 1,
						striped : true,
						locale : 'zh-CN',
						columns : [
								{
								   title: '序号',//标题  可不加  
				                   formatter: function (value, row, index) {  
				                        return index+1;  
				                   }
								},
								{
									field : "appeType",
									title : "附件类型",
									align : "center",
									
								},
								{
									field : "appeName",
									title : "名称",
									align : "center"
								},
								{
									field : "appeFile",
									title : "附件",
									edit : false,
									align : "center"
								},
								{
									field : "newName",
									title : "newName",
									align : "center",
									visible : false,
									edit : false
								},
								{
									field : "fileUrl",
									title : "fileUrl",
									align : "center",
									visible : false,
									edit : false
								},
								{
									field : 'fileId',
									title : "fileId",
									align : 'center',
									visible : false,
									edit : false,
								},
								{
									field : 'status',
									title : '附件 操作',
									align : "center",
									formatter : function(value, row, index) {
										var editStr = '<a class="layui-btn layui-btn-xs  blueInline" onclick="downLoadFile(\''
												+ row.fileId
												+ '\') " style="margin-left:10px !important;">下载</a>'
												+ '<a class="layui-btn layui-btn-sm layui-btn-danger redInline deleta" onclick="deleteFile(\''
												+ row.fileId + '\') ">删除</a>';
										return editStr;
									}
								} ],
						data : data
					});
}
