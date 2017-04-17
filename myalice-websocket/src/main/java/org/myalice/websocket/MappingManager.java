package org.myalice.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.myalice.websocket.message.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class MappingManager {
	
	private Logger log = LoggerFactory.getLogger(MappingManager.class);

	/**
	 * 待处理客户对话
	 */
	private LinkedBlockingQueue<WebSocketSession> customerQueue = null;
	
	/**
	 * 待处理客服对话
	 */
	private LinkedBlockingQueue<WebSocketSession> supporterQueue = null;
	
	@Value("${websocket.customer.connection.limit:1000}")
	private int customerConnectionLimit;
	
	@Value("${websocket.customer.connection.limit:1000}")
	private int supporterConnectionLimit;
	
	@Value("${websocket.supporter.workload.limit:10}")
	private int workloadLimit;
	
	@Value("${websocket.customer.unset.limit:100}")
	private int unsetMessageLimit;
	
	@Value("${websocket.assign.frequence:1000}")
	private int assignFrequence;
	
	@PostConstruct
	public void init() {
		customerQueue = new LinkedBlockingQueue<WebSocketSession>(customerConnectionLimit);
		supporterQueue = new LinkedBlockingQueue<WebSocketSession>(supporterConnectionLimit);
	}
	
	@SuppressWarnings("resource")
	synchronized public boolean addCustomer(WebSocketSession session) throws IOException {
		if (session == null) {
			return false;
		}
		if (!customerQueue.add(session)) {
			return false;
		}
		session.getAttributes().put(Constant.SESSION_KEY_UNSET_MESSAGES, 
				new ArrayBlockingQueue<TextMessage>(unsetMessageLimit));
		if (supporterQueue.size() > 0) {
			WebSocketSession supporterSession = supporterQueue.poll();
			while (!supporterSession.isOpen() && supporterQueue.size() > 0 
					&& isNotFull(supporterSession)) 
				{supporterSession = supporterQueue.poll();}
			
			if (supporterSession != null && supporterSession.isOpen() 
					&& isNotFull(supporterSession)) {
				assignSession(session, supporterSession);
				return true;
			}
		}
		return false;
	} 
	
	@SuppressWarnings("resource")
	synchronized public boolean addSupporter(WebSocketSession session) throws IOException {
		if (session == null) {
			return false;
		}
		if (!supporterQueue.add(session)) {
			return false;
		}
		session.getAttributes().put(Constant.SESSION_KEY_SUPPORTER_WORKLOAD, 
				new AtomicInteger(0));
		session.getAttributes().put(Constant.SESSION_KEY_TALKER_OF_SUPPORTER, 
				new HashMap<String, WebSocketSession>());
		if (customerQueue.size() > 0) {
			WebSocketSession customerSession = null;
			while (isNotFull(session) && customerQueue.size() > 0) {
				customerSession = customerQueue.poll();
				while (!customerSession.isOpen() && customerQueue.size() > 0) 
					{customerSession = customerQueue.poll();}
			
				if (customerSession != null && customerSession.isOpen()) {
					assignSession(customerSession, session);
					return true;
				}
			}
		}
		return false;
	}
	
	synchronized public boolean closeCustomer(WebSocketSession session) throws IOException {
		if (session == null) {
			return false;
		}
		if (session.isOpen()) {
			session.close();
		}
		unassignSession(session);
		return true;
	}
	
	synchronized public boolean closeSupporter(WebSocketSession session) throws IOException {
		if (session == null) {
			return false;
		}
		if (session.isOpen()) {
			session.close();
		}
		@SuppressWarnings("unchecked")
		Map<String, WebSocketSession> customerSessions = (Map<String, WebSocketSession>)session.
			getAttributes().get(Constant.SESSION_KEY_TALKER_OF_SUPPORTER);
		if (customerSessions != null && customerSessions.size() > 0) {
			for (Entry<String, WebSocketSession> item : customerSessions.entrySet()) {
				WebSocketSession itemValue = item.getValue();
				if (itemValue != null && itemValue.isOpen()) {
					customerQueue.add(itemValue);
				}
			}
		}
		return true;
	}
	
	@SuppressWarnings("resource")
	synchronized public boolean assign() throws IOException {
		if (customerQueue.size() > 0 && supporterQueue.size() > 0) {
			WebSocketSession customerSession = customerQueue.poll();
			while (!customerSession.isOpen() && customerQueue.size() > 0) 
				{customerSession = customerQueue.poll();}
			WebSocketSession supporterSession = supporterQueue.poll();
			while (!supporterSession.isOpen() && supporterQueue.size() > 0 && isNotFull(supporterSession)) 
				{supporterSession = supporterQueue.poll();}
			log.info("Assign customer : " + customerSession.getId() + " to supporter : " + supporterSession.getId());
			assignSession(customerSession, supporterSession);
			return true;
		}
		return false;
	}
	
	@Scheduled(fixedRate = 1000)
	public void assignTask() throws IOException {
		assign();
	}
	
	private boolean isNotFull(WebSocketSession supporterSession) {
		AtomicInteger curWorkload = (AtomicInteger)supporterSession.getAttributes().get(Constant.SESSION_KEY_SUPPORTER_WORKLOAD);
		if (curWorkload.intValue() < 0) {
			curWorkload.set(0);
		}
		return curWorkload.intValue() < workloadLimit;
	}
	
	@SuppressWarnings("unchecked")
	private boolean assignSession(WebSocketSession customerSession, WebSocketSession supporterSession) throws IOException {
		if (customerSession != null && customerSession.isOpen() && supporterSession != null && supporterSession.isOpen()) {
			//设置客户和客服的映射关系
			customerSession.getAttributes().put(Constant.SESSION_KEY_TALKER_OF_CUSTOMER, supporterSession);
			
			((Map<String, WebSocketSession>)supporterSession.getAttributes().get(Constant.SESSION_KEY_TALKER_OF_SUPPORTER))
				.put(customerSession.getId(), customerSession);
			AtomicInteger curWorkload = (AtomicInteger)supporterSession.getAttributes().get(Constant.SESSION_KEY_SUPPORTER_WORKLOAD);
			curWorkload.incrementAndGet();
			//向客户端发送对接消息
			customerSession.sendMessage(MessageFactory.generateMessage(customerSession, supporterSession, MessageFactory.MESSAGE_TYPE_ASSIGN_TO_CUSTOMER, null));
			supporterSession.sendMessage(MessageFactory.generateMessage(customerSession, supporterSession, MessageFactory.MESSAGE_TYPE_ASSIGN_TO_SUPPORTER, null));
			//向客服发送未发送的消息
			ArrayBlockingQueue<TextMessage> unsendMessages = (ArrayBlockingQueue<TextMessage>)customerSession.getAttributes().get(Constant.SESSION_KEY_UNSET_MESSAGES); 
			if (unsendMessages != null && unsendMessages.size() > 0) {
				for (TextMessage message : unsendMessages) {
					supporterSession.sendMessage(message);
				}
				unsendMessages.clear();
			}
			return true;
		}
		return false;
	}
	
	private void unassignSession(WebSocketSession customerSession) {
		if (customerSession != null) {
			WebSocketSession supporterSession = (WebSocketSession)customerSession.getAttributes().get(Constant.SESSION_KEY_TALKER_OF_CUSTOMER);
			
			if (supporterSession != null && supporterSession.isOpen()) {
				AtomicInteger curWorkload = (AtomicInteger)supporterSession.getAttributes().get(Constant.SESSION_KEY_SUPPORTER_WORKLOAD);
				if (curWorkload.addAndGet(-1) < 0) {
					curWorkload.set(0);
				}
				@SuppressWarnings("unchecked")
				Map<String, WebSocketSession> customers = (Map<String, WebSocketSession>)supporterSession.getAttributes().get(Constant.SESSION_KEY_TALKER_OF_SUPPORTER);
				if (customers != null && customers.size() > 0) {
					customers.remove(customerSession.getId());
				}
				supporterQueue.add(supporterSession);
			}
		}
	}
}
