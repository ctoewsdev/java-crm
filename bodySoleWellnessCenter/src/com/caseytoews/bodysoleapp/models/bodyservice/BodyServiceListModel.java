/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 1, 2018
 * Time: 9:43:21 AM
 */
package com.caseytoews.bodysoleapp.models.bodyservice;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.caseytoews.bodysoleapp.domain.sales.BodyService;

public class BodyServiceListModel extends AbstractListModel<BodyServiceListItem> {

	private static final long serialVersionUID = 1L;

	private List<BodyServiceListItem> serviceItems;

	public BodyServiceListModel() {
		serviceItems = new ArrayList<>();
	}

	public void setServices(List<BodyService> services) {
		for (BodyService service : services) {
			serviceItems.add(new BodyServiceListItem(service));
		}
	}

	@Override
	public int getSize() {
		return serviceItems == null ? 0 : serviceItems.size();
	}

	@Override
	public BodyServiceListItem getElementAt(int index) {
		return serviceItems.get(index);
	}

	public void add(BodyService service) {
		add(-1, service);
	}

	public void add(int index, BodyService service) {
		BodyServiceListItem item = new BodyServiceListItem(service);
		if (index == -1) {
			serviceItems.add(item);
			index = serviceItems.size() - 1;
		} else {
			serviceItems.add(index, item);
		}

		fireContentsChanged(this, index, index);
	}

	public void update(int index, BodyServiceListItem item) {
		serviceItems.set(index, item);

		fireContentsChanged(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this list.
	 */
	public boolean remove(BodyServiceListItem item) {
		int index = serviceItems.indexOf(item);
		boolean removed = serviceItems.remove(item);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return removed;
	}

}
