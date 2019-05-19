function createCalendar(yyyy,mm){
	$('.timebar').html(yyyy+'年'+(mm+1)+'月');

	$(".cont_day").empty();
	var number = getNumber(yyyy,mm); 
	var numberbef = getNumber(yyyy,mm-1);
	var day = getDay(yyyy,mm);  
    createDay(number,day,numberbef);

	$('.sent').on("click",function(event){
		$('.cont_day_div').removeClass('now');
		if($(event.currentTarget).hasClass('now')){
			$(event.currentTarget).removeClass('now');	
		}else{
			$(event.currentTarget).addClass('now');		
		}
	});
}
//根据年月求天数  
 function getNumber(yyyy,mm){  
    var number=31;  
    switch (parseInt(mm))  
    {  
    	case -1:
        case 0:  
        case 2:  
        case 4:  
        case 6:  
        case 7:  
        case 9:  
        case 11:  
            number = 31;  
            break;  
        case 3:  
        case 5:  
        case 8:  
        case 10:  
            number = 30;  
            break;  
        default:  
            if(yyyy%400==0||(yyyy%4==0&&yyyy%100!=0)){  
                number = 29;  
            }else{  
                number = 28;  
            }  
    }  
    return number;  
}	
//根据年月判断该月第一天是星期几  
function getDay(yyyy,mm){  
    var firstdate = new Date(yyyy,mm,1);  
    return firstdate.getDay();  
}
//审核状态
var statue = ['已发','审核','确认','','','','','','','','已发','审核','确认','','','','','','','','已发','审核','确认','','','','','','',''];
//生成日历天数
function createDay(number,day,numberbef){
	var html = '';
	if((number+day)>35){
		var j = 43;
	}else{
		var j = 36;
	}
	for(var i=1;i<j;i++){
	    var weekendClass='';
	    if(i%7<=1){
	       weekendClass=" weekend";
	    }
		if(i>day&&i<=number+day){
				html +='<div class="cont_day_div sent"><span class="clickspan">'+(i-day)+'</span><span class="manday">排产计划</span><input class="project" type="text" placeholder="请输入"></input></div>';
		}else if(i>number+day){
			html +='<div class="cont_day_div sent1"><span class="clickspan">'+(i-number-day)+'</span><span class="manday"></span><span class="project"></span></div>';
		}else{
			html +='<div class="cont_day_div sent1"><span class="clickspan">'+(numberbef-(day-i))+'</span><span class="manday"></span><span class="project"></span></div>';
		}
    } 
	$(".cont_day").append(html);
	if(j<43){
		$('.cont_day_div').addClass('cont_day_div5');
	}
}
//日期提示
function title(){
	var week = ['Su','Mo','Tu','We','Th','Fr','Sa'];
	var html = '';
	for(var i=0;i<week.length;i++){
		html +='<div class="cont_week_div">'+week[i]+'</div>';
	}
	$(".cont_week").append(html);
}; 
title();
$('.zuo').on('click',function(event){
	report.mm = report.mm - 1;
		if(report.mm==-1){
			report.yyyy = report.yyyy-1;
			report.mm = 11;
		}
		comingTime(report.yyyy,report.mm);
})
$('.you').on('click',function(event){
	report.mm = report.mm + 1;
		if(report.mm==12){
			report.yyyy = report.yyyy+1;
			report.mm = 0;
		}
		comingTime(report.yyyy,report.mm);
})

function comingTime(yyyy,mm){
	var date = new Date();
	$(".cont_day").empty();
	$('.timebar').html(yyyy+'年'+(mm+1)+'月');
	month5 = mm + 1;
	var number = getNumber(yyyy,mm); 
	var numberbef = getNumber(yyyy,mm-1);
    var day = getDay(yyyy,mm);  
    createDay(number,day,numberbef);
    if(yyyy == date.getFullYear()){
    	if((mm) == date.getMonth()){
    		nowColor();
    	}
    }
}

	