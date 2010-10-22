package cz.romario.opensudoku.game;

import junit.framework.TestCase;
import cz.romario.opensudoku.game.CellCollection.OnChangeListener;

public class CellCollectionTest extends TestCase {
	
	public void testCreateEmpty() {
		CellCollection x = CellCollection.createEmpty();
		
		for (int r=0; r<CellCollection.SUDOKU_SIZE; r++) {
			for (int c=0; c<CellCollection.SUDOKU_SIZE; c++) {
				Cell cell = x.getCell(r, c);
				assertEquals(0, cell.getValue());
				assertEquals(true, cell.isEditable());
				assertEquals(true, cell.isValid());
			}
		}
	}
	
	public void testOnChange() {
		CellCollection target = CellCollection.createEmpty();
		TestOnChangeListener listener = new TestOnChangeListener();
		target.addOnChangeListener(CellCollection.CHANGE_TYPE_ALL, listener);
		
		Cell cell = target.getCell(3, 3);
		
		cell.setValue(5);
		assertEquals(1, listener.invokeCount);
		assertEquals(CellCollection.CHANGE_TYPE_VALUE, listener.lastChangeType);
		assertSame(cell, listener.lastCell);

		cell.setNote(new CellNote(5));
		assertEquals(2, listener.invokeCount);
		assertEquals(CellCollection.CHANGE_TYPE_NOTE, listener.lastChangeType);
		assertSame(cell, listener.lastCell);

		cell.setEditable(false);
		assertEquals(3, listener.invokeCount);
		assertEquals(CellCollection.CHANGE_TYPE_EDITABLE, listener.lastChangeType);
		assertSame(cell, listener.lastCell);
		
		cell.select();
		assertEquals(4, listener.invokeCount);
		assertEquals(CellCollection.CHANGE_TYPE_SELECTION, listener.lastChangeType);
		assertSame(cell, listener.lastCell);
	}
	
	public void testOnChangeFilteringValue() {
		CellCollection target = CellCollection.createEmpty();
		TestOnChangeListener listener = new TestOnChangeListener();
		target.addOnChangeListener(CellCollection.CHANGE_TYPE_VALUE, listener);
		
		Cell cell = target.getCell(3, 3);
		
		cell.setValue(5);
		assertEquals(1, listener.invokeCount);
		assertEquals(CellCollection.CHANGE_TYPE_VALUE, listener.lastChangeType);
		assertSame(cell, listener.lastCell);
		
		cell.setNote(new CellNote(5));
		cell.setEditable(false);
		cell.select();
		assertEquals(1, listener.invokeCount);
	}

	public void testOnChangeFilteringNote() {
		CellCollection target = CellCollection.createEmpty();
		TestOnChangeListener listener = new TestOnChangeListener();
		target.addOnChangeListener(CellCollection.CHANGE_TYPE_NOTE, listener);
		
		Cell cell = target.getCell(3, 3);
		
		cell.setNote(new CellNote(5));
		assertEquals(1, listener.invokeCount);
		assertEquals(CellCollection.CHANGE_TYPE_NOTE, listener.lastChangeType);
		assertSame(cell, listener.lastCell);
		
		cell.setValue(5);
		cell.setEditable(false);
		cell.select();
		assertEquals(1, listener.invokeCount);
	}

	public void testOnChangeFilteringEditable() {
		CellCollection target = CellCollection.createEmpty();
		TestOnChangeListener listener = new TestOnChangeListener();
		target.addOnChangeListener(CellCollection.CHANGE_TYPE_EDITABLE, listener);
		
		Cell cell = target.getCell(3, 3);
		
		cell.setEditable(false);
		assertEquals(1, listener.invokeCount);
		assertEquals(CellCollection.CHANGE_TYPE_EDITABLE, listener.lastChangeType);
		assertSame(cell, listener.lastCell);
		
		cell.setNote(new CellNote(5));
		cell.setValue(5);
		cell.select();
		assertEquals(1, listener.invokeCount);
	}

	public void testOnChangeFilteringSelection() {
		CellCollection target = CellCollection.createEmpty();
		TestOnChangeListener listener = new TestOnChangeListener();
		target.addOnChangeListener(CellCollection.CHANGE_TYPE_SELECTION, listener);
		
		Cell cell = target.getCell(3, 3);
		
		cell.select();
		assertEquals(1, listener.invokeCount);
		assertEquals(CellCollection.CHANGE_TYPE_SELECTION, listener.lastChangeType);
		assertSame(cell, listener.lastCell);
		
		cell.setNote(new CellNote(5));
		cell.setValue(5);
		cell.setEditable(false);
		assertEquals(1, listener.invokeCount);
	}
	
	public void testAddOnChangeListenerCannotAddTwice() {
		CellCollection target = CellCollection.createEmpty();
		TestOnChangeListener listener = new TestOnChangeListener();
		target.addOnChangeListener(CellCollection.CHANGE_TYPE_ALL, listener);
		try {
			target.addOnChangeListener(CellCollection.CHANGE_TYPE_ALL, listener);
		} catch (IllegalStateException e) {
			return;
		}
		
		fail("IllegalStateException expected, but nothing was raised.");
	}
	
	public void testRemoveOnChangeListener() {
		CellCollection target = CellCollection.createEmpty();
		TestOnChangeListener listener = new TestOnChangeListener();
		target.addOnChangeListener(CellCollection.CHANGE_TYPE_ALL, listener);
		
		assertEquals(1, target.getListenerCount());

		target.removeOnChangeListener(listener);
		
		assertEquals(0, target.getListenerCount());
	}
	
	
	static class TestOnChangeListener implements OnChangeListener {

		public int invokeCount;
		public int lastChangeType;
		public Cell lastCell;
		
		public TestOnChangeListener() {
		}
		
		@Override
		public void onChange(int changeType, Cell cell) {
			invokeCount++;
			lastChangeType = changeType;
			lastCell = cell;
		}
		
	}
	
	// TODO: test bit masky
	

	
	

}
