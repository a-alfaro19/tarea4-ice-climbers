import model.Juego;
import ui.GameWindow;

import javax.swing.*;
import java.io.*;

public class PlayerClient extends Client implements IClient {
    private String nombreJugador;

    public PlayerClient(String host, int port) throws IOException {
        super(host, port);

    }

    @Override
    public void identify() throws IOException {
        out.write("PLAYER".getBytes());
        out.flush();

        // Leer primero si fue aceptado
        StringBuilder response = new StringBuilder();
        char ch;
        while ((ch = (char) in.readByte()) != '\n') {
            response.append(ch);
        }

        if (!"ACCEPTED".equals(response.toString().trim())) {
            throw new IOException("Jugador rechazado: " + response);
        }

        // Leer nombre exacto de 10 bytes
        byte[] nombreBytes = new byte[10];
        in.readFully(nombreBytes);
        this.nombreJugador = new String(nombreBytes).trim();
        System.out.println("Jugador asignado: " + nombreJugador);
    }




    @Override
    public void sendRequest(String request) throws IOException {
        out.write((request + "\n").getBytes());
        out.flush();
    }

    @Override
    public String getResponse() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);
        return new String(buffer, 0, bytesRead);
    }


    public void startListening() {
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //
            String nombre = this.nombreJugador;


            SwingUtilities.invokeLater(() -> {
                System.out.println("Creando ventana para " + nombreJugador);
                GameWindow window = new GameWindow(nombreJugador, output);


                // Juego de prueba para mostrar algo inicial
                Juego juegoPrueba = crearJuegoDePrueba();
                window.updateGame(juegoPrueba);

                // Hilo para recibir actualizaciones del servidor
                new Thread(() -> {
                    try {
                        while (true) {
                            Juego juego = Juego.readFrom(in);
                            window.updateGame(juego);
                        }
                    } catch (IOException e) {
                        System.err.println("Conexión perdida con el servidor.");
                    }
                }).start();
            });

        } catch (IOException e) {
            System.err.println("Error iniciando la ventana del juego: " + e.getMessage());
        }
    }


    private Juego crearJuegoDePrueba() {
        model.Jugador popo = new model.Jugador();
        popo.nombre = "Popo";
        popo.x = 3;
        popo.y = 8;

        model.Jugador nana = new model.Jugador();
        nana.nombre = "Nana";
        nana.x = 6;
        nana.y = 8;

        model.Juego juego = new model.Juego();
        juego.jugadores = new model.Jugador[]{popo, nana};
        juego.nivelActual = 0;
        juego.enFaseBonus = 0;
        juego.velocidad = 1;
        return juego;
    }
}