

$(function() {

    $("div .row").find(".media").each(function(){
        var span = $(this).find('.index-description');
        var description = span.text();
        var strs = description.split("\n");
        var str = "";
        var pattern = /[\x21-\x2f\x3a-\x40\x5b-\x60\x7B-\x7F]/;
        for(var i = 0; i < strs.length; i++){
            if(!pattern.test(strs[i].substring(0,1))){
                str = str + strs[i];
                break;
            }
        }
        span.text(str);
    });
});