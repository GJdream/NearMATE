package com.nearmate.app;


/**
 * Define application constant.
 * 
 * @author noriyoshi.fukuzaki@kii.com
 */
public final class ApplicationConst {
	
	// TODO:Needs to overwrite APP_ID and APP_KEY. Refer to http://documentation.kii.com/en/starts/android/
	public static final String APP_ID = "358e7409";
	public static final String APP_KEY = "3a5a1666a864bb6a8844ef0f18d58748";
	// TODO:Needs to overwrite SENDER_ID. Refer to http://developer.android.com/google/gcm/gcm.html#senderid
	public static final String SENDER_ID = "875126508313";
	// TODO:Needs to overwrite FACEBOOK_APP_ID. Refer to http://documentation.kii.com/en/guides/android/managing-users/social-network-integration/facebook-integration/
	public static final String FACEBOOK_APP_ID = "1497902477127029";
	// TODO:Needs to overwrite AGGREGATION_RULE_ID. Refer to http://documentation.kii.com/en/guides/android/managing-analytics/flex-analytics/analyze-event-data/
	public static final String AGGREGATION_RULE_ID = "87";
	
	
	public static final String TOPIC_INVITE_NOTIFICATION = "invite_notify";
	/**
	 * 
	 * Represents the action indicating that chat is started.
	 */
	public static final String ACTION_CHAT_STARTED = "com.kii.sample.chat.ACTION_CHAT_STARTED";
	/**
	 * Represents the action indicating that push message is received.
	 */
	public static final String ACTION_MESSAGE_RECEIVED = "com.kii.sample.chat.ACTION_MESSAGE_RECEIVED";
	/**
	 * The name of the extra data.
	 */
	public static final String EXTRA_MESSAGE = "com.kii.sample.chat.EXTRA_MESSAGE";
	
	private ApplicationConst() {
	}
}