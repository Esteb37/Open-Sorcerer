package com.example.opensorcerer.adapters;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opensorcerer.databinding.ItemConversationBinding;
import com.example.opensorcerer.models.Conversation;
import com.example.opensorcerer.models.Message;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.List;

/**
 * ViewHolder class for conversation objects
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ConversationHolder extends RecyclerView.ViewHolder {

    /**
     * Binder object for ViewBinding
     */
    private final ItemConversationBinding mApp;

    /**
     * The Holder's context
     */
    private final Context mContext;

    public ConversationHolder(View view, Context context, ItemConversationBinding binder, ConversationsAdapter.OnClickListener clickListener) {
        super(view);
        mApp = binder;
        mContext = context;
        view.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
    }

    /**
     * Populates the view's items with the conversation's information
     */
    public void bind(Conversation conversation) {


        List<String> messages = conversation.getMessages();

        ParseQuery<Message> query = ParseQuery.getQuery(Message.class).whereContainedIn("objectId",messages);
        query.addDescendingOrder("createdAt");

        query.getFirstInBackground((lastMessage, e) -> {
            if(e == null){
                ((FragmentActivity) mContext).runOnUiThread(() -> {
                    mApp.textViewContent.setText(lastMessage.getContent());
                    mApp.textViewTimestamp.setText(Tools.getRelativeTimeStamp(lastMessage.getCreatedAt()));
                });
            } else {
                e.printStackTrace();
            }
        });

        // Load other user's information
        try {
            User opposite = conversation.getOpposite().fetchIfNeeded();

            mApp.textViewUsername.setText(opposite.getUsername());

            ParseFile oppositeProfilePicture = opposite.getProfilePicture();
            if (oppositeProfilePicture != null) {
                Glide.with(mContext)
                        .load(oppositeProfilePicture.getUrl())
                        .into(mApp.imageViewProfilePicture);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
