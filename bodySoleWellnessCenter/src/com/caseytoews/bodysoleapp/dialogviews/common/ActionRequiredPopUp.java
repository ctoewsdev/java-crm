/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 30, 2018
 * Time: 8:56:45 PM
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

public class ActionRequiredPopUp extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	boolean action;

	public ActionRequiredPopUp(JFrame frame, String message, String yesButton, String noButton) {
		super(frame, true);
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 275);
		setLocationRelativeTo(frame);
		setTitle("Message");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("fill", "[30][400][30]", "[300]"));

		// Smiley face for confirm, or else thinking face for pseudo-error
		JLabel lblNewLabelIcon = new JLabel();
		lblNewLabelIcon.setIcon(UiCommon.QUESTION);
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
		panel.setBorder(BorderFactory.createLineBorder(UiCommon.WHITE_MAIN));
		panel.add(textArea, "cell 0 0,growx,");
		contentPanel.add(panel, "flowx,cell 1 0,growx");

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(UiCommon.PURPLE_MAIN);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = UiCommon.setOKButton(yesButton);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						action = true;
						ActionRequiredPopUp.this.dispose();
					}
				});

				buttonPane.add(okButton);
			}

			JButton cancelButton = UiCommon.setCancelButton(noButton);
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					action = false;
					ActionRequiredPopUp.this.dispose();
				}
			});

			buttonPane.add(cancelButton);
		}
	}

	public boolean getActionValue() {
		return action;
	}
}
