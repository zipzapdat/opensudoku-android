package cz.romario.opensudoku.gui;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View.MeasureSpec;
import junit.framework.TestCase;

public class SudokuBoardView2Test extends
		ActivityInstrumentationTestCase2<SudokuBoardView2TestActivity> {

	private SudokuBoardView2TestClass target;
	
	public SudokuBoardView2Test() {
		super("cz.romario.opensudoku", SudokuBoardView2TestActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		target = new SudokuBoardView2TestClass(getActivity()); 
	}
	
	public void testOnMeasure() {
		int exactSmall = MeasureSpec.makeMeasureSpec(50, MeasureSpec.EXACTLY);
		int exactDefault = MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY);
		int exactLarge = MeasureSpec.makeMeasureSpec(200, MeasureSpec.EXACTLY);
		int atMostSmall = MeasureSpec.makeMeasureSpec(50, MeasureSpec.AT_MOST);
		int atMostLarge = MeasureSpec.makeMeasureSpec(200, MeasureSpec.AT_MOST);
		int unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		
		target.onMeasure(exactSmall, exactLarge);
		assertMeasureSpec(exactSmall, target.lastWidthMeasureSpec);
		assertMeasureSpec(exactLarge, target.lastHeightMeasureSpec);

		target.onMeasure(exactSmall, unspecified);
		assertMeasureSpec(exactSmall, target.lastWidthMeasureSpec);
		assertMeasureSpec(exactSmall, target.lastHeightMeasureSpec);

		target.onMeasure(unspecified, exactSmall);
		assertMeasureSpec(exactSmall, target.lastWidthMeasureSpec);
		assertMeasureSpec(exactSmall, target.lastHeightMeasureSpec);
		
		target.onMeasure(atMostLarge, atMostSmall);
		assertMeasureSpec(exactSmall, target.lastWidthMeasureSpec);
		assertMeasureSpec(exactSmall, target.lastHeightMeasureSpec);
		
		target.onMeasure(atMostSmall, atMostLarge);
		assertMeasureSpec(exactSmall, target.lastWidthMeasureSpec);
		assertMeasureSpec(exactSmall, target.lastHeightMeasureSpec);

		target.onMeasure(exactLarge, atMostSmall);
		assertMeasureSpec(exactLarge, target.lastWidthMeasureSpec);
		assertMeasureSpec(exactSmall, target.lastHeightMeasureSpec);

		target.onMeasure(atMostSmall, exactLarge);
		assertMeasureSpec(exactSmall, target.lastWidthMeasureSpec);
		assertMeasureSpec(exactLarge, target.lastHeightMeasureSpec);

		target.onMeasure(unspecified, unspecified);
		assertMeasureSpec(exactDefault, target.lastWidthMeasureSpec);
		assertMeasureSpec(exactDefault, target.lastHeightMeasureSpec);
	}
	
	private void assertMeasureSpec(int expected, int actual) {
		String expectedString = measureSpecToString(expected);
		String actualString = measureSpecToString(actual);
		
		assertEquals(expectedString, actualString);
	}
	
	private String measureSpecToString(int spec) {
		int size = MeasureSpec.getSize(spec);
		int mode = MeasureSpec.getMode(spec);
		
		String modeName = "?";
		switch (mode) {
		case MeasureSpec.AT_MOST:
			modeName = "AT_MOST";
			break;
		case MeasureSpec.EXACTLY:
			modeName = "EXACTLY";
			break;
		case MeasureSpec.UNSPECIFIED:
			modeName = "UNSPECIFIED";
			break;
		}
		
		return modeName + "/" + size;
	}
	
	private static class SudokuBoardView2TestClass extends SudokuBoardView2 {
		
		public int lastWidthMeasureSpec;
		public int lastHeightMeasureSpec;
		
		public SudokuBoardView2TestClass(Context context) {
			super(context);
		}
		
		@Override
		protected void testOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			lastWidthMeasureSpec = widthMeasureSpec;
			lastHeightMeasureSpec = heightMeasureSpec;
		}
		
	}
}
