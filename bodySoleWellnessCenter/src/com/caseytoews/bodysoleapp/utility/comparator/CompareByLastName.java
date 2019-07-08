/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.utility.comparator;

import java.util.Comparator;

import com.caseytoews.bodysoleapp.domain.people.Customer;

public class CompareByLastName implements Comparator<Customer> {
	@Override
	public int compare(Customer customer1, Customer customer2) {
		return customer1.getLastName().compareTo(customer2.getLastName());
	}
}