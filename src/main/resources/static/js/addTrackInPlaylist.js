$(function () {
    let playlistsWithTrack = $('#playlistsWithTrack');
    //if playlistsWithTrack (ul) wasn't rendered, then user is guest.
    if (!playlistsWithTrack.length) {
        return;
    }

    $('#playlistsWithTrack').hide();

    loadPlaylistsInfo();

    $('#playlistsWithTrackToggle').click(function () {
        $('#playlistsWithTrack').toggle();
    });
});

function addOrRemoveTrack(action, playlistId) {
    let trackId = $('#data').attr('data-trackId');
    $.ajax({
        url: action,
        type: 'post',
        contentType: 'application/json',
        data:
            JSON.stringify({
                "trackId": trackId,
                "playlistId": playlistId
            })
        ,
        success: () => {
            loadPlaylistsInfo();
        }
    });

}

function loadPlaylistsInfo() {
    let pageData = $('#data');
    let getPlaylistsUrl = pageData.attr('data-getPlaylistsUrl');
    let trackId = pageData.attr('data-trackId');
    $.ajax({
        url: getPlaylistsUrl,
        type: 'get',
        data: {"trackId": trackId},
        success: function (playlists) {
            updatePlaylists(playlists)
        }
    });
}

function updatePlaylists(playlists) {
    let pageData = $('#data');
    $("#playlistsWithTrack").empty()
    let addTrackUrl = pageData.attr('data-addTrackUrl');
    let removeTrackUrl = pageData.attr('data-removeTrackUrl');
    let playlistsWithTracks = $('#playlistsWithTrack');
    for (const playlist of playlists) {
        let action = playlist.trackInPlaylist ? removeTrackUrl : addTrackUrl;
        let text = `${playlist.playlistName} ${playlist.trackInPlaylist ? 'X' : '+'}`;
        let badgeText = playlist.trackInPlaylist ? 'Delete': 'Add';
        let badgeClass = playlist.trackInPlaylist ? 'cursor-pointer badge badge-secondary' : 'cursor-pointer badge badge-primary';
        playlistsWithTracks.append(
            $("<li/>", {})
                .append(
                    $('<span/>', {text: playlist.playlistName})
                )
                .append(' ')
                .append(
                    $('<span/>', {
                        text: badgeText, 'class': badgeClass, click: function () {addOrRemoveTrack(action, playlist.playlistId);}
                    })
                )
        );
    }
}