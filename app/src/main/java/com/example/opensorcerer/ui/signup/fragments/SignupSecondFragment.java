package com.example.opensorcerer.ui.signup.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentSignupSecondBinding;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.ui.developer.DeveloperHomeActivity;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import org.parceler.Parcels;

public class SignupSecondFragment extends Fragment {

    private static final String TAG = "SignupSecondFragment";
    private FragmentSignupSecondBinding app ;
    private Context mContext;



    private User newUser;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        app = FragmentSignupSecondBinding.inflate(inflater, container, false);
        return app.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        mContext = getContext();

        newUser = Parcels.unwrap(getArguments().getParcelable("user"));

        app.btnBack.setOnClickListener(v -> {
            Fragment fragment = new SignupFirstFragment();
            fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();
        });

        app.btnFinish.setOnClickListener(v -> {

            newUser.setUsername(app.etUsername.getText().toString());
            newUser.setGithubToken(app.etToken.getText().toString());

            newUser.getHandler().signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Log.d("Signup","User created successfully");
                        Intent i = new Intent(getContext(), DeveloperHomeActivity.class);
                        startActivity(i);
                    }
                    else{
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app = null;
    }

}