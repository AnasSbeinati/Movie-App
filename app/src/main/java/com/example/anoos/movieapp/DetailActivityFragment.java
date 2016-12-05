package com.example.anoos.movieapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private Movie movie;
    ArrayList<Trailer> trailers;
    private ListView trailersList;
    TextView detailed_title;
    TextView detailed_released_date;
    TextView detailed_overview;
    RatingBar ratingBar;
    ToggleButton toggleButton;
    ImageView poster ;
    String reviews = "";
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        trailersList = (ListView) rootView.findViewById(R.id.trailers_list);
        poster = (ImageView) rootView.findViewById(R.id.poster_img);
        detailed_title = ((TextView) rootView.findViewById(R.id.detailed_title));
        detailed_released_date = ((TextView) rootView.findViewById(R.id.detailed_released_date));
        detailed_overview = ((TextView) rootView.findViewById(R.id.detailed_overview));
        ratingBar = ((RatingBar) rootView.findViewById(R.id.ratingBar));
        ratingBar.setNumStars(5);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movie")) {
            movie = (Movie) intent.getSerializableExtra("movie");
            this.updateMovie(movie);
        }
        trailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(i).getKey()));
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.reviews_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setTitle("Reviews");
                if(reviews.equals(""))
                    reviews = "No Reviews";
                builder2.setMessage(reviews).setPositiveButton("ok", dialogClickListener).show();
            }
        });
            toggleButton = (ToggleButton) rootView.findViewById(R.id.myToggleButton);
            toggleButton.setChecked(false);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.img_star_grey));
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        DB db = new DB(getActivity());
                        db.insertMovie(movie);
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.img_star_yellow));
                    }  else
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.img_star_grey));
                }
            });
        return rootView;
    }

    public void updateMovie(Movie movie) {
        this.movie = movie;
        new TrailersDownloader().execute();
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500" +
                movie.getImagepath())
                .into(poster);
        detailed_title.setText(movie.getTitle());
        detailed_released_date.setText(movie.getDate());
        detailed_overview.setText(movie.getOverview());
        ratingBar.setRating(movie.getRate()/2.0f );
    }

    private class TrailersDownloader extends AsyncTask<String, Void, ArrayList<Trailer>> {
        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            super.onPostExecute(trailers);
            trailersList.setAdapter(new TrailersListViewAdapter(getActivity(), trailers));
            if (trailers != null) {
                trailersList.setAdapter(new TrailersListViewAdapter(getActivity(), trailers));
            }
            new ReviewsDownloader().execute();

        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... voids) {
            BufferedReader reader = null;
            String response = null;
            try {
                trailers = new ArrayList<Trailer>();
                HttpURLConnection urlConnection;
                String baseUrl = "http://api.themoviedb.org/3/movie/";
                baseUrl += movie.getId() + "/" + "videos" + "?";
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
                    return parseJSONToGetTrailers(response);
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
    public ArrayList<Trailer> parseJSONToGetTrailers(String dataJson) throws JSONException {
            JSONObject jsObject = new JSONObject(dataJson);
            JSONArray jsArray = jsObject.getJSONArray("results");
            for (int i = 0; i < jsArray.length(); i++) {
                Trailer trailer = new Trailer();
                trailer.setName(jsArray.getJSONObject(i).getString("name"));
                trailer.setKey(jsArray.getJSONObject(i).getString("key"));
                trailer.setId(jsArray.getJSONObject(i).getString("id"));
                trailers.add(trailer);
            }
            return trailers;
        }
    private class ReviewsDownloader extends AsyncTask<String, Void, String> {
            @Override
            protected void onPostExecute(String reviews1) {
                super.onPostExecute(reviews1);
            }
            @Override
            protected String doInBackground(String... voids) {
                BufferedReader reader = null;
                String response =null;
                try {
                    HttpURLConnection urlConnection ;
                    String baseUrl = "http://api.themoviedb.org/3/movie/";
                    baseUrl += movie.getId() + "/" + "reviews" + "?";
                    String api_key =  "7c24faa8de193ba5fe2198c770fd1429";
                    Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
                    builder.appendQueryParameter("api_key",api_key);
                    URL url = new URL(builder.build().toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null ;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    if (buffer.length() == 0) {
                        return null ;
                    }
                    response = buffer.toString();
                    try {
                        return parseJSONToGetReviews(response);
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
    public String parseJSONToGetReviews(String dataJson) throws JSONException {
        JSONObject jsObject = new JSONObject(dataJson);
        JSONArray jsArray = jsObject.getJSONArray("results");
        for (int i = 0; i < jsArray.length(); i++) {
            JSONObject review = jsArray.getJSONObject(i);
            reviews += "Author : \n    " + review.getString("author") + "\n";
            reviews += "Content :\n   " + review.getString("content") + "\n";
            reviews += "**********\n";
        }
        return reviews;
    }

}
