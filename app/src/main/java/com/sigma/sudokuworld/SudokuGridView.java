package com.sigma.sudokuworld;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuGridView extends View {

    //Flag that prepends a str to tell the view to draw the cell as 'locked' (ie: not intractable)
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
    Paint mIncorrectCellFillPaint;
    Paint mTextPaint;

    Float mTextPaintTextHeight;

    String[] mCellLabels;       //Labels for every cell in grid
    int mIncorrectCell = -1;    //Points to first incorrect cell to highlight. -1 = no cell
    int mHighlightedCell = -1;  //Points to cell to draw highlight in. -1 = no cell

    public SudokuGridView(Context context) {
        this(context, null);
    }

    public SudokuGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mCellLabels = new String[SUDOKU_SIZE*SUDOKU_SIZE];
        Arrays.fill(mCellLabels, "");

        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCellFilledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLockedCellFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIncorrectCellFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //Get styling from activity_sudoku.xml
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SudokuGridView,
                0, 0);

        //Apply styling from xml file, if not styling set default values
        try {
            mGridPaint.setStrokeWidth(a.getDimension(R.styleable.SudokuGridView_gridPaintStrokeWidth, 5));
            mGridPaint.setColor(a.getColor(R.styleable.SudokuGridView_gridPaintColor, Color.BLACK));
            mGridPaint.setAlpha(75);
            mBoldPaint.setStrokeWidth(a.getDimension(R.styleable.SudokuGridView_boldGridPaintStrokeWidth, 10));
            mBoldPaint.setColor(a.getColor(R.styleable.SudokuGridView_boldGridPaintColor, Color.BLACK));
            mBoldPaint.setAlpha(125);
            mCellFilledPaint.setColor(a.getColor(R.styleable.SudokuGridView_highlightedCellColour, Color.YELLOW));
            mLockedCellFillPaint.setColor(a.getColor(R.styleable.SudokuGridView_lockedCellColour, Color.GRAY));
            mIncorrectCellFillPaint.setColor(a.getColor(R.styleable.SudokuGridView_incorrectCellColour, Color.RED));
        } finally {
            a.recycle();
        }

        //More paint styling
        mGridPaint.setStrokeCap(Paint.Cap.BUTT);
        mBoldPaint.setStrokeCap(Paint.Cap.ROUND);
        mCellFilledPaint.setStyle(Paint.Style.FILL);
        mLockedCellFillPaint.setStyle(Paint.Style.FILL);
        mIncorrectCellFillPaint.setStyle(Paint.Style.FILL);
    }

    @Override   //This is for accessibility (REQUIRED BY ANDRIOD STUDIO)
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
        mSquareSize = mCellSize * SUDOKU_SIZE; //Guard against for floating point errors

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
        highlightNeighbours(canvas);
    }

    /**
     * Draws the the sudoku grid
     * @param canvas canvas
     */
    private void drawGrid(Canvas canvas) {

        //Horizontal Lines
        for(int i = 0; i <= SUDOKU_SIZE; i++) {

            //Either bold or not bold
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

            //Either bold or not bold
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

            // If its the cell that's currently INCORRECT, draw its highlight
            if(i == mIncorrectCell) {
                Rect cellRect = new Rect(
                        mXOrigin + (cx * mCellSize),
                        mYOrigin + (cy * mCellSize),
                        mXOrigin + ((cx + 1) * mCellSize),
                        mYOrigin + ((cy + 1) * mCellSize)
                );

                canvas.drawRect(cellRect, mIncorrectCellFillPaint);
            }

            //If its the cell that's currently highlighted draw the highlight
            else if (i == mHighlightedCell) {
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
                        mYOrigin + (cy * mCellSize) + (mCellSize / 2f) + (mTextPaintTextHeight / 2) - 10,
                        mTextPaint);

                //Reset text paint size
                mTextPaint.setTextSize(defaultTextSize);
            }



        }
    }

    private void highlightNeighbours(Canvas canvas){
        //This method will highlight all the neighbours of the currently selected cell
        //Neighbours refer to cells in the same column, row, and section as the selected cell

        //No cell is highlighted
        if(mHighlightedCell == -1 ) {return;}


        int row = (mHighlightedCell / SUDOKU_SIZE);
        int column = (mHighlightedCell % SUDOKU_SIZE);
        int subsectionRow = SUDOKU_SIZE * SUDOKU_ROOT_SIZE * (row / SUDOKU_ROOT_SIZE);
        int subsectionColumn = SUDOKU_ROOT_SIZE * (column / SUDOKU_ROOT_SIZE);
        int i;
        List<Integer> visitedList = new ArrayList<Integer>();
        visitedList.add(mHighlightedCell);

        //Draw row and column highlights
        for(i = 0; i < SUDOKU_SIZE; i++) {
            //Row
            int cellnumber = SUDOKU_SIZE * row + i;
            if (cellnumber != mHighlightedCell)
            {
                drawCellHighlight(canvas, cellnumber);
                visitedList.add(cellnumber);
            }


            //Column
            cellnumber = column + i * SUDOKU_SIZE;
            if (cellnumber != mHighlightedCell)
            {
                drawCellHighlight(canvas, cellnumber);
                visitedList.add(cellnumber);
            }
        }

        //Draw subsection highlights
        for(i = 0; i < SUDOKU_ROOT_SIZE; i++){
            for (int j = 0; j < SUDOKU_ROOT_SIZE; j++)
            {
                int cellnumber = subsectionRow + SUDOKU_SIZE * i + subsectionColumn + j;
                if (!visitedList.contains(cellnumber)) {
                    drawCellHighlight(canvas, cellnumber);
                }
            }
        }
    }


    private void drawCellHighlight(Canvas canvas, int cellNumber)
    {
        //Draws the individual highlight of a cell

        int cx = cellNumber % SUDOKU_SIZE;   //x cell pos
        int cy = cellNumber / SUDOKU_SIZE;   //y cell pos

        Rect cellRect = new Rect(
                mXOrigin + (cx * mCellSize),
                mYOrigin + (cy * mCellSize),
                mXOrigin + ((cx + 1) * mCellSize),
                mYOrigin + ((cy + 1) * mCellSize)
        );
        Paint paint = new Paint(mGridPaint);
        paint.setAlpha(17);
        canvas.drawRect(cellRect, paint);
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

    public int getIncorrectCell(){
        return mIncorrectCell;
    }
    public void setIncorrectCell(int cellNumber) {
        mIncorrectCell = cellNumber;
    }
    public void clearIncorrectCell() {
        mIncorrectCell = -1;
    }
}
