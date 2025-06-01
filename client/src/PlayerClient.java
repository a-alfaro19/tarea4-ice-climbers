import java.io.IOException;

public class PlayerClient extends Client {
    public PlayerClient(String host, int port) throws IOException {
        super(host, port);
    }

    @Override
    public void identify() throws IOException {
        out.write("PLAYER".getBytes());
        String response = getResponse();
        if (!"ACCEPTED".equals(response)) {
            throw new IOException("Player Client not registered: " + response);
        }
    }
}
