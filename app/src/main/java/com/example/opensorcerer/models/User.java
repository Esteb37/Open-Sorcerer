package com.example.opensorcerer.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Custom class for handling ParseUser objects without Parse subclass restrictions
 */
public class User implements Parcelable {

    /**
     * Class needed for the Parcelable implementation
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //Database keys
    private static final String KEY_PROFILE_PICTURE = "profilePicture";
    private static final String KEY_PREDICT_SCORES = "predictScores";
    private static final String KEY_LEARN_SCORES = "learnScores";
    private static final String KEY_LOAD_BEFORE = "loadBefore";
    private static final String KEY_EXPERIENCE = "experience";
    private static final String KEY_GITHUB_TOKEN = "ghToken";
    private static final String KEY_LOAD_AFTER = "loadAfter";
    private static final String KEY_INTERESTS = "interests";
    private static final String KEY_LANGUAGES = "languages";
    private static final String KEY_FAVORITES = "favorites";
    private static final String KEY_PROJECTS = "projects";
    private static final String KEY_GITHUB = "github";
    private static final String KEY_ROLE = "role";
    private static final String KEY_NAME = "name";
    private static final String KEY_BIO = "bio";

    private static final int CONVERSATION_SCORE = 3;
    private static final int FAVORITE_SCORE = 2;
    private static final int IGNORE_SCORE = -1;
    private static final int SWIPE_SCORE = 1;

    /**
     * The ParseUser object that allows communication with the database
     */
    private ParseUser mHandler;

    /**
     * Constructor that sets the handler as a ParseUser for database communication
     */
    public User() {
        mHandler = new ParseUser();
    }

    protected User(Parcel in) {
        mHandler = in.readParcelable(ParseUser.class.getClassLoader());
    }

    /**
     * Creates a custom User Object from a ParseUser object
     *
     * @param parseUser The ParseUser object
     * @return a casted custom user object
     */
    public static User fromParseUser(ParseUser parseUser) {
        User user = new User();
        user.setHandler(parseUser);
        return user;
    }

    /**
     * Gets the current logged in user as a custom User object
     */
    public static User getCurrentUser() {
        return fromParseUser(ParseUser.getCurrentUser());
    }

    /**
     * Transforms a list of ParseUsers into a list of custom user objects
     */
    public static List<User> toUserArray(List<ParseUser> parseUsers) {
        List<User> users = new ArrayList<>();
        for (ParseUser parseUser : parseUsers) {
            User user = new User();
            user.setHandler(parseUser);
            users.add(user);
        }
        return users;
    }

    /**
     * Transforms a list of user objects into a list of ParseUser objects
     */
    public static Object toParseUserArray(List<User> users) {
        List<ParseUser> parseUsers = new ArrayList<>();
        for (User user : users) {
            parseUsers.add(user.getHandler());
        }
        return parseUsers;
    }

    /**
     * Handler getter
     */
    public ParseUser getHandler() {
        return mHandler;
    }

    /**
     * Handler setter
     */
    public void setHandler(ParseUser user) {
        mHandler = user;
    }

    /**
     * User ID getter
     */
    public String getObjectId() {
        return mHandler.getObjectId();
    }

    public void setObjectId(String id) {
        mHandler.setObjectId(id);
    }

    /**
     * Username getter
     */
    public String getUsername() {
        return mHandler.getUsername();
    }

    /**
     * Username setter
     */
    public void setUsername(String username) {
        mHandler.setUsername(username);
    }

    /**
     * Password setter
     */
    public void setPassword(String password) {
        mHandler.setPassword(password);
    }

    /**
     * Profile picture getter
     */
    public ParseFile getProfilePicture() {
        return mHandler.getParseFile(KEY_PROFILE_PICTURE);
    }

    /**
     * Profile picture setter
     */
    public void setProfilePicture(ParseFile profilePicture) {
        mHandler.put(KEY_PROFILE_PICTURE, profilePicture);
        update();
    }

