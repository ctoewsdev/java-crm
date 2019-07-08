package com.caseytoews.bodysoleapp.utility.comparator;

import java.util.Comparator;

import com.caseytoews.bodysoleapp.domain.business.Report;

public class CompareReportByPurchaseDateDesc implements Comparator<Report> {
	@Override
	public int compare(Report product1, Report product2) {
		return product2.getMonthYearLD().compareTo(product1.getMonthYearLD());
	}
}