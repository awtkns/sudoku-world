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
    private static final int SUDOKU_SIZE = 9;
    private static final int SUDOKU_ROOT_SIZE = 3;

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

        mCellLabels = new String[SUDOKU_SIZE*SUDOKU_SIZE];
        Arrays.fill(mCellLabels, "");

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

    @Override   //This is for accessibility
    public boolean performClick() {
        return super.performClick();
    }

    /**
     *  Calculates the correct size for all objects to be drawn within the view.
     *  Gets called when the view size changes.
     */
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
        mCellSize = mSquareSize / SUDOKU_SIZE;
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

    /**
     * Tell the layout manager the perfered size for the view.
     * Makes the view square.
     */
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

    /**
     * Draws the the sudoku grid
     * @param canvas canvas
     */
    private void drawGrid(Canvas canvas) {

        //Horizontal Lines
        for(int i = 0; i <= SUDOKU_SIZE; i++) {

            //Either bolded or unbolded
            if (i % SUDOKU_ROOT_SIZE == 0) {
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
        for(int i = 0; i <= SUDOKU_SIZE; i++) {

            //Either bolded or unbolded
            if (i % SUDOKU_ROOT_SIZE == 0) {
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

    /**
     * Draws the content within the cell
     * @param canvas canvas
     */
    private void drawCellFill(Canvas canvas) {
        for (int i = 0; i < mCellLabels.length; i++) {
            int cx = i % SUDOKU_SIZE;   //x cell pos
            int cy = i / SUDOKU_SIZE;   //y cell pos

            //If its the cell thats currently highlighted draw the highlight
             if (i == mHighlightedCell) {
                Rect cellRect = new Rect(
                        mXOrigin + (cx * mCellSize),
                        mYOrigin + (cy * mCellSize),
                        mXOrigin + ((cx + 1) * mCellSize),
                        mYOrigin + ((cy + 1) * mCellSize)
                );

                canvas.drawRect(cellRect, mCellFilledPaint);
            }

            //If the cell has a label
            String label = mCellLabels[i];
            if (!label.equals("")) {

                //Draws the cell fill for squares that cant be edited
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

                //Measure the width of the label and draw it in the cell
                float textWidth = mTextPaint.measureText(label);

                //Text too big for cell decrease size
                float defaultTextSize = mTextPaint.getTextSize();
                while (textWidth > mCellSize) {
                    mTextPaint.setTextSize(mTextPaint.getTextSize() - 1);
                    textWidth = mTextPaint.measureText(label);
                }

                canvas.drawText(label,
                        mXOrigin + (cx * mCellSize) + (mCellSize / 2f) - (textWidth / 2),
                        mYOrigin + (cy * mCellSize) + (mCellSize / 2f) + (mTextPaintTextHeight / 2),
                        mTextPaint);

                //Reset text paint size
                mTextPaint.setTextSize(defaultTextSize);
            }
        }
    }

    public Rect getGridBounds() {
        return mGridBoundingRect;
    }

    /**
     * Retuns the cellnumber that is closest to the coordinate.
     * Used to find out what cell was touched
     * @param x cord
     * @param y cord
     * @return cell number
     */
    public int getCellNumberFromCoordinates(int x, int y) {
        x -= mXOrigin;
        y -= mYOrigin;
        x /= mCellSize;
        y /= mCellSize;
        return (y * SUDOKU_SIZE) + x;
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
