package com.altermovement.www.program01;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.altermovement.www.program01.Waveform.Waveform;


public class signalgen extends Activity implements View.OnClickListener {

    private int w;
    private static int FACTOR_VOL = 327;
    private int frequency;
    private int amp;
    private int mod;
    Handler handler;

    Waveform wave;
    EditText wavefrequency;
    SeekBar modulate;
    SeekBar volumeseek;
    ToggleButton toggle;
    RadioGroup radio;

    public static AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        setContentView(R.layout.signalgen);
        initializeView();

        frequency = Integer.parseInt(wavefrequency.getText().toString());
        w = 2;
        amp = (volumeseek.getProgress() * FACTOR_VOL) + 1;
        mod = 100;


        wavefrequency.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    getApplicationContext();
                    frequency = Integer.parseInt(wavefrequency.getText().toString());
                    if (frequency < 10){
                        wavefrequency.setText("20");
                        wave.frequency = frequency;
                        wave.mode = w;
                        Toast.makeText(getApplicationContext(), "FREQUENCY TOO LOW", Toast.LENGTH_LONG).show();
                    } else if (frequency > 20000){
                        wavefrequency.setText("20000");
                        wave.frequency = frequency;
                        wave.mode = w;
                        Toast.makeText(getApplicationContext(), "FREQUENCY TOO LOW", Toast.LENGTH_LONG).show();
                    } else {
                        wave.frequency = Integer.parseInt(wavefrequency.getText().toString());
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(wavefrequency.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        //wave.mode = w;
                    }
                    return true;
                }
                return false;
            }
        });



        modulate.setOnClickListener(this);
        modulate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int mod = 100;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.mod = modulate.getProgress() * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.mod = modulate.getProgress() * 10;
            }
        });

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rsquare) {
                    w = 1;
                    wave.mode = 1;
                    Toast.makeText(getApplicationContext(), "Square wave", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.rsine) {
                    w = 2;
                    wave.mode = 2;
                    Toast.makeText(getApplicationContext(), "Sine wave", Toast.LENGTH_SHORT).show();
                } else {
                    w = 3;
                    wave.mode = 3;
                    Toast.makeText(getApplicationContext(), "Sawtooth wave", Toast.LENGTH_SHORT).show();
                }

            }

        });

        toggle.setOnClickListener(this);
        volumeseek.setProgress(100);
        volumeseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                wave.amplitude = (volumeseek.getProgress() * FACTOR_VOL) + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // FACTOR value / 100 = x / 32767 (x = amplitude)
                wave.amplitude = (volumeseek.getProgress() * FACTOR_VOL) + 1;
            }
        });

        //wave.stop();
    }

    private void initializeView() {

        audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        wavefrequency = (EditText) findViewById(R.id.freq);
        modulate = (SeekBar) findViewById(R.id.modulate);
        radio = (RadioGroup) findViewById(R.id.Group);
        volumeseek = (SeekBar) findViewById(R.id.volseek);
        toggle = (ToggleButton) findViewById(R.id.toggle);
        wave = new Waveform();

    }


    @Override
    public void onClick(View v) {

        mod = 0;
        frequency = Integer.parseInt(wavefrequency.getText().toString());
        boolean on = toggle.isChecked();
        if (on) {
            if (wave != null)
//                wave.start();
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

    // On Resume
    @Override
    protected void onResume()
    {
        super.onResume();
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
        wave.frequency = frequency;
        wave.mode = w;
        wave.amplitude = amp;

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
}


