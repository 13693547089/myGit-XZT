var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initOrderPackTable();
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  laydate.render({
		    elem: '#startDate', //指定元素
	  });
	  laydate.render({
		    elem: '#endDate', //指定元素
	  });
	  
	  //监听工具条
	  table.on('tool(demo)', function(obj){
		var userId = $("#userId").val();
	    var data = obj.data;
	    console.log(data);
	    if(obj.event === 'check'){//查看
	    	var url  ="/getOrderPackDetailHtml?id="+data.id+"&type=3&oemOrderCode="+data.oemOrderCode;
    		//location=url;
	    	tuoGo(url,'orderPackDetail',"orderPackId");
	    } else if(obj.event === 'del'){//删除
			if (data.status=="已提交"){
                layer.msg('已提交的订单不能删除', {time:2000 });
                return;
			}
	       if(data.creator==userId){
		      layer.confirm('真的删除这个订单吗？', function(index){
		    	  var Ids =[];
		    	  Ids.push(data.id);
		    	  $.ajax({
		    		 type:"post",
		    		 url:"/deleteOrderPackById",
		    		 data:"Ids="+Ids,
		    		 dataType:"JSON",
		    		 success:function(data2){
		    			 if(data2){
		    				 layer.msg('删除成功', {time:2000 });
		    				 // 删除
                             deleteFile(data.acceFileId);
                             initOrderPackTable();
		    			 }else{
		    				 layer.alert('<span style="color:red;">删除失败</sapn>');
		    			 }
		    		 }
		    	  });
		    	  layer.close(index);
		      });
	       }else{
	    	   layer.alert('<span style="color:red;">只能删除自己创建的包材采购订单</sapn>');
	       }
	    } else if(obj.event === 'edit'){//编辑
	    	if(data.creator==userId){
	    		var url  ="/getOrderPackDetailHtml?id="+data.id+"&type=2&oemOrderCode="+data.oemOrderCode;
	    		//location=url;
	    		tuoGo(url,'orderPackDetail',"orderPackId");
	    	}else{
	    		layer.alert('<span style="color:red;">只能编辑自己创建的包材采购订单</sapn>');
	    	}
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      debugger;
			     var startDate = $('#startDate');
			      var endDate = $("#endDate");
			      var oemSuppName = $("#oemSuppName");
			      var purchOrg = $("#purchOrg");
			      var oemOrderCode = $("#oemOrderCode");
			      var status = $("#status");
			      //执行重载
			      table.reload('orderPackId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  startDate: startDate.val(),
			        	  endDate:endDate.val(),
			        	  oemSuppName:oemSuppName.val(),
			        	  purchOrg:purchOrg.val(),
			        	  oemOrderCode:oemOrderCode.val(),
			        	  status:status.val()
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	  
	  
	//新建
	  $("#addOrderPack").click(function(){
	  	var url="/getOrderPackDetailHtml?type=1";
	  	tuoGo(url,'orderPackDetail',"orderPackId");
	  });
});

function initOrderPackTable(){
	table.render({
		  elem:"#orderPackTableId",
		  url:"/queryOrderPackByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"orderPackId",
		  cols:[[
			 {type:'numbers',align:'center',title:"序号"},
		     {field:'status',title:'状态', align:'center',width:96 },
		     {field:'oemOrderCode',title:'OEM采购订单', align:'center',width:118},
		     {field:'subeDate', title:'订单日期',align:'center',width:89,templet:
		    	 function(d){
		    	    var date = new Date(d.subeDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		     },
		     {field:'oemSuppName',title:'OEM供应商',align:'center', width:264},
		     {field:'purchOrg',title:'采购组织', align:'center',width:134},
		     {field:'createName',title:'创建人', align:'center',width:134},
		     {fixed: 'right', title:'操作',width:160, align:'center', toolbar: '#barDemo'}
		  ]]
	  })
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

