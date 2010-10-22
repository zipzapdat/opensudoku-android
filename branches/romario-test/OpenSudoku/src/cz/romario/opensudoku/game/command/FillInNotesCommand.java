package cz.romario.opensudoku.game.command;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import cz.romario.opensudoku.game.Cell;
import cz.romario.opensudoku.game.CellCollection;
import cz.romario.opensudoku.game.CellGroup;
import cz.romario.opensudoku.game.CellNote;
import cz.romario.opensudoku.game.SudokuGame;

public class FillInNotesCommand implements Command {

	private CellCollection mCells; 
	private List<NoteEntry> mOldNotes = new ArrayList<NoteEntry>();
	
	FillInNotesCommand() {
		
	}
	
	public FillInNotesCommand(CellCollection cells) {
		mCells = cells;
	}
	
	
	@Override
	public void execute() {
		mOldNotes.clear();
		for (int r = 0; r < CellCollection.SUDOKU_SIZE; r++) {
			for (int c = 0; c < CellCollection.SUDOKU_SIZE; c++) {
				Cell cell = mCells.getCell(r, c);
				mOldNotes.add(new NoteEntry(r, c, cell.getNote()));
				cell.setNote(new CellNote());
				
				CellGroup row = cell.getRow();
				CellGroup column = cell.getColumn();
				CellGroup sector = cell.getSector();
				for (int i = 1; i <= CellCollection.SUDOKU_SIZE; i++) {
					if (!row.contains(i) && !column.contains(i) && !sector.contains(i)) {
						cell.setNote(cell.getNote().addNumber(i));
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
	
	private static class NoteEntry {
		public int rowIndex;
		public int colIndex;
		public CellNote note;
		
		public NoteEntry(int rowIndex, int colIndex, CellNote note){
			this.rowIndex = rowIndex;
			this.colIndex = colIndex;
			this.note = note;
		}
		
	}

	@Override
	public void restoreState(Bundle state, SudokuGame game) {
		mCells = game.getCells();
		
		int[] rows = state.getIntArray("rows");
		int[] cols = state.getIntArray("cols");
		String[] notes = state.getStringArray("notes");
		
		for (int i = 0; i < rows.length; i++) {
			mOldNotes.add(new NoteEntry(rows[i], cols[i], CellNote
					.deserialize(notes[i])));
		}
	}

	@Override
	public void saveState(Bundle outState) {
		int[] rows = new int[mOldNotes.size()];
		int[] cols = new int[mOldNotes.size()];
		String[] notes = new String[mOldNotes.size()];
		
		int i = 0;
		for (NoteEntry ne : mOldNotes) {
			rows[i] = ne.rowIndex;
			cols[i] = ne.colIndex;
			notes[i] = ne.note.serialize();
		}
		
		outState.putIntArray("rows", rows);
		outState.putIntArray("cols", cols);
		outState.putStringArray("notes", notes);
	}

}
