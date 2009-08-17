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

import cz.romario.opensudoku.game.SudokuCell;
import cz.romario.opensudoku.game.SudokuCellCollection;

public class ClearAllNotesCommand implements Command {

	private SudokuCellCollection mCells; 
	private List<NoteEntry> mOldNotes = new ArrayList<NoteEntry>();
	
	
	public ClearAllNotesCommand(SudokuCellCollection cells) {
		mCells = cells;
	}
	
	@Override
	public void execute() {
		mOldNotes.clear();
		for (int r = 0; r < SudokuCellCollection.SUDOKU_SIZE; r++) {
			for (int c = 0; c < SudokuCellCollection.SUDOKU_SIZE; c++) {
				SudokuCell cell = mCells.getCell(r, c);
				String note = cell.getNote();
				if (note != null && !note.equals("")) {
					mOldNotes.add(new NoteEntry(r, c, note));
					cell.setNote(null);
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
