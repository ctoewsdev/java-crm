/**
 * Project: BodySoleWellness
 * Date: Nov 12, 2018
 * Time: 2:11:46 PM
 */
package com.caseytoews.bodysoleapp.database.people;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.database.Dao;
import com.caseytoews.bodysoleapp.database.sales.BodyPackageDao;
import com.caseytoews.bodysoleapp.domain.people.Group;
import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;
import com.caseytoews.bodysoleapp.io.FilesReader;
import com.caseytoews.bodysoleapp.io.IOConstants;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CRUD for DB group-JSON file
 */
public class GroupDao extends Dao {

	private static final Logger LOG = LogManager.getLogger();
	private static BodyPackageDao packagedDao;

	private static GroupDao groupDaoInstance = new GroupDao();

	private FilesReader filesReader;
	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * private constructor for singleton instance
	 */
	private GroupDao() {
		super("groups");
		filesReader = FilesReader.getTheInstance();
		packagedDao = BodyPackageDao.getTheInstance();
	}

	public static GroupDao getTheInstance() {
		return groupDaoInstance;
	}

	public List<Group> getAllGroups() throws ApplicationException {
		return filesReader.readGroups();
	}

	public void addGroup(long mainCustomerID) throws ApplicationException {
		List<Group> groups;
		try {
			groups = getAllGroups();
			long groupID = groupDaoInstance.getNextID("groupID");
			Group group = new Group();
			group.setGroupID(groupID);
			ArrayList<Long> members = new ArrayList<>();
			members.add(mainCustomerID);
			group.setMembers(members);
			groups.add(group);
			updateGroupsFile(groups);

		} catch (ApplicationException e) {
			LOG.error("ERROR: GroupDao() addCustomer()");
			throw new ApplicationException(e);
		}
	}

	public void updateGroups(Group group) throws ApplicationException {
		List<Group> groups;
		try {
			groups = getAllGroups();
			Iterator<Group> iter = groups.iterator();
			while (iter.hasNext()) {
				if (iter.next().getGroupID() == group.getGroupID()) {
					iter.remove();
					break;
				}
			}
			groups.add(group);
			updateGroupsFile(groups);

		} catch (ApplicationException e) {
			LOG.error("GroupDao/updateGroups() ERROR");
			throw new ApplicationException(e);
		}
	}

	public Group getGroupByGroupId(long groupID) throws ApplicationException {
		Group group = null;
		List<Group> groups;
		try {
			groups = getAllGroups();
			for (Group g : groups) {
				if (g.getGroupID() == groupID) {
					group = g;
					break;
				}
			}
		} catch (ApplicationException e) {
			LOG.error("GroupDao/getGroupByGroupId() ERROR");
			throw new ApplicationException(e);
		}
		return group;
	}

	public Group getGroupByCustomerId(long customerID) throws ApplicationException {
		Group group = null;
		List<Group> groups;
		try {
			groups = filesReader.readGroups();
			if (groups == null) {
				return group;
			}
			for (Group g : groups) {
				for (long c : g.getMembers()) {
					if (c == customerID) {
						group = g;
						break;
					}
				}
			}
		} catch (ApplicationException e) {
			LOG.error("GroupDao/getGroupByCustomerId() ERROR");
			throw new ApplicationException(e);
		}
		return group;
	}

	public double[] getGroupRemaining(Group group) throws ApplicationException {
		double remaining[] = new double[2];
		ArrayList<Long> memberIDs = group.getMembers();

		double bodyRemaining = 0.0;
		double footRemaining = 0.0;
		try {
			for (long mID : memberIDs) {
				for (BodyPackage p : packagedDao.getPackagesByCustomerID(mID)) {
					if (p.getProductCode().startsWith("A")) {
						footRemaining += packagedDao.getPackagedRemaining(p);
					} else {
						bodyRemaining += packagedDao.getPackagedRemaining(p);
					}
				}
			}
			remaining[0] = bodyRemaining;
			remaining[1] = footRemaining;
			return remaining;
		} catch (ApplicationException e) {
			LOG.error("GroupDao/getGroupRemaining() ERROR");
			throw new ApplicationException(e);
		}
	}

	public ArrayList<BodyPackage> getGroupPackagesByGroup(Group group) throws ApplicationException {
		ArrayList<BodyPackage> groupPackages = new ArrayList<>();
		ArrayList<Long> memberIDs = group.getMembers();
		try {
			for (long mID : memberIDs) {
				for (BodyPackage p : packagedDao.getPackagesByCustomerID(mID)) {
					groupPackages.add(p);
				}
			}
			return groupPackages;
		} catch (ApplicationException e) {
			LOG.error("GroupDao/getGroupPackagesByGroup() ERROR");
			throw new ApplicationException(e);
		}
	}

	public void updateGroupsFile(List<Group> groups) throws ApplicationException {
		try {
			groupDaoInstance.updateFile(IOConstants.GROUPS_FILE, groups);
		} catch (Exception e) {
			LOG.error("ERROR: GroupDao() updateGroupsFile()");
			throw new ApplicationException(e);
		}
	}

	public boolean validateGroupMember(long customerID) throws ApplicationException {

		List<Group> groups;
		try {
			groups = filesReader.readGroups();
			if (groups == null) {
				return false;
			}

			// test for invalid and change validationCode accordingly
			for (Group g : groups) {
				for (long c : g.getMembers()) {
					if (c == customerID) {
						return false;
					}
				}
			}
			return true;

		} catch (ApplicationException e) {
			LOG.error("ERROR: GroupDao() validateGroupMember()");
			throw new ApplicationException(e);
		}
	}

	public void delete(Group group) throws ApplicationException {

	}
}