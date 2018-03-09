package com.abrader.tmdb_client;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.abrader.tmdb_client.data.FilmBaseHelper;
import com.squareup.picasso.Picasso;

public class FilmDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        int filmID = getIntent().getIntExtra("aka.Abrader.tmdb.filmID", 0);
        FilmBaseHelper fbh = new FilmBaseHelper(this);
        FilmData film = fbh.getFilmByExternalID(fbh.getReadableDatabase(), filmID);

        TextView vTitle = (TextView) findViewById(R.id.film_title);
        vTitle.setText(film.getTitle());

        TextView vDescription = (TextView) findViewById(R.id.film_description);
        vDescription.setText(film.getOverview());

        ImageView vPoster = (ImageView) findViewById(R.id.poster);
        Bitmap bmPoster = film.getPoster_b64_bitmap();
        if (bmPoster != null){
            vPoster.setImageBitmap(bmPoster);
        } else {
            String path = "https://image.tmdb.org/t/p/w500" + film.getPosterPath();
            Picasso.with(vPoster.getContext()).load(path).into(vPoster);
        }
    }
}
