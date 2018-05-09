package com.homidev.egypt.ehgezmal3ab;


import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewFriends#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ViewFriends extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    boolean buttonListener = false;

    RecyclerView friendsRecycler;

    ConnectionManager connectionManager = ConnectionManager.getConnectionManager();


    public ViewFriends() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewFriends.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFriends newInstance(String param1, String param2) {
        ViewFriends fragment = new ViewFriends();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View friendsFrag = inflater.inflate(R.layout.view_friends_list, container, false);
        friendsRecycler = (RecyclerView) friendsFrag.findViewById(R.id.friendsRecycler);
        final FriendItemAdapter friendItemAdapter = new FriendItemAdapter(getContext(), false);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                friendsRecycler.getContext(),
                layoutManager.getOrientation()
        );
        friendsRecycler.addItemDecoration(dividerItemDecoration);
        friendsRecycler.setLayoutManager(layoutManager);
        friendsRecycler.setAdapter(friendItemAdapter);
        FloatingActionButton addFriendButton = (FloatingActionButton) friendsFrag.findViewById(R.id.addFriends_FAB);
        setAddFriendsListener(addFriendButton, container, friendsFrag);
        final RelativeLayout addFriends = (RelativeLayout) friendsFrag.findViewById(R.id.addFriendLayout);
        final RelativeLayout requestsLayout = (RelativeLayout) friendsFrag.findViewById(R.id.viewRequests_layout);
        ImageButton imageButton = (ImageButton) friendsFrag.findViewById(R.id.hideAddFriendsButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeAddFriendLayout(friendsFrag, addFriends);
                connectionManager.getMyFriends("Accepted", friendItemAdapter);
            }
        });
        ImageButton slideDownButton = (ImageButton) friendsFrag.findViewById(R.id.hideAddFriendsButton_request);
        slideDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAddFriendLayout(friendsFrag, requestsLayout);
                connectionManager.getMyFriends("Accepted", friendItemAdapter);
            }
        });

        FloatingActionButton requestsFloating = (FloatingActionButton) friendsFrag.findViewById(R.id.viewRequestsFAB);
        setRequestsButton(requestsFloating, container, friendsFrag);
        setAddFriendListener(friendsFrag);
        connectionManager.getMyFriends("Accepted", friendItemAdapter);
        return friendsFrag;
    }

    public void setAddFriendsListener(FloatingActionButton addFriendButton, final ViewGroup container, final View itemView){
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pull up the add layout.

                RelativeLayout addFriends = (RelativeLayout) itemView.findViewById(R.id.addFriendLayout);
                RelativeLayout viewFriend = (RelativeLayout) itemView.findViewById(R.id.ViewFriendsLayout);
                //addFriends.bringToFront();
                //addFriends.setVisibility(View.VISIBLE);

                openAddFriendLayout(itemView, addFriends);
            }
        });
    }

    public void setRequestsButton(FloatingActionButton requestButton, final ViewGroup container, final View itemView){
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pull up the add layout.

                RelativeLayout requests = (RelativeLayout) itemView.findViewById(R.id.viewRequests_layout);

                openAddFriendLayout(itemView, requests);
                RecyclerView friendRequests = (RecyclerView) itemView.findViewById(R.id.friendRequestsList);
                ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar_requests);
                FriendItemAdapter requestsAdapter = new FriendItemAdapter(getContext(), true);
                LinearLayoutManager layoutManager =
                        new LinearLayoutManager(getContext());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                        friendsRecycler.getContext(),
                        layoutManager.getOrientation()
                );
                friendRequests.addItemDecoration(dividerItemDecoration);
                friendRequests.setLayoutManager(layoutManager);
                friendRequests.setAdapter(requestsAdapter);
                connectionManager.getMyFriendRequests(requestsAdapter, progressBar);
            }
        });
    }

    private void openAddFriendLayout(View itemView, final RelativeLayout friends){
        final Context root = itemView.getContext();

        friends.setTop(friends.getHeight());
        Animation animation = AnimationUtils.loadAnimation(root, R.anim.slide_up);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                friends.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                friends.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        friends.startAnimation(animation);
    }

    private void closeAddFriendLayout(View itemView, final RelativeLayout layout){
        final Context root = itemView.getContext();

        Animation animate = AnimationUtils.loadAnimation(root, R.anim.slide_down);

        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.startAnimation(animate);
        RelativeLayout viewFriends  = (RelativeLayout) itemView.findViewById(R.id.ViewFriendsLayout);
        viewFriends.setVisibility(View.VISIBLE);
    }

    private void setAddFriendListener(final View itemView)
    {
        ImageButton addFriendButton = (ImageButton) itemView.findViewById(R.id.addFriendButton);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check that the friendUsername is not empty.
                EditText editText = (EditText) itemView.findViewById(R.id.friendUsernameField);
                if(!editText.getText().equals("")){
                    connectionManager.addFriend(editText.getText().toString(), itemView.getContext());
                }else {
                    Toast.makeText(getContext(), "Friend name cannot be empty.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
