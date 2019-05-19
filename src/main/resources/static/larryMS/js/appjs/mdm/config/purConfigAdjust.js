var prefix = '/item';
var currentRow = "";
var table;

var userSourceData = [];
var userTargetData = [];
var adjustData=[];

layui.use('table', function() {
	table = layui.table;
	var $ = layui.$;
	// 获取采购员的数据
	$.ajax({
		type:"post",
		url:prefix+"/getPurchaserInfo",
		dataType:"JSON",
		async: false,
		success:function(data){
			if(data){
				userSourceData = data.data;
				
				// 实现深拷贝
				userTargetData =JSON.parse(JSON.stringify(data.data));
			}
			// 加载采购员数据
			loadUserSource(table,userSourceData);
			loadUsertarget(table,userTargetData);
		}
	});
	
	// 加载调整结果
	loadAdjustData(table, adjustData);
	
	// 添加调整的员工
	$('#addBtn').on('click', function() {
	   	var sCkStatus = table.checkStatus('sourceTb');
	   	var sCkData = sCkStatus.data;
		if(sCkData.length != 1){
			layer.msg("请选择一条源员工数据！",{time:2000});
			return;
		}
		var tCkStatus = table.checkStatus('targetTb');
	   	var tCkData = tCkStatus.data;
		if(tCkData.length != 1){
			layer.msg("请选择一条目标员工数据！",{time:2000});
			return;
		}
		
		if(sCkData[0].userName == tCkData[0].userName){
			layer.msg("请选择不同的用户！",{time:2000});
			return;
		}
		
		var sCode = sCkData[0].userName;
		var tCode = tCkData[0].userName;
		// 加载调整的数据
		var arr={
			sId:sCkData[0].id,
			sUserCode:sCode,
			sUserName:sCkData[0].name,
			tId:tCkData[0].id,
			tUserCode:tCode,
			tUserName:tCkData[0].name
		};
		adjustData.push(arr);
		loadAdjustData(table, adjustData);
		
		// 删除已选择的用户
		userSourceData = delArr(userSourceData,sCode);
		userTargetData = delArr(userTargetData,tCode);
		
		loadUserSource(table,userSourceData);
		loadUsertarget(table,userTargetData);
	});
	
	function delArr(source,code){
		var arr = [];
		for(var i=0;i<source.length;i++){
			if(code != source[i].userName){
				arr.push(source[i]);
			}
		}
		return arr;
	}
	
	// 重置所有的调整数据
	$('#resetBtn').on('click', function() {
		window.location.reload();
	});
	
	 $(document).on("click",".xuanzhong3 table tbody tr",function() {
		  $('#sourceTb + div tr').removeClass('pitch3');
		  $(event.target).parents('tr').addClass('pitch3');
		});
	$(document).on("click",".xuanzhong4 table tbody tr",function() {
		  $('#targetTb + div tr').removeClass('pitch3');
		  $(event.target).parents('tr').addClass('pitch3');
		});
	// 删除选中的物料
	$('#delBtn').on('click',function() {
		var ckStatus = table.checkStatus('adjustTbId');
	   	var ckData = ckStatus.data;
		if(ckData.length == 0){
			layer.msg("请选择需要删除的调整结果！",{time:2000});
			return;
		}
		
		// 获取整个data的行号
		var delIndexMap = new Map();
		for(var i=0;i<adjustData.length;i++){
			var code = adjustData[i].sUserCode;
			delIndexMap.set(code,i);
		}
		
		// 获取需要删除的数据的行号
		var delIndexArr = [];
		for(var i=0;i<ckData.length;i++){
			var code = ckData[i].sUserCode;
			if(delIndexMap.has(code)){
				delIndexArr.push(delIndexMap.get(code));
			}
		}
		
		// 删除
		for (var i = delIndexArr.length-1; i >=0 ; i--) {
			// 记录删除项
			var item = adjustData[delIndexArr[i]];
   			// 删除数组元素
			adjustData.splice(delIndexArr[i],1);
			// 还原删除的用户回选择列表中
			var sArr = {
					id:item.sId,
					userName:item.sUserCode,
					password:"",
					email:"",
					mobile:"",
					status:"",
					name:item.sUserName,
					leader:0
			};
			userSourceData.push(sArr);
			
			var tArr = {
					id:item.tId,
					userName:item.tUserCode,
					password:"",
					email:"",
					mobile:"",
					status:"",
					name:item.tUserName,
					leader:0
			};
			userTargetData.push(tArr);
   		}
		
		loadAdjustData(table, adjustData);
		// 重新加载选择项
		loadUserSource(table,userSourceData);
		loadUsertarget(table,userTargetData);
	});
	
	// 确认调整
	$('#confimBtn').on('click',function() {
		if(adjustData.length==0){
			layer.msg("请添加需要调整的用户！",{time:2000});
			return;
		}
		// 记录添加的用户记录数
		var adjustMap = new Map();
		for(var i = 0;i<adjustData.length;i++){
			var item = adjustData[i];
			var sId = item.sId;
			if(!adjustMap.has(sId)){
				adjustMap.set(sId,1);
			}else{
				adjustMap.set(sId,2);
			}
			var tId = item.tId;
			if(!adjustMap.has(tId)){
				adjustMap.set(tId,1);
			}else{
				adjustMap.set(tId,2);
			}
		}
		
		// 判断所有的值是否都是2
		var flg = "";
		adjustMap.forEach(function (item, key, mapObj) {
			if(item == 1){
				flg = "W";
			}
		});
		
		if(flg == "W"){
			layer.msg("调整的用户不能形成回路，请重新调整！",{time:2000});
			return;
		}
		
		// 处理
		var adjustRsData =JSON.stringify(adjustData);
		$.ajax({
			 type:"post",
			 url:prefix+"/dealAdjustData",
			 dataType:"JSON",
			 data:{"adjustData":adjustRsData},
			 success:function(data){
				 if(data.code == 0){
					 layer.msg("调整成功！");
				 }else{
					 layer.msg("调整失败！");
				 }
			 }
		 });
	});
});

