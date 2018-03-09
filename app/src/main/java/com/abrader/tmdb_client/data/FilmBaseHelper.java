package com.abrader.tmdb_client.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.abrader.tmdb_client.FilmData;

import java.util.ArrayList;
import java.util.List;

import static com.abrader.tmdb_client.data.FilmBaseContract.*;

public class FilmBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tmdb_storage.db";
    private static final int DATABASE_VERSION = 1;

    public FilmBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Строка для создания таблицы
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + FilmEntry.TABLE_NAME + " ("
                + FilmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FilmEntry.COLUMN_NAME_EXTERNAL_ID + " INTEGER NOT NULL, "
                + FilmEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, "
                + FilmEntry.COLUMN_NAME_OVERVIEW + " TEXT NOT NULL, "
                + FilmEntry.COLUMN_NAME_RELEASE_DATE + " TEXT NOT NULL, "
                + FilmEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL NOT NULL DEFAULT 0, "
                + FilmEntry.COLUMN_NAME_POPULARITY + " REAL NOT NULL DEFAULT 0, "
                + FilmEntry.COLUMN_POSTER_PATH + " TEXT, "
                + FilmEntry.COLUMN_POSTER_B64 + " TEXT);";

        // Запускаем создание таблицы
        sqLiteDatabase.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Удаляем старую таблицу и создаём новую
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FilmEntry.TABLE_NAME);
        // Создаём новую таблицу
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertFilmData(SQLiteDatabase sqLiteDatabase, FilmData film){
        ContentValues values = new ContentValues();
        values.put(FilmEntry.COLUMN_NAME_EXTERNAL_ID, film.getId());
        values.put(FilmEntry.COLUMN_NAME_TITLE, film.getTitle());
        values.put(FilmEntry.COLUMN_NAME_OVERVIEW, film.getOverview());
        values.put(FilmEntry.COLUMN_NAME_RELEASE_DATE, film.getReleaseDate());
        values.put(FilmEntry.COLUMN_NAME_VOTE_AVERAGE, film.getVoteAverage());
        values.put(FilmEntry.COLUMN_NAME_POPULARITY, film.getPopularity());
        values.put(FilmEntry.COLUMN_POSTER_PATH, film.getPosterPath());
        values.put(FilmEntry.COLUMN_POSTER_B64, film.getPoster_b64());

        return sqLiteDatabase.insert(FilmEntry.TABLE_NAME,null, values);
    }

    public long updateFilmData(SQLiteDatabase sqLiteDatabase, FilmData film){
        ContentValues values = new ContentValues();
        values.put(FilmEntry.COLUMN_NAME_EXTERNAL_ID, film.getId());
        values.put(FilmEntry.COLUMN_NAME_TITLE, film.getTitle());
        values.put(FilmEntry.COLUMN_NAME_OVERVIEW, film.getOverview());
        values.put(FilmEntry.COLUMN_NAME_RELEASE_DATE, film.getReleaseDate());
        values.put(FilmEntry.COLUMN_NAME_VOTE_AVERAGE, film.getVoteAverage());
        values.put(FilmEntry.COLUMN_NAME_POPULARITY, film.getPopularity());
        values.put(FilmEntry.COLUMN_POSTER_PATH, film.getPosterPath());
        values.put(FilmEntry.COLUMN_POSTER_B64, film.getPoster_b64());

        String selectionString = FilmEntry.COLUMN_NAME_EXTERNAL_ID + "=?";
        String[] selectionArgs = {film.getId().toString()};

        return sqLiteDatabase.update(
                FilmEntry.TABLE_NAME,
                values,
                selectionString,
                selectionArgs);
    }

    public FilmData getFilmByExternalID(SQLiteDatabase sqLiteDatabase, Integer id){
        String[] projection = {
                FilmEntry._ID,
                FilmEntry.COLUMN_NAME_EXTERNAL_ID,
                FilmEntry.COLUMN_NAME_TITLE,
                FilmEntry.COLUMN_NAME_OVERVIEW,
                FilmEntry.COLUMN_NAME_RELEASE_DATE,
                FilmEntry.COLUMN_NAME_VOTE_AVERAGE,
                FilmEntry.COLUMN_NAME_POPULARITY,
                FilmEntry.COLUMN_POSTER_PATH,
                FilmEntry.COLUMN_POSTER_B64
        };

        String selectionString = FilmEntry.COLUMN_NAME_EXTERNAL_ID + "=?";
        String[] selectionArgs = {id.toString()};

        Cursor cursor = sqLiteDatabase.query(
                FilmEntry.TABLE_NAME,  // таблица
                projection,            // столбцы
                selectionString,       // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                FilmEntry.COLUMN_NAME_TITLE + " DESC");  // порядок сортировки

        FilmData result = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            result = new FilmData();
            result.setId(cursor.getInt(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_EXTERNAL_ID)));
            result.setTitle(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_TITLE)));
            result.setOverview(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_OVERVIEW)));
            result.setReleaseDate(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_RELEASE_DATE)));
            result.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_VOTE_AVERAGE)));
            result.setPopularity(cursor.getDouble(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_POPULARITY)));
            result.setPosterPath(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_POSTER_PATH)));
            result.setPoster_b64(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_POSTER_B64)));
        }

        return result;
    }

    public void updateFilmBaseData(List<FilmData> fList){
        long RowID;
        SQLiteDatabase dbWrite = getWritableDatabase();
        SQLiteDatabase dbRead = getReadableDatabase();
        FilmData existFilmData;
        for (FilmData film:fList) {
            existFilmData = getFilmByExternalID(dbRead, film.getId());
            if (existFilmData == null) {
                RowID = insertFilmData(dbWrite, film);
            } else {
                RowID = updateFilmData(dbRead, film);
            }
        };

    }

    public void getAllFilmsData(List<FilmData> toList){
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                FilmEntry._ID,
                FilmEntry.COLUMN_NAME_EXTERNAL_ID,
                FilmEntry.COLUMN_NAME_TITLE,
                FilmEntry.COLUMN_NAME_OVERVIEW,
                FilmEntry.COLUMN_NAME_RELEASE_DATE,
                FilmEntry.COLUMN_NAME_VOTE_AVERAGE,
                FilmEntry.COLUMN_NAME_POPULARITY,
                FilmEntry.COLUMN_POSTER_PATH,
                FilmEntry.COLUMN_POSTER_B64
        };

        Cursor cursor = dbRead.query(
                FilmEntry.TABLE_NAME,  // таблица
                projection,            // столбцы
                null,          // столбцы для условия WHERE
                null,       // значения для условия WHERE
                null,          // Don't group the rows
                null,           // Don't filter by row groups
                FilmEntry.COLUMN_NAME_TITLE + " DESC");  // порядок сортировки

        if (cursor.getCount() > 0) {
            toList.clear();
            while (cursor.moveToNext()) {
                FilmData film = new FilmData();
                film.setId(cursor.getInt(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_EXTERNAL_ID)));
                film.setTitle(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_TITLE)));
                film.setOverview(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_OVERVIEW)));
                film.setReleaseDate(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_RELEASE_DATE)));
                film.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_VOTE_AVERAGE)));
                film.setPopularity(cursor.getDouble(cursor.getColumnIndex(FilmEntry.COLUMN_NAME_POPULARITY)));
                film.setPosterPath(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_POSTER_PATH)));
                film.setPoster_b64(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_POSTER_B64)));

                toList.add(film);
            }
            Log.i("Загрузка данных", String.valueOf(toList.size()));
        }

    }

}
