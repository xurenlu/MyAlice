package org.myalice.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.myalice.websocket.message.MessageFactory;
import org.myalice.websocket.service.TalkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class AssignManager {
	
	private Logger log = LoggerFactory.getLogger(AssignManager.class);
	
	//private static LinkedBlockingDeque<WebSocketSession> assignCustomerInfo = new LinkedBlockingDeque<WebSocketSession>(1000);
	
	//private static LinkedBlockingDeque<WebSocketSession> assignSupporterInfo = new LinkedBlockingDeque<WebSocketSession>(1000);
	
	@Value("${websocket.customer.unset.limit:100}")
	private int unsetMessageLimit;
	
	@Value("${websocket.assign.frequence:1000}")
	private int assignFrequence;
	
	@Autowired
	private CustomerPool customerPool;
	
	@Autowired
	private SupporterPool supporterPool;
	
	@Autowired
	private TalkService talkService;
	
	@Scheduled(fixedRate = 1000)
	public void assignTask() throws IOException, InterruptedException {
		assignSession();
	}
	
	@Scheduled(fixedRate = 5000)
	public void saveAssignInfo() {
		/*WebSocketSession customer = null;
		WebSocketSession supporter = null;
		while (assignCustomerInfo.size() > 0 && assignSupporterInfo.size() > 0) {
			customer = assignCustomerInfo.poll();
			supporter = assignSupporterInfo.poll();
			talkService.assign(customer, supporter);
		}*/
	}
	
	private void assignSession() throws IOException, InterruptedException {
		WebSocketSession customer = customerPool.getUnassignedCustomer();
		while (customer != null && customer.isOpen()) {
			WebSocketSession supporter = supporterPool.getFreeSupporter();
			//在找不到空闲的客服人员时，客户继续等待
			if (supporter == null) {
				customerPool.freeCustomer(customer);
				break;
			}
			
			//设置客户、客服关联关系
			setMapping(customer, supporter);
			//发送分配消息
			sendAssignMessage(customer, supporter);
			//向客服发送未接收的信息
			sendUnsetMessage(customer, supporter);
			
			/*assignCustomerInfo.put(customer);
			assignSupporterInfo.put(supporter);*/
			talkService.assign(customer, supporter);
			
			customer = customerPool.getUnassignedCustomer();
		}
	}
	
	/**
	 * 向客户、客服发送对接消息
	 * @param customer
	 * @param supporter
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private void sendAssignMessage(WebSocketSession customer, WebSocketSession supporter) throws JsonProcessingException, IOException {
		TextMessage customerMessage = MessageFactory.generateMessage(customer, supporter, MessageFactory.MESSAGE_TYPE_ASSIGN_TO_CUSTOMER, null); 
		TextMessage supporterMessage = MessageFactory.generateMessage(customer, supporter, MessageFactory.MESSAGE_TYPE_ASSIGN_TO_SUPPORTER, null);
		customer.sendMessage(customerMessage);
		supporter.sendMessage(supporterMessage);
		log.info("ASSIGN CUSTOMER  : " + customerMessage.getPayload());
		log.info("ASSIGN SUPPORTER : " + supporterMessage.getPayload());
	}
	
	private void setMapping(WebSocketSession customer, WebSocketSession supporter) {
		if (customer == null || supporter == null 
				|| !customer.isOpen() || !supporter.isOpen()) {
			return;
		}
		//一个客户对应一个客服
		customer.getAttributes().put(Constant.WS_SESSION_KEY.SESSION_KEY_TALKER_OF_CUSTOMER, supporter);
		//一个客服对应多个客户
		@SuppressWarnings("unchecked")
		Map<String, WebSocketSession> supporterTalkers = ((Map<String, WebSocketSession>)supporter.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_TALKER_OF_SUPPORTER));
		if (supporterTalkers == null) {
			supporterTalkers = new HashMap<String, WebSocketSession>();
			supporter.getAttributes().put(Constant.WS_SESSION_KEY.SESSION_KEY_TALKER_OF_SUPPORTER, supporterTalkers);
		}
		supporterTalkers.put(customer.getId(), customer);
	}
	
	private void sendUnsetMessage(WebSocketSession customerSession, WebSocketSession supporterSession) {
		if (customerSession == null || supporterSession == null 
				|| !customerSession.isOpen() || !supporterSession.isOpen()) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		ArrayBlockingQueue<String> unsendMessages = (ArrayBlockingQueue<String>)customerSession.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_UNSET_MESSAGES);
		if (unsendMessages != null && unsendMessages.size() > 0) {
			for (String message : unsendMessages) {
				try {
					supporterSession.sendMessage(MessageFactory.generateMessage(customerSession, supporterSession, MessageFactory.MESSAGE_TYPE_TALK_TO_SUPPORTER, message));
				} catch (IOException e) {
					log.error("Send unset messsage error : from " + customerSession.getId() 
						+ " to " + supporterSession.getId(), e);
				}
			}
			unsendMessages.clear();
		}
	}
}
