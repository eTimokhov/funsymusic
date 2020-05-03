$(function () {
    loadComments();
    let addCommentBtn = $('#addCommentBtn');

    addCommentBtn.click(addComment);
    addCommentBtn.attr('disabled', true);
    $('#commentTextarea').keyup(function () {
        if ($(this).val().trim().length !== 0)
            addCommentBtn.attr('disabled', false);
        else
            addCommentBtn.attr('disabled', true);
    })
});

function addComment() {
    let pageData = $('#data');
    let addCommentUrl = pageData.attr('data-addCommentUrl');
    $.ajax({
        type: "POST",
        url: addCommentUrl,
        contentType: "application/json",
        data: JSON.stringify(getFormData($('#addCommentForm'))),
        // data: JSON.stringify("{trackId: 4, muDog: 'lllala'}"),
        success: function () {
            loadComments();
        }
    });
}

function loadComments() {
    let pageData = $('#data');
    let getCommentsUrl = pageData.attr('data-getCommentsUrl');
    let trackId = pageData.attr('data-trackId');
    $.ajax({
        url: getCommentsUrl,
        type: 'get',
        data: {"trackId": trackId},
        success: function (comments) {
            updateComments(comments)
        }
    });
}

function updateComments(comments) {
    let pageData = $('#data');
    let commentsSection = $("#commentSection");
    commentsSection.empty();
    for (const comment of comments) {
        commentsSection.append(
            $('<li/>', {})
                .append(
                    $('<img/>', {src: `/images/${comment.user.image}_small.jpg`})
                )
                .append('by ')
                .append(
                    $('<a/>', {href: `/user/${comment.user.username}`, text: comment.user.username})
                )
                .append(` at ${comment.commentDate}: `)
                .append(comment.text)
        )
    }
}

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}