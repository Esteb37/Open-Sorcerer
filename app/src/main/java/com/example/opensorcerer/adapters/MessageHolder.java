package com.example.opensorcerer.adapters;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.databinding.ItemMessageIncommingBinding;
import com.example.opensorcerer.databinding.ItemMessageOutgoingBinding;
import com.example.opensorcerer.models.Message;
import com.example.opensorcerer.models.Tools;

/**
 * ViewHolder class for conversation objects
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
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
    /**
     * The Holder's context
     */
    private final Context mContext;

    public MessageHolder(View view, Context context, ItemMessageIncommingBinding binder, int viewType) {
        super(view);
        mAppIncoming = binder;
        mContext = context;
        mViewType = viewType;
    }

    public MessageHolder(View view, Context context, ItemMessageOutgoingBinding binder, int viewType) {
        super(view);
        mAppOutgoing = binder;
        mContext = context;
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
