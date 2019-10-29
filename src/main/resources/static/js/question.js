if (typeof jQuery === 'undefined') {
    throw new Error('main\'s JavaScript requires jQuery')
}

function comment() {
    var questionId = $("#question_id").val();
    var content = $("#content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parentId": questionId,
            "type": 1,
            "content": content
        }),
        success: function (result) {
            if (result.code == 200){
                $("#content").hide();
            } else {
                alert(result.message);
            }
            console.log(result);
        }
    });
}