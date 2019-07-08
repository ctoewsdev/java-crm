/**
 * Project: BodySolePOS
 * Date: Nov 12, 2018
 * Time: 11:11:58 AM
 */

package com.caseytoews.bodysoleapp.domain.sales;

/**
 * A BodyService is consumed either as a single-service or as a packaged-service.
 */

public class BodyService extends SuperSale {
	private double quantity;
	private double subtotal;
	private double totalCost;
	private long packageID;

	/**
	 * Default Constructor for Jackson instantiation
	 */
	public BodyService() {
		super();
	}

	public BodyService(long purchaseID, String purchaseDate, String productCode, String productName, double productPrice, double totalGST,
			long customerID, long staffID, double quantity, double subtotal, double totalCost, long packageID) {
		super(purchaseID, purchaseDate, productCode, productName, productPrice, totalGST, totalCost, customerID, staffID);
		this.quantity = quantity;
		this.subtotal = subtotal;
		this.totalCost = totalCost;
		this.packageID = packageID;
	}

	/**
	 * Overloaded Constructor for instantiating object in SellService dialog
	 */
	public BodyService(String purchaseDate, String productCode, String productName, double productPrice, double totalGST, long customerID,
			long staffID, double quantity, double subtotal, double totalCost, long packageID) {
		super(0, purchaseDate, productCode, productName, productPrice, totalGST, totalCost, customerID, staffID);
		this.quantity = quantity;
		this.subtotal = subtotal;
		this.totalCost = totalCost;
		this.packageID = packageID;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	@Override
	public double getTotalCost() {
		return totalCost;
	}

	@Override
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public long getPackageID() {
		return packageID;
	}

	public void setPackageID(long packageID) {
		this.packageID = packageID;
	}

	@Override
	public String toString() {
		return "Service [quantity=" + quantity + ", totalCost=" + totalCost + ", packageID=" + packageID + "]";
	}
}