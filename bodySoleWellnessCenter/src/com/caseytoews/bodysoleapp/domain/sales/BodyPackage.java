/**
 * Project: BodySolePOS
 * Date: Nov 12, 2018
 * Time: 11:11:58 AM
 */

package com.caseytoews.bodysoleapp.domain.sales;

import java.util.ArrayList;

public class BodyPackage extends SuperSale {
	ArrayList<Long> serviceIDs;

	/**
	 * Default Constructor for Jackson instantiation
	 */
	public BodyPackage() {
		super();
	}

	public BodyPackage(long purchaseID, String purchaseDate, String productCode, String productName, double productPrice, double totalGST,
			double totalCost, long customerID, long staffID, ArrayList<Long> serviceIDs, double balance) {
		super(purchaseID, purchaseDate, productCode, productName, productPrice, totalGST, totalCost, customerID, staffID);
		this.serviceIDs = serviceIDs;
	}

	/**
	 * Overloaded Constructor for instantiating inside sellPackage Dialog
	 */
	public BodyPackage(String purchaseDate, String productCode, String productName, double productPrice, double totalGST, double totalCost,
			long customerID, long staffID, ArrayList<Long> serviceIDs, double balance) {
		super(0, purchaseDate, productCode, productName, productPrice, totalGST, totalCost, customerID, staffID);
		this.serviceIDs = serviceIDs;
	}

	public ArrayList<Long> getServices() {
		return serviceIDs;
	}

	public void setServices(ArrayList<Long> serviceIDs) {
		this.serviceIDs = serviceIDs;
	}

	@Override
	public String toString() {
		return "Packaged [serviceIDs=" + serviceIDs + "]";
	}
}