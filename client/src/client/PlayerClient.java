package client;

import model.Fruta;
import model.Juego;
import model.Bloque;
import ui.GameWindow;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerClient extends Client implements IClient {
    private String nombreJugador;
    private boolean dosJugadores = false;

    public PlayerClient(String host, int port) throws IOException {
        super(host, port);
    }

    @Override
    public String identify() throws IOException {
        if (dosJugadores) {
            out.write("PLAYER2\n".getBytes());
        } else {
            out.write("PLAYER1\n".getBytes());
        }

        out.flush();

        // Leer respuesta tipo "ACCEPTED\n"
        StringBuilder response = new StringBuilder();
        char ch;
        while ((ch = (char) in.readByte()) != '\n') {
            response.append(ch);
        }

        if (!"ACCEPTED".equals(response.toString().trim())) {
            throw new IOException("");
        }

        // Leer nombre exacto de 10 bytes
        byte[] nombreBytes = new byte[10];
        in.readFully(nombreBytes);
        this.nombreJugador = new String(nombreBytes).trim();
        System.out.println("Jugador asignado: " + nombreJugador);
        return null;
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

            SwingUtilities.invokeLater(() -> {
                System.out.println("Creando ventana para " + nombreJugador);
                GameWindow window = new GameWindow(nombreJugador, output, this.dosJugadores);

                new Thread(() -> {
                    try {
                        while (true) {
                            Juego juego = Juego.readFrom(in);
                            window.updateGame(juego);

                            int cantidad = readIntLE(in);
                            if (cantidad < 0 || cantidad > 3001) {
                                throw new IOException("Cantidad inválida de bloques: " + cantidad);
                            }

                            List<Bloque> bloques = new ArrayList<>();
                            for (int i = 0; i < cantidad; i++) {
                                bloques.add(Bloque.readFrom(in));
                            }
                            window.updateBloques(bloques);
                        }
                    } catch (IOException e) {
                        System.err.println("Conexión perdida con el servidor: " + e.getMessage());
                    }
                }).start();
            });

        } catch (IOException e) {
            System.err.println("Error iniciando la ventana del juego: " + e.getMessage());
        }
    }
    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setDosJugadores(boolean valor) {
        this.dosJugadores = valor;
    }

    public boolean isDosJugadores() {
        return dosJugadores;
    }

    private int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) |(b3 << 16) | (b2 << 8) | b1;
    }
}

