package com.example.opensorcerer.ui.signup;

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
import androidx.navigation.fragment.NavHostFragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.SignupFirstBinding;

public class SignupFirst extends Fragment {


    private SignupFirstBinding app;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        app = SignupFirstBinding.inflate(inflater, container, false);
        return app.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app.btnNext.setOnClickListener(view1 -> {
            if(inputsAreValid()){
                NavHostFragment.findNavController(SignupFirst.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }

        });
    }

    private boolean inputsAreValid() {
        if(!isValidEmail(app.etEmail.getText())){
            Toast.makeText(getContext(),"Email is invalid",Toast.LENGTH_SHORT);
            return false;
        }
        if(!(app.etPassword.getText().equals(app.etConfirm.getText()))){
            Log.d("SignupFirst", String.format("%s %s %s", app.etPassword.getText(), app.etConfirm.getText(),app.etPassword.getText().equals(app.etConfirm.getText())));
            Toast.makeText(getContext(),"Passwords do not match",Toast.LENGTH_SHORT);
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