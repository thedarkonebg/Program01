package com.altermovement.www.program01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.altermovement.www.program01.Waveform.Waveform;

import java.util.logging.Logger;


public class signalgen extends Activity implements View.OnClickListener {

    private int w = 2;
    private int freq = 100;
    private final static Logger LOGGER = Logger.getLogger(signalgen.class.getName());
    int modul;

    Waveform wave = new Waveform();
    EditText wavefrequency;
    SeekBar modulate;
    ToggleButton toggle;
    RadioGroup radio;

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
    }

    private void initializeView() {

        wavefrequency = (EditText) findViewById(R.id.freq);
        modulate = (SeekBar) findViewById(R.id.modulate);

        radio = (RadioGroup) findViewById(R.id.Group);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            int frequency = Integer.parseInt(wavefrequency.getText().toString());
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rsquare) {
                    w = 1;
                    wave.setWave(frequency, w);
                    Toast.makeText(getApplicationContext(), "Square wave", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.rsine) {
                    w = 2;
                    wave.setWave(frequency, w);
                    Toast.makeText(getApplicationContext(), "Sine wave", Toast.LENGTH_SHORT).show();
                } else {
                    w = 3;
                    wave.setWave(frequency, w);
                    Toast.makeText(getApplicationContext(), "Sawtooth wave", Toast.LENGTH_SHORT).show();
                }

            }

        });

        toggle = (ToggleButton) findViewById(R.id.toggle);
        toggle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        modul = 0;
        int frequency = Integer.parseInt(wavefrequency.getText().toString());
        wave.setWave(frequency, w);
        boolean on = toggle.isChecked();

        if (on) {
            wave.start();
            wave.pause();
            wave.start();
        } else {
            wave.stp();
        }

    }
}


