package org.openxdata.designer.formtree;

import java.awt.Color;
import java.awt.Font;

import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.TreeView.NodeCheckState;
import org.apache.pivot.xml.Element;
import org.apache.pivot.xml.TextNode;

public class NodeRenderer extends Label implements TreeView.NodeRenderer {

	private int maxLength = 256;

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		validate();
	}

	public void render(Object node, Path path, int rowIndex, TreeView treeView,
			boolean expanded, boolean selected, NodeCheckState checkState,
			boolean highlighted, boolean disabled) {
		if (node != null) {
			StringBuffer text = new StringBuffer();
			if (node instanceof Element) {
				Element element = (Element) node;
				text.append("<");
				text.append(element.getName());
				text.append(">");
			} else if (node instanceof TextNode) {
				TextNode treeNode = (TextNode) node;
				text.append(treeNode.getText());
				if (text.length() > maxLength)
					text.replace(maxLength, text.length(), "...");
			} else
				throw new IllegalArgumentException(
						"Unrecognized tree node type: "
								+ node.getClass().getCanonicalName());

			setText(text.toString());

			// Borrow renderer font from tree view
			Font font = (Font) treeView.getStyles().get("font");
			getStyles().put("font", font);

			Color color;
			if (treeView.isEnabled() && !disabled) {
				if (selected) {
					if (treeView.isFocused()) {
						color = (Color) treeView.getStyles().get(
								"selectionColor");
					} else {
						color = (Color) treeView.getStyles().get(
								"inactiveSelectionColor");
					}
				} else {
					color = (Color) treeView.getStyles().get("color");
				}
			} else {
				color = (Color) treeView.getStyles().get("disabledColor");
			}

			getStyles().put("color", color);
		}
	}

	public String toString(Object node) {
		String string;
		if (node instanceof Element) {
			Element element = (Element) node;
			string = element.getName();
		} else if (node instanceof TextNode) {
			TextNode textNode = (TextNode) node;
			string = textNode.getText();
		} else {
			throw new IllegalArgumentException();
		}
		return string;
	}

}
