package com.epam.cisen.teamcity.connectors;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by Vladislav on 19.11.2014.
 */
public class BaseConnection {
    private String baseAddress;
    private String token;

    public BaseConnection(String baseAddress) {
        this(baseAddress, null, null);
    }

    public BaseConnection(String baseAddress, String login, String pass) {
        if (baseAddress == null) {
            throw new IllegalArgumentException("Address cannot be null.");
        }
        this.baseAddress = baseAddress.endsWith("/") ? baseAddress.substring(0, baseAddress.length() - 1) : baseAddress;
        this.token = toToken(login, pass);
    }

    private String toToken(String login, String pass) {
        if (login == null || login.trim().isEmpty()
                || pass == null || pass.trim().isEmpty()) {
            return null;
        }
        String authStr = login + ":" + pass;
        byte[] authArray = Base64.encodeBase64(authStr.getBytes());
        return new String(authArray);
    }

    public String read(String subAddress, Object... params) throws IOException {
        String urlStr = baseAddress + String.format(subAddress, params);
        URL url = new URL(urlStr);
        URLConnection connection = url.openConnection();
        if (token != null) {
            connection.setRequestProperty("Authorization", "Basic " + token);
        }
        return read(connection.getInputStream());
    }


    private String read(InputStream is) throws IOException {
        Scanner scanner = new Scanner(is);
        StringBuilder result = new StringBuilder();
        try {
            while (scanner.hasNextLine()) {
                result.append(scanner.nextLine());
            }
        } finally {
            scanner.close();
        }
        return result.toString();
    }

}
