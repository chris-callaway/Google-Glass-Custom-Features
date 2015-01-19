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

public class FullscreenActivity extends Activity {
	
	private RelativeLayout mLayout; 
	private GestureDetector mGestureDetector;
	TextToSpeech tts;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		  
		
		//Keep screen on without dim
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
		// Init TextToSpeech engine
		tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			
		@Override
		public void onInit(int status) { 
				//tts.speak("Main menu", TextToSpeech.QUEUE_FLUSH, null);
			} 
		
		});
		
		//New layout
		
		//Create text element
		final TextView tv1 = new TextView(this);
		//Label text and set id, layout parameters
        tv1.setText("Welcome"); 
        tv1.setId(5); 
        tv1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        
        //Make new layout
        mLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams firstImageParams = new RelativeLayout.LayoutParams(640,480);
        
        //Add textview to new layout
        mLayout.addView(tv1, firstImageParams);
        
        //Set view to the new layout
        setContentView(mLayout);  
        
	}
	
	public void makeCard(String text){
        //Set card view
        Card card = new Card(this);
        card.setText(text);
        card.setFootnote("my footnote text");
        card.setImageLayout(ImageLayout.LEFT);
      //card.addImage(R.drawable.hitlabnz);
        
        setContentView(card.getView());
    }
	
	public void changeText(String text){
		final TextView tv1 = new TextView(this);
		tv1.setText(text);  
		mLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams firstImageParams = new RelativeLayout.LayoutParams(640,480);
		mLayout.addView(tv1, firstImageParams);
		setContentView(mLayout);  
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
			Intent newView = new Intent(FullscreenActivity.this, ScrollingCards.class);
            startActivity(newView); 
			//makeCard("You swiped down");
			//finish();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
			Log.d("My string", "You are touching down");
			break;
		case MotionEvent.ACTION_MOVE:
			//Log.d("My string", "You touched");
			break;
		case MotionEvent.ACTION_UP:
			Log.d("My string", "You released touch");
			break;
		}
		
		return super.onGenericMotionEvent(event);
	}
	
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}
	
	
	

}
