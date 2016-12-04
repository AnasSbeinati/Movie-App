package com.example.anoos.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anoos on 12/3/2016.
 */
public class TrailersListViewAdapter extends BaseAdapter {
    private Context context;
    ArrayList<Trailer> trailers;
    public TrailersListViewAdapter(Context context, ArrayList<Trailer> trailers) {
       // super(context,R.layout.trailer_item);
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int i) {
        return trailers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.print("in get view 1");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.trailer_item, null, true);
        TextView name = (TextView) listViewItem.findViewById(R.id.trailer_name);
        System.out.print("in get view 2" + trailers.get(position).getName());
        name.setText(trailers.get(position).getName());
        return  listViewItem;
    }
}
