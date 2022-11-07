package com.sitstaycreate.midibugdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // A reference to the MidiDevice
    MidiDevice mMidiDevice;
    // A reference to an open port on the MidiDevice
    MidiOutputPort openPort;
    // A receiver for the MidiDevice
    MidiReceiverDisplay midiReceiverDisplay;
    // A TextView to display the result
    TextView mMidiDisplay;
    // Error tag
    String TAG = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inflate TextView
        mMidiDisplay = (TextView) findViewById(R.id.display_textview);
        // Create a receiver and pass it a reference to the TextView
        midiReceiverDisplay = new MidiReceiverDisplay(mMidiDisplay);
        midiSetup();
    }

    private void midiSetup(){
        // Get the Midi service from the system
        MidiManager m = (MidiManager)this.getSystemService(MIDI_SERVICE);
        // Get info about available devices
        MidiDeviceInfo[] devicesInfo = m.getDevices();
        // If no devices available, log the following message
        if(devicesInfo.length == 0){
            Log.e(TAG, "No devices available");
        } else {
            // Get the info for the first available device (for simplicity in this example)
            MidiDeviceInfo midiDeviceInfo = devicesInfo[0];
            // openDevice method - here's where the problems start
            m.openDevice(midiDeviceInfo, new MidiManager.OnDeviceOpenedListener() {
                @Override
                public void onDeviceOpened(MidiDevice device) {
                    if (device == null) {
                        Log.e(TAG, "could not open device " + midiDeviceInfo);
                    } else {
                        // It is unclear to me what to do here based on the documentation
                        // It seems that I should assign device to mMidiDevice for later use
                        mMidiDevice = device;
                        // It also seems reasonable to open a port on the device here
                        // and assign a reference to it
                        // Arbitrarily using portNumber 1 - I have also tried this with 0
                        openPort = device.openOutputPort(1);
                        // open port is always null in every case I try
                        if (openPort == null) {
                            Log.e(TAG,
                                    "could not open output port for " + midiDeviceInfo);
                            return;
                        }
                        // Connect the open port to the custom receiver
                        // I have also tried this outside of the onDeviceOpened() method
                        openPort.connect(midiReceiverDisplay);
                        // I am using Max 7 to test, the device shows in the program
                        // The documentation does not show a working program for either
                        // receiving or sending midi and so therefore has a "documentation bug"
                        // I have worked with the Javax midi library before, so this shouldn't
                        // be as difficult as it is here. That is a good example of complete
                        // documentation. My device does support midi, because Phil Burk's midi
                        // tools work on my device. However, I cannot figure out his library
                        // without having a more simple working example of connecting midi
                        // input and output, including how to 1) open a device and a port
                        // 2) send or receive data from these ports
                        // 3) how to properly close these ports
                        // In addition to this, documentation should be improved on how
                        // to support hotplugging since this is also not complete or clear
                        // Thank you for your help
                        // Also, I am using a samsung SM-X200, but this should not matter
                        // since my device works with other programs
                    }
                }
                // I have also tried using the value Null instead of a Handler
            }, new Handler(Looper.getMainLooper()));
        }
    }
}