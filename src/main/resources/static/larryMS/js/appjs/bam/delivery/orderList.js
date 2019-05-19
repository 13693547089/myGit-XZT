var table;
var checkedData;
var CheckedtotalNum;
layui.use('table', function(){
	  table = layui.table;
	  var $ = layui.$;
	  var index=parent.layer.getFrameIndex(window.name);
	  var parentData =parent.paraData;
	  var suppId = parentData.suppId;
	  var mateCode = parentData.mateCode;
	  var resiNumber = parentData.resiNumber;
	  var suppRange = parentData.suppRange;//供应商子范围编码
	  $("#mateNum").val(resiNumber);
	  $.ajax({
			type:"post",
			url:"/queryAllOrderOfMate",
			data:{
				suppId:suppId,
				mateCode:mateCode,
				suppRange:suppRange
			},
			dataType:"JSON",
			success:function(data){
				//initMateTable(table,data);
				addCheckbox(table,data);
			}
		});
	  
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
		  var data = obj.data;
		  var checkStatus = table.checkStatus('orderDOTableId');
		  checkedData = checkStatus.data;
		  CheckedtotalNum=0;
		  $.each(checkedData,function(index,item){
			  CheckedtotalNum+=parseInt(item.unpaQuan)
		  });
		  var count = resiNumber-CheckedtotalNum;
		  $("#mateNum").val(count);
	  });
	  //条件搜索
	  var $ = layui.$, active = {
			    reload: function(){
			      var suppInfo = $('#suppInfo');
			      var category = $("#category");
			      var provcity = $("#provcity");
			      //执行重载
			      table.reload('idTest', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {
			         // key: {
			        	  suppInfo: suppInfo.val(),
			        	  category:category.val(),
			        	  provcity:provcity.val()
			         // }
			        }
			      });
			    }
		};
	  
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  

		//取消
		$("#cancel").click(function(){
			var index=parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		});
		
		
		//确定
		$("#confirm").click(function(){
			var checkStatus = table.checkStatus('orderDOTableId');
			checkedData = checkStatus.data;
			var index=parent.layer.getFrameIndex(window.name);
			parent.deliveryMates(checkedData);
			parent.layer.close(index);
		});
		//选中复选框
		function addCheckbox(table,data){
			var index=parent.layer.getFrameIndex(window.name);
			var data2 =parent.paraData;
			var tableData = data2.tableData;
			var orderMateData=[];
			$.each(data,function(orderindex,orderitem){
				var count=0;
				$.each(tableData,function(tableDataIndex,tableDataItem){
					if(tableDataItem.orderId == orderitem.contOrdeNumb && tableDataItem.mateCode ==orderitem.mateNumb){
						count++;
					}
				});
				if(count==0){
					orderMateData.push(orderitem)
				}
			});
			initOrderTable(table,orderMateData);
		}
	  
});



function initOrderTable(table,data){
	var orderTable = table.render({
		 elem:"#orderDOTable",
		 data:data,
		 page:false,
		 width: '100%', 
		 minHeight:30,
		 limit:100,
		 cols:[[{
			 checkbox:true
		 },{
			 field:"contOrdeNumb",
			 title:"采购订单号",
			 align:'center',
		     width:112
		 },{
			 field:"subeDate",
			 title:"订购日期",
			 align:'center',
		     width:112,
		     templet:
		    	 function (d){
		    	    var date = new Date(d.subeDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		 },{
			 field:"frequency",
			 title:"项次",
			 align:'center',
		     width:74
		 },{
			 field:"mateNumb",
			 title:"物料编码",
			 align:'center',
		     width:220
		 },{
				field : "calculNumber",
				title : "订单可用量",
				align : 'center',
				width :148
		},{
			 field:"unpaQuan",
			 title:"订单未交量",
			 align:'center',
		     width:150
		 },{
			 field:"company",
			 title:"单位",
			 align:'center',
		     width:91
		 }
		 ]],
		 id:'orderDOTableId'
	 });
}
function getCheckedData(){
	var checkStatus = table.checkStatus('orderDOTableId');
	checkedData = checkStatus.data;
	return checkedData;
}





