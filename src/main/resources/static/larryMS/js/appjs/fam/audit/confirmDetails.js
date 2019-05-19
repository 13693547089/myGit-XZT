layui.use([ 'table' ], function() {
	var $ = layui.$;
	var table = layui.table;

	var type = $("#type").val();
	if (type == 1) {
		$(".displayBtn").css('display', 'none');
	}

	var approve = $("#approve").val();
	if (approve != undefined && approve != "") {
		var data = JSON.parse(approve);
		loadApproveTable(table, data);
	}

	var auditId = $("#id").val();
	table.render({
		elem : '#mateTable',
		url : '/finance/getMateData?auditId=' + auditId,
		page : true,
		cols : [ [ {
			type : 'numbers'
		}, {
			field : 'mateCode',
			title : '物料编码'
		}, {
			field : 'mateName',
			title : '物料名称'
		}, {
			field : 'lastMonthBala',
			title : '上月结存'
		}, {
			field : 'outWarehouse',
			title : '本月入库'
		}, {
			field : 'inWarehouse',
			title : '本月出库'
		}, {
			field : 'topLoss',
			title : '脱普损耗'
		}, {
			field : 'suppLoss',
			title : '供应商损耗'
		}, {
			field : 'noAdd',
			title : '累计未补'
		}, {
			field : 'addLoss',
			title : '本月补损'
		}, {
			field : 'monthStock',
			title : '月末库存'
		} ] ]
	});

	table.render({
		elem : '#mouldTable',
		url : '/finance/getMouldData?auditId=' + auditId,
		page : true,
		cols : [ [ {
			type : 'numbers'
		}, {
			field : 'mouldName',
			title : '模具名称'
		}, {
			field : 'mouldNum',
			title : '数量'
		}, {
			field : 'mouldStatus',
			title : '状态',
			templet : function(d) {
				if (d.mouldStatus == "1") {
					return "<span>废弃</span>";
				} else if (d.mouldStatus == "2") {
					return "<span>启用</span>";
				} else if (d.mouldStatus == "3") {
					return "<span>闲置</span>";
				}
			}
		}, {
			field : 'storePlace',
			title : '存放地方'
		}, {
			field : 'qualRate',
			title : '生产合格率'
		}, {
			field : 'belongRight',
			title : '归属权',
			templet : function(d) {
				if (d.belongRight == "T") {
					return "<span>脱普</span>";
				} else if (d.belongRight = "S") {
					return "<span>供应商</span>";
				}
			}
		}, {
			field : 'imgName',
			title : '模具图片',
			event : 'download',
			templet:function(d){
				debugger;
				if(d.imgUrl!=null && d.imgUrl!=undefined && d.imgUrl!=''){
					var ImgUrllist = JSON.parse(d.imgUrl)
					var Img=''
					for(var i =0;i<ImgUrllist.length;i++){
						var ImgUrl='ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121'+ImgUrllist[i].fileUrl+'/'+ImgUrllist[i].fileName;
						Img+="<img src="+ImgUrl+" style='width:50px;'></img>"
					}
					return Img;
				}
			}
		}, {
			field : 'remark',
			title : '备注'
		} ] ]
	});

	// 监听下载图片事件
	table.on('tool(mouldTable)', function(obj) {
		var currentData = obj.data;
//		var url = currentData.imgUrl;
//		if (url != undefined && url != "") {
//			var json = JSON.parse(url);
//			if (json.length > 0) {
//				var docId = json[0].docId;
//				window.location.href = "/doc/downLoadDoc?docId=" + docId
//			}
//		} else {
//			layer.msg("没有附件可下载！");
//		}
		if($(this).context.localName=='td'){
			 var ImgUrllist = JSON.parse(currentData.imgUrl);
				var Img=''
					for(var i =0;i<ImgUrllist.length;i++){
						var ImgUrl='ftp://srm:TOPsrm%402018@srmftp.top-china.cn:2121'+ImgUrllist[i].fileUrl+'/'+ImgUrllist[i].fileName;
						Img+="<div style='display:inline-block;'class='Div'><img src="+ImgUrl+" style='width:100px;height:100px;'></img><span style='display:none;'class='DocId'>"+ImgUrllist[i].docId+"</span><button type='button' class='Download layui-btn layui-btn-xs blueHollow' style='display:block;margin:0px auto;margin-top:5px;'>下载</button></div>"
					}
				$("#ExhiImg").children('.Div').detach();
				$('#ExhiImg').append(Img);
				$('#ExhiImg').show();
		}
		//下载照片
		$('.Div').on('click','.Download',function(){
			
			var docId = $(this).prev('.DocId').text();
			window.location.href = "/doc/downLoadDoc?docId=" + docId
		})

	});
	$('.Close').click(function(){
		$('#ExhiImg').hide();
		
	})
	//确认
	$("#confirmBtn").on('click', function() {
		var auditId = $("#id").val();
		var suppName = $("#suppName").val();
		//var createTime = $("#createTime").val();
		var remark = "财务稽核确认: "+suppName;
		var flag = taskProcess(auditId, "finaAudit", remark, "process");
		if(flag == "over_success"){
			$.ajax({
				url : '/finance/auditConfirm?auditIds=' + auditId + '&type=single',
				method : 'post',
				async : false,
				success : function(res) {
					layer.msg(res.msg);
					if (res.code == "0") {
						window.history.back(-1);
					}
				}
			});
		}
	});
	// 退回
	$("#rejectBtn").on('click', function() {
		layer.open({
			type : 2,
			title : '退回信息',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			shade : 0.1,
			area : [ '500px', '220px' ],
			content : '/finance/rejectInfo',
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				var data = $(layero).find("iframe")[0].contentWindow.getData();
				if (data != "") {
					// 退回操作
					var auditId = $("#id").val();
					var result = backProcess(auditId);
					if(result == "back_success"){
						var params = "?auditId=" + auditId + "&apprIdea=" + data
						var url = "/finance/auditReject" + params;
						$.ajax({
							url : url,
							method : 'post',
							async : false,
							success : function(res) {
								layer.msg(res.msg);
								if (res.code == "0") {
									layer.close(index);
									window.history.back(-1);
								}
							}
						
						});
					}
				} else {
					layer.msg("请输入退回意见！");
				}
			},
			btn2 : function(index, layero) {
				layer.close(index);
			}
		});
	});

	$("#backBtn").on('click', function() {
		window.history.back(-1);
	});
});

function loadApproveTable(table, data) {
	table.render({
		elem : '#approveTable',
		data : data,
		page : true,
		cols : [ [ {
			field : 'apprName',
			title : '审批人姓名'
		}, {
			field : 'apprStatus',
			title : '审批状态'
		}, {
			field : 'apprIdea',
			title : '审批意见'
		}, {
			field : 'apprTime',
			title : '审批时间',
			templet : function(d) {
				debugger;
				return formatTime(d.apprTime, 'yyyy-MM-dd');
			}
		} ] ]
	});
};
