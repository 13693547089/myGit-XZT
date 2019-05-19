var tableData = [];
var table;
var receData;
var deliCode2;
var receId;
var funType2;
var oldDeliCode;

layui.use([ 'form', 'table' ], function() {
	var form = layui.form;
	table = layui.table;
	layer = layui.layer;
	var $ = layui.$;
	funType2 = $("#funType").val();
	deliCode2 = $("#deliCode").val();
	oldDeliCode = $("#deliCode").val();
	receId = $("#receId").val();
	var suppr = $("#suppRange").val();
	var supprd = $("#suppRangeDesc").val();
	if(suppr != null && suppr != ''){
		$("#suppRange").val(suppr +' - '+supprd);
	}else{
		$("#suppRange").val('');
	}
	loadReceMate();
	function loadReceMate(){
		$.ajax({
			type : "post",
			url : "/queryReceMatesByReceId?receId=" + receId,
			dataType : "JSON",
			success : function(data) {
				if (data != null) {
					debugger;
					tableData = data;
					initTable(table, tableData);
				}
			},
			error : function() {
				layer.alert("Connection error");
			}
		});
	}
	// 打印
	$("#printBut").click(function() {
		var url = "/getRecePrintHtml?receId=" + receId;
		location = url;
	});
	// 返回
	$("#goBack").click(function() {
		// window.history.back(-1);
		tuoBack('.receiveEdit', '#serachRece');
	});
	// 查看时不能编辑
	if (funType2 == '2') {
		$(".disa").attr("readonly", "readonly");
	}
	
	
	table.on('tool(receMateTableEvent)', function(obj) { 
		var data = obj.data;
		if (obj.event == "edit") {
			var id = data.id;
			console.info(data);
			layer.open({
				  type : 2,
				  title : '编辑收货单物料信息',
				  shadeClose : false,
				  shade : 0.1,
				  content : '/getEditReceMateMessHtml?id='+id,
				  area : [ '600px', '70%' ],
				  maxmin : true, // 开启最大化最小化按钮
				  btn: ['确认', '取消']
			  ,yes: function(index, layero){
				  //按钮【按钮一】的回调
				  // 获取选中的物料数据
				  var map = $(layero).find("iframe")[0].contentWindow
				  .getVersion();
				  if(map.isOccupy == undefined || map.isOccupy == ""){
					  layer.msg("请填写选择是否占用！",{time:2000});
					  return;
				  }
				  if(map.judge){
					  // 关闭弹框
					  layer.close(index);
					  // 处理数据
					  debugger;
					  updateReceMateMess(map);
				  }else{
					  layer.msg(map.msg);
				  }
			  },
			  btn2: function(index, layero){
				  //按钮【按钮二】的回调
				  // 默认会关闭弹框
				  //return false 开启该代码可禁止点击该按钮关闭
			  }
			  });
			
		}
	});
	//修改收货单物料信息
	function updateReceMateMess(map){
		console.info(map);
		$.ajax({
	  		 type:"post",
	  		 url:"/updateReceMateInboDeliCodeAndIsOccupy",
	  		 data:{
	  			 id:map.id,
	  			 inboDeliCode:map.inboDeliCode,
	  			 isOccypy:map.isOccupy
	  		 },
	  		 dataType:"JSON",
	  		 success:function(data2){
	  			 if(data2){
	  				 layer.msg('修改成功', {time:2000 });
	  				 loadReceMate();
	  			 }else{
	  				 layer.alert('<span style="color:red;">修改失败</sapn>');
	  			 }
	  		 }
	  	  });
	}
	
	
	//修改送货单状态
	 $("#editStatus").click(function(){
		 var receCode = $("#receCode").val();
		 var status = $("#status").val();
   	     appoData = {
				  appoCodeDesc:'送货单号',
				  appoStatusDesc : '送货单状态',
				  a : this.itemActive
		  };
		  layer.open({
			  type : 2,
			  title : '编辑收货单状态',
			  shadeClose : false,
			  shade : 0.1,
			  content : '/getEditStatusHtml?code='+receCode+'&status='+status+'&type=receive',
			  area : [ '600px', '70%' ],
			  maxmin : true, // 开启最大化最小化按钮
			  btn: ['确认', '取消']
		  ,yes: function(index, layero){
			  //按钮【按钮一】的回调
			  // 获取选中的物料数据
			  var map = $(layero).find("iframe")[0].contentWindow
			  .getVersion();
			  if(map.code == undefined || map.code == ""){
				  layer.msg("请填写状态！",{time:2000});
				  return;
			  }
			  if(map.judge){
				  // 关闭弹框
				  layer.close(index);
				  // 处理数据
				  debugger;
				  updateStatus(map);
			  }else{
				  layer.msg(map.msg);
			  }
		  },
		  btn2: function(index, layero){
			  //按钮【按钮二】的回调
			  // 默认会关闭弹框
			  //return false 开启该代码可禁止点击该按钮关闭
		  }
		  });
	 });
	//修改状态
	  function updateStatus(map){
	  	 $.ajax({
	  		 type:"post",
	  		 url:"/updateStatus",
	  		 data:{
	  			 status:map.status,
	  			 code:map.code,
	  			 type:'receive'
	  		 },
	  		 dataType:"JSON",
	  		 success:function(data2){
	  			 if(data2){
	  				 layer.msg('修改成功', {time:2000 });
	  				 $("#status").val(map.status);
	  			 }else{
	  				 layer.alert('<span style="color:red;">修改失败</sapn>');
	  			 }
	  		 }
	  	  });
	  }

});

// 初始化预约申请物资表格
function initTable(table, data) {
	receData = table.render({
		elem : "#receMateTable",
		data : data,
		id : "receMateTableId",
		limit : 100,
		cols : [ [ {
			title : "项次",
			field : "frequency",
			align : 'center',
			width : 60
		}, {
			title : "内向交货单号",
			field : "inboDeliCode",
			align : 'center',
			width : 100
		},{
			field : "orderId",
			title : "采购订单号",
			align : 'center',
			width : 100
		}, {
			field : "mateCode",
			title : "物料编码",
			align : 'center'
		}, {
			field : "mateName",
			title : "物料名称",
			align : 'center'
		}, {
			field : "deliNumber",
			title : "送货数量",
			align : 'center',
			width : 120
		}, {
			field : "receNumber",
			title : "实收数量",
			align : 'center',
			edit : 'text',
			width : 120
		}, {
			field : "unit",
			title : "单位",
			align : 'center',
			width : 60
		}, {
			field : "prodPatchNum",
			title : "产品批号",
			align : 'center',
			width : 120
		}, {
			field : "factoryAddr",
			title : "工厂",
			align : 'center',
			width : 80
		}, {
			field : "storLocation",
			title : "库位",
			align : 'center',
			event : "setStorLocation",
			width : 80
		}, {
			field : "remark",
			title : "备注",
			align : 'center',
			edit : 'text'
		}, {
			field : "isOccypy",
			title : "是否占用",
			align : 'center',
			edit : 'text',
			templet:function(d){
				var isOccypy = d.isOccypy;
				if('yes'==isOccypy){
					return '是';
				}else{
					return '否';
				}
			}
		},{
			fixed: 'right', 
			title:'操作',
			width:80, 
			align:'center', 
			toolbar: '#recebarDemo'}
		] ]

	});
	if (funType2 == '2') {
		$('table td').removeAttr('data-edit');
		$('table td').removeAttr('lay-event');
	}
}

Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].id == val.id) {
			this.splice(k, 1);
			return;
		}
	}
};
