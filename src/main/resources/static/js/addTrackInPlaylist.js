let playlistsWithTrack = $('#playlistsWithTrack');
let pageData = $('#data');
let trackId = pageData.attr('data-trackId');
playlistsWithTrack.hide();
$('#playlistsWithTrackToggle').click(function(){
    playlistsWithTrack.toggle();
});

loadPlaylistsInfo();

function addOrRemoveTrack(action, playlistId) {
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
    let getPlaylistsUrl = pageData.attr('data-getPlaylistsUrl');
    $.ajax({
        url: getPlaylistsUrl,
        type: 'get',
        data: { "trackId": trackId },
        success: function(playlists) {
            updatePlaylists(playlists)
        }
    });
}

function updatePlaylists(playlists) {
    $("#playlistsWithTrack").empty()
    let addTrackUrl = pageData.attr('data-addTrackUrl');
    let removeTrackUrl = pageData.attr('data-removeTrackUrl');
    for (const playlist of playlists) {
        let action = playlist.trackInPlaylist ? removeTrackUrl : addTrackUrl;
        let text = `${playlist.playlistName} ${playlist.trackInPlaylist ? 'X' : '+'}`;
        $( "<li/>", {
            text: text,
            click: function() {
                addOrRemoveTrack(action, playlist.playlistId);
            }
        }).appendTo('#playlistsWithTrack');
    }
}