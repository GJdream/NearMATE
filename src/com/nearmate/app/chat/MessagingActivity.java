package com.nearmate.app.chat;

import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;
import com.sinch.android.rtc.PushPair;
import com.nearmate.app.chat.MessageService;
import com.nearmate.app.chat.MessageAdapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nearmate.app.NearMateApp;
import com.nearmate.app.R;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

public class MessagingActivity extends Activity implements ServiceConnection, MessageClientListener {

    private static final String TAG = MessagingActivity.class.getSimpleName();

    private MessageService.MessageServiceInterface mMessageService;

    private MessageAdapter mMessageAdapter;

    private TextView mTxtRecipient;

    private EditText mTxtTextBody;

    private Button mBtnSend;

    private ListView mMessagesList;
    private String recipient;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);
        populateMessageHistory();
        
         recipient =  NearMateApp.to_chat_username;
        doBind();

        mMessagesList = (ListView) findViewById(R.id.lstMessages);
        mTxtRecipient = (TextView)findViewById(R.id.txtRecipient);
        mTxtTextBody = (EditText) findViewById(R.id.txtTextBody);
        mBtnSend = (Button) findViewById(R.id.btnSend);

        mTxtRecipient.setText("To"+"  "+recipient);
        mMessageAdapter = new MessageAdapter(this);
        mMessagesList.setAdapter(mMessageAdapter);
        populateMessageHistory();
        
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }
    
    //get previous messages from parse & display
    private void populateMessageHistory() {
    	try{
    		  //String[] userIds = {currentUserId, recipientId};
            ParseQuery<ParseObject> query = ParseQuery.getQuery("MateChat");
            query.whereContains("senderId", NearMateApp.fb_user_name);
            query.whereContains("recipientId", NearMateApp.to_chat_username);
            query.orderByAscending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < messageList.size(); i++) {
                            WritableMessage message = new WritableMessage(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("messageText").toString());
                            if (messageList.get(i).get("senderId").toString().equals(NearMateApp.fb_user_name)) {
                            	mMessageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
                            } else {
                            	mMessageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
                            }
                        }
                    }
                }
            });
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    }

    @Override
    public void onDestroy() {
        doUnbind();
        super.onDestroy();
    }

    private void doBind() {
        Intent intent = new Intent(this, MessageService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    private void doUnbind() {
        if (mMessageService != null) {
            mMessageService.removeMessageClientListener(this);
        }
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mMessageService = (MessageService.MessageServiceInterface) iBinder;
        mMessageService.addMessageClientListener(this);
        setButtonEnabled(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        setButtonEnabled(false);
        mMessageService = null;
    }


    
    private void sendMessage() {
        //String recipient =  NearMateApp.to_chat_username;
        
        String textBody = mTxtTextBody.getText().toString();
        if (recipient.isEmpty()) {
            Toast.makeText(this, "No recipient added", Toast.LENGTH_SHORT).show();
            return;
        }
        if (textBody.isEmpty()) {
            Toast.makeText(this, "No text message", Toast.LENGTH_SHORT).show();
            return;
        }

        mMessageService.sendMessage(recipient, textBody);
        mTxtTextBody.setText("");
    }

    private void setButtonEnabled(boolean enabled) {
        mBtnSend.setEnabled(enabled);
    }

    @Override
    public void onIncomingMessage(MessageClient client, Message message) {
       // mMessageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
    	
    	WritableMessage writableMessage = new WritableMessage(NearMateApp.to_chat_username, message.getTextBody());
    	mMessageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);
        
        
    }

    @Override
    public void onMessageSent(MessageClient client, Message message, String recipientId) {
        
        final WritableMessage writableMessage = new WritableMessage(NearMateApp.to_chat_username, message.getTextBody());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MateChat");
        query.whereEqualTo("sinchId", message.getMessageId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                if (e == null) {
                    if (messageList.size() == 0) {
                        ParseObject parseMessage = new ParseObject("MateChat");
                        parseMessage.put("senderId", NearMateApp.fb_user_name);
                        parseMessage.put("recipientId", NearMateApp.to_chat_username);
                        parseMessage.put("messageText", writableMessage.getTextBody());
                        parseMessage.put("sinchId", writableMessage.getMessageId());
                        parseMessage.saveInBackground();

                        mMessageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);
                    }
                }
            }
        });
    }

    @Override
    public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {
        //Left intentionally blank
    }

    @Override
    public void onMessageFailed(MessageClient client, Message message,
            MessageFailureInfo failureInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Failed for user: ")
                .append(failureInfo.getRecipientId())
                .append(" with message: ")
                .append(failureInfo.getSinchError().getMessage());

        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        Log.d(TAG, sb.toString());
        sb.setLength(0);
    }

    @Override
    public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {
        Log.d(TAG, "onDelivered");
    }
}
