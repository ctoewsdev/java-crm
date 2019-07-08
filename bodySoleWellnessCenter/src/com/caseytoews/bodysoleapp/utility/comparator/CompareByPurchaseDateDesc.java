/**
 * Project: BSWCApp
 * Date: Jan 31, 2019
 * Time: 2:33:10 PM
 */

package com.caseytoews.bodysoleapp.utility.comparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.caseytoews.bodysoleapp.domain.sales.SuperSale;

public class CompareByPurchaseDateDesc implements Comparator<SuperSale> {
	@Override
	public int compare(SuperSale service1, SuperSale service2) {
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat("MMddyyyy").parse(service1.getPurchaseDate());
			date2 = new SimpleDateFormat("MMddyyyy").parse(service2.getPurchaseDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2.compareTo(date1);
	}
}