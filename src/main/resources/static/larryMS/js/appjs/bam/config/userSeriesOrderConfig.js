var currentRow = "";
var prefix = "/bam/cf";
var table;
var tableData;

// 选择行index
var selIndex=-1;
// 用户列表数据
var userTbData;
layui.use('table', function() {
	table = layui.table;
	var $ = layui.$;
	// 执行一个 table 实例
	var userTable = table.render({
		elem : '#userTable',
		url : prefix+'/getSeriesOrderUserData',
		page : true,
		limit: 30, //显示的数量
		limits: [30,50,100], //显示的数量
		//even: true,//隔行变色
		height:700,
		cols : [ [ {
			checkbox : true
		},{
			field : 'userCode',
			title : '编号',
			align : 'center',
			width : 280
		}, {
			field : 'userName',
			title : '姓名',
			align : 'center'
		} ] ],
		id : 'userTableId',
		done : function(res, curr, count) {
			var data = res.data;
			userTbData = data;
			if(data != ""){
				currentRow = data[0].id;
				inintAssignTable(table, currentRow);
				
				$('.layui-table-body tr').each(function() {
					var dataindex = $(this).attr('data-index');
					var idx = 0;
					for ( var item in data) {
						if (dataindex == idx) {
							$(this).click(function() {
								currentRow = data[item].id;
								inintAssignTable(table, currentRow);
							});
							break;
						}
						idx++;
					}
				});
			}else{
				inintAssignTable(table, "0");
			}
		}
	});
	
	//搜索采购员
	$("#userCodeName").keydown(function(e) {  
        if (e.keyCode == 13) {
        	loadUserData();
        }  
	});
	
	/**
	 * 添加用户
	 */
	$('#addUserBtn').on('click', function() {
		layer.open({
			type : 2,
			title : '添加用户',
			shadeClose : false,// 这个属性为true时点击遮罩会关闭弹窗，false则只有点击关闭按钮才能关闭弹窗
			shade : 0.1,//默认是有遮罩
			content : prefix+'/userSelDialog',
			area : [ '800px', "80%" ],
			maxmin : true, // 开启最大化最小化按钮
			btn: ['确认', '取消'],
			yes: function(index, layero){
				  //按钮【按钮一】的回调
				  // 获取选中的物料数据
				  var checkedData = $(layero).find("iframe")[0].contentWindow
				      .getCheckedData();
				  // 关闭弹框
				  layer.close(index);
				  if(checkedData.length>0){
					  // 处理数据
					  dealSelUserData(checkedData);
				  }
		     },
		     btn2: function(index, layero){
			      //按钮【按钮二】的回调
			      // 默认会关闭弹框
			      //return false 开启该代码可禁止点击该按钮关闭
		     }
		});
	});

	/**
	 * 删除用户系列排序数据
	 */
	$('#delUserBtn').on('click', function() {
		var checkRows = table.checkStatus('userTableId');
		var checkData = checkRows.data;
		if (checkData.length > 0) {
			var ids = new Array();
			for ( var i = 0; i < checkData.length; i++) {
				ids.push(checkData[i].id);
			}
			layer.confirm('确定删除选中的用户及其系列排序数据吗？', function(index){
				layer.close(index);
				$.ajax({
					type : 'post',
					url : prefix+'/delSeriesOrderUserData',
					data:{"ids":ids.join(',')},
					dataType : 'JSON',
					success : function(data) {
						if(data == 1){
							layer.msg('删除成功！',{time:2000});
							// 重新加载数据
							loadUserData();
						}else{
							layer.msg('删除失败！',{time:2000});
						}
					},
					error : function(xhr) {
						layer.msg('刪除异常！');
					}
				});
			});
		}else {
			layer.alert('<span style="color:red;">请选择要删除的用户</sapn>');
		}
	});
	
	// 保存用户系列排序数据
	$('#saveBtn').on('click',function() {
		if(tableData.length == 0){
			return;
		}
		var parentId = tableData[0].parentId;
		var detailData = JSON.stringify(tableData);
		$.ajax({
			type : 'post',
			url : prefix+'/saveUserSeriesOrderDetail',
			data:{"detailData":detailData},
			dataType : 'JSON',
			success : function(data) {
				if(data.code == 0){
					layer.msg('保存成功！',{time:2000});
					inintAssignTable(table, parentId);
				}else{
					layer.msg('保存失败：'+data.msg,{time:2000});
				}
			},
			error : function(xhr) {
				layer.msg('保存异常！');
			}
		});
	});
	
	// 删除选中的系列
	$('#delBtn').on('click',function() {
			var checkRows = table.checkStatus('userSeriesTableId');
			var checkData = checkRows.data;
			if (checkData.length > 0) {
				var ids = new Array();
				// 关联用户id
				var parentId = checkData[0].parentId;
				for ( var i = 0; i < checkData.length; i++) {
					if(checkData[i].id != undefined){
						ids.push(checkData[i].id);
					}
				}
				layer.confirm('确定删除选中的系列吗？', function(index){
					layer.close(index);
					$.ajax({
						type : 'post',
						url : prefix+'/delUserSeriesOrderDetailInfo',
						data:{"ids":ids.join(',')},
						dataType : 'JSON',
						success : function(data) {
							if(data == 1){
								layer.msg('删除成功！',{time:2000});
								inintAssignTable(table, parentId);
							}else{
								layer.msg('删除失败！',{time:2000});
							}
						},
						error : function(xhr) {
							layer.msg('刪除异常！');
						}
					});
				});
			}else {
				layer.alert('<span style="color:red;">请选择要删除的系列</sapn>');
			}
	 });
	
	$(document).on("click",".xuanzhong table tbody tr",function() {
		$('#userTable + div tr').removeClass('pitch');
		$(event.target).parents('tr').addClass('pitch');
		
		// 获取选择行的索引
		var row = $(event.target).parents('tr');
		selIndex=row[0].rowIndex;
	});
});

