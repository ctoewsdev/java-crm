/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */
package com.caseytoews.bodysoleapp.dialogviews.common;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControllerErrorMessageDialog extends JDialog {
	private static final Logger LOG = LogManager.getLogger();

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param frame
	 *            is the parent of the message dialog
	 * @param message
	 *            is the message to display
	 * @param e
	 *            is thrown Exception
	 */
	public ControllerErrorMessageDialog(JFrame frame, String message, Exception e) {
		super(frame, true);
		JOptionPane.showMessageDialog(frame, message + e.getMessage());
		LOG.error("ControllerErrorMessageDialog() opened: " + e.getMessage());
	}
}
