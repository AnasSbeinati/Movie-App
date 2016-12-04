package com.example.anoos.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_popular:
                if (checked)
                    SaveSharedPreference.setTYPE(this, "popular");
                break;
            case R.id.radio_top:
                if (checked)
                    SaveSharedPreference.setTYPE(this, "top_rated");
                break;
            case R.id.radio_fav:
                if (checked)
                    SaveSharedPreference.setTYPE(this, "fav");
                break;
        }
        Intent mainActivity = new Intent(this,
                MainActivity.class);
        startActivity(mainActivity);
    }
}

