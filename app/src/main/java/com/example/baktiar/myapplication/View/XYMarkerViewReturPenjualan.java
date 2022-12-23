package com.example.baktiar.myapplication.View;

import android.content.Context;
import android.widget.TextView;

import com.example.baktiar.myapplication.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;

/**
 * Created by TRIPILAR on 22/11/2018.
 */

public class XYMarkerViewReturPenjualan extends MarkerView {

    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;

    private DecimalFormat format;

    public XYMarkerViewReturPenjualan(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);


        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("#.###");

    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null) + " : Total " + format.format(e.getY())+"%");
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
