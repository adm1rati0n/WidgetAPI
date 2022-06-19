package com.example.vidget;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JokesLoader extends AsyncTask<Void, Void, Void> {
    private static final String REQUEST_URL = "https://api.chucknorris.io/jokes/random";

    private final Callback callback;
    private String joke = "";

    public JokesLoader(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String jsonString = getJson(REQUEST_URL);
        try {
            JSONObject json = new JSONObject(jsonString);
            joke = json.getString("value");
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onEvent("");
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        callback.onEvent(joke);
    }

    private String getJson(String link) {
        String data = "";
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(),
                        StandardCharsets.UTF_8
                ));
                data = reader.readLine();
                connection.disconnect();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return data;
    }
    public interface Callback {
        void onEvent(String text);
    }
}
