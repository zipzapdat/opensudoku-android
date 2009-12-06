package cz.romario.opensudoku.gui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import cz.romario.opensudoku.R;
import cz.romario.opensudoku.gui.generating.GenerateSudokuTask;
import cz.romario.opensudoku.gui.generating.GenerateSudokuTaskParams;
import cz.romario.opensudoku.gui.generating.GenerateSudokuTask.OnGenerateFinishedListener;
import cz.romario.opensudoku.logic.Generator;

/**
 * This activity is responsible for generating new puzzles
 * TODO: This is not working now, code is not using this activity at all.
 * 
 * @author Martin Helff
 *
 */
public class SudokuGenerateActivity extends Activity {
	/**
	 * Id of folder to which game should be generated.
	 */
	public static final String EXTRA_FOLDER_ID = "FOLDER_ID";
	
	/**
	 * Difficulty of generated game.
	 */
	public static final String EXTRA_LEVEL = "LEVEL";
	
	private static final int DIALOG_PROGRESS = 1;
	
	private static final String TAG = "SudokuGenerateActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		//setContentView(R.layout.generate_sudoku);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.opensudoku);

		Intent intent = getIntent();
		GenerateSudokuTask generateTask = new GenerateSudokuTask(null);

		GenerateSudokuTaskParams param = new GenerateSudokuTaskParams();
		param.folderID = intent.getIntExtra(EXTRA_FOLDER_ID, 0);
		param.level = intent.getIntExtra(EXTRA_LEVEL, Generator.DIFFICULTY_EASY);
		
		generateTask.setOnGenerateFinishedListener(mOnGenerateFinishedListener);
		showDialog(DIALOG_PROGRESS);
		generateTask.execute(param);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		
		case DIALOG_PROGRESS:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setIndeterminate(true);
			progressDialog.setTitle(R.string.app_name);
			progressDialog.setMessage(getString(R.string.generating));
			return progressDialog;
		}
		
		return null;
	}
	
	private OnGenerateFinishedListener mOnGenerateFinishedListener = new OnGenerateFinishedListener() {

		@Override
		public void onGenerateFinished(long gameId) {
			dismissDialog(DIALOG_PROGRESS);
			// call finish, so this activity won't be part of history
			finish();
		}
	};

}
