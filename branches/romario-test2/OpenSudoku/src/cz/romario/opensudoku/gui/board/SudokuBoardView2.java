package cz.romario.opensudoku.gui.board;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import cz.romario.opensudoku.game.CellCollection;

public class SudokuBoardView2 extends ViewGroup {
	
	private static final int DEFAULT_BOARD_SIZE = 100;
	
	//private Context mContext;
	
	private CellCollection mModel;
	private List<AbstractBoardOverlay> mOverlays = new ArrayList<AbstractBoardOverlay>();

	
	public SudokuBoardView2(Context context) {
		this(context, null);
	}
	
	public SudokuBoardView2(Context context, AttributeSet attrs) {
		super (context, attrs);
		
		setClipChildren(false);
		
		//mContext = context;
		mOverlays.add(new BoardOverlay(context));
		
		for (AbstractBoardOverlay overlay : mOverlays) {
			addView(overlay);
		}
	}
	
	// TODO: maybe rename CellCollection?
	public CellCollection getModel() {
		return mModel;
	}
	
	public void setModel(CellCollection model) {
		mModel = model;
		
		for (AbstractBoardOverlay overlay : mOverlays) {
			overlay.setModel(model);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        
        int width = DEFAULT_BOARD_SIZE, height = DEFAULT_BOARD_SIZE;
        
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
        	// both width and size have been explicitly set
        	width = widthSize;
        	height = heightSize;
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
        	// only width has been explicitly set, try to set height equal to width (rectangular shape)
        	width = widthSize;
        	if (heightMode == MeasureSpec.AT_MOST && width > heightSize) {
        		height = heightSize;
        	} else {
        		height = width;
        	}
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
        	// only height has been explicitly set, try to set width equal to height (rectangular shape)
        	height = heightSize;
        	if (widthMode == MeasureSpec.AT_MOST && height > widthSize) {
        		width = widthSize;
        	} else {
        		width = height;
        	}
        } else {
        	// neither width nor height have been explicitly set, try to create rectangular shape
        	if (widthMode == MeasureSpec.AT_MOST)
        		width = widthSize;
        	if (heightMode == MeasureSpec.AT_MOST)
        		height = heightSize;
        	
        	if (width > height)
        		width = height;
        	else
        		height = width;
        }

        testOnMeasure(width, height);
        setMeasuredDimension(width, height);
	}
	
	// intended for override in unit tests
	protected void testOnMeasure(int width, int height) {
		
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		left = left + getPaddingLeft();
		top = top + getPaddingTop();
		right = right - getPaddingRight();
		bottom = bottom - getPaddingBottom();
		
		// width of sector lines depends on widget size
		float sectorLineWidth = computeSectorLineWidth(right - left, bottom - top);
		
		float borderOffset =  sectorLineWidth / 2;
		float leftF = left + borderOffset;
		float topF = top + borderOffset;
		float rightF = right - borderOffset;
		float bottomF = bottom - borderOffset;
		
		float width = rightF - leftF;
		float height = bottomF - topF;
		
		float cellWidth = width / CellCollection.SUDOKU_SIZE;
		float cellHeight = height / CellCollection.SUDOKU_SIZE;

		SudokuBoardLayoutInfo layoutInfo = new SudokuBoardLayoutInfo();
		layoutInfo.left = (int)borderOffset;
		layoutInfo.top = (int)borderOffset;
		layoutInfo.right = (int)(borderOffset + width);
		layoutInfo.bottom = (int)(borderOffset + height);
		layoutInfo.cellWidth = cellWidth;
		layoutInfo.cellHeight = cellHeight;
		layoutInfo.sectorLineWidth = (int)sectorLineWidth;
		
		for (AbstractBoardOverlay overlay : mOverlays) {
			overlay.layout(left, top, right, bottom);
			overlay.setLayoutInfo(layoutInfo);
		}
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
