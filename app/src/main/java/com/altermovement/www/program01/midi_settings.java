package com.altermovement.www.program01;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import de.humatic.nmj.NMJConfig;
import de.humatic.nmj.NMJSystemListener;
import de.humatic.nmj.NetworkMidiInput;
import de.humatic.nmj.NetworkMidiListener;
import de.humatic.nmj.NetworkMidiOutput;
import de.humatic.nmj.NetworkMidiSystem;

public class midi_settings extends Activity implements NMJSystemListener, NetworkMidiListener {

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

        NMJConfig.addSystemListener(this);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getLong("firstRun", -1) == -1) {

            try {
                NMJConfig.resetAll();
                NMJConfig.edit(this, true);
                int chIdx = NMJConfig.addChannel();
                NMJConfig.setMode(chIdx, NMJConfig.RTPA);
                NMJConfig.setIO(1, NMJConfig.OUT);
                NMJConfig.setIP(1, "192.168.1.4");
                NMJConfig.setPort(1, 5004);
                NMJConfig.setName(1, "MIDI Ch.1");

                SharedPreferences.Editor editor = prefs.edit();

                NetworkMidiOutput midiout = nmjs.openOutput(1, this);
                midiout.sendMidi(new byte[]{(byte)0x80, 7, 0});
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        setContentView(R.layout.midi_settings);

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
