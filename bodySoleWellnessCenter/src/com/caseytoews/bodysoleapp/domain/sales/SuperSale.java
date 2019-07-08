/**
 * Project: bodySoleWellnessCenter
 * Date: Jan 1, 2019
 * Time: 5:34:15 PM
 */

package com.caseytoews.bodysoleapp.domain.sales;

public class SuperSale {
	private long purchaseID;
	private String purchaseDate;
	private String productCode;
	private String productName;
	private double productPrice;
	private double totalGST;
	private double totalCost;
	private long customerID;
	private long staffID;

	/**
	 * Default Constructor for Jackson instantiation
	 */
	public SuperSale() {
		super();
	}

	public SuperSale(long purchaseID, String purchaseDate, String productCode, String productName, double productPrice, double totalGST,
			double totalCost, long customerID, long staffID) {
		super();
		this.purchaseID = purchaseID;
		this.purchaseDate = purchaseDate;
		this.productCode = productCode;
		this.productName = productName;
		this.productPrice = productPrice;
		this.totalGST = totalGST;
		this.totalCost = totalCost;
		this.customerID = customerID;
		this.staffID = staffID;
	}

	public long getPurchaseID() {
		return purchaseID;
	}

	public void setPurchaseID(long purchaseID) {
		this.purchaseID = purchaseID;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public double getTotalGST() {
		return totalGST;
	}

	public void setTotalGST(double totalGST) {
		this.totalGST = totalGST;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}

	public long getStaffID() {
		return staffID;
	}

	public void setStaffID(long staffID) {
		this.staffID = staffID;
	}

	@Override
	public String toString() {
		return "SuperProduct [purchaseID=" + purchaseID + ", purchaseDate=" + purchaseDate + ", productCode=" + productCode + ", productName="
				+ productName + ", productPrice=" + productPrice + ", totalGST=" + totalGST + ", totalCost=" + totalCost + ", customerID="
				+ customerID + ", staffID=" + staffID + "]";
	}
}