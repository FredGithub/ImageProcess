package ar.edu.itba.imageprocess;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

public class ParamAsker {

	LinkedHashMap<String, Param> mParams;

	public ParamAsker() {
		mParams = new LinkedHashMap<String, Param>();
	}

	/**
	 * Get the param with the corresponding name
	 */
	public Param getParam(String name) {
		return mParams.get(name);
	}

	public String getString(String name) {
		return mParams.get(name).getValue();
	}

	public int getInteger(String name) {
		return mParams.get(name).getValueInt();
	}

	public double getDouble(String name) {
		return mParams.get(name).getValueDouble();
	}

	/**
	 * Adds a param to the ParamAsker
	 */
	public void addParam(Param param) {
		mParams.put(param.getName(), param);
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
		String input = param.getDefaultValue();
		while (true) {
			input = JOptionPane.showInputDialog(message, input);
			if (input == null) {
				break;
			}
			String error = param.checkValidity(input);
			if (error.length() == 0) {
				param.setValue(input);
				valid = true;
				break;
			} else {
				message = error;
			}
		}
		return valid;
	}

	/**
	 * Simple class to store the param name and attributes (type, bounds, etc)
	 * and later the value entered by the user. Optionally, it can store a
	 * default value
	 */
	public static class Param {

		public static final int TYPE_STRING = 1;
		public static final int TYPE_INTEGER = 2;
		public static final int TYPE_DOUBLE = 3;

		private int mType;
		private String mName;
		private boolean mBound;
		private double mLowBound;
		private double mHighBound;
		private String mValue;
		private String mDefaultValue;

		public Param(int type, String name) {
			this(type, name, "");
		}

		public Param(int type, String name, String defaultValue) {
			mType = type;
			mName = name;
			mBound = false;
			mValue = null;
			mDefaultValue = defaultValue;
		}

		public Param(int type, String name, double lowBound, double highBound) {
			this(type, name, lowBound, highBound, "");
		}

		public Param(int type, String name, double lowBound, double highBound, String defaultValue) {
			mType = type;
			mName = name;
			mBound = true;
			mLowBound = lowBound;
			mHighBound = highBound;
			mValue = null;
			mDefaultValue = defaultValue;
		}

		public String getName() {
			return mName;
		}

		public String getValue() {
			return mValue;
		}

		public int getValueInt() {
			int value = 0;
			try {
				value = Integer.parseInt(mValue);
			} catch (NumberFormatException e) {
			}
			return value;
		}

		public double getValueDouble() {
			double value = 0;
			try {
				value = Double.parseDouble(mValue);
			} catch (NumberFormatException e) {
			}
			return value;
		}

		public void setValue(String value) {
			mValue = value;
		}

		public String getDefaultValue() {
			return mDefaultValue;
		}

		/**
		 * Tests the value based on the type of the parameter. Currently
		 * supports the following tests: integer value, double value, bounds
		 * testing for integer and double
		 * 
		 * @param value the value to be tested
		 * @return an error message if the value is invalid, an empty string
		 *         otherwise
		 */
		public String checkValidity(String value) {
			if (mType == TYPE_INTEGER) {
				int valueInt;
				try {
					valueInt = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					return mName + " must be an integer";
				}
				if (mBound && (valueInt < mLowBound || valueInt > mHighBound)) {
					return mName + " must be in the range [" + (int) mLowBound + ", " + (int) mHighBound + "]";
				}
			} else if (mType == TYPE_DOUBLE) {
				double valueDouble;
				try {
					valueDouble = Double.parseDouble(value);
				} catch (NumberFormatException e) {
					return mName + " must be a double";
				}
				if (mBound && (valueDouble < mLowBound || valueDouble > mHighBound)) {
					return mName + " must be in the range [" + mLowBound + ", " + mHighBound + "]";
				}
			}
			return "";
		}
	}
}
