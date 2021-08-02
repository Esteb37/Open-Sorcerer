package com.example.opensorcerer.ui.main.details;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentDetailsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.ui.main.conversations.ConversationFragment;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 * Fragment for displaying a project's details
 */
public class DetailsFragment extends Fragment {

    /**
     * Project being displayed
     */
    private Project mProject;

    /**
     * Binder object for ViewBinding
     */
    private FragmentDetailsBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * GitHub API client
     */
    private GitHub mGitHub;

    /**
     * Current project's repository object
     */
    private GHRepository mRepo;

    /**
     * Amount of forks this project has
     */
    private int mForkCount;

    public DetailsFragment(Project project) {
        mProject = project;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the fragment and sets up ViewBinding
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentDetailsBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupBottomNavigation();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();
    }

    /**
     * Sets up the bottom navigation bar
     */
    private void setupBottomNavigation() {


        //Ensure that the id's of the navigation items are final for the switch
        final int actionDetails = R.id.actionDetails;
        final int actionHomepage = R.id.actionHomepage;
        final int actionMessage = R.id.actionMessage;
        final int actionFork = R.id.actionFork;
        final int actionShare = R.id.actionShare;

        mApp.bottomNavDetails.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            // Navigate to a different fragment depending on the item selected
            switch (item.getItemId()) {
                case actionDetails:
                    fragment = new InformationFragment(mProject, mForkCount);
                    break;

                case actionHomepage:
                    fragment = new HomepageFragment(mProject);
                    break;

                case actionMessage:
                    fragment = new ConversationFragment(mProject);
                    break;

                case actionFork:
                    showForkPopup();
                    break;

                case actionShare:
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

            // Open the selected fragment
            if (item.getItemId() != actionFork && item.getItemId() != actionShare) {
                Tools.loadFragment(mContext, fragment, mApp.flContainerDetailsInternal.getId());
            }
            return true;
        });

        mApp.bottomNavDetails.setSelectedItemId(R.id.actionDetails);
    }

    /**
     * Determines if the "Fork" or "Unfork" popup should be displayed and displays it
     */
    private void showForkPopup() {
        new Thread(() -> {
            if (userHasForkedProject()) {
                requireActivity().runOnUiThread(() ->
                        setupPopupWindow(R.layout.popup_unfork, true));
            } else {
                requireActivity().runOnUiThread(() ->
                        setupPopupWindow(R.layout.popup_fork, false));
            }
        }).start();
    }

    /**
     * Sets up the fork project popup window with the selected layout
     */
    private void setupPopupWindow(int layoutId, boolean hasForked) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(layoutId, null);

        // create the popup window
        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setElevation(10);
        popupWindow.setAnimationStyle(R.style.popup_animation);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window
        popupWindow.showAtLocation(mApp.getRoot(), Gravity.BOTTOM, 0, 300);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            popupView.performClick();
            popupWindow.dismiss();
            return true;
        });

        if (hasForked) {
            setupUnforkButtonListener(popupView, popupWindow);
        } else {
            setupForkButtonListener(popupView, popupWindow);
        }

        setupCancelButtonListener(popupView, popupWindow);
    }

    /**
     * Removes a forked project from the user's account
     */
    private void setupUnforkButtonListener(View popupView, PopupWindow popupWindow) {
        popupView.findViewById(R.id.buttonUnfork).setOnClickListener(v ->
                new Thread(() -> {
                    try {

                        //Get this project's fork from the user's account
                        GHRepository repo = mGitHub.getMyself().getRepository(mRepo.getName());

                        //Delete the repo
                        repo.delete();
                        mForkCount--;

                        //Close the popup
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(mContext, "Project fork removed successfully from your account.", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            mApp.bottomNavDetails.setSelectedItemId(R.id.actionDetails);
                        });
                    } catch (IOException e) {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(mContext, "There was an error removing this project from your account.", Toast.LENGTH_SHORT).show());
                        e.printStackTrace();
                    }
                }).start()
        );
    }

    /**
     * Sets up the fork button listener
     */
    private void setupForkButtonListener(View popupView, PopupWindow popupWindow) {
        popupView.findViewById(R.id.buttonFork).setOnClickListener(v ->
                new Thread(() -> {
                    try {

                        //Fork this project repo
                        mRepo.fork();
                        mForkCount++;
                        //Close the popup
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(mContext, "Project forked successfully!", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            mApp.bottomNavDetails.setSelectedItemId(R.id.actionDetails);
                        });

                    } catch (IOException e) {
                        Toast.makeText(mContext, "There was an error forking this project.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }).start()
        );
    }

    /**
     * Sets up the fork popup cancel button
     */
    private void setupCancelButtonListener(View popupView, PopupWindow popupWindow) {
        popupView.findViewById(R.id.buttonCancel).setOnClickListener(v -> {
            popupWindow.dismiss();
            mApp.bottomNavDetails.setSelectedItemId(R.id.actionDetails);
        });
    }

    /**
     * Updates which project is currently being displayed
     */
    public void updateProject(Project project) {
        mProject = project;

        new Thread(() -> {
            try {
                mRepo = mGitHub.getRepository(mProject.getRepositoryName());
                mForkCount = mRepo.getForksCount();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Tools.loadFragment(mContext, new InformationFragment(mProject, mForkCount), mApp.flContainerDetailsInternal.getId());
    }

    /**
     * Determines if the current project has already been forked by the user
     */
    private boolean userHasForkedProject() {
        try {
            GHRepository repository = mGitHub.getMyself().getRepository(mRepo.getName());
            return repository != null && repository.isFork();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Determines if the current page in the details fragment is the Information page
     */
    public boolean isInformationFragmentVisible() {
        int index = requireActivity().getSupportFragmentManager().getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = requireActivity().getSupportFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();
        assert tag != null;
        return tag.equals("InformationFragment");
    }

}