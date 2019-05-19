var table;
var prefix = "/bam/cf";

var checkedRow = [];
layui.use('table', function(){
	  table = layui.table;
	  var $ = layui.$;
	  
	  // 加载数据
	  initTableData();
	  
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
		  	var type = obj.type;
			var checked = obj.checked;
			var data = obj.data;
			if (type == 'all') {
				var cacheData = table.cache.seriesTableId;
				if (checked) {
					var tempData = checkedRow;
					for ( var k = 0; k < cacheData.length; k++) {
						var elem = cacheData[k];
						if (tempData.indexOf(elem) == -1)
							checkedRow.push(elem);
					}
				} else {
					var tempData = [];
					for ( var k = 0; k < checkedRow.length; k++) {
						var elem = checkedRow[k];
						if (cacheData.indexOf(elem) == -1)
							tempData.push(elem);
					}
					checkedRow = tempData;
				}
			} else if (type = 'one') {
				if (checked) {
					checkedRow.push(data);
				} else {
					var tempData = [];
					for ( var i = 0; i < checkedRow.length; i++) {
						var elem = checkedRow[i];
						if (elem.mateId != data.mateId)
							tempData.push(elem);
					}
					checkedRow = tempData;
				}
			}
	  });
	  
	  /**
	   * 查询
	   */
	  $('#searchBtn').click(function(){
		  initTableData();
	  });
	  
	  //取消
	  $("#cancel").click(function(){
	  	var index=parent.layer.getFrameIndex(window.name);
	  	parent.layer.close(index);
	  });
	  
	  //确定
	  $("#confirm").click(function(){
	  	var checkStatus = table.checkStatus('seriesTableId');
	  	var data = checkStatus.data;
	  	var userId = $("#userId").val();
	  	var suppIds =[];
	  	 $.ajax({
	  			type:"post",
	  			url:"/queryQualSuppOfUser?userId="+userId,
	  			dataType:"JSON",
	  			async: false,
	  			success:function(userData){
	  				$.each(data,function(suppindex,suppitem){
	  					var a = 0;
	  					$.each(userData.data,function(userindex,useritem){
	  						if(useritem.suppId == suppitem.suppId){
	  							a++;
	  						}
	  					});
	  					if(a == 0){
	  						suppIds.push(suppitem.suppId);
	  					}
	  				});
	  			}
	  	});
	  	 if(suppIds.length!=0){
	  		 $.ajax({
	  				type:"post",
	  				url:"/addQualSuppForUser?userId="+userId,
	  				data:"suppIds="+suppIds,
	  				dataType:"JSON",
	  				success:function(data){
	  					if(data){
	  						layer.msg('供应商添加成功',{time:2000});
	  						var index=parent.layer.getFrameIndex(window.name);
	  					  	parent.layer.close(index);
	  					  	parent.reload();
	  					}
	  				},
	  				error : function(xhr) {
	  					layer.msg('程序错误');
	  				}
	  		});
	  	 }else{
	  		layer.msg('无法添加重复供应商');
	  	 }	  	
	  });
	  
	  function initTableData(){
		  var seriesExpl = $("#seriesExpl").val();
		  $.ajax({
			type:"post",
			url:prefix+"/getMaterialSeriesData",
			data:{"seriesExpl":seriesExpl},
			dataType:"JSON",
			async: false,
			success:function(data){
				loadTableData(data);
			},
			error : function(xhr) {
				layer.msg('数据获取异常！');
			}
		  });
	}

	function loadTableData(tableData){
		table.render({
			 elem:"#seriesTable",
			 data:tableData,
			 page:true,
			 width: '100%', 
			 height:'auto',
			 limit:15,
			 limits:[15,30,50,100],
			 cols:[[{
				 checkbox:true
			 },{
				 field:"seriesCode",
				 title:"系列编码",
				 align:'center',
			     width:150
			 },{
				 field:"seriesExpl",
				 title:"系列名称",
				 align:'center',
			     width:200
			 }
			 ]],
			 id:'seriesTableId'
		 });
	}
});

function getCheckedData(){
	return checkedRow;
}






