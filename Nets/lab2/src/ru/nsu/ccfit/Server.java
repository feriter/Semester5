package ru.nsu.ccfit;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Thread {
    private final ServerSocket socket;

    public Server(int port) throws IOException {
        socket = new ServerSocket(port);
    }

    public void run() {
        while (true) {
            try {
                var clientSocket = socket.accept();
                System.out.println("server found new connection");
                var handler = new InputHandler(clientSocket);
                handler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
