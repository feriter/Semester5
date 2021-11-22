package ru.nsu.ccfit;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) {
        try {
            new File("uploads").mkdir();
            new Server(1435).start();
            new Client(InetAddress.getLocalHost(), 1435, "astah1.exe").start();
            new Client(InetAddress.getLocalHost(), 1435, "astah2.exe").start();
            new Client(InetAddress.getLocalHost(), 1435, "astah3.exe").start();
            new Client(InetAddress.getLocalHost(), 1435, "astah4.exe").start();
            new Client(InetAddress.getLocalHost(), 1435, "astah5.exe").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