    /**
     * Experience setter
     */
    public void setExperience(String experience) {
        mHandler.put(KEY_EXPERIENCE, experience);
        update();
    }

    /**
     * GitHub account link getter
     */
    public String getGithub() {
        return mHandler.getString(KEY_GITHUB);
    }

    /**
     * GitHub account link setter
     */
    public void setGithub(String github) {
        mHandler.put(KEY_GITHUB, github);
    }

    /**
     * GitHub token getter
     */
    public String getGithubToken() {
        return mHandler.getString(KEY_GITHUB_TOKEN);
    }

    /**
     * GitHub token setter
     */
    public void setGithubToken(String token) {
        mHandler.put(KEY_GITHUB_TOKEN, token);
    }

    /**
     * Role getter
     */
    public String getRole() {
        return mHandler.getString(KEY_ROLE);
    }

    /**
     * Role setter
     */
    public void setRole(String role) {
        mHandler.put(KEY_ROLE, role);
    }

    /**
     * Name getter
     */
    public String getName() {
        return mHandler.getString(KEY_NAME);
    }

    /**
     * Name setter
     */
    public void setName(String name) {
        mHandler.put(KEY_NAME, name);
        update();
    }

    /**
     * Bio getter
     */
    public String getBio() {
        return mHandler.getString(KEY_BIO);
    }

    /**
     * Bio setter
     */
    public void setBio(String bio) {
        mHandler.put(KEY_BIO, bio);
        update();
    }

    /**
     * Interests getter
     */
    public List<String> getInterests() {
        return mHandler.getList(KEY_INTERESTS);
    }

    /**
     * Interests setter
     */
    public void setInterests(List<String> interests) {
        mHandler.put(KEY_INTERESTS, interests);
        update();
    }

    /**
     * Languages getter
     */
    public List<String> getLanguages() {
        return mHandler.getList(KEY_LANGUAGES);
    }

    /**
     * Languages setter
     */
    public void setLanguages(List<String> languages) {
        mHandler.put(KEY_LANGUAGES, languages);
        update();
    }

    /**
     * Favorites list getter
     */
    public List<String> getFavorites() {
        return mHandler.getList(KEY_FAVORITES);
    }

    /**
     * Favorites list setter
     */
    public void setFavorites(List<String> favorites) {
        mHandler.put(KEY_FAVORITES, favorites);
        update();
    }

    /**
     * Projects list getter
     */
    public ParseRelation<Project> getProjects() {
        return mHandler.getRelation(KEY_PROJECTS);
    }

    /**
     * Projects list setter
     */
    public void setProjects(ParseRelation<Project> projects) {
        mHandler.put(KEY_PROJECTS, projects);
        update();
    }

    /**
     * Learn scores getter
     */
    public JSONObject getLearnScores() {
        return mHandler.getJSONObject(KEY_LEARN_SCORES);
    }

    /**
     * Learn scores setter
     */
    public void setLearnScores(JSONObject scores) {
        mHandler.put(KEY_LEARN_SCORES, scores);
        update();
    }

    /**
     * Learn scores getter
     */
    public JSONObject getPredictScores() {
        return mHandler.getJSONObject(KEY_PREDICT_SCORES);
    }

    /**
     * Learn scores setter
     */
    public void setPredictScores(JSONObject scores) {
        if (scores != null) {
            mHandler.put(KEY_PREDICT_SCORES, scores);
        }
        update();
    }

    /**
     * Load before getter
     */
    public Date getLoadBefore() {
        return mHandler.getDate(KEY_LOAD_BEFORE);
    }

    /**
     * Load before setter
     */
    public void setLoadBefore(Date date) {
        mHandler.put(KEY_LOAD_BEFORE, date);
    }

    /**
     * Load after getter
     */
    public Date getLoadAfter() {
        return mHandler.getDate(KEY_LOAD_AFTER);
    }

    /**
     * Load after setter
     */
    public void setLoadAfter(Date date) {
        mHandler.put(KEY_LOAD_AFTER, date);
    }

