package client;

import observer.GameObserver;

import java.io.IOException;

public interface IClient {
    /**
     * Sends the client.Client Identification to the server.
     *
     * @return
     */
    String identify() throws IOException;

    /**
     * Sends a request to the server.
     * @param request Request to send to the server.
     */
    void sendRequest(String request) throws IOException;

    /**
     * Receive the server response.
     * @return String with the server response.
     */
    String getResponse() throws IOException;

    /**
     * Optional: Adds a game observer (used by ObserverClient only).
     */
    default void addObserver(GameObserver observer) {
    }
}
