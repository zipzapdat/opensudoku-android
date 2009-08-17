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

import cz.romario.opensudoku.game.SudokuCell;

public class EditCellNoteCommand implements Command {

	private SudokuCell mCell;
	private String mNote;
	private String mOldNote;
	
	public EditCellNoteCommand(SudokuCell cell, String note) {
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
	}

}
