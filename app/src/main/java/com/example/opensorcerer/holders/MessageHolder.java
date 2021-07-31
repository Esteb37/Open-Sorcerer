package com.example.opensorcerer.holders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.adapters.MessagesAdapter;
import com.example.opensorcerer.databinding.ItemMessageIncommingBinding;
import com.example.opensorcerer.databinding.ItemMessageOutgoingBinding;
import com.example.opensorcerer.models.Message;
import com.example.opensorcerer.models.Tools;

/**
 * ViewHolder class for conversation objects
 */
public class MessageHolder extends RecyclerView.ViewHolder {

    /**
     * Binder object for ViewBinding for incoming messages
     */
    private ItemMessageIncommingBinding mAppIncoming;

    /**
     * Binder object for ViewBinding for outgoing messages
     */
    private ItemMessageOutgoingBinding mAppOutgoing;

    private final int mViewType;

    public MessageHolder(View view, ItemMessageIncommingBinding binder, int viewType) {
        super(view);
        mAppIncoming = binder;
        mViewType = viewType;
    }

    public MessageHolder(View view, ItemMessageOutgoingBinding binder, int viewType) {
        super(view);
        mAppOutgoing = binder;
        mViewType = viewType;
    }

    /**
     * Populates the view's items with the conversation's information
     */
    public void bind(Message message) {

        if(mViewType == MessagesAdapter.MESSAGE_INCOMING){
            mAppIncoming.textViewContent.setText(message.getContent());
            mAppIncoming.textViewTimestamp.setText(Tools.getRelativeTimeStamp(message.getCreatedAt()));
        } else if (mViewType == MessagesAdapter.MESSAGE_OUTGOING){
            mAppOutgoing.textViewContent.setText(message.getContent());
            mAppOutgoing.textViewTimestamp.setText(Tools.getRelativeTimeStamp(message.getCreatedAt()));
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

}
