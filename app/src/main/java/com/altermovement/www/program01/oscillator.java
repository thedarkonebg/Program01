package com.altermovement.www.program01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.altermovement.www.program01.Waveform.Oscillators;
import com.jjoe64.graphview.GraphView;


public class oscillator extends Activity implements View.OnClickListener {

    private static int FACTOR_VOL = 327;

    private int amp_main;
    private int wavefreq_min = 2;

    public static GraphView graphview;

    Oscillators wave;

    SeekBar wavefrequency_a;
    SeekBar wavefrequency_b;
    SeekBar wavefrequency_c;

    SeekBar volumeseek_a;
    SeekBar volumeseek_b;
    SeekBar volumeseek_c;

    SeekBar wavemode_a;
    SeekBar wavemode_b;
    SeekBar wavemode_c;

    SeekBar volumeseek_main;

    SeekBar modulate_freq;
    SeekBar modulate_amp;

    ToggleButton startstop;


    public static AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        setContentView(R.layout.oscillator_layout);
        initializeView();

        amp_main = (volumeseek_main.getProgress() * FACTOR_VOL) + 1;

        graphview = (GraphView) findViewById(R.id.graph);

        // WAVEFORM A B C FREQUENCY

        wavefrequency_a.setOnClickListener(this);
        wavefrequency_a.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.frequency_a = wavefrequency_a.getProgress() * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.frequency_a = wavefrequency_a.getProgress() * 10;
            }
        });

        wavefrequency_b.setOnClickListener(this);
        wavefrequency_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.frequency_b = wavefreq_min + (wavefrequency_b.getProgress() * 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.frequency_b = wavefreq_min + (wavefrequency_b.getProgress() * 10);
            }
        });

        wavefrequency_c.setOnClickListener(this);
        wavefrequency_c.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.frequency_c = wavefreq_min + (wavefrequency_c.getProgress() * 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.frequency_c = wavefreq_min + (wavefrequency_c.getProgress() * 10);
            }
        });

        // WAVEFORM A B C AMPLITUDE

        volumeseek_a.setOnClickListener(this);
        volumeseek_a.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.amplitude_a = volumeseek_a.getProgress() * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.amplitude_a = volumeseek_a.getProgress() * 10;
            }
        });

        volumeseek_b.setOnClickListener(this);
        volumeseek_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.amplitude_b = volumeseek_b.getProgress() * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.amplitude_b = volumeseek_b.getProgress() * 10;
            }
        });

        volumeseek_c.setOnClickListener(this);
        volumeseek_c.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.amplitude_c = volumeseek_c.getProgress() * 10;
        }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.amplitude_a = volumeseek_c.getProgress() * 10;
            }
        });

        // WAVEFORM A B C MODE

        wavemode_a.setProgress(1);
        wavemode_a.setOnClickListener(this);
        wavemode_a.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.mode_a = (wavemode_a.getProgress() + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.mode_a = (wavemode_a.getProgress() + 1);
            }
        });

        wavemode_b.setProgress(1);
        wavemode_b.setOnClickListener(this);
        wavemode_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.mode_b = (wavemode_b.getProgress() + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.mode_b = (wavemode_b.getProgress() + 1);
            }
        });

        wavemode_c.setProgress(1);
        wavemode_c.setOnClickListener(this);
        wavemode_c.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.mode_c = wavemode_c.getProgress() + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.mode_c = wavemode_c.getProgress() + 1;
            }
        });

        // MODULATE FREQUENCY AND AMPLITUDE

        modulate_amp.setOnClickListener(this);
        modulate_amp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.mod_amp = modulate_amp.getProgress() * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.mod_amp = modulate_amp.getProgress() * 10;
            }
        });

        modulate_freq.setOnClickListener(this);
        modulate_freq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.mod = modulate_freq.getProgress() * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.mod = modulate_freq.getProgress() * 10;
            }
        });

        // MAIN WAVE AMPLITUDE

        volumeseek_main.setProgress(100);
        volumeseek_main.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.amplitude = (volumeseek_main.getProgress() * FACTOR_VOL) + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.amplitude = (volumeseek_main.getProgress() * FACTOR_VOL) + 1;
            }
        });

        // START / STOP TOGGLE

        startstop.setOnClickListener(this);

    }

    private void initializeView() {

        audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);

        wavefrequency_a = (SeekBar) findViewById(R.id.freqseek_a);
        wavefrequency_b = (SeekBar) findViewById(R.id.freqseek_b);
        wavefrequency_c = (SeekBar) findViewById(R.id.freqseek_c);

        volumeseek_a = (SeekBar) findViewById(R.id.ampseek_a);
        volumeseek_b = (SeekBar) findViewById(R.id.ampseek_b);
        volumeseek_c = (SeekBar) findViewById(R.id.ampseek_c);

        wavemode_a = (SeekBar) findViewById(R.id.modeseek_a);
        wavemode_b = (SeekBar) findViewById(R.id.modeseek_b);
        wavemode_c = (SeekBar) findViewById(R.id.modeseek_c);

        volumeseek_main = (SeekBar) findViewById(R.id.ampseek_main);

        modulate_freq = (SeekBar) findViewById(R.id.modamp_seek);
        modulate_amp = (SeekBar) findViewById(R.id.modfreq_seek);

        startstop = (ToggleButton) findViewById(R.id.startstop);
        wave = new Oscillators();
    }

    @Override
    public void onClick(View v) {

        wave.frequency_a = wavefreq_min + (wavefrequency_a.getProgress() * 10);
        wave.frequency_b = wavefreq_min + (wavefrequency_b.getProgress() * 10);
        wave.frequency_c = wavefreq_min + (wavefrequency_c.getProgress() * 10);

        wave.amplitude_a = volumeseek_a.getProgress() * 10;
        wave.amplitude_b = volumeseek_b.getProgress() * 10;
        wave.amplitude_c = volumeseek_c.getProgress() * 10;

        wave.amplitude = amp_main;
        volumeseek_main.setProgress(100);

        wave.mod = modulate_freq.getProgress() * 10;
        wave.mod_amp = modulate_amp.getProgress() * 10;

        boolean on = startstop.isChecked();
        if (on) {
            if (wave != null)
                fadein();
        }

        if (!on) {
                fadeout();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wave != null)
            wave.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (wave != null)
            wave.stop();
    }

    public void fadeout(){
        int targetVol = 0;
        int STEP_DOWN=2;
        int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int nextVol=currentVol;
        // fade music gently
        while(currentVol > targetVol) {
            try {
                Thread.sleep(25);
            }catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol - STEP_DOWN,0);
            currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        wave.stop();
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, nextVol, 0);

    }

    public void fadein(){

        int initialVol = 0;
        int STEP_UP=2;
        int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        wave.start();

        wave.frequency_a = wavefreq_min + (wavefrequency_a.getProgress() * 10);
        wave.frequency_b = wavefreq_min + (wavefrequency_b.getProgress() * 10);
        wave.frequency_c = wavefreq_min + (wavefrequency_c.getProgress() * 10);

        wave.amplitude_a = volumeseek_a.getProgress() * 10;
        wave.amplitude_b = volumeseek_b.getProgress() * 10;
        wave.amplitude_c = volumeseek_c.getProgress() * 10;

        wave.amplitude = amp_main;
        volumeseek_main.setProgress(100);

        wave.mod = modulate_freq.getProgress() * 10;
        wave.mod_amp = modulate_amp.getProgress() * 10;

        while(initialVol < currentVol) {
            try {
                Thread.sleep(25);
            }catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, initialVol + STEP_UP,0);
            initialVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent ob = new Intent(oscillator.this, mainmenu.class);
        startActivity(ob);
        oscillator.this.finish();
    }




}


