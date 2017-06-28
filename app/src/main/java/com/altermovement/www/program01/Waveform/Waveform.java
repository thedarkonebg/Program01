package com.altermovement.www.program01.Waveform;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.altermovement.www.program01.signalgen;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by The Dark One on 15.6.2017 г..
 */

public class Waveform implements Runnable{

    private Thread thread;
    public int frequency;
    public int mode;
    public int amplitude;
    public int mod = 0;
    private short[] samples;
    int size;
    short[] graph;
    int x;
    int i;
    int datasize;

    private LineGraphSeries<DataPoint> waveseries;

    public Waveform(){

        frequency = 100;
        mode = 2;
        amplitude = 32767;

    }

    private synchronized void setWave() {


        int rate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        int minSize = AudioTrack.getMinBufferSize(rate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        int sizes[] = {1024, 2048, 4096, 8192, 16384, 32768};
        size = 0;

        for (int s : sizes) {
            if (s > minSize) {
                size = s;
                break;
            }
        }


        AudioTrack myWave = new AudioTrack(AudioManager.STREAM_MUSIC, rate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, size, AudioTrack.MODE_STREAM);

        int state = myWave.getState();

        if (state != AudioTrack.STATE_INITIALIZED) {
            myWave.release();
            return;
        }

        myWave.play();

        samples = new short[size];
        short carrier[] = new short[size];
        graph = new short[size];
        double doublepi = 8. * Math.atan(1.0);
        double phase = 0.0;
        double phasemod = 0.0;
        x = 0;
        i = 0;
        DataPoint datap;

        datasize = (size / 32);
        final DataPoint[] datapp = new DataPoint[datasize];

        signalgen.graphview.getViewport().setXAxisBoundsManual(true);
        signalgen.graphview.getViewport().setMinX(0);
        signalgen.graphview.getViewport().setMaxX(datasize / 2);

        signalgen.graphview.getViewport().setYAxisBoundsManual(true);
        signalgen.graphview.getViewport().setMinY(-amplitude);
        signalgen.graphview.getViewport().setMaxY(+amplitude);
        signalgen.graphview.getViewport().setScrollable(true);
        signalgen.graphview.getViewport().setBackgroundColor(Color.rgb(255, 255, 255));

        waveseries = new LineGraphSeries<>();
        signalgen.graphview.addSeries(waveseries);

        while (thread != null) {

            for (i = 0; i < samples.length; i++) {

                if (phase < Math.PI) {
                    phase += doublepi * frequency / rate;
                    phasemod += doublepi * mod / rate;
                } else {
                    phase += (doublepi * frequency / rate) - (2 * Math.PI);
                    phasemod += (doublepi * mod / rate) - (2 * Math.PI);
                }

                switch (mode) {

                    case 1: // SQUARE WAVE

                        if (mod == 0) {
                            samples[i] = square(amplitude, phase);
                        } else {
                            carrier[i] = square(amplitude, phase);
                            samples[i] = modulate(carrier[i], phase, phasemod);
                        }

                        break;

                    case 2: // SINE WAVE

                        if (mod == 0) {
                            samples[i] = sine(amplitude, phase);
                        } else {
                            carrier[i] = sine(amplitude, phase);
                            samples[i] = modulate(carrier[i], phase, phasemod);
                        }

                        break;

                    case 3: // TRIANGLE WAVE

                        if (mod == 0) {
                            samples[i] = saw(amplitude, phase);
                        } else {
                            carrier[i] = saw(amplitude, phase);
                            samples[i] = modulate(carrier[i], phase, phasemod);
                        }

                        break;
                }

//                if (x == 0) {
//                    datap = new DataPoint(x, samples[i]);
//                    waveseries.appendData(datap, true, datasize);
//                    x += 1;
//                }
//
//                if (i == (x * 16) - 1) {
//                    datap = new DataPoint(x, samples[i]);
//                    waveseries.appendData(datap, true, datasize);
//                    x += 1;
//                }
//
//                if (x >= datasize - 1) {
//                    x = 0;
//                }

                if (i == ((x * 32) - 1) || x == 0) {
                    datapp[x] = new DataPoint(x, samples[i]);
                    x += 1;
                }
                if (x >= datasize) {
                    x = 0;
                }
            }

            myWave.write(samples, 0, samples.length);
//            new gograph().execute(datapp);
            new Thread(new Runnable() {
                public void run() {
                    waveseries.resetData(datapp);
                }
            }).start();

        }


        myWave.stop();
        myWave.release();
    }

    public void start() {

        signalgen.graphview.removeAllSeries();
        amplitude = 32767;
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

//    class gograph extends AsyncTask<DataPoint[], Void, Void>{
//
//        @Override
//        protected Void doInBackground(DataPoint[]... dataPoint) {
//            waveseries.resetData(dataPoint[0]);
//            return null;
//        }
//    }

}

