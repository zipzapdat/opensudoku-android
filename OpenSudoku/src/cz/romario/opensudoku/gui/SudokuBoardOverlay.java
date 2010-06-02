package cz.romario.opensudoku.gui;

import cz.romario.opensudoku.game.CellCollection;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class SudokuBoardOverlay extends View {
	
	private float mCellWidth;
	private float mCellHeight;
//	private float mDisplayDensity;
	
	private Path mGrid;
	private Paint mGridPaint;
	private Path mSectorGrid;
	private Paint mSectorGridPaint;
	
	public SudokuBoardOverlay(Context context) {
		super(context);

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
		
//		mDisplayDensity = getContext().getResources().getDisplayMetrics().density;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		left = left + getPaddingLeft();
		top = top + getPaddingTop();
		right = right - getPaddingRight();
		bottom = bottom - getPaddingBottom();
		
		// width of sector lines depends on widget size
		int sectorLineWidth = (int)computeSectorLineWidth(right - left, bottom - top);
		mSectorGridPaint.setStrokeWidth(sectorLineWidth);
		
		int borderOffset =  sectorLineWidth / 2;
		left = left + borderOffset;
		top = top + borderOffset;
		right = right - borderOffset;
		bottom = bottom - borderOffset;
		
		int width = right - left;
		int height = bottom - top;
		
		mCellWidth = (float) width / CellCollection.SUDOKU_SIZE;
		mCellHeight = (float) height / CellCollection.SUDOKU_SIZE;
		
		mGrid.reset();
		mSectorGrid.reset();
		
		for (int r = 0; r <= CellCollection.SUDOKU_SIZE; r++) {
			float y = top + r * mCellHeight;
			
			if (r % 3 == 0) {
				mSectorGrid.moveTo(left, y);
				mSectorGrid.lineTo(right, y);
			} else {
				mGrid.moveTo(left, y);
				mGrid.lineTo(right, y);
			}
		}

		for (int c = 0; c <= CellCollection.SUDOKU_SIZE; c++) {
			float x = left + c * mCellWidth;
			
			if (c % 3 == 0) {
				mSectorGrid.moveTo(x, top);
				mSectorGrid.lineTo(x, bottom);
			} else {
				mGrid.moveTo(x, top);
				mGrid.lineTo(x, bottom);
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawPath(mGrid, mGridPaint);
		canvas.drawPath(mSectorGrid, mSectorGridPaint);
	}
	
	private float computeSectorLineWidth(int widthInPx, int heightInPx) {
		int sizeInPx = widthInPx < heightInPx ? widthInPx : heightInPx;
		float dipScale = getContext().getResources().getDisplayMetrics().density;
		float sizeInDip = sizeInPx / dipScale;
		
		float sectorLineWidthInDip = 2.0f;
		
		if (sizeInDip > 150) {
			sectorLineWidthInDip = 5.0f;
		}
		
		return sectorLineWidthInDip * dipScale;
	}
	
}
