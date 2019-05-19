var upload;
var indexId;
var paperData = [];
var oldPaperData = [];
var delPaperData = [];
layui.use([ 'table', 'laytpl', 'upload', 'layer', 'laydate' ], function() {
	var form = layui.form;
	var $ = layui.$;
	upload = layui.upload;
	var table = layui.table;
	var laydate = layui.laydate;
	var layer = layui.layer;
	var suppId = $("#suppId").val();
	$.ajax({
		type : "post",
		url : "/queryLatentPapersOfLatentSupp",
		data : "suppId=" + suppId,
		dataType : "JSON",
		async : false,
		success : function(data) {
			$.each(data, function(index, item) {
				paperData.push(item);
				oldPaperData.push(item);
			});
		},
		error : function() {
			layer.msg("程序出错了！", {
				time : 1000
			});
		}
	});
	initPaperTable(paperData);
	$.ajax({
		type:"post",
		url:"/queryPapers",
		dataType:"JSON",
		async : false,
		success:function(data){
			initDownPaperTable(data)
		}
	});
	// 返回
	$("#goBack").click(function() {
		//window.history.back(-1);
		tuoBack('.latentSuppEdit','#serachSupp');
	});
	$('#addPaperBut').click(function(){
		debugger;
		$('#paperInfo').show();
		
	})
	// 保存潜在供应商
	$("#saveLatentSupp").click(function() {
		var paperTableData = $('#papertable').bootstrapTable("getData");
		if(paperTableData.length != 0){
			var result = checkMust();
			 if(!result.flag){
				 layer.msg(result.msg); 
				 return ;
			 }
			var categoryName = $("#categoryId option:selected").text();
			$("#category").val(categoryName);
			$("#paperTableData").val(JSON.stringify(paperTableData));
			var saveFormData = new FormData($('#latentSuppForm')[0]);// 序列化表单，
			// $("form").serialize()只能序列化数据，不能序列化文件
			$.ajax({
				type : "POST",
				url : "/updateLatentSuppBySuppId",
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
						deletePaperFromSer(paperTableData);
						parent.layer.msg("保存成功！", {
							time : 2000
						});
						//location="/getLatentSuppRegListHtml";
						//window.history.back(-1);
						tuoBack('.latentSuppEdit','#serachSupp');
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

	});
	judgeDis();
	// 删除选中行
	$("#removepaperrow").click(function() {
		// alert(11111)
	});

	$("#editSupp").click(function() {
		var suppId = $("#suppId").val();
		var url = "/updateLatentSupp?suppId=" + suppId + "&type=2";
		location = url;
	});

	function judgeDis() {
		var type = $("#type").val();
		if (type == '1') {
			$(".disa").attr("disabled", true);
		} else {
		}
	}

	// 刷新页面
	function reloadPage() {
		location.reload();
	}

	// 列表上的删除按钮(页面进行删除附件数据，不会删除服务器上的文件数据)
	$("#removepaperrow").click(function() {
		var checkedData = $('#papertable').bootstrapTable("getSelections");
		var docIds = [];
		$.each(checkedData, function(index, item) {
			docIds.push(item.fileId);
		});
		$('#papertable').bootstrapTable('remove', {
			field : 'fileId',
			values : docIds
		});
	});

});
//模板表格初始化
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
				        		   var editStr = '<a class="layui-btn layui-btn-xs blueInline" onclick="downLoadFile(\''
				        			   + row.id
				        			   + '\') " style="margin-left:10px !important;">下载</a>'
				        		   return editStr;
				        	   }
				           } ],
				           data : data
			});
}

// 模板表格初始化
function initPaperTable(data) {
	var type = $("#type").val();
	$('#papertable')
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
									field : '',
									checkbox : "true"
								},
								{
									field : "papersType",
									title : "附件类型",
									align : "center"
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
									formatter : function(value, row, index) {
										if(value==null || value==""){
											return "";
										}
										var date = new Date(value);
										var year = date.getFullYear();
										var month = date.getMonth() + 1;
										var day = date.getDate();
										return year
												+ "-"
												+ (month > 9 ? month : "0"
														+ month) + "-"
												+ (day > 9 ? day : "0" + day);
									}
								},
								{
									field : "endDate",
									title : "有效日期至",
									align : "center",
									formatter : function(value, row, index) {
										if(value==null || value==""){
											return "";
										}
										var date = new Date(value);
										var year = date.getFullYear();
										var month = date.getMonth() + 1;
										var day = date.getDate();
										return year
												+ "-"
												+ (month > 9 ? month : "0"
														+ month) + "-"
												+ (day > 9 ? day : "0" + day);
									}
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
										if(type=='1'){
											var editStr = '<a class="layui-btn layui-btn-xs blueInline" onclick="downLoadFile(\''
						        				   + row.fileId
						        				   + '\') " style="margin-left:10px !important;text-decoration:none;">下载</a>';
						        		   }else{
						        			   var editStr = '<a class="layui-btn layui-btn-xs blueInline" onclick="downLoadFile(\''
						        				   + row.fileId
						        				   + '\') " style="margin-left:10px !important;text-decoration:none;">下载</a>'
						        				   + '<a class="layui-btn layui-btn-danger layui-btn-xs redInline " style="text-decoration:none;" onclick="deleteFile(\''
						        				   + row.fileId + '\') ">删除</a>';
						        		   }
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

// 页面删除附件数据
function deleteFile(docId) {
	var docIds = [];
	var type = $("#type").val();
	if(type!='1'){
		docIds.push(docId);
		$('#papertable').bootstrapTable('remove', {
			field : 'fileId',
			values : docIds
		});
	}else{
		layer.msg("不能删除");
	}
}
// 文件下载
function downLoadFile(docId) {
	window.location.href = "/doc/downLoadDoc?docId=" + docId
}
// 保存成功后删除服务器上的文件（附件）
function deletePaperFromSer(paperTableData) {
	$.each(oldPaperData, function(oldIndex, oldItem) {
		var count = 0;
		$.each(paperTableData, function(index, item) {
			if (oldItem.fileId == item.fileId) {
				count++;
			}
		});
		if (count == 0) {
			delPaperData.push(oldItem);
		}
	});
	var docIds = [];
	$.each(delPaperData, function(index, item) {
		docIds.push(item.fileId);
	});
	$.ajax({
		type : 'post',
		url : "/template/deleteFile",
		data : {
			jsonStr : JSON.stringify(docIds)
		},
		async : false,
		success : function(msg) {

		},
		error : function() {
			layer.msg("程序出错了！", {
				time : 1000
			});
		}
	});
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
