/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.models.staff;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.caseytoews.bodysoleapp.domain.people.Staff;

@SuppressWarnings("serial")
public class StaffListModel extends AbstractListModel<StaffListItem> {

	private List<StaffListItem> staffItems;

	public StaffListModel() {
		staffItems = new ArrayList<>();
	}

	public void setStaff(List<Staff> staffMembers) {
		for (Staff staff : staffMembers) {
			staffItems.add(new StaffListItem(staff));
		}
	}

	@Override
	public int getSize() {
		return staffItems == null ? 0 : staffItems.size();
	}

	@Override
	public StaffListItem getElementAt(int index) {
		return staffItems.get(index);
	}

	public void add(Staff staff) {
		add(-1, staff);
	}

	public void add(int index, Staff staff) {
		StaffListItem item = new StaffListItem(staff);
		if (index == -1) {
			staffItems.add(item);
			index = staffItems.size() - 1;
		} else {
			staffItems.add(index, item);
		}

		fireContentsChanged(this, index, index);
	}

	public void update(int index, StaffListItem item) {
		staffItems.set(index, item);

		fireContentsChanged(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this list.
	 */
	public boolean remove(StaffListItem item) {
		int index = staffItems.indexOf(item);
		boolean removed = staffItems.remove(item);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return removed;
	}
}