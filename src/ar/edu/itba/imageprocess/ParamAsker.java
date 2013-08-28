package ar.edu.itba.imageprocess;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

public class ParamAsker {

	LinkedHashMap<String, Param> mParams;

	public ParamAsker() {

	}

	/**
	 * Get the param with the corresponding name
	 */
	public Param getParam(String name) {
		return mParams.get(name);
	}

	/**
	 * Adds a param to the ParamAsker
	 * @param name
	 */
	public void addParam(String name) {
		Param param = new Param();
		mParams.put(name, param);
	}

	/**
	 * Iterates over all the params and ask them to the user
	 * 
	 * @return true if user entered all the values, false if user canceled
	 */
	public boolean ask() {
		Iterator<Entry<String, Param>> it = mParams.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Param> entry = it.next();
			if (!askParam(entry.getValue())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Ask a single param to the user
	 * 
	 * @param param the param to ask
	 * @return true if user entered a value, false if user canceled
	 */
	private boolean askParam(Param param) {
		boolean valid = false;
		String message = "Enter a value for " + param.getName();
		while (true) {
			String input = JOptionPane.showInputDialog(message);
			if (input == null) {
				break;
			}
			try {
				int value = Integer.parseInt(input);
				if (value <= 0) {
					message = param.getName() + " must be superior to 0";
				} else {
					valid = true;
					param.setValue(value);
					break;
				}
			} catch (NumberFormatException e) {
				message = param.getName() + " must be an integer";
			}
		}
		return valid;
	}

	/**
	 * Simple class to store the param name and attributes (type, bounds, etc)
	 * and later the value entered by the user
	 */
	public static class Param {

		private String mName;
		private Object mValue;

		public Param() {

		}

		public String getName() {
			return mName;
		}

		public String getValueString() {
			return (String) mValue;
		}

		public int getValueInt() {
			return (int) mValue;
		}

		public double getValueDouble() {
			return (double) mValue;
		}

		public void setValue(Object value) {
			mValue = value;
		}
	}
}
