var table;
layui.use(['table','laydate'], function(){
    table = layui.table;
    var $ = layui.$;
    var laydate = layui.laydate;
    var id ='';

    initCutLiaisonTable();

    //添加
    //跳转详情页面
    $('#addBut').click(function(){
      console.log('工具条创建');
      var url ="/getBaoCaiContactSheetDetailHtml";
       //location=url;
       tuoGo(url,'ContactSheetForBaoCaiDetail',"cutLiaisonTableId");
    });

    //监听工具条
    table.on('tool(cutLiaisonTable)', function(obj){
    var data = obj.data;
    if(obj.event === 'check'){//查看
        console.log('行查看');
        var url  ="/getBaoCaiContactSheetCheckHtml?liaiId="+data.liaiId;
        //location=url;
        tuoGo(url,'ContactSheetForBaoCaiCheck','cutLiaisonTableId');
    } else if(obj.event === 'del'){//删除
        console.log('行删除');
       if(data.status=='已保存' || data.status=='已退回'){
          layer.confirm('真的删除这个打切联络单吗？', function(index){
              var liaiIds =[];
              liaiIds.push(data.liaiId);
              $.ajax({
                 type:"post",
                 url:"/deleteBaoCaiCutLiaisonByliaiIds",
                 data:"liaiIds="+liaiIds,
                 dataType:"JSON",
                 success:function(data2){
                     if(data2){
                         layer.msg('删除成功', {time:2000 });
                         initCutLiaisonTable();
                     }else{
                         layer.alert('<span style="color:red;">删除失败</sapn>');
                     }
                 }
              });
              layer.close(index);
          });
       }else{
           layer.alert('<span style="color:red;">只有"已保存","已退回"状态的打切联络单才能被删除</sapn>');
       }
    } else if(obj.event === 'edit'){//编辑
        console.log('行编辑');
        if(data.status=="已保存" || data.status=="已退回"){
            var url  ="/getBaoCaiContactSheetEditHtml?liaiId="+data.liaiId;
            //location=url;
            tuoGo(url,'ContactSheetForBaoCaiEdit','cutLiaisonTableId');
        }else{
            layer.alert('<span style="color:red;">只有"已保存","已退回"状态的打切联络单才可以编辑</sapn>');
        }
    }
    });

    // 条件搜索
    var $ = layui.$, active = {
        reload : function() {
            var cutMonth = $('#cutMonth');
            var suppInfo = $("#suppInfo");
            var status = $("#status");
            var liaiCode = $("#liaiCode");
            // 执行重载
            table.reload('cutLiaisonTableId', {
                page : {
                    curr : 1
                // 重新从第 1 页开始
                },
                where : {
                    cutMonth : cutMonth.val(),
                    suppInfo : suppInfo.val(),
                    status : status.val(),
                    liaiCode : liaiCode.val()
                }
            });
        }
    };

    //年月选择器
    laydate.render({
    elem: '#cutMonth'
    ,type: 'month'
    });

    //删除
    $("#removeBut").click(function(){
    console.log('工具条删除');
      var table = layui.table;
        var checkStatus = table.checkStatus('cutLiaisonTableId');
        var data = checkStatus.data;
        var length = data.length;
        if(length != 0){
              var liaiIds = [];
              var a=0;
              for(var i=0;i<length;i++){
                  liaiIds[i]=data[i].liaiId;
                  if(data[i].status !='已保存' && data[i].status !='已退回'){
                      a++;
                  }
              }
              if(a == 0){
                  layer.confirm('真的删除选中的打切联络单吗？', function(index){
                     $.ajax({
                         type:"post",
                         url:"/deleteBaoCaiCutLiaisonByliaiIds",
                         data:"liaiIds="+liaiIds,
                         dataType:"JSON",
                         success:function(data2){
                             if(data2){
                                 layer.msg('删除成功', {time:2000 });
                                 initCutLiaisonTable();
                             }else{
                                 layer.alert('<span style="color:red;">删除失败</sapn>');
                             }
                         },
                         error:function(){
                            layer.msg('程序出错', {time:2000 });
                         }
                      });
                      layer.close(index);
                  });
              }else{
                  layer.alert('<span style="color:red;">只有"已保存","已退回"状态的包材打切联络单才能被删除</sapn>');
              }
          }else{
              layer.alert('<span style="color:red;">请选择一条或多条数据进行删除</sapn>');
          }
    });

    //提交
    $("#submitBut").click(function(){
      console.log('工具条提交');
      var table = layui.table;
      var checkStatus = table.checkStatus('cutLiaisonTableId');
      var data = checkStatus.data;
      var length = data.length;
      if(length == 1){
          var status = data[0].status;
          if(status == "已保存" || status == "已退回"){
              //layer.confirm('真的提交选中的打切联络单吗？', function(index){
                  var liaiIds = [];
                  var liaiId = data[0].liaiId;
                  liaiIds.push(liaiId);
                  id = liaiId;
                  debugger;
                  var liaiIdsJson =JSON.stringify(liaiIds);
                  $.ajax({
                      type:"post",
                      url:"/updateBaoCaiCutLiaiStatusByliaiIds?liaiIds="+liaiIdsJson+"&types=2",
                      dataType:"JSON",
                      success:function(data2){
                          if(data2){
                              //layer.msg('提交成功', {time:2000 });
                              initCutLiaisonTable();
                          }else{
                              layer.alert('<span style="color:red;">提交失败</sapn>');
                          }
                      },
                      error:function(){
                          layer.msg('程序出错', {time:2000 });
                      }
                  });
          }else{
              layer.alert('<span style="color:red;">只有"已保存","已退回"状态的包材打切联络单才能提交</sapn>');
          }
      }else{
          layer.alert('<span style="color:red;">请选择一条数据进行提交</sapn>');
      }
    });

    //退回
    $("#citeAddBut").click(function(){
      console.log('工具条退回');
      var table = layui.table;
      var checkStatus = table.checkStatus('cutLiaisonTableId');
      var data = checkStatus.data;
      var length = data.length;
      if(length == 1){
          var status = data[0].status;
          if(status == "已提交"){
              var liaiIds = [];
              var liaiId = data[0].liaiId;
              liaiIds.push(liaiId);
              id = liaiId;
              debugger;
              var liaiIdsJson =JSON.stringify(liaiIds);
              $.ajax({
                  type:"post",
                  url:"/updateBaoCaiCutLiaiStatusByliaiIds?liaiIds="+liaiIdsJson+"&types=3",
                  dataType:"JSON",
                  success:function(data2){
                      if(data2){
                          //layer.msg('提交成功', {time:2000 });
                          initCutLiaisonTable();
                      }else{
                          layer.alert('<span style="color:red;">提交失败</sapn>');
                      }
                  },
                  error:function(){
                      layer.msg('程序出错', {time:2000 });
                  }
              });
          }else{
              layer.alert('<span style="color:red;">只有"已提交"状态的包材打切联络单才能退回</sapn>');
          }
      }else{
          layer.alert('<span style="color:red;">请选择一条数据进行退回</sapn>');
      }
    });

    $('.demoTable .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});

//初始化table
function initCutLiaisonTable() {
	table.render({
		elem : "#cutLiaisonTable",
		url : "/queryBaoCaiCutLiaisonByPage",
		page : true,
		width : '100%',
		minHeight : '20px',
		limit : 10,
		id : "cutLiaisonTableId",
		cols : [ [
				{
					checkbox : true,
				},
				{
					title : '序号',
					type : 'numbers'
				},
				{
					field : 'status',
					title : '状态',
					align : 'center',
					width : 92
				},
				{
					field : 'cutMonth',
					title : '打切月份',
					align : 'center',
					width : 99
				},
				{
					field : 'liaiCode',
					align : 'center',
					title : '包材打切联络单号',
					width : 117
				},
				{
					field : 'suppId',
					align : 'center',
					title : '供应商编码',
					width : 110
				},
				{
					field : 'suppName',
					align : 'center',
					title : '供应商',
					width : 271
				},
				{
					field : 'creator',
					align : 'center',
					title : '创建人',
					width : 179
				},
				{
					field : 'createDate',
					align : 'center',
					title : '创建时间',
					width : 113,
					templet : function(d) {
						var date = new Date(d.createDate);
						var year = date.getFullYear();
						var month = date.getMonth() + 1;
						var day = date.getDate();
						return year + "-" + (month < 10 ? "0" + month : month)
								+ "-" + (day < 10 ? "0" + day : day);
					}
				}, {
					fixed : 'right',
					title : '操作',
					width : 160,
					align : 'center',
					toolbar : '#barDemo'
				} ] ]
	})
}