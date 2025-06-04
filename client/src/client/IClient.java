package client;

import ui.Tile;

import java.io.IOException;

public interface IClient {
    /**
     * Sends the client.Client Identification to the server.
     */
    void identify() throws IOException;

    /**
     * Receive the Game Map from the Server
     * @return Game Map from the game
     */
    Tile[][] receiveMap() throws IOException;

    /**
     * Receives and sets Game data from Server
     */
    void fetch() throws IOException;

    /**
     * Sends a request to the server.
     * @param request Request to send to the server.
     */
    void sendMessage(String request) throws IOException;

    /**
     * Receive the server response.
     * @return String with the server response.
     */
    String receiveMessage() throws IOException;


}
