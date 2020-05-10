$(function () {


    let data = $('#data');

    /*
        Initializes AmplitudeJS
    */
    Amplitude.init({
        "songs": [
            {
                "name": data.attr('data-trackName'),
                "artist": data.attr('data-trackArtist'),
                "url": data.attr('data-trackUrl'),
                "cover_art_url": "/images/funsymusic.png"
            }
        ],
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
    document.getElementById('large-visualization').style.height = document.getElementById('album-art').offsetWidth + 'px';
});