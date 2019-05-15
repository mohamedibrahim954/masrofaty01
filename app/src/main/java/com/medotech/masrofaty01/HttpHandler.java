package com.medotech.masrofaty01;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {

    public HttpHandler() {

    }

    public String request_get_data() throws IOException {
        String response = null;

        URL url = new URL("http://ashrafkassem0-001-site1.btempurl.com/api/Concuranceys");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = readStream(in);
        } finally {
            urlConnection.disconnect();
        }
        return response;
    }

    public void request_post_data(String url_text) throws IOException {
        URL url = new URL(url_text);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            //writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);
        } finally {
            urlConnection.disconnect();
        }
    }

    private String readStream(InputStream in) {
        /*
         * To convert the InputStream to String we use the
         * BufferedReader.readLine() method. We iterate until the BufferedReader
         * return null which means there's no more data to read. Each line will
         * appended to a StringBuilder and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
