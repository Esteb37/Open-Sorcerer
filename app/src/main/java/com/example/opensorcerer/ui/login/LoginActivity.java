package com.example.opensorcerer.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.ActivityLoginBinding;
import com.example.opensorcerer.ui.MainActivity;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding app;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        if(ParseUser.getCurrentUser()!=null){
            navigateToMain();
        }

        app = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        app.btnLogin.setOnClickListener(v -> {
            String username = app.etUsername.getText().toString();
            String password = app.etPassword.getText().toString();
            ParseUser.logInInBackground(username, password, (user, e) -> {

                //If the login is successful
                if(e==null){
                    navigateToMain();
                } else{
                    Log.d(TAG,"Issue with login",e);

                    //Notify the user that the login was unsuccessful
                    Toast.makeText(mContext,"Username or password is incorrect.",Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void navigateToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}