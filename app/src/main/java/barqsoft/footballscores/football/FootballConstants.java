package barqsoft.footballscores.football;

import barqsoft.footballscores.R;
import barqsoft.footballscores.football.pojo.FootballMatch;

/**
 * Created by ebal on 03/12/15.
 */
public class FootballConstants {

    public static final String BASE_URL = "http://api.football-data.org/v1/fixtures"; //Base URL
    public static String REQUEST_METHOD = "GET";
    public static String REQUEST_PROPERTY_X_AUTH_TOKEN = "X-Auth-Token";
    public static String API_KEY = "";

    // Query parameters
    public static final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days
    public static final String QUERY_MATCH_DAY = "matchday";

    // Json Field Values
    public static final String BUNDESLIGA1 = "394";
    public static final String BUNDESLIGA2 = "395";
    public static final String LIGUE1 = "396";
    public static final String LIGUE2 = "397";
    public static final String PREMIER_LEAGUE = "398";
    public static final String PRIMERA_DIVISION = "399";
    public static final String SEGUNDA_DIVISION = "400";
    public static final String SERIE_A = "401";
    public static final String PRIMERA_LIGA = "402";
    public static final String Bundesliga3 = "403";
    public static final String EREDIVISIE = "404";

    public static final String STRING_SEASON_LINK = "http://api.football-data.org/v1/soccerseasons/";
    public static final String STRING_MATCH_LINK = "http://api.football-data.org/v1/fixtures/";
    public static final String JSON_ARRAY_MATCHES = "fixtures";
    public static final String JSON_OBJ_LINKS = "_links";
    public static final String JSON_OBJ_SOCCER_SEASON = "soccerseason";
    public static final String JSON_OBJ_SELF = "self";
    public static final String KEY_STRING_HREF = "href";
    public static final String KEY_STRING_MATCH_DATE = "date";
    public static final String KEY_STRING_HOME_TEAM = "homeTeamName";
    public static final String KEY_STRING_AWAY_TEAM = "awayTeamName";
    public static final String JSON_OBJ_RESULT = "result";
    public static final String KEY_STRING_HOME_GOALS = "goalsHomeTeam";
    public static final String KEY_STRING_AWAY_GOALS = "goalsAwayTeam";
    public static final String KEY_STRING_MATCH_DAY = "matchday";

    public static void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }
}
