layui.use('form', function() {
	var form = layui.form;
	var $ = layui.$;
	var mateList=[];
	var segmCode='';
	form.on('select(segmCodeSelected)', function(data) {
		segmCode=data.value;
		$.get("/quote/segmCode/mateList",{segmCode:segmCode},function(res){
			var str='<option value="">请选择</option>';
			$.each(res,function(index,row){
				str+='<option value="'+row.mateCode+'">'+row.mateCode+row.skuEnglish+'</option>';
			});
			mateList=res;
			$("#mateCode").html(str);
			//重新初始化下拉菜单
			form.render('select');
		});
		var segmJson = $("#segmJson").val();
		var sg = JSON.parse(segmJson);
		for (var i = 0; i < sg.length; i++) {
			var elem = sg[i];
			if (elem.dicCode == data.value) {
				$("#segmName").val(elem.dicName);
				$("#asseCode").val(data.value + "1");
				return;
			}
		}
	});

	form.on('select(mateSelected)', function(data) {
		var mateJson = $("#mateJson").val();
		var mj = JSON.parse(mateJson);
		if(segmCode==60){		
			mj=mateList;
		}
		for (var i = 0; i < mj.length; i++) {
			var elem = mj[i];
			if (elem.mateCode == data.value) {
				$("#unit").val(elem.basicUnit);
				$("#asseName").val(elem.skuEnglish);
				return;
			}
		}
	});
	form.verify({
		segmCode : function(value) {
			if (value == '' || value == undefined)
				return '请输入段号！';
		},
		asseName : function(value) {
			if (value == '' || value == undefined)
				return '请输入组件名称！';
		},
		asseCode : function(value) {
			if (value == '' || value == undefined)
				return '请输入组件编号！';
		},
//		mateCode : function(value) {
//			var segmCode = $("#segmCode").val();
//			if (segmCode == '20' && (value == '' || value == undefined))
//				return '请输入物料编号！';
//		},
		detailsNum : function(value) {
			var segmCode=$("#segmCode").val();
			if(segmCode!=60){
				if (value == '' || value == undefined) {
					return '请输入用量！';
				} else if (isNaN(value)) {
					return '请输入数字';
				}
				}
		},
		unit : function(value) {
			if (value == '' || value == undefined)
				return '请输入单位！';
		}
	});

	// 取消
	$("#cancleBtn").on("click", function() {
		var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
		parent.layer.close(index);
	});

	// 监听提交
	form.on('submit(signForm)', function(data) {
		// 监听包材的物料编码必填
		

		var value = data.field;
		value.menuId = guid();
		parent.reload(value);
		var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
		parent.layer.close(index);
	});
});

// 生成GUID
function guid() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}
