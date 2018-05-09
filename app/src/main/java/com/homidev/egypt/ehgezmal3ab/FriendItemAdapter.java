package com.homidev.egypt.ehgezmal3ab;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendItemAdapter extends RecyclerView.Adapter<FriendItemAdapter.FriendViewHolder>{

    private ConnectionManager connectionManager;
    private ArrayList<Friend> friendsList = new ArrayList<>();
    private Context mContext;

    private boolean isRequest;

    public FriendItemAdapter(Context context, boolean isRequest) {
        connectionManager = ConnectionManager.getConnectionManager();
        mContext = context;
        this.isRequest = isRequest;
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public void setFriendsList(ArrayList<Friend> friends){
        if(friends == null){
            friendsList.clear();

        }else {
            friendsList = friends;
        }
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bindFriend(friendsList.get(position), this);
    }

    @Override
    public FriendItemAdapter.FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, parent, false);
        FriendViewHolder viewHolder = new FriendViewHolder(view, isRequest);
        return viewHolder;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView friendFullName;
        private Context mContext;
        Friend friend;
        boolean isRequest;

        public FriendViewHolder(View itemView, boolean isRequest) {
            super(itemView);
            mContext = itemView.getContext();
            friendFullName = (TextView) itemView.findViewById(R.id.friendFullNameTextView);
            this.isRequest = isRequest;
        }

        public void bindFriend(final Friend friend, final FriendItemAdapter recyclerView){
            friendFullName = (TextView) itemView.findViewById(R.id.friendFullNameTextView);
            friendFullName.setText(friend.getFriend().getName());
            this.friend = friend;
            if(isRequest)
            {
                friendFullName.setText(friend.getUsername());
                ImageButton confirmButton = (ImageButton) itemView.findViewById(R.id.confirm_request_friends);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConnectionManager.getConnectionManager().acceptOrDecline("Accepted", friend, recyclerView);
                    }
                });
                confirmButton.setVisibility(View.VISIBLE);
                ImageButton declineButton = (ImageButton) itemView.findViewById(R.id.declineButton_request);
                declineButton.setVisibility(View.VISIBLE);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConnectionManager.getConnectionManager().acceptOrDecline("Declined", friend, recyclerView);
                    }
                });
            }
        }
    }
}
