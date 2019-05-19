layui.use([ 'form', 'table','layer'], function() {
	var $=layui.jquery;
	var prefix="/actReach";
	var table=layui.table;
	var layer=layui.layer;
	var form=layui.form;
	var tableIns=[];
	//加载页面初始化表格
	loadTable();
	//取消按钮点击事件
	$("#cancleBtn").click(function(){
		closeDg();
	});
	/**
	 * layer关闭子表窗口
	 * @returns
	 */
	function closeDg(){
		var index = parent.layer.getFrameIndex(window.name);//获取子窗口索引
		 parent.layer.close(index);
	}
	
	/**
	 * 交货计划表格数据的加载
	 * @returns
	 */
	function loadTable(){
		var mateCode=$("#mateCode").val();
		var mateDesc=$("#mateDesc").val();
		var planMonth=$('#planMonth').val();
		var endDate=$('#endDate').val();
		$.ajax({
			url:prefix+"/getSuppActReach",
			type:"post",
			data:{mateCode:mateCode,mateDesc:mateDesc,planMonth:planMonth,endDate:endDate},
			success:function(res){
				loadSubTotal(res);
			}
		});
	}
	/**
	 * 更新小计等信息
	 * @returns
	 */
	function loadSubTotal(data){
		var subTotal={};
		subTotal.suppNo='小计';
		
		subTotal.planPrdNum=0;
		subTotal.actPrdNum=0;
		subTotal.pudReachScale="";
		
		subTotal.planDlvNum=0;
		subTotal.actDlvNum=0;
		subTotal.dlvReachScale="";
		
		subTotal.suppActNum=0;
		subTotal.safeScale='';
		data.push(subTotal);
		var length=data.length;
		
		var subPlanPrdNum=0;
		var subActPrdNum=0;
		
		var subPlanDlvNum=0;
		var subActDlvNum=0;
		
		var subSuppActNum=0;
		
		for(var i=0;i<length-1;i++){
			var currRow=data[i];
			
			var planPrdNum=currRow.planPrdNum;
			var actPrdNum=currRow.actPrdNum;
			
			var planDlvNum=currRow.planDlvNum;
			var actDlvNum=currRow.actDlvNum;
			
			var nextDevNum=currRow.nextDevNum;
			var suppActNum=currRow.suppActNum;
			
			if(planPrdNum==null){
				planPrdNum=0;
				currRow.planPrdNum=0;
			}
			if(actPrdNum==null){
				actPrdNum=0;
				currRow.actPrdNum=0;
			}
			if(planDlvNum==null){
				planDlvNum=0;
				currRow.planDlvNum=0;
			}
			if(actDlvNum==null){
				actDlvNum=0;
				currRow.actDlvNum=0;
			}
			if(suppActNum==null){
				suppActNum=0;
				currRow.suppActNum=0;
			}
			if(nextDevNum==null){
				nextDevNum=0;
				currRow.nextDevNum=0;
			}
			if(planPrdNum!=0){
				currRow.pudReachScale=decimal(actPrdNum/planPrdNum*100,2)+"%";
			}
			if(planDlvNum!=0){
				currRow.dlvReachScale=decimal(actDlvNum/planDlvNum*100,2)+"%";
			}
			if(nextDevNum!=0){
				currRow.safeScale=decimal(suppActNum/nextDevNum*100,2)+"%";
			}

			subPlanPrdNum+=parseFloat(planPrdNum);
			subActPrdNum+=parseFloat(actPrdNum);
			subPlanDlvNum+=parseFloat(planDlvNum);
			subActDlvNum+=parseFloat(actDlvNum);
			subSuppActNum+=parseFloat(suppActNum);
		}
		subTotal.planPrdNum=subPlanPrdNum;
		subTotal.actPrdNum=subActPrdNum;
		
		subTotal.planDlvNum=subPlanDlvNum;
		subTotal.actDlvNum=subActDlvNum;
		
		subTotal.suppActNum=subSuppActNum;
		
		
		if(subPlanPrdNum!=0){
			subTotal.pudReachScale=decimal(subActPrdNum/subPlanPrdNum*100,2)+"%";
		}
		if(subPlanDlvNum!=0){
			subTotal.dlvReachScale=decimal(subActDlvNum/subPlanDlvNum*100,2)+"%";
		}
		var mateNextDevNum=$('#nextDevNum').val();
		
		if(mateNextDevNum==null || mateNextDevNum==''){
			mateNextDevNum=0;
		}else{
			mateNextDevNum=parseFloat(mateNextDevNum);
		}
		subTotal.nextDevNum=mateNextDevNum;
		if(mateNextDevNum!=0){
			subTotal.safeScale=decimal(subSuppActNum/mateNextDevNum*100,2)+"%";
		}
		initInvenPlanDtetailTable(data);
	}
	/**
	 * 初始化排产计划详情表
	 * @param condition
	 * @returns
	 */
	function initInvenPlanDtetailTable(data){
		tableIns=table.render({
			elem : '#suppTable',
			id:'suppTable',
			data:data,
			limit:5000,
			cols : [ [ {
				type : 'numbers',
				title : '序号'
			},{
				field : 'suppNo',
				width : '10%',
				title : '供应商编码',
			},  {
				field : 'suppName',
				width : '17%',
				title : '供应商'
			},  {
				field : 'planPrdNum',
				width : '8%',
				title : '生产计划'
			}, {
				field : 'actPrdNum',
				width : '8%',
				title : '实际生产'
			}, {
				field : 'pudReachScale',
				width : '10%',
				title : '实际生产达成'
			}, {
				field : 'planDlvNum',
				width : '8%',
				title : '交货计划'
			}, {
				field : 'actDlvNum',
				width : '8%',
				title : '实际交货'
			}, {
				field : 'dlvReachScale',
				width : '8%',
				title : '交货达成'
			}, {
				field : 'suppActNum',
				width : '10%',
				title : '供应商实际库存'
			},{
				field : 'safeScale',
				width : '10%',
				title : '安全库存率'
			} ] ]
		});
	}
})
