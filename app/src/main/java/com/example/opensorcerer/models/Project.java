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
@ParseClassName("Project")
public class Project extends ParseObject implements Parcelable {

    //Database keys
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SWIPE_COUNT = "swipeCount";
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
    private String mLikedByUser = null;

    /**
     * If this project has been swiped left by user
     */
    private boolean mSwipedByUser = false;

    /**
     * If this project has been ignored by user
     */
    private boolean mIgnoredByUser = false;

    /**
     * If the user has created a conversation about this project with its manager
     */
    private boolean mConversationStarted = false;

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
    public String getLogoImageUrl() {
        return getString(KEY_LOGO_URL);
    }

    /**
     * Logo image url setter
     */
    public void setLogoImageUrl(String url) {
        put(KEY_LOGO_URL, url);
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
     * View count setter
     */
    public void setViewCount(long viewCount) {
        put(KEY_VIEW_COUNT, viewCount);
        update();
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

    /**
     * Github name setter
     */
    public void setGithubName(String name) {
        put(KEY_GITHUB_NAME, name);
    }

    /**
     * Swipe count getter
     */
    public long getSwipeCount() {
        return getLong(KEY_SWIPE_COUNT);
    }

    /**
     * Swipe count setter
     */
    public void setSwipeCount(long swipeCount) {
        put(KEY_SWIPE_COUNT, swipeCount);
        update();
    }

    /**
     * Determines if the user has liked this project
     */
    public boolean isLikedByUser() {

        //If it has not been determined before
        if (mLikedByUser == null) {

            //See if the user's liked projects contains this
            List<String> favorites = User.getCurrentUser().getFavorites();
            if (favorites != null) {
                mLikedByUser = String.valueOf(favorites.contains(getObjectId()));
            } else {
                mLikedByUser = "false";
            }

        }
        return Boolean.parseBoolean(mLikedByUser);
    }

    /**
     * Adds a like to the like count
     */
    public void addLike() {
        setLikeCount(getLikeCount() + 1);
        mLikedByUser = "true";
    }

    /**
     * Removes a like from the like count
     */
    public void removeLike() {
        setLikeCount(getLikeCount() - 1);
        mLikedByUser = "false";
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

    /**
     * Adds one to the project's swipe count
     */
    public void addSwipe() {
        mSwipedByUser = true;
        setSwipeCount(getSwipeCount() + 1);
    }

    /**
     * Adds one to the project's view count
     */
    public void addView() {
        setViewCount(getViewCount() + 1);
    }

    /**
     * If the project has been swiped by the user
     */
    public boolean isSwipedByUser() {
        return mSwipedByUser;
    }

    /**
     * If the project has been ignored by the user
     */
    public boolean isIgnoredByUser() {
        return mIgnoredByUser;
    }

    /**
     * Set if the project has been ignored
     */
    public void ignoredByUser(boolean ignoredByUser) {
        mIgnoredByUser = ignoredByUser;
    }

    /**
     * If the user has started a conversation about this project
     */
    public boolean isConversationStarted() {
        return mConversationStarted;
    }

    /**
     * Indicates that the user has started a conversation about this project
     */
    public void startConversation() {
        mConversationStarted = true;
    }

    public String getRepositoryName() {
        return Tools.getRepositoryName(getRepository());
    }

    public boolean isByUser(User user) {
        return getManager().getObjectId().equals(user.getObjectId());
    }
}
