package com.example.anoos.movieapp;

/**
 * Created by BoDy on 03/12/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Movies.db";
    public static final String TABLE_NAME = "Movie";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POSTER = "poster";
    public static final int    DATABASE_VERSION =1;
    private HashMap hp;

    public DB(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_TABLE=" CREATE TABLE " +this.TABLE_NAME+" ( "+
                this.COLUMN_ID+" INTEGER PRIMARY KEY , "+
                this.COLUMN_RATING+" INTEGER , "+
                this.COLUMN_TITLE+" VARCHAR(100) , " +
                this.COLUMN_POSTER+" VARCHAR(100) , "+
                this.COLUMN_OVERVIEW+" VARCHAR(300) , " +
                this.COLUMN_DATE+" VARCHAR(30)); ";

        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String DROP_TABLE="DROP TABLE IF EXISTS "+this.TABLE_NAME;
        db.execSQL(DROP_TABLE);
    }

    public boolean insertMovie (Movie movie) {
        if(movie != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", movie.getId());
            contentValues.put("rating", movie.getRate());
            contentValues.put("date", movie.getDate());
            contentValues.put("title", movie.getTitle());
            contentValues.put("overview", movie.getOverview());
            contentValues.put("poster", movie.getImagepath());
            db.insert(this.TABLE_NAME, null, contentValues);
        }
        return true;
    }
    public ArrayList<Movie> getMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + this.TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Movie movie = new Movie();
            movie.setId(res.getInt(res.getColumnIndex(COLUMN_ID)));
            movie.setDate(res.getString(res.getColumnIndex(COLUMN_DATE)));
            movie.setOverview(res.getString(res.getColumnIndex(COLUMN_OVERVIEW)));
            movie.setImagepath(res.getString(res.getColumnIndex(COLUMN_POSTER)));
            movie.setRate(res.getInt(res.getColumnIndex(COLUMN_RATING)));
            movie.setTitle(res.getString(res.getColumnIndex(COLUMN_TITLE)));
            movies.add(movie);
            res.moveToNext();
        }
        System.out.print("size " + movies.size());
        return movies;
    }
}
