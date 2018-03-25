package com.abrader.tmdb_client.presenters;

import com.abrader.tmdb_client.base.MasterPresenter;
import com.abrader.tmdb_client.preferences.Constants;
import com.abrader.tmdb_client.model.api.DbMovieAPI;
import com.abrader.tmdb_client.model.api.FilmData;
import com.abrader.tmdb_client.model.api.FilmPage;
import com.abrader.tmdb_client.model.api.MdbApiController;
import com.abrader.tmdb_client.model.localdb.FilmBaseHelper;
import com.abrader.tmdb_client.presenters.contracts.MainActivityContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityPresenter extends MasterPresenter<MainActivityContract.View> implements MainActivityContract.Presenter {

    private FilmBaseHelper fDBHelper;

    public MainActivityPresenter(FilmBaseHelper fDBHelper){
        this.fDBHelper = fDBHelper;
    }

    public void loadPage(int pageNum, String sortBy){
        /*//sync query (not for main stream):
        try {
            Response response = modelController.getTMDBApi().getMovieList(Constants.API_KEY, "en-US", "ae", "popularity.desc", false, false, 1, 2018).execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), "receive exception", e);
        }*/

        //ассихронный запрос:
        getTMDBApi().getMoviePage(Constants.API_KEY, "en-US", "ae", sortBy, false, false, pageNum).enqueue(new Callback<FilmPage>() {
            @Override
            public void onResponse(Call<FilmPage> call, Response<FilmPage> response) {
                FilmPage page = response.body();
                List<FilmData> films = getView().getrvAdapter().getData();
                films.clear();
                films.addAll(page.getResults());
                fDBHelper.updateFilmBaseData(films);
                getView().getrvAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FilmPage> call, Throwable t) {
                getView().showMessage("An error occurred during networking");
            }
        });
    }

    public void searchMovie(int pageNum, String query){
        getTMDBApi().searchMovie(Constants.API_KEY, "en-US", query, pageNum, false, "ae").enqueue(new Callback<FilmPage>() {
            @Override
            public void onResponse(Call<FilmPage> call, Response<FilmPage> response) {
                FilmPage page = response.body();
                getView().getrvAdapter().setData(page.getResults());
                fDBHelper.updateFilmBaseData(getView().getrvAdapter().getData());
            }

            @Override
            public void onFailure(Call<FilmPage> call, Throwable t) {
                getView().showMessage("An error occurred during networking");
            }
        });
    }

    @Override
    public void loadListFromDB(){
        List<FilmData> toList = getView().getrvAdapter().getData();
        fDBHelper.getAllFilmsData(toList);
        if (toList.size() > 0) {
            viewIsReady();
        }
    }

    @Override
    public void viewIsReady() {
        getView().updatePage();
    }

    private DbMovieAPI getTMDBApi(){
        return MdbApiController.getApi();
    }

    /*private void loadPosters(List<FilmData> fList){
        for (final FilmData film:fList) {
            String path = "https://image.tmdb.org/t/p/w500" + film.getPosterPath();

            Picasso.with(this).load(path).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    film.setPoster_b64_bitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            //film.setPoster_b64_bitmap(Picasso.with(this).load(path).get());
        };
    }*/

}
