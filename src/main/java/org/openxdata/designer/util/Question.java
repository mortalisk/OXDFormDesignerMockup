package org.openxdata.designer.util;

import org.fcitmuk.epihandy.QuestionDef;

public class Question extends org.fcitmuk.epihandy.QuestionDef implements
		FormElement {

	public Question() {
		setText("Unnamed Question");
	}

	public Question(QuestionDef questionDef) {
		super(questionDef);
	}
}
