package com.sitstaycreate.midibugdemo;

import android.media.midi.MidiReceiver;
import android.widget.TextView;

import java.io.IOException;

public class MidiReceiverDisplay extends MidiReceiver {
    private TextView mDisplay;

    public MidiReceiverDisplay(TextView display){
        mDisplay = display;
    }

    @Override
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {
        // I would normally display one of the bytes from the msg, but for simplicity,
        // I am just showing dummy text
        mDisplay.setText("Msg received");
    }
}
