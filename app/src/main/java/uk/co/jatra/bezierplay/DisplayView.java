package uk.co.jatra.bezierplay;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import javax.inject.Inject;


/**
 * Created by tim on 28/11/2015.
 */
public class DisplayView extends View implements View.OnTouchListener, ValueAnimator.AnimatorUpdateListener {

    @Inject
    PaintFactory paintFactory;
    public static final double TOLERANCE = 75;
    private static final String TAG = DisplayView.class.getSimpleName();
    private final Paint paint = new Paint();
    private Paint directPaint;
    private Paint directPointPaint;
    private Bezier bezier;
    private Point onBezier;
    private Point pointBeingEdited;
    private Point[] line = new Point[2];
    private Point[] controlPoints = new Point[2];
    private Path bezierPath = new Path();
    private Path bezierByPoints = new Path();
    private boolean showCurve;
    private MovingPoint directLine;
    private ProgressListener listener;
    private ValueAnimator fractionAnimator;
    private boolean running;

    public DisplayView(Context context) {
        this(context, null);
    }

    public DisplayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        BezierApplication.getComponent().inject(this);
        setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setPaints();
        initDirectLine();
        initStructure();
        initAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawControlPoints(canvas);
        drawDirectLine(canvas);
        drawBezier(canvas);
        drawEndPoints(canvas);

        if (showCurve) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            canvas.drawPath(bezierByPoints, paint);
        }
        bezier.draw(canvas);

        drawPointBeingEdited(canvas);
    }

    private void drawDirectLine(Canvas canvas) {
        directLine.drawPath(canvas).draw(canvas);
    }

    private void drawBezier(Canvas canvas) {
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        bezierPath.reset();
        bezierPath.moveTo(line[0].x, line[0].y);
        bezierPath.cubicTo(controlPoints[0].x, controlPoints[0].y, controlPoints[1].x, controlPoints[1].y, line[1].x, line[1].y);
        canvas.drawPath(bezierPath, paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(onBezier.x, onBezier.y, 5, paint);
    }

    public void setProgressListener(ProgressListener listener) {
        this.listener = listener;
    }

    public void showMovingControlPoints(boolean showSet1, boolean showSet2, boolean showSet3) {
        bezier.enableOnControlLinePoints(showSet1);
        bezier.enableSecondOrderPoints(showSet2);
        bezier.enableThirdOrderPoints(showSet3);
    }

    public void showMovingControlLines(boolean showSet1, boolean showSet2, boolean showSet3) {
        bezier.enableInterControlLine(showSet1);
        bezier.enableSecondOrderLines(showSet2);
        bezier.enableThirdOrderLines(showSet3);
    }

    public void showCurve(boolean showCurve) {
        this.showCurve = showCurve;
    }

    public void resume() {
        running = true;
        if (fractionAnimator != null) {
            fractionAnimator.start();
        }
    }

    public void pause() {
        running = false;
        if (fractionAnimator != null) {
            fractionAnimator.cancel();
        }
    }

    private void initAnimator() {
        fractionAnimator = ValueAnimator.ofFloat(0, 1);
        fractionAnimator.setInterpolator(new LinearInterpolator());
        fractionAnimator.setDuration(5000);
        fractionAnimator.setRepeatCount(ValueAnimator.INFINITE);
        fractionAnimator.addUpdateListener(this);
        if (running) {
            fractionAnimator.start();
        }
    }

    private void setPaints() {
        directPaint = paintFactory.makePaint(Color.LTGRAY, 3, Paint.Style.STROKE);
        directPointPaint = paintFactory.makePaint(Color.LTGRAY, 5, Paint.Style.FILL);
    }

    private void drawEndPoints(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(line[0].x, line[0].y, 10, paint);
        canvas.drawCircle(line[1].x, line[1].y, 10, paint);
    }

    private void drawControlPoints(Canvas canvas) {
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(controlPoints[0].x, controlPoints[0].y, 10, paint);
        canvas.drawCircle(controlPoints[1].x, controlPoints[1].y, 10, paint);
    }

    private void drawPointBeingEdited(Canvas canvas) {
        if (pointBeingEdited != null) {
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3f);
            canvas.drawCircle(pointBeingEdited.x, pointBeingEdited.y, 20, paint);
        }
    }

    private void initDirectLine() {
        int width = getWidth();
        int height = getHeight();
        line[0] = new Point(width / 4, 3 * height / 4);
        line[1] = new Point(3 * width / 4, 3 * height / 4);
        directLine = new MovingPoint(line[0], line[1]);
        directLine.enablePathDraw(true).pathPaint(directPaint).enableDraw(true).pointPaint(directPointPaint);
    }

    private void initStructure() {
        controlPoints[0] = new Point(line[0].x, line[0].y / 3);
        controlPoints[1] = new Point(line[1].x, line[1].y / 3);
        bezier = new Bezier(line[0], line[1], controlPoints[0], controlPoints[1]);
        Context context = getContext();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointBeingEdited = findClose((int) event.getX(), (int) event.getY());
                return true;
            case MotionEvent.ACTION_UP:
                pointBeingEdited = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointBeingEdited != null) {
                    pointBeingEdited.x = (int) event.getX();
                    pointBeingEdited.y = (int) event.getY();
                }
                break;
        }
        invalidate();
        return true;
    }

    private Point findClose(int x, int y) {
        for (Point point : new Point[]{controlPoints[0], controlPoints[1], line[0], line[1]}) {
            if (hit(point, x, y)) {
                return point;
            }
        }
        return null;
    }

    private boolean hit(Point target, int x, int y) {
        int dx = x - target.x;
        int dy = y - target.y;
        return Math.sqrt(dx * dx + dy * dy) < TOLERANCE;

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float animationFraction = animation.getAnimatedFraction();
        update(animationFraction);
    }

    private void update(float animationFraction) {

        Point[] points = bezier.update(animationFraction);
        directLine.move(animationFraction);

        if (animationFraction == 0) {
            bezierByPoints.reset();
            bezierByPoints.moveTo(line[0].x, line[0].y);
        }
        onBezier = points[5];
        bezierByPoints.lineTo(onBezier.x, onBezier.y);

        if (listener != null) {
            listener.fractionProgress(animationFraction);
        }
        invalidate();
    }

    interface ProgressListener {
        void fractionProgress(float fraction);
    }
}
