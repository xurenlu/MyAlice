package org.myalice.websocket.handler;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.myalice.websocket.Constant;
import org.myalice.websocket.CustomerPool;
import org.myalice.websocket.SupporterPool;
import org.myalice.websocket.Util;
import org.myalice.websocket.message.Message;
import org.myalice.websocket.message.MessageFactory;
import org.myalice.websocket.service.TalkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SupporterHandler extends TextWebSocketHandler {
	
	private Logger log = LoggerFactory.getLogger(SupporterHandler.class);
	
	@Autowired
	private SupporterPool supporterPool;
	
	@Autowired
	private CustomerPool customerPool;
	
	@Autowired
	private TalkService talkService;
	
	@Autowired
	private MessageFactory messageFactory;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		supporterPool.addSupporter(session);
		talkService.connectionOpen(session, Constant.DOMAIN_TYPE.CONNECTION_TYPE_SUPPORTER);
		log.info("Supporter " + session.getId() + " connected.");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("Receive from " + session.getId() + ": " + message.getPayload());
		//获取聊天对象
		WebSocketSession talker = null;
		@SuppressWarnings("unchecked")
		Map<String, WebSocketSession> talkers = (Map<String, WebSocketSession>)session.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_TALKER_OF_SUPPORTER);
		String content = message.getPayload();
		TextMessage sendMessage = null;
		Message tb = null;
		if (StringUtils.isNotEmpty(content)) {
			tb = Util.parseMessage(content, Message.class);
			String sessionId = tb.getContent().get(Message.CONTENT_KEY_SESSIONID);
			if (StringUtils.isNotEmpty(sessionId)) {
				talker = talkers.get(sessionId);
				sendMessage = messageFactory.generateMessage(talker, session, MessageFactory.MESSAGE_TYPE_TALK_TO_CUSTOMER, tb.getContent().get(Message.CONTENT_KEY_TALK_CONTENT));
			}
		}
		
		if (talker != null && talker.isOpen()) {
			talker.sendMessage(sendMessage);
			//发送聊天信息
			talkService.saveTalk(session, talker, Constant.DOMAIN_TYPE.TALK_TYPE_SUPPORTER_TO_CUSTOMER, tb.getContent().get(Message.CONTENT_KEY_TALK_CONTENT));
			log.info(session.getId() + " To " + talker.getId() + " : " + tb.getContent().get(Message.CONTENT_KEY_TALK_CONTENT));
		} else if (talker != null) {
			talkers.remove(talker.getId());
			talker.close();
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		supporterPool.removeSupporter(session);
		@SuppressWarnings("unchecked")
		Map<String, WebSocketSession> talkers = (Map<String, WebSocketSession>)session.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_TALKER_OF_SUPPORTER);
		if (talkers != null && talkers.size() > 0) {
			for (Entry<String, WebSocketSession> item : talkers.entrySet()) {
				customerPool.freeCustomer(item.getValue());
			}
		}
		
		talkService.connectionClose(session);
		log.info("Supporter " + session.getId() + " closed.");
	}
}