    /**
     * Likes or unlikes the selected project
     *
     * @param project The project to like or unlike
     */
    public void toggleLike(Project project) {
        if (project.isLikedByUser()) {
            project.removeLike();
            removeFavorite(project);
        } else {
            project.addLike();
            addFavorite(project);
        }
    }

    /**
     * Adds the project to the user's list of liked projects
     *
     * @param project The project to like
     */
    private void addFavorite(Project project) {
        List<String> favorites = getFavorites();
        if (favorites == null) favorites = new ArrayList<>();
        favorites.add(project.getObjectId());
        setFavorites(favorites);
        addScores(project, FAVORITE_SCORE);
    }

    /**
     * Removes the project from the user's list of liked projects
     *
     * @param project The project to unlike
     */
    private void removeFavorite(Project project) {
        List<String> favorites = getFavorites();
        if (favorites == null) favorites = new ArrayList<>();
        favorites.remove(project.getObjectId());
        setFavorites(favorites);
        addScores(project, -FAVORITE_SCORE);
    }

    /**
     * Registers the user's swiping behavior on a project
     */
    public void registerSwipedProject(Project project) {
        if (!project.isSwipedByUser()) {
            addScores(project, SWIPE_SCORE);
        }

    }

    /**
     * Registers that the user has scrolled past a project and determines if they ignored it
     */
    public void registerScrolledProject(Project project) {
        if (!(project.isLikedByUser() || project.isSwipedByUser()) && !project.isIgnoredByUser()) {
            registerIgnoredProject(project);
        }
    }

    /**
     * Registers that the user ignored a project without interacting
     */
    public void registerIgnoredProject(Project project) {
        addScores(project, IGNORE_SCORE);
        project.ignoredByUser(true);
    }

    /**
     * Registers that the user has created a conversation about a project
     */
    public void registerCreatedConversation(Project project) {
        project.startConversation();
        addScores(project, CONVERSATION_SCORE);
    }

    /**
     * Adds the specified score to all the project's categories for this user
     */
    public void addScores(Project project, int score) {
        Set<String> tags = new HashSet<>();

        if (project.getTags() != null)
            tags.addAll(project.getTags());

        JSONObject scores = getLearnScores();

        if (scores == null) {
            scores = new JSONObject();
        }

        for (String tag : tags) {
            if (!tag.isEmpty()) {
                try {
                    scores.put(tag, scores.getInt(tag) + score);
                } catch (JSONException e) {
                    try {
                        scores.put(tag, score);
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            }
        }
        setLearnScores(scores);
    }

    /**
     * Calculates the overall score for the project given by the user's previous response to its categories
     */
    public int calculateScore(Project project) {
        Set<String> scoreKeys = new HashSet<>();

        if (project.getTags() != null)
            scoreKeys.addAll(project.getTags());

        JSONObject userScores = getPredictScores();

        if (scoreKeys.size() > 0 && userScores != null) {
            int totalScore = 0;
            for (String key : scoreKeys) {
                try {
                    totalScore += Integer.compare(userScores.getInt(key), 0);
                } catch (JSONException ignored) {

                }
            }
            return totalScore;
        } else {
            return 0;
        }
    }

    /**
     * Determines if the project includes at least one language that the user knows
     */
    public boolean includesLanguages(Project project) {
        for (String language : getLanguages()) {
            if (project.getLanguages().contains(language)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the user will likely not ignore a project
     */
    public boolean probablyLikes(Project project) {
        return includesLanguages(project) && calculateScore(project) >= 0;
    }


    /**
     * Fetches the user handler in the background
     *
     * @return the user handler after the request has been completed
     * @throws ParseException in case the user is not found
     */
    public User fetchIfNeeded() throws ParseException {
        return fromParseUser(mHandler.fetchIfNeeded());
    }

    /**
     * Updates the user's information in the database
     */
    public void update() {
        mHandler.saveInBackground(e -> {
            if (e == null) {
                Log.d("User", "User updated successfully.");
            } else {
                Log.d("User", "Error updating user");
                e.printStackTrace();
            }
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mHandler, flags);
    }

}
