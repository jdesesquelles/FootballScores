package barqsoft.footballscores.football.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.Vector;

import barqsoft.footballscores.data.FootballDataContract;
import barqsoft.footballscores.football.pojo.FootballMatch;

/**
 * Created by ebal on 04/12/15.
 */
public class FootballDatabaseOperations {

    public static void addBulkMovies(ArrayList<FootballMatch> matchArrayList, Context context) {
        if (matchArrayList.size() > 0) {
            ContentValues[] cvArray = new ContentValues[matchArrayList.size()];
            matchArrayList.toArray(cvArray);
            int rowsInserted = context.getContentResolver()
                    .bulkInsert(FootballDataContract.BASE_CONTENT_URI, cvArray);
        }
    }

}
