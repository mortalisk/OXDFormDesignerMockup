package org.openxdata.designer.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.ListListener;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.ListenerList;
import org.fcitmuk.epihandy.FormDef;
import org.fcitmuk.epihandy.PageDef;

/**
 * 
 * @author brent
 * 
 */
public class Form extends org.fcitmuk.epihandy.FormDef implements List<Page> {

	public Form(FormDef formDef) {

		super(formDef);

		// Patch up pages into alternative model representation
		@SuppressWarnings("unchecked")
		Vector<PageDef> pages = (Vector<PageDef>) getPages();
		for (int i = 0; i < pages.size(); i++) {
			PageDef pageDef = pages.elementAt(i);
			Page page = new Page(pageDef);
			pages.setElementAt(page, i);
		}

		// TODO: Patch up other references
	}

	@Override
	public void addPage() {
		@SuppressWarnings("unchecked")
		Vector<PageDef> pages = (Vector<PageDef>) getPages();
		synchronized (pages) {
			int pageNum = pages.size() + 1;
			Page newPage = new Page("Page" + pageNum, (short) pageNum,
					new Vector<Question>());
			add(newPage); // Method sends notifications
		}
	}

	public int remove(Page item) {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		for (int i = 0; i < pages.size(); i++) {
			if (item.equals(pages.get(i))) {
				Page removedPage = pages.remove(i);
				listenerList.itemsRemoved(this, i, new ArrayList<Page>(
						removedPage));
				return i;
			}
		}
		return -1;
	}

	public Page get(int index) {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		return (Page) pages.get(index);
	}

	public int indexOf(Page item) {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		return pages.indexOf(item);
	}

	public boolean isEmpty() {
		return getPages().isEmpty();
	}

	class PageComparator implements Comparator<Page> {

		public int compare(Page o1, Page o2) {
			if (o1 == o2 && o1 == null)
				return 0;

			if (o1 == null)
				return -1;

			if (o2 == null)
				return 1;

			return o1.toString().compareTo(o2.toString());
		}

	}

	private Comparator<Page> comparator = new PageComparator();

	public Comparator<Page> getComparator() {
		return comparator;
	}

	public Iterator<Page> iterator() {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		return pages.iterator();
	}

	public int add(Page item) {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		int index = -1;
		synchronized (pages) {
			index = pages.size();
			pages.add(item);
			listenerList.itemInserted(this, index);
			return index;
		}
	}

	public void insert(Page item, int index) {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		pages.insertElementAt(item, index);
		listenerList.itemInserted(this, index);
	}

	public Page update(int index, Page item) {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		Page exiled = null;
		synchronized (pages) {
			exiled = pages.get(index);
			pages.setElementAt(item, index);
		}
		listenerList.itemUpdated(this, index, exiled);
		return exiled;
	}

	public Sequence<Page> remove(int index, int count) {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		Sequence<Page> removedPages = new ArrayList<Page>();
		for (int i = 0; i < count && index + i < pages.size(); i++) {
			Page removedPage = pages.remove(index);
			removedPages.add(removedPage);
		}
		listenerList.itemsRemoved(this, index, removedPages);
		return removedPages;
	}

	public void clear() {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		pages.clear();
	}

	public int getLength() {
		@SuppressWarnings("unchecked")
		Vector<Page> pages = (Vector<Page>) getPages();
		return pages.size();
	}

	public void setComparator(Comparator<Page> comparator) {
		this.comparator = comparator;
	}

	private ListListenerList<Page> listenerList = new ListListenerList<Page>();

	public ListenerList<ListListener<Page>> getListListeners() {
		return listenerList;
	}
}
