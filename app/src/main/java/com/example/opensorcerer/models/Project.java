package com.example.opensorcerer.models;

import android.os.Parcelable;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.kohsuke.github.GHRepository;

import java.util.List;

/**
 * Class for handling Project objects from the Parse database
 */
@SuppressWarnings("unused")
@ParseClassName("Project")
public class Project extends ParseObject implements Parcelable {

    //Database keys
    private static final String KEY_BANNER_IMAGE = "bannerImage";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CLICK_COUNT = "clickCount";
    private static final String KEY_GITHUB_NAME = "githubName";
    private static final String KEY_REPOSITORY = "repository";
    private static final String KEY_LOGO_IMAGE = "logoImage";
    private static final String KEY_LIKE_COUNT = "likeCount";
    private static final String KEY_VIEW_COUNT = "viewCount";
    private static final String KEY_LANGUAGES = "languages";
    private static final String KEY_LOGO_URL = "logoUrl";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_MANAGER = "manager";
    private static final String KEY_README = "readme";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TAGS = "tags";

    /**
     * If this project has been liked by the user
     */
    private String likedByUser = null;

    /**
     * GitHub repository object linked to this project
     */
    private GHRepository mRepoObject;

    /**
     * Description getter
     **/
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    /**
     * Description setter
     **/
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    /**
     * README link getter
     **/
    public String getReadme() {
        return getString(KEY_README);
    }

    /**
     * README link setter
     **/
    public void setReadme(String readme) {
        put(KEY_README, readme);
    }

    /**
     * Logo image getter
     */
    public ParseFile getLogoImage() {
        return getParseFile(KEY_LOGO_IMAGE);
    }

    /**
     * Logo image setter
     */
    public void setLogoImage(ParseFile logoImage) {
        put(KEY_LOGO_IMAGE, logoImage);
    }

    /**
     * Logo image url getter
     */
    public String getLogoImageUrl(){
        return getString(KEY_LOGO_URL);
    }

    /**
     * Logo image url setter
     */
    public void setLogoImageUrl(String url){
        put(KEY_LOGO_URL,url);
    }

    /**
     * Title getter
     */
    public String getTitle() {
        return getString(KEY_TITLE);
    }

    /**
     * Title setter
     */
    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    /**
     * Manager getter
     */
    public User getManager() {
        return User.fromParseUser(getParseUser(KEY_MANAGER));
    }

    /**
     * Manager setter
     */
    public void setManager(User manager) {
        put(KEY_MANAGER, manager.getHandler());
    }

    /**
     * Repository link getter
     */
    public String getRepository() {
        return getString(KEY_REPOSITORY);
    }

    /**
     * Repository link setter
     */
    public void setRepository(String repository) {
        put(KEY_REPOSITORY, repository);
    }

    /**
     * Repository object getter
     */
    public GHRepository getRepoObject() {
        return mRepoObject;
    }

    /**
     * Repository object setter
     */
    public void setRepoObject(GHRepository repoObject) {
        mRepoObject = repoObject;
    }

    /**
     * Click count getter
     */
    public long getClickCount() {
        return getLong(KEY_CLICK_COUNT);
    }

    /**
     * Like count getter
     */
    public long getLikeCount() {
        return getLong(KEY_LIKE_COUNT);
    }

    /**
     * Like count setter
     */
    private void setLikeCount(long likes) {
        put(KEY_LIKE_COUNT, likes);
        update();
    }

    /**
     * View count getter
     */
    public long getViewCount() {
        return getLong(KEY_VIEW_COUNT);
    }

    /**
     * Languages list getter
     */
    public List<String> getLanguages() {
        return getList(KEY_LANGUAGES);
    }

    /**
     * Languages list setter
     */
    public void setLanguages(List<String> languages) {
        put(KEY_LANGUAGES, languages);
    }

    /**
     * Tag list getter
     */
    public List<String> getTags() {
        return getList(KEY_TAGS);
    }

    /**
     * Tag list setter
     */
    public void setTags(List<String> tags) {
        put(KEY_TAGS, tags);
    }

    /**
     * Website getter
     */
    public String getWebsite() {
        return getString(KEY_WEBSITE);
    }

    /**
     * Website setter
     */
    public void setWebsite(String website) {
        put(KEY_WEBSITE, website);
    }

    public String getGithubName(){
        return getString(KEY_GITHUB_NAME);
    }

    public void setGithubName(String name){
        put(KEY_GITHUB_NAME,name);
    }
    /**
     * Determines if the user has liked this project
     */
    public boolean isLikedByUser(User user) {

        //If it has not been determined before
        if (likedByUser == null) {

            //See if the user's liked projects contains this
            List<String> favorites = user.getFavorites();
            if (favorites != null) {
                likedByUser = String.valueOf(favorites.contains(getObjectId()));
            } else {
                likedByUser = "false";
            }

        }
        return Boolean.parseBoolean(likedByUser);
    }

    /**
     * Adds a like to the like count
     */
    public void addLike() {
        setLikeCount(getLikeCount() + 1);
        likedByUser = "true";
    }

    /**
     * Removes a like from the like count
     */
    public void removeLike() {
        setLikeCount(getLikeCount() - 1);
        likedByUser = "false";
    }

    /**
     * Updates the project's information in the database
     */
    public void update() {
        saveInBackground(e -> {
            if (e == null) {
                Log.d("User", "Project updated successfully.");
            } else {
                Log.d("User", "Error updating project");
                e.printStackTrace();
            }
        });
    }
}
