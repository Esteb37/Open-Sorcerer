package com.example.opensorcerer.ui.signup.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
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

import com.example.opensorcerer.databinding.FragmentSignupTagsBinding;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

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
    private FragmentSignupTagsBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Newly created user for signup
     */
    private User mNewUser;

    /**
     * User's selected profile picture
     */
    private Bitmap mProfilePicture;
    private int spannedLengthLanguages = 0;
    private int spannedLengthTags = 0;

    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = FragmentSignupTagsBinding.inflate(inflater, container, false);
        return mApp.getRoot();

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

        setupChipInput(mApp.chipInputLanguages, Arrays.asList(Tools.getLanguages()));

        setupChipInput(mApp.chipInputTags, Arrays.asList(Tools.getLanguages()));
    }

    /**
     * Sets up the click listeners for the buttons
     */
    private void setupButtonListeners() {

        mApp.buttonFinish.setOnClickListener(v -> {
            mNewUser.setLanguages(Arrays.asList(mApp.chipInputLanguages.getText().toString().split(",")));
            mNewUser.setInterests(Arrays.asList(mApp.chipInputTags.getText().toString().split(",")));
            navigateToMain();
        });

        mApp.buttonSkip.setOnClickListener(v -> navigateToMain());

        mApp.buttonBack.setOnClickListener(v -> navigateBackward());
    }

    /**
     * Navigates to the corresponding home activity depending on the user's role
     */
    private void navigateToMain() {
        Intent i = new Intent(mContext, MainActivity.class);

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

    /**
     * Sets up the text input to behave like a chip group
     */
    private void setupChipInput(AppCompatMultiAutoCompleteTextView chipInput, List<String> recommendationItems) {

        //Set the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line, recommendationItems);
        chipInput.setAdapter(adapter);

        //Set the tokenizer to separate items by commas
        chipInput.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        //Create a new token when a recommended item is selected
        chipInput.setOnItemClickListener((parent, arg1, pos, id) -> tokenize(chipInput));

        //Create a new token when a comma is typed
        chipInput.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_COMMA) {
                tokenize(chipInput);
            }
            return true;
        });
    }

    /**
     * Creates a new chip from the last imputed word and adds it to the group
     */
    private void tokenize(AppCompatMultiAutoCompleteTextView chipInput) {

        //Get the spanned length depending on which chip input is being tokenized
        int spannedLength = chipInput == mApp.chipInputLanguages ? spannedLengthLanguages : spannedLengthTags;

        //Add a new chip to the input
        Editable editable = Tools.addChip(mContext, chipInput.getEditableText(), spannedLength);

        //Update the current length of the selected input
        if (chipInput == mApp.chipInputLanguages) {
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
        mApp = null;
    }
}