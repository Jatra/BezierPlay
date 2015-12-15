package uk.co.jatra.bezierplay;

import android.graphics.Paint;

/**
 * Created by tim on 12/12/2015.
 */
public class PaintFactory {
    public Paint makePaint(int color, float strokeWidth, Paint.Style style) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        return paint;
    }
}
