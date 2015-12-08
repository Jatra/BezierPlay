package uk.co.jatra.bezierplay;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by tim on 04/12/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class LineTest {

    private static final Point START = new TestPoint(100, 100);
    private static final Point END = new TestPoint(200, 200);
    private static final Point MOVED_END = new TestPoint(300, 300);
    private static final Point MOVED_START = new TestPoint(50, 50);;

    @Mock
    Canvas mockCanvas;
    Paint paint;
    private Line line;

    @Before
    public void setUp() {
        paint = new Paint();
        line = new Line(START, END);
    }

    @Test
    public void shouldDrawLine() {
        line.draw(mockCanvas, paint);
        verify(mockCanvas).drawLine(START.x, START.y, END.x, END.y, paint);
    }

    @Test
    public void shouldNotDrawLineWhenNotEnabled() {
        line.when(false).draw(mockCanvas, paint);
        verify(mockCanvas, never()).drawLine(START.x, START.y, END.x, END.y, paint);
    }

    @Test
    public void shouldDrawLineWhenEnabled() {
        line.when(true).draw(mockCanvas, paint);
        verify(mockCanvas).drawLine(START.x, START.y, END.x, END.y, paint);
    }

    @Test
    public void shouldDrawLineWhenAlways() {
        line.always().draw(mockCanvas, paint);
        verify(mockCanvas).drawLine(START.x, START.y, END.x, END.y, paint);
    }

    @Test
    public void shouldMoveEnd() {
        line.end(MOVED_END).draw(mockCanvas, paint);
        verify(mockCanvas).drawLine(START.x, START.y, MOVED_END.x, MOVED_END.y, paint);
    }

    @Test
    public void shouldMoveStart() {
        line.start(MOVED_START).draw(mockCanvas, paint);
        verify(mockCanvas).drawLine(MOVED_START.x, MOVED_START.y, END.x, END.y, paint);
    }

    @Test
    public void shouldDrawPointOn() {
        line.drawPointOn(mockCanvas, 0f, paint);
        verify(mockCanvas).drawCircle(START.x, START.y, 5, paint);
    }

    @Test
    public void shouldDrawPointOnEnd() {
        line.drawPointOn(mockCanvas, 1f, paint);
        verify(mockCanvas).drawCircle(END.x, END.y, 5, paint);
    }

    @Test
    public void shouldDrawPointOnWhenEnabled() {
        line.when(true).drawPointOn(mockCanvas, 0f, paint);
        verify(mockCanvas).drawCircle(START.x, START.y, 5, paint);
    }

    @Test
    public void shouldDrawPointOnWhenAlways() {
        line.always().drawPointOn(mockCanvas, 0f, paint);
        verify(mockCanvas).drawCircle(START.x, START.y, 5, paint);
    }

    @Test
    public void shouldNotDrawPointOnWhenNotEnabled() {
        line.when(false).drawPointOn(mockCanvas, 0f, paint);
        verify(mockCanvas, never()).drawCircle(START.x, START.y, 5, paint);
    }

    @Test
    public void shouldSetPointRadius() {
        line.setPointRadius(200).drawPointOn(mockCanvas, 0, paint);
        verify(mockCanvas).drawCircle(START.x, START.y, 200, paint);
    }

    @Test
    public void shouldMovePointAlong() {
        Point moving = new TestPoint();
        for (float f=0; f<= 1.0f; f += 0.01) {
            line.moveAlong(moving, f);
            Point expected = interpolate(START, END, f);
            assertThat(moving).isEqualTo(expected);
        }
    }

    @Test
    public void shouldCloneAllFields() throws CloneNotSupportedException {
        Line clone = (Line)line.clone();
        assertThat(clone).isEqualTo(line);
    }


    private TestPoint interpolate(Point start, Point end, float fraction) {
        int x = start.x + (int)((end.x-start.x)*fraction);
        int y = start.y + (int)((end.y-start.y)*fraction);
        return new TestPoint(x, y);
    }


    @SuppressLint("ParcelCreator")
    public static class TestPoint extends Point {
        public TestPoint() {
        }

        public TestPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void set(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public TestPoint(Point src) {
            this.x = src.x;
            this.y = src.y;
        }

        public boolean equals(Object other) {
            if (other == null || !(other instanceof TestPoint)) {
                return false;
            }
            TestPoint that = (TestPoint)other;
            return that.x == this.x && that.y == this.y;
        }
    }
}