function loadUserSource(table,userSourceData){
	table.render({
		elem : '#sourceTb',
		data: userSourceData,
		page : true,
		//even: true,//隔行变色
		height:'auto',
		limit: 100, //显示的数量
		limits: [100,200,1000], //显示的数量
		cols : [ [ 
				{checkbox:true},
				//  {field:'sn', title: '序号',width:50,type:'numbers'},
				{
					field : 'userName',
					title : '员工编号',
					align : 'center',
					width : 120
				}, {
					field : 'name',
					title : '采购员姓名',
					align : 'center'
				} ] ]//,
		//id : 'userSourceId'
	});
}

function loadUsertarget(table,userTargetData){
	table.render({
		elem : '#targetTb',
		data: userTargetData,
		page : true,
		//even: true,//隔行变色
		height:'auto',
		limit: 100, //显示的数量
		limits: [100,200,1000], //显示的数量
		cols : [ [ 
		  	 {checkbox:true},
			 // {field:'sn', title: '序号',width:50,type:'numbers'},
			 {
				field : 'userName',
				title : '员工编号',
				align : 'center',
				width : 120
			}, {
				field : 'name',
				title : '采购员姓名',
				align : 'center'
			} ] ]//,
		//id : 'userTargetId'
	});
}

function loadAdjustData(table, adjustData) {
	table.render({
		elem : '#adjustTb',
		data: adjustData,
		page : true,
		height:'auto',
		limit: 100, //显示的数量
		limits: [100,200,1000], //显示的数量
		cols : [ [ {
			checkbox : true
		}, 
		  {field:'sn', title: '序号',width:50,type:'numbers'},
		  {
			field : 'sUserCode',
			title : '源员工编码',
			align : 'center',
			width : 150
		}, {
			field : 'sUserName',
			title : '源采购员',
			align : 'center'
		}, {
			field : 'tUserCode',
			title : '目标员工编码',
			align : 'center',
			width : 150
		} , {
			field : 'tUserName',
			title : '目标采购员',
			align : 'center'
		}
		] ],
		id : 'adjustTbId'
	});
}