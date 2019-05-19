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
layui.use([ 'table', 'laytpl', 'upload', 'layer', 'laydate','form' ], function(){
	var form = layui.form;
	table=layui.table;
	var $=layui.jquery;
	var type= $('#type').val();
	var status=$('#status').val();
	var pagePattern= $('#pagePattern').val();
	if(pagePattern == 'read'){
		debugger;
		status ='待确认';
		$('.add').hide();
		$("[name='inteAmou']").attr("disabled",true)
		 $('.Textarea').attr("readonly",true);
		 $('.deleta').hide();
	}
	var radio= $('#radio').val();
	var fid = $('#Fid').val();
	if(radio=='数据无误'){
		$('#accuracy').attr('checked','checked');
	}else if(radio=='数据有误'){
		$('#Error').attr('checked','checked');
	}
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
	 if(status=='已保存'||status=='已退回'){
		   $('.add').hide();
		   $('.orderdiv').hide();
	 }else if(status=='待确认'){
		 $('.confirm').show();
		 $('td').removeAttr('data-edit');
		 $('.SuppSelect').show();
		 $('.layui-form-select').hide();
		 $('.SuppSelect').attr('disabled','disabled')
		 $('.SuppSelect').css({"width":"100%","height":"38px","border":"1px solid #e6e6e6"});
		 $('layui-input').attr('readonly','readonly');
		 $('.disa').attr("readonly",true);
		 $('.synch').hide();
		 //$('input').attr('disabled',true);
		 //$('textarea').attr('readonly','readonly');
	 }
	 if(status == "已退回"){
		 $('.confirm').show();
		 $("[name='inteAmou']").attr("disabled",true)
		 $('#accuracy').attr("readonly",true);
		 $('.Textarea').attr("readonly",true);
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
			           reconciliationmoneTable(table,receMateData);
			           reconciliationmonesTable(table,receMateData2);	
		        	   
	               }else{
	            	  
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
	//导出
	  $('#Exprot').click(function(){
		  var fid = $('#Fid').val();
		   var url ="/intercourse/exportCostInfo?Fids="+fid;
		   location=url;
	   })
// 提交保存
$('#reconPublOrder').click(
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
					debugger;
					var fid = $('#Fid').val();
					var SuppSapId = $('.SuppSelect').val();
					var status = $('#status').val();
					/*if(status == "已保存" || status == '已退回'){
						$('.status').val('待确认');
					}else if(status == "待确认"){
						$('.status').val('已确认');
					}*/
					$('.status').val('已保存');
					var data = tableDatea.config.data;
					var data2 = tableDatea2.config.data;
					$.each(data, function(index, item) {
						paperData.push(item);
					});
					$.each(data2, function(index, item) {
						paperData2.push(item);
					});
					$("#receMateData").val(JSON.stringify(paperData));
					$("#receMateData2").val(JSON.stringify(paperData2));
					var text = $('.SuppSelect option:selected').text();
					var a = text.indexOf(" ");
					var suppName = text.substring(a+1);
					$("#suppName").val(suppName);
					var taskName = $("#taskName").val();
					var processCode = $("#processCode").val();
					//var flag = taskProcess(fid,processCode, taskName,"process");
					var formData = $("#oderform").serialize();
					//if (flag == "process_success") {
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
											var flag = taskProcess(fid,processCode, taskName,"process");
											//layer.msg("提交成功");
											//setTimeout("window.history.back(-1)",1000)
										} else {
											layer.alert("提交失败，无法查询到供应商编码");
											$('.status').val('');
										}
									}
						});
					/*}else if(flag == "over_success"){
						var typ = 'comfirm';
						$.ajax({
					         type : "POST",
					         url : "/intercourse/savedateList?typ="+typ+"&Fid="+fid+"&SuppId="+SuppSapId,
					         data : formData,
					         dataType:"JSON",
					         async: false,
					         error : function(request) {
					          layer.alert("Connection error");
					         },
					         success : function(data) {
						          if(data){
						             layer.msg("提交成功");
						             window.history.back(-1)
						          }else{
						             layer.alert("提交失败");
						          }
					         }
					    });
					}*/
});
	//在弹窗中选择执行人后，点击确认按钮回调
	   window.returnFunction = function() {
			debugger
			var fids = [];
			fids.push(fid);
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
	 //给单选框赋予点击事件
		/*$(":radio").click(function(){
			debugger;
			var inteAmou = $(this).val()
			if(inteAmou == '数据有误'){
				$(".add").hide();
			}else if(inteAmou == '数据无误'){
				$(".add").show();
			}
		});*/
	   form.on('radio(aa)', function(data){
		   debugger;
		   //console.log(data.elem); //得到radio原始DOM对象
		   //console.log(data.value); //被点击的radio的value值
		   var inteAmou = data.value;
		   if(inteAmou == '数据有误'){
				$(".add").hide();
			}else if(inteAmou == '数据无误'){
				$(".add").show();
			}
		 }); 
	//确认详情提交
	   $('#reset').click(function(){
		   debugger;
		   var fid = $('#Fid').val();
			$('.status').val('已确认');
			var SuppSapId=$('#SuppNumb').val();
			var inteAmou=$("input[name='inteAmou']:checked").val();
			if(inteAmou == undefined || inteAmou ==""){
				layer.msg("请在单选框中选择数据是否有误");
				return;
			}else if(inteAmou == '数据有误'){
				layer.msg("数据有误,无法确认提交");
				return;
			}
			var data = tableDatea.config.data;
			var data2 = tableDatea2.config.data;
			$.each(data,function( index,item) {
					paperData.push(item);
			});
			$.each(data2,function( index,item) {
				paperData2.push(item);
		    });
				$("#receMateData").val(JSON.stringify(paperData));
				$("#receMateData2").val(JSON.stringify(paperData2));
				//保存附件
				var paperTableData = $('#orderEncl').bootstrapTable("getData");
				if(paperTableData.length==0){
					parent.layer.msg("请添加附件！", {
						time : 2000
					});
					return;
					
				}
				var papData =[];
				$.each(paperTableData,function(index,item){
					papData.push(item);
				});
				 var paperDataJson =  JSON.stringify(papData);
				 $('#papData').val(JSON.stringify(papData));
				 var taskName = $("#taskName").val();
				var processCode = $("#processCode").val();
				var flag = taskProcess(fid,processCode, taskName,"process");;
				if (flag == "over_success") {
					var formData = $("#oderform").serialize();
					$.ajax({
						type : "POST",
						url : "/intercourse/savedateList?Fid="+fid+"&SuppId="+SuppSapId,
						data : formData,
						dataType:"JSON",
						async: false,
						error : function(request) {
							layer.alert("Connection error");
						},
						success : function(data) {
							if(data){
								layer.msg("提交成功");
								window.history.back(-1);
							}else{
								layer.alert("提交失败，无法识别供应商编码");
							}
						}
					});
					
				}
		   });
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
	
	//回退
	$("#backBut").click(function(){
		var fid = $('#Fid').val();
		var status = $('#status').val();
		debugger;
		var Radio=$('input:radio[name="inteAmou"]:checked').val();
		var Textarea = $('.Textarea').val();
		if(Radio==undefined & Textarea==''||Textarea==undefined){			
			layer.alert("请选择数据有误或者数据无误以及输入备注");
			return;
		}else if(Textarea==''||Textarea==undefined){
			layer.alert("请输入备注");
			return;
		}else if(Radio==undefined ){
			layer.alert("请选择数据有误或者数据无误");
			return;
		}
		if(status=='待确认'){
			debugger;
			var result = backProcess(fid);
			if(result=="back_success"){
				$.ajax({
					type : "POST",
					url : "/intercourse/confirmReturn?Fid="+fid+"&Textarea="+encodeURIComponent(Textarea)+"&Radio="+encodeURIComponent(Radio),
					dataType:"JSON",
					async: false,
					error : function(request) {
						layer.alert("Connection error");
					},
					success : function(data) {
						if(data){
							//layer.msg("退回成功");
							window.history.back(-1);
						}else{
							layer.alert("退回失败");
						}
					}
				});
			}
			
		}else{
			 layer.alert("请选择待确认的数据");
		}
	});
})
	 //确认详情提交
	   /*$('#reset').click(function(){
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
			         url : "/intercourse/savedateList?type="+type+"&Fid="+fid+"&SuppId="+SuppSapId,
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
		   }); */

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
			var status = $('.status').val('已保存');
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
			var formData = $("#oderform").serialize();
			var text = $('.SuppSelect option:selected').text();
			var a = text.indexOf(" ");
			var suppName = text.substring(a+1);
			$("#suppName").val(suppName);
			var name = $("#suppName").val();
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
					if (data) {
						debugger;
						layer.msg("保存成功");
						//$('#savedate').hide();
						window.history.back(-1);
					} else {
						layer.alert("保存失败,没有查询到供应商编码");
						var status = $('.status').val(' ');
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
	window.history.back(-1);
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
	var pagePattern= $('#pagePattern').val();
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
										var editStr = '<a class="layui-btn layui-btn-xs blueInline " onclick="downLoadFile(\''
												+ row.fileId
												+ '\') " style="margin-left:10px !important;">下载</a>'
												+ '<a class="layui-btn layui-btn-xs redInline layui-btn-danger deleta" onclick="deleteFile(\''
												+ row.fileId + '\') ">删除</a>';
										if(pagePattern=='read'){
											editStr = '<a class="layui-btn layui-btn-xs blueInline " onclick="downLoadFile(\''
												+ row.fileId
												+ '\') " style="margin-left:10px !important;">下载</a>';
										}
										return editStr;
									}
								} ],
						data : data
					});
}

