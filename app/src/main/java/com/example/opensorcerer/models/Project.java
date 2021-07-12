package com.example.opensorcerer.models;

import android.os.Parcelable;

import com.example.opensorcerer.models.users.roles.Manager;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Class for handling Project objects from the Parse database
 */
@ParseClassName("Project")
public class Project extends ParseObject implements Parcelable {

    //Database keys
    private static final String KEY_SHORT_DESCRIPTION = "shortDescription";
    private static final String KEY_LONG_DESCRIPTION = "longDescription";
    private static final String KEY_BANNER_IMAGE = "bannerImage";
    private static final String KEY_CLICK_COUNT = "clickCount";
    private static final String KEY_REPOSITORY = "repository";
    private static final String KEY_LOGO_IMAGE = "logoImage";
    private static final String KEY_LIKE_COUNT = "likeCount";
    private static final String KEY_VIEW_COUNT = "viewCount";
    private static final String KEY_MANAGER = "manager";
    private static final String KEY_TITLE = "title";

    public String getShortDescription() {
        return getString(KEY_SHORT_DESCRIPTION);
    }

    public String getLongDescription() {
        return getString(KEY_LONG_DESCRIPTION);
    }

    public ParseFile getBannerImage() {
        return getParseFile(KEY_BANNER_IMAGE);
    }

    public ParseFile getLogoImage() {
        return getParseFile(KEY_LOGO_IMAGE);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public long getClickCount() {
        return getLong(KEY_CLICK_COUNT);
    }

    public String getRepository() {
        return getString(KEY_REPOSITORY);
    }

    public long getLikeCount() {
        return getLong(KEY_LIKE_COUNT);
    }

    public long getViewCount() {
        return getLong(KEY_VIEW_COUNT);
    }

    public Manager getManager() {
        return Manager.fromParseUser(getParseUser(KEY_MANAGER));
    }
}
