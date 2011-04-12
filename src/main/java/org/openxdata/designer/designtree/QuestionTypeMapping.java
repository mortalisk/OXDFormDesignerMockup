package org.openxdata.designer.designtree;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.wtk.ListView;
import org.fcitmuk.epihandy.QuestionDef;

public class QuestionTypeMapping implements ListView.ItemBindMapping,
		ListView.ListDataBindMapping {

	public static final Map<String, Byte> QUESTION_VALUES = new LinkedHashMap<String, Byte>();
	public static final String QUESTION_TYPE_FIELD_PREFIX = "QTN_TYPE_";
	private static final Pattern uscorePattern = Pattern.compile("_(\\w)");
	
	// TODO: Use localized labels
	static {
		for (Field field : QuestionDef.class.getFields()) {
			if (field.getName().startsWith(QUESTION_TYPE_FIELD_PREFIX)) {
				String name = field.getName()
						.replace(QUESTION_TYPE_FIELD_PREFIX, "").toLowerCase();
				try {
					Byte value = (Byte) field.get(QuestionDef.class);
					QUESTION_VALUES.put(name, value);
				} catch (Exception e) {
					System.err.println("Failed to load question type " + name
							+ ": " + e.getLocalizedMessage());
				}
			}
		}
	}

	public static List<String> getValueNames() {
		List<String> valuesList = new ArrayList<String>();
		for (String valueName : QUESTION_VALUES.keySet())
			valuesList.add(valueName);
		return valuesList;
	}

	private String getNameForValue(Byte value) {
		for (Map.Entry<String, Byte> entry : QUESTION_VALUES.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static String convertToCamelCase(String name) {
		StringBuffer camelCaseName = new StringBuffer();
		Matcher m = uscorePattern.matcher(name);
		while (m.find()) {
			m.appendReplacement(camelCaseName, m.group(1).toUpperCase());
		}
		m.appendTail(camelCaseName);
		return camelCaseName.toString();
	}

	/**
	 * Should get a list of byte values, want to return a list of names.
	 */
	public List<?> toListData(Object value) {
		List<Object> nameList = new ArrayList<Object>();
		for (Object item : (List<?>) value) {
			if (item instanceof String) {
				nameList.add(item.toString());
			} else if (item instanceof Byte) {
				String valueName = getNameForValue((Byte) item);
				if (valueName != null)
					nameList.add(valueName);
			}
		}
		return nameList;
	}

	/**
	 * Should get a list of names, want to return a list of values.
	 */
	public Object valueOf(List<?> listData) {
		List<Object> valueList = new ArrayList<Object>();
		for (Object item : listData) {
			if (item instanceof String && QUESTION_VALUES.containsKey(item)) {
				valueList.add(QUESTION_VALUES.get(item.toString()));
			}
		}
		return valueList;
	}

	/**
	 * Take the value and return the position in the list.
	 */
	public int indexOf(List<?> listData, Object value) {
		@SuppressWarnings("unchecked")
		List<String> typedListData = (List<String>) listData;
		String valueName = getNameForValue((Byte) value);
		return typedListData.indexOf(valueName);
	}

	/**
	 * Take the index of selection and translate into an assignable value.
	 */
	public Object get(List<?> listData, int index) {
		@SuppressWarnings("unchecked")
		List<String> typedListData = (List<String>) listData;
		String valueName = typedListData.get(index);
		return QUESTION_VALUES.get(valueName);
	}
}
