$(function () {
    loadLikeStatus();
});

function loadLikeStatus() {
    let data = $('#data');
    let itemId = data.attr('data-itemId');
    let getLikeStatusUrl = data.attr('data-getLikeStatusUrl');
    $.ajax({
        url: getLikeStatusUrl,
        type: 'get',
        data: {itemId: itemId},
        success: function (likeStatus) {
            renderLike(likeStatus)
        }
    });
}

function renderLike(likeStatus) {
    let data = $('#data');
    let itemId = parseInt(data.attr('data-itemId'));
    let performLikeUrl = data.attr('data-performLikeUrl');
    let text = `${likeStatus.liked ? "Unlike" : "Like"} (${likeStatus.totalCount})`;
    let likeBtn = $('#like');
    likeBtn.text(text);
    if (likeStatus.auth) {
        likeBtn.one('click', (function () {
            $.ajax({
                url: performLikeUrl,
                type: 'post',
                data: {itemId: itemId, setLike: !likeStatus.liked},
                success: () => {
                    loadLikeStatus();
                }
            });
        }));
    }
}