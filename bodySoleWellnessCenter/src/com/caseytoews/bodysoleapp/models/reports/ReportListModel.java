/**
 * Project: bodySoleWellnessCenter
 * Date: Dec 20, 2018
 * Time: 4:39:17 PM
 */

package com.caseytoews.bodysoleapp.models.reports;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.caseytoews.bodysoleapp.domain.business.Report;

@SuppressWarnings("serial")
public class ReportListModel extends AbstractListModel<ReportListItem> {

	private List<ReportListItem> reportItems;

	public ReportListModel() {
		reportItems = new ArrayList<>();
	}

	public void setReport(List<Report> reportList) {
		for (Report report : reportList) {
			reportItems.add(new ReportListItem(report));
		}
	}

	@Override
	public int getSize() {
		return reportItems == null ? 0 : reportItems.size();
	}

	@Override
	public ReportListItem getElementAt(int index) {
		return reportItems.get(index);
	}

	public void add(Report report) {
		add(-1, report);
	}

	public void add(int index, Report report) {
		ReportListItem item = new ReportListItem(report);
		if (index == -1) {
			reportItems.add(item);
			index = reportItems.size() - 1;
		} else {
			reportItems.add(index, item);
		}

		fireContentsChanged(this, index, index);
	}

	public void update(int index, ReportListItem item) {
		reportItems.set(index, item);

		fireContentsChanged(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this list.
	 */
	public boolean remove(ReportListItem item) {
		int index = reportItems.indexOf(item);
		boolean removed = reportItems.remove(item);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return removed;
	}
}