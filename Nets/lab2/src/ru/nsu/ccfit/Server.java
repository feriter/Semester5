package ru.nsu.ccfit;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
