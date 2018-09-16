package com.simplexorg.mapfragment.cluster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.simplexorg.mapfragment.R;

public class IconMaker {
    private View mContainer;
    private TextView mTextView;

    public IconMaker(Context context) {
        mContainer = LayoutInflater.from(context).inflate(R.layout.map_amu_text_bubble, null);
        mTextView = mContainer.findViewById(R.id.map_bubble_txt);
    }

    Bitmap makeIcon(String text) {
        mTextView.setText(text);

        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mContainer.measure(measureSpec, measureSpec);

        int measuredWidth = mContainer.getMeasuredWidth();
        int measuredHeight = mContainer.getMeasuredHeight();

        mContainer.layout(0, 0, measuredWidth, measuredHeight);

        Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);

        Canvas canvas = new Canvas(bitmap);
        mContainer.draw(canvas);
        return bitmap;
    }
}
