package com.sigma.sudokuworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class SudokuGridView extends View {

    static final char LOCKED_FLAG = '~';

    int mSudokuRootSize;
    int mSudokuSize;

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
    Paint mLockedCellFillPaint;
    Paint mTextPaint;

    Float mTextPaintTextHeight;

    String[] mCellLabels;
    int mHighlightedCell = -1;

    public SudokuGridView(Context context) {
        this(context, null);
    }

    public SudokuGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mCellLabels = new String[0];

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

        mLockedCellFillPaint = new Paint();
        mLockedCellFillPaint.setColor(Color.BLACK);
        mLockedCellFillPaint.setAlpha(50);
        mLockedCellFillPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
    }

    public void setSudokuRootSize(int rootSize) {
        mSudokuRootSize = rootSize;
        mSudokuSize = mSudokuRootSize * mSudokuRootSize;
        mCellLabels = new String[mSudokuSize * mSudokuSize];
        Arrays.fill(mCellLabels, "");
    }

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
        mCellSize = mSquareSize / mSudokuSize;
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCellFill(canvas);
        drawGrid(canvas);
    }

    private void drawGrid(Canvas canvas) {

        //Horizontal Lines
        for(int i = 0; i <= mSudokuSize; i++) {
            if (i % mSudokuRootSize == 0) {
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
        for(int i = 0; i <= mSudokuSize; i++) {
            if (i % mSudokuRootSize == 0) {
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
        for (int i = 0; i < mCellLabels.length; i++) {
            int cx = i % mSudokuSize;
            int cy = i / mSudokuSize;

             if (i == mHighlightedCell) {
                Rect cellRect = new Rect(
                        mXOrigin + (cx * mCellSize),
                        mYOrigin + (cy * mCellSize),
                        mXOrigin + ((cx + 1) * mCellSize),
                        mYOrigin + ((cy + 1) * mCellSize)
                );

                canvas.drawRect(cellRect, mCellFilledPaint);
            }

            String label = mCellLabels[i];
            if (!label.equals("")) {

                //Locked colour
                if (label.charAt(0) == LOCKED_FLAG) {

                    Rect cellRect = new Rect(
                            mXOrigin + (cx * mCellSize),
                            mYOrigin + (cy * mCellSize),
                            mXOrigin + ((cx + 1) * mCellSize),
                            mYOrigin + ((cy + 1) * mCellSize)
                    );

                    canvas.drawRect(cellRect, mLockedCellFillPaint);
                    label = label.substring(1);
                }

                float textWidth = mTextPaint.measureText(label);
                float defaultTextSize = mTextPaint.getTextSize();
                while (textWidth > mCellSize) {
                    mTextPaint.setTextSize(mTextPaint.getTextSize() - 1);
                    textWidth = mTextPaint.measureText(label);
                }

                canvas.drawText(label,
                        mXOrigin + (cx * mCellSize) + (mCellSize / 2f) - (textWidth / 2),
                        mYOrigin + (cy * mCellSize) + (mCellSize / 2f) + (mTextPaintTextHeight / 2),
                        mTextPaint);

                mTextPaint.setTextSize(defaultTextSize);
            }
        }
    }

    public Rect getGridBounds() {
        return mGridBoundingRect;
    }

    public int getCellNumberFromCoordinates(int x, int y) {
        x -= mXOrigin;
        y -= mYOrigin;
        x /= mCellSize;
        y /= mCellSize;
        return (y * mSudokuSize) + x;
    }

    public void setCellLabel(int cellNumber, String string) {
        mCellLabels[cellNumber] = string;
    }

    public String getCellLabel(int cellNumber) {
        return mCellLabels[cellNumber];
    }

    public int getHighlightedCell() {
        return mHighlightedCell;
    }

    public void setHighlightedCell(int cellNumber) {
        mHighlightedCell = cellNumber;
    }

    public void clearHighlightedCell() {
        mHighlightedCell = -1;
    }
}
