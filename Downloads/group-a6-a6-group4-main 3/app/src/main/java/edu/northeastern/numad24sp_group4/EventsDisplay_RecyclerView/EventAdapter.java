package edu.northeastern.numad24sp_group4.EventsDisplay_RecyclerView;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

import edu.northeastern.numad24sp_group4.R;


public class EventAdapter extends RecyclerView.Adapter<EventHolder>{

    private final ArrayList<EventCard> itemList;
    private EventInterface listener;

    //Constructor
    public EventAdapter(ArrayList<EventCard> itemList) {
        this.itemList = itemList;
    }

    public void setOnItemClickListener(EventInterface listener) {
        this.listener = listener;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new EventHolder(view, listener);
    }


    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        EventCard currentItem = itemList.get(position);

        holder.name.setText(currentItem.getName());
        holder.location.setText(currentItem.getLocation());
        holder.dateTime.setText(currentItem.getDateTime());

        String imageUrl = currentItem.getImage();
        if (imageUrl != null && imageUrl!="") {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.image);
        } else {
            // Handle null case or set a default image
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLinkClick(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
