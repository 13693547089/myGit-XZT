var layer;
var table;
var tableData;
var upload;
var packOrderData = [];
var PackMessTable = [];
var ordertype =1;
var fid = $("#fid").val();
var fileData = null;
var form;
var addMaps=[];
var orderPackID;

layui.use([ 'table', 'laytpl', 'upload', 'layer', 'laydate','form' ], function(){
	form = layui.form;
	table = layui.table;
	upload = layui.upload;
	var laydate = layui.laydate;
    layer = layui.layer;
    var $ = layui.$;
	//获取订单编号，判断是查看还是添加
    var oemOrderCode = $('#OrderCode').val();
    var type = $('#type').val();
    if(type == 1){//编号和ID初始是空代表是创建
        initOrderTable([]);
        initOrderPackMessTable([]);
        initOrderPackMateTable([]);
    }else if(type == 2){//代表是编辑
        getOrderPackMessage(oemOrderCode);
	}else {//代表是查看
        $("#submitBut").hide();
        $("#saveBut").hide();
        $("#addOrder").hide();
        getOrderPackMessage(oemOrderCode);
        $("#oemOrderCode").append("<option value='Value'>oemOrderCode</option>");
        // $("#oemOrderCode").find("option[text="+oemOrderCode+"]").attr("selected",true);
	}

	  //返回
	$("#goBack").click(function() {
		//window.history.back(-1);
		tuoBack('.orderPackDetail','#serachSupp')
	});

	 //OEM采购订下拉框触发
	form.on("select(oemOrderCode)",function(obj){
	    var oemOrderCode = obj.value;
	    console.log('选择的订单编号：' + oemOrderCode);
	    if(oemOrderCode == '' || oemOrderCode == null || oemOrderCode == undefined){
	    	layer.msg("请选择订单编号");
	    	return;
	    }
        getOrderPackMessage(oemOrderCode);
	});

    $('#addOrder').on('click', function() {
        goAddPackHtml('add');
	});

    table.on('tool(orderPackMateTableDemo)',function(obj) {
        var row = obj.data;
        console.log(row);
        data = {
            rowData : row
        };
        if (obj.event === 'del'){
            /*layer.confirm('真的删除这行数据吗?', function(index){
                //obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                debugger;
                packOrderData.splice(index-1,1);
                initOrderPackMateTable(packOrderData);
                layer.close(index);
            });*/
        	console.log('我要把你删了~');
            var type = $('#type').val();
            if (type == 2) {
                for (var i = 0;i < packOrderData.length;i++) {
                    if (packOrderData[i].id == row.id) {
                        for (var j = 0;j < PackMessTable.length;j++) {
                            if (PackMessTable[j].mateCode == row.mateCode && PackMessTable[j].packCode == row.packCode) {
                                PackMessTable[j].placedNum = parseFloat(PackMessTable[j].placedNum) - parseFloat(row.packNum);
                                PackMessTable[j].resjdueNum = parseFloat(PackMessTable[j].resjdueNum) + parseFloat(row.packNum);
                            }
                        }
                        initOrderPackMessTable(PackMessTable)
                        packOrderData.splice(i,1);
                        initOrderPackMateTable(packOrderData);
                    }
                }
            } else {
                //删除前修改已下单和未下单数量
                for (var i = 0;i < PackMessTable.length;i++) {
                    if (PackMessTable[i].mateCode == row.mateCode && PackMessTable[i].packCode == row.packCode) {
                        PackMessTable[i].placedNum = parseFloat(PackMessTable[i].placedNum) - parseFloat(row.packNum);
                        PackMessTable[i].residueNum = parseFloat(PackMessTable[i].residueNum) + parseFloat(row.packNum);
                    }
                }
                initOrderPackMessTable(PackMessTable)
                packOrderData.remove(row.id);
                initOrderPackMateTable(packOrderData);
            }
           // packOrderData.splice(row,1);
            // deleteFile(row.acceFileId);
            //initOrderPackMateTable(packOrderData);
        }else if (obj.event === 'edit'){//编辑
            var ff = goAddPackHtml('edit',obj);
        }
        if (obj.event === 'down'){
        	console.log('下载你哟~');
            downLoadFile(row.acceFileId);
        }
    });

    //采购员选择供应商
    var selSuppCode;
    form.on("select(oemSupp)",function(obj){
        selSuppCode = obj.value;
        $('#oemSapId').val(selSuppCode);
        console.log(selSuppCode);
        getOrderNums(selSuppCode);

    });

    //订单编码选择
   /* var oemOrderCode;
    form.on("select(oemOrderCode)",function(obj){
        oemOrderCode = obj.value;
        $('#OrderCode').val(oemOrderCode);
        console.log(oemOrderCode);
    });*/

});

