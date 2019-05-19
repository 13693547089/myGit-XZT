var upload;
var indexId;
var paperData = [];
layui.use([ 'table', 'laytpl', 'upload', 'layer', 'laydate' ], function() {
	var form = layui.form;
	var $ = layui.$;
	upload = layui.upload;
	var table = layui.table;
	var laydate = layui.laydate;
	var layer = layui.layer;
	initPaperTable(paperData);
	$.ajax({
		type:"post",
		url:"/queryPapers",
		dataType:"JSON",
		success:function(data){
			initDownPaperTable(data)
		}
	});
	// 返回
	$("#goBackBut").click(function() {
		/*
		 * var paperTableData = $('#papertable').bootstrapTable("getData"); var
		 * docIds = []; $.each(paperTableData,function(index,item){
		 * docIds.push(item.fileId); }); $.ajax({ type:'post',
		 * url:"/deletePaperFile", data:{jsonStr:JSON.stringify(docIds)},
		 * success:function(msg){
		 *  }, error:function(){ layer.msg("程序出错了！",{time:1000}); } });
		 */
		//window.history.back(-1);
		tuoBack('.latentSuppReg','#serachSupp');
	});

	// 下载
	$("[name=downPaper]").click(function() {
		window.location.href = "/doc/downLoadDoc?docId=" + docId
	});
	// 列表上的删除按钮
	$("#removepaperrow").click(function() {
		var checkedData = $('#papertable').bootstrapTable("getSelections");
		var docIds = [];
		$.each(checkedData, function(index, item) {
			docIds.push(item.fileId);
		});
		$.ajax({
			type : 'post',
			url : "/deletePaperFile",
			data : {
				jsonStr : JSON.stringify(docIds)
			},
			success : function(msg) {
				if (msg.code == '0') {
					$('#papertable').bootstrapTable('remove', {
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

	});
	
	window.returnFunction = function() {
		debugger
		var suppId = $("#suppId").val();
		$.ajax({
			type:"post",
			url:"/buyerAuditLatentSupp?suppId="+suppId,
			dataType:"JSON",
			error:function(request){
				parent.layer.msg("程序出错了！", {
					time : 2000
				});
			},
			success:function(data){
				if(data){
					window.history.go(-1);
				}else{
					layer.msg("初审失败！");
				}
			}
		});
	}
	
});

// 模板表格初始化
function initDownPaperTable(data) {
	$('#downPapertable')
	.bootstrapTable(
			{
				method : 'get',
				editable : false,// 关闭编辑模式
				undefinedText : '',
				pageList : [ 10, 20 ],
				pageSize : 10,
				pageNumber : 1,
				striped : true,
				locale : 'zh-CN',
				columns : [
						  {
								field : "index",
								title : "序号",
								align : "center",
								width : "100",
								formatter : function(value, row, index) {
									return row.index = index+1; //返回行号  
								}
						   },
				           {
				        	   field : "realName",
				        	   title : "附件名称",
				        	   align : "center"
				        	   
				           },
				           {
				        	   field : "id",
				        	   title : "fileId",
				        	   align : "center",
				        	   visible : false
				           },
				           {
				        	   field : 'status',
				        	   title : '附件 操作',
				        	   align : "center",
				        	   width : "120",
				        	   formatter : function(value, row, index) {
				        		   var editStr = '<a class="layui-btn layui-btn-sm blueInline" onclick="downLoadFile(\''
				        			   + row.id
				        			   + '\') " style="margin-left:10px !important;">下载</a>'
				        		   return editStr;
				        	   }
				           } ],
				           data : data
			});
}
// 模板表格初始化附件
function initPaperTable(data) {
	$('#papertable')
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
									field : '',
									checkbox : "true"
								},
								{
									field : "papersType",
									title : "附件类型",
									align : "center",
									edit : {
										type : 'select',// 下拉框
										required : true,
										url : '/queryPaperType',
										valueField : 'dicName',
										textField : 'dicName',
										onSelect : function(val, rec) {
										}
									}
								},
								{
									field : "papersName",
									title : "名称",
									align : "center"
								},
								{
									field : "startDate",
									title : "有效日期从",
									align : "center",
								},
								{
									field : "endDate",
									title : "有效日期至",
									align : "center",
								},
								{
									field : "acceOldName",
									title : "附件",
									edit : false,
									align : "center"
								},
								{
									field : "acceNewName",
									title : "新文件名称",
									align : "center",
									visible : false,
									edit : false
								},
								{
									field : "acceUrl",
									title : "文件Url",
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
									field : 'indexId',
									title : "indexId",
									align : 'center',
									visible : false,
									edit : false,
								},
								{
									field : 'status',
									title : '附件 操作',
									align : "center",
									formatter : function(value, row, index) {
										var editStr = '<a class="layui-btn layui-btn-sm blueInline" onclick="downLoadFile(\''
												+ row.fileId
												+ '\') " style="margin-left:10px !important;">下载</a>'
												+ '<a class="layui-btn layui-btn-sm layui-btn-danger blueInline" onclick="deleteFile(\''
												+ row.fileId + '\') ">删除</a>';
										return editStr;
									}
								} ],
						data : data
					});
}

// 弹出层的确认按钮,添加附件
function affirm() {
	var result = checkRequ();
	 if(!result.flag){
		 layer.msg(result.msg); 
		 return ;
	 }
	var papersType = $("#papersType").val();
	var papersId = $("#papersId").val();
	var papersName = $("#papersId option:selected").text();
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var saveFormData = new FormData($('#paperForm')[0]);// 序列化表单，
	// $("form").serialize()只能序列化数据，不能序列化文件
	// 文件上传
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
			if (res.code == '0') {
				// 文件上传成功
				var index = $('#papertable').bootstrapTable('getData').length;
				var object = new Object();
				object.papersType = papersType;
				object.papersName = papersName;
				object.startDate = startDate;
				object.endDate = endDate;
				object.acceOldName = res.data[0].realName;
				object.acceNewName = res.data[0].fileName;
				object.acceUrl = res.data[0].fileUrl;
				object.fileId = res.data[0].id;
				object.indexId = index;
				paperData.push(object);
				$('#papertable').bootstrapTable("load", paperData);
			}

		}
	});
}
// 删除附件(包括文件服务上的文件)
function deleteFile(docId) {
	var docIds = [];
	docIds.push(docId);
	$.ajax({
		type : 'post',
		url : "/template/deleteFile",
		data : {
			jsonStr : JSON.stringify(docIds)
		},
		success : function(msg) {
			if (msg.code == '0') {
				$('#papertable').bootstrapTable('remove', {
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
// 文件下载
function downLoadFile(docId) {
	window.location.href = "/doc/downLoadDoc?docId=" + docId
}
// 保存/批准潜在供应商 type=1 ：保存，type=2 :批准
function saveLatentSupp(type) {
	var paperTableData = $('#papertable').bootstrapTable("getData");
	if(paperTableData.length != 0){
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg); 
			 return ;
		 }
		var categoryName = $("#categoryId option:selected").text();
		$("#category").val(categoryName);
		debugger;
		var suppId = guid();
		$("#suppId").val(suppId);
		var suppName = $("#suppName").val();
		//var format = new Date().Format("yyyy-MM-dd");
		var remark = "潜在供应商审核: "+suppName;
		$("#paperTableData").val(JSON.stringify(paperTableData));
		var saveFormData = new FormData($('#latentSuppForm')[0]);// 序列化表单，
		// $("form").serialize()只能序列化数据，不能序列化文件
		$.ajax({
			type : "POST",
			url : "/addLatentSupp?type=" + type,
			data : saveFormData,// 你的form的id属性值
			processData : false,
			contentType : false,
			async : false,
			error : function(request) {
				parent.layer.msg("程序出错了！", {
					time : 2000
				});
			},
			success : function(data) {
				if (data) {
					parent.layer.msg("保存成功！", {
						time : 2000
					});
					if(type=="1"){//保存
						var flag = taskProcess(suppId, "suppAudit", remark, "save");
						//window.history.back(-1);
						tuoBack('.latentSuppReg','#serachSupp');
					}else{//批准
						var flag = taskProcess(suppId, "suppAudit", remark, "process");
					}
					//location="/getLatentSuppRegListHtml";
				} else {
					parent.layer.msg("保存失败！", {
						time : 2000
					});
				}
			}
		});
	}else{
		layer.alert('<span style="color:red;"><h3>请添加相关证件</h3></span>');
	}
}


function checkRequ(){
	var result={};
	var flag=true;
	var list=[];
	var  msg='';
	$(".requ").each(function(index,row){
		var doc =$(this);
		var val=doc.val();
		var name=doc.parent("div").prev("label").html();
		var arr=name.split("</i>");
		name=arr[arr.length-1];
		if(val==null || val==''){
			msg+=name+'不能为空！';
			flag=false;
		}
	});
	result.msg=msg;
	result.flag=flag;
	return result;
}

//生成GUID
/*function guid() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}*/
