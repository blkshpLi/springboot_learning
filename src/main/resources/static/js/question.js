if (typeof jQuery === 'undefined') {
    throw new Error('main\'s JavaScript requires jQuery')
}

/**
 * 提交回复
 */
function comment() {
    var questionId = $("#question_id").val();
    var content = $("#content").val();
    comment2target(questionId, 1, content);
}

function reply(btn) {
    var id = btn.getAttribute("data-id");
    var content = $("#input-"+id).val();
    comment2target(id, 2, content)
}


function comment2target(targetId, type, content) {
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
            "content": content
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
    var commentBox = $("#comment-"+id);
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

                $.each(result.data , function (index , comment) {

                    var media_left = "<div class='media-left'>"
                        + "<a href='#'>"
                        + "<img class='media-object image-rounded' "
                        + "src=" + comment.user.avatarUrl + "></a>"
                        + "</div>"

                    var media_body = "<div class='media-body'>"
                        + "<a href='#' class='media-heading'>"
                        + comment.user.name +"</a><br>"
                        + "<span>" + comment.content + "</span><br>"
                        + "<div class='reply-info'>"
                        + "<span class='time'>" + assertTime(comment.gmtCreate) + "</span>"
                        + "<span class='glyphicon glyphicon-thumbs-up like'>"
                        + "<span class='liked-count'> " + comment.likeCount + "</span></span>"
                        + "<span class='reply'>回复</span>"
                        + "</div></div><hr class='index-divider'>"

                    //创建
                    var media = $("<div/>",{
                        "class" : "media"
                    });

                    media.append(media_left);
                    media.append(media_body)
                    commentBox.prepend(media);
                });
            }

        });
    }


    //切换对应的二级评论展开状态
    $("#comment-"+id).toggleClass("in");
    console.log(id);
}

$(function() {
    $(".comment-box").find(".media").each(function(){
        var span = $(this).find(".comment-date");
        var date = span.text();
        var newDate = assertTime(date);
        span.text(newDate);
    });
});