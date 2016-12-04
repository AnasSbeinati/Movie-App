package com.example.anoos.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivityFragmenet extends Fragment {

    private ArrayList<Movie> movies;
    View v;
    GridView gridView;

    public MainActivityFragmenet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            v = inflater.inflate(R.layout.fragment_main_activity_fragmenet, container, false);
            gridView = (GridView) v.findViewById(R.id.gridView);
            new MoviesDownloader().execute("data");
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Movie movie = movies.get(i);
                    Intent intent = new Intent(getActivity(),DetailActivity.class);
                    intent.putExtra("movie",movie);
                    startActivity(intent);
                }
            });
            return v;
        } catch (Exception e) {
            Log.e("line 35", e.getMessage());
            return null;
        }
    }

    private class MoviesDownloader extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            if(movies != null)
                gridView.setAdapter(new GridViewAdapter(getActivity(), movies));
        }
        @Override
        protected ArrayList<Movie> doInBackground(String... voids) {
            String type;
            type = SaveSharedPreference.gettype(getActivity());
            if(type == null || type.equals(""))
                type = "popular";
            if(type.equals("fav")) {
                DB db = new DB(getActivity());
                movies = db.getMovies();
                return movies;
            }else {
                BufferedReader reader = null;
                String response = null;
                try {
                    movies = new ArrayList<Movie>();
                    HttpURLConnection urlConnection;
                    String baseUrl = "http://api.themoviedb.org/3/movie/";
                    baseUrl += type + "?";
                    String api_key = "7c24faa8de193ba5fe2198c770fd1429";
                    Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
                    builder.appendQueryParameter("api_key", api_key);
                    URL url = new URL(builder.build().toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    if (buffer.length() == 0) {
                        return null;
                    }
                    response = buffer.toString();
                    try {
                        return parseJSONToGetMovies(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                } catch (IOException e) {
                    System.out.println("error" + e.toString());
                    e.printStackTrace();
                    return null;
                }
            }
        }

        public ArrayList<Movie> parseJSONToGetMovies(String dataJson) throws JSONException {
            JSONObject jsonObject=new JSONObject(dataJson);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            for(int i=0;i<jsonArray.length();i++){
                Movie mov = new Movie();
                mov.setId(jsonArray.getJSONObject(i).getInt("id"));
                mov.setOverview(jsonArray.getJSONObject(i).getString("overview"));
                mov.setDate(jsonArray.getJSONObject(i).getString("release_date"));
                mov.setTitle(jsonArray.getJSONObject(i).getString("title"));
                mov.setImagepath(jsonArray.getJSONObject(i).getString("poster_path"));
                mov.setRate(jsonArray.getJSONObject(i).getInt("vote_average"));
                movies.add(mov);
            }
            return movies;
        }
    }

}
