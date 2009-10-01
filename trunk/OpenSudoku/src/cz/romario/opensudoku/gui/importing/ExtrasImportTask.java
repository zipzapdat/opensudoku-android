package cz.romario.opensudoku.gui.importing;

import cz.romario.opensudoku.db.SudokuInvalidFormatException;

public class ExtrasImportTask extends AbstractImportTask {

	private String mFolderName;
	private String mGames;
	private boolean mAppendToFolder;

	public ExtrasImportTask(String folderName, String games, boolean appendToFolder) {
		mFolderName = folderName;
		mGames = games;
		mAppendToFolder = appendToFolder;
	}
	
	@Override
	protected void processImport()  throws SudokuInvalidFormatException {
		importFolder(mFolderName, mAppendToFolder);
		
		for (String game : mGames.split("\n")) {
			importGame(game);
		}
	}
	
}