function goAddPackHtml(type,obj) {
    debugger;
    var object = obj;
    var oldMateCode;
    var oldMateName;
    var oldOrderCode;
    var oldPackSupp;
    var oldPackName;
    var oldPackCode;
    var oldPackNum;
    var oldOrderDate;
    var oldDeliDate;
    var oldAcceFileName;
    if(object != null && object != undefined){
    	oldMateCode = object.data.mateCode;
    	oldMateName = object.data.mateName;
    	oldOrderCode = object.data.orderCode;
    	oldPackSupp = object.data.packSupp;
    	oldPackName = object.data.packName;
    	oldPackCode = object.data.packCode;
    	oldPackNum = object.data.packNum;
    	oldOrderDate = object.data.orderDate;
    	oldDeliDate = object.data.deliDate;
    	oldAcceFileName = object.data.acceFileName;
    }
    var oemOrderCode = $('#oemOrderCode').val();
    layer.open({
        type : 2,
        title : '添加包材订单',
        shadeClose : false,
        shade : 0.1,
        content : "/getAddBaoCaiOrderHtml?oemOrderCode=" + oemOrderCode + "&type=" + type,
        area : [ '650px', '80%' ],
        maxmin : false, // 开启最大化最小化按钮
        btn: ['确认', '取消'],
        yes: function(index, layero){//确认
            // 获取填写的订单数据
            var addMap = $(layero).find("iframe")[0].contentWindow.getAddOrderData();
            var judge = false;
            if (addMap.type == 'edit'){//编辑
                console.log('编辑');
                object.update({
                    mateCode : addMap.mateCode,
                    mateName : addMap.prodName,
                    orderCode : addMap.orderCode,
                    packSupp : addMap.baocaiSupp,
                    packName : addMap.baoCaiName,
                    packCode : addMap.baoCaiCode,
                    packNum : addMap.baoCaiNumb,
                    orderDate : addMap.orderDate,
                    deliDate : addMap.deliDate,
                    acceFileName : addMap.filename
                });
                //选择完订单修改对应包材信息的已下单和未下单数量
                debugger
                var packMessTableData = [];
                packMessTableData = PackMessTable;
                for (var j = 0;j < packMessTableData.length;j++) {
            		var packMess = packMessTableData[j];
            		var mateCode1 = packMess.mateCode;
                	var packCode1 = packMess.packCode;
                	var packTotalNum = packMess.packTotalNum;
                	var totalPlacedNum = 0;
                	for (var i = 0;i < packOrderData.length;i++) {
                		var packOrder = packOrderData[i];
                		var mateCode2 = packOrder.mateCode;
                		var packCode2 = packOrder.packCode;
                		var packNum = packOrder.packNum;
                		if(mateCode1 == mateCode2 && packCode1 == packCode2 ){
                			totalPlacedNum=parseFloat(totalPlacedNum)+parseFloat(packNum);
                		}
                		
                	}
                	var residueNum = parseFloat(packTotalNum) - parseFloat(totalPlacedNum);
                	if(parseFloat(residueNum)<0){
                    	judge = true;
                    	break;
                    }
                	packMess.placedNum = totalPlacedNum;
                	packMess.residueNum = parseFloat(packTotalNum) - parseFloat(totalPlacedNum);
                	
            	}
                if(!judge){
                	layer.close(index);// 关闭弹框
                	addMaps.push(addMap);
                	PackMessTable = packMessTableData;
                	initOrderPackMessTable(PackMessTable)
                }else{
                	object.update({
                		mateCode : oldMateCode,
                		mateName : oldMateName,
                		orderCode : oldOrderCode,
                		packSupp : oldPackSupp,
                		packName : oldPackName,
                		packCode : oldPackCode,
                		packNum : oldPackNum,
                		orderDate : oldOrderDate,
                		deliDate : oldDeliDate,
                		acceFileName : oldAcceFileName
                	});
                	layer.msg("当前包材订单数量超过剩余可下单数量,无法添加");
                }
            }else {//新增
                console.log('新增');
                var uuid = guid();//新建订单的uuid存入map中，用于保存时文件上传后给订单文件字段赋值
                addMap.id = uuid;
                console.log(addMaps);
                if(addMap.judge){
                    fileData = null;
                    if(addMap.orderDoc !== null && addMap.orderDoc !== undefined){
                        // affirm(addMap.orderCode,addMap.paperFormData);
                    }
                    // alert(addMap.filename);
                    var obj = {
                        id:uuid,
                        mateCode : addMap.mateCode,
                        mateName : addMap.prodName,
                        orderCode : addMap.orderCode,
                        packSupp : addMap.baocaiSupp,
                        packName : addMap.baoCaiName,
                        packCode : addMap.baoCaiCode,
                        packNum : addMap.baoCaiNumb,
                        orderDate : addMap.orderDate,
                        deliDate : addMap.deliDate,
                        acceFileName : addMap.filename
                        // acceFileName : fileData.appeFile,
                        // acceFileUrl : fileData.fileUrl,
                        // acceFileId : fileData.fileId
                    };
                    var type = $('#type').val();
                    if (type == 2 ) {
                        obj.orderPackId = $('#OrderId').val();
                    }
                    //选择完订单修改对应包材信息的已下单和未下单数量
                    for (var i = 0;i < PackMessTable.length;i++) {
                        debugger;
                        if (PackMessTable[i].mateCode == addMap.mateCode && PackMessTable[i].packCode == addMap.baoCaiCode) {
                            var residueNum = parseFloat(PackMessTable[i].residueNum) - parseFloat(addMap.baoCaiNumb);
                            if(parseFloat(residueNum)<0){
                            	judge = true;
                            	break;
                            }
                        	PackMessTable[i].placedNum = parseFloat(PackMessTable[i].placedNum) + parseFloat(addMap.baoCaiNumb);
                            PackMessTable[i].residueNum = residueNum;
                        }
                    }
                    if(!judge){
                    	layer.close(index);// 关闭弹框
                    	addMaps.push(addMap);
                    	packOrderData.push(obj);
                    	console.log(packOrderData);
                    	initOrderPackMateTable(packOrderData);
                    	initOrderPackMessTable(PackMessTable)
                    }else{
                    	layer.msg("当前包材订单数量超过剩余可下单数量,无法添加");
                    }
                }else{
                    layer.msg(addMap.msg);
                }
            }

        },
        btn2: function(index, layero){
            //按钮【按钮二】的回调
            // 默认会关闭弹框
            //return false 开启该代码可禁止点击该按钮关闭
        }
    });
}

