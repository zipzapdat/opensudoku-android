package cz.romario.opensudoku.gui;

import android.content.Context;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import cz.romario.opensudoku.game.Cell;

public class SudokuBoardView2 extends FrameLayout {
	
	private static final int DEFAULT_BOARD_SIZE = 100;
	
	private Context mContext;

	
	public SudokuBoardView2(Context context) {
		this(context, null);
	}
	
	public SudokuBoardView2(Context context, AttributeSet attrs) {
		super (context, attrs);
		
		mContext = context;
		
		addView(new SudokuBoardOverlay(context));
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
    	
    	widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
    	heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
    	
		testOnMeasure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}
	
	// intended for override in unit tests
	protected void testOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
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
