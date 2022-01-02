package ru.nsu.ccfit;

import java.time.Instant;
import java.util.Calendar;

public class SpeedCounter extends Thread {
    private Long bytesUploaded;
    private Long newBytes;
    private Long begin;
    private Long lastUpdate;
    private Double currentSpeed;
    private Double averageSpeed;

    public SpeedCounter() {
        bytesUploaded = 0L;
        newBytes = 0L;
        currentSpeed = 0D;
        averageSpeed = 0D;
        begin = Calendar.getInstance().getTimeInMillis();
        lastUpdate = Calendar.getInstance().getTimeInMillis();
    }

    public void update(Long bytes) {
        bytesUploaded += bytes;
        averageSpeed = (double) (bytesUploaded) / (Instant.now().toEpochMilli() - begin) * 1000;
        newBytes += bytes;
    }

    public Double getCurrentSpeed() {
        return currentSpeed;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void run() {
        begin = Instant.now().toEpochMilli();
        lastUpdate = Instant.now().toEpochMilli();
        while (!isInterrupted()) {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                currentSpeed = (double) (newBytes)
                        / (Instant.now().toEpochMilli() - lastUpdate) * 1000;
                System.out.println("cur speed : " + currentSpeed.intValue() + " bytes/s");
                System.out.println("avg speed : " + averageSpeed.intValue() + " bytes/s");
                break;
            }
            currentSpeed = (double) (newBytes)
                    / (Instant.now().toEpochMilli() - lastUpdate) * 1000;
            newBytes = 0L;
            System.out.println("cur speed : " + currentSpeed.intValue() + " bytes/s");
            System.out.println("avg speed : " + averageSpeed.intValue() + " bytes/s");
        }
    }
}
