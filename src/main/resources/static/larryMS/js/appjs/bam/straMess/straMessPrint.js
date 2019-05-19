var tableData=[];
var table;
layui.use(['form','table'], function(){
	  var form = layui.form;
	  table = layui.table;
	  layer = layui.layer;
	  var $ = layui.$;
	  var messId = $("#messId").val();
	  var img=$('#Img').attr('src');
	  $.ajax({
		 type:"post",
		 url:"/queryMessMateForStraMess?messId="+messId,
	  	 dataType:"JSON",
	  	 success:function(data){
	  		 debugger;
	  		 tableData = data.list
	  		 initTable(table,tableData);
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
	table.render({
		elem:"#MessmateTable",
		data:data,
		id:"MessmateTableId",
		width:'100%',
		limit:100,
		cols:[[
		   {field:"frequency",title:"项次",align:'center',width:83},
		   {field:"orderId",title:"调拨单号",align:'center',width:110},
		   {field:"mateName",title:"物料名称",align:'center',width:160},
		   {field:"mateCode",title:"物料编码",align:'center',width:160},
		   {field:"mateNumber",title:"数量",align:'center',width:70},
		   {field:"mateAmount",title:"方量",align:'center',width:70},
		   {field:"unit",title:"单位",align:'center',width:70},
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

