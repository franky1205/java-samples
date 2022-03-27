package org.example.messaging;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Frankie Chao
 */
@Slf4j
@Component
public class MessageTransformer {

    @Setter
    private String messageFormat = "_%s_";

    public Message<String> transform(Message<String> message) {
        final String resultMsg = String.format(messageFormat, message.getPayload());
        log.info("Transform payload message with headers: [{}] from: [{}] to [{}]", message.getHeaders(), message.getPayload(), resultMsg);
        return MessageBuilder.createMessage(resultMsg, message.getHeaders());
    }
}
