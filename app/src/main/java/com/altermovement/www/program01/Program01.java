package com.altermovement.www.program01;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MotionEvent;

import java.util.Arrays;

public class Program01 extends AppCompatActivity {

    private TextView bpm_tap;       // TEXT FIELD TAP BPM

    // TAP BPM CALCULATION VARIABLES

    private static final int MAX_WAIT = 2000;
    private long mPreviousBeat;

    // ARRAY FOR LAST 8 TAPS

    private long[] mLastBeats = new long[8];
    private int mCurrentBeat;

    // TAP BPM CALCULATION VARIABLES

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program01);

    // CONVERSION BUTTONS

    final Button button = (Button) findViewById(R.id.button);
    final Button calc_reset = (Button) findViewById(R.id.calc_reset);

    // CONVERSION TEXT INPUT OUTPUT FIELDS

    final EditText field1 = (EditText) findViewById(R.id.field1);
    final EditText field2 = (EditText) findViewById(R.id.field2);
    final TextView change = (TextView) findViewById(R.id.change);

    // RESET BUTTON

        calc_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                field1.setText("");
                field2.setText("");
                change.setText(R.string.pitch1);
                change.setTextColor(Color.WHITE);
            }

        });

    // CONVERSION BUTTON

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                // CHECK FOR EMPTY FIELDS
                // RETURNS ERROR IF FOUND

                if ( (field1.getText().length()==0) || (field2.getText().length()==0) ) {

                    String error = "ERROR !!!";
                    change.setText(error);
                    change.setTextColor(Color.RED);

                }   else {

                    double bpm1;
                    double bpm2;
                    double bpmc;

                    bpm1 = Double.parseDouble(field1.getText().toString());
                    bpm2 = Double.parseDouble(field2.getText().toString());

                    bpmc = ((bpm2 / bpm1) - 1) * 100;

                    if (bpmc > 0) {
                        change.setText(String.format("+ %.3f", bpmc));
                        change.setTextColor(Color.GREEN);   // POSITIVE NUMBERS = GREEN

                    } else {
                        change.setText(String.format("%.3f", bpmc));
                        change.setTextColor(Color.RED);     // NEGATIVE NUMBERS = RED
                    }

                    }

            }

        });

        // TAP BPM CALCULATION

        bpm_tap = (TextView) findViewById(R.id.bpm_tap);        // TAP BPM OUTPUT FIELD

        final Button reset = (Button) findViewById(R.id.reset); // TAP BPM RESET BUTTON

        // RESET OUTPUT FIELD AND CREATE NEW EMPTY ARRAY FOR 8 TAPS

        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mLastBeats = new long[8];
                mCurrentBeat = 0;
                mPreviousBeat = 0;
                bpm_tap.setText("TAP BPM");

            }

        });

        // TAP BPM BUTTON FUNCTION

        findViewById(R.id.tap).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (mPreviousBeat > 0) {
                        long beat = System.currentTimeMillis();
                        long diff = beat - mPreviousBeat;

                        // reset
                        if (diff >= MAX_WAIT) {
                            Arrays.fill(mLastBeats, 0);
                        }

                        else {
                            mLastBeats[mCurrentBeat] = diff;
                            mCurrentBeat++;
                            if (mCurrentBeat >= mLastBeats.length)
                                mCurrentBeat = 0;
                        }

                    }

                    // OUTPUTS AVERAGE BPM FROM TAP WHEN FIRST ENTRY IN ARRAY IS > 0

                    if (mLastBeats[0] > 0) {
                        long avgDiff = average(mLastBeats);
                        final int bpmt = (int) (60000 / avgDiff);
                        bpm_tap.setText(String.format("%d BPM", bpmt));
                    }

                    mPreviousBeat = System.currentTimeMillis();

                    return true;
                }

                return false;


            }

            // TAP BPM AVERAGE CALCULATION

            private long average(long[] values) {
                long sum = 0;
                long count = 0;
                for (long v : values) {
                    if (v > 0) {
                        sum += v;
                        count++;
                    }
                }

                return sum / count;
            }

        });

    }

}
