/**
 * Project: BodySoleWellness
 * Date: Nov 12, 2018
 * Time: 2:11:46 PM
 */
package com.caseytoews.bodysoleapp.database.people;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.Dao;
import com.caseytoews.bodysoleapp.domain.people.Staff;
import com.caseytoews.bodysoleapp.io.FilesReader;
import com.caseytoews.bodysoleapp.io.IOConstants;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CRUD for DB staff-JSON file
 */
public class StaffDao extends Dao {

	private static final Logger LOG = LogManager.getLogger();

	private static StaffDao staffDaoInstance = new StaffDao();
	private FilesReader filesReader;
	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * private constructor for singleton instance
	 */
	private StaffDao() {
		super("staff");
		filesReader = FilesReader.getTheInstance();
	}

	public static StaffDao getTheInstance() {
		return staffDaoInstance;
	}

	public List<Staff> getAllStaff() throws ApplicationException {
		return filesReader.readStaff();
	}

	public void addStaff(Staff staff) throws ApplicationException {

		List<Staff> staffMembers;
		try {
			staffMembers = filesReader.readStaff();
			long staffID = staffDaoInstance.getNextID("staffID");
			staff.setID(staffID);

			// Capitalize first letter of first name and last name
			String fn = staff.getFirstName();
			String firstName = fn.substring(0, 1).toUpperCase() + fn.substring(1);
			staff.setFirstName(firstName);
			String ln = staff.getLastName();
			String lastName = ln.substring(0, 1).toUpperCase() + ln.substring(1);
			staff.setLastName(lastName);

			staffMembers.add(staff);

			updateStaffFile(staffMembers);

		} catch (ApplicationException e) {
			LOG.error("ERROR: StaffDao() addStaff()");
			throw new ApplicationException(e);
		}
	}

	public Staff getStaffByID(long staffID) throws ApplicationException {
		Staff staff = null;
		List<Staff> allStaff;

		if (staffID != 0) {
			try {
				allStaff = filesReader.readStaff();
				for (Staff s : allStaff) {

					if (staffID == s.getID()) {
						staff = s;
					}
				}
			} catch (ApplicationException e) {
				LOG.error("ERROR: StaffDao() getStaffByID()");
				throw new ApplicationException(e);
			}

		} else {
			LOG.debug("Staff ID in StaffDao.getStaffByID() is null");
		}

		return staff;
	}

	public void updateStaffMember(Staff staff) throws ApplicationException {
		List<Staff> staffMembers;
		try {
			staffMembers = filesReader.readStaff();
			Iterator<Staff> iter = staffMembers.iterator();
			while (iter.hasNext()) {
				if (iter.next().getID() == staff.getID()) {
					iter.remove();
					break;
				}
			}

			// Capitalize first letter of first name and last name
			String fn = staff.getFirstName();
			String firstName = fn.substring(0, 1).toUpperCase() + fn.substring(1);
			staff.setFirstName(firstName);
			String ln = staff.getLastName();
			String lastName = ln.substring(0, 1).toUpperCase() + ln.substring(1);
			staff.setLastName(lastName);

			staffMembers.add(staff);
			updateStaffFile(staffMembers);

		} catch (ApplicationException e) {
			LOG.error("StaffDao/updateStaffMebers() ERROR");
			throw new ApplicationException(e);
		}
	}

	public void updateStaffFile(List<Staff> staffMembers) throws ApplicationException {
		try {
			staffDaoInstance.updateFile(IOConstants.STAFF_FILE, staffMembers);

		} catch (Exception e) {
			LOG.error("ERROR: StaffDao() updateStaffFile()");
			throw new ApplicationException(e);
		}
	}

	public void delete(Staff staff) throws ApplicationException {

	}
}
