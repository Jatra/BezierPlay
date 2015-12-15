package uk.co.jatra.bezierplay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import javax.inject.Inject;

/**
 * Created by tim on 10/12/2015.
 */
public class Bezier {
    @Inject
    PaintFactory paintFactory;

    private MovingPoint[] movingPoints;
    private int numPoints;

    public Bezier(Point lineStart, Point lineEnd, Point controlOne, Point controlTwo) {

        BezierApplication.getComponent().inject(this);

        Paint pointPaint = paintFactory.makePaint(Color.MAGENTA, 5, Paint.Style.FILL);
        Paint pointPaint1 = paintFactory.makePaint(Color.RED, 5, Paint.Style.FILL);
        Paint pointPaint2 = paintFactory.makePaint(Color.BLACK, 5, Paint.Style.FILL);
        Paint linePaint = paintFactory.makePaint(Color.CYAN, 3, Paint.Style.STROKE);
        Paint linePaint1 = paintFactory.makePaint(Color.MAGENTA, 3, Paint.Style.STROKE);
        Paint linePaint2 = paintFactory.makePaint(Color.RED, 3, Paint.Style.STROKE);

        movingPoints = new MovingPoint[6];
        numPoints = movingPoints.length;

        movingPoints[0] = new MovingPoint(lineStart, controlOne);
        movingPoints[1] = new MovingPoint(controlOne, controlTwo);
        movingPoints[2] = new MovingPoint(controlTwo, lineEnd);

        movingPoints[3] = new MovingPoint(movingPoints[0], movingPoints[1]);
        movingPoints[4] = new MovingPoint(movingPoints[1], movingPoints[2]);

        movingPoints[5] = new MovingPoint(movingPoints[3], movingPoints[4]);

        movingPoints[0].pathPaint(linePaint).enablePathDraw(true).pointPaint(pointPaint);
        movingPoints[1].pathPaint(linePaint).pointPaint(pointPaint);
        movingPoints[2].pathPaint(linePaint).enablePathDraw(true).pointPaint(pointPaint);
        movingPoints[3].pathPaint(linePaint1).pointPaint(pointPaint1);
        movingPoints[4].pathPaint(linePaint1).pointPaint(pointPaint1);
        movingPoints[5].pathPaint(linePaint2).pointPaint(pointPaint2);
    }

    public Point[] update(float fraction) {
        for (int i=0; i<numPoints; i++) {
            movingPoints[i].move(fraction);
        }
        return movingPoints;
    }

    public void draw(Canvas canvas) {
        for (int i=0; i<numPoints; i++) {
            movingPoints[i].draw(canvas);
            movingPoints[i].drawPath(canvas);
        }
    }

    public void enableInterControlLine(boolean enable) {
        movingPoints[1].enablePathDraw(enable);
    }

    public void enableOnControlLinePoints(boolean enable) {
        movingPoints[0].enableDraw(enable);
        movingPoints[1].enableDraw(enable);
        movingPoints[2].enableDraw(enable);
    }

    public void enableSecondOrderLines(boolean enable) {
        movingPoints[3].enablePathDraw(enable);
        movingPoints[4].enablePathDraw(enable);
    }

    public void enableSecondOrderPoints(boolean enable) {
        movingPoints[3].enableDraw(enable);
        movingPoints[4].enableDraw(enable);
    }

    public void enableThirdOrderLines(boolean enable) {
        movingPoints[5].enablePathDraw(enable);
    }

    public void enableThirdOrderPoints(boolean enable) {
        movingPoints[5].enableDraw(enable);
    }
}
