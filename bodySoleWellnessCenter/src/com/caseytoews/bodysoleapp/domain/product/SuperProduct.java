/**
 * Project: bodySoleWellnessCenter
 * Date: Jan 1, 2019
 * Time: 5:34:15 PM
 */

package com.caseytoews.bodysoleapp.domain.product;

public class SuperProduct {
	private String productCode;
	private String productName;
	private double productPrice;

	/**
	 * Default Constructor for Jackson instantiation
	 */
	public SuperProduct() {
		super();
	}

	public SuperProduct(String productCode, String productName, double productPrice) {
		super();
		this.productCode = productCode;
		this.productName = productName;
		this.productPrice = productPrice;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	@Override
	public String toString() {
		return "ServiceProduct [productID=" + productCode + ", productName=" + productName + ", productPrice=" + productPrice + "]";
	}
}