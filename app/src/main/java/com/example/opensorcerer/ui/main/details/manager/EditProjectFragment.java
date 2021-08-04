package com.example.opensorcerer.ui.main.details.manager;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.databinding.FragmentEditProjectBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EditProjectFragment extends Fragment {

    /**
     * Project being displayed
     */
    private final Project mProject;

    /**
     * Binder object for ViewBinding
     */
    private FragmentEditProjectBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Spanned length of the languages text edit
     */
    private int spannedLengthLanguages = 0;

    /**
     * Spanned length of the tags text edit
     */
    private int spannedLengthTags = 0;

    public EditProjectFragment(Project project) {
        mProject = project;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentEditProjectBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        loadProjectDetails();

        setupChipInput(mApp.chipInputLanguages, Arrays.asList(Tools.getLanguages()));

        setupChipInput(mApp.chipInputTags, Arrays.asList(Tools.getLanguages()));
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        ((AppCompatActivity) requireActivity()).setSupportActionBar(mApp.toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Edit project");

    }

    private void loadProjectDetails() {

        mApp.editTextTitle.setText(mProject.getTitle());
        mApp.editTextDescription.setText(mProject.getDescription());
        mApp.editTextHomepage.setText(mProject.getWebsite());
        mApp.editTextRepo.setText(mProject.getRepository());

        loadChips(mApp.chipInputLanguages, mProject.getLanguages());
        loadChips(mApp.chipInputTags, mProject.getTags());

        // Load the project's logo from URL if any or the image file if no URL is provided
        String imageURL = mProject.getLogoImageUrl();
        ParseFile imageFile = mProject.getLogoImage();
        if (imageURL != null) {
            Tools.loadImageFromURL(mContext, imageURL, mApp.imageViewLogo);
        } else if (imageFile != null) {
            Tools.loadImageFromFile(mContext, imageFile, mApp.imageViewLogo);
        }
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
     * Loads all items from a list as a chip
     */
    public void loadChips(AppCompatMultiAutoCompleteTextView chipInput, List<String> list) {
        String listText = "";
        if (list != null) {
            for (String item : list) {
                listText += item + ",";
                chipInput.setText(listText);
                Tools.addChip(mContext, item, chipInput);
            }
        }
    }

}