package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by engineer on 09/04/18.
 */

public class PitchItemAdapter extends RecyclerView.Adapter<PitchItemAdapter.PitchItemViewHolder> {

    private ConnectionManager connectionManager;
    private List<Pitch> pitchList;
    private Context context;

    public PitchItemAdapter(Context context) {
        this.context = context;
        this.connectionManager = ConnectionManager.getConnectionManager();
        //pitchList = connectionManager.getPitches(Venue venue);
    }

    @Override
    public PitchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_pitches_layout, parent, false);
        return new PitchItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PitchItemViewHolder holder, int position) {
        Pitch pitch = pitchList.get(position);
        holder.pitchTitle.setText(pitch.getPitchTitle());
        holder.pitchDescription.setText(pitch.getPitchDescription());
    }

    @Override
    public int getItemCount() {
        return pitchList.size();
    }

    public class PitchItemViewHolder extends RecyclerView.ViewHolder {

        private TextView pitchTitle;
        private TextView pitchDescription;

        public PitchItemViewHolder(View itemView) {
            super(itemView);
            pitchTitle = (TextView) itemView.findViewById(R.id.pitchTitle);
            pitchDescription = (TextView) itemView.findViewById(R.id.pitchDescription);
        }
    }

}
