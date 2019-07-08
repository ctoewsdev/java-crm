/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.models.customer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.caseytoews.bodysoleapp.domain.people.Customer;

@SuppressWarnings("serial")
public class CustomerListModel extends AbstractListModel<CustomerListItem> {

	public static final DateFormat birthDateFormat = new SimpleDateFormat("EEE yyyy MMM dd");

	private List<CustomerListItem> customerItems;

	public CustomerListModel() {
		customerItems = new ArrayList<>();
	}

	public void setCustomers(List<Customer> customers) {
		for (Customer customer : customers) {
			customerItems.add(new CustomerListItem(customer));
		}
	}

	@Override
	public int getSize() {
		return customerItems == null ? 0 : customerItems.size();
	}

	@Override
	public CustomerListItem getElementAt(int index) {
		return customerItems.get(index);
	}

	public void add(Customer customer) {
		add(-1, customer);
	}

	public void add(int index, Customer customer) {
		CustomerListItem item = new CustomerListItem(customer);
		if (index == -1) {
			customerItems.add(item);
			index = customerItems.size() - 1;
		} else {
			customerItems.add(index, item);
		}

		fireContentsChanged(this, index, index);
	}

	public void update(int index, CustomerListItem item) {
		customerItems.set(index, item);

		fireContentsChanged(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this list.
	 */
	public boolean remove(CustomerListItem item) {
		int index = customerItems.indexOf(item);
		boolean removed = customerItems.remove(item);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return removed;
	}
}
