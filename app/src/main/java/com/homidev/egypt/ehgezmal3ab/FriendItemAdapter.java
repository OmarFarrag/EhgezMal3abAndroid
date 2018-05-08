package com.homidev.egypt.ehgezmal3ab;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class FriendItemAdapter extends RecyclerView.Adapter<FriendItemAdapter.FriendViewHolder>{

    private ConnectionManager connectionManager;
    private static ArrayList<Friend> friendsList = new ArrayList<>();
    private Context mContext;

    public FriendItemAdapter(Context context) {
        connectionManager = ConnectionManager.getConnectionManager();
        mContext = context;
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
        holder.bindFriend(friendsList.get(position));
    }

    @Override
    public FriendItemAdapter.FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, parent, false);
        FriendViewHolder viewHolder = new FriendViewHolder(view);
        return viewHolder;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView friendFullName;
        private Context mContext;

        public FriendViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            friendFullName = (TextView) itemView.findViewById(R.id.friendFullNameTextView);
        }

        public void bindFriend(Friend friend){
            friendFullName = (TextView) itemView.findViewById(R.id.friendFullNameTextView);
            friendFullName.setText(friend.getFriend().getName());
        }
    }
}
