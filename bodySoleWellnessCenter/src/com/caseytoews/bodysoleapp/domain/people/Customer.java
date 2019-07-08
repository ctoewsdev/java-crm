/**
 * Project: BodySolePOS
 * Date: Nov 12, 2018
 * Time: 11:11:58 AM
 */

package com.caseytoews.bodysoleapp.domain.people;

import java.util.ArrayList;

public class Customer extends SuperPerson {
	private ArrayList<Long> serviceIDs;
	private ArrayList<Long> packageIDs;
	private long currentPackageID;

	/**
	 * Default Constructor
	 * Required for Jackson instantiation
	 */
	public Customer() {
		super();
	}

	public Customer(long ID, String phone, String firstName, String lastName, String email, String joinDate, double totalSales,
			ArrayList<Long> serviceIDs, ArrayList<Long> packageIDs, long currentPackageID) {
		super(ID, phone, firstName, lastName, email, joinDate, totalSales);
		setServiceIDs(serviceIDs);
		setPackageIDs(packageIDs);
	}

	/**
	 * Overloaded Constructorwhen adding new customer in initial tester
	 */
	public Customer(String phone, String firstName, String lastName, String email) {
		super(0, phone, firstName, lastName, email, null, 0);
		setServiceIDs(new ArrayList<Long>());
		setPackageIDs(new ArrayList<Long>());
	}

	public ArrayList<Long> getServiceIDs() {
		return serviceIDs;
	}

	public void setServiceIDs(ArrayList<Long> serviceIDs) {
		this.serviceIDs = serviceIDs;
	}

	public ArrayList<Long> getPackageIDs() {
		return packageIDs;
	}

	public void setPackageIDs(ArrayList<Long> packageIDs) {
		this.packageIDs = packageIDs;
	}

	public long getCurrentPackageID() {
		return currentPackageID;
	}

	public void setCurrentPackageID(long currentPackageID) {
		this.currentPackageID = currentPackageID;
	}

	@Override
	public String toString() {
		return "Customer [serviceIDs=" + serviceIDs + ", packageIDs=" + packageIDs + ", currentPackageID=" + currentPackageID + "]";
	}
}