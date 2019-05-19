//地址前缀
var prefix = "/bam/sf";
//选中节点
var direId="";

//定义树形控件
layui.use(['table','laydate','upload'], function() {
	var $ = layui.$;
	var table = layui.table;
	var upload = layui.upload;
	//定义日期格式
	var laydate = layui.laydate;
	//常规用法
    laydate.render({
    	elem: '#search_crtDateStart'
    });
	laydate.render({
		elem: '#search_crtDateEnd'
	});
	
	//初始上传按钮
	upload.render({
		elem: '#importBtn' //绑定元素
        ,url: prefix+'/importSaleFcstInfo' //上传接口
        ,accept: 'file' //普通文件
        ,exts: 'xls|xlsx' //只允许上传excel
    	,before: function(obj){
    		/*obj.preview(function(index, file, result){
    	    });*/
    	}
        ,done: function(res){
          //上传完毕回调
        	if(res){
        		if(res.code == 0){
        			layer.msg("导入成功！",{time:2000});
                	// 刷新
            		reloadTable();
        		}else{
        			layer.msg(res.msg,{time:2000});
        		}
        	}else{
    			layer.msg("导入异常，请检查！",{time:2000});
        	}
        }
        ,error: function(){
        	//请求异常回调
			layer.msg("导入异常！",{time:2000});
        }
    });
	
	initListTable(prefix+"/getSaleFcstPageList");
	//初始化表格
	function initListTable(url){
		var str=$('#createSpan').html().trim();
		var operateBar="#barDemo";
		if( str==''){
			operateBar="#barDemo1";
		}
	   table.render({
		     elem: '#list-table'
		    ,cellMinWidth: 100 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,url:url
		    //,id:'olist'
		    //,even: true
		    ,cols: [[
		       {checkbox:true}
			  ,{field:'sn', title: '序号',width:'5%',type:'numbers'}
			  //,{field:'status', title: '状态',width:'7%'}
		      ,{field:'fsctYear', title: '预测期间',width:'15%'}
		      ,{field:'fsctCode', title: '预测编号',width:'15%'}
		      ,{field:'fsctName', title: '预测名称'}
		      ,{field:'status', title: '状态',width:'8%'}
		      ,{field:'crtUser', title: '创建人',width:'8%'}
		      ,{field:'crtDate', title: '创建日期',width:'8%',
		    	  templet: function(d){
		    		  return formatTime(d.crtDate,'yyyy-MM-dd');
		    	  }
		      }
		      ,{field:'tools', title: '操作',width:'12%',toolbar:operateBar,fixed: 'right'}
		    ]]
		  ,page:true
		  ,initSort: {
		    field: 'fsctCode' //排序字段，对应 cols 设定的各字段名
		    ,type: 'desc' //排序方式  asc: 升序、desc: 降序、null: 默认排序
		  }
	  });
	}
	//表格监听事件
	 table.on('tool(docTool)', function(obj) {
		var data = obj.data;
		var id=data.id;
		if(obj.event == 'show'){
			//查看
			var type="2";
			var url=prefix+"/saleFcstDetailPage?mainId="+id+"&type="+type;
			//window.location.href=url;
			tuoGo(url,'saleFcstDetail','list-table');
		}else if (obj.event === 'edit') {
			/*var status = data.status;
			if(status == '已提交'){
				layer.msg("已提交状态下不允许编辑！",{time:2000});
				return;
			}*/
			
			//编辑
			var type="1";
			var url=prefix+"/saleFcstDetailPage?mainId="+id+"&type="+type;
			//window.location.href=url;
			tuoGo(url,'saleFcstDetail','list-table');
		}else if(obj.event === 'del'){
			
			var status = data.status;
			if(status == '激活'){
				layer.msg("激活状态下不允许删除！",{time:2000});
				return;
			}
			
			layer.confirm('是否删除该销售预测？', {
			  btn: ['确定','取消'] //按钮
			}, function(){
				//删除
				$.ajax({
					type:'POST',
					url:prefix+"/deleteSaleFcst",
					data:{id:id},
					success:function(msg){
						reloadTable();
						layer.msg("操作成功！",{time:1000});
					},
					error:function(){
						layer.msg("操作失败！",{time:1000});
					}
				});
			}, function(){
			});
		}
	});
	function reloadTable(){
		var fcstName=$("#search_fsctName").val();
		
		var crtDateStart=$("#search_crtDateStart").val();
		var crtDateEnd=$("#search_crtDateEnd").val();
		var url=prefix+"/getSaleFcstPageList?search_fsctName="+fcstName
		+"&search_crtDateStart="+crtDateStart+"&search_crtDateEnd="+crtDateEnd;
		initListTable(url);
	}
	
	//新建按钮点击事件
	$("#createBtn").click(function(){
		var type='1';
		var url=prefix+"/saleFcstDetailPage?mainId="+"&type="+type;
		//window.location.href=url;
		tuoGo(url,'saleFcstDetail','list-table');
	});
	
	//删除按钮点击事件
	$("#deleteBtn").click(function(){
		var rows = table.checkStatus('list-table').data;
		var ids=[];
		for(var i=0;i<rows.length;i++){
			if(rows[i].status == '未激活'){
				ids.push(rows[i].id);
			}
		}
		
		if(ids.length == 0){
			layer.msg("请选择状态为未激活需要删除的销售预测数据！",{time:1000});
			return;
		}
		
		layer.confirm('是否删除选中的销售预测数据？', {
		  btn: ['确定','取消'] //按钮
		}, function(){
			$.ajax({
				type:'POST',
				url:prefix+"/deleteBatchSaleFcst",
				data:{ids:JSON.stringify(ids)},
				success:function(msg){
					reloadTable();
					layer.msg("操作成功！",{time:1000});
				},
				error:function(){
					layer.msg("操作失败！",{time:1000});
				}
			});
		}, function(){
			
		});
	});
	
	//导出事件
	$("#exportBtn").click(function(){
		exportExcel(table);
	});
	
	// 查询点击事件
	$("#searchBtn").click(function(){
		reloadTable();
	});
	//查询条件重置
	$("#resetBtn").click(function(){
		 $("#searchForm")[0].reset();
	});
	// 导入模板下载
	$("#impTempDownBtn").click(function(){
		impTempDownload();
	});
});

/**
 * 导出excel
 * @returns
 */
function exportExcel(table){
	var checkStatus = table.checkStatus('list-table');
   	var checkedData = checkStatus.data;
	if(checkedData.length != 1){
		layer.msg("请选择一条导出的项！",{time:2000});
		return;
	}
	
	var year = checkedData[0].fsctYear;
	if(year == undefined || year =='' || year == null){
		layer.msg("请设置好销售预测期间！",{time:2000});
		return;
	}
   	
	var url = prefix+"/exportSaleFcstInfo?id="+checkedData[0].id+"&year="+checkedData[0].fsctYear;
	location.href=url;
}

/**
 * 导入模板下载
 * @returns
 */
function impTempDownload(){
	var url = prefix+"/impTempDownload";
	location.href=url;
}
