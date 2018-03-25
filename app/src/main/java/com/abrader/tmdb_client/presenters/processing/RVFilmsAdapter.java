package com.abrader.tmdb_client.presenters.processing;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abrader.tmdb_client.FilmDetailActivity;
import com.abrader.tmdb_client.R;
import com.abrader.tmdb_client.model.api.FilmData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
            cv = itemView.findViewById(R.id.cv);
            filmTitle = itemView.findViewById(R.id.film_title);
            poster = itemView.findViewById(R.id.poster);
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

    private List<FilmData> films;
    int screenWidth;

    public RVFilmsAdapter(int screenWidth){
        this.screenWidth = screenWidth;
        films = new ArrayList<FilmData>();
    }

    public void setData(List<FilmData> films){
        this.films.clear();
        this.films.addAll(films);
        notifyDataSetChanged();
    }

    public List<FilmData> getData(){
        return films;
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
