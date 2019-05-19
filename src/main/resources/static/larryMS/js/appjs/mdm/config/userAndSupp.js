var currentRow = "";
var table;
layui.use('table', function() {
	table = layui.table;
	var $ = layui.$;
	// 执行一个 table 实例
	var userTable = table.render({
		elem : '#userTable',
		url : '/queryAllUser',
		page : true,
		limit: 100, //显示的数量
		limits: [100,200,1000], //显示的数量
		//even: true,//隔行变色
		height:465,
		cols : [ [ {
			field : 'userName',
			title : '员工编号',
			align : 'center',
			width : 120
		}, {
			field : 'name',
			title : '采购员姓名',
			align : 'center',
			width : 170
		} ] ],
		id : 'userTableId',
		done : function(res, curr, count) {
			var data = res.data;
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
								// layer.msg(JSON.stringify(data[item]), {
								// time : 10000
								// });
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
        		//alert(1221)
        		debugger;
        	  var userInfo = $('#userInfo');
		      //执行重载
		      table.reload('userTableId', {
		        page: {
		          curr: 1 //重新从第 1 页开始
		        }
		        ,where: {//后台定义对象接收
		        	  params: userInfo.val(),
		        }
		      });
        }  
	});
	
	 //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	var $ = layui.$,active = {
			    reload: function(){
			      var suppInfo = $('#suppInfo');
			      var category = $("#category");
			      //执行重载
			      table.reload('qualSuppTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	 suppInfo: suppInfo.val(),
			        	 category:category.val(),
			        }
			      });
			    }
			  
		};
	
	// 点击弹出添加框
	$('#addBtn').on('click', function() {
		layer.open({
			type : 2,
			title : '添加供应商',
			shadeClose : false,// 这个属性为true时点击遮罩会关闭弹窗，false则只有点击关闭按钮才能关闭弹窗
			shade : 0.1,//默认是有遮罩
			content : '/getAllQualSuppListHtml?userId='+currentRow,
			area : [ '1100px', '95%' ],
			maxmin : true, // 开启最大化最小化按钮
			btn: ['确认', '取消']
		       ,yes: function(index, layero){
		      //按钮【按钮一】的回调
		      // 获取选中的物料数据
		      var checkedData = $(layero).find("iframe")[0].contentWindow
		          .getCheckedData();
		      // 关闭弹框
		      layer.close(index);
		      // 处理数据
		      deliSupps(checkedData);
		     },
		     btn2: function(index, layero){
		      //按钮【按钮二】的回调
		      // 默认会关闭弹框
		      //return false 开启该代码可禁止点击该按钮关闭
		     }
		});
	});

	$('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	
	 $(document).on("click",".xuanzhong table tbody tr",function() {
		  $('#userTable + div tr').removeClass('pitch');
		  $(event.target).parents('tr').addClass('pitch');
		});
	// 删除选中的供应商
	$('#delBtn').on('click',function() {
				var checkRows = table.checkStatus('qualSuppTableId');
				if (checkRows.data.length > 0) {
					var suppIds = new Array();
					for ( var int = 0; int < checkRows.data.length; int++) {
						suppIds.push(checkRows.data[int].suppId);
					}
					layer.confirm('您确定要删除选中的供应商吗？', function(index){
						 layer.close(index);
						$.ajax({
							type : 'post',
							url : '/deleteQualSuppOfUserByUserId?userId=' + currentRow,
							data:"suppIds="+suppIds,
							dataType : 'JSON',
							success : function(data) {
								layer.msg('供应商删除成功',{time:2000});
								reload();
							},
							error : function(xhr) {
								layer.msg('程序错误');
							}
						});
					});
				}else {
					layer.alert('<span style="color:red;">请选择要删除的供应商</sapn>');
				}

	 });
	
});

function inintAssignTable(table, userId) {
	table.render({
		elem : '#qualSuppTable',
		url : '/queryQualSuppOfUser?userId=' + userId,
		page : true,
		height:'auto',
		limit:15,
		limits:[15,30,50,100],
		cols : [ [ {
			checkbox : true
		}, {
			field : 'sapId',
			title : 'SAP编码',
			align : 'center',
			width : 139
		}, {
			field : 'category',
			title : '供应商分类',
			align : 'center',
			width : 158
		} , {
			field : 'suppName',
			title : '供应商名称',
			align : 'center',
			width : 291
		} , {
			field : 'suppAbbre',
			title : '供应商简称',
			align : 'center',
			width : 105
		}
		] ],
		id : 'qualSuppTableId'
	});
}

function  deliSupps(data){
	var suppIds =[];
	$.ajax({
 			type:"post",
 			url:"/queryAllQualSuppListOfUser?userId="+currentRow,
 			dataType:"JSON",
 			async: false,
 			success:function(suppList){
 				$.each(data,function(suppindex,suppitem){
 					var a = 0;
 					$.each(suppList,function(userindex,useritem){
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
 				url:"/addQualSuppForUser?userId="+currentRow,
 				data:"suppIds="+suppIds,
 				dataType:"JSON",
 				success:function(data){
 					if(data){
 						layer.msg('供应商添加成功',{time:2000});
 						reload();
 					}
 				},
 				error : function(xhr) {
 					layer.msg('程序错误');
 				}
 		});
 	 }else{
 		layer.msg('无法添加重复供应商');
 	 }
 	
}

function reload() {
	inintAssignTable(table, currentRow);
}

	

