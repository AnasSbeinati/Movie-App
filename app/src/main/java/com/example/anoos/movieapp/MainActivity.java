package com.example.anoos.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragmenet.Communicator{
    MainActivityFragmenet mainFragment;
    DetailActivityFragment detailActivityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainFragment=(MainActivityFragmenet)getSupportFragmentManager().findFragmentById(R.id.fragment);
        mainFragment.setCommunicator(this);
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
        if (id == R.id.action_settings) {
            Intent secondActivity = new Intent(MainActivity.this,
                    SettingsActivity.class);
            startActivity(secondActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void respond(Movie movie) {
        detailActivityFragment=(DetailActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment1);
        if(detailActivityFragment!=null && detailActivityFragment.isVisible()){
            detailActivityFragment.updateMovie(movie);
        }
        else{
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        }

    }
}
