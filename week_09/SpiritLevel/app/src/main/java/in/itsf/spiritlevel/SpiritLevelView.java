package in.itsf.spiritlevel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * TODO: document your custom view class.
 */
public class SpiritLevelView extends View {
    private static final float BUBBLE_SIZE = 100;

    private float width;
    private float height;
    private final Paint paint;
    private final Point center = new Point();
    private final Point bubble = new Point();

    public SpiritLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Get attributes from XML
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SpiritLevelView);

        int color = a.getColor(
            R.styleable.SpiritLevelView_color,
            getResources().getColor(R.color.purple_200)
        );

        paint = new Paint(color);

        a.recycle();
    }

    @Override
    public void onSizeChanged(int newW, int newH, int oldW, int oldH) {
        super.onSizeChanged(newW, newH, oldW, oldH);
        center.x = newW / 2f;
        center.y = newH / 2f;
        width = newW;
        height = newH;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(bubble.x, bubble.y, BUBBLE_SIZE, paint);
    }

    public void setBubble(float x, float y) {
        bubble.x = center.x + x / 9.81f * width;
        bubble.y = center.y - y / 9.81f * height; // The guide uses + but it inverts, so I use - which gives the expected result
        Log.i("SpiritLevelView", bubble.toString());

        invalidate();
    }
}