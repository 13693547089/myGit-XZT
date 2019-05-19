var prefix = '/sys/assign';

var roleData;
layui.use('table', function() {
	var $ = layui.$;
	table = layui.table;
	var orgId = $('#orgId').val();
	$.ajax({
		type : "GET",
		url : prefix + "/selected?orgId=" + orgId,
		success : function(data) {
			roleData = data;
			table.render({
				elem : '#roleTable',
				data : roleData,
				page : true,
				cols : [ [ {
					checkbox : true
				}, {
					field : 'id',
					title : '角色ID',
					width : 100
				}, {
					field : 'roleName',
					title : '角色名称',
					width : 120
				}, {
					field : 'remark',
					title : '备注',
					width : 170
				} ] ]
			});
		}
	});

	$("#subBtn").on('click', function() {
		var roleIds = "";
		var orgId = $('#orgId').val();
		for ( var i = 0; i < roleData.length; i++) {
			if (roleData[i].LAY_CHECKED === true) {
				roleIds += roleData[i].id + ",";
			}
		}
		$.ajax({
			cache : true,
			type : "POST",
			url : prefix + "/updateByOrgId",
			data : {
				orgId : orgId,
				roleIds : roleIds
			},
			async : false,
			error : function(request) {
				alert("Connection error");
			},
			success : function(r) {
				var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
				if (r.code == 0) {
					parent.layer.msg(r.msg);
					parent.reload();
					parent.layer.close(index);
				} else {
					parent.layer.close(index);
					parent.layer.msg(r.msg);
				}

			}
		});
	});
});