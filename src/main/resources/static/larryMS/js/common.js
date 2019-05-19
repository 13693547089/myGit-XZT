// 日期格式化
Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

function timeFormatter(value, row, index) {
	if (value == null) {
		return "";
	} else if (value == "") {
		return "";
	} else {
		return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
	}
}
// layui日期格式化 value 时间戳 format 日期格式化
function formatTime(value, format) {
	if(value==null || value==''){
		return '';
	}
	return new Date(value).Format(format);
}
/* 创建iframe */
function creatIframe(url, title) {
	var topWindow = $(window.parent.document);
	var iframeLength = topWindow.find("iframe");
	var tabs_content = topWindow.find(".page-tabs-content").find(".J_menuTab");
	for ( var i = 0; i < iframeLength.length; i++) {
		if (iframeLength.length - 1 == i) {

		} else {
			var tab = tabs_content[i + 1];
			$(tab).toggleClass('active');
			$(tab).prop('class', 'J_menuTab');
			// removeClass(tab,"active");
		}
		var cur = iframeLength[i];// 隐藏ifarme
		cur.style = "display:none";

	}
	var ms = new Date().getTime();
	var show_nav = topWindow.find('.page-tabs-content').append(
			' <a href="javascript:;" class="J_menuTab active" data-id="' + url
					+ '">' + title + ' <i class="fa fa-times-circle"></i></a>');
	var iframe = '<iframe class="J_iframe" name="iframe' + ms
			+ '" width="100%" height="100%" src="' + url
			+ '" frameborder="0" data-id="' + url + '" seamless=""></iframe>';
	topWindow.find("#content-main").append(iframe);
}

function guid() {
	var guid36= 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
    var guid32 = guid36.replace( /-/g , '' )
    return guid32;
}
function disableFormItem(){
	$("input").attr("readOnly","readOnly");
	$(".disableBtn").attr("disabled","disabled");
	$(".disableBtn").css('display','none');
	$("textArea").attr("disabled","disabled");
	$('.layui-edge').css('display','none');
	$('dl').css('display','none')
	$('td').removeAttr('data-edit');
}

function decimal(num,v){
	var vv = Math.pow(10,v);
	return Math.round(num*vv)/vv;
}

function checkMust(){
	var result={};
	var flag=true;
	var list=[];
	var  msg='';
	$(".must").each(function(index,row){
		var doc =$(this);
		var val=doc.val();
		var name=doc.parent("div").prev("label").html();
		var arr=name.split("</i>");
		name=arr[arr.length-1];
		if(val==null || val==''){
			msg+=name+'不能为空！';
			flag=false;
		}
	});
	result.msg=msg;
	result.flag=flag;
	return result;
}

/**
 * 日期加减天数
 * @param date
 * @param days
 * @returns
 */
function c_addDate(date,days){ 
	var d;
	if(date == null || date == ""){
		d = new Date();
	}else{
		d=new Date(date);
	}
	d.setDate(d.getDate()+days);
	var month=d.getMonth()+1;
	var day = d.getDate();
	if(month<10){
		month = "0"+month;
	}
	if(day<10){
		day = "0"+day;
	} 
	var val = d.getFullYear()+"-"+month+"-"+day;
	return val;
}

//跳到详情
function tuoGo(url,detailName,tableId){
	var iframe = document.createElement('iframe');
	iframe.setAttribute('tableId',tableId);
	iframe.setAttribute('src',url);
	iframe.setAttribute('class',detailName);
	$(".layui-show", parent.document).append(iframe);
	$(".layui-show", parent.document).find('.larryms-iframe').css('display','none');
}
//跳到列表
function tuoBack(detailName,searchBtn){
	if(detailName.indexOf('.') == -1){
		detailName = '.'+detailName;
	}
	$(".layui-show", parent.document).find('.larryms-iframe').css('display','');
//	parent.$(".layui-show .larryms-iframe")[0].contentWindow.$(searchBtn).click();
	var tableId = $(".layui-show", parent.document).find('.larryms-iframe ~ iframe').attr('tableId');
	parent.$(".layui-show .larryms-iframe")[0].contentWindow.layui.table.reload(tableId);
	$(".layui-show", parent.document).find(detailName).remove();
}
/**
 * 根据ID进行数据的删除
 * @param originalArr
 * @param delArr
 */
function reomveArrById (originalArr, delArr) {
  for (var i = originalArr.length - 1; i >= 0; i--) {
    var row = originalArr[i]
    for (var j = delArr.length - 1; j >= 0; j--) {
      var delRow = delArr[j]
      if (row.id === delRow.id) {
        originalArr.splice(i, 1)
      }
    }
  }
}