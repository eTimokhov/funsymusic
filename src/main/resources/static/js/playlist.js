$(function() {
    let savePlaylistBtn = $('#savePlaylistBtn');
    let cancelEditingBtn = $('#cancelEditingBtn');
    let trackList = $('#trackList');

    savePlaylistBtn.hide();
    cancelEditingBtn.hide();
    loadTracks();

    trackList.sortable();
    trackList.sortable("disable");

    $('#editPlaylistBtn').click(activateEdit);
    savePlaylistBtn.click(sendPlaylist);
    cancelEditingBtn.click(cancelEditing);
});


function loadTracks() {
    $('#trackList').empty();
    let data = $('#data');
    let playlistId = data.attr('data-playlistId');
    let getTracksUrl = data.attr('data-getTracksUrl');
    $.ajax({
        url: getTracksUrl,
        type: 'get',
        data: {"playlistId": playlistId},
        success: function (tracks) {
            renderTracks(tracks)
        }
    });
}

function renderTracks(tracks) {
    let trackList = $('#trackList');
    let trackUrl = $('#data').attr('data-trackUrl');
    for (const track of tracks) {
        trackList.append(
            $('<li/>', {
                'data-trackId': track.id,
                'class': 'list-group-item d-flex justify-content-between align-items-center'
            }).append(
                $('<a/>', {href: 'trackUrlPlaceholder', text: track.name})
            )
        )
    }
}

function activateEdit() {
    $('#trackList').sortable("enable");
    let listItems = $('#trackList li');
    listItems.addClass('list-group-item-info');
    //add spans (x) to delete tracks from playlist
    listItems.each(function () {
        let li = $(this);
        $('<span/>', {
            'class': 'badge badge-danger badge-pill',
            text: 'x',
            click: function() {
                if (li.attr('data-deleted') === 'true') {
                    li.addClass('list-group-item-info').removeClass('list-group-item-secondary');
                    li.removeAttr('data-deleted');
                } else {
                    li.addClass('list-group-item-secondary').removeClass('list-group-item-info');
                    li.attr('data-deleted', 'true');
                }
            }
        }).appendTo(li);
    })

    $('#editPlaylistBtn').hide();
    $('#cancelEditingBtn').show();
    $('#savePlaylistBtn').show();
}

function cancelEditing() {
    $('#trackList').sortable("disable");
    $('#cancelEditingBtn').hide();
    $('#savePlaylistBtn').hide();
    $('#editPlaylistBtn').show();
    loadTracks();
}

function sendPlaylist() {
    let data = $('#data');
    let savePlaylistUrl = data.attr('data-savePlaylistUrl');
    let playlistId = data.attr('data-playlistId')
    let updatePlaylistDto = {
        playlistId: parseInt(playlistId),
        trackIds: []
    };
    let newListItems = $('#trackList li[data-deleted!="true"]');
    newListItems.each(function() {
        let li = $(this);
        updatePlaylistDto.trackIds.push(parseInt(li.attr('data-trackId')));
    })

    $.ajax({
        url: savePlaylistUrl,
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(updatePlaylistDto),
        success: () => {
            cancelEditing();
        }
    });
}