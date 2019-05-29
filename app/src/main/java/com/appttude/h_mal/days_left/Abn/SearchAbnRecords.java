package com.appttude.h_mal.days_left.Abn;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SearchAbnRecords {

    private String URL = "https://abr.business.gov.au/json/";

    private final static String key = "aaca3f35-d6ac-42a3-bed2-733e7f21a0fc";

    private final static String TAG = "SearchAbnRecords";

    public SearchAbnRecords() {
    }

    public URL searchViaAbn(String searchTerm){
        Uri baseUri = Uri.parse(URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath("AbnDetails.aspx")
        .appendQueryParameter("abn",searchTerm)
        .appendQueryParameter("callback","callback")
        .appendQueryParameter("guid",key);

        return createUrl(builder.build().toString());
    }

    public URL searchViaName(String searchTerm){
        Uri baseUri = Uri.parse(URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath("MatchingNames.aspx")
                .appendQueryParameter("name",searchTerm)
                .appendQueryParameter("callback","callback")
                .appendQueryParameter("guid",key);

        return createUrl(builder.build().toString());
    }

    static java.net.URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error with creating URL ", e);
        }
        return url;
    }


    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = "";
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        Log.d(TAG, output.toString());
        return output.toString();

    }

    public static List<AbnObject> ExtractFeatureFromAbnJson(String json){
        List<AbnObject> abnObjectArrayList = new ArrayList<>();

        Log.i(TAG, "ExtractFeatureFromAbnJson: " + json);

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        if (json.contains("callback")){
            json = json.replace("callback(","");
            json.substring(0,json.length()-1);
        }

        try {
            JSONObject jObject = new JSONObject(json);
            AbnObject abnObject = new AbnObject(jObject.getString("Abn"),jObject.getString("EntityName")
                    ,jObject.getInt("AddressPostcode"),jObject.getString("AddressState"),null,null,true);
            abnObjectArrayList.add(abnObject);
        } catch (JSONException e) {
            Log.e("SearchAbn", "Problem parsing the JSON results", e);
        }

        return abnObjectArrayList;
    }

    public static List<AbnObject> ExtractFeatureFromNameJson(String json){
        List<AbnObject> abnObjectArrayList = new ArrayList<>();

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        try {
            JSONObject jObject = new JSONObject(json);
            JSONArray employersList = jObject.getJSONArray("Names");

            for (int i = 0; i < employersList.length(); i++){
                JSONObject current = employersList.getJSONObject(i);

                Log.i(TAG, "ExtractFeatureFromNameJson: " + current.get("Abn"));

                String abn = current.getString("Abn");
                String name = current.getString("Name");
                int postcode = current.getInt("Postcode");
                String state = current.getString("State");

                AbnObject abnObject = new AbnObject(abn,name,postcode,state,null,null,false);

                abnObjectArrayList.add(abnObject);
            }

        } catch (JSONException e) {
            Log.e("SearchAbnRecords", "Problem parsing the JSON results", e);
        }

        return abnObjectArrayList;
    }
}
