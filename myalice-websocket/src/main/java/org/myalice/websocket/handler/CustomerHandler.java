package org.myalice.websocket.handler;

import org.apache.commons.lang3.StringUtils;
import org.myalice.websocket.AssignManager;
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
public class CustomerHandler extends TextWebSocketHandler {
	
	private Logger log = LoggerFactory.getLogger(CustomerHandler.class);
	
	@Autowired
	private CustomerPool customerPool;
	
	@Autowired
	private SupporterPool supporterPool;
	
	@Autowired
	private TalkService talkService;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		TextMessage message = MessageFactory.generateMessage(session, 
				null, MessageFactory.MESSAGE_TYPE_CONNECT, null);
		if (message != null) {
			session.sendMessage(message);
		}
		customerPool.addCustomer(session);
		talkService.connectionOpen(session, Constant.DOMAIN_TYPE.CONNECTION_TYPE_CUSTOMER);
		log.info("Customer " + session.getId() + " connected.");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {		
		log.info("Receive from " + session.getId() + ": " + message.getPayload());
		//获取聊天内容
		Message tb = null;
		String content = message.getPayload();
		if (StringUtils.isNotEmpty(content)) {
			tb = Util.parseMessage(content, Message.class);
		}
		if (tb == null) {
			return;
		}
		//获取聊天对象
		WebSocketSession talker = (WebSocketSession)session.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_TALKER_OF_CUSTOMER);
		//插入未发送队列
		if (talker == null || !talker.isOpen()) {
			Util.setUnsetMessage(session, (String)tb.getContent().get(Message.CONTENT_KEY_TALK_CONTENT));
			if (tb != null) {
				talkService.saveTalk(session, null, Constant.DOMAIN_TYPE.TALK_TYPE_CUSTOMER_TO_SUPPORTER, tb.getContent().get(Message.CONTENT_KEY_TALK_CONTENT));
				log.info(session.getId() + " To no one : " + tb.getContent().get(Message.CONTENT_KEY_TALK_CONTENT));
			}
		} 
		//发送聊天内容
		else {
			talker.sendMessage(MessageFactory.generateMessage(session, talker, 
					MessageFactory.MESSAGE_TYPE_TALK_TO_SUPPORTER, 
					tb.getContent().get(Message.CONTENT_KEY_TALK_CONTENT)));
			talkService.saveTalk(session, talker, Constant.DOMAIN_TYPE.TALK_TYPE_CUSTOMER_TO_SUPPORTER, tb.getContent().get(Message.CONTENT_KEY_TALK_CONTENT));
			log.info(session.getId() + " To " + talker.getId() + " : " + tb.getContent().get(Message.CONTENT_KEY_TALK_CONTENT));
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		customerPool.removeCustomer(session);
		WebSocketSession talker = (WebSocketSession)session.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_TALKER_OF_CUSTOMER);
		supporterPool.freeSupporter(talker);
		if (talker != null && talker.isOpen()) {
			TextMessage message = MessageFactory.generateMessage(session, talker, MessageFactory.MESSAGE_TYPE_CLOSE_CUSTOMER_TO_SUPPORTER, "");
			talker.sendMessage(message);
		}
		talkService.connectionClose(session);
		log.info("Customer " + session.getId() + " closed.");
	}
}
