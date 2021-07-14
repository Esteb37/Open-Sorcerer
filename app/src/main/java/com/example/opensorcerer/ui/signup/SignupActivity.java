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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.opensorcerer.R;
import com.parse.ParseUser;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private ActivitySignupBinding app;
    private Context mContext;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        mContext = this;

        ParseUser.logOut();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new SignupRoleFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}