function getOrderNums(selSuppCode){
    $.ajax({
        type:"post",
        url:"/querySuppOrderCodeList?suppSapCode=" + selSuppCode,
        dataType:"JSON",
        async:false,
        success:function(data){
        	console.log(data);
            if(data.orderCodeList != null){
                var str = '<option value ="" selected="selected">请选择</option>';
                $.each(data.orderCodeList,function(index,row){
                    str+='<option value ="' + row + '">' + row + '</option>'
                });
            }
            console.log(str);
            $("#oemOrderCode").html(str);
            //重新初始化下拉框
            form.render('select');
        },
        error:function (data) {

        }
    });
}

//获取oem订单数据
function getOrderPackMessage(oemOrderCode){
    var type = $('#type').val();
    layer.load();
    $.ajax({
        type:"post",
        url:"/queryOrderPackMessage?oemOrderCode="+oemOrderCode+"&type="+type,
        dataType:"JSON",
        async:true,//注意
        success:function(data){
            layer.closeAll();
            // console.info(data);
            //OEM订单物料信息
            var orderMateList = data.orderMateList;
            //创建包材采购订单时，包材信息
            var baocaiInfoList = data.baocaiInfoList;
            //小计，合计，税额
            var caculateList = data.caculateList;
            //OEM订单信息
            var orderPackVO = data.orderPackVO;
            //编辑和查看时，包材信息
            var baocaiInfo2List = data.baocaiInfo2List;
            //包材订单信息
            packOrderData = data.baocaiOrderDetailList;

            $('#subeDate').val(data.subeDate);//订购日期
            if (type === '1'){//新建，取oem订单里的下单限比
                $('#limitThan').val(orderPackVO.limitThan);//下单限比（应该是点击更新的时候给下单限比重新赋值，默认取包材主表里面下单限比的值）
            }
            $('#purchOrg').val(orderPackVO.purchOrg);//采购组织
            $('#status').val(orderPackVO.status);//状态
            orderPackID = orderPackVO.id;
			console.log(orderMateList);
            initOrderTable(orderMateList);
            if (type == 1){//新建
            	initOrderPackMessTable(baocaiInfoList);
                PackMessTable = baocaiInfoList;
                if (baocaiInfoList.length == 0) {
                    layer.msg("该订单尚未维护半成品报价结构20段物料信息，请联系采购员后，再添加包材订单！", {
                        time : 3000
                    });
                    return;
                }
			}else if (type == 2){//编辑
                initOrderPackMessTable(baocaiInfo2List);
                PackMessTable = baocaiInfo2List;
                initOrderPackMateTable(packOrderData);
			}else {//查看，数据从表里面获取
            	initOrderPackMessTable(baocaiInfo2List);
                initOrderPackMateTable(packOrderData);
			}

			$('.Mone').val(caculateList[0]);
			$('.TaxMone').val(caculateList[1]);
			$('.tax').val(caculateList[2]);
        },
        error:function (data) {
            // console.log(data);
        }
    });
}