/**
 * 加载用户系列排序数据
 * @param table
 * @param parentId
 * @returns
 */
function inintAssignTable(table, parentId) {
	$.ajax({
		type : 'post',
		url : prefix+'/getUserSeriesOrderDetail',
		data:{"parentId":parentId},
		dataType : 'JSON',
		success : function(data) {
			if(data.length>0){
				tableData= data;
			}else{
				tableData = [];
			}
			loadDetailData(tableData);
		},
		error : function(xhr) {
			tableData = [];
			loadDetailData(tableData);
		}
	});
}

/**
 * 加载数据
 * @param data
 * @returns
 */
function loadDetailData(data){
	table.render({
		elem : '#userSeriesTable',
		data : tableData,
		page : true,
		height:'auto',
		limit:20,
		limits:[20,30,50,100],
		cols : [ [ {
			checkbox : true
		},{
			field : 'sn',
			title : '排序',
			align : 'center',
			edit: 'text',
			width : 170
		}, {
			field : 'seriesCode',
			title : '系列编码',
			align : 'center',
			width : 150
		}, {
			field : 'seriesName',
			title : '系列名称',
			align : 'center'
		}
		] ],
		id : 'userSeriesTableId'
	});
}

/**
 * 处理选择的用户数据
 */
function dealSelUserData(data){
	
	if(data.length==0){
		return;
	}
	
	var userData = [];
	for(var i=0;i<data.length;i++){
		var item = {userCode:data[i].username,
				userName:data[i].name};
		userData.push(item);
	}
	
	// 转成json
	var selUserData = JSON.stringify(userData);
	$.ajax({
		type:"post",
		url:prefix+"/saveSeriesOrderUserData",
		data:{"userData":selUserData},
		dataType:"JSON",
		async: false,
		success:function(data){
			if(data.code==0){
				layer.msg('添加成功！',{time:2000});
				// 重新加载数据
				loadUserData();
			}else{
				layer.msg('添加失败:'+data.msg,{time:2000});
			}
		}
 	});
}

function reload() {
	inintAssignTable(table, currentRow);
}

/**
 * 重新加载用户数据
 * @returns
 */
function loadUserData(){
	var userInfo = $('#userCodeName').val();
      //执行重载
      table.reload('userTableId', {
        page: {
        	curr: 1 //重新从第 1 页开始
        },
        where: {//后台定义对象接收
        	userCodeName: userInfo, 
        }
      });
}
	

