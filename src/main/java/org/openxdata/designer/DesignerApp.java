package org.openxdata.designer;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Locale;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Theme;
import org.apache.pivot.wtk.Window;

/**
 * The main entry point of the form designer application.
 * 
 * @author brent
 * 
 */
public class DesignerApp implements Application {

	public static final String LANGUAGE_KEY = "language";

	private Window window;

	public void startup(Display display, Map<String, String> properties)
			throws Exception {

		String language = properties.get(LANGUAGE_KEY);
		Locale locale = (language == null) ? Locale.getDefault() : new Locale(
				language);
		Resources resources = new Resources(getClass().getName(), locale);

		Theme theme = Theme.getTheme();
		Font font = theme.getFont();

		// Search for a font that can support the sample string
		String sampleResource = (String) resources.get("greeting");
		if (font.canDisplayUpTo(sampleResource) != -1) {
			Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getAllFonts();

			for (int i = 0; i < fonts.length; i++) {
				if (fonts[i].canDisplayUpTo(sampleResource) == -1) {
					theme.setFont(fonts[i].deriveFont(Font.PLAIN, 12));
					break;
				}
			}
		}

		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		window = (Window) bxmlSerializer.readObject(
				DesignerApp.class.getResource("designer.bxml"), resources);
		window.open(display);
	}

	public boolean shutdown(boolean optional) throws Exception {

		if (window != null)
			window.close();

		return false;
	}

	public void suspend() throws Exception {
		// TODO Auto-generated method stub
	}

	public void resume() throws Exception {
		// TODO Auto-generated method stub
	}

}
