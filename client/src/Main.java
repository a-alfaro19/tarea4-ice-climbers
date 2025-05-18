public class Main {
    public static void main(String[] args) {
        IClient client = ClientFactory.createClient("default", "localhost", 8080);
        client.start();
    }
}