package com.altermovement.www.program01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import static com.altermovement.www.program01.R.layout.splash;

public class splashscreen extends Activity {

	private static final String LOG_TAG = splashscreen.class.getSimpleName();

	@Override
	public void onBackPressed() {
		// Do Here what ever you want do on back press;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate: savedInstanceState = " + (savedInstanceState == null ? "NULL" : "Not NULL"));

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(splash);

		Handler handler = new Handler();

		final ImageView background = (ImageView) findViewById(R.id.background);
		background.setImageResource(R.drawable.bg);
		background.setAlpha(0f);
		background.animate()
				.alpha(1f)
				.setDuration(500);

		final ImageView logo = (ImageView) findViewById(R.id.logo);
		logo.setImageResource(R.drawable.alterlogo);
		logo.setAlpha(0f);
		logo.setScaleX(2);
		logo.setScaleY(2);

		final ImageView appname = (ImageView) findViewById(R.id.appname);
		appname.setImageResource(R.drawable.logot);
		appname.setAlpha(0f);
		appname.setScaleX(2);
		appname.setScaleY(2);

		Runnable logofi = new Runnable() {

			@Override
			public void run() {

				logo.setAlpha(0f);
				logo.animate()
						.alpha(1f)
						.setDuration(500)
						.x(0)
						.scaleX(1)
						.scaleY(1);
			}
		};


		Runnable logofo = new Runnable() {

			@Override
			public void run() {
				logo.setAlpha(1f);
				logo.animate()
						.alpha(0f)
						.setDuration(1000);
			}
		};

		Runnable apptextfi = new Runnable() {

			@Override
			public void run() {
				appname.setAlpha(0f);
				appname.animate()
						.alpha(1f)
						.setDuration(500)
						.scaleX(1)
						.scaleY(1);
			}
		};

		Runnable apptextfo = new Runnable() {

			@Override
			public void run() {
				appname.setAlpha(1f);
				appname.animate()
						.alpha(0f)
						.setDuration(1000);
				appname.setAlpha(1f);
				background.animate()
						.alpha(0f)
						.setDuration(500);
			}
		};

		Runnable finishsplash = new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(getApplicationContext(), mainmenu.class));
				finish();
			}
		};

		handler.postDelayed(logofi, 1000);
		handler.postDelayed(logofo, 3000);
		handler.postDelayed(apptextfi, 4000);
		handler.postDelayed(apptextfo, 6000);
		handler.postDelayed(finishsplash, 7000);

	}
}
