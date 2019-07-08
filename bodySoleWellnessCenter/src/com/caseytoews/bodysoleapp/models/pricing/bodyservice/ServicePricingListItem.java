/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.models.pricing.bodyservice;

import com.caseytoews.bodysoleapp.domain.product.BodyServiceProduct;

public class ServicePricingListItem {

	private BodyServiceProduct product;

	public ServicePricingListItem(BodyServiceProduct product) {
		this.product = product;
	}

	public BodyServiceProduct getProduct() {
		return product;
	}

	public void setStaff(BodyServiceProduct packaged) {
		this.product = packaged;
	}

	@Override
	public String toString() {
		if (product == null) {
			return null;
		}

		return String.format("Product Code: %s \"%s\" [Per service: $%.2f]", product.getProductCode(), product.getProductName(),
				product.getProductPrice());
	}
}
