/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 18, 2018
 * Time: 3:52:13 PM
 */

package com.caseytoews.bodysoleapp.domain.people;

public class SuperPerson {
	private String phone;
	private long ID;
	private String firstName;
	private String lastName;
	private String email;
	private String joinDate;
	private double totalSales;

	/**
	 * Deafault Constructor
	 * Required for Jackson instantiation
	 */
	public SuperPerson() {
		super();
	}

	public SuperPerson(long ID, String phone, String firstName, String lastName, String email, String joinDate, double totalSales) {
		super();
		setPhone(phone);
		setID(ID);
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setJoinDate(joinDate);
		setTotalSales(totalSales);
	}

	public long getID() {
		return ID;
	}

	public void setID(long ID) {
		this.ID = ID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public double getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}

	@Override
	public String toString() {
		return "SuperPerson [phone=" + phone + ", ID=" + ID + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", joinDate=" + joinDate + ", totalSales=" + totalSales + "]";
	}
}