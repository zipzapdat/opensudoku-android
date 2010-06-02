/* 
 * Copyright (C) 2009 Roman Masek
 * 
 * This file is part of OpenSudoku.
 * 
 * OpenSudoku is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenSudoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OpenSudoku.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package cz.romario.opensudoku.game.command;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import cz.romario.opensudoku.game.Cell;
import cz.romario.opensudoku.game.CellCollection;
import cz.romario.opensudoku.game.CellNote;
import cz.romario.opensudoku.game.SudokuGame;

public class ClearAllNotesCommand implements Command {

	private CellCollection mCells; 
	private List<NoteEntry> mOldNotes = new ArrayList<NoteEntry>();
	
	ClearAllNotesCommand() {
		
	}
	
	public ClearAllNotesCommand(CellCollection cells) {
		mCells = cells;
	}
	
	@Override
	public void execute() {
		mOldNotes.clear();
		for (int r = 0; r < CellCollection.SUDOKU_SIZE; r++) {
			for (int c = 0; c < CellCollection.SUDOKU_SIZE; c++) {
				Cell cell = mCells.getCell(r, c);
				CellNote note = cell.getNote();
				if (!note.isEmpty()) {
					mOldNotes.add(new NoteEntry(r, c, note));
					cell.setNote(new CellNote());
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
