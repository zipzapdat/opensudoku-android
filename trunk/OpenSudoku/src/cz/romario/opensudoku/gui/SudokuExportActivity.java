package cz.romario.opensudoku.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.xmlpull.v1.XmlSerializer;

import cz.romario.opensudoku.R;
import cz.romario.opensudoku.db.SudokuColumns;
import cz.romario.opensudoku.db.SudokuDatabase;
import cz.romario.opensudoku.utils.Const;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class SudokuExportActivity extends Activity {
	
	/**
	 * Id of exported folder. -1 if all folders should be exported.
	 */
	public static final String EXTRA_FOLDER_ID = "FOLDER_ID";
	
	public static final long ALL_FOLDERS = -1;
	
	private static final String TAG = SudokuExportActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.export_sudoku);
		
		long folderID;
		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_FOLDER_ID)) {
			folderID = intent.getLongExtra(EXTRA_FOLDER_ID, ALL_FOLDERS);
		} else {
			Log.d(TAG, "No 'FOLDER_ID' extra provided, exiting.");
			finish();
			return;
		}

		try {
			// TODO: multiple export methods (file, mail)
			// TODO: integration with OI file manager
			// TODO: asynctask
			saveToSdCard(folderID, "/sdcard/all_folders.opensudoku");
			
		// TODO: better exception handling
		} catch (Exception e) {
			Toast.makeText(this, R.string.unknown_export_error, Toast.LENGTH_LONG).show();
		}
		
		finish();
	}
	
	private void saveToSdCard(long folderID, String fileName) {
		long start = System.currentTimeMillis();

		SudokuDatabase database = null;
		Cursor cursor = null;
		Writer writer = null;
		try {
			database = new SudokuDatabase(getApplicationContext());
			
			cursor = database.exportSudoku(folderID);
			
			XmlSerializer serializer = Xml.newSerializer();
			writer = new BufferedWriter(new FileWriter(fileName, false));
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "opensudoku");
			serializer.attribute("", "version", "2");
			
			long currentFolderId = -1;
			while (cursor.moveToNext()) {
				if (currentFolderId != cursor.getLong(cursor.getColumnIndex("folder_id"))) {
					// next folder
					if (currentFolderId != -1) {
						serializer.endTag("", "folder");
					}
					currentFolderId = cursor.getLong(cursor.getColumnIndex("folder_id"));
					serializer.startTag("", "folder");
					attribute(serializer, "name", cursor, "folder_name");
					attribute(serializer, "created", cursor, "folder_created");
				}
				
				String data = cursor.getString(cursor.getColumnIndex(SudokuColumns.DATA));
				if (data != null) {
					serializer.startTag("", "game");
					attribute(serializer, "created", cursor, SudokuColumns.CREATED);
					attribute(serializer, "state", cursor, SudokuColumns.STATE);
					attribute(serializer, "time", cursor, SudokuColumns.TIME);
					attribute(serializer, "last_played", cursor, SudokuColumns.LAST_PLAYED);
					attribute(serializer, "data", cursor, SudokuColumns.DATA);
					attribute(serializer, "note", cursor, SudokuColumns.PUZZLE_NOTE);
					serializer.endTag("", "game");
				}
			}
			if (currentFolderId != -1) {
				serializer.endTag("", "folder");
			}
			
			serializer.endTag("", "opensudoku");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (cursor != null) cursor.close();
			if (database != null) database.close();
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			
		}
		
		long end = System.currentTimeMillis();
		
		Toast.makeText(this, getString(R.string.puzzles_exported_to_file, fileName), Toast.LENGTH_LONG).show();

		Log.i(Const.TAG, String.format("Exported in %f seconds.",
				(end - start) / 1000f));
		
	}
	
	private void attribute(XmlSerializer serializer, String attributeName, Cursor cursor, String columnName) throws IllegalArgumentException, IllegalStateException, IOException {
		String value = cursor.getString(cursor.getColumnIndex(columnName));
		if (value != null) {
			serializer.attribute("", attributeName, value);
		}
	}
	
	
	
	private void sendByMailPrototype() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, getTestData());
        try {
            startActivity(Intent.createChooser(intent, "TODO: vyber kam"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "TODO: no way to share folder", Toast.LENGTH_SHORT).show();
        } 
	}
	
	private String getTestData() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		"<opensudoku>"+
		"  <name>Gnome-Sudoku Easy</name>"+
		"  <author>romario333</author>"+
		"  <description></description>"+
		"  <comment></comment>"+
		"  <created>2009-09-16</created>"+
		"  <source>gnome-sudoku</source>"+
		"  <level>easy</level>"+
		"  <sourceURL>http://opensudoku.eu/puzzles</sourceURL>"+
		"  <game data=\"379000014060010070080009005435007000090040020000800436900700080040080050850000249\" />"+
		"</opensudoku>";

	}

}
