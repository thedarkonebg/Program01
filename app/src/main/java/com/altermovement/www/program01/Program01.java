package com.altermovement.www.program01;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class Program01 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program01);

    final Button button = (Button) findViewById(R.id.button);
    final EditText field1 = (EditText) findViewById(R.id.field1);
    final EditText field2 = (EditText) findViewById(R.id.field2);
    final TextView change = (TextView) findViewById(R.id.change);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                double bpm1;
                double bpm2;
                double bpmc;

                bpm1 = Double.parseDouble(field1.getText().toString());
                bpm2 = Double.parseDouble(field2.getText().toString());

                bpmc = ((bpm2 / bpm1)-1)*100;


                if (bpmc > 0) {
                    change.setText(String.format("+ %.3f", bpmc));
                    change.setTextColor(Color.GREEN);

                }
                else {
                    change.setText(String.format("%.3f", bpmc));
                    change.setTextColor(Color.RED);
                }

            }





        });


    }
}
