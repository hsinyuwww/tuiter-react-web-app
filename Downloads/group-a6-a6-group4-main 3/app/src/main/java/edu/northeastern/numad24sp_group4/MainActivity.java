package edu.northeastern.numad24sp_group4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void launchA6Activity(View view) {
        Intent intent = new Intent(this, Activity6.class);
        startActivity(intent);
    }
}