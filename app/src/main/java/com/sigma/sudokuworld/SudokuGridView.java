package com.sigma.sudokuworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SudokuGridView extends View {

    int mViewWidth;
    int mViewHeight;

    Paint mPaint;

    public SudokuGridView(Context context) {
        this(context, null);
        init();
    }

    public SudokuGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //TODO: Account for padding:
        //https://developer.android.com/training/custom-views/custom-drawing#layouteevent
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(0, 0, mViewWidth, mPaint);
    }

    private void init() {
        mPaint = new Paint(R.color.colorPrimary);

    }
}
