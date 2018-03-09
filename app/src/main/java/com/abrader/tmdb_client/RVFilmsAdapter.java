package com.abrader.tmdb_client;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class RVFilmsAdapter extends RecyclerView.Adapter<RVFilmsAdapter.FilmsViewHolder>{

    public static class FilmsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int filmID;
        CardView cv;
        TextView filmTitle;
        ImageView poster;

        FilmsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cv = (CardView)itemView.findViewById(R.id.cv);
            filmTitle = (TextView)itemView.findViewById(R.id.film_title);
            poster = (ImageView)itemView.findViewById(R.id.poster);
        }

        public void setFilmID(int filmID){
            this.filmID = filmID;
        }

        @Override
        public void onClick(View view) {
            //Log.d("CLICK", "onClick " + " " + filmTitle.getText());
            Context context = view.getContext();
            Intent intent = new Intent(context, FilmDetailActivity.class);
            intent.putExtra("aka.Abrader.tmdb.filmID", filmID);
            context.startActivity(intent);
        }
    }

    List<FilmData> films;
    int screenWidth;

    RVFilmsAdapter(List<FilmData> films, int screenWidth){
        this.films = films;
        this.screenWidth = screenWidth;
    }

    @Override
    public FilmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvitem, parent, false);
        FilmsViewHolder fvh = new FilmsViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(FilmsViewHolder holder, int position) {
        int imageTargetWidth = (int)((screenWidth-5) /2);
        FilmData film = films.get(position);
        holder.setFilmID(film.getId());
        holder.filmTitle.setText(film.getTitle());
        Bitmap bmPoster = film.getPoster_b64_bitmap();
        if (bmPoster != null){
            ImageResizeByWidth imageTransform = new ImageResizeByWidth(imageTargetWidth);
            holder.poster.setImageBitmap(imageTransform.transform(bmPoster));
            imageTransform = null;
        } else {
            String path = "https://image.tmdb.org/t/p/w500" + film.getPosterPath();
            Picasso.with(holder.poster.getContext()).load(path).placeholder(R.drawable.empty_poster).transform(new ImageResizeByWidth(imageTargetWidth)).into(holder.poster);
        }

    }

    @Override
    public int getItemCount() {
        return films.size();
    }


}
