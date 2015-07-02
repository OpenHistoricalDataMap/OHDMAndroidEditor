package android.ohdm.de.editor.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnect {

    private static final String TAG = "ApiConnect";

    private static final String REQUEST_PROPERTY = "application/json; charset=utf-8";

    public static final int UPLOAD_RESPONSE_OK = 201;
    public static final int UPLOAD_RESPONSE_ERROR = 500;

    public static final int DOWNLOAD_RESPONSE_OK = 200;
    public static final int DOWNLOAD_RESPONSE_ERROR = 500;
    public static final int DOWNLOAD_RESPONSE_NOT_FOUND = 404;

    private String serverUrl;

    public ApiConnect(String serverUrl) {

        this.serverUrl = serverUrl;
    }

    public int putPolyObject(JSONObject polyObject) {

        try {
            URL url = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", REQUEST_PROPERTY);

            OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream());
            out.write(polyObject.toString());
            out.close();

            int responseCode = con.getResponseCode();
            Log.d(TAG, "response code: " + responseCode);

            if (responseCode == UPLOAD_RESPONSE_ERROR) {

                Log.d(TAG, con.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getErrorStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                Log.d(TAG, response.toString());

                return UPLOAD_RESPONSE_ERROR;

            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                Log.d(TAG, response.toString());
            }

        } catch (IOException ex) {

            Log.d(TAG, ex.toString());

            return UPLOAD_RESPONSE_ERROR;
        }

        return UPLOAD_RESPONSE_OK;
    }

    public JSONObject getJSONObjectById(int objectId) throws JSONException, ApiException {

        JSONObject geoObject = null;

        try {
            URL url = new URL(serverUrl + objectId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", REQUEST_PROPERTY);

            int responseCode = con.getResponseCode();
            Log.d(TAG, "response code: " + responseCode);

            if (responseCode == DOWNLOAD_RESPONSE_NOT_FOUND || responseCode == DOWNLOAD_RESPONSE_ERROR) {
                Log.d(TAG, "ApiException: " + responseCode);
                throw new ApiException(String.valueOf(responseCode));
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            try {

                geoObject = new JSONObject(response.toString());

            } catch (JSONException e) {
                throw e;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return geoObject;
    }

    public JSONArray getNearJSONObjectsByGeoPoint(String[] params) throws ApiException, JSONException {

        JSONArray geoObjects = null;

        String nearObjects = "nearObjects//";
        String since = "since/"+params[3]+"/";
        String until = "until/"+params[4]+"/";
        String distance = params[2]+"/";
        String longitude = params[0];
        String latitude = params[1];

        String parameterUrl = nearObjects + since + until + distance + longitude + "/" + latitude;

        String requestUrl = serverUrl + parameterUrl;

        try {

            Log.d(TAG, requestUrl);

            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", REQUEST_PROPERTY);

            int responseCode = con.getResponseCode();
            Log.d(TAG, "response code: " + responseCode);

            if (responseCode == DOWNLOAD_RESPONSE_NOT_FOUND || responseCode == DOWNLOAD_RESPONSE_ERROR) {
                Log.d(TAG, "ApiException: " + responseCode);
                throw new ApiException(String.valueOf(responseCode));
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            Log.d(TAG, response.toString());

            try {

                geoObjects = new JSONArray(response.toString());

            } catch (JSONException e) {
                throw e;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return geoObjects;
    }
}