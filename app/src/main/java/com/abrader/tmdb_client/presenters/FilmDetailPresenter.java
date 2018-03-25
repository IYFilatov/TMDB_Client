package com.abrader.tmdb_client.presenters;

import android.graphics.Bitmap;

import com.abrader.tmdb_client.base.MasterPresenter;
import com.abrader.tmdb_client.model.api.FilmData;
import com.abrader.tmdb_client.model.localdb.FilmBaseHelper;
import com.abrader.tmdb_client.presenters.contracts.FilmDetailContract;
import com.squareup.picasso.Picasso;

public class FilmDetailPresenter extends MasterPresenter<FilmDetailContract.View> implements FilmDetailContract.Presenter {

    private FilmBaseHelper fDBHelper;

    public FilmDetailPresenter(FilmBaseHelper fDBHelper) {
        this.fDBHelper = fDBHelper;
    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void fillByID(int fID) {
        FilmData film = fDBHelper.getFilmByExternalID(fDBHelper.getReadableDatabase(), fID);
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
