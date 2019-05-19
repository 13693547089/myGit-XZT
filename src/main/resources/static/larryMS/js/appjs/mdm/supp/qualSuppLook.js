var upload;
var indexId;
var paperData = [];
var oldPaperData = [];
var delPaperData = [];
layui.use('form', function(){
	  var form = layui.form;
	  var $ = layui.$;
	  var suppId = $("#suppId").val();
		$.ajax({
			type : "post",
			url : "/queryPapersOfQualSuppBysuppId",
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
		
		var bankAccount = $("#bankAccount2").val();
		var accountHolder = $("#accountHolder2").val();
		if(bankAccount == "null" || bankAccount == "NULL"){
			$("#bankAccount").val("");
		}else{
			$("#bankAccount").val(bankAccount);
		}
		if(accountHolder == "null" || accountHolder == "NULL"){
			$("#accountHolder").val("");
		}else{
			$("#accountHolder").val(accountHolder);
		}
		//返回
		 $("#goBack").click(function(){
		 	 //window.history.back(-1);
			 tuoBack('.qualSuppLook','#serachSupp');
		 });
		// 保存合格供应商的附件
		$("#saveBut").click(function() {
			var paperTableData = $('#papertable').bootstrapTable("getData");
			if(paperTableData.length>0){
				$("#paperTableData").val(JSON.stringify(paperTableData));
				var saveFormData = new FormData($('#qualSuppForm')[0]);// 序列化表单，
				// $("form").serialize()只能序列化数据，不能序列化文件
				$.ajax({
					type : "POST",
					url : "/updatePaperOfQualSuppBySuppId",
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
							tuoBack('.qualSuppLook','#serachSupp');
						} else {
							parent.layer.msg("保存失败！", {
								time : 2000
							});
						}
					}
				});
			}else{
				layer.msg("请添加附件")
			}

		});
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
		
		//添加弹窗
		$("#addpaperrow").click(function(){
			$("#endDate").val("9999-12-31");
		});
		
});
 


//模板表格初始化
function initPaperTable(data) {
	var type = $("#type").val();
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
									formatter : function(value, row, index) {
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
									width : "160",
									formatter : function(value, row, index) {
										if(type=='1'){
											var editStr = '<a class="layui-btn layui-btn-xs blueInline" onclick="downLoadFile(\''
												+ row.fileId
												+ '\') " style="margin-left:10px !important; text-decoration:none;">下载</a>';
										}else{
											var editStr = '<a class="layui-btn layui-btn-xs blueInline" onclick="downLoadFile(\''
												+ row.fileId
												+ '\') " style="margin-left:10px !important; text-decoration:none;">下载</a>'
												+ '<a class="layui-btn layui-btn-danger layui-btn-xs redInline " style="text-decoration:none;" onclick="deleteFile(\''
												+ row.fileId + '\') ">删除</a>';
										}
										return editStr;
									}
								} ],
						data : data
					});
}

//弹出层的确认按钮,添加附件
function affirm() {
	var result = checkMust();
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

//页面删除附件数据
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

//文件下载
function downLoadFile(docId) {
	window.location.href = "/doc/downLoadDoc?docId=" + docId
}

//保存成功后删除服务器上的文件（附件）
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
