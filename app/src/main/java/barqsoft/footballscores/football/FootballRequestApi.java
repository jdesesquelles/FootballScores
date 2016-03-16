package barqsoft.footballscores.football;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import barqsoft.footballscores.R;
import barqsoft.footballscores.football.pojo.FootballMatch;
import java.util.ArrayList;
import android.content.Context;
/**
 * Created by ebal on 03/12/15.
 */
public class FootballRequestApi {
    public static final String LOG_TAG = FootballRequestApi.class.getSimpleName();

    public static ArrayList<FootballMatch> getData(String timeFrame, Context context) {
        Uri fetch_build = Uri.parse(FootballConstants.BASE_URL).buildUpon().
                appendQueryParameter(FootballConstants.QUERY_TIME_FRAME, timeFrame).build();

        String jsonStr = null;
        // Getting Json from the API
        jsonStr = RequestAPI(fetch_build.toString());

        try {
            if (jsonStr == null) {Log.d(LOG_TAG, "Could not connect to server.");}
            else {
                JSONArray matches = new JSONObject(jsonStr).getJSONArray("fixtures");
                if (matches.length() == 0) {
                    //OFF SEASON : if no data, call dummy data
                    jsonStr = context.getString(R.string.dummy_data);
//                    FootballJsonParser.getScoresFromJson(getString(R.string.dummy_data), false);
                }
                return FootballJsonParser.getScoresFromJson(jsonStr, true);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }


    public static String RequestAPI(String stringUrl) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;
        try {
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(FootballConstants.REQUEST_METHOD);
            urlConnection.addRequestProperty(FootballConstants.REQUEST_PROPERTY_X_AUTH_TOKEN, FootballConstants.API_KEY);
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Adding a newline does make debugging a *lot* easier
                // print out the completed buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException : " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream");
                }
            }
            return jsonStr;
        }
    }
}
