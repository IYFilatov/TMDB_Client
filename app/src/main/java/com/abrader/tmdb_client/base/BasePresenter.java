package com.abrader.tmdb_client.base;

public interface BasePresenter<V extends BaseView> {

    void attachView(V incomingView);

    void viewIsReady();

    void detachView();

    void destroy();
}
