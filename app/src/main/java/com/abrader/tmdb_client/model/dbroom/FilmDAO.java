package com.abrader.tmdb_client.model.dbroom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.abrader.tmdb_client.model.FilmData;

import java.util.List;

@Dao
public interface FilmDAO {
    @Insert
    public void insert(FilmData film);

    @Update
    public void update(FilmData film);

    @Delete
    public void delete(FilmData film);

    @Query("SELECT * FROM films WHERE name = :id ORDER BY title DESC")
    public FilmData getFilmByExternalID(Integer id);

    @Query("SELECT * FROM films")
    public List<FilmData> getAllFilmsData();

}
