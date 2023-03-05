package com.example.socialcompass.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcompass.R;
import com.example.socialcompass.model.Location;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<Location> locationList = Collections.emptyList();
    private Consumer<Location> onDeleteBtnClicked;
    private BiConsumer<Location, Float> onLatChanged;
    private BiConsumer<Location, Float> onLongChanged;
    private BiConsumer<Location, String> onLocationNameChanged;
    private BiConsumer<Location, String> onIconChanged;

    public void setLocationList(List<Location> locationList) {
        this.locationList.clear();
        this.locationList = locationList;
        Log.d("LOCATIONADAPTER", locationList.toString());
        notifyDataSetChanged();
    }

    public void setOnDeleteBtnClickedHandler(Consumer<Location> onDeleteBtnClicked) {
        this.onDeleteBtnClicked = onDeleteBtnClicked;
    }

    public void setOnLatChanged(BiConsumer<Location, Float> onLatChanged) {
        this.onLatChanged = onLatChanged;
    }

    public void setOnLongChanged(BiConsumer<Location, Float> onLongChanged) {
        this.onLongChanged = onLongChanged;
    }

    public void setOnLocationNameChanged(BiConsumer<Location, String> onLocationNameChanged) {
        this.onLocationNameChanged = onLocationNameChanged;
    }

    public void setOnIconChanged(BiConsumer<Location, String> onIconChanged) {
        this.onIconChanged = onIconChanged;
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
        return locationList.get(position).public_code.hashCode();
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
            this.removeBtn = itemView.findViewById(R.id.deleteBtn);

            this.removeBtn.setOnClickListener(view -> {
                if(onDeleteBtnClicked == null) return;
                onDeleteBtnClicked.accept(location);
            });

            this.locLat.setOnFocusChangeListener((view, hasFocus) -> {
                if(!hasFocus) {
                    onLatChanged.accept(location, Float.valueOf(locLat.getText().toString()));
                }
            });

            this.locLong.setOnFocusChangeListener((view, hasFocus) -> {
                if(!hasFocus) {
                    onLongChanged.accept(location, Float.valueOf(locLong.getText().toString()));
                }
            });


            this.locIcon = itemView.findViewById(R.id.locIcon);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
            this.locIcon.setAdapter(adapter);
            this.locIcon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    onIconChanged.accept(location, items[i]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    onIconChanged.accept(location, "gray");
                }
            });
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
            this.locName.setText(location.public_code);
            this.locLat.setText(new DecimalFormat("##.##").format(location.latitude));
            this.locLong.setText(new DecimalFormat("##.##").format(location.longitude));
            for (int i = 0; i < items.length; i++) {
                if (location.label.equals(items[i])) {
                    this.locIcon.setSelection(i);
                }
            }
        }
    }
}
