/**
 * Project: bodySoleWellnessCenter
 * Date: Jan 1, 2019
 * Time: 5:34:15 PM
 */

package com.caseytoews.bodysoleapp.domain.product;

/**
 * A body package product available for purchase
 */
public class BodyPackageProduct extends SuperProduct {
	double pricePerPackageService;

	/**
	 * Default Constructor for Jackson instantiation
	 */
	public BodyPackageProduct() {
		super();
	}

	public BodyPackageProduct(String productCode, String productName, double productPrice, double pricePerPackageService) {
		super(productCode, productName, productPrice);
		this.pricePerPackageService = pricePerPackageService;
	}

	public double getPricePerPackageService() {
		return pricePerPackageService;
	}

	public void setPricePerPackageService(double pricePerPackageService) {
		this.pricePerPackageService = pricePerPackageService;
	}

	@Override
	public String toString() {
		return "BodyPackageProduct [pricePerPackageService=" + pricePerPackageService + "]";
	}
}