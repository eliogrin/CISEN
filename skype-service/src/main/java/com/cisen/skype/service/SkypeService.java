package com.cisen.skype.service;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SkypeService implements Runnable {

    public static final int SERVICE_PORT = 9000;
    private final Socket mSocket;
    private final int mNum;

    public SkypeService(Socket socket, int num) {
        mSocket = socket;
        mNum = num;

        Thread handler = new Thread(this, "handler-" + mNum);
        handler.start();
    }

    public void run() {
        try {
            try {
                System.out.println(mNum + " Connected.");

                DataInputStream is = new DataInputStream(mSocket.getInputStream());

                while (true) {
                    String line = is.readUTF();
                    if ("exit".equals(line)) {
                        return;
                    }
                    try {
                        System.out.println("Message: " + line);

                        String[] parts = line.split("\\|");
                        String chatName = parts[0];
                        String message = parts[1];
                        boolean updateTopic = Boolean.parseBoolean(parts[2]);
                        String newTopic = parts[3];

                        Chat chat = Skype.chat(chatName);
                        if (updateTopic) {
                            chat.setTopic(newTopic);
                        }
                        System.out.println("Send: " + message);

                        chat.send(message);

                    } catch (SkypeException e) {
                        System.out.print(e.getMessage());
                    }
                }
            } finally {
                mSocket.close();
            }
        } catch (IOException ex) {
            System.out.println(mNum + " Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {

        System.out.println("Accepting connections on port: " + SERVICE_PORT);

        int nextNum = 1;
        ServerSocket serverSocket = new ServerSocket(SERVICE_PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            new SkypeService(socket, nextNum++);
        }
    }

}
