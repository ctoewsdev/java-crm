/**
 * Project: BodySolePOS
 * Date: Nov 12, 2018
 * Time: 11:11:58 AM
 */

package com.caseytoews.bodysoleapp.domain.people;

import java.util.ArrayList;

public class Group {
	private long groupID;
	private ArrayList<Long> members;

	/**
	 * Default Constructor
	 * Required for Jackson instantiation
	 */
	public Group() {
		super();
	}

	public Group(long groupID, ArrayList<Long> members) {
		super();
		this.groupID = groupID;
		this.members = members;
	}

	public long getGroupID() {
		return groupID;
	}

	public void setGroupID(long groupID) {
		this.groupID = groupID;
	}

	public ArrayList<Long> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<Long> members) {
		this.members = members;
	}

	@Override
	public String toString() {
		return "Group [groupID=" + groupID + ", members=" + members + "]";
	}
}