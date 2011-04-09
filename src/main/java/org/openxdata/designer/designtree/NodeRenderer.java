package org.openxdata.designer.designtree;

import java.awt.Color;
import java.awt.Font;

import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.TreeView.NodeCheckState;
import org.openxdata.designer.util.Form;
import org.openxdata.designer.util.Page;
import org.openxdata.designer.util.Question;

public class NodeRenderer extends Label implements TreeView.NodeRenderer {

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
			if (node instanceof Form) {
				Form form = (Form) node;
				text.append(form.getName());
			} else if (node instanceof Page) {
				Page page = (Page) node;
				text.append(page.getName());
			} else if (node instanceof Question) {
				Question question = (Question) node;
				text.append(question.getText());
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
		return node.toString();
	}

}
