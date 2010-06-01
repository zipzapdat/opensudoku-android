package cz.romario.opensudoku.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import cz.romario.opensudoku.game.Cell;
import cz.romario.opensudoku.game.CellCollection;

public class SudokuBoardView2 extends View {
	
	private static final int DEFAULT_BOARD_SIZE = 100;

	private float mCellWidth;
	private float mCellHeight;
	
	private Path mGrid;
	private Paint mGridPaint;
	private Path mSectorGrid;
	private Paint mSectorGridPaint;
	
	public SudokuBoardView2(Context context) {
		this(context, null);
	}
	
	public SudokuBoardView2(Context context, AttributeSet attrs) {
		super (context, attrs);
		
		setBackgroundColor(Color.WHITE);
		
		// standard grid
		mGrid = new Path();
		mGridPaint = new Paint();
		mGridPaint.setColor(Color.BLACK);
		mGridPaint.setStyle(Paint.Style.STROKE);

		// sector grid (lines between sectors)
		mSectorGrid = new Path();
		mSectorGridPaint = new Paint();
		mSectorGridPaint.setColor(Color.BLACK);
		mSectorGridPaint.setStyle(Paint.Style.STROKE);
		mSectorGridPaint.setStrokeWidth(5);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        int width = -1, height = -1;
        if (widthMode == MeasureSpec.EXACTLY) {
        	width = widthSize;
        } else {
        	width = DEFAULT_BOARD_SIZE;
        	if (widthMode == MeasureSpec.AT_MOST && width > widthSize ) {
        		width = widthSize;
        	}
        }
        if (heightMode == MeasureSpec.EXACTLY) {
        	height = heightSize;
        } else {
        	height = DEFAULT_BOARD_SIZE;
        	if (heightMode == MeasureSpec.AT_MOST && height > heightSize ) {
        		height = heightSize;
        	}
        }
        
        if (widthMode != MeasureSpec.EXACTLY) {
        	width = height;
        }
        
        if (heightMode != MeasureSpec.EXACTLY) {
        	height = width;
        }
        
    	if (widthMode == MeasureSpec.AT_MOST && width > widthSize ) {
    		width = widthSize;
    	}
    	if (heightMode == MeasureSpec.AT_MOST && height > heightSize ) {
    		height = heightSize;
    	}
        
        setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		int width = right - left;
		int height = bottom - top;
		
		mCellWidth = (float) width / CellCollection.SUDOKU_SIZE;
		mCellHeight = (float) height / CellCollection.SUDOKU_SIZE;
		
		mGrid.reset();
		mSectorGrid.reset();
		
		for (int r = 0; r <= CellCollection.SUDOKU_SIZE; r++) {
			float y = r * mCellHeight;
			mGrid.moveTo(0, y);
			mGrid.lineTo(width, y);
			
			if (r % 3 == 0) {
				mSectorGrid.moveTo(0, y);
				mSectorGrid.lineTo(width, y);
			}
		}

		for (int c = 0; c <= CellCollection.SUDOKU_SIZE; c++) {
			float x = c * mCellWidth;
			mGrid.moveTo(x, 0);
			mGrid.lineTo(x, height);
			
			if (c % 3 == 0) {
				mSectorGrid.moveTo(x, 0);
				mSectorGrid.lineTo(x, height);
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawPath(mGrid, mGridPaint);
		canvas.drawPath(mSectorGrid, mSectorGridPaint);
	}
	
	/**
	 * Occurs when user tap the cell.
	 * 
	 * @author romario
	 *
	 */
	public interface OnCellTappedListener {
		void onCellTapped(Cell cell);
	}
	
	/**
	 * Occurs when user selects the cell.
	 * 
	 * @author romario
	 *
	 */
	public interface OnCellSelectedListener {
		void onCellSelected(Cell cell);
	}
	
}
