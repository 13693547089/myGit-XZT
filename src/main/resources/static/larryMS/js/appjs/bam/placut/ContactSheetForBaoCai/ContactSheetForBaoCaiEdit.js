var table;
var str;
var tableData = [];
var cutLiaiMate;
var fieldsData = [];
var nowRowData
var nowField;
var nextField;
var totalField;
var differenceField;
layui.use([ 'table', 'laydate','form' ], function() {
	table = layui.table;
    var form = layui.form;
	var $ = layui.$;
	var laydate = layui.laydate;
	liaiId = $("#liaiId").val();

	// 返回
	$("#goBack").click(function() {
		//window.history.back(-1);
		tuoBack('.ContactSheetForBaoCaiEdit','#search');
	});

	// 获取字段
	$.ajax({
		type : "post",
		url : "/queryBaoCaiLiaiMateFields?liaiId="+liaiId,
		dataType : "JSON",
		async : false,// 注意
		success : function(data) {
			str = data;
			initMateTable(tableData);
		},
		error : function() {
			layer.alert("Connection error");
		}
	});

	//获取物料数据
    $.ajax({
        type : "post",
        url : "/getBaoCaiData?id="+liaiId,
        dataType : "JSON",
        async : false,// 注意
        success : function(data) {
            tableData = data.data;
            initMateTable(tableData);
            var ermSuppList = data.ermSuppList;
            if(ermSuppList != null){
                var str = '<option value ="" selected="selected">请选择</option>';
                $.each(ermSuppList,function(index,row){
                    str+='<option value = "' + row + '">' + row + '</option>'
                });
            }
            $("#oemSuppName").html(str);
            //重新初始化下拉框
            form.render('select');
        },
        error : function() {
            layer.alert("Connection error");
        }
    });
	
	// table 数据刷新处理
	table.on('tool(cutLiaiMateTable)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'setSign'){
	    	var upd={};
	    	//prodNum 作为upd对象的属性
	    	upd.prodNum=data.prodNum;
	    	//var nextField = B302 ，nextField的所代表的值B302 作为upd对象的属性，
	    	//data[nextField] ：表示获取data对象B302属性的值
	    	upd[nextField]=data[nextField];
	    	upd[totalField]=data[totalField];
	    	upd[differenceField]= data[differenceField];
	    	$.each(str,function(index,item){
	    		// debugger;
	    		for(var e in item){
	    			if(e == 'field'){
	    				var v = item[e];//获取合计字段名
	    				if(v.indexOf("C") == 0){
	    					var diffField = "D"+v.substring(1);
	    					upd[diffField] = data[diffField];
	    				}
	    			}
	    		}
	    	});
	    	obj.update(upd);
	    	nextField="";
	    	totalField="";
	    	differenceField="";
	    }
	});

	//编辑保存        -----注意
	$("#SaveBut").click(function() {
		var result = getResultData();
		$("#cutLiaiMateData").val(JSON.stringify(result));
		$("#fields").val(JSON.stringify(str)); 
		var formData = $("#cutLiaisonForm").serialize();
		 $.ajax({
			 type : "POST",
			 url : "/udpateBaoCaiCutLiaiMate?type=1",
			 data : formData,
			 dataType:"JSON",
			 async: false,
			 error : function(request) {
				 layer.alert("Connection error");
			 },
			 success : function(data) {
				 if(data){
					 layer.msg("打切联络单保存成功");
					 //window.history.back(-1);
					 tuoBack('.ContactSheetForBaoCaiEdit','#search');
				 }else{
					 layer.alert("打切联络单保存失败");
				 }
			 }
		 });

	});
	function getResultData(){
		var result = [];
		for (var i = 0; i < tableData.length; i++) {
			debugger;
			var elem = tableData[i];
			var r = {};
			var ss = [];
			for ( var e in elem) {
				if (e == 'mateCode' || e == 'mateName'
                    || e == 'oemSuppName'|| e == 'oemSuppCode'
                    || e == 'version' || e == 'isSpecial' || e == 'id') {
					r[e] = elem[e];
				}else if(e == 'LAY_TABLE_INDEX'){
					
				}else{
					var q = {};
					q[e] = elem[e];
					ss.push(q);
				}
			}
			r.fields = JSON.stringify(ss);
			result.push(r);
		}
		return result;
	}
	function find(str,cha,num){
	    var x=str.indexOf(cha);
	    for(var i=0;i<num;i++){
	        x=str.indexOf(cha,x+1);
	    }
	    return x;
	}

	//编辑提交       -----注意
	$("#Submit").click(function() {
		var result = getResultData();
		$("#cutLiaiMateData").val(JSON.stringify(result));
		$("#fields").val(JSON.stringify(str));
		var formData = $("#cutLiaisonForm").serialize();
			$.ajax({
				type : "POST",
				url : "/udpateBaoCaiCutLiaiMate?type=2",
				data : formData,
				dataType:"JSON",
				async: false,
				error : function(request) {
					layer.alert("Connection error");
				},
				success : function(data) {
					if(data.judge){
						layer.msg("打切联络单提交成功");
						//window.history.back(-1);
					}else{
						layer.alert(data.msg);
					}
				}
			});
	});

	window.returnFunction = function() {
		debugger;
		var liaiIds = [];
		var liaiId = $("#liaiId").val();
		liaiIds.push(liaiId);
		var liaiIdsJson =JSON.stringify(liaiIds);
		$.ajax({
			  type:"post",
			  url:"/updateBaoCaiCutLiaiStatusByliaiIds?liaiIds="+encodeURIComponent(liaiIdsJson),
			  dataType:"JSON",
			  success:function(data2){
				  if(data2){
					  debugger;
					  layer.msg('提交成功', {time:2000 });
					  //window.history.back(-1);
					  tuoBack('.ContactSheetForBaoCaiEdit','#search');
				  }else{
					  layer.alert('<span style="color:red;">提交失败</sapn>');
				  }
			  },
			  error:function(){
				  layer.msg('程序出错', {time:2000 });
			  }
		 });
	};

    //重置
    $("#resetBtn").click(function(){
        $("#oemSuppName").val('');
        $("#mateName").val('');
        form.render('select');
        initMateTable(tableData);
    });

    //筛选
    $("#screenBtn").click(function(){
        var oemSuppName = $("#oemSuppName").val();
        var mateName = $("#mateName").val();

        var searchTbData = new Array();
        for(var i=0;i<tableData.length;i++){
            var item = tableData[i];
            var oemSuppFlag = false;
            var mateFlag = false;

            if(oemSuppName != ''){
                if(item.oemSuppCode != undefined
                    && item.oemSuppName != undefined
                    && (item.oemSuppCode.toUpperCase().indexOf(oemSuppName.toUpperCase())!= -1 || item.oemSuppName.toUpperCase().indexOf(oemSuppName.toUpperCase())!= -1)){
                    oemSuppFlag = true;
                }
            }else{
                oemSuppFlag = true;
            }
            if(mateName != ''){
                if(item.mateCode != undefined
                    && item.mateName != undefined
                    && (item.mateCode.toUpperCase().indexOf(mateName.toUpperCase())!= -1 || item.mateName.toUpperCase().indexOf(mateName.toUpperCase())!= -1)){
                    mateFlag = true;
                }
            }else{
                mateFlag = true;
            }
            if(oemSuppFlag && mateFlag){
                searchTbData.push(item);
            }
        }
        // 重新加载数据
        initMateTable(searchTbData);
    });
});

function initMateTable(tableData){
	table.render({
		elem : "#cutLiaiMateTable",
		data : tableData,
		page : true,
		width : '100%',
		minHeight : '20px',
		id : "cutLiaiMateTableId",
		cols : [ str ]
	})
}

Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].id == val.id) {
			this.splice(k, 1);
			return;
		}
	}
};