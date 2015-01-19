package com.example.glass_test;

import java.util.List;
import java.util.Locale;

import com.example.glass_test.util.SystemUiHider;
import com.google.android.glass.app.Card;
import com.google.android.glass.app.Card.ImageLayout;

import java.io.BufferedReader;

import com.google.android.glass.touchpad.GestureDetector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle; 

public class voiceRecognition extends Activity { 
	
	TextToSpeech tts;
	private GestureDetector mGestureDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		  
		
		//Keep screen on without dim
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        displaySpeechRecognizer();
        
        //Original layout
      	setContentView(R.layout.voice_recoginition);
        
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			
    		@Override
    		public void onInit(int status) {
    				//tts.speak("Main menu", TextToSpeech.QUEUE_FLUSH, null);
    			} 	
    		});
        
	}
        
	private static final int SPEECH_REQUEST = 0;

	private void displaySpeechRecognizer() {
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    startActivityForResult(intent, SPEECH_REQUEST);
	} 

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
	    if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
	        List<String> results = data.getStringArrayListExtra(
	                RecognizerIntent.EXTRA_RESULTS);
	        String spokenText = results.get(0);
	        
	        tts.speak("You said " + spokenText, TextToSpeech.QUEUE_FLUSH, null);
	        // Do something with spokenText.
	        TextView tv = (TextView) findViewById(R.id.textView1);
            tv.setText(spokenText);
	           
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    TextView tv = (TextView) findViewById(R.id.textView1);
	    if ( tv.getText().toString().equals("go back to main menu") ){ 
        	tts.speak("Going home", TextToSpeech.QUEUE_FLUSH, null);  
        	Intent fullscreenActivity = new Intent(voiceRecognition.this, FullscreenActivity.class);
            startActivity(fullscreenActivity); 
        } 
	    if ( tv.getText().toString().equals("go to cards") ){ 
        	tts.speak("Going to cards", TextToSpeech.QUEUE_FLUSH, null);  
        	Intent fullscreenActivity = new Intent(voiceRecognition.this, ScrollingCards.class);
            startActivity(fullscreenActivity); 
        } 
	    if ( tv.getText().toString().equals("go to main activity") ){ 
        	tts.speak("Going to main activity", TextToSpeech.QUEUE_FLUSH, null);  
        	Intent fullscreenActivity = new Intent(voiceRecognition.this, FullscreenActivity.class);
            startActivity(fullscreenActivity); 
        } 
	    
	  
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		//Main Click
		if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
			//User tapped touchpad, do something
			
            return true;
		}
		//Camera click
		if (keycode == KeyEvent.KEYCODE_CAMERA) {

			return true;
		}
		//Swipe down
		if (keycode == KeyEvent.KEYCODE_BACK) {
			//User swiped down, do something
			finish();
			Intent newView = new Intent(voiceRecognition.this, ScrollingCards.class);
            startActivity(newView); 
			//makeCard("You swiped down");
			//finish();
			return true;
		}
		
		return false;
	} 


}
