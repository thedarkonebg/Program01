package com.altermovement.www.program01.Waveform;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

/**
 * Created by The Dark One on 15.6.2017 г..
 */

public class Waveform implements Runnable{

    private Thread thread;
    public int frequency;
    public int mode;
    public int amplitude;

    public Waveform(){
        frequency = 100;
        mode = 2;
        amplitude = 32767;
    }

    public synchronized void setWave() {

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

        int sampleCount = (int) ((double) rate / frequency);
        short samples[] = new short[sampleCount];
        double doublepi = 8. * Math.atan(1.0);
        double phase = 0.0;

        while (thread != null) {
            switch (mode) {

                case 1: // SQUARE WAVE

                    for (int i = 0; i < samples.length; i++) {
                        samples[i] = (short) Math.round(amplitude * Math.signum(Math.sin(phase)));
                        phase += doublepi * frequency / rate;
                    }
                    break;

                case 2: // SINE WAVE
                    for (int i = 0; i < samples.length; i++) {
                        samples[i] = (short) Math.round(amplitude * Math.sin(phase));
                        phase += doublepi * frequency / rate;
                    }
                    break;

                case 3: // TRIANGLE WAVE
                    for (int i = 0; i < samples.length; i++) {
                        samples[i] = (short) Math.round(amplitude * (Math.floor(phase) - phase + 0.5));
                        phase += doublepi * frequency / rate;
                    }
                    break;
            }

            myWave.write(samples, 0, samples.length);
        }

        myWave.stop();
        myWave.release();
    }

    public void start() {

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

    public void dataset(LineChart wavechart, int w) {

        LineData data = wavechart.getData();
        wavechart.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void run()
    {
        setWave();
    }
}

