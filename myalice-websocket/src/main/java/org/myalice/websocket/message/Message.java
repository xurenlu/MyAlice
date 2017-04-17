package org.myalice.websocket.message;

import java.util.Map;

public class Message {
	
	public static final String CONTENT_KEY_SESSIONID = "sessionId";
	
	public static final String CONTENT_KEY_TALK_CONTENT = "talkContent";
	
	public static final String CONTENT_KEY_USERNAME = "userName";
	
	public static final String CONTENT_KEY_USERLOGO = "userLogo";
	
	private String type;
	
	private Map<String, String> content;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getContent() {
		return content;
	}

	public void setContent(Map<String, String> content) {
		this.content = content;
	}
}
