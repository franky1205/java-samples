package org.example.messaging;

/**
 * @author Frankie Chao
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MessageAggregator {

    public String aggregate(List<Message<String>> messages) {
        log.info("Get all aggregated list of messages: [{}]", messages);
        if (CollectionUtils.isEmpty(messages)) {
            return "";
        }
        return messages.stream()
                .map(Message::getPayload)
                .collect(Collectors.joining(""));
    }
}
