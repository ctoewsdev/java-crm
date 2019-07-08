/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.utility.validation;

public class Validator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PHONE_PATTERN = "([0-9]{3})-([0-9]{3})-([0-9]{4})";
	private static final String FOUR_DIGITS_PATTERN = "([0-9]{4})";
	private static final String NAME_PATTERN = "^[A-Za-z .'-]+$";
	private static final String YYYYMMDD_PATTERN = "(20\\\\d{2})(\\\\d{2})(\\\\d{2})"; // valid for years 2000-2099

	private Validator() {
		super();
	}

	public static boolean validateEmail(String email) {
		if (email.length() == 0) {
			return true;
		}
		return email.matches(EMAIL_PATTERN);
	}

	public static boolean validatePhone(String phone) {
		return phone.matches(PHONE_PATTERN);
	}

	public static boolean validate4Digits(String phone) {
		return phone.matches(FOUR_DIGITS_PATTERN);
	}

	public static boolean validateName(String name) {

		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (Character.isUpperCase(ch) || Character.isLowerCase(ch)) {
				return name.matches(NAME_PATTERN);
			}
		}
		return false;
	}

	public static boolean validateJoinedDate(String yyyymmdd) {
		return yyyymmdd.matches(YYYYMMDD_PATTERN);
	}
}