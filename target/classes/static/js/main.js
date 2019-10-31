if (typeof jQuery === 'undefined') {
    throw new Error('main\'s JavaScript requires jQuery')
}

/*
   导航栏切换active属性
 */
$(function () {
    $(".navbar-nav").find("li").each(function () {
        var a = $(this).find("a:first")[0];
        if ($(a).attr("href") === location.pathname) {
            $(this).addClass("active");
        } else {
            $(this).removeClass("active");
        }
    });
});

/*
   优化时间显示
 */
$(function() {

    $("div .row").find(".media").each(function(){
        var span = $(this).find(".time-ago");
        var date = span.text();
        var newDate = assertTime(date);
        span.text(newDate);
    });

});

function assertTime(dateTimeStamp) {
    var min = 1000 * 60;
    var hour = min * 60;
    var day = hour * 24;
    if (isNaN(parseInt(dateTimeStamp))) {
        console.log('未从标签中获取到有效时间#timeAgo');
        return "--:--";
    }
    var now = new Date().getTime();
    var diff = now - dateTimeStamp;
    if (diff < 0) {
        console.log('系统时间或者服务器提供的时间有误！');
        return '刚刚';
    }
    var countDay = diff / day;
    var countHour = diff / hour;
    var countMin = diff / min;
    if (parseInt(countDay) > 30) {
        return dateFormat(dateTimeStamp);
    }else if(parseInt(countDay) >= 1){
        return parseInt(countDay) + '天前';
    }else if(parseInt(countHour) >= 1){
        return parseInt(countHour) + '小时前';
    }else if(parseInt(countMin) >= 1){
        return parseInt(countMin) + '分钟前';
    }else{
        return '刚刚';
    }
}
/*
  日期格式化
 */
function dateFormat(dts){
    var jsonDate = new Date(parseInt(dts));
    Date.prototype.format = function(format){
        var o = {
            "y+" : this.getFullYear(),
            "M+" : this.getMonth() + 1,
            "d+" : this.getDate(),
            "h+" : this.getHours(),
            "m+" : this.getMinutes(),
            "s+" : this.getSeconds(),
        }

        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1,
                (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for ( var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1,
                    RegExp.$1.length == 1 ? o[k] : ("00" + o[k]) .substr(("" + o[k]).length));
            }
        }
        return format;
    }
    var newDate = jsonDate.format("yyyy-MM-dd hh:mm:ss");
    return newDate;
}