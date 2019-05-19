//页面加载时初始化表格
layui.use([ 'table', 'layer','upload'],function(){
	var table=layui.table;
	var prefix = "/invenPlan";
	var layer =layui.layer;
	var planCode=$("#planCode").val();
	var data=[];
	var tableLns=[];
	var upload=layui.upload;
	initMainCheckTable(data);
	//点击excel按钮

	 var uploadInst = upload.render({
		    elem: '#excelBtn' //绑定元素
		    ,url: prefix+'/analysisTemp' //上传接口
		    ,accept: 'file'
		    ,done: function(res){
		      //上传完毕回调
		    	if(res.code==0){
		    		layer.msg("文件解析成功！",{time:1000});
		    		initMainCheckTable(res.data);
		    	}else{
			    	layer.msg("文件解析失败！",{time:1000});
		    	}
		    }
	 });

	//点击校验按钮后台校验数据
	$("#checkBtn").click(function(){
		var saveindex = layer.load(1, {shade: [0.8, '#1a1a1a']});
		var rows=tableLns.config.data;
		if(rows.length<=0){
			parent.layer.msg("没有需要校验的数据！", {
				time : 1000
			});
			layer.close(saveindex);
			return false;
		}
		$.ajax({
			url: prefix + "/checkTemp",
			type:"post",
			success:function(data){
				layer.close(saveindex);
				initMainCheckTable(data);
			},
			error:function(){
				layer.close(saveindex);
				layer.msg("数据校验出错了！", {
					time : 1000
				});
			}
		});
	});
	//点击确认进行数据的导入
	$("#confirmBtn").click(function(){
		var saveindex = layer.load(1, {shade: [0.8, '#1a1a1a']});
		var rows=tableLns.config.data;
		if(rows.length<=0){
			layer.msg("没有需要导入的数据！", {
				time : 1000
			});
			layer.close(saveindex);
			return false;
		}
		//查看数据是否校验通过
		//0 未校验数据，1校验通过 2,校验失败,3已导入
		var flag=1;
		$.each(rows,function(index,row){
			var state=row.restCode.code;
			if(state==null || state==-1){
				flag=0;
			}else if(state=="1"){
				flag=2;
			}
		});
		//提示信息
		if(flag==0){
			layer.msg("请先校验需要导入的数据！", {
				time : 1000
			});
			layer.close(saveindex);
			return false;
		}else if(flag==2){
			layer.msg("校验的数据存在错误，请修正后导入！", {
				time : 1000
			});
			layer.close(saveindex);
			return false;
		}else if(flag==3){
			layer.msg("该数据已经导入完成！", {
				time : 1000
			});
			layer.close(saveindex);
			return false;
		}
		$.ajax({
			url: prefix + "/saveTempData",
			type:"post",
			success:function(data){
				layer.close(saveindex);
				parent.loadMateTable();	
				closeDg();
				},
			error:function(){
				layer.close(saveindex);
				parent.layer.msg("程序出错了！", {
					time : 1000
				});
			}
		});
	});	
	
	function closeDg(){
		var index = parent.layer.getFrameIndex(window.name);//获取子窗口索引
		 parent.layer.close(index);
	}
	
	function initMainCheckTable(data){
		tableLns=table.render({
			elem : '#checkImportTable',
			id:'checkImportTable',
			data: data,
			page : true,
			cols : [ [ {
				field : 'rowNum',
				width: '5%',
				title : '序号'
			},{
				field : 'prodSeriesDesc',
				width: '8%',
				title : '系列'
			},{
				field : 'itemName',
				width: '8%',
				title : '产能类别'
			},{
				field : 'suppNo',
				width: '8%',
				title : 'SAP编码'
			},{
				field : 'suppName',
				width: '15%',
				title : '供应商'
			},{
				field : 'ranking',
				width: '5%',
				title : '排名'
			},  {
				field : 'mateDesc',
				width: '15%',
				title : '物料名称'
			}, {
				field : 'restCode',
				title : '校验结果',
				templet:function(d){
					var code=d.restCode.code;
					var msg=d.restCode.msg;
					if(code==0){
						return '<span class="green">校验通过</span>';
					}else if(code==1){
						return '<span class="red">'+msg+'</span>';
					}else{
						return "";
					}
				}
			},] ]
		});
	}
});


