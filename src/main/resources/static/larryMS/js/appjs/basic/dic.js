var inintData;
var userId = "";
var userName ="";

$(function() {
	var config = {
		'.chosen-select' : {
			search_contains : true
		},
		'.chosen-select-deselect' : {
			allow_single_deselect : true
		},
		'.chosen-select-no-single' : {
			disable_search_threshold : 10
		},
		'.chosen-select-no-results' : {
			no_results_text : 'Oops, nothing found!'
		},
		'.chosen-select-width' : {
			width : "95%"
		}
	}
	for ( var selector in config) {
		$(selector).chosen(config[selector]);
	}
	//获取当前登录人信息
//	$.ajax({
//		type : "GET",
//		url : "/sys/user/getUserMessage",
//		async : true,
//		error : function(request) {
//			parent.layer.msg("程序出错了！", {
//				time : 1000
//			});
//		},
//		success : function(r) {
//			var user = r.user;
//			userId = user.userId;
//			userName = user.name;
//		}
//	});

	function inint(searchArgs) {
		$("#dicTable").bootstrapTable('destroy');
		$("#dicTable").bootstrapTable({
			onDblClickRow : function(row, $element, field) {
				$('#viewCode').val(row.cateCode);
				$('#viewName').val(row.cateName);
				// 通过ajax初始化数据
				inintDicAjax(row.id, 'view');
				$('#viewModal').modal('show');
			},
			url : '/dicInfo/dic',
			method : 'post',
			editable : true,
			contentType : "application/x-www-form-urlencoded",
			queryParamsType : searchArgs,
			queryParams : function queryParams(params) {
				var param = {
					page : params.pageNumber,
					limit : params.pageSize
				};
				for ( var key in searchArgs) {
					param[key] = searchArgs[key]
				}
				return param;
			},
			undefinedText : "",
			locale : 'zh-CN',
			pagination : true,
			pageNumber : 1,
			pageSize : 10,
			pageList : [ 10, 20 ],
			sidePagination : "server",
			showRefresh : false,
			columns : [ {
				checkbox : true
			}, {
				field : 'cateName',
				title : '分类名称',
				edit : false
			}, {
				field : 'cateCode',
				title : '分类编码',
				edit : false
			}, {
				field : 'isUsed',
				title : '状态',
				formatter : function(value, row, index) {
					if (value == '1') {
						return '启用';
					} else {
						return '禁用';
					}
				},
				edit : false
			}, {
				field : 'creatorName',
				title : '创建人',
				edit : false
			}, {
				field : 'createTime',
				title : '创建时间',
				formatter : timeFormatter,
				edit : false
			}, {
				field : 'modifier',
				title : '修改人',
				edit : false
			}, {
				field : 'updatetime',
				title : '修改时间',
				formatter : timeFormatter,
				edit : false
			} ]
		})
	}

	function inintDicInfoTable(data) {
		$("#dicInfoTable").bootstrapTable('destroy');
		$("#dicInfoTable").bootstrapTable({
			editable : true,// 开启编辑模式
			data : data,
			undefinedText : "",
			locale : 'zh-CN',
			pagination : true,
			pageNumber : 1,
			pageSize : 10,
			pageList : [ 10, 20 ],
			showRefresh : false,
			columns : [ {
				checkbox : true
			}, {
				field : 'parentId',
				title : '组别'
			},{
				field : 'dicCode',
				title : '分类编码'
			},{
				field : 'dicName',
				title : '名称'
			}, {
				field : 'remark',
				title : '备注',
				width:"150px",
			}, {
				field : 'isDelete',
				title : '是否停用',
				width:"100px",
				edit:{
					type:'select',//下拉框
					data:[{id:"X",text:'是'},{id:"O",text:'否'}],
        			valueField:'id',
        			textField:'text',
        			onSelect:function(val,rec){
        				console.log("value---"+val);
        				console.log("text---"+JSON.stringify(rec));
        			}
				},
				formatter:function(value,row,index){
					if(value=="X"){
						return "是";
					}else {
						return "否";
					}
				}
			},{
				field : 'sortIndex',
				title : '排序码',
			},{
				field : 'modifyTime',
				title : '最后修改时间',
				edit:false,
				width:"100px",
				formatter : function(value,row,index) {
					return formatTime(value, 'yyyy-MM-dd');
				}
			},{
				field : 'modifierName',
				edit:false,
				width:"100px",
				title : '最后修改人',
			},{
				field : 'tableId',
				title : 'ID',
				visible : false
			} ]
		})

	}

	function inintDicInfoViewTable(data) {
		$("#dicInfoViewTable").bootstrapTable('destroy');
		$("#dicInfoViewTable").bootstrapTable({
			data : data.rows,
			undefinedText : "",
			locale : 'zh-CN',
			pagination : true,
			pageNumber : 1,
			pageSize : 10,
			pageList : [ 10, 20 ],
			showRefresh : false,
			columns : [ {
				field : 'parentId',
				title : '组别'
			},{
				field : 'dicCode',
				title : '分类编码'
			},{
				field : 'dicName',
				title : '名称'
			}, {
				field : 'remark',
				title : '备注',
				width:"150px",
			}, {
				field : 'isDelete',
				title : '是否停用',
				width:"100px",
				formatter:function(value,row,index){
					if(value=="X"){
						return "是";
					}else {
						return "否";
					}
				}
			},{
				field : 'sortIndex',
				title : '排序码',
			},{
				field : 'modifyTime',
				title : '最后修改时间',
				edit:false,
				width:"100px",
				formatter : function(value,row,index) {
					return formatTime(value, 'yyyy-MM-dd');
				}
			},{
				field : 'modifierName',
				edit:false,
				width:"100px",
				title : '最后修改人',
			} ]
		})
	}

	inint({});
	validateForm();

	// 新建字典信息
	$('#createCategory').click(function() {
		$('#code').val("");
		$('#name').val("");
		$('#cateId').val("");
		inintDicInfoTable({});
	});
	// 点击查询
	$('#seachBtn').click(function() {
		var searchArgs = {
			cateName : $('#categoryName').val(),
			cateCode : $('#categoryCode').val(),
			cateType : $('#status').val()
		}
		inint(searchArgs);
	});
	//重置
	$('#resetBut').click(function() {
		$('#categoryName').val(""),
		$('#categoryCode').val(""),
		$("#status option[value='']").prop("selected",true);
		var searchArgs={};
		inint(searchArgs);
	});

	// 使用ajax初始化弹出字典表数据
	function inintDicAjax(categoryId, flag) {
		$.ajax({
			type : "POST",
			url : "/findDiByCateID",
			async : true,
			data : {
				cateId : categoryId
			},
			error : function(request) {
				parent.layer.msg("程序出错了！", {
					time : 1000
				});
			},
			success : function(r) {
				if (r) {
					if (flag === 'view') {
						inintDicInfoViewTable(r);
					} else if (flag === 'edit') {
						inintDicInfoTable(r.rows);
					}
				}
			}
		});
	}

	// 编辑功能
	$('#editBtn').click(function() {
		debugger;
		var rows = $('#dicTable').bootstrapTable('getAllSelections');
		if (rows.length != 1) {
			parent.layer.msg("请选择一条进行编辑！", {
				time : 1000
			});
		} else {
			$('#cateCode').val(rows[0].cateCode);
			$('#cateName').val(rows[0].cateName);
			$('#cateId').val(rows[0].id);
			// 通过ajax初始化数据
			inintDicAjax(rows[0].id, 'edit');
			$('#createModal').modal('show');
		}
	});

	// 删除功能
	$('#deleteBtn').click(function() {
		debugger;
		var rows = $('#dicTable').bootstrapTable('getAllSelections');
		if (rows.length > 0) {
			if (confirm("是否删除？"))
				$.ajax({
					type : "POST",
					url : "/deleteCategoryInfo",
					async : true,
					data : {
						selected : JSON.stringify(rows)
					},
					error : function(request) {
						parent.layer.msg("程序出错了！", {
							time : 1000
						});
					},
					success : function(r1) {
						if (r1) {
							$('#dicTable').bootstrapTable('refresh');
							$('#createModal').modal('hide');
						}
					}
				});
		} else {
			parent.layer.msg("请选择要删除的分类！");
		}
	});
});
// 可编辑列表新增一行
$("#addDicRow").click(function() {
	var row = {
		id : new Date().getTime(),
	};
	// 通过mark来判断为哪个可编辑框创建新一行
	$('#dicInfoTable').bootstrapTable('append', row);
});
// 删除选中的
$('#removeDicRows').click(function() {
	var rows = $('#dicInfoTable').bootstrapTable('getAllSelections');
	var ids = [];
	$.each(rows, function(index, row) {
		ids.push(row.id);
	});
	// 通过mark来判断为哪个可编辑框创建新一行
	$('#dicInfoTable').bootstrapTable('remove', {
		field : 'id',
		values : ids
	});
});

