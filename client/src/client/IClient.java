package client;

import java.io.IOException;

public interface IClient {
    /**
     * Sends the client.Client Identification to the server.
     */
    void identify() throws IOException;

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
}
