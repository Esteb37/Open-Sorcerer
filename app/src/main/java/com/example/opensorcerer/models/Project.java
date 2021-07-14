package com.example.opensorcerer.models;

import android.os.Parcelable;

import com.example.opensorcerer.models.users.roles.Manager;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Class for handling Project objects from the Parse database
 */
@SuppressWarnings("unused")
@ParseClassName("Project")
public class Project extends ParseObject implements Parcelable {

    //Database key
    private static final String KEY_BANNER_IMAGE = "bannerImage";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CLICK_COUNT = "clickCount";
    private static final String KEY_REPOSITORY = "repository";
    private static final String KEY_LOGO_IMAGE = "logoImage";
    private static final String KEY_LIKE_COUNT = "likeCount";
    private static final String KEY_VIEW_COUNT = "viewCount";
    private static final String KEY_MANAGER = "manager";
    private static final String KEY_README = "readme";
    private static final String KEY_TITLE = "title";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION,description);
    }

    public String getReadme() {
        return getString(KEY_README);
    }

    public void setReadme(String readme) {
        put(KEY_README,readme);
    }

    public ParseFile getBannerImage() {
        return getParseFile(KEY_BANNER_IMAGE);
    }

    public void setBannerImage(ParseFile bannerImage) {
        put(KEY_BANNER_IMAGE,bannerImage);
    }

    public ParseFile getLogoImage() {
        return getParseFile(KEY_LOGO_IMAGE);
    }

    public void setLogoImage(ParseFile logoImage) {
        put(KEY_LOGO_IMAGE,logoImage);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE,title);
    }

    public Manager getManager() {
        return Manager.fromParseUser(getParseUser(KEY_MANAGER));
    }

    public void setManager(Manager manager) {
        put(KEY_MANAGER,manager.getHandler());
    }

    public void setRepository(String repository) {
        put(KEY_REPOSITORY,repository);
    }

    public String getRepository() {
        return getString(KEY_REPOSITORY);
    }

    public long getClickCount() {
        return getLong(KEY_CLICK_COUNT);
    }

    public long getLikeCount() {
        return getLong(KEY_LIKE_COUNT);
    }

    public long getViewCount() {
        return getLong(KEY_VIEW_COUNT);
    }



}
