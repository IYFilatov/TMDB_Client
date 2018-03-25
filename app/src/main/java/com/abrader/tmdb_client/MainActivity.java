package com.abrader.tmdb_client;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abrader.tmdb_client.model.localdb.FilmBaseHelper;
import com.abrader.tmdb_client.presenters.MainActivityPresenter;
import com.abrader.tmdb_client.presenters.contracts.MainActivityContract;
import com.abrader.tmdb_client.presenters.processing.RVFilmsAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View, NavigationView.OnNavigationItemSelectedListener, NavigationView.OnCreateContextMenuListener {

    private RecyclerView rvMain;
    private MaterialSearchView searchView;
    private MainActivityContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menuNav = navigationView.getMenu();
        final MenuItem searchItem = menuNav.findItem(R.id.action_search);

        searchView = findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setMenuItem(searchItem);
        //searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        //searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //showMessage("Search for " + query);
                presenter.searchMovie(1, query);
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
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        rvMain = findViewById(R.id.rv_main);
        //rvMain.setHasFixedSize(true);
        GridLayoutManager lm = new GridLayoutManager(this, 2);
        rvMain.setLayoutManager(lm);
        RVFilmsAdapter adapter = new RVFilmsAdapter(this.getWindow().getDecorView().getWidth());
        rvMain.setAdapter(adapter);



        FilmBaseHelper fDbHelper = new FilmBaseHelper(this);
        presenter = new MainActivityPresenter(fDbHelper);
        presenter.attachView(this);
        presenter.loadListFromDB();
        presenter.loadPage(1, "primary_release_date.desc");
        presenter.viewIsReady();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (isFinishing()) {
            presenter.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            presenter.loadPage(1, "popularity.desc");
        } else if (id == R.id.top) {
            presenter.loadPage(1, "vote_average.desc");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updatePage() {
        getrvAdapter().notifyDataSetChanged();
    }

    @Override
    public RVFilmsAdapter getrvAdapter() {
        return (RVFilmsAdapter) rvMain.getAdapter();
    }

    @Override
    public void showMessage(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void close() {
        finish();
    }
}
