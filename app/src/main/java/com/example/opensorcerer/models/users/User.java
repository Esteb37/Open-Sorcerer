package com.example.opensorcerer.models.users;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.opensorcerer.models.Conversation;
import com.example.opensorcerer.models.Project;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom class for handling ParseUser objects without Parse subclass restrictions
 */

@SuppressWarnings("unused")
public class User implements Parcelable {

    //Database keys
    private static final String KEY_PROFILE_PICTURE = "profilePicture";
    private static final String KEY_CONVERSATIONS = "conversations";
    private static final String KEY_EXPERIENCE = "experience";
    private static final String KEY_GITHUB_TOKEN = "ghToken";
    private static final String KEY_FAVORITES = "favorites";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GITHUB = "github";
    private static final String KEY_ROLE = "role";
    private static final String KEY_NAME = "name";
    private static final String KEY_BIO = "bio";

    /**
     * The ParseUser object that allows communication with the database
     */
    protected ParseUser mHandler;


    protected User(Parcel in) {
        mHandler = in.readParcelable(ParseUser.class.getClassLoader());
    }

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

    /**
     * Creates a custom User Object from a ParseUser object
     *
     * @param parseUser The ParseUser object
     * @return a casted custom user object
     */
    public static User fromParseUser(ParseUser parseUser){
        User user = new User();
        user.setHandler(parseUser);
        return user;
    }

    /**
     * Constructor that sets the handler as a ParseUser for database communication
     */
    public User(){
        mHandler = new ParseUser();
    }

    /**Handler getter*/
    public ParseUser getHandler() { return mHandler; }

    /**Handler setter*/
    public void setHandler(ParseUser user) {
        mHandler = user;
    }

    /**User ID getter*/
    public String getObjectId(){
        return mHandler.getObjectId();
    }

    /**Username getter*/
    public String getUsername(){
        return mHandler.getUsername();
    }

    /**Username setter*/
    public void setUsername(String username) { mHandler.setUsername(username);}

    /**Email getter*/
    public String getEmail() {
        return mHandler.getEmail();
    }

    /**Email setter*/
    public void setEmail(String email) {
        mHandler.setEmail(email);
    }

    /**Password setter*/
    public void setPassword(String password) {
        mHandler.setPassword(password);
    }

    /**Profile picture getter*/
    public ParseFile getProfilePicture() {
        return mHandler.getParseFile(KEY_PROFILE_PICTURE);
    }

    /**Profile picture setter*/
    public void setProfilePicture(ParseFile profilePicture) {
        mHandler.put(KEY_PROFILE_PICTURE,profilePicture);
    }

    /**Experience getter*/
    public String getExperience() {
        return mHandler.getString(KEY_EXPERIENCE);
    }

    /**Experience setter*/
    public void setExperience(String experience) {
        mHandler.put(KEY_EXPERIENCE,experience);
    }

    /**GitHub account link getter*/
    public String getGithub() {
        return mHandler.getString(KEY_GITHUB);
    }

    /**GitHub account link setter*/
    public void setGithub(String github) {
        mHandler.put(KEY_GITHUB,github);
    }

    /**GitHub token getter*/
    public String getGithubToken() {
        return mHandler.getString(KEY_GITHUB_TOKEN);
    }

    /**GitHub token setter*/
    public void setGithubToken(String token) {
        mHandler.put(KEY_GITHUB_TOKEN,token);
    }

    /**Role getter*/
    public String getRole() {
        return mHandler.getString(KEY_ROLE);
    }

    /**Role setter*/
    public void setRole(String role) {
        mHandler.put(KEY_ROLE,role);
    }

    /**Name getter*/
    public String getName() {
        return mHandler.getString(KEY_NAME);
    }

    /**Name setter*/
    public void setName(String name) {
        mHandler.put(KEY_NAME,name);
    }

    /**Bio getter*/
    public String getBio() {
        return mHandler.getString(KEY_BIO);
    }

    /**Bio setter*/
    public void setBio(String bio) {
        mHandler.put(KEY_BIO,bio);
    }

    /**Conversation list getter*/
    public ParseRelation<Conversation> getConversations(){
        return mHandler.getRelation(KEY_CONVERSATIONS);
    }

    /**Conversation list setter*/
    public void setConversations(ParseRelation<Conversation> conversation) {
        mHandler.put(KEY_CONVERSATIONS,conversation);
    }

    public List<String> getFavorites(){
        return mHandler.getList(KEY_FAVORITES);
    }

    public void setFavorites(List<String> favorites){
        mHandler.put(KEY_FAVORITES,favorites);
        update();
    }

    /**
     * Likes or unlikes the selected project
     * @param project The project to like or unlike
     */
    public void toggleLike(Project project) {
        if(project.isLikedByUser(this)){
            project.removeLike();
            removeFavorite(project);
        } else {
            project.addLike();
            addFavorite(project);
        }
    }

    private void removeFavorite(Project project) {
        List<String> favorites = getFavorites();
        if(favorites == null) favorites = new ArrayList<>();
        favorites.remove(project.getObjectId());
        setFavorites(favorites);
    }

    private void addFavorite(Project project) {
        List<String> favorites = getFavorites();
        if(favorites == null) favorites = new ArrayList<>();
        favorites.add(project.getObjectId());
        setFavorites(favorites);
    }


    /**
     * Fetches the user handler in the background
     * @return the user handler after the request has been completed
     * @throws ParseException in case the user is not found
     */
    public ParseUser fetchIfNeeded() throws ParseException {
        return mHandler.fetchIfNeeded();
    }

    /**
     * Updates the user's information in the database
     */
    public void update(){
        mHandler.saveInBackground(e -> {
            if(e==null){
                Log.d("User","User updated successfully.");
            } else {
                Log.d("User","Error updating user");
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
