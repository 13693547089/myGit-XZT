var currentRow = "";
var prefix = "/bam/cf";
var table;
var tableData;

// 行 用户信息
var rowUserCode='';
var rowUserName='';
var rowUserId='';

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
		url : prefix+'/getPsUserData',
		page : true,
		limit: 100, //显示的数量
		limits: [100,200,1000], //显示的数量
		//even: true,//隔行变色
		height:600,
		cols : [ [ {
			field : 'userName',
			title : '编号',
			align : 'center',
			width : 120
		}, {
			field : 'name',
			title : '姓名',
			align : 'center',
			width : 170
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
								currentRow = data[item].userName;
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
	$("#userInfo").keydown(function(e) {  
        if (e.keyCode == 13) {
        	  var userInfo = $('#userInfo').val();
		      //执行重载
		      table.reload('userTableId', {
		        page: {
		        	curr: 1 //重新从第 1 页开始
		        },
		        where: {//后台定义对象接收
		        	params: userInfo, 
		        }
		      });
        }  
	});
	
	// 点击弹出添加框
	$('#addBtn').on('click', function() {
		
		if(selIndex == -1){
			layer.msg('请选择用户行！',{time:2000});
			return;
		}
		
		rowUserCode = userTbData[selIndex].userName;
		rowUserName = userTbData[selIndex].name;
		rowUserId = userTbData[selIndex].id;
		
		layer.open({
			type : 2,
			title : '添加物料系列',
			shadeClose : false,// 这个属性为true时点击遮罩会关闭弹窗，false则只有点击关闭按钮才能关闭弹窗
			shade : 0.1,//默认是有遮罩
			content : prefix+'/materialSeriesSelDialog',
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
					  dealSelSeriesData(checkedData);
				  }
		     },
		     btn2: function(index, layero){
			      //按钮【按钮二】的回调
			      // 默认会关闭弹框
			      //return false 开启该代码可禁止点击该按钮关闭
		     }
		});
	});
	
	$(document).on("click",".xuanzhong table tbody tr",function() {
		$('#userTable + div tr').removeClass('pitch');
		$(event.target).parents('tr').addClass('pitch');
		
		// 获取选择行的索引
		var row = $(event.target).parents('tr');
		selIndex=row[0].rowIndex;
	});
	// 删除选中的供应商
	$('#delBtn').on('click',function() {
			var checkRows = table.checkStatus('userSeriesTableId');
			var checkData = checkRows.data;
			if (checkData.length > 0) {
				var ids = new Array();
				// 关联用户编码
				var userCode = checkData[0].userCode;
				for ( var i = 0; i < checkData.length; i++) {
					ids.push(checkData[i].id);
				}
				layer.confirm('您确定要删除选中的系列吗？', function(index){
					layer.close(index);
					$.ajax({
						type : 'post',
						url : prefix+'/delUserSeriesData',
						data:{"ids":ids.join(',')},
						dataType : 'JSON',
						success : function(data) {
							if(data == 1){
								layer.msg('删除成功！',{time:2000});
								inintAssignTable(table, userCode);
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
	
});

function inintAssignTable(table, userCode) {
	$.ajax({
		type : 'post',
		url : prefix+'/getUserSeriesData',
		data:{"userCode":userCode},
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
		limit:15,
		limits:[15,30,50,100],
		cols : [ [ {
			checkbox : true
		}, {
			field : 'seriesCode',
			title : '系列编码',
			align : 'center',
			width : 139
		}, {
			field : 'seriesExpl',
			title : '系列名称',
			align : 'center',
			width : 291
		}
		] ],
		id : 'userSeriesTableId'
	});
}

/**
 * 处理选择的系列数据
 */
function dealSelSeriesData(data){
	// 转成json
	var selSeriesData = JSON.stringify(data);
	$.ajax({
		type:"post",
		url:prefix+"/saveSelMaterialSeries",
		data:{"userCode":rowUserCode,"userName":rowUserName,"userId":rowUserId,"selSeriesData":selSeriesData},
		dataType:"JSON",
		async: false,
		success:function(data){
			if(data==1){
				layer.msg('添加成功！',{time:2000});
				// 重新加载数据
				inintAssignTable(table, rowUserCode);
			}else{
				layer.msg('添加失败！',{time:2000});
			}
		}
 	});
}

function reload() {
	inintAssignTable(table, currentRow);
}

	

