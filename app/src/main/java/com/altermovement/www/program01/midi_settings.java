package com.altermovement.www.program01;

import android.app.Activity;
import android.os.Bundle;
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
