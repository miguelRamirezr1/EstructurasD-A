package org.test4;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class llamadoApi {
    private static final String API_URL = "https://zenquotes.io/api/quotes";
    private final Gson gson;

    public llamadoApi() {
        this.gson = new Gson();
    }

    public List<Quote> fetchQuotes() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();

        // Parsear JSON usando Gson
        List<Quote> quotes = gson.fromJson(
                response.toString(),
                new TypeToken<List<Quote>>(){}.getType()
        );

        System.out.println("Quotes obtenidas: " + quotes.size());
        return quotes;
    }
}
