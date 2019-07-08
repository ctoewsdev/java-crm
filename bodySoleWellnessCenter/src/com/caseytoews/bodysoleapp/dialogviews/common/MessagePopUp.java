/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.common;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class MessagePopUp extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Constructor for class MessagePopUp.java
	 * 
	 * @param frame
	 * @param message
	 *            is the message to display
	 * @param isConfirmMessage
	 *            true is happy face, false is thinking face
	 */
	public MessagePopUp(JFrame frame, String message, Boolean isConfirmMessage) {
		super(frame, true);
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 390, 275);
		setLocationRelativeTo(frame);
		setTitle("Message");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("fill", "[][]", "[]"));

		// Smiley face for confirm, or else thinking face for pseudo-error
		JLabel lblNewLabelIcon = new JLabel();
		lblNewLabelIcon.setIcon((isConfirmMessage) ? UiCommon.SMILEY_FACE : UiCommon.THINKING_FACE);
		contentPanel.add(lblNewLabelIcon, "cell 0 0,alignx center,aligny center");

		JTextArea textArea = new JTextArea(message);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(UiCommon.GREEN_MAIN);
		textArea.setFont(UiCommon.DIALOG_FONT);
		textArea.setBorder(null);
		JPanel panel = new JPanel();
		panel.setBackground(UiCommon.GREEN_MAIN);
		panel.setLayout(new MigLayout("fill", "[]", "[]"));
		panel.add(textArea, "cell 0 0,growx,alignx center,aligny center");
		panel.setBorder(BorderFactory.createLineBorder(UiCommon.WHITE_MAIN));
		contentPanel.add(panel, "cell 1 0,growx,alignx center,aligny center");

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(UiCommon.PURPLE_MAIN);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = UiCommon.setOKButton(" OK. Got it! ");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						MessagePopUp.this.dispose();
					}
				});

				buttonPane.add(okButton);

			}

		}
	}

}
