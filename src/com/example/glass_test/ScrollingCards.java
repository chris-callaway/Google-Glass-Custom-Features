package com.example.glass_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.glass_test.util.SystemUiHider;
import com.google.android.glass.app.Card;
import com.google.android.glass.app.Card.ImageLayout;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.speech.tts.TextToSpeech;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
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
 
public class ScrollingCards extends Activity {

    private List<Card> mCards;
    private CardScrollView mCardScrollView;
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
     				setupClickListener();
     			} 
     		
     		});
     		
        createCards();

        mCardScrollView = new CardScrollView(this);
        CardScrollAdapter adapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        setContentView(mCardScrollView);
    }

    private void createCards() {
        mCards = new ArrayList<Card>();

        Card card;

        card = new Card(this);
        card.setText("Go to main activity");
        mCards.add(card);

        card = new Card(this);
        card.setText("Go to voice recognition");
        card.setImageLayout(Card.ImageLayout.FULL);
        //card.addImage(R.drawable.puppy_bg);
        mCards.add(card);
        
        card = new Card(this);
        card.setText("Go to camera preview");
        mCards.add(card);
        
        card = new Card(this);
        card.setText("Go to curl request");
        mCards.add(card);

//        card = new Card(this);
//        card.setText("This card has a mosaic of puppies.");
//        card.setFootnote("Aren't they precious?");
//        card.setImageLayout(Card.ImageLayout.LEFT);
////        card.addImage(R.drawable.puppy_small_1);
////        card.addImage(R.drawable.puppy_small_2);
////        card.addImage(R.drawable.puppy_small_3);
//        mCards.add(card);
    }

    private class ExampleCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
            return mCards.indexOf(item);
        } 

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return Card.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position){
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView,
                ViewGroup parent) {
            return  mCards.get(position).getView(convertView, parent);
        }
    }
    
    private void setupClickListener() {
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	int index = mCardScrollView.getSelectedItemPosition();
            	
            	//Go to main activity
            	if (index == 0){
            		//tts.speak("Going to main activity", TextToSpeech.QUEUE_FLUSH, null);
            		finish();
            		Intent newView = new Intent(ScrollingCards.this, FullscreenActivity.class);
                    startActivity(newView);
            	}
            	//Go to voice recognition
            	if (index == 1){
            		//tts.speak("Going to voice recognition", TextToSpeech.QUEUE_FLUSH, null);
            		finish();
            		Intent newView = new Intent(ScrollingCards.this, voiceRecognition.class);
                    startActivity(newView);
            	}
            	//Go to camera preview
            	if (index == 2){
            		//tts.speak("Going to camera preview", TextToSpeech.QUEUE_FLUSH, null);
            		finish();
            		Intent newView = new Intent(ScrollingCards.this, CameraActivity.class);
                    startActivity(newView);
            	}
            	//Go to curl request
            	if (index == 3){
            		//tts.speak("Going to camera preview", TextToSpeech.QUEUE_FLUSH, null);
            		finish();
            		Intent newView = new Intent(ScrollingCards.this, CurlRequest.class);
                    startActivity(newView);
            	}
            }
        });
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
    		return true;
    	}
    	
    	return false;
    }

} 