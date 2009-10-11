package cz.romario.opensudoku.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.xmlpull.v1.XmlSerializer;

import cz.romario.opensudoku.R;
import cz.romario.opensudoku.db.SudokuColumns;
import cz.romario.opensudoku.db.SudokuDatabase;
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
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class SudokuExportActivity extends Activity {
	
	/**
	 * Id of folder to export. If -1, all folders will be exported.
	 */
	public static final String EXTRA_FOLDER_ID = "FOLDER_ID";
	/**
	 * Id of sudoku to export. 
	 */
	public static final String EXTRA_SUDOKU_ID = "SUDOKU_ID";
	
	public static final long ALL_FOLDERS = -1;
	
	private static final int DIALOG_SELECT_EXPORT_METHOD = 1;
	
	private static final String TAG = SudokuExportActivity.class.getSimpleName();
	
	private FileExportTask mFileExportTask;
	private FileExportTaskParams mExportParams;
	private ProgressDialog mProgressDialog;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: vymazat layout, jestli nebude treba
		//setContentView(R.layout.export_sudoku);
		

		mFileExportTask = new FileExportTask(this);
		mExportParams = new FileExportTaskParams();
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setIndeterminate(true);

		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_FOLDER_ID)) {
			mExportParams.folderID = intent.getLongExtra(EXTRA_FOLDER_ID, ALL_FOLDERS);
		} else if (intent.hasExtra(EXTRA_SUDOKU_ID)) {
			mExportParams.sudokuID = intent.getLongExtra(EXTRA_SUDOKU_ID, 0);
		} else {
			Log.d(TAG, "No 'FOLDER_ID' extra provided, exiting.");
			finish();
			return;
		}

		showDialog(DIALOG_SELECT_EXPORT_METHOD);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SELECT_EXPORT_METHOD:
	        
			final int EXPORT_METHOD_FILE = 0;
			final int EXPORT_METHOD_SDCARD = 1;
			CharSequence[] exportMethods = new CharSequence[] {
					getString(R.string.save_to_sdcard),
					getString(R.string.send_by_mail)
			};
			
			return new AlertDialog.Builder(this)
	        .setTitle(R.string.share)
	        .setItems(exportMethods, 
	        		new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	switch (which) {
	            	case EXPORT_METHOD_FILE:
	            		exportToFile();
	            		break;
	            	case EXPORT_METHOD_SDCARD:
	            		exportToMail();
	            		break;
	            	}
	            }
	        })
	        .create();
		}
		
		return null;
	}
	
	private void exportToFile() {
		mFileExportTask.setOnExportFinishedListener(new OnExportFinishedListener() {
			
			@Override
			public void onExportFinished(FileExportTaskResult result) {
				// TODO: asi by melo byt na GUI threadu
				mProgressDialog.dismiss();
				
				if (result.successful) {
		            Toast.makeText(SudokuExportActivity.this, "TODO: successfully exported", Toast.LENGTH_SHORT).show();
				} else {
					// TODO: what to do?
					Toast.makeText(SudokuExportActivity.this, "TODO: not successful, maybe I should not show myself?.", Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});
		
		// TODO: pridat timestamp, moznost vyberu adresare a jmena souboru
		mExportParams.fileName = "/sdcard/all-folders.opensudoku";
		
		mProgressDialog.show();
		mFileExportTask.execute(mExportParams);
	}
	
	private void exportToMail() {
		
		mFileExportTask.setOnExportFinishedListener(new OnExportFinishedListener() {
			
			@Override
			public void onExportFinished(FileExportTaskResult result) {
				mProgressDialog.dismiss();

				if (result.successful) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType(Const.MIME_TYPE_OPENSUDOKU);
					intent.putExtra(Intent.EXTRA_TEXT, "Puzzles attached.");
					intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(result.file));
					
			        try { 
			            startActivity(intent);
			        } catch (android.content.ActivityNotFoundException ex) {
			            Toast.makeText(SudokuExportActivity.this, "TODO: no way to share folder", Toast.LENGTH_SHORT).show();
			        }
				} else {
					// TODO: what to do?
					Toast.makeText(SudokuExportActivity.this, "TODO: not successful, maybe I should not show myself?.", Toast.LENGTH_LONG).show();
				}
				
				finish();
			}
		});
		
		// TODO: tady by bylo hezky, aby bylo videt, ze po nem chci vygenerovat temp soubor
		
		mProgressDialog.show();
		mFileExportTask.execute(mExportParams);
	}
	
	
	
	
	private void sendByMailPrototype() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(Const.MIME_TYPE_OPENSUDOKU);
		//intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "Puzzles attached.");
		File f = new File("/sdcard/all_folders.opensudoku");
		//File.createTempFile(prefix, suffix)
		
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
		
        try { 
            startActivity(Intent.createChooser(intent, "TODO: vyber kam"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "TODO: no way to share folder", Toast.LENGTH_SHORT).show();
            
        } 
	}
}