function validateForm() {
	$('#createCategoryForm').bootstrapValidator({
		message : "this is not valid",
		excluded : [ ":disabled" ],
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			name : {
				validators : {
					notEmpty : {
						message : "分类名称不能为空！"
					}
				}
			},
			code : {
				validators : {
					notEmpty : {
						message : "分类编码不能为空！"
					}
				}
			},
		}
	})
}

// 保存分类信息
$('#saveCategoryBtn').click(function() {
	debugger;
	// 所有验证过再请求后台
	var bv = $('#createCategoryForm').data('bootstrapValidator');
	bv.validate();
	if (bv.isValid()) {
		debugger;
		$.ajax({
			type : "POST",
			url : "/checkCode",
			async : true,
			data : $('#createCategoryForm').serialize(),
			error : function(request) {
				parent.layer.msg("程序出错了！", {
					time : 1000
				});
			},
			success : function(r) {
				if (r) {
					debugger;
					var rows = $('#dicInfoTable').bootstrapTable('getData');
					var cateid = 
					$("#dicList").val(JSON.stringify(rows));
					$.ajax({
						type : "POST",
						url : "/saveDicInfo",
						async : true,
						data : $('#createCategoryForm').serialize(),
						error : function(request) {
							parent.layer.msg("程序出错了！", {
								time : 1000
							});
						},
						success : function(r1) {
							if (r1) {
								$('#dicTable').bootstrapTable('refresh');
								$('#createModal').modal('hide');
							}
						}
					});

				} else {
					parent.layer.msg("编码重复！", {
						time : 1000
					});
				}
			}
		});
	}
});
