/**
 * Project: BodySoleWellness
 * Date: Nov 12, 2018
 * Time: 2:11:46 PM
 */

package com.caseytoews.bodysoleapp.domain.business;

import java.time.LocalDate;

/**
 * For building manager reports
 */
public class Report {

	private LocalDate monthYearLD;
	private int visits;
	private double totalGST;
	private double totalCost;

	public Report() {
		super();
	}

	public Report(LocalDate monthYearLD, double totalGST, double totalCost, int visits) {
		this.monthYearLD = monthYearLD;
		this.totalGST = totalGST;
		this.totalCost = totalCost;
		this.visits = visits;
	}

	public LocalDate getMonthYearLD() {
		return monthYearLD;
	}

	public void setMonthYearLD(LocalDate monthYearLD) {
		this.monthYearLD = monthYearLD;
	}

	public int getVisits() {
		return visits;
	}

	public void setVisits(int frequency) {
		this.visits = frequency;
	}

	public double getTotalGST() {
		return totalGST;
	}

	public void setTotalGST(double totalGST) {
		this.totalGST = totalGST;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	@Override
	public String toString() {
		return "Report [monthYearLD=" + monthYearLD + ", frequency=" + visits + ", totalGST=" + totalGST + ", totalCost=" + totalCost + "]";
	}
}