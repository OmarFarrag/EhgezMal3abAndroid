package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by engineer on 27/03/18.
 * this class adapts Venue to use it in recyclerview
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class VenueItemAdapter extends RecyclerView.Adapter<VenueItemAdapter.VenueItemViewHolder> {

    private ConnectionManager connectionManager;
    private static List<Venue> venueList;
    private Context context;
    private IRecyclerViewClickListener listener;

    public VenueItemAdapter(Context context, IRecyclerViewClickListener listener) {
        this.context = context;
        connectionManager = ConnectionManager.getConnectionManager();
        //venueList = connectionManager.getAllVenues();
        EhgezMal3abAPI ehgezMal3abAPI = createEhgezMal3abService();
        ehgezMal3abAPI.getAllVenues().enqueue(getAllVenuesCallback);
        this.listener = listener;
    }

    //Called when RecyclerView needs a new VenueItemHolder to represent a venue item.
    @Override
    public VenueItemAdapter.VenueItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_venues_layout, parent, false);
        return new VenueItemViewHolder(view, listener);
    }

    //Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final VenueItemAdapter.VenueItemViewHolder holder, int position) {
        if(holder instanceof VenueItemViewHolder) {
            Venue venue = venueList.get(position);
            //set the attributes of the venue item to be displayed
            holder.venueTitle.setText(venue.getVenueTitle());
            holder.venueStreet.setText(venue.getStreet());
            holder.venueArea.setText(venue.getArea());
            holder.venuePhoneNumber.setText(venue.getPhoneNumber());
            Picasso
                    .with(context)
                    .load(venue.getImageLink())
                    .placeholder(R.drawable.close_icon)
                    .error(R.drawable.close_icon)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            holder.background.setAlpha((float)0.6);
                            holder.background.setBackground(new BitmapDrawable(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

        }
    }

    public static ArrayList<Venue> getVenuesList() {
        return (ArrayList<Venue>) venueList;
    }

    public static Venue getItem(int position) {
        return venueList.get(position);
    }

    @Override
    public int getItemCount() {
        if(venueList == null){
            return 0;
        }
        return venueList.size();
    }


    //A ViewHolder describes an item view and metadata about its place within the RecyclerView
    public class VenueItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView venueTitle;
        TextView venueStreet;
        TextView venueArea;
        TextView venuePhoneNumber;
        ImageView imageView;
        LinearLayout background;
        private IRecyclerViewClickListener listener;
        CardView cardView;

        public VenueItemViewHolder(View itemView, IRecyclerViewClickListener listener) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.venueCardView);
            venueTitle = (TextView) itemView.findViewById(R.id.venueTitle);
            venueStreet = (TextView) itemView.findViewById(R.id.venueStreet);
            venueArea = (TextView) itemView.findViewById(R.id.venueArea);
            venuePhoneNumber = (TextView) itemView.findViewById(R.id.venuePhoneNumber);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            background = (LinearLayout) itemView.findViewById(R.id.container);
            this.listener = listener;
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public EhgezMal3abAPI createEhgezMal3abService(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EhgezMal3abAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(EhgezMal3abAPI.class);

    }

    Callback<List<Venue>> getAllVenuesCallback = new Callback<List<Venue>>() {
        @Override
        public void onResponse(Call<List<Venue>> call, Response<List<Venue>> response) {
            if(response.isSuccessful()){
                venueList = response.body();
            }
        }

        @Override
        public void onFailure(Call<List<Venue>> call, Throwable t) {

        }
    };

}
