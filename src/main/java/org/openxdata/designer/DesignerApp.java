package org.openxdata.designer;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.io.FileList;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.DropAction;
import org.apache.pivot.wtk.HorizontalAlignment;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Manifest;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.Theme;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.VerticalAlignment;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.effects.OverlayDecorator;
import org.apache.pivot.xml.Element;
import org.apache.pivot.xml.XMLSerializer;

/**
 * The main entry point of the form designer application.
 * 
 * @author brent
 * 
 */
public class DesignerApp implements Application {

	public static final String LANGUAGE_KEY = "language";
	public static final String APPLICATION_KEY = "application";

	@BXML
	private TreeView formTree;

	private Window window;
	
	private OverlayDecorator promptDecorator = new OverlayDecorator();

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

		// Install this object as "application" in the default namespace
		bxmlSerializer.getNamespace().put(APPLICATION_KEY, this);

		window = (Window) bxmlSerializer.readObject(
				DesignerApp.class.getResource("designer.bxml"), resources);

		// Apply the binding annotations to this object
		bxmlSerializer.bind(this);

		Label prompt = new Label("Drag or paste XML here");
		prompt.getStyles().put("horizontalAlignment",
				HorizontalAlignment.CENTER);
		prompt.getStyles().put("verticalAlignment", VerticalAlignment.CENTER);
		promptDecorator.setOverlay(prompt);
		formTree.getDecorators().add(promptDecorator);

		window.open(display);
	}

	public DropAction drop(Manifest dragContent) {
		DropAction dropAction = null;

		try {
			FileList fileList = dragContent.getFileList();
			if (fileList.getLength() == 1) {
				File file = fileList.get(0);

				XMLSerializer xmlSerializer = new XMLSerializer();
				FileInputStream fileInputStream = null;
				try {
					try {
						fileInputStream = new FileInputStream(file);
						setDocument(xmlSerializer.readObject(fileInputStream));
					} finally {
						if (fileInputStream != null) {
							fileInputStream.close();
						}
					}
				} catch (Exception exception) {
					Prompt.prompt(exception.getMessage(), window);
				}

				window.setTitle(file.getName());

				dropAction = DropAction.COPY;
			} else {
				Prompt.prompt("Multiple files not supported.", window);
			}
		} catch (IOException exception) {
			Prompt.prompt(exception.getMessage(), window);
		}

		return dropAction;
	}

	private void setDocument(Element document) {
		// Remove prompt decorator
		if (promptDecorator != null) {
			formTree.getDecorators().remove(promptDecorator);
			promptDecorator = null;
		}

		ArrayList<Element> treeData = new ArrayList<Element>();
		treeData.add(document);
		formTree.setTreeData(treeData);

		Sequence.Tree.Path path = new Sequence.Tree.Path(0);
		formTree.expandBranch(path);
		formTree.setSelectedPath(path);
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
