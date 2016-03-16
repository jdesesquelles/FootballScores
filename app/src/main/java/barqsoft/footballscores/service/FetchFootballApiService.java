/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package barqsoft.footballscores.service;

import barqsoft.footballscores.data.FootballDataContract;
import barqsoft.footballscores.football.FootballConstants;
import barqsoft.footballscores.football.FootballRequestApi;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import barqsoft.footballscores.R;
import barqsoft.footballscores.football.pojo.FootballMatch;

/**
 * Created by yehya khaled on 3/2/2015.
 */

/**
 * Fetch the Data from the football Api webservice into the local SQLITE Database Scores.db
 */
public class FetchFootballApiService extends IntentService {
    public static final String LOG_TAG = "FetchFootballApiService";

    public FetchFootballApiService() {
        super("FetchFootballApiService");
    }

    // Widget
    public static final String ACTION_DATA_UPDATED =
            "barqsoft.footballscores.ACTION_DATA_UPDATED";


    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        FootballConstants.setApiKey(getApplicationContext().getString(R.string.api_key));
        FootballRequestApi.getData("n2", context);
        FootballRequestApi.getData("p2", context);
    }

    private void updateWidgets() {
        Context context = getApplicationContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

    private void getData(String timeFrame) {
        Uri fetch_build = Uri.parse(FootballConstants.BASE_URL).buildUpon().
                appendQueryParameter(FootballConstants.QUERY_TIME_FRAME, timeFrame).build();

        String jsonStr = null;
        // Getting Json from the API
        jsonStr = FootballRequestApi.RequestAPI(fetch_build.toString(), getString(R.string.api_key));

        try {
            if (jsonStr == null) {Log.d(LOG_TAG, "Could not connect to server.");}
            else {
                JSONArray matches = new JSONObject(jsonStr).getJSONArray("fixtures");
                if (matches.length() == 0) {
                    //OFF SEASON : if no data, call dummy data
                    jsonStr = getString(R.string.dummy_data);
//                    processJSONdata(getString(R.string.dummy_data), getApplicationContext(), false);
//                    return;
                }
                processJSONdata(jsonStr, getApplicationContext(), true);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void processJSONdata(String JSONdata, Context mContext, boolean isReal) {
        //JSON data
        // This set of league codes is for the 2015/2016 season. In fall of 2016, they will need to
        // be updated. Feel free to use the codes

        //Match data
        String League;
        String mDate;
        String mTime;
        String Home;
        String Away;
        String Home_goals;
        String Away_goals;
        String match_id;
        String match_day;

        try {
            JSONArray matches = new JSONObject(JSONdata).getJSONArray(FootballConstants.JSON_ARRAY_MATCHES);
            //ContentValues to be inserted
            Vector<ContentValues> values = new Vector<ContentValues>(matches.length());
            for (int i = 0; i < matches.length(); i++) {
                JSONObject match_data = matches.getJSONObject(i);
                League = match_data.getJSONObject(FootballConstants.JSON_OBJ_LINKS).getJSONObject(FootballConstants.JSON_OBJ_SOCCER_SEASON).
                        getString("href");

                League = League.replace(FootballConstants.STRING_SEASON_LINK, "");
                //This if statement controls which leagues we're interested in the data from.
                //add leagues here in order to have them be added to the DB.
                // If you are finding no data in the app, check that this contains all the leagues.
                // If it doesn't, that can cause an empty DB, bypassing the dummy data routine.
                if (League.equals(FootballConstants.PREMIER_LEAGUE) ||
                        League.equals(FootballConstants.SERIE_A) ||
                        League.equals(FootballConstants.BUNDESLIGA1) ||
                        League.equals(FootballConstants.BUNDESLIGA2) ||
                        League.equals(FootballConstants.PRIMERA_DIVISION) ||
                        League.equals(FootballConstants.LIGUE1) ||
                        League.equals(FootballConstants.LIGUE2) ||
                        League.equals(FootballConstants.PREMIER_LEAGUE) ||
                        League.equals(FootballConstants.PRIMERA_DIVISION) ||
                        League.equals(FootballConstants.SEGUNDA_DIVISION) ||
                        League.equals(FootballConstants.SERIE_A) ||
                        League.equals(FootballConstants.PRIMERA_LIGA) ||
                        League.equals(FootballConstants.Bundesliga3) ||
                        League.equals(FootballConstants.EREDIVISIE)
                        ) {
                    match_id = match_data.getJSONObject(FootballConstants.JSON_OBJ_LINKS).getJSONObject(FootballConstants.JSON_OBJ_SELF).
                            getString("href");
                    match_id = match_id.replace(FootballConstants.STRING_MATCH_LINK, "");
                    if (!isReal) {
                        //This if statement changes the match ID of the dummy data so that it all goes into the database
                        match_id = match_id + Integer.toString(i);
                    }

                    mDate = match_data.getString(FootballConstants.KEY_STRING_MATCH_DATE);
                    mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z"));
                    mDate = mDate.substring(0, mDate.indexOf("T"));
                    SimpleDateFormat match_date = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    match_date.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date parseddate = match_date.parse(mDate + mTime);
                        SimpleDateFormat new_date = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                        new_date.setTimeZone(TimeZone.getDefault());
                        mDate = new_date.format(parseddate);
                        mTime = mDate.substring(mDate.indexOf(":") + 1);
                        mDate = mDate.substring(0, mDate.indexOf(":"));
            //todo move
                        if (!isReal) {
                            //This if statement changes the dummy data's date to match our current date range.
                            Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
                            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                            mDate = mformat.format(fragmentdate);
                        }
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "error here!");
                        Log.e(LOG_TAG, e.getMessage());
                    }

                    Home = match_data.getString(FootballConstants.KEY_STRING_HOME_TEAM);
                    Away = match_data.getString(FootballConstants.KEY_STRING_AWAY_TEAM);
                    Home_goals = match_data.getJSONObject(FootballConstants.JSON_OBJ_RESULT).getString(FootballConstants.KEY_STRING_HOME_GOALS);
                    Away_goals = match_data.getJSONObject(FootballConstants.JSON_OBJ_RESULT).getString(FootballConstants.KEY_STRING_AWAY_GOALS);
                    match_day = match_data.getString(FootballConstants.KEY_STRING_MATCH_DAY);
                    ContentValues match_values = new ContentValues();
                    match_values.put(FootballDataContract.scores_table.MATCH_ID, match_id);
                    match_values.put(FootballDataContract.scores_table.DATE_COL, mDate);
                    match_values.put(FootballDataContract.scores_table.TIME_COL, mTime);
                    match_values.put(FootballDataContract.scores_table.HOME_COL, Home);
                    match_values.put(FootballDataContract.scores_table.AWAY_COL, Away);
                    match_values.put(FootballDataContract.scores_table.HOME_GOALS_COL, Home_goals);
                    match_values.put(FootballDataContract.scores_table.AWAY_GOALS_COL, Away_goals);
                    match_values.put(FootballDataContract.scores_table.LEAGUE_COL, League);
                    match_values.put(FootballDataContract.scores_table.MATCH_DAY, match_day);
                    values.add(match_values);
                }
            }
            ContentValues[] insert_data = new ContentValues[values.size()];
            values.toArray(insert_data);
            int inserted_data = 0;
            inserted_data = mContext.getContentResolver().bulkInsert(
                    FootballDataContract.BASE_CONTENT_URI,insert_data);

            Log.e(LOG_TAG,"Succesfully Inserted : " + String.valueOf(inserted_data));
            updateWidgets();
        } catch (JSONException e) {Log.e(LOG_TAG, e.getMessage());}
    }
}

