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

import com.caseytoews.bodysoleapp.controller.UiController;
import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.dialogviews.common.ActionRequiredPopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.MessagePopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.caseytoews.bodysoleapp.utility.validation.Validator;

import net.miginfocom.swing.MigLayout;

public class AddNewCustomerDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldFirstName;
	private JTextField textFieldLastName;
	private JTextField textFieldEmail;
	private JButton okButton;
	private String email = "";

	// Create the dialog.
	public AddNewCustomerDialog(JFrame frame, CustomerDao customerDao) throws ApplicationException {
		super(frame, true);
		setModal(true);
		setResizable(false);
		setTitle("Add New Customer");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 364, 260);
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
		contentPanel.add(textFieldFirstName, "cell 1 1,growx");

		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblLastName, "cell 0 2,alignx trailing");
		textFieldLastName = UiCommon.setEditableTextField();
		contentPanel.add(textFieldLastName, "cell 1 2,growx");

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPhone, "cell 0 3,alignx trailing");
		JFormattedTextField formattedTextFieldPhone1 = UiCommon.setPhoneNumberField();
		contentPanel.add(formattedTextFieldPhone1, "flowx,cell 1 3");

		JLabel lblEmail = new JLabel("*Email:");
		lblEmail.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblEmail, "cell 0 4,alignx trailing");
		textFieldEmail = UiCommon.setEditableTextField();
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
					email = textFieldEmail.getText();

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
						MessagePopUp popUp = new MessagePopUp(frame,
								"Invalid! Please use proper email format. Or leave blank if you would like to add email address later. Thank you.",
								false);
						popUp.setVisible(true);
					} else {
						Customer customer = new Customer(inputedPhone, firstName, lastName, email);

						char validation = 0;
						try {
							validation = customerDao.validateNewCustomer(customer);

							// customer was added
							if (validation == 'A') {
								customerDao.addCustomer(customer);
								MessagePopUp popUp = new MessagePopUp(frame,
										"Congratulations! \"" + firstName + " " + lastName + "\" has been added.", true);
								popUp.setVisible(true);
								MainFrame.updateMainTextFields(customer);
								AddNewCustomerDialog.this.dispose();

								// name and phone are duplicate
							} else if (validation == 'B') {
								MessagePopUp popUp = new MessagePopUp(frame,
										"Oops! \"" + firstName + " " + lastName + "\" at : \'" + inputedPhone + "\' already exists.", true);
								popUp.setVisible(true);
								MainFrame.updateMainTextFields(customer);
								AddNewCustomerDialog.this.dispose();

								// if name only is duplicate
							} else if (validation == 'N') {
								ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame, "Warning! \"" + firstName + " " + lastName
										+ "\" (using: \'" + inputedPhone + "\') already exists. Do you still want to add?", "Yes. Please Add.",
										"No, do not add.");
								popUp.setVisible(true);
								if (popUp.getActionValue()) {
									customerDao.addCustomer(customer);
									MainFrame.updateMainTextFields(customer);
									AddNewCustomerDialog.this.dispose();
								} else {
									AddNewCustomerDialog.this.dispose();
								}

							} else if (validation == 'P') {
								ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame, "Warning!  At least one other customer uses: \'"
										+ inputedPhone + "\'. Do you still want to add " + firstName + " " + lastName + "anyway?", "Yes. Please Add.",
										"No, do not add.");
								popUp.setVisible(true);
								if (popUp.getActionValue()) {
									AddNewCustomerDialog.this.dispose();
									customerDao.addCustomer(customer);
									MessagePopUp confirm = new MessagePopUp(frame,
											"Congratulations! \"" + firstName + " " + lastName + "\" has been added.", true);
									confirm.setVisible(true);
									MainFrame.updateMainTextFields(customer);
								} else {

									AddNewCustomerDialog.this.dispose();
								}
								MainFrame.updateMainTextFields(customer);
								AddNewCustomerDialog.this.dispose();

							}
						} catch (Exception e) {
							LOG.error("AddNewCustomerDialog() ");
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
					AddNewCustomerDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
	}
}
