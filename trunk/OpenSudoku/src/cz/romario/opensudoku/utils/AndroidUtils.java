package cz.romario.opensudoku.utils;

import java.util.List;

import cz.romario.opensudoku.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;

public class AndroidUtils {
	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(intent,
	                    PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	public static void setThemeFromPreferences(Context context) {
		SharedPreferences gameSettings = PreferenceManager.getDefaultSharedPreferences(context);
		String theme = gameSettings.getString("theme", "defaulti");
		if (theme.equals("defaultii")) {
			context.setTheme(R.style.Theme_DefaultII);
		} else if (theme.equals("paperi")) {
			context.setTheme(R.style.Theme_PaperI);
		} else if (theme.equals("paperii")) {
			context.setTheme(R.style.Theme_PaperII);
		} else {
			context.setTheme(R.style.Theme_DefaultI);
		}
	}
}
