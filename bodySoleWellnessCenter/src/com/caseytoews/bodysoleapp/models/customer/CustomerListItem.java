/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.models.customer;

import com.caseytoews.bodysoleapp.domain.people.Customer;

public class CustomerListItem {

	private Customer customer;

	public CustomerListItem(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customerForSqlServer) {
		this.customer = customerForSqlServer;
	}

	@Override
	public String toString() {
		if (customer == null) {
			return null;
		}

		return String.format("Customer Id: %d \u2022 %s, %s (%s) %s", customer.getID(), customer.getLastName(), customer.getFirstName(),
				customer.getPhone(), customer.getEmail());
	}
}
