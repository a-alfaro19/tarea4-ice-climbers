import java.io.IOException;

public class ClientFactory {
    public static IClient createClient(String type, String host, int port) throws IOException {
        return switch (type.toUpperCase()) {
            case "PLAYER" -> new PlayerClient(host, port);
            case "OBSERVER" -> new ObserverClient(host, port);
            default -> throw new IllegalArgumentException("Invalid Client Type: " + type);
        };
    }
}