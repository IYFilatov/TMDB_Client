package com.abrader.tmdb_client;

import android.arch.persistence.room.Room;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abrader.tmdb_client.model.dbmanual_not_in_use.FilmBaseHelper;
import com.abrader.tmdb_client.model.dbroom.AppDatabase;
import com.abrader.tmdb_client.presenters.FilmDetailPresenter;
import com.abrader.tmdb_client.presenters.contracts.FilmDetailContract;
import com.squareup.picasso.Picasso;

public class FilmDetailActivity extends AppCompatActivity implements FilmDetailContract.View {

    private FilmDetailContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        int filmID = getIntent().getIntExtra("aka.Abrader.tmdb.filmID", 0);

        AppDatabase dbRoom = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tmdb_storage").allowMainThreadQueries().build();
        presenter = new FilmDetailPresenter(dbRoom);
        presenter.attachView(this);
        presenter.fillByID(filmID);

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
    public void setTitle(String title){
        TextView vTitle = findViewById(R.id.film_title);
        vTitle.setText(title);
    }

    @Override
    public void setDescription(String description){
        TextView vDescription = findViewById(R.id.film_description);
        vDescription.setText(description);
    }

    @Override
    public void setPoster(Bitmap bmPoster) {
        if (bmPoster != null){
            ImageView vPoster = findViewById(R.id.poster);
            vPoster.setImageBitmap(bmPoster);
        }
    }

    @Override
    public void loadPoster(String posterPath) {
        ImageView vPoster = findViewById(R.id.poster);
        Picasso.with(vPoster.getContext()).load(posterPath).into(vPoster);
    }

    @Override
    public void showMessage(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