//文件下载
function downLoadFile(docId) {
	window.location.href = "/doc/downLoadDoc?docId=" + docId
}
//删除附件(包括文件服务上的文件)
function deleteFile(docId) {
	var docIds = [];
	docIds.push(docId);
	$.ajax({
		type : 'post',
		url : "/deletePaperFile",
		data : {
			jsonStr : JSON.stringify(docIds)
		},
		success : function(msg) {
			if (msg.code == '0') {
				$('#orderEncl').bootstrapTable('remove', {
					field : 'fileId',
					values : docIds
				});
				layer.msg("删除成功！", {
					time : 1000
				});
			} else {
				layer.msg("删除失败！", {
					time : 1000
				});
			}
		},
		error : function() {
			layer.msg("程序出错了！", {
				time : 1000
			});
		}
	});
}
//弹出层的确认按钮,添加附件
function affirm() {
	debugger;
	var appeType = $("#appeType").val();
	var appeName = $("#appeName").val();
	var saveFormData = new FormData($('#paperForm')[0]);// 序列化表单，
	// $("form").serialize()只能序列化数据，不能序列化文件
	$.ajax({
		type : "POST",
		url : '/doc/docUpload?direCode=' + 'MBGL',
		data : saveFormData,// 你的form的id属性值
		processData : false,
		contentType : false,
		async : false,
		error : function(request) {
			parent.layer.msg("程序出错了！", {
				time : 2000
			});
		},
		success : function(res) {
			debugger;
			if (res.code == '0') {
				// 文件上传成功
				debugger;
				console.info(res.data)
				var index = $('#papertable').bootstrapTable('getData').length;
				var object = new Object();
				object.appeType = appeType;
				object.appeName = appeName;
				object.appeFile = res.data[0].realName;
				object.newName = res.data[0].fileName;
				object.fileUrl = res.data[0].fileUrl;
				object.fileId = res.data[0].id;
				paperAtta.push(object);
				$('#orderEncl').bootstrapTable("load", paperAtta);
			}

		}
	});
	ordertype=2;
}