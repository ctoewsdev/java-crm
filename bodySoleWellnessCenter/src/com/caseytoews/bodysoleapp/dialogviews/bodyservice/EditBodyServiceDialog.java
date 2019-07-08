/**
 * Project: bodySoleWellnessCenter
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
import javax.swing.JCheckBox;
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

public class EditBodyServiceDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private Customer customer;
	private JTextField customerName;
	private JButton okButton;
	private JTextField textFieldDate;
	private JFormattedTextField textFieldPrice;
	private JFormattedTextField textFieldGST;
	private JFormattedTextField textFieldTotalCost;
	private JTextField textFieldSubTotal;
	private JComboBox<String> packageComboBox;
	private boolean hasCurrentPackage;

	// variables for constructing Service Object
	private String serviceProductName;
	private String serviceProductCodeSubstring = "Ple";
	private double servicePrice;
	private double subtotal;
	private double totalGST;
	private double totalCost;
	private long servicePackageID;
	ArrayList<BodyPackage> activeCustomerPackages;
	private BodyService originalService;

	// Create the dialog.
	public EditBodyServiceDialog(long serviceID, JFrame frame, CustomerDao customerDao, BodyServiceDao serviceDao, BodyPackageDao packagedDao,
			ProductDetailsDao productsDao, StaffDao staffDao, GroupDao groupDao) throws ApplicationException {
		super(frame, true);
		// preserve original service in case it is restored
		originalService = serviceDao.getServiceById(serviceID);

		// remove original service from database to return state to the purchase state
		serviceDao.deleteService(originalService);

		// set up the UI
		setTitle("Edit Body Service");
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
		LocalDate date = LocalDate.parse(originalService.getPurchaseDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
		textFieldDate.setText(date.format(UiCommon.DATE_FORMAT_UI));
		contentPanel.add(textFieldDate, "cell 3 1, growx");

		// STAFF MEMBER PERFORMING THE SERVICE
		JLabel lblStaff = new JLabel("Staff:");
		lblStaff.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblStaff, "cell 2 2,alignx trailing,aligny center");
		JComboBox<String> staffComboBox = new JComboBox<>();
		staffComboBox.setFont(UiCommon.DIALOG_FONT);
		List<Staff> staff = staffDao.getAllStaff();
		int count = 0;
		for (Staff s : staff) {
			staffComboBox.addItem("(#" + s.getID() + ") " + s.getFirstName() + " " + s.getLastName());
			if (s.getID() == originalService.getStaffID()) {
				staffComboBox.setSelectedIndex(count);
			}
			count++;
		}
		contentPanel.add(staffComboBox, "cell 3 2,growx");

		// TYPE OF SERVICE
		JLabel lblService = new JLabel("Service:");
		lblService.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblService, "cell 0 2,alignx trailing");
		ArrayList<BodyServiceProduct> services = productsDao.getProductDetails().getServiceProducts();
		JComboBox<String> serviceComboBox = new JComboBox<>();
		serviceComboBox.setFont(UiCommon.DIALOG_FONT);
		serviceComboBox.addItem("Please select a service");
		count = 1;
		for (BodyServiceProduct s : services) {
			String serviceProduct = s.getProductCode() + " | " + s.getProductName();
			serviceComboBox.addItem(serviceProduct);
			if (s.getProductCode().equalsIgnoreCase(originalService.getProductCode())) {
				serviceComboBox.setSelectedIndex(count);
			}
			count++;
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
				count = 1;
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
					if (p.getPurchaseID() == originalService.getPackageID()) {
						packageComboBox.setSelectedIndex(count);
					}
					count++;
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

		// DO NOT USE PACKAGE
		JCheckBox chckbxDoNotUsePack = new JCheckBox("Do not use package");
		chckbxDoNotUsePack.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(chckbxDoNotUsePack, "cell 1 4");

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
		ArrayList<Double> quantities = productsDao.getProductDetails().getQuantities();
		count = 0;
		int qtyBoxIndex = 0;
		for (Double q : quantities) {
			qtyComboBox.addItem(q);
			if (q == originalService.getQuantity()) {
				qtyBoxIndex = count;
			}
			count++;
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

					boolean doNotUsePackage = chckbxDoNotUsePack.isSelected();

					// SERVICE PRODUCT CODE SUBSTRING
					String serviceSelection = String.valueOf(serviceComboBox.getSelectedItem());
					serviceProductCodeSubstring = serviceSelection.substring(0, 3);// set Service Object variable
					BodyServiceProduct sp = productsDao.getServiceProductDetailsByProductCode(serviceProductCodeSubstring);
					boolean packageIsSet = false;

					// NO SERVICE SELECTED - RESET VALUES
					if (serviceProductCodeSubstring.equalsIgnoreCase("Ple")) {
						qtyComboBox.setSelectedIndex(0);
						textFieldSubTotal.setText("0.00");
						textFieldTotalCost.setText("0.00");
						textFieldGST.setText("0.00");
						textFieldPrice.setText("0.00");
						packageComboBox.setSelectedIndex(0);
					}

					else if (doNotUsePackage) {
						packageComboBox.setSelectedIndex(0);
						servicePrice = sp.getProductPrice();

					}
					// SERVICE IS SELECTED - SET PRICE TO REGULAR PRODUCT PRICE THEN TEST BELOW
					else {
						servicePrice = sp.getProductPrice();

						// IF HAS CURRENT PACKAGE and SERVICE IS PACKAGEABLE
						if (hasCurrentPackage && (serviceProductCodeSubstring.equalsIgnoreCase("F50")
								|| serviceProductCodeSubstring.equalsIgnoreCase("B50") || serviceProductCodeSubstring.equalsIgnoreCase("A60"))) {

							// BODY SERVICE - (FOOT OR BODY)
							if (serviceProductCodeSubstring.equalsIgnoreCase("F50") || serviceProductCodeSubstring.equalsIgnoreCase("B50")) {
								int i = 1;
								for (BodyPackage pack : activeCustomerPackages) {
									if (pack.getProductCode().equalsIgnoreCase("B11") || pack.getProductCode().equalsIgnoreCase("B12")) {
										packageComboBox.setSelectedIndex(i); // set to evaluated package
										packageIsSet = true;
										break;
									}
									i++;
								}
								if (!packageIsSet) {
									packageComboBox.setSelectedIndex(0); // set to no package
								}
							}

							// SERVICE IS ACCUPUNCTURE
							else {
								int i = 1;
								for (BodyPackage pack : activeCustomerPackages) {
									if (pack.getProductCode().equalsIgnoreCase("A05") || pack.getProductCode().equalsIgnoreCase("A11")) {
										packageComboBox.setSelectedIndex(i); // set to evaluated package
										packageIsSet = true;
										break;
									}
									i++;
								}
								if (!packageIsSet) {
									packageComboBox.setSelectedIndex(0); // set to no package
								}

							}

						} // DOES NOT HAVE A PACKAGE OR SERVICE IS NOT PACKAGEABLE
						else {
							packageComboBox.setSelectedIndex(0);
						}
					} // END SERVICE PRICE EVALUATION

					if (packageIsSet) {

						// PACKAGE SELECTION SUBSTRING
						String packageSelection = String.valueOf(packageComboBox.getSelectedItem());
						String packageProdCodeSubstring = packageSelection.substring(0, 3);

						// GET PACKAGE ID
						Matcher packageIDMatcher = Pattern.compile("#(\\d+)").matcher(packageSelection);
						packageIDMatcher.find();
						servicePackageID = Long.valueOf(packageIDMatcher.group(1));

						// MAKE SURE QTY DOES NOT EXCEED REMAINING
						String sqq = String.valueOf(qtyComboBox.getSelectedItem());
						double dqq = Double.parseDouble(sqq);
						BodyPackage pack = packagedDao.getPackagedByPackagedId(servicePackageID);
						double rr = packagedDao.getPackagedRemaining(pack);
						if (dqq > rr) {
							int index = (int) (rr / .5);
							qtyComboBox.setSelectedIndex(index - 1);
							MessagePopUp popUp = new MessagePopUp(frame,
									"Selected quantity can not be larger than remaining. Quantity has been set to maximum remaining.", false);
							popUp.setVisible(true);
						} else {
							BodyPackageProduct pp = productsDao.getPackageProductDetailsByProductCode(packageProdCodeSubstring);
							servicePrice = pp.getPricePerPackageService();
						}

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

				} catch (ApplicationException e) {
					LOG.error("ERROR in sellServiceComboListener()" + e);
				}
			}
		};

		packageComboBox.addActionListener(servicePackageComboListener);
		serviceComboBox.addActionListener(servicePackageComboListener);
		textFieldPrice.addActionListener(servicePackageComboListener);
		qtyComboBox.addActionListener(servicePackageComboListener);
		chckbxDoNotUsePack.addActionListener(servicePackageComboListener);
		qtyComboBox.setSelectedIndex(qtyBoxIndex);

		// OK AND CANCEL BUTTONS
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton deleteServiceButton = new JButton("   Delete this Service   ");
			deleteServiceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ActionRequiredPopUp deleteConfirmation = new ActionRequiredPopUp(
							frame, "Please confirm you would like to delete service edit:\n" + "\u2022 Service Number: "
									+ originalService.getPurchaseID() + ".\nThis will permenantly remove the service from the database!",
							"Yes, please delete.", "No, do not delete.");
					deleteConfirmation.setVisible(true);

					if (deleteConfirmation.getActionValue()) {
						try {
							MainFrame.updateMainTextFields(customerDao.getCustomerById(customer.getID()));
							EditBodyServiceDialog.this.dispose();
						} catch (ApplicationException e) {
							LOG.debug("Error in EditServiceDialog() deleteService button");
						}

					} else {

						deleteConfirmation.dispose();
					}

				}
			});
			deleteServiceButton.setFont(UiCommon.DIALOG_FONT);
			buttonPane.add(deleteServiceButton);

			JLabel lblNewLabel_1 = new JLabel("                        ");
			buttonPane.add(lblNewLabel_1);

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

					if (serviceProductCodeSubstring.equalsIgnoreCase("Ple")) {
						MessagePopUp msg = new MessagePopUp(frame, "Oops! You have not selected a service.", false);
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

						BodyService editedService = new BodyService(originalService.getPurchaseID(), serviceDate, serviceProductCodeSubstring,
								serviceProductName, servicePrice, totalGST, customerID, serviceStaffID, serviceQuantity, subtotal, totalCost,
								servicePackageID);
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
								"Please confirm your edit:\n" + "\u2022 Date: " + dateOfService.format(UiCommon.DATE_FORMAT_UI) + "\n\u2022 Staff: "
										+ staff + "\n\u2022 Service: " + serviceProductName + "\n\u2022 Quantity: " + quantity
										+ " (don't mix body and feet)\n\u2022 Package:  " + packageMessage,
								"Edit is correct.", "No, I need to change something.");
						popUp.setVisible(true);

						if (popUp.getActionValue()) {
							try {
								serviceDao.updateEditedService(editedService);
								MainFrame.updateMainTextFields(customerDao.getCustomerById(customer.getID()));
								EditBodyServiceDialog.this.dispose();
							} catch (ApplicationException e) {
								LOG.debug("Error in EditServiceDialog() OK button");
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
					try {
						serviceDao.updateEditedService(originalService);
					} catch (ApplicationException e1) {
						// TODO Auto-generated catch block
						LOG.debug("Error in EditServiceDialog() Cancel button");
					}
					EditBodyServiceDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		JLabel lblNewLabel_1 = new JLabel("            ");
		buttonPane.add(lblNewLabel_1);
	}

}
