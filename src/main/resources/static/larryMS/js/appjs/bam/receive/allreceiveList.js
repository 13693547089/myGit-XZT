var table;
layui.use(['table','laydate'], function(){
	  table = layui.table;
	  var $ = layui.$;
	  var laydate = layui.laydate;
	  initReceTable();
	  laydate.render({
		    elem: '#startDate', //指定元素
	  });
	  laydate.render({
		    elem: '#endDate', //指定元素
	  });
	  laydate.render({
		  elem: '#createDate', //指定元素
	  });
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
	  });
	  //监听工具条
	  table.on('tool(demo)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'check'){//查看
			var url  ="/getReceiveEditHtml2?receId="+data.receId+"&funType=2";
			//location=url;
			tuoGo(url,'receiveEdit',"receiveTableId");
	    } else if(obj.event === 'del'){//删除
	       
	    } else if(obj.event === 'edit'){//编辑
	    	
	    }
	  });
	  //条件搜索 --注意这是给予按钮赋点击事件，必须与按钮的data-type的属性值相对应
	  var $ = layui.$, active = {
			    reload: function(){
			      var startDate = $('#startDate');
			      var endDate = $("#endDate");
			      var status = $("#status");
			      var receCode = $("#receCode");
			      var suppName = $("#suppName");
			      var createDate = $("#createDate");
			      var deliCode = $("#deliCode");
			      //执行重载
			      table.reload('receiveTableId', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			        ,where: {//后台定义对象接收
			        	  startDate: startDate.val(),
			        	  endDate:endDate.val(),
			        	  status:status.val(),
			        	  receCode:receCode.val(),
			        	  suppName:suppName.val(),
			        	  deliCode:deliCode.val(),
			        	  createDate:createDate.val()
			        }
			      });
			    }
			  
		};
	  
	  $('.demoTable .layui-btn').on('click', function(){
	    var type = $(this).data('type');
	    active[type] ? active[type].call(this) : '';
	  });
	 
	
	  //重置
	  $("#reset").click(function(){
	  	$("#suppInfo").val('');
	  	$("#category").val('');
	  });
	  
	  
	  
});

function initReceTable(){
	table.render({
		  elem:"#receiveTable",
		  url:"/queryAllReceiveByPage",
		  page:true,
		  width: '100%',
		  minHeight:'20px',
		  limit:10,
		  id:"receiveTableId",
		  cols:[[
		     {checkbox: true, fixed:'left'},
		     {field:'status',title:'单据状态', align:'center',width:93},
		     {field:'receCode',title:'收货单号', align:'center',width:113 },
		     {field:'receDate', title:'收货日期',align:'center',width:106,templet:
		    	 function(d){
		    	    var date = new Date(d.receDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					/*var h = date.getHours();
					var m = date.getMinutes();
					var s =date.getSeconds();*/
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		     },
		     {field:'suppName', title:'供应商',align:'center',width:145},
		     {field:'deliCode', title:'送货单号',align:'center',width:113},
		     {field:'creator', title:'创建人',align:'center',width:109},
		     {field:'createDate',title:'创建时间', align:'center',width:106,templet:
		    	 function (d){
		    	    var date = new Date(d.createDate);
					var year = date.getFullYear();
					var month = date.getMonth()+1;
					var day = date.getDate();
					return year+"-"+(month<10 ? "0"+month : month)+"-"+(day<10 ? "0"+day : day);
		     	 }
		     },
		     {fixed: 'right', title:'操作',width:150, align:'center', toolbar: '#barDemo'}
		  ]]
	  })
}
