package com.altermovement.www.program01.Waveform;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * Created by The Dark One on 15.6.2017 г..
 */

public class Waveform extends Thread{

    private static String TAG = "tag";
    private final int SAMPLE_RATE = 44100;
    private AudioTrack myWave;
    private int sampleCount;

    public Waveform(){
        int buffersize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        myWave = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffersize, AudioTrack.MODE_STATIC, AudioTrack.STATE_INITIALIZED);
        String aaa = String.valueOf(myWave.getState());
        Log.d(TAG, aaa);
    }

    public void setWave(int frequency, int mode) {

        sampleCount = (int) ((double) SAMPLE_RATE / frequency);
        short samples[] = new short[sampleCount];
        int amplitude = 32767;
        double doublepi = 8. * Math.atan(1.);
        double phase = 0.0;

        switch (mode) {

            case 1: // SQUARE WAVE

                for (int i = 0; i < sampleCount; i++) {
                    samples[i] = (short) (amplitude * Math.signum(Math.sin(phase)));
                    phase += doublepi * frequency / SAMPLE_RATE;
                }
                break;

            case 2: // SINE WAVE
                for (int i = 0; i < sampleCount; i++) {
                    samples[i] = (short) (amplitude * Math.sin(phase));
                    phase += doublepi * frequency / SAMPLE_RATE;
                }
                break;

            case 3: // TRIANGLE WAVE
                for (int i = 0; i < sampleCount; i++) {
                    samples[i] = (short) (amplitude * (Math.floor(phase) - phase + 0.5));
                    phase += doublepi * frequency / SAMPLE_RATE;
                }
                break;
        }

        myWave.write(samples, 0, sampleCount);
        String samp = String.valueOf(samples[5]);
        Log.d(TAG, samp);
    }

    public void start() {
        myWave.reloadStaticData();
        myWave.setLoopPoints(0, sampleCount, -1);
        myWave.play();
    }


    public void stp() {
        myWave.stop();
    }

    public void pause() {
        myWave.pause();
        myWave.reloadStaticData();
    }

//    public void reload() {
//        myWave.reloadStaticData();
//    }

}

