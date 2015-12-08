package uk.co.jatra.bezierplay;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * Created by tim on 28/11/2015.
 */
public class DisplayView extends View implements View.OnTouchListener, ValueAnimator.AnimatorUpdateListener {


    interface ProgressListener {
        void fractionProgress(float fraction);
    }

    private static final String TAG = DisplayView.class.getSimpleName();
    public static final double TOLERANCE = 75;
    private final Paint paint = new Paint();
    private final Paint directLinePaint = new Paint();
    private final Paint pathPaint = new Paint();
    private final Paint dashedPaint = new Paint();
    private final Paint controlPointPaint = new Paint();
    private final Paint joinedPaint = new Paint();
    private final Paint pointPaint = new Paint();

    private Point pointBeingEdited;

    private Point[] line = {new Point(), new Point()};
    private Point[] controls = {new Point(), new Point()};
    private Point onFirst = new Point();
    private Point onSecond = new Point();
    private Point onInter = new Point();
    private Point tangentStart = new Point();
    private Point tangentEnd = new Point();
    private Point bezierPoint = new Point();

    private Path bezier = new Path();
    private Path bezierByPoints = new Path();

    private Line directLine;

    private Line interControlsLine;
    private Line lineToFirstControlPoint;
    private Line lineToSecondControlPoint;
    private Line tangentLine;

    private Line firstToInter;
    private Line interToSecond;

    private float animationFraction;

    private boolean showControlLine1;
    private boolean showControlLine2;
    private boolean showControlLine3;
    private boolean showingMovingControlPoints1;
    private boolean showingMovingControlPoints2;
    private boolean showingMovingControlPoints3 = true;

    private ProgressListener listener;

    public DisplayView(Context context) {
        this(context, null);
    }

    public DisplayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setPaints();
        setLine();
        initControls();
        createBezier();
        initAnimator();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawEndPoints(canvas);

        drawDirectLine(canvas);

        drawControls(canvas);

        canvas.drawPath(bezierByPoints, pathPaint);

