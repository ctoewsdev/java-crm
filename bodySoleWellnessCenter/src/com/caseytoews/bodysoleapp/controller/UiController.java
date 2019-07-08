/**
 * Project: Body & Sole Wellness Center
 * File: UiController.java
 * Date: Dec 1, 2018
 * Time: 9:43:21 AM
 */

package com.caseytoews.bodysoleapp.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.database.people.GroupDao;
import com.caseytoews.bodysoleapp.database.people.StaffDao;
import com.caseytoews.bodysoleapp.database.product.ProductDetailsDao;
import com.caseytoews.bodysoleapp.database.sales.BodyPackageDao;
import com.caseytoews.bodysoleapp.database.sales.BodyServiceDao;
import com.caseytoews.bodysoleapp.dialogviews.bodypackage.EditBodyPackageDialog;
import com.caseytoews.bodysoleapp.dialogviews.bodypackage.SellBodyPackageDialog;
import com.caseytoews.bodysoleapp.dialogviews.bodyservice.EditBodyServiceDialog;
import com.caseytoews.bodysoleapp.dialogviews.bodyservice.SellBodyServiceDialog;
import com.caseytoews.bodysoleapp.dialogviews.bodyservice.SellComboDialog;
import com.caseytoews.bodysoleapp.dialogviews.common.ControllerErrorMessageDialog;
import com.caseytoews.bodysoleapp.dialogviews.common.MessagePopUp;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.customer.AddNewCustomerDialog;
import com.caseytoews.bodysoleapp.dialogviews.customer.EditCustomerDialog;
import com.caseytoews.bodysoleapp.dialogviews.customer.GetCustomerByListDialog;
import com.caseytoews.bodysoleapp.dialogviews.customer.GetCustomerByPhoneDialog;
import com.caseytoews.bodysoleapp.dialogviews.customer.GetGroupNewMemberByPhoneDialog;
import com.caseytoews.bodysoleapp.dialogviews.customer.InvoiceCustomerDialog;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.dialogviews.pricing.EditGSTDialog;
import com.caseytoews.bodysoleapp.dialogviews.pricing.GetProductByListDialog;
import com.caseytoews.bodysoleapp.dialogviews.pricing.ViewProductsListDialog;
import com.caseytoews.bodysoleapp.dialogviews.reports.ReportPackageSalesDialog;
import com.caseytoews.bodysoleapp.dialogviews.reports.ReportServicePackageSalesDialog;
import com.caseytoews.bodysoleapp.dialogviews.reports.ReportServiceSalesDialog;
import com.caseytoews.bodysoleapp.dialogviews.staff.AddNewStaffDialog;
import com.caseytoews.bodysoleapp.dialogviews.staff.GetOneStaffByListDialog;
import com.caseytoews.bodysoleapp.dialogviews.staff.ViewStaffListDialog;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.io.FilesReader;
import com.caseytoews.bodysoleapp.utility.email.Emailer;
import com.caseytoews.bodysoleapp.utility.email.EmailerImpl;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

public class UiController {

	private static final Logger LOG = LogManager.getLogger();
	private static MainFrame mainFrame;
	private static Customer customer;
	private static CustomerDao customerDao;
	private static BodyServiceDao serviceDao;
	private static BodyPackageDao packagedDao;
	private static ProductDetailsDao productsDao;
	private static GroupDao groupDao;
	private static StaffDao staffDao;
	private static FilesReader filesReader;

	public UiController(MainFrame mainFrame) {
		UiController.mainFrame = mainFrame;
		customer = MainFrame.getMainFrameCustomer();
		customerDao = CustomerDao.getTheInstance();
		serviceDao = BodyServiceDao.getTheInstance();
		packagedDao = BodyPackageDao.getTheInstance();
		productsDao = ProductDetailsDao.getTheInstance();
		staffDao = StaffDao.getTheInstance();
		groupDao = GroupDao.getTheInstance();
		filesReader = FilesReader.getTheInstance();
	}

	/**
	 * Passes error message to Class ErrorMessageDialogn
	 */
	public static void handle(Exception e) {
		String message = "UI Controller Exception: \n";
		new ControllerErrorMessageDialog(mainFrame, message, e);
	}

