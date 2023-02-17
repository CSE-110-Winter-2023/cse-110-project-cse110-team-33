package com.example.socialcompass;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<Location> locationList = Collections.emptyList();
    private Consumer<Location> onDeleteBtnClicked;
    private BiConsumer<Location, String> onLocationNameChanged;

    public void setLocationList(List<Location> locationList) {
        this.locationList.clear();
        this.locationList = locationList;
        Log.d("LOCATIONADAPTER", locationList.toString());
        notifyDataSetChanged();
    }

    public void setOnDeleteBtnClickedHandler(Consumer<Location> onDeleteBtnClicked) {
        this.onDeleteBtnClicked = onDeleteBtnClicked;
    }

    public void setOnLocationNameChanged(BiConsumer<Location, String> onLocationNameChanged) {
        this.onLocationNameChanged = onLocationNameChanged;
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
        private final Spinner locIcon;

        private Location location;


        private String[] items = new String[]{"blue", "red", "yellow", "green"};

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.locName = itemView.findViewById(R.id.locName);
            this.locName.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    onLocationNameChanged.accept(location,
                            locName.getText().toString());
                }
            });
            this.locLat = itemView.findViewById(R.id.locLat);
            this.locLong = itemView.findViewById(R.id.locLong);
            this.removeBtn = itemView.findViewById(R.id.addBtn);

            this.removeBtn.setOnClickListener(view -> {
                if(onDeleteBtnClicked == null) return;
                onDeleteBtnClicked.accept(location);
            });


            this.locIcon = itemView.findViewById(R.id.locIcon);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
            this.locIcon.setAdapter(adapter);
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
            this.locName.setText(location.name);
            this.locLat.setText(Double.toString(location.latitude));
            this.locLong.setText(Double.toString(location.longitude));
            for (int i = 0; i < items.length; i++) {
                if (location.icon.equals(items[i])) {
                    this.locIcon.setSelection(i);
                }
            }
        }
    }
}
