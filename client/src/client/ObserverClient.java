package client;

import java.io.IOException;

public class ObserverClient extends Client {
    public  ObserverClient(String host, int port) throws IOException {
        super(host, port);
    }

    @Override
    public void identify() throws IOException {
        out.write("OBSERVER".getBytes());
        String response = receiveMessage();
        if (!"ACCEPTED".equals(response)) {
            throw new IOException("Observer client.Client not registered: " + response);
        }
    }
}
