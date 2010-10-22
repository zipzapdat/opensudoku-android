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
import cz.romario.opensudoku.game.SudokuGame;

public class SetCellValueCommand implements Command {

	private Cell mCell;
	private int mValue;
	private int mOldValue;
	
	SetCellValueCommand() {
		
	}
	
	public SetCellValueCommand(Cell cell, int value) {
		if (cell == null) {
			throw new IllegalArgumentException("Cell cannot be null.");
		}
		if (value < 0 || value > 9) {
			throw new IllegalArgumentException("Value must be between 0-9.");
		}

		mCell = cell;
		mValue = value;
	}
	
	@Override
	public void execute() {
		mOldValue = mCell.getValue();
		mCell.setValue(mValue);
	}

	@Override
	public void undo() {
		mCell.setValue(mOldValue);
		mCell.select();
	}

	@Override
	public void restoreState(Bundle state, SudokuGame game) {
		int rowIndex = state.getInt("rowIndex");
		int colIndex = state.getInt("colIndex");
		mCell = game.getCells().getCell(rowIndex, colIndex);
	
		mValue = state.getInt("value");
		mOldValue = state.getInt("oldValue");
	}

	@Override
	public void saveState(Bundle outState) {
		outState.putInt("rowIndex", mCell.getRowIndex());
		outState.putInt("colIndex", mCell.getColumnIndex());
		outState.putInt("value", mValue);
		outState.putInt("oldValue", mOldValue);
	}

}
