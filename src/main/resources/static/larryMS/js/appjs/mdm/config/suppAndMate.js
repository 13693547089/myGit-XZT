var currentRow = "";
var table;
layui.use('table', function() {
	table = layui.table;
	var $ =layui.$;
	// 执行一个 table 实例
	var userTable = table.render({
		elem : '#suppTable',
		url : '/queryAllQualSuppOfUser',
		page : true,
		limit: 100, //显示的数量
		limits: [100,200,1000], //显示的数量
		//even: true,//隔行变色
		height:465,
		cols : [ [ {
			field : 'suppName',
			title : '供应商名称',
			align : 'center',
			width : 126
		}, {
			field : 'suppId',
			title : '供应商编码',
			align : 'center',
			width : 95
		} , {
			field : 'username',
			title : '采购员',
			align : 'center',
			width : 70
		} ] ],
		id : 'suppTable',
		done : function(res, curr, count) {
			var data = res.data;
			if(data != ""){
				//debugger;
				currentRow = data[0].suppId;
				inintAssignTable(table, currentRow);
				$('.layui-table-body tr').each(function() {
					var dataindex = $(this).attr('data-index');
					var idx = 0;
					for ( var item in data) {
						if (dataindex == idx) {
							$(this).click(function() {
								currentRow = data[item].suppId;
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
	//搜索供应商
	$("#suppInfo").keydown(function(e) {  
        if (e.keyCode == 13) { 
        		var suppInfo = $('#suppInfo');
			      //执行重载
			      table.reload('suppTable', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {
			        	 suppInfo: suppInfo.val(),
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
	  $(document).on("click",".xuanzhong2 table tbody tr",function() {
		  $('#suppTable + div tr').removeClass('pitch2');
		  $(event.target).parents('tr').addClass('pitch2');
		});
	  $('.demoTable .layui-btn').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
		});
	// 点击弹出添加框
	$('#addBtn').on('click', function() {
		layer.open({
			type : 2,
			title : '添加物料',
			shadeClose : false,
			shade : 0.1,
			content : '/getAllMateListOfUserHtml?suppId='+currentRow,
			area : [ '1100px', '95%' ],
			maxmin : true ,// 开启最大化最小化按钮
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

	// 删除选中的物料
	$('#delBtn').on('click',function() {
		var checkRows = table.checkStatus('mateTable');
				if (checkRows.data.length > 0) {
					var mateIds = new Array();
					debugger;
					for ( var int = 0; int < checkRows.data.length; int++) {
						mateIds.push(checkRows.data[int].mateId);
					}
					var mateIds = JSON.stringify(mateIds);
					layer.confirm('您确定要删除选中的物料吗？', function(index){
						 layer.close(index);
						$.ajax({
							type : 'post',
							url : '/deleteMaterialOfUserAndSupp?suppId=' + currentRow+"&mateIds="+mateIds,
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

function inintAssignTable(table, suppId) {
	table.render({
		elem : '#mateTable',
		url : '/queryMaterialOfUserAndSupp?suppId=' + suppId,
		page : false,
		height:465,
		cols : [ [ {
			checkbox : true
		}, {
			field : 'finMateId',
			title : '成品物料编码',
			align : 'center',
			width : 135
		}, {
			field : 'mateCode',
			title : '物料编码',
			align : 'center',
			width : 135
		}, {
			field : 'mateName',
			title : '物料名称',
			align : 'center',
			width : 240
		} , {
			field : 'mateGroupExpl',
			title : '物料组说明',
			align : 'center',
			width : 116
		} , {
			field : 'mateType',
			title : '物料类型编码',
			align : 'center',
			width : 108
		} , {
			field : 'basicUnit',
			title : '基本单位',
			align : 'center',
			width : 85
		} , {
			field : 'procUnit',
			title : '采购单位',
			align : 'center',
			width : 85
		} 
		] ],
		id : 'mateTable'
	});
}

function dealMates(data){
	if(currentRow == "" || currentRow == undefined){
		layer.msg("添加失败，请选择供应商！");
		return;
	}
	var mateIds =[];
	 $.ajax({
			type:"post",
			url:"/queryMaterialOfUserAndSupp?suppId="+currentRow,
			dataType:"JSON",
			async: false,
			success:function(mateData){
				$.each(data,function(mateindex,mateitem){
					var a = 0;
					$.each(mateData.data,function(mateDataIndex,mateDataItem){
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
				url:"/addMaterialForUserAndSupp?suppId="+currentRow,
				data:"mateIds="+mateIds,
				dataType:"JSON",
				success:function(data){
					if(data){
						layer.msg('物料添加成功',{time:2000});
						reload();
					}else{
						layer.msg('物料添加失败',{time:2000});
					}
				},
				error : function(xhr) {
					layer.msg('程序错误');
				}
		});
	 }else{
		 layer.msg('无法重复添加物料');
	 }
}
function reload() {
	inintAssignTable(table, currentRow);
}

	

