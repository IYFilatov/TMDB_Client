package com.abrader.tmdb_client.presenters.contracts;

import android.content.Context;

import com.abrader.tmdb_client.base.*;
import com.abrader.tmdb_client.presenters.processing.RVFilmsAdapter;

import java.util.List;

public interface MainActivityContract {

    interface View extends BaseView {

        void updatePage();
        RVFilmsAdapter getrvAdapter();
        void showMessage(CharSequence message);
        void close();
    }


    interface Presenter extends BasePresenter<MainActivityContract.View> {
        void loadListFromDB();
        void loadPage(int pageNum, String sortBy);
        void searchMovie(int pageNum, String query);
    }

}
