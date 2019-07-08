/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 23, 2018
 * Time: 11:26:32 AM
 */

package com.caseytoews.bodysoleapp.models.bodyservice;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.people.StaffDao;
import com.caseytoews.bodysoleapp.database.sales.BodyServiceDao;
import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;
import com.caseytoews.bodysoleapp.utility.comparator.CompareByPurchaseDateDesc;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;

public class BodyServiceTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();
	private static BodyServiceDao serviceDaoInstance;
	private static StaffDao staffDaoInstance;
	private String[] columnNames = { "ID", "Package ID", "Date", "Service", "Price", "Qty", "Subtotal", "GST", "Total Cost", "Staff" };
	private ArrayList<BodyService> serviceData;

	public BodyServiceTableModel(Customer customer) throws ApplicationException {
		serviceDaoInstance = BodyServiceDao.getTheInstance();
		staffDaoInstance = StaffDao.getTheInstance();
		if (customer != null) {
			serviceData = serviceDaoInstance.getServicesByCustomerID(customer.getID());
			serviceData.sort(new CompareByPurchaseDateDesc());
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		int size;
		if (serviceData == null) {
			size = 15;
		} else if (serviceData.size() < 15) {
			size = 15;
		} else {
			size = serviceData.size();
		}
		return size;
	}

	// Service ID", "Package ID", "Date", "Service", "Price", "Qty", "Subtotal," "GST", "Total Cost" "Staff
	@Override
	public Object getValueAt(int row, int col) {
		double price = 0.0;
		double qty = 0.0;
		Object temp = null;
		if (serviceData != null) {
			if (row < serviceData.size()) {
				if (col == 0) {
					temp = serviceData.get(row).getPurchaseID();
				} else if (col == 1) {
					long packageID = serviceData.get(row).getPackageID();
					temp = (packageID != 0) ? packageID : "N/A";

				} else if (col == 2) {
					LocalDate date = LocalDate.parse(serviceData.get(row).getPurchaseDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
					temp = date.format(UiCommon.DATE_FORMAT_UI);
				} else if (col == 3) {
					temp = serviceData.get(row).getProductName();
				} else if (col == 4) {

					price = serviceData.get(row).getProductPrice();
					temp = UiCommon.TWO_DECIMAL_FORMAT.format(price);

				} else if (col == 5) {
					qty = serviceData.get(row).getQuantity();
					temp = qty;
				} else if (col == 6) {
					price = serviceData.get(row).getSubtotal();
					temp = UiCommon.TWO_DECIMAL_FORMAT.format(price);
				} else if (col == 7) {
					price = serviceData.get(row).getTotalGST();
					temp = UiCommon.TWO_DECIMAL_FORMAT.format(price);
				} else if (col == 8) {
					price = serviceData.get(row).getTotalCost();
					temp = UiCommon.TWO_DECIMAL_FORMAT.format(price);
				} else if (col == 9) {
					Staff staff;
					try {
						staff = staffDaoInstance.getStaffByID(serviceData.get(row).getStaffID());
						temp = staff.getFirstName() + " " + staff.getLastName();
					} catch (ApplicationException e) {
						LOG.error("ERROR in ServicesTableModel() ");
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