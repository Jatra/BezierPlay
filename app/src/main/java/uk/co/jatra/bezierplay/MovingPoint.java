package uk.co.jatra.bezierplay;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tim on 10/12/2015.
 */
public class MovingPoint extends Point {
    private Point start;
    private Point end;
    private Paint paint;
    private boolean pathDrawEnabled;

    public MovingPoint(Point start, Point end) {
        super(start.x, start.y);
        this.start = start;
        this.end = end;
    }

    public MovingPoint move(float fraction) {
        x = start.x + (int)((end.x - start.x) * fraction);
        y = start.y + (int)((end.y - start.y) * fraction);
        return this;
    }

    public MovingPoint drawPath(Canvas canvas) {
        if (pathDrawEnabled) {
            canvas.drawLine(start.x, start.y, end.x, end.y, paint);
        }
        return this;
    }

    public MovingPoint pathPaint(Paint paint) {
        this.paint = paint;
        return this;
    }

    public MovingPoint enablePathDraw(boolean enable) {
        pathDrawEnabled = enable;
        return this;
    }

}