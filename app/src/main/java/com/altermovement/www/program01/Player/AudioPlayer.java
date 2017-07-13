package com.altermovement.www.program01.Player;


import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.altermovement.www.program01.DJPlayer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;

import java.io.File;
import java.util.Formatter;
import java.util.Locale;

public class AudioPlayer {
    
    private Handler handler;
    private static final String TAG = "AudioPlayer";

    /**
     *          PLAYER CONTROL BOOLEANS AND TRACK STATE IDENTIFIER
     * **/

    private boolean isPlaying = false;      // TRACK PLAY / STOP BOOLEAN // DEFAULT: FALSE
    private int i;                          // Use in PLAY/CUE logic to get string trackstate[i] and identify track state
    private String[] trackstate = new String[]{"PLAY", "STOP", "LOOP"};

    /**
     *          INITIALIZE EXO PLAYER AND EVENT LISTENERS
     * **/

    private SimpleExoPlayer exoPlayer;
    private ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            Log.i(TAG,"onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.i(TAG,"onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.i(TAG,"onLoadingChanged");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.i(TAG,"onPlayerStateChanged: playWhenReady = " + String.valueOf(playWhenReady) + " playbackState = " + playbackState);

            switch (playbackState){

                case ExoPlayer.STATE_ENDED:
                    Log.i(TAG,"Playback ended!");
                    //Stop playback and return to start position
                    setPlayPause();
                    exoPlayer.seekTo(0);
                    break;

                case ExoPlayer.STATE_READY:
                    Log.i(TAG,"ExoPlayer ready! pos: "+exoPlayer.getCurrentPosition()
                            +" max: "+stringForTime((int)exoPlayer.getDuration()));
                    setProgress();
                    break;

                case ExoPlayer.STATE_BUFFERING:
                    Log.i(TAG,"Playback buffering!");
                    break;

                case ExoPlayer.STATE_IDLE:
                    Log.i(TAG,"ExoPlayer idle!");
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.i(TAG,"onPlaybackError: "+error.getMessage());
        }

        @Override
        public void onPositionDiscontinuity() {
            Log.i(TAG,"onPositionDiscontinuity");
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.i(TAG, String.valueOf(playbackParameters));
        }
    };

    /**
     *          LOAD TRACK TO EXO PLAYER
     * **/

    private void prepareExoPlayerFromFileUri(Uri uri){

        context =

        exoPlayer = ExoPlayerFactory.newSimpleInstance(DJPlayer.getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
        exoPlayer.addListener(eventListener);

        DataSpec dataSpec = new DataSpec(uri);
        final FileDataSource fileDataSource = new FileDataSource();
        try {
            fileDataSource.open(dataSpec);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return fileDataSource;
            }
        };
        MediaSource audioSource = new ExtractorMediaSource(fileDataSource.getUri(),
                factory, new DefaultExtractorsFactory(), null, null);

        exoPlayer.prepare(audioSource);

    }

    /**
     *          GENERATE TRACK CURRENT AND TOTAL TIME
     * **/

    private String stringForTime(int timeMs) {

        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds =  timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     *          SET DJPLAYER PROGRESS BAR TO TRACK CURRENT PROGRESS
     * **/

    private void setProgress() {

        if(handler == null)handler = new Handler();

        //Make sure you update Seekbar on UI thread

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && isPlaying) {
                    int mCurrentPosition = (int) exoPlayer.getCurrentPosition() / 1000;
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    /**
     *          AUDIO PLAYER PUBLIC INTERFACE
     *          => GET PARAMS || GET ARGS
     *          => SET PARAMS || SET ARGS
     *          <= RETURN DISPLAY DATA
     *
     *          PLAY BUTTON LOGIC [SET TRACK STATE]
     * **/

    public void setPlayPause(){
        switch(trackstate[i]){

            case "PLAY":
                i = 2;
                break;

            case "STOP":
                i = 0;
                break;

            case "LOOP":
                i = 0;
                break;
        }
    }

    /**
     *          CUE BUTTON LOGIC [SET TRACK STATE]
     * **/

    public void setCue(){
        switch(trackstate[i]){

            case "PLAY":
                i = 1;
                break;

            case "STOP":
                i = 1;
                break;

            case "LOOP":
                i = 1;
                break;
        }
    }

    /**
     *          => GET FILE URI FROM FILE SELECTOR IN DJPLAYER ACTIVITY
     *
     *          => LOAD FILE FROM URI [FROM EXTERNAL DRIVE]
     *
     *          SUPPORTED FORMATS = { MP3 ; OGG ; WAV ; AAC }
     * **/

    public void setAudiofile(File file){
        prepareExoPlayerFromFileUri(Uri.fromFile(file));
    }

}