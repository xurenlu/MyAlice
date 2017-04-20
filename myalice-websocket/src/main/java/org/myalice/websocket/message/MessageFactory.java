package org.myalice.websocket.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.myalice.domain.websocket.TalkRecord;
import org.myalice.websocket.Constant;
import org.myalice.websocket.Util;
import org.myalice.websocket.service.TalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class MessageFactory {

	public static final String MESSAGE_TYPE_CUSTOMER_CONNECT = "customer_connect";
	
	public static final String MESSAGE_TYPE_SUPPORTER_CONNECT = "supporter_connect";
	
	public static final String MESSAGE_TYPE_CLOSE_TO_CUSTOMER = "customer_close";
	
	public static final String MESSAGE_TYPE_CLOSE_CUSTOMER_TO_SUPPORTER = "customer_of_supporter_close";
	
	public static final String MESSAGE_TYPE_TALK_TO_CUSTOMER = "customer_talk";
	
	public static final String MESSAGE_TYPE_TALK_TO_SUPPORTER = "supporter_talk";
	
	public static final String MESSAGE_TYPE_ASSIGN_TO_CUSTOMER = "customer_assign";
	
	public static final String MESSAGE_TYPE_ASSIGN_TO_SUPPORTER = "supporter_assign";
	
	public static final String MESSAGE_TYPE_HISTORY_TO_CUSTOMER = "history";
	
	@Autowired
	private TalkService talkService;
	
	public TextMessage generateMessage(WebSocketSession customerSession, 
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
					Util.formatMessage(generateAssignToSupporterMessage(customerSession, supporterSession)));
		} else if (type.equals(MESSAGE_TYPE_CUSTOMER_CONNECT)) {
			return new TextMessage(
					Util.formatMessage(generateCustomerConnectMessage(customerSession)));
		} else if (type.equals(MESSAGE_TYPE_SUPPORTER_CONNECT)) {
			
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
	
	private Message createMessage(Map<String, String> bean, String type) {
		if (bean == null || StringUtils.isEmpty(type)) {
			return null;
		}
		Message reValue = new Message();
		reValue.setType(type);
		reValue.setContent(bean);
		return reValue;
	}
	
	private Map<String, String> generateCloseMessage(WebSocketSession session) {
		if (session != null) {
			Map<String, String> reValue = new HashMap<String, String>();
			reValue.put(Message.CONTENT_KEY_SESSIONID, session.getId());
			return reValue;
		}
		return null;
	}
	
	private Map<String, String> generateTalkMessage(WebSocketSession session, 
			String message) {
		Map<String, String> reValue = new HashMap<String, String>();
		reValue.put(Message.CONTENT_KEY_SESSIONID, session.getId());
		reValue.put(Message.CONTENT_KEY_TALK_CONTENT, message);
		return reValue;
	}
	
	private Map<String, String> generateAssignMessage(WebSocketSession otherSession) {
		String userName = (String)otherSession.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_USER_NAME);
		if (StringUtils.isEmpty(userName)) {
			userName = otherSession.getId();
		}
		String logoUrl =  (String)otherSession.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_LOGO_URL);
		if (StringUtils.isEmpty(logoUrl)) {
			logoUrl = Constant.USER_LOGO_DEFAULT_ADDRESS;
		}
		Map<String, String> reValue = new HashMap<String, String>();
		reValue.put(Message.CONTENT_KEY_SESSIONID, otherSession.getId());
		reValue.put(Message.CONTENT_KEY_USERNAME, userName);
		reValue.put(Message.CONTENT_KEY_USERLOGO, logoUrl);
		return reValue;
	}
	
	private Message generateAssignToSupporterMessage(WebSocketSession customerSession, WebSocketSession supporterSession) {
		String userName = (String)customerSession.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_USER_NAME);
		if (StringUtils.isEmpty(userName)) {
			userName = customerSession.getId();
		}
		String logoUrl =  (String)customerSession.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_LOGO_URL);
		if (StringUtils.isEmpty(logoUrl)) {
			logoUrl = Constant.USER_LOGO_DEFAULT_ADDRESS;
		}
		Message message = new Message();
		message.setType(MESSAGE_TYPE_ASSIGN_TO_SUPPORTER);
		
		Map<String, String> reMap = new HashMap<String, String>();
		reMap.put(Message.CONTENT_KEY_SESSIONID, customerSession.getId());
		reMap.put(Message.CONTENT_KEY_USERNAME, userName);
		reMap.put(Message.CONTENT_KEY_USERLOGO, logoUrl);
		message.setContent(reMap);
		
		List<TalkRecord> historyTalk = talkService.getHistoryTalk(customerSession);
		if (historyTalk != null && historyTalk.size() > 0) {
			List<SimpleTalk> reHistory = new ArrayList<SimpleTalk>();
			for (TalkRecord talk : historyTalk) {
				SimpleTalk item = new SimpleTalk();
				item.setType(talk.getType());
				item.setContent(talk.getContent());
				reHistory.add(item);
			}
			message.setHistory(reHistory);
		}
		return message;
	}
	
	public Message generateCustomerConnectMessage(WebSocketSession customerSession) throws JsonProcessingException {
		if (customerSession == null) {
			return null;
		}
		List<TalkRecord> historyTalk = talkService.getHistoryTalk(customerSession);
		if (historyTalk != null && historyTalk.size() > 0) {
			List<SimpleTalk> reValue = new ArrayList<SimpleTalk>();
			for (TalkRecord talk : historyTalk) {
				SimpleTalk item = new SimpleTalk();
				item.setType(talk.getType());
				item.setContent(talk.getContent());
				reValue.add(item);
			}
			Message orgMessage = new Message();
			orgMessage.setType(MESSAGE_TYPE_CUSTOMER_CONNECT);
			orgMessage.setHistory(reValue);
			return orgMessage;
		} 
		return null;
	}
}
