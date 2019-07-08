/**
 * Project: bodySoleWellnessCenter
 * File: EvenOddCellRenderer.java
 * Date: Dec 23, 2018
 * Time: 5:50:29 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.common;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class EvenOddCellRenderer extends JLabel implements ListCellRenderer<Object> {
	private static final long serialVersionUID = 1L;

	public EvenOddCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		// Assumes value has a pretty toString
		setText(value.toString());

		if (index % 2 == 0) {
			setBackground(UiCommon.LIGHT_GREY);
			setForeground(UiCommon.BLACK);
			this.setFont(UiCommon.DIALOG_FONT);
		} else {
			setBackground(UiCommon.PURPLE_LITE);
			setForeground(UiCommon.BLACK);
			this.setFont(UiCommon.DIALOG_FONT);
		}

		return this;
	}

}
