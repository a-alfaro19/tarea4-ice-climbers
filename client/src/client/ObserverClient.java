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

    //Metodo requerido por IClient
    @Override
    public String identify() throws IOException {
        return identify("OBSERVER"); // por defecto, modo 1 jugador
    }

    // Metodo extendido para selección Popo/Nana
    public String identify(String tipoObservador) throws IOException {
        out.write((tipoObservador + "\n").getBytes());
        out.flush();

        // Leer respuesta: ACCEPTED o REJECTED
        StringBuilder response = new StringBuilder();
        char ch;
        while ((ch = (char) in.readByte()) != '\n') {
            response.append(ch);
        }

        if (!"ACCEPTED".equals(response.toString().trim())) {
            throw new IOException("\nIntente más tarde o con otro jugador. ");
        }

        // Leer int LE indicando a quién se observa
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