//订单物料信息列表
function initOrderTable(data){
	table.render({
		  elem:"#orderTable",
		  data:data,
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"orderTableId",
		  cols:[[
		     {type: 'numbers',title:'序号'},
		     {field:'frequency',title:'项次', align:'center',width:60},
		     {field:'mateNumb',title:'料号', align:'center',width:120},
		     {field:'prodName', title:'品名',align:'center',width:120},
		     {field:'suppRange', title:'供应商子范围编码',align:'center',width:113},
		     {field:'suppRangeDesc', title:'供应商子范围描述',align:'center',width:150},
		     {field:'boxEntrNumb',title:'箱入数',align:'center', width:60},
		     {field:'purcQuan',title:'采购数量', align:'center',width:60},
		     {field:'quanMate', title:'已交数量',align:'center',width:60},
		     {field:'unpaQuan', title:'未交数量',align:'center',width:60},
		     {field:'company',title:'单位', align:'center',width:50},
		     // {field: 'unitPric', title:'单价',width:100, align:'center'},
              {field: 'mateTex', title:'含税单价',width:100, align:'center'},
              {field: 'taxAmou', title:'含税金额',width:60, align:'center'},
              // {field: 'mone', title:'不含税金额',width:100, align:'center'},
		     {field: 'deliDate', title:'预定交货日',width:100, align:'center'}
		  ]]
	  })
}

