package com.abrader.tmdb_client.base;

public abstract class MasterPresenter<T extends BaseView> implements BasePresenter<T>{

    private T view;

    public T getView() {
        return view;
    }

    protected boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void attachView(T incomingView) {
        view = incomingView;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void destroy() {

    }
}
