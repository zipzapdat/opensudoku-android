package cz.romario.opensudoku.gui.importing;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import cz.romario.opensudoku.R;
import cz.romario.opensudoku.db.SudokuDatabase;
import cz.romario.opensudoku.db.SudokuInvalidFormatException;
import cz.romario.opensudoku.game.FolderInfo;
import cz.romario.opensudoku.utils.Const;

public abstract class AbstractImportTask extends
		AsyncTask<Void, Integer, Boolean> {
	static final int NUM_OF_PROGRESS_UPDATES = 20;

	protected Context mContext;
	private ProgressBar mProgressBar;
	
	private OnImportFinishedListener mOnImportFinishedListener;
	
	private SudokuDatabase mDatabase;
	private FolderInfo mFolder; // currently processed folder
	private int mFolderCount; // count of processed folders
	private int mGameCount; //count of processed puzzles
	private String mImportError;
	private boolean mImportSuccessful;
	
	public void initialize(Context context, ProgressBar progressBar) {
		mContext = context;
		mProgressBar = progressBar;
	}
	
	public void setOnImportFinishedListener(OnImportFinishedListener listener) {
		mOnImportFinishedListener = listener;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {

		try {
			return processImportInternal();
		} catch (Exception e) {
			Log.e(Const.TAG, "Exception occurred during import.", e);
			setError(mContext.getString(R.string.unknown_import_error));
		}

		return false;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if (values.length == 2) {
			mProgressBar.setMax(values[1]);
		}
		mProgressBar.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			
			if (mFolderCount == 1) {
				Toast.makeText(mContext, mContext.getString(R.string.puzzles_saved, mFolder.name), 
						Toast.LENGTH_LONG).show();
			} else if (mFolderCount > 1) {
				Toast.makeText(mContext, mContext.getString(R.string.folders_created, mFolderCount), 
						Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(mContext, mImportError, Toast.LENGTH_LONG).show();
		}

		if (mOnImportFinishedListener != null) {
			long folderId = -1;
			if (mFolderCount == 1) {
				folderId = mFolder.id;
			}
			mOnImportFinishedListener.onImportFinished(result, folderId);
		}
	}

	private Boolean processImportInternal() {
		mImportSuccessful = true;
		
		long start = System.currentTimeMillis();

		mDatabase = new SudokuDatabase(mContext);
		try {
			mDatabase.beginTransaction();
			
			// let subclass handle the import
			processImport();
			
			mDatabase.setTransactionSuccessful();
		} catch ( SudokuInvalidFormatException e) {
			setError(mContext.getString(R.string.invalid_format));
		} finally {
			mDatabase.endTransaction();
			mDatabase.close();
			mDatabase = null;
		}
		

		if (mFolderCount == 0 && mGameCount == 0) {
			setError(mContext.getString(R.string.no_puzzles_found));
			return false;
		}

		long end = System.currentTimeMillis();

		Log.i(Const.TAG, String.format("Imported in %f seconds.",
				(end - start) / 1000f));
		
		return mImportSuccessful;
	}

	/**
	 * Subclasses should do all import work in this method.
	 * 
	 * @return
	 */
	protected abstract void processImport() throws SudokuInvalidFormatException;

	/**
	 * Imports folder with given name. 
	 * 
	 * If <code>appendToExistingFolder</code> is true, folder with given <code>name</code>
	 * won't be created if it already exists.
	 * 
	 * If <code>appendToExistingFolder</code> is false, new folder is created
	 * every time this method is called.
	 * 
	 * @param name
	 * @param appendToExistingFolder
	 */
	protected void importFolder(String name, boolean appendToExistingFolder) {
		if (mDatabase == null) {
			throw new IllegalStateException("Database is not opened.");
		}
		
		mFolderCount++;
		
		mFolder = null;
		if (appendToExistingFolder) {
			mFolder = mDatabase.findFolder(name);
		}
		if (mFolder == null) {
			mFolder = new FolderInfo();
			mFolder.name = name;
			mFolder.id = mDatabase.insertFolder(mFolder.name);
		}
	}
	
	/**
	 * Imports game. Game will be stored in folder, which was set by {@link #importFolder(String, boolean)}
	 * method. 
	 * 
	 * @param game
	 * @throws SudokuInvalidFormatException
	 */
	protected void importGame(String data) throws SudokuInvalidFormatException {
		if (mDatabase == null) {
			throw new IllegalStateException("Database is not opened.");
		}
		
		mGameCount++;
		mDatabase.insertSudokuImport(mFolder.id, data);
	}

	protected void setError(String error) {
		mImportError = error;
		mImportSuccessful = false;
	}
	
	public interface OnImportFinishedListener
	{
		/**
		 * Occurs when import is finished.
		 * 
		 * @param importSuccessful Indicates whether import was successful.
		 * @param folderId Contains id of imported folder, or -1 if multiple folders were imported.
		 */
		void onImportFinished(boolean importSuccessful, long folderId);
	}
	
}
