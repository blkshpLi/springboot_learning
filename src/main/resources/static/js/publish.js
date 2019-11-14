if (typeof jQuery === 'undefined') {
    throw new Error('main\'s JavaScript requires jQuery')
}

/**
 * MD编辑器
 */
$(function() {
    editormd("md-editor", {
        width: "100%",
        height: 500,
        delay: 1000,
        placeholder:"快来完善你的问题吧！！",
        path: "/js/lib/",
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL: "/file/upload",
        toolbarIcons: "self"
    });
});


/**
 * 切换下拉菜单的开关状态
 */
$(function () {
    $("#btn-add-tag").click(function () {
        $(".add-tag").toggleClass("open");
    });
});

/**
 * 点击别处时关闭下拉菜单
 */
$(function () {
    $("body").click(function (e) {
        var addTag = $(".add-tag");
        if(!addTag.is(e.target) && addTag.has(e.target).length === 0){
            $(".add-tag").removeClass("open");
        }
    });
});

/**
 * 下拉菜单默认选择第一个分类
 */
$(function () {
    $("#tag-category-0").addClass("active");
    $("#tag-panel-0").addClass("active");
});

/**
 * 点击标签时：
 * 1.检查是否重复
 * 2.检查是否已经选择了5个标签
 * 3.取消标签时，显示选择标签按键
 */
$(function () {
    $(".tag-list a").click(function () {
        var repeated = false;
        var text = $(this).text();
        $(".add-tag").find(".btn-tag").each(function () {
            var selected = $(this).contents()[0].nodeValue;
            if(text === selected){
                var tag = $(this);
                tag.addClass("warning");
                tag.fadeTo(1200,0.5);
                setTimeout(function () {
                    tag.removeClass("warning");
                    tag.fadeTo(100,1);
                },1200);
                repeated = true;
                return false;
            }
        });
        if(!repeated){
            $("#btn-add-tag").before("<a href='javascript:;' class='btn btn-tag' id='selected-tag'>" + text + "<span class='del-tag'>×</span></a>");
        }
        if($(".add-tag").find(".btn-tag").length === 5){
            $(".add-tag").removeClass("open");
            $("#btn-add-tag").hide();
        }
    });
    $(".add-tag").on("click",".del-tag",function () {
        $(this).parent().remove();
        $("#btn-add-tag").show();
    });
});

/**
 * submit的同时填充tag输入框中的value
 */
$(function () {
    $(".btn-publish").click(function () {
        var tags = $("#tag");
        tags.val("");
        var text = "";
        $(".add-tag").find(".btn-tag").each(function () {
            var selected = $(this).contents()[0].nodeValue;
            text = text + selected + ";";
        });
        if(text.length < 1){
            alert("请至少选择一个标签");
            return false;
        }
        tags.val(text.substr(0,text.length - 1));
    });
});

/**
 * 进入发布页面时获取tag输入框中的value
 */
$(function () {
    var tagValue = $("#tag").val();
    if(tagValue != ""){
        var tags = tagValue.split(";");
        for(var i = 0; i < tags.length; i++){
            $("#btn-add-tag").before("<a href='javascript:;' class='btn btn-tag' id='selected-tag'>" + tags[i] + "<span class='del-tag'>×</span></a>");
        }
    }
});