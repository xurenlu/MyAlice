package org.myalice.websocket.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.myalice.websocket.Constant;
import org.myalice.websocket.Util;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MessageFactory {

	public static final String MESSAGE_TYPE_CONNECT = "connect";
	
	public static final String MESSAGE_TYPE_CLOSE_TO_CUSTOMER = "customer_close";
	
	public static final String MESSAGE_TYPE_CLOSE_CUSTOMER_TO_SUPPORTER = "customer_of_supporter_close";
	
	public static final String MESSAGE_TYPE_TALK_TO_CUSTOMER = "customer_talk";
	
	public static final String MESSAGE_TYPE_TALK_TO_SUPPORTER = "supporter_talk";
	
	public static final String MESSAGE_TYPE_ASSIGN_TO_CUSTOMER = "customer_assign";
	
	public static final String MESSAGE_TYPE_ASSIGN_TO_SUPPORTER = "supporter_assign";
	
	public static TextMessage generateMessage(WebSocketSession customerSession, 
			WebSocketSession supporterSession, String type, String message) throws JsonProcessingException {
		if (StringUtils.isEmpty(type)) {
			return null;
		}
		if (type.equals(MESSAGE_TYPE_TALK_TO_CUSTOMER)) {
			Map<String, String> contentMap = new HashMap<String, String>();
			contentMap.put(Message.CONTENT_KEY_TALK_CONTENT, message);
			return new TextMessage(
					Util.formatMessage(
							createMessage(contentMap, 
									MESSAGE_TYPE_TALK_TO_CUSTOMER)));
		} else if (type.equals(MESSAGE_TYPE_TALK_TO_SUPPORTER)) {
			return new TextMessage(
					Util.formatMessage(
							createMessage(generateTalkMessage(customerSession, message), 
									MESSAGE_TYPE_TALK_TO_SUPPORTER)));
		} else if (type.equals(MESSAGE_TYPE_ASSIGN_TO_CUSTOMER)) {
			return new TextMessage(
					Util.formatMessage(
							createMessage(generateAssignMessage(supporterSession), 
									MESSAGE_TYPE_ASSIGN_TO_CUSTOMER)));
		} else if (type.equals(MESSAGE_TYPE_ASSIGN_TO_SUPPORTER)) {
			return new TextMessage(
					Util.formatMessage(
							createMessage(generateAssignMessage(customerSession), 
									MESSAGE_TYPE_ASSIGN_TO_SUPPORTER)));
		} else if (type.equals(MESSAGE_TYPE_CONNECT)) {
			
		} else if (type.equals(MESSAGE_TYPE_CLOSE_TO_CUSTOMER)) {
			return new TextMessage(
					Util.formatMessage(
							createMessage(generateCloseMessage(customerSession), 
									MESSAGE_TYPE_CLOSE_TO_CUSTOMER)));
		} else if (type.equals(MESSAGE_TYPE_CLOSE_CUSTOMER_TO_SUPPORTER)) {
			return new TextMessage(
					Util.formatMessage(
							createMessage(generateCloseMessage(customerSession), 
									MESSAGE_TYPE_CLOSE_CUSTOMER_TO_SUPPORTER)));
		}
		return null;
	}
	
	private static Message createMessage(Map<String, String> bean, String type) {
		if (bean == null || StringUtils.isEmpty(type)) {
			return null;
		}
		Message reValue = new Message();
		reValue.setType(type);
		reValue.setContent(bean);
		return reValue;
	}
	
	private static Map<String, String> generateCloseMessage(WebSocketSession session) {
		if (session != null) {
			Map<String, String> reValue = new HashMap<String, String>();
			reValue.put(Message.CONTENT_KEY_SESSIONID, session.getId());
			return reValue;
		}
		return null;
	}
	
	private static Map<String, String> generateTalkMessage(WebSocketSession session, 
			String message) {
		Map<String, String> reValue = new HashMap<String, String>();
		reValue.put(Message.CONTENT_KEY_SESSIONID, session.getId());
		reValue.put(Message.CONTENT_KEY_TALK_CONTENT, message);
		return reValue;
	}
	
	private static Map<String, String> generateAssignMessage(WebSocketSession otherSession) {
		String userName = (String)otherSession.getAttributes().get(Constant.SESSION_KEY_USER_NAME);
		if (StringUtils.isEmpty(userName)) {
			userName = otherSession.getId();
		}
		String logoUrl =  (String)otherSession.getAttributes().get(Constant.SESSION_KEY_LOGO_URL);
		if (StringUtils.isEmpty(logoUrl)) {
			logoUrl = Constant.USER_LOGO_DEFAULT_ADDRESS;
		}
		Map<String, String> reValue = new HashMap<String, String>();
		reValue.put(Message.CONTENT_KEY_SESSIONID, otherSession.getId());
		reValue.put(Message.CONTENT_KEY_USERNAME, userName);
		reValue.put(Message.CONTENT_KEY_USERLOGO, logoUrl);
		return reValue;
	}
	
	public static void main(String[] args) throws IOException {
		Map<String, String> cMap = new HashMap<String, String>();
		cMap.put(Message.CONTENT_KEY_SESSIONID, "123456");
		cMap.put(Message.CONTENT_KEY_USERNAME, "hehe");
		cMap.put(Message.CONTENT_KEY_USERLOGO, null);
		String string = Util.formatMessage(createMessage(cMap, MessageFactory.MESSAGE_TYPE_ASSIGN_TO_CUSTOMER));
		System.out.println(string);
		Message message = Util.parseMessage(string, Message.class);
		System.out.println(message);
		System.out.println(message.getContent());
		System.out.println(message.getType());
	}
}
