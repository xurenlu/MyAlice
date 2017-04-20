package org.myalice.websocket;

import org.myalice.websocket.handler.CustomerHandler;
import org.myalice.websocket.handler.SupporterHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@PropertySource("classpath:/websocket.properties")
public class WebSocketConfig implements WebSocketConfigurer {
	
	@Autowired
	private CustomerHandler customerHandler;
	
	@Autowired
	private SupporterHandler supporterHandler;
	
	@Value("${websocket.endpoint.customer:/customer}")
	private String customerEndPoint;
	
	@Value("${websocket.endpoint.supporter:/supporter}")
	private String supporterEndPoint;

	@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customerHandler, customerEndPoint).
        	addHandler(supporterHandler, supporterEndPoint).
        	addInterceptors(new HttpSessionHandshakeInterceptor()).
        	setAllowedOrigins("*").
        	withSockJS();
    }
	
	@Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        container.setMaxSessionIdleTimeout(1000 * 60 * 5);
        return container;
    }
}