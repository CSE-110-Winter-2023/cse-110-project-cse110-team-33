package com.example.socialcompass.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcompass.R;
import com.example.socialcompass.model.Location;

import java.util.Collections;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Location> locations = Collections.emptyList();
    private Consumer<Location> onLocationDeleteClicked;

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        var location = locations.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    @Override
    public long getItemId(int position) {
        return locations.get(position).public_code.hashCode();
    }

    public void setOnLocationDeleteClickedListener(Consumer<Location> onLocationDeleteClicked) {
        this.onLocationDeleteClicked = onLocationDeleteClicked;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View itemView;
        public final TextView displayName;
        public final TextView UID;
        public final Button deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.displayName = itemView.findViewById(R.id.displayName);
            this.UID = itemView.findViewById(R.id.UID);
            this.deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }

        public void bind(Location location) {
            displayName.setText(location.label);
            UID.setText(location.public_code);
            deleteBtn.setOnClickListener(v -> onLocationDeleteClicked.accept(location));
        }
    }
}
