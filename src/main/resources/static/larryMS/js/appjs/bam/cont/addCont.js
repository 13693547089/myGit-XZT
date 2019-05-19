	//地址前缀
var prefix = "/cont";
var id='';
var type="1";
//定义树形控件
layui.use([ 'tree', 'table','laytpl','upload','layer','laydate','form'], function() {
	var $ = layui.$;
	var upload = layui.upload;
	var laydate=layui.laydate;
	var layer=layui.layer;
	var form=layui.form;
	var table = layui.table;
	var fileData=[];
	var menus=[];
	type=$('#type').val();
	if(type==3){
		disableFormItem();
	}else{
	  laydate.render({
		    elem: '#startDate'
		  });
	  laydate.render({
		    elem: '#endDate'
		  });
	}
	id=$("#id").val();
	if(id==null || id==''){
		initTempTable(fileData);
		initLogTable([]);
	}else{
		$.ajax({
			url:"/doc/getDocsByLinkNo",
			data:{linkNo:id,docCate:'bam_cont'},
			success:function(msg){
				initTempTable(msg);
			}
		});
		$.ajax({
			url:prefix+"/getTempLogByContId",
			data:{contId:id},
			success:function(msg){
				initLogTable(msg);
			}
		});
	}
	initDate();
	 //指定允许上传的文件类型
	upload.render({
	   elem: '#addFile'
	   ,url: '/doc/docUpload?direCode='+'HTGL'+'&linkNo='+id+"&docCate="+'bam_cont'
	   ,accept: 'file' //普通文件
	   ,done: function(res){
	      if(res.code=='0'){
	    	  $('#filetable').bootstrapTable("append",res.data[0]); 
	      }
	    }
	 });
	function initDate(){
		 //创建时这是创建时间合有效开始时间与结束时间
		 var id=$('#id').val();
		 if(id==null || id==''){
			 $('#createTime').val(formatTime(new Date(),'yyyy-MM-dd'));
		 }
	}
	$("#backBtn").click(function(){
		tuoBack("addCont","#searchBtn");
//		 window.history.go(-1);
	});
	$("#saveBtn").click(function(){
		saveCont();
	});
	$("#publishBtn").click(function(){
		$("#contStatus").val("YFB");
		saveCont();
	});
	
	function saveCont(){
		var result=checkMust();
		if(result.flag==false){
			layer.msg(result.msg,{time:1000});
			return;
		}
		var id=$("#id").val();
		var contCode=$("#contNo").val();
		var flag=true;
		$.ajax({
			async:false,
			url:prefix+"/checkIsExist",
			data:{id:id,contCode:contCode},
			success:function(res){
				flag=res;
			}
		});
		if(!flag){
			layer.msg('合同编号已存在！',{time:1000});
			return;
		}
		var files=$('#filetable').bootstrapTable("getData");
		$('#jsonFile').val(JSON.stringify(files));
		var options = {
				url: prefix+"/saveCont",
				success: function (msg) {
					if(msg.code=="0"){
						id=msg.cont.id;
						$('#id').val(id);	
						layer.msg("操作成功！",{time: 1000});
						type=$('#type').val('2');
					}else{
						layer.msg("操作失败！",{time: 1000});
					}
				},
				error: function(request) {
					layer.msg("程序出错了！",{time: 1000});
				}
		};
		$("#tempForm").ajaxSubmit(options);
	}
	
	form.on('select(firstPart)', function(data){
		 var firstPartName=$(data.elem).find("option:selected").text();
		 $("#firstPartName").val(firstPartName);
	});
	form.on('select(secondPart)', function(data){
		var secondPartName=$(data.elem).find("option:selected").text();
		$("#secondPartName").val(secondPartName);
	});
	form.on('select(contType)', function(data){
		var contTypeName=$(data.elem).find("option:selected").text();
		$("#contTypeName").val(contTypeName);
	});
	//模板表格初始化
	function initTempTable(data) {
		$('#filetable').bootstrapTable({
			method: 'get',
			editable:true,//开启编辑模式
			undefinedText:'',
			pageList: [10,20],  
			pageSize:10,  
			pageNumber:1, 
			striped: true,  
			columns: [
		      {checkbox: true},
		      {
		    	  field:"attachType",
		    	  edit:{
		    		  type:'select',//下拉框
		    		  required:true,
		    		  url:'/dic/findDicListByCategoryCode?cateCode=CONTTYPE',
		    		  valueField:'dicCode',
		    		  textField:'dicName',
		    		  onSelect:function(val,rec){
		    		  }
		    	  },
		    	  title:"文件类型",
		    	  align:"center"
		      },
		      {
		    	  field:"realName",
		    	  title:"文件名称",
		    	  edit:false,
		    	  align:"center"
		      },
		      {field:"docSize",title:"文件大小",align:"center",edit:false},
		      {field:'id',title:"id", align:'center',visible:false,edit:false,},
		      {field: 'status',title: '操作',align:"center",
                  formatter : function(value, row,  index) {
						var editStr=
				  		'<a class="layui-btn layui-btn-danger layui-btn-xs redInline " style="text-decoration:none;" onclick="deleteFile(\''+row.id+'\') ">删除</a>'+
				  		'<a class="layui-btn layui-btn-xs blueInline "  style="text-decoration:none;" onclick="downLoadFile(\''+row.id+'\') ">下载</a>';
						if(type==3){
							editStr='<a class="layui-btn layui-btn-xs blueInline "  style="text-decoration:none;" onclick="downLoadFile(\''+row.id+'\') ">下载</a>';
						}
						return editStr;
					}
              }
		      ],
		      data:data
		});
	} 
});
//日志表格初始化
function initLogTable(logs) {
	$('#logtable').bootstrapTable('destroy');
	$('#logtable').bootstrapTable({
		method: 'get',
		editable:true,//开启编辑模式
		clickToSelect: true,
		undefinedText:'',
		pageList: [10,20],  
		pageSize:10,  
		pageNumber:1, 
		striped: true,  
		columns: [
	          {field:"operatorName",title:"操作人",align:"center",edit:false},
	          {field:'operateTime',title:"操作时间", align:'center',edit:false,formatter:timeFormatter},
	          {field:'operation',title:"操作内容", align:'center',edit:false,},
	      ],
	      data:logs
	});
} 
//文件下载
function downLoadFile(docId){
	window.location.href="/doc/downLoadDoc?docId="+docId
}
//文件删除
function deleteFile(docId){
	var docIds=[];
	docIds.push(docId);
	$.ajax({
		type:'post',
		url:prefix+"/deleteFile",
		data:{jsonStr:JSON.stringify(docIds)},
		success:function(msg){
			if(msg.code=='0'){
				$('#filetable').bootstrapTable('remove',{field:'id',values:docIds});
				layer.msg("删除成功！",{time:1000});
			}else{
				layer.msg("删除失败！",{time:1000});
			}
		},
		error:function(){
			layer.msg("程序出错了！",{time:1000});
		}
	});
}
//点击文件列表上方的删除按钮
$('#delFile').click(function(){
	//获取选中文件列表的文件
	var rows=$('#filetable').bootstrapTable('getSelections');
	var docIds=[];
	$.each(rows,function(index,row){
		docIds.push(row.id);
	});
	$.ajax({
		type:'post',
		url:prefix+"/deleteFile",
		data:{jsonStr:JSON.stringify(docIds)},
		success:function(msg){
			if(msg.code=='0'){
				//后台删除成功后  将文件从列表中移除
				$('#filetable').bootstrapTable('remove',{filed:'id',value:docIds});
				layer.msg("删除成功！",{time:1000});
			}else{
				layer.msg("删除失败！",{time:1000});
			}
		},
		error:function(){
			layer.msg("程序出错了！",{time:1000});
		}
	});
});
//下载全部
$('#downLoadAll').click(function(){
	var rows=$('#filetable').bootstrapTable('getSelections');
	$.each(rows,function(index,row){
		debugger;
		var docId=row.id;
		alert(docId);
		setTimeout(function(){ window.location.href="/doc/downLoadDoc?docId="+docId;}, 5000);
	});
});