        drawPointBeingEdited(canvas);
    }

    private void drawDirectLine(Canvas canvas) {
        directLine
                .always().draw(canvas, directLinePaint)
                .always().drawPointOn(canvas, animationFraction, directLinePaint);
    }

    public void setProgressListener(ProgressListener listener) {
        this.listener = listener;
    }

    public void showMovingControlPoints(boolean showSet1, boolean showSet2, boolean showSet3) {
        this.showingMovingControlPoints1 = showSet1;
        this.showingMovingControlPoints2 = showSet2;
        this.showingMovingControlPoints3 = showSet3;
    }

    public void showMovingControlLines(boolean showSet1, boolean showSet2, boolean showSet3) {
        this.showControlLine1 = showSet1;
        this.showControlLine2 = showSet2;
        this.showControlLine3 = showSet3;
    }


    private void initAnimator() {
        ValueAnimator fractionAnimator = ValueAnimator.ofFloat(0, 1);
        fractionAnimator.setInterpolator(new LinearInterpolator());
        fractionAnimator.setDuration(5000);
        fractionAnimator.setRepeatCount(ValueAnimator.INFINITE);
        fractionAnimator.addUpdateListener(this);
        fractionAnimator.start();
    }

    private void setPaints() {
        pathPaint.setColor(Color.DKGRAY);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(3f);

        directLinePaint.setColor(Color.LTGRAY);
        directLinePaint.setStyle(Paint.Style.STROKE);
        directLinePaint.setStrokeWidth(3f);

        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setStrokeWidth(5f);

        dashedPaint.setStrokeWidth(3f);
        dashedPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        dashedPaint.setStyle(Paint.Style.STROKE);
        dashedPaint.setColor(Color.CYAN);

        controlPointPaint.setStyle(Paint.Style.FILL);
        controlPointPaint.setColor(Color.MAGENTA);

        joinedPaint.setColor(Color.MAGENTA);
        joinedPaint.setStyle(Paint.Style.STROKE);
        joinedPaint.setStrokeWidth(3f);
    }

    private void drawEndPoints(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(line[0].x, line[0].y, 10, paint);
        canvas.drawCircle(line[1].x, line[1].y, 10, paint);
    }

    private void drawControls(Canvas canvas) {
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(controls[0].x, controls[0].y, 10, paint);
        canvas.drawCircle(controls[1].x, controls[1].y, 10, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        drawLine(lineToFirstControlPoint, canvas, showingMovingControlPoints1 ? DrawWhat.LINE_AND_POINT : DrawWhat.LINE_ONLY, paint, controlPointPaint, animationFraction);
//        lineToFirstControlPoint
//                .always()
//                .draw(canvas, paint)
//                .when(showingMovingControlPoints1)
//                .drawPointOn(canvas, animationFraction, controlPointPaint);
        drawLine(lineToSecondControlPoint, canvas, showingMovingControlPoints1 ? DrawWhat.LINE_AND_POINT : DrawWhat.LINE_ONLY, paint, controlPointPaint, animationFraction);
//        lineToSecondControlPoint
//                .always()
//                .draw(canvas, paint)
//                .when(showingMovingControlPoints1)
//                .drawPointOn(canvas, animationFraction, controlPointPaint);
        interControlsLine
                .when(showControlLine1)
                .draw(canvas, paint)
                .when(showingMovingControlPoints1)
                .drawPointOn(canvas, animationFraction, controlPointPaint);

        firstToInter
                .when(showControlLine2)
                .draw(canvas, joinedPaint)
                .when(showingMovingControlPoints2)
                .drawPointOn(canvas, animationFraction, pointPaint);
        interToSecond
                .when(showControlLine2)
                .draw(canvas, joinedPaint)
                .when(showingMovingControlPoints2)
                .drawPointOn(canvas, animationFraction, pointPaint);

        tangentLine
                .when(showControlLine3)
                .draw(canvas, pointPaint)
                .when(showingMovingControlPoints3)
                .drawPointOn(canvas, animationFraction, pathPaint);

        tangentLine.moveAlong(bezierPoint, animationFraction);
        bezierByPoints.lineTo(bezierPoint.x, bezierPoint.y);

    }

    private void drawLine(Line line, Canvas canvas, DrawWhat what, Paint linePaint, Paint pointPaint, float fraction) {
        line
            .when(what == DrawWhat.LINE_AND_POINT || what == DrawWhat.LINE_ONLY)
            .draw(canvas, linePaint)
            .when(what == DrawWhat.LINE_AND_POINT || what == DrawWhat.POINT_ONLY)
            .drawPointOn(canvas, fraction, pointPaint);
    }

    private void drawPointBeingEdited(Canvas canvas) {
        if (pointBeingEdited != null) {
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3f);
            canvas.drawCircle(pointBeingEdited.x, pointBeingEdited.y, 20, paint);
        }
    }

    private void setLine() {
        int width = getWidth();
        int height = getHeight();
        line[0].x = width / 4;
        line[0].y = 3 * height / 4;
        line[1].x = 3 * width / 4;
        line[1].y = 3 * height / 4;
        directLine = new Line(line[0], line[1]);
    }

    private void initControls() {
        int width = getWidth();
        int height = getHeight();
        controls[0].x = width / 4;
        controls[0].y = height / 4;
        controls[1].x = 3 * width / 4;
        controls[1].y = height / 4;
        lineToFirstControlPoint = new Line(line[0], controls[0]);
        lineToSecondControlPoint = new Line(controls[1], line[1]);
        interControlsLine = new Line(controls[0], controls[1]);
        tangentLine = new Line(line[0], controls[1]).setPointRadius(10);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointBeingEdited = findClose(event.getX(), event.getY());
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
        createBezier();
        updateControlLines();
        invalidate();
        return true;
    }

    private void createBezier() {
        bezier.reset();
        bezier.moveTo(line[0].x, line[0].y);
        bezier.cubicTo(controls[0].x, controls[0].y, controls[1].x, controls[1].y, line[1].x, line[1].y);
    }

    private void updateControlLines() {
        lineToFirstControlPoint
                .start(line[0])
                .end(controls[0]);
        lineToSecondControlPoint
                .start(controls[1])
                .end(line[1]);
        interControlsLine
                .start(controls[0])
                .end(controls[1]);
    }

    private Point findClose(double x, double y) {
        Point target = new Point((int) x, (int) y);
        if (hit(target, controls[0])) {
            return controls[0];
        }
        if (hit(target, controls[1])) {
            return controls[1];
        }
        if (hit(target, line[0])) {
            return line[0];
        }
        if (hit(target, line[1])) {
            return line[1];
        }
        return null;
    }

    private boolean hit(Point target, Point control) {
        int dx = control.x - target.x;
        int dy = control.y - target.y;
        return Math.sqrt(dx * dx + dy * dy) < TOLERANCE;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        animationFraction = animation.getAnimatedFraction();
        update(animationFraction);
    }

    private void update(float animationFraction) {

        if (animationFraction == 0) {
            bezierByPoints.reset();
            bezierByPoints.moveTo(line[0].x, line[0].y);
        }

        lineToFirstControlPoint.moveAlong(onFirst, animationFraction);
        lineToSecondControlPoint.moveAlong(onSecond, animationFraction);
        interControlsLine.moveAlong(onInter, animationFraction);

        firstToInter = new Line(onFirst, onInter);
        interToSecond = new Line(onInter, onSecond);

        firstToInter.moveAlong(tangentStart, animationFraction);
        interToSecond.moveAlong(tangentEnd, animationFraction);

        tangentLine = new Line(tangentStart, tangentEnd);

        if (listener != null) {
            listener.fractionProgress(animationFraction);
        }
        invalidate();
    }
}
