package com.abrader.tmdb_client.presenters;

import android.graphics.Bitmap;

import com.abrader.tmdb_client.base.MasterPresenter;
import com.abrader.tmdb_client.model.FilmData;
import com.abrader.tmdb_client.model.dbmanual_not_in_use.FilmBaseHelper;
import com.abrader.tmdb_client.model.dbroom.AppDatabase;
import com.abrader.tmdb_client.model.dbroom.FilmDAO;
import com.abrader.tmdb_client.presenters.contracts.FilmDetailContract;

public class FilmDetailPresenter extends MasterPresenter<FilmDetailContract.View> implements FilmDetailContract.Presenter {

    private AppDatabase dbRoom;

    public FilmDetailPresenter(AppDatabase dbRoom) {
        this.dbRoom = dbRoom;
    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void fillByID(int fID) {
        FilmDAO filmDAO = dbRoom.getFilmDAO();
        FilmData film = filmDAO.getFilmByExternalID(fID);
        getView().setTitle(film.getTitle());
        getView().setDescription(film.getOverview());
        Bitmap bmPoster = film.getPoster_b64_bitmap();
        if (bmPoster != null){
            getView().setPoster(bmPoster);
        } else {
            getView().loadPoster("https://image.tmdb.org/t/p/w500" + film.getPosterPath());
        }
    }
}
