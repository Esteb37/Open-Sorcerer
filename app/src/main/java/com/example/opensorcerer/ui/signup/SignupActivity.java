package com.example.opensorcerer.ui.signup;

import android.content.Context;
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

    private static final String TAG = "SignupActivity";
    private ActivitySignupBinding app;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        mContext = this;

        ParseUser.logOut();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new SignupRoleFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();

    }
}