/**
 * Project: BodySolePOS
 * Date: Nov 12, 2018
 * Time: 9:43:21 AM
 */

package com.caseytoews.bodysoleapp.utility.exception;

public class ApplicationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ApplicationException(Exception e) {
		super(e);
	}

	public ApplicationException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public ApplicationException(String data, Throwable e) {
		super(data, e);
	}
}