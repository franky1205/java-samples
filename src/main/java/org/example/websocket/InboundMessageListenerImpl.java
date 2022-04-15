package org.example.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketMessage;

/**
 * @author Frankie Chao
 */
@Slf4j
public class InboundMessageListenerImpl implements InboundMessageListener {

    @Override
    public void receive(String sessionId, WebSocketMessage webSocketMessage) {
        log.info("Get inbound message from ID/Context: [{}/{}]", sessionId, webSocketMessage.getPayloadAsText());
    }
}
