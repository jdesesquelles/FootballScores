package barqsoft.footballscores.football;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import barqsoft.footballscores.football.pojo.FootballMatch;

import java.util.ArrayList;


/**
 * Created by ebal on 03/12/15.
 */
public class FootballJsonParser {
    public static final String LOG_TAG = FootballJsonParser.class.getSimpleName();

    public static ArrayList<FootballMatch> getScoresFromJson(String JSONdata, boolean isReal) throws JSONException {

        ArrayList<FootballMatch> footballMatchArrayList = new ArrayList<FootballMatch>();
        String mDate;
        String mTime;
        String league;

        JSONArray matchesJsonArray = new JSONObject(JSONdata).getJSONArray(FootballConstants.JSON_ARRAY_MATCHES);
        for (int i = 0; i < matchesJsonArray.length(); i++) {
            FootballMatch footballMatch = new FootballMatch();
            JSONObject matchDataJsonObj = matchesJsonArray.getJSONObject(i);

            league = matchDataJsonObj.getJSONObject(FootballConstants.JSON_OBJ_LINKS).getJSONObject(FootballConstants.JSON_OBJ_SOCCER_SEASON).
                    getString(FootballConstants.KEY_STRING_HREF);
            footballMatch.setLeague(league.replace(FootballConstants.STRING_SEASON_LINK, ""));

            //This if statement controls which leagues we're interested in the data from.
            //add leagues here in order to have them be added to the DB.
            // If you are finding no data in the app, check that this contains all the leagues.
            // If it doesn't, that can cause an empty DB, bypassing the dummy data routine.
            if (footballMatch.getLeague().equals(FootballConstants.PREMIER_LEAGUE) ||
                    footballMatch.getLeague().equals(FootballConstants.SERIE_A) ||
                    footballMatch.getLeague().equals(FootballConstants.BUNDESLIGA1) ||
                    footballMatch.getLeague().equals(FootballConstants.BUNDESLIGA2) ||
                    footballMatch.getLeague().equals(FootballConstants.PRIMERA_DIVISION) ||
                    footballMatch.getLeague().equals(FootballConstants.LIGUE1) ||
                    footballMatch.getLeague().equals(FootballConstants.LIGUE2) ||
                    footballMatch.getLeague().equals(FootballConstants.PREMIER_LEAGUE) ||
                    footballMatch.getLeague().equals(FootballConstants.PRIMERA_DIVISION) ||
                    footballMatch.getLeague().equals(FootballConstants.SEGUNDA_DIVISION) ||
                    footballMatch.getLeague().equals(FootballConstants.SERIE_A) ||
                    footballMatch.getLeague().equals(FootballConstants.PRIMERA_LIGA) ||
                    footballMatch.getLeague().equals(FootballConstants.Bundesliga3) ||
                    footballMatch.getLeague().equals(FootballConstants.EREDIVISIE)
                    ) {
                footballMatch.setMatch_id(matchDataJsonObj.getJSONObject(FootballConstants.JSON_OBJ_LINKS).getJSONObject(FootballConstants.JSON_OBJ_SELF).
                        getString(FootballConstants.KEY_STRING_HREF));
                footballMatch.setMatch_id(footballMatch.getMatch_id().replace(FootballConstants.STRING_MATCH_LINK, ""));
                if (!isReal) {
                    //This if statement changes the match ID of the dummy data so that it all goes into the database
                    footballMatch.setMatch_id(footballMatch.getMatch_id() + Integer.toString(i));
                }

                mDate = matchDataJsonObj.getString(FootballConstants.KEY_STRING_MATCH_DATE);
                mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z"));
//                        footballMatch.setmTime(mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z")));
                mDate = mDate.substring(0, mDate.indexOf("T"));
//                footballMatch.setmDate(mDate.substring(0, mDate.indexOf("T")));
                SimpleDateFormat match_date = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                match_date.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    Date parseddate = match_date.parse(mDate + footballMatch.getmTime());
                    SimpleDateFormat new_date = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                    new_date.setTimeZone(TimeZone.getDefault());
                    mDate = new_date.format(parseddate);
                    mTime = mDate.substring(mDate.indexOf(":") + 1);
                    mDate = mDate.substring(0, mDate.indexOf(":"));

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
                footballMatch.setmDate(mDate);
                footballMatch.setmTime(mTime);

                footballMatch.setHome(matchDataJsonObj.getString(FootballConstants.KEY_STRING_HOME_TEAM));
                footballMatch.setAway(matchDataJsonObj.getString(FootballConstants.KEY_STRING_AWAY_TEAM));
                footballMatch.setHome_goals(matchDataJsonObj.getJSONObject(FootballConstants.JSON_OBJ_RESULT).getString(FootballConstants.KEY_STRING_HOME_GOALS));
                footballMatch.setAway_goals(matchDataJsonObj.getJSONObject(FootballConstants.JSON_OBJ_RESULT).getString(FootballConstants.KEY_STRING_AWAY_GOALS));
                footballMatch.setMatch_day(matchDataJsonObj.getString(FootballConstants.KEY_STRING_MATCH_DAY));

            }
            footballMatchArrayList.add(footballMatch);
        }
        return footballMatchArrayList;
    }
}
