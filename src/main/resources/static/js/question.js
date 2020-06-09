if (typeof jQuery === 'undefined') {
    throw new Error('main\'s JavaScript requires jQuery')
}

$(function() {
    editormd.markdownToHTML("md-view", {

    });
});

/**
 * 点赞
 */
$(function() {
    $(".btn-agree").click(function () {
        var btnAgree = $(this);
        if(!($(this).hasClass("disabled") || $(this).hasClass("agreed"))){
            var commentId = $(this).attr("data-id");
            $.post(
                "/agree/"+commentId,
                function (result) {
                    if (result.code == 200){
                        var agreeCount = btnAgree.find(".agree-count");
                        var count = agreeCount.text();
                        btnAgree.addClass("agreed");
                        agreeCount.text("");
                        agreeCount.text(++count);
                    } else {
                        alert(result.message);
                    }
                }
            );
        }
    });
});


/**
 * 提交回复
 */
function comment() {
    var questionId = $("#question_id").val();
    var content = $("#content").val();
    comment2target(questionId, 1, content, questionId);
}

/* 弃用的回复click事件
function reply(btn) {
    var questionId = $("#question_id").val();
    var id = btn.getAttribute("data-id");
    var content = $("#input-"+id).val();
    comment2target(id, 2, content, questionId);
}*/


function comment2target(targetId, type, content, replyTo) {
    if(!content) {
        alert("回复内容不能为空！！！");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "type": type,
            "content": content,
            "replyTo": replyTo
        }),
        success: function (result) {
            if (result.code == 200){
                window.location.reload();
            } else {
                alert(result.message);
            }
            console.log(result);
        }
    });
}

/**
 * 展开二级评论
 */