//包材信息列表
function initOrderPackMessTable(data){
	table.render({
		  elem:"#orderPackMessTable",
		  data:data,
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"orderPackMessTableId",
		  cols:[[
		     {field:'frequency',title:'项次', align:'center',width:60},
		     {field:'mateCode',title:'料号', align:'center',width:120},
		     {field:'mateName', title:'品名',align:'center',width:120},
		     // {field:'packElem',title:'段名',align:'center', width:60,},
		     {field:'packCode',title:'包材编码', align:'center',width:120},
		     {field:'packName',title:'包材名称', align:'center',width:120},
		     // {field:'mateCode',title:'物料编号', align:'center',width:60},
		     {field:'dosageNum', title:'用量',align:'center',width:60},
		     {field:'packTotalNum', title:'包材总量',align:'center',width:100},
		     {field:'placedNum', title:'已下单数量',width:100, align:'center'},
		     {field:'residueNum', title:'剩余可下单数量',width:120, align:'center'}
		  ]]
	  })
}

//包材订单列表
function initOrderPackMateTable(data){
    var type = $('#type').val();
    var operateBar;
    if (type === '1'){//新建
        operateBar = "#barDemo1";
    }else if(type === '2'){//编辑
        operateBar = "#barDemo2";
    }else{//查看
        operateBar = "#barDemo3";
    }
	table.render({
		  elem:"#orderPackMateTable",
		  data:data,
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"orderPackMateTableId",
		  cols:[[
		     {type: 'numbers',title:'序号'},
		     {field:'mateCode',title:'料号', align:'center',width:120},
		     {field:'mateName', title:'品名',align:'center',width:120},
		     {field:'packCode',title:'包材编码',align:'center', width:120},
		     {field:'packName',title:'包材名称', align:'center',width:120},
		     {field:'packNum', title:'采购数量',align:'center',width:60},
		     {field:'packSupp', title:'包材供应商',align:'center',width:100},
		     {field:'orderDate',title:'订单日期', align:'center',width:60},
		     {field:'orderCode', title:'订单编号',width:100, align:'center'},
		     {field:'deliDate', title:'交货日期',width:60, align:'center'},
		     {field:'acceFileName', title:'附件',width:100, align:'center'},
		     {field:'right', title:'操作',width:100, align:'center',toolbar: operateBar}
		  ]]
	  })
}

//弹出层的确认按钮,添加附件
function affirm(orderCode,paperFormData) {
	$('.btn-primary').hide();
	// var appeType = $("#appeType").val();
	// var appeName = $("#appeName").val();
	// var orderNumb= $("#contOrdeNumb").val();
	// var saveFormData = new FormData($('#paperForm')[0]);// 序列化表单，
	// $("form").serialize()只能序列化数据，不能序列化文件
	$.ajax({
		type : "POST",
		url : '/doc/docUpload?direCode=DDGL&linkNo='+orderCode,
		data : paperFormData,// 你的form的id属性值
		processData : false,
		contentType : false,
		async : false,
		error : function(request) {
			parent.layer.msg("程序出错了！", {
				time : 2000
			});
		},
		success : function(res) {
			if(res.msg=='上传失败'){
				parent.layer.msg("上传文件失败", {
					time : 2000
				});
			}
			if (res.code == '0') {// 文件上传成功
				console.info(res.data);
				var index = $('#papertable').bootstrapTable('getData').length;
                var paperData = [];
				var object = new Object();
				// object.appeType = appeType;
				// object.appeName = appeName;
				object.appeFile = res.data[0].realName;
				object.newName = res.data[0].fileName;
				object.fileUrl = res.data[0].fileUrl;
				object.fileId = res.data[0].id;
				fileData = object;
                console.log(fileData);
                paperData.push(object);
				$('#orderEncl').bootstrapTable("load", paperData);
				return object;
				// parent.layer.msg("添加附件成功", {
				// 	time : 2000
				// });
			}
		}
	});
	$('.btn-primary').attr('data-dismiss','modal');
	ordertype=2;
}

//文件下载
function downLoadFile(docId) {
	window.location.href = "/doc/downLoadDoc?docId=" + docId
}

//删除附件(包括文件服务上的文件)
function deleteFileById(docId) {
    var docIds = [];
    docIds.push(docId);
    $.ajax({
        type : 'post',
        url : "/deletePaperFileOfOrder",
        data : {
            jsonStr : JSON.stringify(docIds)
        },
        success : function(msg) {},
        error : function() {
            layer.msg("程序出错了！", {
                time : 1000
            });
        }
    });
}

