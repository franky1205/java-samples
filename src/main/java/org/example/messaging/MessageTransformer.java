package org.example.messaging;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
        try {
            TimeUnit.SECONDS.sleep((long)(5 * Math.random()));
        } catch (InterruptedException e) {
            log.info("Ignore interruption.");
        }
        log.info("Transform payload message with headers: [{}] from: [{}] to [{}]", message.getHeaders(), message.getPayload(), resultMsg);
        return MessageBuilder.createMessage(resultMsg, message.getHeaders());
    }
}
