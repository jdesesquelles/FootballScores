package barqsoft.footballscores.football.pojo;

/**
 * Created by ebal on 03/12/15.
 */
public class FootballMatch {

    public String League;
    public String mDate;
    public String mTime;
    public String Home;
    public String Away;
    public String Home_goals;
    public String Away_goals;
    public String match_id;
    public String match_day;

    public FootballMatch() {
    }

    public FootballMatch(String league, String mDate, String mTime, String home, String away, String home_goals, String away_goals, String match_id, String match_day) {
        League = league;
        this.mDate = mDate;
        this.mTime = mTime;
        Home = home;
        Away = away;
        Home_goals = home_goals;
        Away_goals = away_goals;
        this.match_id = match_id;
        this.match_day = match_day;
    }

    public String getLeague() {
        return League;
    }

    public void setLeague(String league) {
        League = league;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getHome() {
        return Home;
    }

    public void setHome(String home) {
        Home = home;
    }

    public String getAway() {
        return Away;
    }

    public void setAway(String away) {
        Away = away;
    }

    public String getHome_goals() {
        return Home_goals;
    }

    public void setHome_goals(String home_goals) {
        Home_goals = home_goals;
    }

    public String getAway_goals() {
        return Away_goals;
    }

    public void setAway_goals(String away_goals) {
        Away_goals = away_goals;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getMatch_day() {
        return match_day;
    }

    public void setMatch_day(String match_day) {
        this.match_day = match_day;
    }
}
