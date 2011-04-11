package org.openxdata.designer.designtree;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.Menu.Section;
import org.apache.pivot.wtk.MenuBar;
import org.apache.pivot.wtk.TreeView;
import org.openxdata.designer.util.Form;
import org.openxdata.designer.util.Page;
import org.openxdata.designer.util.Question;

public class MenuHandler implements org.apache.pivot.wtk.MenuHandler {

	@BXML
	private Dialog formDialog;

	@BXML
	private Dialog pageDialog;

	@BXML
	private Dialog questionDialog;

	public void configureMenuBar(Component component, MenuBar menuBar) {
	}

	public void cleanupMenuBar(Component component, MenuBar menuBar) {
	}

	public boolean configureContextMenu(Component component, Menu menu, int x,
			int y) {

		final TreeView designTree = (TreeView) component;
		Path clickedPath = designTree.getNodeAt(y);
		List<?> treeData = designTree.getTreeData();

		if (clickedPath != null) {

			List<?> deepestBranch = treeData;

			// Traverse the path until we reach the deepest branch
			for (int i = 0; i < clickedPath.getLength() - 1; i++) {
				List<?> nextBranch = (List<?>) deepestBranch.get(clickedPath
						.get(i));
				deepestBranch = nextBranch;
			}

			// Get the index of the selected item in the deepest branch
			Integer deepestBranchIndex = clickedPath.get(clickedPath
					.getLength() - 1);

			Object clickedParent = deepestBranch;
			Object clickedObject = deepestBranch.get(deepestBranchIndex);

			// TODO: Use localized Strings
			Section section = new Section();
			if (clickedObject instanceof Form) {

				final Form form = (Form) clickedObject;

				Menu.Item newPageItem = new Menu.Item("Add Page");
				Menu.Item propertiesItem = new Menu.Item("Properties...");

				newPageItem.setAction(new Action() {
					@Override
					public void perform(Component source) {
						form.addPage();
					}
				});

				propertiesItem.setAction(new Action() {
					@Override
					public void perform(Component source) {
						formDialog.open(designTree.getDisplay(),
								designTree.getWindow());
					}
				});

				section.add(newPageItem);
				section.add(propertiesItem);
			} else if (clickedObject instanceof Page) {

				final Form form = (Form) clickedParent;
				final Page page = (Page) clickedObject;

				Menu.Item newQuestionItem = new Menu.Item("New Question");
				Menu.Item removePageItem = new Menu.Item("Remove Page");
				Menu.Item propertiesItem = new Menu.Item("Properties...");

				newQuestionItem.setAction(new Action() {
					@Override
					public void perform(Component source) {
						page.add(new Question());
					}
				});

				removePageItem.setAction(new Action() {
					@Override
					public void perform(Component source) {
						form.remove(page);
					}
				});

				propertiesItem.setAction(new Action() {
					@Override
					public void perform(Component source) {
						pageDialog.open(designTree.getDisplay(),
								designTree.getWindow());
					}
				});

				section.add(newQuestionItem);
				section.add(removePageItem);
				section.add(propertiesItem);
			} else if (clickedObject instanceof Question) {

				@SuppressWarnings("unchecked")
				final List<Question> questionList = (List<Question>) clickedParent;
				final Question question = (Question) clickedObject;

				Menu.Item removeQuestionItem = new Menu.Item("Remove Question");
				Menu.Item propertiesItem = new Menu.Item("Properties...");

				removeQuestionItem.setAction(new Action() {
					public void perform(Component source) {
						questionList.remove(question);
					}
				});

				propertiesItem.setAction(new Action() {
					@Override
					public void perform(Component source) {
						questionDialog.open(designTree.getDisplay(),
								designTree.getWindow());
					}
				});

				section.add(removeQuestionItem);
				section.add(propertiesItem);
			}

			// Only add menu section if there were items added
			if (section.getLength() > 0)
				menu.getSections().add(section);
		}

		return false;
	}
}
