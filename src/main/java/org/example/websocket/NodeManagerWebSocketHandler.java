package org.example.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Frankie Chao
 */
@Slf4j
public class NodeManagerWebSocketHandler implements WebSocketHandler {

    private final Map<String, Flux<WebSocketMessage>> messagePublishers = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        final String greeting = "Server Get inbound message: [%s]";
        return session.send(session.receive()
                        .doOnComplete(() -> log.info("Complete inbound connection."))
                        .map(WebSocketMessage::getPayloadAsText)
                        .map(textMessage -> String.format(greeting, textMessage))
                        .map(session::textMessage))
                .doOnSuccess(success -> log.info("Outbound connection Success."));
    }
}
