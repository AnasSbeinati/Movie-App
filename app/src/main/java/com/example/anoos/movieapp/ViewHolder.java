package com.example.anoos.movieapp;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Anoos on 10/21/2016.
 */
public class ViewHolder {
    public ImageView movie;
    public ViewHolder(View view) {
        movie = (ImageView) view.findViewById(R.id.imageView);
    }

}
