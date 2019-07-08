/**
 * Project: BodySolePOS
 * Date: Nov 12, 2018
 * Time: 11:11:58 AM
 */

package com.caseytoews.bodysoleapp.domain.people;

public class Staff extends SuperPerson {
	/**
	 * Default Constructor
	 * Required for Jackson instantiation
	 */
	public Staff() {
		super();
	}

	public Staff(long ID, String phone, String firstName, String lastName, String email, String joinDate, double totalSales) {
		super(ID, phone, firstName, lastName, email, joinDate, totalSales);
	}

	/**
	 * Overloaded Constructor when adding new staff in initial tester
	 */
	public Staff(String phone, String firstName, String lastName, String email, String joinDate) {
		super(0, phone, firstName, lastName, email, joinDate, 0);
	}
}