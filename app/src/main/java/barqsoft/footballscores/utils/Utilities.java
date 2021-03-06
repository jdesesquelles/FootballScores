package barqsoft.footballscores.utils;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

import barqsoft.footballscores.R;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities
{
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;
    public static String getLeague(int league_num, Context context)
    {
        switch (league_num)
        {
            case SERIE_A : return context.getString(R.string.utility_hard_coded_data_serie_a);
            case PREMIER_LEGAUE : return context.getString(R.string.utility_hard_coded_data_premier_league);
            case CHAMPIONS_LEAGUE : return context.getString(R.string.utility_hard_coded_data_champions_league);
            case PRIMERA_DIVISION : return context.getString(R.string.utility_hard_coded_data_primera_division);
            case BUNDESLIGA : return context.getString(R.string.utility_hard_coded_data_bundeslig_a);
            default: return context.getString(R.string.utility_hard_coded_data_not_known_league);
        }
    }
    public static String getMatchDay(int match_day, int league_num, Context context)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return context.getString(R.string.utility_hard_coded_data_group_stages_matchday_6);
            }
            else if(match_day == 7 || match_day == 8)
            {
                return context.getString(R.string.utility_hard_coded_data_first_knockout_round);
            }
            else if(match_day == 9 || match_day == 10)
            {
                return context.getString(R.string.utility_hard_coded_data_quarter_final);
            }
            else if(match_day == 11 || match_day == 12)
            {
                return context.getString(R.string.utility_hard_coded_data_semi_final);
            }
            else
            {
                return context.getString(R.string.utility_hard_coded_data_final);
            }
        }
        else
        {
            return context.getString(R.string.utility_hard_coded_data_matchday) + String.valueOf(match_day);
        }
    }


    public static String getScores(int home_goals, int awaygoals, Context context)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return context.getString(R.string.utility_hard_coded_data_match_score_separator);
        }
        else
        {
            return String.valueOf(home_goals) + context.getString(R.string.utility_hard_coded_data_match_score_separator) + String.valueOf(awaygoals);
        }
    }


    public static int getTeamCrestByTeamName(String teamname, Context context)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            default: return R.drawable.no_icon;
        }
    }


    static String formatDate(long dateInMilliseconds) {
        Date date = new Date(dateInMilliseconds);
        return DateFormat.getDateInstance().format(date);
    }
}
