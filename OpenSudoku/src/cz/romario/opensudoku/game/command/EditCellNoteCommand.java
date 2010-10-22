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

import android.os.Bundle;
import cz.romario.opensudoku.game.Cell;
import cz.romario.opensudoku.game.CellNote;
import cz.romario.opensudoku.game.SudokuGame;

public class EditCellNoteCommand implements Command {

	private Cell mCell;
	private CellNote mNote;
	private CellNote mOldNote;
	
	EditCellNoteCommand() {
		
	}
	
	public EditCellNoteCommand(Cell cell, CellNote note) {
		if (cell == null) {
			throw new IllegalArgumentException("Cell cannot be null.");
		}
		if (note == null) {
			throw new IllegalArgumentException("Note cannot be null.");
		}

		mCell = cell;
		mNote = note;
	}
	
	@Override
	public void execute() {
		mOldNote = mCell.getNote();
		mCell.setNote(mNote);
	}

	@Override
	public void undo() {
		mCell.setNote(mOldNote);
		mCell.select();
	}

	@Override
	public void restoreState(Bundle state, SudokuGame game) {
		int rowIndex = state.getInt("rowIndex");
		int colIndex = state.getInt("colIndex");
		mCell = game.getCells().getCell(rowIndex, colIndex);
		
		mNote = CellNote.deserialize(state.getString("note"));
		mOldNote = CellNote.deserialize(state.getString("oldNote"));
	}

	@Override
	public void saveState(Bundle outState) {
		outState.putInt("rowIndex", mCell.getRowIndex());
		outState.putInt("colIndex", mCell.getColumnIndex());
		outState.putString("note", mNote.serialize());
		outState.putString("oldNote", mOldNote.serialize());
	}

}
