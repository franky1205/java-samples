package org.example.websocket;

import lombok.extern.slf4j.Slf4j;
import org.example.NodeManagerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Frankie Chao
 */
@Slf4j
public class NodeManagerWebSocketHandler implements WebSocketHandler, NodeManagerHandler {

    private final Map<WebSocketSession, FluxSink<WebSocketMessage>> messagePublishers = new ConcurrentHashMap<>();

    private InboundMessageListener inboundMessageListener;

    @Autowired
    public void setInboundMessageListener(InboundMessageListener inboundMessageListener) {
        this.inboundMessageListener = inboundMessageListener;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        session.receive()
                .doOnComplete(() -> closeSink(session))
                .subscribe(webSocketMessage -> this.inboundMessageListener.receive(session.getId(), webSocketMessage));

        return session.send(Flux.create(sink -> messagePublishers.put(session, sink)));
    }

    @Override
    public List<String> getSessionIds() {
        return this.messagePublishers.keySet().stream()
                .map(WebSocketSession::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void sendBroadcastMessage(String message) {
        this.messagePublishers.forEach((session, sink) -> sink.next(session.textMessage(message)));
    }

    @Override
    public void sendDirectMessage(String sessionId, String message) {
        this.messagePublishers.keySet().stream()
                .filter(sessionObj -> sessionObj.getId().equals(sessionId))
                .findAny()
                .ifPresent(webSocketSession -> this.messagePublishers.get(webSocketSession)
                        .next(webSocketSession.textMessage(message)));
    }

    private void closeSink(WebSocketSession session) {
        Optional.ofNullable(messagePublishers.get(session))
                .ifPresent(FluxSink::complete);
        messagePublishers.remove(session);
        log.info("Connection closed with Session Id: [{}]", session.getId());
    }
}
