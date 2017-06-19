package com.altermovement.www.program01;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.github.mikephil.charting.charts.LineChart;


public class signalgen extends Activity implements View.OnClickListener {

    private int w;
    private static int FACTOR_VOL = 327;
    private int frequency;
    private int amp;
    private int mod;

    Waveform wave;
    EditText wavefrequency;
    SeekBar modulate;
    SeekBar volumeseek;
    ToggleButton toggle;
    RadioGroup radio;
    LineChart wavechart;


    @SuppressWarnings("ConstantConditions")
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } else {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

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
                        frequency = Integer.parseInt(wavefrequency.getText().toString());
                        wave.frequency = frequency;
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
                mod = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mod= modulate.getProgress();
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

        wave = new Waveform();
        wave.frequency = frequency;
        wave.mode = w;
        wave.amplitude = amp;
        wave.stop();
    }

    private void initializeView() {

        wavechart = (LineChart) findViewById(R.id.wavechart);
        wavefrequency = (EditText) findViewById(R.id.freq);
        modulate = (SeekBar) findViewById(R.id.modulate);
        radio = (RadioGroup) findViewById(R.id.Group);
        volumeseek = (SeekBar) findViewById(R.id.volseek);
        toggle = (ToggleButton) findViewById(R.id.toggle);

    }

    @Override
    public void onClick(View v) {

        mod = 0;
        amp = (volumeseek.getProgress() * FACTOR_VOL);
        frequency = Integer.parseInt(wavefrequency.getText().toString());
        boolean on = toggle.isChecked();
        if (on) {
            wave.frequency = frequency;
            wave.amplitude = amp;
            wave.mode = w;
            if (wave != null)
                wave.start();
        } else {
            wave.stop();
        }

    }
}


