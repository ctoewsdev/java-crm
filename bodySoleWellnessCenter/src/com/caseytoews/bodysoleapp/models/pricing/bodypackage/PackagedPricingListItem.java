/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.models.pricing.bodypackage;

import com.caseytoews.bodysoleapp.domain.product.BodyPackageProduct;

public class PackagedPricingListItem {

	private BodyPackageProduct product;

	public PackagedPricingListItem(BodyPackageProduct product) {
		this.product = product;
	}

	public BodyPackageProduct getProduct() {
		return product;
	}

	public void setStaff(BodyPackageProduct packaged) {
		this.product = packaged;
	}

	@Override
	public String toString() {
		if (product == null) {
			return null;
		}

		return String.format("Product Code: %s \"%s\" $%.2f [Per service: $%.2f]", product.getProductCode(), product.getProductName(),
				product.getProductPrice(), product.getPricePerPackageService());
	}
}
