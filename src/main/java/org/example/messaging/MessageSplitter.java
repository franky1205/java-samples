package org.example.messaging;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Frankie Chao
 */
@Component
public class MessageSplitter {

    @Setter
    private String delimiter = ",";

    public List<String> splitting(String message) {
        return Arrays.asList(message.split(delimiter));
    }
}
