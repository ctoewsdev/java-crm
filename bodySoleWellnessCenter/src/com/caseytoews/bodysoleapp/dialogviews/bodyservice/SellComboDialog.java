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

public class SellComboDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private Customer customer;
	private JTextField customerName;
	private JButton okButton;
	private JTextField textFieldDate;
	private JFormattedTextField textFieldBodyPrice;
	private JFormattedTextField textFieldFootPrice;
	private JFormattedTextField textFieldGST;
	private JFormattedTextField textFieldTotalCost;
	private JTextField textFieldSubTotal;
	private JComboBox<String> packageComboBox;
	private boolean hasCurrentBodyPackage;

	// variables for constructing Service Object
	private String bodyServiceProductName;
	private String footServiceProductName;
	private String bodyServiceProductCodeSubstring;
	private String footServiceProductCodeSubstring;
	private double bodyServicePrice;
	private double footServicePrice;
	private long servicePackageID;
	ArrayList<BodyPackage> activeCustomerPackages;
	private double footQty;
	private double bodyQty;
	private double footSubtotal;
	private double bodySubtotal;
	private double footGST;
	private double bodyGST;
	private double footTotalCost;
	private double bodyTotalCost;
	private String packageProdCodeSubstring = "---";

	// Create the dialog.
	public SellComboDialog(double comboQuantity, JFrame frame, CustomerDao customerDao, BodyServiceDao serviceDao, BodyPackageDao packagedDao,
			ProductDetailsDao productsDao, StaffDao staffDao, GroupDao groupDao) throws ApplicationException {
		super(frame, true);

		if (comboQuantity == 1.5) {
			bodyQty = 1.0;
			footQty = 0.5;
		}
		if (comboQuantity == 2.0) {
			bodyQty = 1.0;
			footQty = 1.0;
		}

		setTitle("Purchase Service");
		customer = MainFrame.getMainFrameCustomer();
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 742, 450);
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
		LocalDate date = LocalDate.now();
		textFieldDate.setText(date.format(UiCommon.DATE_FORMAT_UI));
		contentPanel.add(textFieldDate, "cell 3 1, growx");

		// BODY SERVICE
		JLabel lblBodyService = new JLabel("Body:");
		lblBodyService.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblBodyService, "cell 0 2,alignx trailing");
		JComboBox<String> bodyServiceComboBox = new JComboBox<>();
		bodyServiceComboBox.setFont(UiCommon.DIALOG_FONT);
		String bodyProduct = "B50 | Acupressure 50 | " + bodyQty;
		bodyServiceComboBox.addItem(bodyProduct);
		contentPanel.add(bodyServiceComboBox, "cell 1 2,growx");

		// BODY STAFF MEMBER
		JLabel lblBodyStaff = new JLabel("Staff:");
		lblBodyStaff.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblBodyStaff, "cell 2 2,alignx trailing,aligny center");
		JComboBox<String> bodyStaffComboBox = new JComboBox<>();
		bodyStaffComboBox.setFont(UiCommon.DIALOG_FONT);
		bodyStaffComboBox.addItem("--- Staff ---");
		List<Staff> staff = staffDao.getAllStaff();
		for (Staff s : staff) {
			bodyStaffComboBox.addItem("(#" + s.getID() + ") " + s.getFirstName() + " " + s.getLastName());
		}
		contentPanel.add(bodyStaffComboBox, "cell 3 2,growx");

		// BODY QUANTITY
		JLabel lblBodyQty = new JLabel("Qty:");
		lblBodyQty.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblBodyQty, "cell 0 3,alignx trailing");
		JComboBox<Double> qtyBodyComboBox = new JComboBox<>();
		qtyBodyComboBox.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(qtyBodyComboBox, "flowx,cell 1 3,alignx left");
		qtyBodyComboBox.addItem(bodyQty);

		// BODY PRICE
		JLabel lblBodyPrice = new JLabel("        Price:");
		lblBodyPrice.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblBodyPrice, "cell 1 3,alignx right");
		textFieldBodyPrice = UiCommon.setCurrencyField();
		contentPanel.add(textFieldBodyPrice, "cell 1 3,alignx left,aligny center");

		// FOOT SERVICE
		JLabel lblFootService = new JLabel("Foot:");
		lblFootService.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblFootService, "cell 0 4,alignx trailing");
		JComboBox<String> footServiceComboBox = new JComboBox<>();
		footServiceComboBox.setFont(UiCommon.DIALOG_FONT);
		String footProduct = "F50 | Reflexology 50 | " + footQty;
		footServiceComboBox.addItem(footProduct);
		contentPanel.add(footServiceComboBox, "cell 1 4,growx");

		// FOOT STAFF MEMBER
		JLabel lblFootStaff = new JLabel("Staff:");
		lblFootStaff.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblFootStaff, "cell 2 4,alignx trailing,aligny center");
		JComboBox<String> footStaffComboBox = new JComboBox<>();
		footStaffComboBox.setFont(UiCommon.DIALOG_FONT);
		footStaffComboBox.addItem("--- Staff ---");
		for (Staff s : staff) {
			footStaffComboBox.addItem("(#" + s.getID() + ") " + s.getFirstName() + " " + s.getLastName());
		}
		contentPanel.add(footStaffComboBox, "cell 3 4,growx");

		// FOOT QUANTITY
		JLabel lblFootQty = new JLabel("Qty:");
		lblFootQty.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblFootQty, "cell 0 5,alignx trailing");
		JComboBox<Double> qtyFootComboBox = new JComboBox<>();
		qtyFootComboBox.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(qtyFootComboBox, "flowx,cell 1 5,alignx left");
		qtyFootComboBox.addItem(footQty);

		// FOOT PRICE 3 4
		JLabel lblFootPrice = new JLabel("         Price:");
		lblFootPrice.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblFootPrice, "cell 1 5,alignx right");
		textFieldFootPrice = UiCommon.setCurrencyField();
		contentPanel.add(textFieldFootPrice, "cell 1 5,alignx left,aligny center");

		// PACKAGE
		JLabel lblPackage = new JLabel("Package:");
		lblPackage.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblPackage, "cell 0 6,alignx trailing");
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
						if (p.getProductCode().equalsIgnoreCase("B11") || p.getProductCode().equalsIgnoreCase("B12")) {
							activeCustomerPackages.add(p);
						}
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
				for (BodyPackage p : activeCustomerPackages) {
					if (p.getProductCode().equalsIgnoreCase("B11") || p.getProductCode().equalsIgnoreCase("B12")) {
						hasCurrentBodyPackage = true;
						String balance = UiCommon.TWO_DECIMAL_FORMAT.format(packagedDao.getPackagedBalance(p));
						double remaining = packagedDao.getPackagedRemaining(p);
						LocalDate purchaseDate = LocalDate.parse(p.getPurchaseDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
						String currentPackage = p.getProductCode() + " (#" + p.getPurchaseID() + ") | " + purchaseDate.format(UiCommon.DATE_FORMAT_UI)
								+ " | " + p.getProductName() + " | " + " Remaining: " + remaining + " | " + " = $" + balance;
						packageComboBox.addItem(currentPackage);
					}
				}

			} else {
				hasCurrentBodyPackage = false;
				packageComboBox.addItem("Customer does not have a current package."); //
			}

		} else {
			hasCurrentBodyPackage = false;
			packageComboBox.addItem("Customer does not have a current package."); //
		}

		contentPanel.add(packageComboBox, "flowx,cell 1 6 3,growx");

		// SUBTOTAL
		JLabel labelSubTotal = new JLabel("Sub Total:");
		labelSubTotal.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(labelSubTotal, "cell 0 8,alignx trailing");
		textFieldSubTotal = UiCommon.setCurrencyField();
		contentPanel.add(textFieldSubTotal, "cell 1 8,alignx left");

		// GST COST
		JLabel lblGst = new JLabel("       GST:");
		lblGst.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblGst, "cell 1 8,alignx right");
		textFieldGST = UiCommon.setCurrencyField();
		textFieldGST.setEditable(false);
		contentPanel.add(textFieldGST, "cell 1 8,alignx left,aligny center");

		// TOTAL COST
		JLabel lblTotalCost = new JLabel("             Total Cost:");
		lblTotalCost.setFont(UiCommon.DIALOG_FONT);
		contentPanel.add(lblTotalCost, "cell 3 8,alignx right");
		textFieldTotalCost = UiCommon.setCurrencyField();
		textFieldTotalCost.setEditable(false);
		contentPanel.add(textFieldTotalCost, "cell 3 8,alignx left,aligny center");

		// THIS LISTENER SETS ALL THE COST RELATED TEXT FIELDS
		ActionListener servicePackageComboListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					bodyServicePrice = 0;
					footServicePrice = 0;
					servicePackageID = 0;
					boolean isPackageChanged;
					;

					// GET SERVICE SUBSTRINGS
					String bodyServiceSelection = String.valueOf(bodyServiceComboBox.getSelectedItem());
					bodyServiceProductCodeSubstring = bodyServiceSelection.substring(0, 3);// set Service Object variable
					String footServiceSelection = String.valueOf(footServiceComboBox.getSelectedItem());
					footServiceProductCodeSubstring = footServiceSelection.substring(0, 3);// set Service Object variable

					// PRICE EVERYTHING WITH BODY SERVICE PRODUCT CODE SUBSTRING (since foot is the same price)
					BodyServiceProduct bodySP = productsDao.getServiceProductDetailsByProductCode(bodyServiceProductCodeSubstring);
					BodyServiceProduct footSP = productsDao.getServiceProductDetailsByProductCode(footServiceProductCodeSubstring);

					// GET PACKAGE SELECTION
					String packageSelection = String.valueOf(packageComboBox.getSelectedItem());
					String tempPackageProdCodeSubstring = packageSelection.substring(0, 3);
					if (packageProdCodeSubstring.equalsIgnoreCase(tempPackageProdCodeSubstring)) {
						isPackageChanged = false;
					} else {
						isPackageChanged = true;
					}
					packageProdCodeSubstring = tempPackageProdCodeSubstring;

					// SERVICE IS SELECTED - SET PRICE TO REGULAR PRODUCT PRICE THEN TEST BELOW

					// IF HAS CURRENT BODY PACKAGE
					if (hasCurrentBodyPackage) {

						if (!isPackageChanged) {
							packageComboBox.setSelectedIndex(1); // SET TO FIRST DEFAULT ON INITIAL LOAD
						}

						if (!packageSelection.startsWith("---")) {
							Matcher packageIDMatcher = Pattern.compile("#(\\d+)").matcher(packageSelection);
							packageIDMatcher.find();
							servicePackageID = Long.valueOf(packageIDMatcher.group(1));
							// MAKE SURE QTY DOES NOT EXCEED REMAINING
							BodyPackage pack = packagedDao.getPackagedByPackagedId(servicePackageID);
							double remaining = packagedDao.getPackagedRemaining(pack);
							if (comboQuantity > remaining) {
								MessagePopUp popUp = new MessagePopUp(frame,
										"combo quantity is larger than the selected package. Please select different package or sell services seperately",
										false);
								popUp.setVisible(true);
							} else {
								BodyPackageProduct pp = productsDao.getPackageProductDetailsByProductCode(packageProdCodeSubstring);
								bodyServicePrice = pp.getPricePerPackageService();
								footServicePrice = pp.getPricePerPackageService();
							}

						} else {
							bodyServicePrice = bodySP.getProductPrice();
							footServicePrice = footSP.getProductPrice();
						}

						// DOES NOT HAVE A PACKAGE
					} else {
						packageComboBox.setSelectedIndex(0);
						bodyServicePrice = bodySP.getProductPrice();
						footServicePrice = footSP.getProductPrice();
					}

					// TOTAL PRICE
					CurrencyCalculator bodyPrice = new CurrencyCalculator(Double.toString(bodyServicePrice));
					String bodyPriceString = bodyPrice.bigDecimalToString(bodyPrice.getAmountOne());
					textFieldBodyPrice.setText(bodyPriceString);

					CurrencyCalculator footPrice = new CurrencyCalculator(Double.toString(footServicePrice));
					String footPriceString = footPrice.bigDecimalToString(footPrice.getAmountOne());
					textFieldFootPrice.setText(footPriceString);

					// BODY SUBTOTAL (PRICE * QTY)
					CurrencyCalculator calculateBodySubTotal = new CurrencyCalculator(Double.toString(bodyServicePrice), Double.toString(bodyQty));
					String bodyST = calculateBodySubTotal.getProduct();
					CurrencyCalculator calculateFootSubTotal = new CurrencyCalculator(Double.toString(footServicePrice), Double.toString(footQty));
					String footST = calculateFootSubTotal.getProduct();
					CurrencyCalculator addSubtotal = new CurrencyCalculator(bodyST, footST);
					String subTotal = addSubtotal.getSum();
					textFieldSubTotal.setText(subTotal);
					bodySubtotal = Double.parseDouble(bodyST);
					footSubtotal = Double.parseDouble(footST);

					// GST
					String GSTConstant = Double.toString(productsDao.getProductDetails().getGST());
					CurrencyCalculator calculateBodyGST = new CurrencyCalculator(bodyST, GSTConstant);
					String bodygst = calculateBodyGST.getProduct();
					bodyGST = Double.parseDouble(bodygst);

					CurrencyCalculator calculateFootGST = new CurrencyCalculator(footST, GSTConstant);
					String footgst = calculateFootGST.getProduct();
					footGST = Double.parseDouble(footgst);

					CurrencyCalculator addGST = new CurrencyCalculator(bodygst, footgst);
					String gst = addGST.getSum();
					textFieldGST.setText(gst);

					// TOTAL COST
					CurrencyCalculator calculateTotalCost = new CurrencyCalculator(subTotal, Double.toString(bodyGST + footGST));
					String cost = calculateTotalCost.getSum();
					textFieldTotalCost.setText(cost);
					// totalCost = Double.parseDouble(cost);

					CurrencyCalculator calculateBodyTotalCost = new CurrencyCalculator(bodyST, bodygst);
					String bodyCost = calculateBodyTotalCost.getSum();
					bodyTotalCost = Double.parseDouble(bodyCost);

					CurrencyCalculator calculateFootTotalCost = new CurrencyCalculator(footST, footgst);
					String footCost = calculateFootTotalCost.getSum();
					footTotalCost = Double.parseDouble(footCost);

				} catch (ApplicationException e) {
					LOG.error("ERROR in sellServiceComboListener()" + e);
				}
			}
		};

		// Set Listeners
		packageComboBox.addActionListener(servicePackageComboListener);
		textFieldBodyPrice.addActionListener(servicePackageComboListener);
		textFieldFootPrice.addActionListener(servicePackageComboListener);
		qtyBodyComboBox.addActionListener(servicePackageComboListener);
		qtyFootComboBox.addActionListener(servicePackageComboListener);
		qtyBodyComboBox.setSelectedIndex(0);
		qtyFootComboBox.setSelectedIndex(0);

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

					} else if (String.valueOf(bodyStaffComboBox.getSelectedItem()).startsWith("---")) {
						MessagePopUp msg = new MessagePopUp(frame, "Oops! You have not selected the 'body' staff.", false);
						msg.setVisible(true);
					} else if (String.valueOf(footStaffComboBox.getSelectedItem()).startsWith("---")) {
						MessagePopUp msg = new MessagePopUp(frame, "Oops! You have not selected the 'foot' staff.", false);
						msg.setVisible(true);
					} else {

						// @param purchaseDate
						LocalDate dateOfService = LocalDate.parse(textFieldDate.getText(), UiCommon.DATE_FORMAT_UI);
						String serviceDate = dateOfService.format(UiCommon.DATETIME_FORMAT_MMddyyyy);

						// @param productCode (serviceProductCode is assigned in package/server listener)

						// @param productName
						try {
							bodyServiceProductName = productsDao.getServiceProductDetailsByProductCode(bodyServiceProductCodeSubstring)
									.getProductName();
							footServiceProductName = productsDao.getServiceProductDetailsByProductCode(footServiceProductCodeSubstring)
									.getProductName();
						} catch (ApplicationException e1) {

						}

						// @param customerID
						long customerID = customer.getID();

						// @param quantity

						// @param totalCost
						// class variable totalCost

						// @param staffID
						// set service object variable

						String bodyStaff = String.valueOf(bodyStaffComboBox.getSelectedItem());
						Matcher bodyStaffMatcher = Pattern.compile("(\\d+)").matcher(bodyStaff);
						bodyStaffMatcher.find();
						int i = Integer.valueOf(bodyStaffMatcher.group(1));
						long bodyServiceStaffID = i;

						String footStaff = String.valueOf(footStaffComboBox.getSelectedItem());
						Matcher footStaffMatcher = Pattern.compile("(\\d+)").matcher(footStaff);
						footStaffMatcher.find();
						i = Integer.valueOf(footStaffMatcher.group(1));
						long footServiceStaffID = i;

						// @param packageID

						BodyService bodyService = new BodyService(serviceDate, bodyServiceProductCodeSubstring, bodyServiceProductName,
								bodyServicePrice, bodyGST, customerID, bodyServiceStaffID, bodyQty, bodySubtotal, bodyTotalCost, servicePackageID);
						BodyService footService = new BodyService(serviceDate, footServiceProductCodeSubstring, footServiceProductName,
								footServicePrice, footGST, customerID, footServiceStaffID, footQty, footSubtotal, footTotalCost, servicePackageID);
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

						// GET CONFIRMATIONS FROM USER AND PROCESS THE SERVICE ORDE

						ActionRequiredPopUp popUp = new ActionRequiredPopUp(frame,
								"Please confirm \"" + customer.getFirstName() + "\'s\" combo purchase:\n\u2022 Date: "
										+ dateOfService.format(UiCommon.DATE_FORMAT_UI) + "\n\u2022 Combo: " + comboQuantity + "\n\u2022 "
										+ bodyServiceProductName + " | Staff: " + bodyStaff + "\n\u2022 " + footServiceProductName + " | Staff: "
										+ footStaff + "\n\u2022 Package:  " + packageMessage,
								"Yes. This is correct.", "No, I need to change something.");
						popUp.setVisible(true);

						if (popUp.getActionValue()) {
							try {
								serviceDao.addService(bodyService);
								serviceDao.addService(footService);
								MainFrame.updateMainTextFields(customerDao.getCustomerById(customer.getID()));
								SellComboDialog.this.dispose();
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
					SellComboDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		JLabel lblNewLabel_1 = new JLabel("            ");
		buttonPane.add(lblNewLabel_1);
	}
}
