package com.caseytoews.bodysoleapp.models.bodypackage;

import com.caseytoews.bodysoleapp.domain.sales.BodyPackage;

public class BodyPackageListItem {

	private BodyPackage packaged;

	public BodyPackageListItem(BodyPackage packaged) {
		this.packaged = packaged;
	}

	public BodyPackage getPackaged() {
		return packaged;
	}

	public void setPackaged(BodyPackage packaged) {
		this.packaged = packaged;
	}

	@Override
	public String toString() {
		if (packaged == null) {
			return null;
		}
		return String.format("%d %s", packaged.getPurchaseID(), packaged.getProductName());
	}
}