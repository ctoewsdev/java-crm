/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 19, 2018
 * Time: 1:17:27 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.bodypackage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.database.people.StaffDao;
import com.caseytoews.bodysoleapp.database.product.ProductDetailsDao;
import com.caseytoews.bodysoleapp.database.sales.BodyPackageDao;
import com.caseytoews.bodysoleapp.database.sales.BodyServiceDao;
import com.caseytoews.bodysoleapp.dialogviews.common.ActionRequiredPopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.MessagePopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.domain.product.BodyPackageProduct;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.utility.calculator.CurrencyCalculator;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class EditBodyPackageDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private JTextField customerName;
	private JButton okButton;
	private JTextField textFieldDate;
	private JFormattedTextField textFieldPrice;
	private JFormattedTextField textFieldGST;
	private JFormattedTextField textFieldTotalCost;
	private JTextField textFieldPurchaseID;
	private JComboBox<String> packageComboBox;
	private static BodyPackageProduct packagedProduct;
	private static Customer customer;

	// Package Object Variables
	String packageProductCode = "Ple";
	String packageName;
	double packagePrice;
	double totalGST;
	double totalCost;

	private BodyPackage originalBodyPackage;

	/**
	 * Create the dialog.
	 * 
	 * @throws ApplicationException
	 */
	public EditBodyPackageDialog(long packagedID, JFrame frame, CustomerDao customerDao, BodyServiceDao serviceDao, BodyPackageDao packagedDao,
			ProductDetailsDao productsDao, StaffDao staffDao) throws ApplicationException {
		super(frame, true);

		// ???????????????????? preserve original packaged in case it is restored
		originalBodyPackage = packagedDao.getPackagedByPackagedId(packagedID);
		String packCode = originalBodyPackage.getProductCode();

		setTitle("Edit Body Package");
		customer = MainFrame.getMainFrameCustomer();
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 642, 280);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		contentPanel.setLayout(new MigLayout("", "[100][220,grow][90][200][30]", "[][][][][]"));

		// CUSTOMER
		JLabel lblCustomer = new JLabel("Customer: ");
		lblCustomer.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblCustomer, "cell 0 1,alignx trailing");
		customerName = UiCommon.setNotEditableTextField();
		customerName.setText(customer.getFirstName() + " " + customer.getLastName());
		contentPanel.add(customerName, "cell 1 1,growx");

		// DATE
		JLabel lblDate = new JLabel("      Date:");
		lblDate.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblDate, "cell 3 1,alignx right,aligny center");
		textFieldDate = UiCommon.setEditableTextField();
		LocalDate date = LocalDate.parse(originalBodyPackage.getPurchaseDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
		textFieldDate.setText(date.format(UiCommon.DATE_FORMAT_UI));
		contentPanel.add(textFieldDate, "cell 3 1, growx");

		// PACKAGE COMBO BOX
		JLabel lblPackage = new JLabel("Package:");
		lblPackage.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPackage, "cell 0 2,alignx trailing");
		packageComboBox = new JComboBox<>();
		packageComboBox.setFont(UiCommon.DIALOG_FONT);
		// packageComboBox.requestFocus();
		contentPanel.add(packageComboBox, "flowx,cell 1 2 2,growx");
		// ArrayList<BodyPackageProduct> packageProducts = productsDao.getProductDetails().getPackagedProducts();

		if (packCode.startsWith("B")) {
			BodyPackageProduct B11 = productsDao.getPackageProductDetailsByProductCode("B11");
			BodyPackageProduct B12 = productsDao.getPackageProductDetailsByProductCode("B12");
			String D11 = B11.getProductCode() + " | " + B11.getProductName() + " | " + " = $" + B11.getProductPrice();
			String D12 = B12.getProductCode() + " | " + B12.getProductName() + " | " + " = $" + B12.getProductPrice();
			packageComboBox.addItem(D11);
			packageComboBox.addItem(D12);

		} else if (packCode.startsWith("A")) {

		}

		// PURCHASE ID
		JLabel lblPurchaseID = new JLabel("    Purchase ID:");
		lblPurchaseID.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPurchaseID, "cell 3 2,alignx trailing");
		textFieldPurchaseID = UiCommon.setNotEditableTextField();
		contentPanel.add(textFieldPurchaseID, "flowx,cell 3 2,growx,aligny center");
		textFieldPurchaseID.setText(Long.toString(originalBodyPackage.getPurchaseID()));

		// PRICE
		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPrice, "cell 0 3,alignx trailing");
		textFieldPrice = UiCommon.setCurrencyField();
		textFieldPrice.setForeground(UiCommon.TEXT_FIELD_TEXT);
		contentPanel.add(textFieldPrice, "cell 1 3,growx,aligny center");

		// GST
		JLabel lblGst = new JLabel("GST:");
		lblGst.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblGst, "cell 1 3,alignx trailing");
		textFieldGST = UiCommon.setCurrencyField();
		contentPanel.add(textFieldGST, "cell 1 3,alignx trailing");

		// TOTAL COST
		JLabel lblTotalCost = new JLabel("   Total Cost:");
		lblTotalCost.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblTotalCost, "cell 3 3,alignx trailing");
		textFieldTotalCost = UiCommon.setCurrencyField();
		contentPanel.add(textFieldTotalCost, "flowx,cell 3 3,growx,aligny center");

		// PACKAGE COMBOBOX LISTNER FOR CHANGES TO PACKAGE SELECTION
		ActionListener servicePackageComboListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {

					String packagedSelection = String.valueOf(packageComboBox.getSelectedItem());
					System.out.println("selected item: " + packagedSelection.toString());
					packageProductCode = packagedSelection.substring(0, 3); // parse package code

					if (packCode.equalsIgnoreCase("B12") && (packagedDao.getPackagedRemaining(originalBodyPackage) < 1)) {

						JOptionPane.showMessageDialog(null, "Cannot change from 12-pack to 11-pack. Not enough services remaining.", "dialog",
								JOptionPane.ERROR_MESSAGE);

					} else if (packCode.equalsIgnoreCase("A11") && (packagedDao.getPackagedRemaining(originalBodyPackage) < 6)) {

						JOptionPane.showMessageDialog(null, "Cannot change from 11-pack to 5-pack. Not enough services remaining.", "dialog",
								JOptionPane.ERROR_MESSAGE);
					} else {

						// Set Price Field
						packagedProduct = productsDao.getPackageProductDetailsByProductCode(packageProductCode);
						packageName = packagedProduct.getProductName();

						packagePrice = packagedProduct.getProductPrice(); // set local variable
						String productPrice = Double.toString(packagePrice);

						CurrencyCalculator calculatePrice = new CurrencyCalculator(productPrice);
						String price = calculatePrice.bigDecimalToString(calculatePrice.getAmountOne());
						textFieldPrice.setText(price);

						// Set GST Field
						String gst = "0";
						if (packageProductCode.equalsIgnoreCase("A05") || packageProductCode.equalsIgnoreCase("A10")) {
							totalGST = 0.0;
							textFieldGST.setText("0.00");

						} else {
							String GSTConstant = Double.toString(productsDao.getProductDetails().getGST());
							CurrencyCalculator calculateGST = new CurrencyCalculator(price, GSTConstant);
							gst = calculateGST.getProduct();
							textFieldGST.setText(gst);
							totalGST = Double.parseDouble(gst);
						}
						// Set Total Cost Field
						CurrencyCalculator calculateTotalCost = new CurrencyCalculator(price, gst);
						String cost = calculateTotalCost.getSum();
						textFieldTotalCost.setText(cost);
						totalCost = Double.parseDouble(cost);
					}

				} catch (ApplicationException e) {

				}
			}
		};
		packageComboBox.addActionListener(servicePackageComboListener);
		// Set combobox to originalpackage to triger the event listener
		if (packCode.equalsIgnoreCase("B11"))

		{
			packageComboBox.setSelectedIndex(0);
		} else {
			packageComboBox.setSelectedIndex(1);
		}

		// STAFF MEMBER PERFORMING THE SERVICE
		JLabel lblStaff = new JLabel("Staff:");
		lblStaff.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblStaff, "cell 0 4,alignx trailing");
		JComboBox<String> staffComboBox = new JComboBox<>();
		staffComboBox.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(staffComboBox, "cell 1 4,growx");

		List<Staff> staff = staffDao.getAllStaff();
		int count = 0;
		for (Staff s : staff) {
			staffComboBox.addItem("(#" + s.getID() + ") " + s.getFirstName() + " " + s.getLastName());
			if (s.getID() == originalBodyPackage.getStaffID()) {
				staffComboBox.setSelectedIndex(count);
			}
			count++;
		}

		// PANEL DISPLAY
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{

			// Delete Package Button
			JButton deletePackageButton = new JButton("   Delete this Package   ");
			deletePackageButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					if (originalBodyPackage.getServices().size() > 0) {
						MessagePopUp msg = new MessagePopUp(frame, "Oops! You cannot delete a package that has used services.", false);
						msg.setVisible(true);

					} else {

						ActionRequiredPopUp deleteConfirmation = new ActionRequiredPopUp(frame,
								"Please confirm you would like to delete package edit:\n" + "\u2022 Package ID: "
										+ originalBodyPackage.getPurchaseID() + ".\nThis will permenantly remove the package from the database!",
								"Yes, please delete.", "No, do not delete.");
						deleteConfirmation.setVisible(true);

						if (deleteConfirmation.getActionValue()) {

							try {
								packagedDao.deleteBodyPackage(originalBodyPackage);
								MainFrame.updateMainTextFields(customerDao.getCustomerById(customer.getID()));
								EditBodyPackageDialog.this.dispose();
							} catch (ApplicationException e) {
								LOG.debug("Error in EditBodyPackageDialog() deleteService button");
							}

						} else {
							deleteConfirmation.dispose();
						}
					}
				}
			});
			deletePackageButton.setFont(UiCommon.DIALOG_FONT);
			buttonPane.add(deletePackageButton);

			JLabel lblNewLabel_1 = new JLabel("                        ");
			buttonPane.add(lblNewLabel_1);

			okButton = UiCommon.setOKButton(" OK ");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					LocalDate date = LocalDate.parse(textFieldDate.getText(), UiCommon.DATE_FORMAT_UI);
					String purchaseDate = date.format(UiCommon.DATETIME_FORMAT_MMddyyyy);

					String packagedSelection = String.valueOf(packageComboBox.getSelectedItem());
					packageProductCode = packagedSelection.substring(0, 3); // parse package code

					String staff = String.valueOf(staffComboBox.getSelectedItem());
					Matcher matcher = Pattern.compile("\\d+").matcher(staff);
					matcher.find();

					int i = Integer.valueOf(matcher.group());
					long staffID = i;
					ArrayList<Long> origServiceIDs = originalBodyPackage.getServices();

					BodyPackage editedBodyPackage = new BodyPackage(originalBodyPackage.getPurchaseID(), purchaseDate, packageProductCode,
							packageName, packagePrice, totalGST, totalCost, customer.getID(), staffID, origServiceIDs, packagePrice);

					ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame, "Please confirm your edit:\n" + "\u2022 Date: "
							+ date.format(UiCommon.DATE_FORMAT_UI) + "\n\u2022 Staff: " + staff + "\n\u2022 Package: " + packageName
							+ "\n\u2022 Package Price: $" + UiCommon.TWO_DECIMAL_FORMAT.format(packagePrice) + "  \u2022 GST: $"
							+ UiCommon.TWO_DECIMAL_FORMAT.format(totalGST) + "\n\u2022 Total Cost: $" + UiCommon.TWO_DECIMAL_FORMAT.format(totalCost),
							"Yes. This is correct.", "No, I need to change something.");

					popUp.setVisible(true);
					if (popUp.getActionValue()) {
						try {
							packagedDao.deleteBodyPackage(originalBodyPackage);
							packagedDao.updateEditedBodyPackage(editedBodyPackage);
							MainFrame.updateMainTextFields(customerDao.getCustomerById(customer.getID()));
							EditBodyPackageDialog.this.dispose();
						} catch (ApplicationException e) {
							LOG.debug("Error in EditBodyDialogDialog() confirm popUp action required.");
						}

					} else {

						popUp.dispose();
					}

				}
			});

			buttonPane.add(okButton);

			JButton cancelButton = UiCommon.setCancelButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					EditBodyPackageDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		JLabel lblCancelBtnSpacer = new JLabel("        ");
		buttonPane.add(lblCancelBtnSpacer);
	}

}
