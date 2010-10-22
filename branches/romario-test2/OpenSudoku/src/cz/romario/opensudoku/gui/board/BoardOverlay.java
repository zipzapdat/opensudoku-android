package cz.romario.opensudoku.gui.board;

import java.util.Collection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import cz.romario.opensudoku.game.Cell;
import cz.romario.opensudoku.game.CellCollection;
import cz.romario.opensudoku.game.CellCollection.OnChangeListener;

public class BoardOverlay extends AbstractBoardOverlay {

	/**
	 * "Color not set" value. (In relation to {@link Color}, it is in fact black color with 
	 * alpha channel set to 0 => that means it is completely transparent).
	 */
	private static final int NO_COLOR = 0;

	private CellCollection mModel;
	
	private Path mGrid;
	private Paint mGridPaint;
	private Path mSectorGrid;
	private Paint mSectorGridPaint;
	
	private Paint mCellValuePaint;
	private Paint mCellValueReadonlyPaint;
	private Paint mCellNotePaint;
	private Paint mBackgroundColorReadOnly;
	// offset coordinates of number in cell
	private int mNumberLeft; 
	private int mNumberTop;
	// offset of note
	private float mNoteTop;
	
	private SudokuBoardLayoutInfo mLayoutInfo;

	public BoardOverlay(Context context) {
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

		mCellValuePaint = new Paint();
		mCellValueReadonlyPaint = new Paint();
		mCellNotePaint = new Paint();
		mBackgroundColorReadOnly = new Paint();

		mCellValuePaint.setAntiAlias(true);
		mCellValueReadonlyPaint.setAntiAlias(true);
		mCellNotePaint.setAntiAlias(true);
		
		// TODO: themes
		mBackgroundColorReadOnly.setColor(Color.rgb(204, 204, 204));
		
//		mDisplayDensity = getContext().getResources().getDisplayMetrics().density;
	}
	
	@Override
	protected CellCollection getModel() {
		return mModel;
	}
	
	@Override
	protected void setModel(CellCollection model) {
		mModel = model;
		
		mModel.addOnChangeListener(
				CellCollection.CHANGE_TYPE_EDITABLE |
				CellCollection.CHANGE_TYPE_NOTE |
				CellCollection.CHANGE_TYPE_VALUE, mModelOnChangeListener);
	}
	

	@Override
	protected void setLayoutInfo(SudokuBoardLayoutInfo layoutInfo) {
		
		mLayoutInfo = layoutInfo;
		
		mSectorGridPaint.setStrokeWidth(layoutInfo.sectorLineWidth);
		
		mGrid.reset();
		mSectorGrid.reset();
		
		mSectorGrid.addRect(layoutInfo.left, layoutInfo.top, layoutInfo.right, layoutInfo.bottom, Direction.CW);
		
		for (int r = 1; r < CellCollection.SUDOKU_SIZE; r++) {
			float y = layoutInfo.top + r * layoutInfo.cellHeight;
			
			if (r % 3 == 0) {
				mSectorGrid.moveTo(layoutInfo.left, y);
				mSectorGrid.lineTo(layoutInfo.right, y);
			} else {
				mGrid.moveTo(layoutInfo.left, y);
				mGrid.lineTo(layoutInfo.right, y);
			}
		}

		for (int c = 1; c < CellCollection.SUDOKU_SIZE; c++) {
			float x = layoutInfo.left + c * layoutInfo.cellWidth;
			
			if (c % 3 == 0) {
				mSectorGrid.moveTo(x, layoutInfo.top);
				mSectorGrid.lineTo(x, layoutInfo.bottom);
			} else {
				mGrid.moveTo(x, layoutInfo.top);
				mGrid.lineTo(x, layoutInfo.bottom);
			}
		}
		
        float cellTextSize = layoutInfo.cellHeight * 0.75f;
        mCellValuePaint.setTextSize(cellTextSize);
        mCellValueReadonlyPaint.setTextSize(cellTextSize);
        mCellNotePaint.setTextSize(layoutInfo.cellHeight / 3.0f);
        // compute offsets in each cell to center the rendered number
        mNumberLeft = (int) ((layoutInfo.cellWidth - mCellValuePaint.measureText("9")) / 2);
        mNumberTop = (int) ((layoutInfo.cellHeight - mCellValuePaint.getTextSize()) / 2);
        
        // add some offset because in some resolutions notes are cut-off in the top
        mNoteTop = layoutInfo.cellHeight / 50.0f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// draw cells
		int cellLeft, cellTop;
		if (mModel != null) {
			
			boolean hasBackgroundColorReadOnly = mBackgroundColorReadOnly.getColor() != NO_COLOR;

			float numberAscent = mCellValuePaint.ascent();
			float noteAscent = mCellNotePaint.ascent();
			float noteWidth = mLayoutInfo.cellWidth / 3f;
			for (int row = 0; row < 9; row++) {
				for (int col = 0; col < 9; col++) {
					Cell cell = mModel.getCell(row, col);

					cellLeft = Math.round((col * mLayoutInfo.cellWidth) + mLayoutInfo.left);
					cellTop = Math.round((row * mLayoutInfo.cellHeight) + mLayoutInfo.top);

					// draw read-only field background
					if (!cell.isEditable() && hasBackgroundColorReadOnly) {
						if (mBackgroundColorReadOnly.getColor() != NO_COLOR) {
							canvas.drawRect(cellLeft, cellTop, cellLeft
									+ mLayoutInfo.cellWidth, cellTop
									+ mLayoutInfo.cellHeight,
									mBackgroundColorReadOnly);
						}
					}

					// draw cell Text
					int value = cell.getValue();
					if (value != 0) {
						Paint cellValuePaint = cell.isEditable() ? mCellValuePaint
								: mCellValueReadonlyPaint;

						canvas.drawText(Integer.toString(value), cellLeft
								+ mNumberLeft, cellTop + mNumberTop
								- numberAscent, cellValuePaint);
					} else {
						if (!cell.getNote().isEmpty()) {
							Collection<Integer> numbers = cell.getNote()
									.getNotedNumbers();
							for (Integer number : numbers) {
								int n = number - 1;
								int c = n % 3;
								int r = n / 3;
								// canvas.drawText(Integer.toString(number),
								// cellLeft + c*noteWidth + 2, cellTop +
								// noteAscent + r*noteWidth - 1, mNotePaint);
								canvas
										.drawText(Integer.toString(number),
												cellLeft + c * noteWidth + 2,
												cellTop + mNoteTop - noteAscent
														+ r * noteWidth - 1,
												mCellNotePaint);
							}
						}
					}
				}
			}
		}
		
		canvas.drawPath(mGrid, mGridPaint);
		canvas.drawPath(mSectorGrid, mSectorGridPaint);
	}
		
	private OnChangeListener mModelOnChangeListener = new OnChangeListener() {
		@Override
		public void onChange(int changeType, Cell cell) {
			// something has changed in model, update view
			postInvalidate();
		}
	};
		
}
