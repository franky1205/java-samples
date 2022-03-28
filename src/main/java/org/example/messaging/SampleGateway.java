package org.example.messaging;

/**
 * @author Frankie Chao
 */
public interface SampleGateway {

    String MSG_ID_HEADER = "MSG_ID";

    void perform(String message, String id);
}
