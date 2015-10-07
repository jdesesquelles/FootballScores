package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import barqsoft.footballscores.R;
import barqsoft.footballscores.utils.Utilities;
import barqsoft.footballscores.data.DatabaseContract;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = DetailWidgetRemoteViewsService.class.getSimpleName();
    private static final String[] GAME_SCORE_COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_ID
    };
    // these indices must match the projection
    private static final int INDEX_HOME_CREST = 0;
    private static final int INDEX_HOME_GOALS = 1;
    private static final int INDEX_AWAY_CREST = 2;
    private static final int INDEX_AWAY_GOALS = 3;
    private static final int INDEX_MATCH_ID = 4;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, GAME_SCORE_COLUMNS, DatabaseContract.scores_table.DATE_COL,
                        null, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_list_item);
                int matchId = data.getInt(INDEX_MATCH_ID);

                String homeTeamName = data.getString(INDEX_HOME_CREST);
                int homeCrestResourceId = Utilities.getTeamCrestByTeamName(homeTeamName);
                String homeGoals= data.getString(INDEX_HOME_GOALS);

                String awayTeamName = data.getString(INDEX_AWAY_CREST);
                int awayCrestResourceId = Utilities.getTeamCrestByTeamName(awayTeamName);
                String awayGoals = data.getString(INDEX_AWAY_GOALS);


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

                final Intent fillInIntent = new Intent();
                Uri matchUri = DatabaseContract.scores_table.buildScoreWithId();
                fillInIntent.setData(matchUri);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget_list_item, description);

            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_detail_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_MATCH_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
