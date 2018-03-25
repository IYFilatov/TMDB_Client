package com.abrader.tmdb_client.model.localdb;

import android.provider.BaseColumns;

public class FilmBaseContract {

    public FilmBaseContract() {
    }

    public static final class FilmEntry implements BaseColumns {
        public final static String TABLE_NAME = "films";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME_EXTERNAL_ID = "name";
        public final static String COLUMN_NAME_TITLE = "title";
        public final static String COLUMN_NAME_OVERVIEW = "overview";
        public final static String COLUMN_NAME_RELEASE_DATE = "release_date";
        public final static String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public final static String COLUMN_NAME_POPULARITY = "popularity";
        public final static String COLUMN_POSTER_PATH = "poster_path";
        public final static String COLUMN_POSTER_B64 = "poster_b64";

    }
}