//function sele_Change(selectC){
//	alert(selectC.options[selectC.selectedIndex].value);
//	selectC.options[selectC.selectedIndex].value;
//   }

//多文件列表示例
// var demoListView = $('#demoList')
// ,uploadListIns = upload.render({
//   elem: '#testList'
//   ,url: '/upload/'
//   ,accept: 'file'
//   ,multiple: true
//   ,auto: false
//   ,bindAction: '#testListAction'
//   ,choose: function(obj){   
//     var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
//     //读取本地文件
//     obj.preview(function(index, file, result){
//   	var length = $("#demoList").find("tr").length-1;
//   	alert(length);
//   	alert(index);
//   	var myIndex=parseInt(index.split('-')[1])+1+length;
//       var tr = $(['<tr id="upload-'+ index +'">'
//         ,'<td>'+ myIndex +'</td>'
//         ,'<td>'+ file.name +'</td>'
//         ,'<td>'+ file.name +'</td>'
//         ,'<td><select id="MySelect" onchange="sele_Change(this)"><option id="1">123</option> <option id="2">321</option></select></td>'
//         ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
//         ,'<td>'
//           ,'<button class="layui-btn layui-btn-mini demo-reload layui-hide">重传</button>'
//           ,'<button class="layui-btn layui-btn-mini layui-btn-danger demo-delete">删除</button>'
//         ,'</td>'
//       ,'</tr>'].join(''));
//       
//       //单个重传
//       tr.find('.demo-reload').on('click', function(){
//         obj.upload(index, file);
//       });
//       
//       //删除
//       tr.find('.demo-delete').on('click', function(){
//         delete files[index]; //删除对应的文件
//         tr.remove();
//         uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
//       });
//       
//       demoListView.append(tr);
//     });
//   }
//   ,done: function(res, index, upload){
//     if(res.code == 0){ //上传成功
//       var tr = demoListView.find('tr#upload-'+ index)
//       ,tds = tr.children();
//       tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
//       tds.eq(3).html(''); //清空操作
//       return delete this.files[index]; //删除文件队列已经上传成功的文件
//     }
//     this.error(index, upload);
//   }
//   ,error: function(index, upload){
//     var tr = demoListView.find('tr#upload-'+ index)
//     ,tds = tr.children();
//     tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
//     tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
//   }
// });
