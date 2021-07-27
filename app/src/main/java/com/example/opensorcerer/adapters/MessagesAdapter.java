package com.example.opensorcerer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.opensorcerer.databinding.ItemMessageIncommingBinding;
import com.example.opensorcerer.databinding.ItemMessageOutgoingBinding;
import com.example.opensorcerer.models.Message;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * RecyclerView Adapter class for messages
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MessagesAdapter extends RecyclerView.Adapter<MessageHolder> {

    /**
     * The adapter's current context
     */
    private final Context mContext;

    /**
     * The list of messages to display
     */
    private final List<Message> mMessages;

    /**
     * The current user
     */
    private final User mUser;


    public static final int MESSAGE_INCOMING = 0;
    public static final int MESSAGE_OUTGOING = 1;

    public MessagesAdapter(List<Message> messages, Context context) {
        mMessages = messages;
        mContext = context;
        mUser = User.getCurrentUser();
    }

    /**
     * Inflates the ViewHolder and prepares it for the items
     *
     * @return the inflated MessageHolder
     */
    @NonNull
    @NotNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_INCOMING){
            ItemMessageIncommingBinding app = ItemMessageIncommingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MessageHolder(app.getRoot(), mContext, app, viewType);
        } else if (viewType == MESSAGE_OUTGOING){
            ItemMessageOutgoingBinding app = ItemMessageOutgoingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MessageHolder(app.getRoot(), mContext, app, viewType);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    /**
     * Binds the message to a ViewHolder to display it
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageHolder holder, int position) {
        holder.bind(mMessages.get(position));
    }

    /**
     * Message list item count getter
     */
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    /**
     * Cleans the message list and notifies the adapter
     */
    public void clear() {
        mMessages.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a list of messages to the adapter
     *
     * @param list A list of messages
     */
    public void addAll(List<Message> list) {
        mMessages.addAll(list);
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        mMessages.add(0,message);
        notifyItemInserted(0);;
    }

    /**
     * Interface for detecting clicks on the messages
     */
    public interface OnClickListener {
        void onItemClicked(int position);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessages.get(position);
        Log.d("Test",mUser.getObjectId() + message.getAuthor().getObjectId());
        if (mUser.getObjectId().equals(message.getAuthor().getObjectId())) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }

}