//删除附件(包括文件服务上的文件)
function deleteFile(docId) {
	var docIds = [];
	docIds.push(docId);
	$.ajax({
		type : 'post',
		url : "/deletePaperFileOfOrder",
		data : {
			jsonStr : JSON.stringify(docIds)
		},
		success : function(msg) {
			if (msg.code == '0') {
				$('#orderEncl').bootstrapTable('remove', {
					field : 'fileId',
					values : docIds
				});
				layer.msg("删除成功！", {
					time : 1000
				});
				 $.ajax({
					  type:"post",
					  url:"/setDeleteEncl?docId="+docId,
					  dataType:"JSON",
					  success:function(data){
						  
					  }
				  });
			} else {
				layer.msg("删除失败！", {
					time : 1000
				});
			}
		},
		error : function() {
			layer.msg("程序出错了！", {
				time : 1000
			});
		}
	});
}

//保存包材订单
function saveOrderPackMate(status){

	console.log(packOrderData);
    if(packOrderData==null || packOrderData.length==0){
        layer.msg('请添加订单数据！',{time:1000});
        return false;
    }
    /*var flag=true;
    $.each(mateList,function(index,row){
        var startDate=row.startDate;
        var endDate=row.endDate;
        if(startDate==null || startDate=='' ||endDate==null || endDate==''){
            flag=false;
        }
    });
    if(!flag){
        layer.msg('请维护有效期后保存！',{time:1000});
        return false;
    }*/
    var orderPackList = [];
    var row = {};
    row.id= orderPackID;
    row.oemOrderCode= $("#oemOrderCode").val();
    row.status= status;
    row.createTime= $("#subeDate").val();
    row.limitThan= $("#limitThan").val();
    row.suppCode= $("#oemSapId").val();

    var suppNameTemp;
    var obj = document.getElementById("oemSupp");
    var type = $('#type').val();
    var userType = $('#userType').val();
    if(type == 1 && userType=='buyer'){//如果是采购员创建,去下拉框中获取供应商名称
        for(i=0;i<obj.length;i++) {//下拉框的长度就是它的选项数.
            if(obj[i].selected==true) {
                suppNameTemp=obj[i].text;//获取当前选择项的值.
            }
        }
	}else {
        suppNameTemp = $("#oemSuppName").val();
    }
    row.suppName= suppNameTemp;

    row.purchOrg= $("#purchOrg").val();
    // row.remark= $("#remark").val();
    orderPackList.push(row);

    //上传文件
    for (var i=0;i<packOrderData.length;i++){
        for (var j=0;j<addMaps.length;j++){
            if (packOrderData[i].id == addMaps[j].id) {
				var paperFormData = addMaps[j].paperFormData;
				var packOrderCode = packOrderData[i].orderCode;
                if (addMaps[j].filename != null && addMaps[j].filename != '') {
                    if (addMaps[j].deleteFileId != '' && addMaps[j].deleteFileId != null) {
                        deleteFileById(addMaps[j].deleteFileId);
                    }
                    affirm(packOrderCode,paperFormData);
                    packOrderData[i].acceFileUrl = fileData.fileUrl;
                    packOrderData[i].acceFileId = fileData.fileId;
                }
            }
		}
	}
    var type = $('#type').val();
    $.ajax({
        type:"POST",
        data:{orderPackJson:JSON.stringify(orderPackList),
            packMessJson:JSON.stringify(PackMessTable),
            packMateJson:JSON.stringify(packOrderData),type:type},
        url:"/saveOrderPackMate",
        success:function(res){
        	debugger;
            var code=res.code;
            if(code==0){
                if (status == '已保存') {
                    layer.msg('保存成功',{time:1000});
                    $("#saveBut").attr("disabled",'disabled');
                    $("#status").val("已保存");
                }
            }
        }
    });
    /*$.ajax({
        type:"POST",
        data:{mateJson:JSON.stringify(mateList)},
        url:prefix+"/updateValidDate",
        success:function(res){
            var code=res.code;
            if(code==0){
                layer.msg('操作成功',{time:1000});
            }
        }
    });*/
}

