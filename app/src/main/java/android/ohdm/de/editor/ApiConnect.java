package android.ohdm.de.editor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnect {

    String serverUrl;

    ApiConnect(String serverUrl){

        this.serverUrl = serverUrl;

    }

    public JSONObject getJSONObjectById(int objectId) {

        JSONObject geoObject = null;

        try {
            URL url = new URL(serverUrl+objectId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type",
                    "application/json; charset=utf-8");

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
                e.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return geoObject;
    }
}
