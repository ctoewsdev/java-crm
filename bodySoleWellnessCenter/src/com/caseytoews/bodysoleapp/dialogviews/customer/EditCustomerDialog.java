/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 19, 2018
 * Time: 1:17:27 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.customer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.dialogviews.common.MessagePopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.caseytoews.bodysoleapp.utility.validation.Validator;

import net.miginfocom.swing.MigLayout;

public class EditCustomerDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldFirstName;
	private JTextField textFieldLastName;
	private JTextField textFieldEmail;
	private JButton okButton;
	private Customer customer;

	// Create the dialog.
	public EditCustomerDialog(JFrame frame, CustomerDao customerDao) throws ApplicationException {
		super(frame, true);
		customer = MainFrame.getMainFrameCustomer();
		setModal(true);
		setResizable(false);
		setTitle("Edit Customer");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 260);
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
		textFieldFirstName.setText(customer.getFirstName());
		contentPanel.add(textFieldFirstName, "cell 1 1,growx");

		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblLastName, "cell 0 2,alignx trailing");
		textFieldLastName = UiCommon.setEditableTextField();
		textFieldLastName.setText(customer.getLastName());
		contentPanel.add(textFieldLastName, "cell 1 2,growx");

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPhone, "cell 0 3,alignx trailing");
		JFormattedTextField formattedTextFieldPhone1 = UiCommon.setPhoneNumberField();
		formattedTextFieldPhone1.setText(customer.getPhone());
		contentPanel.add(formattedTextFieldPhone1, "flowx,cell 1 3");

		JLabel lblEmail = new JLabel("Email:");
		lblPhone.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblEmail, "cell 0 4,alignx trailing");
		textFieldEmail = UiCommon.setEditableTextField();
		textFieldEmail.setText(customer.getEmail());
		contentPanel.add(textFieldEmail, "cell 1 4,growx");

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

					} else if (!Validator.validateEmail(textFieldEmail.getText())) {
						MessagePopUp popUp = new MessagePopUp(frame, "Invalid! Please use proper email format. Thank you.", false);
						popUp.setVisible(true);

					} else {
						String email = textFieldEmail.getText().trim();
						customer.setFirstName(firstName);
						customer.setLastName(lastName);
						customer.setPhone(inputedPhone);
						customer.setEmail(email);

						try {
							customerDao.updateCustomer(customer);
							MessagePopUp popUp = new MessagePopUp(frame, "Congratulations! \"" + firstName + " " + lastName + "\" has been updated.",
									true);
							popUp.setVisible(true);
							EditCustomerDialog.this.dispose();
							MainFrame.updateMainTextFields(customer);
						} catch (ApplicationException e) {
							LOG.error("EditCustomerDialog() ");
							e.printStackTrace();
						}
					}
				}
			});

			buttonPane.add(okButton);

			JButton cancelButton = UiCommon.setCancelButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EditCustomerDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		JLabel lblCancelBtnSpacer = new JLabel("  ");
		buttonPane.add(lblCancelBtnSpacer);
	}
}
