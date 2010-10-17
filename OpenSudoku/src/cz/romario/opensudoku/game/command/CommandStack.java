package cz.romario.opensudoku.game.command;

import java.util.Stack;

import cz.romario.opensudoku.game.CellCollection;

import android.os.Bundle;

public class CommandStack {
	private Stack<Command> mCommandStack = new Stack<Command>();
	
	// TODO: I need cells collection, because I have to call validate on it after some
	//	commands. CellCollection should be able to validate itself on change.
	private CellCollection mCells;
	
	public CommandStack(CellCollection cells) {
		mCells = cells;
	}

	public void saveState(Bundle outState) {
		
	}
	
    public void restoreState(Bundle inState) {
    	
    }

	public boolean empty() {
		return mCommandStack.empty();
	}
	
	public void execute(Command command) {
		command.execute();
		mCommandStack.push(command);
	}
	
	public void undo() {
		if (!mCommandStack.empty()) {
			Command c = mCommandStack.pop();
			c.undo();
			validateCells();
		}
	}
	
	public boolean hasSomethingToUndo() {
		return mCommandStack.size() != 0;
	}
	
    private void validateCells() {
    	mCells.validate();
    }


}
