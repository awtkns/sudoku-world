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
    int mSquareSize;
    float mCellSize;

    Paint mPaint;
    Paint mBoldPaint;

    public SudokuGridView(Context context) {
        this(context, null);
    }

    public SudokuGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint(R.color.colorPrimary);
        mPaint.setStrokeWidth(5);

        mBoldPaint = new Paint(R.color.colorPrimary);
        mBoldPaint.setStrokeWidth(15);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //TODO: Account for padding:
        //https://developer.android.com/training/custom-views/custom-drawing#layouteevent
        mViewWidth = w;
        mViewHeight = h;
        mSquareSize = Math.min(mViewWidth, mViewHeight);
        mCellSize = ((float) mSquareSize) / 9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Horizontal Lines
        for(int i = 0; i <= 9; i++) {
            if (i % 3 == 0) {
                canvas.drawLine(0, i * mCellSize, mSquareSize, i * mCellSize, mBoldPaint);
            } else {
                canvas.drawLine(0, i * mCellSize, mSquareSize, i * mCellSize, mPaint);
            }
        }

        //Vertical Lines
        for(int i = 0; i <= 9; i++) {
            if (i % 3 == 0) {
                canvas.drawLine(i * mCellSize, 0, i * mCellSize, mSquareSize, mBoldPaint);
            } else {
                canvas.drawLine(i * mCellSize, 0, i * mCellSize, mSquareSize, mPaint);
            }
        }
    }


}
