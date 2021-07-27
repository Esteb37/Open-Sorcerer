package com.example.opensorcerer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.databinding.ItemConversationBinding;
import com.example.opensorcerer.models.Conversation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * RecyclerView Adapter class for conversations
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ConversationsAdapter extends RecyclerView.Adapter<ConversationHolder> {

    /**
     * The adapter's current context
     */
    private final Context mContext;

    /**
     * The list of conversations to display
     */
    private final List<Conversation> mConversations;

    /**
     * Binder object for ViewBinding
     */
    private ItemConversationBinding mApp;

    /**
     * Click listener
     */
    private final OnClickListener mClickListener;

    public ConversationsAdapter(List<Conversation> conversations, Context context, OnClickListener clickListener) {
        mConversations = conversations;
        mContext = context;
        mClickListener = clickListener;
    }

    /**
     * Inflates the ViewHolder and prepares it for the items
     *
     * @return the inflated ConversationHolder
     */
    @NonNull
    @NotNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        mApp = ItemConversationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = mApp.getRoot();
        return new ConversationHolder(view, mContext, mApp ,mClickListener);
    }

    /**
     * Binds the conversation to a ViewHolder to display it
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ConversationHolder holder, int position) {
        holder.bind(mConversations.get(position));
    }

    /**
     * Conversation list item count getter
     */
    @Override
    public int getItemCount() {
        return mConversations.size();
    }

    /**
     * Cleans the conversation list and notifies the adapter
     */
    public void clear() {
        mConversations.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a list of conversations to the adapter
     *
     * @param list A list of conversations
     */
    public void addAll(List<Conversation> list) {
        mConversations.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Interface for detecting clicks on the conversation
     */
    public interface OnClickListener {
        void onItemClicked(int position);
    }
}
