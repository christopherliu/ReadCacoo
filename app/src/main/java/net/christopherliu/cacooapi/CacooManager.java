package net.christopherliu.cacooapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import net.christopherliu.cacooapi.types.AccountInfo;
import net.christopherliu.cacooapi.types.Diagram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Christopher Liu on 5/23/2016.
 * Interface to Cacoo's API methods. Maps those methods to native Java objects.
 */
public class CacooManager {
    private static final String API_BASE_URL = "https://cacoo.com/api/v1/";

    private String apiKey;

    public CacooManager(String apiKey) {
        this.apiKey = apiKey;
    }

    public AccountInfo retrieveAccountInfo() throws JSONException, InvalidKeyException, IOException {
        JSONObject accountInfo = retrieveJSONFromCacoo("account.json");
        JSONObject licenseInfo = retrieveJSONFromCacoo("account/license.json");

        return new AccountInfo(downloadAsImage(new URL(accountInfo.getString("imageUrl"))),
                accountInfo.getString("nickname"), licenseInfo.getString("plan"));
    }

    public Diagram[] retrieveDiagrams() throws JSONException, IOException, InvalidKeyException {
        JSONArray allDiagramsRaw = retrieveJSONFromCacoo("diagrams.json").getJSONArray("result");
        Diagram[] diagrams = new Diagram[allDiagramsRaw.length()];
        for (int i = 0; i < allDiagramsRaw.length(); i++) {
            diagrams[i] = new Diagram(downloadAsImage(new URL(allDiagramsRaw.getJSONObject(i).getString("imageUrlForApi") + "?apiKey=" + this.apiKey)));
        }
        return diagrams;
    }

    @NonNull
    private JSONObject retrieveJSONFromCacoo(String uri) throws MalformedURLException, JSONException, InvalidKeyException {
        try {
            URL url = new URL(API_BASE_URL + uri + "?apiKey=" + this.apiKey);
            return new JSONObject(downloadAsString(url));
        } catch (MalformedURLException e) {
            Log.e("ERROR", e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            //TODO IOException is not necessarily equivalent to invalid key
            Log.e("ERROR", e.getMessage(), e);
            throw new InvalidKeyException("The API key specified does not return a valid account from Cacoo.", e);
        }
    }

    // region Basic utility functions

    @NonNull
    private Bitmap downloadAsImage(URL url) throws IOException {
        Log.d("DEBUG", "Downloading as image: " + url);
        Bitmap image = null;
        InputStream in = url.openStream();
        image = BitmapFactory.decodeStream(in);
        in.close();
        return image;
    }

    @NonNull
    private String downloadAsString(URL url) throws IOException {
        Log.d("DEBUG", "Downloading as image: " + url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } finally {
            urlConnection.disconnect();
        }
    }
    // endregion
}
