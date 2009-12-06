package cz.romario.opensudoku.gui.generating;

import android.content.Context;
import android.os.AsyncTask;
import cz.romario.opensudoku.db.SudokuDatabase;
import cz.romario.opensudoku.game.CellCollection;
import cz.romario.opensudoku.game.SudokuGame;
import cz.romario.opensudoku.logic.Generator;

/**
 * Generates a new Sudoku game
 * 
 * @author Martin Helff
 *
 */
public class GenerateSudokuTask extends AsyncTask<GenerateSudokuTaskParams, Integer, Void> {

	private Context mContext;
	private OnGenerateFinishedListener mOnGenerateFinishedListener;
	
	public GenerateSudokuTask(Context context) {
		this.mContext = context;
	}
	
	public OnGenerateFinishedListener getOnGenerateFinishedListener() {
		return mOnGenerateFinishedListener;
	}
	
	public void setOnGenerateFinishedListener(OnGenerateFinishedListener listener) {
		mOnGenerateFinishedListener = listener;
	}
	
	protected void generateSudoku(GenerateSudokuTaskParams params) {		
		Generator gen = new Generator();
		gen.generate(params.level);
		int[][] game = gen.getGeneratedGame();
		StringBuffer gameBuf = new StringBuffer();
		for(int i = 0; i < game.length; i++) {
			for(int j = 0; j < 9; j++) {
				gameBuf.append(Integer.toString(game[i][j]));
			}
		}
		
		SudokuGame sudokuGame = SudokuGame.createEmptyGame();
		sudokuGame.setCells(CellCollection.fromString(gameBuf.toString()));
		sudokuGame.setNote(params.comment);
		SudokuDatabase database = new SudokuDatabase(mContext);
		long gameId = database.insertSudoku(params.folderID, sudokuGame);
		if (mOnGenerateFinishedListener != null) {
			mOnGenerateFinishedListener.onGenerateFinished(gameId);
		}
	}

	@Override
	protected Void doInBackground(GenerateSudokuTaskParams... params) {
		for (GenerateSudokuTaskParams par : params) {
			generateSudoku(par);
		}
		return null;
	}
	
	public interface OnGenerateFinishedListener
	{
		/**
		 * Occurs when game generating is finished.
		 * 
		 * @param gameId Id of generated game or -1.
		 */
		void onGenerateFinished(long gameId);
	}

}
