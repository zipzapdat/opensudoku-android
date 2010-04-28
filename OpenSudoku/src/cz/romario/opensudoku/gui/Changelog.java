package cz.romario.opensudoku.gui;

import cz.romario.opensudoku.R;
import cz.romario.opensudoku.utils.AndroidUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Changelog {
	
	private static final String PREF_FILE_CHANGELOG = "changelog";
	
	private Context mContext;
	private SharedPreferences mPrefs;
	private AlertDialog mChangelogDialog;
	
	public Changelog(Context context) {
		mContext = context;
		mPrefs = mContext.getSharedPreferences(PREF_FILE_CHANGELOG, Context.MODE_PRIVATE);
		
		mChangelogDialog = new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_menu_info_details)
		.setTitle(R.string.what_is_new)
		.setMessage(R.string.changelog)
		.setPositiveButton(R.string.close, null).create();
	}
	
	public void showOnFirstRun() {
		String versionKey = "changelog_" + AndroidUtils.getAppVersionCode(mContext);

		if (!mPrefs.getBoolean(versionKey, false)) {
			mChangelogDialog.show();
			
			Editor editor = mPrefs.edit();
			editor.putBoolean(versionKey, true);
			editor.commit();
		}
	}

	

}
