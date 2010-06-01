package cz.romario.opensudoku.game.command;

import java.util.Stack;

import android.os.Bundle;

import cz.romario.opensudoku.game.SudokuGame;


public class CommandInvoker {
	
	private Stack<Command> mUndoStack = new Stack<Command>();
	
	private SudokuGame mGame;
	
	public CommandInvoker(SudokuGame game) {
		mGame = game;
	}
	
	public void execute(Command c) {
		c.execute();
		mUndoStack.push(c);
	}	
	
	/** 
	 * Undo last command.
	 */
	public void undo() {
		if (!mUndoStack.empty()) {
			Command c = mUndoStack.pop();
			c.undo();
		}
	}
	
	public boolean hasSomethingToUndo() {
		return mUndoStack.size() != 0;
	}
	
	// TODO:
	public void saveState(Bundle outState) {
	}
	
	public void restoreState(Bundle state) {
	}
}
