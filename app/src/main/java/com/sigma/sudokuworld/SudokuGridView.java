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

    Rect mGridBoundingRect;

    Paint mGridPaint;
    Paint mBoldPaint;
    Paint mCellFilledPaint;
    Paint mTextPaint;

    Float mTextPaintTextHeight;

    GameModel mGameModel;

    public SudokuGridView(Context context) {
        this(context, null);
    }

    public SudokuGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(onTouchListener);

        mGameModel = new GameModel();

        mGridPaint = new Paint();
        mGridPaint.setColor(getResources().getColor(R.color.gridColour));
        mGridPaint.setStrokeWidth(5);

        mBoldPaint = new Paint();
        mBoldPaint.setColor(getResources().getColor(R.color.gridColour));
        mBoldPaint.setStrokeWidth(15);

        mCellFilledPaint = new Paint();
        mCellFilledPaint.setColor(Color.YELLOW);
        mCellFilledPaint.setAlpha(100);
        mCellFilledPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
    }

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean wasEventHandled = false;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                if (mGridBoundingRect.contains(x, y)) {
                    x -= mXOrigin;
                    y -= mYOrigin;
                    x /= mCellSize;
                    y /= mCellSize;

                    if (mGameModel.getValue(x, y) == 0) {
                        mGameModel.setValue(x, y, 1);
                    } else {
                        mGameModel.setValue(x, y, 0);
                    }

                    invalidate();
                    performClick();
                    wasEventHandled = true;
                }
            }

            return wasEventHandled;
        }
    };

    @Override   //This is for accessibility
    public boolean performClick() {
        return super.performClick();
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
        mCellSize = mSquareSize / GameModel.SUDOKU_SIZE;
        mGridBoundingRect = new Rect(
                mXOrigin,
                mYOrigin,
                mXOrigin + mSquareSize,
                mYOrigin + mSquareSize
        );

        mTextPaint.setTextSize(mCellSize / 2f);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextPaintTextHeight = fontMetrics.descent - fontMetrics.ascent;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCellFill(canvas);
        drawGrid(canvas);
    }

    private void drawGrid(Canvas canvas) {

        //Horizontal Lines
        for(int i = 0; i <= GameModel.SUDOKU_SIZE; i++) {
            if (i % GameModel.SUDOKU_ROOT_SIZE == 0) {
                canvas.drawLine(
                        mXOrigin,mYOrigin + (i * mCellSize),
                        mSquareSize + mXOrigin, mYOrigin + (i * mCellSize),
                        mBoldPaint);
            } else {
                canvas.drawLine(
                        mXOrigin,mYOrigin + (i * mCellSize),
                        mSquareSize + mXOrigin, mYOrigin + (i * mCellSize),
                        mGridPaint);
            }
        }

        //Vertical Lines
        for(int i = 0; i <= GameModel.SUDOKU_SIZE; i++) {
            if (i % GameModel.SUDOKU_ROOT_SIZE == 0) {
                canvas.drawLine(
                        mXOrigin + (i * mCellSize), mYOrigin,
                        mXOrigin + (i * mCellSize), mSquareSize + mYOrigin,
                        mBoldPaint);
            } else {
                canvas.drawLine(
                        mXOrigin + (i * mCellSize), mYOrigin,
                        mXOrigin + (i * mCellSize), mSquareSize + mYOrigin,
                        mGridPaint);
            }
        }
    }

    private void drawCellFill(Canvas canvas) {
        int[] filledCells = mGameModel.getFilledCells();

        for (int cell: filledCells) {
            int cx = GameModel.cellNumToXPosition(cell);
            int cy = GameModel.cellNumToYPosition(cell);

            Rect cellRect = new Rect(
                    mXOrigin + (cx * mCellSize),
                    mYOrigin + (cy * mCellSize),
                    mXOrigin + ((cx + 1) * mCellSize),
                    mYOrigin + ((cy + 1) * mCellSize)
            );
            canvas.drawRect(cellRect, mCellFilledPaint);

            String text = Integer.toString(cell);
            float textWidth = mTextPaint.measureText(text);
            canvas.drawText(text,
                    mXOrigin + (cx * mCellSize) + (mCellSize / 2f) - (textWidth / 2),
                    mYOrigin + (cy * mCellSize) + (mCellSize / 2f) + (mTextPaintTextHeight / 2),
                    mTextPaint);
        }
    }

}
