package com.example.hemant.moviesdirectory.Util;

import java.util.HashMap;

/**
 * Created by Hemant on 21-08-2017.
 */

public class Constants {
    public static final String URL_LEFT = "http://api.themoviedb.org/3/search/movie?api_key=2f58894cd4a6129876a761eab25c0046&query=";
    public static final String URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "?api_key=2f58894cd4a6129876a761eab25c0046";
    public static final String CREDIT_KEY = "/credits?api_key=2f58894cd4a6129876a761eab25c0046";
    public static final String URL_RIGHT = "&page=2";
    public static final HashMap<Integer, String> genres;
    static
    {
        genres = new HashMap<Integer, String>();
        genres.put(28, "Action");
        genres.put(12, "Adventure");
        genres.put(16,"Animation");
        genres.put(35,"Comedy");
        genres.put(80,"Crime");
        genres.put(99,"Documentary");
        genres.put(18,"Drama");
        genres.put(10751,"Family");
        genres.put(14,"Fantasy");
        genres.put(36,"History");
        genres.put(27,"Horror");
        genres.put(10402,"Music");
        genres.put(9648,"Mystery");
        genres.put(10749,"Romance");
        genres.put(878,"Science Fiction");
        genres.put(10770,"TV Movie");
        genres.put(53,"Thriller");
        genres.put(10752,"War");
        genres.put(37,"Western");

    }

}
