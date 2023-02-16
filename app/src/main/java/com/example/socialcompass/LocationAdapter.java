package com.example.socialcompass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<Location> locationList = Collections.emptyList();

    public void setLocationList(List<Location> locationList) {
        this.locationList.clear();
        this.locationList = locationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        holder.setLocation(locationList.get(position));
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    @Override
    public long getItemId(int position) {
        return locationList.get(position).id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final EditText locName;
        private final EditText locLat;
        private final EditText locLong;
        private final Button removeBtn;

        private Location location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.locName = itemView.findViewById(R.id.locName);
            this.locLat = itemView.findViewById(R.id.locLat);
            this.locLong = itemView.findViewById(R.id.locLong);
            this.removeBtn = itemView.findViewById(R.id.removeBtn);
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
            this.locName.setText(location.name);
            this.locLat.setText(Double.toString(location.latitude));
            this.locLong.setText(Double.toString(location.longitude));
        }
    }
}
