import java.io.*;
import java.net.Socket;

public class Client implements IClient {
    private final String address;
    private final int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;

    }

    @Override
    public void start() {
        Socket socket = null;
        BufferedReader input = null;
        BufferedWriter output = null;

        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            input = new BufferedReader(new InputStreamReader(System.in));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }

        // String to read message from input
        String m = "";

        // Keep reading until "Over" is input
        while (!m.equals("Over")) {
            try {
                m = input.readLine();
                output.write(m + '\n');
                output.flush();
            }
            catch (IOException i) {
                System.out.println(i);
            }
        }

        // Close the connection
        try {
            input.close();
            output.close();
            socket.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }
}
