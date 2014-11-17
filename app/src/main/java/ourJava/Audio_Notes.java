/*
 * The application needs to have the permission to write to external storage
 * if the output file is written to the external storage, and also the
 * permission to record audio. These permissions must be set in the
 * application's AndroidManifest.xml file, with something like:
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *
 *Audio_Note calls to android.media.MediaRecorder
 *
 *
 */
package com.android.audiorecordtest;

import android.app.Activity;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.IOException;


public class AudioNotes extends Activity
{
    private static final String LOG_TAG = "AudioRecordTest"; //Audio Test Case; removable
    private static String mFileName = null;

    private RecordButton recButton = null; //recButton = <Button on Calendar/Event UI>
    private MediaRecorder runRecorder = null; //MediaRecorder class variable

    private PlayButton   playButton = null; //playButton = <Button on Calendar/Event UI>
    private MediaPlayer   runPlayer = null; //MediaPlayer class variable

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        runPlayer = new MediaPlayer();
        try {
            runPlayer.setDataSource(mFileName);
            runPlayer.prepare();
            runPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        runPlayer.release();
        runPlayer = null;
    }

    private void startRecording() {
        runRecorder = new MediaRecorder();
        runRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        runRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        runRecorder.setOutputFile(mFileName);
        runRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            runRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        runRecorder.start();
    }

    private void stopRecording() {
        runRecorder.stop();
        runRecorder.release();
        runRecorder = null;
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

//AudioRecordTest() example used to catch exceptions	
    public AudioRecordTest() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }
//Removable

//UI Creator to run example code, will need to be removed to interface with Calendar UI 
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        LinearLayout ll = new LinearLayout(this);
        recButton = new RecordButton(this);
        ll.addView(recButton,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        playButton = new PlayButton(this);
        ll.addView(playButton,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        setContentView(ll);
    }
//Removable

    @Override
    public void onPause() {
        super.onPause();
        if (runRecorder != null) {
            runRecorder.release();
            runRecorder = null;
        }

        if (runPlayer != null) {
            runPlayer.release();
            runPlayer = null;
        }
    }
}