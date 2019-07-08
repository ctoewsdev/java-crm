/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */
package com.caseytoews.bodysoleapp.models.reports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.dialogviews.common.UiCommon;
import com.caseytoews.bodysoleapp.domain.business.Report;

public class ReportListItem {
	public static final Logger LOG = LogManager.getLogger();
	private Report report;

	public ReportListItem(Report report) {
		this.report = report;
	}

	public Report getReport() {
		return report;
	}

	public void getReport(Report staffForSqlServer) {
		this.report = staffForSqlServer;
	}

	@Override
	public String toString() {
		if (report == null) {
			return null;
		}
		return String.format(" %s \u2022 Events: [%d] Sales: $%.2f (GST: $%.2f)", report.getMonthYearLD().format(UiCommon.REPORT_MMyyyy_FORMAT_UI),
				report.getVisits(), report.getTotalCost(), report.getTotalGST());
	}
}