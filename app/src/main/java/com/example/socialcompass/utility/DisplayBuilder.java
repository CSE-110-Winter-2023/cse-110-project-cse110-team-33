package com.example.socialcompass.utility;

import android.util.Pair;
import android.view.Gravity;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.R;
import com.example.socialcompass.model.Location;

public class DisplayBuilder {

    private Context context;
    private ConstraintLayout parent;

    private ImageView centerCircle;
    // for centering views

    private int zoom = 2; // values between 1 and 4, default for now is 4
    private ImageView[] radarImageViews = new ImageView[4]; // 0-1, 1-10, 10-500, 500+ miles

    private List<LiveData<Location>> liveLocations = Collections.emptyList();

    private int[] boundary = new int[]{0, 1, 10, 500, 1000};

    //    private Map<String, TextView> labels = new HashMap<>();
    private AngleCalculation angleCalculator;
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
        int i = 0;
        while (i < 2) {
            this.radarImageViews[i] = addImageViewPercent(percent);
            percent -= 0.5;
            i++;
        }
        while (i < 4) {
            this.radarImageViews[i] = addImageViewPercent(1);
            this.radarImageViews[i].setVisibility(View.INVISIBLE);
            i++;
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

                    //Log.d("HEIGHT5", String.format("%d %f", parent.getHeight(), percent));

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
        return this;
    }
    public DisplayBuilder zoomOut() {
        if (zoom == 4) return this;
        zoom++;
        updateZoom();
        return this;
    }

    // should we update location icons in builder?
    public void setLiveLocations(Pair<Double, Double> self_location, Location location,
                                 Map<String, TextView> labels,
                                 ImageView compassDisplay,ConstraintLayout compassConstraintLayout) {

        double level = currentZoomLevel();
        double parentRadius = (double)getConstraintLayout().getHeight()/ 2.0;

        this.liveLocations = liveLocations;

        double distance = distanceCalculator.CalculateDistance(self_location.first,
                self_location.second, location.latitude, location.longitude);
        double relative_angle = angleCalculator.calculateBearing(self_location.first,
                self_location.second, location.latitude,location.longitude);

        if (!labels.containsKey(location.label)) {
            TextView textView = new TextView(context);
            textView.setId(View.generateViewId());
            textView.setText(location.label);


            //If the view is outside the boundary, make it X
            switch (zoom){
                case 1:
                    if (distance >= boundary[1]){
                        textView.setText("X");
                    }
                    break;
                case 2:
                    if (distance > boundary[2]){
                        textView.setText("X");
                    }
                    break;
                case 3:
                    if (distance > boundary[3]){
                        textView.setText("X");
                    }
                    break;
                case 4:
                    if (distance > boundary[4]){
                        textView.setText("X");
                    }
                    break;
            }


            ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(newParams);
            textView.setGravity(Gravity.CENTER);
            newParams.circleAngle = 0;
            newParams.circleRadius = (int)distanceCalculator.pixelCalculator(level,parentRadius,distance);

            newParams.circleConstraint = compassConstraintLayout.getId();

            compassConstraintLayout.addView(textView);
            labels.put(location.label, textView);
        }
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)
                labels.get(location.label).getLayoutParams();


        params.circleAngle = (float) relative_angle;
        labels.get(location.label).setLayoutParams(params);
        int loc[] = new int[2];
        labels.get(location.label).getLocationOnScreen(loc);

    }

    public ConstraintLayout getConstraintLayout() {
        return parent;
    }

    public int currentZoomLevel() {
        return zoom;
    }

    public void viewsOverlap(Map<String, TextView> labels) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (var target : labels.entrySet()) {

                    int targetDimens[] = new int[4];
                    getDimens(target.getValue(), targetDimens);

                    ConstraintLayout.LayoutParams targetParams =
                            (ConstraintLayout.LayoutParams) target.getValue().getLayoutParams();

                    for (var check : labels.entrySet()) {
                        if (target.getKey().equals(check.getKey())) continue;

                        int checkDimens[] = new int[4];
                        getDimens(check.getValue(), checkDimens);

                        if (overlap(targetDimens, checkDimens)) {
                            Log.d("ABSOLUTELOC", String.format("%s %s",
                                    target.getKey(), check.getKey()));


                            ConstraintLayout.LayoutParams checkParams =
                                    (ConstraintLayout.LayoutParams) check.getValue().getLayoutParams();


                            if (!target.getValue().getText().equals("X")){
                                target.getValue().setText(target.getKey().substring(0, 4));
                            }
                            if (!check.getValue().getText().equals("X")){
                                check.getValue().setText(check.getKey().substring(0, 4));
                            }

                            Log.d("ABSOLUTELOC", String.format("%d %d",
                                    targetParams.circleRadius, checkParams.circleRadius));

                            if (Math.abs(targetParams.circleRadius - checkParams.circleRadius) > 60) continue;

                            if (targetParams.circleRadius <= checkParams.circleRadius) {
                                targetParams.circleRadius -= 5;
                                checkParams.circleRadius += 5;
                            } else {
                                checkParams.circleRadius -= 5;
                                targetParams.circleRadius += 5;
                            }

                        }

                    }

            }
        }
        }).run();
    }

    private void getDimens(View view, int[] dimens) {

        int loc[] = new int[2];
        view.getLocationOnScreen(loc);

        dimens[0] = loc[0];
        dimens[1] = loc[1];
        dimens[2] = view.getWidth();
        dimens[3] = view.getHeight();

    }

    private boolean overlap(int[] target, int[] check) {

        boolean horizontalOverlap = (Math.abs(target[0] - check[0]) <= target[2] + check[2]);
        boolean verticalOverlap = (Math.abs(target[1] - check[1]) <= target[3] + check[3]);

        if (horizontalOverlap && verticalOverlap) return true;
        return false;
    }


}

