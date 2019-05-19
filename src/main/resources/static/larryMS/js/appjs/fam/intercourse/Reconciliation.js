var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.jquery;
	  var laydate = layui.laydate;
	  var id = '';
	  //跳转详情页面
	  $('#serachSupp').click(function(){
		  var url ="/intercourse/getReconciliationDetail?type=1"
//		     location=url;
		  tuoGo(url,'ReconciliationDetail','reconciliationListTable')
		  
	  })
	  laydate.render({
	      elem:".dateSta", 
	  });
	  laydate.render({
		  elem:".dateEnd", 
	  });
	 //获取数据
	  debugger;
	 initInterCourseTable();
//	  table.on('tool(demo)', function(obj){
//		    var data = obj.data;
//		    console.info(data.fid)
//		    if(obj.event === 'setSign'){
//		     var url ="/intercourse/getReconciliationDetailList?Fid="+data.fid
//		     location=url;
//		    }
//		  });
	  
	  //监听工具条
	   table.on('tool(demo)', function(obj){
	     var data = obj.data;
	     if(obj.event === 'check'){//查看
		     var url  ="/intercourse/getReconciliationDetailList?Fid="+data.fid+"&type=2";
//		     location=url;
		     tuoGo(url,'ReconciliationDetail')
	     }else if(obj.event === 'del'){//删除
	         console.info(data);
	         if(data.status=='已保存'||data.status=='已退回'){
		         layer.confirm('真的删除这个对账单吗？', function(index){
			         var fids =[];
			         fids.push(data.fid);
			         $.ajax({
				         type:"post",
				         url:"/intercourse/deleteReconciliationByFid",
				         data:"fids="+fids,
				         dataType:"JSON",
				         success:function(data2){
					           if(data2){
					        	   layer.msg('删除成功', {time:2000 });
					        	   initInterCourseTable();
				               }else{
				            	   layer.alert('<span style="color:red;">删除失败</sapn>');
				               }
				         }
			         });
		             layer.close(index);
		        });
	        }else{
	            layer.alert('<span style="color:red;">只有"已保存"状态的对账单才能被删除</sapn>');
	        }
	     } else if(obj.event === 'edit'){//编辑
	    	 
		      if(data.status=="已保存"||data.status=='已退回'){
			       var url  ="/intercourse/getReconciliationDetailList?Fid="+data.fid+"&type=3";
			       location=url;
		      }else{
		    	   layer.alert('<span style="color:red;">只有"已保存"状态的采购订单可以编辑</sapn>');
		      }
	     } else if(obj.event === 'setSign'){
		     var url ="/intercourse/getReconciliationDetailList?Fid="+data.fid
		     location=url;
		    }
	   });
	   $('#Rese').click(function(){
		   debugger;
		   $('.supply').val('');
		   $('.Buyer').val('');
		   $('.dateSta').val('');
		   $('.dateEnd').val('');
		   
	   })
	   //导出
	   $('#removeAppo').click(function(){
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
		   var url ="/intercourse/exportAppointList?Fids="+fids;
	   })
	   
	 //删除
	   $("#remRece").click(function(){
	       var table = layui.table;
	       var checkStatus = table.checkStatus('orderTableId');
	       var data = checkStatus.data;
	       var length = data.length;
	       if(length != 0){
	    	    var fids =[];
		        var a=0;
		        for(var i=0;i<length;i++){
		        	 fids.push(data[i].fid);
			         if(data[i].status !='已保存'&&data[i].status !='已退回'){
			            a++;
			         }
		        }
		        if(a == 0){
		        layer.confirm('是否删除收货单？', function(index){
		        	console.info(fids)
			         $.ajax({
				         type:"post",
				         url:"/intercourse/deleteReconciliationByFid",
				         data:"fids="+fids,
				         dataType:"JSON",
				         success:function(data2){
					           if(data2){
					        	   layer.msg('删除成功', {time:2000 });
					        	   initInterCourseTable();
				               }else{
				            	   layer.alert('<span style="color:red;">删除失败</sapn>');
				               }
				         }
			         });
		              layer.close(index);
		        });
		        }else{
		            layer.alert('<span style="color:red;">只有"已保存和已退回"状态的收货单才能被删除</sapn>');  
		        }
		        
	       }else{
	           layer.alert('<span style="color:red;">请选择其中一条或多条数据</sapn>'); 
	       }
	   });
	   //批量提交
	   $('#reset').click(function(){
		      var table = layui.table;
		       var checkStatus = table.checkStatus('orderTableId');
		       var data = checkStatus.data;
		       var length = data.length;
		       if(length == 1){//单个提交
		    	    var fids =[];
			        var a=0;
			        for(var i=0;i<length;i++){
			        	 fids.push(data[i].fid);
				         if(data[i].status !='已保存'){
				            a++;
				         }
			        }
			        if(a == 0){
			        layer.confirm('是否提交订单？', function(index){
				        for(var i=0;i<length;i++){
				        	fids=[];
				        	fid = data[i].fid;
				        	id = fid;
				        	fids.push(fid);
				        	var suppName =data[i].suppName;
	  						var creatTime = data[i].creatTime;
	  						//var format = new Date(creatTime).Format("yyyy-MM-dd");
	  						var remark = "财务往来对账确认: "+suppName;
	  						var flag = taskProcess(fid, "finaTranAudit", remark, "process");
	  						//if(flag=="process_success"){
	  							/*$.ajax({
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
	  							});*/
	  						//}
				        } 
			             layer.close(index);
			        });
			        }else{
			            layer.alert('<span style="color:red;">只有"已保存"状态的财务对账单才能被提交</sapn>');  
			        }
			        
		       }else{
		           layer.alert('<span style="color:red;">请选择其中一条数据提交</sapn>'); 
		       }
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
							initInterCourseTable();
						}else{
							layer.alert('<span style="color:red;">提交失败</sapn>');
						}
					}
			});
		}
	   //财务确认提交
	   $('#confirmReset').click(function(){
		   var table = layui.table;
	       var checkStatus = table.checkStatus('orderTableId');
	       var data = checkStatus.data;
	       var length = data.length;
	       if(length != 0){
	    	    var fids =[];
		        var a=0;
		        for(var i=0;i<length;i++){
		        	 fids.push(data[i].fid);
			         if(data[i].status !='待确认'){
			            a++;
			         }
		        }
		        if(a == 0){
		        layer.confirm('是否提交订单？', function(index){
		        	console.info(fids)
			         $.ajax({
				         type:"post",
				         url:"/intercourse/financialConfirmation",
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
		            layer.alert('<span style="color:red;">只有"待确认"状态的收货单才能被提交</sapn>');  
		        }
		        
	       }else{
	           layer.alert('<span style="color:red;">请选择其中一条或多条数据</sapn>'); 
	       }
	   })
		   
	  
	  
});


function initInterCourseTable() {
	var data = {};
	var supply = $('.supply').val(); //供应商
	var Buyer = $('.Buyer').val();//财务对账单号

	data = {
			supply : supply,
			Buyer : Buyer,
		
	};
	var dateSta = $('.dateSta').val();
	var dateEnd = $('.dateEnd').val();
	if(dateSta!==''||dateEnd!==''){
		debugger;
		if(dateSta==''){
			alert('请填写开始时间');
			return;
		}else if(dateEnd==''){
			alert('请填写结束时间');
			return;
		}else{
			data.dateSta = dateSta;
			data.dateEnd = dateEnd;
		}
	}

			table.render({
				elem : "#reconciliationListTable",
				page : true,
				where:data,
				url : "/intercourse/queryReconciliationList",
				width : '100%',
				minHeight : '20px',
				limit : 10,
				id : "orderTableId",
				cols : [ [
						{
							checkbox : true,
							title : '',
						},
						{
							field : 'status',
							title : '状态',
							align : 'center',
							width : 93,
							
						},
						{
							field : 'recoNumb',
							title : '财务对账单号',
							align : 'center',
							width : 150,
							
						},
						{
							field : 'suppName',
							title : '供应商名称',
							align : 'center',
							width : 160,
						
						}, {
							field : 'suppNumb',
							title : '供应商编码',
							align : 'center',
							width : 120,
							
						}, {
							field : 'founder',
							title : '创建人',
							align : 'center',
							width : 100,
							
						}, {
							field : 'creatTime',
							title : '创建时间',
							align : 'center',
							width : 110,
							
							templet : function(d) {
								var date = new Date(d.creatTime);
								var year = date.getFullYear();
								var month = date.getMonth() + 1;
								var day = date.getDate();
								/*
								 * var h = date.getHours(); var m =
								 * date.getMinutes(); var s =date.getSeconds();
								 */
								return year + "-"
										+ (month < 10 ? "0" + month : month)
										+ "-" + (day < 10 ? "0" + day : day);
							}
						},
						{fixed: 'right', title:'操作',width:150, align:'center', toolbar: '#barDemo'}
						] ],
			})
			$('.tableCont .layui-table-view .layui-form-checkbox[lay-skin=primary] i').css('margin-top','0px');
			  $('.tableCont .layui-table-view .layui-form-checkbox[lay-skin=primary] i').eq(0).css('margin-top','6px');
	
	
	
};
$('#search').click(function(){
	initInterCourseTable();
		
	
})