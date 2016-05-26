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

    private JSONObject getAccountInfo() throws InvalidKeyException, JSONException, MalformedURLException {
        return getJsonObjectFromURL("account.json");
    }

    private JSONObject getLicenseInfo() throws InvalidKeyException, JSONException, MalformedURLException {
        return getJsonObjectFromURL("account/license.json");
    }

    private JSONObject getAllDiagrams() throws InvalidKeyException, JSONException, MalformedURLException {
        return getJsonObjectFromURL("diagrams.json");
    }

    public AccountInfo downloadAccountInfo() throws JSONException, InvalidKeyException, MalformedURLException {
        AccountInfo accountInfo = new AccountInfo(this.getAccountInfo(), this.getLicenseInfo());
        accountInfo.accountImage = downloadImage(accountInfo.accountInfo.getString("imageUrl"));
        return accountInfo;
    }

    public Diagram[] downloadDiagrams() throws JSONException, MalformedURLException, InvalidKeyException {
        JSONArray allDiagramsRaw = getAllDiagrams().getJSONArray("result");
        Diagram[] diagrams = new Diagram[allDiagramsRaw.length()];
        for (int i = 0; i < allDiagramsRaw.length(); i++) {
            diagrams[i] = new Diagram(downloadImage(allDiagramsRaw.getJSONObject(i).getString("imageUrlForApi") + "?apiKey=" + this.apiKey));
        }
        return diagrams;
    }

    @NonNull
    private JSONObject getJsonObjectFromURL(String licenseInfoURL) throws MalformedURLException, JSONException, InvalidKeyException {
        try {
            URL url = new URL(API_BASE_URL + licenseInfoURL + "?apiKey=" + this.apiKey);
            return new JSONObject(readAsString(url));
        } catch (MalformedURLException e) {
            Log.e("ERROR", e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            //TODO IOException is not necessarily equivalent to invalid key
            Log.e("ERROR", e.getMessage(), e);
            throw new InvalidKeyException("The API key specified does not return a valid account from Cacoo.", e);
        }
    }

    @NonNull
    private String readAsString(URL url) throws IOException {
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

    private Bitmap downloadImage(String url) {
        Log.d("DEBUG", "Downloading image: " + url);
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }
}
