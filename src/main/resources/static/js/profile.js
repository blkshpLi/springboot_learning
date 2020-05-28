if (typeof jQuery === 'undefined') {
    throw new Error('main\'s JavaScript requires jQuery')
}

$(function () {
    $(".row .media").mouseover(function () {
        $(this).find(".options").show();
    }).mouseout(function () {
        $(this).find(".options").hide();
    })
});

/**
 * 删除问题
 */
$(function () {
    $(".row .media .option-remove").click(function () {

        var questionId = $(this).attr("data-id");
        var title = $("#question-title-"+questionId).text();
        $("#remove-confirm").find("p").text(title);

        $("#remove-confirm").modal({
            backdrop: true,
            keyboard: true,
            show: true
        });

        $("#remove-confirm .btn-remove").click(function () {
            $.post(
                "/profile/questions/remove",
                {"id" : questionId},
                function (result) {
                    if (result.code === 200){
                        window.location.reload();
                    } else {
                        alert(result.message);
                    }
                    console.log(result);
                }
            )
        });

    });
});

/**
 * 更新通知已读状态
 */
$(function (){
    var newMessage = $(".new-message");
    if(newMessage.text() !== ""){
        var isBlank = newMessage.find(".media").text();
        if (isBlank === "" && $(".pagination .active a").text() == 1){
            var noComment = "<div class='no-comment'>暂无新通知</div>";
            newMessage.append(noComment);
        } else{
            newMessage.find(".media:last").find("hr").remove();
            setTimeout(function () {
                $.post(
                    "/read",
                    function (result) {
                        if (result.code === 200){
                            $(".unread").remove();
                        } else {
                            alert(result.message);
                        }
                        console.log(result);
                    }
                );
            },2000);
        }
    }
});

/**
 * 删除消息通知
 */
$(function () {
    $(".row .media .message-trash").click(function () {
        var notification = $(this).parent();
        var id = notification.attr("data-id");
        $.get(
            "/profile/replies/del/" + id,
            function (result) {
                if (result.code === 200){
                    window.location.reload();
                } else {
                    notification.remove();
                }
                console.log(result);
            }
        )
    });
});