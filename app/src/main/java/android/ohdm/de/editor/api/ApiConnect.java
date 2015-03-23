package android.ohdm.de.editor.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnect {

    private final static String TAG = "ApiConnect";

    private final static String REQUEST_PROPERTY = "application/json; charset=utf-8";

    public final static int UPLOAD_RESPONSE_OK = 201;
    public final static int UPLOAD_RESPONSE_ERROR = 500;

    public final static int DOWNLOAD_RESPONSE_OK = 200;
    public final static int DOWNLOAD_RESPONSE_ERROR = 500;
    public final static int DOWNLOAD_RESPONSE_NOT_FOUND = 404;

    private String serverUrl;

    public ApiConnect(String serverUrl) {

        this.serverUrl = serverUrl;
    }

    public int putPolyObject(JSONObject polyObject) {

        try {
            URL url = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type",REQUEST_PROPERTY);

            OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream());
            out.write(polyObject.toString());
            out.close();

            int responseCode = con.getResponseCode();
            Log.d(TAG,"response code: "+String.valueOf(responseCode));

            if(responseCode == UPLOAD_RESPONSE_ERROR){

                return UPLOAD_RESPONSE_ERROR;

            }else{
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                Log.d(TAG,response.toString());
            }

        } catch (IOException ex) {

            Log.d(TAG, ex.toString());

            return UPLOAD_RESPONSE_ERROR;
        }

        return UPLOAD_RESPONSE_OK;
    }

    public JSONObject getJSONObjectById(int objectId) throws JSONException,ApiException {

        JSONObject geoObject = null;

        try {
            URL url = new URL(serverUrl + objectId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type",REQUEST_PROPERTY);

            int responseCode = con.getResponseCode();
            Log.d(TAG,"response code: "+String.valueOf(responseCode));

            if(responseCode == DOWNLOAD_RESPONSE_NOT_FOUND || responseCode == DOWNLOAD_RESPONSE_ERROR) {
                Log.d(TAG,"ApiException");
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
}