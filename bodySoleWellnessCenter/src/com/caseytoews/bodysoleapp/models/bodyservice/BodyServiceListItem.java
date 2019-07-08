/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 1, 2018
 * Time: 9:43:21 AM
 */

package com.caseytoews.bodysoleapp.models.bodyservice;

import java.time.LocalDate;

import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.dialogviews.mainframe.MainFrame;
import com.caseytoews.bodysoleapp.domain.people.Customer;
import com.caseytoews.bodysoleapp.domain.sales.BodyService;

public class BodyServiceListItem {

	private BodyService service;
	private Customer customer = MainFrame.getMainFrameCustomer();

	public BodyServiceListItem(BodyService service) {
		this.service = service;
	}

	public BodyService getService() {
		return service;
	}

	public void setService(BodyService service) {
		this.service = service;
	}

	@Override
	public String toString() {
		LocalDate serviceDate = null;

		if (service == null) {
			return null;
		}

		if (service.getPurchaseDate() != null) {
			serviceDate = LocalDate.parse(service.getPurchaseDate(), UiCommon.DATETIME_FORMAT_MMddyyyy);
			serviceDate.format(UiCommon.DATE_FORMAT_UI);
		}

		return String.format("ServiceID: %d (%s) \"%s\" [$%.2f] %s", service.getPurchaseID(), serviceDate, service.getProductName(),
				service.getTotalCost(), customer.getEmail());
	}
}
