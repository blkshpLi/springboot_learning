if (typeof jQuery === 'undefined') {
    throw new Error('main\'s JavaScript requires jQuery')
}
/**
 * 搜索框预查找
 */
$(function() {
    var cpLock = false;
    $('#keyword').on('compositionstart', function () {
        cpLock = true;
        console.log('暂不搜索');
    });
    $('#keyword').on('compositionend', function () {
        cpLock = false;
        console.log('搜索问题');
        preSearch();
    });
    $('#keyword').on('input', function () {
            if (!cpLock) {
                console.log('搜索字母');
                if($('#keyword').val().trim() != ""){
                    preSearch();
                }
            }
    });
});

function preSearch(){
    $.getJSON("/preSearch/"+ $('#keyword').val() , function (result) {
        if(result.data != null && result.data.length != 0){
            result.data.reverse();
            var ul = $(".search-menu");
            if(ul.find("li").length != 0){
                ul.empty();
            }
            $.each(result.data , function (index , question) {
                var li = "<li><a href='/question/" + question.id + "' title='" + question.title + "'>" + question.title + "</a></li>";
                ul.prepend(li);
            });
            if(!$(".pre-search .dropdown").hasClass("open")){
                $(".pre-search .dropdown").addClass("open");
            }
        }
    });
}

/**
 * 点击别处时关闭搜索下拉框
 */
$(function () {
    $("body").click(function (e) {
        var preSearch = $(".pre-search");
        if(!preSearch.is(e.target) && preSearch.has(e.target).length === 0){
            $(".pre-search .dropdown").removeClass("open");
        }
    });
});

/**
 * input获得焦点
 */
$(function () {
    $('#keyword').focus(function(){
        if($(".pre-search .dropdown").find("li").length != 0){
            $(".pre-search .dropdown").addClass("open");
        }
    });
});