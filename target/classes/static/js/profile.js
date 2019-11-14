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
                    if (result.code == 200){
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


$(function () {
    $(".row .media .message-trash").click(function () {
        var notification = $(this).parent();
        var id = notification.attr("data-id");
        $.get(
            "/profile/replies/del/" + id,
            function (result) {
                if (result.code == 200){
                    window.location.reload();
                } else {
                    notification.remove();
                }
                console.log(result);
            }
        )
    });
});