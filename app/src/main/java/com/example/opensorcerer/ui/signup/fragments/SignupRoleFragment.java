package com.example.opensorcerer.ui.signup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentSignupRoleBinding;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.models.users.roles.Developer;
import com.example.opensorcerer.models.users.roles.Manager;

import org.parceler.Parcels;

public class SignupRoleFragment extends Fragment {

    private FragmentSignupRoleBinding app;

    User newUser;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        app = FragmentSignupRoleBinding.inflate(inflater, container, false);
        return app.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app.btnDeveloper.setOnClickListener(v-> {
            newUser = new Developer();
            navigateToSignup();
        });

        app.btnManager.setOnClickListener(v-> {
            newUser = new Manager();
            navigateToSignup();
        });
    }

    private void navigateToSignup() {
        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user",Parcels.wrap(newUser));
        Fragment fragment = new SignupFirstFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app = null;
    }

}