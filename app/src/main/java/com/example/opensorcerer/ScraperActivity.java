package com.example.opensorcerer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.parse.ParseQuery;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Activity for scraping Google's Open Source project catalogue and creating projects from it
 */
public class ScraperActivity extends AppCompatActivity {

    private WebView mWebView = null;

    private final String[] mCategories = {"featured","cloud","analytics-visualization","databases","developer-tools","education","enterprise","games","graphics-video-audio",
            "i18n","iot","mobile","machine-learning","geo","networking","programming","samples","security","testing","utilities","web"};

    private String[] mProjectLinks;

    private int mCurrentCategory = 0;

    private int mCurrentProject = 0;

    private String mLastRepo = "";

    private String mLastCategory = "";

    private GitHub mGitHub;

    /**
     * Sets up the activity's methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scraper);

        buildGithub();

        setupWebView();

        loadNextCategory();
    }

    /**
     * Builds the GitHub application
     */
    private void buildGithub() {
        User mUser = User.getCurrentUser();

        mGitHub = ((OSApplication) getApplication()).buildGitHub(mUser.getGithubToken());
    }

    /**
     * Sets up the web viewer
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        mWebView = findViewById(R.id.scraper);

        //Reset cache
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //Set JavaScript interface
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new LoadListener(), "HTMLOUT");
    }


    /**
     * Gets the projects from the next category
     */
    private void loadNextCategory(){
        runOnUiThread(() ->{

            //Reset web view
            mWebView.clearCache(true);
            try{

                //Load the catalogue page
                Log.d("Scraper","-----"+ mCategories[mCurrentCategory]+"-----");
                mWebView.loadUrl("https://opensource.google/projects/list/"+ mCategories[mCurrentCategory++]);

                //Get the projects from this catalogue page
                injectGetProjectLinks();
            }
            catch(IndexOutOfBoundsException e){
                Log.d("Test","End of file");
            }
        });
    }

    /**
     * Retrieves the links from all project cards in the catalogue
     */
    private void injectGetProjectLinks(){
        runOnUiThread(() -> {
            try {
                String getProjectLinksJS = Tools.getFileContents(this, "getProjectLinks.js");
                mWebView.loadUrl("javascript:window.HTMLOUT.getProjectLinks("+getProjectLinksJS+");");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Loads the next project page from the catalogue
     */
    private void loadNextProject(){

        runOnUiThread(() -> {

            //Reset the viewer
            mWebView.clearCache(true);
            try {

                //Load the project
                mWebView.loadUrl(mProjectLinks[mCurrentProject++]);

                //Find the repository link and the logo
                injectGetProjectInformation();

            //If all the projects have been scraped
            } catch (IndexOutOfBoundsException e){

                //Go to the next category
                mCurrentProject = 0;
                loadNextCategory();
            }
        });
    }

    /**
     * Scrapes the site to find the first GitHub repository link and the logo
     */
    private void injectGetProjectInformation() {
            runOnUiThread(() -> {
                try {
                    String getProjectInfoJS = Tools.getFileContents(this, "getProjectInfo.js");
                    mWebView.loadUrl("javascript:window.HTMLOUT.getProjectInformation("+getProjectInfoJS+");");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    /**
     * Creates a new project from the repository and logo image and saves it into the database
     */
    private void createProjectFromRepo(String repoName, String projectImage) {

        new Thread(() -> {
            try {

                GHRepository repo = mGitHub.getRepository(repoName);

                Project project = new Project();

                //Set name and title
                project.setGithubName(repoName);
                assert repo != null;
                project.setTitle(Tools.formatTitle(repo.getName()));

                //Set description
                String description = repo.getDescription();
                project.setDescription(description == null ? "No description provided." : description);

                //Set repository link
                project.setRepository(repo.getHtmlUrl().toString());

                //Set homepage if any, or repository link if none
                String website = repo.getHomepage();
                if (website == null || website.equals("")) {
                    project.setWebsite(repo.getHtmlUrl().toString());
                } else {
                    project.setWebsite(website);
                }

                //Set tags
                try {
                    project.setTags(repo.listTopics());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    project.setLanguages(new ArrayList<>(repo.listLanguages().keySet()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Set logo
                project.setLogoImageUrl(projectImage);

                //Set manager to be Google
                User manager = new User();
                manager.setObjectId("HE3w1yqEa1");
                project.setManager(manager);
                project.update();

                Log.d("Scraper","Created "+project.getTitle());
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }

        }).start();
    }

    /**
     * Listener interface for when the web view has loaded fully
     */
    @SuppressWarnings("unused")
    public class LoadListener{

        /**
         * Interface for getting all links for the projects in the catalogue page
         */
        @JavascriptInterface
        public void getProjectLinks(String result)
        {

            //Recursively scrape until a new link is found correctly
            if(result.equals("TypeError") || result.equals(mLastCategory)){
                injectGetProjectLinks();
            } else {
                mLastCategory = result;

                //Get projects link as list
                mProjectLinks = result.split(",");

                //Load first project
                loadNextProject();
            }
        }

        /**
         * Interface for getting the GitHub repo link from a project's page
         */
        @JavascriptInterface
        public void getProjectInformation(String[] result){

            //Split the result into link and image
            String repoLink = result[0];
            String projectImage = result[1];

            //Recursively scrape until the correct link is found
            if(repoLink.equals("TypeError") || repoLink.equals(mLastRepo)){
                injectGetProjectInformation();
            } else {
                if(repoLink.contains("github.com")){
                    try {

                        //Create a project from the repo link
                        String repoName = Tools.getRepositoryName(repoLink);

                        ParseQuery<Project> query = ParseQuery.getQuery(Project.class).whereEqualTo("githubName",repoName);

                        query.findInBackground((projects, e) -> {
                            if (e == null) {
                                if (projects.size() > 0) {
                                    Log.d("Scraper", "Project already exists.");
                                } else {
                                    createProjectFromRepo(repoName, projectImage);
                                }
                            } else {
                                e.printStackTrace();
                            }
                        });
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }

                //Get the next project
                mLastRepo = repoLink;
                loadNextProject();
            }
        }
    }
}