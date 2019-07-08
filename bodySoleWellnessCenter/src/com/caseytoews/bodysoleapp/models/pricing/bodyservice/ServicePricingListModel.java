/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */
package com.caseytoews.bodysoleapp.models.pricing.bodyservice;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.caseytoews.bodysoleapp.domain.product.BodyServiceProduct;

@SuppressWarnings("serial")
public class ServicePricingListModel extends AbstractListModel<ServicePricingListItem> {

	private List<ServicePricingListItem> productItems;

	public ServicePricingListModel() {
		productItems = new ArrayList<>();
	}

	public void setPackaged(List<BodyServiceProduct> products) {
		for (BodyServiceProduct product : products) {
			productItems.add(new ServicePricingListItem(product));
		}
	}

	@Override
	public int getSize() {
		return productItems == null ? 0 : productItems.size();
	}

	@Override
	public ServicePricingListItem getElementAt(int index) {
		return productItems.get(index);
	}

	public void add(BodyServiceProduct product) {
		add(-1, product);
	}

	public void add(int index, BodyServiceProduct product) {
		ServicePricingListItem item = new ServicePricingListItem(product);
		if (index == -1) {
			productItems.add(item);
			index = productItems.size() - 1;
		} else {
			productItems.add(index, item);
		}

		fireContentsChanged(this, index, index);
	}

	public void update(int index, ServicePricingListItem item) {
		productItems.set(index, item);

		fireContentsChanged(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this list.
	 */
	public boolean remove(ServicePricingListItem item) {
		int index = productItems.indexOf(item);
		boolean removed = productItems.remove(item);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return removed;
	}
}
