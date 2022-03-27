package org.example.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Frankie Chao
 */
@Slf4j
@Component
public class MessageDiscardingService {

    public void accept(List<String> partialMessages) {
        log.warn("Message discarded with size: [{}] and messages: [{}]", partialMessages.size(), partialMessages);
    }
}
