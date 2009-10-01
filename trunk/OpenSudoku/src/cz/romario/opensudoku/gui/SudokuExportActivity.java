package cz.romario.opensudoku.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SudokuExportActivity extends Activity {
	
	/**
	 * Id of exported folder. -1 if all folders should be exported.
	 */
	public static final String EXTRA_FOLDER_ID = "FOLDER_ID";
	
	public static final long ALL_FOLDERS = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: add dialog theme
		
		// TODO: let user select export method (mail, file atd.)
		
		// TODO: AndroidUtils.isIntentAvailable, pokud available neni, vubec mail nenabizet
		sendByMailPrototype();
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
