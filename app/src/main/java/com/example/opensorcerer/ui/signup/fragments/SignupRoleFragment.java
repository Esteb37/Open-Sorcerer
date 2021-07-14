package com.example.opensorcerer.ui.signup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.opensorcerer.databinding.FragmentSignupRoleBinding;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.models.users.roles.Developer;
import com.example.opensorcerer.models.users.roles.Manager;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupRoleFragment extends Fragment {

    private static final String TAG = "SignupRoleFragment";
    private FragmentSignupRoleBinding app;
    private Context mContext;

    User newUser;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        app = FragmentSignupRoleBinding.inflate(inflater, container, false);
        return app.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();


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
        SignupRoleFragmentDirections.RoleToFirstAction roleToFirstAction = SignupRoleFragmentDirections.roleToFirstAction(newUser);
        NavHostFragment.findNavController(this)
                .navigate(roleToFirstAction);
        /*final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user",Parcels.wrap(newUser));
        Fragment fragment = new SignupFirstFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app = null;
    }

}