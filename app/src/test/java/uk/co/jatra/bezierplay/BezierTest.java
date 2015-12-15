package uk.co.jatra.bezierplay;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by tim on 12/12/2015.
 */
public class BezierTest {
    private static int START_X = 100;
    private static int START_Y = 200;
    private static int END_X = 300;
    private static int END_Y = 200;
    public static final int CONTROL1_X = 100;
    public static final int CONTROL1_Y = 100;
    public static final int CONTROL2_X = 300;
    public static final int CONTROL2_y = 100;

    private Canvas canvas;
    private Point lineStart = new Point(START_X, START_Y);
    private Point lineEnd = new Point(END_X, END_Y);
    private Point controlOne = new Point(CONTROL1_X, CONTROL1_Y);
    private Point controlTwo = new Point(CONTROL2_X, CONTROL2_y);
    private Bezier bezier;

    @Before
    public void setUp() {
        AppModule.setPaintFactoryClass(TestPaintFactory.class);
        canvas = mock(Canvas.class);
        bezier = new Bezier(lineStart, lineEnd, controlOne, controlTwo);
    }

    @Test
    public void shouldHaveBezierPointAtStart() {
        Point[] updated = bezier.update(0);
        assertThat(updated).hasSize(6);
        assertThat(updated[5].x).isEqualTo(START_X);
        assertThat(updated[5].y).isEqualTo(START_Y);
    }

    @Test
    public void shouldHaveBezierPointAtEnd() {
        Point[] updated = bezier.update(1);
        assertThat(updated).hasSize(6);
        assertThat(updated[5].x).isEqualTo(END_X);
        assertThat(updated[5].y).isEqualTo(END_Y);
    }

    public static class TestPaintFactory extends PaintFactory {
        @Override
        public Paint makePaint(int color, float strokeWidth, Paint.Style style) {
            return new Paint();
        }
    }
}