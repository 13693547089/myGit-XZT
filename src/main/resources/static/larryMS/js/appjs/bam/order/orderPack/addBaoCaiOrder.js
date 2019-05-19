var table;
var parentData;
var deleteFileId = '';
var deleteFileName = '';
var oldID = '';
layui.use(['form','table', 'laydate','upload'], function(){
    var form = layui.form;
    table = layui.table;
    var upload = layui.upload;
    var laydate = layui.laydate;
    var $ = layui.$;

    laydate.render({
        elem: '#deliDate', //指定元素
    });
    laydate.render({
        elem: '#orderDate', //指定元素
    });

    $('.demoTable .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    var type = $("#type").val();
    if (type == 'edit'){
        initPackOrder();
        layui.form.render();
    }

    //料号下拉框触发（半成品）
    form.on("select(mateCode)",function(obj){
        var index = obj.elem.selectedIndex; // 选中索引
        var mateText = obj.elem.options[index].text; // 选中文本
        var text = mateText.split(" ");
        var prodName = "";
        if (text.length > 1){
            prodName = text[1];
        }
        $('#prodName').val(prodName);
        console.log(prodName);

        var mateCode = obj.value;
        if(mateCode == '' || mateCode == null || mateCode == undefined){
            layer.msg("请选择半成品");
            return;
        }

        /*$.ajax({
            type:"post",
            url:"/queryMateNameByMateNumb?mateNumb="+mateCode,
            dataType:"JSON",
            async:false,//注意
            success:function(data){
                $('#prodName').val(data.mateName);
            },
            error:function (data) {
                // console.log(data);
            }
        });*/
        var asseCodeNameList = JSON.parse($('#asseCodeNameList').val());
        console.log(asseCodeNameList);
        if(asseCodeNameList != null){
            var str = '<option value ="" selected="selected">请选择</option>';
            for (var i=0;i<asseCodeNameList.length;i++){
                debugger
                if (asseCodeNameList[i].mateCode == mateCode) {
                    str+='<option value ="' + asseCodeNameList[i].packCode + '">' + asseCodeNameList[i].packCode + " " + asseCodeNameList[i].packName + '</option>'
                }
            };
        }
        $("#baoCaiName").html(str);
        //重新初始化下拉框
        form.render('select');
    });

    $('#file').on('change', function(){
        $('#fileName').html('');
    });
});

function initPackOrder() {
    var index = parent.layer.getFrameIndex(window.name);
    parentData = parent.data.rowData;

    // $('#mateNumbNameList').val(parentData.mateCode);//半成品
    // $('#baoCaiName').val(parentData.packCode);//包材
    // $('#mateNumbNameList').text(parentData.mateCode + " " + parentData.mateName);//半成品
    // $('#baoCaiName').text(parentData.packCode + " " + parentData.packName);//包材

    $('#prodName').val(parentData.mateName);
    $('#baocaiSupp').val(parentData.packSupp);//包材供应商
    $('#baoCaiNumb').val(parentData.packNum);//包材数量
    $('#orderCode').val(parentData.orderCode);//包材订单编码
    $('#orderDate').val(parentData.orderDate);//下单日期
    $('#deliDate').val(parentData.deliDate);//交货日期
    $('#fileName').html(parentData.acceFileName);//交货日期
    deleteFileId = parentData.acceFileId;
    deleteFileName = parentData.acceFileName;
    oldID = parentData.id;

    $("[id='mateCode'] option[value='"+parentData.mateCode+"']").attr("selected","true");
    // $("#mateCode").val(parentData.mateCode);
    // $("#baoCaiName").val(parentData.packCode);
    var str = '';
    var packCode = parentData.packCode;
    var packName = parentData.packName;
    /*if (packCode == null){
        str = '<option value ="" selected="selected">请选择</option>';
    }else{
        str = '<option value ='+packCode+' selected="selected">'+packCode+" " + packName+'</option>';
    }
    $("#baoCaiName").html(str);*/

    var asseCodeNameList = JSON.parse($('#asseCodeNameList').val());
    if(asseCodeNameList != null){
        var str = '<option value ="" selected="selected">请选择</option>';
        for (var i=0;i<asseCodeNameList.length;i++){
            if (asseCodeNameList[i].mateCode === parentData.mateCode) {
                str+='<option value ="' + asseCodeNameList[i].packCode + '">' + asseCodeNameList[i].packCode + " " + asseCodeNameList[i].packName + '</option>'
            }
        };
    }
    $("#baoCaiName").html(str);

    layui.use(['form'], function () {
        $ = layui.jquery;
        var form = layui.form;
        form.render('select'); //刷新select选择框渲染
    });
    $("[id='baoCaiName'] option[value='"+packCode+"']").attr("selected","true");
   /* $("#mateNumbNameList option").each(function(){
        if($(this).val() == parentData.mateCode) {
            $(this).attr("selected",true)
        }
    });
    $("#baoCaiName option").each(function(){
        if($(this).val() == parentData.packCode) {
            $(this).attr("selected",true)
        }
    });*/

}

function getAddOrderData(){
    var type = $("#type").val();
    // var filename = document.getElementById('file').value;//带路径的文件名
    var filename;
    var a = document.getElementById('file').files[0];
    if ( a !== null && a !== undefined){
        filename = a.name;//文件名
    }
    var paperFormData = new FormData($('#paperForm')[0]);// 序列化表单，
    var mateCode = $("#mateCode").val();
    var prodName = $("#prodName").val();
    var orderCode = $("#orderCode").val();
    var baocaiSupp = $("#baocaiSupp").val();
    var baoCaiNumb = $("#baoCaiNumb").val();
    var baoCaiName;
    var baoCaiCode;
    var obj=document.getElementById("baoCaiName");
    for(var i=0;i<obj.length;i++) {//下拉框的长度就是它的选项数.
        if(obj[i].selected === true) {
            var baoCaiNameCode=obj[i].text;//获取当前选择项的文本.
            var a = baoCaiNameCode.split(" ");
            if (a.length > 1){
                baoCaiName = a[1];
            }
            baoCaiCode=obj[i].value;//获取当前选择项的值.
        }
    }
    var orderDate = $("#orderDate").val();
    var deliDate = $("#deliDate").val();
    var orderDoc = $("#file").val();
    var result = checkMust();

    // console.log(mateCode);
    // console.log(prodName);

    var map={};
    if(!result.flag){
        map.judge=false;
        map.msg=result.msg;
        return map;
    }else{
        map.judge=true;
        map.mateCode = mateCode;
        map.prodName = prodName;
        map.orderCode = orderCode;
        map.baocaiSupp = baocaiSupp;
        map.baoCaiName = baoCaiName;
        map.baoCaiCode = baoCaiCode;
        map.baoCaiNumb = baoCaiNumb;
        map.orderDate = orderDate;
        map.deliDate = deliDate;
        map.orderDoc = orderDoc;
        map.paperFormData = paperFormData;
        if (type == 'edit'){
            map.id = oldID;
            if (filename ==null || filename =='') {
                filename = deleteFileName;
            }
        }
        map.filename = filename;
        map.type = type;
        if (deleteFileId != '' || deleteFileId != null) {
            map.deleteFileId = deleteFileId;
        }
        return map;
    }
}






