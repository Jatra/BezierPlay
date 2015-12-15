package uk.co.jatra.bezierplay;

import android.graphics.Paint;

/**
 * Created by tim on 11/12/2015.
 */
public class Painter {
    private Paint paint;

    public Painter() {
        paint = new Paint();
    }

    public Painter(int color, float strokeWidth, Paint.Style style) {
        paint = new Paint();
        colour(color);
        strokeWidth(strokeWidth);
        paintStyle(style);
    }

    public Painter colour(int color) {
        paint.setColor(color);
        return this;
    }

    public Painter strokeWidth(float strokeWidth) {
        paint.setStrokeWidth(strokeWidth);
        return this;
    }

    public Painter paintStyle(Paint.Style style) {
        paint.setStyle(style);
        return this;
    }

    public Paint getPaint() {
        return paint;
    }
}
