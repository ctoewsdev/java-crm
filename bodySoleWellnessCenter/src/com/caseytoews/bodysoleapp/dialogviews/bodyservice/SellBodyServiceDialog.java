/**
 * Project: bodySoleWellnessCenter
 * File: AddCustomerDialog.java
 * Date: Dec 19, 2018
 * Time: 1:17:27 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.bodyservice;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.caseytoews.bodysoleapp.database.people.GroupDao;
import com.caseytoews.bodysoleapp.database.people.StaffDao;
import com.caseytoews.bodysoleapp.database.product.ProductDetailsDao;
import com.caseytoews.bodysoleapp.database.sales.BodyPackageDao;
import com.caseytoews.bodysoleapp.database.sales.BodyServiceDao;
import com.caseytoews.bodysoleapp.dialogviews.common.ActionRequiredPopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.MessagePopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.people.Group;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.domain.product.BodyPackageProduct;
import com.caseytoews.bodysoleapp.domain.product.BodyServiceProduct;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.utility.calculator.CurrencyCalculator;
import com.caseytoews.bodysoleapp.utility.comparator.CompareByPurchaseDateDesc;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class SellBodyServiceDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private Customer customer;
	private JTextField customerName;
	private JButton okButton;
	// private JTextField textFieldDate;
	private JTextField textFieldDate;
	private JFormattedTextField textFieldPrice;
	private JFormattedTextField textFieldGST;
	private JFormattedTextField textFieldTotalCost;
	private JTextField textFieldSubTotal;
	private JComboBox<String> packageComboBox;
	private boolean hasCurrentPackage;
	private boolean isPackageSet;

	// variables for constructing Service Object
	private String serviceProductName;
	private String serviceProductCodeSubstring = "---";
	private String packageProductCodeSubstring = "---";
	private double servicePrice;
	private double subtotal;
	private double totalGST;
	private double totalCost;
	private long servicePackageID;
	ArrayList<BodyPackage> activeCustomerPackages;
	private boolean isServiceChanged;
	private boolean isPackageChanged;

	/**
	 * Create the dialog.
	 */
	public SellBodyServiceDialog(JFrame frame, CustomerDao customerDao, BodyServiceDao serviceDao, BodyPackageDao packagedDao,
			ProductDetailsDao productsDao, StaffDao staffDao, GroupDao groupDao) throws ApplicationException {
		super(frame, true);
		setTitle("Purchase Service");
		customer = MainFrame.getMainFrameCustomer();
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 742, 310);
		setLocationRelativeTo(frame);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[100][230][100][220][30]", "[][][][][][][]"));

		// CUSTOMER
		JLabel lblCustomer = new JLabel("Customer: ");
		lblCustomer.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblCustomer, "cell 0 1,alignx trailing");
		customerName = UiCommon.setNotEditableTextField();
		customerName.setText(customer.getFirstName() + " " + customer.getLastName());
		contentPanel.add(customerName, "cell 1 1,growx");

		// DATE
		JLabel lblDate = new JLabel("    Date:");
		lblDate.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblDate, "cell 3 1,alignx right,aligny center");
		textFieldDate = UiCommon.setEditableTextField();
		// textFieldDate = UiCommon.setFormattedDateField();
		LocalDate date = LocalDate.now();
		textFieldDate.setText(date.format(UiCommon.DATE_FORMAT_UI));
		contentPanel.add(textFieldDate, "cell 3 1, growx");

		// STAFF MEMBER PERFORMING THE SERVICE
		JLabel lblStaff = new JLabel("Staff:");
		lblStaff.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblStaff, "cell 2 2,alignx trailing,aligny center");
		JComboBox<String> staffComboBox = new JComboBox<>();
		staffComboBox.setFont(UiCommon.DIALOG_FONT);
		staffComboBox.addItem("--- Staff ---");
		List<Staff> staff = staffDao.getAllStaff();
		for (Staff s : staff) {
			staffComboBox.addItem("(#" + s.getID() + ") " + s.getFirstName() + " " + s.getLastName());
		}
		contentPanel.add(staffComboBox, "cell 3 2,growx");

		// TYPE OF SERVICE
		JLabel lblService = new JLabel("Service:");
		lblService.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblService, "cell 0 2,alignx trailing");
		ArrayList<BodyServiceProduct> services = productsDao.getProductDetails().getServiceProducts();
		JComboBox<String> serviceComboBox = new JComboBox<>();
		serviceComboBox.setFont(UiCommon.DIALOG_FONT);
		serviceComboBox.addItem("--- Service ---");
		for (BodyServiceProduct s : services) {
			String serviceProduct = s.getProductCode() + " | " + s.getProductName();
			serviceComboBox.addItem(serviceProduct);
		}
		contentPanel.add(serviceComboBox, "cell 1 2,growx");

		// PACKAGE
		JLabel lblPackage = new JLabel("Package:");
		lblPackage.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPackage, "cell 0 3,alignx trailing");
		packageComboBox = new JComboBox<>();
		packageComboBox.setFont(UiCommon.DIALOG_FONT);

		ArrayList<BodyPackage> activeCustomerPackages = packagedDao.getPackagesByCustomerID(customer.getID());
		// Get Group Members Packages
		if (!groupDao.validateGroupMember(customer.getID())) {
			Group group = groupDao.getGroupByCustomerId(customer.getID());
			ArrayList<Long> memberIDs = group.getMembers();
			for (long mID : memberIDs) {
				if (mID != customer.getID()) {
					ArrayList<BodyPackage> packages = packagedDao.getPackagesByCustomerID(mID);
					for (BodyPackage p : packages) {
						activeCustomerPackages.add(p);
					}
				}
			}
		}
		if (activeCustomerPackages.size() > 0) {
			Iterator<BodyPackage> iter = activeCustomerPackages.iterator();
			while (iter.hasNext()) {
				BodyPackage packaged = iter.next();
				double packagedRemaining = packagedDao.getPackagedRemaining(packaged);
				if (packagedRemaining <= 0.0) {
					iter.remove();
				}
			}

			// IF THERE ARE REMAINING (THUS VALID) PACKAGES
			if (activeCustomerPackages.size() > 0) {
				activeCustomerPackages.sort(new CompareByPurchaseDateDesc());
				packageComboBox.addItem("--- No Package ---"); // default index[0] as single service
				hasCurrentPackage = true;
				for (BodyPackage p : activeCustomerPackages) {
					String balance = UiCommon.TWO_DECIMAL_FORMAT.format(packagedDao.getPackagedBalance(p));
					double remaining = packagedDao.getPackagedRemaining(p);
					LocalDate purchaseDate = LocalDate.parse(p.getPurchaseDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
					String currentPackage = p.getProductCode() + " (#" + p.getPurchaseID() + ") | " + purchaseDate.format(UiCommon.DATE_FORMAT_UI)
							+ " | " + p.getProductName() + " | " + " Remaining: " + remaining + " | " + " = $" + balance;
					packageComboBox.addItem(currentPackage);
				}

			} else {
				hasCurrentPackage = false;
				packageComboBox.addItem("Customer does not have a current package."); //
			}

		} else {
			hasCurrentPackage = false;
			packageComboBox.addItem("Customer does not have a current package."); //
		}

		contentPanel.add(packageComboBox, "flowx,cell 1 3 3,growx");

		// SUBTOTAL
		JLabel labelSubTotal = new JLabel("Sub Total:");
		labelSubTotal.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(labelSubTotal, "cell 0 5,alignx trailing");
		textFieldSubTotal = UiCommon.setCurrencyField();
		contentPanel.add(textFieldSubTotal, "cell 1 5,alignx left");

		// GST COST
		JLabel lblGst = new JLabel("       GST:");
		lblGst.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblGst, "cell 1 5,alignx right");
		textFieldGST = UiCommon.setCurrencyField();
		textFieldGST.setEditable(false);
		contentPanel.add(textFieldGST, "cell 1 5,alignx left,aligny center");

		// QUANTITY
		JLabel lblQty = new JLabel("Qty:");
		lblQty.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblQty, "cell 2 4,alignx trailing");
		JComboBox<Double> qtyComboBox = new JComboBox<>();
		qtyComboBox.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(qtyComboBox, "flowx,cell 3 4,alignx left");
		qtyComboBox.addItem(0.0);
		ArrayList<Double> quantities = productsDao.getProductDetails().getQuantities();
		for (Double q : quantities) {
			qtyComboBox.addItem(q);
		}

		// PRICE 3 4
		JLabel lblPrice = new JLabel("        Price:");
		lblPrice.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPrice, "cell 3 4,alignx right");
		textFieldPrice = UiCommon.setCurrencyField();
		contentPanel.add(textFieldPrice, "cell 3 4,alignx left,aligny center");

		// TOTAL COST
		JLabel lblTotalCost = new JLabel("             Total Cost:");
		lblTotalCost.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblTotalCost, "cell 3 5,alignx right");
		textFieldTotalCost = UiCommon.setCurrencyField();
		textFieldTotalCost.setEditable(false);
		contentPanel.add(textFieldTotalCost, "cell 3 5,alignx left,aligny center");

		// THIS LISTENER SETS ALL THE COST RELATED TEXT FIELDS
		ActionListener servicePackageComboListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					servicePrice = 0;
					totalGST = 0;
					totalCost = 0;
					servicePackageID = 0;

					// SERVICE PRODUCT CODE SUBSTRING
					String serviceSelection = String.valueOf(serviceComboBox.getSelectedItem());
					String tempServiceProductCodeSubstring = serviceSelection.substring(0, 3);// set Service Object variable

					// TEST IF SERVICE HAS CHANGED
					if (!serviceProductCodeSubstring.equalsIgnoreCase(tempServiceProductCodeSubstring)) {
						isServiceChanged = true;
					} else {
						isServiceChanged = false;
					}
					// ASSIGN TEMP TO SERVICEPRODUCTCODESUBSTRING
					serviceProductCodeSubstring = tempServiceProductCodeSubstring;
					BodyServiceProduct sp = productsDao.getServiceProductDetailsByProductCode(serviceProductCodeSubstring);

					// PACKAGE SELECTION SUBSTRING
					String packageSelection = String.valueOf(packageComboBox.getSelectedItem());
					String tempPackageProductCodeSubstring = packageSelection.substring(0, 3);

					// TEST IF PACKAGE HAS CHANGED
					if (!packageProductCodeSubstring.equalsIgnoreCase(tempPackageProductCodeSubstring)) {
						isPackageChanged = true;
					} else {
						isPackageChanged = false;
					}
					packageProductCodeSubstring = tempPackageProductCodeSubstring;

					if (packageProductCodeSubstring.equalsIgnoreCase("---")) {
						isPackageSet = false;
					}
					// NO SERVICE SELECTED - RESET VALUES
					if (serviceProductCodeSubstring.equalsIgnoreCase("---")) {
						textFieldSubTotal.setText("0.00");
						textFieldTotalCost.setText("0.00");
						textFieldGST.setText("0.00");
						textFieldPrice.setText("0.00");
						// packageComboBox.setSelectedIndex(0);
					}

					// SERVICE IS SELECTED
					else {

						// HAS CURRENT PACKAGE AND APPLICABLE SERVICE
						if (hasCurrentPackage && (serviceProductCodeSubstring.equalsIgnoreCase("F50")
								|| serviceProductCodeSubstring.equalsIgnoreCase("B50") || serviceProductCodeSubstring.equalsIgnoreCase("A60"))) {

							// IF SERVICE HAS CHANGED
							if (isServiceChanged) {

								// BODY SERVICE
								if (serviceProductCodeSubstring.equalsIgnoreCase("F50") || serviceProductCodeSubstring.equalsIgnoreCase("B50")) {
									int i = 1;
									for (BodyPackage pack : activeCustomerPackages) {
										if (pack.getProductCode().equalsIgnoreCase("B11") || pack.getProductCode().equalsIgnoreCase("B12")) {
											isPackageSet = true;
											packageComboBox.setSelectedIndex(i); // set to evaluated package

											break;
										}
										i++;
										packageComboBox.setSelectedIndex(0); // if no package was assigned
									}

								} else { // ACUPUNCTURE SERVICE
									int i = 1;
									for (BodyPackage pack : activeCustomerPackages) {
										if (pack.getProductCode().equalsIgnoreCase("A05") || pack.getProductCode().equalsIgnoreCase("A11")) {
											packageComboBox.setSelectedIndex(i); // set to evaluated package
											isPackageSet = true;
											break;
										}
										i++;
										packageComboBox.setSelectedIndex(0); // since no package was assigned
									}

								}

								// IF PACKAGE HAS CHANGED
							} else if (isPackageChanged) {

								// BODY SERVICE
								if (serviceProductCodeSubstring.equalsIgnoreCase("F50") || serviceProductCodeSubstring.equalsIgnoreCase("B50")) {
									if (packageProductCodeSubstring.equalsIgnoreCase("B11") || packageProductCodeSubstring.equalsIgnoreCase("B12")) {
										isPackageSet = true;
									} else {
										packageComboBox.setSelectedIndex(0);
									}

								} else { // ACUPUNCTURE SERVICE

									if (packageProductCodeSubstring.equalsIgnoreCase("A05") || packageProductCodeSubstring.equalsIgnoreCase("A11")) {
										isPackageSet = true;
									} else {
										packageComboBox.setSelectedIndex(0);
									}
								}

							} // END PACKAGE HAS CHANGED

						} else if (hasCurrentPackage) {// HAS PACKAGE BUT NOT PACKAGABLE SERVCIE
							servicePrice = sp.getProductPrice();
							isPackageSet = false;
							packageComboBox.setSelectedIndex(0);
						} else { // HAS NO PACKAGE

						}

						// HAS PACKAGE AND IT HAS BEEN SET
						if (isPackageSet) {

							// GET PACKAGE ID
							if (!packageSelection.startsWith("---")) {
								Matcher packageIDMatcher = Pattern.compile("#(\\d+)").matcher(packageSelection);
								packageIDMatcher.find();
								servicePackageID = Long.valueOf(packageIDMatcher.group(1));
							}

							// MAKE SURE QTY DOES NOT EXCEED REMAINING OR SET TO MAX OF REMAINING ON SELECTED PACKAGE
							String stringQty = String.valueOf(qtyComboBox.getSelectedItem());
							double doubleQty = Double.parseDouble(stringQty);
							BodyPackage selectedPackage = packagedDao.getPackagedByPackagedId(servicePackageID);
							double selectedPackageRemaining = packagedDao.getPackagedRemaining(selectedPackage);
							double allPackagesRemaining = 0.0;
							double financials[] = customerDao.getCustomerSales_Balance_Remaining(customer);
							if (packageProductCodeSubstring.startsWith("B")) {
								allPackagesRemaining = financials[2];

							} else if (packageProductCodeSubstring.startsWith("A")) {
								allPackagesRemaining = financials[3];
							} else {
								LOG.error("SellService() calculating all packages remaining ERROR: package does not start with 'A' or 'B'");
							}

							if (doubleQty > selectedPackageRemaining) {
								// reset quantity to the max available for the package
								int index = (int) (selectedPackageRemaining / .5);
								qtyComboBox.setSelectedIndex(index);

								if (doubleQty > allPackagesRemaining) {

									MessagePopUp popUp = new MessagePopUp(frame,
											"Quantity (" + doubleQty + ") is larger than the remaining. Customer has '" + (allPackagesRemaining)
													+ "' remaining with available package(s).",
											false);
									popUp.setVisible(true);

								} else {
									MessagePopUp popUp = new MessagePopUp(frame,
											"Quantity (" + doubleQty + ") is larger than the remaining. The remaining '"
													+ (doubleQty - selectedPackageRemaining) + "' can be sold with the next available package.",
											false);
									popUp.setVisible(true);

								}

							}
							BodyPackageProduct pp = productsDao.getPackageProductDetailsByProductCode(packageProductCodeSubstring);
							servicePrice = pp.getPricePerPackageService();

						} else {
							servicePrice = sp.getProductPrice();
						}

						// PRICE
						CurrencyCalculator service = new CurrencyCalculator(Double.toString(servicePrice));
						String price = service.bigDecimalToString(service.getAmountOne());
						textFieldPrice.setText(price);

						// SUBTOTAL (PRICE * QTY)
						String qty = String.valueOf(qtyComboBox.getSelectedItem());
						CurrencyCalculator calculateSubTotal = new CurrencyCalculator(price, qty);
						String subT = calculateSubTotal.getProduct();
						textFieldSubTotal.setText(subT);
						subtotal = Double.parseDouble(subT);

						// GST
						String gst;
						if (serviceProductCodeSubstring.equalsIgnoreCase("A60")) {
							gst = "0.00";
						} else {

							String GSTConstant = Double.toString(productsDao.getProductDetails().getGST());
							CurrencyCalculator calculateGST = new CurrencyCalculator(subT, GSTConstant);
							gst = calculateGST.getProduct();
							totalGST = Double.parseDouble(gst);
						}
						textFieldGST.setText(gst);

						// TOTAL COST
						CurrencyCalculator calculateTotalCost = new CurrencyCalculator(subT, gst);
						String cost = calculateTotalCost.getSum();
						textFieldTotalCost.setText(cost);
						totalCost = Double.parseDouble(cost);
					}

				} catch (ApplicationException e) {
					LOG.error("ERROR in sellServiceComboListener()" + e);
				}
			}
		};

		// Set Listeners
		packageComboBox.addActionListener(servicePackageComboListener);
		serviceComboBox.addActionListener(servicePackageComboListener);
		textFieldPrice.addActionListener(servicePackageComboListener);
		qtyComboBox.addActionListener(servicePackageComboListener);

		// OK AND CANCEL BUTTONS
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{

			okButton = UiCommon.setOKButton(" OK ");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					Pattern DATE_PATTERN = Pattern.compile("^[0-9][0-9]/[0-9][0-9]/20[123][0-9]$");
					String date = textFieldDate.getText();

					if (!DATE_PATTERN.matcher(date).matches()) {

						JOptionPane.showMessageDialog(null, "Sorry, but the date must be in the format of mm/dd/yyyy", "dialog",
								JOptionPane.ERROR_MESSAGE);

					}

					else if (serviceProductCodeSubstring.equalsIgnoreCase("---")) {
						MessagePopUp msg = new MessagePopUp(frame, "Oops! You have not selected a service.", false);
						msg.setVisible(true);

					}

					else if (String.valueOf(staffComboBox.getSelectedItem()).startsWith("---")) {
						MessagePopUp msg = new MessagePopUp(frame, "Oops! You have not selected the staff.", false);
						msg.setVisible(true);
					} else if (String.valueOf(qtyComboBox.getSelectedItem()).equalsIgnoreCase("0.0")) {
						MessagePopUp msg = new MessagePopUp(frame, "Please select the quantity.", false);
						msg.setVisible(true);
					} else {

						// @param purchaseDate
						LocalDate dateOfService = LocalDate.parse(textFieldDate.getText(), UiCommon.DATE_FORMAT_UI);
						String serviceDate = dateOfService.format(UiCommon.DATETIME_FORMAT_MMddyyyy);

						// @param productCode (serviceProductCode is assigned in package/server listener)

						// @param productName
						try {
							serviceProductName = productsDao.getServiceProductDetailsByProductCode(serviceProductCodeSubstring).getProductName();
						} catch (ApplicationException e1) {

						}

						// @param customerID
						long customerID = customer.getID();

						// @param quantity
						String quantity = String.valueOf(qtyComboBox.getSelectedItem());
						double serviceQuantity = Double.parseDouble(quantity); // assign value to Service Object Field

						// @param totalCost
						// class variable totalCost

						// @param staffID
						// set service object variable

						String staff = String.valueOf(staffComboBox.getSelectedItem());
						Matcher staffMatcher = Pattern.compile("(\\d+)").matcher(staff);
						staffMatcher.find();
						int i = Integer.valueOf(staffMatcher.group(1));
						long serviceStaffID = i;

						// @param packageID
						// class variable servicePackageID

						BodyService newService = new BodyService(serviceDate, serviceProductCodeSubstring, serviceProductName, servicePrice, totalGST,
								customerID, serviceStaffID, serviceQuantity, subtotal, totalCost, servicePackageID);
						String packageMessage = "";
						if (servicePackageID == 0) {
							packageMessage = "N/A (no package used).";
						} else {
							try {
								String packageName = packagedDao.getPackagedByPackagedId(servicePackageID).getProductName();
								packageMessage = "Using package \"" + packageName + "\"";
							} catch (ApplicationException e) {
							}

						}

						// GET CONFIRMATIONS FROM USER AND PROCESS THE SERVICE ORDER
						ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame,
								"Please confirm \"" + customer.getFirstName() + "\'s\" service purchase:\n\u2022 Date: "
										+ dateOfService.format(UiCommon.DATE_FORMAT_UI) + "\n\u2022 Staff: " + staff + "\n\u2022 Service: "
										+ serviceProductName + "\n\u2022 Quantity: " + quantity + " (don't mix body and feet)\n\u2022 Package:  "
										+ packageMessage,
								"Yes. This is correct.", "No, I need to change something.");
						popUp.setVisible(true);

						if (popUp.getActionValue()) {
							try {
								serviceDao.addService(newService);
								MainFrame.updateMainTextFields(customerDao.getCustomerById(customer.getID()));
								SellBodyServiceDialog.this.dispose();
							} catch (ApplicationException e) {
								LOG.debug("Error in SellServiceDialog() OK button");
							}

						} else {

							popUp.dispose();
						}
					}
				}

			});

			buttonPane.add(okButton);

			JButton cancelButton = UiCommon.setCancelButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SellBodyServiceDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		JLabel lblNewLabel_1 = new JLabel("            ");
		buttonPane.add(lblNewLabel_1);
	}

}
