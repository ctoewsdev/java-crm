/**
 * Project: Body Sole Wellness
 */

package com.caseytoews.bodysoleapp.utility.comparator;

import java.util.Comparator;

import com.caseytoews.bodysoleapp.domain.people.Staff;

public class CompareStaffByLastName implements Comparator<Staff> {
	@Override
	public int compare(Staff staff1, Staff staff2) {
		return staff1.getLastName().compareTo(staff2.getLastName());
	}
}