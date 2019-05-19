layui.use([ 'table' ], function() {
	var table = layui.table;
	var $ = layui.$;
	// 条件搜索
	var $ = layui.$, active = {
		reload : function() {
			var mateInfo = $('#mateInfo');
			var mateGroupInfo = $("#mateGroupInfo");
			var mateTypeInfo = $("#mateTypeInfo");
			// 执行重载
			table.reload('mateTableId', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					mateInfo : mateInfo.val(),
					mateGroupInfo : mateGroupInfo.val(),
					mateTypeInfo : mateTypeInfo.val()
				}
			});
		}
	};
	// 监听单元格事件
	table.on('tool(demo)', function(obj) {
		var data = obj.data;
		var mateId = data.mateId;
		if (obj.event === 'check') {// 查看
			var url = "/getMaterialLookHtml?mateId=" + data.mateId;
			//location = url;
			tuoGo(url,'materialLook','mateTableId');
		} else if (obj.event === 'basicInfo') {
			// 基础信息
			layer.open({
				type : 2,
				title : '产品规格',
				maxmin : true,
				shadeClose : false, // 点击遮罩关闭层
				shade : 0.1,
				area : [ '800px', '520px' ],
				content : '/mate/basicInfo?mateCode=' + data.mateCode,
				btn : [ '确定', '取消' ],
				yes : function(index, layero) {
					var basicData = $(layero).find("iframe")[0].contentWindow
							.getBasicData();
					if (basicData.length > 0) {
						$.ajax({
							url : '/mate/saveBasicInfo',
							method : 'post',
							data : {
								mateId : mateId,
								basicInfo : JSON.stringify(basicData)
							},
							async : false,
							error : function(res) {

							},
							success : function(res) {
								layer.msg(res.msg);
								if (res.code == 0) {
									table.reload('mateTableId');
									layer.close(index);
								}
							}
						});
					}
				},
				but2 : function(index, layero) {
					layer.close(index);
				}
			});
		}
	});

	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});

	$("#asyncSupp").on('click', function() {
		// 同步物料数据
		var asyncMate = $.ajax({
			url : '/asyncMateInfo',
			method : 'get',
			async : true,
			success : function(res) {
				layer.confirm(res.msg);
			},
			complete : function(XMLHttpRequest, status) {
				layer.msg("超时");
			}
		});
	});
});
