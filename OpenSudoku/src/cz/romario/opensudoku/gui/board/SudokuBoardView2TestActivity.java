package cz.romario.opensudoku.gui.board;

import cz.romario.opensudoku.R;
import cz.romario.opensudoku.game.CellCollection;
import android.app.Activity;
import android.os.Bundle;

public class SudokuBoardView2TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sudoku_board_view2_test);
		
		SudokuBoardView2 boardView = (SudokuBoardView2) findViewById(R.id.sudoku_board);
		CellCollection model = CellCollection.createDebugGame();
		boardView.setModel(model);
	}
}
