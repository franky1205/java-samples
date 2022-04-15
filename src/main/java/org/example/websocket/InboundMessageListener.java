package org.example.websocket;

import org.springframework.web.reactive.socket.WebSocketMessage;

/**
 * @author Frankie Chao
 */
public interface InboundMessageListener {

    void receive(String sessionId, WebSocketMessage webSocketMessage);
}
