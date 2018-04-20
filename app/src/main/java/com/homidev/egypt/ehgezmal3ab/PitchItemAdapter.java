package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by engineer on 09/04/18.
 */

public class PitchItemAdapter extends RecyclerView.Adapter<PitchItemAdapter.PitchItemViewHolder> {

    private ConnectionManager connectionManager;
    private static List<Pitch> pitchList;
    private Context context;
    private IRecyclerViewClickListener listener;

    public PitchItemAdapter(Context context, String venueID, IRecyclerViewClickListener listener) {
        this.context = context;
        this.connectionManager = ConnectionManager.getConnectionManager();
        connectionManager.getVenuePitches(venueID,this);
        this.listener = listener;
    }

    @Override
    public PitchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_pitches_layout, parent, false);
        return new PitchItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(PitchItemViewHolder holder, int position) {
        if(holder instanceof PitchItemViewHolder) {
            Pitch pitch = pitchList.get(position);
            holder.pitchTitle.setText(pitch.getPitchName());
            holder.pitchCapacity.setText(pitch.getCapacity().toString() + "x" + pitch.getCapacity().toString());
            holder.pitchPrice.setText(pitch.getPrice().toString());
            holder.pitchType.setText(pitch.getType());
        }
    }

    @Override
    public int getItemCount() {
        if(pitchList == null) {
            return 0;
        }
        return pitchList.size();
    }

    public void setPitchList(ArrayList<Pitch> pitches){
        pitchList = pitches;
        notifyDataSetChanged();
    }

    public static Pitch getItem(int position) {
        return pitchList.get(position);
    }

    public class PitchItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView pitchTitle;
        private TextView pitchCapacity;
        private TextView pitchType;
        private TextView pitchPrice;
        private IRecyclerViewClickListener listener;
        CardView cardView;

        public PitchItemViewHolder(View itemView, IRecyclerViewClickListener listener) {
            super(itemView);
            pitchTitle = itemView.findViewById(R.id.pitchTitle);
            pitchCapacity = itemView.findViewById(R.id.pitchCapacity);
            pitchPrice = itemView.findViewById(R.id.pitchPrice);
            pitchType = itemView.findViewById(R.id.pitchType);
            this.listener = listener;
            cardView = itemView.findViewById(R.id.pitchCardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

}
