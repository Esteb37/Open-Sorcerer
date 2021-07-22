package com.example.opensorcerer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.ItemCardProjectBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;


import java.util.List;

/**
 * ViewHolder class for Projects in linear Card format
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "FieldMayBeFinal"})
public class ProjectCardHolder extends RecyclerView.ViewHolder{

    /**Binder object for ViewBinding*/
    private final ItemCardProjectBinding mApp;

    /**The duration for the like animations*/
    private final int LIKE_ANIMATION_DURATION = 500;

    /**The Holder's context*/
    private final Context mContext;

    /**Current user*/
    private User mUser;

    /**Current Project*/
    private Project mProject;

    /**
     * Sets up the item's methods
     */
    public ProjectCardHolder(View view, Context context, ItemCardProjectBinding binder, ProjectsCardAdapter.OnClickListener clickListener, ProjectsCardAdapter.OnDoubleTapListener doubleTapListener) {
        super(view);

        mApp = binder;

        mContext = context;

        mUser = User.getCurrentUser();

        //Set the listener for clicking on the card
        view.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

        //Set the listener for double tapping on the card
        view.setOnTouchListener(new View.OnTouchListener() {

            //Create a gesture detector
            private final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override

                //Like the project on double tap
                public boolean onDoubleTap(MotionEvent e){
                    doubleTapListener.onItemDoubleTap(getAdapterPosition());

                    //Animate the project like
                    setLikeButton();
                    showLikeAnimation(e.getX(),e.getY());

                    return super.onDoubleTap(e);
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    /**
     * Populates the view's items with the project's information
     * @param project The project to display
     */
    public void bind(Project project) {

        mProject = project;
        
        //Set text details
        mApp.textViewTitle.setText(mProject.getTitle());
        mApp.textViewDescription.setText(mProject.getDescription());
        mApp.textViewTitle.setMaxLines(mProject.getTitle().split(" ").length);
        

        //Show the first three tags
        TextView[] tagViews = {mApp.textViewTag1,mApp.textViewTag2,mApp.textViewTag3};
        List<String> tags = mProject.getTags();
        for(int i = 0;i<3;i++){
            try {
                tagViews[i].setText(tags.get(i).replace("-"," "));
            } catch(IndexOutOfBoundsException e){
                tagViews[i].setText("");
            }
        }

        //Show the first three languages
        TextView[] languageViews = {mApp.textViewLanguage1,mApp.textViewLanguage2,mApp.textViewLanguage3};
        List<String> languages = mProject.getLanguages();
        for(int i = 0;i<3;i++){
            try {
                languageViews[i].setText(languages.get(i).replace("-"," "));
            } catch(IndexOutOfBoundsException e){
                languageViews[i].setText("");
            }
        }

        //Load the project's logo
        ParseFile image = mProject.getLogoImage();
        if(image != null){
            Glide.with(mContext)
                    .load(image.getUrl())
                    .transform(new RoundedCorners(1000))
                    .into(mApp.imageViewLogo);
        }

        User manager;
        try {
            manager = mProject.getManager().fetchIfNeeded();
            assert manager != null;
            mApp.textViewAuthor.setText(manager.getUsername());
            ParseFile profilePicture = manager.getProfilePicture();
            if(image != null) {
                Glide.with(mContext)
                        .load(profilePicture.getUrl())
                        .into(mApp.imageViewProfilePicture);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        
        setLikeButton();
    }

    /**
     * Sets the color of the like button depending on if the user has liked the project
     */
    private void setLikeButton() {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.ufi_heart_active);
        assert unwrappedDrawable != null;
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, mProject.isLikedByUser(mUser) ? Color.RED : ContextCompat.getColor(mContext,R.color.darker_blue));
        mApp.buttonLike.setImageDrawable(wrappedDrawable);
        mApp.buttonLikeAnimator.setImageDrawable(wrappedDrawable);
    }

    /**
     * Shows a growing and fading animation to notify the user that the project was liked
     */
    private void showLikeAnimation(float posX, float posY) {

        //Setup the animated icon
        mApp.buttonLikeAnimator.clearAnimation();
        mApp.buttonLikeAnimator.setVisibility(View.VISIBLE);

        //Set the fade animation
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator()); //and this
        fadeOut.setDuration(LIKE_ANIMATION_DURATION);

        //Set the scale animation
        Animation growOut= new ScaleAnimation(
                1f, 2f, // Start and end values for the X axis scaling
                1f, 2f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        growOut.setFillAfter(true); // Needed to keep the result of the animation
        growOut.setDuration(LIKE_ANIMATION_DURATION);

        //Combine the scale and fade animations
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeOut);
        animation.addAnimation(growOut);

        //Set the animation on the animated icon
        mApp.buttonLikeAnimator.setAnimation(animation);

        //Listen for the icon's animation end
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                mApp.buttonLikeAnimator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
