package com.altermovement.www.program01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;


public class mainmenu extends Activity{
	@Override

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mainmenu);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		final ImageButton but1 = (ImageButton) findViewById(R.id.but1);
		final ImageButton but2 = (ImageButton) findViewById(R.id.but2);
		final ImageButton but3 = (ImageButton) findViewById(R.id.but3);
		final ImageButton but4 = (ImageButton) findViewById(R.id.but4);
		final ImageButton but5 = (ImageButton) findViewById(R.id.but5);
		final ImageButton but6 = (ImageButton) findViewById(R.id.but6);

		but1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(getApplicationContext(), Program01.class));

			}
		});

		but2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//dasdsadasdsad
			}
		});

		but3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//dasdsadasdsad
			}
		});

		but4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//dasdsadasdsad
			}
		});

		but5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//dasdsadasdsad
			}
		});

		but6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//dasdsadasdsad
			}
		});

	}
}
