public class ClientFactory {
    public static IClient createClient(String type, String address, int port) {
        if ("default".equalsIgnoreCase(type)) {
            return new Client(address, port);
        }

        throw new IllegalArgumentException("Unknown client type: " + type);

    }
}