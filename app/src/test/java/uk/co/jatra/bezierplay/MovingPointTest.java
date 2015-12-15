package uk.co.jatra.bezierplay;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by tim on 12/12/2015.
 */
public class MovingPointTest {
    private static final int STARTX = 101;
    private static final int STARTY = 202;
    private static final int ENDX = 666;
    private static final int ENDY = 667;

    private Point start;
    private Point end;
    private MovingPoint movingPoint;

    private Paint paint;
    private Canvas canvas;

    @Before
    public void setUp() {
        start = new Point(STARTX, STARTY);
        end = new Point(ENDX, ENDY);
        movingPoint = new MovingPoint(start, end);
        paint = mock(Paint.class);
        canvas = mock(Canvas.class);
    }

    @Test
    public void shouldSetAtStart() {
        assertThat(movingPoint.x).isEqualTo(STARTX);
        assertThat(movingPoint.y).isEqualTo(STARTY);
    }

    @Test
    public void shouldMoveToEnd() {
        movingPoint.move(1);
        assertThat(movingPoint.x).isEqualTo(ENDX);
        assertThat(movingPoint.y).isEqualTo(ENDY);
    }

    @Test
    public void shouldMoveHalfWay() {
        movingPoint.move(0.5f);
        assertThat(movingPoint.x).isEqualTo(STARTX + (ENDX-STARTX)/2);
        assertThat(movingPoint.y).isEqualTo(STARTY + (ENDY-STARTY)/2);
    }

    @Test
    public void shouldNotHaveDrawingEnabledByDefault() {
        movingPoint.drawPath(canvas);
        verifyZeroInteractions(canvas);
    }

    @Test
    public void shouldNotDrawWhenDisabled() {
        movingPoint.enablePathDraw(true);
        movingPoint.enablePathDraw(false);
        movingPoint.drawPath(canvas);
        verifyZeroInteractions(canvas);
    }

    @Test
    public void shouldDrawWhenEnabled() {
        movingPoint.enablePathDraw(true);
        movingPoint.pathPaint(paint);
        movingPoint.drawPath(canvas);
        verify(canvas).drawLine(STARTX, STARTY, ENDX, ENDY, paint);
    }
}