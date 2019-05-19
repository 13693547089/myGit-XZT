layui.use([ 'form', 'table' ,'laydate','layer'], function() {
	var $=layui.jquery;
	var prefix="/invenPlan";
	var planDetailArr=[];
	var table=layui.table;
	var laydate=layui.laydate;
	var layer=layui.layer;
	var form=layui.form;
	var tableIns=[];
	var type=$("#type").val();
	if(type==3){
		disableFormItem();
	}else{
		laydate.render({
		    elem: '#planMonth',
		    type:'month',
		    done: function(value, date, endDate){
		    	$("#planMonth").val(value);
		    	inintSersiers();
		     }
		});
	}
	window.loadDetailTable=function(){
		var id=$("#id").val();
		$.post(prefix+"/getPlanDetailByMainId",{mainId:id},function(res){
			planDetailArr=res;
			initInvenPlanDtetailTable(planDetailArr)
		});
	}
	//加载页面初始化表格
	initDate();
	inintSersiers();
	if(type=='1'){
		initInvenPlanDtetailTable(planDetailArr)
		$("#id").val(guid());
	}else{
		loadDetailTable();
	}
	
	//保存事件
	$("#saveBtn").click(function(){
		var status=$("#status").val();
		if(status=='已提交'){
			layer.msg('已提交的计划不能更改',{time:1000});
		}
		saveInvenPlan();
	});
	//提交事件
	$("#submitBtn").click(function(){
		var status=$("#status").val();
		if(status=='已提交'){
			layer.msg('已提交的计划请勿重复提交',{time:1000});
			return;
		}
		var rows=tableIns.config.data;
		var flag=false;
		$.each(rows,function(index,row){
			if(row.status=='未分解'){
				flag=true;
			}
		});
		if(flag){
			layer.msg('存在未分解的数据,请分解后提交');
			return ;
		}
		$("#status").val('已提交');
		saveInvenPlan();
	});
	
	//排产计划按钮
	$("#suppProdBtn").click(function(){
		var id=$("#id").val();
		var status=$("#status").val();
		if(status!='已提交'){
			layer.msg("只有已确认的备货计划才能进行排产",{time:1000});
			return ;
		}
		window.location.href=prefix+"/getSuppProdPage?id="+id;
	});
	//返回事件
	$("#backBtn").click(function(){
		history.go(-1);
	});
	//点击添加扣款信息按钮
	$("#addBtn").click(function(){
		//判断年月是否选择
		var planMonth=$("#planMonth").val();
		if(planMonth==null || planMonth==''){
			layer.msg('请选择年月',{time:1000});
			return false;
		}
		//判断产品系列是否选择 
		var prodSeriesCode=$("#prodSeriesCode").val();
		if(prodSeriesCode==null || prodSeriesCode==''){
			layer.msg("请选择产品系列",{time:1000});
			return false;
		}
		//弹出添加物料的页面
		var perContent=layer.open({
			  type: 2,
			  shade: false,
			  title:'交货计划',
			  area: ['800px', '500px'],
			  maxmin: true,
			  content: prefix+'/getAddInvenPlanDPage?planMonth='+planMonth+"&prodSeriesCode="+prodSeriesCode,
			  yes: function(index, layero){

			  }
		});
		layer.full(perContent);
	});
	//监听表格事件  分解或者撤销	
	 table.on('tool(planDetail-table)', function(obj) {
			if(type==3 && obj.event!="view"){
				return;
			}
			var data = obj.data;
			var planCode=$('#planCode').val();
			var mainStatus=$("#status").val();
			var status=data.status;
			var delRows=[];
			delRows.push(data);
			if(planCode==null || planCode==''){
				layer.msg('请先保存备货计划',{time:1000});
				return ;
			}else if(mainStatus =='已提交' && obj.event!='view'){
				layer.msg('备货计划已提交不能进行该操作',{time:1000});
				return ;
			}
			if (obj.event === 'decompose') {
				if(status!='未分解'){
					layer.msg('请分解未分解状态下的数据',{time:1000});
				}else{
					//进行分解的业务逻辑
					decomposeDetail(data);
				}
			}else if(obj.event === 'cancle'){
				if(status!='已分解'){
					layer.msg('请分解状态为排产计划已提交的数据',{time:1000});
				}else{
					//撤销分解的业务逻辑
					$.ajax({
						url:prefix+"/cancleDecompose",
						data:{id:data.id,status:"未分解"},
						success:function(res){
							loadDetailTable();
							layer.msg("操作成功！",{time:1000});
						},
						error:function(){
							layer.msg("操作失败！",{time:1000});
						}
					});
				}
			}else if(obj.event==='view'){
				if(status!='已分解'){
					layer.msg('请查看已分解的数据',{time:1000});
				}else{
					viewDecomposeDetail(data);
				}
			}
		});
	/**
	 * 保存备货计划信息
	 * @returns
	 */
	function saveInvenPlan(){
		var status=$("#status").val();
		if(status==null || status==''){
			$("#status").val("已保存");
		}
		var details=tableIns.config.data;
		var detailJson=JSON.stringify(details);
		$("#detailJson").val(detailJson);
		var planMonth=$('#planMonth').val();
		var planCode=$('#planCode').val();
		var mateCodes=[];
		$.each(details,function(index,row){
			mateCodes.push(row.mateCode);
		});
		debugger;
		//判断子表物料是否存在
		var flag=true;
		var msg="该月的备货计划中已经存在该物料！";
		$.ajax({
			url:prefix+"/isExist",
			async:false,
			data:{planCode:planCode,planMonth:planMonth,mateCodeJson:JSON.stringify(mateCodes)},
			success:function(res){
				if(res.code!=0){
					flag=false;
				}
			},
		});
		debugger;
		if(!flag){
			layer.msg(msg,{time:1000});
			return;
		}
		var options = {
				url: prefix+"/saveInvenPlan",
				type:"POST",
				success: function (msg) {
					if(msg.code=="0"){
						id=msg.data.id;
						$('#id').val(id);	
						$('#planCode').val(msg.data.planCode);	
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
		$("#invenPlan-form").ajaxSubmit(options);
	}
	//产品选择
	form.on('select(prodSeriesCode)', function(data){
		var prodSeriesDesc=$(data.elem).find("option:selected").text();
		$("#prodSeriesDesc").val(prodSeriesDesc);
	});
	/**
	 * 初始化创建日期
	 * @returns
	 */
	function initDate(){
		 var id=$('#id').val();
		 if(id==null || id==''){
			 $('#createDate').val(formatTime(new Date(),'yyyy-MM-dd'));
		 }
	}
	/**
	 * 初始化产品系列
	 * @returns
	 */
	function inintSersiers(){
		var planCode=$("#planCode").val();
		var planMonth=$("#planMonth").val();
		if(planMonth==null || planMonth==''){
			if(planCode==null || planCode==''){
				return;
			}
			layer.msg('请选择年月',{time:1000});
			return ;
		}else{
			$.post(prefix+"/getProdSeriers",{planMonth:planMonth},function(res){
				var seriesCode=$("#seriesCode").val();
				var str='<option value="">请选择</option>';
				$.each(res,function(index,row){
					if(row.code==seriesCode){
						str+='<option selected="selected" value="'+row.code+'">'+row.name+'</option>';
					}else{
						str+='<option value="'+row.code+'">'+row.name+'</option>';
					}
				});
				$("#prodSeriesCode").html(str);
				//重新初始化下拉菜单
				form.render('select');
			});
		}
	}
	/**
	 * 初始化排产计划详情表
	 * @param data
	 * @returns
	 */
	function initInvenPlanDtetailTable(data){
		if(type==3){
			tableIns=table.render({
				elem : '#planDetail-table',
				id:'planDetail-table',
				data:data,
				page : true,
				cols : [ [ {
					type : 'checkbox',
				},{
					field : 'status',
					title : '状态'
				},{
					field : 'mateCode',
					title : '物料编码',
				},{
					field : 'mateDesc',
					title : '物料名称',
				},{
					field : 'ranking',
					title : '排名',
				},{
					field : 'beginOrder',
					title : '期初订单',
				},{
					field : 'beginStock',
					title : '期初库存',
				}, {
					field : 'beginEnableOrder',
					title : '期初可生产订单'
				}, {
					field : 'prodPlan',
					title : '生产计划'
				},{
					field : 'deliveryPlan',
					title : '交货计划'
				},{
					field : 'endStock',
					title : '期末预计库存'
				},{
					field : 'safeScale',
					title : '安全库存率',
				},{
					title : '操作',
					width : 150,
					fixed : 'right',
					field : 'operateBar1',
					align : 'center',
					toolbar : '#operateBar1'
				} ] ]
			});
			
		}else{
			tableIns=table.render({
				elem : '#planDetail-table',
				id:'planDetail-table',
				data:data,
				page : true,
				cols : [ [ {
					type : 'checkbox',
				},{
					field : 'status',
					fixed : 'left',
					width : 100,
					title : '状态'
				},{
					field : 'mateCode',
					fixed : 'left',
					width : 180,
					title : '物料编码',
				},{
					field : 'mateDesc',
					fixed : 'left',
					width : 180,
					title : '物料名称',
				},{
					field : 'ranking',
					width : 100,
					title : '排名',
				},{
					field : 'beginOrder',
					width : 100,
					title : '期初订单',
				},{
					field : 'beginStock',
					width : 100,
					title : '期初库存',
				}, {
					field : 'beginEnableOrder',
					width : 100,
					title : '期初可生产订单'
				}, {
					field : 'prodPlan',
					width : 100,
					title : '生产计划'
				},{
					field : 'deliveryPlan',
					width : 100,
					title : '交货计划'
				},{
					field : 'endStock',
					width : 100,
					title : '期末预计库存'
				},{
					field : 'safeScale',
					width : 100,
					title : '安全库存率',
				},{
					title : '操作',
					width : 150,
					fixed : 'right',
					field : 'operateBar',
					align : 'center',
					toolbar : '#operateBar'
				} ] ]
			});
		}
	}
	/**
	 * 子表点击时添加父类表格
	 * data 子表中选中的数据的JSON
	 * 
	 */
	window.addRows=function(data){
		var dataSource=tableIns.config.data;
		$.each(data,function(addIndex,addRow){
			var flag=false;//不包含
			$.each(dataSource,function(index,row){
				if(row.mateCode==addRow.mateCode){
					flag=true;//包含
				}
			});
			if(!flag){//不包含添加数据
				dataSource.push(addRow);
			}
		});
		initInvenPlanDtetailTable(dataSource);
	}
	/**
	 * 分解备货计划详情
	 * @returns
	 */
	function decomposeDetail(data){
		var nextMonthDeliveryNum=data.nextMonthDeliveryNum;
		if(nextMonthDeliveryNum==null || nextMonthDeliveryNum==''){
			nextMonthDeliveryNum=0;
		}
		var planMonth=$('#planMonth').val();
		//弹出添加物料的页面
		var perContent=layer.open({
			  type: 2,
			  shade: false,
			  title:'分解计划',
			  area: ['800px', '500px'],
			  maxmin: true,
			  content: prefix+'/getDecomposePage?mateCode='+data.mateCode+"&mateDesc="+data.mateDesc+"&deliveryPlan="+data.deliveryPlan+"&mainId="+data.id+"&planMonth="+planMonth+"&nextMonthDeliveryNum="+nextMonthDeliveryNum,
			  yes: function(index, layero){

			  }
		});
		layer.full(perContent);
	}
	
	/**
	 * 查看分解的排查计划
	 * @param data
	 * @returns
	 */
	function viewDecomposeDetail(data){
		var nextMonthDeliveryNum=data.nextMonthDeliveryNum;
		if(nextMonthDeliveryNum==null || nextMonthDeliveryNum==''){
			nextMonthDeliveryNum=0;
		}
		var planMonth=$('#planMonth').val();
		//弹出添加物料的页面
		var perContent=layer.open({
			  type: 2,
			  shade: false,
			  title:'分解计划',
			  area: ['800px', '500px'],
			  maxmin: true,
			  content: prefix+'/getDecomposeViewPage?mateCode='+data.mateCode+"&mateDesc="+data.mateDesc+"&deliveryPlan="+data.deliveryPlan+"&mainId="+data.id+"&planMonth="+planMonth+"&nextMonthDeliveryNum="+nextMonthDeliveryNum,
			  yes: function(index, layero){

			  }
		});
		layer.full(perContent);
	}
})

