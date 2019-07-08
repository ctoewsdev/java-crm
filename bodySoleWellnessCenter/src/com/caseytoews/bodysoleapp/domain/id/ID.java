/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 22, 2018
 * Time: 12:08:13 PM
 */

package com.caseytoews.bodysoleapp.domain.id;

/**
 * data storage model for all IDs required in the project
 *
 */
public class ID {

	private long customersFileID;
	private long servicesFileID;
	private long packagedFileID;
	private long staffFileID;
	private long pricingFileID;
	private long customerID;
	private long packagedID;
	private long serviceID;
	private long staffID;
	private long customerInvoiceID;
	private long groupID;

	/**
	 * Default Constructor
	 * Required for Jackson instantiation
	 */
	public ID() {
		super();
	}

	public ID(long customersFileID, long servicesFileID, long packagedFileID, long staffFileID, long pricingFileID, long customerID, long packagedID,
			long serviceID, long staffID, long customerInvoiceID, long groupID) {
		super();
		this.customersFileID = customersFileID;
		this.servicesFileID = servicesFileID;
		this.packagedFileID = packagedFileID;
		this.staffFileID = staffFileID;
		this.pricingFileID = pricingFileID;
		this.customerID = customerID;
		this.packagedID = packagedID;
		this.serviceID = serviceID;
		this.staffID = staffID;
		this.customerInvoiceID = customerInvoiceID;
		this.groupID = groupID;
	}

	public long getCustomersFileID() {
		return customersFileID;
	}

	public void setCustomersFileID(long customersFileID) {
		this.customersFileID = customersFileID;
	}

	public long getServicesFileID() {
		return servicesFileID;
	}

	public void setServicesFileID(long servicesFileID) {
		this.servicesFileID = servicesFileID;
	}

	public long getPackagedFileID() {
		return packagedFileID;
	}

	public void setPackagedFileID(long packagedFileID) {
		this.packagedFileID = packagedFileID;
	}

	public long getStaffFileID() {
		return staffFileID;
	}

	public void setStaffFileID(long staffFileID) {
		this.staffFileID = staffFileID;
	}

	public long getPricingFileID() {
		return pricingFileID;
	}

	public void setPricingFileID(long pricingFileID) {
		this.pricingFileID = pricingFileID;
	}

	public long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}

	public long getPackagedID() {
		return packagedID;
	}

	public void setPackagedID(long packagedID) {
		this.packagedID = packagedID;
	}

	public long getServiceID() {
		return serviceID;
	}

	public void setServiceID(long serviceID) {
		this.serviceID = serviceID;
	}

	public long getStaffID() {
		return staffID;
	}

	public void setStaffID(long staffID) {
		this.staffID = staffID;
	}

	public long getCustomerInvoiceID() {
		return customerInvoiceID;
	}

	public void setCustomerInvoiceID(long customerInvoiceID) {
		this.customerInvoiceID = customerInvoiceID;
	}

	public long getGroupID() {
		return groupID;
	}

	public void setGroupID(long groupID) {
		this.groupID = groupID;
	}

	@Override
	public String toString() {
		return "ID [customersFileID=" + customersFileID + ", servicesFileID=" + servicesFileID + ", packagedFileID=" + packagedFileID
				+ ", staffFileID=" + staffFileID + ", pricingFileID=" + pricingFileID + ", customerID=" + customerID + ", packagedID=" + packagedID
				+ ", serviceID=" + serviceID + ", staffID=" + staffID + ", customerInvoiceID=" + customerInvoiceID + ", groupID=" + groupID + "]";
	}
}