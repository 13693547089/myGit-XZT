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
	//条件搜索
	  var $ = layui.$, active = {
			    reload: function(){
			      var mateInfo = $('#mateInfo');
			      //执行重载
			      table.reload('mateTable', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {
			        	  mateInfo: mateInfo.val(),
			        }
			      });
			    }
		};
	  $(document).on("click",".xuanzhong1 table tbody tr",function() {
		  $('#userTable + div tr').removeClass('pitch1');
		  $(event.target).parents('tr').addClass('pitch1');
		});
	// 点击弹出添加框
	$('#addBtn').on('click', function() {
		layer.open({
			type : 2,
			title : '添加物料',
			shadeClose : false,
			shade : 0.1,
			content : '/getAllMateListHtml?userId='+currentRow,
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
			      dealMates(checkedData);
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
	// 删除选中的物料
	$('#delBtn').on('click',function() {
				var checkRows = table.checkStatus('mateTable');
				if (checkRows.data.length > 0) {
					var mateIds = new Array();
					for ( var int = 0; int < checkRows.data.length; int++) {
						mateIds.push(checkRows.data[int].mateId);
					}
					var mateIds = JSON.stringify(mateIds);
					layer.confirm('您确定要删除选中的物料吗？', function(index){
						 layer.close(index);
						$.ajax({
							type : 'post',
							url : '/deleteMaterialOfUser?userId=' + currentRow+"&mateIds="+mateIds,
							dataType : 'JSON',
							success : function(data) {
								layer.msg('物料删除成功',{time:2000});
								reload();
							},
							error : function(xhr) {
								layer.msg('error');
							}
						});
					});
				}else {
					layer.alert('<span style="color:red;">请选择要删除的物料</sapn>');
				}

	});
	
});

function inintAssignTable(table, userId) {
	table.render({
		elem : '#mateTable',
		url : '/queryMaterialOfUser?userId=' + userId,
		page : true,
		height:'auto',
		limit:15,
		limits:[15,30,50,100],
		cols : [ [ {
			checkbox : true
		}, {
			field : 'finMateId',
			title : '成品物料编码',
			align : 'center',
			width : 136
		}, {
			field : 'mateCode',
			title : '物料编码',
			align : 'center',
			width : 136
		}, {
			field : 'mateName',
			title : '物料名称',
			align : 'center',
			width : 231
		} , {
			field : 'mateGroupExpl',
			title : '物料组说明',
			align : 'center',
			width : 110
		} , {
			field : 'mateType',
			title : '物料类型编码',
			align : 'center',
			width : 109
		} , {
			field : 'basicUnit',
			title : '基本单位',
			align : 'center',
			width : 85
		} , {
			field : 'procUnit',
			title : '采购单位',
			align : 'center',
			width : 84
		} 
		] ],
		id : 'mateTable'
	});
}

function reload() {
	inintAssignTable(table, currentRow);
}

function dealMates(data){
	var mateIds =[];
	$.ajax({
			type:"post",
			url:"/queryAllMaterialListOfUser?userId="+currentRow,
			dataType:"JSON",
			async: false,
			success:function(mateData){
				$.each(data,function(mateindex,mateitem){
					var a = 0;
					$.each(mateData,function(mateDataIndex,mateDataItem){
						if(mateDataItem.mateCode == mateitem.mateCode){
							a++;
						}
					});
					if(a == 0){
						mateIds.push(mateitem.mateId);
					}
				});
			}
	});
	 if(mateIds.length!=0){
		 $.ajax({
				type:"post",
				url:"/addMaterialForUser?userId="+currentRow,
				data:"mateIds="+mateIds,
				dataType:"JSON",
				success:function(data){
					if(data.judge){
						layer.msg('物料添加成功',{time:2000});
						reload();
					}else{
						layer.alert(data.msg);
					}
				},
				error : function(xhr) {
					layer.msg('程序错误');
				}
		});
	 }else{
		 layer.msg('无法添加重复物料');
	 }
}



