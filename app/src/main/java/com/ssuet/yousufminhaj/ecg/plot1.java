package com.ssuet.yousufminhaj.ecg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;

import com.androidplot.Plot;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.*;
//import com.macroyau.blue2serial.BluetoothDeviceListDialog;
//import com.macroyau.blue2serial.BluetoothSerial;
//import com.macroyau.blue2serial.BluetoothSerialListener;

import java.lang.ref.*;
import java.util.Set;

import static android.R.attr.data;

public class plot1 extends Activity {
    private XYPlot plot;
    // public BluetoothSerial bluetoothSerial;

    /**
     * Uses a separate thread to modulate redraw frequency.
     */

    private Redrawer redrawer;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot1);




        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);


        ECGModel ecgSeries = new ECGModel(2000, 200);
        // add a new series' to the xyplot:
        MyFadeFormatter formatter =new MyFadeFormatter(2000);
        formatter.setLegendIconEnabled(false);
        plot.addSeries(ecgSeries, formatter);
        plot.setRangeBoundaries(0, 10, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, 2000, BoundaryMode.FIXED);

        // reduce the number of range labels
        plot.setLinesPerRangeLabel(3);

        // start generating ecg data in the background:
        ecgSeries.start(new WeakReference<>(plot.getRenderer(AdvancedLineAndPointRenderer.class)));

        // set a redraw rate of 30hz and start immediately:
        redrawer = new Redrawer(plot, 30, true);




        /**
         * Special {@link AdvancedLineAndPointRenderer.Formatter} that draws a line
         * that fades over time.  Designed to be used in conjunction with a circular buffer model.
         */
    }









    public static class MyFadeFormatter extends AdvancedLineAndPointRenderer.Formatter {

        private int trailSize;

        public MyFadeFormatter(int trailSize) {
            this.trailSize = trailSize;
        }

        @Override
        public Paint getLinePaint(int thisIndex, int latestIndex, int seriesSize) {
            // offset from the latest index:
            int offset;
            if(thisIndex > latestIndex) {
                offset = latestIndex + (seriesSize - thisIndex);
            } else {
                offset =  latestIndex - thisIndex;
            }

            float scale = 255f / trailSize;
            int alpha = (int) (255 - (offset * scale));
            getLinePaint().setAlpha(alpha > 0 ? alpha : 0);
            return getLinePaint();
        }

    }

    /**
     * Primitive simulation of some kind of signal.  For this example,
     * we'll pretend its an ecg.  This class represents the data as a circular buffer;
     * data is added sequentially from left to right.  When the end of the buffer is reached,
     * i is reset back to 0 and simulated sampling continues.
     */
    public static class ECGModel implements XYSeries {

        private final Number[] data;
        private final long delayMs;
        private final int blipInteral;
        private final Thread thread;
        private boolean keepRunning;
        private int latestIndex;
        boolean sta = true;
        private WeakReference<AdvancedLineAndPointRenderer> rendererRef;
        //private BluetoothSerial bluetoothSerial;



        /**
         *
         * @param size Sample size contained within this model
         * @param updateFreqHz Frequency at which new samples are added to the model
         */
        public ECGModel(int size, int updateFreqHz) {


            data = new Number[size];
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }

            // translate hz into delay (ms):
            delayMs = 5000 / updateFreqHz;

            // add 7 "blips" into the signal:
            blipInteral = size / 7;


            thread = new Thread(new Runnable() {
                @Override
                public void run() {



                    try {

                        while (keepRunning) {




                            if (latestIndex >= data.length) {
                                latestIndex = 0;
                            }


                            // generate some random data:
                            if (latestIndex % blipInteral == 0) {
                                // insert a "blip" to simulate a heartbeat:
                                data[latestIndex] = (Math.random() * 10) + 3;
                            } else {
                                // insert a random sample:
                                data[latestIndex] = Math.random() * 2;
                            }

                            if (latestIndex < data.length - 1) {
                                // null out the point immediately following i, to disable
                                // connecting i and i+1 with a line:
                                data[latestIndex + 1] = null;
                            }

                            if (rendererRef.get() != null) {
                                rendererRef.get().setLatestIndex(latestIndex);
                                Thread.sleep(delayMs);
                            }
                            else {
                                keepRunning = false;
                            }
                            latestIndex++;
                        }

                    }

                    catch (InterruptedException e) {
                        keepRunning = false;
                    }

                }

            });

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void start(final WeakReference<AdvancedLineAndPointRenderer> rendererRef) {




            this.rendererRef = rendererRef;
            keepRunning = true;
            thread.start();



        }

        @Override
        public int size() {
            return data.length;
        }

        @Override
        public Number getX(int index) {
            return index;
        }

        @Override
        public Number getY(int index) {
            return data[index];
        }

        @Override
        public String getTitle() {
            return "Signal";
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        redrawer.finish();
    }

}
