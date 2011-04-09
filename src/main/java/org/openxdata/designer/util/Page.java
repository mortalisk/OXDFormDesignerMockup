package org.openxdata.designer.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.ListListener;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.ListenerList;
import org.fcitmuk.epihandy.PageDef;
import org.fcitmuk.epihandy.QuestionDef;

public class Page extends org.fcitmuk.epihandy.PageDef implements FormElement,
		List<Question> {

	public Page() {
		this("Unnamed Question", (short) -1, new Vector<Question>());
	}

	protected Page(String name, short pageNo, Vector<?> questions) {
		super(name, pageNo, questions);
	}

	public Page(PageDef pageDef) {

		super(pageDef);

		@SuppressWarnings("unchecked")
		Vector<QuestionDef> questions = (Vector<QuestionDef>) getQuestions();
		for (int i = 0; i < questions.size(); i++) {
			QuestionDef questionDef = questions.elementAt(i);
			Question question = new Question(questionDef);
			questions.setElementAt(question, i);
		}
	}

	public int remove(Question item) {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		for (int i = 0; i < questions.size(); i++) {
			if (item.equals(questions.get(i))) {
				Question removedQuestion = questions.remove(i);
				listenerList.itemsRemoved(this, i, new ArrayList<Question>(
						removedQuestion));
				return i;
			}
		}
		return -1;
	}

	public Question get(int index) {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		return (Question) questions.get(index);
	}

	public int indexOf(Question item) {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		return questions.indexOf(item);
	}

	public boolean isEmpty() {
		return getQuestions().isEmpty();
	}

	class QuestionComparator implements Comparator<Question> {

		public int compare(Question o1, Question o2) {
			if (o1 == o2 && o1 == null)
				return 0;

			if (o1 == null)
				return -1;

			if (o2 == null)
				return 1;

			return o1.toString().compareTo(o2.toString());
		}

	}

	private Comparator<Question> comparator = new QuestionComparator();

	public Comparator<Question> getComparator() {
		return comparator;
	}

	public Iterator<Question> iterator() {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		return questions.iterator();
	}

	public int add(Question item) {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		int index = -1;
		synchronized (questions) {
			index = questions.size();
			questions.add(item);
			listenerList.itemInserted(this, index);
			return index;
		}
	}

	public void insert(Question item, int index) {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		questions.insertElementAt(item, index);
		listenerList.itemInserted(this, index);
	}

	public Question update(int index, Question item) {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		Question exiled = null;
		synchronized (questions) {
			exiled = questions.get(index);
			questions.setElementAt(item, index);
			listenerList.itemUpdated(this, index, item);
		}
		return exiled;
	}

	public Sequence<Question> remove(int index, int count) {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		Sequence<Question> removedQuestions = new ArrayList<Question>();
		for (int i = 0; i < count && index + i < questions.size(); i++) {
			Question removedQuestion = questions.remove(index);
			removedQuestions.add(removedQuestion);
		}
		listenerList.itemsRemoved(this, index, removedQuestions);
		return removedQuestions;
	}

	public void clear() {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		questions.clear();
	}

	public int getLength() {
		@SuppressWarnings("unchecked")
		Vector<Question> questions = (Vector<Question>) getQuestions();
		return questions.size();
	}

	public void setComparator(Comparator<Question> comparator) {
		this.comparator = comparator;
	}

	private ListListenerList<Question> listenerList = new ListListenerList<Question>();

	public ListenerList<ListListener<Question>> getListListeners() {
		return listenerList;
	}

}