	/**
	 * handle window closing event
	 */
	public static class MainFrameWindowEventHandler extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			try {
				LOG.debug("Closing Application: " + UiCommon.dateNow());
			} catch (Exception e1) {
				LOG.error("UiController() MainFrameWindowEevntHandler() \"windowClosing\" exception:" + e1);
				UiController.handle(e1);
			}
			exit(0);
		}
	}

	/**
	 * Method exit() invoked to initiate system exit.
	 */
	public static void exit(int exitCode) {
		System.exit(exitCode);
	}

	// ***************************
	// GROUP ITEM HANDLERS
	// **************************
	public static class AddGroupMemberItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				long walkInID = customerDao.getWalkInCustomerID();
				if (customer.getID() == walkInID) {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "A Walk-In customer cannot form a group. First make them a new customer.",
							false);
					popUp.setVisible(true);
				} else if (customer.getID() != 0) {
					GetGroupNewMemberByPhoneDialog dialog = new GetGroupNewMemberByPhoneDialog(mainFrame, customerDao, groupDao);
					dialog.setVisible(true);
				} else {
					MessagePopUp popUp = new MessagePopUp(mainFrame,
							"Please first get a customer on the main page before adding to the group. Thank you.", false);
					popUp.setVisible(true);
				}
			} catch (Exception e1) {
				LOG.error("UiController () AddGroupMemberItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	// ***************************
	// CUSTOMER MENU ITEM HANDLERS
	// ***************************
	public static class GetWalkInCustomerItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {

				long walkInID = customerDao.getWalkInCustomerID();

				if (walkInID > 0) {
					MainFrame.updateMainTextFields(customerDao.getCustomerById(walkInID));

				} else {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "There is no Walk-In Customer set. Please create one", false);
					popUp.setVisible(true);
				}

			} catch (Exception e1) {
				LOG.error("UiController () GetWalkInCustomerItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class GetCustomerByPhoneItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				GetCustomerByPhoneDialog dialog = new GetCustomerByPhoneDialog(mainFrame, customerDao);
				dialog.setVisible(true);
			} catch (Exception e1) {
				LOG.error("UiController () GetCustomerByPhoneItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class GetCustomerByListItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				GetCustomerByListDialog dialog = new GetCustomerByListDialog(mainFrame, customerDao, filesReader);
				dialog.setVisible(true);
			} catch (Exception e1) {
				LOG.error("UiController () GetCustomerByListItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class AddCustomerItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				AddNewCustomerDialog dialog = new AddNewCustomerDialog(mainFrame, customerDao);
				dialog.setVisible(true);
			} catch (Exception e1) {
				LOG.error("UiController () AddCustomerItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class InvoiceCustomerItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (customer.getID() == 0) {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "Please first get a customer on the main page. Thank you.", false);
					popUp.setVisible(true);

				} else if (customer.getEmail().length() == 0) {
					MessagePopUp popUp = new MessagePopUp(mainFrame,
							"Customer must have an email address for invoicing. Please first edit the customer. Thank you.", false);
					popUp.setVisible(true);
				} else {
					InvoiceCustomerDialog dialog = new InvoiceCustomerDialog(mainFrame);
					dialog.setVisible(true);
				}
			} catch (Exception e1) {
				LOG.error("UiController() InvoiceCustomerItemHandler" + e1);
				e1.printStackTrace();
				UiController.handle(e1);
			}
		}
	}

	public static class EditCustomerItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (customer.getID() != 0) {
					EditCustomerDialog dialog = new EditCustomerDialog(mainFrame, customerDao);
					dialog.setVisible(true);
				} else {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "Please first get a customer on the main page. Thank you.", false);
					popUp.setVisible(true);
				}

			} catch (Exception e1) {
				LOG.error("UiController() EditCustomerItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	// ***************************
	// SERVICE MENU ITEM HANDLERS
	// *****************************
	public static class SellServiceItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (customer.getID() != 0) {
					SellBodyServiceDialog dialog = new SellBodyServiceDialog(mainFrame, customerDao, serviceDao, packagedDao, productsDao, staffDao,
							groupDao);
					dialog.setVisible(true);
				} else {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "Please first get a customer on the main page. Thank you.", false);
					popUp.setVisible(true);
				}
			} catch (Exception e1) {
				LOG.error("UiController() SellServiceItemHandler" + e1);
				e1.printStackTrace();
				UiController.handle(e1);
			}
		}
	}

	public static class SellCombo1_5ItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (customer.getID() != 0) {
					SellComboDialog dialog = new SellComboDialog(1.5, mainFrame, customerDao, serviceDao, packagedDao, productsDao, staffDao,
							groupDao);
					dialog.setVisible(true);
				} else {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "Please first get a customer on the main page. Thank you.", false);
					popUp.setVisible(true);
				}
			} catch (Exception e1) {
				LOG.error("UiController() SellCombo1_5ItemHandler" + e1);
				e1.printStackTrace();
				UiController.handle(e1);
			}
		}
	}

	public static class SellCombo2_0ItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (customer.getID() != 0) {
					SellComboDialog dialog = new SellComboDialog(2.0, mainFrame, customerDao, serviceDao, packagedDao, productsDao, staffDao,
							groupDao);
					dialog.setVisible(true);
				} else {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "Please first get a customer on the main page. Thank you.", false);
					popUp.setVisible(true);
				}
			} catch (Exception e1) {
				LOG.error("UiController() SellCombo1_5ItemHandler" + e1);
				e1.printStackTrace();
				UiController.handle(e1);
			}
		}
	}

	// ***************************
	// PACKAGE MENU ITEM HANDLERS
	// *****************************
	public static class SellPackageItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				long walkInID = customerDao.getWalkInCustomerID();
				if (customer.getID() == walkInID) {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "A Walk-In customer cannot purchase a package. First make them a new customer.",
							false);
					popUp.setVisible(true);
				} else if (customer.getID() != 0) {
					SellBodyPackageDialog dialog = new SellBodyPackageDialog(mainFrame, customerDao, serviceDao, packagedDao, productsDao, staffDao);
					dialog.setVisible(true);
				} else {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "Please first get a customer on the main page. Thank you.", false);
					popUp.setVisible(true);
				}
			} catch (Exception e1) {
				LOG.error("UiController() SellPackageItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	// ***************************
	// STAFF MENU ITEM HANDLERS
	// *****************************
	public static class AddStaffItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				AddNewStaffDialog dialog = new AddNewStaffDialog(mainFrame, staffDao);
				dialog.setVisible(true);
			} catch (Exception e1) {
				LOG.error("UiController() AddStaffItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class EditStaffItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				GetOneStaffByListDialog dialog = new GetOneStaffByListDialog(mainFrame, staffDao);
				dialog.setVisible(true);

			} catch (Exception e1) {
				LOG.error("UiController() EditStaffItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class ViewStaffItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				ViewStaffListDialog dialog = new ViewStaffListDialog(mainFrame, staffDao);
				dialog.setVisible(true);

			} catch (Exception e1) {
				LOG.error("UiControlle () ViewStaffItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	// ***************************
	// REPORTS MENU ITEM HANDLERS
	// *****************************
	public static class ReportServiceItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				ReportServiceSalesDialog dialog = new ReportServiceSalesDialog(mainFrame, filesReader);
				dialog.setVisible(true);

			} catch (Exception e1) {
				LOG.error("UiControlle () ReportServiceItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class ReportPackageServiceItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				ReportServicePackageSalesDialog dialog = new ReportServicePackageSalesDialog(mainFrame, filesReader);
				dialog.setVisible(true);
			} catch (Exception e1) {
				LOG.error("UiController() ReportPackageServiceItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class ReportPackageItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				ReportPackageSalesDialog dialog = new ReportPackageSalesDialog(mainFrame, filesReader);
				dialog.setVisible(true);
			} catch (Exception e1) {
				LOG.error("UiController() ReportPackageItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class CloseShopItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				LOG.debug("Closing Shop.");
				LocalDate now = LocalDate.now();
				String today = now.format(UiCommon.DATE_FORMAT_UI);

				String mmddyyyyString = now.format(UiCommon.DATETIME_FORMAT_MMddyyyy);

				EmailerImpl backUps = new EmailerImpl(Emailer.MANAGER, "Daily Backups for " + today, "Daily Backups for " + today);
				backUps.setIsBackUp();
				backUps.sendEmail();

				EmailerImpl caseyLog = new EmailerImpl(Emailer.IT, "Daily Error Log: " + today, "Daily Error Log: " + today);
				caseyLog.setIsErrorLog();
				caseyLog.sendEmail();

				// Get daily SERVICES WITH NO PACKAGE
				List<BodyService> dailyServices = serviceDao.getServiceByDate(mmddyyyyString);
				double serviceSales = 0.0;
				double serviceQty = 0;
				double serviceGST = 0.0;
				for (BodyService service : dailyServices) {
					if (service.getPackageID() == 0) {
						serviceSales += service.getTotalCost();
						serviceQty += service.getQuantity();
						serviceGST += service.getTotalGST();
					}
				}
				StringBuilder salesReport = new StringBuilder("DAILY SALES REPORT FOR - " + today + "\n\n");
				salesReport.append("Quantity of Services (no package): " + UiCommon.TWO_DECIMAL_FORMAT.format(serviceQty) + "\n");
				salesReport.append("Total Service Sales (no package): $" + UiCommon.TWO_DECIMAL_FORMAT.format(serviceSales) + "\n");
				salesReport.append("Total Service GST (no package): $" + UiCommon.TWO_DECIMAL_FORMAT.format(serviceGST) + "\n\n");

				// Get daily SERVICES WITH PACKAGE
				double serviceSalesP = 0.0;
				double serviceQtyP = 0;
				double serviceGSTP = 0.0;
				for (BodyService service : dailyServices) {
					if (service.getPackageID() != 0) {
						serviceSalesP += service.getTotalCost();
						serviceQtyP += service.getQuantity();
						serviceGSTP += service.getTotalGST();
					}
				}
				salesReport.append("Quantity of Services (with package): " + UiCommon.TWO_DECIMAL_FORMAT.format(serviceQtyP) + "\n");
				salesReport.append("Total Service Sales Value (with package): [$" + UiCommon.TWO_DECIMAL_FORMAT.format(serviceSalesP) + "]\n");
				salesReport.append("Total Service GST Value (with package): [$" + UiCommon.TWO_DECIMAL_FORMAT.format(serviceGSTP) + "]\n\n");

				// Get daily packages
				List<BodyPackage> dailyPackages = packagedDao.getPackagesByDate(mmddyyyyString);
				double packageSales = 0.0;
				int packageQty = 0;
				double packageGST = 0.0;
				for (BodyPackage pack : dailyPackages) {
					LOG.info(pack.getTotalCost());
					packageSales += pack.getTotalCost();
					packageQty++;
					packageGST += pack.getTotalGST();
				}
				salesReport.append("Quantity of Packages: " + UiCommon.TWO_DECIMAL_FORMAT.format(packageQty) + "\n");
				salesReport.append("Total Package Sales: $" + UiCommon.TWO_DECIMAL_FORMAT.format(packageSales) + "\n");
				salesReport.append("Total Package GST: $" + UiCommon.TWO_DECIMAL_FORMAT.format(packageGST) + "\n\n");

				EmailerImpl dailySalesManager = new EmailerImpl(Emailer.MANAGER, "Daily Sales Report - " + today, salesReport.toString());
				dailySalesManager.sendEmail();

				// Get daily new customers

			} catch (ApplicationException e) {
				LOG.error("UiController () CloseShopItemHandler exception:" + e);
			}

			exit(0);
		}
	}

	// ***************************
	// PRICING MENU ITEM HANDLERS
	// *****************************
	public static class ViewPricingItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				ViewProductsListDialog dialog = new ViewProductsListDialog(mainFrame, productsDao);
				dialog.setVisible(true);
			} catch (Exception e1) {
				LOG.error("UiController() ViewPricingItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class EditPricingItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				GetProductByListDialog dialog = new GetProductByListDialog(mainFrame, productsDao);
				dialog.setVisible(true);
			} catch (Exception e1) {
				LOG.error("UiController() EditPricingItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	public static class EditGSTItemHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				EditGSTDialog dialog = new EditGSTDialog(mainFrame, productsDao);
				dialog.setVisible(true);
			} catch (Exception e1) {
				LOG.error("UiControlle () EditGSTItemHandler exception:" + e1);
				UiController.handle(e1);
			}
		}
	}

	// ***************************
	// SERVICES TABLE ITEM HANDLERS
	// *****************************
	public static class EditServiceItemHandler {

		public EditServiceItemHandler(long serviceID) {

			try {
				if (customer.getID() != 0) {
					EditBodyServiceDialog dialog = new EditBodyServiceDialog(serviceID, mainFrame, customerDao, serviceDao, packagedDao, productsDao,
							staffDao, groupDao);
					dialog.setVisible(true);
				} else {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "Please first get a customer on the main page. Thank you.", false);
					popUp.setVisible(true);
				}
			} catch (Exception e1) {
				LOG.error("UiController() EditServiceItemHandler" + e1);
				e1.printStackTrace();
				UiController.handle(e1);
			}
		}
	}

	// ***************************
	// PACKAGES TABLE ITEM HANDLERS
	// *****************************
	public static class EditBodyPackageItemHandler {

		public EditBodyPackageItemHandler(long packagedID) {

			try {
				if (customer.getID() != 0) {

					// Test if bodypackage is editable
					BodyPackage originalBodyPackage = packagedDao.getPackagedByPackagedId(packagedID);
					String packCode = originalBodyPackage.getProductCode();

					if ((packCode.equalsIgnoreCase("B12") && (packagedDao.getPackagedRemaining(originalBodyPackage) < 1))
							|| (packCode.equalsIgnoreCase("A11") && (packagedDao.getPackagedRemaining(originalBodyPackage) < 6))) {

						JOptionPane.showMessageDialog(null, "This package cannot be deleted or edited. Contact Administrator for assistance.",
								"dialog", JOptionPane.ERROR_MESSAGE);
					} else {

						EditBodyPackageDialog dialog = new EditBodyPackageDialog(packagedID, mainFrame, customerDao, serviceDao, packagedDao,
								productsDao, staffDao);
						dialog.setVisible(true);
					}
				} else {
					MessagePopUp popUp = new MessagePopUp(mainFrame, "Please first get a customer on the main page. Thank you.", false);
					popUp.setVisible(true);
				}
			} catch (Exception e1) {
				LOG.error("UiController() EditBodyPackageItemHandler" + e1);
				e1.printStackTrace();
				UiController.handle(e1);
			}
		}
	}
}