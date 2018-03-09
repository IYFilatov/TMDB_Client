package com.abrader.tmdb_client;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abrader.tmdb_client.data.FilmBaseHelper;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationView.OnCreateContextMenuListener {

    private static DbMovieAPI dmMovieApi;
    private List<FilmData> films;
    private RecyclerView rvMain;
    private FilmBaseHelper fDbHelper;
    private MaterialSearchView searchView;


    private void searchMovie(int pageNum, String query){
        dmMovieApi.searchMovie("c95f3a9820133bccee9234e973296a3a", "en-US", query, 1, false, "ae").enqueue(new Callback<FilmPage>() {
            @Override
            public void onResponse(Call<FilmPage> call, Response<FilmPage> response) {
                FilmPage page = response.body();
                films.clear();
                films.addAll(page.getResults());
                //loadPosters(films);
                fDbHelper.updateFilmBaseData(films);
                rvMain.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FilmPage> call, Throwable t) {
                Toast.makeText(MainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPage(int pageNum, String sortBy){
        /*//синхронный запрос (ждет ответа в основном потоке не практикуется:
        try {
            Response response = dmMovieApi.getMovieList("c95f3a9820133bccee9234e973296a3a", "en-US", "ae", "popularity.desc", false, false, 1, 2018).execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), "Получено исключение", e);
        }*/

        //ассихронный запрос:
        dmMovieApi.getMoviePage("c95f3a9820133bccee9234e973296a3a", "en-US", "ae", sortBy, false, false, pageNum).enqueue(new Callback<FilmPage>() {
            @Override
            public void onResponse(Call<FilmPage> call, Response<FilmPage> response) {
                FilmPage page = response.body();
                films.clear();
                films.addAll(page.getResults());
                //loadPosters(films);
                fDbHelper.updateFilmBaseData(films);
                rvMain.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FilmPage> call, Throwable t) {
                /*if (t instanceof IOException) {
                    Toast.makeText(RVActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RVActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                }
                Log.e("RVActivity", t.toString());*/
                Toast.makeText(MainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPosters(List<FilmData> fList){
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
    }

    private void loadListFromDB(){
        fDbHelper.getAllFilmsData(films);
        if (films.size() > 0) {
            rvMain.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menuNav = navigationView.getMenu();
        final MenuItem searchItem = menuNav.findItem(R.id.action_search);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setMenuItem(searchItem);
        //searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        //searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "Search for " + query, Toast.LENGTH_SHORT).show();
                searchMovie(1, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        films = new ArrayList<FilmData>();
        rvMain = (RecyclerView)findViewById(R.id.rv_main);
        //rvMain.setHasFixedSize(true);
        GridLayoutManager lm = new GridLayoutManager(this, 2);
        rvMain.setLayoutManager(lm);
        RVFilmsAdapter adapter = new RVFilmsAdapter(films, this.getWindow().getDecorView().getWidth());
        rvMain.setAdapter(adapter);

        fDbHelper = new FilmBaseHelper(this);
        dmMovieApi = MdbApiController.getApi();

        loadListFromDB();
        loadPage(1, "primary_release_date.desc");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
                searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.popular) {
            loadPage(1, "popularity.desc");
        } else if (id == R.id.top) {
            loadPage(1, "vote_average.desc");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
