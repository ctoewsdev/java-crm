/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.models.staff;

import java.time.LocalDate;

import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.people.Staff;

public class StaffListItem {

	private Staff staff;

	public StaffListItem(Staff staff) {
		this.staff = staff;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staffForSqlServer) {
		this.staff = staffForSqlServer;
	}

	@Override
	public String toString() {
		if (staff == null) {
			return null;
		}

		LocalDate startDate = LocalDate.parse(staff.getJoinDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
		startDate.format(UiCommon.DATE_FORMAT_UI);
		return String.format("Staff Id: %d \"%s, %s\" (%s) %s [%s]", staff.getID(), staff.getLastName(), staff.getFirstName(), staff.getPhone(),
				staff.getEmail(), startDate);
	}
}