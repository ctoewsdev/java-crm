/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 19, 2018
 * Time: 1:17:27 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.staff;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.controller.UiController;
import com.caseytoews.bodysoleapp.database.people.StaffDao;
import com.caseytoews.bodysoleapp.dialogviews.common.MessagePopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.caseytoews.bodysoleapp.utility.validation.Validator;

import net.miginfocom.swing.MigLayout;

public class EditStaffDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldFirstName;
	private JTextField textFieldLastName;
	private JTextField textFieldEmail;
	private JTextField textFieldStartDate;
	private JButton okButton;

	public EditStaffDialog(JFrame frame, StaffDao staffDao, Staff staffMember) throws ApplicationException {
		super(frame, true);
		setModal(true);
		setResizable(false);
		setTitle("Edit Staff");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 364, 320);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][]"));

		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblFirstName, "cell 0 1,alignx trailing");
		textFieldFirstName = UiCommon.setEditableTextField();
		textFieldFirstName.setText(staffMember.getFirstName());
		contentPanel.add(textFieldFirstName, "cell 1 1,growx");

		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblLastName, "cell 0 2,alignx trailing");
		textFieldLastName = UiCommon.setEditableTextField();
		textFieldLastName.setText(staffMember.getLastName());
		contentPanel.add(textFieldLastName, "cell 1 2,growx");

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPhone, "cell 0 3,alignx trailing");
		JFormattedTextField formattedTextFieldPhone1 = UiCommon.setPhoneNumberField();
		formattedTextFieldPhone1.setText(staffMember.getPhone());
		contentPanel.add(formattedTextFieldPhone1, "flowx,cell 1 3");

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblEmail, "cell 0 4,alignx trailing");
		textFieldEmail = UiCommon.setEditableTextField();
		textFieldEmail.setText(staffMember.getEmail());
		contentPanel.add(textFieldEmail, "cell 1 4,growx");

		JLabel lblDate = new JLabel("Start Date:");
		lblDate.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblDate, "cell 0 5,alignx trailing");
		textFieldStartDate = UiCommon.setEditableTextField();
		LocalDate date = LocalDate.now();
		textFieldStartDate.setText(date.format(UiCommon.DATE_FORMAT_UI));
		contentPanel.add(textFieldStartDate, "cell 1 5,growx");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{

			okButton = UiCommon.setOKButton(" OK ");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					String firstName = textFieldFirstName.getText().trim();
					String lastName = textFieldLastName.getText().trim();
					String inputedPhone = formattedTextFieldPhone1.getText().replaceAll("([()])", "");
					String email = textFieldEmail.getText();
					Pattern DATE_PATTERN = Pattern.compile("^[0-9][0-9]/[0-9][0-9]/20[123][0-9]$");
					String date = textFieldStartDate.getText();

					if (!Validator.validateName(firstName)) {
						MessagePopUp popUp = new MessagePopUp(frame,
								"Please use only proper name characters (at least one) for the first name. Thank you.", false);
						popUp.setVisible(true);

					} else if (!Validator.validateName(lastName)) {
						MessagePopUp popUp = new MessagePopUp(frame,
								"Please use only proper name characters (at least one) for the last name. Thank you.", false);
						popUp.setVisible(true);

					} else if (!Validator.validatePhone(inputedPhone)) {
						MessagePopUp popUp = new MessagePopUp(frame, "Invalid Number! Please enter 10 digts. Thank you.", false);
						popUp.setVisible(true);

					} else if (!Validator.validateEmail(email)) {
						MessagePopUp popUp = new MessagePopUp(frame, "Invalid! Please use proper email format. Thank you.", false);
						popUp.setVisible(true);

					} else if (!DATE_PATTERN.matcher(date).matches()) {
						JOptionPane.showMessageDialog(null, "Sorry, but the date must be in the format of mm/dd/yyyy", "dialog",
								JOptionPane.ERROR_MESSAGE);
					} else {

						LocalDate sDate = LocalDate.parse(textFieldStartDate.getText(), UiCommon.DATE_FORMAT_UI);
						String startDate = sDate.format(UiCommon.DATETIME_FORMAT_MMddyyyy);

						staffMember.setFirstName(firstName);
						staffMember.setLastName(lastName);
						staffMember.setPhone(inputedPhone);
						staffMember.setEmail(email);
						staffMember.setJoinDate(startDate);

						try {

							EditStaffDialog.this.dispose();
							staffDao.updateStaffMember(staffMember);
							MessagePopUp confirm = new MessagePopUp(frame,
									"Congratulations! \"" + firstName + " " + lastName + "\" has been updated.", true);
							confirm.setVisible(true);

						} catch (Exception e) {
							LOG.error("ERROR in EditStaffDialog()");
							UiController.handle(e);
						}
					}
				}
			});

			buttonPane.add(okButton);

			JButton cancelButton = UiCommon.setCancelButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					EditStaffDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
	}
}