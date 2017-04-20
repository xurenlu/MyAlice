package org.myalice.websocket;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
	
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(Util.class);

	private static ObjectMapper mapper = new ObjectMapper(); 
	
	public static <T> T parseMessage(String message, Class<T> cls) throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isEmpty(message)) {
			return null;
		}
		return mapper.readValue(message, cls);
	}
	
	/**
	 * 将存储在bean中的值转为JSON字符串
	 * @param message
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String formatMessage(Object message) throws JsonProcessingException {
		if (message == null) {
			return "";
		}
		return mapper.writeValueAsString(message);
	}
	
	/**
	 * 将聊天内容加入未发送聊天队列
	 * @param session
	 * @param message
	 */
	public static void setUnsetMessage(WebSocketSession session, String message) {
		@SuppressWarnings("unchecked")
		ArrayBlockingQueue<String> talkContent = 
			(ArrayBlockingQueue<String>)session.getAttributes()
			.get(Constant.WS_SESSION_KEY.SESSION_KEY_UNSET_MESSAGES);
		if (talkContent != null) {
			while (!talkContent.offer(message)) {
				talkContent.poll();
			}
		}
	}
	
	/**
	 * 创建随机 UUID
	 * @return
	 */
	public static String randomUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 获取用户ID
	 * @param session
	 * @return
	 */
	public static String getUserId(WebSocketSession session) {
		if (session == null) {
			return null;
		}
		String userId = (String)session.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_USER_ID);
		return userId == null ? "" : userId;
	}
	
	/**
	 * 获取用户名
	 * @param session
	 * @return
	 */
	public static String getUserName(WebSocketSession session) {
		if (session == null) {
			return null;
		}
		String userName = (String)session.getAttributes().get(Constant.WS_SESSION_KEY.SESSION_KEY_USER_NAME);
		return userName == null ? session.getId() : userName;
	}
	
	/**
	 * 获取客户端IP地址
	 * @param session
	 * @return
	 */
	public static String getRemoteIp(WebSocketSession session) {
		if (session == null) {
			return null;
		}
		String remoteIp = session.getRemoteAddress().getAddress().getHostAddress();
		return remoteIp == null ? "" : remoteIp;
	}
	
	/**
	 * 获取服务端IP地址
	 * @param session
	 * @return
	 */
	public static String getLocalIp(WebSocketSession session) {
		if (session == null) {
			return null;
		}
		String localIp = session.getLocalAddress().getAddress().getHostAddress();
		return localIp == null ? "" : localIp;
	}
}
