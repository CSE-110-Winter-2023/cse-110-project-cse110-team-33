package com.example.socialcompass.utility;

import android.app.Activity;
import android.util.Pair;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.R;
import com.example.socialcompass.model.Location;

import org.w3c.dom.Text;

public class DisplayBuilder {

    private Context context;
    private ConstraintLayout parent;

    private ImageView centerCircle;
    // for centering views

    private int zoom = 4; // values between 1 and 4, default for now is 4
    private ImageView[] radarImageViews = new ImageView[4]; // 0-1, 1-10, 10-500, 500+ miles

    private List<LiveData<Location>> liveLocations = Collections.emptyList();

//    private Map<String, TextView> labels = new HashMap<>();
    private AngleCalculation calculator;
    private DistanceCalculation distanceCalculator;
//    private ImageView compassDisplay = centerCircle.findViewById(R.id.compassDisplay);
//    private ConstraintLayout compassConstraintLayout = parent.findViewById(R.id.compassConstraintLayout);
//    compassDisplay =
//    compassConstraintLayout =

    public DisplayBuilder(Context context) {
        this.context = context;
        parent = new ConstraintLayout(this.context);
        parent.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        ));

        centerCircle = new ImageView(this.context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        centerCircle.setLayoutParams(layoutParams);
        centerCircle.setBackgroundResource(R.drawable.radar_invisible);
        this.parent.addView(centerCircle);

        // important for centering views
        centerCircle.setId(View.generateViewId());

        createRadar();
    }

    private ImageView addImageViewPercent(double percent) {
        ImageView imageView = new ImageView(this.context);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(0, 0);

        imageView.setLayoutParams(layoutParams);
        imageView.setBackgroundResource(R.drawable.radar_circle);
        this.parent.addView(imageView);

        imageView.post(new Runnable() {
            @Override
            public void run() {
                int height = parent.getHeight();
                int width = parent.getWidth();

                Log.d("HEIGHT", String.format("%d %d", height, width));


                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();

                params.height = (int) (height * percent);
                params.width = (int) (width * percent);
                params.circleConstraint = centerCircle.getId();

                Log.d("HEIGHT", String.format("%d %d", imageView.getLayoutParams().height,
                        imageView.getLayoutParams().width));

                imageView.requestLayout();
            }
        });

        return imageView;
    }

    private void createRadar() {
        double percent = 1;
        for (int i = 3; i >= 0; i--) {
            this.radarImageViews[i] = addImageViewPercent(percent);
            percent -= 0.25;
        }
    }

    private void updateZoom() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                double percent = 1;
                int i = 0;
                while (i < zoom) {
                    final var imageView = radarImageViews[i];

                    imageView.setVisibility(View.VISIBLE);
                    var params = imageView.getLayoutParams();

                    // not waiting till image loads
                    params.height = (int) (parent.getHeight() * percent);
                    params.width = (int) (parent.getWidth() * percent);

                    Log.d("HEIGHT5", String.format("%d %f", parent.getHeight(), percent));

                    imageView.requestLayout();

                    percent -= (1.0 / zoom);
                    i++;
                }
                while (i < 4) {
                    radarImageViews[i].setVisibility(View.INVISIBLE);
                    radarImageViews[i].requestLayout();
                    i++;
                }
            }
        }).run();
    }

    public DisplayBuilder zoomIn() {
        if (zoom == 1) return this;
        zoom--;
        updateZoom();

//        displayIcon(level,parentRadius,this);

//        Log.d("ZOOM", String.valueOf(zoom));
        return this;
    }
    public DisplayBuilder zoomOut() {
        if (zoom == 4) return this;
        zoom++;
        updateZoom();
//        Log.d("ZOOM", String.valueOf(zoom));
        return this;
    }

    // should we update location icons in builder?
    public void setLiveLocations(Pair<Double, Double> self_location, Location location, Map<String, TextView> labels,ImageView compassDisplay,ConstraintLayout compassConstraintLayout) {
        // remove all icons
//        for (Map.Entry<String, TextView> entry : labels.entrySet()) {
//            labels.remove(entry.getKey());
//        }

        double level = currentZoomLevel();
        double parentRadius = (double)getConstraintLayout().getHeight()/ 2.0;
//        double parentRadius = 450;
        this.liveLocations = liveLocations;

//         create icons
        double distance = distanceCalculator.CalculateDistance(self_location.first,
                self_location.second, location.longitude, location.latitude);
        double mockdistance = 300;
        if (!labels.containsKey(location.label)) {
            TextView textView = new TextView(context);
            textView.setId(View.generateViewId());
            textView.setText(location.label);

            ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(88, 88);
            textView.setLayoutParams(newParams);
            newParams.circleAngle = 0;
            newParams.circleRadius = (int)distanceCalculator.pixelCalculator(level,parentRadius,mockdistance);
            System.out.println(newParams.circleRadius);
//            newParams.circleRadius = (int) distance / 100;
            newParams.circleConstraint = compassDisplay.getId();

            compassConstraintLayout.addView(textView);
            labels.put(location.label, textView);
        }
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)
                labels.get(location.label).getLayoutParams();
        double relative_angle = calculator.calculateBearing(self_location.first,
                self_location.second, location.longitude, location.latitude);
        params.circleAngle = (float) relative_angle;
        labels.get(location.label).setLayoutParams(params);
    }

    public ConstraintLayout getConstraintLayout() {
        return parent;
    }

    public int currentZoomLevel() {
        return zoom;
    }


}

