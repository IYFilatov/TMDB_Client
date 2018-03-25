package com.abrader.tmdb_client.presenters.contracts;

import android.graphics.Bitmap;

import com.abrader.tmdb_client.base.*;

public interface FilmDetailContract {

    interface View extends BaseView {
        void setTitle(String title);
        void setDescription(String description);
        void setPoster(Bitmap bmPoster);
        void loadPoster(String posterPath);
        void showMessage(CharSequence message);

    }


    interface Presenter extends BasePresenter<View> {
        void fillByID(int fID);
    }

}
