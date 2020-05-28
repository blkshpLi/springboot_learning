if (typeof jQuery === 'undefined') {
    throw new Error('main\'s JavaScript requires jQuery')
}

/**
 * 自动填充搜索框(废案)

$(function(){
    var url = location.search;
    if(url.indexOf("?") != -1){
        var str = url.split("=")[1];
        $("#keyword").val(str);
    }
});
 */