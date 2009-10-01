package cz.romario.opensudoku.gui.importing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.net.Uri;
import cz.romario.opensudoku.R;
import cz.romario.opensudoku.db.SudokuInvalidFormatException;

public class OpenSudokuImportTask extends AbstractImportTask {

	private Uri mUri;

	public OpenSudokuImportTask(Uri uri) {
		mUri = uri;
	}

	@Override
	protected void processImport() throws SudokuInvalidFormatException {
		try {
			java.net.URI juri;
			juri = new java.net.URI(mUri.getScheme(), mUri
					.getSchemeSpecificPart(), mUri.getFragment());
			InputStreamReader isr = new InputStreamReader(juri.toURL()
					.openStream());
			try {
				importXml(isr);
			} finally {
				isr.close();
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void importXml(Reader in) throws SudokuInvalidFormatException {
		BufferedReader inBR = new BufferedReader(in);
		/*
		 * while((s=in.readLine())!=null){ Log.i(tag, "line: "+s); }
		 */

		// parse xml
		XmlPullParserFactory factory;
		XmlPullParser xpp;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			xpp = factory.newPullParser();
			xpp.setInput(inBR);
			int eventType = xpp.getEventType();
			String rootTag = "";
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					rootTag = xpp.getName();
					if (rootTag.equals("opensudoku")) {
						String version = xpp.getAttributeValue(null, "version");
						if (version == null) {
							// no version provided, assume that it's version 1
							importV1(xpp);
						} else if (version.equals("2")) {
							importV2(xpp);
						} else {
							setError("Unknown version of data.");
						}
					} else {
						setError(mContext.getString(R.string.invalid_format));
						return;
					}
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void importV2(XmlPullParser parser)
		throws XmlPullParserException, IOException {
		// TODO: version 2 parsing
	}

	private void importV1(XmlPullParser parser)
			throws XmlPullParserException, IOException, SudokuInvalidFormatException {
		int eventType = parser.getEventType();
		String lastTag = "";

		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				lastTag = parser.getName();
				if (lastTag.equals("game")) {
					importGame(parser.getAttributeValue(null, "data"));
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				lastTag = "";
			} else if (eventType == XmlPullParser.TEXT) {
				if (lastTag.equals("name")) {
					importFolder(parser.getText(), false);
				}

			}
			eventType = parser.next();
		}

		
	}

}
