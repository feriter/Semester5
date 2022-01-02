package ru.nsu.ccfit.GH;

import ru.nsu.ccfit.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ButtonPressListener implements ActionListener {
    private final String owmApiKey = "743901209fe5b20df60cef133334fca1";
    private final String otmApiKey = "5ae2e3f221c38a28845f05b62d74b058328136e597d1168993723a42";
    private final Hit hit;
    private HttpClient client;
    private MainWindow mainWindow;

    public ButtonPressListener(Hit h, HttpClient c, MainWindow m) {
        hit = h;
        client = c;
        mainWindow = m;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.openweathermap.org/data/2.5/weather" +
                        "?lat=" + hit.getPoint().getLat() +
                        "&lon=" + hit.getPoint().getLng() +
                        "&appid=" + owmApiKey))
                .GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(mainWindow::handleOWMResponse);
        var request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://api.opentripmap.com/0.1/ru/places/radius" +
                        "?lang=en" +
                        "&radius=500" +
                        "&lon=" + hit.getPoint().getLng() +
                        "&lat=" + hit.getPoint().getLat() +
                        "&apikey=" + otmApiKey))
                .GET()
                .build();
        client.sendAsync(request2, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(mainWindow::handleOTMResponse);
        mainWindow.getHitInfo().setHit(hit);
    }
}
