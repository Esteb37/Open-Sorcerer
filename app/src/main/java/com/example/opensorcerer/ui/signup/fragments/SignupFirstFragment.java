package com.example.opensorcerer.ui.signup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.opensorcerer.databinding.FragmentSignupFirstBinding;
import com.example.opensorcerer.models.users.User;

import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupFirstFragment extends Fragment {

    private static final String TAG = "SignupRoleFragment";
    private FragmentSignupFirstBinding app;
    private Context mContext;

    private User newUser;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        app = FragmentSignupFirstBinding .inflate(inflater, container, false);
        return app.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        newUser = SignupFirstFragmentArgs.fromBundle(getArguments()).getUser();

        app.btnNext.setOnClickListener(v -> {
            if(inputsAreValid()){
                newUser.setEmail(app.etEmail.getText().toString());
                newUser.setPassword(app.etPassword.getText().toString());
                navigateForward();
            }
        });

        app.btnBack.setOnClickListener(v -> navigateBackward());
    }

    private void navigateBackward() {
        NavDirections firstToRoleAction = SignupFirstFragmentDirections.firstToRoleAction();
        NavHostFragment.findNavController(this)
                .navigate(firstToRoleAction);
    }

    private void navigateForward() {
        SignupFirstFragmentDirections.FirstToSecondAction firstToSecondAction = SignupFirstFragmentDirections.firstToSecondAction(newUser);
        NavHostFragment.findNavController(this)
                .navigate(firstToSecondAction);
    }

    private boolean inputsAreValid() {
        if(!isValidEmail(app.etEmail.getText())){
            Toast.makeText(getContext(),"Email is invalid",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(app.etPassword.getText() == app.etConfirm.getText()){
            Log.d("SignupFirst", String.format("%s %s %s", app.etPassword.getText(), app.etConfirm.getText(),app.etPassword.getText().equals(app.etConfirm.getText())));
            Toast.makeText(getContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app = null;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}