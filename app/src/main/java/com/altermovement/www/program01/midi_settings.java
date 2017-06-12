package com.altermovement.www.program01;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import de.humatic.nmj.NMJConfig;
import de.humatic.nmj.NMJSystemListener;
import de.humatic.nmj.NetworkMidiInput;
import de.humatic.nmj.NetworkMidiListener;
import de.humatic.nmj.NetworkMidiOutput;
import de.humatic.nmj.NetworkMidiSystem;

public class midi_settings extends Activity implements NMJSystemListener, NetworkMidiListener {

    private static final String TAG = "midi_settings";
    private NetworkMidiInput midiIn;
    private NetworkMidiOutput midiOut;
    private byte[] myNote = new byte[]{(byte) 0x90, (byte) 0x24, 0};
    private NetworkMidiSystem nmjs;
    private int lastPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        NMJConfig.addSystemListener(this);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getLong("firstRun", -1) == -1) {

            try {



                NMJConfig.edit(this, true);

                int chIdx = NMJConfig.addChannel();


                NMJConfig.setMode(chIdx, NMJConfig.RTP);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("firstRun", System.currentTimeMillis());
                editor.commit();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            setContentView(R.layout.midi_settings);


            final NetworkMidiListener ml = this;

            Spinner spinner = (Spinner) findViewById(R.id.spinner3);
            int numCh = NMJConfig.getNumChannels();
            String[] channelArray = new String[numCh];
            for (int i = 0; i < numCh; i++) channelArray[i] = NMJConfig.getName(i);
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, channelArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                            if (position == lastPosition) return;
                            lastPosition = position;

                            Runnable r = new Runnable() {
                                public void run() {

                                    try {
                                        midiIn.close(null);
                                    } catch (NullPointerException ne) {
                                    }
                                    try {
                                        midiOut.close(null);
                                    } catch (NullPointerException ne) {
                                    }

                                    try {
                                        midiIn = nmjs.openInput(position, ml);
                                    } catch (Exception e) {}
                                    try {
                                        midiOut = nmjs.openOutput(position, ml);
                                    } catch (Exception e) {
                                        Message msg = Message.obtain();
                                        msg.what = 1;
                                    }
                                }
                            };
                            new Thread(r).start();
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


        }



    }

    @Override
    public void systemChanged(int i, int i1, int i2) {

    }

    @Override
    public void systemError(int i, int i1, String s) {

    }

    @Override
    public void midiReceived(int i, int i1, byte[] bytes, long l) {

    }
}
