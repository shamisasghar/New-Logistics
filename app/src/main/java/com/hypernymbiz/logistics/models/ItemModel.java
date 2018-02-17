package com.hypernymbiz.logistics.models;

import android.animation.TimeInterpolator;

/**
 * Created by Metis on 16-Feb-18.
 */

public class ItemModel {
    public final String description;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;
    public final String start_time;
    public final String end_time;
    public final boolean rowCheck;

    public ItemModel(boolean rowCheck,String description, String start_time, String end_time, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.description = description;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
        this.start_time=start_time;
        this.end_time=end_time;
        this.rowCheck=rowCheck;
    }

}