var table;
var str;
var tableData = [];
var cutLiaiMate;
var fieldsData = [];
layui.use([ 'table', 'laydate','form' ], function() {
	table = layui.table;
    var form = layui.form;
	var $ = layui.$;
	var laydate = layui.laydate;
	var liaiId = $("#liaiId").val();
	var liaiStatus = $("#liaiStatus").val();
	console.log('查看===主表id：' + liaiId);
	console.log('查看===主表状态：' + liaiStatus);

	// 返回
	$("#goBack").click(function() {
		//window.history.back(-1);
		tuoBack('.ContactSheetForBaoCaiCheck','#search');
	});

    //退回
    $("#goReturn").click(function(){
        console.log('查看退回');
        if (liaiStatus !== '已提交'){
            layer.alert('<span style="color:red;">只有状态为已提交才可以退回</sapn>');
            return;
        }
        var liaiIds = [];
        if (liaiId == null){
            return;
        }
        liaiIds.push(liaiId);
        var liaiIdsJson =JSON.stringify(liaiIds);
        $.ajax({
            type:"post",
            url:"/updateBaoCaiCutLiaiStatusByliaiIds?liaiIds=" + liaiIdsJson+"&types=3",
            dataType:"JSON",
            success:function(data2){
                if(data2){
                    layer.msg('退回成功', {time:2000});
                    initMateTable(tableData);
                }else{
                    layer.alert('<span style="color:red;">提交失败</sapn>');
                }
            },
            error:function(){
                layer.msg('程序出错', {time:2000 });
            }
        });
    });

	// 年月选择器
	laydate.render({
		elem : '#cutMonth',
		type : 'month'
	});
	
	$('td').removeAttr('data-edit');
	
	// 获取字段
	$.ajax({
		type : "post",
		url : "/queryBaoCaiLiaiMateFields?liaiId="+liaiId,
		dataType : "JSON",
		async : false,// 注意
		success : function(data) {
			str = data;
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
            var ermSuppList = data.ermSuppList;
            console.log(ermSuppList);
            if(ermSuppList != null){
                var str = '<option value ="" selected="selected">请选择</option>';
                $.each(ermSuppList,function(index,row){
                    str += '<option value = "' + row + '">' + row + '</option>'
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
	initMateTable(tableData);

    //重置
    $("#resetBtn").click(function(){
        $("#oemSuppName").val('');
        $("#mateName").val('');
        form.render('select');
        initMateTable(tableData);
        // $("#oemSuppName").trigger("change");
        // $("#oemSuppName  option[value='defaultVal'] ").attr("selected",true);
        // options.first().attr("selected", true);
        // $("#mateName").find("option[text='请选择']").attr("selected",true);
        // layui.form('select').render();
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
	debugger;
	table.render({
		elem : "#cutLiaiMateTable",
		data : tableData,
		page : true,
		width : '100%',
		minHeight : '20px',
		id : "cutLiaiMateTableId",
		cols : [ str ]
	})
	$('table td').removeAttr('data-edit');
}
