	//地址前缀
var prefix = "/sync";

var form;
$(function(){
	
	// 同步
	$("#syncBtn").click(function(){

		var padMonth = $('#padMonth').val();
		var str = "所有月份";
		if(padMonth != '' && padMonth != undefined){
			str = padMonth+"月份";
		}else{
			// 暂时只开放  同步某月份
			layer.msg("请选月份！",{time: 2000});
			return;
		}
		
		var isChecked =  $("input[type='checkbox']").is(':checked'); // $("#isFlag").is(':checked')
		if(isChecked){
			isChecked = "1";
		}else{
			isChecked = "0";
		}
		
		var confirmDialog = layer.confirm('是否同步'+str+'数据？', {
		  btn: ['确定','取消'] //按钮
		}, function(){
			layer.close(confirmDialog);
			
			// 启动加载层
			var index = layer.load(0, {shade: false});
			// 获取明细数据
			$.ajax({
				 type:"post",
				 url:prefix+"/syncPadMaterial",
				 data:{"padMonth":padMonth,"isChecked":isChecked},
				 dataType:"JSON",
				 success:function(data){
					// 关闭加载层
					layer.close(index);
					if(data.code==0){
						layer.msg("同步成功！",{time: 2000});
					}else{
						layer.msg(data.msg,{time: 2000});
					}
				 },error: function (XMLHttpRequest, textStatus, errorThrown) {
			           /*  // 状态码
		             console.log(XMLHttpRequest.status);
		             // 状态
		             console.log(XMLHttpRequest.readyState);
		             // 错误信息   
		             console.log(textStatus);*/
					 
		 	    	// 关闭加载层
		 	    	layer.close(index);
		         }
			});
		}, function(){
		});
	});
	
	/*// 删除
	$("#delBtn").click(function(){
		
	});
	
	// 删除所有
	$("#delAllBtn").click(function(){
		
	});*/
	
});

// 定义日期格式
function initLayuiDate(laydate){
	  // 月份
	  laydate.render({
	    elem: '#padMonth',
	    type: 'month',
	    done: function(value, date, endDate){
	        //value 得到日期生成的值，如：2017-08-18
	        //date 得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
	        //endDate 得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员同上。
	     }
	  });
}

// 物料明细table
layui.use([ 'form','laydate'], function() {
	var $ = layui.$
	,laydate = layui.laydate;
	form = layui.form;
	
	// 初始日期控件
	initLayuiDate(laydate);
});
