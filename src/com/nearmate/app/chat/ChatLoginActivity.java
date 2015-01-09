package com.nearmate.app.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nearmate.app.NearMateApp;
import com.nearmate.app.R;
import com.nearmate.app.chat.MessageService;


public class ChatLoginActivity extends Activity{
//public class ChatLoginActivity extends Activity implements AnimationListener{
	
	private static final String TAG = "ChatLoginActivity";

 // Animation
    Animation animFadein;
private ImageView logo_image;

private final int SPLASH_TIME = 2000;
private final int STOP_SPLASH = 1;
private Button enter_chat;





@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
        
        logo_image = (ImageView)findViewById(R.id.imageView1);
        enter_chat = (Button)findViewById(R.id.enter_chat);
        
     // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        logo_image.startAnimation(animFadein);
        
        enter_chat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				login();
			}
		});
}
        
        
        
        
private void login() {

	String userName = NearMateApp.fb_user_name;

    if (userName.isEmpty()) {
        Toast.makeText(ChatLoginActivity.this, "Please enter a name", Toast.LENGTH_LONG).show();
        return;
    }

    Log.e(TAG, "Login Invoked");
    Intent intent = new Intent(ChatLoginActivity.this, MessageService.class);
    intent.putExtra(MessageService.INTENT_EXTRA_USERNAME, userName);
    startService(intent);
}
    

}
