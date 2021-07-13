package com.example.opensorcerer.ui.signup;

import android.os.Bundle;


import com.example.opensorcerer.databinding.ActivitySignupBinding;
import com.example.opensorcerer.models.users.roles.Developer;
import com.example.opensorcerer.models.users.roles.Manager;
import com.example.opensorcerer.ui.signup.fragments.SignupRoleFragment;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.opensorcerer.R;
import com.parse.ParseUser;

public class SignupActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySignupBinding app;

    public static Manager mNewManager;
    public static Developer mNewDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser.logOut();
        app = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        setSupportActionBar(app.toolbar);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new SignupRoleFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();

    }
}