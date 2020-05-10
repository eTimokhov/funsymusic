$(function() {
    let savePlaylistBtn = $('#savePlaylistBtn');
    let cancelEditingBtn = $('#cancelEditingBtn');
    let trackList = $('#trackList');

    savePlaylistBtn.hide();
    cancelEditingBtn.hide();
    trackList.hide();
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
            if (tracks.length == 0) {
                $('#editPlaylistBtn').hide();
            }
            renderTracks(tracks)
            initPlayer(tracks)
        }
    });
}

function renderTracks(tracks) {
    let trackList = $('#trackList');
    for (const track of tracks) {
        trackList.append(
            $('<li/>', {
                'data-trackId': track.id,
                'class': 'list-group-item d-flex justify-content-between align-items-center'
            }).append(
                $('<a/>', {href: `/track/${track.id}`, text: track.name})
            )
        )
    }
}

function activateEdit() {
    $('#trackList').show();
    $('#trackList').sortable("enable");
    let listItems = $('#trackList li');
    listItems.addClass('list-group-item-info');
    //add spans (x) to delete tracks from playlist
    listItems.each(function () {
        let li = $(this);
        $('<span/>', {
            'class': 'badge badge-danger badge-pill cursor-pointer',
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
    $('#trackList').hide();
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
            $('#trackList').hide();
        }
    });
}

function mapTrackToAmplSong(track) {
    return {
        name: track.name,
        artist: track.artist,
        url: `/media/audio/${track.id}`,
        cover_art_url: '/images/funsymusic.png'
    }
}

function initPlayer(tracks) {
    let songs = [];
    for (const track of tracks) {
        songs.push(mapTrackToAmplSong(track));
    }
    let amplitudePlaylist = $('#amplitude-right');
    amplitudePlaylist.empty();
    Amplitude.pause();
    for (const [i, track] of tracks.entries()) {
        amplitudePlaylist.append(
            $('<div/>', {'class': 'song amplitude-song-container amplitude-play-pause', 'data-amplitude-song-index': i}).append(
                $('<div/>', {'class': 'song-now-playing-icon-container'}).append(
                    $('<div/>', {'class': 'play-button-container'})
                ).append(
                    $('<img/>', {'class': 'now-playing', src: 'https://521dimensions.com/img/open-source/amplitudejs/blue-player/now-playing.svg'})
                )
            ).append(
                $('<div/>', {'class': 'song-meta-data'}).append(
                    $('<span/>', {'class': 'song-title', text: track.name})
                ).append(
                    $('<span/>', {'class': 'song-artist', text: track.artist})
                )
            ).append(
                $('<a/>', {href: `/track/${track.id}`, 'class': 'bandcamp-link', target: '_blank'}).append(
                    $('<img/>', {'class': 'bandcamp-grey', src: '/images/more.svg', width: '24px'})
                )
            ).append(
                $('<span/>', {'class': 'song-duration', text: convertSecondsToTimeFormat(track.length)})
            )
        )
    }
    /*
        Initializes AmplitudeJS
    */
    Amplitude.init({
        songs: songs,
        "callbacks": {
            'play': function () {
                document.getElementById('album-art').style.visibility = 'hidden';
                document.getElementById('large-visualization').style.visibility = 'visible';
            },

            'pause': function () {
                document.getElementById('album-art').style.visibility = 'visible';
                document.getElementById('large-visualization').style.visibility = 'hidden';
            }
        },
        waveforms: {
            sample_rate: 50
        }
    });


    //document.getElementById('large-visualization').style.height = document.getElementById('album-art').offsetWidth + 'px';
}
function str_pad_left(string,pad,length) {
    return (new Array(length+1).join(pad)+string).slice(-length);
}

function convertSecondsToTimeFormat(time) {
    let minutes = Math.floor(time / 60);
    let seconds = time - minutes * 60;
    return str_pad_left(minutes,'0',2)+':'+str_pad_left(seconds,'0',2);
}