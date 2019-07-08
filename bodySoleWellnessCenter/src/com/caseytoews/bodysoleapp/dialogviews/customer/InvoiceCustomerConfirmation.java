/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 30, 2018
 * Time: 8:56:45 PM
 */

package com.caseytoews.bodysoleapp.dialogviews.customer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.database.people.GroupDao;
import com.caseytoews.bodysoleapp.database.people.StaffDao;
import com.caseytoews.bodysoleapp.database.sales.BodyPackageDao;
import com.caseytoews.bodysoleapp.database.sales.BodyServiceDao;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.domain.business.CustomerInvoice;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.people.Group;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.utility.comparator.CompareByPurchaseDateDesc;
import com.caseytoews.bodysoleapp.utility.email.EmailerImpl;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

import net.miginfocom.swing.MigLayout;

public class InvoiceCustomerConfirmation extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private final JPanel contentPanel = new JPanel();
	private Customer customer;
	private String serviceDateToInvoice;
	private Staff staff;
	private CustomerDao customerDaoInstance;
	private BodyPackageDao packagedDaoInstance;
	private BodyServiceDao serviceDaoInstance;
	private StaffDao staffDaoInstance;
	private GroupDao groupDaoInstance;
	private String invoiceBody;
	private CustomerInvoice customerInvoice;
	private EmailerImpl emailer;
	ArrayList<String> packageOwnersEmail = new ArrayList<>();

	public InvoiceCustomerConfirmation(JFrame frame, String serviceDate) throws ApplicationException {
		super(frame, true);
		serviceDateToInvoice = serviceDate;
		customerDaoInstance = CustomerDao.getTheInstance();
		packagedDaoInstance = BodyPackageDao.getTheInstance();
		serviceDaoInstance = BodyServiceDao.getTheInstance();
		staffDaoInstance = StaffDao.getTheInstance();
		groupDaoInstance = GroupDao.getTheInstance();
		customer = MainFrame.getMainFrameCustomer();
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 890, 700);
		setLocationRelativeTo(frame);
		setTitle("Message");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(UiCommon.PURPLE_MAIN);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("fill", "[120][600]", "[]"));

		JLabel lblNewLabelIcon = new JLabel();
		lblNewLabelIcon.setIcon(UiCommon.SMILEY_FACE);
		contentPanel.add(lblNewLabelIcon, "cell 0 0,alignx center,aligny center");

		String messageBody = createInvoiceBody();

		JTextArea txtrBody = new JTextArea(messageBody);
		txtrBody.setEditable(false);
		txtrBody.setLineWrap(true);
		txtrBody.setWrapStyleWord(true);
		txtrBody.setBackground(UiCommon.GREEN_MAIN);
		txtrBody.setFont(new Font(UiCommon.UI_FONT, Font.PLAIN, 16));
		txtrBody.setBorder(null);

		JScrollPane scroll = new JScrollPane(txtrBody);
		scroll.setBorder(null);

		JPanel panel = new JPanel();
		panel.setBackground(UiCommon.GREEN_MAIN);
		panel.setLayout(new MigLayout("fill", "[]", "[]"));
		panel.add(scroll, "cell 0 0,growx,alignx center");
		panel.setBorder(BorderFactory.createLineBorder(UiCommon.WHITE_MAIN));
		contentPanel.add(panel, "cell 1 0,growx,alignx center,aligny center");

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(UiCommon.PURPLE_MAIN);
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = UiCommon.setOKButton("This is correct. Send Email!");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				customerInvoice = new CustomerInvoice(customer.getID(), invoiceBody);

				try {
					customerDaoInstance.addCustomerInvoice(customerInvoice);
					emailer = new EmailerImpl(customer.getEmail(), "Service Invoice", invoiceBody);
					emailer.setIsInvoice();

					// CUSTOMER BELONGS TO A GROUP
					if (packageOwnersEmail.size() > 0) {
						emailer.setPackageOwnersCC(packageOwnersEmail);
					}
					emailer.sendEmail();
				} catch (ApplicationException e1) {
					LOG.debug("InvoiceCustomerDialog OK button error - INVOICE:\n" + invoiceBody);
				}

				InvoiceCustomerConfirmation.this.dispose();
			}
		});

		buttonPane.add(okButton);

		JButton cancelButton = UiCommon.setCancelButton(" Cancel ");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				InvoiceCustomerConfirmation.this.dispose();
			}
		});

		buttonPane.add(cancelButton);

	}

	private String createInvoiceBody() throws ApplicationException {

		boolean isPackagedService = false; // do any of today's services use a package
		ArrayList<BodyService> todaysServices = new ArrayList<>();
		ArrayList<Long> todaysPackageIds = new ArrayList<>();
		ArrayList<Long> packageIDsForInvoice = new ArrayList<>();
		ArrayList<BodyPackage> packagesForInvoice = new ArrayList<>();

		StringBuilder body = new StringBuilder(customer.getFirstName() + ",\n\nWe hope you enjoyed your experience with us\n");
		body.append("and look forward to seeing you again soon.\n\n");
		body.append("To schedule your next appointment:\n");
		body.append(" \t\u2022 Book Online: bodysolewellness.ca\n");
		body.append(" \t\u2022 By Text Message: 604-760-3933\n");
		body.append(" \t\u2022 By Phone: 604-427-2988\n\n");

		LocalDate serviceDate = LocalDate.parse(serviceDateToInvoice, UiCommon.DATETIME_FORMAT_MMddyyyy);
		body.append("--BODY & SOLE WELLNESS CENTER INVOICE--\n");
		body.append("\u2022 Date: " + serviceDate.format(UiCommon.DATE_FORMAT_UI) + "\n");
		body.append("\u2022 Customer: " + customer.getFirstName() + " " + customer.getLastName() + "\n");
		body.append("\u2022 Phone: " + customer.getPhone() + "\n");
		body.append("\u2022 Email: " + customer.getEmail() + "\n\n");

		// GET ALL SERVICE ON THE SERVICE DATE
		ArrayList<BodyService> allCustomerServices = serviceDaoInstance.getServicesByCustomerID(customer.getID());
		for (BodyService service : allCustomerServices) {
			if (service.getPurchaseDate().equalsIgnoreCase(serviceDateToInvoice)) {
				todaysServices.add(service);
				if (service.getPackageID() > 0) {
					isPackagedService = true; // yes a package was used
				}
			}
		}

		// LOOP AND BUILD INVOICE STRING
		body.append("--SERVICE SUMMARY--\n");
		for (BodyService ts : todaysServices) {
			staff = staffDaoInstance.getStaffByID(ts.getStaffID());

			body.append("\u2022 Service: " + ts.getProductName() + "  |  ");
			body.append("\u2022 ID: " + ts.getPurchaseID() + "\n");
			body.append("\u2022 Package No: " + (ts.getPackageID() == 0 ? "N/A" : "#" + String.valueOf(ts.getPackageID())) + "  |  ");
			body.append("\u2022 Staff: " + staff.getFirstName() + " " + staff.getLastName().substring(0, 1) + ".\n");
			String totalGST = UiCommon.TWO_DECIMAL_FORMAT.format(ts.getTotalGST());
			String subTotal = UiCommon.TWO_DECIMAL_FORMAT.format(ts.getSubtotal());
			String totalPrice = UiCommon.TWO_DECIMAL_FORMAT.format(ts.getTotalCost());
			body.append("\u2022 Qty: " + ts.getQuantity() + "  |  \u2022 Subtotal: " + subTotal + "\n");
			body.append("\u2022 GST: " + totalGST + "  |  \u2022 Total Price: " + totalPrice + "\n\n");

		}

		// PACKAGES SUMMARY
		// 1. add packages used in todays services because they might be zero now
		if (isPackagedService) {
			ArrayList<Long> sumPackageIDs = new ArrayList<>();
			for (BodyService ts : todaysServices) {
				if (ts.getPackageID() > 0) {
					todaysPackageIds.add(ts.getPackageID());
					packageIDsForInvoice.add(ts.getPackageID());
				}
			}
		}

		// 2. add customer's current packages
		if (packagedDaoInstance.getPackagesByCustomerID(customer.getID()) != null) {
			ArrayList<BodyPackage> thisCustAllPackages = packagedDaoInstance.getPackagesByCustomerID(customer.getID());

			for (BodyPackage pack : thisCustAllPackages) {
				packageIDsForInvoice.add(pack.getPurchaseID());
			}
		}

		// 3. add customer's group packages

		if (!groupDaoInstance.validateGroupMember(customer.getID())) {
			Group group = groupDaoInstance.getGroupByCustomerId(customer.getID());

			if (groupDaoInstance.getGroupPackagesByGroup(group) != null) {
				ArrayList<BodyPackage> groupPackages = groupDaoInstance.getGroupPackagesByGroup(group);

				for (BodyPackage pack : groupPackages) {
					packageIDsForInvoice.add(pack.getPurchaseID());
				}
			}
		}

		// 4. loop through the summary packages and append them

		// convert list to hashset to remove duplicates and then back to list
		Set<Long> set = new LinkedHashSet<>();
		set.addAll(packageIDsForInvoice);
		packageIDsForInvoice.clear();
		packageIDsForInvoice.addAll(set);

		// add packages
		for (long id : packageIDsForInvoice) {
			System.out.println("pack id: " + id);
			BodyPackage pack = packagedDaoInstance.getPackagedByPackagedId(id);
			if (packagedDaoInstance.getPackagedRemaining(pack) == 0) {

				for (long pid : todaysPackageIds) {
					if (pack.getPurchaseID() == pid) {
						packagesForInvoice.add(pack);
						// 4. add owner to packageOwnersEmail array if it is not the customer
						if (pack.getCustomerID() != customer.getID()) {
							Customer c = customerDaoInstance.getCustomerById(pack.getCustomerID());
							packageOwnersEmail.add(c.getEmail());
						}
					}
				}

			} else {
				packagesForInvoice.add(pack);
				// 4. add owner to packageOwnersEmail array if it is not the customer
				if (pack.getCustomerID() != customer.getID()) {
					Customer c = customerDaoInstance.getCustomerById(pack.getCustomerID());
					packageOwnersEmail.add(c.getEmail());
				}
			}
		}

		if (packagesForInvoice.size() > 0) {
			packagesForInvoice.sort(new CompareByPurchaseDateDesc());

			body.append("--CURRENT PACKAGE SUMMARY--\n"); // HEADER
			for (BodyPackage pack : packagesForInvoice) { // loop through todays services and append their packages

				LocalDate purchaseDate = LocalDate.parse(pack.getPurchaseDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
				Customer owner = customerDaoInstance
						.getCustomerById(packagedDaoInstance.getPackagedByPackagedId(pack.getPurchaseID()).getCustomerID());
				body.append("\u2022 Package: " + pack.getProductName());
				body.append("\n\u2022 Purchased: " + purchaseDate.format(UiCommon.DATE_FORMAT_UI) + "  |  \u2022 By: " + owner.getFirstName() + " "
						+ owner.getLastName() + "\n");
				body.append("\u2022 Package No: #" + pack.getPurchaseID() + "  |  \u2022 Remaining: " + packagedDaoInstance.getPackagedRemaining(pack)
						+ "\n\n");

			}
		}
		// CLOSING
		body.append("***Thank you for chosing Body & Sole Wellness Center - Vivi***");

		invoiceBody = body.toString();
		return invoiceBody;

	}
}
