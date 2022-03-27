package org.example.messaging;

/**
 * @author Frankie Chao
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Component
public class MessageAggregator {

    public String aggregate(List<String> messages) {
        log.info("Get all aggregated list of messages: [{}]", messages);
        if (CollectionUtils.isEmpty(messages)) {
            return "";
        }
        return String.join("", messages);
    }
}
