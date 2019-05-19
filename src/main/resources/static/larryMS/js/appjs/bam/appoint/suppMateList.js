var table;
var checkedData;
layui.use('table', function(){
	  table = layui.table;
	  var $ = layui.$;
	  initMateTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  
	  //条件搜索
	  var $ = layui.$, active = {
			    reload: function(){
			      var mateInfo = $('#mateInfo');
			      var seriesInfo = $('#seriesInfo');
			      //执行重载
			      table.reload('mateTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {
			        	 mateInfo: mateInfo.val(),
			        	 seriesInfo: seriesInfo.val(),
			        }
			      });
			    }
		};
	 
	  //监听单元格事件
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'setSign'){
	     var url ="/getCheckQualSuppHtml?suppId="+data.suppId;
	     location=url;
	    }
	  });
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  
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



function initMateTable(){
	var mateTable = table.render({
		 elem:"#mateTable",
		 url:"/queryAllMaterialOfSupp",
		 page:true,
		 width: '100%',
		 height:'auto',
		 limit:10,
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
		 },
		 {
			 field:"seriesCode",
			 title:"系列编码",
			 align:'center',
			 width:192
		 },
		 {
			 field:"seriesExpl",
			 title:"系列说明",
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





