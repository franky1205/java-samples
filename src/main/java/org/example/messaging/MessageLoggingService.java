package org.example.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author Frankie Chao
 */
@Slf4j
@Component
public class MessageLoggingService {

    public void accept(Message<String> message) {
        log.info("Message Payload: [{}] and Headers: [{}]", message.getPayload(), message.getHeaders());
    }
}
