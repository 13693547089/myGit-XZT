var table;
var tableData=[];
var upload;
var paperData = [];
var paperData2 = [];
var paperAtta=[];
var tableDatea;
var tableDatea2;
var ordertype =1;
var  receMateData = [];
var  receMateData2 = [];
layui.use([ 'table', 'laytpl', 'upload', 'layer', 'laydate' ], function(){
	table=layui.table;
	var $=layui.jquery;
	var type= $('#type').val();
	var radio= $('#radio').val();
	var status = $('#status').val();
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
	 
	if(type=='2'){
		 $('#reset').hide();
		 $('#return').hide();
		 $('#editRow').hide();
		 $('.deleta').hide();
		 $('.add').hide();
		 $('#accuracy').attr('disabled',true);
		 $('#Error').attr('disabled',true);
		 $('.Textarea').attr('readonly','readonly');
		var fid = $('#Fid').val();
		var moneJosn=$("#json1").val();
		console.log(moneJosn)
		var moneJosn2=$("#json2").val();
		reconciliationmoneTable(table,JSON.parse(moneJosn));
		reconciliationmonesTable(table,JSON.parse(moneJosn2));
	}else if(type=='3'){
	    $('#reset').show();
	    $('#return').show();
		var fid = $('#Fid').val();
		var moneJosn=$("#json1").val();
		console.log(moneJosn)
		var moneJosn2=$("#json2").val();
		reconciliationmoneTable(table,JSON.parse(moneJosn));
		reconciliationmonesTable(table,JSON.parse(moneJosn2));
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
	//提交
	$('#publOrder').click(function(){
		var status = $('#status').val();
		var fid = $('#Fid').val();
		if(status=='已保存'){
			 var fids =[];
			 fids.push(fid);
			 layer.confirm('是否提交订单？', function(index){
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
				            	   layer.alert('<span style="color:red;">提交失败</span>');
				               }
				         }
			         });
		              layer.close(index);
		        });
		}else{
			layer.alert('<span style="color:red;">只有"已保存"状态的收货单才能被提交</span>');  
		}
		
	})
	//导出
	  $('#Exprot').click(function(){
		   debugger;
//		   var checkStatus = table.checkStatus('orderTableId');
//		     var data = checkStatus.data;
//		     alert(data.length)
//		     var fids
//		     for(var i=0;i<data.length;i++){
//		    	 if(i==0){
//		    		 fids=data[i].fid;
//		    	 }else{
//		    		 fids+='","'+data[i].fid;
//		    	 }
//		     }
		   var fid = $('#Fid').val();
		   var url ="/intercourse/exportCostInfo?Fids="+fid;
		   location=url;
	   })
	//提交保存
	$('#reconPublOrder').click(function(){
		var fid = $('#Fid').val();
		$('.status').val('待确认');
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
		         url : "/intercourse/savedateList?type="+type+"&Fid="+fid,
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
		           layer.alert("提交失败，无法识别供应商编码");
		          }
		         }
		        });
	});
	//退回
	$('#return').click(function(){
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
							window.history.back(-1)
							layer.msg("退回成功");
						}else{
							layer.alert("退回失败");
						}
					}
				});
			}
			
		}else{
			 layer.alert("请选择待确认的数据");
		}
		
		
	})
	//给单选框赋予点击事件
	$(":radio").click(function(){
		debugger;
		var inteAmou = $(this).val()
		if(inteAmou == '数据有误'){
			$(".add").hide();
		}else if(inteAmou == '数据无误'){
			$(".add").show();
		}
	});
})
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
	//文件下载
	function downLoadFile(docId) {
		window.location.href = "/doc/downLoadDoc?docId=" + docId
	}

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
				 var suppName = $("#suppName").val();
				 //var format = $("#creatTime").val();
				 var remark = "财务往来对账确认: " + suppName ;
				var flag = taskProcess(fid,"finaTranAudit", remark,"process");
				if (flag == "over_success") {
					var formData = $("#oderform").serialize();
					$.ajax({
						type : "POST",
						url : "/intercourse/savedateList?Fid="+fid+"&sapId="+SuppSapId,
						data : formData,
						dataType:"JSON",
						async: false,
						error : function(request) {
							layer.alert("Connection error");
						},
						success : function(data) {
							if(data){
								window.history.back(-1)
								layer.msg("提交成功");
							}else{
								layer.alert("提交失败，无法识别供应商编码");
							}
						}
					});
					
				}
		   }); 
	//保存
	$('#savedate').click(function (){
		
		var fid = $('#Fid').val();
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
		         url : "/intercourse/savedateList?type="+type+"&Fid="+fid,
		         data : formData,
		         dataType:"JSON",
		         async: false,
		         error : function(request) {
		          layer.alert("Connection error");
		         },
		         success : function(data) {
		          if(data){
		           layer.msg("保存成功");
		          }else{
		           layer.alert("保存失败");
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
		     {type: 'numbers',title:'序号',width:60},
		     {field:'unseAcco',title:'账款未结清期间', align:'center',width:128},
		     {field:'amouPaya',title:'当期采购应付帐款金额', align:'center',width:170},
		     {field:'amouRece', title:'当期物料销售应收帐款金额',align:'center',width:200},
		     {field:'prepAcco', title:'预付账款金额',align:'center',width:200},
		     {field:'warrMone',title:'质保金（其他应付款）',align:'center', width:170},
		     {field:'remarks',title:'备注', align:'center',width:200}
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
		     {type: 'numbers',title:'序号',width:60},
		     {field:'tempEsti',title:'应付暂估款期间', align:'center',width:128},
		     {field:'mone',title:'金额', align:'center',width:170},
		     {field:'remarks',title:'备注', align:'center',width:200}
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
	tuoBack('.intercourseConfirmDetail','#search')
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
			            	   layer.alert('<span style="color:red;">提交失败</span>');
			               }
			         }
		         });
	              layer.close(index);
	        });
	}else{
		layer.alert('<span style="color:red;">只有"已保存"状态的收货单才能被提交</span>');  
	}
	
})
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
										var editStr = '<a class="layui-btn layui-btn-xs blueInline " onclick="downLoadFile(\''
												+ row.fileId
												+ '\') " style="margin-left:10px !important;">下载</a>'
												+ '<a class="layui-btn layui-btn-xs redInline layui-btn-danger deleta" onclick="deleteFile(\''
												+ row.fileId + '\') ">删除</a>';
										return editStr;
									}
								} ],
						data : data
					});
}



