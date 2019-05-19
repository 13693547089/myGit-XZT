var table;
var checkedData;
layui.use('table', function(){
	  table = layui.table;
	  var $ = layui.$;
	  var index=parent.layer.getFrameIndex(window.name);
	  var parentData =parent.data;
	  var suppId = parentData.suppId;
	  var mateInfo = $('#mateInfo').val();
	  getMateData(suppId,mateInfo);
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	    //console.log(obj)
	  });
	  //条件搜索
	  var $ = layui.$, active = {
			  reload: function(){
				  var mateInfo2 = $('#mateInfo').val();
				  getMateData(suppId,mateInfo2);
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
			var checkStatus = table.checkStatus('mateTableId');
			checkedData = checkStatus.data;
			var index=parent.layer.getFrameIndex(window.name);
			parent.appoMates(checkedData);
			parent.layer.close(index);
		});
		function getMateData(suppId,mateInfo){
			$.ajax({
				type:"post",
				url:"/queryAllMaterialOfSuppForMan?suppId="+suppId+"&mateInfo="+mateInfo,
				dataType:"JSON",
				success:function(data){
					//initMateTable(table,data);
					addCheckbox(table,data);
				}
			});
		}
		//选中复选框
		function addCheckbox(table,data){
			var index=parent.layer.getFrameIndex(window.name);
			var data2 =parent.data;
			var tableData = data2.tableData;
			$.each(data,function(mateindex,mateitem){
				$.each(tableData,function(tableDataIndex,tableDataItem){
					if(tableDataItem.mateCode == mateitem.mateCode){
						data[mateindex]["LAY_CHECKED"]='true';
					}
				});
			});
			initMateTable(table,data);
		}
		
		
	  
});



function initMateTable(table,data){
	var mateTable = table.render({
		 elem:"#mateTable",
		 data:data,
		 page:false,
		 width: '100%', 
		 height:'auto',
		 limit:500,
		 cols:[[{
			 checkbox:true
		 },{
			 field:"mateName",
			 title:"物料名称",
			 align:'center',
		     width:230
		 },{
			 field:"mateCode",
			 title:"物料编码",
			 align:'center',
		     width:192
		 },{
			 field:"procUnit",
			 title:"单位",
			 align:'center',
		     width:91
		 }
		 ]],
		 id:'mateTableId'
	 });
}


function getCheckedData(){
	var checkStatus = table.checkStatus('mateTableId');
	checkedData = checkStatus.data;
	return checkedData;
}





