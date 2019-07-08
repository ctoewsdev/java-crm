/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 18, 2018
 * Time: 8:46:26 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.customer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.controller.UiController;
import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.database.people.GroupDao;
import com.caseytoews.bodysoleapp.dialogviews.common.ActionRequiredPopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.EvenOddCellRenderer;
import com.caseytoews.bodysoleapp.dialogviews.common.MessagePopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.people.Group;
import com.caseytoews.bodysoleapp.models.customer.CustomerListItem;
import com.caseytoews.bodysoleapp.models.customer.CustomerListModel;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.caseytoews.bodysoleapp.utility.validation.Validator;

import net.miginfocom.swing.MigLayout;

public class GetGroupNewMemberByPhoneDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JFormattedTextField textFieldPhone;
	private JList<CustomerListItem> customersList;
	private CustomerListModel customerModel;
	private JButton okButton;
	private JButton cancelButton;
	private JScrollPane scrollPane;
	private static Customer customer;

	// Create the dialog.
	public GetGroupNewMemberByPhoneDialog(JFrame frame, CustomerDao customerDao, GroupDao groupDao) throws ApplicationException {
		customer = MainFrame.getMainFrameCustomer();
		setBounds(100, 100, 650, 350);
		setTitle("Get Customer by Phone Number");
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[35px][grow][60px][grow][35px]", "[][][45px][150px]"));
		{
			JLabel lblTitle1 = new JLabel("Please enter the last four digits of the client's phone number and click OK.");
			lblTitle1.setFont(UiCommon.DIALOG_FONT_HEADER);
			lblTitle1.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblTitle1, "cell 1 0 3 1,alignx center,aligny center");
			JLabel lblTitle2 = new JLabel("Multiple customers with the same digits will display below.");
			lblTitle2.setFont(UiCommon.DIALOG_FONT_HEADER);
			lblTitle2.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblTitle2, "cell 1 1 3 1,alignx center,aligny center");
		}
		{
			textFieldPhone = UiCommon.set4DigitsNumberField();
			textFieldPhone.setHorizontalAlignment(JFormattedTextField.CENTER);
			contentPanel.add(textFieldPhone, "cell 2 2 1 1, growx, alignx center,aligny center");
		}

		{
			customerModel = new CustomerListModel();
			customersList = new JList<>(customerModel);
			customersList.setBackground(UiCommon.LIGHT_GREY);
			customersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			customersList.setCellRenderer(new EvenOddCellRenderer());
			scrollPane = new JScrollPane(customersList);
			scrollPane.setForeground(UiCommon.BLACK);
			contentPanel.add(scrollPane, "cell 1 3 3 1,grow");

			customersList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent event) {
					if (!event.getValueIsAdjusting()) {
						CustomerListItem listCustomer = customersList.getSelectedValue();
						Customer newMember = listCustomer.getCustomer();
						long ID = newMember.getID();

						try {
							GetGroupNewMemberByPhoneDialog.this.dispose();

							ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame, "Please confirm you want to add \"" + newMember.getFirstName()
									+ " " + newMember.getLastName() + "\" to \"" + customer.getFirstName() + "'s\" group", "Yes. This is correct.",
									"No. Do not add.");
							popUp.setVisible(true);

							if (popUp.getActionValue()) {

								if (groupDao.validateGroupMember(ID)) {
									if (groupDao.validateGroupMember(customer.getID())) {
										groupDao.addGroup(customer.getID());
									}
									Group group = groupDao.getGroupByCustomerId(customer.getID());
									group.getMembers().add(ID);
									groupDao.updateGroups(group);
									MainFrame.updateMainTextFields(customer);
								} else {
									MessagePopUp popUpAlready = new MessagePopUp(frame, "\"" + listCustomer.getCustomer().getFirstName() + " "
											+ listCustomer.getCustomer().getLastName() + " already belongs to a group\"", false);
									popUpAlready.setVisible(true);
								}
							} else {
								popUp.dispose();
							}

						} catch (ApplicationException e) {
							LOG.error("GetCustomerByPhoneDialog() 1");
						}
						return;
					}
				}
			});
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		{
			okButton = UiCommon.setOKButton(" OK ");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					try {
						String fourDigits = textFieldPhone.getText();
						String formattedPhone = fourDigits.replaceAll("([()])", "");
						if (Validator.validate4Digits(formattedPhone)) {

							ArrayList<Customer> customers = customerDao.getCustomerBy4Digits(formattedPhone);

							// remove the Main Customer from the list
							Iterator<Customer> iter = customers.iterator();
							while (iter.hasNext()) {
								Customer c = iter.next();
								if (c.getID() == customer.getID()) {
									iter.remove();
								}
							}

							if (customers.size() == 1) {
								GetGroupNewMemberByPhoneDialog.this.dispose();

								ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame,
										"Please confirm you want to add \"" + customers.get(0).getFirstName() + " " + customers.get(0).getLastName()
												+ "\" to \"" + customer.getFirstName() + "'s\" group",
										"Yes. Please add " + customers.get(0).getFirstName() + ".", "No. Do not add.");
								popUp.setVisible(true);

								if (popUp.getActionValue()) {

									if (groupDao.validateGroupMember(customers.get(0).getID())) {
										if (groupDao.validateGroupMember(customer.getID())) {
											groupDao.addGroup(customer.getID());
										}
										Group group = groupDao.getGroupByCustomerId(customer.getID());
										group.getMembers().add(customers.get(0).getID());
										groupDao.updateGroups(group);
										MainFrame.updateMainTextFields(customer);
									} else {
										MessagePopUp popUpAlready = new MessagePopUp(frame, "\"" + customers.get(0).getFirstName() + " "
												+ customers.get(0).getLastName() + " already belongs to a group\"", false);
										popUpAlready.setVisible(true);
									}
								} else {
									popUp.dispose();
								}

							} else if (customers.size() > 1) {

								for (Customer customer : customers) {
									customerModel.add(customer);
								}

							} else {
								MessagePopUp popUp = new MessagePopUp(frame,
										"Sorry! No customer with number ending \'" + textFieldPhone.getText() + "\' is able to be added.", false);
								popUp.setVisible(true);
								textFieldPhone.setValue(null);
							}
						} else { // invalid phone
							MessagePopUp popUp = new MessagePopUp(frame, "Invalid! Please enter 4 digts. Thnak you.", false);
							popUp.setVisible(true);
							textFieldPhone.setValue(null);
						}
					} catch (Exception e) {
						LOG.error("GetCustomerByPhoneDialog() 2");
						UiController.handle(e);
					}

				}
			});

			buttonPane.add(okButton);
			// getRootPane().setDefaultButton(okButton);

			cancelButton = UiCommon.setCancelButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GetGroupNewMemberByPhoneDialog.this.dispose();
				}
			});

			buttonPane.add(cancelButton);
		}

		JLabel lblCancelBtnSpacer = new JLabel("              ");
		buttonPane.add(lblCancelBtnSpacer);
	}
}
