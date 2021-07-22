package com.example.opensorcerer.ui.main.create;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentCreateThirdBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.home.HomeFragment;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Second fragment for creating a new project and adding it to the database
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class CreateProjectThirdFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "CreateProjectSecondFragment";

    /**Binder object for ViewBinding*/
    private FragmentCreateThirdBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**The created project's repo object*/
    private GHRepository mRepo;

    /**The newly created project*/
    private Project mNewProject;

    public CreateProjectThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = FragmentCreateThirdBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        loadRepoDetails();

        setupChipInput(app.chipInputLanguages, Arrays.asList(Tools.getLanguages()));

        setupChipInput(app.chipInputTags, Arrays.asList(Tools.getLanguages()));

        setupButtonListeners();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();

        mNewProject = Parcels.unwrap(requireArguments().getParcelable("project"));
    }

    /**
     * Populates the inputs with the imported repo's details
     */
    private void loadRepoDetails() {

        //Get the repo object
        mRepo = mNewProject.getRepoObject();


        new Thread(() -> {
            try {
                List<String> languages = new ArrayList<>(mRepo.listLanguages().keySet());
                requireActivity().runOnUiThread(() -> loadChips(app.chipInputLanguages, languages));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                List<String> tags = mRepo.listTopics();
                requireActivity().runOnUiThread(() -> loadChips(app.chipInputTags, tags));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Sets up the text input to behave like a chip group
     */
    private void setupChipInput(AppCompatMultiAutoCompleteTextView chipInput, List<String> recommendationItems){

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
     * Sets up the navigation button listeners
     */
    public void setupButtonListeners(){
       app.buttonBack.setOnClickListener(v -> navigateBackward());

       app.buttonFinish.setOnClickListener(v -> saveProject());
    }

    /**
     * Saves the project in the database
     */
    private void saveProject() {
        //Set the tags and languages as lists
        List<String> languages = Arrays.asList(app.chipInputLanguages.getText().toString().split(","));
        mNewProject.setLanguages(languages);
        List<String> tags = Arrays.asList(app.chipInputTags.getText().toString().split(","));
        mNewProject.setTags(tags);

        //Save the project
        mNewProject.saveInBackground(e -> {
            if(e==null){
                //Go back to the projects fragment
                navigateToHome();
            } else {
                Toast.makeText(mContext, "There was an error creating your project.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private int spannedLengthLanguages = 0;
    private int spannedLengthTags = 0;

    /**
     * Creates a new chip from the last imputed word and adds it to the group
     */
    private void tokenize(AppCompatMultiAutoCompleteTextView chipInput) {

        //Get the spanned length depending on which chip input is being tokenized
        int spannedLength = chipInput == app.chipInputLanguages ? spannedLengthLanguages : spannedLengthTags;

        //Add a new chip to the input
        Editable editable = Tools.addChip(mContext,chipInput.getEditableText(),spannedLength);

        Log.d("Test", String.valueOf(spannedLength));

        //Update the current length of the selected input
        if(chipInput == app.chipInputLanguages){
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
        if(list!=null){
            for(String item:list){
                listText+=item+",";
                chipInput.setText(listText);
                Tools.addChip(mContext,item,chipInput);
            }
        }
    }

    /**
     * Navigates to the third Create Project Fragment
     */
    private void navigateToHome() {
        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Fragment fragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    /**
     * Navigates to the first Create Project Fragment
     */
    private void navigateBackward() {
        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Fragment fragment = new CreateProjectSecondFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("project",Parcels.wrap(mNewProject));
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
}