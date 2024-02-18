package edu.northeastern.numad24sp_group4.EventsDisplay_RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad24sp_group4.R;

public class EventHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView name;
    public TextView location;
    public TextView dateTime;

    public EventHolder(View itemView, final EventInterface listener) {
        super(itemView);
        image = itemView.findViewById(R.id.eventImage);
        name = itemView.findViewById(R.id.eventName);
        location = itemView.findViewById(R.id.eventLocation);
        dateTime = itemView.findViewById(R.id.eventDateTime);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onLinkClick(position);
                    }
                }
            }
        });


    }
}