function collapseComment(btn) {

    var id = btn.getAttribute("data-id");
    var commentBox = $("#comment-"+id).find(".comment-box");

    //填充数据
    if(commentBox.find(".media").length == 0){
        $.getJSON("/comment/"+id , function (result) {

            if(result.data == null){

                var no_comment = "<div class='no-comment'>暂时还没有人回复TA</div>";

                var media = $("<div/>",{
                    "class" : "media"
                });

                media.append(no_comment);
                commentBox.prepend(media);

            }else {

                result.data.reverse();

                //拼接二级评论
                $.each(result.data , function (index , comment) {

                    var media_left = "<div class='media-left'>"
                        + "<a href='#'>"
                        + "<img class='media-object img-rounded'"
                        + "src=" + comment.user.avatarUrl + "></a>"
                        + "</div>"

                    var media_body = "<div class='media-body'>"
                        + "<a href='#' class='media-heading'>"
                        + comment.user.name +"</a>";

                    if(comment.replyTo != null){
                        media_body = media_body + " 回复 <a href='#'>@" + comment.replyTo.name + "</a> ："
                    }
                    media_body = media_body
                        + "<br><span>" + comment.content + "</span><br>"
                        + "<div class='reply-info'>"
                        + "<span class='time'>" + assertTime(comment.gmtCreate) + "</span>"
                        + "<span class='glyphicon glyphicon-thumbs-up like'>"
                        + "<span class='liked-count'> " + comment.likeCount + "</span></span>"
                        + "<span class='reply'>回复</span>"
                        + "</div></div><hr class='index-divider'>";

                    //创建
                    var media = $("<div/>",{
                        "class" : "media",
                        "data-id" : comment.id
                    });

                    media.append(media_left);
                    media.append(media_body);
                    commentBox.prepend(media);

                });
            }

        });
        //绑定回复评论的按钮
        commentBox.on("click",".btn-reply",function () {
            var parentId = $(this).closest(".reply-box").attr("data-parent-id");
            var replyTo = $(this).attr("data-reply-id");
            var content = $("#reply-content-" + replyTo).val();
            comment2target(parentId, 2, content, replyTo);
        });

        //检测用户是否登录
        var noLogin = $("body").find(".tip-login").text();
        var userName = $("#comment-"+id).find(".media-heading").first().text();

        //已登录点击回复图标显示回复框
        if(noLogin == ""){

            $("body").find(".reply-box").remove();

            //回复框
            var replyBox = "<div class='reply-box' data-parent-id='" + id + "'>" +
                "<div class='input-group'>" +
                "<input type='text' class='form-control' id='reply-content-" + id + "' placeholder='@ " + userName + " ：'>" +
                "<span class='input-group-btn'>" +
                "<button type='button' class='btn btn-reply' data-reply-id='" + id + "'>回复</button>" +
                "</span>" +
                "</div>" +
                "</div>";

            commentBox.append(replyBox);

            //绑定点赞按钮
            commentBox.on("click",".like",function () {
                var btnLike = $(this);
                var media = $(this).closest(".media");
                var id = media.attr("data-id");
                var userName = media.find(".media-heading").text();
                var currentUser = $("body").find(".user-name").find(".media-heading").text();
                if(userName != currentUser){
                    $.post(
                        "/agree/"+id,
                        function (result) {
                            if (result.code == 200){
                                var agreeCount = btnLike.find(".liked-count");
                                var count = agreeCount.text();
                                btnLike.addClass("liked");
                                agreeCount.text("");
                                agreeCount.text(" " + (++count));
                            } else {
                                alert(result.message);
                            }
                        }
                    );
                }
            });

            //绑定二级评论回复按钮
            commentBox.on("click",".reply",function () {
                var parentId = $(this).closest(".reply-box").attr("data-parent-id");
                var media = $(this).closest(".media");
                var id = media.attr("data-id");
                var userName = media.find(".media-heading").text();
                var reply = media.closest(".comment-box").find(".reply-box");
                var text = reply.text();
                if (text != "") {
                    reply.find(".form-control").attr("id" , "reply-content-" + id);
                    reply.find(".form-control").attr("placeholder" , "@ " + userName + " ：");
                    reply.find(".btn-reply").attr("data-reply-id" , id);
                } else {
                    $("body").find(".reply-box").remove();

                    //回复框
                    var replyBox = "<div class='reply-box' data-parent-id='" + parentId + "'>" +
                        "<div class='input-group'>" +
                        "<input type='text' class='form-control' id='reply-content-" + id + "' placeholder='@ " + userName + " ：'>" +
                        "<span class='input-group-btn'>" +
                        "<button type='button' class='btn btn-reply' data-reply-id='" + id + "'>回复</button>" +
                        "</span>" +
                        "</div>" +
                        "</div>";

                    commentBox.append(replyBox);
                }
                $("html, body").animate({
                    scrollTop: $(".reply-box").offset().top - 300
                },0);
            });

        }else{
            $("body").find(".reply-box").remove();
            //未登录不能回复
            commentBox.on("click",".like",function () {
                alert("登录后才能点赞哦！");
            });
            commentBox.on("click",".reply",function () {
                alert("请先登录再进行回复！");
            });
        }
    }



    //切换对应的二级评论展开状态
    commentBox.toggleClass("in");
    console.log(id + '\n' + userName);
}

$(function() {
    $(".comment-box").find(".media").each(function(){
        var span = $(this).find(".comment-date");
        var date = span.text();
        var newDate = assertTime(date);
        span.text(newDate);
    });
});

//
$(function () {
    $(".follow").click(function () {
        if($(this).hasClass("followed")){
            $(this).empty();
            $(this).prepend("<span class=\"glyphicon glyphicon-plus\" aria-hidden=\"true\"></span> 关注");
            $(this).removeClass("followed");
        }else{
            $(this).empty();
            $(this).prepend("已关注");
            $(this).addClass("followed");
        }
    });
    $(".collect").click(function () {
        if($(this).hasClass("collected")){
            $(this).empty();
            $(this).prepend("<span class=\"glyphicon glyphicon-star\" aria-hidden=\"true\"></span> 收藏");
            $(this).removeClass("collected");
        }else{
            $(this).empty();
            $(this).prepend("已收藏");
            $(this).addClass("collected");
        }
    });
});