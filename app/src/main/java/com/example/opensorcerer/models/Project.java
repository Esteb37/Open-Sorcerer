package com.example.opensorcerer.models;

import android.os.Parcelable;

import com.example.opensorcerer.models.users.roles.Manager;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

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
    private static final String KEY_REPOSITORY = "repository";
    private static final String KEY_LOGO_IMAGE = "logoImage";
    private static final String KEY_LIKE_COUNT = "likeCount";
    private static final String KEY_VIEW_COUNT = "viewCount";
    private static final String KEY_LANGUAGES = "languages";
    private static final String KEY_MANAGER = "manager";
    private static final String KEY_README = "readme";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TAGS = "tags";

    /**Description getter**/
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    /**Description setter**/
    public void setDescription(String description) {
        put(KEY_DESCRIPTION,description);
    }

    /**README link getter**/
    public String getReadme() {
        return getString(KEY_README);
    }

    /**README link setter**/
    public void setReadme(String readme) {
        put(KEY_README,readme);
    }

    /**Logo image getter*/
    public ParseFile getLogoImage() {
        return getParseFile(KEY_LOGO_IMAGE);
    }

    /**Logo image setter*/
    public void setLogoImage(ParseFile logoImage) {
        put(KEY_LOGO_IMAGE,logoImage);
    }

    /**Title getter*/
    public String getTitle() {
        return getString(KEY_TITLE);
    }

    /**Title setter*/
    public void setTitle(String title) {
        put(KEY_TITLE,title);
    }

    /**Manager getter*/
    public Manager getManager() {
        return Manager.fromParseUser(getParseUser(KEY_MANAGER));
    }

    /**Manager setter*/
    public void setManager(Manager manager) {
        put(KEY_MANAGER,manager.getHandler());
    }

    /**Repository link getter*/
    public String getRepository() {
        return getString(KEY_REPOSITORY);
    }

    /**Repository link setter*/
    public void setRepository(String repository) {
        put(KEY_REPOSITORY,repository);
    }

    /**Click count getter*/
    public long getClickCount() {
        return getLong(KEY_CLICK_COUNT);
    }

    /**Like count getter*/
    public long getLikeCount() {
        return getLong(KEY_LIKE_COUNT);
    }

    /**View count getter*/
    public long getViewCount() {
        return getLong(KEY_VIEW_COUNT);
    }

    /**Languages list getter*/
    public List<String> getLanguages(){
        return getList(KEY_LANGUAGES);
    }

    /**Languages list setter*/
    public void setLanguages(List<String> languages) {
        put(KEY_LANGUAGES,languages);
    }

    /**Tag list getter*/
    public List<String> getTags(){
        return getList(KEY_TAGS);
    }

    /**Tag list setter*/
    public void setTags(List<String> tags) {
        put(KEY_TAGS,tags);
    }
}
