/**
 * Project: bodySoleWellnessCenter
 * Date: Jan 8, 2019
 * Time: 9:03:54 AM
 */

package com.caseytoews.bodysoleapp.utility.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyCalculator {
	private BigDecimal amountOne;
	private BigDecimal amountTwo;
	private NumberFormat currencyFormat;
	private static DecimalFormat DF = new DecimalFormat("#.00");

	/**
	 * Instantiate one amount
	 */
	public CurrencyCalculator(String amountOne) {
		this.amountOne = roundCurrency(new BigDecimal(amountOne));
	}

	/**
	 * Instantiate two amounts
	 */
	public CurrencyCalculator(String amountOne, String amountTwo) {
		this.amountOne = roundCurrency(new BigDecimal(amountOne));
		this.amountTwo = roundCurrency(new BigDecimal(amountTwo));
	}

	public void setCurrencyFormat() {
		currencyFormat = NumberFormat.getCurrencyInstance(Locale.CANADA);
		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
	}

	public BigDecimal getAmountOne() {
		return amountOne;
	}

	public void setAmountOne(BigDecimal amountOne) {
		this.amountOne = amountOne;
	}

	public BigDecimal getAmountTwo() {
		return amountTwo;
	}

	public void setAmountTwo(BigDecimal amountTwo) {
		this.amountTwo = amountTwo;
	}

	public String getSum() {
		return bigDecimalToString(amountOne.add(amountTwo));
	}

	public String getDifference() {
		return bigDecimalToString(amountTwo.subtract(amountOne));
	}

	public String getProduct() {
		return bigDecimalToString(amountOne.multiply(amountTwo));
	}

	public BigDecimal getProductBD() {
		return amountOne.multiply(amountTwo);
	}

	public BigDecimal roundCurrency(BigDecimal number) {
		return number.setScale(2, RoundingMode.HALF_UP);

	}

	public String bigDecimalToString(BigDecimal number) {
		return String.valueOf(DF.format(number));
	}

	public String bigDecimalToStringCurrencyFormat(BigDecimal number) {
		return String.valueOf(currencyFormat.format(number.doubleValue()));
	}
}