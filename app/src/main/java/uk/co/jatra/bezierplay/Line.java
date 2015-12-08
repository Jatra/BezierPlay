package uk.co.jatra.bezierplay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by tim on 03/12/2015.
 */
public class Line implements Cloneable {
    private static final float DEFAULT_RADIUS = 5f;
    private Point start;
    private Point end;
    private Point temp;
    private float pointRadius = DEFAULT_RADIUS;
    private boolean drawingEnabled = true;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
        temp = new Point();
    }

    public Line start(Point start) {
        this.start.set(start.x, start.y);
        return this;
    }

    public Line end(Point end) {
        this.end.set(end.x, end.y);
        return this;
    }

    public Line draw(Canvas canvas, Paint paint) {
        if (drawingEnabled) {
            canvas.drawLine(start.x, start.y, end.x, end.y, paint);
        }
        return this;
    }

    public Line moveAlong(Point along, float fraction) {
        along.x = start.x + (int)((end.x - start.x) * fraction);
        along.y = start.y + (int)((end.y - start.y) * fraction);
        return this;
    }

    public Line drawPointOn(Canvas canvas, float fraction, Paint paint) {
        if (drawingEnabled) {
            moveAlong(temp, fraction);
            canvas.drawCircle(temp.x, temp.y, pointRadius, paint);
        }
        return this;
    }

    public Line setPointRadius(float radius) {
        this.pointRadius = radius;
        return this;
    }

    public Line when(boolean drawingEnabled) {
        Line ret = (Line)clone();
        ret.drawingEnabled = drawingEnabled;
        return ret;
    }

    public Line always() {
        return when(true);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (Float.compare(line.pointRadius, pointRadius) != 0) return false;
        if (drawingEnabled != line.drawingEnabled) return false;
        if (!start.equals(line.start)) return false;
        return end.equals(line.end);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + temp.hashCode();
        result = 31 * result + (pointRadius != +0.0f ? Float.floatToIntBits(pointRadius) : 0);
        result = 31 * result + (drawingEnabled ? 1 : 0);
        return result;
    }
}
