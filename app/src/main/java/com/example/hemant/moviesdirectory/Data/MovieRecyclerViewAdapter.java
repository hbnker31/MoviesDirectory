package com.example.hemant.moviesdirectory.Data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hemant.moviesdirectory.Activities.MovieDetails;
import com.example.hemant.moviesdirectory.Model.Movie;
import com.example.hemant.moviesdirectory.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hemant on 21-08-2017.
 */

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Movie> movieList;
    public MovieRecyclerViewAdapter(Context context, List<Movie> movies ) {
        this.context=context;
        movieList=movies;
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row,parent,false);


        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(MovieRecyclerViewAdapter.ViewHolder holder, int position) {
        Movie movie=movieList.get(position);
        String posterLink="https://image.tmdb.org/t/p/w500" + movie.getPoster();
        holder.title.setText(movie.getTitle());
        holder.type.setText(movie.getMovieType());

       Picasso.with(context).load(posterLink)
               .placeholder(android.R.drawable.btn_star)
               .into(holder.poster);
        holder.date.setText(movie.getDate());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private ImageView poster;
        private TextView date;
        private TextView type;


        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            title = (TextView) itemView.findViewById(R.id.movietitle);
            poster = (ImageView) itemView.findViewById(R.id.movieimage);
            date = (TextView) itemView.findViewById(R.id.moviereleaseID);
            type = (TextView) itemView.findViewById(R.id.moviecategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Movie movie = movieList.get(getAdapterPosition());

                    Intent intent = new Intent(context, MovieDetails.class);

                    intent.putExtra("movie", movie);
                    ctx.startActivity(intent);

                }
            });

        }

        @Override
        public void onClick(View view) {

        }
    }
}

