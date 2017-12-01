package net.wizapps.fgallery.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.tool.Constants;

public class SquareLayout extends FrameLayout {

    private int aspectWidth = Constants.ONE;
    private int aspectHeight = Constants.ONE;

    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extractCustomAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        extractCustomAttrs(context, attrs);
    }

    private void extractCustomAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray a = context.getResources().obtainAttributes(attrs, R.styleable.SquareLayout);
        try {
            aspectWidth = a.getInteger(R.styleable.SquareLayout_aspect_width, Constants.ONE);
            aspectHeight = a.getInteger(R.styleable.SquareLayout_aspect_height, Constants.ONE);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int newSpecWidth = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        int newH = Math.round(((float) getMeasuredWidth()) * aspectHeight / aspectWidth);
        int newSpecHeight = MeasureSpec.makeMeasureSpec(newH, MeasureSpec.EXACTLY);

        super.onMeasure(newSpecWidth, newSpecHeight);
    }
}