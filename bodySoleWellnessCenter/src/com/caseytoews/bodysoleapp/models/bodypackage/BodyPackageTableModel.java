/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 23, 2018
 * Time: 11:26:32 AM
 */

package com.caseytoews.bodysoleapp.models.bodypackage;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.people.CustomerDao;
import com.caseytoews.bodysoleapp.database.people.GroupDao;
import com.caseytoews.bodysoleapp.database.people.StaffDao;
import com.caseytoews.bodysoleapp.database.sales.BodyPackageDao;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.people.Group;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.utility.comparator.CompareByPurchaseDateDesc;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

public class BodyPackageTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static BodyPackageDao packagedDao;
	private static StaffDao staffDao;
	private static CustomerDao customerDao;
	private static GroupDao groupDao;
	private static final Logger LOG = LogManager.getLogger();
	private String[] columnNames = { "ID", "Purchaser", "Date", "Package", "Price", "GST", "Total Cost", "Balance", "Remaining", "Staff" };
	private ArrayList<BodyPackage> packageData;

	public BodyPackageTableModel(Customer customer) throws ApplicationException {
		packagedDao = BodyPackageDao.getTheInstance();
		staffDao = StaffDao.getTheInstance();
		groupDao = GroupDao.getTheInstance();
		customerDao = CustomerDao.getTheInstance();
		if (customer != null) {
			// Get Customers Packages
			packageData = packagedDao.getPackagesByCustomerID(customer.getID());

			// Get Group Members Packages
			if (!groupDao.validateGroupMember(customer.getID())) {
				Group group = groupDao.getGroupByCustomerId(customer.getID());
				ArrayList<Long> memberIDs = group.getMembers();
				for (long mID : memberIDs) {
					if (mID != customer.getID()) {
						ArrayList<BodyPackage> packages = packagedDao.getPackagesByCustomerID(mID);
						for (BodyPackage p : packages) {
							packageData.add(p);
						}
					}
				}
			}
			// Sort Packages
			packageData.sort(new CompareByPurchaseDateDesc());

		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		int size;
		if (packageData == null) {
			size = 10;
		} else if (packageData.size() < 10) {
			size = 10;
		} else {
			size = packageData.size();
		}
		return size;
	}

	// "Package ID", "Purchaser", "Date", "Package", "Price", "GST", "Total Cost", "Balance", "Remaining," "Staff"
	@Override
	public Object getValueAt(int row, int col) {
		Object temp = null;
		double price = 0.0;
		if (packageData != null) {
			if (row < packageData.size()) {
				if (col == 0) {
					temp = packageData.get(row).getPurchaseID();
				} else if (col == 1) {
					try {
						long id = packageData.get(row).getCustomerID();
						Customer customer = customerDao.getCustomerById(id);
						String firstName = customer.getFirstName();
						String lastName = customer.getLastName();
						temp = firstName + " " + lastName.substring(0, 1) + ".";
					} catch (ApplicationException e) {

					}
				} else if (col == 2) {
					LocalDate date = LocalDate.parse(packageData.get(row).getPurchaseDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
					temp = date.format(UiCommon.DATE_FORMAT_UI);
				} else if (col == 3) {
					temp = packageData.get(row).getProductName();
				} else if (col == 4) {
					price = packageData.get(row).getProductPrice();
					temp = UiCommon.TWO_DECIMAL_FORMAT.format(price);
				} else if (col == 5) {
					price = packageData.get(row).getTotalGST();
					temp = UiCommon.TWO_DECIMAL_FORMAT.format(price);
				} else if (col == 6) {
					price = packageData.get(row).getTotalCost();
					temp = UiCommon.TWO_DECIMAL_FORMAT.format(price);
				} else if (col == 7) {
					try {
						price = packagedDao.getPackagedBalance(packageData.get(row));
						temp = UiCommon.TWO_DECIMAL_FORMAT.format(price);
					} catch (ApplicationException e) {
						LOG.error("ERROR in PackagesTableModel() ");

					}
				} else if (col == 8) {
					try {
						temp = packagedDao.getPackagedRemaining(packageData.get(row));
					} catch (ApplicationException e) {
						LOG.error("ERROR in PackagesTableModel() ");
					}
				} else if (col == 9) {
					Staff staff;
					try {
						staff = staffDao.getStaffByID(packageData.get(row).getStaffID());
						temp = staff.getFirstName() + " " + staff.getLastName();
					} catch (ApplicationException e) {
						LOG.error("ERROR in PackagesTableModel() ");

					}
				}
			}
		}
		return temp;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/*
	 * Don't need to implement this method unless data can change.
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		// data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}