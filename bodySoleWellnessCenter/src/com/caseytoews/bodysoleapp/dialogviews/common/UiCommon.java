/**
 * Project: bodySoleWellnessCenter
 * File: UiCommon.java
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

public interface UiCommon {
	// Format
	public static final DecimalFormat TWO_DECIMAL_FORMAT = new DecimalFormat("0.00");
	public static final DecimalFormat ONE_DECIMAL_FORMAT = new DecimalFormat("0.0");
	public static final DateTimeFormatter DATETIME_FORMAT_MMddyyyy = DateTimeFormatter.ofPattern("MMddyyyy");
	public static final DateTimeFormatter DATE_FORMAT_UI = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	public static final DateTimeFormatter REPORT_MMyyyy_FORMAT_UI = DateTimeFormatter.ofPattern("MM/yyyy");
	public static final SimpleDateFormat LOG_FORMAT_MMddyyy_time = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	// Images
	public static final int ICON_SIZE = 35;
	public static final ImageIcon LOGO_RECOLOR = new ImageIcon(
			new ImageIcon("images/logorecolor.png").getImage().getScaledInstance(55, 55, Image.SCALE_DEFAULT));
	public static final ImageIcon SMILEY_FACE = new ImageIcon(
			new ImageIcon("images/smiley.png").getImage().getScaledInstance(125, 125, Image.SCALE_DEFAULT));
	public static final ImageIcon THINKING_FACE = new ImageIcon(
			new ImageIcon("images/sad.png").getImage().getScaledInstance(125, 100, Image.SCALE_DEFAULT));

	public static final ImageIcon QUESTION = new ImageIcon(
			new ImageIcon("images/question.png").getImage().getScaledInstance(125, 125, Image.SCALE_DEFAULT));

	public static final ImageIcon CUSTOMER_ICON = new ImageIcon(
			new ImageIcon("images/customer.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_DEFAULT));

	public static final ImageIcon SERVICE_ICON = new ImageIcon(
			new ImageIcon("images/service.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));

	public static final ImageIcon STAFF_ICON = new ImageIcon(
			new ImageIcon("images/staff.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_DEFAULT));

	public static final ImageIcon REPORTS_ICON = new ImageIcon(
			new ImageIcon("images/reports.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_DEFAULT));

	public static final ImageIcon PRICING_ICON = new ImageIcon(
			new ImageIcon("images/pricing.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_DEFAULT));

	public static final ImageIcon PACKAGE_ICON = new ImageIcon(
			new ImageIcon("images/package.png").getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_DEFAULT));

	// Colors
	public static final String UI_FONT = "Yu Gothic";
	public static final int UI_FONT_SIZE = 16;
	public static final Font DIALOG_FONT = new Font(UI_FONT, Font.BOLD, 15);
	public static final Font DIALOG_FONT_HEADER = new Font(UI_FONT, Font.BOLD, 17);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color DARK_GREY = new Color(77, 77, 77);
	public static final Color LIGHT_GREY = new Color(169, 169, 169);
	public static final Color WHITE_MAIN = new Color(255, 255, 255);
	public static final Color PURPLE_MAIN = new Color(73, 25, 73);
	public static final Color PURPLE_LITE = new Color(230, 204, 255);
	public static final Color GREEN_MAIN = new Color(94, 128, 0);
	public static final Color TEXT_FIELD = WHITE_MAIN;
	public static final Color TEXT_FIELD_TEXT = new Color(73, 25, 73);
	public static final Color MENU_TEXT = WHITE_MAIN;
	public static final Color MENU_BAR_BACKGROUND = PURPLE_MAIN;
	public static final Color MAIN_MENU_SELECT = new Color(51, 77, 0);
	public static final Color MENU_VERTICAL_SEPARATOR = PURPLE_MAIN;

	// Functions
	public static String dateNow() {
		return LOG_FORMAT_MMddyyy_time.format(new Date());
	}

	public static JButton setOKButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(GREEN_MAIN);
		button.setForeground(WHITE_MAIN);
		button.setFont(UiCommon.DIALOG_FONT);
		button.setActionCommand("OK");
		button.setFocusPainted(false);
		return button;
	}

	public static JButton setCancelButton(String text) {
		JButton button = new JButton(" Cancel ");
		button.setBackground(DARK_GREY);
		button.setForeground(WHITE_MAIN);
		button.setFont(UiCommon.DIALOG_FONT);
		button.setActionCommand(text);
		button.setFocusPainted(false);
		return button;
	}

	public static JTextField setNotEditableTextField() {
		JTextField textField = new JTextField();
		textField.setEditable(false);
		textField.setBackground(TEXT_FIELD);
		textField.setForeground(TEXT_FIELD_TEXT);
		textField.setFont(UiCommon.DIALOG_FONT);
		textField.setColumns(10);
		return textField;
	}

	public static JTextField setEditableTextField() {
		JTextField textField = new JTextField();
		textField.setBackground(TEXT_FIELD);
		textField.setForeground(TEXT_FIELD_TEXT);
		textField.setFont(UiCommon.DIALOG_FONT);
		textField.setColumns(10);
		return textField;
	}

	public static JFormattedTextField setPhoneNumberField() throws ApplicationException {
		JFormattedTextField phoneNumberField;
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter("(###)-(###)-(####)");
			formatter.setPlaceholderCharacter('#');

		} catch (ParseException e1) {
			throw new ApplicationException(e1);
		}
		phoneNumberField = new JFormattedTextField(formatter);
		phoneNumberField.setFont(UiCommon.DIALOG_FONT);
		phoneNumberField.setBackground(TEXT_FIELD);
		phoneNumberField.setForeground(TEXT_FIELD_TEXT);
		return phoneNumberField;
	}

	public static JFormattedTextField set4DigitsNumberField() throws ApplicationException {
		JFormattedTextField phoneNumberField;
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter("(####)");
			formatter.setPlaceholderCharacter('#');

		} catch (ParseException e1) {
			throw new ApplicationException(e1);
		}
		phoneNumberField = new JFormattedTextField(formatter);
		phoneNumberField.setFont(UiCommon.DIALOG_FONT);
		phoneNumberField.setBackground(TEXT_FIELD);
		phoneNumberField.setForeground(TEXT_FIELD_TEXT);
		return phoneNumberField;
	}

	public static JFormattedTextField setCurrencyField() {
		JFormattedTextField currencyField = new JFormattedTextField();
		currencyField.setEditable(false);
		currencyField.setForeground(UiCommon.TEXT_FIELD_TEXT);
		currencyField.setText("0.00");
		currencyField.setHorizontalAlignment(SwingConstants.LEFT);
		currencyField.setFont(UiCommon.DIALOG_FONT);
		currencyField.setColumns(10);
		return currencyField;
	}

	public class FontCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			Font font = new Font(UI_FONT, Font.PLAIN, 14);
			label.setFont(font);
			return label;
		}
	}
}
