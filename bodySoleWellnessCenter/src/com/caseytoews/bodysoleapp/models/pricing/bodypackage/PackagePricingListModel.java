/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.models.pricing.bodypackage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.caseytoews.bodysoleapp.domain.product.BodyPackageProduct;

@SuppressWarnings("serial")
public class PackagePricingListModel extends AbstractListModel<PackagedPricingListItem> {

	private List<PackagedPricingListItem> productItems;

	public PackagePricingListModel() {
		productItems = new ArrayList<>();
	}

	public void setPackaged(List<BodyPackageProduct> products) {
		for (BodyPackageProduct product : products) {
			productItems.add(new PackagedPricingListItem(product));
		}
	}

	@Override
	public int getSize() {
		return productItems == null ? 0 : productItems.size();
	}

	@Override
	public PackagedPricingListItem getElementAt(int index) {
		return productItems.get(index);
	}

	public void add(BodyPackageProduct product) {
		add(-1, product);
	}

	public void add(int index, BodyPackageProduct product) {
		PackagedPricingListItem item = new PackagedPricingListItem(product);
		if (index == -1) {
			productItems.add(item);
			index = productItems.size() - 1;
		} else {
			productItems.add(index, item);
		}

		fireContentsChanged(this, index, index);
	}

	public void update(int index, PackagedPricingListItem item) {
		productItems.set(index, item);

		fireContentsChanged(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this list.
	 */
	public boolean remove(PackagedPricingListItem item) {
		int index = productItems.indexOf(item);
		boolean removed = productItems.remove(item);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return removed;
	}
}
