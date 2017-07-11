package com.altermovement.www.program01;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.maxproj.simplewaveform.SimpleWaveform;

import java.io.File;

public class DJPlayer extends Activity {

    // USER INTERFACE //

    ImageButton     button_play;
    ImageButton     button_cue;

    Button          button_bpm;
    Button          button_load;
    Button          button_pitchplus;
    Button          button_pitchminus;
    Button          button_range;

    SeekBar         seekbar_pitch;

    ImageView       image_disk;

    ProgressBar     progressbar_time;

    TextView        text_time;
    TextView        text_pitch;
    TextView        text_artist;
    TextView        text_track;

    SimpleWaveform  waveform;

    String          filename;

    // CHOOSE FILE DIALOG WINDOW //

    OpenFile        filedialog;

    // AUDIO MODULES //

    AudioManager audioManager;
    AudioTrack audioTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);

        setContentView(R.layout.player_layout);
        initializeView();

        button_load.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                filedialog.setFileListener(new OpenFile.FileSelectedListener() {
                    @Override public void fileSelected(final File file) {
                        filename = file.getAbsolutePath();
                        Log.d("File", filename);
                    }
                });
                filedialog.setExtension("mp3");
                filedialog.showDialog();
            }
        });
    }

    private void initializeView() {

        // DISPLAY //

        progressbar_time = (ProgressBar) findViewById(R.id.progressbar_time);

        text_time = (TextView) findViewById(R.id.text_time);
        text_pitch = (TextView) findViewById(R.id.text_pitch);
        text_artist = (TextView) findViewById(R.id.text_artist);
        text_track = (TextView) findViewById(R.id.text_track);

        // CONTROLS //

        button_play = (ImageButton) findViewById(R.id.button_play);
        button_cue = (ImageButton) findViewById(R.id.button_cue);

        button_bpm = (Button) findViewById(R.id.button_bpm);
        button_load = (Button) findViewById(R.id.button_load);
        button_pitchplus = (Button) findViewById(R.id.button_pitchplus);
        button_pitchminus = (Button) findViewById(R.id.button_pitchminus);
        button_range = (Button) findViewById(R.id.button_range);

        seekbar_pitch = (SeekBar) findViewById(R.id.seekbar_pitch);

        image_disk = (ImageView) findViewById(R.id.image_disk);

        waveform = (SimpleWaveform) findViewById(R.id.simplewaveform);

        filedialog = new OpenFile(this);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent ob = new Intent(DJPlayer.this, mainmenu.class);
                        startActivity(ob);
                        DJPlayer.this.finish();
                    }
                }).setNegativeButton("No", null).show();
    }


}
