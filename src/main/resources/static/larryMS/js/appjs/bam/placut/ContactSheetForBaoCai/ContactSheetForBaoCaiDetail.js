var table;
var str;
var tableData = [];
// var cutArr = [];
var cutLiaiMate;
var fieldsData = [];
var nowRowData;
var nowField;
var nextField;
var totalField;
var differenceField;
var origNum;
layui.use([ 'table', 'laydate','form' ], function() {
    var form = layui.form;
	table = layui.table;
	var $ = layui.$;
	var laydate = layui.laydate;
	var suppId = $("#suppId").val();

	// 返回
	$("#goBack").click(function() {
		//window.history.back(-1);
		tuoBack('.ContactSheetForBaoCaiDetail','#search');
	});

	// 年月选择器
	laydate.render({
		elem : '#cutMonth',
		type : 'month',
		done : function(value, date, endDate) {
			var oldPlanMonth = $("#cutMonth").val();
			if (oldPlanMonth != value) {
				getCutLiaiMate(value);
			}
		}
	});

	// 获取字段
	$.ajax({
		type : "post",
		url : "/queryBaoCaiFields",
		dataType : "JSON",
		async : false,// 注意
		success : function(data) {
			debugger;
			str = data;
			initCutLiaisonTable(tableData);
		},
		error : function() {
			layer.alert("Connection error");
		}
	});

	function getCutLiaiMate(cutMonth){
		debugger;
		tableData=[];
		// 获取物料数据
		$.ajax({
			type : "post",
			url : "/queryBaoCaiAllCutLiaiMate?suppId="+suppId+"&cutMonth="+cutMonth,
			dataType : "JSON",
			async : false,// 注意
			success : function(data) {
				debugger;
				if(data.judge){
					$.each(data.proList, function(index, item) {
						var arr = {
                            version : item.version,
                            mateCode : item.mateCode,
                            mateName : item.mateName,
                            oemSuppCode : item.sapCode,
                            oemSuppName : item.suppName,
                            isSpecial : item.isSpecial
                        };
						tableData.push(arr);
					});
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
				}else{
					layer.msg(data.msg);
				}
			},
			error : function() {
				layer.alert("Connection error");
			}
		})
		initCutLiaisonTable(tableData);
	}

	// table 数据刷新处理
	/*table.on('tool(cutLiaiMateTable)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'setSign'){
            var url ="/getBaoCaiContactSheetDetailHtml";
            location=url;
	    }
	});*/

	//保存        -----注意
	$("#SaveBut").click(function() {
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg);
			 return ;
		}
		 var length = tableData.length;
		 if(length == 0){
			 layer.msg("物料数据为空，不能保存");
			 return;
		 }
		var result = getResultData();
		debugger;
		var liaiId = guid();
		$("#liaiId").val(liaiId);
		var suppName = $("#suppName").val();
		//var format = new Date().Format("yyyy-MM-dd");
		$("#cutLiaiMateData").val(JSON.stringify(result));
		$("#fields").val(JSON.stringify(str));

        var formData = $("#cutLiaisonForm").serialize();
        $.ajax({
            type : "POST",
            url : "/addBaoCaiCutLiaison?type=1",
            data : formData,
            dataType:"JSON",
            async: false,
            error : function(request) {
                layer.alert("Connection error");
            },
            success : function(data) {
                if(data){
                    $("#Submit").attr("disabled","disabled");
                    $("#SaveBut").attr("disabled","disabled");
                    layer.msg("打切联络单保存成功");
                    // tuoBack('.ContactSheetForBaoCaiList','#search');
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
								|| e == 'version' || e == 'isSpecial' ) {
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

	//提交       -----注意
	$("#Submit").click(function() {
		var result = checkMust();
		 if(!result.flag){
			 layer.msg(result.msg);
			 return ;
		 }
		 var length = tableData.length;
		 if(length == 0){
			 layer.msg("物料数据为空，不能保存");
			 return;
		 }
		 var result = getResultData();
		var liaiId = guid();
		$("#liaiId").val(liaiId);
		var suppName = $("#suppName").val();
		$("#cutLiaiMateData").val(JSON.stringify(result));
		$("#fields").val(JSON.stringify(str));
		var formData = $("#cutLiaisonForm").serialize();

         $.ajax({
             type : "POST",
             url : "/addBaoCaiCutLiaison?type=2",
             data : formData,
             dataType:"JSON",
             async: false,
             error : function(request) {
                 layer.alert("Connection error");
             },
             success : function(data) {
                 if(data){
                     layer.msg("打切包材联络单提交成功");
                     // tuoBack('.ContactSheetForBaoCaiList','#search');
                     //window.history.back(-1);
                 }else{
                     layer.alert("打切联络单提交失败");
                 }
             }
         });
	});

    //重置
    $("#resetBtn").click(function(){
        $("#oemSuppName").val('');
        $("#mateName").val('');
        form.render('select');
        initCutLiaisonTable(tableData);
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
        initCutLiaisonTable(searchTbData);
	});

    form.on("select(mapgCode)",function(obj){
        var mapgCode = obj.value;
        console.log(mapgCode);
    });
});

/*function resetCutBaoCaieTable(cutArr) {
    table.render({
        elem : "#cutLiaiMateTable",
        data : cutArr,
        page : true,
        width : '100%',
        minHeight : '20px',
        id : "cutLiaiMateTableId",
        cols : [ str ]
    })
}*/

// 初始化table
function initCutLiaisonTable(tableData) {
	table.render({
		elem : "#cutLiaiMateTable",
		data : tableData,
		page : true,
		width : '100%',
		minHeight : '20px',
		id : "cutLiaiMateTableId",
		cols : [ str ]/*,
        done: function(res){
            cutArr = res.data;
		}*/
	});
}

