package com.example.geogooglemapslab8.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Network {

    public static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public String getData(final String request)  {

        String data = "";

        final HttpURLConnection connection;
        try {
            connection = buildConnection(request);
            data = getResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

       private HttpURLConnection buildConnection(final String url) throws Exception {

        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {

            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        }};

        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HostnameVerifier allHostsValid = (hostname, session) -> true;

        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        final URL _url = new URL(url);
        final HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(15 * 1000);
        connection.setConnectTimeout(15 * 1000);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND){
            Log.v("APP", "Not found!");
        }

        return connection;

    }

    private String getResponse(final HttpURLConnection connection) throws Exception {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

            final StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line.trim());
            }

            bufferedReader.close();

            return stringBuilder.toString();
        }
    }

}
