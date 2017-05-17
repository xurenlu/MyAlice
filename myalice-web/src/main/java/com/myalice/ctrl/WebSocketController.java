package com.myalice.ctrl;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.myalice.beans.Message;
import com.myalice.beans.Response;

@Controller
public class WebSocketController {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	// http://localhost:8080/ws
	@MessageMapping("/welcome") // 浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
	@SendTo("/topic/getResponse") // 服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
	public Response say(Message message) throws Exception {
		Thread.sleep(1000);
		return new Response("Welcome, " + message.getName() + "!");
	}

	@MessageMapping("/chat")
	// 在springmvc 中可以直接获得principal,principal 中包含当前用户的信息
	public void handleChat(Principal principal, Message message) {
		/**
		 * 此处是一段硬编码。如果发送人是admin 则发送给 basara 如果发送人是basara 就发送给 admin。
		 * 通过当前用户,然后查找消息,如果查找到未读消息,则发送给当前用户。
		 */
		if (principal.getName().equals("admin")) {
			// 通过convertAndSendToUser 向用户发送信息,
			// 第一个参数是接收消息的用户,第二个参数是浏览器订阅的地址,第三个参数是消息本身
			messagingTemplate.convertAndSendToUser("basara", "/queue/notifications",
					principal.getName() + "-send:" + message.getName());
		} else {
			messagingTemplate.convertAndSendToUser("admin", "/queue/notifications",
					principal.getName() + "-send:" + message.getName());
		}
	}
}
