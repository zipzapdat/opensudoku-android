package cz.romario.opensudoku.game.command;

import java.util.ArrayList;
import java.util.List;

import cz.romario.opensudoku.game.SudokuCell;
import cz.romario.opensudoku.game.SudokuCellCollection;
import cz.romario.opensudoku.game.SudokuCellGroup;

public class FillInNotesCommand implements Command {

	private SudokuCellCollection mCells; 
	private List<NoteEntry> mOldNotes = new ArrayList<NoteEntry>();
	
	
	public FillInNotesCommand(SudokuCellCollection cells) {
		mCells = cells;
	}
	
	
	@Override
	public void execute() {
		mOldNotes.clear();
		for (int r = 0; r < SudokuCellCollection.SUDOKU_SIZE; r++) {
			for (int c = 0; c < SudokuCellCollection.SUDOKU_SIZE; c++) {
				SudokuCell cell = mCells.getCell(r, c);
				mOldNotes.add(new NoteEntry(r, c, cell.getNote()));
				cell.setNote(null);
				
				SudokuCellGroup row = cell.getRow();
				SudokuCellGroup column = cell.getColumn();
				SudokuCellGroup sector = cell.getSector();
				for (int i = 1; i <= SudokuCellCollection.SUDOKU_SIZE; i++) {
					if (!row.contains(i) && !column.contains(i) && !sector.contains(i)) {
						cell.setNoteNumber(i, true);
					}
				}
			}
		}
	}

	@Override
	public void undo() {
		for (NoteEntry ne : mOldNotes) {
			mCells.getCell(ne.rowIndex, ne.colIndex).setNote(ne.note);
		}
	}
	
	private class NoteEntry {
		public int rowIndex;
		public int colIndex;
		public String note;
		
		public NoteEntry(int rowIndex, int colIndex, String note){
			this.rowIndex = rowIndex;
			this.colIndex = colIndex;
			this.note = note;
		}
		
	}
}
