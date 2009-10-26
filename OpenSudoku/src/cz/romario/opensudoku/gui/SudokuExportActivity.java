package cz.romario.opensudoku.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import cz.romario.opensudoku.R;
import cz.romario.opensudoku.db.SudokuColumns;
import cz.romario.opensudoku.db.SudokuDatabase;
import cz.romario.opensudoku.game.FolderInfo;
import cz.romario.opensudoku.gui.exporting.FileExportTask;
import cz.romario.opensudoku.gui.exporting.FileExportTaskParams;
import cz.romario.opensudoku.gui.exporting.FileExportTaskResult;
import cz.romario.opensudoku.gui.exporting.FileExportTask.OnExportFinishedListener;
import cz.romario.opensudoku.utils.Const;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SudokuExportActivity extends Activity {
	
	/**
	 * Id of folder to export. If -1, all folders will be exported.
	 */
	public static final String EXTRA_FOLDER_ID = "FOLDER_ID";
	/**
	 * Id of sudoku to export. 
	 */
//	public static final String EXTRA_SUDOKU_ID = "SUDOKU_ID";
	
	public static final long ALL_FOLDERS = -1;
	
	private static final int DIALOG_SELECT_EXPORT_METHOD = 1;
	
	private static final String TAG = SudokuExportActivity.class.getSimpleName();
	
	private FileExportTask mFileExportTask;
	private FileExportTaskParams mExportParams;
	private ProgressDialog mProgressDialog;
	
	private EditText mFileNameEdit;
	private EditText mDirectoryEdit;
	private Button mSaveButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sudoku_export);
		
		mFileNameEdit = (EditText)findViewById(R.id.filename);
		mDirectoryEdit = (EditText)findViewById(R.id.directory);
		mSaveButton = (Button)findViewById(R.id.save_button);
		mSaveButton.setOnClickListener(mOnSaveClickListener);

		mFileExportTask = new FileExportTask(this);
		mExportParams = new FileExportTaskParams();
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setTitle(R.string.app_name);
		mProgressDialog.setMessage(getString(R.string.exporting));

		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_FOLDER_ID)) {
			mExportParams.folderID = intent.getLongExtra(EXTRA_FOLDER_ID, ALL_FOLDERS);
//		} else if (intent.hasExtra(EXTRA_SUDOKU_ID)) {
//			mExportParams.sudokuID = intent.getLongExtra(EXTRA_SUDOKU_ID, 0);
		} else {
			Log.d(TAG, "No 'FOLDER_ID' extra provided, exiting.");
			finish();
			return;
		}
		
		String fileName = null;
		String timestamp = DateFormat.format("yyyy-MM-dd", new Date()).toString();
		if (mExportParams.folderID == -1) {
			fileName = "all-folders-" + timestamp;
		} else {
			SudokuDatabase database = new SudokuDatabase(getApplicationContext());
			FolderInfo folder = database.getFolderInfo(mExportParams.folderID);
			if (folder == null) {
				Log.d(TAG, String.format("Folder with id %s not found, exiting.", mExportParams.folderID));
			}
			fileName = folder.name + "-" + timestamp;
			database.close();
		}
		mFileNameEdit.setText(fileName);
		
		

		//showDialog(DIALOG_SELECT_EXPORT_METHOD);
	}
	
	private OnClickListener mOnSaveClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			exportToFile(mDirectoryEdit.getText().toString(), mFileNameEdit.getText().toString());
		}
	};

	
//	@Override
//	protected Dialog onCreateDialog(int id) {
//		switch (id) {
//		case DIALOG_SELECT_EXPORT_METHOD:
//	        
//			final int EXPORT_METHOD_FILE = 0;
//			final int EXPORT_METHOD_SDCARD = 1;
//			CharSequence[] exportMethods = new CharSequence[] {
//					getString(R.string.save_to_sdcard),
//					getString(R.string.send_by_mail)
//			};
//			
//			return new AlertDialog.Builder(this)
//	        .setTitle(R.string.export)
//	        .setItems(exportMethods, 
//	        		new DialogInterface.OnClickListener() {
//	            public void onClick(DialogInterface dialog, int which) {
//	            	
//	            	switch (which) {
//	            	case EXPORT_METHOD_FILE:
//	            		exportToFile();
//	            		break;
//	            	case EXPORT_METHOD_SDCARD:
//	            		exportToMail();
//	            		break;
//	            	}
//	            }
//	        })
//	        .create();
//		}
//		
//		return null;
//	}
	
	private void exportToFile(String directory, String filename) {
		File sdcard = new File("/sdcard");
		if (!sdcard.exists()) {
			Toast.makeText(SudokuExportActivity.this, R.string.sdcard_not_found, Toast.LENGTH_LONG);
			finish();
		}
		
		// TODO: ask user what to do when file already exists
		
		mFileExportTask.setOnExportFinishedListener(new OnExportFinishedListener() {
			
			@Override
			public void onExportFinished(FileExportTaskResult result) {
				mProgressDialog.dismiss();
				
				if (result.successful) {
		            Toast.makeText(SudokuExportActivity.this, getString(R.string.puzzles_have_been_saved, result.file), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SudokuExportActivity.this, getString(R.string.unknown_export_error), Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});
		
		mExportParams.file = new File(directory, filename + ".opensudoku");
		
		mProgressDialog.show();
		mFileExportTask.execute(mExportParams);
	}
	
//	private void exportToMail() {
//		
//		mFileExportTask.setOnExportFinishedListener(new OnExportFinishedListener() {
//			
//			@Override
//			public void onExportFinished(FileExportTaskResult result) {
//				mProgressDialog.dismiss();
//
//				if (result.successful) {
//					Intent intent = new Intent(Intent.ACTION_SEND);
//					intent.setType(Const.MIME_TYPE_OPENSUDOKU);
//					intent.putExtra(Intent.EXTRA_TEXT, "Puzzles attached.");
//					intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(result.file));
//					
//			        try { 
//			            startActivity(intent);
//			        } catch (android.content.ActivityNotFoundException ex) {
//			            Toast.makeText(SudokuExportActivity.this, "TODO: no way to share folder", Toast.LENGTH_SHORT).show();
//			        }
//				} else {
//					// TODO: what to do?
//					Toast.makeText(SudokuExportActivity.this, "TODO: not successful, maybe I should not show myself?.", Toast.LENGTH_LONG).show();
//				}
//				
//				finish();
//			}
//		});
//		
//		// TODO: tady by bylo hezky, aby bylo videt, ze po nem chci vygenerovat temp soubor
//		
//		mProgressDialog.show();
//		mFileExportTask.execute(mExportParams);
//	}
	
	
	
	
	
	private void ensureOpenSudokuDirectory() throws SDCardNotFoundException {
		File sdcard = new File("/sdcard");
		if (!sdcard.exists()) {
			throw new SDCardNotFoundException();
		}
		
		File ouDir = new File("/sdcard/opensudoku");
		if (!ouDir.exists()) {
			ouDir.mkdir();
		}
	}
	
	public static class SDCardNotFoundException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7187487062106017855L;

		public SDCardNotFoundException() {
			super("SD card not found.");
		}
	}
	
}
