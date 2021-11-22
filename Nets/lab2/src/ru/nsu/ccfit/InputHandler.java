package ru.nsu.ccfit;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class InputHandler extends Thread {
    private final BufferedInputStream in;
    private final BufferedOutputStream out;
    private final Header header;
    private final SpeedCounter speedCounter;
    private Long bytesCounter;

    private Header readHeader() throws IOException {
        var buffer = new byte[1024];
        in.read(buffer);
        return new Gson().fromJson(new String(buffer, StandardCharsets.UTF_8), Header.class);
    }

    private boolean fileExists(String name) {
        return (new File(name).isFile());
    }

    public InputHandler(Socket c) throws IOException {
        bytesCounter = 0L;
        in = new BufferedInputStream(c.getInputStream());
        out = new BufferedOutputStream(c.getOutputStream());
        header = readHeader();
        speedCounter = new SpeedCounter();
    }

    private BufferedOutputStream createFileWriter() throws IOException {
        var dividedName = header.getFileName().split("\\.");
        String newFileName;
        int c = 1;
        while (fileExists((newFileName = "uploads/" + dividedName[0] + c + "." + dividedName[1])) && c < 1000) {
            c++;
        }
        var newFile = new File(newFileName);
        newFile.createNewFile();
        return new BufferedOutputStream(new FileOutputStream(newFile));
    }

    private void downloadFile(BufferedOutputStream fileWriter) throws IOException {
        var buffer = new byte[1024];
        var countedNow = 0L;
        speedCounter.start();
        do {
            countedNow = in.read(buffer);
            bytesCounter += countedNow;
            speedCounter.update(countedNow);
            fileWriter.write(buffer, 0, (int)countedNow);
            fileWriter.flush();
        } while (countedNow == 1024);
        speedCounter.interrupt();
    }

    private void sendFileSizeCheck() throws IOException {
        var count = new BytesUploaded(bytesCounter);
        var checkStr = new Gson().toJson(count);
        out.write((checkStr + "\0").getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    public void run() {
        try {
            var fileOut = createFileWriter();
            downloadFile(fileOut);
            fileOut.close();
            sendFileSizeCheck();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
