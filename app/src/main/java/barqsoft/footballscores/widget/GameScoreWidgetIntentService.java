package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import barqsoft.footballscores.activity.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.data.FootballDataContract;
import barqsoft.footballscores.utils.Utilities;

import java.util.Date;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by ebal on 05/10/15.
 */
public class GameScoreWidgetIntentService extends IntentService {
    private String[] mDate = new String[1];

    private static final String[] GAME_SCORE_COLUMNS = {
            FootballDataContract.scores_table.HOME_COL,
            FootballDataContract.scores_table.HOME_GOALS_COL,
            FootballDataContract.scores_table.AWAY_COL,
            FootballDataContract.scores_table.AWAY_GOALS_COL,
    };
    // these indices must match the projection
    private static final int INDEX_HOME_CREST = 0;
    private static final int INDEX_HOME_GOALS = 1;
    private static final int INDEX_AWAY_CREST = 2;
    private static final int INDEX_AWAY_GOALS = 3;

    public GameScoreWidgetIntentService() {
        super("GameScoreWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the Today widget ids: these are the widgets we need to update

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                GameScoreWidgetProvider.class));

        // Get today's data from the ContentProvider
        Date date = new Date(System.currentTimeMillis());
        mDate[0] = date.toString();

        Cursor data = getContentResolver().query(FootballDataContract.BASE_CONTENT_URI, GAME_SCORE_COLUMNS, FootballDataContract.scores_table.DATE_COL,
                mDate, null);

        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        String homeTeamName = data.getString(INDEX_HOME_CREST);
        int homeCrestResourceId = Utilities.getTeamCrestByTeamName(homeTeamName, null);
        String homeGoals= data.getString(INDEX_HOME_GOALS);

        String awayTeamName = data.getString(INDEX_AWAY_CREST);
        int awayCrestResourceId = Utilities.getTeamCrestByTeamName(awayTeamName, null);
        String awayGoals = data.getString(INDEX_AWAY_GOALS);

        data.close();

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            int widgetWidth = getWidgetWidth(appWidgetManager, appWidgetId);
            int defaultWidth = getResources().getDimensionPixelSize(R.dimen.widget_game_score_default_width);
            int largeWidth = getResources().getDimensionPixelSize(R.dimen.widget_game_score_large_width);
            int layoutId;
            if (widgetWidth >= largeWidth) {
                layoutId = R.layout.widget_game_score_large;
            } else if (widgetWidth >= defaultWidth) {
                layoutId = R.layout.widget_game_score;
            } else {
                layoutId = R.layout.widget_game_score_small;
            }

//            int layoutId = R.layout.widget_game_score_small;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setImageViewResource(R.id.widget_home_crest_icon, homeCrestResourceId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, homeTeamName);
            }
            views.setTextViewText(R.id.widget_home_score_text, homeGoals);

            views.setImageViewResource(R.id.widget_away_crest_icon, awayCrestResourceId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, awayTeamName);
            }
            views.setTextViewText(R.id.widget_away_score_text, awayGoals);

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_game_score_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return  getResources().getDimensionPixelSize(R.dimen.widget_game_score_default_width);
    }



    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget, description);
    }
}

