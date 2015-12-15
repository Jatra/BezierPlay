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
public class PointTest {

    private static final int X = 101;
    private static final int Y = 666;
    private Canvas canvas;
    private Point point;
    private Paint paint;

    @Before
    public void setUp() {
        point = new Point(X, Y);
        canvas = mock(Canvas.class);
        paint = mock(Paint.class);
    }

    @Test
    public void shouldInitialise() {
        assertThat(point.getX()).isEqualTo(X);
        assertThat(point.x).isEqualTo(X);
        assertThat(point.getY()).isEqualTo(Y);
        assertThat(point.y).isEqualTo(Y);
    }

    @Test
    public void shouldUpdateX() {
        point.x(X*2);
        assertThat(point.getX()).isEqualTo(X*2);
    }

    @Test
    public void shouldUpdateY() {
        point.y(Y*2);
        assertThat(point.getY()).isEqualTo(Y*2);
    }

    @Test
    public void shouldNotHaveDrawEnabled() {
        point.draw(canvas);
        verifyZeroInteractions(canvas);
    }

    @Test
    public void shouldDrawWhenEnabled() {
        point.enableDraw(true);
        point.pointPaint(paint);
        point.draw(canvas);
        verify(canvas).drawCircle(X, Y, 8, paint);
    }

    @Test
    public void shouldNotDrawWhenDisabled() {
        point.enableDraw(false);
        point.draw(canvas);
        verifyZeroInteractions(canvas);
    }

    @Test
    public void shouldBeEqualIfXandYAreEqual() {
        Point point2 = new Point(X, Y);
        assertThat(point).isEqualTo(point2);
    }

    @Test
    public void hashShouldChange() {
        Point point2 = new Point(X+1, Y);
        Point point3 = new Point(X, Y+1);
        assertThat(point).isNotEqualTo(point2);
        assertThat(point).isNotEqualTo(point3);
        assertThat(point2).isNotEqualTo(point3);
    }
}