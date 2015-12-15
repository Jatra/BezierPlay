package uk.co.jatra.bezierplay;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tim on 10/12/2015.
 */
public class Point {
    int x;
    int y;
    private Paint paint;
    private boolean drawEnabled;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point x(int x) {
        this.x = x;
        return this;
    }

    public Point y(int y) {
        this.y = y;
        return this;
    }

    public Point pointPaint(Paint paint) {
        this.paint = paint;
        return this;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public Point draw(Canvas canvas) {
        if (drawEnabled) {
            canvas.drawCircle(x, y, 8, paint);
        }
        return this;
    }

    public Point enableDraw(boolean enable) {
        drawEnabled = enable;
        return this;
    }
}
