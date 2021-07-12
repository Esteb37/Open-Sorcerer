package com.example.opensorcerer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.opensorcerer.models.users.roles.Developer;
import com.example.opensorcerer.models.users.roles.Manager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Developer developer = new Developer();
        Manager manager = new Manager();

    }
}