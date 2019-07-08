/**
 * Project: bodySoleWellnessCenter
 * Date: Jan 1, 2019
 * Time: 5:52:34 PM
 */

package com.caseytoews.bodysoleapp.domain.product;

import java.util.ArrayList;

/**
 * Data schema to store all products information in products json file
 */
public class ProductDetails {
	private double GST;
	private ArrayList<Double> quantities;
	ArrayList<BodyServiceProduct> serviceProducts;
	ArrayList<BodyPackageProduct> bodyPackageProducts;

	/**
	 * Default Constructor for Jackson instantiation
	 */
	public ProductDetails() {
		super();
	}

	public ProductDetails(double GST, ArrayList<Double> quantities, ArrayList<BodyServiceProduct> serviceProducts,
			ArrayList<BodyPackageProduct> packagedProducts) {
		super();
		this.GST = GST;
		this.quantities = quantities;
		this.serviceProducts = serviceProducts;
		this.bodyPackageProducts = packagedProducts;
	}

	public double getGST() {
		return GST;
	}

	public void setGST(double GST) {
		this.GST = GST;
	}

	public ArrayList<Double> getQuantities() {
		return quantities;
	}

	public void setQuantities(ArrayList<Double> quantities) {
		this.quantities = quantities;
	}

	public ArrayList<BodyServiceProduct> getServiceProducts() {
		return serviceProducts;
	}

	public void setServiceProducts(ArrayList<BodyServiceProduct> serviceProducts) {
		this.serviceProducts = serviceProducts;
	}

	public ArrayList<BodyPackageProduct> getPackagedProducts() {
		return bodyPackageProducts;
	}

	public void setPackagedProducts(ArrayList<BodyPackageProduct> packagedProducts) {
		this.bodyPackageProducts = packagedProducts;
	}

	@Override
	public String toString() {
		return "Products [GST=" + GST + ", quantities=" + quantities + ", serviceProducts=" + serviceProducts + ", packagedProducts="
				+ bodyPackageProducts + "]";
	}
}