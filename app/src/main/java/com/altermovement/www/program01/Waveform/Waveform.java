package com.altermovement.www.program01.Waveform;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by The Dark One on 15.6.2017 г..
 */

public class Waveform implements Runnable{

    private Thread thread;
    public int frequency;
    public int mode;
    public int amplitude;
    public int givephase = 0;
    public short giveamp = 0;
    public int mod = 0;
    public int ampmod = 0;

    public Waveform(){

        frequency = 100;
        mode = 2;
        amplitude = 32767;

    }

    private synchronized void setWave() {

        int rate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        int minSize = AudioTrack.getMinBufferSize(rate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        int sizes[] = {1024, 2048, 4096, 8192, 16384, 32768};
        int size = 0;

        for (int s : sizes)
        {
            if (s > minSize)
            {
                size = s;
                break;
            }
        }

        AudioTrack myWave = new AudioTrack(AudioManager.STREAM_MUSIC, rate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, size, AudioTrack.MODE_STREAM);

        int state = myWave.getState();

        if (state != AudioTrack.STATE_INITIALIZED)
        {
            myWave.release();
            return;
        }

        myWave.play();

        short samples[] = new short[size];
        short carrier[] = new short[size];
        double doublepi = 8. * Math.atan(1.0);
        double phase = 0.0;
        double phasemod = 0.0;

        while (thread != null) {


            for (int i = 0; i < samples.length; i++) {

                if (phase < Math.PI) {
                    phase += doublepi * frequency / rate;
                    phasemod += doublepi * mod / rate;
                }
                else {
                    phase += (doublepi * frequency / rate) - (2 * Math.PI);
                    phasemod += (doublepi * mod / rate) - (2 * Math.PI);
                }

                switch (mode) {

                    case 1: // SQUARE WAVE

                        if(mod == 0) {
                            samples[i] = square(amplitude, phase);
                        } else {
                            carrier[i] = square(amplitude, phase);
                            samples[i] = modulate(carrier[i], phase, phasemod);
                        }

                        break;

                    case 2: // SINE WAVE

                        if(mod == 0) {
                            samples[i] = sine(amplitude, phase);
                        } else {
                            carrier[i] = sine(amplitude, phase);
                            samples[i] = modulate(carrier[i], phase, phasemod);
                        }

                        break;

                    case 3: // TRIANGLE WAVE

                        if(mod == 0) {
                            samples[i] = saw(amplitude, phase);
                        } else {
                            carrier[i] = saw(amplitude, phase);
                            samples[i] = modulate(carrier[i], phase, phasemod);
                        }

                        break;

                }
            }

            myWave.write(samples, 0, samples.length);
        }
            myWave.stop();
            myWave.release();
    }

    public void start() {

        amplitude = 0;
        thread = new Thread(this, "myWave");
        thread.start();

    }

    public void stop() {

        Thread t = thread;
        thread = null;
        // Wait for the thread to exit
        while (t != null && t.isAlive())
        Thread.yield();
    }

    @Override
    public void run()
    {
        setWave();
    }

    private short modulate(short am, double ph, double fph){
        return (short) (am * Math.cos( ph + Math.sin(fph) ));
    }

    private short square(int am, double ph) {
        return (short) Math.round(am * Math.signum(Math.sin(ph)));
    }

    private short sine(int am, double ph) {
        return (short) Math.round(am * Math.sin(ph));
    }

    private short saw(int am, double ph) {
        return (short) Math.round(am * Math.round((ph) / Math.PI));
    }

    public short getAmplitude() {
        return giveamp;
    }

}

