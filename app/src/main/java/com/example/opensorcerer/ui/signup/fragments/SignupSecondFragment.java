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
import androidx.navigation.fragment.NavHostFragment;

import com.example.opensorcerer.databinding.FragmentSignupSecondBinding;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.ui.developer.DeveloperHomeActivity;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupSecondFragment extends Fragment {

    private static final String TAG = "SignupSecondFragment";
    private FragmentSignupSecondBinding app ;
    private Context mContext;



    private User newUser;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        app = FragmentSignupSecondBinding.inflate(inflater, container, false);
        return app.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        mContext = getContext();

        newUser = SignupSecondFragmentArgs.fromBundle(getArguments()).getUser();

        app.btnBack.setOnClickListener(v -> navigateBack());

        app.btnFinish.setOnClickListener(v -> {

            newUser.setUsername(app.etUsername.getText().toString());
            newUser.setGithubToken(app.etToken.getText().toString());

            newUser.getHandler().signUpInBackground(e -> {
                if(e==null){
                    Log.d("Signup","User created successfully");
                    Intent i = new Intent(getContext(), DeveloperHomeActivity.class);
                    startActivity(i);
                }
                else{
                    e.printStackTrace();
                }
            });
        });
    }

    private void navigateBack() {
        SignupSecondFragmentDirections.SecondToFirstAction secondToFirstAction = SignupSecondFragmentDirections.secondToFirstAction(newUser);
        NavHostFragment.findNavController(this)
                .navigate(secondToFirstAction);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app = null;
    }

}