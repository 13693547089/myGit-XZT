var tableData=[];
var table;
var receData;
var receId;
layui.use(['form','table'], function(){
	  var form = layui.form;
	  table = layui.table;
	  layer = layui.layer;
	  var $ = layui.$;
	  receId = $("#receId").val();
	  $.ajax({
		 type:"post",
		 url:"/queryReceMatesByReceId?receId="+receId,
		 dataType:"JSON",
		 success:function(data){
			 if(data!= null){
				 $.each(data,function(index,item){
					tableData.push(item); 
				 });
				 initTable(table,tableData);
			 }
		 },
		 error:function(){
			 layer.alert("Connection error");
		 }
	  });
     //返回
	 $("#goBack").click(function(){
	 	window.history.back(-1);
	 });
	 //打印  
     $("#affirmBut").bind("click", function (event) {
     	$('#affirmBut').hide();
     	$('#goBack').hide();
     	  $('.layui-table-box, .layui-table-view').css('width','1392px !important');
     	  window.history.back(-1);
         doPrint(); 
     }); 
	   
		
});
function doPrint() {  
	 window.document.close();                   //这句很重要，没有就无法实现    
    window.print();   
}

//初始化预约申请物资表格
function initTable(table,data){
	 receData = table.render({
		elem:"#receMateTable",
		data:data,
		id:"receMateTableId",
		width:'100%',
		limit:100,
		cols:[[
		   {title:"项次",field:"frequency",align:'center',width:50},
		   {field:"orderId",title:"采购订单号",align:'center',width:80},
		   {field:"mateCode",title:"物料编码",align:'center',width:145},
		   {field:"mateName",title:"物料名称",align:'center',width:180},
		   {field:"deliNumber",title:"送货数量",align:'center',width:60},
		   {field:"receNumber",title:"实收数量",align:'center',width:60},
		   {field:"unit",title:"单位",align:'center',width:45},
		   {field:"prodPatchNum",title:"产品批号",align:'center',width:86},
		   {field:"remark",title:"备注",align:'center',width:60},
		]]
		
	});
}






Array.prototype.remove = function(val) {
	for ( var k = 0; k < this.length; k++) {
		if (this[k].id == val.id) {
			this.splice(k, 1);
			return;
		}
	}
};

