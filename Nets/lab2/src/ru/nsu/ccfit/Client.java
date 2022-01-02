package ru.nsu.ccfit;

import com.google.gson.Gson;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Client extends Thread {
    private final String fileName;
    private final Long fileSize;
    private final BufferedOutputStream out;
    private final BufferedInputStream in;
    private final Gson g = new Gson();

    public Client(InetAddress address, int port, String file) throws IOException {
        var socket = new Socket(address, port);
        fileName = file;
        fileSize = Files.size(Paths.get(fileName));
        out = new BufferedOutputStream(socket.getOutputStream());
        in = new BufferedInputStream(socket.getInputStream());
    }

    private void sendHeader() throws IOException {
        var header = new Header(fileName, fileSize);
        var line = g.toJson(header);
        out.write((line + "\n").getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    private void sendFileContent() throws IOException {
        var fileReader = new BufferedInputStream(new FileInputStream(fileName));
        var buffer = new byte[1024];
        var charsRead = 0;
        while ((charsRead = fileReader.read(buffer)) > 0) {
            out.write(buffer, 0, charsRead);
            out.flush();
        }
        fileReader.close();
    }

    private void checkUploadedFileSize() throws IOException {
        var buffer = new byte[1024];
        in.read(buffer);
        var str = new String(buffer, StandardCharsets.UTF_8);
        var returnedSize = g.fromJson(str, BytesUploaded.class).getCount();
        if (returnedSize.equals(fileSize)) {
            System.out.println("File uploaded successfully");
        } else {
            System.out.println("Error occurred while uploading. Progress: " +
                    returnedSize + "/" + fileSize);
        }
    }

    public void run() {
        try (in; out;) {
            sendHeader();
            sendFileContent();
            checkUploadedFileSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
