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
import com.example.opensorcerer.databinding.ItemProjectBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.roles.Developer;
import com.parse.ParseFile;


import java.util.List;

/**
 * ViewHolder class for Projects in linear Card format
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "FieldMayBeFinal"})
public class ProjectCardHolder extends RecyclerView.ViewHolder{

    /**Binder object for ViewBinding*/
    private final ItemProjectBinding app;

    /**The Holder's context*/
    private final Context mContext;

    /**Current user*/
    private Developer mUser;

    /**Current Project*/
    private Project mProject;
    
    public ProjectCardHolder(View view, Context context, ItemProjectBinding binder, ProjectsCardAdapter.OnClickListener clickListener, ProjectsCardAdapter.OnDoubleTapListener doubleTapListener) {
        super(view);
        app = binder;
        
        mContext = context;

        mUser = Developer.getCurrentUser();

        
        view.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

        view.setOnTouchListener(new View.OnTouchListener() {

            private final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e){
                    doubleTapListener.onItemDoubleTap(getAdapterPosition());
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
        app.tvTitle.setText(mProject.getTitle());
        app.tvDescription.setText(mProject.getDescription());
        app.tvTitle.setMaxLines(mProject.getTitle().split(" ").length);
        

        //Show the first three tags
        TextView[] tagViews = {app.tvTag1,app.tvTag2,app.tvTag3};
        List<String> tags = mProject.getTags();
        for(int i = 0;i<3;i++){
            try {
                tagViews[i].setText(tags.get(i).replace("-"," "));
            } catch(IndexOutOfBoundsException e){
                tagViews[i].setText("");
            }
        }

        //Show the first three languages
        TextView[] languageViews = {app.tvLanguage1,app.tvLanguage2,app.tvLanguage3};
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
                    .into(app.ivImage);
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
        app.buttonLike.setImageDrawable(wrappedDrawable);
        app.buttonLikeAnimator.setImageDrawable(wrappedDrawable);
    }

    private void showLikeAnimation(float posX, float posY) {
        app.buttonLikeAnimator.clearAnimation();
        app.buttonLikeAnimator.setVisibility(View.VISIBLE);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator()); //and this
        fadeOut.setDuration(500);

        Animation growOut= new ScaleAnimation(
                1f, 2f, // Start and end values for the X axis scaling
                1f, 2f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        growOut.setFillAfter(true); // Needed to keep the result of the animation
        growOut.setDuration(500);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeOut);
        animation.addAnimation(growOut);
        app.buttonLikeAnimator.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                app.buttonLikeAnimator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
