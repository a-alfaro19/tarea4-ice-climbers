package client;

import model.Bloque;
import model.Juego;
import observer.GameObservable;
import observer.GameObserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObserverClient extends Client {
    private final GameObservable observable = new GameObservable();

    public ObserverClient(String host, int port) throws IOException {
        super(host, port);
    }

    @Override
    public String identify() throws IOException {
        out.write("OBSERVER\n".getBytes());
        out.flush();

        String response = getResponse().trim();
        if (!"ACCEPTED".equals(response)) {
            throw new IOException("Observer client not registered: " + response);
        }

        // Esperar int de 4 bytes indicando a quién observa (0 = Popo, 1 = Nana)
        int observandoA = readIntLE(in);
        return (observandoA == 0) ? "Popo" : "Nana";
    }

    public void addObserver(GameObserver observer) {
        observable.addObserver(observer);
    }

    public void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    Juego juego = Juego.readFrom(in);

                    int cantidad = readIntLE(in);
                    List<Bloque> bloques = new ArrayList<>();
                    for (int i = 0; i < cantidad; i++) {
                        bloques.add(Bloque.readFrom(in));
                    }

                    observable.notifyObservers(juego);  // Notifica a todos los observadores
                }
            } catch (IOException e) {
                System.err.println("Conexión perdida (observador): " + e.getMessage());
            }
        }).start();
    }

    private int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}

