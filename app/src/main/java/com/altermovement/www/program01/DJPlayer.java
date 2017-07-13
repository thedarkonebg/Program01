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
import android.widget.Toast;

import com.altermovement.www.program01.Controls.OpenFile;
import com.altermovement.www.program01.Player.AudioPlayer;
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

    OpenFile filedialog;

    // AUDIO PLAYER //

    AudioPlayer audioPlayer;

    // AUDIO MODULES //

    AudioManager audioManager;
    AudioTrack audioTrack;

    // CONTROL VARIABLES //

    private double pitch_coeff;
    private int sw_case = 0;
    private double pitch_factor = 0.02;
    private double temp_coeff_max;

    // INFO TOAST //

    Toast toast;
    Toast toast1;
    Toast toast2;

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
                        audioPlayer.setAudiofile(file);
                        Log.d("File", filename);
                    }
                });
                filedialog.setExtension("mp3");
                filedialog.showDialog();
            }
        });

        // PLAY AND CUE //

        button_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                audioPlayer.setPlayPause();

            }
        });

        button_cue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                audioPlayer.setCue();

            }
        });



        // PITCH CONTROLS //

        seekbar_pitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                temp_coeff_max = seekbar_pitch.getMax() * pitch_factor;

                int temp_coeff = seekbar_pitch.getProgress();
                if (temp_coeff == 0){
                    pitch_coeff = - temp_coeff_max * 0.5;
                } else {
                    pitch_coeff = (temp_coeff * pitch_factor) - temp_coeff_max * 0.5;
                }
                text_pitch.setText(String.format("%.2f", pitch_coeff));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                temp_coeff_max = seekbar_pitch.getMax() * pitch_factor;
                double temp_coeff = seekbar_pitch.getProgress();
                if (temp_coeff == 0){
                    pitch_coeff = - temp_coeff_max * 0.5;
                } else {
                    pitch_coeff = (temp_coeff * pitch_factor) - temp_coeff_max * 0.5;
                }
                text_pitch.setText(String.format("%.2f", pitch_coeff));
            }
        });

        button_pitchplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekbar_pitch.setProgress(seekbar_pitch.getProgress() + 1);
            }
        });

        button_pitchminus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekbar_pitch.setProgress(seekbar_pitch.getProgress() - 1);
            }
        });

        // PITCH RANGE CHANGER //

        button_range.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int temp_progress;
                switch (sw_case) {

                    case 0:

                        temp_progress = seekbar_pitch.getProgress();
                        seekbar_pitch.setMax(400);
                        pitch_factor = 0.05;

                        seekbar_pitch.setProgress(temp_progress * 400 / 600);

                        temp_coeff_max = seekbar_pitch.getMax() * pitch_factor;
                        text_pitch.setText(String.format("%.2f", (seekbar_pitch.getProgress() * pitch_factor) - temp_coeff_max * 0.5));
                        toast = Toast.makeText(getApplicationContext(), "RANGE +/- 10 %", Toast.LENGTH_SHORT);

                        sw_case += 1;

                        if (toast2 == null) {
                            break;
                        }

                        toast2.cancel();
                        toast.show();
                        break;

                    case 1:

                        temp_progress = seekbar_pitch.getProgress();
                        seekbar_pitch.setMax(640);
                        pitch_factor = 0.05;

                        seekbar_pitch.setProgress(temp_progress * 640 / 400);

                        temp_coeff_max=(int)(seekbar_pitch.getMax() * pitch_factor);
                        text_pitch.setText(String.format("%.2f", (seekbar_pitch.getProgress() * pitch_factor) - temp_coeff_max * 0.5));
                        toast1 = Toast.makeText(getApplicationContext(), "RANGE +/- 16 %", Toast.LENGTH_SHORT);

                        sw_case += 1;

                        if (toast == null) {
                            break;
                        }

                        toast.cancel();
                        toast1.show();
                        break;

                    case 2:

                        temp_progress = seekbar_pitch.getProgress();
                        seekbar_pitch.setMax(600);
                        pitch_factor = 0.02;

                        seekbar_pitch.setProgress(temp_progress * 600 / 640);

                        temp_coeff_max=(int)(seekbar_pitch.getMax() * pitch_factor);
                        text_pitch.setText(String.format("%.2f", (seekbar_pitch.getProgress() * pitch_factor) - temp_coeff_max * 0.5));
                        toast2 = Toast.makeText(getApplicationContext(), "RANGE +/- 6 %", Toast.LENGTH_SHORT);

                        sw_case = 0;

                        if (toast1 == null) {
                            break;
                        }

                        toast1.cancel();
                        toast2.show();
                        break;
                }

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
        seekbar_pitch.setMax(600);
        seekbar_pitch.setProgress(300);

        image_disk = (ImageView) findViewById(R.id.image_disk);

        waveform = (SimpleWaveform) findViewById(R.id.simplewaveform);

        filedialog = new OpenFile(this);
        audioPlayer = new AudioPlayer();
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
