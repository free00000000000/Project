package com.example.project.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.example.project.R;

import org.billthefarmer.mididriver.MidiDriver;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;


public class PlayPianoActivity extends AppCompatActivity implements View.OnTouchListener,
        MidiDriver.OnMidiStartListener {

    private MidiDriver midiDriver;
    private byte[] event;
    private int[] config;
    private int octaveno;
    public int recordingno;


    private HorizontalScrollView scrollpiano;
    private TextView octave;


    private Button buttonC3;
    private Button buttonC3sharp;
    private Button buttonD3;
    private Button buttonD3sharp;
    private Button buttonE3;
    private Button buttonF3;
    private Button buttonF3sharp;
    private Button buttonG3;
    private Button buttonG3sharp;
    private Button buttonA3;
    private Button buttonA3sharp;
    private Button buttonB3;

    private Button buttonC4;
    private Button buttonC4sharp;
    private Button buttonD4;
    private Button buttonD4sharp;
    private Button buttonE4;
    private Button buttonF4;
    private Button buttonF4sharp;
    private Button buttonG4;
    private Button buttonG4sharp;
    private Button buttonA4;
    private Button buttonA4sharp;
    private Button buttonB4;

    private Button buttonC5;
    private Button buttonC5sharp;
    private Button buttonD5;
    private Button buttonD5sharp;
    private Button buttonE5;
    private Button buttonF5;
    private Button buttonF5sharp;
    private Button buttonG5;
    private Button buttonG5sharp;
    private Button buttonA5;
    private Button buttonA5sharp;
    private Button buttonB5;

    private Button buttonC6;
    private Button buttonC6sharp;
    private Button buttonD6;
    private Button buttonD6sharp;
    private Button buttonE6;
    private Button buttonF6;
    private Button buttonF6sharp;
    private Button buttonG6;
    private Button buttonG6sharp;
    private Button buttonA6;
    private Button buttonA6sharp;
    private Button buttonB6;

    private Button buttonC7;
    private Button buttonC7sharp;
    private Button buttonD7;
    private Button buttonD7sharp;
    private Button buttonE7;
    private Button buttonF7;
    private Button buttonF7sharp;
    private Button buttonG7;
    private Button buttonG7sharp;
    private Button buttonA7;
    private Button buttonA7sharp;
    private Button buttonB7;


    private TextView tc3;
    private TextView td3;
    private TextView te3;
    private TextView tf3;
    private TextView tg3;
    private TextView ta3;
    private TextView tb3;

    private TextView tc4;
    private TextView td4;
    private TextView te4;
    private TextView tf4;
    private TextView tg4;
    private TextView ta4;
    private TextView tb4;

    private TextView tc5;
    private TextView td5;
    private TextView te5;
    private TextView tf5;
    private TextView tg5;
    private TextView ta5;
    private TextView tb5;

    private TextView tc6;
    private TextView td6;
    private TextView te6;
    private TextView tf6;
    private TextView tg6;
    private TextView ta6;
    private TextView tb6;

    private TextView tc7;
    private TextView td7;
    private TextView te7;
    private TextView tf7;
    private TextView tg7;
    private TextView ta7;
    private TextView tb7;
    private ViewGroup transitionsContainer;

    private MediaPlayer mPlayer;
    String note, duration, staff;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_piano);

        scrollpiano=(HorizontalScrollView)findViewById(R.id.scrollpiano);

        buttonC3 = (Button)findViewById(R.id.w1);
        buttonC3sharp = (Button)findViewById(R.id.b1);
        buttonD3 = (Button)findViewById(R.id.w2);
        buttonD3sharp = (Button)findViewById(R.id.b2);
        buttonE3 = (Button)findViewById(R.id.w3);
        buttonF3 = (Button)findViewById(R.id.w4);
        buttonF3sharp = (Button)findViewById(R.id.b3);
        buttonG3 = (Button)findViewById(R.id.w5);
        buttonG3sharp = (Button)findViewById(R.id.b4);
        buttonA3 = (Button)findViewById(R.id.w6);
        buttonA3sharp = (Button)findViewById(R.id.b5);
        buttonB3 = (Button)findViewById(R.id.w7);

        buttonC4 = (Button)findViewById(R.id.w8);
        buttonC4sharp = (Button)findViewById(R.id.b6);
        buttonD4 = (Button)findViewById(R.id.w9);
        buttonD4sharp = (Button)findViewById(R.id.b7);
        buttonE4 = (Button)findViewById(R.id.w10);
        buttonF4 = (Button)findViewById(R.id.w11);
        buttonF4sharp = (Button)findViewById(R.id.b8);
        buttonG4 = (Button)findViewById(R.id.w12);
        buttonG4sharp = (Button)findViewById(R.id.b9);
        buttonA4 = (Button)findViewById(R.id.w13);
        buttonA4sharp = (Button)findViewById(R.id.b10);
        buttonB4 = (Button)findViewById(R.id.w14);

        buttonC5 = (Button)findViewById(R.id.w15);
        buttonC5sharp = (Button)findViewById(R.id.b11);
        buttonD5 = (Button)findViewById(R.id.w16);
        buttonD5sharp = (Button)findViewById(R.id.b12);
        buttonE5 = (Button)findViewById(R.id.w17);
        buttonF5 = (Button)findViewById(R.id.w18);
        buttonF5sharp = (Button)findViewById(R.id.b13);
        buttonG5 = (Button)findViewById(R.id.w19);
        buttonG5sharp = (Button)findViewById(R.id.b14);
        buttonA5 = (Button)findViewById(R.id.w20);
        buttonA5sharp = (Button)findViewById(R.id.b15);
        buttonB5 = (Button)findViewById(R.id.w21);

        buttonC6 = (Button)findViewById(R.id.w22);
        buttonC6sharp = (Button)findViewById(R.id.b16);
        buttonD6 = (Button)findViewById(R.id.w23);
        buttonD6sharp = (Button)findViewById(R.id.b17);
        buttonE6 = (Button)findViewById(R.id.w24);
        buttonF6 = (Button)findViewById(R.id.w25);
        buttonF6sharp = (Button)findViewById(R.id.b18);
        buttonG6 = (Button)findViewById(R.id.w26);
        buttonG6sharp = (Button)findViewById(R.id.b19);
        buttonA6 = (Button)findViewById(R.id.w27);
        buttonA6sharp = (Button)findViewById(R.id.b20);
        buttonB6 = (Button)findViewById(R.id.w28);

        buttonC7 = (Button)findViewById(R.id.w29);
        buttonC7sharp = (Button)findViewById(R.id.b21);
        buttonD7 = (Button)findViewById(R.id.w30);
        buttonD7sharp = (Button)findViewById(R.id.b22);
        buttonE7 = (Button)findViewById(R.id.w31);
        buttonF7 = (Button)findViewById(R.id.w32);
        buttonF7sharp = (Button)findViewById(R.id.b23);
        buttonG7 = (Button)findViewById(R.id.w33);
        buttonG7sharp = (Button)findViewById(R.id.b24);
        buttonA7 = (Button)findViewById(R.id.w34);
        buttonA7sharp = (Button)findViewById(R.id.b25);
        buttonB7 = (Button)findViewById(R.id.w35);

        buttonC3.setOnTouchListener(this);
        buttonC3sharp.setOnTouchListener(this);
        buttonD3.setOnTouchListener(this);
        buttonD3sharp.setOnTouchListener(this);
        buttonE3.setOnTouchListener(this);
        buttonF3.setOnTouchListener(this);
        buttonF3sharp.setOnTouchListener(this);
        buttonG3.setOnTouchListener(this);
        buttonG3sharp.setOnTouchListener(this);
        buttonA3.setOnTouchListener(this);
        buttonA3sharp.setOnTouchListener(this);
        buttonB3.setOnTouchListener(this);
        buttonC4.setOnTouchListener(this);
        buttonC4sharp.setOnTouchListener(this);
        buttonD4.setOnTouchListener(this);
        buttonD4sharp.setOnTouchListener(this);
        buttonE4.setOnTouchListener(this);
        buttonF4.setOnTouchListener(this);
        buttonF4sharp.setOnTouchListener(this);
        buttonG4.setOnTouchListener(this);
        buttonG4sharp.setOnTouchListener(this);
        buttonA4.setOnTouchListener(this);
        buttonA4sharp.setOnTouchListener(this);
        buttonB4.setOnTouchListener(this);
        buttonC5.setOnTouchListener(this);
        buttonC5sharp.setOnTouchListener(this);
        buttonD5.setOnTouchListener(this);
        buttonD5sharp.setOnTouchListener(this);
        buttonE5.setOnTouchListener(this);
        buttonF5.setOnTouchListener(this);
        buttonF5sharp.setOnTouchListener(this);
        buttonG5.setOnTouchListener(this);
        buttonG5sharp.setOnTouchListener(this);
        buttonA5.setOnTouchListener(this);
        buttonA5sharp.setOnTouchListener(this);
        buttonB5.setOnTouchListener(this);
        buttonC6.setOnTouchListener(this);
        buttonC6sharp.setOnTouchListener(this);
        buttonD6.setOnTouchListener(this);
        buttonD6sharp.setOnTouchListener(this);
        buttonE6.setOnTouchListener(this);
        buttonF6.setOnTouchListener(this);
        buttonF6sharp.setOnTouchListener(this);
        buttonG6.setOnTouchListener(this);
        buttonG6sharp.setOnTouchListener(this);
        buttonA6.setOnTouchListener(this);
        buttonA6sharp.setOnTouchListener(this);
        buttonB6.setOnTouchListener(this);
        buttonC7.setOnTouchListener(this);
        buttonC7sharp.setOnTouchListener(this);
        buttonD7.setOnTouchListener(this);
        buttonD7sharp.setOnTouchListener(this);
        buttonE7.setOnTouchListener(this);
        buttonF7.setOnTouchListener(this);
        buttonF7sharp.setOnTouchListener(this);
        buttonG7.setOnTouchListener(this);
        buttonG7sharp.setOnTouchListener(this);
        buttonA7.setOnTouchListener(this);
        buttonA7sharp.setOnTouchListener(this);
        buttonB7.setOnTouchListener(this);

        tc3=(TextView)findViewById(R.id.tc3);
        td3=(TextView)findViewById(R.id.td3);
        te3=(TextView)findViewById(R.id.te3);
        tf3=(TextView)findViewById(R.id.tf3);
        tg3=(TextView)findViewById(R.id.tg3);
        ta3=(TextView)findViewById(R.id.ta3);
        tb3=(TextView)findViewById(R.id.tb3);

        tc4=(TextView)findViewById(R.id.tc4);
        td4=(TextView)findViewById(R.id.td4);
        te4=(TextView)findViewById(R.id.te4);
        tf4=(TextView)findViewById(R.id.tf4);
        tg4=(TextView)findViewById(R.id.tg4);
        ta4=(TextView)findViewById(R.id.ta4);
        tb4=(TextView)findViewById(R.id.tb4);

        tc5=(TextView)findViewById(R.id.tc5);
        td5=(TextView)findViewById(R.id.td5);
        te5=(TextView)findViewById(R.id.te5);
        tf5=(TextView)findViewById(R.id.tf5);
        tg5=(TextView)findViewById(R.id.tg5);
        ta5=(TextView)findViewById(R.id.ta5);
        tb5=(TextView)findViewById(R.id.tb5);

        tc6=(TextView)findViewById(R.id.tc6);
        td6=(TextView)findViewById(R.id.td6);
        te6=(TextView)findViewById(R.id.te6);
        tf6=(TextView)findViewById(R.id.tf6);
        tg6=(TextView)findViewById(R.id.tg6);
        ta6=(TextView)findViewById(R.id.ta6);
        tb6=(TextView)findViewById(R.id.tb6);

        tc7=(TextView)findViewById(R.id.tc7);
        td7=(TextView)findViewById(R.id.td7);
        te7=(TextView)findViewById(R.id.te7);
        tf7=(TextView)findViewById(R.id.tf7);
        tg7=(TextView)findViewById(R.id.tg7);
        ta7=(TextView)findViewById(R.id.ta7);
        tb7=(TextView)findViewById(R.id.tb7);


        tc3.setOnTouchListener(this);
        td3.setOnTouchListener(this);
        te3.setOnTouchListener(this);
        tf3.setOnTouchListener(this);
        tg3.setOnTouchListener(this);
        ta3.setOnTouchListener(this);
        tb3.setOnTouchListener(this);

        tc4.setOnTouchListener(this);
        td4.setOnTouchListener(this);
        te4.setOnTouchListener(this);
        tf4.setOnTouchListener(this);
        tg4.setOnTouchListener(this);
        ta4.setOnTouchListener(this);
        tb4.setOnTouchListener(this);

        tc5.setOnTouchListener(this);
        td5.setOnTouchListener(this);
        te5.setOnTouchListener(this);
        tf5.setOnTouchListener(this);
        tg5.setOnTouchListener(this);
        ta5.setOnTouchListener(this);
        tb5.setOnTouchListener(this);

        tc6.setOnTouchListener(this);
        td6.setOnTouchListener(this);
        te6.setOnTouchListener(this);
        tf6.setOnTouchListener(this);
        tg6.setOnTouchListener(this);
        ta6.setOnTouchListener(this);
        tb6.setOnTouchListener(this);

        tc7.setOnTouchListener(this);
        td7.setOnTouchListener(this);
        te7.setOnTouchListener(this);
        tf7.setOnTouchListener(this);
        tg7.setOnTouchListener(this);
        ta7.setOnTouchListener(this);
        tb7.setOnTouchListener(this);

        transitionsContainer = findViewById(R.id.activity_play_piano);
        final View button = findViewById(R.id.play_button);

        button.setOnClickListener(new View.OnClickListener() {

            boolean mToRightAnimation;

            @Override
            public void onClick(View v) {
                mToRightAnimation = !mToRightAnimation;

                Transition transition = new ChangeBounds();
                transition.setDuration(mToRightAnimation ? 700 : 300);
                transition.setInterpolator(mToRightAnimation ? new FastOutSlowInInterpolator() : new AccelerateInterpolator());
                transition.setStartDelay(mToRightAnimation ? 0 : 500);
                TransitionManager.beginDelayedTransition(transitionsContainer, transition);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
                params.gravity = mToRightAnimation ? (Gravity.RIGHT | Gravity.TOP) : (Gravity.LEFT | Gravity.TOP);
                button.setLayoutParams(params);
            }

        });

        scrollpiano.post(new Runnable() {
                             public void run() {

                                 int vLeft = buttonF5.getLeft();
                                 int vRight = buttonF5.getRight();
                                 int sWidth = scrollpiano.getWidth();
                                 scrollpiano.scrollTo(((vLeft + vRight - sWidth) / 2),0);
                             }
                         }
        );

        // Instantiate the driver.
        midiDriver = new MidiDriver();
        // Set the listener.
        midiDriver.setOnMidiStartListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        midiDriver.start();

        // Get the configuration.
        config = midiDriver.config();

        // Print out the details.
        Log.d(this.getClass().getName(), "maxVoices: " + config[0]);
        Log.d(this.getClass().getName(), "numChannels: " + config[1]);
        Log.d(this.getClass().getName(), "sampleRate: " + config[2]);
        Log.d(this.getClass().getName(), "mixBufferSize: " + config[3]);

    }

    @Override
    protected void onPause() {
        super.onPause();
        midiDriver.stop();
        if(mPlayer!=null) {
           mPlayer.release();

           mPlayer = null;
        }
    }

    @Override
    public void onMidiStart() {
        Log.d(this.getClass().getName(), "onMidiStart()");
    }

    private void playNote(int noteNumber) {

        // Construct a note ON message for the note at maximum velocity on channel 1:
        event = new byte[3];
        event[0] = (byte) (0x90 | 0x00);  // 0x90 = note On, 0x00 = channel 1
        event[1] = (byte) noteNumber;
        event[2] = (byte) 0x7F;  // 0x7F = the maximum velocity (127)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

    }

    private void stopNote(int noteNumber, boolean sustainUpEvent) {

        // Stop the note unless the sustain button is currently pressed. Or stop the note if the
        // sustain button was depressed and the note's button is not pressed.
        if (sustainUpEvent) {
            // Construct a note OFF message for the note at minimum velocity on channel 1:
            event = new byte[3];
            event[0] = (byte) (0x80 | 0x00);  // 0x80 = note Off, 0x00 = channel 1
            event[1] = (byte) noteNumber;
            event[2] = (byte) 0x00;  // 0x00 = the minimum velocity (0)

            // Send the MIDI event to the synthesizer.
            midiDriver.write(event);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.d(this.getClass().getName(), "Motion event: " + event);


        // Stop any notes whose buttons are not held down.
        if (!buttonC3.isPressed()) {
            stopNote(octaveno*12, true);
        }
        if (!buttonC3sharp.isPressed()) {
            stopNote(octaveno*12+1, true);
        }
        if (!buttonD3.isPressed()) {
            stopNote(octaveno*12+2, true);
        }
        if (!buttonD3sharp.isPressed()) {
            stopNote(octaveno*12+3, true);
        }
        if (!buttonE3.isPressed()) {
            stopNote(octaveno*12+4, true);
        }
        if (!buttonF3.isPressed()) {
            stopNote(octaveno*12+5, true);
        }
        if (!buttonF3sharp.isPressed()) {
            stopNote(octaveno*12+6, true);
        }if (!buttonG3.isPressed()) {
            stopNote(octaveno*12+7, true);
        }
        if (!buttonG3sharp.isPressed()) {
            stopNote(octaveno*12+8, true);
        }
        if (!buttonA3.isPressed()) {
            stopNote(octaveno*12+9, true);
        }
        if (!buttonA3sharp.isPressed()) {
            stopNote(octaveno*12+10, true);
        }
        if (!buttonB3.isPressed()) {
            stopNote(octaveno*12+11, true);
        }

        int noteNumber;

        switch (v.getId()) {


            //case R.id.tc3:
            case R.id.w1:
                noteNumber = 36;
                //buttonC3.setPressed(false);
                break;
            case R.id.b1:
                noteNumber = 37;
                break;

            case R.id.td3:
            case R.id.w2:
                noteNumber = 38;
                break;
            case R.id.b2:
                noteNumber = 39;
                break;

            case R.id.te3:
            case R.id.w3:
                noteNumber = 40;
                break;

            case R.id.tf3:
            case R.id.w4:
                noteNumber = 41;
                break;
            case R.id.b3:
                noteNumber = 42;
                break;

            case R.id.tg3:
            case R.id.w5:
                noteNumber = 43;
                break;
            case R.id.b4:
                noteNumber = 44;
                break;

            case R.id.ta3:
            case R.id.w6:
                noteNumber = 45;
                break;
            case R.id.b5:
                noteNumber = 46;
                break;

            case R.id.tb3:
            case R.id.w7:
                noteNumber = 47;
                break;

            case R.id.tc4:
            case R.id.w8:
                noteNumber = 48;
                break;
            case R.id.b6:
                noteNumber = 49;
                break;

            case R.id.td4:
            case R.id.w9:
                noteNumber = 50;
                break;
            case R.id.b7:
                noteNumber = 51;
                break;

            case R.id.te4:
            case R.id.w10:
                noteNumber = 52;
                break;

            case R.id.tf4:
            case R.id.w11:
                noteNumber = 53;
                break;
            case R.id.b8:
                noteNumber = 54;
                break;

            case R.id.tg4:
            case R.id.w12:
                noteNumber = 55;
                break;
            case R.id.b9:
                noteNumber = 56;
                break;

            case R.id.ta4:
            case R.id.w13:
                noteNumber = 57;
                break;
            case R.id.b10:
                noteNumber = 58;
                break;

            case R.id.tb4:
            case R.id.w14:
                noteNumber = 59;
                break;

            case R.id.tc5:
            case R.id.w15:
                noteNumber = 60;
                break;
            case R.id.b11:
                noteNumber = 61;
                break;

            case R.id.td5:
            case R.id.w16:
                noteNumber = 62;
                break;
            case R.id.b12:
                noteNumber = 63;
                break;

            case R.id.te5:
            case R.id.w17:
                noteNumber = 64;
                break;

            case R.id.tf5:
            case R.id.w18:
                noteNumber = 65;
                break;
            case R.id.b13:
                noteNumber = 66;
                break;

            case R.id.tg5:
            case R.id.w19:
                noteNumber = 67;
                break;
            case R.id.b14:
                noteNumber = 68;
                break;

            case R.id.ta5:
            case R.id.w20:
                noteNumber = 69;
                break;
            case R.id.b15:
                noteNumber = 70;
                break;

            case R.id.tb5:
            case R.id.w21:
                noteNumber = 71;
                break;


            case R.id.tc6:
            case R.id.w22:
                noteNumber = 72;
                break;
            case R.id.b16:
                noteNumber = 73;
                break;

            case R.id.td6:
            case R.id.w23:
                noteNumber = 74;
                break;
            case R.id.b17:
                noteNumber = 75;
                break;

            case R.id.te6:
            case R.id.w24:
                noteNumber = 76;
                break;

            case R.id.tf6:
            case R.id.w25:
                noteNumber = 77;
                break;
            case R.id.b18:
                noteNumber = 78;
                break;

            case R.id.tg6:
            case R.id.w26:
                noteNumber = 79;
                break;
            case R.id.b19:
                noteNumber = 80;
                break;

            case R.id.ta6:
            case R.id.w27:
                noteNumber = 81;
                break;
            case R.id.b20:
                noteNumber = 82;
                break;

            case R.id.tb6:
            case R.id.w28:
                noteNumber = 83;
                break;


            case R.id.tc7:
            case R.id.w29:
                noteNumber = 84;
                break;
            case R.id.b21:
                noteNumber = 85;
                break;

            case R.id.td7:
            case R.id.w30:
                noteNumber = 86;
                break;
            case R.id.b22:
                noteNumber = 87;
                break;

            case R.id.te7:
            case R.id.w31:
                noteNumber = 88;
                break;

            case R.id.tf7:
            case R.id.w32:
                noteNumber = 89;
                break;
            case R.id.b23:
                noteNumber = 90;
                break;

            case R.id.tg7:
            case R.id.w33:
                noteNumber = 91;
                break;
            case R.id.b24:
                noteNumber = 92;
                break;

            case R.id.ta7:
            case R.id.w34:
                noteNumber = 93;
                break;
            case R.id.b25:
                noteNumber = 94;
                break;

            case R.id.tb7:
            case R.id.w35:
                noteNumber = 95;
                break;

            default:
                noteNumber = -1;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
            playNote(noteNumber);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
            stopNote(noteNumber, false);
        }

        return false;
    }

    // Parse MusicXML
    // Only works for right hand and major notes on C
    public void auto_play_piano(View view){
        try {
            InputStream is = getAssets().open("Image-3968.musicxml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("part");

            for (int i=0; i<nList.getLength(); i++) {
                Element partNode = (Element) nList.item(i);
                NodeList measureList = partNode.getElementsByTagName("measure");
                //Log.d("measureAppear", "i = "+Integer.toString(i));
                for(int j=0; j<measureList.getLength(); j++)
                {
                    Element measureNode = (Element) measureList.item(j);
                    NodeList noteList = measureNode.getElementsByTagName("note");
                    //Log.d("noteAppear", "i = "+Integer.toString(i)+", j = "+Integer.toString(j));
                    for(int k=0; k<noteList.getLength(); k++)
                    {
                        Element pitchNode = (Element) noteList.item(k);
                        NodeList pitchList = pitchNode.getElementsByTagName("pitch");
                        for(int l=0; l<pitchList.getLength(); l++)
                        {
                            Node node = pitchList.item(l);
                            if(node.getNodeType() == Node.ELEMENT_NODE)
                            {
                                Element eElement = (Element) node;
                                note = getValue("step", eElement) + getValue("octave", eElement);
                            }
                        }
                        duration = getValue("duration", pitchNode);
                        staff = getValue("staff", pitchNode);
                    }
                    // Only play right hand
                    if(staff.equals("1")) {
                        // Change played note color to YELLOW
                        changeColor(note, Color.YELLOW);
                        //changeColor(note, Color.WHITE);

                        Log.d("changeColor", note+" to BLACK");


                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            //@Override
                            public void run() {
                                // After delay (duration), change it back to normal WHITE
                                changeColor(note, Color.WHITE);
                                Log.d("changeColor", note+" to WHITE");
                            }
                        }, 10);
                        handler.removeCallbacks(null);
                    }
                }
            }

        } catch (Exception e) {e.printStackTrace();}
    }

    private void changeColor(String n, int color) {
        switch(n) {
            case "C3":
                buttonC3.setBackgroundColor(color);
                break;
            case "D3":
                buttonD3.setBackgroundColor(color);
                break;
            case "E3":
                buttonE3.setBackgroundColor(color);
                break;
            case "F3":
                buttonF3.setBackgroundColor(color);
                break;
            case "G3":
                buttonG3.setBackgroundColor(color);
                break;
            case "A3":
                buttonA3.setBackgroundColor(color);
                break;
            case "B3":
                buttonB3.setBackgroundColor(color);
                break;
            case "C4":
                buttonC4.setBackgroundColor(color);
                break;
            case "D4":
                buttonD4.setBackgroundColor(color);
                break;
            case "E4":
                buttonE4.setBackgroundColor(color);
                break;
            case "F4":
                buttonF4.setBackgroundColor(color);
                break;
            case "G4":
                buttonG4.setBackgroundColor(color);
                break;
            case "A4":
                buttonA4.setBackgroundColor(color);
                break;
            case "B4":
                buttonB4.setBackgroundColor(color);
                break;
            case "C5":
                buttonC5.setBackgroundColor(color);
                break;
            case "D5":
                buttonD5.setBackgroundColor(color);
                break;
            case "E5":
                buttonE5.setBackgroundColor(color);
                break;
            case "F5":
                buttonF5.setBackgroundColor(color);
                break;
            case "G5":
                buttonG5.setBackgroundColor(color);
                break;
            case "A5":
                buttonA5.setBackgroundColor(color);
                break;
            case "B5":
                buttonB5.setBackgroundColor(color);
                break;
            case "C6":
                buttonC6.setBackgroundColor(color);
                break;
            case "D6":
                buttonD6.setBackgroundColor(color);
                break;
            case "E6":
                buttonE6.setBackgroundColor(color);
                break;
            case "F6":
                buttonF6.setBackgroundColor(color);
                break;
            case "G6":
                buttonG6.setBackgroundColor(color);
                break;
            case "A6":
                buttonA6.setBackgroundColor(color);
                break;
            case "B6":
                buttonB6.setBackgroundColor(color);
                break;
            case "C7":
                buttonC7.setBackgroundColor(color);
                break;
            case "D7":
                buttonD7.setBackgroundColor(color);
                break;
            case "E7":
                buttonE7.setBackgroundColor(color);
                break;
            case "F7":
                buttonF7.setBackgroundColor(color);
                break;
            case "G7":
                buttonG7.setBackgroundColor(color);
                break;
            case "A7":
                buttonA7.setBackgroundColor(color);
                break;
            case "B7":
                buttonB7.setBackgroundColor(color);
                break;
                default:
                    break;
        }
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
