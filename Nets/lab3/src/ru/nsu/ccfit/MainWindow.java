package ru.nsu.ccfit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.http.*;

import com.google.gson.Gson;
import ru.nsu.ccfit.GH.*;
import ru.nsu.ccfit.OTM.*;
import ru.nsu.ccfit.OWM.*;

public class MainWindow extends JFrame {
    private final SpringLayout layout = new SpringLayout();
    private final Gson g = new Gson();
    private final JTextField fieldToEnterText = new JTextField();
    private final HttpClient client;
    private final String ghApiKey = "96a9c6e7-d7c5-460e-a7bc-70142ebe3aef";
    private final HitList hitList;
    private final HitInfo hitInfo;

    public HitInfo getHitInfo() {
        return hitInfo;
    }

    public class HitInfo extends JPanel {
        private Hit hit = new Hit();
        private SpringLayout innerLayout;
        private JTextArea weatherInfo = new JTextArea();
        private JTextArea places = new JTextArea();

        public HitInfo() {
            super();
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setVisible(true);
            innerLayout = new SpringLayout();
            this.setLayout(innerLayout);

            this.add(weatherInfo);
            this.add(places);

            weatherInfo.setVisible(true);
            weatherInfo.setLineWrap(true);

            places.setVisible(true);
            places.setLineWrap(true);

            CustomPlacing.placeComponent(innerLayout, this, weatherInfo, 5, 5, 550, 100);
            CustomPlacing.placeComponent(innerLayout, this, places, 5, 110, 550, 400);
        }

        public void setHit(Hit h) {
            hit = h;
        }

        public void refreshWeather(OWMResponse response) {
            weatherInfo.setText(hit.getName() + ", " +
                    hit.getCountry() + ", " +
                    hit.getState() + ", " +
                    hit.getCity() + "\n" +
                    "Temperature: " + (response.getMain().getTemp() - 273) + " C\n" +
                    "Pressure: " + response.getMain().getPressure() + "\n" +
                    "Humidity: " + response.getMain().getHumidity() + "%\n" +
                    "Weather: " + response.getWeather()[0].getMain() + ", " +
                    response.getWeather()[0].getDescription() + "\n");
            setVisible(true);
        }

        public void refreshPlaces(OTMResponse response) {
            places.setText("");
            for (var place : response.getFeatures()) {
                places.append(place.getProperties().getName() + "\n" +
                        "Distance: " + place.getProperties().getDist() + "\n\n");
            }
            setVisible(true);
        }
    }

    public class HitList extends JPanel {
        private Hit[] hits;
        private SpringLayout innerLayout;

        public HitList() {
            super();
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setVisible(true);
            innerLayout = new SpringLayout();
            this.setLayout(innerLayout);
        }

        public void refresh(Hit[] h) {
            this.removeAll();
            hits = h;
            for (int i = 0; i < hits.length; ++i) {
                var b = new HitListButton();
                b.setVisible(true);
                b.setText((i + 1) + ". " + hits[i].getName());
                b.setHorizontalAlignment(SwingConstants.LEFT);
                b.addActionListener(new ButtonPressListener(hits[i], client, MainWindow.this));
                CustomPlacing.placeComponent(innerLayout, this, b, 5, 10 + 30 * i, 180, 25);
            }
        }
    }

    public MainWindow(HttpClient c) {
        super("Mesta");

        hitList = new HitList();
        hitList.setVisible(true);

        hitInfo = new HitInfo();
        hitInfo.setVisible(true);
        hitInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        client = c;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 20, 900, 600);

        var container = getContentPane();
        container.setLayout(layout);
        CustomPlacing.placeComponent(layout, container, fieldToEnterText, 20, 10, 200, 25);
        CustomPlacing.placeComponent(layout, container, hitList, 20, 40, 200, 500);
        CustomPlacing.placeComponent(layout, container, hitInfo, 230, 10, 600, 530);
        fieldToEnterText.addActionListener((ActionEvent e) -> {
            var text = e.getActionCommand().trim();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://graphhopper.com/api/1/geocode" +
                            "?q=" + text.replace(" ", "+") +
                            "&locale=en" +
                            "&debug=true" +
                            "&key=" + ghApiKey))
                    .GET()
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(this::handleGHResponse);
        });
    }

    private void handleGHResponse(String text) {
        var response = g.fromJson(text, GHResponse.class);
        hitList.refresh(response.getHits());
        setVisible(true);
    }

    public void handleOWMResponse(String text) {
        var response = g.fromJson(text, OWMResponse.class);
        hitInfo.refreshWeather(response);
        setVisible(true);
    }

    public void handleOTMResponse(String text) {
        var response = g.fromJson(text, OTMResponse.class);
        hitInfo.refreshPlaces(response);
        setVisible(true);
    }
}
