package com.andtinder.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.nearmate.app.NearMateApp;
import com.nearmate.app.R;
import com.nearmate.app.SearchedPeopleActivity;
import com.nearmate.app.chat.ChatLoginActivity;
import com.andtinder.model.CardModel;
import com.facebook.widget.ProfilePictureView;

public final class SimpleCardStackAdapter extends CardStackAdapter {

	private ImageButton btn_user_About;
	private ImageButton btn_user_Chat;
	private ProfilePictureView profile_pic;
	Typeface HelType;

	public SimpleCardStackAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public View getCardView(int position, final CardModel model, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.searchcardview, parent, false);
			assert convertView != null;
			//HelType = Typeface.createFromAsset(mContext.getAssets(),"AlexandriaFLF.ttf"); 
			HelType = Typeface.createFromAsset(getContext().getAssets(),"AlexandriaFLF.ttf");
		}

		btn_user_About = (ImageButton)convertView.findViewById(R.id.btn_user_about);
		btn_user_Chat = (ImageButton)convertView.findViewById(R.id.btn_user_chat);
		
		
		((ProfilePictureView) convertView.findViewById(R.id.image)).setProfileId(model.getCardImageDrawable());
		
		//profile_pic = (ProfilePictureView)convertView.findViewById(R.id.image);
		//profile_pic.getLayoutParams().height = 
		
		TextView tv_name = (TextView)convertView.findViewById(R.id.name);
		tv_name.setTypeface(HelType);
		tv_name.setText(model.getTitle());
		//((TextView) convertView.findViewById(R.id.name)).setText(model.getTitle());
		((TextView) convertView.findViewById(R.id.location)).setText(model.getDescription());
	
		btn_user_About.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), SearchedPeopleActivity.class);
				intent.putExtra("fb_user_id", model.getCardImageDrawable());
				intent.putExtra("fb_user_name", model.getTitle());
				intent.putExtra("fb_user_location", model.getDescription());
				getContext().startActivity(intent);
				
			//	Toast.makeText(getContext(), "clicked About" +" "+ model.getCardImageDrawable() +" "+ model.getTitle() +" "+ model.getDescription(), Toast.LENGTH_SHORT).show();
			}
		});
		
		btn_user_Chat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				NearMateApp.to_chat_username = model.getTitle();
				
				Intent intent = new Intent(getContext(), ChatLoginActivity.class);
				intent.putExtra("fb_user_id", model.getCardImageDrawable());
				intent.putExtra("fb_user_name", model.getTitle());
				intent.putExtra("fb_user_location", model.getDescription());
				getContext().startActivity(intent);
				
			}
		});
		
		

		return convertView;
	}
}
