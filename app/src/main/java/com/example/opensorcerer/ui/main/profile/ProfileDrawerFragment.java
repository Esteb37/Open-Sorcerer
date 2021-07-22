package com.example.opensorcerer.ui.main.profile;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentFavoritesBinding;
import com.example.opensorcerer.databinding.FragmentRightDrawerBinding;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.login.LoginActivity;
import com.example.opensorcerer.ui.main.HomeActivity;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public class ProfileDrawerFragment extends androidx.fragment.app.Fragment {

    private FragmentRightDrawerBinding app;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        app = FragmentRightDrawerBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app.logoutLayout.setOnClickListener(v->{
            ParseUser.logOutInBackground(e -> {
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                requireActivity().finish();
            });
        });
    }

}