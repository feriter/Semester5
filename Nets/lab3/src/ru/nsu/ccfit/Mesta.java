package ru.nsu.ccfit;

import java.net.http.HttpClient;

public class Mesta {
    private final MainWindow mainWindow;

    public Mesta() {
        var client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        mainWindow = new MainWindow(client);
        mainWindow.setVisible(true);
    }
}
