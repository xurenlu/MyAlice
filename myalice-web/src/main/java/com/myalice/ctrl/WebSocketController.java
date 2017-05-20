package com.myalice.ctrl;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.myalice.beans.Message;
import com.myalice.beans.Response;
import com.myalice.domain.TalkRecord;
import com.myalice.services.ESQuestionService;
import com.myalice.services.TalkRecordService;
import com.myalice.utils.MyAliceUtils;
import com.myalice.utils.Tools;

@Controller
public class WebSocketController {
	
	@Autowired
	protected SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	protected ESQuestionService questionService ;
	
	@Autowired
	protected TalkRecordService talkRecordService;
	
	@MessageMapping("/welcome")
	@SendTo("/topic/getResponse")
	public Response say(Message message) throws Exception {
		Thread.sleep(1000);
		return new Response("Welcome, " + message.getName() + "!");
	}

	@MessageMapping("/chat")
	// 在springmvc 中可以直接获得principal,principal 中包含当前用户的信息
	public void handleChat(Principal principal, Message message) {
		
		Map<String, Object> answer = questionService.searchAnswer(message.getName()) ;
		TalkRecord record = new TalkRecord();
		{
			Map<String,Object> responseMsg = new HashMap<>() ;
			responseMsg.put("date", Tools.currentDate());
			responseMsg.put("clazz", "even");
			responseMsg.put("name", principal.getName() ); 
			responseMsg.put("anwser", message.getName());
			record.setContent( message.getName() ); ;
			messagingTemplate.convertAndSendToUser(principal.getName()
					, "/queue/notifications",JSON.toJSONStringWithDateFormat(responseMsg, "yyyy-MM-dd HH:mm:ss") );
		}
		record.setUserId(principal.getName());
		record.setUserType("");
		record.setConnectionId("");
		Map<String,Object> responseMsg = new HashMap<>() ;
		responseMsg.put("date", Tools.currentDate());
		responseMsg.put("clazz", "odd");
		responseMsg.put("name", "系统管理员" );
		if(null != answer){
			record.setReplyType(1);
			responseMsg.put("anwser", answer.get("anwser"));
			messagingTemplate.convertAndSendToUser(principal.getName()
				, "/queue/notifications",JSON.toJSONStringWithDateFormat(responseMsg, "yyyy-MM-dd HH:mm:ss") );
		}else{
			record.setReplyType(0);
			responseMsg.put("anwser", "请重新描述您的问题");
			messagingTemplate.convertAndSendToUser(principal.getName()
					, "/queue/notifications",JSON.toJSONStringWithDateFormat(responseMsg, "yyyy-MM-dd HH:mm:ss") );   
		}
		
		record.setReply( MyAliceUtils.toString(responseMsg.get("anwser")));
		
		talkRecordService.insert( record ); 
		/*
		if (principal.getName().equals("admin")) {
			// 通过convertAndSendToUser 向用户发送信息,
			// 第一个参数是接收消息的用户,第二个参数是浏览器订阅的地址,第三个参数是消息本身
			messagingTemplate.convertAndSendToUser("basara", "/queue/notifications",
					principal.getName() + "-send:" + message.getName());
		} else {
			messagingTemplate.convertAndSendToUser("admin", "/queue/notifications",
					principal.getName() + "-send:" + message.getName());
		}
		*/
	}
}