//保存包材订单
$('#saveBut').click(function(){
    if (checkNoOrderedNum()) {//校验剩余数量
        saveOrderPackMate("已保存");
    }else{
        layer.msg("当前包材订单数量超过剩余可下单数量！",{time: 1000});
    }
});

// 提交
$("#submitBut").click(function(){
    if (checkNoOrderedNum()) {
        var type = $('#type').val();
        if(type == 1){//创建,编号和ID初始是空
            if ($("#status").val() == "" || $("#status").val() == null){
                saveOrderPackMate("已提交");
            }else{
                var oemOrderCode = $('#oemOrderCode').val();
                $.ajax({
                    type:"post",
                    url:"/submitOrder?oemOrderCode="+oemOrderCode,
                    dataType:"JSON",
                    success:function(data){
                        if(data.code=="0"){
                            layer.msg("提交成功！",{time: 1000});
                            $("#saveBut").attr("disabled",'disabled');
                            $("#submitBut").attr("disabled",'disabled');
                            $("#status").val("已提交");
                        }else{
                            layer.msg("提交失败！",{time: 1000});
                        }
                    }
                });
            }
        }else if(type == 2){//编辑
            saveOrderPackMate("已提交");//先调用保存的方法
            var oemOrderCode = $('#OrderCode').val();
            $.ajax({
                type:"post",
                url:"/submitOrder?oemOrderCode="+oemOrderCode,
                dataType:"JSON",
                success:function(data){
                    if(data.code=="0"){
                        layer.msg("提交成功！", {time: 1000});
                        $("#saveBut").attr("disabled", 'disabled');
                        $("#submitBut").attr("disabled", 'disabled');
                        $("#status").val("已提交");
                    }else{
                        layer.msg("提交失败！",{time: 1000});
                    }
                }
            });
        }
    }else{
        layer.msg("当前包材订单数量超过剩余可下单数量",{time: 1000});
    }
});

//更新
$("#updateBut").click(function(){
    updateOrderPackMess();
});

//更新
function updateOrderPackMess(){
    layer.load();
    var oemOrderCode = $('#OrderCode').val();
    var limitThan = $('#limitThan').val();
    var orderId = $('#OrderId').val();
    $.ajax({
        type:"post",
        data:{
            packMessJson:JSON.stringify(PackMessTable),
            limitThan:limitThan,
            oemOrderCode:oemOrderCode,
            packId:orderId
        },
        url:"/updateOrderPackMess",
        async:true,
        success:function(data){
            layer.closeAll();
            console.log(data);
            if(data.judge){
            	debugger;
            	//最新包材信息列表数据
            	var packMessList = data.baocaiInfoList;
            	//OEM订单数据
            	var orderPackVO = data.orderPackVO;
            	//OEM订单物料数据
            	var orderMateList = data.orderMateList;
            	//小计，合计，税额
            	var caculateList = data.caculateList;
            	//更新下单限比
            	$('#limitThan').val(orderPackVO.limitThan);
            	initOrderTable(orderMateList);
            	//更新包材信息表
            	PackMessTable = packMessList;
            	initOrderPackMessTable(PackMessTable);
            	$('.Mone').val(caculateList[0]);
    			$('.TaxMone').val(caculateList[1]);
    			$('.tax').val(caculateList[2]);
            	layer.msg("更新成功！",{time: 1000});
            }else{
            	layer.msg(data.msg,{time: 1000});
            }

        },
        error:function (data) {

        }
    });
}

//校验剩余数量
function checkNoOrderedNum() {
    var flag = true;
    for ( var i = 0; i < PackMessTable.length; i++) {
        if (PackMessTable[i].residueNum < 0) {
            flag = false;
        }
    }
    return flag;
}

Array.prototype.remove = function(val) {
    for ( var k = 0; k < this.length; k++) {
        if (this[k].id == val) {
            this.splice(k, 1);
            return;
        }
    }
};





