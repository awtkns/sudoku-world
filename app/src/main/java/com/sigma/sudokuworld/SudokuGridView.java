package com.sigma.sudokuworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SudokuGridView extends View {

    int mViewWidth;
    int mViewHeight;

    int mXOrigin;
    int mYOrigin;
    int mSquareSize;
    int mCellSize;

    Paint mPaint;
    Paint mBoldPaint;
    Paint mCellFilledPaint;

    GameModel mGameModel;

    public SudokuGridView(Context context) {
        this(context, null);
    }

    public SudokuGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(onTouchListener);

        mGameModel = new GameModel();

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);

        mBoldPaint = new Paint();
        mBoldPaint.setStrokeWidth(15);

        mCellFilledPaint = new Paint(Color.YELLOW);
        mCellFilledPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;

        mXOrigin = getPaddingLeft();
        mYOrigin = getPaddingTop();

        int xPad = getPaddingLeft() + getPaddingRight();
        int yPad = getPaddingTop() + getPaddingBottom();
        int maxPad = Math.max(xPad, yPad);

        mSquareSize = Math.min(w - maxPad, h - maxPad);
        mCellSize = mSquareSize / 9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrid(canvas);
    }

    private void drawGrid(Canvas canvas) {

        int[] filledCells = mGameModel.getFilledCells();
        for (int i = 0; i < filledCells.length; i++) {
            int cx = GameModel.cellNumToXPosition(filledCells[i]);
            int cy = GameModel.cellNumToYPosition(filledCells[i]);

            Rect cell = new Rect(
                    mXOrigin + (cx * mCellSize),
                    mYOrigin + (cy * mCellSize),
                    mXOrigin + ((cx + 1) * mCellSize),
                    mYOrigin + ((cy + 1) * mCellSize)
            );
            canvas.drawRect(cell, mCellFilledPaint);
        }

        //Horizontal Lines
        for(int i = 0; i <= 9; i++) {
            if (i % 3 == 0) {
                canvas.drawLine(
                        mXOrigin,mYOrigin + (i * mCellSize),
                        mSquareSize + mXOrigin, mYOrigin + (i * mCellSize),
                        mBoldPaint);
            } else {
                canvas.drawLine(
                        mXOrigin,mYOrigin + (i * mCellSize),
                        mSquareSize + mXOrigin, mYOrigin + (i * mCellSize),
                        mPaint);
            }
        }

        //Vertical Lines
        for(int i = 0; i <= 9; i++) {
            if (i % 3 == 0) {
                canvas.drawLine(
                        mXOrigin + (i * mCellSize), mYOrigin,
                        mXOrigin + (i * mCellSize), mSquareSize + mYOrigin,
                        mBoldPaint);
            } else {
                canvas.drawLine(
                        mXOrigin + (i * mCellSize), mYOrigin,
                        mXOrigin + (i * mCellSize), mSquareSize + mYOrigin,
                        mPaint);
            }
        }
    }

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                x -= mXOrigin;
                y -= mYOrigin;
                x /= mCellSize;
                y /= mCellSize;

                mGameModel.setValue(x, y, 1);

                invalidate();
                performClick();
            }

            return true;
        }
    };

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
