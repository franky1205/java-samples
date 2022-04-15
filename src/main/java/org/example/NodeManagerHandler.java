package org.example;

import java.util.List;

/**
 * @author Frankie Chao
 */
public interface NodeManagerHandler {

    List<String> getSessionIds();

    void sendBroadcastMessage(String message);

    void sendDirectMessage(String sessionId, String message);
}
