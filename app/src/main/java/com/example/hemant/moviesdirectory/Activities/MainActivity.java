package com.example.hemant.moviesdirectory.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hemant.moviesdirectory.Data.MovieRecyclerViewAdapter;
import com.example.hemant.moviesdirectory.Model.Movie;
import com.example.hemant.moviesdirectory.R;
import com.example.hemant.moviesdirectory.Util.Constants;
import com.example.hemant.moviesdirectory.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder alertdialogBuilder;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue= Volley.newRequestQueue(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputdialog();

            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Prefs prefs=new Prefs(MainActivity.this);
        String search=prefs.getSearch();
        movieList=new ArrayList<>();


        movieList=getMovies(search);
        movieRecyclerViewAdapter=new MovieRecyclerViewAdapter(this,movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            showInputdialog();

        }


        return super.onOptionsItemSelected(item);
    }
    public void showInputdialog(){
        alertdialogBuilder=new AlertDialog.Builder(this);
        View view =getLayoutInflater().inflate(R.layout.dialog_view,null);
        final EditText searchEdit=(EditText)view.findViewById(R.id.searchet);
        Button submit=(Button) view.findViewById(R.id.submitbutton);
        alertdialogBuilder.setView(view);
        alertDialog=alertdialogBuilder.create();
        alertDialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs prefs=new Prefs(MainActivity.this);
                if(!searchEdit.getText().toString().isEmpty()){
                    String search=searchEdit.getText().toString();
                    prefs.setSearch(search);
                    movieList.clear();

                    getMovies(search);
                    movieRecyclerViewAdapter.notifyDataSetChanged();
                }
                alertDialog.dismiss();
            }
        });
    }
    public List<Movie> getMovies(String searchterm){
        movieList.clear();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Constants.URL_LEFT + searchterm, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray movieArray=response.getJSONArray("results");

                    for(int i=0;i<movieArray.length();i++){
                        JSONObject movieObj=movieArray.getJSONObject(i);
                        JSONArray genreArray=movieObj.getJSONArray("genre_ids");
                        StringBuilder genre=new StringBuilder();
                        for(int j=0;j<genreArray.length();j++){
                            genre.append(Constants.genres.get(genreArray.getInt(j)));
                            if(j+1!=genreArray.length())
                                genre.append(", ");
                        }
                        Movie movie=new Movie();
                        movie.setTitle(movieObj.getString("title"));
                        movie.setPoster(movieObj.getString("poster_path"));
                        movie.setDate("Released On: "+movieObj.getString("release_date"));
                        movie.setMovieType(genre.toString());
                        movie.setId(movieObj.getString("id"));

                        // Log.d("Movies: ",movie.getTitle()+"   "+movie.getMovieType());
                        movieList.add(movie);


                    }
                    movieRecyclerViewAdapter.notifyDataSetChanged();
                }
                catch(JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
        return movieList;
    }


}



