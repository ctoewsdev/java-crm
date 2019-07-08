/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 22, 2018
 * Time: 12:08:13 PM
 */

package com.caseytoews.bodysoleapp.domain.business;

public class CustomerInvoice {

	private long customerInvoiceID;
	private long customerId;
	private String invoiceBody;

	/**
	 * Default Constructor
	 * Required for Jackson instantiation
	 */
	public CustomerInvoice() {
		super();
	}

	/**
	 * Overloaded Constructor for No customerInvoiceId
	 */
	public CustomerInvoice(long customerId, String invoiceBody) {
		super();
		this.customerInvoiceID = customerId;
		this.invoiceBody = invoiceBody;
	}

	public long getCustomerInvoiceID() {
		return customerInvoiceID;
	}

	public void setCustomerInvoiceID(long customerInvoiceID) {
		this.customerInvoiceID = customerInvoiceID;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getInvoiceBody() {
		return invoiceBody;
	}

	public void setInvoiceBody(String invoiceBody) {
		this.invoiceBody = invoiceBody;
	}

	@Override
	public String toString() {
		return "CustomerInvoice [customerInvoiceID=" + customerInvoiceID + ", customerId=" + customerId + ", invoiceBody=" + invoiceBody + "]";
	}
}