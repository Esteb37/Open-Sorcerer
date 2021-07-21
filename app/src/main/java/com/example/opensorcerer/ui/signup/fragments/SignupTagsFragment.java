package com.example.opensorcerer.ui.signup.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentSignupTagsBinding;
import com.example.opensorcerer.models.Tags;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.ui.developer.DeveloperHomeActivity;
import com.example.opensorcerer.ui.manager.ManagerHomeActivity;
import com.google.android.material.chip.ChipDrawable;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Fragment for adding interested categories and languages
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupTagsFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "SignupTagsFragment";

    /**
     * Binder for View Binding
     */
    private FragmentSignupTagsBinding app;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Newly created user for signup
     */
    private User mNewUser;

    /**User's selected profile picture*/
    private Bitmap mProfilePicture;



    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = FragmentSignupTagsBinding.inflate(inflater, container, false);
        return app.getRoot();

    }

    /**
     * Sets up the fragment's methods
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        //Get the user created in the Role fragment
        mNewUser = SignupTagsFragmentArgs.fromBundle(getArguments()).getUser();

        setupButtonListeners();

        setupLanguagesChip();

        setupTagsChip();
    }



    /**
     * Sets up the click listeners for the buttons
     */
    private void setupButtonListeners() {

        app.buttonFinish.setOnClickListener(v -> {
            mNewUser.setLanguages(Arrays.asList(app.chipInputLanguages.getText().toString().split(",")));
            mNewUser.setInterests(Arrays.asList(app.chipInputTags.getText().toString().split(",")));
            navigateToMain(mNewUser.getRole());
        });

        app.buttonSkip.setOnClickListener(v -> navigateToMain(mNewUser.getRole()));

        app.buttonBack.setOnClickListener(v -> navigateBackward());
    }

    /**
     * Navigates to the corresponding home activity depending on the user's role
     */
    private void navigateToMain(String role) {
        Intent i = null;

        //Determine the home activity to navigate to
        if(role.equals("developer")){
            i = new Intent(mContext, DeveloperHomeActivity.class);
        } else if (role.equals("manager")) {
            i = new Intent(mContext, ManagerHomeActivity.class);
        }

        //Navigate to the selected home activity
        startActivity(i);
        requireActivity().finish();
    }

    /**
     * Goes to the account details  fragment
     */
    private void navigateBackward() {
        SignupTagsFragmentDirections.TagsToDetailsAction tagsToDetailsAction = SignupTagsFragmentDirections.tagsToDetailsAction(mNewUser);
        NavHostFragment.findNavController(this)
                .navigate(tagsToDetailsAction);
    }

    private int spannedLengthLanguages = 0;

    private int spannedLengthTags = 0;

    private void setupLanguagesChip(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line, Tags.getLanguages());

        app.chipInputLanguages.setAdapter(adapter);
        app.chipInputLanguages.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        app.chipInputLanguages.setOnItemClickListener((parent, arg1, pos, id) -> tokenize(app.chipInputLanguages));

        app.chipInputLanguages.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_COMMA) {
                tokenize(app.chipInputLanguages);
            }
            return true;
        });
    }

    private void setupTagsChip() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line, Tags.getLanguages());

        app.chipInputTags.setAdapter(adapter);
        app.chipInputTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        app.chipInputTags.setOnItemClickListener((parent, arg1, pos, id) -> tokenize(app.chipInputTags));

        app.chipInputTags.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_COMMA) {
                tokenize(app.chipInputTags);
            }
            return true;
        });
    }

    private void tokenize(AppCompatMultiAutoCompleteTextView chipInput) {
        int spannedLength = chipInput == app.chipInputLanguages ? spannedLengthLanguages : spannedLengthTags;

        Editable editable = chipInput.getEditableText();
        ChipDrawable chip = ChipDrawable.createFromResource(mContext, R.xml.chip);
        chip.setText(editable.subSequence(spannedLength,editable.length()).toString().replace(",",""));
        chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(chip);
        editable.setSpan(span, spannedLength, editable.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(chipInput == app.chipInputLanguages){
            spannedLengthLanguages = editable.length();
        } else {
            spannedLengthTags = editable.length();
        }
    }

    /**
     * Resets the ViewBinder
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app = null;
    }
}