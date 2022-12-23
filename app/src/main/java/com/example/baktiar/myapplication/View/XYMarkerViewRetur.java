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
 * Created by TRIPILAR on 19/11/2018.
 */

public class XYMarkerViewRetur extends MarkerView {

    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;

    private DecimalFormat format;

    public XYMarkerViewRetur(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("#.#");
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        BarEntry be = (BarEntry) e;
        tvContent.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null) + " : " + Utils.formatNumber(be.getYVals()[highlight.getStackIndex()], 1, true)+"%");
//        GrafikBarActivity.
        super.refreshContent(e, highlight);
//        tvContent.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null) + " : Total " + format.format(e.getY())+"%");
    }


//    @Override
//    public void refreshContent(Entry e, Highlight highlight) {
//
//        if (e instanceof BarEntry) {
//
//            BarEntry be = (BarEntry) e;
//
//            if(be.getYVals() != null) {
//
//                // draw the stack value
//                tvContent.setText("" + Utils.formatNumber(be.getYVals()[highlight.getStackIndex()], 0, true));
//            } else {
//                tvContent.setText("" + Utils.formatNumber(be.getY(), 0, true));
//            }
//        } else {
//
//            tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true));
//        }
//
//        super.refreshContent(e, highlight);
//    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
