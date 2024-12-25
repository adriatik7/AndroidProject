package com.example.androidproject.utils;

import android.content.Context;
import android.widget.TextView;

import com.example.androidproject.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

public class MyMarkerView extends MarkerView {
    private final TextView markerTextView;
    private final ArrayList<String> categories;

    public MyMarkerView(Context context, int layoutResource, ArrayList<String> categories) {
        super(context, layoutResource);
        this.categories = categories;
        markerTextView = findViewById(R.id.marker_text);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = (int) e.getX();
        if (index >= 0 && index < categories.size()) {
            markerTextView.setText(categories.get(index));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
