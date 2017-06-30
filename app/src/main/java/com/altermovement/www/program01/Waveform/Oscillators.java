package com.altermovement.www.program01.Waveform;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.altermovement.www.program01.oscillator;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Oscillators implements Runnable{

    private Thread thread;

    // WAVE GENERATOR PUBLIC VARIABLES

    public int frequency_a;
    public int mode_a = 1;
    public int amplitude_a;

    public int frequency_b;
    public int mode_b = 1;
    public int amplitude_b;

    public int frequency_c;
    public int mode_c = 1;
    public int amplitude_c;

    public int amplitude = 32700;

    public int mod = 0;
    public boolean mod_a;
    public boolean mod_b;
    public boolean mod_c;
    public int mod_amp;

    // DATAPOINT AND GRAPH SERIES

    private DataPoint[] datapp;
    private LineGraphSeries<DataPoint> waveseries;

    private synchronized void setWave() {

        // AUDIOTRACK INIT PARAMETERS

        int rate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        int minSize = AudioTrack.getMinBufferSize(rate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        int sizes[] = {1024, 2048, 4096, 8192, 16384, 32768};
        int size = 0;

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


        // MOD BOOLEAN INITIAL STATE = FALSE

        mod_a = true;
        mod_b = true;
        mod_c = true;

        // WAVEFORM DATA CARRIERS

        short[] samples_a = new short[size];
        short[] samples_b = new short[size];
        short[] samples_c = new short[size];

        // MAIN DATA CARRIER

        short[] samples = new short[size];

        // MODULATION DATA CARRIERS

        short carrier_a[] = new short[size];
        short carrier_b[] = new short[size];
        short carrier_c[] = new short[size];

        short carrier_mod[] = new short[size];

        double doublepi = 8. * Math.atan(1.0);

        // WAVEFORM A B C PHASE

        double phase_a = 0.0;
        double phase_b = 0.0;
        double phase_c = 0.0;

        // MODULATOR WAVE PHASE

        double phasemod = 0.0;

        // ITERATOR PARAMETERS

        int x = 0;
        int i;

        // GRAPH ARRAY DATA SIZE
        int factor = 32;
        int datasize = (size / factor);

        // GRAPH INIT PARAMETERS

        datapp = new DataPoint[datasize];

        oscillator.graphview.getViewport().setXAxisBoundsManual(true);
        oscillator.graphview.getViewport().setMinX(8);
        oscillator.graphview.getViewport().setMaxX(datasize/2 - 8);

        oscillator.graphview.getViewport().setYAxisBoundsManual(true);
        oscillator.graphview.getViewport().setMinY(-amplitude * 0.01);
        oscillator.graphview.getViewport().setMaxY(+amplitude * 0.01);
        oscillator.graphview.getViewport().setBackgroundColor(Color.rgb(255, 255, 255));

        waveseries = new LineGraphSeries<>();
        oscillator.graphview.addSeries(waveseries);

        // WAVEFORM GENERATOR MAIN THREAD

        while (thread != null) {

            for (i = 0; i < samples.length; i++) {

                // WAVE A PHASE

                if (phase_a < Math.PI) {
                    phase_a += doublepi * frequency_a / rate;
                } else {
                    phase_a += (doublepi * frequency_a / rate) - (2 * Math.PI);
                }

                // WAVE B PHASE

                if (phase_b < Math.PI) {
                    phase_b += doublepi * frequency_b / rate;
                } else {
                    phase_b += (doublepi * frequency_b / rate) - (2 * Math.PI);
                }

                // WAVE C PHASE

                if (phase_c < Math.PI) {
                    phase_c += doublepi * frequency_c / rate;
                } else {
                    phase_c += (doublepi * frequency_c / rate) - (2 * Math.PI);
                }

                // WAVE MODULATOR PHASE

                if (phasemod < Math.PI) {
                    phasemod += doublepi * mod / rate;
                } else {
                    phasemod += (doublepi * mod / rate) - (2 * Math.PI);
                }

                carrier_mod[i] = sine(mod_amp, phasemod);

                // GENERATE WAVEFORM A

                switch (mode_a) {

                    case 1: // SQUARE WAVE

                        if (mod == 0 && mod_a) {
                            samples_a[i] = square(amplitude_a, phase_a);
                        } else {
                            carrier_a[i] = square(amplitude_a, phase_a);
                            samples_a[i] = (short)(carrier_mod[i] * modulate(carrier_a[i], phase_a, phasemod));
                        }
                        break;

                    case 2: // SINE WAVE

                        if (mod == 0 && mod_a) {
                            samples_a[i] = sine(amplitude_a, phase_a);
                        } else {
                            carrier_a[i] = sine(amplitude_a, phase_a);
                            samples_a[i] = (short)(carrier_mod[i] * modulate(carrier_a[i], phase_a, phasemod));
                        }
                        break;

                    case 3: // TRIANGLE WAVE

                        if (mod == 0 && mod_a) {
                            samples_a[i] = saw(amplitude_a, phase_a);
                        } else {
                            carrier_a[i] = saw(amplitude_a, phase_a);
                            samples_a[i] = (short)(carrier_mod[i] * modulate(carrier_a[i], phase_a, phasemod));
                        }
                        break;
                }

                // GENERATE WAVEFORM B

                switch (mode_b) {

                    case 1: // SQUARE WAVE

                        if (mod == 0 && mod_b) {
                            samples_b[i] = square(amplitude_b, phase_b);
                        } else {
                            carrier_b[i] = square(amplitude_b, phase_b);
                            samples_b[i] = (short)(carrier_mod[i] * modulate(carrier_b[i], phase_b, phasemod));
                        }
                        break;

                    case 2: // SINE WAVE

                        if (mod == 0 && mod_b) {
                            samples_b[i] = sine(amplitude_b, phase_b);
                        } else {
                            carrier_b[i] = sine(amplitude_b, phase_b);
                            samples_b[i] = (short)(carrier_mod[i] * modulate(carrier_b[i], phase_b, phasemod));
                        }
                        break;

                    case 3: // TRIANGLE WAVE

                        if (mod == 0 && mod_b) {
                            samples_b[i] = saw(amplitude_b, phase_b);
                        } else {
                            carrier_b[i] = saw(amplitude_b, phase_b);
                            samples_b[i] = (short)(carrier_mod[i] * modulate(carrier_b[i], phase_b, phasemod));
                        }
                        break;
                }

                // GENERATE WAVEFORM C

                switch (mode_c) {

                    case 1: // SQUARE WAVE

                        if (mod == 0 && mod_c) {
                            samples_c[i] = square(amplitude_c, phase_c);
                        } else {
                            carrier_c[i] = square(amplitude_c, phase_c);
                            samples_c[i] = (short)(carrier_mod[i] * modulate(carrier_c[i], phase_c, phasemod));
                        }
                        break;

                    case 2: // SINE WAVE

                        if (mod == 0 && mod_c) {
                            samples_c[i] = sine(amplitude_c, phase_c);
                        } else {
                            carrier_c[i] = sine(amplitude_c, phase_c);
                            samples_c[i] = (short)(carrier_mod[i] * modulate(carrier_c[i], phase_c, phasemod));
                        }
                        break;

                    case 3: // TRIANGLE WAVE

                        if (mod == 0 && mod_c) {
                            samples_c[i] = saw(amplitude_c, phase_c);
                        } else {
                            carrier_c[i] = saw(amplitude_c, phase_c);
                            samples_c[i] = (short)(carrier_mod[i] * modulate(carrier_c[i], phase_c, phasemod));
                        }
                        break;
                }

                short temp = (short)(samples_a[i] + samples_b[i] + samples_c[i]);
                short three = 3;
                samples[i] = (short)((temp / three) * amplitude / 32767);


                // WAVE DATAPOINT CALCULATION AND ADD IN DATAPOINT ARRAY

                if (i == ((x * 16) - 1) || x == 0) {
                    int temp_graph = (int)Math.round(samples[i] * 0.01);
                    datapp[x] = new DataPoint(x, temp_graph);
                    x += 1;
                }
                if (x >= datasize) {
                    x = 0;
                }

            }

            myWave.write(samples, 0, samples.length);

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

        amplitude = 32767;
        thread = new Thread(this, "myWave");
        thread.start();

    }

    public void stop() {

        oscillator.graphview.removeAllSeries();
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

