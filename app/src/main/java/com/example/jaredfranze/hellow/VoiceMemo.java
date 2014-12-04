package com.example.jaredfranze.hellow;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;

/**
 * Created by Jared.Franze on 11/17/2014.
 */

public class VoiceMemo extends Activity {

    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private ImageButton start,stop,play,save,erase;
    long event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = extras.getLong("event");
        }

        setContentView(R.layout.activity_voicememo);
        start = (ImageButton)findViewById(R.id.button1);
        stop = (ImageButton)findViewById(R.id.button2);
        play = (ImageButton)findViewById(R.id.button3);
        erase = (ImageButton)findViewById(R.id.button4);
        save = (ImageButton)findViewById(R.id.button5);

        /*
        final ImageButton swapView =(ImageButton)findViewById(R.id.button5);
        swapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),Agenda.class));
            }
        });
        */

        stop.setEnabled(false);
        play.setEnabled(false);
        erase.setEnabled(false);
        save.setEnabled(false);
        //May need to do something with this
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/recording" + event;

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        File ourFile = new File(outputFile);
        if (ourFile.exists())
        {
            erase.setEnabled(true);
        }

    }

    public void start(View view){
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        start.setEnabled(false);
        stop.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Begin Recording", Toast.LENGTH_SHORT).show();

    }

    public void stop(View view){
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder  = null;
        stop.setEnabled(false);
        play.setEnabled(true);
        erase.setEnabled(true);
        save.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Audio recorded successfully",
                Toast.LENGTH_SHORT).show();
    }

    public void play(View view) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException{

        MediaPlayer m = new MediaPlayer();
        m.setDataSource(outputFile);
        m.prepare();
        m.start();
        Toast.makeText(getApplicationContext(), "Playing Voice Memo", Toast.LENGTH_SHORT).show();

    }

    /*
        ERASE CURRENT FILE & START OVER
     */
    public void erase(View view)
    {
        File ourFile = new File(outputFile);
        if (ourFile.exists())
        {
            ourFile.delete();
        }
        stop.setEnabled(false);
        play.setEnabled(false);
        erase.setEnabled(false);
        save.setEnabled(false);
        start.setEnabled(true);
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
    }

    /*
        SEND FILE TO EVENT
     */
    public void save(View view)
    {
        finish();
    }

    public void cancel(View view)
    {
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.voice_memo, menu);
        return false;
    }

}

