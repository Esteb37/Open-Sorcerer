package com.example.opensorcerer.ui.views;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Custom Text view that expands and retracts on click
 * Code fragments taken from:
 *      Title: Android - Expandable TextView with Animation
 *      Author: Bazlur Rahman Rokon
 *      Date: September 7, 2013
 *      Availability: https://stackoverflow.com/questions/15627530/android-expandable-textview-with-animation
 */
@SuppressWarnings("unused")
public class ExpandableTextView extends androidx.appcompat.widget.AppCompatTextView {

    TextView mMoreMessage;

    private boolean trim = true;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnClickListener(v -> {
            trim = !trim;

            this.setMaxLines(trim ? 1 : 10);

            mMoreMessage.setText(trim ? "See more" : "See less");

            requestFocusFromTouch();
        });
    }

    private boolean isEllipsized(TextView textView){
        Layout layout = textView.getLayout();
        if (layout != null) {
            int lines = layout.getLineCount();
            if (lines > 0)
                return layout.getEllipsisCount(lines - 1) > 0;
        }
        return false;
    }

    public void setMoreMessage(TextView moreMessage){
        mMoreMessage = moreMessage;
        moreMessage.setVisibility(isEllipsized(this) ? View.VISIBLE : View.GONE);
    }
    public boolean isTrimmed(){
        return trim;
    }
}
