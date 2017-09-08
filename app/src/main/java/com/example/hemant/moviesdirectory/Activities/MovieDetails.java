package com.example.hemant.moviesdirectory.Activities;

import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hemant.moviesdirectory.Model.Movie;
import com.example.hemant.moviesdirectory.R;
import com.example.hemant.moviesdirectory.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetails extends AppCompatActivity {

    private Movie movie;
    private TextView movieTitle;
    private ImageView movieImage;
    private TextView movieYear;
    private TextView directors;
    private TextView actors;
    private TextView category;
    private TextView rating;
    private TextView plot;
    private TextView boxOffice;
    private TextView runtime;
    private TextView writers;

    private RequestQueue queue;
    private String movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        queue= Volley.newRequestQueue(this);
        movie=(Movie) getIntent().getSerializableExtra("movie");
        movieId=movie.getId();

        setupUI();

        getMovieDetails(movieId);
        getPeopleDetails(movieId);
    }

    private void getMovieDetails(String movieId) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Constants.URL + movieId + Constants.API_KEY, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray genrearray=response.getJSONArray("genres");

                    StringBuilder genre=new StringBuilder();
                    for(int j=0;j<genrearray.length();j++){
                        genre.append(genrearray.getJSONObject(j).getString("name"));
                        if(j+1!=genrearray.length())
                            genre.append(", ");
                    }
                    category.setText("Genre: "+genre.toString());
                    movieTitle.setText(response.getString("title"));
                    movieYear.setText("Released on: "+response.getString("release_date" ));
                    Float rate=Float.parseFloat(response.getString("vote_average"));
                    rating.setText("Rating: "+rate.toString()+"/10");
                    runtime.setText("RunTime: "+response.getString("runtime")+" mins");
                    plot.setText("Plot: "+response.getString("overview"));
                    Integer money=Integer.parseInt(response.getString("revenue"));
                    boxOffice.setText("Collection: $"+ NumberFormat.getIntegerInstance().format(money));
                    Picasso.with(getApplicationContext())
                            .load("https://image.tmdb.org/t/p/w500" + response.getString("poster_path"))
                            .into(movieImage);



                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ",error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);

    }

    private void getPeopleDetails(String movieId) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Constants.URL + movieId + Constants.CREDIT_KEY, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String actor="";
                    JSONArray people=response.getJSONArray("cast");
                    for(int i=0;i<5;i++){
                        actor+=people.getJSONObject(i).getString("name");
                        if(i!=4)
                            actor+=", ";
                    }
                    actors.setText("Actors: "+actor+".");
                    String director="",writer="";
                    JSONArray crewarray=response.getJSONArray("crew");
                    for(int i=0;i<crewarray.length();i++){
                        String direct=crewarray.getJSONObject(i).getString("job");
                        String dept=crewarray.getJSONObject(i).getString("department");
                        if(direct.equals("Director")){
                            director+=crewarray.getJSONObject(i).getString("name");
                            director+=", ";
                        }
                        if(dept.equals("Writing")){
                            writer+=crewarray.getJSONObject(i).getString("name")+" ("+crewarray.getJSONObject(i).getString("job")+"), ";
                        }
                    }

                    if(director.charAt(director.length()-2)==','){
                        director=director.substring(0,director.length()-2);
                        director+=".";

                    }
                    if(writer.charAt(writer.length()-2)==','){
                        writer=writer.substring(0,writer.length()-2);
                        writer+=".";
                    }
                    Log.d("movie:",actor);
                    writers.setText("Writer: "+writer);
                    directors.setText("Director: "+director);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ",error.getMessage());

            }
        });
        queue.add(jsonObjectRequest);
    }



    private void setupUI() {
        movieTitle = (TextView) findViewById(R.id.detailmovietitle);
        movieImage = (ImageView) findViewById(R.id.movieImageIDDets);
        movieYear = (TextView) findViewById(R.id.detailmovierelease);
        directors = (TextView) findViewById(R.id.directedByDet);
        category = (TextView) findViewById(R.id.movieCatIDDet);
        rating = (TextView) findViewById(R.id.movieRatingIDDet);
        writers=(TextView)findViewById(R.id.writersDet);
        plot = (TextView) findViewById(R.id.plotDet);
        boxOffice = (TextView) findViewById(R.id.boxOfficeDet);
        runtime = (TextView) findViewById(R.id.runtimeDet);
        actors = (TextView) findViewById(R.id.actorsDet);

    }
}
