/**
 * Project: bodySoleWellnessCenter
 */

package com.caseytoews.bodysoleapp.models.bodypackage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;

@SuppressWarnings("serial")
public class BodyPackageListModel extends AbstractListModel<BodyPackageListItem> {

	private List<BodyPackageListItem> packagedItems;

	public BodyPackageListModel() {
		packagedItems = new ArrayList<>();
	}

	public void setCustomers(List<BodyPackage> packaged) {
		for (BodyPackage pack : packaged) {
			packagedItems.add(new BodyPackageListItem(pack));
		}
	}

	@Override
	public int getSize() {
		return packagedItems == null ? 0 : packagedItems.size();
	}

	@Override
	public BodyPackageListItem getElementAt(int index) {
		return packagedItems.get(index);
	}

	public void add(BodyPackage packaged) {
		add(-1, packaged);
	}

	public void add(int index, BodyPackage packaged) {
		BodyPackageListItem item = new BodyPackageListItem(packaged);
		if (index == -1) {
			packagedItems.add(item);
			index = packagedItems.size() - 1;
		} else {
			packagedItems.add(index, item);
		}

		fireContentsChanged(this, index, index);
	}

	public void update(int index, BodyPackageListItem item) {
		packagedItems.set(index, item);

		fireContentsChanged(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this list.
	 */
	public boolean remove(BodyPackageListItem item) {
		int index = packagedItems.indexOf(item);
		boolean removed = packagedItems.remove(item);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return removed;
	}

}
