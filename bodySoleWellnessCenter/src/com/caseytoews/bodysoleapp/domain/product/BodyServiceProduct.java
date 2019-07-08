/**
 * Project: bodySoleWellnessCenter
 * Date: Jan 1, 2019
 * Time: 5:34:15 PM
 */

package com.caseytoews.bodysoleapp.domain.product;

/**
 * A body service product available for purchase
 */
public class BodyServiceProduct extends SuperProduct {

	/**
	 * Default Constructor for Jackson instantiation
	 */
	public BodyServiceProduct() {
		super();
	}

	public BodyServiceProduct(String productCode, String productName, double productPrice) {
		super(productCode, productName, productPrice);
	}
}
