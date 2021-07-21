package com.example.opensorcerer.ui.developer.views;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * User: Bazlur Rahman Rokon
 * Date: 9/7/13 - 3:33 AM
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
