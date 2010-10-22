package cz.romario.opensudoku.gui.board;

import cz.romario.opensudoku.game.CellCollection;
import android.content.Context;
import android.view.View;

abstract class AbstractBoardOverlay extends View {

	public AbstractBoardOverlay(Context context) {
		super(context);
	}
	
	abstract protected void setLayoutInfo(SudokuBoardLayoutInfo layoutInfo);
	abstract protected CellCollection getModel();
	abstract protected void setModel(CellCollection model);
}
